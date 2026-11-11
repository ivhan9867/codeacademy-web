---
id: web-foundation-lanjutan-17
courseId: fe-web-foundation-00
order: 40
title: "Latihan: Mengelompokkan Konten dengan Div, Article, Aside, dan Section"
readingMinutes: 8
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menata kartu info, artikel hobi, dan panel samping menggunakan kombinasi section, article, dan aside"
  - "Menerapkan pembungkus generic <div> untuk keperluan tata letak visual"
codeSnippet: |
  <main>
    <div id="content">
      <article id="tentang-saya">
        <h2>Tentang Saya</h2>
        <p>Aku suka menggambar dan bermain gitar.</p>
      </article>
  
      <article id="hobi">
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
  
      <article id="rencana-belajar">
        <h2>Rencana Belajar</h2>
        <p>Menuntaskan dasar HTML sebelum lanjut ke CSS.</p>
      </article>
    </div>
  
    <aside>
      <article>
        <h2>Info Singkat</h2>
        <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
      </article>
    </aside>
  </main>
---
Konten besar sudah tertata. Sekarang saatnya menata bagian dalam `<main>` lebih detail, memakai `<div>`, `<article>`, `<aside>`, dan `<section>` sekaligus.

## Alur latihan

1. Di dalam `<main>`, pisahkan konten menjadi dua: konten utama (dibungkus `<div>`) dan konten info singkat (dibungkus `<aside>`)
2. Di dalam `<div>`, bungkus setiap topik (Tentang Saya, Hobi, Rencana Belajar) dengan `<article>` masing-masing
3. Di dalam `<article>` Hobi, bungkus sub-bagian Menggambar dan Bermain Gitar masing-masing dengan `<section>`
4. Pindahkan atribut `id` dari elemen `<h2>` ke elemen `<article>` pembungkusnya, karena `<article>`-lah yang mewakili keseluruhan topik itu
5. Tambahkan `id="content"` pada `<div>` pembungkus, memudahkan penataan gaya nanti
6. Jalankan dan bandingkan hasilnya

## Bedah kode

Perhatikan `codeSnippet`. Struktur di dalam `<main>` sekarang:

```text
main
  div#content
    article#tentang-saya
    article#hobi
      section (Menggambar)
      section (Bermain Gitar)
    article#rencana-belajar
  aside
    article (Info Singkat)
```

Beberapa hal penting:

- `id` sekarang ada di `<article>`, bukan lagi di `<h2>` di dalamnya. Karena tautan navigasi memakai `href="#tentang-saya"`, dan `id="tentang-saya"` kini ada di `<article>`, tautan itu tetap berfungsi tanpa masalah
- `<div id="content">` tidak membawa makna apa pun secara semantik. Fungsinya murni memudahkan penataan gaya nanti, memisahkan "konten utama" dari "info singkat" di sampingnya
- `<section>` dipakai untuk Menggambar dan Bermain Gitar karena keduanya adalah sub-tema di dalam tema besar Hobi, masing-masing diawali heading

Kalau kamu jalankan halaman ini, sekali lagi tidak ada perubahan tampilan. Tapi coba klik tautan navigasi "Hobi" — halaman tetap melompat ke bagian yang benar, karena `id`-nya sudah dipindahkan dengan tepat.

## Kesalahan yang sering terjadi

- Memindahkan `id` ke `<article>` tapi lupa menghapusnya dari `<h2>` lama, sehingga ada dua elemen dengan `id` yang sama
- Menaruh `<section>` tanpa heading sebagai elemen pertamanya

## Coba sendiri

Tambahkan satu `<article>` baru untuk topik "Kontak" di dalam `<div id="content">`, lengkap dengan `id`-nya, lalu tambahkan tautan navigasinya di `<header>`.
                    
