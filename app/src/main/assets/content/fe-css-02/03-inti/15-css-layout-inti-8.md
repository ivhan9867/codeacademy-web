---
id: css-layout-inti-8
courseId: fe-css-02
order: 15
title: "Latihan Praktik: Membangun Navigation Bar Modern & Responsif"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Menggabungkan display: flex, justify-content, align-items, dan gap ke dalam komponen dunia nyata"
  - "Membangun bilah navigasi (Navbar) yang elegan dengan logo, menu tautan, dan tombol aksi (CTA)"
  - "Menerapkan teknik reset list margin dan penyelarasan vertikal yang presisi"
codeLanguage: css
codeSnippet: |
  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #104246;
    padding: 12px 24px;
  }

  .nav-links {
    display: flex;
    gap: 20px;
    list-style: none;
    margin: 0;
    padding: 0;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: Navbar Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <!-- Header Navigasi -->
      <nav class="navbar">
        <div class="logo">CodeAcademy</div>
        <ul class="nav-links">
          <li><a href="#">Kelas</a></li>
          <li><a href="#">Tantangan</a></li>
          <li><a href="#">Komunitas</a></li>
        </ul>
        <button class="btn-cta">Mulai Belajar</button>
      </nav>

      <main class="hero">
        <h1>Kuasai Skill Web Bersama Kami</h1>
        <p>Belajar coding dengan materi terstruktur dan latihan interaktif langsung di browsermu.</p>
      </main>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 0;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Atur navbar menjadi flex container */
  .navbar {
    background-color: #104246;
    padding: 14px 24px;
    color: white;
    /* Tambahkan display: flex; justify-content; align-items di sini */
  }

  .logo {
    font-size: 20px;
    font-weight: bold;
    color: #389b9e;
  }

  /* 2. Atur menu links agar sejajar horizontal */
  .nav-links {
    list-style: none;
    margin: 0;
    padding: 0;
    /* Jadikan flex container juga untuk meratakan tautan di dalamnya! */
  }

  .nav-links a {
    color: #faf7f2;
    text-decoration: none;
    font-size: 15px;
    font-weight: 500;
  }

  .nav-links a:hover {
    color: #389b9e;
  }

  /* 3. Tombol Call to Action */
  .btn-cta {
    background-color: #df7b56;
    color: white;
    border: none;
    padding: 10px 18px;
    border-radius: 8px;
    font-weight: bold;
    cursor: pointer;
  }

  .hero {
    padding: 40px 24px;
    text-align: center;
  }
playgroundTask: "Selesaikan 2 langkah flexbox pada CSS: (1) Di '.navbar', tambahkan 'display: flex; justify-content: space-between; align-items: center;'. (2) Di '.nav-links', tambahkan 'display: flex; gap: 20px;'. Jalankan kodenya dan saksikan sebuah Navbar website modern terwujud di Live Preview!"
---
Saatnya merangkum semua jurus Flexbox yang sudah kita pelajari ke dalam satu komponen paling penting di setiap website: **Bilah Navigasi Atas (*Top Navigation Bar*)**.

Sebuah navbar yang baik memiliki tiga zona elemen:
1. **Logo Web** di sisi kiri.
2. **Daftar Tautan Halaman** di tengah atau samping logo.
3. **Tombol Panggilan Aksi (*Call-to-Action*)** di sisi kanan.

![Anatomi Navbar Flexbox Modern](asset:images/fe-css-02/navbar-flexbox-anatomy.png)

## Bedah Arsitektur: Flexbox Bersarang (*Nested Flexbox*)

Perhatikan struktur HTML pada latihan ini:

```html
<nav class="navbar">       <!-- Container Luar (Flex Level 1) -->
  <div class="logo">...</div>
  <ul class="nav-links">   <!-- Container Dalam (Flex Level 2) -->
    <li><a href="#">...</a></li>
    <li><a href="#">...</a></li>
  </ul>
  <button class="btn-cta">...</button>
</nav>
```

Di sini kita menerapkan teknik **Nested Flexbox** yang sangat umum di dunia profesional:

1. **Flex Level 1 (Elemen `<nav class="navbar">`)**:
   - `display: flex;` mengaktifkan baris fleksibel.
   - `justify-content: space-between;` meletakkan Logo di ujung paling kiri, tombol CTA di ujung paling kanan, dan mendorong menu links berada di antaranya.
   - `align-items: center;` menyelaraskan tinggi logo, teks tautan, dan tombol CTA tepat di tengah-tengah tinggi navbar secara vertikal.

2. **Flex Level 2 (Elemen `<ul class="nav-links">`)**:
   - Tag `<ul>` secara bawaan bertipe `block` dan membuat tiap `<li>` bertumpuk ke bawah.
   - Dengan memberi `display: flex; gap: 20px;` pada `.nav-links`, seluruh tautan menu seketika berbaris mendatar dengan jarak 20px yang rapi!
   - Jangan lupa menambahkan `list-style: none; margin: 0; padding: 0;` untuk membuang bullet titik dan margin bawaan browser.

Buka editor Playground di bawah dan buktikan sendiri betapa mudah dan bersihnya kode CSS Flexbox untuk membuat komponen antarmuka yang elegan!
