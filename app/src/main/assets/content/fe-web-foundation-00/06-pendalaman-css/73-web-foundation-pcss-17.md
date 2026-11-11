---
id: web-foundation-pcss-17
courseId: fe-web-foundation-00
order: 73
title: "Warna untuk Teks dan Latar Belakang"
readingMinutes: 4
section: PENDALAMAN_CSS
codeSnippet: |
  p { color: gray; }
  p { color: #666666; }
  p { color: #666; }
  p { color: rgb(102, 102, 102); }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Area Latihan</title>
    </head>
    <body>
      <header>
        <h1>Judul Contoh</h1>
        <p>Paragraf contoh untuk mencoba properti CSS di atas.</p>
      </header>
      <nav>
        <ul>
          <li><a href="#">Beranda</a></li>
          <li><a href="#">Tentang</a></li>
          <li><a href="#">Kontak</a></li>
        </ul>
      </nav>
      <main>
        <div class="box">
          <span>Teks di dalam span</span>
          <p>Paragraf lain di dalam sebuah div.</p>
          <img src="https://placehold.co/150" alt="Contoh gambar" />
        </div>
      </main>
    </body>
  </html>
learningObjectives:
  - "Mengubah warna teks dengan color"
  - "Mengubah warna latar dengan background-color dan memahami nilai defaultnya"
---
## Warna untuk teks dan latar belakang

Sekarang kita terapkan warna pada elemen. Ada dua properti utama.

### Warna teks: `color`

Properti `color` mengubah warna teks. Nilainya bisa berupa nama warna atau numeric value. Keempat contoh ini menghasilkan warna abu-abu yang sama:

```css
p { color: gray; }
p { color: #666666; }
p { color: #666; }
p { color: rgb(102, 102, 102); }
```

Seperti properti teks lainnya, `color` diwariskan ke elemen turunan. Jadi kamu bisa mengubah warna seluruh teks di halaman cukup dengan menerapkannya pada `body`:

```css
body {
  color: #333;
}
```

### Warna latar: `background-color`

CSS memperlakukan setiap elemen seperti kotak. Properti `background-color` mengatur warna latar kotak tersebut. Seperti `color`, nilainya bisa berupa nama atau numeric value.

Secara default, `background-color` bernilai transparan. Banyak browser menampilkan latar putih, tetapi pengguna bisa mengubah pengaturan ini. Agar tampilan halaman konsisten, tetapkan latar secara eksplisit pada `body`:

```css
body {
  background-color: white;
}
```

Saat memberi `background-color` pada elemen, biasanya kamu juga menambahkan `padding`. Tanpa padding, teks akan menempel kaku ke batas warna latar.

```css
.kartu {
  background-color: #f0f8ff;
  padding: 16px;
}
```

## Contoh Kode Lengkap Kombinasi Warna Teks dan Latar Belakang

Berikut contoh dokumen HTML lengkap yang memadukan pengaturan warna teks dan latar belakang secara harmonis:

```html
<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8">
  <title>Pengumuman Beasiswa</title>
  <style>
    body {
      font-family: sans-serif;
      background-color: #F8FAFC; /* Latar halaman abu-abu sangat muda */
      color: #1E293B;            /* Teks default abu-abu gelap */
      margin: 20px;
    }

    .banner-pengumuman {
      background-color: #1E40AF; /* Latar biru tua */
      color: #FFFFFF;            /* Teks putih kontras tinggi */
      padding: 20px;
      border-radius: 8px;
    }

    .badge-status {
      background-color: #FEF08A; /* Kuning muda cerah */
      color: #854D0E;            /* Cokelat keemasan gelap agar mudah dibaca */
      padding: 4px 8px;
      border-radius: 4px;
      font-weight: bold;
    }

    .catatan {
      background-color: #E2E8F0; /* Abu-abu netral */
      color: #475569;
      padding: 12px;
      border-radius: 6px;
    }
  </style>
</head>
<body>
  <div class="banner-pengumuman">
    <span class="badge-status">Buka Pendaftaran</span>
    <h2>Program Beasiswa Coding Indonesia 2026</h2>
    <p>Dapatkan bimbingan intensif front-end web development dari para mentor industri berpengalaman.</p>
    <div class="catatan">
      Batas akhir pengumpulan berkas: 30 November 2026.
    </div>
  </div>
</body>
</html>
```

## Penjelasan Hasil di Layar Browser

Saat halaman di atas dirender oleh browser:
1. **Dasar Halaman Bersih**: `<body>` memiliki latar abu-abu sejuk (`#F8FAFC`) dengan teks dasar berwarna abu-abu gelap (`#1E293B`). Hal ini menjaga kontras teks tetap tajam namun tidak menyilaukan.
2. **Kontras Tinggi pada Banner**: Kotak `.banner-pengumuman` tampil menonjol dengan latar biru tua (`#1E40AF`) dan teks putih (`#FFFFFF`), menghasilkan rasio kontras yang sangat tinggi sehingga langsung memikat perhatian mata pengunjung.
3. **Badge Status yang Memikat**: Elemen `.badge-status` menggunakan kombinasi latar kuning muda (`#FEF08A`) dengan teks cokelat gelap (`#854D0E`). Kombinasi ini menghindari kesalahan umum developer pemula yang sering menulis teks putih di atas latar kuning terang (yang membuat teks sulit dibaca).
4. **Ruang Napas yang Nyaman**: Berkat adanya `padding` pada setiap elemen yang diberi `background-color`, teks tidak menyentuh garis pinggir kotak latar belakang sehingga nyaman dan enak dipandang.
