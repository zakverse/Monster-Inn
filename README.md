# 🏨 Monster Inn: Pixel Front Desk System

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)

**Monster Inn** adalah aplikasi manajemen penginapan bertema fantasi-retro (Pixel Art) yang dibangun menggunakan **Spring Boot**. Aplikasi ini dirancang untuk mengelola reservasi tamu monster (Naga) berdasarkan elemen mereka (Api, Air, Tanah) dengan sistem perhitungan tagihan otomatis.

---

## ✦ Fitur Utama

-   **Dashboard Habitat**: Monitoring visual status kamar (Ready, Terisi, Kotor) dengan icon dinamis.
-   **Registrasi Tamu**: Check-in naga dengan perhitungan deposit dan durasi menginap.
-   **Layanan Spesifik**: Sistem pesanan tambahan (Magma Injection, Deep Sea Mist, dll) berdasarkan elemen monster.
-   **Finalisasi Tagihan**: Fitur checkout otomatis yang menghitung total biaya, tambahan layanan, dan kembalian.
-   **Laporan Harian**: Rekapitulasi omset harian dan statistik popularitas klan monster.

---

## 🛠️ Tech Stack

-   **Backend**: Java 21, Spring Boot, Spring Data JPA.
-   **Database**: MySQL.
-   **Frontend**: Thymeleaf, Tailwind CSS, FontAwesome.
-   **Tools**: Maven, Lombok, Pixelify Sans Fonts.

---

## 🚀 Cara Menjalankan Projek

1.  **Clone Repository**
    ```bash
    git clone [https://github.com/zakverse/Monster-Inn.git](https://github.com/zakverse/Monster-Inn.git)
    cd Monster-Inn
    ```

2.  **Konfigurasi Database**
    -   Buat database bernama `monster_inn` di MySQL.
    -   Sesuaikan `username` dan `password` database di file `src/main/resources/application.properties`.

3.  **Setup Data Awal (Seeder)**
    -   Impor file `data.sql` (jika ada) atau jalankan aplikasi sekali agar Hibernate membuat tabel secara otomatis.
    -   Gunakan query SQL manual yang disediakan untuk mengisi daftar kamar dan layanan awal.

4.  **Running Aplikasi**
    ```bash
    mvn spring-boot:run
    ```
    Buka di browser: `http://localhost:8080`

---

## 📂 Struktur Folder

```text
src/main/java/monsterinn/modules/
├── auth/         # Autentikasi & Login
├── dashboard/    # Ringkasan Sistem
├── monster/      # Model & Logic Tamu (Naga)
├── room/         # Manajemen Kamar & Status
├── service/      # Modul Layanan Tambahan
├── transaction/  # Logic Checkout & Pembayaran
└── report/       # Rekapitulasi & Laporan Harian