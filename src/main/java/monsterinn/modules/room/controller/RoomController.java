package monsterinn.modules.room.controller;

import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping("/room")
    public String showRoomStatus(Model model) {
        // Ambil semua data kamar dari MySQL
        List<Room> allRooms = roomRepository.findAll();

        // Filter menggunakan String yang sama dengan Database (FIRE, WATER, EARTH)
        model.addAttribute("fireRooms", allRooms.stream()
            .filter(r -> r.getElementCap().equalsIgnoreCase("FIRE")).toList());
        
        model.addAttribute("waterRooms", allRooms.stream()
            .filter(r -> r.getElementCap().equalsIgnoreCase("WATER")).toList());
        
        model.addAttribute("earthRooms", allRooms.stream()
            .filter(r -> r.getElementCap().equalsIgnoreCase("EARTH")).toList());

        // Penanda untuk sidebar (biar menu Status Kamar kelihatan aktif)
        model.addAttribute("activePage", "room");
        
        // Pastikan nama file HTML lu adalah status_kamar.html
        return "modules/room/status_kamar"; 
    }
}