package monsterinn.modules.service.controller;

import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.service.model.ServiceEntity;
import monsterinn.modules.service.model.ServiceRequest;
import monsterinn.modules.service.repository.ServiceRepository;
import monsterinn.modules.service.repository.ServiceRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/service")
public class ServiceController {

    private final MonsterRepository monsterRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceRequestRepository requestRepository;

    public ServiceController(MonsterRepository monsterRepository, 
                            ServiceRepository serviceRepository, 
                            ServiceRequestRepository requestRepository) {
        this.monsterRepository = monsterRepository;
        this.serviceRepository = serviceRepository;
        this.requestRepository = requestRepository;
    }

    @GetMapping
    public String showServicePage(Model model) {
        // Ambil tamu yang beneran lagi nginep aja
        model.addAttribute("activeGuests", monsterRepository.findAll());
        // Ambil semua daftar menu layanan dari DB
        model.addAttribute("allServices", serviceRepository.findAll());
        model.addAttribute("activePage", "layanan");
        return "modules/layanan/layanan";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addService(@RequestBody ServiceRequest request) {
        try {
            // Update tagihan monster secara otomatis
            Monster monster = monsterRepository.findById(request.getGuestId()).orElse(null);
            if (monster != null) {
                monster.pushService(request.getOrderName(), request.getRate());
                monsterRepository.save(monster); // Simpan extraCost baru
            }
            
            requestRepository.save(request);
            return ResponseEntity.ok("✦ Layanan berhasil ditambahkan! ✦");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal: " + e.getMessage());
        }
    }
}