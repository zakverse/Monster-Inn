package monsterinn.modules.monster.controller;

import monsterinn.modules.monster.model.*;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.repository.RoomRepository;
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
        return "modules/registrasi/registrasi";
    }

    @PostMapping("/simpan")
    public String processRegistration(@RequestParam("namaTamu") String nama,
                                    @RequestParam("elemen") String elemen,
                                    @RequestParam("noKamar") String roomId,
                                    @RequestParam("deposit") double deposit,
                                    RedirectAttributes redirectAttributes) {
        try {
            // 1. Ambil Kamar
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

            // 3. PBO Logic (Check-in & Validasi)
            kamar.checkIn(monsterBaru);

            // 4. Simpan (Karena sudah Cascade, cukup save Kamar saja)
            roomRepository.save(kamar);

            return "redirect:/room"; // Langsung ke Status Kamar buat liat hasil

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/registrasi";
        }
    }
}