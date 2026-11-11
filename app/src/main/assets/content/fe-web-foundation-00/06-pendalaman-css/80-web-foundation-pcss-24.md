---
id: web-foundation-pcss-24
courseId: fe-web-foundation-00
order: 80
title: "Box Model: Margin"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu {
    margin: 20px;
  }
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
  - "Menjelaskan fungsi margin sebagai jarak di luar border elemen"
  - "Memahami margin collapsing pada jarak vertikal antarelemen"
  - "Mengatur margin per sisi dan dengan shorthand satu hingga empat nilai"
---
Seperti padding, margin juga merupakan ruang atau jarak pada sebuah elemen. Bedanya, margin terletak **di luar** border, bukan di dalamnya. Margin berfungsi menjaga elemen agar tidak saling bertabrakan, dan memberi jarak dari tepi jendela browser.

```css
.kartu {
  margin: 20px;
}
```

Bayangkan dua kartu yang diletakkan bersebelahan tanpa margin: keduanya akan menempel rapat, terasa sesak. Dengan margin, ada jarak yang memisahkan keduanya sehingga tata letak terasa lebih lega dan terstruktur.

## Margin collapsing

Ada satu sifat unik pada margin vertikal yang disebut **margin collapsing** (margin yang menumpuk). Ketika dua elemen bertumpuk secara vertikal dan masing-masing punya margin, margin di antara keduanya **tidak dijumlahkan**, melainkan **diambil nilai yang paling besar**.

```css
.atas {
  margin-bottom: 20px;
}

.bawah {
  margin-top: 20px;
}
```

Kalau dua elemen di atas diletakkan bersebelahan secara vertikal, jarak di antara keduanya **bukan** 40px (20px + 20px), melainkan tetap 20px, karena kedua margin itu saling menumpuk dan yang dipakai adalah nilai terbesarnya.

Margin collapsing ini sering membingungkan pemula, karena hasilnya terasa tidak sesuai dugaan. Yang perlu diingat: fenomena ini hanya terjadi pada margin **vertikal** antarelemen sibling, bukan pada margin horizontal.

## Mengatur margin per sisi

Pola penulisannya sama persis dengan border dan padding:

```css
.kotak {
  margin-top: 10px;
  margin-right: 15px;
  margin-bottom: 20px;
  margin-left: 25px;
}
```

Atau dengan shorthand:

```css
.kotak {
  margin: 10px 15px 20px 25px; /* top right bottom left */
}
```

Dua nilai juga bisa dipakai untuk membedakan margin vertikal dan horizontal:

```css
.kotak {
  margin: 10px 15px; /* top & bottom: 10px, left & right: 15px */
}
```

## Latihan singkat

1. Buat dua `<div>` bersebelahan secara vertikal. Beri `margin-bottom: 30px` pada yang pertama dan `margin-top: 20px` pada yang kedua. Ukur jarak di antara keduanya — apakah 50px atau 30px?
2. Buat tiga kartu sejajar horizontal dengan `margin: 0 10px` di antaranya.
3. Bandingkan hasil `margin: 20px` (satu nilai) dengan `margin: 20px 10px` (dua nilai) pada elemen yang sama.
