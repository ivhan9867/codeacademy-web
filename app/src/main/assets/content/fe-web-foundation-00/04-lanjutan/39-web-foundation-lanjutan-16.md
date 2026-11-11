---
id: web-foundation-lanjutan-16
courseId: fe-web-foundation-00
order: 39
title: "Latihan: Menyusun Header, Main, dan Footer"
readingMinutes: 8
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menata ulang proyek profil menggunakan tag layout semantik <header>, <main>, dan <footer>"
  - "Memverifikasi struktur semantik halaman profil di Playground"
codeSnippet: |
  <header>
    <h1>Profil Rani</h1>
    <p>Pelajar dan calon web developer dari Semarang.</p>
    <nav>
      <ul>
        <li><a href="#tentang-saya">Tentang Saya</a></li>
        <li><a href="#hobi">Hobi</a></li>
        <li><a href="#rencana-belajar">Rencana Belajar</a></li>
      </ul>
    </nav>
  </header>
  
  <main>
    <h2 id="tentang-saya">Tentang Saya</h2>
    <p>Aku suka menggambar dan bermain gitar.</p>
  
    <h2 id="hobi">Hobi</h2>
    <h3>Menggambar</h3>
    <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
    <h3>Bermain Gitar</h3>
    <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
  
    <h2 id="rencana-belajar">Rencana Belajar</h2>
    <p>Menuntaskan dasar HTML sebelum lanjut ke CSS.</p>
  
    <h2>Info Singkat</h2>
    <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
  </main>
  
  <footer>
    <p>&copy; 2026 Rani. Dibuat sebagai latihan belajar HTML.</p>
  </footer>
---
Saatnya menata ulang seluruh halaman profilmu memakai elemen semantik yang baru kamu pelajari.

## Tujuan

Halaman profilmu sejauh ini sudah punya judul, navigasi, dan beberapa bagian isi, tapi semuanya masih "rata" langsung di dalam `<body>` tanpa pengelompokan besar. Di latihan ini, kamu mengelompokkannya jadi tiga bagian besar: kepala (`header`), isi utama (`main`), dan kaki (`footer`).

## Alur latihan

1. Tambahkan satu paragraf hak cipta di bagian paling bawah halaman, sebelum ditata
2. Bungkus judul, deskripsi, dan navigasi dengan `<header>`
3. Bungkus paragraf hak cipta dengan `<footer>`
4. Bungkus semua konten yang tersisa (di antara header dan footer) dengan `<main>`
5. Jalankan dan bandingkan tampilannya dengan sebelumnya

## Bedah kode

Lihat `codeSnippet`. Perhatikan tiga hal:

- **Konten kepala**: judul (`h1`), deskripsi (`p`), dan `nav` berisi daftar tautan, semuanya dibungkus `<header>`
- **Konten utama**: semua bagian isi (Tentang Saya, Hobi dengan dua sub-bagiannya, Rencana Belajar) dibungkus `<main>`, karena inilah konten yang datang untuk dibaca pengunjung
- **Konten kaki**: paragraf hak cipta dibungkus `<footer>`

Kalau kamu jalankan halaman ini, tampilannya **tidak berubah sama sekali** dibanding sebelum ditata. Ini wajar. Kamu hanya menyusun ulang struktur dokumennya, belum mengubah gaya apa pun (itu tugas CSS nanti). Tapi sekarang browser, mesin pencari, dan pembaca layar jauh lebih paham peran setiap bagian halamanmu.

## Kesalahan yang sering terjadi

- Menaruh navigasi di luar `<header>`, padahal ia bagian dari kepala halaman
- Membungkus footer di dalam `<main>` (footer sebaiknya jadi saudara `<main>`, bukan anaknya)

## Coba sendiri

Setelah menata ulang, tambahkan satu kalimat baru di dalam `<footer>`, misalnya nama dan tahun pembuatan halamanmu.
                    
