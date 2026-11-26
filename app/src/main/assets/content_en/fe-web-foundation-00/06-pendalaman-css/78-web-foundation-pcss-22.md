---
id: web-foundation-pcss-22
courseId: fe-web-foundation-00
order: 78
title: "Box Model: Border"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  .box {
    border-top-width: 2px;
    border-right-width: 1px;
    border-bottom-width: 1px;
    border-left-width: 2px;
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
  - "Set border thickness with border-width, including the 1–4 value rule"
  - "Choose border line styles with border-style"
  - "Set border colors with border-color"
  - "Write the border shorthand and understand that the value order is flexible"
---
A border is a line surrounding an element's content and padding area. Borders are optional, yet once applied, they can dramatically change how a box looks—turning a plain card into one with crisp edges, or turning an ordinary text input into a clearly defined field ready for typing.

There are three things we can configure on a border: **thickness**, **line style**, and **color**. Let's look at each one.

## Border Width

The `border-width` property controls the thickness of the border line. Its value can be in pixels, or one of the built-in keywords `thin`, `medium`, and `thick`. Keep in mind that this property **cannot** use percentage values.

You can set each side's thickness individually using these four properties:

```css
.box {
  border-top-width: 2px;
  border-right-width: 1px;
  border-bottom-width: 1px;
  border-left-width: 2px;
}
```

Or write them all at once on a single line:

```css
.box {
  border-width: 2px 1px 1px 2px; /* top right bottom left */
}
```

`border-width` accepts one to four values, and the number of values determines how they apply:

| Number of values | How it applies |
|---|---|
| 1 value | Applies to all four sides |
| 2 values | First value for top & bottom, second value for left & right |
| 3 values | First value for top, second for left & right, third for bottom |
| 4 values | First top, second right, third bottom, fourth left (clockwise) |

This 1–4 value pattern doesn't only apply to borders. We will see the exact same pattern for `padding` and `margin` in upcoming lessons.

## Border Style

The `border-style` property specifies the kind of line displayed. Common values include:

| Value | Appearance |
|---|---|
| `solid` | A continuous, unbroken line |
| `dotted` | A line made of dots |
| `dashed` | A line made of short dashes |
| `double` | Two parallel solid lines |
| `groove` | Appears like a carved or raised 3D frame |
| `hidden` | Hides the border (still takes up space, though invisible) |

```css
.card {
  border-style: solid;
}

.dashed-card {
  border-style: dashed;
}
```

Just like `border-width`, you can style each side individually:

```css
.box {
  border-top-style: solid;
  border-right-style: dashed;
  border-bottom-style: solid;
  border-left-style: dashed;
}
```

> **Note:** Without `border-style`, the border will not appear at all, even if `border-width` and `border-color` are already set. `border-style` is required for a border to be visible.

## Border Color

The `border-color` property sets the color of the line, accepting the same color formats as other color properties: RGB, HEX, or named colors.

```css
/* using RGB format */
border-color: rgb(80, 138, 212);

/* using HEX format */
border-color: #4ee717;

/* using a named color */
border-color: red;
```

Like the previous two properties, each side's color can also be set separately, or written together in top-right-bottom-left order:

```css
border-color: #919191 #111111 #4ee717 #00c8eb;
```

## Border Shorthand

Writing `border-width`, `border-style`, and `border-color` separately can get verbose. CSS provides the `border` shorthand to combine all three in one line:

```css
.card {
  border: 2px solid #00a2c6;
}
```

Interestingly, **the order of values is flexible**. All three of these produce the exact same result:

```css
div { border: 2px solid #00a2c6; }   /* width style color */
div { border: #00a2c6 2px solid; }   /* color width style */
div { border: solid #00a2c6 2px; }   /* style color width */
```

Browsers are smart enough to recognize each value by its type (a number with units for width, a keyword for style, a color value for color), so you don't have to memorize a strict order. Even so, most developers stick to `width style color` out of habit because it is easier to read.

## Quick exercise

1. Create a `<div>` with `border-width: 1px 2px 3px 4px`, and observe which side is the thickest.
2. Create three boxes with different `border-style` values: `solid`, `dashed`, and `double`.
3. Rewrite one of the borders above using the `border` shorthand, in whatever value order you prefer.
