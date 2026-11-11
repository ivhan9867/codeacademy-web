---
id: css-layout-prep-3
courseId: fe-css-02
order: 3
title: "Mengatur Playground untuk Eksperimen Layout"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Mengetahui teknik visualisasi kotak layout menggunakan background warna dan garis pembatas kontras"
  - "Memanfaatkan tombol Jalankan, Reset, dan Live Preview di Playground CodeAcademy"
  - "Mencoba starter template pertama untuk menguji perilaku kontainer dan item"
codeLanguage: css
codeSnippet: |
  .wadah {
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
  }

  .kotak {
    background-color: #df7b56;
    color: white;
    padding: 16px;
    margin: 8px;
    border-radius: 8px;
    text-align: center;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji Layout Playground</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="wadah">
        <div class="kotak">Kotak 1</div>
        <div class="kotak">Kotak 2</div>
        <div class="kotak">Kotak 3</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah {
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
  }

  .kotak {
    background-color: #df7b56;
    color: white;
    padding: 16px;
    margin: 8px;
    border-radius: 8px;
    text-align: center;
  }
playgroundTask: "Coba buka tab CSS di Playground, tambahkan baris 'display: flex;' di dalam blok selector '.wadah', lalu ketuk tombol 'Jalankan'. Perhatikan bagaimana ketiga kotak langsung berbaris ke samping!"
---
Mempelajari tata letak (*layout*) tanpa visualisasi warna ibarat menyusun perabotan kamar dalam kegelapan. Sering kali kita bingung kenapa elemen bergeser ke bawah atau tidak mau membesar, padahal penyebabnya adalah batas kotak pembungkus yang tidak kelihatan.

![Trik Visualisasi Batas Kontainer dengan Warna Terang](asset:images/fe-css-02/inspeksi-visual-kotak.png)

## Trik Rahasia Pengembang: Warna Kontras & Garis Putus-Putus

Sepanjang kelas ini, kita akan sering memakai teknik **debugging visual**:

1. **Wadah Luar (Container)**: Kita beri warna latar lembut dengan garis tepi putus-putus (`border: 2px dashed #1a6368`). Ini membantu kita melihat batas maksimal ruang yang tersedia.
2. **Elemen Anak (Items)**: Kita beri warna solid cerah dan sudut membulat (`border-radius: 8px`). Ini membantu kita mengamati bagaimana ukuran dan posisi masing-masing anak berubah saat properti layout diaktifkan.

## Memanfaatkan Fitur Playground CodeAcademy

Di bawah modul materi praktikum, kamu bisa langsung bereksperimen dengan editor mini:

- **Tab HTML & CSS**: Kamu bisa berpindah antara struktur dokumen (`index.html`) dan aturan gaya (`style.css`).
- **Tombol Jalankan**: Merender ulang seluruh kode ke panel **Live Preview** di bagian bawah atau samping layarmu.
- **Tombol Reset**: Jika kodenya keliru atau terhapus secara tidak sengaja, tombol Reset akan mengembalikan teks editor ke kondisi awal (*starter state*) yang bersih.

## Saatnya Mencoba Sendiri

Perhatikan kode starter di modul ini. Saat ini, ketiga elemen `.kotak` berada di dalam `.wadah`. Karena elemen `<div>` secara bawaan bertipe *block*, ketiga kotak tersebut bertumpuk dari atas ke bawah.

Silakan selesaikan tantangan **playgroundTask**:
> Tambahkan `display: flex;` di dalam selector `.wadah`, lalu tekan **Jalankan**. Dalam sekejap, ketiga kotak akan berbaris berdampingan. Itulah daya magis Flexbox yang akan kita kupas tuntas!
