---
id: web-foundation-lanjutan-13
courseId: fe-web-foundation-00
order: 36
title: "Semantic HTML: Header, Footer, Main, dan Nav"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menyusun struktur utama halaman dengan <header>, <nav>, <main>, dan <footer>"
  - "Memastikan setiap halaman web memiliki satu konten utama <main> yang jelas"
codeSnippet: |
  <header>
    <h1>Profil Rani</h1>
    <p>Pelajar dan calon web developer dari Semarang.</p>
  </header>
  
  <main>
    <p>Konten utama halaman akan ada di sini.</p>
  </main>
  
  <footer>
    <p>&copy; 2026 Rani.</p>
  </footer>
---
Empat elemen semantik pertama yang kamu pelajari berperan seperti bagian kepala, kaki, isi utama, dan navigasi sebuah halaman.

## Header

`<header>` adalah pengantar atau pembuka konten. Di level `<body>`, ia biasanya berisi judul situs dan navigasi utama.

```html
<header>
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  <nav>
    <ul>
      <li><a href="#tentang-saya">Tentang Saya</a></li>
      <li><a href="#hobi">Hobi</a></li>
    </ul>
  </nav>
</header>
```

`<header>` juga bisa dipakai di dalam `<article>` atau `<section>`, biasanya untuk menampung judul dan penulis bagian tersebut. Satu aturan penting: `<header>` tidak boleh diletakkan di dalam `<header>` lain (tidak boleh bersarang).

## Footer

`<footer>` adalah catatan kaki, biasanya berisi informasi hak cipta, kontak, atau tautan tambahan.

```html
<footer>
  <p>&copy; 2026 Rani. Dibuat sebagai latihan belajar HTML.</p>
</footer>
```

Sama seperti `<header>`, `<footer>` juga bisa dipakai di dalam `<article>` atau `<section>` untuk info tambahan spesifik bagian itu, dan juga tidak boleh bersarang di dalam `<footer>` lain.

## Main

`<main>` membungkus konten **utama** halaman. Di dalamnya boleh ada banyak `<section>` atau `<article>`, asal semuanya memang bagian dari konten utama.

```html
<body>
  <header>
    <!-- konten header -->
  </header>
  <main>
    <!-- konten utama -->
  </main>
  <footer>
    <!-- konten footer -->
  </footer>
</body>
```

Aturan ketatnya: **hanya boleh ada satu `<main>` per halaman**.

> Catatan: `<!-- teks -->` adalah komentar HTML. Teks di dalamnya tidak ditampilkan di halaman, hanya catatan untuk sesama developer.

## Nav

`<nav>` membungkus kumpulan tautan navigasi yang **penting**, seperti menu utama situs. Kamu sudah melihat contohnya di dalam `<header>` di atas.

Tidak semua kumpulan tautan perlu dibungkus `<nav>`. Misalnya, beberapa tautan "artikel terkait" di akhir sebuah artikel biasanya tidak dianggap navigasi utama, jadi tidak perlu `<nav>`.

Navigasi yang jelas sangat membantu aksesibilitas: pengguna pembaca layar bisa langsung melompat ke navigasi tanpa harus menyimak seluruh isi halaman dulu.

## Kesalahan yang sering terjadi

- Meletakkan lebih dari satu `<main>` dalam satu halaman
- Menaruh `<header>` di dalam `<header>` lain
- Membungkus semua kumpulan tautan dengan `<nav>`, padahal hanya navigasi utama yang perlu

## Coba sendiri

Sebelum masuk ke latihan penuh di modul berikutnya, coba bungkus judul dan satu paragraf pembukamu sendiri dengan `<header>` di Playground, lalu lihat apakah tampilannya berubah (jawabannya: tidak, karena `<header>` belum diberi gaya apa pun).
                    
