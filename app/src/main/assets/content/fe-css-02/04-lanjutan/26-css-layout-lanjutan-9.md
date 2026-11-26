---
id: css-layout-lanjutan-9
courseId: fe-css-02
order: 28
title: "Strategi Mobile-First, Breakpoints, & Media Queries Modern"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - "Memahami filosofi Mobile-First dan alasan mengapa min-width lebih bersih daripada max-width"
  - "Mengenal daftar Breakpoint standar industri untuk ponsel, tablet, dan layar desktop"
  - "Mengubah struktur Grid dan Flexbox secara dinamis menggunakan aturan @media"
codeLanguage: css
codeSnippet: |
  /* Dasar: Mobile (< 640px) - 1 kolom */
  .grid-artikel {
    display: grid;
    grid-template-columns: 1fr;
    gap: 16px;
  }

  /* Tablet (>= 768px) - 2 kolom */
  @media (min-width: 768px) {
    .grid-artikel {
      grid-template-columns: repeat(2, 1fr);
    }
  }

  /* Desktop (>= 1024px) - 3 kolom */
  @media (min-width: 1024px) {
    .grid-artikel {
      grid-template-columns: repeat(3, 1fr);
    }
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Latihan: Mobile-First & Media Query</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <header class="site-header">
        <h2>Berita Teknologi</h2>
        <span class="tag-mode">Responsif Mobile-First</span>
      </header>

      <section class="grid-artikel">
        <article class="post">
          <h4>1. CSS Grid Level 2</h4>
          <p>Menyusun tata letak kompleks dengan baris dan kolom yang bersih.</p>
        </article>
        <article class="post">
          <h4>2. Mengenal Media Query</h4>
          <p>Menyesuaikan jumlah kolom berdasarkan lebar layar perangkat.</p>
        </article>
        <article class="post">
          <h4>3. Performa Mobile</h4>
          <p>Desain ringan dimulai dari layar sempit lalu berkembang ke desktop.</p>
        </article>
      </section>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  .site-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #104246;
    color: white;
    padding: 12px 18px;
    border-radius: 10px;
    margin-bottom: 18px;
  }

  .site-header h2 {
    margin: 0;
    font-size: 18px;
  }

  .tag-mode {
    background-color: #df7b56;
    padding: 4px 10px;
    border-radius: 14px;
    font-size: 12px;
    font-weight: bold;
  }

  /* Gaya dasar Mobile-First (1 kolom) */
  .grid-artikel {
    display: grid;
    grid-template-columns: 1fr;
    gap: 14px;
  }

  .post {
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 16px;
  }

  .post h4 {
    margin: 0 0 8px 0;
    color: #104246;
  }

  .post p {
    margin: 0;
    font-size: 13px;
    color: #6b655d;
    line-height: 1.5;
  }

  /* Tambahkan @media (min-width: 768px) dan @media (min-width: 1024px) di bawah ini */
playgroundTask: "Tambahkan aturan Media Query Mobile-First di akhir CSS: buat '@media (min-width: 600px)' untuk mengubah '.grid-artikel' menjadi 'grid-template-columns: repeat(2, 1fr);' dan '@media (min-width: 900px)' menjadi 'repeat(3, 1fr);' (atau gunakan breakpoint 768px/1024px). Amati tampilannya di preview!"
verificationRules:
  - "css:@media"
  - "css:grid-template-columns"
---
Lebih dari separuh pengguna internet mengakses website melalui ponsel pintar. Alih-alih mendesain untuk monitor desktop lebar terlebih dahulu lalu bingung memotongnya untuk HP, praktik standar modern menggunakan pendekatan **Mobile-First**.

```text
  PROGRESI MOBILE-FIRST (min-width)
+----------------+     +-------------------------+     +-----------------------------------+
| Mobile < 640px | --> | Tablet >= 768px         | --> | Desktop >= 1024px                 |
| [ Kartu 1 ]    |     | [ Kartu 1 ] [ Kartu 2 ] |     | [ Kartu 1 ] [ Kartu 2 ] [ Krt 3 ] |
| [ Kartu 2 ]    |     | [ Kartu 3 ]             |     |                                   |
| [ Kartu 3 ]    |     +-------------------------+     +-----------------------------------+
+----------------+
```

## Tabel Breakpoint Standar Industri

**Breakpoint** adalah titik lebar layar saat tata letak beradaptasi. Kamu tidak perlu membuat *media query* untuk setiap merek HP; cukup gunakan rentang umum berikut:

| Kategori Perangkat | Rentang Lebar Layar | Media Query (`min-width`) | Pola Layout Umum |
| :--- | :--- | :--- | :--- |
| **Mobile** *(Dasar)* | `< 640px` | *(Tanpa `@media` — gaya default)* | 1 kolom menumpuk ke bawah, menu kompak |
| **Tablet Kecil / Landscape HP** | `>= 640px` | `@media (min-width: 640px)` | Mulai 2 kolom untuk kartu ringkas |
| **Tablet** | `>= 768px` | `@media (min-width: 768px)` | Grid 2 kolom, navbar horizontal tampil penuh |
| **Desktop** | `>= 1024px` | `@media (min-width: 1024px)` | Grid 3–4 kolom + sidebar samping muncul |
| **Layar Lebar** | `>= 1280px` | `@media (min-width: 1280px)` | `max-width` kontainer utama agar teks tetap nyaman dibaca |

> **Wajib Ada di HTML**: Selalu sertakan `<meta name="viewport" content="width=device-width, initial-scale=1.0" />` di dalam `<head>`. Tanpanya, browser ponsel akan mengecilkan versi desktop sehingga `@media` tidak bekerja semestinya!

## Mengapa `min-width` Lebih Unggul daripada `max-width`?

| Aspek | Pendekatan Mobile-First (`min-width`) | Pendekatan Desktop-First (`max-width`) |
| :--- | :--- | :--- |
| **Urutan CSS** | Mulai sederhana (1 kolom), lalu **menambah** fitur di layar lebar | Mulai kompleks (banyak kolom), lalu **menimpa/reset** di layar kecil |
| **Kebersihan Kode** | Minim deklarasi reset (`float: none`, hapus margin, dll.) | Penuh kode penimpa untuk membatalkan layout desktop |
| **Performa Ponsel** | HP tidak perlu memproses aturan grid berat milik desktop | HP harus membaca aturan desktop dulu sebelum menimpanya |

Contoh penulisan **Media Query** yang bersih dengan pola Mobile-First:

```css
/* 1. Gaya dasar untuk semua layar (Mobile pertama) */
.layout-utama {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
}

/* 2. Tambahan aturan khusus Tablet ke atas */
@media (min-width: 768px) {
  .layout-utama {
    grid-template-columns: 240px 1fr; /* Sidebar muncul di kiri */
  }
}

/* 3. Penyesuaian Desktop ke atas */
@media (min-width: 1024px) {
  .layout-utama {
    grid-template-columns: 280px 1fr 220px; /* 3 kolom penuh */
  }
}
```

Perhatikan bagaimana aturan yang ditulis belakangan otomatis mewarisi `display: grid` dan `gap: 16px` tanpa perlu ditulis ulang. Cobalah melengkapi blok `@media` di Playground!
