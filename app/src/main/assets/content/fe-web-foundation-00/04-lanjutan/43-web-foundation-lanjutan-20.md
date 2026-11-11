---
id: web-foundation-lanjutan-20
courseId: fe-web-foundation-00
order: 43
title: "Elemen dan Atribut Lain pada Tabel"
readingMinutes: 5
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menambahkan judul tabel dengan <caption> dan menstrukturkan tabel dengan <thead>, <tbody>, dan <tfoot>"
  - "Meningkatkan aksesibilitas tabel menggunakan atribut scope pada elemen <th>"
codeSnippet: |
  <table border="1">
    <caption>Data Singkat</caption>
    <thead>
      <tr>
        <th scope="col">Kategori</th>
        <th scope="col">Keterangan</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <th scope="row">Kota Asal</th>
        <td>Semarang</td>
      </tr>
      <tr>
        <th scope="row">Mulai Belajar</th>
        <td>2026</td>
      </tr>
    </tbody>
  </table>
---
Selain `table`, `tr`, `td`, dan `th`, ada beberapa elemen dan atribut tabel lain yang jarang dipakai tapi baik untuk diketahui.

## Elemen tambahan

- **caption**: judul untuk keseluruhan tabel, ditulis tepat setelah tag pembuka `<table>`
- **thead**: mengelompokkan baris-baris header tabel
- **tbody**: mengelompokkan baris-baris isi/data tabel
- **tfoot**: mengelompokkan baris-baris footer tabel (misalnya baris total)
- **col** dan **colgroup**: menetapkan satu kolom atau sekelompok kolom, biasanya dipakai untuk memberi gaya kolom tertentu lewat CSS

```html
<table border="1">
  <caption>Jadwal Belajar Mingguan</caption>
  <thead>
    <tr>
      <th>Hari</th>
      <th>Materi</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Senin</td>
      <td>HTML</td>
    </tr>
    <tr>
      <td>Rabu</td>
      <td>CSS</td>
    </tr>
  </tbody>
</table>
```

## Atribut tambahan pada td/th

- **headers**: mengaitkan sebuah sel data dengan `id` milik sel header tertentu, berguna untuk aksesibilitas pada tabel yang rumit
- **scope**: pada `<th>`, menyatakan apakah header itu berlaku untuk `row` (baris), `col` (kolom), `rowgroup`, atau `colgroup`

```html
<table border="1">
  <tr>
    <th scope="col">Nama</th>
    <th scope="col">Hobi</th>
  </tr>
  <tr>
    <th scope="row">Rani</th>
    <td>Menggambar</td>
  </tr>
</table>
```

Elemen dan atribut ini tidak wajib dipakai di tabel sederhana, tapi berguna sekali untuk tabel besar dan kompleks, terutama demi aksesibilitas.

## Kesalahan yang sering terjadi

- Menaruh `<caption>` di tempat selain tepat setelah tag pembuka `<table>`
- Memakai `thead`/`tbody`/`tfoot` tapi urutannya tertukar (urutan yang benar: `thead`, lalu `tbody`, baru `tfoot`)

## Coba sendiri

Tambahkan `<caption>` pada tabel yang sudah kamu buat di modul-modul sebelumnya.
                    
