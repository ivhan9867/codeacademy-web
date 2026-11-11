---
id: web-foundation-pcss-46
courseId: fe-web-foundation-00
order: 102
title: "Specific Style dengan Media Query"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Menulis aturan dasar @media dengan media-type dan media-feature-rule"
  - "Menerapkan styling khusus yang hanya berlaku di bawah atau di atas lebar viewport tertentu"
---
Meta tag viewport sudah membuat ukuran halaman menyesuaikan perangkat, tapi itu belum cukup. Elemen `<article>` masih terlalu sempit, dan `<aside>` masih sulit dibaca di layar kecil. Untuk benar-benar mengatur tampilan yang berbeda di tiap ukuran layar, CSS menyediakan fitur **Media Query**.

Dengan media query, kita bisa memberi instruksi seperti: "terapkan styling ini **hanya jika** lebar viewport lebih kecil dari 480 piksel."

## Struktur penulisan media query

```css
@media media-type and (media-feature-rule) {
  /* CSS rule apa pun diletakkan di sini */
}
```

- **media-type**: jenis media yang dituju. Nilai yang umum dipakai adalah `screen` (layar perangkat) atau `print` (saat dicetak).
- **media-feature-rule**: kondisi yang harus dipenuhi, misalnya lebar viewport (`max-width`, `min-width`), orientasi layar, atau jenis perangkat penunjuk (sentuh, mouse, dsb).
- **media-block**: kumpulan CSS rule yang hanya berlaku kalau kedua syarat di atas terpenuhi.

Contoh penerapannya:

```css
aside {
  width: 25%;
}

@media screen and (max-width: 600px) {
  aside {
    width: 100%;
  }
}
```

Pada contoh ini, `<aside>` biasanya memakai lebar 25%. Tapi begitu lebar viewport turun di bawah 600px, aturan di dalam `@media` akan "menimpa" aturan sebelumnya, membuat `<aside>` memakai lebar penuh (100%).

Coba bayangkan: dengan media query seperti ini, kita bisa memindahkan posisi `<aside>` ke atas `#content` ketika layar menyempit, dan mengembalikannya sejajar horizontal ketika layar melebar lagi — semuanya otomatis mengikuti lebar viewport, tanpa JavaScript sama sekali.

Dengan bekal ini, kita sudah siap memperbaiki masalah layout mobile pada Halaman Profil di latihan berikutnya.
