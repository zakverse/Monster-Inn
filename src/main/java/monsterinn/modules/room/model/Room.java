package monsterinn.modules.room.model;

import monsterinn.modules.monster.model.Monster;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    private String roomId;
    private String elementCap; // FIRE, WATER, EARTH (Samakan dengan DB)
    private double roomRate;
    private boolean isOccupied = false;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.AVAILABLE;

    // Tambahkan CascadeType.ALL agar simpan otomatis
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "guest_id")
    private Monster currentGuest;

    public Room(String roomId, String elementCap, double roomRate) {
        this.roomId = roomId;
        this.elementCap = elementCap;
        this.roomRate = roomRate;
    }

    public void checkIn(Monster guest) {
        if (guest == null) throw new IllegalArgumentException("Monster tidak boleh kosong!");
        if (this.status != RoomStatus.AVAILABLE) throw new IllegalStateException("Kamar tidak siap!");
        
        // Pastikan elementCap di DB (FIRE/WATER/EARTH) sama dengan guest.getElement()
        if (!guest.getElement().equalsIgnoreCase(this.elementCap)) {
            throw new IllegalArgumentException("Habitat tidak cocok! Kamar ini untuk elemen " + this.elementCap);
        }

        this.currentGuest = guest;
        this.isOccupied = true;
        this.status = RoomStatus.OCCUPIED;
        guest.setRoomId(this.roomId);
    }

    public void checkOut() {
        if (this.currentGuest != null) {
            this.currentGuest.setRoomId(null);
        }
        this.currentGuest = null;
        this.isOccupied = false;
        this.status = RoomStatus.DIRTY;
    }

    public void markCleaned() {
        this.status = RoomStatus.AVAILABLE;
    }
}