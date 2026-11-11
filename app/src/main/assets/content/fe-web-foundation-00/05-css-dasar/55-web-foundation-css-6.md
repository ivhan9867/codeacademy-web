---
id: web-foundation-css-6
courseId: fe-web-foundation-00
order: 55
title: "Styling Dasar: Warna Teks"
readingMinutes: 4
section: CSS_DASAR
codeSnippet: |
  p {
    color: red;
  }

  h1 {
    color: #1E90FF;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan Warna Teks</title>
    </head>
    <body>
      <h1>Judul Berwarna Biru</h1>
      <p>Paragraf ini akan berwarna merah.</p>
    </body>
  </html>
learningObjectives:
  - "Menerapkan properti color untuk mengubah warna teks"
  - "Mengenal dua cara menuliskan nilai warna: nama warna dan kode hex"
---
Kamu sudah melihat properti `color` digunakan pada latihan sebelumnya—sekarang mari kita bedah cara kerjanya lebih mendalam.

## Dua Cara Menulis Nilai Warna Dasar

Di tahap dasar ini, ada dua cara umum yang paling sering dipakai untuk menentukan nilai warna:

1. **Nama Warna (*Named Color*)** — CSS menyediakan nama warna bawaan yang langsung dikenali browser, seperti `red`, `green`, `blue`, `crimson`, atau `navy`. Cara ini sangat praktis untuk mencoba tampilan secara cepat tanpa perlu mencari kode angka.
2. **Kode Heksadesimal (*Hex Code*)** — ditulis dengan tanda pagar (`#`) diikuti enam digit angka atau huruf, misalnya `#1E90FF` (biru langit cerah) atau `#333333` (abu-abu gelap). Cara ini jauh lebih fleksibel karena memberi kamu akses ke jutaan gradasi warna yang presisi.

## Contoh Kode Lengkap

Berikut contoh dokumen HTML lengkap yang memadukan kedua cara penulisan nilai warna tersebut:

```html
<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8">
  <title>Eksplorasi Warna Teks</title>
  <style>
    h1 {
      color: #1E90FF; /* Biru terang menggunakan kode hex */
    }

    h2 {
      color: darkslateblush; /* Nama warna standar */
      color: darkslateblue;
    }

    p.intro {
      color: crimson; /* Merah menyala dengan nama warna */
    }

    p.deskripsi {
      color: #4B5563; /* Abu-abu netral menggunakan kode hex */
    }
  </style>
</head>
<body>
  <h1>Selamat Datang di Toko Kopi Senja</h1>
  <h2>Racikan Terbaik dari Petani Lokal</h2>
  <p class="intro">Pemberitahuan: Buka setiap hari mulai pukul 08.00 WIB.</p>
  <p class="deskripsi">Nikmati seduhan biji kopi pilihan dengan suasana hangat dan nyaman bersama teman-temanmu.</p>
</body>
</html>
```

## Penjelasan Hasil di Layar Browser

Saat kode di atas dibuka di browser:
- Judul utama `<h1>` akan dicetak tebal dengan warna biru cerah (`#1E90FF`), menarik perhatian pengunjung sebagai titik fokus pertama.
- Subjudul `<h2>` tampil dengan warna ungu kebiruan gelap (`darkslateblue`), menegaskan posisinya di bawah judul utama.
- Paragraf pertama dengan kelas `.intro` tampil dalam warna merah menyala (`crimson`), menandakan bahwa kalimat ini merupakan pengumuman penting.
- Paragraf kedua dengan kelas `.deskripsi` tampil dalam abu-abu lembut (`#4B5563`), membuat paragraf panjang nyaman dibaca tanpa membuat mata lelah dibandingkan hitam pekat.

Di Playground, cobalah mengganti nilai `color` pada elemen-elemen di atas untuk melihat perubahannya secara langsung. Pengaturan warna yang lebih luas—seperti warna latar belakang, transparansi, dan palet warna modern—akan dibahas tuntas pada section Pendalaman CSS.
