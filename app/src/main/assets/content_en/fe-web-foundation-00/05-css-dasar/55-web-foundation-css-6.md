---
id: web-foundation-css-6
courseId: fe-web-foundation-00
order: 55
title: "Basic Styling: Text Color"
readingMinutes: 4
section: CSS_DASAR
codeSnippet: |
  p {
    color: red;
  }

  h1 {
    color: #1E90FF;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan Warna Teks</title>
    </head>
    <body>
      <h1>Judul Berwarna Biru</h1>
      <p>Paragraf ini akan berwarna merah.</p>
    </body>
  </html>
learningObjectives:
  - "Apply the color property to change text color"
  - "Recognize two ways to write color values: named colors and hex codes"
---
You already saw the `color` property used in the previous exercise—now let's examine how it works in more detail.

## Two Basic Ways to Write Color Values

At this introductory stage, there are two common ways most often used to specify color values:

1. **Named Colors** — CSS provides built-in color names that browsers recognize directly, such as `red`, `green`, `blue`, `crimson`, or `navy`. This is super handy for testing looks quickly without hunting for numeric codes.
2. **Hexadecimal Codes (*Hex Code*)** — written with a hash sign (`#`) followed by six digits or letters, such as `#1E90FF` (bright sky blue) or `#333333` (dark gray). This method is far more flexible because it gives you access to millions of precise color shades.

## Complete Code Example

Here is a complete HTML document combining both ways of writing color values:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Exploring Text Colors</title>
  <style>
    h1 {
      color: #1E90FF; /* Bright blue using hex code */
    }

    h2 {
      color: darkslateblush; /* Standard color name */
      color: darkslateblue;
    }

    p.intro {
      color: crimson; /* Vivid red using named color */
    }

    p.description {
      color: #4B5563; /* Neutral gray using hex code */
    }
  </style>
</head>
<body>
  <h1>Welcome to Senja Coffee Shop</h1>
  <h2>Finest Blends from Local Farmers</h2>
  <p class="intro">Notice: Open daily starting at 08:00 AM.</p>
  <p class="description">Enjoy freshly brewed coffee beans in a warm and cozy atmosphere with your friends.</p>
</body>
</html>
```

## Explaining the Result on Screen

When the code above is opened in a browser:
- The main heading `<h1>` is rendered bold in bright blue (`#1E90FF`), drawing visitors' attention as the primary focal point.
- The subheading `<h2>` appears in dark slate blue (`darkslateblue`), reinforcing its position beneath the main heading.
- The first paragraph with class `.intro` appears in vivid red (`crimson`), signaling that this sentence is an important announcement.
- The second paragraph with class `.description` appears in soft gray (`#4B5563`), making long paragraphs comfortable to read without straining the eyes compared to pure black.

In the Playground, try changing the `color` values on the elements above to see the updates live. Broader color settings—such as background colors, transparency, and modern color palettes—will be covered thoroughly in the CSS Deep Dive section.
