package monsterinn.modules.transaction.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.monster.model.Monster;

@Data // Menggunakan Lombok agar selaras dengan kode temanmu (otomatis bikin getter setter)
@NoArgsConstructor // Lombok untuk constructor kosong (wajib untuk JPA/Database)
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String transId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room bookedRoom;

    @ManyToOne
    @JoinColumn(name = "monster_id")
    private Monster guest;

    private Integer durationDays;
    private Double cashPaid = 0.0;
    private Double totalCost = 0.0;
    private Double changeAmount = 0.0; 

    // Constructor custom
    public Transaction(String transId, Room bookedRoom, Monster guest, Integer durationDays) {
        this.transId = transId;
        this.bookedRoom = bookedRoom;
        this.guest = guest;
        this.durationDays = durationDays;
    }

    // --- METHOD IMPLEMENTASI OOP ---

    public void calculateTotal() {
        if (bookedRoom != null && guest != null) {
            // Biaya Kamar (Memanggil getRoomRate() yang dibuat gaib oleh Lombok)
            double roomCost = bookedRoom.getRoomRate() * durationDays;
            
            // Biaya Tamu (Polimorfisme: memanggil hitungan spesifik Fire/Water/Earth Monster)
            double guestCost = guest.calculateTotalCost(); 
            
            this.totalCost = roomCost + guestCost;
        }
    }

    public boolean processPayment(Double amountPaid) {
        calculateTotal(); // Pastikan total dihitung dulu
        
        if (amountPaid >= this.totalCost) {
            this.cashPaid = amountPaid;
            this.changeAmount = amountPaid - this.totalCost;
            return true; // Sukses
        }
        return false; // Uang kurang
    }

    public String generateInvoice() {
        if (this.cashPaid == 0.0) {
            return "Transaksi Gagal: Tagihan belum dibayar lunas!";
        }
        return String.format(
            "=== INVOICE MONSTER INN ===\n" +
            "ID Transaksi : %s\n" +
            "Kamar        : %s (Rp %.2f / malam)\n" +
            "Tamu         : %s (%s)\n" +
            "Durasi       : %d Malam\n" +
            "---------------------------\n" +
            "Total Biaya  : Rp %.2f\n" +
            "Tunai        : Rp %.2f\n" +
            "Kembalian    : Rp %.2f\n" +
            "===========================",
            this.transId, 
            this.bookedRoom.getRoomId(), 
            this.bookedRoom.getRoomRate(),
            this.guest.getName(), 
            this.guest.getElement(),
            this.durationDays, 
            this.totalCost, 
            this.cashPaid, 
            this.changeAmount
        );
    }
}