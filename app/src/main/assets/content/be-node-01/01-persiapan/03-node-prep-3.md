---
id: node-prep-3
courseId: be-node-01
order: 3
title: "Menyiapkan Lingkungan Node.js di Komputermu (Opsional)"
readingMinutes: 6
section: PERSIAPAN
learningObjectives:
  - "Mengetahui cara mengunduh dan memasang Node.js versi LTS dari situs resminya"
  - "Memverifikasi instalasi runtime dan manajer paket npm via terminal"
  - "Membuat file skrip pertama dan menjalankannya menggunakan perintah node"
codeLanguage: javascript
codeSnippet: |
  // Simpan sebagai server-info.js
  const platform = "Node.js Environment";
  const pesan = "Selamat datang di pemrograman sisi server!";

  console.log(`Platform: ${platform}`);
  console.log(`Pesan: ${pesan}`);
---

# Menyiapkan Lingkungan Node.js di Komputermu (Opsional)

Seperti yang telah kita sepakati, seluruh materi di aplikasi CodeAcademy ini dapat kamu ikuti dan selesaikan sepenuhnya melalui ponselmu dengan metode **Baca & Tebak Output**.

Namun, jika kamu memiliki laptop atau komputer desktop dan ingin merasakan langsung bagaimana rasanya mengeksekusi perintah di terminal layaknya seorang insinyur back-end profesional, modul ini adalah panduan praktis untukmu!

![Alur Instalasi dan Eksekusi Node.js](asset:images/be-node-01/alur-instalasi-eksekusi.png)

---

## 1. Mengapa Memilih Versi LTS?

Saat kamu membuka situs resmi Node.js melalui peramban di komputermu, kamu akan melihat dua pilihan unduhan:

1. **LTS (*Long Term Support*)**: Versi yang paling stabil, telah teruji bebas dari bug kritis, dan didukung pembaruan keamanannya selama bertahun-tahun. **Ini adalah versi yang wajib kamu pilih** untuk belajar dan keperluan produksi.
2. **Current**: Versi yang berisi fitur eksperimental terbaru. Versi ini sering berubah dan tidak disarankan untuk pemula.

Unduh berkas instalasi sesuai sistem operasi komputermu (Windows, macOS, atau Linux) dan ikuti petunjuk pemasangan hingga selesai (cukup klik tombol *Next* pada installer).

---

## 2. Memverifikasi Instalasi via Terminal

Setelah proses instalasi selesai, buka aplikasi terminal di komputermu:
- Di Windows: **Command Prompt** atau **PowerShell**
- Di macOS atau Linux: **Terminal**

Ketikkan perintah berikut untuk memastikan Node.js dan npm (*Node Package Manager*) telah terpasang dengan baik:

```bash
node -v
npm -v
```

Jika terminal menampilkan nomor versi (misalnya diawali huruf `v` untuk node dan deretan angka untuk npm), artinya komputermu kini telah memiliki mesin runtime JavaScript sisi server yang siap pakai!

---

## 3. Membuat dan Menjalankan Skrip Pertamamu

Mari kita coba membuat satu file skrip JavaScript sederhana di komputermu:

1. Buat sebuah folder baru di komputermu, misalnya bernama `belajar-node`.
2. Buka text editor favoritmu (seperti VS Code atau Notepad).
3. Buat file baru bernama `halo-server.js` dan ketikkan kode berikut:

```javascript
// halo-server.js
const pengembang = "Programmer Indonesia";
const tahun = 2025;

console.log("=== EKSEKUSI PERDANA NODE.JS ===");
console.log(`Halo Dunia dari ${pengembang}!`);
console.log(`Tahun Mulai Belajar Back-End: ${tahun}`);
console.log("================================");
```

Output:
```
=== EKSEKUSI PERDANA NODE.JS ===
Halo Dunia dari Programmer Indonesia!
Tahun Mulai Belajar Back-End: 2025
================================
```

4. Kembali ke terminal komputermu, masuklah ke folder tempat file tersebut disimpan, lalu jalankan perintah:

```bash
node halo-server.js
```

Seketika teks di dalam blok `Output:` akan muncul di layar terminal komputermu! Komputer mengeksekusi file tersebut langsung menggunakan CPU dan RAM lokalmu tanpa perantara browser sama sekali.

---

## 4. Alternatif: Compiler / Playground Online

Jika saat ini kamu tidak sedang berada di depan laptop dan tetap ingin mencoba mengetik, kamu bisa mencari *"Node.js Online Compiler"* atau *"Online Node Runner"* di mesin pencari peramban ponselmu. Ada banyak situs peramban gratis yang menyediakan terminal online sederhana di cloud untuk menjalankan perintah `node file.js`.

Ingat, ini hanyalah pelengkap opsional. Di modul-modul berikutnya di aplikasi CodeAcademy, fokus utama kita adalah membedah cara kerja Node.js secara konseptual dan menajamkan kemampuan analisismu.

Persiapan selesai! Di Section 2 berikutnya, kita akan menyelami jantung konsep Node.js: **Apa itu V8, mengapa JavaScript bisa berjalan di server, dan bagaimana arsitektur non-blocking bekerja.**
