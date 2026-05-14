package monsterinn.modules.room.controller;

import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping("/room")
    public String showRoomStatus(Model model) {
        List<Room> allRooms = roomRepository.findAll();

        // Pisahin kamar berdasarkan elemen (Sesuai Enum/String di DB: FIRE, WATER, EARTH)
        model.addAttribute("fireRooms", allRooms.stream()
            .filter(r -> r.getElementCap().equalsIgnoreCase("FIRE")).toList());
        
        model.addAttribute("waterRooms", allRooms.stream()
            .filter(r -> r.getElementCap().equalsIgnoreCase("WATER")).toList());
        
        model.addAttribute("earthRooms", allRooms.stream()
            .filter(r -> r.getElementCap().equalsIgnoreCase("EARTH")).toList());

        model.addAttribute("activePage", "room");
        return "modules/room/status_kamar"; 
    }

    // Fitur tambahan buat bersihin kamar dari kotor ke tersedia
    @PostMapping("/room/clean/{id}")
    public String cleanRoom(@PathVariable String id) {
        Room kamar = roomRepository.findById(id).orElseThrow();
        kamar.markCleaned();
        roomRepository.save(kamar);
        return "redirect:/room";
    }
}