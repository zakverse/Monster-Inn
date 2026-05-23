# STEP UI - Repair, Animation, and Java Integration

## Tujuan
Memperbaiki UI Monster Inn agar rapi, konsisten, responsive, mengikuti design system pixel-art dark isekai RPG. Memperbaiki bug routing, CSRF, form, sidebar overlap, dan data seeding. Memastikan logic Java/Spring Boot tersambung rapi ke UI Thymeleaf.

## Masalah Awal
Berikut masalah yang ditemukan saat audit Phase 1:

### Layout
- Sidebar (fixed 260px) menutupi konten `<main>` pada 5 dari 6 halaman app-shell (semua kecuali dashboard)
- TailwindCSS CDN dimuat redundan di 6 template, berpotensi konflik dengan CSS design system yang sudah dibuat
- Mobile responsive sidebar tidak dihandle saat breakpoint < 1100px (konten terpotong)

### Tombol & Interaksi
- Tombol "KONFIRMASI PESANAN" di layanan.html tidak berfungsi karena CSRF blocking fetch POST
- Tombol "PROSES CHECKOUT" di checkout.html tidak berfungsi karena CSRF blocking fetch POST
- Redirect setelah checkout ke `/room` (route tidak ada → 404), seharusnya `/room/status`

### Routing & Form
- Form registrasi sudah tersambung ke controller tapi flash message success/error tidak ditampilkan di UI
- Form clean room sudah tersambung tapi flash message success tidak ditampilkan
- API endpoints `/service/add` dan `/api/checkout/confirm/{id}` diblokir CSRF

### Data
- `data.sql` kosong — tidak ada room yang ter-seed di database
- Aplikasi dimulai dengan 0 room → semua fitur registrasi, layanan, checkout tidak bisa digunakan
- `application.properties` tidak memiliki konfigurasi untuk menjalankan `data.sql` setelah Hibernate create table

### Serialisasi
- Room object di-serialize ke JSON untuk registrasi template tapi field `currentGuest` (OneToOne ke Monster) berpotensi menyebabkan circular serialization error

## File yang Diubah

| File | Alasan |
|------|--------|
| `SecurityConfig.java` | Menambahkan CSRF exception untuk `/api/**` dan `/service/**` |
| `application.properties` | Menambahkan `spring.sql.init.mode=always` dan `spring.jpa.defer-datasource-initialization=true` |
| `data.sql` | Mengisi 9 room (3 Fire, 3 Water, 3 Earth) dengan INSERT ON DUPLICATE KEY |
| `Room.java` | Menambahkan `@JsonIgnoreProperties({"currentGuest"})` |
| `dashboard.html` | Hapus TailwindCSS CDN, tambah margin-left 260px, replace arbitrary class dengan inline style |
| `status_kamar.html` | Hapus TailwindCSS CDN, tambah margin-left 260px, tambah flash message display, tambah empty state, tambah bg-maintenance |
| `registrasi.html` | Hapus TailwindCSS CDN, tambah margin-left 260px, tambah flash success/error display, fix element selection (UPPERCASE) |
| `layanan.html` | Hapus TailwindCSS CDN, tambah margin-left 260px, replace arbitrary class dengan inline style |
| `checkout.html` | Hapus TailwindCSS CDN, tambah margin-left 260px, fix redirect `/room` → `/room/status`, tambah error handling |
| `laporan.html` | Hapus TailwindCSS CDN, tambah margin-left 260px, tambah emoji di chart labels |
| `monster-inn-ui.css` | Tambah `margin-left: 0 !important` di breakpoint 1100px dan 640px |

## Perubahan UI

### Layout
- **Sidebar offset**: Semua halaman app-shell sekarang memiliki `margin-left: 260px` di `<main>` yang direset ke `0` pada breakpoint mobile
- **TailwindCSS CDN dihapus**: Semua 6 halaman tidak lagi memuat `cdn.tailwindcss.com`. Semua styling menggunakan design system dari `monster-style.html` dan `monster-inn-ui.css`
- **Arbitrary Tailwind classes diganti inline styles**: Class seperti `text-[#ffd96b]`, `bg-[#3a2e28]`, dll diganti dengan `style="color: #ffd96b;"` untuk menghindari dependensi pada Tailwind CDN
- **Mobile responsive**: CSS media query di 1100px dan 640px sekarang mereset margin-left sidebar

### Konsistensi Visual
- Semua halaman menggunakan color palette yang sama: bg-night #0f0b14, gold #ffd96b, fire #e8734a, water #6ba3c5, earth #85a67a, magic #9c7ac4
- Font tetap pixel-art: Press Start 2P, VT323, Pixelify Sans
- Card menggunakan `pixel-card` class dengan border solid #3a2e28 dan box-shadow pixel

