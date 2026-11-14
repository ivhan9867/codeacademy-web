---
id: css-layout-inti-6
courseId: fe-css-02
order: 13
title: "Fleksibilitas Item: flex-grow, flex-shrink, dan flex-basis"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami cara kerja trio properti fleksibilitas pada elemen anak: flex-grow, flex-shrink, dan flex-basis"
  - "Mengetahui bagaimana ruang kosong didistribusikan secara proporsional"
  - "Menguasai penulisan shorthand flex: 1 yang sangat populer di industri"
codeLanguage: css
codeSnippet: |
  .kolom-utama {
    flex: 2; /* Mengambil jatah ruang 2x lipat */
  }

  .sidebar {
    flex: 1; /* Mengambil jatah ruang 1x */
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji flex-grow, shrink, basis</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Pembagian Jatah Ruang</h2>
      <div class="wadah">
        <div class="item item-kiri">Sidebar (flex: 1)</div>
        <div class="item item-kanan">Konten Utama (flex: 2)</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah {
    display: flex;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
    gap: 12px;
  }

  .item {
    padding: 24px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    text-align: center;
  }

  .item-kiri {
    background-color: #df7b56;
    flex: 1; /* Coba ganti nilainya! */
  }

  .item-kanan {
    background-color: #1a6368;
    flex: 2; /* Coba ganti nilainya! */
  }
playgroundTask: "Ubah properti '.item-kiri' menjadi 'flex: 1;' dan '.item-kanan' menjadi 'flex: 1;'. Jalankan kode dan perhatikan bagaimana kedua kolom sekarang memiliki lebar yang persis sama (50% - 50%) terlepas dari panjang teks di dalamnya!"
---
Sejauh ini kita lebih banyak memberi perintah pada **Kontainer Induk**. Sekarang saatnya memberi kekuatan pada **Elemen Anak (Flex Items)** agar mereka bisa menentukan nasib ukuran mereka sendiri!

Kekuatan ini dikendalikan oleh tiga serangkai: **`flex-grow`**, **`flex-shrink`**, dan **`flex-basis`**.

> **Ilustrasi Konsep:** Konsep flex-grow, flex-shrink, dan flex-basis
## 1. `flex-basis`: Ukuran Awal Sebelum Fleksibel

`flex-basis` adalah ukuran ideal awal suatu item sebelum sisa ruang kosong dibagikan.
- Secara default nilainya `auto` (mengikuti lebar/tinggi konten atau nilai `width` yang ditetapkan).
- Kamu bisa menetapkan nilai absolut seperti `flex-basis: 200px;`.

## 2. `flex-grow`: Kemampuan Merebut Ruang Kosong

Jika kontainer memiliki sisa ruang kosong berlebih, siapa yang berhak mengambilnya dan seberapa banyak?
- Nilai default adalah `0` (artinya item menolak membesar, tetap pada ukuran aslinya).
- Jika kamu menetapkan `flex-grow: 1;` pada satu item, item tersebut akan meregang menghabiskan seluruh sisa ruang yang ada.
- Jika item A diberi `flex-grow: 1` dan item B diberi `flex-grow: 2`, maka sisa ruang kosong akan dibagi menjadi 3 bagian: 1 bagian untuk A dan 2 bagian untuk B.

## 3. `flex-shrink`: Kemampuan Menyusut Saat Ruang Sempit

Sebaliknya, jika kontainer terlalu sempit dan ruangannya minus, siapa yang rela dikorbankan untuk menyusut?
- Nilai default adalah `1` (artinya semua item rela menyusut secara proporsional agar tidak jebol keluar wadah).
- Jika kamu memberi `flex-shrink: 0;`, item tersebut berstatus **kebal**: ia menolak menyusut dan akan tetap mempertahankan ukuran aslinya!

## Sang Legenda: Shorthand `flex: 1`

Menulis tiga properti secara terpisah (`flex-grow`, `flex-shrink`, `flex-basis`) cukup panjang. Karena itu pengembang memakai bentuk ringkas (*shorthand*):

```css
/* flex: [grow] [shrink] [basis] */
.kotak {
  flex: 1 1 0%; /* Sering ditulis ringkas: flex: 1; */
}
```

Ketika kamu menulis **`flex: 1;`** pada beberapa elemen anak di dalam kontainer yang sama, kamu sedang memerintahkan:
> *"Bagi rata seluruh lebar kontainer ini secara adil kepada kami semua, abaikan ukuran teks bawaan masing-masing!"*

Inilah cara paling cepat dan bersih untuk membuat kolom yang sama rata di Flexbox.
