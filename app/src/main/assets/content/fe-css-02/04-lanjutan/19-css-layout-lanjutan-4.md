---
id: css-layout-lanjutan-4
courseId: fe-css-02
order: 23
title: "Menempatkan Item Berdasarkan Jalur Garis (grid-column & grid-row)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami sistem penomoran garis Grid Lines (garis awal dan garis akhir)"
  - "Menggunakan properti grid-column dan grid-row untuk merentangkan elemen (span)"
  - "Menciptakan layout kartu unggulan (featured card) yang membentang multi-kolom"
codeLanguage: css
codeSnippet: |
  .kartu-unggulan {
    grid-column: 1 / 3; /* Membentang dari garis 1 sampai garis 3 (2 kolom) */
    grid-row: 1 / 2;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji grid-column & grid-row</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Grid Spanning: Kartu Berita</h2>
      <div class="wadah-berita">
        <div class="kartu unggulan">Berita Utama (Membentang Lebar)</div>
        <div class="kartu">Berita 2</div>
        <div class="kartu">Berita 3</div>
        <div class="kartu">Berita 4</div>
        <div class="kartu">Berita 5</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah-berita {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    background-color: #1a6368;
    color: white;
    padding: 24px;
    border-radius: 6px;
    text-align: center;
    font-weight: bold;
  }

  /* Targetkan kartu unggulan untuk membentang: */
  .unggulan {
    background-color: #df7b56;
    grid-column: 1 / 3; /* Dari garis kolom 1 ke garis 3 */
  }
playgroundTask: "Coba ubah '.unggulan' menjadi 'grid-column: 1 / -1;'. Tanda '-1' berarti garis paling ujung kanan! Jalankan kode dan amati bagaimana Berita Utama kini membentang penuh di ketiga kolom (garis 1 sampai 4)!"
---
Secara default, setiap elemen anak di dalam Grid hanya menempati **satu sel tunggal** dan mengalir otomatis mengisi petak demi petak berikutnya.

Namun, bagaimana jika kamu ingin membuat satu elemen yang lebih penting—seperti spanduk promo atau kartu berita utama—membentang selebar 2 kolom atau setinggi 2 baris?

Kuncinya adalah mengarahkan elemen memakai nomor **Grid Lines (Garis Kisi)**!

```text
grid-column: 1 / 3; (membentang dari garis kolom 1 ke 3)
+---------------------------+-------------+
|       Featured (2 col)    |   Item 2    |
+-------------+-------------+-------------+
|   Item 3    |   Item 4    |   Item 5    |
+-------------+-------------+-------------+
```
## Sistem Penomoran Garis Kisi

Jika kamu membuat 3 kolom:
```
Garis 1        Garis 2        Garis 3        Garis 4
   |   Kolom 1    |   Kolom 2    |   Kolom 3    |
```
Perhatikan bahwa **ada 4 garis kolom untuk 3 kolom**!

Untuk menyuruh elemen membentang dari Garis 1 sampai Garis 3, gunakan sintaks pemisah garis miring (`/`):

```css
.unggulan {
  /* grid-column: [garis_mulai] / [garis_selesai]; */
  grid-column: 1 / 3; /* Menghabiskan Kolom 1 dan Kolom 2 */
}
```

## Kata Kunci Ajaib: `span`

Daripada menghitung nomor garis akhir, kamu juga bisa menggunakan kata kunci **`span`** (rentangkan):

```css
/* Artinya: mulai dari posisi alamimu, lalu rentangkan selebar 2 kolom */
.unggulan {
  grid-column: span 2;
}
```

Bila kamu ingin elemen membentang setinggi 2 baris ke bawah:

```css
.poster-tinggi {
  grid-row: span 2;
}
```

## Trik Garis Negatif (`-1`)

Ingin sebuah elemen membentang penuh dari ujung paling kiri ke ujung paling kanan tanpa perlu tahu berapa total kolomnya? Gunakan angka `-1`!

```css
.header-penuh {
  grid-column: 1 / -1; /* Dari garis paling awal (1) sampai garis paling akhir (-1) */
}
```

Teknik ini adalah senjata utama untuk merancang tata letak berita atau galeri foto majalah (*masonry / magazine layout*) yang dinamis.
