package monsterinn.modules.service.controller;

import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.service.repository.ServiceRepository;
import monsterinn.modules.service.repository.ServiceRequestRepository;
import monsterinn.modules.service.model.ServiceRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("activeGuests", monsterRepository.findAll());
        model.addAttribute("availableServices", serviceRepository.findAll());
        model.addAttribute("activePage", "layanan");
        return "modules/layanan/layanan";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addService(@RequestBody ServiceRequest request) {
        requestRepository.save(request);
        return "✦ Pesanan Berhasil Dicatat! ✦";
    }
}