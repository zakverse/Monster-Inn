package monsterinn.modules.report.controller;

import monsterinn.modules.transaction.model.Transaction;
import monsterinn.modules.report.repository.TransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
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
                .mapToDouble(Transaction::getTotalCost)
                .sum();

        // 2. Hitung Popularitas Elemen (Persentase)
        long total = history.size();
        long fireCount = history.stream().filter(t -> t.getElement() != null && t.getElement().equalsIgnoreCase("FIRE")).count();
        long waterCount = history.stream().filter(t -> t.getElement() != null && t.getElement().equalsIgnoreCase("WATER")).count();
        long earthCount = history.stream().filter(t -> t.getElement() != null && t.getElement().equalsIgnoreCase("EARTH")).count();

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

    @GetMapping("/laporan/export")
    public void exportToExcelCsv(HttpServletResponse response) throws IOException {
        List<Transaction> history = transactionRepo.findAll();

        // Set konfigurasi header HTTP agar browser mendownloadnya sebagai file .csv berkas Excel
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=laporan_operasional_monster_inn.csv");

        PrintWriter writer = response.getWriter();

        // 1. Cetak Header Kolom Excel (Gunakan pembatas ';' agar terbaca otomatis rapi di Excel Regional Indo)
        writer.println("No;ID Transaksi;Nama Tamu;Klan Elemen;Nomor Kamar;Durasi Stay (Malam);Uang Muka (Rp);Total Tagihan (Rp);Status;Waktu Checkout");

        int counter = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 2. Looping data riwayat dari database MySQL untuk dimasukkan ke baris Excel
        for (Transaction t : history) {
            String transId = t.getTransId() != null ? t.getTransId() : "-";
            String guestName = t.getName() != null ? t.getName() : "-"; // Menggunakan custom getter getName() lu
            String element = t.getElement() != null ? t.getElement() : "-";
            String roomId = t.getRoomId() != null ? t.getRoomId() : "-";
            int stayDays = t.getStayDays();
            double prepaid = t.getPrepaidAmount();
            double totalCost = t.getTotalCost();
            String status = t.isPaid() ? "LUNAS" : "BELUM LUNAS";
            String checkoutTime = t.getCheckoutTime() != null ? t.getCheckoutTime().format(formatter) : "-";

            // Gabungkan variabel menjadi satu baris teks csv dipisahkan dengan titik koma
            writer.println(counter + ";" + transId + ";" + guestName + ";" + element + ";" + roomId + ";" + stayDays + ";" + (long)prepaid + ";" + (long)totalCost + ";" + status + ";" + checkoutTime);
            counter++;
        }

        writer.flush();
        writer.close();
    }
}