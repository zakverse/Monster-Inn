package monsterinn.modules.monster.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MonsterController {
    @GetMapping("/registrasi")
    public String showMonsterPage() {
        // Asumsi UI HTML bernama registrasi.html sudah disiapkan oleh Front-end
        return "modules/registrasi/registrasi"; 
    }
}
