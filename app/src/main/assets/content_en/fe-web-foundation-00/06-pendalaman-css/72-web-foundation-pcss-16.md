---
id: web-foundation-pcss-16
courseId: fe-web-foundation-00
order: 72
title: "Setting Color Values"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  /* Format RGB */
  color: rgb(78, 231, 23);

  /* Format HEX */
  color: #4ee717;

  /* Format HSL */
  color: hsl(104, 82%, 50%);
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
  - "Write colors using RGB, HEX, and HSL"
  - "Use predefined color names appropriately"
---
### How colors work on screen

Every color on a screen is created by mixing three lights: **red, green, and blue** (RGB). A display is made up of thousands of pixels, and each pixel contains three tiny sub-lights. By adjusting the brightness of each light, a single pixel can show thousands of different colors.

You do not need to memorize numeric combinations for every color. Use a **color picker** in design apps like Figma, GIMP, or Photoshop, or search for "color picker" in a search engine. From there, you can copy the color value straight into your CSS.

### Numeric values

The most widely used approach is a numeric value, because it gives you exact control. This example writes the same shade of green in three formats:

```css
/* RGB format */
color: rgb(78, 231, 23);

/* HEX format */
color: #4ee717;

/* HSL format */
color: hsl(104, 82%, 50%);
```

- **RGB** specifies the strength of red, green, and blue with numbers from 0 to 255.
- **HEX** is a compact representation of RGB using six hexadecimal digits preceded by a hash symbol.
- **HSL** describes a color using *hue* (the color angle from 0 to 360 degrees), *saturation* (intensity), and *lightness* (brightness). HSL is often easier to reason about when creating lighter or darker variations of a color.

### Predefined color names

Besides numbers, CSS also accepts color names like `black`, `white`, or `blue`:

```css
color: black;
color: white;
color: blue;
```

Modern browsers support around 140 named colors. You can view the full list in the HTML color names reference on w3schools.com. Named colors are handy for quick prototypes, but for serious designs it is usually more precise to use numeric values.
