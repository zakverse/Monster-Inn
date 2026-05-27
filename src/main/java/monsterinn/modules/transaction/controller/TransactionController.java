package monsterinn.modules.transaction.controller;

import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.model.RoomStatus;
import monsterinn.modules.room.repository.RoomRepository;
import monsterinn.modules.transaction.model.Transaction;
import monsterinn.modules.report.repository.TransactionRepository;
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
        return "modules/transaction/checkout"; 
    }

    // 2. API untuk Ambil Info Tagihan (Dipakai JS)
    @GetMapping("/api/checkout-info/{id}")
    @ResponseBody
    public ResponseEntity<?> getCheckoutInfo(@PathVariable String id) {
        Monster monster = monsterRepo.findById(id).orElse(null);
        if (monster == null || monster.getRoomId() == null) {
            return ResponseEntity.badRequest().body("Tamu tidak ditemukan.");
        }

        Room room = roomRepo.findById(monster.getRoomId()).orElse(null);
        if (room == null) return ResponseEntity.badRequest().body("Kamar tidak ditemukan.");

        Map<String, Object> data = new HashMap<>();
        data.put("monsterName", monster.getName());
        data.put("roomId", monster.getRoomId());
        data.put("stayDays", monster.getStayDays());
        data.put("roomRate", room.getRoomRate());
        data.put("roomTotal", room.getRoomRate() * monster.getStayDays());
        data.put("extraCost", monster.getExtraCost());
        data.put("prepaid", monster.getPrepaidAmount());
        data.put("serviceLog", monster.getServiceLog());
        
        // Rumus Induk Tagihan: (Sewa Kamar * Hari) + Biaya Layanan - Uang Muka
        data.put("grandTotal", (room.getRoomRate() * monster.getStayDays()) + monster.getExtraCost() - monster.getPrepaidAmount());

        return ResponseEntity.ok(data);
    }

    // 3. API untuk Konfirmasi Checkout (Sistem Validasi Lunas Ketat Keamanan Ganda)
    @PostMapping("/api/checkout/confirm/{id}")
    @ResponseBody
    public ResponseEntity<?> confirmCheckout(@PathVariable String id, @RequestBody Map<String, Object> payload) {
        Monster monster = monsterRepo.findById(id).orElse(null);
        if (monster == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Data naga tidak ada.");
            return ResponseEntity.badRequest().body(error);
        }

        Room room = roomRepo.findById(monster.getRoomId()).orElse(null);
        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Kamar tidak ditemukan.");
            return ResponseEntity.badRequest().body(error);
        }

        // Ambil nominal bayar dari request payload JS frontend (default ke 0 jika kosong)
        double inputPayment = payload.get("paymentAmount") != null ? Double.parseDouble(payload.get("paymentAmount").toString()) : 0;

        // CALCULATE GRAND TOTAL BERDASARKAN LOGIC INDUK DI BACKEND
        double grandTotal = (room.getRoomRate() * monster.getStayDays()) + monster.getExtraCost() - monster.getPrepaidAmount();

        // FIX SAKTI MUTLAK: Blokir di backend jika kasir nekat bypass nominal uang kurang!
        if (inputPayment < grandTotal) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "⚠ Gagal checkout: Uang pembayaran kurang dari total tagihan induk! Tamu dilarang keluar.");
            return ResponseEntity.badRequest().body(error);
        }

        // Bikin riwayat baru memanfaatkan Constructor utama model Transaction lu
        Transaction riwayatBaru = new Transaction(monster, room, inputPayment);
        riwayatBaru.processPayment(); // Mengubah state boolean isPaid menjadi TRUE secara otomatis

        // Simpan nota ke database laporan permanen
        transactionRepo.save(riwayatBaru);

        // Update Kamar: Kosongkan tamu dan set status jadi DIRTY (Kotor)
        room.setOccupied(false);
        room.setCurrentGuest(null);
        room.setStatus(RoomStatus.DIRTY);
        roomRepo.save(room);

        // Hapus data naga dari tabel aktif (Monsters)
        monsterRepo.delete(monster);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Berhasil Checkout!");
        return ResponseEntity.ok(response);
    }
}