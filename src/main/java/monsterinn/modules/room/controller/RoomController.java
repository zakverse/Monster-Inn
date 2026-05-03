package monsterinn.modules.room.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

    @GetMapping("/status")
    public String showStatusPage(Model model) {
        return "modules/room/status_kamar";
    }
}