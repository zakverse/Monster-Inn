package monsterinn.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // Nama tabel di database
public class User {

    // --- ATRIBUT (ENKAPSULASI) ---
    // Sesuai proposal, atribut harus diproteksi (private)
    @Id
    private String username;
    
    private String password;
    
    private String role;

    // --- CONSTRUCTOR ---
    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- GETTER & SETTER ---
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Diperlukan oleh Spring Security untuk proses autentikasi
    public String getPassword() {
        return this.password;
    }

    // --- METHOD UTAMA ---
    /**
     * Memvalidasi input dari layar login dengan data kredensial objek ini.
     * Mengembalikan true jika cocok, false jika tidak.
     */
    public boolean validate(String inputUsername, String inputPassword) {
        if (this.username == null || this.password == null) {
            return false;
        }
        // Validasi mencocokkan username dan password
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }
}