### Flash Messages
- Registrasi: Menampilkan success (hijau ✦) dan error (merah ⚠) flash message dengan animasi fadeIn
- Status Kamar: Menampilkan success flash message setelah clean room

## Animasi yang Ditambahkan/Diperbaiki

### Sudah Ada (Dipertahankan)
- ✅ Reveal-on-scroll via IntersectionObserver (`monster-inn-ui.js`)
- ✅ Portal spin animation (`mi-portal-spin`)
- ✅ Flame/window flicker animation
- ✅ Star twinkle animation
- ✅ Button hover translate + box-shadow effect
- ✅ Card hover transform + border-color change
- ✅ Portal transition overlay saat navigasi dari landing page
- ✅ Moon parallax di landing page

### Ditambahkan/Diperbaiki
- ✨ Flash message fadeIn animation (`@keyframes flashIn`)
- ✨ Hover effects pada tombol CLEAN, SUBMIT, EXPORT via inline `onmouseover`/`onmouseout`
- ✨ Element card icon color transition saat dipilih di registrasi
- ✨ Service item hover dengan translate + shadow effect

### Aksesibilitas
- ✅ `@media (prefers-reduced-motion: reduce)` sudah ada di `monster-inn-ui.css` dan `monster-style.html` — semua animasi di-disable

## Integrasi Java ke UI

### Controller → Template Mapping

| Controller | Route | Template | Data Model |
|-----------|-------|----------|------------|
| HomeController | `GET /` | `home/home.html` | (none) |
| AuthController | `GET /login` | `auth/login.html` | `error`, `logoutMsg` |
| DashboardController | `GET /dashboard` | `dashboard/dashboard.html` | `totalKamar`, `tersedia`, `terisi`, `occupancy`, `activeGuests` |
| RoomController | `GET /room/status` | `room/status_kamar.html` | `fireRooms`, `waterRooms`, `earthRooms`, `allRooms` |
| RoomController | `POST /room/clean/{id}` | → redirect `/room/status` | flash: `successMsg` |
| RegistrasiController | `GET /registrasi` | `registrasi/registrasi.html` | `availableRooms` |
| RegistrasiController | `POST /registrasi/simpan` | → redirect `/registrasi` | flash: `successMsg`/`errorMsg` |
| LayananController | `GET /layanan` | `layanan/layanan.html` | `activeGuests`, `allServices` |
| LayananController | `POST /service/add` | JSON response | (JSON body input) |
| CheckoutController | `GET /checkout` | `transaction/checkout.html` | `activeGuests` |
| CheckoutController | `GET /api/checkout-info/{id}` | JSON response | name, roomId, stayDays, costs |
| CheckoutController | `POST /api/checkout/confirm/{id}` | JSON response | (confirmation) |
| LaporanController | `GET /laporan` | `report/laporan.html` | `revenue`, `totalOrders`, `totalMonsters`, `firePct`, `waterPct`, `earthPct`, `history`, `allServices` |

### Form Connections Verified
- ✅ Login form → `POST /login` (Spring Security) → redirect `/dashboard`
- ✅ Logout form → `POST /logout` (Spring Security) → redirect `/login?logout`
- ✅ Registrasi form → `POST /registrasi/simpan` → redirect `/registrasi`
- ✅ Clean room form → `POST /room/clean/{id}` → redirect `/room/status`
- ✅ Service add → `POST /service/add` (JSON fetch, CSRF exempted)
- ✅ Checkout confirm → `POST /api/checkout/confirm/{id}` (JSON fetch, CSRF exempted)

## Bug yang Diperbaiki

| # | Bug | Severity | Fix |
|---|-----|----------|-----|
| 1 | CSRF blocks POST fetch to `/service/add` | 🔴 CRITICAL | Added `.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/service/**"))` |
| 2 | CSRF blocks POST fetch to `/api/checkout/confirm/{id}` | 🔴 CRITICAL | Same as above |
| 3 | Checkout redirect to `/room` (404) | 🔴 CRITICAL | Changed to `/room/status` |
| 4 | Sidebar overlaps content on 5 pages | 🔴 CRITICAL | Added `margin-left: 260px` to all `<main>` |
| 5 | No rooms in database (data.sql empty) | 🔴 CRITICAL | Added 9 rooms: F-101/102/103, W-201/202/203, E-301/302/303 |
| 6 | data.sql not executed by Spring Boot | 🟡 MEDIUM | Added `spring.sql.init.mode=always` + `defer-datasource-initialization=true` |
| 7 | Room JSON serialization circular ref | 🟡 MEDIUM | Added `@JsonIgnoreProperties({"currentGuest"})` |
| 8 | Flash messages not displayed (registrasi) | 🟡 MEDIUM | Added success/error flash message HTML |
| 9 | Flash messages not displayed (room clean) | 🟡 MEDIUM | Added success flash message HTML |
| 10 | Element selection sends lowercase to Java | 🟡 MEDIUM | Changed to `.toUpperCase()` |
| 11 | TailwindCSS CDN conflicts with design system | 🟢 LOW | Removed CDN from all 6 templates |
| 12 | Mobile sidebar margin not reset | 🟢 LOW | Added `margin-left: 0 !important` at 1100px and 640px |
| 13 | bg-maintenance CSS class missing | 🟢 LOW | Added in status_kamar.html inline styles |

