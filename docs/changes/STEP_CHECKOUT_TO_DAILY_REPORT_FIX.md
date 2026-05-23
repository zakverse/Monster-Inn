# STEP - Checkout to Daily Report Fix

## Masalah
Transaksi checkout yang berhasil tidak muncul di halaman Laporan Harian (`/laporan`). Setelah checkout, monster aktif dihapus dari database sehingga laporan yang sebelumnya membaca data monster aktif menjadi kosong atau tidak mencatat histori checkout.

## Penyebab
Penyebab teknis yang ditemukan:
- `CheckoutController` belum membuat object `Transaction`.
- Checkout langsung memanggil `room.checkOut()`, menghapus service request, dan menghapus monster tanpa menyimpan snapshot transaksi.
- `Transaction` masih berupa class kosong, sehingga tidak ada `calculateTotal()` atau `processPayment()`.
- `LaporanController` menghitung laporan dari `MonsterRepository.findAll()` dan `ServiceRepository.findAll()`, bukan dari histori checkout.
- `ReportManager` masih kosong, sehingga tidak ada sumber data bersama antara checkout dan laporan.

## File yang Diubah
- `src/main/java/monsterinn/modules/transaction/model/Transaction.java` - menambahkan model transaksi checkout, snapshot monster/room, total tagihan, pembayaran, kembalian, `calculateTotal()`, dan `processPayment()`.
- `src/main/java/monsterinn/modules/report/model/ReportManager.java` - menambahkan Spring singleton in-memory history untuk menyimpan transaksi sukses dan menghitung laporan.
- `src/main/java/monsterinn/modules/transaction/controller/CheckoutController.java` - membuat transaksi saat checkout, memvalidasi pembayaran, menyimpan transaksi sukses ke `ReportManager`, lalu melakukan checkout room.
- `src/main/java/monsterinn/modules/report/controller/LaporanController.java` - membaca data laporan dari `ReportManager`, bukan monster aktif.
- `src/main/resources/templates/modules/transaction/checkout.html` - menambahkan input uang pembayaran dan mengirim nominal ke API checkout.
- `src/test/java/monsterinn/modules/report/model/ReportManagerTest.java` - menambahkan unit test untuk alur transaksi sukses/gagal, revenue, recent transactions, dan status room.
- `docs/changes/STEP_CHECKOUT_TO_DAILY_REPORT_FIX.md` - dokumentasi perubahan.

## Perubahan Logic
Alur baru:
1. User memilih monster di halaman checkout.
2. UI mengambil info tagihan dari `/api/checkout-info/{guestId}`.
3. User mengisi uang pembayaran.
4. `CheckoutController` mengambil monster dan room dari repository.
5. `Transaction` dibuat dari snapshot monster dan room.
6. `calculateTotal()` dan `processPayment()` dijalankan.
7. Jika pembayaran kurang, response error dikembalikan, transaksi tidak masuk laporan, room tidak checkout.
8. Jika pembayaran cukup, room menjalankan `checkOut()` sehingga status menjadi `DIRTY`.
9. Service request dan monster aktif dibersihkan.
10. Transaksi sukses disimpan ke `ReportManager`.
11. UI redirect ke `/laporan`.

## Integrasi Controller
`CheckoutController` dan `LaporanController` sekarang memakai sumber data yang sama, yaitu bean singleton `ReportManager`.

`CheckoutController` menulis transaksi sukses ke `ReportManager`, sedangkan `LaporanController` membaca:
- daftar transaksi;
- transaksi terbaru;
- total revenue;
- total transaksi;
- total monster checkout;
- popularitas elemen Fire/Water/Earth.

## Perubahan UI
Tidak ada redesign besar. Perubahan kecil hanya pada `checkout.html`:
- input `Uang Pembayaran`;
- error message jika pembayaran kurang;
- redirect sukses ke `/laporan`.

`laporan.html` tetap mempertahankan style Monster Inn dan membaca model lama (`revenue`, `totalOrders`, `totalMonsters`, `history`) yang sekarang diisi dari transaksi checkout. Controller juga mengirim alias model baru seperti `totalRevenue`, `totalTransactions`, `recentTransactions`, dan `elementPopularity`.

## Testing
Testing otomatis yang ditambahkan:
- transaksi sukses masuk report;
- transaksi gagal ditolak dari report;
- total revenue bertambah sesuai total transaksi;
- recent transactions mengembalikan transaksi terbaru;
- room berubah menjadi `DIRTY` setelah checkout sukses.

Percobaan menjalankan `.\mvnw.cmd test` belum berhasil di environment ini karena Maven wrapper gagal start dengan error:

```text
Cannot start maven from wrapper
```

Manual test yang perlu dilakukan:
1. Jalankan aplikasi.
2. Registrasi monster dan check-in ke room.
3. Tambahkan layanan jika diperlukan.
4. Buka `/checkout`.
5. Pilih monster, isi pembayaran cukup, lalu checkout.
6. Pastikan diarahkan ke `/laporan`.
7. Pastikan total pendapatan, total transaksi, total monster, log transaksi, dan popularitas elemen bertambah.
8. Buka `/room/status` dan pastikan kamar menjadi `DIRTY`.
9. Ulangi dengan pembayaran kurang dan pastikan transaksi tidak masuk laporan serta room tidak checkout.

## Batasan
Perubahan fokus pada bug checkout tidak masuk laporan. Tidak ada redesign besar, tidak mengubah Auth/Login, tidak mengubah SecurityConfig, tidak membuat database baru, dan tidak menghapus fitur yang sudah berjalan.

## Next Step
- Jika histori transaksi harus bertahan setelah aplikasi restart, jadikan `Transaction` entity JPA dan simpan ke database.
- Tambahkan pesan sukses/error berbasis flash message untuk flow non-AJAX.
- Tambahkan integration test controller saat konfigurasi test database sudah stabil.
