package monsterinn.modules.dashboard.controller;

import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.room.repository.RoomRepository;
import monsterinn.modules.room.model.RoomStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final MonsterRepository monsterRepository;
    private final RoomRepository roomRepository;

    public DashboardController(MonsterRepository monsterRepository, RoomRepository roomRepository) {
        this.monsterRepository = monsterRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Mengambil statistik langsung dari DB
        long totalKamar = roomRepository.count();
        long tersedia = roomRepository.findByStatus(RoomStatus.AVAILABLE).size();
        long terisi = roomRepository.findByStatus(RoomStatus.OCCUPIED).size();
        
        double occupancy = (totalKamar > 0) ? ((double) terisi / totalKamar) * 100 : 0;

        // Kirim data ke UI
        model.addAttribute("totalKamar", String.format("%02d", totalKamar));
        model.addAttribute("tersedia", String.format("%02d", tersedia));
        model.addAttribute("terisi", String.format("%02d", terisi));
        model.addAttribute("occupancy", String.format("%.1f%%", occupancy));
        
        // List Tamu untuk tabel
        model.addAttribute("activeGuests", monsterRepository.findAll());
        model.addAttribute("activePage", "dashboard");

        return "modules/dashboard/dashboard";
    }
}