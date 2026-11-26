---
id: css-layout-inti-10
courseId: fe-css-02
order: 19
title: "Pola Nyata Flexbox: Media Object, Card Footer, & Sticky Footer"
readingMinutes: 8
section: INTI
learningObjectives:
  - "Membangun pola Media Object klasik untuk komentar, notifikasi, dan profil pengguna"
  - "Menyelaraskan posisi Card Footer agar selalu menempel rapi di dasar kartu memakai margin-top: auto"
  - "Mengimplementasikan Sticky Footer halaman penuh menggunakan min-height: 100vh dan flex-direction: column"
codeLanguage: css
codeSnippet: |
  .page-shell {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }

  .page-footer {
    margin-top: auto;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Pola Nyata Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="page-shell">
        <header class="top-bar">Portal Belajar Web</header>

        <main class="main-area">
          <!-- Pola 1: Media Object -->
          <article class="media-object">
            <div class="avatar">RA</div>
            <div class="media-body">
              <h4>Rina Andriani</h4>
              <p>Trik margin-top: auto sangat membantu merapikan footer kartu yang panjang teksnya berbeda!</p>
            </div>
          </article>

          <!-- Pola 2: Kartu dengan Footer Menempel di Bawah -->
          <div class="card-row">
            <div class="card">
              <h4>Paket Dasar</h4>
              <p>Cocok untuk pemula.</p>
              <button class="card-action">Pilih Paket</button>
            </div>
            <div class="card">
              <h4>Paket Lengkap</h4>
              <p>Berisi akses penuh proyek studi kasus, latihan interaktif, dan evaluasi akhir.</p>
              <button class="card-action">Pilih Paket</button>
            </div>
          </div>
        </main>

        <!-- Pola 3: Sticky Footer -->
        <footer class="page-footer">
          &copy; 2026 CodeAcademy Indonesia
        </footer>
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

  /* 1. Sticky Footer Shell: lengkapi display: flex dan flex-direction: column */
  .page-shell {
    min-height: 100vh;
    /* Tambahkan display: flex; flex-direction: column; di sini */
  }

  .top-bar {
    background-color: #104246;
    color: white;
    padding: 14px 20px;
    font-weight: bold;
  }

  .main-area {
    padding: 20px;
    flex: 1;
  }

  /* 2. Media Object */
  .media-object {
    display: flex;
    align-items: flex-start;
    gap: 14px;
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 14px;
    margin-bottom: 20px;
  }

  .avatar {
    width: 44px;
    height: 44px;
    border-radius: 50%;
    background-color: #df7b56;
    color: white;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .media-body h4 {
    margin: 0 0 4px 0;
    color: #104246;
  }

  .media-body p {
    margin: 0;
    font-size: 13px;
    line-height: 1.5;
  }

  /* 3. Card Footer Sejajar */
  .card-row {
    display: flex;
    gap: 14px;
  }

  .card {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 16px;
  }

  .card h4 {
    margin: 0 0 6px 0;
    color: #104246;
  }

  .card p {
    margin: 0 0 14px 0;
    font-size: 13px;
    color: #6b655d;
  }

  .card-action {
    margin-top: auto;
    background-color: #1a6368;
    color: white;
    border: none;
    padding: 8px 12px;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
  }

  .page-footer {
    margin-top: auto;
    background-color: #e2f0f1;
    color: #104246;
    text-align: center;
    padding: 12px;
    font-size: 13px;
    font-weight: bold;
  }
playgroundTask: "Lihat '.page-shell' di CSS: tambahkan 'display: flex;' dan 'flex-direction: column;' agar footer halaman langsung turun menempel ke dasar layar (Sticky Footer). Coba juga ubah panjang teks di salah satu kartu untuk melihat bagaimana '.card-action' dengan 'margin-top: auto' selalu rata!"
verificationRules:
  - "css:display: flex"
  - "css:flex-direction"
---
Di proyek web sungguhan, kamu akan berulang kali menemukan tiga pola komponen klasik: **Media Object**, **Card Footer Sejajar**, dan **Sticky Footer**. Ketiganya bisa diselesaikan secara elegan dengan kombinasi `flex-direction: column`, `flex-shrink: 0`, dan `margin-top: auto`.

```text
1) MEDIA OBJECT                2) CARD FOOTER AUTO           3) STICKY FOOTER (100vh)
+-----------------------+      +-----------+ +-----------+   +------------------------+
| [IMG] Judul Komentar  |      | Judul A   | | Judul B   |   | Header                 |
| (fix) Isi teks yang   |      | Pendek.   | | Teks ini  |   +------------------------+
|       panjang & rapi  |      |           | | lebih pjg |   | Main Content           |
+-----------------------+      | [Tombol]  | | [Tombol]  |   | (ruang sisa otomatis)  |
                               +-----------+ +-----------+   +------------------------+
                                                             | Footer (nempel bawah)  |
                                                             +------------------------+
```

## Pola 1: Media Object (Avatar + Konten)

Komponen komentar media sosial, item notifikasi, atau daftar kontak selalu terdiri dari satu gambar/ikon berukuran tetap di kiri dan blok teks fleksibel di kanan.

```css
.media-object {
  display: flex;
  align-items: flex-start; /* Jaga avatar tetap di atas meski teks panjang */
  gap: 14px;
}

.avatar {
  width: 48px;
  height: 48px;
  flex-shrink: 0; /* KRUSIAL: cegah avatar gepeng saat teks kanan panjang! */
}

.media-body {
  flex: 1; /* Ambil seluruh sisa lebar yang tersedia */
}
```

Tanpa `flex-shrink: 0`, teks panjang di sebelah kanan akan menekan avatar hingga bentuk lingkarannya penyok menjadi lonjong.

## Pola 2: Card Footer Rata Bawah dengan `margin-top: auto`

Saat beberapa kartu berjejer dalam satu baris, panjang deskripsi tiap kartu hampir selalu berbeda. Agar tombol aksi di dasar kartu selalu segaris:

```css
.card {
  display: flex;
  flex-direction: column; /* Susun isi kartu dari atas ke bawah */
}

.card-action {
  margin-top: auto; /* Dorong tombol ke dasar kartu dengan menyerap ruang sisa di atasnya */
}
```

Mengapa `margin-top: auto` sangat hebat di Flexbox? Karena dalam mode `flex-direction: column`, margin vertikal bernilai `auto` akan mengambil **seluruh sisa ruang kosong** antara paragraf terakhir dan tombol, lalu mengunci tombol di bagian terbawah kartu.

## Pola 3: Sticky Footer (*Footer Selalu di Dasar Layar*)

Halaman yang isinya pendek (seperti halaman login atau halaman "Tentang Kami") sering kali membuat elemen `<footer>` melayang tanggung di tengah layar. Solusi Flexbox modern hanya butuh 4 baris:

```css
.page-shell {
  min-height: 100vh;      /* Tinggi minimal sama dengan tinggi layar (Viewport Height) */
  display: flex;
  flex-direction: column;
}

.main-area {
  flex: 1;                /* Konten utama mengisi sisa ruang; otomatis mendorong footer ke dasar */
}
```

Dengan kombinasi `min-height: 100vh` dan `flex: 1` (atau `margin-top: auto` pada `<footer>`), footer akan menempel rapi di dasar layar saat konten sedikit, dan tetap ikut turun secara normal saat konten halaman bertambah panjang!

Buka Playground dan lengkapi kode `.page-shell` untuk menguji ketiganya sekaligus!
