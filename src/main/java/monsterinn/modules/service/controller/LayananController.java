package monsterinn.modules.service.controller;

import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.service.model.ServiceRequest;
import monsterinn.modules.service.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class LayananController {

    @Autowired private MonsterRepository monsterRepository;
    @Autowired private ServiceRepository serviceRepository;

    // Daftar layanan statis per elemen
    private static final List<Map<String, Object>> FIRE_SERVICES = List.of(
        Map.of("serviceName", "Injeksi Magma",      "price", 60000.0, "icon", "fa-fire",       "targetElement", "FIRE"),
        Map.of("serviceName", "Pemandian Lava",     "price", 80000.0, "icon", "fa-hot-tub-person","targetElement", "FIRE"),
        Map.of("serviceName", "Nutrisi Sulfur",     "price", 45000.0, "icon", "fa-flask",       "targetElement", "FIRE")
    );
    private static final List<Map<String, Object>> WATER_SERVICES = List.of(
        Map.of("serviceName", "Nutrisi Akuatik",    "price", 70000.0, "icon", "fa-droplet",     "targetElement", "WATER"),
        Map.of("serviceName", "Filtrasi Kolam",     "price", 55000.0, "icon", "fa-water",       "targetElement", "WATER"),
        Map.of("serviceName", "Terapi Mineral Air", "price", 90000.0, "icon", "fa-spa",          "targetElement", "WATER")
    );
    private static final List<Map<String, Object>> EARTH_SERVICES = List.of(
        Map.of("serviceName", "Pupuk Mineral",      "price", 50000.0, "icon", "fa-seedling",    "targetElement", "EARTH"),
        Map.of("serviceName", "Kompos Premium",     "price", 40000.0, "icon", "fa-leaf",         "targetElement", "EARTH"),
        Map.of("serviceName", "Tanah Vulkanik",     "price", 65000.0, "icon", "fa-mountain",    "targetElement", "EARTH")
    );

    @GetMapping("/layanan")
    public String showLayanan(Model model) {
        List<Monster> activeGuests = monsterRepository.findByRoomIdIsNotNull();

        // Gabungkan semua layanan
        List<Map<String, Object>> allServices = new java.util.ArrayList<>();
        allServices.addAll(FIRE_SERVICES);
        allServices.addAll(WATER_SERVICES);
        allServices.addAll(EARTH_SERVICES);

        model.addAttribute("activeGuests", activeGuests);
        model.addAttribute("allServices", allServices);
        return "modules/layanan/layanan";
    }

    @PostMapping("/service/add")
    @ResponseBody
    public ResponseEntity<String> addService(@RequestBody Map<String, Object> body) {
        try {
            String guestId   = (String) body.get("guestId");
            String roomId    = (String) body.get("roomId");
            String orderName = (String) body.get("orderName");
            double rate      = ((Number) body.get("rate")).doubleValue();

            // Simpan ke DB
            ServiceRequest sr = new ServiceRequest(roomId, guestId, orderName, rate);
            serviceRepository.save(sr);

            // Update extraCost pada monster
            monsterRepository.findById(guestId).ifPresent(monster -> {
                monster.pushService(orderName, rate);
                monsterRepository.save(monster);
            });

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
