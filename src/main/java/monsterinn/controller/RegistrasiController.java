package monsterinn.controller;

import monsterinn.model.*;
import monsterinn.repository.MonsterRepository;
import monsterinn.model.Room;
import monsterinn.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/registrasi")
public class RegistrasiController {

    private final MonsterRepository monsterRepository;
    private final RoomRepository roomRepository;

    public RegistrasiController(MonsterRepository monsterRepository, RoomRepository roomRepository) {
        this.monsterRepository = monsterRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("availableRooms", roomRepository.findAll());
        model.addAttribute("activePage", "registrasi");
        return "view/registrasi";
    }

    @PostMapping("/simpan")
    public String processRegistration(@RequestParam("namaTamu") String nama,
                                    @RequestParam("elemen") String elemen,
                                    @RequestParam("noKamar") String roomId,
                                    @RequestParam("deposit") double deposit,
                                    RedirectAttributes redirectAttributes) {
        try {
            // 1. Ambil Kamar dari Database
            Room kamar = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Kamar tidak ditemukan!"));

            // 2. Factory Logic (Gunakan FIRE, WATER, EARTH)
            Monster monsterBaru;
            String idBaru = "M-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

            if (elemen.equalsIgnoreCase("fire")) {
                monsterBaru = new FireMonster(idBaru, nama, 150000, 25000);
            } else if (elemen.equalsIgnoreCase("water")) {
                monsterBaru = new WaterMonster(idBaru, nama, 170000, 20000);
            } else {
                monsterBaru = new EarthMonster(idBaru, nama, 140000, 15000);
            }

            monsterBaru.setPrepaidAmount(deposit);

            // 3. PBO Logic (Hubungkan tamu ke kamar & sinkronisasi ID)
            kamar.checkIn(monsterBaru);

            // FIX DUPLICATE OBJECT: Cukup simpan kamarnya saja, monsternya otomatis kesimpen karena CascadeType.ALL di Room.java!
            roomRepository.save(kamar);

            // Beri feedback sukses ke user
            redirectAttributes.addFlashAttribute("successMsg", "Inkripsi check-in berhasil! Selamat datang di Monster Inn, " + nama + "!");
            return "redirect:/registrasi"; 

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/registrasi";
        }
    }
}