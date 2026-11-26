---
id: web-foundation-css-2
courseId: fe-web-foundation-00
order: 51
title: "Writing CSS Rules"
readingMinutes: 5
section: CSS_DASAR
codeSnippet: |
  h1 {
    color: green;
  }

  p {
    font-size: small;
    font-family: sans-serif;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan CSS Rule</title>
    </head>
    <body>
      <h1>Judul Halaman</h1>
      <p>Ini adalah paragraf pertama.</p>
    </body>
  </html>
learningObjectives:
  - "Understand the basic structure of a CSS rule: selector and declaration block"
  - "Write selectors and declarations with proper syntax"
---
A single styling instruction in CSS is called a **rule**. Every rule has two parts: a **selector** (which HTML element you want to style) and a **declaration block** (what styling to apply).

Look at the example code. The first rule says "all `<h1>` elements have green text". The second rule says "all `<p>` elements have a small font size and use a sans-serif font".

A few important rules about the syntax:
- The selector comes first, followed by curly braces `{ }`.
- Inside the curly braces, each declaration follows the form `property: value;` — do not forget the semicolon at the end of every line.
- You can write more than one declaration inside a single rule, just like the `<p>` example above which sets two properties at once.
- CSS does not care about spaces or line breaks (similar to other languages), though putting one declaration per line makes it much easier to read.

One trap that often confuses beginners: values with units (like `px` or `em`) must not have a space between the number and the unit. `margin: 2em;` is valid, while `margin: 2 em;` will not work at all — the browser treats the declaration as invalid and ignores it completely.

You can also write comments in CSS using `/* ... */`, just like in HTML but with a different syntax. Anything written between `/*` and `*/` is ignored by the browser — handy for leaving notes in your own code.
