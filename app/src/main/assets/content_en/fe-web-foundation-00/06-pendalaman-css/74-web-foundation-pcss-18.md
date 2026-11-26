---
id: web-foundation-pcss-18
courseId: fe-web-foundation-00
order: 74
title: "Opacity"
readingMinutes: 4
section: PENDALAMAN_CSS
codeSnippet: |
  img {
    opacity: 50%;
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
  - "Understand the difference between transparency and opacity"
  - "Use the alpha channel so only the color becomes transparent"
---
## Opacity

Imagine a sheet of colored transparent plastic. One sheet makes objects behind it slightly see-through. Stacking more sheets lets less light pass through, making the objects behind them harder to see. CSS uses this exact concept through the `opacity` property.

We can sum up the difference like this: **transparency** is how see-through an object is, whereas **opacity** is how visible (solid) an object is. The two are opposites.

| Value | Result |
|---|---|
| `0%` | The element is completely invisible |
| Between 0% and 100% | The element is semi-transparent, yet still visible |
| `100%` | The element is completely solid/visible |

```css
img {
  opacity: 50%;
}
```

### Opacity for color only

Applying `opacity` to an element affects the **entire element**, including its contents and child elements. Sometimes you only want the color itself to be transparent while keeping the text inside completely solid. For that, use an **alpha channel**, which is a fourth component after red, green, and blue. Formats that support it include `rgba()`, `hsla()`, and HEX with two extra digits.

```css
.overlay {
  background-color: rgba(0, 0, 0, 0.5); /* black at 50% transparency */
}
```

This way, the `.overlay` background becomes half transparent, while any text on top of it stays fully crisp and solid.
