---
id: web-foundation-pcss-42
courseId: fe-web-foundation-00
order: 98
title: "Clear Property dan Teknik Overflow"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Menggunakan properti clear untuk menghentikan efek float"
  - "Memahami nilai left, right, dan both pada properti clear"
  - "Menggunakan overflow: auto sebagai cara praktis mengatasi masalah collapse akibat float"
---
Ada dua cara umum untuk mengatasi masalah "induk kehilangan tinggi" akibat `float` yang kita bahas di modul sebelumnya.

## Cara 1: Properti clear

Properti `clear` dibuat khusus untuk menghentikan efek `float`. Dengan `clear`, sebuah elemen akan kembali "dianggap ada" oleh elemen-elemen di normal flow.

Cara menerapkannya biasanya dengan membuat satu elemen kosong (sering memakai `<div>` tanpa konten apa pun) yang diberi properti `clear`:

```html
<div class="container">
  <img src="foto.jpg" alt="Foto" style="float: left;" />
  <div class="clearfix"></div>
</div>
```

```css
.clearfix {
  clear: left;
}
```

Nilai `clear` mengikuti nilai yang dipakai pada `float` yang ingin dihentikan:

- `clear: left` menghentikan efek elemen yang di-float ke kiri.
- `clear: right` menghentikan efek elemen yang di-float ke kanan.
- `clear: both` menghentikan **kedua arah sekaligus** — pilihan paling umum dipakai ketika kamu tidak yakin arah float mana yang perlu dihentikan, atau ketika ada dua elemen float (kiri dan kanan) sekaligus.

## Cara 2: Teknik overflow

Cara kedua lebih praktis: cukup tambahkan `overflow: auto` pada elemen **induk** yang menampung elemen float, tanpa perlu membuat elemen kosong tambahan.

```css
.container {
  overflow: auto;
}
```

Dengan satu baris ini, induk akan kembali menghitung tinggi anak-anaknya dengan benar, termasuk anak yang di-float. Banyak developer lebih menyukai cara ini karena lebih singkat dan tidak menambah elemen HTML yang sebenarnya tidak diperlukan secara semantik.

Kedua cara ini menghasilkan efek akhir yang sama. Pemilihannya biasanya tergantung kebiasaan atau kebutuhan proyek — dan di latihan berikutnya, kita akan memakai teknik `overflow: auto` untuk merapikan layout Halaman Profil.

## Latihan singkat

1. Buat sebuah `<div>` berisi satu `<img>` dengan `float: left`, lalu amati bahwa tinggi `<div>` menjadi 0.
2. Tambahkan `overflow: auto` pada `<div>` tersebut, lalu amati bagaimana tingginya kembali normal.
3. Buat dua elemen dengan `float: left` dan `float: right` di dalam satu induk, lalu terapkan `clear: both` pada elemen kosong setelahnya untuk menghentikan kedua float tersebut.
