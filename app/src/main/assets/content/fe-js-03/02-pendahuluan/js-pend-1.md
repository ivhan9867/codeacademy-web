---
id: js-pend-1
courseId: fe-js-03
order: 4
title: Apa Itu JavaScript? Otak di Balik Web Interaktif
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Memahami peran utama JavaScript sebagai bahasa pemrograman dinamis di browser web
  - Membedakan pembagian tugas antara HTML, CSS, dan JavaScript pada sebuah website
  - Mengenal standar ECMAScript modern (ES2015+) yang menjadi fondasi web masa kini
---

# Apa Itu JavaScript? Otak di Balik Web Interaktif

Bayangkan kamu sedang merakit sebuah mobil:
1. **HTML** adalah sasis, rangka baja, pintu, dan kursi (*struktur dasar*).
2. **CSS** adalah warna cat metalik, bentuk velg, jok kulit, dan kaca film (*keindahan estetika*).
3. **JavaScript** adalah mesin, aki, setir, pedal gas, dan sistem komputer (*fungsi dan pergerakan*).

Tanpa JavaScript, sebuah website hanyalah brosur kertas digital: indah dipandang, tetapi diam membisu saat disentuh. Bersama JavaScript, halaman web dapat berpikir, merespons klik mouse, menghitung harga belanjaan, memvalidasi formulir pendaftaran, dan memperbarui data tanpa harus memuat ulang (*reload*) seluruh halaman.

---

## Sejarah Singkat & Standar ECMAScript

JavaScript diciptakan oleh **Brendan Eich** pada tahun 1995 hanya dalam waktu 10 hari di Netscape. Meskipun namanya mengandung kata "Java", JavaScript adalah bahasa yang sama sekali berbeda dari Java, baik dari segi filosofi maupun penggunaannya.

Agar semua peramban (*browser*) seperti Chrome, Firefox, Safari, dan Edge dapat mengeksekusi JavaScript dengan cara yang seragam, bahasa ini distandardisasi di bawah nama **ECMAScript**.

Pada tahun 2015, dirilis pembaruan besar yang dikenal sebagai **ES6 (ECMAScript 2015)**. Pembaruan ini membawa fitur-fitur modern seperti:
- Deklarasi variabel `let` dan `const`.
- *Arrow function* (`=>`).
- *Template literals* (backtick `` ` ``).
- Pengelolaan data asinkron (*Promise*).

Semua materi yang akan kamu pelajari di kelas ini menggunakan standar modern (ES2015+) yang menjadi praktik standar industri saat ini.

---

## Bagaimana JavaScript Bekerja di Layar Kamu?

Setiap browser modern memiliki program penerjemah (*engine*) khusus di dalamnya (misalnya Google V8 di balik Chrome). Saat browser membaca dokumen web kamu:
1. Browser merakit elemen HTML menjadi struktur pohon dokumen.
2. Browser menerapkan aturan CSS untuk tata letak dan warna.
3. Begitu menemukan kode JavaScript, mesin browser akan mengeksekusi instruksi baris demi baris dari atas ke bawah.

Misalnya, perhatikan baris kode sederhana berikut:

```javascript
const namaSiswa = "Andi";
const tahunSekarang = 2026;

console.log("Selamat datang, " + namaSiswa);
console.log("Tahun aktif: " + tahunSekarang);
```

Output:
```text
Selamat datang, Andi
Tahun aktif: 2026
```

Hebatnya, kamu tidak memerlukan aplikasi tambahan atau instalasi rumit untuk menjalankan kode di atas. Browser di ponsel dan laptop kamu sudah siap menjalankannya detik ini juga!

Di modul berikutnya, kita akan mempelajari bagaimana cara menaruh kode JavaScript ke dalam dokumen HTML agar keduanya dapat saling terhubung.
