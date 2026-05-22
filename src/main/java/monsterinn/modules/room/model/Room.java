package monsterinn.modules.room.model;

import monsterinn.modules.monster.model.Monster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rooms")
@JsonIgnoreProperties({"currentGuest"})
public class Room {

    @Id
    private String roomId;
    private String elementCap; // Isi: FIRE, WATER, atau EARTH
    private double roomRate;
    private boolean isOccupied = false;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @OneToOne
    @JoinColumn(name = "guest_id")
    private Monster currentGuest;

    public Room(String roomId, String elementCap, double roomRate) {
        this.roomId = roomId;
        this.elementCap = elementCap;
        this.roomRate = roomRate;
    }

    public void checkIn(Monster guest) {
        if (guest == null) throw new IllegalArgumentException("Guest tidak boleh kosong!");
        
        if (this.status != RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Kamar sedang tidak tersedia!");
        }

        // Validasi Habitat: Memastikan elemen monster cocok dengan kapasitas kamar
        if (!guest.getElement().equalsIgnoreCase(this.elementCap)) {
            throw new IllegalArgumentException("Elemen Monster " + guest.getElement() + 
                " tidak cocok dengan habitat " + this.elementCap);
        }

        this.currentGuest = guest;
        this.isOccupied = true;
        this.status = RoomStatus.OCCUPIED;
    }

    public void checkOut() {
        if (this.status != RoomStatus.OCCUPIED || this.currentGuest == null) {
            throw new IllegalStateException("Kamar tidak sedang ditempati!");
        }
        this.currentGuest = null;
        this.isOccupied = false;
        this.status = RoomStatus.DIRTY;
    }

    public void markCleaned() {
        this.status = RoomStatus.AVAILABLE;
    }
}