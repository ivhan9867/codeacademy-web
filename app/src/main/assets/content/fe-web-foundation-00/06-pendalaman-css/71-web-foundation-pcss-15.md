---
id: web-foundation-pcss-15
courseId: fe-web-foundation-00
order: 71
title: "Pewarnaan"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Memahami konsep nilai warna di CSS"
  - "Mengenal tiga cara penulisan nilai warna: nama warna, kode heksadesimal (hex), dan fungsi rgb()"
  - "Menghubungkan nilai warna dengan penerapannya pada properti CSS"
---
## Pengantar Pewarnaan di CSS

Warna membuat halaman web terasa hidup, nyaman dibaca, dan memiliki identitas visual yang kuat. Bayangkan website di era awal tahun 1990-an, ketika hampir semua halaman hanya berlatar abu-abu kusam dengan teks hitam standar. Seiring berkembangnya browser modern, CSS menyediakan sistem pewarnaan yang sangat kaya dan didukung penuh oleh semua perangkat digital.

Sebelum mempelajari properti apa saja yang bisa diberi warna, kamu perlu memahami dulu apa itu **nilai warna** (*color value*) di CSS dan bagaimana cara menuliskannya.

## Apa Itu Nilai Warna di CSS?

Layar monitor atau ponsel pintar menampilkan warna dengan memancarkan kombinasi tiga cahaya warna utama: **Merah (*Red*)**, **Hijau (*Green*)**, dan **Biru (*Blue*)**, atau disingkat **RGB**. Ketika kamu menuliskan nilai warna di CSS, kamu sebenarnya sedang memberi tahu browser berapa intensitas campuran ketiga cahaya tersebut untuk digambar pada elemen layar.

CSS menyediakan tiga cara utama yang paling sering digunakan untuk menuliskan nilai warna:

### 1. Nama Warna (*Named Colors*)

CSS menyediakan 140+ nama warna standar berbahasa Inggris yang bisa langsung kamu gunakan tanpa menghafal kode angka. Contohnya antara lain `red`, `blue`, `forestgreen`, `gold`, atau `tomato`.

```css
h1 {
  color: tomato;
}
```

Cara ini sangat mudah dibaca dan cepat ditulis untuk uji coba awal, tetapi pilihannya terbatas pada daftar nama yang sudah ditentukan.

### 2. Kode Heksadesimal (*Hex Color*)

Kode heksadesimal diawali dengan tanda pagar (`#`) diikuti oleh kombinasi 6 karakter (angka `0-9` dan huruf `a-f`). Dua karakter pertama mewakili intensitas merah, dua karakter tengah mewakili hijau, dan dua karakter terakhir mewakili biru (`#RRGGBB`).

```css
h1 {
  color: #ff6347; /* Sama dengan tomato */
}
```

Format hex adalah cara yang paling populer di industri web design karena sangat ringkas dan mendukung lebih dari 16 juta kombinasi warna unik.

### 3. Fungsi `rgb()`

Pada fungsi `rgb()`, kamu menuliskan intensitas merah, hijau, dan biru secara langsung dalam bentuk angka desimal dari `0` (gelap/mati) hingga `255` (paling terang/maksimal).

```css
h1 {
  color: rgb(255, 99, 71); /* Sama dengan tomato */
}
```

Format ini mempermudah kamu memahami takaran fisik dari masing-masing komponen warna penyusunnya.

## Melangkah ke Modul Berikutnya

Ketiga cara penulisan di atas menghasilkan warna yang persis sama di layar browser. Di modul-modul setelah ini, kita akan melihat lebih dalam bagaimana sistem ruang warna RGB bekerja, bagaimana menambahkan efek transparansi, lalu menerapkannya langsung pada teks dan latar belakang elemen web kamu.
