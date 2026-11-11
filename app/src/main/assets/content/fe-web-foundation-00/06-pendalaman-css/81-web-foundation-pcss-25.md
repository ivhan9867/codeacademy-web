---
id: web-foundation-pcss-25
courseId: fe-web-foundation-00
order: 81
title: "Box Model: Centering Content dengan Margin Auto"
readingMinutes: 5
section: PENDALAMAN_CSS
codeSnippet: |
  .kontainer {
    width: 800px;
    margin: 0 auto;
  }
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
  - "Membuat kotak berada di tengah halaman atau elemen induk dengan margin: auto"
  - "Memahami bahwa width harus ditentukan agar centering dengan margin auto berfungsi"
---
Salah satu kebutuhan layout yang paling sering muncul adalah membuat sebuah kotak berada **tepat di tengah** halaman, atau di tengah elemen induknya. Caranya cukup sederhana: atur margin kiri dan kanan menjadi `auto`.

```css
.kontainer {
  width: 800px;
  margin: 0 auto;
}
```

Ada satu syarat penting agar teknik ini berfungsi: elemen **harus punya `width` yang ditentukan**. Tanpa `width`, elemen block akan otomatis mengambil lebar penuh dari halaman atau induknya, sehingga tidak ada ruang kosong di kiri-kanan untuk "ditengahkan".

Begini cara kerjanya: setelah kamu menentukan lebar kotak, lalu mengatur margin kiri dan kanan ke `auto`, browser akan menghitung sisa ruang di sisi kiri dan kanan kotak, lalu membaginya **sama rata**. Hasilnya, kotak terlihat berada tepat di tengah.

```css
body {
  margin: 0;
}

.kontainer {
  width: 600px;
  margin: 0 auto;
  background-color: #f0f8ff;
  padding: 24px;
}
```

Pola `width` + `margin: 0 auto` ini sangat umum dipakai untuk membungkus seluruh isi halaman dalam satu kontainer yang rapi dan tidak melebar penuh di layar besar. Banyak website profesional memakai pola ini sebagai pembungkus utama kontennya.

> **Catatan:** Teknik ini hanya menengahkan kotak secara **horizontal**. Untuk menengahkan secara vertikal, dibutuhkan teknik lain seperti Flexbox atau Grid, yang akan dibahas di bagian berikutnya.

## Latihan singkat

1. Buat sebuah `<div class="kontainer">` dengan `width: 700px` dan `margin: 0 auto`. Lebarkan jendela browser dan amati apakah kotak tetap di tengah.
2. Hapus properti `width` dari kontainer tersebut, dan amati apa yang terjadi pada posisinya.
3. Bungkus seluruh isi Halaman Profil dengan satu `<div class="kontainer">` yang diberi `max-width: 800px` dan `margin: 0 auto`.
