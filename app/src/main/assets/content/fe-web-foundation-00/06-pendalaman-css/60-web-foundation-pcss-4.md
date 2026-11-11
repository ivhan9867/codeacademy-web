---
id: web-foundation-pcss-4
courseId: fe-web-foundation-00
order: 60
title: "Combinators: Adjacent Sibling dan General Sibling Selector"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  img + p {
    color: green;
  }
codeLanguage: css
starterHtml: |
  <img src="foto.jpg" alt="Foto profil">
  <p>Paragraf ini tepat di bawah gambar, jadi berwarna hijau.</p>
  <p>Paragraf ini tidak tepat di bawah gambar, jadi warnanya tetap.</p>
learningObjectives:
  - "Membedakan adjacent sibling selector (+) dan general sibling selector (~)"
  - "Menjelaskan syarat dua elemen disebut sibling"
---
Sejauh ini selector kita hanya menargetkan satu jenis elemen atau satu kondisi. Dengan **combinator**, kita bisa menyusun selector dari beberapa basic selector yang saling berhubungan. Ada empat combinator: adjacent sibling, general sibling, child, dan descendant. Dua yang pertama kita bahas sekarang.

Sebelum itu, pahami dulu istilah **sibling** (saudara). Dua elemen disebut sibling jika keduanya memiliki **induk (parent) yang sama**. Misalnya, dua paragraf di dalam satu `<article>` adalah sibling.

## Adjacent sibling selector (+)

Adjacent sibling selector menggabungkan dua selector dengan tanda `+`. Rule hanya berlaku untuk elemen **kedua**, dan hanya jika elemen itu berada **tepat setelah** elemen pertama.

```css
img + p {
  color: green;
}
```

```html
<img src="foto.jpg" alt="Foto profil">
<p>Paragraf ini tepat di bawah gambar, jadi berwarna hijau.</p>
<p>Paragraf ini tidak tepat di bawah gambar, jadi warnanya tetap.</p>
```

Ada dua syarat yang harus dipenuhi:
- Elemen kedua harus **langsung** mengikuti elemen pertama di kode HTML.
- Keduanya harus punya **induk yang sama**.

Pola ini berguna, misalnya, untuk memberi jarak khusus pada paragraf pertama setelah sebuah heading.

## General sibling selector (~)

General sibling selector bekerja mirip dengan adjacent, tetapi lebih longgar. Rule berlaku untuk **semua** elemen kedua yang berada *setelah* elemen pertama selama masih satu induk, walaupun tidak tepat bersebelahan.

```css
img ~ p {
  color: green;
}
```

Dengan aturan ini, semua paragraf yang berada setelah `<img>` di induk yang sama akan berwarna hijau. Simbol yang digunakan adalah tilde (`~`), yang di keyboard biasanya ada di kiri tombol angka `1`.

## Perbedaan keduanya dalam satu kalimat

`+` mengincar **satu** elemen yang tepat bersebelahan; `~` mengincar **semua** saudara yang muncul sesudahnya.

## Latihan singkat

Buat sebuah `<article>` dengan urutan: `<h2>`, `<p>`, `<p>`, `<img>`, `<p>`, `<p>`.

1. Gunakan `h2 + p` untuk membuat paragraf pertama setelah judul berwarna biru.
2. Gunakan `img + p` untuk membuat paragraf yang tepat setelah gambar berwarna hijau.
3. Gunakan `img ~ p` dan amati: paragraf mana saja yang ikut berubah?
