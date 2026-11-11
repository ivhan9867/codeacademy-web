---
id: web-foundation-pcss-2
courseId: fe-web-foundation-00
order: 58
title: "Selector Dasar: Type Selector dan Class Selector"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .red {
    color: red;
  }
codeLanguage: css
starterHtml: |
  <h1 class="red">Judul berwarna merah</h1>
  <p class="red">Paragraf berwarna merah</p>
learningObjectives:
  - "Menjelaskan fungsi type selector dan class selector"
  - "Menerapkan satu class pada beberapa elemen, dan menggabungkan beberapa class dalam satu elemen"
---
Selector adalah bagian dari sebuah rule yang menentukan elemen mana yang akan terkena gaya. Sejauh ini kamu sudah memakai salah satu bentuk paling dasarnya. Sekarang kita bahas lima jenis *basic selector* yang umum dipakai:

1. Type selector
2. Class selector
3. ID selector
4. Attribute selector
5. Universal selector

Dua yang pertama kita bahas sekarang.

## Type selector

Type selector memakai **nama elemen HTML** sebagai target. Artinya, rule yang kamu tulis akan berlaku untuk *semua* elemen dengan nama itu di dokumen.

```css
span {
  color: red;
}
```

Kalau dokumen punya lima `<span>`, semuanya akan berwarna merah. Type selector cocok untuk gaya dasar yang memang berlaku secara menyeluruh, misalnya semua paragraf memakai font yang sama.

## Class selector

Class selector memilih elemen berdasarkan nilai atribut `class`. Penulisannya diawali titik (`.`) lalu nama class.

```css
.red {
  color: red;
}
```

```html
<h1 class="red">Judul berwarna merah</h1>
<p class="red">Paragraf berwarna merah</p>
```

Keunggulan class adalah sifatnya yang **shareable**: satu class bisa dipakai di elemen dengan jenis yang berbeda-beda. Heading dan paragraf di atas sama-sama berwarna merah karena sama-sama memakai class `red`.

### Satu elemen, banyak class

Sebuah elemen boleh memiliki lebih dari satu class. Pisahkan nama class dengan spasi di dalam atribut `class`.

```html
<h1 class="red skyblue-bg">Teks merah dengan latar biru langit</h1>
<p class="red fancy">Paragraf merah yang bergaya fancy</p>
```

Dengan cara ini kamu bisa menyusun gaya seperti balok lego: satu class mengatur warna, class lain mengatur latar, dan elemen cukup memakai kombinasi yang dibutuhkan.

### Menargetkan class pada elemen tertentu

Kadang kamu ingin class yang sama memberi hasil berbeda tergantung jenis elemennya. Caranya, tulis nama elemen diikuti titik dan nama class.

```css
h1.fancy {
  font-style: italic;
}

p.fancy {
  text-decoration: underline;
}
```

Di sini, class `fancy` pada heading akan miring, sedangkan pada paragraf akan bergaris bawah. Kedua elemen sama-sama memakai class `fancy`, tetapi aturan yang berlaku berbeda.

> **Tips:** Untuk penamaan class, gunakan nama yang menjelaskan *fungsi* atau *tujuan*, misalnya `.card` atau `.teks-penting`, bukan nama warna seperti `.merah`. Nama yang berbasis warna akan terasa aneh kalau suatu hari warnanya diubah.

## Latihan singkat

Buat sebuah dokumen dengan satu `<h2>`, dua `<p>`, dan satu `<span>`. Lalu:

1. Buat rule type selector yang membuat semua `<p>` berwarna biru.
2. Buat class `.penting` dan terapkan ke salah satu paragraf dan ke `<span>`. Pastikan keduanya berubah menjadi tebal (`font-weight: bold;`).
3. Tambahkan class kedua, misalnya `.kecil` dengan `font-size: 14px;`, pada paragraf yang sama dengan `.penting`. Perhatikan bahwa keduanya berlaku bersamaan.
