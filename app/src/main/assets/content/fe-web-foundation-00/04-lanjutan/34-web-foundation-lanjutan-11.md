---
id: web-foundation-lanjutan-11
courseId: fe-web-foundation-00
order: 34
title: "Latihan: Navigasi yang Benar-Benar Berfungsi"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menghubungkan menu navigasi ke bagian-bagian halaman menggunakan tautan anchor id (#id)"
  - "Menguji perpindahan fokus halaman secara instan saat tautan internal diklik"
codeSnippet: |
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  
  <ul>
    <li><a href="#tentang-saya">Tentang Saya</a></li>
    <li><a href="#hobi">Hobi</a></li>
    <li><a href="#rencana-belajar">Rencana Belajar</a></li>
  </ul>
  
  <h2 id="tentang-saya">Tentang Saya</h2>
  <p>Aku suka menggambar dan bermain gitar.</p>
  
  <h2 id="hobi">Hobi</h2>
  <h3>Menggambar</h3>
  <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
  
  <h2 id="rencana-belajar">Rencana Belajar</h2>
  <p>Menuntaskan dasar HTML sebelum lanjut ke CSS.</p>
---
Ingat daftar navigasi yang kamu buat beberapa modul lalu? Sekarang saatnya membuatnya benar-benar berfungsi: diklik, lalu berpindah ke bagian yang dituju.

## Alur latihan

1. Beri atribut `id` pada setiap `<h2>` yang jadi tujuan navigasi
2. Ubah setiap item di daftar navigasi menjadi elemen `<a>`
3. Isi `href` setiap `<a>` dengan tanda pagar (`#`) diikuti `id` tujuannya
4. Jalankan dan coba klik setiap tautan navigasi

## Kenapa memakai id dan tanda pagar?

`id` adalah atribut yang bisa dipakai di elemen mana pun untuk memberi pengenal unik. Salah satu kegunaannya: menjadi tujuan lompatan di dalam halaman yang sama. Kalau kamu menulis `href="#tentang-saya"`, browser akan langsung menggulir ke elemen yang punya `id="tentang-saya"`.

```html
<a href="#hobi">Hobi</a>
<!-- ... -->
<h2 id="hobi">Hobi</h2>
```

Tanda pagar (`#`) di depan nama `id` inilah yang memberi tahu browser bahwa tujuannya ada di dalam halaman ini sendiri, bukan URL baru.

## Bedah kode

Lihat `codeSnippet`: setiap `<h2>` sekarang punya `id` yang sesuai dengan topiknya (`tentang-saya`, `hobi`, `rencana-belajar`), dan setiap item di daftar navigasi dibungkus `<a>` dengan `href` yang menunjuk ke `id` tersebut lewat tanda pagar.

Coba klik salah satu tautan navigasi di hasil Playground. Halaman akan langsung menggulir ke bagian yang dituju, meskipun tidak ada satu baris JavaScript pun yang ditulis.

## Kesalahan yang sering terjadi

- Nilai `href` tidak sama persis dengan nilai `id` tujuannya (termasuk besar-kecil huruf)
- Lupa menambahkan tanda pagar di depan nama `id` pada `href`
- Memakai `id` yang sama di lebih dari satu elemen, sehingga browser bingung menuju ke mana

## Coba sendiri

Tambahkan satu bagian baru "Kontak" lengkap dengan `id`-nya, lalu tambahkan satu item navigasi baru yang menuju ke sana.
                    
