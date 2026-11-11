---
id: web-foundation-css-4
courseId: fe-web-foundation-00
order: 53
title: "Latihan: Styling Halaman Profil"
readingMinutes: 6
section: CSS_DASAR
codeLanguage: css
codeSnippet: |
  body {
    font-family: sans-serif;
  }

  h2, h3 {
    color: #00a2c6;
  }

  footer {
    padding: 20px;
    color: white;
    background-color: #00a2c6;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Halaman Profil</title>
    </head>
    <body>
      <h2>Rani Pratiwi</h2>
      <h3>Tentang Saya</h3>
      <p>Saya suka membaca dan belajar hal baru.</p>
      <footer>© 2026 Halaman Profil Rani</footer>
    </body>
  </html>
learningObjectives:
  - "Membuat file CSS terpisah dan menghubungkannya ke halaman profil yang sudah dibuat"
  - "Menerapkan styling dasar: font, warna teks, dan warna latar belakang"
---
Saatnya mempercantik halaman profil Rani yang sudah kamu susun di modul-modul HTML sebelumnya.

## Alur latihan

1. Buka halaman profil terakhirmu di Playground
2. Tambahkan CSS baru: atur `font-family` ke `sans-serif` untuk seluruh `<body>`
3. Beri warna pada `<h2>` dan `<h3>` pakai kode hex, misalnya `#00a2c6`
4. Beri `<footer>` warna latar (`background-color`), warna teks putih, dan sedikit `padding`
5. Hubungkan CSS ke HTML lewat `<link rel="stylesheet" href="...">` di `<head>` — tanpa ini, styling-mu gak akan muncul sama sekali walau kodenya sudah benar
6. Jalankan dan bandingkan tampilan sebelum-sesudah

## Bedah kode

Lihat `codeSnippet`: `h2` dan `h3` sama-sama diberi warna `#00a2c6`. Daripada nulis dua rule terpisah, keduanya digabung jadi satu pakai koma — ini namanya **group selector**, cara meringkas kode kalau beberapa elemen butuh styling yang sama persis.

## Kesalahan yang sering terjadi

- Nulis rule CSS tapi lupa `<link>` ke HTML — hasilnya halaman tetap polos meski file CSS-nya sudah benar
- Lupa titik koma di akhir declaration, bikin satu rule rusak diam-diam
- Nulis `color: #00a2c6` tapi lupa pagar (`#`) di depan kode warnanya

## Coba sendiri

Ganti warna `#00a2c6` dengan kode hex lain, atau coba pakai nama warna biasa seperti `color: teal;` — bandingkan mana yang lebih gampang buat kamu kontrol presisinya.
