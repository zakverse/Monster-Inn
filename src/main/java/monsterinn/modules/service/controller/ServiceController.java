package monsterinn.modules.service.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {
    @GetMapping("/service")
    public String showServicePage() {
        // Asumsi UI HTML bernama service.html sudah disiapkan oleh Front-end
        return "modules/layanan/layanan"; 
    }
    
}
