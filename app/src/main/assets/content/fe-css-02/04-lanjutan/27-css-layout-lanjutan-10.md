---
id: css-layout-lanjutan-10
courseId: fe-css-02
order: 29
title: "CSS Variables (--var) & Sistem Spacing Konsisten"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Mendeklarasikan Design Tokens berupa jarak, warna, dan radius di dalam selector :root"
  - "Menggunakan fungsi var() untuk menjaga konsistensi gap, padding, dan sudut kartu"
  - "Mengubah jumlah kolom CSS Grid secara dinamis di dalam Media Query hanya dengan memperbarui nilai variabel"
codeLanguage: css
codeSnippet: |
  :root {
    --space-sm: 8px;
    --space-md: 16px;
    --space-lg: 24px;
    --radius-card: 12px;
    --grid-cols: 1;
  }

  .katalog {
    display: grid;
    grid-template-columns: repeat(var(--grid-cols), 1fr);
    gap: var(--space-md);
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: CSS Variables & Spacing</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Katalog Berbasis Design Tokens</h2>
      <div class="katalog">
        <div class="item-kartu">
          <span class="label">Token A</span>
          <h4>Spacing Konsisten</h4>
          <p>Semua jarak memakai skala kelipatan 8px dari :root.</p>
        </div>
        <div class="item-kartu">
          <span class="label">Token B</span>
          <h4>Warna Terpusat</h4>
          <p>Ubah satu baris di :root, seluruh tema ikut berubah serempak.</p>
        </div>
        <div class="item-kartu">
          <span class="label">Token C</span>
          <h4>Kolom Dinamis</h4>
          <p>Jumlah kolom grid dikontrol variabel --grid-cols.</p>
        </div>
      </div>
    </body>
  </html>
starterCss: |
  /* 1. Definisikan Design Tokens di :root di sini! */
  /* Contoh:
  :root {
    --space-sm: 8px;
    --space-md: 16px;
    --color-primary: #104246;
    --color-accent: #df7b56;
    --radius-md: 12px;
    --grid-cols: 2;
  }
  */

  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  h2 {
    color: var(--color-primary, #104246);
    margin-bottom: var(--space-md, 16px);
  }

  .katalog {
    display: grid;
    grid-template-columns: repeat(var(--grid-cols, 1), 1fr);
    gap: var(--space-md, 16px);
  }

  .item-kartu {
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: var(--radius-md, 10px);
    padding: var(--space-md, 16px);
  }

  .label {
    display: inline-block;
    background-color: var(--color-accent, #df7b56);
    color: white;
    padding: 4px 10px;
    border-radius: 999px;
    font-size: 11px;
    font-weight: bold;
    margin-bottom: var(--space-sm, 8px);
  }

  .item-kartu h4 {
    margin: 0 0 var(--space-sm, 8px) 0;
    color: var(--color-primary, #104246);
  }

  .item-kartu p {
    margin: 0;
    font-size: 13px;
    color: #6b655d;
    line-height: 1.5;
  }
playgroundTask: "Aktifkan blok ':root { ... }' di bagian paling atas CSS dengan nilai variabel seperti '--space-md: 20px;', '--color-primary: #1a6368;', '--radius-md: 14px;', dan '--grid-cols: 2;'. Pastikan properti di bawahnya memanggil 'var(...)', lalu klik Jalankan untuk melihat perubahan serempak!"
verificationRules:
  - "css::root"
  - "css:var("
---
Bayangkan kamu sudah menulis 300 baris CSS dengan jarak `padding: 16px`, `gap: 16px`, dan warna utama `#104246` tersebar di puluhan selector. Tiba-tiba desainer meminta semua jarak diperlebar menjadi `20px` dan warna utama diganti. Mengubahnya satu per satu sangat melelahkan dan rawan terlewat!

Di sinilah **CSS Custom Properties** (sering disebut **CSS Variables**) menjadi tulang punggung sistem desain modern.

```text
                       +----------------------------+
                       | :root (Sumber Kebenaran)   |
                       | --space-md: 16px;          |
                       | --grid-cols: 2;            |
                       +-------------+--------------+
                                     |
             +-----------------------+-----------------------+
             v                       v                       v
    .navbar { gap: var(...) }  .grid { repeat(var(...)) }  .card { padding: var(...) }
```

## 1. Mendefinisikan Token di `:root`

Selector `:root` merujuk pada elemen tertinggi dokumen (`<html>`), sehingga variabel di dalamnya bisa diakses di mana saja secara global. Nama variabel **wajib diawali dua tanda hubung (`--`)**:

```css
:root {
  /* Skala Spacing (Sistem kelipatan 4px / 8px) */
  --space-xs: 4px;
  --space-sm: 8px;
  --space-md: 16px;
  --space-lg: 24px;
  --space-xl: 32px;

  /* Palet Warna & Sudut */
  --color-primary: #104246;
  --color-accent: #df7b56;
  --radius-card: 12px;
}
```

## 2. Memanggil Variabel dengan `var()` & Nilai Cadangan

Untuk memakai token tersebut di dalam `gap`, `padding`, atau warna, gunakan fungsi `var(--nama-variabel)`. Kamu juga bisa memberi nilai cadangan (*fallback*) di parameter kedua apabila variabel belum terdefinisi:

```css
.kartu {
  padding: var(--space-md);
  border-radius: var(--radius-card, 8px); /* Pakai 8px jika --radius-card tidak ada */
}
```

## 3. Trik Cerdas: Mengontrol CSS Grid dengan Variabel di `@media`

Alih-alih menulis ulang deklarasi panjang `grid-template-columns` di setiap *breakpoint*, kamu cukup mengubah **satu variabel angka** di dalam Media Query:

```css
:root {
  --grid-cols: 1;
}

.galeri {
  display: grid;
  grid-template-columns: repeat(var(--grid-cols), 1fr);
  gap: var(--space-md);
}

@media (min-width: 768px) {
  :root {
    --grid-cols: 2; /* Otomatis menjadi 2 kolom! */
  }
}

@media (min-width: 1024px) {
  :root {
    --grid-cols: 3; /* Otomatis menjadi 3 kolom! */
  }
}
```

Menggunakan sistem *Design Tokens* berbasis `:root` dan `var()` membuat kode layout CSS kamu rapi, seragam, mudah dirawat dalam tim besar, dan siap diadaptasi ke tema gelap (*dark mode*) di masa depan!
