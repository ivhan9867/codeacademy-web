---
id: web-foundation-pcss-22
courseId: fe-web-foundation-00
order: 78
title: "Box Model: Border"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  .box {
    border-top-width: 2px;
    border-right-width: 1px;
    border-bottom-width: 1px;
    border-left-width: 2px;
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
  - "Mengatur ketebalan border dengan border-width, termasuk aturan 1-4 nilai"
  - "Memilih jenis garis border dengan border-style"
  - "Mengatur warna border dengan border-color"
  - "Menulis shorthand border dan memahami bahwa urutan nilainya bebas"
---
Border adalah garis yang mengelilingi area konten dan padding sebuah elemen. Garis ini bersifat opsional, tetapi begitu dipakai, border bisa sangat mengubah tampilan sebuah kotak: dari kartu yang polos menjadi kartu dengan batas yang jelas, atau dari input teks biasa menjadi input yang terlihat bisa diisi.

Ada tiga hal yang bisa kita atur pada border: **ketebalan**, **jenis garis**, dan **warna**. Mari kita bahas satu per satu.

## Border Width

Properti `border-width` mengatur ketebalan garis border. Nilainya bisa berupa piksel, atau kata kunci bawaan seperti `thin`, `medium`, dan `thick`. Perlu diingat, properti ini **tidak bisa** memakai nilai persentase.

Kamu bisa mengatur ketebalan tiap sisi secara terpisah dengan empat properti berikut:

```css
.box {
  border-top-width: 2px;
  border-right-width: 1px;
  border-bottom-width: 1px;
  border-left-width: 2px;
}
```

Atau menuliskannya sekaligus dalam satu baris:

```css
.box {
  border-width: 2px 1px 1px 2px; /* top right bottom left */
}
```

`border-width` menerima satu sampai empat nilai, dan jumlah nilai itu menentukan cara penerapannya:

| Jumlah nilai | Cara penerapan |
|---|---|
| 1 nilai | Berlaku untuk keempat sisi |
| 2 nilai | Nilai pertama untuk atas & bawah, nilai kedua untuk kiri & kanan |
| 3 nilai | Nilai pertama untuk atas, nilai kedua untuk kiri & kanan, nilai ketiga untuk bawah |
| 4 nilai | Nilai pertama atas, kedua kanan, ketiga bawah, keempat kiri (searah jarum jam) |

Pola 1–4 nilai ini bukan hanya berlaku untuk border. Kita akan menemukan pola yang sama persis pada `padding` dan `margin` di modul berikutnya.

## Border Style

Properti `border-style` menentukan jenis garis yang ditampilkan. Beberapa nilai yang umum dipakai:

| Nilai | Tampilan |
|---|---|
| `solid` | Garis padat, tidak terputus |
| `dotted` | Garis berupa titik-titik |
| `dashed` | Garis berupa potongan pendek-pendek |
| `double` | Dua garis padat sejajar |
| `groove` | Tampak seperti ukiran/frame timbul |
| `hidden` | Border disembunyikan (tetap memakan ruang, tapi tidak terlihat) |

```css
.kartu {
  border-style: solid;
}

.kartu-putus {
  border-style: dashed;
}
```

Sama seperti `border-width`, kamu bisa mengatur tiap sisi secara terpisah:

```css
.box {
  border-top-style: solid;
  border-right-style: dashed;
  border-bottom-style: solid;
  border-left-style: dashed;
}
```

> **Catatan:** Tanpa `border-style`, border tidak akan terlihat sama sekali, walaupun `border-width` dan `border-color` sudah diatur. `border-style` adalah properti yang wajib ada agar border tampil.

## Border Color

Properti `border-color` menentukan warna garis, dan menerima format yang sama seperti warna lainnya: RGB, HEX, atau nama warna.

```css
/* menggunakan format RGB */
border-color: rgb(80, 138, 212);

/* menggunakan format HEX */
border-color: #4ee717;

/* menggunakan nama warna */
border-color: red;
```

Seperti dua properti sebelumnya, warna tiap sisi juga bisa diatur satu per satu, atau ditulis sekaligus dengan urutan atas-kanan-bawah-kiri:

```css
border-color: #919191 #111111 #4ee717 #00c8eb;
```

## Shorthand Border

Menulis `border-width`, `border-style`, dan `border-color` satu per satu cukup panjang. CSS menyediakan shorthand `border` yang menggabungkan ketiganya dalam satu baris:

```css
.kartu {
  border: 2px solid #00a2c6;
}
```

Yang menarik, **urutan nilainya bebas**. Ketiga penulisan berikut menghasilkan hasil yang sama persis:

```css
div { border: 2px solid #00a2c6; }   /* width style color */
div { border: #00a2c6 2px solid; }   /* color width style */
div { border: solid #00a2c6 2px; }   /* style color width */
```

Browser cukup pintar mengenali setiap nilai berdasarkan jenisnya (angka dengan satuan untuk width, kata kunci untuk style, format warna untuk color), jadi kamu tidak perlu menghafal urutan tertentu. Meski begitu, kebanyakan developer tetap menulis dengan urutan `width style color` sebagai kebiasaan, karena lebih mudah dibaca.

## Latihan singkat

1. Buat sebuah `<div>` dengan `border-width: 1px 2px 3px 4px`, lalu amati sisi mana yang paling tebal.
2. Buat tiga kotak dengan `border-style` berbeda: `solid`, `dashed`, dan `double`.
3. Tulis ulang salah satu border di atas menggunakan shorthand `border`, dengan urutan nilai bebas sesuai pilihanmu.
