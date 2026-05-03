package monsterinn.modules.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layanan")
public class ServiceController {

    @GetMapping
    public String showServicePage(Model model) {
        // Mengaktifkan menu 'Layanan' di sidebar
        model.addAttribute("activePage", "layanan");
        return "modules/layanan/layanan";
    }
}