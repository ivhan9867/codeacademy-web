---
id: web-foundation-lanjutan-19
courseId: fe-web-foundation-00
order: 42
title: "Spanning Cell: Colspan dan Rowspan"
readingMinutes: 5
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menggabungkan beberapa kolom sel tabel menggunakan atribut colspan"
  - "Menggabungkan beberapa baris sel tabel menggunakan atribut rowspan"
codeSnippet: |
  <table border="1">
    <tr>
      <th colspan="2">Rencana Belajar</th>
    </tr>
    <tr>
      <th rowspan="2">Bulan Ini</th>
      <td>Menuntaskan dasar HTML</td>
    </tr>
    <tr>
      <td>Berlatih di Playground setiap hari</td>
    </tr>
  </table>
---
Kadang satu sel perlu menjangkau lebih dari satu kolom atau baris sekaligus, mirip fitur "merge cell" di Excel. Di HTML, ini disebut **spanning cell**.

## Colspan: merentangkan kolom

Atribut `colspan` pada `<td>` atau `<th>` membuat sel itu menjangkau beberapa kolom sekaligus.

```html
<table border="1">
  <tr>
    <th colspan="2">Data Diri</th>
  </tr>
  <tr>
    <td>Nama</td>
    <td>Rani</td>
  </tr>
</table>
```

Di atas, header "Data Diri" merentang selebar dua kolom di bawahnya.

## Rowspan: merentangkan baris

Atribut `rowspan` bekerja serupa, tapi merentang ke bawah, menjangkau beberapa baris.

```html
<table border="1">
  <tr>
    <th rowspan="2">Hobi</th>
    <td>Menggambar</td>
  </tr>
  <tr>
    <td>Bermain Gitar</td>
  </tr>
</table>
```

Sel `<th>Hobi</th>` di atas menjangkau dua baris ke bawah, jadi baris kedua tidak perlu menuliskannya lagi, cukup satu `<td>` untuk isi tambahannya.

## Kesalahan yang sering terjadi

- Lupa mengurangi jumlah `<td>`/`<th>` di baris berikutnya setelah memakai `rowspan`, sehingga tabel jadi punya kolom ekstra yang tidak diinginkan
- Memakai `colspan`/`rowspan` dengan nilai yang tidak sesuai jumlah kolom/baris sebenarnya, membuat tabel miring atau tidak rapi

## Coba sendiri

Tambahkan atribut `border="1"` pada tabelmu supaya garis selnya terlihat, lalu coba buat satu header yang merentang dua kolom dengan `colspan`.
                    
