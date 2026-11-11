---
id: web-foundation-lanjutan-10
courseId: fe-web-foundation-00
order: 33
title: "Format Teks Inline Lanjutan: Citation, Istilah, Subscript, Superscript, Sorotan, dan Line Break"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menggunakan <cite> untuk judul rujukan dan <dfn> untuk istilah yang didefinisikan"
  - "Menulis format teks <sub>, <sup>, <mark>, serta pemisah baris <br>"
codeSnippet: |
  <p>Aku baru selesai membaca <cite>Filosofi Teras</cite> bulan ini.</p>
  
  <p><dfn>HTML</dfn> adalah bahasa markup untuk menyusun struktur halaman web.</p>
  
  <p>Air memiliki rumus kimia H<sub>2</sub>O, dan 2<sup>10</sup> sama dengan 1024.</p>
  
  <p>Kata kunci favoritku sejauh ini: <mark>elemen</mark> dan <mark>atribut</mark>.</p>
  
  <p>
    Alamat latihanku:<br>
    Jl. Belajar No. 1<br>
    Semarang, Jawa Tengah
  </p>
---
Masih ada beberapa elemen inline lagi yang berguna untuk memperkaya konten halamanmu.

## Citation: cite

Elemen `<cite>` dipakai untuk merujuk judul sebuah karya: buku, artikel, film, lagu, dan sejenisnya. Bawaannya ditampilkan miring.

```html
<p>Aku baru selesai membaca <cite>Laskar Pelangi</cite> minggu lalu.</p>
```

## Mendefinisikan istilah: dfn

Elemen `<dfn>` dipakai saat kamu mendefinisikan sebuah istilah untuk pertama kalinya, biasanya di dalam kalimat yang menjelaskannya. Bawaannya juga ditampilkan miring.

```html
<p><dfn>HTML</dfn> adalah bahasa markup untuk menyusun struktur halaman web.</p>
```

## Subscript dan superscript: sub dan sup

- **`<sub>`**: menampilkan teks lebih kecil, sedikit di bawah baris normal
- **`<sup>`**: menampilkan teks lebih kecil, sedikit di atas baris normal

Biasa dipakai untuk rumus kimia atau matematika.

```html
<p>Air memiliki rumus kimia H<sub>2</sub>O.</p>
<p>5<sup>2</sup> sama dengan 25.</p>
```

## Menyorot teks: mark

Elemen `<mark>` menyorot bagian teks yang paling relevan dalam sebuah kalimat, misalnya kata kunci yang sedang dicari. Bawaannya tampil dengan latar kuning dan teks hitam.

```html
<p>Hasil pencarian untuk kata <mark>Playground</mark> ditemukan di tiga modul.</p>
```

## Baris baru: br

Kamu sudah tahu browser mengabaikan Enter di kode sumber. Kalau kamu memang butuh baris baru di tengah sebuah paragraf (bukan paragraf baru), pakai `<br>`. Elemen ini tidak punya konten dan tidak butuh tag penutup, sama seperti `<img>`.

```html
<p>
  Jl. Merdeka No. 10<br>
  Semarang, Jawa Tengah
</p>
```

Pakai `<br>` secukupnya. Kalau kamu butuh memisahkan dua ide besar, biasanya `<p>` baru lebih tepat daripada menumpuk `<br>`.

## Kesalahan yang sering terjadi

- Memakai `<br>` berkali-kali untuk membuat jarak antarparagraf, padahal seharusnya memakai `<p>` terpisah
- Memakai `<mark>` untuk sekadar mempercantik tampilan tanpa alasan relevansi konten

## Coba sendiri

Tambahkan satu kalimat yang memakai `<sup>` untuk menuliskan pangkat, dan satu alamat singkat yang memakai `<br>` untuk pindah baris.
                    
