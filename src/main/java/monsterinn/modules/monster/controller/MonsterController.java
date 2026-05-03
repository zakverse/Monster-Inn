package monsterinn.modules.monster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MonsterController {

    @GetMapping("/registrasi")
    public String showRegistrationPage(Model model) {
        // Mengaktifkan menu 'Registrasi' di sidebar
        model.addAttribute("activePage", "registrasi");
        return "modules/registrasi/registrasi";
    }
}