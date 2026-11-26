---
id: css-layout-lanjutan-4
courseId: fe-css-02
order: 23
title: "Placing Items by Line Numbers (grid-column & grid-row)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Understand the Grid Lines numbering system (start line and end line)"
  - "Use grid-column and grid-row to span elements across multiple tracks"
  - "Create a featured card layout that spans across multiple columns"
codeLanguage: css
codeSnippet: |
  .kartu-unggulan {
    grid-column: 1 / 3; /* Membentang dari garis 1 sampai garis 3 (2 kolom) */
    grid-row: 1 / 2;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji grid-column & grid-row</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Grid Spanning: Kartu Berita</h2>
      <div class="wadah-berita">
        <div class="kartu unggulan">Berita Utama (Membentang Lebar)</div>
        <div class="kartu">Berita 2</div>
        <div class="kartu">Berita 3</div>
        <div class="kartu">Berita 4</div>
        <div class="kartu">Berita 5</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah-berita {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    background-color: #1a6368;
    color: white;
    padding: 24px;
    border-radius: 6px;
    text-align: center;
    font-weight: bold;
  }

  /* Targetkan kartu unggulan untuk membentang: */
  .unggulan {
    background-color: #df7b56;
    grid-column: 1 / 3; /* Dari garis kolom 1 ke garis 3 */
  }
playgroundTask: "Try changing '.unggulan' to 'grid-column: 1 / -1;'. The '-1' value means the very last line on the right! Run the code and watch how the featured news card now spans across all three columns (lines 1 to 4)!"
---
By default, each child element inside a Grid occupies only **a single cell** and flows automatically into the next available slot one by one.

However, what if you want one highlighted element—such as a promo banner or a featured news card—to span across 2 columns or 2 rows?

The key is directing the element using **Grid Line numbers**!

```text
grid-column: 1 / 3; (spans from column line 1 to 3)
+---------------------------+-------------+
|       Featured (2 col)    |   Item 2    |
+-------------+-------------+-------------+
|   Item 3    |   Item 4    |   Item 5    |
+-------------+-------------+-------------+
```
## The Grid Line Numbering System

If you create 3 columns:
```text
Line 1         Line 2         Line 3         Line 4
   |  Column 1    |  Column 2    |  Column 3    |
```
Notice that **there are 4 column lines for 3 columns**!

To make an element stretch from Line 1 to Line 3, use the slash (`/`) syntax:

```css
.unggulan {
  /* grid-column: [start_line] / [end_line]; */
  grid-column: 1 / 3; /* Covers Column 1 and Column 2 */
}
```

## The Magic Keyword: `span`

Instead of calculating the ending line number, you can also use the **`span`** keyword:

```css
/* Meaning: start from your natural position, then span 2 columns wide */
.unggulan {
  grid-column: span 2;
}
```

If you want an element to stretch 2 rows tall vertically:

```css
.poster-tinggi {
  grid-row: span 2;
}
```

## The Negative Line Trick (`-1`)

Want an element to stretch all the way from the leftmost edge to the rightmost edge without needing to know how many columns exist? Use `-1`!

```css
.header-penuh {
  grid-column: 1 / -1; /* From the very first line (1) to the very last line (-1) */
}
```

This technique is your main weapon for building dynamic news layouts and magazine-style photo galleries.
