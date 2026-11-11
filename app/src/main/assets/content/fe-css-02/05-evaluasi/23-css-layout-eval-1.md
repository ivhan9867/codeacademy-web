---
id: css-layout-eval-1
courseId: fe-css-02
order: 23
title: "Mini Proyek: Halaman Dasbor Web Modern"
readingMinutes: 7
section: EVALUASI
learningObjectives:
  - "Mengintegrasikan seluruh teknik Flexbox dan CSS Grid dalam proyek arsitektur dasbor nyata"
  - "Membangun struktur makro halaman (Sidebar + Main Header + Widget Grid) menggunakan CSS Grid"
  - "Menyusun komponen mikro kartu analitik metrik menggunakan Flexbox"
codeLanguage: css
codeSnippet: |
  .dasbor {
    display: grid;
    grid-template-columns: 220px 1fr;
    grid-template-rows: 60px 1fr;
    grid-template-areas:
      "sidebar header"
      "sidebar konten";
    min-height: 100vh;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Mini Proyek: Dasbor Web</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="dasbor">
        <!-- 1. Sidebar Kiri -->
        <aside class="sidebar">
          <div class="brand">⚡ CodeDash</div>
          <ul class="side-menu">
            <li><a href="#" class="aktif">Ringkasan</a></li>
            <li><a href="#">Proyek</a></li>
            <li><a href="#">Analisis</a></li>
            <li><a href="#">Pengaturan</a></li>
          </ul>
        </aside>

        <!-- 2. Header Atas -->
        <header class="header">
          <div class="sapaan">Selamat Datang, <strong>Rani</strong> 👋</div>
          <div class="user-avatar">RP</div>
        </header>

        <!-- 3. Konten Utama -->
        <main class="konten">
          <h2>Statistik Belajar Minggu Ini</h2>
          
          <div class="grid-metrik">
            <!-- Kartu Metrik 1 -->
            <div class="kartu-metrik">
              <span class="label">Total XP</span>
              <strong class="nilai">1,450 XP</strong>
              <span class="info">+20% dari minggu lalu</span>
            </div>

            <!-- Kartu Metrik 2 -->
            <div class="kartu-metrik">
              <span class="label">Streak Belajar</span>
              <strong class="nilai">12 Hari 🔥</strong>
              <span class="info">Pertahankan terus!</span>
            </div>

            <!-- Kartu Metrik 3 -->
            <div class="kartu-metrik">
              <span class="label">Modul Selesai</span>
              <strong class="nilai">24 Modul</strong>
              <span class="info">Target: 30 Modul</span>
            </div>
          </div>
        </main>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 0;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Tata Letak Makro dengan CSS Grid */
  .dasbor {
    display: grid;
    grid-template-columns: 200px 1fr;
    grid-template-rows: 60px 1fr;
    grid-template-areas:
      "sidebar header"
      "sidebar konten";
    min-height: 100vh;
  }

  .sidebar {
    grid-area: sidebar;
    background-color: #104246;
    color: white;
    padding: 20px 16px;
  }

  .brand {
    font-size: 18px;
    font-weight: bold;
    color: #389b9e;
    margin-bottom: 24px;
  }

  .side-menu {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .side-menu a {
    color: #b5ada4;
    text-decoration: none;
    padding: 8px 12px;
    border-radius: 6px;
    display: block;
    font-size: 14px;
  }

  .side-menu a.aktif, .side-menu a:hover {
    background-color: #1a6368;
    color: white;
  }

  /* 2. Header Atas dengan Flexbox */
  .header {
    grid-area: header;
    background-color: #ffffff;
    border-bottom: 1px solid #e2ddd5;
    padding: 0 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .user-avatar {
    width: 36px;
    height: 36px;
    background-color: #df7b56;
    color: white;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: bold;
    font-size: 13px;
  }

  /* 3. Area Konten & Metrik Grid */
  .konten {
    grid-area: konten;
    padding: 24px;
  }

  .konten h2 {
    margin: 0 0 16px 0;
    font-size: 20px;
  }

  .grid-metrik {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 16px;
  }

  .kartu-metrik {
    background-color: white;
    padding: 16px;
    border-radius: 10px;
    border: 1px solid #e2ddd5;
    display: flex;
    flex-direction: column;
    gap: 6px;
  }

  .label { font-size: 12px; color: #6b655d; }
  .nilai { font-size: 20px; color: #104246; font-weight: bold; }
  .info  { font-size: 11px; color: #287948; font-weight: 500; }
playgroundTask: "Coba ubah tata letak agar responsif di layar ponsel: ubah 'grid-template-columns: 200px 1fr;' menjadi '1fr;', dan jadikan 'grid-template-areas: \"header\" \"konten\";'. Beri '.sidebar { display: none; }'. Jalankan kode dan lihat bagaimana dasbor desktop bertransformasi menjadi versi aplikasi mobile yang bersih!"
---
Saatnya menguji kemampuanmu dengan merakit proyek puncak kelas ini: **Halaman Dasbor Web Modern (*Analytics Dashboard*)**.

Perhatikan bagaimana halaman ini menggabungkan semua materi yang telah kamu pelajari menjadi satu kesatuan yang solid:

![Arsitektur Halaman Dasbor: CSS Grid Makro dan Flexbox Mikro](asset:images/fe-css-02/arsitektur-proyek-dasbor.png)

## Bedah Arsitektur Proyek

### 1. Fondasi Makro: CSS Grid dengan `grid-template-areas`
Kerangka utama halaman dibagi menjadi 2 kolom dan 2 baris:
```css
.dasbor {
  display: grid;
  grid-template-columns: 200px 1fr;
  grid-template-rows: 60px 1fr;
  grid-template-areas:
    "sidebar header"
    "sidebar konten";
}
```
Sidebar mengunci posisi di sebelah kiri setinggi layar penuh (`min-height: 100vh`), sementara Header dan Konten tertata rapi di sebelah kanannya.

### 2. Komponen Mikro: Flexbox
- **Menu Sidebar**: Menggunakan `display: flex; flex-direction: column; gap: 8px;` untuk mengatur jarak vertikal antar-tautan menu secara presisi.
- **Top Header**: Menggunakan `display: flex; justify-content: space-between; align-items: center;` untuk melempar teks sapaan ke kiri dan avatar pengguna ke kanan.
- **Avatar Pengguna**: Menggunakan `display: flex; justify-content: center; align-items: center;` untuk menengahkan inisial teks "RP" tepat di tengah bulatan.
- **Kartu Statistik Metrik**: Menggunakan `repeat(auto-fit, minmax(180px, 1fr))` pada wadah kartu agar otomatis menyesuaikan jumlah kolom, dan Flexbox vertikal di dalam tiap kartu agar label, angka, dan keterangan berbaris rapi.

Kamu baru saja menyusun halaman dasbor web kelas industri dengan standar kode yang bersih, modern, dan tanpa satu pun trik float atau tabel kuno!
