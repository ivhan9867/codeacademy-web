---
id: web-foundation-pcss-48
courseId: fe-web-foundation-00
order: 104
title: "Latihan: Menerapkan Sticky Position pada Navigasi"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Mengenal position: sticky sebagai gabungan sifat relative dan fixed"
  - "Menerapkan sticky pada elemen nav agar tetap terlihat saat halaman di-scroll"
  - "Memahami peran display: inline pada header agar sticky berfungsi dengan benar"
---
Ada satu masalah kecil yang mungkin belum kamu sadari di Halaman Profil kita: navigasi hanya muncul di bagian atas, tepat di bawah jumbotron. Begitu pengguna men-scroll ke bawah untuk membaca artikel, navigasi itu ikut hilang dari layar — padahal mungkin pengguna ingin langsung lompat ke bagian lain.

## Mengenal sticky positioning

Ada satu nilai `position` yang belum kita bahas: **`sticky`**. Nilai ini adalah gabungan dari sifat `relative` dan `fixed`:

- Seperti `relative`, elemen tetap berada di posisi normalnya selama belum "tersentuh" tepi viewport.
- Begitu pengguna men-scroll sampai elemen mencapai tepi viewport (misalnya `top: 0`), elemen akan "menempel" di sana seperti `fixed`, dan tetap terlihat selama induknya masih berada di layar.

## Menerapkan sticky pada nav

Tambahkan `position: sticky` dan `top: 0` pada selector `nav`:

```css
nav {
  background-color: #00a2c6;
  padding: 5px;

  position: sticky;
  top: 0;
}
```

`top: 0` penting di sini — ini memberi tahu browser bahwa navigasi harus menempel **tepat di tepi atas** viewport saat di-scroll.

## Mengapa belum berfungsi? Peran display: inline pada header

Jika kamu coba sekarang, navigasi mungkin masih belum menempel dengan benar. Ini karena `<nav>` berada di dalam `<header>`, dan secara default `<header>` (sebagai elemen block) "menahan" anak-anaknya tetap berada dalam batas tingginya sendiri.

Solusinya, ubah `<header>` menjadi `display: inline`:

```css
header {
  display: inline;
}
```

Dengan ini, `<header>` tidak lagi "mengunci" `<nav>` di dalam batas tingginya, sehingga `<nav>` bisa benar-benar menempel ke tepi viewport browser saat halaman di-scroll — bukan hanya tepi `<header>`.

Sekarang, saat pengguna men-scroll ke bawah, navigasi akan tetap terlihat menempel di bagian atas layar, membuatnya jauh lebih mudah diakses kapan saja.

## Latihan singkat

1. Tambahkan `position: sticky; top: 0;` pada `nav`, lalu scroll halaman dan amati apakah navigasi sudah menempel.
2. Jika belum, tambahkan `display: inline` pada `header`, lalu coba lagi.
3. Eksperimen dengan `top: 10px` dan amati bagaimana jarak navigasi dari tepi atas berubah saat menempel.
