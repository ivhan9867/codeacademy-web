---
id: css-layout-eval-2
courseId: fe-css-02
order: 24
title: "Rangkuman Panduan Cepat & Langkah Belajar Berikutnya"
readingMinutes: 4
section: EVALUASI
learningObjectives:
  - "Mengkaji intisari perbedaan sintaks dan peran Flexbox vs CSS Grid"
  - "Memiliki tabel sontekan cepat (cheat sheet mental) untuk debugging tata letak CSS"
  - "Mempersiapkan diri menuju materi interaktivitas JavaScript di kelas berikutnya"
---
Selamat! Kamu telah menyelesaikan seluruh 24 modul pembelajaran di kelas **CSS Layout Modern: Flexbox & Grid**.

Kini kamu memiliki fondasi yang sangat kokoh dalam merancang antarmuka web masa kini. Mari kita simpan intisari materi ini ke dalam memori jangka panjangmu.

![Peta Rangkuman Mental Flexbox dan CSS Grid](asset:images/fe-css-02/rangkuman-mental-layout.png)

## Tabel Sontekan Cepat (*Cheat Sheet*)

### 1. Flexbox (Sistem 1-Dimensi)
- `display: flex;` — Menghidupkan mode fleksibel pada anak langsung.
- `flex-direction: row | column | row-reverse | column-reverse;` — Menentukan arah Sumbu Utama (*Main Axis*).
- `justify-content: flex-start | center | flex-end | space-between | space-evenly;` — Meratakan dan membagikan sisa ruang di sepanjang Sumbu Utama.
- `align-items: stretch | center | flex-start | flex-end;` — Menyelaraskan item pada Sumbu Silang (*Cross Axis*).
- `flex-wrap: wrap;` — Mengizinkan elemen melipat ke baris baru saat ruang habis.
- `gap: 16px;` — Memberi jarak antar-item secara bersih tanpa margin hack.
- `flex: 1;` — Memerintahkan elemen anak untuk meregang mengisi ruang kosong secara merata.
- `order: -1 | 0 | 1;` — Mengatur ulang urutan visual tanpa merusak urutan dokumen HTML.

### 2. CSS Grid (Sistem 2-Dimensi)
- `display: grid;` — Mengaktifkan sistem kisi dua dimensi.
- `grid-template-columns: repeat(3, 1fr);` — Membelah kontainer menjadi 3 kolom fraksi seimbang.
- `grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));` — Mantra galeri responsif otomatis tanpa `@media` query!
- `grid-column: 1 / 3;` atau `grid-column: span 2;` — Merentangkan kartu melintasi beberapa kolom.
- `grid-template-areas:` — Menggambar denah visual halaman langsung di dalam kode CSS.

## 3 Aturan Emas Debugging Layout

Jika suatu hari layout halamanmu terasa tidak berjalan sesuai harapan, periksa 3 hal ini:
1. **Apakah properti ditulis di tempat yang benar?**
   Jangan terbalik antara properti kontainer induk (`justify-content`, `gap`, `flex-wrap`, `grid-template-columns`) dan properti elemen anak (`flex: 1`, `align-self`, `grid-column`, `order`).
2. **Periksa Sumbu Utama saat ini**:
   Jika kamu menetapkan `flex-direction: column;`, maka `justify-content` kini mengendalikan arah **vertikal**, bukan horizontal!
3. **Beri Garis Batas Sementara**:
   Beri `border: 1px dashed red;` pada kontainer untuk melihat batas ruangan yang sebenarnya dimiliki elemen tersebut.

## Langkah Selanjutnya: Bersiap Menuju JavaScript!

Tampilan antarmukamu sekarang sudah cantik, rapi, dan responsif di berbagai perangkat. Namun, tombol-tombol tersebut belum memiliki fungsi logika dan aksi dinamis.

Di kelas berikutnya (**JavaScript Fundamentals**), kamu akan mempelajari bagaimana memanipulasi elemen HTML dan CSS ini menggunakan kode program interaktif: membuka sidebar menu dengan ketukan tombol hamburger, mengubah tema gelap (*dark mode*), dan mengambil data langsung dari internet.

Sebelum itu, tuntaskan **Kuis Evaluasi Akhir** di bawah untuk menguji pemahamanmu dan mengklaim sertifikat kelulusan kelas ini!
