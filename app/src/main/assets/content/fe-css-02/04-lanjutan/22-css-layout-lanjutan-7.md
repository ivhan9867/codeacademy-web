---
id: css-layout-lanjutan-7
courseId: fe-css-02
order: 26
title: "Latihan Praktik: Galeri Kartu Produk E-Commerce Responsif"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Mengombinasikan CSS Grid (untuk makro galeri) dan Flexbox (untuk isi internal kartu)"
  - "Membuat kartu e-commerce yang memiliki tombol 'Beli' selalu sejajar rata di dasar kartu"
  - "Menghasilkan komponen antarmuka profesional yang siap pakai untuk proyek nyata"
codeLanguage: css
codeSnippet: |
  .galeri-produk {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
  }

  .kartu-produk {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: Galeri Produk E-Commerce</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Katalog Kursus Pilihan</h2>
      <div class="galeri-produk">
        <!-- Kartu 1 -->
        <div class="kartu">
          <div class="badge">Terpopuler</div>
          <h3>Belajar HTML5 Semantik</h3>
          <p>Membangun struktur dokumen web modern dan ramah SEO dari dasar.</p>
          <div class="footer-kartu">
            <span class="harga">Gratis</span>
            <button class="btn-beli">Daftar</button>
          </div>
        </div>

        <!-- Kartu 2 (Deskripsi Pendek) -->
        <div class="kartu">
          <div class="badge">Baru</div>
          <h3>CSS Flexbox & Grid</h3>
          <p>Kuasai tata letak responsif masa kini.</p>
          <div class="footer-kartu">
            <span class="harga">Rp 0</span>
            <button class="btn-beli">Daftar</button>
          </div>
        </div>

        <!-- Kartu 3 (Deskripsi Panjang) -->
        <div class="kartu">
          <div class="badge">Lanjutan</div>
          <h3>JavaScript & DOM Interaktif</h3>
          <p>Memberi nyawa interaktif pada antarmuka web, event listener, dan manipulasi elemen.</p>
          <div class="footer-kartu">
            <span class="harga">Gratis</span>
            <button class="btn-beli">Daftar</button>
          </div>
        </div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Atur galeri luar dengan CSS Grid */
  .galeri-produk {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
  }

  /* 2. Jadikan kartu sebagai Flexbox vertikal agar tombol selalu di dasar */
  .kartu {
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    /* Tambahkan display: flex; flex-direction: column; di sini */
  }

  .badge {
    align-self: flex-start;
    background-color: #e2f0f1;
    color: #1a6368;
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 11px;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .kartu h3 {
    margin: 0 0 8px 0;
    color: #104246;
    font-size: 16px;
  }

  .kartu p {
    color: #6b655d;
    font-size: 13px;
    line-height: 1.4;
    margin: 0 0 16px 0;
    /* Beri flex-grow: 1 agar paragraf ini mendorong footer kartu ke bawah! */
  }

  /* 3. Footer kartu (Harga & Tombol) */
  .footer-kartu {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #f1ece4;
    padding-top: 12px;
  }

  .harga {
    font-weight: bold;
    color: #df7b56;
    font-size: 14px;
  }

  .btn-beli {
    background-color: #1a6368;
    color: white;
    border: none;
    padding: 6px 14px;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
  }
playgroundTask: "Perhatikan di Live Preview: tombol Daftar belum rata di bawah karena panjang paragraf kartu berbeda-beda. Tambahkan: (1) 'display: flex; flex-direction: column;' pada '.kartu'. (2) 'flex-grow: 1;' pada '.kartu p'. Tekan 'Jalankan' dan saksikan footer kartu otomatis terkunci rata di dasar!"
---
Saatnya membuktikan kolaborasi sejati antara **CSS Grid** dan **Flexbox** dalam proyek antarmuka dunia nyata: **Galeri Kartu Produk E-Commerce**.

Banyak desainer pemula mengalami masalah klasik pada kartu produk:
- Kartu dengan teks deskripsi panjang menjadi sangat tinggi.
- Kartu dengan teks deskripsi pendek menjadi bantet.
- Akibatnya, tombol "Beli" atau "Daftar" di bawah kartu berada pada ketinggian yang naik-turun berantakan!

```text
Grid (Galeri Luar) -> [ Flexbox Card (column) ]
                      | [  Gambar Produk    ] |
                      | [  Judul & Harga    ] |
                      | [  Tombol Beli      ] | (margin-top: auto)
```
## Resep Kolaborasi Juara

Kita membagi tugas secara harmonis:

1. **Wadah Luar Menggunakan CSS Grid**:
   ```css
   .galeri-produk {
     display: grid;
     grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
     gap: 16px;
   }
   ```
   Grid memastikan bahwa seluruh kartu produk memiliki **lebar dan tinggi yang seragam secara otomatis**, berapapun jumlah kolom yang terbentuk di layar.

2. **Isi Kartu Menggunakan Flexbox Vertikal**:
   ```css
   .kartu {
     display: flex;
     flex-direction: column; /* Mengalir ke bawah */
   }

   .kartu p {
     flex-grow: 1; /* Trik kunci: paragraf meregang mengambil sisa ruang */
   }
   ```
   Dengan memberikan `flex-grow: 1` pada teks paragraf, elemen paragraf akan meregang dan **mendorong elemen `.footer-kartu` tepat ke lantai paling bawah** kartu.

3. **Footer Kartu Menggunakan Flexbox Horizontal**:
   ```css
   .footer-kartu {
     display: flex;
     justify-content: space-between; /* Harga di kiri, tombol di kanan */
     align-items: center;
   }
   ```

Hasilnya? Antarmuka katalog produk yang rapi, simetris, dan sangat memanjakan mata di perangkat apa pun!
