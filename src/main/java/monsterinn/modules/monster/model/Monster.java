package monsterinn.modules.monster.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data// Lombok untuk generate getter, setter, toString, equals, hashCode
@NoArgsConstructor // Lombok untuk generate constructor tanpa argumen
@Entity// Kelas abstrak untuk monster, dengan strategi inheritance SINGLE_TABLE
@Table(name = "monsters") // Nama tabel untuk menyimpan semua jenis monster
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Semua jenis monster disimpan dalam satu tabel dengan kolom discriminator
@DiscriminatorColumn(name = "element_type")
public abstract class Monster {

    @Id
    protected String idMonster; // ID unik untuk setiap monster (misal: "M001", "M002", "M003")
    protected String name; // Nama monster (misal: "Golem", "Salamander", "Leviathan")
    protected double baseCost; // Biaya dasar per hari
    protected double extraCost = 0; // Akumulasi Layanan Kamar
    protected String roomId; // ID kamar yang ditempati monster (relasi ke modul room)
    protected int stayDays = 1; // Lama menginap dalam hari (default 1 hari)
    protected double prepaidAmount = 0; // Jumlah uang muka yang sudah dibayar oleh pelanggan

    // List untuk menyimpan catatan layanan (Service Log)
    @ElementCollection
    protected List<String> serviceLog = new ArrayList<>();

    // Constructor Dasar
    public Monster(String idMonster, String name, double baseCost) {
        this.idMonster = idMonster;
        this.name = name;
        this.baseCost = baseCost;
    }

    // --- 4 METHOD ---

    // 1.getDetail(): Output informasi dasar
    public String getDetail() {
        return String.format("ID: %s | Nama: %s | Base: Rp%.2f", idMonster, name, baseCost);
    }

    // 2. pushService(): Menambah biaya layanan (Koneksi ke modul Dzaki)
    public void pushService(String message, double fee) {
        this.extraCost += fee;
        this.serviceLog.add(message + " (Rp" + fee + ")");
    }

    // 3. validateState(): Validasi data sebelum simpan ke DB
    public boolean validateState() {
        return idMonster != null && !idMonster.isEmpty() 
            && name != null && !name.isEmpty() 
            && baseCost > 0;
    }

    // 4. calculateTotalCost(): Method Polimorfik (Abstract)
    public abstract double calculateTotalCost();

    // 5. getElement(): Method Polimorfik (Abstract)
    public abstract String getElement();
}