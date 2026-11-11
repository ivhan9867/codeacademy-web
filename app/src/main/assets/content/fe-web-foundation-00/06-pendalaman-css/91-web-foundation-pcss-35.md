---
id: web-foundation-pcss-35
courseId: fe-web-foundation-00
order: 91
title: "Positioning: Perbedaan Static Flow dan Non-Static Flow"
readingMinutes: 8
section: PENDALAMAN_CSS
learningObjectives:
  - "Membedakan static flow dan non-static flow secara konseptual"
  - "Memahami bahwa relative positioning mengeluarkan elemen dari static flow tanpa mengganggu elemen lain"
  - "Menggunakan properti top, right, bottom, dan left untuk menggeser elemen non-static"
---
CSS punya dua cara menampilkan elemen: **static flow** dan **non-static flow**. Untuk memahami perbedaannya, mari bayangkan sebuah halaman web dalam bentuk tiga dimensi.

Bayangkan kita punya tiga `<div>` berukuran 200×200 piksel, masing-masing dengan warna berbeda:

```css
.box {
  width: 200px;
  height: 200px;
}

.first { background-color: #60d0a8; }
.second { background-color: #6495ed; }
.third { background-color: #ffa500; }
```

Karena kita belum mengatur `position` pada ketiganya, semua elemen ditampilkan dengan **static flow**: tersusun rapi dari atas ke bawah, saling menempel seperti balok yang ditumpuk.

## Efek domino pada static flow

Sekarang, coba geser kotak kedua ke bawah dengan `margin-top`:

```css
.second {
  background-color: #6495ed;
  margin-top: 20px;
}
```

Hasilnya, kotak ketiga (oranye) ikut bergeser ke bawah mengikuti kotak kedua. Inilah ciri khas static flow: setiap elemen saling bergantung posisinya terhadap elemen lain.

## Mengeluarkan elemen dari static flow

Bandingkan dengan menerapkan `position: relative` pada kotak kedua:

```css
.second {
  background-color: #6495ed;
  position: relative;
}
```

Secara visual di browser, mungkin tidak ada perubahan apa pun — tapi sebenarnya elemen ini sudah "diangkat" keluar dari static flow. Artinya, elemen ini sekarang bisa digeser ke posisi mana pun **tanpa memengaruhi** posisi elemen lain di sekitarnya.

## Menggeser elemen non-static

Untuk benar-benar memindahkan posisinya, gunakan properti `top`, `right`, `bottom`, atau `left`:

```css
.second {
  background-color: #6495ed;

  position: relative;
  top: 30px;
  left: 10px;
}
```

Kotak kedua akan bergeser 30px ke bawah dan 10px ke kanan dari posisi aslinya, **tanpa** membuat kotak ketiga ikut bergeser seperti saat memakai margin.

Ini poin yang sangat penting untuk diingat: properti `top`, `right`, `bottom`, dan `left` **hanya berfungsi** pada elemen yang sudah keluar dari static flow, yaitu elemen dengan `position: relative`, `absolute`, atau `fixed`. Pada elemen dengan `position: static` (nilai bawaan), keempat properti ini tidak akan memberi efek apa pun.

## Latihan singkat

1. Buat tiga `<div>` dengan ukuran dan warna berbeda, lalu geser kotak kedua dengan `margin-top: 20px`. Amati bagaimana kotak ketiga ikut bergeser.
2. Ganti `margin-top` dengan `position: relative; top: 20px;` pada kotak kedua, lalu bandingkan: apakah kotak ketiga tetap di tempat?
3. Tambahkan `left: 10px` pada kotak yang sama, dan amati arah pergeserannya.
