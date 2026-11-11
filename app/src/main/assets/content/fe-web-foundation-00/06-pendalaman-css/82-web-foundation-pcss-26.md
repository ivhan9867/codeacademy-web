---
id: web-foundation-pcss-26
courseId: fe-web-foundation-00
order: 82
title: "Display Roles: Inline, Block, Inline-Block, dan None"
readingMinutes: 9
section: PENDALAMAN_CSS
codeSnippet: |
  span {
    display: block;
  }

  a {
    display: inline-block;
    width: 120px;
    padding: 8px;
  }
codeLanguage: css
starterHtml: |
  <nav>
    <ul>
      <li><a href="#">Beranda</a></li>
      <li><a href="#">Tentang</a></li>
      <li><a href="#">Kontak</a></li>
    </ul>
  </nav>
learningObjectives:
  - "Mengingat kembali sifat elemen inline dan block dari modul HTML"
  - "Mengubah perilaku tampilan elemen dengan properti display"
  - "Menggunakan display: inline-block untuk menggabungkan sifat inline dan block"
  - "Menyembunyikan elemen dari halaman dengan display: none"
---
Di modul HTML sebelumnya, kamu sudah mengenal bahwa setiap elemen HTML punya salah satu dari dua sifat tampilan bawaan: **inline** atau **block**. Sekarang kita bahas lebih dalam, karena CSS memungkinkan kita **mengubah** sifat bawaan itu.

## Mengingat sifat inline dan block

**Inline element** (seperti `<span>`, `<a>`, `<strong>`):
- Tidak membuat baris baru ketika muncul.
- Lebar dan tingginya mengikuti ukuran kontennya, dan **tidak bisa diubah** dengan `width`/`height`.
- Margin dan padding hanya berpengaruh secara **horizontal**, tidak memengaruhi jarak vertikal ke elemen lain.

**Block element** (seperti `<div>`, `<p>`, `<h1>`):
- Selalu membuat baris baru, sehingga elemen berikutnya turun ke bawah.
- Jika lebar tidak ditentukan, elemen akan memenuhi lebar penuh dari induknya.
- Dimensinya (`width`, `height`) bisa diatur bebas.
- Bisa berisi elemen HTML lain di dalamnya.

## Mengubah sifat dengan properti display

Properti `display` memungkinkan kita mengubah sifat bawaan ini. Nilainya antara lain:

| Nilai | Efek |
|---|---|
| `inline` | Elemen block jadi berperilaku seperti inline |
| `block` | Elemen inline jadi berperilaku seperti block |
| `inline-block` | Tidak membuat baris baru (seperti inline), tetapi dimensinya bisa diatur (seperti block) |
| `none` | Elemen disembunyikan sepenuhnya dari halaman |

```css
span {
  display: block;
}

a {
  display: inline-block;
  width: 120px;
  padding: 8px;
}
```

## Kasus penggunaan: navigasi horizontal

Salah satu penggunaan `display` yang paling umum adalah membuat navigasi menjadi horizontal. Daftar navigasi biasanya dibuat dengan `<ul>` dan `<li>`, yang keduanya bersifat block. Akibatnya, setiap item menu akan tampil di baris sendiri-sendiri.

```html
<nav>
  <ul>
    <li><a href="#">Beranda</a></li>
    <li><a href="#">Tentang</a></li>
    <li><a href="#">Kontak</a></li>
  </ul>
</nav>
```

Dengan mengubah `<li>` menjadi `inline`, item-item menu akan tampil sejajar dalam satu baris:

```css
nav li {
  display: inline;
  margin-right: 16px;
}
```

Jika kamu butuh menambahkan `padding` vertikal pada setiap item (misalnya untuk memperbesar area klik), gunakan `inline-block` alih-alih `inline`, karena `inline` tidak mendukung pengaturan dimensi:

```css
nav li {
  display: inline-block;
  padding: 8px 16px;
}
```

## Menyembunyikan elemen dengan display: none

Nilai `none` membuat elemen hilang sepenuhnya, seolah tidak pernah ada di halaman. Elemen lain akan mengisi ruang yang ditinggalkannya.

```css
.sembunyikan-di-mobile {
  display: none;
}
```

Properti ini sering dipakai bersama *media query* untuk menyembunyikan elemen tertentu di layar kecil, topik yang akan kita bahas di modul responsivitas.

## Latihan singkat

1. Buat daftar navigasi dengan `<ul>` dan `<li>`, lalu ubah `<li>` menjadi `inline-block` dengan padding.
2. Buat sebuah `<span>` dan ubah menjadi `display: block`. Amati bagaimana elemen di sekitarnya ikut bergeser.
3. Buat sebuah elemen dengan `display: none`, lalu bandingkan dengan elemen yang memakai `opacity: 0`. Apa bedanya terhadap ruang yang ditinggalkan di halaman?
