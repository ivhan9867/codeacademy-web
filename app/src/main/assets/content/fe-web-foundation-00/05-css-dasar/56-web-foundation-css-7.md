---
id: web-foundation-css-7
courseId: fe-web-foundation-00
order: 56
title: "Rangkuman Dasar CSS"
readingMinutes: 3
section: CSS_DASAR
learningObjectives:
  - "Merangkum konsep inti CSS yang sudah dipelajari di section ini"
---
Sampai titik ini, kamu sudah memahami pilar-pilar penting dalam dasar CSS:

- CSS adalah bahasa **deklaratif**, bukan bahasa pemrograman—tugasnya mengatur presentasi dan gaya visual, bukan memproses logika.
- Satu aturan (**rule**) CSS tersusun atas **selector** (penanda target elemen) dan **declaration block** (kumpulan properti dan nilainya di dalam tanda kurung kurawal `{}`).
- Tiga cara menyematkan CSS ke HTML: **external stylesheet** (metode standar terbaik), **embedded/internal** (menggunakan tag `<style>`), dan **inline style** (langsung pada atribut elemen).
- **Inheritance (Pewarisan)**: Elemen anak secara otomatis mewarisi sebagian gaya dari elemen induknya, seperti jenis font dan warna teks.
- **Cascade**: CSS dibaca dari atas ke bawah. Bila ada selektor yang setara menargetkan elemen yang sama, deklarasi yang ditulis paling akhir akan mengesampingkan deklarasi sebelumnya.
- Properti `color` berfungsi mengubah warna teks, yang nilainya dapat ditulis menggunakan nama warna maupun kode heksadesimal.

## Contoh Kode Lengkap Rangkuman Dasar CSS

Berikut contoh dokumen HTML utuh yang menggabungkan seluruh konsep dasar tersebut:

```html
<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8">
  <title>Kartu Profil Pengembang</title>
  <style>
    /* 1. Inheritance: warna teks dan jenis font diwariskan ke semua elemen anak */
    body {
      font-family: sans-serif;
      color: #333333;
      background-color: #F3F4F6;
    }

    /* 2. Rule CSS dengan selector class dan declaration block */
    .kartu-profil {
      background-color: #FFFFFF;
      padding: 24px;
      border-radius: 12px;
      max-width: 400px;
    }

    /* 3. Selector elemen spesifik dengan kode warna hex */
    h1 {
      color: #1E40AF; /* Biru tua elegan */
      margin-top: 0;
    }

    /* 4. Cascade: aturan kedua menimpa warna aturan pertama */
    p.status {
      color: gray;
    }
    p.status {
      color: #059669; /* Hijau aktif yang menang karena ditulis terakhir */
    }
  </style>
</head>
<body>
  <div class="kartu-profil">
    <h1>Ahmad Fauzi</h1>
    <p class="status">● Status: Siap Menerima Proyek Web</p>
    <p>Pengembang web pemula yang fokus mendalami HTML dan CSS modern.</p>
  </div>
</body>
</html>
```

## Penjelasan Hasil di Layar Browser

Saat kode di atas dijalankan:
1. **Latar Belakang & Tipografi Bersih**: `<body>` memberikan warna abu-abu muda (`#F3F4F6`) pada seluruh layar dan mewariskan font `sans-serif` serta warna teks `#333333` ke semua paragraf di dalam `.kartu-profil`.
2. **Kartu Konten Berdiri Sendiri**: Elemen `<div class="kartu-profil">` tampil sebagai kotak putih bersih dengan sudut membulat, memisahkan konten kartu dari latar belakang halaman.
3. **Hirarki Judul Jelas**: Tag `<h1>` tampil kontras dan tegas dalam warna biru tua (`#1E40AF`).
4. **Prinsip Cascade Terbukti**: Teks status `<p class="status">` ditampilkan berwarna **hijau** (`#059669`), bukan abu-abu (`gray`), membuktikan bahwa deklarasi kedua yang berada di baris lebih bawah berhasil menimpa deklarasi pertama.

Ini baru langkah awal pondasi. Di bagian berikutnya, kita akan melangkah lebih jauh ke **Pendalaman CSS**—mempelajari Box Model, penataan layout, float, hingga flexibilitas tampilan. Kerjakan kuis di bawah ini untuk memastikan seluruh konsep dasar telah kamu kuasai dengan baik!
