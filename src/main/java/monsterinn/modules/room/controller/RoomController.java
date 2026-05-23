package monsterinn.modules.room.controller;

import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.model.RoomStatus;
import monsterinn.modules.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/room/status")
    public String showRoomStatus(Model model) {
        List<Room> allRooms = roomRepository.findAll();
        model.addAttribute("fireRooms",  roomRepository.findByElementCap("FIRE"));
        model.addAttribute("waterRooms", roomRepository.findByElementCap("WATER"));
        model.addAttribute("earthRooms", roomRepository.findByElementCap("EARTH"));
        model.addAttribute("allRooms", allRooms);
        return "modules/room/status_kamar";
    }

    @PostMapping("/room/clean/{id}")
    public String cleanRoom(@PathVariable String id, RedirectAttributes ra) {
        roomRepository.findById(id).ifPresent(room -> {
            room.markCleaned();
            roomRepository.save(room);
        });
        ra.addFlashAttribute("successMsg", "Kamar #" + id + " selesai dibersihkan!");
        return "redirect:/room/status";
    }
}
