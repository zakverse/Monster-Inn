package monsterinn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data 
@NoArgsConstructor 
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String transId;
    
    private String guestId;
    private String guestName;
    private String element;
    private String roomId;
    private int stayDays;
    private double roomTotal;
    private double serviceTotal;
    private double prepaidAmount;
    private double totalCost;
    // FIX CHECKOUT PREPAID REFUND: field settlement checkout dipisah dari total biaya kotor.
    private double remainingDue;
    private double refundAmount;
    private double paymentAmount;
    private double changeAmount;
    private LocalDateTime checkoutTime;

    @ElementCollection
    @CollectionTable(name = "transaction_service_logs", joinColumns = @JoinColumn(name = "trans_id"))
    @Column(name = "service_detail")
    private List<String> serviceLog = new ArrayList<>();
    
    private boolean paid;

    // Constructor Utama untuk logic pas checkout (FIX INDUK: Langsung set lunas di konstruktor)
    public Transaction(Monster monster, Room room, double paymentAmount) {
        if (monster == null) throw new IllegalArgumentException("Monster tidak boleh kosong");
        if (room == null) throw new IllegalArgumentException("Room tidak boleh kosong");

        this.transId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.guestId = monster.getIdMonster();
        this.guestName = monster.getName();
        this.element = monster.getElement();
        this.roomId = room.getRoomId();
        this.stayDays = monster.getStayDays();
        // FIX CHECKOUT PREPAID REFUND: totalCost adalah biaya asli, uang muka hanya mengurangi sisa bayar.
        this.roomTotal = Math.max(monster.calculateTotalCost() - monster.getExtraCost(), 0);
        this.serviceTotal = monster.getExtraCost();
        this.prepaidAmount = monster.getPrepaidAmount();
        this.totalCost = this.roomTotal + this.serviceTotal;
        this.remainingDue = Math.max(this.totalCost - this.prepaidAmount, 0);
        this.refundAmount = Math.max(this.prepaidAmount - this.totalCost, 0);
        // FIX CHECKOUT PREPAID REFUND: pembayaran checkout tidak boleh negatif.
        this.paymentAmount = Math.max(paymentAmount, 0);
        this.changeAmount = Math.max(this.paymentAmount - this.remainingDue, 0);
        this.checkoutTime = LocalDateTime.now();
        this.serviceLog = new ArrayList<>(monster.getServiceLog());
        
        // Pemicu mutlak status kelunasan saat objek memori pertama kali dibangun
        this.paid = this.remainingDue == 0 || this.paymentAmount >= this.remainingDue;
    }

    public double calculateTotal() {
        return totalCost;
    }

    public double calculateTotalCost() {
        return totalCost;
    }

    public boolean processPayment() {
        // FIX CHECKOUT PREPAID REFUND: lunas jika sisa bayar 0 atau pembayaran tambahan mencukupi.
        this.paymentAmount = Math.max(paymentAmount, 0);
        this.changeAmount = Math.max(this.paymentAmount - this.remainingDue, 0);
        this.paid = this.remainingDue == 0 || this.paymentAmount >= this.remainingDue;
        return paid;
    }

    // FIX SAKTI GETTER FOR JPA: Amankan kembalian nilai boolean paid agar dibaca valid oleh Hibernate & Excel
    public boolean isPaid() {
        return this.paid;
    }

    // Custom getter karena namanya beda dengan field variabel
    public String getName() {
        return guestName;
    }

    // Biar tetep me-return list yang unmodifiable/aman dari perubahan luar
    public List<String> getServiceLog() {
        return List.copyOf(serviceLog);
    }
}