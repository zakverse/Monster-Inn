package monsterinn.modules.transaction.model;

import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.room.model.Room;
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
        this.serviceTotal = monster.getExtraCost();
        this.roomTotal = monster.calculateTotalCost() - monster.getExtraCost();
        this.prepaidAmount = monster.getPrepaidAmount();
        this.totalCost = Math.max(0, monster.calculateTotalCost() - monster.getPrepaidAmount());
        this.paymentAmount = paymentAmount;
        this.changeAmount = Math.max(0, paymentAmount - this.totalCost);
        this.checkoutTime = LocalDateTime.now();
        this.serviceLog = new ArrayList<>(monster.getServiceLog());
        
        // Pemicu mutlak status kelunasan saat objek memori pertama kali dibangun
        this.paid = paymentAmount >= this.totalCost;
    }

    public double calculateTotal() {
        return totalCost;
    }

    public double calculateTotalCost() {
        return totalCost;
    }

    public boolean processPayment() {
        this.paid = paymentAmount >= totalCost;
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