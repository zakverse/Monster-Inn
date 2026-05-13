package monsterinn.modules.monster.controller;

import monsterinn.modules.monster.model.*;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        // Ambil semua kamar untuk ditampilkan di dropdown (nanti difilter di JS)
        model.addAttribute("availableRooms", roomRepository.findAll());
        model.addAttribute("activePage", "registrasi");
        return "modules/registrasi/registrasi";
    }

    @PostMapping("/simpan")
    public String processRegistration(@RequestParam("namaTamu") String nama,
                                    @RequestParam("elemen") String elemen,
                                    @RequestParam("noKamar") String roomId,
                                    @RequestParam("deposit") double deposit) {
        
        // 1. Factory Logic: Bikin objek monster berdasarkan elemen
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

        // 2. Eksekusi Check-In lewat Model Room (PBO Logic)
        Room kamar = roomRepository.findById(roomId).orElseThrow(); // Ambil data kamar dari DB berdasarkan ID yang dipilih
        kamar.checkIn(monsterBaru); // Di sini validasi elemen dicek

        // 3. Simpan data (Save monster dulu baru kamar karena OneToOne)
        monsterRepository.save(monsterBaru);
        roomRepository.save(kamar);

        return "redirect:/dashboard";
    }
}