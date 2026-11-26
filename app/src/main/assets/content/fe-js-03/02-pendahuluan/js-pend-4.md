---
id: js-pend-4
courseId: fe-js-03
order: 9
title: "Menulis Kode yang Rapi: Komentar & Aturan Sintaks Dasar"
readingMinutes: 4
section: PENDAHULUAN
learningObjectives:
  - Mengetahui fungsi dan cara membuat komentar satu baris (`//`) serta multi-baris (`/* ... */`)
  - Memahami sifat sensitif terhadap huruf besar/kecil (*case-sensitive*) pada JavaScript
  - Menerapkan konvensi penamaan standar (*camelCase*) dan memahami peran tanda titik koma (`;`)
codeLanguage: javascript
codeSnippet: |
  // Ini komentar satu baris
  const namaSiswa = "Budi";

  /* Komentar multi-baris
     berguna untuk penjelasan panjang */
  console.log(namaSiswa);
starterJs: |
  // 1. Ini adalah catatan komentar yang diabaikan mesin
  const namaDepan = "Rani";
  const namaBelakang = "Kusuma";

  /* 2. Di bawah ini kita mencetak kedua nama
        ke dalam panel Console */
  console.log(namaDepan, namaBelakang);
playgroundTask: |
  Jalankan kode dan periksa panel Console. Pastikan hanya tercetak satu baris:
  Rani Kusuma
  Perhatikan bagaimana teks yang diawali dengan tanda komentar sama sekali tidak mempengaruhi jalannya program.
---

# Menulis Kode yang Rapi: Komentar & Aturan Sintaks Dasar

Menulis kode bukan hanya soal membuat komputer mengerti instruksi kita, tetapi juga agar rekan setim (atau diri kamu sendiri di masa depan) dapat membaca dan memahaminya dengan mudah.

Mari pelajari aturan-aturan dasar penulisan kode JavaScript yang rapi dan standar.

---

## 1. Komentar Kode (Comments)

Komentar adalah catatan teks yang sengaja ditulis untuk dibaca oleh manusia. Browser akan mengabaikan komentar sepenuhnya saat mengeksekusi program.

Ada dua jenis komentar di JavaScript:

### a. Komentar Satu Baris (`//`)
Gunakan dua garis miring untuk membuat catatan singkat pada satu baris:

```javascript
// Hitung diskon member 10%
const hargaAsli = 100000;
const hargaDiskon = hargaAsli * 0.9;
```

### b. Komentar Multi-Baris (`/* ... */`)
Gunakan pembuka `/*` dan penutup `*/` untuk penjelasan panjang yang memakan beberapa baris:

```javascript
/*
  Fungsi berikut digunakan untuk
  memvalidasi alamat email pengguna
  sebelum data dikirimkan ke database.
*/
console.log("Validasi siap");
```

---

## 2. Sensitivitas Huruf (Case-Sensitivity)

JavaScript adalah bahasa yang sangat **case-sensitive**. Huruf besar dan huruf kecil dianggap sebagai hal yang berbeda mutlak oleh komputer:

```javascript
const skor = 10;
const Skor = 20;
const SKOR = 30;

console.log(skor); // Menghasilkan 10, bukan 20 atau 30
```

Tiga nama di atas dianggap sebagai tiga variabel yang terpisah. Oleh karena itu, berhati-hatilah saat mengetik nama variabel atau fungsi agar ejaannya sama persis.

---

## 3. Gaya Penamaan Standar: camelCase

Di ekosistem JavaScript, standar penamaan variabel dan fungsi yang disepakati secara global adalah **`camelCase`** (seperti punggung unta):
- Kata pertama dimulai dengan **huruf kecil**.
- Setiap kata berikutnya diawali dengan **huruf kapital tanpa spasi**.

Contoh yang tepat:
- `namaLengkap` *(bukan nama_lengkap atau NamaLengkap)*
- `totalHargaBarang`
- `statusAktifLogin`

---

## 4. Perlukah Tanda Titik Koma (Semicolon `;`)?

Di akhir setiap baris instruksi JavaScript, kamu boleh menambahkan tanda titik koma (`;`):

```javascript
const kota = "Jakarta";
console.log(kota);
```

JavaScript memiliki fitur pintar bernama *Automatic Semicolon Insertion (ASI)* yang otomatis menganggap akhir baris sebagai penutup instruksi. Namun, membiasakan diri menulis titik koma atau konsisten mengikuti gaya proyek adalah kebiasaan baik agar kodemu terhindar dari perilaku ambigu.

Di modul berikutnya, kita akan belajar mengenali pesan error agar kamu tidak panik saat salah mengetik kode!
