---
id: css-layout-inti-5
courseId: fe-css-02
order: 14
title: "Neat Multi-Line Wrapping: flex-wrap & gap"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Understand Flexbox's default behavior of forcing all items onto a single line (nowrap)"
  - "Allow elements to wrap onto the next line using flex-wrap: wrap"
  - "Use the modern gap property to space items cleanly without margin hacks"
codeLanguage: css
codeSnippet: |
  .daftar-tag {
    display: flex;
    flex-wrap: wrap; /* Izinkan pindah baris jika layar sempit */
    gap: 12px;       /* Jarak horizontal dan vertikal sekaligus */
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji flex-wrap & gap</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Kategori Topik Belajar</h2>
      <div class="wadah-kategori">
        <span class="chip">HTML5</span>
        <span class="chip">CSS3</span>
        <span class="chip">Flexbox</span>
        <span class="chip">CSS Grid</span>
        <span class="chip">Responsive Web</span>
        <span class="chip">JavaScript</span>
        <span class="chip">DOM API</span>
        <span class="chip">Web Design</span>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah-kategori {
    display: flex;
    flex-wrap: nowrap; /* Nilai default: memaksakan sebaris */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
    width: 320px; /* Lebar dibatasi agar terjadi overflow */
  }

  .chip {
    background-color: #1a6368;
    color: white;
    padding: 8px 14px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: 500;
    white-space: nowrap;
  }
playgroundTask: "Observe the initial view: the topic chips are clipped or spilling outside the border because of 'flex-wrap: nowrap'. Change its value to 'flex-wrap: wrap;', then add 'gap: 8px;' inside '.wadah-kategori'. Run the code and watch the chips automatically wrap onto new lines with very neat spacing!"
---
By default, Flexbox has a very stubborn personality: **all items are forced to line up in a single line (*single-line flex container*)**.

Even if the screen width shrinks (for instance on a smartphone), flex items will keep shrinking and squeezing against each other to preserve that single line until they eventually *overflow* outside the screen in a mess.

To solve this, we need two saviors: **`flex-wrap`** and **`gap`**.

```text
flex-wrap: wrap; gap: 12px;
+-----------------------------+
| [Card 1]  [Card 2] [Card 3] |
|                             | <-- gap: 12px
| [Card 4]  [Card 5]          |
+-----------------------------+
```
## Allowing New Lines with `flex-wrap`

The `flex-wrap` property is placed on the parent container to determine whether items are allowed to drop to the next line when space runs out:

1. **`nowrap` (Default)**:
   All items are forced onto one line. If they don't fit, items will shrink or burst outside the container.
2. **`wrap`**:
   Items that no longer fit on the first line will gracefully drop down and form a new line below it. Essential for photo galleries, rows of tags/categories, and card lists.
3. **`wrap-reverse`**:
   Works just like wrap, except new lines appear **above** the previous line.

## Setting Elegant Spacing with `gap`

In the past, to add spacing between items in Flexbox, we had to put `margin-right: 10px; margin-bottom: 10px;` on items, then add a negative margin trick on the container so the rightmost item wouldn't jut out.

Now, modern CSS brings the **`gap`** property directly on the parent container:

```css
.wadah {
  display: flex;
  flex-wrap: wrap;
  gap: 16px; /* 16px spacing sideways and downward */
}
```

You can even distinguish row and column spacing:
- `row-gap: 20px;` (vertical spacing between rows)
- `column-gap: 12px;` (horizontal spacing between columns)
- Shorthand: `gap: 20px 12px;` (vertical horizontal)

The `gap` property only adds space **between** two items, and **never adds extra margin** against the outer walls of the container. Clean, precise, and modern!