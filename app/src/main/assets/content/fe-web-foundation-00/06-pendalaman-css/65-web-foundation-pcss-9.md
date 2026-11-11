---
id: web-foundation-pcss-9
courseId: fe-web-foundation-00
order: 65
title: "Font Styling: Font Weight, Font Style, Font Variant, dan Shorthand"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  strong {
    font-weight: 700;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Area Latihan</title>
    </head>
    <body>
      <header>
        <h1>Judul Contoh</h1>
        <p>Paragraf contoh untuk mencoba properti CSS di atas.</p>
      </header>
      <nav>
        <ul>
          <li><a href="#">Beranda</a></li>
          <li><a href="#">Tentang</a></li>
          <li><a href="#">Kontak</a></li>
        </ul>
      </nav>
      <main>
        <div class="box">
          <span>Teks di dalam span</span>
          <p>Paragraf lain di dalam sebuah div.</p>
          <img src="https://placehold.co/150" alt="Contoh gambar" />
        </div>
      </main>
    </body>
  </html>
learningObjectives:
  - "Menggunakan font-weight, font-style, dan font-variant"
  - "Menulis shorthand font dengan urutan nilai yang benar"
---
### font-weight

`font-weight` mengatur ketebalan huruf. Nilainya bisa berupa angka dari `100` sampai `900`, atau kata kunci:

- `normal` (setara `400`)
- `bold` (setara `700`)
- `bolder` dan `lighter`, yang sifatnya relatif terhadap induknya

```css
strong {
  font-weight: 700;
}
```

Angka hanya efektif jika font yang dipakai punya varian ketebalan tersebut. Jika tidak, browser memilih varian terdekat. Ini penting untuk Google Fonts yang akan kita pasang nanti.

### font-style

`font-style` mengatur kemiringan:

- `normal`: tegak.
- `italic`: memakai versi miring khusus dari font, jika tersedia.
- `oblique`: membuat font biasa jadi miring secara digital.

```css
em {
  font-style: italic;
}
```

Untuk sebagian besar kasus, `italic` sudah cukup.

### font-variant

`font-variant` paling sering dipakai dengan nilai `small-caps`, yaitu huruf kapital kecil yang tampak seperti kapital tetapi berukuran lebih kecil.

```css
h1 {
  font-variant: small-caps;
}
```

Kalau kamu pernah memakai fitur *small caps* di Microsoft Word, konsepnya sama persis.

### Shorthand font

Menulis `font-family`, `font-size`, `font-weight`, `font-style`, dan `font-variant` satu per satu bisa berulang-ulang. CSS menyediakan shorthand `font` untuk menggabungkannya dalam satu properti.

```css
p {
  font: italic bold 16px/1.5 "Open Sans", sans-serif;
}
```

Ada dua hal yang harus diingat:

1. **Urutannya penting.** Bentuk umumnya adalah `style weight size/line-height family`. Jika urutan salah, seluruh declaration dianggap tidak valid.
2. **Dua nilai wajib ada:** `font-size` dan `font-family`. Nilai lainnya boleh dihilangkan.

Contoh paling sederhana yang valid:

```css
p {
  font: 1em sans-serif;
}
```
