<div align="center">
  <img src="src/main/resources/static/images/logo-cropped.png" alt="Monster Inn Logo" width="190">

  # MONSTER INN

  ### Pixel Front Desk System

  <p>
    Kelola penginapan antar-dimensi untuk naga api, air, dan tanah<br>
    melalui pengalaman web fantasy retro berbasis Spring Boot.
  </p>

  <p>
    <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17">
    <img src="https://img.shields.io/badge/Spring_Boot-4.0.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 4.0.5">
    <img src="https://img.shields.io/badge/MySQL-MariaDB-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
    <img src="https://img.shields.io/badge/Thymeleaf-Template-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf">
  </p>

  <p>
    <img src="https://img.shields.io/badge/UI-Pixel_Art-9C7AC4?style=flat-square" alt="Pixel Art">
    <img src="https://img.shields.io/badge/Audio-Web_Audio_API-D4A94A?style=flat-square" alt="Web Audio API">
    <img src="https://img.shields.io/badge/Security-BCrypt-3A2E28?style=flat-square" alt="BCrypt">
    <img src="https://img.shields.io/badge/Branch-demo-C9A8F0?style=flat-square" alt="Demo Branch">
  </p>

  <p>
    <a href="#-fitur-utama">Fitur</a> •
    <a href="#-quick-start">Quick Start</a> •
    <a href="#-akun-login-lokal">Login</a> •
    <a href="#-troubleshooting">Troubleshooting</a> •
    <a href="#-struktur-project">Struktur</a>
  </p>
</div>

---

Monster Inn adalah aplikasi web manajemen penginapan fantasi bergaya pixel-art. Aplikasi ini mensimulasikan pekerjaan resepsionis untuk tamu naga berelemen api, air, dan tanah, mulai dari registrasi, pengelolaan kamar, pemberian layanan, checkout, hingga laporan transaksi.

## ✨ Fitur Utama

| Fitur | Deskripsi |
|---|---|
| 🏰 Landing Fantasy | Landing page pixel-art dengan atmosfer penginapan magis. |
| 🔐 Login Staf | Autentikasi Spring Security dengan password BCrypt. |
| 📊 Dashboard | Ringkasan operasional penginapan dalam satu tampilan. |
| 🐲 Registrasi Monster | Check-in naga api, air, dan tanah. |
| 🛏️ Manajemen Kamar | Status kamar disesuaikan dengan habitat dan kondisi kamar. |
| ✨ Layanan Elemental | Perawatan khusus berdasarkan elemen monster. |
| 💰 Checkout | Perhitungan biaya kamar, layanan, pembayaran, dan kembalian. |
| 📜 Laporan | Rekap transaksi dan aktivitas operasional. |
| 🎵 BGM | Musik latar lokal dengan kontrol `MUSIC: ON/OFF`. |
| 🕹️ Native 8-bit SFX | Efek `click`, `success`, `error`, `magic`, dan `coin` melalui Web Audio API. |

## 🧰 Tech Stack

- Java 17
- Spring Boot 4.0.5
- Spring MVC
- Spring Data JPA / Hibernate
- Spring Security
- Thymeleaf
- MySQL atau MariaDB
- Maven Wrapper
- HTML, CSS, JavaScript, dan Web Audio API
- Lombok

## 📋 Prasyarat

Pastikan perangkat sudah memiliki:

- JDK 17 atau versi yang kompatibel
- MySQL/MariaDB yang aktif pada port `3306`
- Git

Maven tidak wajib dipasang secara global karena repository menyediakan Maven Wrapper.

## 🚀 Quick Start

### 1. Clone repository

```bash
git clone --branch demo https://github.com/zakverse/Monster-Inn.git
cd Monster-Inn
```

### 2. Jalankan MySQL

Konfigurasi lokal default:

```text
Host     : localhost
Port     : 3306
Database : monster_inn
Username : root
Password : kosong
```

URL JDBC memiliki parameter `createDatabaseIfNotExist=true`, sehingga database `monster_inn` dapat dibuat otomatis apabila user MySQL memiliki izin.

Jika perlu membuat database secara manual:

