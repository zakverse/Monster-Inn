package monsterinn.controller;

import monsterinn.model.Monster;
import monsterinn.repository.MonsterRepository;
import monsterinn.model.ServiceEntity;
import monsterinn.model.ServiceRequest;
import monsterinn.repository.ServiceRepository;
import monsterinn.repository.ServiceRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/layanan")
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
        // 1. Ambil data tamu monster aktif yang sedang menginap di hotel
        model.addAttribute("activeGuests", monsterRepository.findAll());
        
        // FIX KEBOCORAN DATA: Pastikan mengambil data MASTER MENU yang sah dari serviceRepository!
        model.addAttribute("allServices", serviceRepository.findAll());
        
        // Load all service requests to display on the service dashboard
        model.addAttribute("serviceRequests", requestRepository.findAll());
        
        model.addAttribute("activePage", "layanan");
        return "view/layanan";
    }

    // API untuk kebutuhan fleksibilitas AJAX Fetch (Mengembalikan List data mentah bebas error compile)
    @GetMapping("/api/menu/{element}")
    @ResponseBody
    public ResponseEntity<?> getMenuByElement(@PathVariable String element) {
        List<Object> allData = new ArrayList<>(serviceRepository.findAll());
        return ResponseEntity.ok(allData); 
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addService(@RequestBody ServiceRequest request) {
        try {
            // PBO State Mutation: Ambil data monster dan suntikkan akumulasi tagihan extraCost
            Monster monster = monsterRepository.findById(request.getGuestId()).orElse(null);
            if (monster != null) {
                monster.pushService(request.getOrderName(), request.getRate());
                monsterRepository.save(monster); // Persist data tagihan baru ke database MySQL
            }
            
            requestRepository.save(request);
            return ResponseEntity.ok("✦ Layanan berhasil ditambahkan! ✦");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal: " + e.getMessage());
        }
    }

    @PostMapping("/complete/{id}")
    @ResponseBody
    public ResponseEntity<String> completeService(@PathVariable Integer id) {
        try {
            ServiceRequest request = requestRepository.findById(id).orElse(null);
            if (request == null) {
                return ResponseEntity.status(404).body("Permintaan layanan tidak ditemukan.");
            }
            request.setServed(true);
            requestRepository.save(request);
            return ResponseEntity.ok("✦ Layanan diselesaikan! ✦");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Gagal: " + e.getMessage());
        }
    }
}