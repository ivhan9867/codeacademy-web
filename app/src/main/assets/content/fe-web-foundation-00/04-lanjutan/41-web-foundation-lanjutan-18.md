---
id: web-foundation-lanjutan-18
courseId: fe-web-foundation-00
order: 41
title: "Tabel: Pengenalan dan Struktur Dasar"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menyusun tabel data dua dimensi menggunakan elemen <table>, <tr>, <th>, dan <td>"
  - "Membedakan sel judul kolom (<th>) dan sel data reguler (<td>)"
codeSnippet: |
  <table>
    <tr>
      <th>Bulan</th>
      <th>Target Belajar</th>
    </tr>
    <tr>
      <td>Januari</td>
      <td>Dasar HTML</td>
    </tr>
    <tr>
      <td>Februari</td>
      <td>Dasar CSS</td>
    </tr>
  </table>
---
Sebagian informasi lebih mudah dipahami kalau ditampilkan dalam baris dan kolom: klasemen pertandingan, jadwal, atau daftar harga. Untuk itulah HTML menyediakan elemen tabel.

## Apa itu tabel di HTML

Elemen `<table>` merepresentasikan **data tabular**: informasi yang disajikan dalam dua dimensi, baris dan kolom, dengan setiap perpotongan baris-kolom disebut **sel**.

## Tiga elemen dasar penyusun tabel

- **`<table>`**: pembungkus seluruh tabel
- **`<tr>`** (*table row*): satu baris tabel
- **`<td>`** (*table data*) atau **`<th>`** (*table header*): satu sel dalam baris itu

`<td>` dipakai untuk sel data biasa, sedangkan `<th>` dipakai untuk sel header yang menjadi judul kolom atau baris. Secara bawaan, isi `<th>` ditampilkan tebal dan rata tengah.

```html
<table>
  <tr>
    <th>Nama</th>
    <th>Hobi</th>
  </tr>
  <tr>
    <td>Rani</td>
    <td>Menggambar</td>
  </tr>
  <tr>
    <td>Budi</td>
    <td>Bermain Gitar</td>
  </tr>
</table>
```

Setiap `<tr>` baru berarti baris baru, dan jumlah `<td>`/`<th>` di dalamnya menentukan jumlah kolom pada baris itu. Isi sebuah sel boleh apa saja: teks, gambar, bahkan tabel lain di dalamnya.

## Kesalahan yang sering terjadi

- Menaruh `<td>` atau `<th>` langsung di dalam `<table>` tanpa dibungkus `<tr>`
- Jumlah sel yang tidak konsisten antar baris, sehingga tabel terlihat berantakan
- Memakai `<th>` untuk semua sel, padahal hanya baris/kolom judul yang seharusnya `<th>`

## Coba sendiri

Buat satu tabel kecil berisi dua kolom (misalnya "Bulan" dan "Target Belajar") dan tiga baris data.
                    
