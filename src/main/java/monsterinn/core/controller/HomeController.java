package monsterinn.core.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Rute untuk Landing Page (Sudah benar)
    @GetMapping("/")
    public String showLandingPage() {
        return "modules/home/home";
    }

    // Ttambahkan rute ini untuk menangkap hasil redirect dari Login!
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "modules/dashboard/dashboard"; // Pastikan kamu sudah membuat file dashboard.html di folder templates
    }

}