```sql
CREATE DATABASE IF NOT EXISTS monster_inn
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Hibernate akan membuat atau memperbarui tabel. Setelah itu, `data.sql` mengisi data awal kamar, layanan, dan akun admin.

### 3. Jalankan test

Windows:

```powershell
.\mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

### 4. Jalankan aplikasi

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Buka:

- Landing page: <http://localhost:8080>
- Login: <http://localhost:8080/login>

## 🔑 Akun Login Lokal

```text
Username : admin
Password : admin123
Role     : ADMIN
```

Password disimpan di database sebagai hash BCrypt, bukan plain text.

## ⚙️ Konfigurasi Environment

Konfigurasi database dapat diganti melalui environment variable:

| Variable | Default |
|---|---|
| `PORT` | `8080` |
| `MYSQL_URL` | `jdbc:mysql://localhost:3306/monster_inn?createDatabaseIfNotExist=true` |
| `MYSQLUSER` | `root` |
| `MYSQLPASSWORD` | kosong |

Contoh PowerShell:

```powershell
$env:MYSQL_URL="jdbc:mysql://localhost:3306/monster_inn"
$env:MYSQLUSER="root"
$env:MYSQLPASSWORD="password_mysql"
.\mvnw.cmd spring-boot:run
```

## 🧯 Troubleshooting

### MySQL aktif tetapi login gagal

Pastikan database dan tabel pengguna tersedia:

```sql
SHOW DATABASES LIKE 'monster_inn';
USE monster_inn;
SELECT username, role FROM users;
```

### Pesan `Unknown database 'monster_inn'`

Buat database secara manual menggunakan query pada bagian setup, kemudian restart aplikasi.

### Reset password admin

Reset hanya akun admin tanpa menghapus data lain:

```sql
USE monster_inn;

UPDATE users
SET password = '$2a$10$KjdVekRNLv/pJwOo1P73oO.Z3dQfYSNNoFWutVvp/NJI0vVcVR5Sq',
    role = 'ADMIN'
WHERE username = 'admin';
```

Hash tersebut telah diverifikasi untuk password `admin123`.

Jika akun admin belum ada:

```sql
INSERT INTO users (username, password, role)
VALUES (
    'admin',
    '$2a$10$KjdVekRNLv/pJwOo1P73oO.Z3dQfYSNNoFWutVvp/NJI0vVcVR5Sq',
    'ADMIN'
);
```

### Port 8080 sudah digunakan

Gunakan port lain:

```powershell
$env:PORT="8081"
.\mvnw.cmd spring-boot:run
```

## 🎧 Audio Landing Page

Background music dan SFX merupakan dua sistem terpisah:

- BGM menggunakan aset lokal dan mulai setelah interaksi pertama karena aturan autoplay browser.
- SFX dibuat langsung menggunakan `AudioContext`, `OscillatorNode`, dan `GainNode`.
- Preferensi toggle SFX disimpan pada `localStorage`.
- Tidak ada audio eksternal atau suara berhak cipta yang digunakan untuk native SFX.

## 🗂️ Struktur Project

```text
src/
├── main/
│   ├── java/monsterinn/
│   │   ├── config/         # Konfigurasi Spring Security
│   │   ├── controller/     # Controller MVC dan proses aplikasi
│   │   ├── model/          # Entity dan domain model
│   │   ├── repository/     # Spring Data JPA repository
│   │   └── service/        # UserDetailsService autentikasi
│   └── resources/
│       ├── static/         # CSS, JavaScript, gambar, logo, dan audio
│       ├── templates/
│       │   ├── layout/     # Layout dan sidebar
│       │   └── view/       # Landing, login, dashboard, dan halaman fitur
│       ├── application.properties
│       └── data.sql
└── test/                   # Automated tests
```

## 🛡️ Catatan Pengembangan

- Jangan menyimpan password database produksi langsung di repository.
- Gunakan environment variable untuk konfigurasi deployment.
- Jangan menghapus database untuk memperbaiki satu akun; gunakan query reset yang spesifik.
- Browser memerlukan interaksi pengguna sebelum BGM dan Web Audio API dapat diputar.

---

<div align="center">
  <strong>Monster Inn</strong><br>
  Dibangun sebagai proyek Pemrograman Berorientasi Objek dengan nuansa fantasy pixel-game.
</div>
