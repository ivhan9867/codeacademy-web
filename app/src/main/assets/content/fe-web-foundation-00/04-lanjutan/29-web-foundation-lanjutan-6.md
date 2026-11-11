---
id: web-foundation-lanjutan-6
courseId: fe-web-foundation-00
order: 29
title: "Gambar"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menampilkan gambar pada halaman web menggunakan elemen <img>"
  - "Mengisi atribut wajib src untuk path gambar dan alt untuk teks alternatif aksesibilitas"
  - "Mengatur proporsi dimensi gambar secara responsif dengan width atau height"
codeSnippet: |
  <h2>Tentang Saya</h2>
  <img src="https://picsum.photos/400/250" alt="Ilustrasi suasana belajar" width="400">
  <p>Aku suka menggambar dan bermain gitar.</p>
---
Halaman tanpa gambar terasa kurang hidup. Modul ini membahas cara menampilkan gambar di HTML.

## Elemen img

Gambar ditampilkan dengan elemen `<img>`. Elemen ini berbeda dari kebanyakan elemen lain: ia tidak punya konten dan tidak punya tag penutup.

```html
<img src="https://contoh.com/foto.jpg" alt="Deskripsi gambar">
```

Dua atribut pentingnya:

- **src**: sumber gambar, bisa berupa alamat website (URL) atau lokasi berkas lokal
- **alt**: teks alternatif yang tampil kalau gambar gagal dimuat, dan dibacakan oleh pembaca layar. Tidak wajib secara teknis, tapi sangat dianjurkan selalu diisi

Atribut lain yang berguna: **title**, teks tambahan yang muncul saat kursor diarahkan ke gambar.

## Format gambar yang umum

- **GIF** (`.gif`): bisa animasi, ukuran kecil, kualitas terbatas
- **JPEG** (`.jpg`, `.jpeg`): ukuran relatif kecil, cocok untuk foto, kurang tajam untuk gambar bertulisan
- **PNG** (`.png`): teks dan detail lebih tajam dari JPEG, tapi ukuran berkas lebih besar
- **WebP** (`.webp`): kompresi lebih baik dari JPEG dan PNG dengan kualitas setara
- **SVG** (`.svg`): berbasis vektor, ukuran kecil dan tetap tajam di ukuran berapa pun, cocok untuk logo dan ikon, kurang cocok untuk foto

## Mengatur ukuran gambar

Ukuran diatur lewat atribut `width` dan `height`. Aturan pentingnya: **isi salah satu saja**, biarkan yang lain menyesuaikan otomatis mengikuti rasio asli gambar.

```html
<!-- Aman: hanya width, height menyesuaikan -->
<img src="foto.jpg" alt="Foto" width="400">
```

Kalau kamu memaksa mengisi keduanya dengan rasio yang tidak sesuai aslinya, gambar akan tampil gepeng atau melar, tidak proporsional.

```html
<!-- Hindari ini kalau rasionya tidak sesuai -->
<img src="foto.jpg" alt="Foto" width="500" height="200">
```

## Kesalahan yang sering terjadi

- Lupa mengisi `alt`
- Mengisi `width` dan `height` sekaligus dengan rasio yang salah, sehingga gambar terlihat gepeng
- Menganggap `<img>` butuh tag penutup seperti `</img>`

## Coba sendiri

Tambahkan satu `<img>` ke halaman profilmu memakai gambar apa pun dari internet (isi `src` dengan URL-nya), lengkap dengan `alt` yang menjelaskan gambar itu.
                    
