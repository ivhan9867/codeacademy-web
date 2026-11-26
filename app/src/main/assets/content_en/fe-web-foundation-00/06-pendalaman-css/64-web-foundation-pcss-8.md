---
id: web-foundation-pcss-8
courseId: fe-web-foundation-00
order: 64
title: "Font Styling: Font Family and Font Size"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  body {
    font-family: sans-serif;
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
  - "Configure font-family with proper fallbacks, including a generic font family at the end"
  - "Choose the appropriate font-size units (px, em, rem, %, vw, vh)"
  - "Understand when @font-face is necessary"
---
## Font: Type, Size, and Weight

### font-family

`font-family` determines the typeface used for text. It is usually defined on the `body` element so that it applies to the entire page.

```css
body {
  font-family: sans-serif;
}
```

The `font-family` value can contain **multiple fonts**, known as a *font stack*. The browser attempts to use the first font. If it is not available on the visitor's device, the browser moves to the next font, and so on.

```css
body {
  font-family: "Open Sans", Arial, sans-serif;
}
```

There are a few rules for writing this:

- Separate each value with a **comma**.
- Font names that contain **spaces** must be enclosed in quotes, for example, `"Open Sans"`.
- End the list with a **generic font family** (`serif`, `sans-serif`, `monospace`, etc.). These values are guaranteed to be supported by all browsers, serving as a final fallback.

Commonly used generic font families:

| Value | Characteristics |
|---|---|
| `serif` | Characters have small strokes (feet) at the ends, e.g., Times New Roman |
| `sans-serif` | No strokes, clean appearance, e.g., Arial or Open Sans |
| `monospace` | Every character has the same width, e.g., Consolas, often used for code |
| `cursive` | Resembles handwriting |
| `system-ui` | Uses the default font of the visitor's operating system |

### @font-face

If you want to use a font that is not installed on the visitor's computer, CSS provides `@font-face` to register your own font files:

```css
@font-face {
  font-family: "My Font";
  src: url("FONT-FILE.ttf");
}

p {
  font-family: "My Font", sans-serif;
}
```

Once registered, the name `"My Font"` can be used like any standard font. We won't dive deeper into `@font-face` in this module. In the Google Fonts section later, you will see that this technique is handled automatically.

### font-size

`font-size` controls the size of the text. The value and its unit must be **attached without a space**.

```css
h1 {
  font-size: 1.5em;  /* correct */
}

/* font-size: 1.5 em;  -> incorrect, this rule will be ignored by the browser */
```

Units are divided into two groups:

**Relative units** depend on something else:

- `em`: relative to the font size of the element itself. `2em` means twice the current font size.
- `rem`: relative to the font size of the root element (`<html>`). Easier to predict because it doesn't compound like `em`.
- `%`: a percentage of the parent's font size. `150%` means 1.5 times the parent's size.
- `vw` and `vh`: 1% of the viewport width and height, respectively. Useful for text that scales with the screen size.

**Absolute units** have fixed values:

- `px`: pixels. The most commonly used and easiest to understand.
- `pt`, `pc`, `mm`, `cm`, `in`: more commonly used for print documents.

```css
body {
  font-size: 16px;
}

h1 {
  font-size: 150%; /* 150% of 16px = 24px */
}
```

In the example above, the `<h1>` inherits `16px` from the `body`, then multiplies it by 150%. The result is 24px, or 50% larger than the standard text.

You can also use keywords: `xx-small`, `x-small`, `small`, `medium`, `large`, `x-large`, and `xx-large`. Browsers typically start at `medium` and scale the others consistently.

> **Accessibility note:** For main text, it is better to use `rem` or `em` rather than `px`. This ensures that the text scales up when visitors change the font size in their browser settings.
