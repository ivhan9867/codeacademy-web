---
id: web-foundation-pcss-10
courseId: fe-web-foundation-00
order: 66
title: "Text Styling: Line Height, Text Indent, and Text Alignment"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  /* Cara 1: angka tanpa satuan (dikalikan font-size) */
  p { font-size: 16px; line-height: 2; }

  /* Cara 2: persentase */
  p { font-size: 16px; line-height: 200%; }

  /* Cara 3: nilai absolut */
  p { font-size: 16px; line-height: 32px; }
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
  - "Adjust spacing between lines with line-height"
  - "Create indentation and hanging indents with text-indent"
  - "Control text alignment with text-align"
---
## Text: paragraph layout and styling

### line-height

`line-height` controls the spacing between lines of text. Its value sets the **minimum distance** from one line to the next. If a line contains a character that is taller than usual, the line height expands automatically so it doesn't get cut off.

There are three common ways to write it. All three produce spacing equal to twice the font size in this example:

```css
/* Method 1: unitless number (multiplied by font-size) */
p { font-size: 16px; line-height: 2; }

/* Method 2: percentage */
p { font-size: 16px; line-height: 200%; }

/* Method 3: absolute value */
p { font-size: 16px; line-height: 32px; }
```

The first method is the most recommended. Because it has no unit, the spacing automatically scales if the font size changes elsewhere.

### text-indent

`text-indent` shifts the **first line** of a paragraph, similar to indentation in printed documents.

```css
p {
  text-indent: 2em;
}
```

Its value can be in `px`, `em`, `in`, or `%`. Percentages are calculated relative to the width of the parent element.

Negative values are also allowed. This creates a *hanging indent*, where the first line extends to the left outside the rest of the paragraph. This effect is often used in reference lists or bibliographies.

Keep in mind that this property only affects the first line. To shift all lines of an element, use `margin` or `padding`, which we will cover in the box model.

### text-align

`text-align` controls the horizontal alignment of text within a block:

- `left` (default for languages read from left to right)
- `right`
- `center`
- `justify`: aligns text evenly along both the left and right edges by adjusting word spacing.

```css
h1 {
  text-align: center;
}
```

The default value `left` switches to `right` if the page uses a language read from right to left, such as Arabic.
