---
id: web-foundation-pcss-33
courseId: fe-web-foundation-00
order: 89
title: "Latihan: Menyesuaikan Padding dan Margin pada Elemen Body dan Main"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Mengenali margin dan padding bawaan browser pada elemen body"
  - "Menghilangkan celah bawaan dengan margin: 0 dan padding: 0 pada body"
  - "Memberi padding pada elemen main agar konten tetap nyaman dilihat"
---
Ada satu hal penting yang mungkin belum kamu sadari. Coba perhatikan baik-baik: ada celah tipis antara konten Halaman Profil dan tepi jendela browser. Celah ini mungkin tidak terlalu kelihatan di ukuran normal, tapi akan sangat jelas jika halaman diperbesar.

## Dari mana celah ini berasal?

Jawabannya: **margin bawaan browser**. Setiap browser menerapkan gaya standarnya sendiri (*default stylesheet*) pada elemen `<body>`, termasuk margin kecil di sekelilingnya. Karena itu, meskipun kita tidak pernah menulis margin apa pun untuk `<body>`, celah itu tetap muncul.

## Menghilangkan celah pada body

Hilangkan margin dan padding bawaan dengan menetapkan keduanya ke `0`:

```css
body {
  font-family: 'Quicksand', sans-serif;

  margin: 0;
  padding: 0;
}
```

Setelah ini, layout akan memenuhi seluruh lebar dan tinggi browser tanpa celah.

## Memberi ruang pada main

Tapi sekarang muncul masalah baru: konten di dalam `<main>` jadi menempel langsung ke tepi layar, tanpa ruang sama sekali. Untuk itu, beri `padding` pada elemen `<main>`:

```css
main {
  padding: 20px;
}
```

Dengan kombinasi ini — `body` tanpa margin/padding, tapi `main` dengan padding — halaman akan terasa penuh sampai ke tepi layar, sementara kontennya sendiri tetap punya jarak yang nyaman dari tepi.

Dengan selesainya latihan ini, hampir seluruh bagian Halaman Profil sudah rapi. Tinggal satu hal besar yang belum kita sentuh: membuat `<div id="content">` dan `<aside>` tampil **sejajar secara horizontal**. Untuk itu, kita perlu mempelajari **positioning** dan **floating**, dua topik yang akan kita bahas di bagian berikutnya.

## Latihan singkat

1. Tambahkan `margin: 0; padding: 0;` pada `body`, lalu amati celah di tepi layar yang hilang.
2. Tambahkan `padding: 20px` pada `main`, lalu bandingkan kenyamanan membaca konten sebelum dan sesudahnya.
3. Coba ubah nilai padding `main` menjadi `40px`, lalu lihat bagaimana ruang di sekitar konten berubah.
