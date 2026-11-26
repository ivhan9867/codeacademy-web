---
id: web-foundation-pcss-24
courseId: fe-web-foundation-00
order: 80
title: "Box Model: Margin"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu {
    margin: 20px;
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
  - "Explain the role of margin as the space outside an element's border"
  - "Understand margin collapsing in vertical spacing between elements"
  - "Set margin per side and with one-to-four-value shorthand"
---
Like padding, margin is also space around an element. The difference is that margin sits **outside** the border, not inside it. Margin keeps elements from colliding with one another and creates space from the edge of the browser window.

```css
.kartu {
  margin: 20px;
}
```

Imagine two cards placed side by side with no margin: they would stick tightly together and feel cramped. With margin, there is space separating them so the layout feels more spacious and structured.

## Margin collapsing

Vertical margins have a unique behavior called **margin collapsing**. When two elements are stacked vertically and each has a margin, the space between them is **not added together**; instead, **the larger value is used**.

```css
.atas {
  margin-bottom: 20px;
}

.bawah {
  margin-top: 20px;
}
```

If the two elements above are placed vertically next to each other, the gap between them is **not** 40px (20px + 20px), but stays 20px, because the two margins collapse into one another and the largest value wins.

Margin collapsing often surprises beginners because the result feels unexpected. What to remember: this phenomenon only happens with **vertical** margins between sibling elements, not with horizontal margins.

## Setting margin per side

The syntax pattern is exactly the same as border and padding:

```css
.kotak {
  margin-top: 10px;
  margin-right: 15px;
  margin-bottom: 20px;
  margin-left: 25px;
}
```

Or with shorthand:

```css
.kotak {
  margin: 10px 15px 20px 25px; /* top right bottom left */
}
```

Two values can also be used to differentiate vertical and horizontal margins:

```css
.kotak {
  margin: 10px 15px; /* top & bottom: 10px, left & right: 15px */
}
```

## Quick exercise

1. Create two `<div>` elements stacked vertically. Give the first one `margin-bottom: 30px` and the second one `margin-top: 20px`. Measure the distance between them — is it 50px or 30px?
2. Create three cards side by side horizontally with `margin: 0 10px` between them.
3. Compare the result of `margin: 20px` (one value) with `margin: 20px 10px` (two values) on the same element.
