---
id: web-foundation-lanjutan-25
courseId: fe-web-foundation-00
order: 48
title: "Special Characters"
readingMinutes: 4
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Display special HTML characters such as <, >, &, and \" using named entities (&lt;, &gt;, &amp;, &quot;)"
  - "Display the copyright symbol (&copy;) and non-ASCII symbols using character references"
codeSnippet: |
  <p>1 &lt; 2 dan 5 &gt; 3</p>
  <p>Harga mulai dari &euro;10</p>
  <p>Profil Rani &copy; 2026. Dibuat dengan penuh semangat belajar&hellip;</p>
---
Have you ever wondered what happens if you type the `<` symbol directly inside article text? The browser will think you are opening a new HTML tag. As a result, the page structure can break.

This module covers how to display those special symbols safely using **Character References**.

## Why Are Special Characters Needed?

Certain characters have special meaning in HTML syntax. Other characters, such as the copyright symbol (©), are not always available on every keyboard key.

To display them, HTML uses a special code format. This format always starts with an ampersand (`&`) and ends with a semicolon (`;`):

- **Named Entity**: written using its keyword name, for example `&copy;`.
- **Numeric Entity**: written using its character code number, for example `&#169;`.

Both produce the exact same symbol on the browser screen.

## 1. HTML Reserved Characters

These characters are used directly by the browser engine to parse HTML code. Therefore, never type them plainly inside content:

- **`&lt;`** : less-than symbol (`<`). Stands for *less than*.
- **`&gt;`** : greater-than symbol (`>`). Stands for *greater than*.
- **`&amp;`** : ampersand symbol (`&`).
- **`&quot;`** : straight double quotation mark (`"`).
- **`&apos;`** : single quotation mark (`'`).

```html
<!-- The browser reads this as plain text, not as tags -->
<p>Math formula: 5 &lt; 10 and 20 &gt; 15.</p>
<p>To create a heading, use the &lt;h1&gt; tag in your HTML file.</p>
```

## 2. Copyright & Trademark Symbols

This group is most often used in website footers to indicate legal status:

- **`&copy;`** : copyright symbol (©).
- **`&reg;`** : registered trademark symbol (®).
- **`&trade;`** : general trademark symbol (™).

```html
<footer>
  <p>&copy; 2026 PT Teknologi Nusantara &trade;. All rights reserved.</p>
</footer>
```

## 3. Typography & Refined Punctuation

This group gives article text a clean look like professionally printed books:

- **`&nbsp;`** : a space that cannot break across lines (*non-breaking space*). Prevents two words from splitting onto a new line.
- **`&mdash;`** : long dash or em-dash (—).
- **`&hellip;`** : ellipsis or three consecutive dots (…).
- **`&ldquo;`** and **`&rdquo;`** : curly opening and closing double quotation marks (“ ”).

```html
<p>She said, &ldquo;Wait a moment&hellip;&rdquo; then walked away &mdash; without hesitation.</p>
<p>Bundle price: Rp&nbsp;50.000 (the number will not separate from the word Rp).</p>
```

## 4. International Currency Symbols

When your physical keyboard lacks foreign currency keys, use these entity codes:

- **`&euro;`** : Euro currency symbol (€).
- **`&pound;`** : Pound sterling currency symbol (£).
- **`&yen;`** : Yen or Yuan currency symbol (¥).

```html
<p>European server rental fee is &euro;25 per month.</p>
```

## Common Mistakes

1. **Forgetting the semicolon (`;`) at the end**: The browser will not recognize the code and will display it as raw text like `&copy`.
2. **Writing `<` directly in text**: The browser may treat it as an unclosed HTML tag.

## Try It Yourself

Open your Profile Page practice file. Add a copyright line at the very bottom of the page, for example: `Profile Page &copy; 2026 Rani`. Check the result in the browser!
