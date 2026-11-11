---
id: web-foundation-pcss-16
courseId: fe-web-foundation-00
order: 72
title: "Menetapkan Nilai Warna"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  /* Format RGB */
  color: rgb(78, 231, 23);

  /* Format HEX */
  color: #4ee717;

  /* Format HSL */
  color: hsl(104, 82%, 50%);
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Area Latihan</title>
    </head>
    <body>
      <header>
        <h1>Judul Contoh</h1>
        <p>Paragraf contoh untuk mencoba properti CSS di atas.</p>
      </header>
      <nav>
        <ul>
          <li><a href="#">Beranda</a></li>
          <li><a href="#">Tentang</a></li>
          <li><a href="#">Kontak</a></li>
        </ul>
      </nav>
      <main>
        <div class="box">
          <span>Teks di dalam span</span>
          <p>Paragraf lain di dalam sebuah div.</p>
          <img src="https://placehold.co/150" alt="Contoh gambar" />
        </div>
      </main>
    </body>
  </html>
learningObjectives:
  - "Menulis warna dengan RGB, HEX, dan HSL"
  - "Menggunakan predefined color name dengan tepat"
---
### Dasar warna di layar

Setiap warna di layar dibentuk dari campuran tiga cahaya: **merah, hijau, dan biru** (RGB). Layar terdiri dari ribuan piksel, dan setiap piksel memiliki tiga lampu kecil itu. Dengan mengatur kekuatan masing-masing lampu, satu piksel bisa menampilkan ribuan warna.

Kamu tidak perlu menghafal kombinasi angka untuk setiap warna. Gunakan **color picker** di aplikasi desain seperti Figma, GIMP, atau Photoshop, atau cari "color picker" di mesin pencari. Dari sana kamu bisa menyalin nilai warna langsung ke CSS.

### Numeric value

Cara yang paling banyak dipakai adalah numeric value, karena memberi kontrol yang tepat. Contoh ini menuliskan warna hijau yang sama dalam tiga format:

```css
/* Format RGB */
color: rgb(78, 231, 23);

/* Format HEX */
color: #4ee717;

/* Format HSL */
color: hsl(104, 82%, 50%);
```

- **RGB** menyatakan kekuatan merah, hijau, dan biru dengan angka 0 sampai 255.
- **HEX** adalah format ringkas dari RGB dengan notasi enam digit heksadesimal, diawali tanda pagar.
- **HSL** menyatakan warna dengan *hue* (jenis warna, 0 sampai 360 derajat), *saturation* (kepekatan), dan *lightness* (kecerahan). HSL sering lebih mudah dipahami saat ingin membuat variasi warna yang lebih terang atau lebih gelap.

### Predefined color name

Selain angka, CSS juga menerima nama warna seperti `black`, `white`, atau `blue`:

```css
color: black;
color: white;
color: blue;
```

Browser modern mendukung sekitar 140 nama warna. Daftar lengkapnya bisa kamu lihat di referensi HTML color names dari w3schools.com. Nama warna praktis untuk prototipe, tetapi untuk desain yang serius biasanya lebih presisi memakai numeric value.
