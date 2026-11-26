---
id: web-foundation-pcss-28
courseId: fe-web-foundation-00
order: 84
title: "Rounded Corner (Border Radius)"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu {
    border-radius: 8px;
  }

  .tombol {
    border-radius: 999px; /* sangat besar, menghasilkan ujung pil */
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
  - "Round the corners of a box with border-radius"
  - "Set the radius for each corner individually"
  - "Create a full circle element using border-radius: 50%"
---
CSS3 introduced the `border-radius` property, which lets us turn box corners into rounded corners instead of their default sharp right angles. This property is widely used on buttons, cards, avatars, and badges to create a softer, more modern look.

The `border-radius` value determines the degree of curvature, and is usually written in pixels or percentages.

```css
.kartu {
  border-radius: 8px;
}

.tombol {
  border-radius: 999px; /* very large, produces pill-shaped ends */
}
```

The larger the value, the rounder the corners. A very large value (larger than half of the box's shortest side) produces a *pill* or capsule shape on rectangular buttons.

## Setting each corner individually

Just like border, padding, and margin, `border-radius` also supports per-corner settings:

```css
.kartu {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
```

Or written all on one line, in the order **top-left, top-right, bottom-right, bottom-left** (clockwise, starting from the top-left corner):

```css
.kartu {
  border-radius: 12px 12px 0 0; /* only the top corners are rounded */
}
```

This pattern is useful, for example, for a card that attaches to another element at the bottom, so only the top corners need to be rounded.

## Making a full circle

A commonly used trick: if a **square** element (equal width and height) is given `border-radius: 50%`, the result is a **perfect circle**. This technique is widely used for circular profile pictures.

```css
.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}
```

The `object-fit: cover` property is usually paired with this trick on `<img>` elements so the image fills the circle proportionally without stretching or squishing.

## Quick exercise

1. Create a card with `border-radius: 8px` on all corners.
2. Create a button with a large `border-radius` (such as `999px`) so its ends form a pill shape.
3. Create an `<img>` sized 80×80px with `border-radius: 50%` to produce a circular avatar.
4. Create a notification card with rounded top corners (`12px`) and sharp bottom corners (`0`).
