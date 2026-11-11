---
id: web-foundation-lanjutan-15
courseId: fe-web-foundation-00
order: 38
title: "Elemen Generic: Div dan Span"
readingMinutes: 5
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Memahami peran elemen generic <div> (block) dan <span> (inline) saat tidak ada tag semantik yang pas"
  - "Mengetahui kapan harus menggunakan div/span dan kapan memprioritaskan tag semantik"
codeSnippet: |
  <div id="content">
    <article>
      <h2>Tentang Saya</h2>
      <p>Aku suka menggambar dan bermain gitar.</p>
    </article>
  </div>
  
  <p>
    Kontak singkat: <span class="info">Semarang, Jawa Tengah</span>.
  </p>
---
Kadang tidak ada elemen semantik yang benar-benar cocok untuk sebuah bagian konten. Untuk itu, HTML menyediakan dua elemen generic (umum) yang tidak membawa makna apa pun: `<div>` dan `<span>`.

## Div

`<div>` adalah wadah (container) generic yang bersifat **block**. Ia tidak memberi efek visual apa pun sebelum diberi gaya lewat CSS. Fungsinya murni untuk mengelompokkan beberapa elemen supaya lebih mudah ditata atau diberi gaya bersama, biasanya lewat atribut `class` atau `id`.

```html
<div id="content">
  <article>Konten pertama</article>
  <article>Konten kedua</article>
</div>
```

Gunakan `<div>` kalau kamu memang butuh sebuah pembungkus, tapi tidak ada elemen semantik (`article`, `section`, `aside`, dan seterusnya) yang maknanya cocok.

## Span

`<span>` punya fungsi serupa `<div>`, tapi bersifat **inline**: tidak membuat baris baru, dan dipakai untuk membungkus sebagian kecil teks di dalam sebuah paragraf, list, atau heading.

```html
<ul>
  <li>Rani: <span class="phone">0812xxxxxxx</span></li>
  <li>Budi: <span class="phone">0813xxxxxxx</span></li>
</ul>
```

Dengan `<span class="phone">`, nantinya kamu bisa memberi gaya CSS khusus untuk semua nomor telepon itu, tanpa mengubah teks lain di sekitarnya.

Contoh lain, menandai kata-kata tertentu di dalam sebuah paragraf:

```html
<p>
  Untuk membuat sebuah puding, kamu butuh <span class="bahan">susu</span>,
  <span class="bahan">gula</span>, dan <span class="bahan">agar-agar</span>.
</p>
```

## Kesalahan yang sering terjadi

- Memakai `<div>` padahal ada elemen semantik yang lebih tepat (misalnya `<article>` atau `<section>`)
- Memakai `<div>` untuk teks di tengah kalimat, padahal seharusnya `<span>` (karena `<div>` bersifat block dan akan memutus baris)

## Coba sendiri

Bungkus seluruh konten utama halaman profilmu (semua bagian di antara header dan footer) dengan satu `<div id="content">`.
                    
