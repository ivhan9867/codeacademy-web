---
id: web-foundation-pcss-11
courseId: fe-web-foundation-00
order: 67
title: "Text Styling: Text Decoration, Text Transform, Spacing, and Text Shadow"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  a {
    text-decoration: none;
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
  - "Remove or customize text decoration using text-decoration"
  - "Change text capitalization with text-transform without modifying HTML"
  - "Adjust letter and word spacing as well as text shadows"
---
## Text decoration and transformation

### text-decoration

`text-decoration` adds lines to text:

- `underline`: adds a line below the text.
- `overline`: adds a line above the text.
- `line-through`: draws a line through the middle of the text (strikethrough).
- `none`: removes any line decoration.

The most common use case is removing the default underline on links:

```css
a {
  text-decoration: none;
}
```

Be careful when removing underlines from links. Visitors still need to know which parts are clickable, so provide another visual cue such as a distinct color or hover effect.

### text-transform

`text-transform` changes capitalization **without modifying the HTML content**:

- `none`: displays the text as written.
- `capitalize`: capitalizes the first letter of each word.
- `lowercase`: converts all letters to lowercase.
- `uppercase`: converts all letters to uppercase.

```css
h2 {
  text-transform: uppercase;
}
```

Its main advantage is that the original text stays written normally in HTML. This is important for search indexing and screen readers.

### letter-spacing and word-spacing

- `letter-spacing` adjusts the space between individual letters.
- `word-spacing` adjusts the space between words.

```css
h1 {
  letter-spacing: 2px;
}

p {
  word-spacing: 4px;
}
```

Both are very helpful for large headings. Headings with slightly wider `letter-spacing` often look cleaner and more elegant.

### text-shadow

`text-shadow` adds a shadow behind text. Its value contains four parts in the following order:

1. Horizontal offset (left or right)
2. Vertical offset (up or down)
3. Blur radius (optional)
4. Shadow color

```css
h1 {
  text-shadow: 2px 2px 4px #888;
}
```

Shadows that are too sharp or too dark actually make text harder to read. Use them sparingly.
