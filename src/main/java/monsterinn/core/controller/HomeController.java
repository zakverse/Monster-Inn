package monsterinn.core.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showLandingPage() {
        
        return "modules/home/home";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "modules/dashboard/dashboard";
    }

}