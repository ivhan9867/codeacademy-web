---
id: web-foundation-pcss-25
courseId: fe-web-foundation-00
order: 81
title: "Box Model: Centering Content with Margin Auto"
readingMinutes: 5
section: PENDALAMAN_CSS
codeSnippet: |
  .kontainer {
    width: 800px;
    margin: 0 auto;
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
  - "Center a box on a page or inside its parent element using margin: auto"
  - "Understand that width must be specified for centering with margin auto to work"
---
One of the most common layout needs is placing a box **right in the center** of the page or its parent element. The approach is quite simple: set the left and right margins to `auto`.

```css
.kontainer {
  width: 800px;
  margin: 0 auto;
}
```

There is one important requirement for this technique to work: the element **must have a specified `width`**. Without a `width`, a block element automatically takes up the full width of the page or its parent, leaving no empty space on the left and right to "center" into.

Here is how it works: after you set the box's width and set the left and right margins to `auto`, the browser calculates the remaining space on the left and right sides of the box and divides it **equally**. As a result, the box appears centered.

```css
body {
  margin: 0;
}

.kontainer {
  width: 600px;
  margin: 0 auto;
  background-color: #f0f8ff;
  padding: 24px;
}
```

This `width` + `margin: 0 auto` pattern is very commonly used to wrap an entire page's contents in a neat container that does not stretch edge-to-edge on large screens. Many professional websites use this pattern as their main content wrapper.

> **Note:** This technique only centers a box **horizontally**. To center vertically, you need other techniques such as Flexbox or Grid, which will be covered in the next section.

## Quick exercise

1. Create a `<div class="kontainer">` with `width: 700px` and `margin: 0 auto`. Widen the browser window and observe whether the box stays centered.
2. Remove the `width` property from that container and observe what happens to its position.
3. Wrap the entire contents of the Profile Page in a `<div class="kontainer">` with `max-width: 800px` and `margin: 0 auto`.
