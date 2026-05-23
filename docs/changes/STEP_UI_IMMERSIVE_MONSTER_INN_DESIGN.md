# STEP UI - Immersive Monster Inn Design

## Tujuan
Mengubah UI Monster Inn menjadi pengalaman pixel-art fantasy yang terasa seperti simulator resepsionis penginapan isekai, sambil mempertahankan route, controller, dan logic backend yang sudah ada.

## Referensi Desain
Desain mengikuti file referensi `C:/Users/HP/Downloads/index (2) (1).html`: dark RPG hero, magical inn atmosphere, portal transition, parchment letter, monster card Fire/Water/Earth yang lebih terang, feature cards, OOP pillars, dan storytelling PBO.

Landing page memakai struktur dan styling referensi secara langsung, dengan adaptasi route CTA dari `./monster-inn.html` menjadi `/login` agar sesuai aplikasi Spring Boot.

## File yang Diubah
- `src/main/resources/templates/modules/home/home.html` - landing page diganti mengikuti file referensi: sticky navbar, hero, moon, torch, monster parade, parchment letter, guest tiles, feature section, OOP pillars, final CTA portal, footer, dan transition overlay.
- `src/main/resources/templates/modules/auth/login.html` - halaman login disesuaikan menjadi portal receptionist dengan inn art, stars, dan form pixel yang konsisten.
- `src/main/resources/templates/modules/dashboard/dashboard.html` - dihubungkan ke shared CSS/JS agar mengikuti sistem visual dan reveal animation.
- `src/main/resources/templates/modules/registrasi/registrasi.html` - dihubungkan ke shared CSS/JS untuk konsistensi UI, hover, focus, dan responsiveness.
- `src/main/resources/templates/modules/room/status_kamar.html` - dihubungkan ke shared CSS/JS untuk card motion, scroll area, dan app shell responsive.
- `src/main/resources/templates/modules/layanan/layanan.html` - ditambahkan viewport dan shared CSS/JS untuk layout responsive dan motion.
- `src/main/resources/templates/modules/transaction/checkout.html` - ditambahkan viewport dan shared CSS/JS untuk invoice page yang lebih konsisten.
- `src/main/resources/templates/modules/report/laporan.html` - dihubungkan ke shared CSS/JS untuk card reveal dan responsive shell.
- `src/main/resources/static/css/monster-inn-ui.css` - design system, komponen visual, responsive behavior, dan animasi utama.
- `src/main/resources/static/js/monster-inn-ui.js` - navbar shrink, mobile menu, reveal-on-scroll, parallax, dan portal transition.
- `src/main/resources/templates/layout/monster-style.html` - fallback CSS inline untuk login dan halaman aplikasi agar UI tetap tampil saat CDN Tailwind atau static CSS belum termuat.
- `docs/changes/STEP_UI_IMMERSIVE_MONSTER_INN_DESIGN.md` - dokumentasi perubahan UI.

## Design System yang Dipakai
- Warna utama: `#0f0b14`, `#1e1825`, `#050208`, `#f4e8d0`, `#3a2e28`, `#d4a94a`, `#ffd96b`, `#e8734a`, `#6ba3c5`, `#85a67a`, `#9c7ac4`.
- Font: `Press Start 2P` untuk title, heading, badge, dan CTA; `VT323` untuk narasi retro; `Pixelify Sans` untuk teks umum.
- Spacing: section besar memakai padding responsif `clamp(...)`, kartu memakai padding 24px ke atas.
- Border: border pixel tebal 4-5px dengan warna wood dark.
- Shadow: box-shadow hard pixel seperti `6px 6px 0 #050208` dan variasi lebih besar untuk parchment.
- Komponen visual: star field, scanline, moon, portal ring, pixel inn silhouette, torch flame, parchment letter, elemental monster sprite, feature card, OOP rune card, dan footer dark wood.

## Animasi yang Ditambahkan
- Reveal-on-scroll untuk section dan card melalui IntersectionObserver.
- Smooth scroll untuk anchor navigation.
- Navbar shrink saat scroll.
- Hover animation pada card, CTA, input, room box, element card, dan service item.
- Floating moon dan subtle parallax pada hero art.
- Flickering torch/flame pada inn dan window.
- Monster idle bobbing pada kartu Fire/Water/Earth.
- Portal spin/pulse pada hero dan final CTA.
- Portal transition overlay saat klik CTA utama menuju `/login`.
- Support `prefers-reduced-motion` untuk menonaktifkan animasi besar bagi user yang membutuhkannya.

## Responsiveness
Landing page menggunakan grid yang berubah menjadi satu kolom pada tablet/mobile. Navbar berubah menjadi menu toggle pada layar kecil. App internal memakai shell responsive: sidebar menjadi area atas, menu berubah menjadi grid, dan konten utama menyesuaikan padding serta grid pada mobile.

Halaman login dan app internal juga mendapat fallback utility CSS lokal agar layout tetap terbaca tanpa bergantung penuh pada Tailwind CDN.

## Batasan
Perubahan fokus pada UI, styling, motion, dan pengalaman visual. Tidak ada perubahan pada logic backend, controller route, repository, model, atau konfigurasi build/package.

## Cara Menjalankan
1. Pastikan database MySQL `monster_inn` tersedia.
2. Sesuaikan `spring.datasource.username` dan `spring.datasource.password` di `src/main/resources/application.properties`.
3. Jalankan project:

```bash
mvn spring-boot:run
```

4. Buka aplikasi di:

```text
http://localhost:8080
```

## Next Step
- Polishing halaman app internal satu per satu agar setiap form/table memiliki microcopy fantasy yang lebih konsisten.
- Menambahkan asset pixel-art bitmap khusus jika file referensi final sudah tersedia.
- Menambahkan visual QA dengan screenshot desktop/mobile setelah server berjalan.
- Merapikan teks lama yang masih mengalami encoding/mojibake di beberapa template internal.
