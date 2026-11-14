---
id: css-layout-pend-3
courseId: fe-css-02
order: 6
title: "Masa Lalu yang Melelahkan: Jebakan CSS Float & Positioning"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Memahami tujuan asli properti float dan mengapa penggunaannya untuk layout kolom sangat rapuh"
  - "Mengenal fenomena container collapsing dan mantra clearfix masa lalu"
  - "Mengetahui batasan properti positioning saat digunakan untuk tata letak halaman menyeluruh"
codeLanguage: css
codeSnippet: |
  .kolom-kiri {
    float: left;
    width: 60%;
    background-color: #389b9e;
  }

  .kolom-kanan {
    float: right;
    width: 35%;
    background-color: #df7b56;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Eksperimen Float Klasik</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="pembungkus">
        <div class="kolom-kiri">Konten Utama (Float Left)</div>
        <div class="kolom-kanan">Sidebar (Float Right)</div>
      </div>
      <div class="footer">Footer Bawah Halaman</div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 20px;
    background-color: #faf7f2;
  }

  .pembungkus {
    border: 3px solid #104246;
    background-color: #e2f0f1;
    padding: 10px;
  }

  .kolom-kiri {
    float: left;
    width: 60%;
    background-color: #389b9e;
    color: white;
    padding: 20px;
    box-sizing: border-box;
  }

  .kolom-kanan {
    float: right;
    width: 35%;
    background-color: #df7b56;
    color: white;
    padding: 20px;
    box-sizing: border-box;
  }

  .footer {
    background-color: #24211d;
    color: white;
    padding: 15px;
    margin-top: 15px;
    text-align: center;
  }
playgroundTask: "Coba amati panel Live Preview: kotak pembungkus (.pembungkus) tampak kempes atau runtuh tingginya karena anak-anaknya di-float! Sekarang tambahkan properti 'overflow: auto;' di dalam selector '.pembungkus', lalu jalankan kembali. Amati bagaimana kontainer pembungkus kembali memeluk anak-anaknya."
---
Sebelum kita bersyukur atas kemudahan Flexbox dan Grid, penting untuk memahami "trauma kolektif" yang pernah dialami seluruh pengembang web di seluruh dunia: **membuat kolom dengan `float`**.

> **Ilustrasi Konsep:** Fenomena Container Collapsing Akibat Float dan Mantra Clearfix
## Apa Sebenarnya Tujuan Awal `float`?

Properti `float` (dengan nilai `left` atau `right`) diciptakan untuk meniru tata letak surat kabar:
> Kamu meletakkan foto di sisi kiri artikel, lalu teks paragraf akan mengalir membungkus sisi kanan dan bawah foto tersebut.

Hanya itu tujuannya.

Tetapi karena di masa lalu CSS belum memiliki cara lain untuk menaruh dua kotak berdampingan, pengembang web terpaksa mengapungkan (`float: left`) semua kolom halaman.

## Malapetaka 1: Kontainer Runtuh (*Container Collapsing*)

Saat sebuah elemen di-float, elemen tersebut **terangkat keluar dari aliran normal dokumen**.

Akibatnya, elemen induknya (`.pembungkus`) merasa tidak memiliki anak sama sekali di dalamnya. Tinggi kontainer induk seketika menjadi 0 piksel (*collapse*), latar belakangnya lenyap, dan elemen di bawahnya (seperti `.footer`) akan melompat naik menabrak kolom-kolom yang mengapung!

Untuk mengatasi hal ini, pengembang harus menyuntikkan kode aneh bernama **clearfix hack**:

```css
/* Trik kuno yang merepotkan */
.pembungkus::after {
  content: "";
  display: table;
  clear: both;
}
```

## Malapetaka 2: Kesulitan Menyamakan Tinggi Kolom

Jika kolom konten memiliki teks panjang sedangkan kolom sidebar hanya satu paragraf, kolom sidebar akan pendek dan menyisakan lubang kosong di bawahnya. Menyamakan tinggi kedua kolom menggunakan float adalah hal yang hampir mustahil tanpa trik JavaScript atau padding-margin berlebihan.

## Bagaimana dengan `position: absolute`?

Properti `position: absolute` memang bisa menaruh elemen di mana saja dengan koordinat `top`, `left`, `right`, dan `bottom`. Namun, elemen absolut **sama sekali tidak memengaruhi letak elemen lainnya**. Jika konten di dalamnya bertambah panjang, elemen absolut akan menabrak dan menimpa teks di bawahnya tanpa ampun.

Karena itulah, industri web sepakat: **kita butuh sistem layout resmi yang cerdas, fleksibel, dan tidak merusak aliran dokumen**.
