package monsterinn.core.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // URL root untuk landing page
    public String showLandingPage() {
        return "modules/home/home";
    }


    // @GetMapping("/dashboard") // URL untuk dashboard setelah login berhasil
    // public String showDashboard() {
    //     return "modules/dashboard/dashboard"; 
    // }

}