---
id: css-layout-lanjutan-6
courseId: fe-css-02
order: 21
title: "Grid Ajaib Tanpa Media Query: minmax() & auto-fit"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami cara kerja fungsi minmax(min, max) untuk fleksibilitas ukuran kolom"
  - "Membedakan auto-fill vs auto-fit dalam mengisi slot kolom yang tersedia"
  - "Menguasai satu baris mantra CSS paling sakti untuk galeri kartu otomatis responsif"
codeLanguage: css
codeSnippet: |
  .galeri-ajaib {
    display: grid;
    /* Kolom responsif otomatis tanpa media query: */
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji auto-fit & minmax</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Galeri Kartu Responsif Otomatis</h2>
      <div class="galeri">
        <div class="kartu">Kursus HTML</div>
        <div class="kartu">Kursus CSS</div>
        <div class="kartu">Kursus Flexbox</div>
        <div class="kartu">Kursus CSS Grid</div>
        <div class="kartu">Kursus JavaScript</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .galeri {
    display: grid;
    /* Mantra Ajaib CSS Grid: */
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
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
    padding: 30px 16px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Coba ubah nilai batas minimal pada minmax: dari 'minmax(180px, 1fr)' menjadi 'minmax(120px, 1fr)'. Tekan 'Jalankan' dan perhatikan bagaimana browser langsung memuat lebih banyak kolom kartu karena syarat minimal lebar kartu menjadi lebih kecil!"
---
Di era sebelum CSS Grid, jika kamu ingin membuat kartu produk yang tampil 4 kolom di monitor besar, 3 kolom di laptop, 2 kolom di tablet, dan 1 kolom di HP, kamu harus menulis puluhan baris aturan **`@media` query**:

```css
/* Cara lama yang panjang dan melelahkan */
@media (max-width: 1200px) { ... }
@media (max-width: 900px)  { ... }
@media (max-width: 600px)  { ... }
```

Sekarang bersiaplah menyaksikan satu baris mantra CSS paling terkenal di jagat web modern:

```css
grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
```

Satu baris ini menggantikan seluruh aturan media query di atas secara otomatis!

![Mantra Responsif Otomatis dengan repeat, auto-fit, dan minmax](asset:images/fe-css-02/grid-autofit-minmax.png)

## Membedah Mantra Baris Sakti

Mari kita pecah menjadi 3 bagian:

### 1. `minmax(250px, 1fr)`
Fungsi `minmax` menetapkan rentang ukuran:
- **Ukuran Minimum (`250px`)**: Kolom ini tidak boleh lebih sempit dari 250px. Jika layar mengecil sampai di bawah 250px, kartu akan melipat turun ke baris berikutnya.
- **Ukuran Maksimum (`1fr`)**: Jika masih ada sisa ruang kosong di layar, kolom boleh meregang selebar mungkin untuk memenuhi layar.

### 2. `auto-fit` (Bukan Angka Kaku)
Daripada menulis angka kaku seperti `repeat(3, ...)`, kata kunci `auto-fit` menyuruh browser:
> *"Hitung sendiri berapa banyak kolom selebar minimal 250px yang muat di layar saat ini. Jika muat 4, buat 4 kolom. Jika hanya muat 1 kolom (seperti di layar HP), jadikan 1 kolom penuh!"*

### 3. Keunggulan `auto-fit` Dibanding `auto-fill`
- `auto-fill` akan tetap membuat petak kolom kosong di sebelah kanan jika itemnya sedikit.
- `auto-fit` akan secara cerdas meregangkan kartu-kartu yang ada agar menghabiskan seluruh lebar kontainer tanpa menyisakan ruang hampa di sebelah kanan.

Dengan satu baris kode ini, galerimu dijamin tampil sempurna di semua ukuran layar tanpa satu pun baris `@media` query tambahan!
