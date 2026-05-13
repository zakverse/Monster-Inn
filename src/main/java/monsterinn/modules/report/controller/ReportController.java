package monsterinn.modules.report.controller;

import monsterinn.modules.transaction.model.Transaction;
import monsterinn.modules.report.repository.TransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReportController {

    private final TransactionRepository transactionRepo;

    public ReportController(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @GetMapping("/laporan")
    public String showReportPage(Model model) {
        List<Transaction> history = transactionRepo.findAll();

        // 1. Hitung Total Omset
        double totalRevenue = history.stream()
                .mapToDouble(t -> t.getTotalCost() != null ? t.getTotalCost() : 0.0)
                .sum();

        // 2. Hitung Popularitas Elemen (Persentase)
        long total = history.size();
        long fireCount = history.stream().filter(t -> t.getGuest().getElement().equalsIgnoreCase("FIRE")).count();
        long waterCount = history.stream().filter(t -> t.getGuest().getElement().equalsIgnoreCase("WATER")).count();
        long earthCount = history.stream().filter(t -> t.getGuest().getElement().equalsIgnoreCase("EARTH")).count();

        // Jaga-jaga kalau pembagi nol
        double firePct = total > 0 ? (double) fireCount / total * 100 : 0;
        double waterPct = total > 0 ? (double) waterCount / total * 100 : 0;
        double earthPct = total > 0 ? (double) earthCount / total * 100 : 0;

        // Kirim data ke HTML
        model.addAttribute("revenue", totalRevenue);
        model.addAttribute("totalOrders", total);
        model.addAttribute("totalMonsters", total); // Dalam konteks ini sama dengan total transaksi
        model.addAttribute("firePct", (int) firePct);
        model.addAttribute("waterPct", (int) waterPct);
        model.addAttribute("earthPct", (int) earthPct);
        
        // Kirim data history untuk log (Opsional)
        model.addAttribute("history", history);
        model.addAttribute("activePage", "laporan");

        return "modules/report/laporan";
    }
}