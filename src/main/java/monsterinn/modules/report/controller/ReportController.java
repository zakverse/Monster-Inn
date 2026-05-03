package monsterinn.modules.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/laporan")
public class ReportController {

    @GetMapping
    public String showReportPage(Model model) {
        // Mengaktifkan menu 'Laporan' di sidebar
        model.addAttribute("activePage", "laporan");
        return "modules/report/laporan";
    }
}