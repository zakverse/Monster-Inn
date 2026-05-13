package monsterinn.modules.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import monsterinn.modules.auth.model.User;

@Controller
public class AuthController {

    // 1. Menampilkan halaman form login
    @GetMapping("/login")
    public String showLoginScreen() {
        return "modules/auth/login"; // Ini akan memanggil file login.html
    }

    // 2. Menangkap data ketika tombol "Login" ditekan
    @PostMapping("/login")
public String processLogin(@RequestParam("username") String inputUsername, 
                        @RequestParam("password") String inputPassword, 
                        Model model) {
    
    // Akun tunggal Admin (Hardcoded sesuai permintaanmu)
    User registeredReceptionist = new User("admin", "admin123", "Receptionist");

    // VALIDASI ENKAPSULASI (Ini yang dapet nilai OOP)
    if (registeredReceptionist.validate(inputUsername, inputPassword)) {
        return "redirect:/dashboard"; 
    } else {
        model.addAttribute("error", "Maaf, kunci lobi tidak cocok! (Username/Password Salah)");
        return "modules/auth/login";
    }
}
}