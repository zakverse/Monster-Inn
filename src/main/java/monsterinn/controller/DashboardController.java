package monsterinn.controller;

import monsterinn.model.Monster;
import monsterinn.repository.MonsterRepository;
import monsterinn.model.Room;
import monsterinn.model.RoomStatus;
import monsterinn.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MonsterRepository monsterRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Room> allRooms = roomRepository.findAll();
        long totalKamar = allRooms.size();
        long tersedia = roomRepository.countByStatus(RoomStatus.AVAILABLE);
        long terisi = roomRepository.countByStatus(RoomStatus.OCCUPIED);

        String occupancy = totalKamar > 0
            ? String.format("%d%%", (int) ((terisi * 100.0) / totalKamar))
            : "0%";

        // Ambil semua monster yang sedang menginap (punya roomId)
        List<Monster> activeGuests = monsterRepository.findByRoomIdIsNotNull();

        model.addAttribute("totalKamar", totalKamar);
        model.addAttribute("tersedia", tersedia);
        model.addAttribute("terisi", terisi);
        model.addAttribute("occupancy", occupancy);
        model.addAttribute("activeGuests", activeGuests);

        return "view/dashboard";
    }
}
