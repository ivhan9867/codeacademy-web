---
id: web-foundation-lanjutan-4
courseId: fe-web-foundation-00
order: 27
title: "List: Unordered, Ordered, dan Description List"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Membuat daftar tanpa urutan dengan <ul> dan daftar berurutan dengan <ol>"
  - "Menyusun pasangan istilah dan deskripsi menggunakan elemen <dl>, <dt>, dan <dd>"
  - "Mengatur atribut type, start, dan reversed pada ordered list"
codeSnippet: |
  <h2>Navigasi</h2>
  <ul>
    <li>Tentang Saya</li>
    <li>Hobi</li>
    <li>Rencana Belajar</li>
  </ul>
  
  <h2>Langkah Belajarku Hari Ini</h2>
  <ol>
    <li>Baca materi</li>
    <li>Coba kodenya di Playground</li>
    <li>Kerjakan tantangan kecil</li>
  </ol>
---
Tidak semua teks cocok dibungkus paragraf. Daftar (list) adalah salah satu contohnya, dan HTML punya tiga jenis daftar.

## Tiga jenis list

- **Unordered list**: daftar tanpa urutan penting, biasanya tampil dengan bullet
- **Ordered list**: daftar yang urutannya penting, tampil dengan penomoran otomatis
- **Description list**: daftar berisi istilah beserta penjelasannya

## Unordered list

Dibuat dengan `<ul>`, dan setiap itemnya dibungkus `<li>`.

```html
<ul>
  <li>Sejarah</li>
  <li>Geografis</li>
  <li>Wisata</li>
</ul>
```

Isi `<li>` boleh berupa elemen HTML lain, bahkan heading atau paragraf, dan `<ul>` boleh berisi `<ul>` lain di dalamnya untuk membuat daftar bersarang (*nested list*).

## Ordered list

Dibuat dengan `<ol>`, isinya tetap `<li>`. Bedanya, setiap item diberi nomor otomatis dan berurutan, cocok untuk instruksi langkah demi langkah.

```html
<ol>
  <li>Buka Playground</li>
  <li>Tulis kerangka dokumen</li>
  <li>Jalankan kodenya</li>
</ol>
```

Kamu bisa mengubah gaya penomoran lewat atribut `type`:

- `type="1"` — angka (bawaan)
- `type="a"` — huruf kecil
- `type="A"` — huruf besar
- `type="i"` — angka romawi kecil
- `type="I"` — angka romawi besar

Dua atribut lain yang berguna pada `<ol>`:

- **start**: menentukan nomor awal, misalnya `start="7"` membuat item pertama bernomor 7
- **reversed**: membalik urutan nomor jadi menurun. Atribut ini tidak butuh nilai, cukup ditulis namanya saja

```html
<ol start="7">
  <li>Item ketujuh</li>
  <li>Item kedelapan</li>
</ol>

<ol reversed>
  <li>Ditampilkan dengan nomor menurun</li>
  <li>Lalu turun satu lagi</li>
</ol>
```

## Description list

Dipakai untuk pasangan istilah dan penjelasannya, misalnya glosarium. Dibuat dengan `<dl>`, dengan `<dt>` (*description term*) untuk istilahnya dan `<dd>` (*description details*) untuk penjelasannya.

```html
<dl>
  <dt>HTML</dt>
  <dd>Bahasa markup untuk menyusun struktur halaman web.</dd>

  <dt>Elemen</dt>
  <dd>Unit pembangun HTML yang menandai satu bagian konten.</dd>
</dl>
```

Satu `<dt>` boleh diikuti lebih dari satu `<dd>` bila istilahnya punya beberapa penjelasan.

## Kesalahan yang sering terjadi

- Memakai `<ol>` untuk daftar yang urutannya sebenarnya tidak penting
- Lupa bahwa `<li>` harus berada di dalam `<ul>` atau `<ol>`, tidak berdiri sendiri

## Coba sendiri

Tambahkan `<ul>` berisi daftar hobimu di halaman profil, lalu buat satu `<ol>` berisi tiga langkah cara kamu belajar coding sehari-hari.
                    
