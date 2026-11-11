---
id: web-foundation-pcss-40
courseId: fe-web-foundation-00
order: 96
title: "Floating"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Memahami fungsi dasar properti float untuk meletakkan elemen di kiri atau kanan"
  - "Menggunakan float untuk menyusun layout multi-kolom"
  - "Memahami pentingnya menetapkan width saat memakai float"
---
Properti `float` punya fungsi sederhana: membuat sebuah elemen "mengambang" ke sisi **kiri** atau **kanan** dari induknya. Jika diterapkan pada konteks tertentu, elemen lain di sekitarnya akan mengalir mengelilingi elemen yang di-float (disebut *text wrapping*) — mirip seperti gambar yang dikelilingi teks di majalah atau artikel.

```css
img {
  float: left;
  margin-right: 12px;
}
```

Selain untuk text wrapping, `float` juga lama dipakai sebagai salah satu teknik utama membangun **layout multi-kolom**, karena konsepnya sederhana dan mudah dipahami.

## Menyusun dua kolom dengan float

```css
.left-content {
  float: left;
  width: 30%;
}

.right-content {
  float: right;
  width: 70%;
}
```

Dengan `float: left` dan `float: right`, kedua elemen ini akan tersusun bersebelahan: konten kiri mengambil 30% lebar, konten kanan mengambil 70% sisanya.

> **Penting:** Saat memakai `float`, **selalu tetapkan `width`**. Tanpa lebar yang jelas, browser tidak tahu seberapa banyak ruang yang harus diberikan untuk setiap elemen, dan hasilnya bisa tidak terduga.

## Menyusun lebih dari dua kolom

Bagaimana kalau kita butuh tiga kolom atau lebih? Cukup beri `float: left` pada semuanya, dan kolom-kolom akan tersusun berurutan dari kiri ke kanan, sesuai urutan penulisannya di HTML:

```css
.kolom {
  float: left;
  width: 33.33%;
}
```

```html
<div class="kolom">Kolom 1</div>
<div class="kolom">Kolom 2</div>
<div class="kolom">Kolom 3</div>
```

Ketiga `<div>` ini akan tersusun bersebelahan dari kiri ke kanan, masing-masing mengambil sepertiga lebar induknya.
