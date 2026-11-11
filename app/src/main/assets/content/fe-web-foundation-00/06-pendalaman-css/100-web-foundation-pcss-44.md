---
id: web-foundation-pcss-44
courseId: fe-web-foundation-00
order: 100
title: "Latihan: Menerapkan Float pada Elemen Content dan Aside"
readingMinutes: 12
section: PENDALAMAN_CSS
learningObjectives:
  - "Menyusun #content dan aside sejajar horizontal dengan float"
  - "Mengatasi masalah collapse pada main dengan overflow: auto"
  - "Memahami mengapa padding pada elemen float bisa merusak total lebar, dan cara mengatasinya dengan box-sizing: border-box"
---
Teknik layouting dengan `float` sudah kita pelajari secara konsep. Sekarang saatnya menerapkannya ke studi kasus kita: membuat `<div id="content">` dan `<aside>` tersusun **sejajar secara horizontal**.

## Langkah 1: Float pada content

Buka `style.css`, lalu tambahkan `float: left` pada selector `#content`:

```css
#content {
  float: left;
}
```

Tambahkan juga lebarnya, 75%:

```css
#content {
  float: left;
  width: 75%;
}
```

Jika dijalankan sekarang, tampilan justru akan terlihat **berantakan**. Ini normal — karena `<aside>` belum diberi `float` apa pun, elemen itu (dan `<footer>` di bawahnya) akan ikut bergeser menempati ruang yang ditinggalkan `#content`.

## Langkah 2: Float pada aside

Tambahkan `float: right` pada `<aside>`, dengan lebar 25% (sisa dari 75% yang sudah dipakai `#content`):

```css
#content {
  float: left;
  width: 75%;
}

aside {
  float: right;
  width: 25%;
}
```

Sekarang tampilan malah terlihat **lebih kacau** dari sebelumnya: tinggi `<main>` menjadi 0 piksel, sehingga `<footer>` naik menimpa konten di atasnya.

## Langkah 3: Mengatasi collapse dengan overflow: auto

Masalah ini adalah masalah *collapse* yang sudah kita bahas di modul sebelumnya: `<main>` tidak lagi menganggap `#content` dan `<aside>` sebagai anaknya, karena keduanya sudah di-float. Solusinya, tambahkan `overflow: auto` pada `<main>`:

```css
main {
  padding: 20px;
  overflow: auto;
}
```

Sekarang layout sudah jauh lebih baik, dan `<footer>` kembali ke posisi seharusnya.

## Langkah 4: Memberi jarak antar kolom

Supaya `#content` dan `<aside>` tidak saling menempel, tambahkan `padding-left` pada `<aside>`:

```css
aside {
  float: right;
  width: 25%;
  padding-left: 20px;
}
```

Tapi begini perubahan ini malah membuat `<aside>` turun ke bawah `#content`! Ini terjadi karena menambahkan `padding-left: 20px` membuat **total lebar** `<aside>` menjadi lebih dari 25% (lebar konten + padding), sehingga tidak lagi muat di sisa ruang yang tersedia.

## Langkah 5: Memperbaiki dengan box-sizing: border-box

Ingat materi `box-sizing` dari modul Box Dimensions? Inilah saatnya kita memakainya. Dengan `box-sizing: border-box`, `padding` dan `border` akan dihitung **di dalam** lebar yang sudah ditetapkan, bukan menambah di luar lebar tersebut.

Terapkan ke seluruh elemen dengan universal selector:

```css
* {
  box-sizing: border-box;
}

body {
  font-family: 'Quicksand', sans-serif;
  margin: 0;
  padding: 0;
}
```

Setelah ini, lebar `<aside>` akan tetap tepat 25% meski sudah diberi `padding-left`, dan layout kembali rapi dengan `#content` dan `<aside>` sejajar sempurna.

Latihan ini memang menantang — kita menghadapi tiga masalah berturut-turut (collapse, susunan berantakan, dan lebar yang melar). Tapi justru dari sinilah kita belajar bagaimana box model, float, dan box-sizing saling berkaitan dalam praktik nyata.

## Latihan singkat

1. Terapkan `float: left` pada `#content` (75%) dan `float: right` pada `aside` (25%), lalu amati masalah collapse pada `main`.
2. Tambahkan `overflow: auto` pada `main`, lalu amati perbaikannya.
3. Tambahkan `padding-left: 20px` pada `aside`, lalu amati `aside` turun ke bawah.
4. Tambahkan `* { box-sizing: border-box; }` dan amati layout kembali rapi.
