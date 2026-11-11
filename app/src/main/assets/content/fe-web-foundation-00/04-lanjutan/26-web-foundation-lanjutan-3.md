---
id: web-foundation-lanjutan-3
courseId: fe-web-foundation-00
order: 26
title: "Heading"
readingMinutes: 4
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menyusun hierarki heading terstruktur dari <h1> hingga <h6>"
  - "Memahami pentingnya heading untuk aksesibilitas pembaca layar dan SEO"
codeSnippet: |
  <h1>Profil Rani</h1>
  
  <h2>Tentang Saya</h2>
  <p>Pelajar dan calon web developer.</p>
  
  <h2>Rencana Belajar</h2>
  <h3>Bulan Ini</h3>
  <p>Menguntaskan dasar HTML sebelum lanjut ke CSS.</p>
---
Kamu sudah pernah memakai `<h1>` dan `<h2>`. Modul ini membahasnya secara utuh.

## Enam level heading

HTML menyediakan elemen heading dari `<h1>` sampai `<h6>`. Semakin kecil angkanya, semakin tinggi tingkatnya. `<h1>` adalah judul paling penting di halaman, dan seterusnya menurun sampai `<h6>`.

```html
<h1>Judul Utama</h1>
<h2>Sub Judul</h2>
<h3>Sub dari Sub Judul</h3>
```

## Kenapa heading penting, bukan cuma soal ukuran huruf

Heading bukan sekadar cara membuat teks jadi besar dan tebal. Ia menyatakan **garis besar (outline)** halaman. Alat bantu baca seperti *screen reader* memakai susunan heading untuk membantu penggunanya melompat dari satu bagian ke bagian lain tanpa harus membaca semuanya dari atas. Mesin pencari seperti Google juga memakai heading untuk memahami struktur dan topik utama halamanmu.

Karena itu, urutan heading sebaiknya masuk akal: `<h1>` sekali di puncak, lalu `<h2>` untuk bagian besar di bawahnya, `<h3>` untuk sub-bagian dari `<h2>`, dan seterusnya, tanpa melompat level hanya demi ukuran huruf tertentu.

## Tampilan bawaan browser

Secara default, browser menampilkan heading dengan huruf tebal, dengan `<h1>` berukuran paling besar dan mengecil secara berurutan sampai `<h6>`. Tampilan ini nanti bisa diubah total lewat CSS, tapi maknanya (level berapa) tidak akan berubah.

## Coba sendiri

Di halaman profilmu, tambahkan satu `<h2>` baru untuk bagian "Rencana Belajar", lalu satu `<p>` di bawahnya.
                    
