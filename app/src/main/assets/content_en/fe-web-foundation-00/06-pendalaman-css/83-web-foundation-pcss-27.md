---
id: web-foundation-pcss-27
courseId: fe-web-foundation-00
order: 83
title: "Box Shadow"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu {
    box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.3);
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
  - "Add shadows to element boxes using box-shadow"
  - "Configure horizontal offset, vertical offset, blur, spread, and color on box-shadow"
---
Earlier, in the text formatting lesson, you learned how to add shadows to text using `text-shadow`. Now it is the **element box's** turn to get a shadow, using the `box-shadow` property.

`box-shadow` works similarly to `text-shadow`: you specify the horizontal offset, vertical offset, blur radius, and color. The difference is that `box-shadow` has one extra value: **spread** (how much the shadow expands).

```css
.kartu {
  box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.3);
}
```

Here is the order and meaning of each value:

1. **Horizontal offset**: how far the shadow shifts to the left (negative value) or right (positive value).
2. **Vertical offset**: how far the shadow shifts up (negative value) or down (positive value).
3. **Blur** (optional): how blurry the shadow is. The larger the value, the softer and more diffused the shadow becomes.
4. **Spread** (optional): how far the shadow expands. The larger the value, the wider the shadow grows in all directions instead of just following the box shape.
5. **Color**: the color of the shadow, usually written with `rgba()` so you can adjust transparency.

```css
.kartu-lembut {
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.15);
}

.kartu-tegas {
  box-shadow: 2px 2px 0 0 #333;
}
```

The difference between those two examples is quite noticeable: the first uses a large blur and transparent color, creating a soft, diffused shadow like a "floating" effect. The second has no blur and no transparency, making the shadow look crisp like an extra outline.

Keep in mind that `box-shadow` follows the shape of the element box **outside the border**, and does not affect the margin area.

## Quick exercise

1. Create a card with `box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2)`, then observe the floating effect.
2. Change the blur value to `0`, then compare the result with the previous step.
3. Add a positive spread value (such as `4px`) to one of the shadows, then observe how the shadow expands.
