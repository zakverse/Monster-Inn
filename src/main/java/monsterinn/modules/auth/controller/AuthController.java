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
        
        // Simulasi data dari database (karena ini purwarupa)
        User registeredReceptionist = new User("admin", "admin123", "Receptionist");

        // MEMANGGIL METODE ENKAPSULASI: Validasi password dilakukan di dalam objek User
        boolean isValid = registeredReceptionist.validate(inputUsername, inputPassword);

        if (isValid) {
            // Jika benar, pindah ke halaman dashboard
            return "redirect:/dashboard"; 
        } else {
            // Jika salah, kembali ke login dan tampilkan pesan error
            model.addAttribute("error", "Username atau Password salah!");
            return "modules/auth/login";
        }
    }
}