package monsterinn.modules.registrasi.controller;

import monsterinn.modules.monster.model.EarthMonster;
import monsterinn.modules.monster.model.FireMonster;
import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.monster.model.WaterMonster;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.model.RoomStatus;
import monsterinn.modules.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class RegistrasiController {

    @Autowired
    private MonsterRepository monsterRepository;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/registrasi")
    public String showRegistrasi(Model model) {
        List<Room> availableRooms = roomRepository.findAll().stream()
            .filter(r -> r.getStatus() == RoomStatus.AVAILABLE)
            .toList();
        model.addAttribute("availableRooms", availableRooms);
        return "modules/registrasi/registrasi";
    }

    @PostMapping("/registrasi/simpan")
    public String simpanRegistrasi(
            @RequestParam String namaTamu,
            @RequestParam String elemen,
            @RequestParam String noKamar,
            @RequestParam(defaultValue = "200000") double deposit,
            RedirectAttributes ra) {

        try {
            Room room = roomRepository.findById(noKamar)
                .orElseThrow(() -> new IllegalArgumentException("Kamar tidak ditemukan: " + noKamar));

            String idMonster = "M-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

            Monster monster;
            switch (elemen.toUpperCase()) {
                case "FIRE"  -> monster = new FireMonster(idMonster, namaTamu, 150000, 25000);
                case "WATER" -> monster = new WaterMonster(idMonster, namaTamu, 170000, 20000);
                case "EARTH" -> monster = new EarthMonster(idMonster, namaTamu, 140000, 15000);
                default      -> throw new IllegalArgumentException("Elemen tidak valid: " + elemen);
            }

            monster.setPrepaidAmount(deposit);
            monster.setRoomId(noKamar);
            monsterRepository.save(monster);

            room.checkIn(monster);
            roomRepository.save(room);

            ra.addFlashAttribute("successMsg", namaTamu + " berhasil check-in ke kamar #" + noKamar + "!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Gagal registrasi: " + e.getMessage());
        }

        return "redirect:/registrasi";
    }
}
