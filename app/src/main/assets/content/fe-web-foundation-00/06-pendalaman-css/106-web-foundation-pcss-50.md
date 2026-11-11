---
id: web-foundation-pcss-50
courseId: fe-web-foundation-00
order: 106
title: "Rangkuman Pendalaman CSS"
readingMinutes: 10
section: PENDALAMAN_CSS
learningObjectives:
  - "Mengingat kembali seluruh materi Pendalaman CSS, dari selector sampai responsivitas"
---
Kita sudah menempuh perjalanan panjang di Pendalaman CSS — dari selector yang presisi, styling teks dan warna, box model, sampai membuat layout yang responsif. Berikut rangkuman untuk membantu kamu mengingat kembali setiap bagiannya.

## Selector Dasar

- **Type selector** (`p`) menargetkan semua elemen dengan nama tersebut.
- **Class selector** (`.nama`) menargetkan elemen berdasarkan atribut `class`, dan bisa dipakai di banyak elemen sekaligus (*shareable*).
- **ID selector** (`#nama`) menargetkan satu elemen dengan atribut `id` yang unik di seluruh dokumen.
- **Attribute selector** (`[type="text"]`) menargetkan elemen berdasarkan keberadaan atau nilai atributnya.
- **Universal selector** (`*`) menargetkan seluruh elemen, sering dipakai untuk reset gaya.

## Combinators

- **Adjacent sibling (`+`)**: hanya elemen kedua yang tepat berada setelah elemen pertama, dengan induk yang sama.
- **General sibling (`~`)**: semua elemen kedua setelah elemen pertama, selama induknya sama.
- **Child (`>`)**: hanya anak langsung dari elemen induk.
- **Descendant (spasi)**: semua turunan, langsung maupun tidak langsung.

## Pseudo Selector

- **Pseudo-class** (`:hover`, `:visited`, `:focus`) memilih elemen berdasarkan keadaannya.
- **Pseudo-element** (`::before`, `::after`) memilih atau menambahkan bagian semu dari sebuah elemen, berguna untuk menyisipkan konten tanpa menulisnya di HTML.

## Font Styling

- `font-family`: jenis font, idealnya ditulis sebagai font stack dengan generic font family di akhir.
- `font-size`: ukuran huruf — gunakan `rem`/`em` untuk aksesibilitas, `px` untuk kontrol pasti.
- `font-weight`, `font-style`, `font-variant`: ketebalan, kemiringan, dan small caps.
- `font`: shorthand dari properti di atas, dengan urutan `style weight size/line-height family`.

## Text Styling

- `line-height`: jarak antarbaris.
- `text-indent`: indentasi baris pertama paragraf.
- `text-align`: perataan teks (`left`, `right`, `center`, `justify`).
- `text-decoration`: garis bawah/atas/coret.
- `text-transform`: mengubah kapitalisasi tanpa mengubah teks asli.
- `letter-spacing` & `word-spacing`: jarak antarhuruf dan antarkata.
- `text-shadow`: bayangan pada teks.

## Menetapkan Nilai Warna

- **Numeric value**: RGB, HEX, atau HSL — presisi dan fleksibel.
- **Predefined color name**: nama warna seperti `red`, `blue` — cepat tapi terbatas.
- `color` mengatur warna teks, `background-color` mengatur warna latar.
- `opacity` mengatur tingkat terlihat seluruh elemen; alpha channel (`rgba()`, `hsla()`) membuat hanya warna yang transparan, tanpa memengaruhi isi di dalamnya.

## Box Model

Setiap elemen HTML menghasilkan sebuah kotak dengan empat lapisan: **content**, **padding**, **border**, dan **margin**.

- **Dimension**: `width` dan `height` mengatur ukuran konten; `min-width`/`max-width` membatasi rentang ukuran; konten yang melebihi ukuran kotak akan *overflow*.
- **Box-sizing**: `content-box` (default) menghitung padding dan border di luar `width`; `border-box` menghitung keduanya di dalam `width`, membuat ukuran akhir elemen sesuai yang ditetapkan.
- **Border**: `border-width`, `border-style`, `border-color`, atau shorthand `border` (urutan nilainya bebas).
- **Padding**: jarak antara konten dan border.
- **Margin**: jarak di luar border; margin vertikal antarelemen sibling bisa saling menumpuk (*margin collapsing*), dan `margin: 0 auto` menengahkan elemen secara horizontal.
- **Display roles**: `inline`, `block`, `inline-block`, dan `none` mengubah perilaku tampilan elemen.
- **Box shadow** dan **border-radius** (*rounded corner*) untuk mempercantik tampilan kotak.

## Positioning

- **Normal flow** (`static`): perilaku bawaan, elemen block tersusun vertikal berurutan.
- **Relative**: menggeser elemen dari posisi aslinya tanpa menghilangkan ruang kosongnya.
- **Absolute**: mengeluarkan elemen dari flow sepenuhnya, posisinya relatif terhadap induk ber-posisi non-static terdekat (atau jendela browser).
- **Fixed**: seperti absolute, tapi selalu relatif terhadap jendela browser dan tidak ikut ter-scroll.
- **Sticky**: gabungan relative dan fixed — menempel begitu mencapai tepi viewport.

## Floating

- `float: left`/`float: right` membuat elemen mengambang di satu sisi, dengan elemen lain mengalir di sekitarnya (*text wrapping*), dan juga dipakai untuk layout multi-kolom.
- Masalah umum: induk yang kehilangan tingginya (*collapse*) jika semua anaknya di-float.
- Solusinya: properti `clear` (biasanya pada elemen kosong), atau `overflow: auto` pada induknya.

## Responsivitas

- **Meta tag viewport** (`width=device-width, initial-scale=1`) membuat halaman menyesuaikan lebar layar perangkat, bukan otomatis mengecil.
- **Media query** (`@media screen and (max-width: ...)`) menerapkan styling khusus hanya pada kondisi layar tertentu, misalnya mengubah layout dua kolom menjadi bertumpuk di layar sempit.

## Debugging dan Optimasi

- Gunakan tools validasi CSS online untuk memeriksa kesalahan sintaks.
- **Minifikasi** CSS (menghapus whitespace, komentar, baris baru yang tidak perlu) mempercepat waktu muat halaman.

Dengan rangkuman ini, kamu sudah punya gambaran menyeluruh tentang CSS — dari detail kecil seperti selector sampai teknik membuat halaman responsif. Saatnya menguji pemahamanmu lewat kuis di bagian berikutnya!
