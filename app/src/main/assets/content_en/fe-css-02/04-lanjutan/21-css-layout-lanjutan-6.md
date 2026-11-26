---
id: css-layout-lanjutan-6
courseId: fe-css-02
order: 25
title: "Magic Grid Without Media Queries: minmax() & auto-fit"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Understand how minmax(min, max) works for flexible column sizing"
  - "Distinguish between auto-fill and auto-fit when filling available column slots"
  - "Master the classic one-line CSS spell for automatically responsive card galleries"
codeLanguage: css
codeSnippet: |
  .galeri-ajaib {
    display: grid;
    /* Kolom responsif otomatis tanpa media query: */
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji auto-fit & minmax</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Galeri Kartu Responsif Otomatis</h2>
      <div class="galeri">
        <div class="kartu">Kursus HTML</div>
        <div class="kartu">Kursus CSS</div>
        <div class="kartu">Kursus Flexbox</div>
        <div class="kartu">Kursus CSS Grid</div>
        <div class="kartu">Kursus JavaScript</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .galeri {
    display: grid;
    /* Mantra Ajaib CSS Grid: */
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    background-color: #1a6368;
    color: white;
    font-weight: bold;
    padding: 30px 16px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Try changing the minimum width in minmax from 'minmax(180px, 1fr)' to 'minmax(120px, 1fr)'. Click 'Run' and notice how the browser immediately fits more card columns because the minimum width requirement per card is smaller!"
---
Before CSS Grid, if you wanted product cards to display in 4 columns on a large monitor, 3 on a laptop, 2 on a tablet, and 1 on a phone, you had to write dozens of lines of **`@media` query** rules:

```css
/* The long, exhausting old way */
@media (max-width: 1200px) { ... }
@media (max-width: 900px)  { ... }
@media (max-width: 600px)  { ... }
```

Now get ready to witness the most famous one-line CSS spell in modern web development:

```css
grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
```

This single line replaces all those media query rules automatically!

```text
grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
Desktop (900px) : [Card][Card][Card][Card]
Tablet  (520px) : [   Card   ][   Card   ]
Mobile  (360px) : [         Card         ]
```
## Breaking Down the Magic Line

Let's split it into 3 parts:

### 1. `minmax(250px, 1fr)`
The `minmax` function sets a size range:
- **Minimum Size (`250px`)**: A column may never become narrower than 250px. If the screen shrinks below 250px per card, cards wrap down to the next row.
- **Maximum Size (`1fr`)**: If there is extra space left on the screen, columns can stretch evenly to fill the container.

### 2. `auto-fit` (No Hardcoded Number)
Instead of writing a fixed number like `repeat(3, ...)`, the `auto-fit` keyword tells the browser:
> *"Calculate on your own how many columns of at least 250px fit on the screen right now. If 4 fit, create 4 columns. If only 1 fits (like on a phone), make it 1 full-width column!"*

### 3. Why `auto-fit` Beats `auto-fill`
- `auto-fill` keeps empty invisible column slots on the right if there are only a few items.
- `auto-fit` smartly collapses empty tracks and stretches the existing cards so they fill the entire container width without leaving awkward blank space on the right.

With this single line of code, your gallery is guaranteed to look great on every screen size without a single extra `@media` query!
