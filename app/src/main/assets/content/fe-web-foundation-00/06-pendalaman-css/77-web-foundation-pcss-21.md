---
id: web-foundation-pcss-21
courseId: fe-web-foundation-00
order: 77
title: "Box Model: Box Dimensions"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  <div style="width: 300px; height: 300px;">
    <p style="width: 75%; height: 75%;">Paragraf ini 75% dari div induknya.</p>
  </div>
codeLanguage: html
learningObjectives:
  - "Mengatur dimensi box dengan width dan height"
  - "Membatasi ukuran dengan min-width, max-width, min-height, dan max-height"
  - "Menangani konten yang meluap dengan overflow"
---
## Dimensi box

Dimensi box dikontrol dengan `width` dan `height`. Secara default, box selalu cukup besar untuk menampung kontennya. Dengan menetapkan `width` atau `height`, kamu mengambil alih ukuran itu.

Ada tiga satuan yang paling sering dipakai:

- **Piksel (`px`)**: ukuran pasti dan mudah dikontrol. Populer, tetapi kurang fleksibel untuk layar yang berbeda-beda.
- **Persentase (`%`)**: relatif terhadap ukuran induk atau jendela browser. Cocok untuk layout yang menyesuaikan lebar layar.
- **Em (`em`)**: relatif terhadap ukuran teks elemen itu. Ukuran box ikut membesar ketika teksnya membesar.

Banyak developer kini memakai persentase dan `em` agar layout lebih adaptif.

```html
<div style="width: 300px; height: 300px;">
  <p style="width: 75%; height: 75%;">Paragraf ini 75% dari div induknya.</p>
</div>
```

Di contoh ini, `<div>` berukuran 300 × 300 piksel. Paragraf di dalamnya berukuran 75% dari induknya, yaitu sekitar 225 × 225 piksel.

### Membatasi dimensi

Layout modern sering melebar dan menyempit mengikuti layar. Agar konten tetap nyaman dibaca, kita bisa memberi batas dengan `min-width`, `max-width`, `min-height`, dan `max-height`:

- `min-width` menetapkan lebar minimal.
- `max-width` menetapkan lebar maksimal.

```css
.artikel {
  max-width: 650px;
}
```

Batas lebar seperti ini penting agar baris teks tidak terlalu panjang. Baris yang terlalu panjang membuat mata pembaca cepat lelah, dan di ponsel justru bisa terlalu sempit jika tidak dibatasi.

### Overflow

Jika kamu menetapkan ukuran tetap, tetapi kontennya lebih besar dari kotak, konten akan **meluap** (overflow) keluar dari kotak. Ini bisa terjadi ketika teks atau gambar terlalu banyak.

```html
<div style="width: 200px; height: 100px;">
  <p>Paragraf yang sangat panjang sekali sehingga tidak muat di dalam kotak kecil ini dan akan meluap keluar.</p>
</div>
```

Untuk mengatur perilaku ini, gunakan properti `overflow`. Pilihan nilainya akan dibahas di bagian berikutnya, bersama konsep `box-sizing`.

## Latihan singkat

1. Buat tautan navigasi dengan `font-size: 18px`, `font-weight: 400`, dan tanpa garis bawah. Tambahkan `:hover` yang membuatnya tebal.
2. Beri class `profile` pada `<article>`, lalu buat `<header>` di dalamnya rata tengah.
3. Ubah warna teks `body` menjadi `#333`, dan beri latar `#f0f8ff` pada sebuah `.kartu` dengan `padding: 16px`.
4. Buat satu `<div>` dengan latar hitam 50% transparan menggunakan `rgba()`, dan pastikan teks di atasnya tetap solid.
5. Terapkan `* { border: 1px solid red; }` lalu perhatikan semua kotak elemen yang muncul. Hapus kembali setelah selesai.
6. Buat `<div>` dengan `width: 200px` dan paragraf panjang di dalamnya, lalu amati bagaimana teks meluap.
