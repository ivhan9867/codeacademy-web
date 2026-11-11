---
id: web-foundation-pcss-23
courseId: fe-web-foundation-00
order: 79
title: "Box Model: Padding"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu-sesak {
    background-color: #f0f8ff;
    border: 1px solid #00a2c6;
  }

  .kartu-lega {
    background-color: #f0f8ff;
    border: 1px solid #00a2c6;
    padding: 16px;
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
  - "Menjelaskan fungsi padding sebagai jarak antara konten dan border"
  - "Mengatur padding per sisi dan dengan shorthand satu baris"
  - "Memahami bahwa nilai persentase pada padding relatif terhadap elemen induk"
---
Padding adalah jarak antara **konten** dan **border** sebuah elemen. Padding paling terasa manfaatnya ketika elemen memakai `background-color` atau `border`: tanpa padding, teks akan menempel langsung ke tepi warna atau garis, dan terasa sesak dibaca.

Bandingkan dua kartu berikut. Yang pertama tanpa padding, yang kedua dengan padding:

```css
.kartu-sesak {
  background-color: #f0f8ff;
  border: 1px solid #00a2c6;
}

.kartu-lega {
  background-color: #f0f8ff;
  border: 1px solid #00a2c6;
  padding: 16px;
}
```

Kartu kedua terasa jauh lebih nyaman dilihat, karena ada ruang napas antara teks dan tepi kotaknya. Kotak itu sendiri juga jadi sedikit lebih lebar dan tinggi, karena padding menambah ruang di dalam border.

## Satuan padding

Piksel adalah satuan yang paling sering dipakai untuk padding, meskipun persentase dan `em` juga bisa dipakai. Jika memakai persentase, nilainya dihitung relatif terhadap **lebar elemen induk** (atau jendela browser, jika elemen tidak punya induk).

## Mengatur padding per sisi

Sama seperti border, padding bisa diatur per sisi dengan empat properti:

```css
.kotak {
  padding-top: 10px;
  padding-right: 15px;
  padding-bottom: 10px;
  padding-left: 15px;
}
```

Or ditulis sekaligus dengan shorthand, memakai pola 1–4 nilai yang sama seperti `border-width`:

```css
.kotak {
  padding: 10px 15px 10px 15px; /* top right bottom left */
}
```

Karena pada contoh ini nilai atas sama dengan bawah, dan kiri sama dengan kanan, kita juga bisa menyingkatnya menjadi dua nilai:

```css
.kotak {
  padding: 10px 15px; /* top & bottom, left & right */
}
```

## Latihan singkat

1. Buat dua paragraf dengan `border` dan `background-color` yang sama. Beri `padding: 16px` hanya pada paragraf kedua, lalu bandingkan kenyamanan membacanya.
2. Buat sebuah kartu dengan padding atas-bawah 12px dan kiri-kanan 20px, ditulis dalam satu baris shorthand.
3. Terapkan `padding: 5%` pada sebuah elemen di dalam `<div>` berukuran tetap, lalu ubah lebar `<div>` tersebut. Amati bagaimana padding ikut berubah.
