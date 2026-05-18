package monsterinn.modules.room.model; // Menentukan lokasi package dari class ini

import lombok.Getter; // Mengimpor anotasi Getter dari library Lombok

@Getter // Anotasi Lombok untuk membuatkan method getter (getLabel(), getCssClass()) secara otomatis
public enum RoomStatus { // Mendeklarasikan sebuah Enum (tipe data yang berisi konstanta tetap) bernama RoomStatus
    AVAILABLE("Available", "status-available"), // Konstanta untuk kamar yang tersedia, dengan label "Available" dan kelas CSS "status-available"
    OCCUPIED("Occupied", "status-occupied"), // Konstanta untuk kamar yang terisi, dengan label "Occupied" dan kelas CSS "status-occupied"
    DIRTY("Dirty", "status-dirty"), // Konstanta untuk kamar yang kotor, dengan label "Dirty" dan kelas CSS "status-dirty"
    MAINTENANCE("Maintenance", "status-maintenance"); // Konstanta untuk kamar dalam perbaikan, dengan label "Maintenance" dan kelas CSS "status-maintenance"

    private final String label; // Variabel konstan untuk menyimpan teks label dari status
    private final String cssClass; // Variabel konstan untuk menyimpan nama kelas CSS untuk tampilan UI

    RoomStatus(String label, String cssClass) { // Constructor dari enum untuk menginisialisasi nilai label dan cssClass
        this.label = label; // Memasukkan nilai parameter label ke variabel instance label
        this.cssClass = cssClass; // Memasukkan nilai parameter cssClass ke variabel instance cssClass
    }
}