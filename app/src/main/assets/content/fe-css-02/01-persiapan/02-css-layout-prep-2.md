---
id: css-layout-prep-2
courseId: fe-css-02
order: 2
title: "Peta Kemampuan: Kapan Flexbox, Kapan Grid?"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Membedakan sistem 1-dimensi (Flexbox) dan 2-dimensi (CSS Grid) melalui analogi dunia nyata"
  - "Memahami pendekatan Content-First vs Layout-First dalam perancangan antarmuka"
  - "Menentukan pilihan teknologi yang tepat untuk komponen antarmuka yang akan dibangun"
---
Salah satu pertanyaan paling sering dari pemula adalah: *"Kalau sudah ada Grid yang canggih, apakah Flexbox masih perlu? Dan kapan saya harus memilih salah satunya?"*

Jawabannya: **keduanya saling melengkapi, bukan saling bersaing**. Pengembang web profesional menggunakan Flexbox dan Grid berdampingan di halaman yang sama.

```text
Flexbox (1 Dimensi - 1 sumbu aktif):
  [ Item 1 ]──[ Item 2 ]──[ Item 3 ]  (baris ATAU kolom)

CSS Grid (2 Dimensi - baris & kolom serentak):
  +----------+----------+----------+
  | Header (span 3 kolom)          |
  +----------+---------------------+
  | Sidebar  | Konten Utama        |
  +----------+---------------------+
```
## Analogi 1: Rak Buku vs Denah Kursi Bioskop

Bayangkan dua situasi di dunia nyata:

1. **Flexbox itu seperti Rak Buku (1 Dimensi)**:
   Saat kamu menyusun buku di atas rak, kamu mengaturnya berjejer sepanjang satu garis horizontal. Kalau ada buku yang lebih tebal atau lebih tinggi, rak tersebut tetap mengalirkan buku-buku lain ke samping kanannya. Arah alirannya fokus pada **satu dimensi**: mendatar (baris) atau menurun (kolom).
2. **Grid itu seperti Denah Kursi Bioskop (2 Dimensi)**:
   Saat memesan tiket bioskop, kursi disusun dalam baris (huruf A, B, C...) dan kolom (nomor 1, 2, 3...). Kamu bisa menunjuk kursi `C-4` yang berada tepat di perpotongan baris dan kolom. CSS Grid bekerja pada **dua dimensi sekaligus** secara presisi.

## Content-First vs Layout-First

Perbedaan pola pikir mendasar keduanya terletak pada cara kamu memulai:

- **Flexbox menganut pola Content-First (Berbasis Isi Konten)**:
  Kamu punya beberapa elemen dengan ukuran teks atau ikon yang berbeda-beda, dan kamu ingin elemen-elemen tersebut saling berbagi tempat, sejajar rapi, atau menyesuaikan ruang secara lentur. Contoh: menu navigasi, deretan tag/kategori, dan tombol aksi dengan ikon.
- **Grid menganut pola Layout-First (Berbasis Denah Kerangka)**:
  Kamu merancang wadah kotak-kotak terlebih dahulu (misalnya: area Header di atas, Sidebar selebar 250px di kiri, Konten di tengah, dan Footer di bawah), baru kemudian memasukkan elemen ke dalam kotak-kotak yang sudah dipesan.

## Tabel Panduan Cepat Pengambilan Keputusan

| Kebutuhan Antarmuka | Pilihan Ideal | Alasan Utama |
| :--- | :---: | :--- |
| Merapikan ikon di samping teks tombol | **Flexbox** | Aliran 1D, sangat mudah mengatur posisi vertikal ke tengah |
| Membuat bilah navigasi dengan logo di kiri & menu di kanan | **Flexbox** | Pembagian ruang fleksibel dengan `space-between` |
| Membagi kerangka halaman utama (Header, Sidebar, Main, Footer) | **CSS Grid** | Tata letak 2D yang kokoh dan tidak bergeser |
| Galeri foto kartu produk yang sama rata dalam kotak | **CSS Grid** | Mengunci baris dan kolom dengan ukuran proporsional |
| Menyelaraskan teks harga dan tombol di dalam kartu produk | **Flexbox** | Flexbox bersarang (*nested*) di dalam sel Grid |

Di kelas ini, kita akan mempelajari Flexbox terlebih dahulu sebagai fondasi kelenturan elemen, lalu melangkah ke CSS Grid untuk menaklukkan arsitektur tata letak dua dimensi.
