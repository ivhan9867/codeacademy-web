---
id: web-foundation-pcss-3
courseId: fe-web-foundation-00
order: 59
title: "Selector Dasar: ID, Attribute, dan Universal Selector"
readingMinutes: 7
section: PENDALAMAN_CSS
codeSnippet: |
  #special {
    background-color: skyblue;
  }
codeLanguage: css
starterHtml: |
  <div id="special">
    <p>Konten ini punya latar biru.</p>
  </div>
learningObjectives:
  - "Menggunakan ID selector secara benar, termasuk alasan mengapa nilai id harus unik"
  - "Memilih elemen berdasarkan atribut dengan attribute selector"
  - "Memakai universal selector dengan tepat dan hati-hati"
---
Tiga basic selector terakhir melengkapi daftar yang sudah kita kenal. Ketiganya sederhana dipakai, tetapi masing-masing punya kebiasaan yang perlu dipahami.

## ID selector

ID selector memilih elemen berdasarkan atribut `id`. Penulisannya memakai tanda pagar (`#`), yang sering disebut *hash* atau *octothorpe*.

```css
#special {
  background-color: skyblue;
}
```

```html
<div id="special">
  <p>Konten ini punya latar biru.</p>
</div>
```

Hal yang paling penting tentang `id` adalah **keunikannya**. Nilai `id` harus unik dan hanya dipakai di satu elemen dalam satu halaman. Atribut `id` juga *tidak* shareable, berbeda dengan `class`.

Sering kali ID dipakai untuk memberi arti pada generic element seperti `<div>` atau `<span>`, misalnya `<div id="header">`. Namun, jika kamu ingin satu gaya dipakai di banyak elemen, gunakan `class`, bukan `id`. Memakai `id` untuk gaya berulang akan membuat CSS sulit dirawat.

> **Catatan:** ID juga punya *bobot* (specificity) yang lebih tinggi daripada class. Itu akan kita bahas lebih dalam di modul tentang *cascade*. Untuk sekarang, cukup ingat bahwa ID harus unik dan sebaiknya dipakai seperlunya.

## Attribute selector

Attribute selector memilih elemen berdasarkan **keberadaan atribut** atau **nilai atributnya**. Contoh sederhananya:

```css
input[type="text"] {
  border: 1px solid #00a2c6;
}
```

Aturan di atas hanya menyasar `<input>` yang memiliki atribut `type` bernilai `text`. Ada beberapa bentuk yang sering dipakai:

| Syntax | Arti |
|---|---|
| `[attr]` | Elemen yang memiliki atribut `attr`, berapa pun nilainya |
| `[attr="value"]` | Atribut `attr` bernilai persis `value` |
| `[attr~="value"]` | Atribut `attr` punya satu kata `value` di antara beberapa kata yang dipisah spasi |
| `[attr^="value"]` | Nilai atribut `attr` diawali `value` |
| `[attr$="value"]` | Nilai atribut `attr` diakhiri `value` |
| `[attr*="value"]` | Nilai atribut `attr` mengandung `value` di mana pun posisinya |

Contoh praktis: `a[href^="https"]` menyasar tautan yang alamatnya diawali `https`, misalnya untuk memberi ikon kecil pada tautan eksternal yang aman.

## Universal selector

Universal selector ditulis dengan tanda bintang (`*`) dan berlaku untuk **semua elemen** di dokumen.

```css
* {
  box-sizing: border-box;
}
```

Universal selector sering dipakai untuk reset gaya awal, seperti aturan di atas yang membuat perhitungan lebar elemen lebih mudah dipahami (akan kita bahas di box model). Universal selector juga bisa digabung dengan selector lain untuk mempersempit target:

```css
.kartu * {
  margin: 0;
}
```

Aturan tersebut hanya berlaku untuk semua elemen di dalam `.kartu`. Hati-hati memakainya tanpa batas, karena aturan yang menyentuh semua elemen bisa berdampak besar pada performa dan membuat gaya sulit ditebak.

## Latihan singkat

1. Beri `id="judul-utama"` pada sebuah `<h1>`, lalu buat aturan `#judul-utama` yang memberinya warna teks berbeda.
2. Buat dua `<img>`: satu dengan `alt` dan satu tanpa `alt`. Gunakan `img[alt]` untuk memberi `border` hanya pada gambar yang punya `alt`.
3. Buat tautan dengan `href` yang diawali `https` dan satu lagi dengan `href` yang diawali `/`. Gunakan `a[href^="https"]` untuk memberi warna berbeda pada tautan eksternal.
