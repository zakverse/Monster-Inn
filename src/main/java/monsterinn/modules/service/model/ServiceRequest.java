package monsterinn.modules.service.model;

import jakarta.persistence.*; // JPA annotations
import lombok.Data; // lombok untuk getter/setter otomatis

@Data // untuk otomatis generate getter, setter, toString, equals, dan hashCode
@Entity // Menandakan bahwa kelas ini adalah entitas JPA yang akan dipetakan ke tabel database
@Table(name = "service_requests")
public class ServiceRequest {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id; // Auto Increment

    private String roomId;
    private String guestId;
    private String orderName;
    private Double rate;
    private Boolean served = false; // Status apakah layanan sudah diantar/selesai

    // Constructor kosong untuk JPA
    public ServiceRequest() {}

    // Constructor untuk mempermudah pembuatan objek baru
    public ServiceRequest(String roomId, String guestId, String orderName, Double rate) {
        this.roomId = roomId;
        this.guestId = guestId;
        this.orderName = orderName;
        this.rate = rate;
    }
}