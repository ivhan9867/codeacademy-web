---
id: css-layout-lanjutan-11
courseId: fe-css-02
order: 30
title: "Arsitektur Hibrida: Memadukan Flexbox & CSS Grid"
readingMinutes: 9
section: LANJUTAN
learningObjectives:
  - "Merancang kerangka halaman penuh menggunakan arsitektur hibrida Grid (makro) dan Flexbox (mikro)"
  - "Menggunakan checklist keputusan cepat untuk memilih Grid atau Flexbox pada tiap komponen UI"
  - "Mengintegrasikan navbar, sidebar, galeri kartu, dan daftar tag pill dalam satu halaman terpadu"
codeLanguage: css
codeSnippet: |
  /* Makro Halaman: CSS Grid */
  .app-shell {
    display: grid;
    grid-template-columns: 200px 1fr;
    grid-template-rows: auto 1fr auto;
    gap: 16px;
  }

  /* Mikro Komponen: Flexbox */
  .top-nav {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Arsitektur Hibrida: Grid + Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="app-shell">
        <!-- Area Header: Menggunakan Flexbox -->
        <header class="app-header">
          <strong class="brand">DevBoard</strong>
          <nav class="nav-menu">
            <a href="#">Ringkasan</a>
            <a href="#">Proyek</a>
            <a href="#">Pengaturan</a>
          </nav>
        </header>

        <!-- Area Sidebar: Grid Track Kiri -->
        <aside class="app-sidebar">
          <h4>Filter Topik</h4>
          <div class="pill-group">
            <span class="pill">Grid</span>
            <span class="pill">Flexbox</span>
            <span class="pill">UI Kit</span>
            <span class="pill">Tokens</span>
          </div>
        </aside>

        <!-- Area Konten Utama: Grid di dalamnya & Flexbox di kartu -->
        <main class="app-main">
          <div class="card">
            <h4>Desain Sistem</h4>
            <p>Menyatukan Grid sebagai struktur luar dan Flexbox di dalam komponen.</p>
            <div class="card-actions">
              <span class="status">Aktif</span>
              <button class="btn">Buka</button>
            </div>
          </div>
          <div class="card">
            <h4>Audit Layout</h4>
            <p>Pastikan jarak dan perataan konsisten di semua ukuran layar.</p>
            <div class="card-actions">
              <span class="status">Selesai</span>
              <button class="btn">Buka</button>
            </div>
          </div>
        </main>

        <footer class="app-footer">
          Arsitektur Hibrida CSS Modern &bull; Flexbox &amp; Grid
        </footer>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 12px;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. MAKRO SHELL: Ubah .app-shell menjadi CSS Grid 2 kolom */
  .app-shell {
    /* Tambahkan display: grid; dan grid-template-columns: 180px 1fr; di sini */
    gap: 14px;
  }

  /* 2. MIKRO NAVBAR: Jadikan .app-header menggunakan Flexbox */
  .app-header {
    grid-column: 1 / -1;
    /* Tambahkan display: flex; justify-content: space-between; align-items: center; */
    background-color: #104246;
    color: white;
    padding: 12px 18px;
    border-radius: 10px;
  }

  .nav-menu {
    display: flex;
    gap: 14px;
  }

  .nav-menu a {
    color: #e2f0f1;
    text-decoration: none;
    font-size: 13px;
  }

  .app-sidebar {
    background-color: #e2f0f1;
    padding: 14px;
    border-radius: 10px;
  }

  .app-sidebar h4 {
    margin: 0 0 10px 0;
    color: #104246;
  }

  .pill-group {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  .pill {
    background-color: #1a6368;
    color: white;
    padding: 4px 10px;
    border-radius: 999px;
    font-size: 11px;
    font-weight: bold;
  }

  .app-main {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 12px;
  }

  .card {
    display: flex;
    flex-direction: column;
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 14px;
  }

  .card h4 {
    margin: 0 0 6px 0;
    color: #104246;
  }

  .card p {
    margin: 0 0 14px 0;
    font-size: 13px;
    color: #6b655d;
    flex-grow: 1;
  }

  .card-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #f1ece4;
    padding-top: 10px;
  }

  .status {
    font-size: 12px;
    font-weight: bold;
    color: #df7b56;
  }

  .btn {
    background-color: #104246;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 12px;
    cursor: pointer;
  }

  .app-footer {
    grid-column: 1 / -1;
    text-align: center;
    background-color: #f1ece4;
    padding: 10px;
    border-radius: 8px;
    font-size: 12px;
    color: #6b655d;
  }
playgroundTask: "Lengkapi arsitektur hibrida: (1) Tambahkan 'display: grid; grid-template-columns: 180px 1fr;' pada '.app-shell' untuk kerangka makro halaman. (2) Tambahkan 'display: flex; justify-content: space-between; align-items: center;' pada '.app-header' untuk menyusun logo dan menu navigasi!"
verificationRules:
  - "css:display: grid"
  - "css:display: flex"
---
Pertanyaan terbesar yang sering muncul setelah menguasai Flexbox dan CSS Grid adalah: *"Mana yang harus kupakai?"* Jawabannya bukan memilih salah satu, melainkan **memadukan keduanya dalam satu Arsitektur Hibrida**.

Aturan emas yang dipegang para *UI Engineer* modern sangat sederhana:
> **Gunakan CSS Grid untuk tata letak makro (kerangka halaman), dan gunakan Flexbox untuk tata letak mikro (isi di dalam komponen).**

## Cetak Biru Arsitektur Hibrida (*Page Blueprint*)

Perhatikan bagaimana satu halaman dashboard profesional membagi tugas antara Grid dan Flexbox:

```text
+-----------------------------------------------------------------------+
| HEADER (.app-header)  --> [FLEXBOX] Logo di kiri, menu & CTA di kanan |
+-------------------+---------------------------------------------------+
| SIDEBAR           | MAIN CONTENT (.app-main) --> [CSS GRID]           |
| (.app-sidebar)    | +-----------------------+ +---------------------+ |
|                   | | Kartu 1 (.card)       | | Kartu 2 (.card)     | |
| Tag Pills:        | | [FLEXBOX column]      | | [FLEXBOX column]    | |
| [Grid] [Flexbox]  | | Judul + Deskripsi     | | Judul + Deskripsi   | |
| [UI Kit] [Tokens] | | --------------------- | | ------------------- | |
| [FLEXBOX wrap]    | | [Status]     [Tombol] | | [Status]   [Tombol] | |
|                   | +-----------------------+ +---------------------+ |
+-------------------+---------------------------------------------------+
| FOOTER (.app-footer) --> [GRID span 1 / -1]                           |
+-----------------------------------------------------------------------+
```

## Checklist Keputusan Cepat: Grid vs Flexbox

Gunakan tabel panduan ini setiap kali kamu mulai menulis CSS untuk sebuah blok baru:

| Pertanyaan Desain | Pilih **CSS Grid** | Pilih **Flexbox** |
| :--- | :--- | :--- |
| **Dimensi tata letak?** | 2 Dimensi (baris & kolom serempak) | 1 Dimensi (satu arah baris atau kolom) |
| **Sumber kendali ukuran?** | **Layout-First**: kerangka struktur menentukan ukuran sel | **Content-First**: ukuran konten mendorong ruang |
| **Komponen yang dibangun?** | Page shell (header, sidebar, main), galeri kartu simetris | Navbar, barisan tombol, tag *pill* (`wrap`), isi kartu |
| **Perlu item menumpuk di sel yang sama?** | Ya (`grid-area` / baris & kolom sama) | Tidak (item mengalir berurutan) |
| **Perataan tombol di dasar kartu?** | Tidak langsung | Ya (`flex-direction: column` + `flex-grow: 1` / `margin-top: auto`) |

## Alur Kerja Profesional dalam 3 Langkah

1. **Bungkus halaman dengan Grid** (`.app-shell`) untuk membagi zona Header, Sidebar, Konten Utama, dan Footer menggunakan `grid-template-columns` atau `grid-template-areas`.
2. **Atur galeri berulang dengan Grid** (`.app-main`) memakai `repeat(auto-fit, minmax(..., 1fr))` agar kartu sejajar simetris.
3. **Rapikan detail di dalam tiap zona memakai Flexbox**: barisan tautan di header (`justify-content: space-between`), deretan tag di sidebar (`flex-wrap: wrap`), dan tombol aksi di bawah setiap kartu (`margin-top: auto`).

Dengan menyelesaikan latihan Playground di modul ini, kamu sudah memiliki fondasi lengkap untuk membangun proyek akhir kelas dengan arsitektur web berstandar industri!
