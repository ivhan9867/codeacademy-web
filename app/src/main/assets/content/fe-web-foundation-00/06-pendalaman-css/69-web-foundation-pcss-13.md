---
id: web-foundation-pcss-13
courseId: fe-web-foundation-00
order: 69
title: "Latihan: Menggunakan Font dari Google Fonts"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  <head>
    <meta charset="utf-8" />
    <title>Halaman Profil</title>

    <link rel="stylesheet" href="assets/styles/style.css" />

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300..700&display=swap"
      rel="stylesheet"
    />
  </head>
codeLanguage: html
learningObjectives:
  - "Memasang font dari Google Fonts lewat tag <link>"
  - "Menerapkan font pada CSS dan memahami cara kerjanya di balik layar"
---
## Mengambil font dari Google Fonts

Daripada mengunduh berkas font satu per satu, kamu bisa memakai **Google Fonts**. Layanan ini menyediakan lebih dari 1500 font gratis dan open-source. Di bagian ini, kita pakai Quicksand sebagai contoh.

Langkah-langkahnya:

1. Buka **fonts.google.com** dan cari "Quicksand".
2. Pilih ketebalan yang kamu butuhkan, misalnya 300 dan 700, lalu klik tombol untuk menambahkannya ke daftar pilihan.
3. Klik **Get embed code**, lalu pilih cara **`<link>`**.
4. Salin kode `<link>` ke dalam elemen `<head>` HTML.

```html
<head>
  <meta charset="utf-8" />
  <title>Halaman Profil</title>

  <link rel="stylesheet" href="assets/styles/style.css" />

  <!-- Google Fonts -->
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link
    href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300..700&display=swap"
    rel="stylesheet"
  />
</head>
```

Setelah itu, gunakan nama font di CSS:

```css
body {
  font-family: "Quicksand", sans-serif;
}
```

Yang terjadi di balik layar: URL pada `<link>` sebenarnya berisi berkas CSS yang berisi `@font-face`. Jadi teknik yang kita pelajari tadi dipakai otomatis oleh Google Fonts. Parameter `wght@300..700` memberi tahu Google bahwa kita ingin rentang ketebalan dari 300 sampai 700.

> **Catatan:** Google Fonts hanya bisa dimuat ketika perangkat online. Untuk proyek yang sering dikembangkan secara offline, lebih aman memakai `@font-face` dengan berkas font yang disimpan lokal.

## Latihan singkat

Gunakan halaman profil yang sudah kamu buat sebelumnya.

1. Ubah `font-family` pada `body` menjadi `"Quicksand", sans-serif`, lalu lihat perubahan tampilannya.
2. Buat `h1` dengan `font-size` dalam `rem`, lalu ubah ukuran font di pengaturan browser dan amati apa yang terjadi.
3. Terapkan `text-align: center` pada judul, dan `text-indent: 2em` pada satu paragraf panjang.
4. Buat `a` tanpa garis bawah, tetapi berubah warna ketika `:hover`.
5. Tulis ulang salah satu rule `font` dengan shorthand, lalu pastikan urutan nilainya benar.
