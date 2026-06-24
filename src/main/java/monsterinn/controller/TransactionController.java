package monsterinn.controller;

import monsterinn.model.Monster;
import monsterinn.repository.MonsterRepository;
import monsterinn.model.Room;
import monsterinn.model.RoomStatus;
import monsterinn.repository.RoomRepository;
import monsterinn.model.Transaction;
import monsterinn.repository.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TransactionController {

    private final MonsterRepository monsterRepo;
    private final RoomRepository roomRepo;
    private final TransactionRepository transactionRepo;

    public TransactionController(MonsterRepository monsterRepo, RoomRepository roomRepo, TransactionRepository transactionRepo) {
        this.monsterRepo = monsterRepo;
        this.roomRepo = roomRepo;
        this.transactionRepo = transactionRepo;
    }

    // 1. Tampilkan Halaman Checkout
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model) {
        model.addAttribute("activeGuests", monsterRepo.findAll());
        model.addAttribute("activePage", "checkout");
        return "view/checkout";
    }

    // 2. API untuk Ambil Info Tagihan (Dipakai JS Frontend)
    @GetMapping("/api/checkout-info/{id}")
    @ResponseBody
    public ResponseEntity<?> getCheckoutInfo(@PathVariable String id) {
        Monster monster = monsterRepo.findById(id).orElse(null);
        if (monster == null || monster.getRoomId() == null) {
            return ResponseEntity.badRequest().body("Tamu tidak ditemukan.");
        }

        Room room = roomRepo.findById(monster.getRoomId()).orElse(null);
        if (room == null) return ResponseEntity.badRequest().body("Kamar tidak ditemukan.");

        // FIX CHECKOUT PREPAID REFUND: API mengirim total biaya, sisa bayar, dan refund sebagai nilai terpisah.
        double roomTotal = Math.max(monster.calculateTotalCost() - monster.getExtraCost(), 0);
        double serviceTotal = monster.getExtraCost();
        double prepaid = monster.getPrepaidAmount();
        double grossTotal = roomTotal + serviceTotal;
        double remainingDue = Math.max(grossTotal - prepaid, 0);
        double refundAmount = Math.max(prepaid - grossTotal, 0);
        double dailyRoomRate = monster.getStayDays() > 0 ? roomTotal / monster.getStayDays() : 0;

        Map<String, Object> data = new HashMap<>();
        data.put("monsterName", monster.getName());
        data.put("roomId", monster.getRoomId());
        data.put("stayDays", monster.getStayDays());
        data.put("roomRate", dailyRoomRate);
        data.put("roomTotal", roomTotal);
        data.put("extraCost", serviceTotal);
        data.put("serviceTotal", serviceTotal);
        data.put("prepaid", prepaid);
        data.put("grossTotal", grossTotal);
        data.put("totalCost", grossTotal);
        data.put("remainingDue", remainingDue);
        data.put("refundAmount", refundAmount);
        data.put("grandTotal", remainingDue);
        data.put("serviceLog", monster.getServiceLog());

        return ResponseEntity.ok(data);
    }

    // 3. API untuk Konfirmasi Checkout (Sistem Keamanan Ganda Pencegah Status Belum Lunas)
    @PostMapping("/api/checkout/confirm/{id}")
    @ResponseBody
    public ResponseEntity<?> confirmCheckout(@PathVariable String id, @RequestBody Map<String, Object> payload) {
        Monster monster = monsterRepo.findById(id).orElse(null);
        if (monster == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Data tamu tidak ada.");
            return ResponseEntity.badRequest().body(error);
        }

        Room room = roomRepo.findById(monster.getRoomId()).orElse(null);
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Kamar tidak ditemukan.");
            return ResponseEntity.badRequest().body(error);
        }

        double inputPayment;
        try {
            inputPayment = payload.get("paymentAmount") != null
                    ? Double.parseDouble(payload.get("paymentAmount").toString())
                    : 0;
        } catch (NumberFormatException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Nominal pembayaran tidak valid.");
            return ResponseEntity.badRequest().body(error);
        }

        if (Double.isNaN(inputPayment) || Double.isInfinite(inputPayment) || inputPayment < 0) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Nominal pembayaran tidak boleh negatif.");
            return ResponseEntity.badRequest().body(error);
        }

        // FIX CHECKOUT PREPAID REFUND: backend validasi pembayaran terhadap sisa bayar, bukan total negatif.
        double roomTotal = Math.max(monster.calculateTotalCost() - monster.getExtraCost(), 0);
        double grossTotal = roomTotal + monster.getExtraCost();
        double remainingDue = Math.max(grossTotal - monster.getPrepaidAmount(), 0);

        if (remainingDue > 0 && inputPayment < remainingDue) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Gagal checkout: Uang pembayaran kurang dari sisa bayar.");
            return ResponseEntity.badRequest().body(error);
        }

        // Instansiasi objek transaksi baru memanfaatkan constructor utama model Transaction.
        Transaction riwayatBaru = new Transaction(monster, room, inputPayment);
        if (!riwayatBaru.processPayment()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Gagal checkout: Transaksi belum lunas.");
            return ResponseEntity.badRequest().body(error);
        }

        // Simpan nota permanen ke database MySQL laporan
        transactionRepo.save(riwayatBaru);

        // Update Kamar: Kosongkan kamar dan alihkan status operasional menjadi DIRTY (Kotor)
        room.setOccupied(false);
        room.setCurrentGuest(null);
        room.setStatus(RoomStatus.DIRTY);
        roomRepo.save(room);

        // Hapus data monster dari tabel aktif hunian (Monsters)
        monsterRepo.delete(monster);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Berhasil Checkout!");
        return ResponseEntity.ok(response);
    }
}