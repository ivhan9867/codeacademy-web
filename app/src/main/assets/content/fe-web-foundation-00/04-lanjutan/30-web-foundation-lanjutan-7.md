---
id: web-foundation-lanjutan-7
courseId: fe-web-foundation-00
order: 30
title: "Latihan: Menambahkan Gambar ke Halaman Profil"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Memasang foto profil dan gambar pendukung pada proyek profil pribadi"
  - "Menerapkan teks alternatif (alt) yang deskriptif untuk setiap gambar"
codeSnippet: |
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  
  <h2>Tentang Saya</h2>
  <img src="https://picsum.photos/seed/rani/400/220" alt="Ilustrasi suasana belajar coding">
  <p>Aku suka menggambar dan bermain gitar.</p>
  
  <h2>Hobi</h2>
  <img src="https://picsum.photos/seed/hobi/400/220" alt="Ilustrasi alat gambar dan gitar">
  <h3>Menggambar</h3>
  <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
---
Sekarang giliran menambahkan gambar ke halaman profilmu sendiri.

## Alur latihan

1. Buka halaman profil terakhirmu di Playground
2. Tambahkan satu `<img>` di setiap bagian yang menurutmu cocok diberi gambar (misalnya di bawah heading "Hobi")
3. Isi `src` dengan URL gambar dari internet, dan `alt` dengan deskripsi singkat
4. Jalankan dan lihat hasilnya

## Tentang alamat gambar: absolute vs relative URL

Karena di Playground kamu belum mengelola berkas gambar secara lokal, latihan ini memakai **absolute URL**: alamat lengkap gambar dari sebuah situs, seperti `https://picsum.photos/400/250`.

Sebagai pengetahuan tambahan: kalau nanti kamu bekerja dengan proyek yang punya folder gambar sendiri (misalnya folder `assets/image/`), kamu bisa memakai **relative URL**, yaitu alamat gambar yang ditulis relatif terhadap lokasi berkas `index.html`, tanpa menyebut domain apa pun:

```html
<!-- Relative URL: gambar disimpan di folder assets/image milik proyek -->
<img src="assets/image/bosscha.jpg" alt="Observatorium Bosscha">
```

Keduanya sama-sama valid, tinggal disesuaikan dengan di mana gambarnya disimpan.

## Bedah kode

Perhatikan `codeSnippet`: setiap bagian isi (Tentang Saya, Hobi) kini punya gambar pendukung setelah headingnya, dengan `alt` yang relevan dengan isi bagian tersebut.

## Kesalahan yang sering terjadi

- Menaruh `<img>` di dalam `<head>` (tidak akan tampil, karena `head` bukan untuk konten yang dilihat)
- Mengisi `alt` dengan teks yang tidak relevan dengan gambarnya

## Coba sendiri

Bandingkan tampilan sebelum dan sesudah menambahkan gambar. Coba juga sengaja salah ketik URL gambar, lalu lihat teks `alt` yang muncul menggantikannya.
                    
