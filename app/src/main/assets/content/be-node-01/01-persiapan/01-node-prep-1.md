---
id: node-prep-1
courseId: be-node-01
order: 1
title: "Cara Belajar Node.js: Metode Baca & Tebak Output"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Memahami metode belajar 'Baca & Tebak Output' untuk melatih intuisi server"
  - "Mengetahui batasan lingkungan belajar di aplikasi (tanpa server runner atau terminal)"
  - "Mengenal format penulisan kode JavaScript sisi server dan blok verifikasi output"
codeLanguage: javascript
codeSnippet: |
  // Contoh pembacaan alur kode server
  const namaServer = "CodeAcademy API";
  const status = "Aktif";

  console.log(`[SYSTEM] ${namaServer} berjalan dengan status: ${status}`);
---

# Cara Belajar Node.js: Metode Baca & Tebak Output

Selamat datang di kelas **Node.js Fundamentals**! Jika di kelas JavaScript sebelumnya kamu berfokus pada apa yang terjadi di browser (tampilan visual, tombol diklik, manipulasi DOM), sekarang kita melangkah ke dunia **Back-End Development**: mesin tak kasat mata di balik layar yang melayani data, mengelola file di komputer server, dan menangani ribuan permintaan pengguna secara bersamaan.

![Metode Belajar Baca dan Tebak Output](asset:images/be-node-01/metode-baca-tebak-output.png)

---

## 1. Lingkungan Belajar di Aplikasi Ini

Sebelum kita melangkah lebih jauh, mari kita pahami bersama lingkungan belajar di aplikasi CodeAcademy ini:

> **PENTING UNTUK DIINGAT:**
> Aplikasi CodeAcademy memiliki playground interaktif untuk HTML, CSS, dan JavaScript browser. Namun, aplikasi ini **TIDAK MEMILIKI runtime Node.js, terminal Linux, maupun manajer paket npm**. 
>
> Mengapa? Karena Node.js membutuhkan akses langsung ke sistem operasi fisik komputer (membuka port jaringan internet, mengakses hard drive lokal, membaca sinyal CPU), yang tidak bisa dan tidak aman jika dijalankan langsung di dalam sandbox WebView aplikasi mobile.

Oleh karena itu, kelas ini dirancang khusus dengan metode **BACA & TEBAK OUTPUT (*Mental Execution*)**. 

Kamu tidak perlu menekan tombol "Jalankan" atau membuka terminal di aplikasi. Tugas utamamu adalah melatih otakmu menjadi mesin eksekusi: membaca kode, memprediksi alur data serta urutan waktu eksekusi (*event loop*), lalu mencocokkannya dengan blok output yang telah disediakan.

---

## 2. Format Contoh Kode & Output

Setiap contoh program di kelas ini akan disajikan dalam dua blok berdampingan: blok kode sumber dan blok hasil terminal yang persis seperti ketika dieksekusi di komputer nyata.

Perhatikan contoh berikut:

```javascript
const serverId = "SRV-001";
const port = 8080;

console.log("=== MEMULAI SERVICE ===");
console.log(`Mengikat alamat ke port: ${port}`);
console.log(`ID Server terdaftar: ${serverId}`);
console.log("Status: Siap menerima request.");
```

Output:
```
=== MEMULAI SERVICE ===
Mengikat alamat ke port: 8080
ID Server terdaftar: SRV-001
Status: Siap menerima request.
```

Saat membaca contoh seperti di atas:
1. Bacalah baris demi baris dari atas ke bawah.
2. Perhatikan variabel yang didefinisikan dan urutan pemanggilan fungsi `console.log`.
3. Bayangkan apa yang dicetak ke layar terminal sebelum kamu melihat bagian `Output:`.

---

## 3. Perbedaan Potongan Kode Murni vs API Node.js

Selama belajar di kelas ini, kamu akan menemui dua jenis kode:
- **JavaScript Murni**: Logika kondisional, manipulasi string, array, object, dan kalkulasi angka tanpa API khusus server. Potongan kode murni seperti ini bisa kamu coba secara mandiri di Playground JavaScript pada kelas *JavaScript Fundamentals* (`fe-js-03`).
- **Kode Khusus Node.js**: Kode yang memanggil fungsi bawaan Node.js seperti `require()`, `import fs from 'node:fs'`, `process.env`, atau `http.createServer()`. Kode ini **tidak akan berjalan** di browser maupun playground aplikasi karena objek-objek tersebut hanya tersedia di lingkungan server Node.js.

---

## 4. Tips Melatih Intuisi Programmer Back-End

Untuk mendapatkan manfaat maksimal dari kelas ini:
1. **Jangan terburu-buru scrolling**: Tahan pandanganmu pada blok kode, telusuri alur logikanya di kepala selama 10–20 detik, baru gulir ke blok `Output:`.
2. **Perhatikan urutan asinkron**: Back-end sangat kental dengan proses yang tidak instan (membaca file, menunggu koneksi jaringan). Perhatikan dengan seksama urutan mana yang selesai lebih dulu.
3. **Gunakan catatan mandiri**: Siapkan secarik kertas untuk mencoret-coret urutan antrean eksekusi ketika kita masuk ke materi Event Loop dan Asynchronous I/O.

Sekarang, mari kita lihat peta perjalanan yang akan kita tempuh bersama di modul berikutnya!
