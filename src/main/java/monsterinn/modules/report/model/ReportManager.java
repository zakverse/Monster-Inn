package monsterinn.modules.report.model;

import monsterinn.modules.transaction.model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    // Relasi Komposisi: ReportManager menampung riwayat transaksi
    private List<Transaction> history;

    public ReportManager() {
        // Inisialisasi list kosong saat objek ReportManager dibuat
        this.history = new ArrayList<>();
    }

    // 1. Method untuk menambahkan transaksi baru ke dalam riwayat
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            this.history.add(transaction);
        }
    }

    // 2. Method untuk mengakumulasi total pendapatan harian
    public Double genRevenueReport() {
        double totalRevenue = 0.0;
        for (Transaction t : history) {
            // Memastikan nilai tidak null sebelum ditambahkan
            if (t.getTotalCost() != null) {
                totalRevenue += t.getTotalCost();
            }
        }
        return totalRevenue;
    }

    // 3. Method untuk mengekspor data laporan menjadi teks
    public String exportData() {
        StringBuilder report = new StringBuilder();
        report.append("===================================\n");
        report.append("     LAPORAN PENDAPATAN HARIAN     \n");
        report.append("===================================\n");
        report.append("Total Transaksi : ").append(history.size()).append(" tamu\n");
        report.append("Total Omset     : Rp ").append(String.format("%.2f", genRevenueReport())).append("\n");
        report.append("-----------------------------------\n");
        report.append("Rincian Transaksi:\n");
        
        for (Transaction t : history) {
            // Memeriksa apakah tamu dan kamar ada datanya untuk menghindari NullPointerException
            String roomName = (t.getBookedRoom() != null) ? t.getBookedRoom().getRoomId() : "N/A";
            String guestName = (t.getGuest() != null) ? t.getGuest().getName() : "N/A";
            
            report.append(String.format("- [%s] Kamar %s | %s | Rp %.2f\n", 
                t.getTransId(), roomName, guestName, t.getTotalCost()));
        }
        
        report.append("===================================");
        return report.toString();
    }

    // Getter untuk mengambil array riwayat
    public List<Transaction> getHistory() {
        return history;
    }
}