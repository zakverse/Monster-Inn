package monsterinn.modules.room.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {
    @GetMapping("/room")
    public String showRoomPage() {
        // Asumsi UI HTML bernama room.html sudah disiapkan oleh Front-end
        return "modules/room/status_kamar"; 
    }
    
}
