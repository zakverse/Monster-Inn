package monsterinn.modules.report.controller;

import monsterinn.modules.report.model.ReportManager;
import monsterinn.modules.transaction.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class LaporanController {

    @Autowired private ReportManager reportManager;

    @GetMapping("/laporan")
    public String showLaporan(Model model) {
        List<Transaction> transactions = reportManager.getTransactions();
        List<Transaction> recentTransactions = reportManager.getRecentTransactions();
        Map<String, Long> elementPopularity = reportManager.getElementPopularity();

        double revenue = reportManager.getTotalRevenue();
        long totalOrders = transactions.size();
        long totalMonsters = reportManager.getTotalCheckedOutMonsters();

        long fireCount = elementPopularity.getOrDefault("FIRE", 0L);
        long waterCount = elementPopularity.getOrDefault("WATER", 0L);
        long earthCount = elementPopularity.getOrDefault("EARTH", 0L);
        long total = fireCount + waterCount + earthCount;

        int firePct  = total > 0 ? (int) (fireCount  * 100 / total) : 0;
        int waterPct = total > 0 ? (int) (waterCount * 100 / total) : 0;
        int earthPct = total > 0 ? (int) (earthCount * 100 / total) : 0;

        model.addAttribute("revenue", revenue);
        model.addAttribute("totalRevenue", revenue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalTransactions", totalOrders);
        model.addAttribute("totalMonsters", totalMonsters);
        model.addAttribute("firePct", firePct);
        model.addAttribute("waterPct", waterPct);
        model.addAttribute("earthPct", earthPct);
        model.addAttribute("history", recentTransactions);
        model.addAttribute("recentTransactions", recentTransactions);
        model.addAttribute("elementPopularity", elementPopularity);

        return "modules/report/laporan";
    }
}
