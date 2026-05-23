package monsterinn.modules.transaction.controller;

import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.model.RoomStatus;
import monsterinn.modules.room.repository.RoomRepository;
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

    public TransactionController(MonsterRepository monsterRepo, RoomRepository roomRepo) {
        this.monsterRepo = monsterRepo;
        this.roomRepo = roomRepo;
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
        // Rumus: (Sewa Kamar * Hari) + Biaya Layanan - DP
        data.put("grandTotal", (room.getRoomRate() * monster.getStayDays()) + monster.getExtraCost() - monster.getPrepaidAmount());

        return ResponseEntity.ok(data);
    }

    // 3. API untuk Konfirmasi Checkout (Hapus Naga & Kosongkan Kamar)
    @PostMapping("/api/checkout/confirm/{id}")
    @ResponseBody
    public ResponseEntity<String> confirmCheckout(@PathVariable String id) {
        Monster monster = monsterRepo.findById(id).orElse(null);
        if (monster == null) return ResponseEntity.badRequest().body("Data naga tidak ada.");

        Room room = roomRepo.findById(monster.getRoomId()).orElse(null);
        if (room != null) {
            // Update Kamar: Kosongkan tamu dan set status jadi DIRTY (Kotor)
            room.setOccupied(false);
            room.setCurrentGuest(null);
            room.setStatus(RoomStatus.DIRTY);
            roomRepo.save(room);
        }

        // Hapus data naga dari tabel aktif (Monsters)
        monsterRepo.delete(monster);

        return ResponseEntity.ok("Berhasil Checkout!");
    }
}