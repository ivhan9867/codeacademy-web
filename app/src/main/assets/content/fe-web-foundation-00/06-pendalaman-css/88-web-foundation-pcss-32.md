---
id: web-foundation-pcss-32
courseId: fe-web-foundation-00
order: 88
title: "Latihan: Menerapkan Jumbotron dan Navigasi pada Elemen Header"
readingMinutes: 9
section: PENDALAMAN_CSS
learningObjectives:
  - "Membuat efek jumbotron pada header dengan padding besar dan latar warna"
  - "Mengubah daftar navigasi menjadi horizontal dengan display dan list-style-type"
  - "Menyesuaikan warna latar dan warna teks navigasi"
---
Elemen `<header>` kita masih terasa polos. Saatnya kita beri efek **jumbotron**: header besar berwarna dengan judul yang menonjol, dilengkapi navigasi yang rapi.

## Membuat efek jumbotron

Buat rule baru dengan selector `.jumbotron`:

```css
.jumbotron {
  font-size: 20px;
  padding: 60px;
  background-color: #00c8eb;
  text-align: center;
  color: white;
}
```

Terapkan class ini dengan membungkus `<h1>` dan `<p>` di dalam `<header>` memakai `<div>`:

```html
<header>
  <div class="jumbotron">
    <h1>Bandung</h1>
    <p>Kota metropolitan terbesar di Provinsi Jawa Barat, sekaligus menjadi ibu kota provinsi tersebut.</p>
  </div>
  <nav>
    <ul>
      <li><a href="#sejarah">Sejarah</a></li>
      <li><a href="#geografis">Geografis</a></li>
      <li><a href="#wisata">Wisata</a></li>
    </ul>
  </nav>
</header>
```

Header sekarang akan tampil dengan padding besar dan latar warna cerah, menyerupai jumbotron pada website-website modern.

## Merapikan navigasi

Selanjutnya, buat navigasi tampil horizontal. Tambahkan rule `nav li`:

```css
nav li {
  display: inline;
  list-style-type: none;
  margin-right: 20px;
}
```

- `display: inline` membuat setiap `<li>` tampil sejajar, tanpa baris baru.
- `list-style-type: none` menghilangkan tanda bullet di depan setiap item.

Lalu beri warna latar pada navigasi dengan rule `nav`:

```css
nav {
  background-color: #00a2c6;
  padding: 5px;
}
```

Terakhir, sesuaikan warna teks tautan navigasi dengan menambahkan `color: white` pada rule `nav a` yang sudah ada dari latihan sebelumnya:

```css
nav a {
  font-size: 18px;
  font-weight: 400;
  text-decoration: none;
  color: white;
}
```

Dengan seluruh langkah ini, header dan navigasi Halaman Profil sudah mulai terlihat cantik dan kohesif, dengan warna yang senada dari jumbotron sampai navigasi.

## Latihan singkat

1. Buat `.jumbotron` dan bungkus `<h1>` + `<p>` di `<header>` dengan `<div class="jumbotron">`.
2. Ubah `<li>` navigasi menjadi `inline` dan hilangkan bullet-nya.
3. Beri warna latar pada `<nav>`, lalu sesuaikan warna teks tautan agar kontras dengan latar tersebut.
