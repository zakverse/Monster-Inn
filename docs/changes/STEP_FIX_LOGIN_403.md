# STEP: Fix Login 403 Forbidden

**Tanggal:** 2026-06-24  
**Branch:** `fix-login-403-forbidden`  
**Status:** Selesai — belum di-commit

---

## 1. Penyebab 403 Forbidden

Halaman login (`GET /login`) menghasilkan **Whitelabel Error Page 403 Forbidden** karena:

### Penyebab Utama: Endpoint `/error` Tidak Diizinkan

Spring Boot 4.0.5 menggunakan **Spring Security 7.x** yang lebih ketat dalam penanganan error.
Ketika terjadi error apapun pada request unauthenticated (misalnya resource tidak ditemukan,
atau error saat rendering), Spring Boot mem-forward request ke endpoint `/error`.

**Masalahnya:** Endpoint `/error` **tidak ada** di daftar `permitAll()` pada `SecurityConfig.java`.
Akibatnya, Spring Security memblokir akses ke `/error` untuk user yang belum login,
dan menampilkan 403 Forbidden sebagai gantinya.

### Penyebab Tambahan: CSRF Token Tidak Eksplisit

Meskipun Thymeleaf `th:action` otomatis menyertakan CSRF token, form login tidak memiliki
hidden input CSRF token eksplisit sebagai lapisan keamanan tambahan untuk Spring Security 7.x.

---

## 2. File yang Diubah

| File | Jenis Perubahan |
|------|-----------------|
| `src/main/java/monsterinn/config/SecurityConfig.java` | Tambah `/error`, `/audio/**`, `/sounds/**`, `/favicon.ico` ke `permitAll()` |
| `src/main/resources/templates/view/login.html` | Tambah hidden input CSRF token eksplisit di dalam form login |
| `docs/changes/STEP_FIX_LOGIN_403.md` | Dokumentasi ini |

---

## 3. Perbaikan yang Dilakukan

### SecurityConfig.java

**Sebelum:**
```java
.requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
```

**Sesudah:**
```java
.requestMatchers("/", "/login", "/error",
    "/css/**", "/js/**", "/images/**", "/audio/**", "/sounds/**", "/favicon.ico").permitAll()
```

**Penjelasan:**
- `/error` — Endpoint error bawaan Spring Boot. **Wajib** diizinkan agar error handling berfungsi untuk user yang belum login.
- `/audio/**`, `/sounds/**` — Asset audio untuk musik dan SFX (saat ini audio ada di `/images/audio/`, tapi ini sebagai antisipasi jika dipindah).
- `/favicon.ico` — Ikon browser di root static.

### login.html

**Ditambahkan** di dalam tag `<form>`:
```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />
```

**Penjelasan:** Ini memastikan CSRF token selalu tersedia di form login secara eksplisit,
tidak hanya bergantung pada auto-include dari `th:action`.

---

## 4. Diagnosis Penyebab

| Kemungkinan Penyebab | Status |
|----------------------|--------|
| **SecurityConfig — `/error` tidak di-permit** | ✅ **PENYEBAB UTAMA** |
| CSRF token tidak ada di form | ⚠️ Ditambahkan sebagai pengamanan tambahan |
| Form action salah | ❌ Sudah benar: `th:action="@{/login}"` method="post" |
| Input name salah | ❌ Sudah benar: `name="username"`, `name="password"` |
| JavaScript menghalangi submit | ❌ Tidak ada interference — JS hanya untuk SFX, musik, dan animasi |
| Static asset diblokir | ❌ `/css/**`, `/js/**`, `/images/**` sudah di-permit |

---

## 5. Cara Testing Login

1. **Jalankan aplikasi:**
   ```bash
   ./mvnw.cmd spring-boot:run
   ```

2. **Buka browser:**
   ```
   http://localhost:8080/login
   ```

3. **Verifikasi:**
   - [ ] Halaman login muncul (bukan Whitelabel 403)
   - [ ] CSS, gambar, font Google Fonts ter-load
   - [ ] Animasi pixel art Inn dan bintang berjalan
   - [ ] Musik toggle button muncul di kanan bawah
   - [ ] Masukkan username salah → pesan error tampil
   - [ ] Masukkan `admin` / `admin123` → redirect ke `/dashboard`
   - [ ] Dashboard terbuka normal
   - [ ] Logout → kembali ke login dengan pesan sukses

---

## 6. Konfirmasi: UI / Musik / SFX Tidak Dihapus

✅ **Login UI design** — Pixel art Inn, moon, stars, animasi, responsive layout **TIDAK DIUBAH**  
✅ **Musik** — `monster-inn-music.js` dan audio files **TIDAK DIUBAH**  
✅ **SFX** — `monster-inn-ui.js` SFX logic **TIDAK DIUBAH**  
✅ **CSS** — `monster-inn-ui.css` **TIDAK DIUBAH**  
✅ **Loading screen** — Tidak ada loading screen di login, tidak terpengaruh  
✅ **Music toggle button** — Tetap ada di login page  

---

## 7. Konfirmasi: Backend Business Logic Tidak Diubah

✅ Model — Tidak diubah  
✅ Repository — Tidak diubah  
✅ Service — Tidak diubah  
✅ Controller (Auth, Dashboard, Home, Room, Service, Transaction, Report, Registrasi) — Tidak diubah  
✅ `data.sql` — Tidak diubah  
✅ `application.properties` — Tidak diubah  
✅ Database — Tidak terpengaruh  
