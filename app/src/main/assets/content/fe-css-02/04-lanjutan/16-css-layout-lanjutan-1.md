---
id: css-layout-lanjutan-1
courseId: fe-css-02
order: 20
title: "Selamat Datang di Matriks: Konsep Dasar CSS Grid"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami perbedaan mendasar arsitektur tata letak dua dimensi (baris dan kolom sekaligus)"
  - "Mengenal istilah anatomi Grid: Grid Lines, Grid Tracks, Grid Cells, dan Grid Areas"
  - "Mengaktifkan display: grid pada elemen kontainer"
codeLanguage: css
codeSnippet: |
  .kontainer-grid {
    display: grid;
    grid-template-columns: 1fr 1fr; /* Membelah jadi 2 kolom seimbang */
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Konsep Dasar CSS Grid</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Memasuki Matriks 2 Dimensi</h2>
      <div class="kontainer-grid">
        <div class="kartu">Sel 1</div>
        <div class="kartu">Sel 2</div>
        <div class="kartu">Sel 3</div>
        <div class="kartu">Sel 4</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .kontainer-grid {
    display: grid;
    grid-template-columns: 1fr 1fr; /* Dua kolom sama lebar */
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    background-color: #1a6368;
    color: white;
    font-weight: bold;
    padding: 30px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Coba ubah 'grid-template-columns: 1fr 1fr;' menjadi 'grid-template-columns: 1fr 1fr 1fr 1fr;'. Tekan 'Jalankan' dan amati bagaimana 4 kartu tersebut langsung berbaris menjadi 4 kolom dalam satu baris horizontal!"
---
Jika Flexbox adalah raja tata letak **satu dimensi** (kamu memilih mengatur elemen berbaris mendatar ATAU menurun), maka **CSS Grid** adalah kaisar tata letak **dua dimensi**.

Dengan CSS Grid, kamu bisa mendikte posisi elemen pada **baris (*rows*) dan kolom (*columns*) secara bersamaan**.

```text
   Line 1       Line 2       Line 3       Line 4
     |            |            |            |
  ---+------------+------------+------------+--- Row Line 1
     |  Cell 1,1  |  Cell 1,2  |  Cell 1,3  |
  ---+------------+------------+------------+--- Row Line 2
     |  Cell 2,1  |  Cell 2,2  |  Cell 2,3  |
  ---+------------+------------+------------+--- Row Line 3
```
## Anatomi Kosakata CSS Grid

Agar kita bisa berbicara dalam bahasa yang sama, mari pahami 4 istilah penting di dunia Grid:

1. **Grid Lines (Garis Kisi)**:
   Garis pemisah tak kasatmata yang membelah baris dan kolom. Garis ini diberi nomor urut mulai dari `1` (bukan dari 0!). Jika kamu punya 2 kolom, maka ada 3 garis kolom: garis paling kiri (1), garis pemisah tengah (2), dan garis paling kanan (3).
2. **Grid Track (Jalur Kisi)**:
   Ruang di antara dua garis kisi yang bersebelahan. Sederhananya, ini adalah sebutan teknis untuk **satu kolom penuh** atau **satu baris penuh**.
3. **Grid Cell (Sel Kisi)**:
   Kotak terkecil di dalam Grid, yaitu perpotongan antara satu baris dan satu kolom. Seperti satu kotak kecil di kertas berpetak matematika.
4. **Grid Area (Area Kisi)**:
   Gabungan dari beberapa sel persegi panjang yang membentuk satu wilayah besar (misalnya: area seluas 2 baris dan 3 kolom).

## Mengaktifkan Grid: `display: grid`

Sama seperti Flexbox, langkah pertama adalah menyalakan mesin Grid pada kontainer induk:

```css
.wadah {
  display: grid;
}
```

Ketika kamu hanya menulis `display: grid`, belum ada perubahan visual yang dramatis karena secara default Grid hanya membentuk 1 kolom saja.

Di modul berikutnya, kita akan belajar cara membelah kolom dan baris sesuai keinginan kita!
