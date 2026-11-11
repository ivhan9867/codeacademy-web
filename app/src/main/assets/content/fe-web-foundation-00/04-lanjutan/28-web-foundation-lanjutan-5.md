---
id: web-foundation-lanjutan-5
courseId: fe-web-foundation-00
order: 28
title: "Latihan: Navigasi dengan List"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Mempraktikkan pembuatan menu navigasi sederhana menggunakan daftar <ul> dan <li>"
  - "Menata tautan menu di dalam struktur daftar yang semantik"
codeSnippet: |
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  
  <ul>
    <li>Tentang Saya</li>
    <li>Hobi</li>
    <li>Rencana Belajar</li>
  </ul>
  
  <h2>Tentang Saya</h2>
  <p>Aku suka menggambar dan bermain gitar.</p>
---
Saatnya memakai `<ul>` untuk kebutuhan nyata: daftar navigasi di halaman profilmu.

## Alur latihan

1. Buka halaman profil terakhirmu di Playground
2. Tambahkan `<ul>` berisi daftar bagian-bagian halaman, tepat di bawah paragraf pembuka
3. Jalankan dan lihat hasilnya

## Kenapa `<ul>`, bukan `<ol>`?

Daftar navigasi menunjuk ke bagian-bagian halaman yang bisa dibuka dalam urutan apa pun sesuai keinginan pembaca, tidak seperti resep masakan yang harus diikuti berurutan. Karena urutannya tidak penting secara makna, `<ul>` adalah pilihan yang tepat, meskipun nanti tampilannya (misalnya dibuat horizontal, tanpa bullet) akan diatur dengan CSS di kelas berikutnya.

## Bedah kode

Perhatikan pada `codeSnippet`: setelah paragraf pembuka, ada `<ul>` dengan tiga `<li>` yang masing-masing menyebut satu bagian halaman.

Sekarang, daftar itu baru **terlihat** seperti navigasi, tapi belum **berfungsi** sebagai navigasi. Mengklik tulisan "Hobi" belum akan membawamu ke bagian Hobi. Untuk itu, kamu butuh elemen anchor (`<a>`), yang akan dipelajari beberapa modul lagi.

## Kesalahan yang sering terjadi

- Menaruh `<li>` di luar `<ul>` atau `<ol>`
- Mengira daftar navigasi harus selalu pakai `<ol>` karena "urut dari atas ke bawah" — urutan tampilan berbeda dengan urutan makna

## Coba sendiri

Tambahkan satu item baru ke daftar navigasimu, misalnya "Kontak".
                    
