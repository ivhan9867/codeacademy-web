---
id: web-foundation-inti-7
courseId: fe-web-foundation-00
order: 22
title: "Anatomi Dokumen HTML"
readingMinutes: 7
section: INTI
codeLanguage: html
isProjectContinuation: true
verificationRules:
  - "<!DOCTYPE html>"
  - "<title>"
  - "<body>"
learningObjectives:
  - "Menjelaskan fungsi deklarasi <!DOCTYPE html> untuk standar HTML5 modern"
  - "Membedakan peran elemen <html>, <head> untuk metadata, dan <body> untuk konten visual"
  - "Menambahkan elemen <meta charset="utf-8"> dan <title> pada bagian head"
codeSnippet: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="utf-8">
      <title>Profil Rani</title>
    </head>
    <body>
      <h1>Profil Rani</h1>
      <p>Pelajar dan calon web developer dari Semarang.</p>
  
      <h2>Tentang Saya</h2>
      <p>
        Aku suka menggambar dan bermain gitar. Sejak kecil aku penasaran
        bagaimana sebuah aplikasi dibuat.
      </p>
  
      <h2>Hobi</h2>
      <h3>Menggambar</h3>
      <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
      <h3>Bermain Gitar</h3>
      <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
    </body>
  </html>
---
Sebuah koran punya susunan yang jelas: judul besar, subjudul, lalu paragraf-paragraf di bawahnya. Halaman web pun begitu. Bedanya, susunannya tidak hanya dilihat manusia, tetapi juga dibaca browser. Di modul ini kita bedah seluruh anatomi dokumen HTML dari atas sampai bawah.

## Pohon dokumen (DOM Tree)

Karena elemen boleh berada di dalam elemen lain, seluruh dokumen membentuk **hierarki**, mirip silsilah keluarga. Ada induk, anak, dan saudara. Bentuknya bisa digambarkan seperti pohon:

```text
html
  head
    meta
    title
  body
    h1
    h2
    p
```

Elemen `html` adalah **akar** (*root*) pohon. Elemen `head` dan `body` adalah anak dari `html`, dan sekaligus bersaudara satu sama lain. Elemen `h1`, `h2`, dan `p` adalah anak dari `body`.

Browser menyimpan dokumenmu dalam bentuk pohon ini di memorinya, yang dinamakan **DOM** (*Document Object Model*). Nanti, saat belajar JavaScript, kamu akan mengubah tampilan halaman dengan cara mengubah pohon ini.

## Doctype

Baris pertama dokumen HTML adalah **doctype**. Fungsinya memberi tahu browser standar HTML mana yang dipakai dokumen ini. Di masa lalu, doctype ditulis panjang dan sulit dihafal. Sekarang, cukup satu baris:

```html
<!DOCTYPE html>
```

Kalau doctype tidak ada, browser bisa masuk ke *quirks mode*, yaitu mode kompatibilitas untuk halaman lawas. Akibatnya tampilan bisa berbeda antarbrowser dan sulit ditebak. Jadi tulislah selalu, selalu di baris paling atas.

## Elemen html

Elemen `<html>` membungkus seluruh isi dokumen dan menjadi akar pohon. Sebaiknya tambahkan atribut `lang` untuk memberi tahu bahasa halaman:

```html
<html lang="id">
```

Dengan itu, browser dan pembaca layar tahu bahwa halamanmu berbahasa Indonesia.

## Elemen head

Elemen `<head>` menyimpan **informasi tentang halaman**, bukan isi yang dilihat pengunjung. Apa pun yang ada di `head` tidak tampil di halaman. Elemen yang biasa ada di dalamnya:

- `<title>`: judul halaman untuk tab browser, bookmark, dan hasil pencarian
- `<meta>`: metadata, seperti pengodean karakter, deskripsi halaman, atau pengaturan tampilan di HP
- `<link>`: menghubungkan sumber luar, misalnya berkas CSS atau ikon halaman
- `<style>`: menulis CSS langsung di dalam dokumen
- `<script>`: memuat atau menulis JavaScript
- `<noscript>`: konten cadangan yang tampil bila JavaScript dimatikan
- `<base>`: alamat dasar untuk semua tautan relatif di halaman

Contoh isi `head` yang sederhana:

```html
<head>
  <meta charset="utf-8">
  <title>Judul Halaman</title>
  <style>
    /* kode CSS ditulis di sini */
  </style>
</head>
```

Spesifikasi HTML5 sebenarnya mengizinkan tag `<head>` dan `<body>` dilewati, karena browser akan menambahkannya sendiri. Jangan dimanfaatkan. Menulis keduanya membuat dokumen lebih jelas dan minim kejutan, dan itu kebiasaan yang dipakai developer profesional.

## Elemen body

Elemen `<body>` menyimpan **semua yang tampil** di layar: judul, paragraf, gambar, tautan, dan seterusnya. Kalau `head` adalah dapur, `body` adalah ruang makan tempat hidangannya disajikan.

## Dokumen lengkap

Gabungan semuanya, seperti di bagian *Contoh Kode Praktik*, membentuk dokumen HTML yang utuh dan benar:

- Baris pertama: doctype
- Elemen `html` dengan `lang="id"` sebagai akar
- `head` berisi `meta charset` dan `title`
- `body` berisi konten yang tampil, dengan elemen judul dan paragraf

## Kesalahan yang sering terjadi

- Menaruh `<title>` di dalam `<body>`
- Menulis dua elemen `<body>` atau dua elemen `<html>`
- Lupa doctype
- Mengira isi `head` akan tampil di halaman

## Coba sendiri

Tambahkan `<meta name="description" content="Halaman profil pribadi">` ke dalam `head`, lalu jalankan. Tidak ada yang berubah di layar. Kenapa? Karena `head` memang bukan untuk ditampilkan. Informasi itu dibaca oleh browser dan mesin pencari.
                    