## Cara Menjalankan

### Prerequisites
- Java 17+
- Maven
- MySQL running on `localhost:3306`
- Database `monster_inn` sudah dibuat:
  ```sql
  CREATE DATABASE IF NOT EXISTS monster_inn;
  ```

### Command Terminal
```bash
cd "d:\Semester 4\PBO\Tubes\Monster Inn"
.\mvnw spring-boot:run
```
Atau:
```bash
mvn spring-boot:run
```

### URL yang harus dibuka
- Landing Page: http://localhost:8080/
- Login: http://localhost:8080/login

### Akun Login
- **Username**: `admin`
- **Password**: `admin123`

### Halaman yang harus dites
Setelah login, semua halaman diakses melalui sidebar:
1. http://localhost:8080/dashboard — Overview
2. http://localhost:8080/room/status — Status Kamar
3. http://localhost:8080/registrasi — Registrasi Tamu
4. http://localhost:8080/layanan — Layanan Tamu
5. http://localhost:8080/checkout — Checkout & Tagihan
6. http://localhost:8080/laporan — Laporan Harian

## Manual Test Checklist

- [ ] Landing page (/) terbuka dengan animasi pixel-art, portal, monster parade
- [ ] Navbar landing page sticky dan semua link anchor scroll dengan smooth
- [ ] Tombol "MASUK PENGINAPAN" navigasi ke /login dengan transition overlay
- [ ] Login page terbuka dan form bisa disubmit (admin/admin123)
- [ ] Setelah login, redirect ke /dashboard
- [ ] Dashboard menampilkan data: Total Kamar = 9, Tersedia = 9, Terisi = 0, Occupancy = 0%
- [ ] Sidebar semua link bisa diklik dan navigasi benar
- [ ] Status Kamar menampilkan 3 zone (Fire/Water/Earth) masing-masing 3 kamar
- [ ] Semua kamar awalnya berstatus AVAILABLE/VACANT
- [ ] Registrasi: pilih elemen → kamar terfilter sesuai elemen
- [ ] Registrasi: submit form → flash message success muncul
- [ ] Setelah registrasi, dashboard menampilkan tamu di tabel
- [ ] Status Kamar menampilkan kamar yang terisi (OCCUPIED + nama tamu)
- [ ] Layanan: pilih tamu aktif → menu layanan muncul sesuai elemen
- [ ] Layanan: tambah item ke keranjang → total terupdate
- [ ] Layanan: konfirmasi pesanan → alert sukses
- [ ] Checkout: pilih tamu → invoice muncul dengan biaya benar
- [ ] Checkout: proses checkout → redirect ke /room/status, kamar berubah jadi DIRTY
- [ ] Status Kamar: tombol CLEAN pada kamar DIRTY → kamar kembali AVAILABLE
- [ ] Laporan: menampilkan pendapatan, total order, popularitas elemen
- [ ] Logout button → kembali ke login page
- [ ] Responsive: halaman tidak overflow horizontal
- [ ] Tidak ada error console browser yang kritis

## Batasan

### Belum Dikerjakan (Butuh Tahap Lanjutan)
1. **User.java** masih kosong — belum ada custom user management (menggunakan default Spring Security user)
2. **Transaction.java** masih kosong — checkout logic langsung di controller tanpa model Transaction
3. **ReportManager.java** masih kosong — report logic langsung di controller
4. **Drag-and-drop check-in** seperti yang disebutkan di landing page belum diimplementasi
5. **OOP Inspector Live Panel** belum diimplementasi
6. **8-Bit SFX** hanya ada di landing page transition, belum di halaman lain
7. **Room management** (tambah/hapus kamar) belum ada UI-nya — hanya bisa via database
8. **StayDays increment** belum ada mekanisme otomatis — masih default 1 hari
9. **Landing page** home.html tetap menggunakan inline CSS (1850 lines) — tidak diubah karena sudah berfungsi baik dan standalone
