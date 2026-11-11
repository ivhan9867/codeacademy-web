---
id: web-foundation-pcss-38
courseId: fe-web-foundation-00
order: 94
title: "Positioning: Absolute Positioning"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Memahami bahwa absolute positioning benar-benar menghilangkan elemen dari normal flow"
  - "Mengetahui bahwa posisi absolute relatif terhadap jendela browser secara default"
  - "Membuat posisi absolute relatif terhadap elemen induk dengan memberi induk position: relative"
---
`position: absolute` mirip dengan `relative` dalam satu hal: elemen dikeluarkan dari normal flow, sehingga perpindahannya tidak memengaruhi elemen di sekitarnya. Tapi ada perbedaan besar: elemen dengan `position: absolute` **benar-benar dianggap tidak ada** oleh elemen-elemen di normal flow.

## Ruang kosong yang ditinggalkan terisi

Berbeda dengan `relative` yang menyisakan ruang kosong di posisi aslinya, elemen `absolute` tidak menyisakan ruang sama sekali. Akibatnya, elemen **di bawahnya akan naik** menempati ruang yang tadinya dipakai elemen tersebut.

```css
h1 {
  position: absolute;
  left: 500px;
}
```

Pada contoh ini, `<h1>` bergeser 500px dari kiri, dan paragraf yang tadinya berada di bawah `<h1>` sekarang naik menempati posisi semula `<h1>`.

## Relatif terhadap apa?

Secara default, posisi absolute dihitung **relatif terhadap jendela browser**. Tapi ada pengecualian penting: jika elemen ini punya **induk** yang juga berada di luar normal flow (artinya induknya memakai `position: relative`, `absolute`, atau `fixed`), maka posisi absolute akan dihitung relatif terhadap induk tersebut, bukan terhadap jendela browser.

```html
<div class="parent">
  <h1>Judul</h1>
</div>
```

Jika `.parent` **tidak** diberi `position` apa pun (tetap `static`), maka `<h1>` dengan `position: absolute` akan mengabaikan `.parent` sepenuhnya dan menghitung posisinya relatif terhadap jendela browser.

Tapi begitu kita tambahkan:

```css
.parent {
  position: relative;
}
```

Posisi `<h1>` sekarang akan dihitung **relatif terhadap `.parent`**, bukan lagi terhadap jendela browser. Pola ini — induk dengan `position: relative` tanpa nilai `top`/`left` apa pun, hanya untuk "menjangkarkan" posisi anaknya — sangat umum dipakai dalam praktik, dan sering disebut *positioning context*.
