---
id: web-foundation-pcss-17
courseId: fe-web-foundation-00
order: 73
title: "Colors for Text and Backgrounds"
readingMinutes: 4
section: PENDALAMAN_CSS
codeSnippet: |
  p { color: gray; }
  p { color: #666666; }
  p { color: #666; }
  p { color: rgb(102, 102, 102); }
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
  - "Change text color using the color property"
  - "Change background color using background-color and understand its default value"
---
## Colors for text and backgrounds

Now let's apply colors to elements. There are two primary properties.

### Text color: `color`

The `color` property changes the color of text. Its value can be a color name or a numeric value. All four examples below produce the exact same shade of gray:

```css
p { color: gray; }
p { color: #666666; }
p { color: #666; }
p { color: rgb(102, 102, 102); }
```

Like other text properties, `color` is inherited by descendant elements. So you can change the text color for the entire page simply by applying it to `body`:

```css
body {
  color: #333;
}
```

### Background color: `background-color`

CSS treats every element like a box. The `background-color` property sets the background color of that box. Just like `color`, its value can be a color name or a numeric value.

By default, `background-color` is transparent. Many browsers display a white background, though users can change that setting. To keep your page looking consistent, set the background explicitly on `body`:

```css
body {
  background-color: white;
}
```

When giving an element a `background-color`, you usually also add `padding`. Without padding, the text sits flush against the edge of the background box.

```css
.card {
  background-color: #f0f8ff;
  padding: 16px;
}
```

## Complete Example Combining Text and Background Colors

Here is a complete HTML document that pairs text and background color settings harmoniously:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Scholarship Announcement</title>
  <style>
    body {
      font-family: sans-serif;
      background-color: #F8FAFC; /* Very light gray page background */
      color: #1E293B;            /* Dark gray default text */
      margin: 20px;
    }

    .announcement-banner {
      background-color: #1E40AF; /* Deep blue background */
      color: #FFFFFF;            /* High-contrast white text */
      padding: 20px;
      border-radius: 8px;
    }

    .status-badge {
      background-color: #FEF08A; /* Bright light yellow */
      color: #854D0E;            /* Dark golden brown for readability */
      padding: 4px 8px;
      border-radius: 4px;
      font-weight: bold;
    }

    .note {
      background-color: #E2E8F0; /* Neutral gray */
      color: #475569;
      padding: 12px;
      border-radius: 6px;
    }
  </style>
</head>
<body>
  <div class="announcement-banner">
    <span class="status-badge">Registration Open</span>
    <h2>Indonesia Coding Scholarship Program 2026</h2>
    <p>Get intensive front-end web development mentorship from experienced industry mentors.</p>
    <div class="note">
      Application submission deadline: November 30, 2026.
    </div>
  </div>
</body>
</html>
```

## How It Renders on Screen

When the page above is rendered by the browser:
1. **Clean Page Base**: `<body>` has a cool gray background (`#F8FAFC`) with dark gray base text (`#1E293B`). This keeps text contrast crisp without glaring brightness.
2. **High-Contrast Banner**: The `.announcement-banner` box stands out with a deep blue background (`#1E40AF`) and white text (`#FFFFFF`), producing a very high contrast ratio that immediately draws the visitor's eye.
3. **Eye-Catching Status Badge**: The `.status-badge` element pairs a light yellow background (`#FEF08A`) with dark brown text (`#854D0E`). This avoids a common beginner mistake of putting white text on a bright yellow background (which makes text hard to read).
4. **Comfortable Breathing Room**: Thanks to `padding` on every element with a `background-color`, the text never touches the edges of its background box, making it comfortable and pleasant to read.
