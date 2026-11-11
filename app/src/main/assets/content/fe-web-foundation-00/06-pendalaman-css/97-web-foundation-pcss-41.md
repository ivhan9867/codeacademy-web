---
id: web-foundation-pcss-41
courseId: fe-web-foundation-00
order: 97
title: "Permasalahan Penerapan Float"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Mengenali masalah collapse: induk dengan tinggi 0px saat anaknya di-float"
  - "Memahami bahwa elemen float dikeluarkan dari normal flow sehingga tidak dianggap ada oleh induknya"
---
`float` terlihat sangat mudah dipakai, baik untuk text wrapping maupun menyusun layout. Tapi ada satu efek samping yang hampir selalu ditemui pemula: **induk yang kehilangan tingginya**.

Bayangkan sebuah `<div>` yang hanya berisi satu elemen `<img>`, dan gambar tersebut diberi `float: left`:

```html
<div class="container">
  <img src="foto.jpg" alt="Foto" />
</div>
```

```css
.container img {
  float: left;
}
```

Hasilnya mengejutkan: `<div class="container">` akan memiliki **tinggi 0 piksel**, seolah-olah tidak berisi apa-apa — padahal secara visual gambar tetap terlihat di layar.

## Mengapa ini terjadi?

Penyebabnya adalah sifat dasar `float`: elemen yang di-float **dikeluarkan dari normal flow**, sehingga **tidak dianggap ada** oleh elemen induk yang menaunginya. Induk hanya menghitung tinggi dari elemen-elemen yang masih berada di normal flow. Karena satu-satunya anak di dalam `.container` sudah di-float, induk menganggap dirinya kosong.

Masalah ini jadi lebih terasa kalau kamu menambahkan elemen lain (misalnya paragraf) di bawah gambar tanpa float: paragraf itu akan ikut "naik" seolah gambar tidak pernah ada, karena ruang yang seharusnya ditempati gambar tidak lagi dihitung oleh induknya.

Ada dua cara umum untuk mengatasi masalah ini, yang akan kita bahas di dua modul berikutnya: menggunakan properti **`clear`**, dan menetapkan **`overflow: auto`** pada elemen induk.
