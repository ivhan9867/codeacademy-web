---
id: css-layout-lanjutan-8
courseId: fe-css-02
order: 27
title: "Alignment Matriks: place-items, place-content, & justify-self"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - "Memahami matriks 6 properti alignment di CSS Grid untuk mengatur isi sel maupun keseluruhan kisi"
  - "Menggunakan shorthand place-items, place-content, dan place-self agar kode CSS jauh lebih ringkas"
  - "Membedakan penyelarasan elemen di dalam sel (items/self) versus penyelarasan seluruh grid di dalam container (content)"
codeLanguage: css
codeSnippet: |
  .grid-matriks {
    display: grid;
    grid-template-columns: repeat(3, 90px);
    grid-template-rows: repeat(2, 70px);
    gap: 8px;
    place-items: center;
    place-content: center;
  }

  .item-khusus {
    justify-self: end;
    align-self: start;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: Alignment Matriks CSS Grid</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>Matriks Alignment CSS Grid</h3>
      <div class="grid-board">
        <div class="cell">1</div>
        <div class="cell">2</div>
        <div class="cell spesial">3 (Pojok)</div>
        <div class="cell">4</div>
        <div class="cell">5</div>
        <div class="cell">6</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Atur Grid Container dengan ukuran track tetap agar terlihat ruang ekstra */
  .grid-board {
    /* Tambahkan display: grid; dan place-items: center; di sini */
    grid-template-columns: repeat(3, 96px);
    grid-template-rows: repeat(2, 80px);
    gap: 10px;
    min-height: 240px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    border-radius: 10px;
    padding: 12px;
    place-content: center;
  }

  .cell {
    background-color: #104246;
    color: white;
    padding: 8px 14px;
    border-radius: 6px;
    font-weight: bold;
    font-size: 13px;
  }

  /* 2. Pengecualian untuk satu item saja */
  .spesial {
    background-color: #df7b56;
    justify-self: end;
    align-self: start;
  }
playgroundTask: "Tambahkan 'display: grid;' dan 'place-items: center;' pada '.grid-board'. Amati bagaimana seluruh kotak bernomor otomatis berada di tengah sel masing-masing, sementara kotak nomor 3 menempel ke pojok kanan atas selnya berkat 'justify-self: end' dan 'align-self: start'!"
verificationRules:
  - "css:display: grid"
  - "css:place-items"
---
Pada CSS Grid, sistem perataan jauh lebih lengkap daripada Flexbox karena kita memiliki **dua sumbu aktif sekaligus** (horizontal dan vertikal). Terdapat **6 properti alignment utama** plus 3 properti *shorthand* yang membentuk matriks teratur.

## Konsep Kunci: Isi Sel (*Items*) vs Seluruh Kisi (*Content*)

Sebelum menghafal namanya, pahami dua level area di CSS Grid:

```text
  LEVEL 1: *-items / *-self (Di dalam SEL)      LEVEL 2: *-content (Seluruh KISI di Container)
+-----------------------------------------+    +-----------------------------------------+
| [  (item)  ] | [  (item)  ]             |    |                                         |
| -------------+-------------             |    |         +-------+-------+               |
| [  (item)  ] | [  (item)  ]             |    |         | Sel 1 | Sel 2 |  (kumpulan sel|
|                                         |    |         +-------+-------+   di tengah   |
| (mengatur posisi kotak di dalam selnya) |    |         | Sel 3 | Sel 4 |   container)  |
+-----------------------------------------+    |         +-------+-------+               |
                                               +-----------------------------------------+
```

## Tabel Matriks 6 Properti Alignment CSS Grid

Aturan penamaannya sangat konsisten: **`justify-*`** selalu untuk sumbu horizontal (baris), dan **`align-*`** selalu untuk sumbu vertikal (kolom).

| Sasaran Perataan | Sumbu Horizontal (`justify-*`) | Sumbu Vertikal (`align-*`) | Shorthand (`place-*`) |
| :--- | :--- | :--- | :--- |
| **Semua Item di Dalam Sel** *(pada Container)* | `justify-items` | `align-items` | `place-items` |
| **Satu Item Spesifik di Selnya** *(pada Item)* | `justify-self` | `align-self` | `place-self` |
| **Seluruh Track Kisi di Container** *(pada Container)* | `justify-content` | `align-content` | `place-content` |

Nilai yang paling sering dipakai meliputi `start`, `end`, `center`, dan `stretch` (bawaan untuk `*-items` sehingga isi sel memenuhi kotak sel). Khusus `*-content`, tersedia pula `space-between`, `space-around`, dan `space-evenly`.

## Keajaiban Shorthand `place-items` & `place-content`

Sintaks `place-*` menerima satu atau dua nilai dengan urutan: **`<align> <justify>`** (vertikal dahulu, lalu horizontal). Jika hanya menulis satu nilai, keduanya bernilai sama.

```css
/* Centering paling singkat di seluruh sejarah CSS (2 baris!) */
.kotak-tengah {
  display: grid;
  place-items: center; /* Setara: align-items: center; justify-items: center; */
}
```

Sementara jika Grid kamu memiliki ukuran kolom/baris tetap (misalnya `grid-template-columns: repeat(3, 100px)`) di dalam container yang lebih lebar, kamu bisa memposisikan seluruh tabel kisi ke tengah container dengan `place-content`:

```css
.papan-grid {
  display: grid;
  grid-template-columns: repeat(3, 100px);
  grid-template-rows: repeat(2, 80px);
  place-content: center; /* Geser seluruh kisi ke tengah container */
}
```

## Pengecualian Per Item: `justify-self` & `align-self`

Ingin seluruh sel rata tengah, tetapi satu badge atau tombol "Edit" menempel di pojok kanan atas selnya? Gunakan `justify-self` dan `align-self` langsung pada Grid Item tersebut (atau singkat dengan `place-self: start end;`).

```css
.badge-pojok {
  justify-self: end;   /* Rapat kanan di dalam selnya */
  align-self: start;   /* Rapat atas di dalam selnya */
}
```

Aktifkan `display: grid` dan `place-items: center` di Playground untuk menyaksikan matriks alignment bekerja secara nyata!
