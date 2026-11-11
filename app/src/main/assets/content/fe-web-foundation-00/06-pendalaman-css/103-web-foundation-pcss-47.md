---
id: web-foundation-pcss-47
courseId: fe-web-foundation-00
order: 103
title: "Latihan: Membuat Mobile Version untuk Halaman Profil"
readingMinutes: 8
section: PENDALAMAN_CSS
learningObjectives:
  - "Menambahkan meta tag viewport pada dokumen HTML"
  - "Menulis media query agar #content dan aside bertumpuk vertikal di layar sempit"
---
Dua modul terakhir sudah membekali kita dengan teori yang cukup. Sekarang saatnya menerapkan keduanya pada Halaman Profil: meta tag viewport, dan media query.

## Langkah 1: Menambahkan meta tag viewport

Buka `index.html`, lalu tambahkan tag berikut di dalam `<head>`:

```html
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Halaman Profil Bandung</title>

  <link rel="stylesheet" href="assets/styles/style.css" />
</head>
```

Jika dijalankan sekarang di simulasi mobile, halaman sudah tidak mengecil lagi — tapi layout dua-kolomnya (`#content` dan `aside`) masih terasa terlalu sempit untuk layar kecil.

## Langkah 2: Menyusun ulang layout dengan media query

Kita akan membuat `#content` dan `aside` **bertumpuk vertikal** (bukan sejajar horizontal) ketika lebar viewport turun di bawah 1000px. Caranya, beri keduanya `width: 100%` di dalam media query:

```css
#content {
  float: left;
  width: 75%;
}

aside {
  float: right;
  width: 25%;
  padding-left: 20px;
}

@media screen and (max-width: 1000px) {
  #content,
  aside {
    width: 100%;
    padding: 0;
  }
}
```

Penjelasan tiap bagian:

- `screen`: media query ini berlaku untuk tampilan layar (bukan saat dicetak).
- `max-width: 1000px`: aturan di dalamnya hanya aktif ketika lebar viewport **1000px atau kurang**.
- `width: 100%` pada `#content` dan `aside`: karena keduanya sama-sama 100%, mereka otomatis bertumpuk vertikal (ingat: elemen float butuh ruang kosong di samping untuk sejajar; dengan lebar 100%, tidak ada ruang tersisa).
- `padding: 0`: menghapus padding yang sebelumnya dipakai untuk memberi jarak di layout dua-kolom, karena tidak diperlukan lagi saat bertumpuk.

Setelah perubahan ini, Halaman Profil akan otomatis menyesuaikan diri: tampil dua kolom sejajar di layar lebar, dan bertumpuk rapi secara vertikal di layar sempit — semuanya hanya dengan satu blok media query.

## Latihan singkat

1. Tambahkan meta tag viewport ke `index.html`, lalu coba buka halaman di simulasi mobile DevTools.
2. Tambahkan media query `@media screen and (max-width: 1000px)` yang membuat `#content` dan `aside` menjadi `width: 100%`.
3. Perlebar dan persempit jendela browser secara manual (tanpa DevTools) untuk melihat transisi antara layout dua-kolom dan layout bertumpuk.
