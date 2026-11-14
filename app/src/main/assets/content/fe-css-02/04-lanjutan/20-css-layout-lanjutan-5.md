---
id: css-layout-lanjutan-5
courseId: fe-css-02
order: 20
title: "Sketsa Tata Letak Visual dengan grid-template-areas"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami konsep perancangan layout halaman berbasis peta nama (ASCII art)"
  - "Menghubungkan elemen HTML ke nama wilayah menggunakan properti grid-area"
  - "Menyusun kerangka halaman lengkap (header, sidebar, content, footer) secara visual"
codeLanguage: css
codeSnippet: |
  .layout-halaman {
    display: grid;
    grid-template-areas:
      "kepala kepala"
      "samping utama"
      "kaki kaki";
    grid-template-columns: 240px 1fr;
    gap: 12px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji grid-template-areas</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="halaman">
        <header class="header">Area Header (Logo & Nav)</header>
        <aside class="sidebar">Area Sidebar</aside>
        <main class="content">Area Konten Utama Artikel</main>
        <footer class="footer">Area Footer Hak Cipta</footer>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .halaman {
    display: grid;
    /* Peta Denah Wilayah */
    grid-template-areas:
      "header header"
      "sidebar content"
      "footer footer";
    grid-template-columns: 180px 1fr;
    grid-template-rows: 60px 180px 50px;
    gap: 10px;
  }

  /* Menghubungkan elemen ke nama peta: */
  .header  { grid-area: header; background-color: #104246; color: white; padding: 16px; border-radius: 6px; }
  .sidebar { grid-area: sidebar; background-color: #389b9e; color: white; padding: 16px; border-radius: 6px; }
  .content { grid-area: content; background-color: #ffffff; border: 1px solid #1a6368; padding: 16px; border-radius: 6px; }
  .footer  { grid-area: footer; background-color: #24211d; color: white; padding: 16px; border-radius: 6px; text-align: center; }
playgroundTask: "Coba tukar posisi 'sidebar' dan 'content' pada baris kedua peta: ubah '\"sidebar content\"' menjadi '\"content sidebar\"', dan ubah 'grid-template-columns: 180px 1fr;' menjadi '1fr 180px;'. Tekan 'Jalankan' dan saksikan sidebar berpindah ke kanan semudah membalik gambar denah!"
---
Fitur paling intuitif dan menyenangkan dari seluruh fitur CSS adalah **`grid-template-areas`**.

Banyak desainer web menggambar denah kasar halaman di secarik kertas:
```
+--------------------------+
|          HEADER          |
+--------------+-----------+
|   SIDEBAR    |  CONTENT  |
+--------------+-----------+
|          FOOTER          |
+--------------------------+
```

Dengan `grid-template-areas`, kamu bisa menuliskan sketsa denah tersebut **langsung ke dalam kode CSS-mu** persis seperti gambar di atas!

> **Ilustrasi Konsep:** Merancang Denah Halaman dengan grid-template-areas
## Dua Langkah Mudah Menerapkan Template Areas

### Langkah 1: Beri Nama Wilayah pada Elemen Anak
Di CSS untuk tiap elemen anak, gunakan properti **`grid-area`** untuk memberi label nama (tanpa tanda kutip):

```css
header { grid-area: header; }
aside  { grid-area: sidebar; }
main   { grid-area: content; }
footer { grid-area: footer; }
```

### Langkah 2: Gambar Peta Denah pada Kontainer Induk
Di kontainer induk, gunakan properti **`grid-template-areas`**. Setiap tanda kutip ganda mewakili **satu baris**, dan kata di dalamnya mewakili **kolom**:

```css
.halaman {
  display: grid;
  grid-template-columns: 240px 1fr;
  grid-template-areas:
    "header header"   /* Baris 1: header membentang 2 kolom */
    "sidebar content" /* Baris 2: sidebar di kiri, content di kanan */
    "footer footer";  /* Baris 3: footer membentang 2 kolom */
}
```

## Aturan Main Template Areas

1. Setiap baris harus memiliki **jumlah kolom yang sama persis**.
2. Setiap wilayah area harus berbentuk **persegi panjang atau bujur sangkar yang utuh**. Kamu tidak boleh membuat bentuk "L" atau "T".
3. Jika ada sel yang ingin kamu biarkan kosong tanpa elemen apa pun, gunakan tanda titik (`.`):
   ```css
   grid-template-areas:
     "header header"
     "content ."       /* Kolom kedua baris ini dibiarkan kosong */
     "footer footer";
   ```

Merancang tata letak halaman tidak pernah seringkas dan sejelas ini!
