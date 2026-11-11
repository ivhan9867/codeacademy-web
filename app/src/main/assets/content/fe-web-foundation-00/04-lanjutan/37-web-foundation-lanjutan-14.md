---
id: web-foundation-lanjutan-14
courseId: fe-web-foundation-00
order: 37
title: "Semantic HTML: Article, Aside, dan Section"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Mengelompokkan konten mandiri dengan <article>, bagian samping dengan <aside>, dan segmen bertema dengan <section>"
  - "Menyusun hierarki konten artikel yang lengkap dengan heading internal"
codeSnippet: |
  <main>
    <article>
      <h2>Hobi</h2>
      <section>
        <h3>Menggambar</h3>
        <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
      </section>
      <section>
        <h3>Bermain Gitar</h3>
        <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
      </section>
    </article>
  
    <aside>
      <article>
        <h2>Info Singkat</h2>
        <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
      </article>
    </aside>
  </main>
---
Tiga elemen semantik berikutnya lebih kecil dan spesifik dibanding header, main, dan footer.

## Article

`<article>` membungkus konten yang **berdiri sendiri**: bisa dipahami tanpa bergantung pada konten lain di sekitarnya. Contohnya: artikel blog, komentar, atau satu unggahan forum. Kalau sebuah halaman punya beberapa konten mandiri, masing-masing sebaiknya dalam `<article>`-nya sendiri.

```html
<article>
  <h2>Tentang Saya</h2>
  <p>Aku suka menggambar dan bermain gitar.</p>
</article>

<article>
  <h2>Rencana Belajar</h2>
  <p>Menuntaskan dasar HTML sebelum lanjut ke CSS.</p>
</article>
```

`<article>` boleh berada di dalam `<article>` lain, selama isinya memang masih berkaitan dengan artikel induknya.

## Aside

`<aside>` punya dua makna tergantung posisinya:

- Di **dalam** `<article>`: berisi info terkait artikel itu, tapi bukan bagian inti isinya
- Di **luar** `<article>` (langsung di `<main>` atau `<body>`): berisi info terkait keseluruhan halaman

Biasanya ditampilkan di samping elemen yang menampungnya (nanti diatur lewat CSS).

```html
<main>
  <article>
    <!-- konten utama -->
  </article>

  <aside>
    <article>
      <h2>Info Singkat</h2>
      <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
    </article>
  </aside>
</main>
```

## Section

`<section>` mengelompokkan konten yang punya tema sama, dan sebaiknya diawali sebuah heading (`h1`-`h6`) sebagai elemen pertamanya, menandakan judul bagian itu. Cocok dipakai di dalam `<article>` yang isinya panjang dan bisa dipecah jadi beberapa bagian.

```html
<article>
  <section>
    <h3>Menggambar</h3>
    <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
  </section>

  <section>
    <h3>Bermain Gitar</h3>
    <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
  </section>
</article>
```

## Kesalahan yang sering terjadi

- Memakai `<section>` tanpa heading di dalamnya
- Memakai `<article>` untuk konten yang sebenarnya tidak bisa berdiri sendiri (bergantung penuh pada konten di sekitarnya)
- Bingung membedakan `<section>` dan `<div>` — `<section>` dipakai kalau isinya memang sebuah tema dengan judul, `<div>` (modul berikutnya) dipakai kalau tidak ada makna semantik yang cocok

## Coba sendiri

Bungkus bagian "Rencana Belajar" di halaman profilmu dengan `<article>`, lalu tambahkan `<aside>` kecil berisi satu kalimat tentang halaman ini sendiri.
                    
