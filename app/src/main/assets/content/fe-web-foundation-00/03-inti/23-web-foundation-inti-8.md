---
id: web-foundation-inti-8
courseId: fe-web-foundation-00
order: 23
title: "Rangkuman Pengenalan HTML"
readingMinutes: 3
section: INTI
learningObjectives:
  - "Merangkum anatomi elemen, atribut, dan kerangka standar dokumen HTML"
  - "Mengevaluasi kesiapan membuat dokumen HTML mandiri sebelum masuk materi lanjutan"
---
Selamat! Kamu sudah menulis baris kode pertamamu dan membangun dokumen HTML utuh dari nol. Sebelum masuk ke kuis, mari kita rangkum perjalananmu.

## Poin-poin utama

- **Pengantar HTML**: HTML adalah bahasa markup untuk menyusun struktur halaman web. Analoginya kerangka tubuh, dengan CSS sebagai tampilan dan JavaScript sebagai gerak.
- **Halaman web pertama**: teks polos tanpa penanda akan digabung browser menjadi satu blok. Struktur harus dinyatakan lewat HTML.
- **Folder proyek dan index.html**: website adalah kumpulan berkas dalam satu folder proyek. Nama berkas terdiri dari nama dan format, dan `index.html` adalah berkas yang dikirim server secara default.
- **Menandai konten**: setiap bagian teks dibungkus elemen yang sesuai perannya, yaitu `h1` sampai `h3` untuk judul berjenjang dan `p` untuk paragraf.
- **Anatomi elemen**: elemen punya tag pembuka, konten, dan tag penutup. Elemen bisa bersarang, dengan induk dan anak.
- **Atribut**: informasi tambahan di tag pembuka dengan bentuk `nama="nilai"`, misalnya `src`, `class`, `id`, dan `lang`.
- **Anatomi dokumen**: doctype, lalu `html` sebagai akar, dengan `head` untuk informasi halaman dan `body` untuk isi yang tampil. Semuanya membentuk pohon DOM.

## Kerangka yang kini kamu kuasai

```html
<!DOCTYPE html>
<html lang="id">
  <head>
    <meta charset="utf-8">
    <title>Judul Halaman</title>
  </head>
  <body>
    <h1>Judul Utama</h1>
    <p>Sebuah paragraf.</p>
  </body>
</html>
```

Kerangka ini akan menjadi titik awal hampir semua halaman web yang kamu buat ke depan.

## Istilah kunci

- **Elemen**: unit pembangun HTML yang menandai satu bagian konten
- **Tag pembuka dan penutup**: penanda awal dan akhir sebuah elemen
- **Atribut**: informasi tambahan pada elemen, ditulis di tag pembuka
- **Child-element**: elemen yang berada di dalam elemen lain
- **DOM Tree**: struktur hierarki elemen dalam sebuah dokumen
- **Doctype**: baris pertama yang menyatakan standar HTML yang dipakai

Merasa sudah siap? Lanjut ke Kuis Pengenalan HTML. Setelah itu, perjalananmu berlanjut ke bagian Pendalaman HTML dan Dasar CSS di kelas ini.
