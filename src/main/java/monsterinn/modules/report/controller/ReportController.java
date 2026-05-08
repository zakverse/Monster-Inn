package monsterinn.modules.report.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;  

@Controller
public class ReportController {
    @GetMapping("/laporan")
    public String showReportPage() {
        // Asumsi UI HTML bernama report.html sudah disiapkan oleh Front-end
        return "modules/report/laporan"; 
    }
    
}
