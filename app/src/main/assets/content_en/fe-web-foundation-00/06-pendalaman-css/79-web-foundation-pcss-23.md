---
id: web-foundation-pcss-23
courseId: fe-web-foundation-00
order: 79
title: "Box Model: Padding"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu-sesak {
    background-color: #f0f8ff;
    border: 1px solid #00a2c6;
  }

  .kartu-lega {
    background-color: #f0f8ff;
    border: 1px solid #00a2c6;
    padding: 16px;
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
  - "Explain the role of padding as the space between content and border"
  - "Set padding per side and with a single-line shorthand"
  - "Understand that percentage values for padding are relative to the parent element"
---
Padding is the space between an element's **content** and its **border**. You will notice padding the most when an element uses `background-color` or `border`: without padding, text sits right up against the edge of the color or line, making it feel cramped to read.

Compare the following two cards. The first has no padding, while the second has padding:

```css
.kartu-sesak {
  background-color: #f0f8ff;
  border: 1px solid #00a2c6;
}

.kartu-lega {
  background-color: #f0f8ff;
  border: 1px solid #00a2c6;
  padding: 16px;
}
```

The second card feels much more comfortable to look at because there is breathing room between the text and the edges of its box. The box itself also becomes slightly wider and taller because padding adds space inside the border.

## Padding units

Pixels are the most commonly used unit for padding, although percentages and `em` can also be used. When using a percentage, the value is calculated relative to the **width of the parent element** (or the browser window, if the element has no parent).

## Setting padding per side

Just like borders, padding can be set per side using four properties:

```css
.kotak {
  padding-top: 10px;
  padding-right: 15px;
  padding-bottom: 10px;
  padding-left: 15px;
}
```

Or written all at once with shorthand, using the same 1–4 value pattern as `border-width`:

```css
.kotak {
  padding: 10px 15px 10px 15px; /* top right bottom left */
}
```

Because in this example the top value equals the bottom, and the left value equals the right, we can also shorten it to two values:

```css
.kotak {
  padding: 10px 15px; /* top & bottom, left & right */
}
```

## Quick exercise

1. Create two paragraphs with the same `border` and `background-color`. Add `padding: 16px` only to the second paragraph, then compare how comfortable each is to read.
2. Create a card with 12px top-and-bottom padding and 20px left-and-right padding, written in a single shorthand line.
3. Apply `padding: 5%` to an element inside a fixed-size `<div>`, then change the width of that `<div>`. Observe how the padding changes along with it.
