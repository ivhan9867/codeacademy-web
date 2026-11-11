---
id: web-foundation-lanjutan-21
courseId: fe-web-foundation-00
order: 44
title: "Latihan: Menambahkan Tabel Data Singkat"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Membuat tabel jadwal belajar atau data profil singkat di dalam halaman web"
  - "Menerapkan struktur <thead>, <tbody>, dan spanning cell pada tabel data profil"
codeSnippet: |
  <aside>
    <article>
      <header>
        <h2>Info Singkat</h2>
        <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
      </header>
  
      <section>
        <h3>Data Singkat</h3>
        <table>
          <tr>
            <th>Kota Asal</th>
            <td>Semarang</td>
          </tr>
          <tr>
            <th>Mulai Belajar</th>
            <td>2026</td>
          </tr>
          <tr>
            <th>Alat Belajar</th>
            <td>CodeAcademy Playground</td>
          </tr>
          <tr>
            <th>Target</th>
            <td>Menguasai dasar web</td>
          </tr>
        </table>
      </section>
    </article>
  </aside>
---
Sekarang giliran menambahkan tabel sungguhan ke halaman profilmu, di dalam kartu "Info Singkat" yang sudah kamu buat.

## Tujuan

Bagian "Info Singkat" di `<aside>` saat ini cuma berisi judul dan satu kalimat. Kamu akan memperkayanya dengan tabel data singkat tentang dirimu, tanpa border, menyatu rapi dengan kartu itu.

## Alur latihan

1. Bungkus judul dan kalimat "Info Singkat" yang sudah ada dengan `<header>`, di dalam `<article>` yang sama
2. Tambahkan `<section>` baru tepat setelah `<header>` itu, di dalam `<article>` yang sama
3. Di dalam `<section>`, tambahkan `<h3>` "Data Singkat" dan sebuah `<table>` berisi beberapa baris data tentang dirimu
4. Jalankan dan lihat hasilnya

## Bedah kode

Lihat `codeSnippet`. Struktur `<article>` di dalam `<aside>` sekarang:

```text
article
  header
    h2 "Info Singkat"
    p "Halaman ini dibuat sebagai..."
  section
    h3 "Data Singkat"
    table
```

Kenapa dipecah jadi `header` dan `section`? Supaya jelas mana bagian pembuka kartu (judul dan deskripsi singkat) dan mana bagian data tambahan (tabelnya) — pola yang sama seperti `header` dan `section` di dalam sebuah `article` besar yang kamu pelajari sebelumnya.

Tabel di sini sengaja tidak diberi atribut `border`, jadi garis selnya tidak terlihat kasat mata. Ini valid — atribut `border` memang opsional, dan nanti tampilan tabel (termasuk border, jarak antar sel, dan sebagainya) akan diatur lebih halus lewat CSS.

## Kesalahan yang sering terjadi

- Menaruh `<table>` di luar `<section>`, sehingga strukturnya tidak konsisten dengan pola header+section yang sudah dipelajari
- Lupa bahwa tanpa atribut `border`, tabel tetap valid meskipun garisnya tidak terlihat

## Coba sendiri

Tambahkan satu baris lagi ke tabelmu, misalnya "Bahasa yang Sedang Dipelajari: HTML".
                    
