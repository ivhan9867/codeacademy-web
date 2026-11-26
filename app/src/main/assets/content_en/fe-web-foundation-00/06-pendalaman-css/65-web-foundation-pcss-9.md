---
id: web-foundation-pcss-9
courseId: fe-web-foundation-00
order: 65
title: "Font Styling: Font Weight, Font Style, Font Variant, and Shorthand"
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
  - "Use font-weight, font-style, and font-variant"
  - "Write the font shorthand with the correct value order"
---
### font-weight

`font-weight` controls how thick or bold the letters appear. Its value can be a number from `100` to `900`, or a keyword:

- `normal` (equivalent to `400`)
- `bold` (equivalent to `700`)
- `bolder` and `lighter`, which are relative to the parent element

```css
strong {
  font-weight: 700;
}
```

Numeric values only take effect if the font being used includes that specific weight variant. Otherwise, the browser picks the closest available weight. This will be important when we load Google Fonts later.

### font-style

`font-style` controls the slant of the text:

- `normal`: upright.
- `italic`: uses the font's dedicated italic version, if available.
- `oblique`: tilts the regular font digitally.

```css
em {
  font-style: italic;
}
```

For most cases, `italic` is all you need.

### font-variant

`font-variant` is most commonly used with the value `small-caps`, which displays lowercase letters as smaller-sized uppercase letters.

```css
h1 {
  font-variant: small-caps;
}
```

If you have ever used the *small caps* feature in Microsoft Word, the concept is exactly the same.

### Shorthand font

Writing `font-family`, `font-size`, `font-weight`, `font-style`, and `font-variant` one by one can get repetitive. CSS provides the `font` shorthand to combine them into a single property.

```css
p {
  font: italic bold 16px/1.5 "Open Sans", sans-serif;
}
```

There are two things to remember:

1. **Order matters.** The general format is `style weight size/line-height family`. If the order is wrong, the entire declaration is treated as invalid.
2. **Two values are required:** `font-size` and `font-family`. All other values are optional.

The simplest valid example:

```css
p {
  font: 1em sans-serif;
}
```
