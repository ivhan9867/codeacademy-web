---
id: css-layout-lanjutan-2
courseId: fe-css-02
order: 21
title: "Dividing Tracks: grid-template-columns & grid-template-rows"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Master the syntax of grid-template-columns and grid-template-rows"
  - "Define column and row track sizes using pixels (px) and percentages (%)"
  - "Apply gap to cleanly separate rows and columns"
codeLanguage: css
codeSnippet: |
  .tata-letak {
    display: grid;
    grid-template-columns: 200px 1fr; /* Kolom 1 tetap 200px, kolom 2 sisa layar */
    grid-template-rows: 60px auto 50px;
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji grid-template</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Membagi Kolom dan Baris</h2>
      <div class="wadah-grid">
        <div class="kotak">Kolom 1</div>
        <div class="kotak">Kolom 2</div>
        <div class="kotak">Kolom 3</div>
        <div class="kotak">Kolom 4</div>
        <div class="kotak">Kolom 5</div>
        <div class="kotak">Kolom 6</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah-grid {
    display: grid;
    /* Tentukan jumlah kolom di sini: */
    grid-template-columns: 120px 180px 100px;
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kotak {
    background-color: #df7b56;
    color: white;
    font-weight: bold;
    padding: 20px 10px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Try changing 'grid-template-columns' to '33.3% 33.3% 33.3%;'. Run the code and observe how the 3 columns divide the container width proportionally! Then try adding 'grid-template-rows: 100px 50px;'."
---
After turning on `display: grid`, the next step is telling the browser:
> *"How many columns and rows do you want, and how wide or tall should each track be?"*

This question is answered by two fundamental properties: **`grid-template-columns`** and **`grid-template-rows`**.

```text
grid-template-columns: 200px 1fr 200px;
+------------+------------------------+------------+
|   200px    |  1fr (Remaining Space) |   200px    |
+------------+------------------------+------------+
```
## Defining Columns: `grid-template-columns`

Each space-separated value you write represents **one column track**:

```css
/* Creates 3 columns: 1st is 100px, 2nd is 200px, 3rd is 150px */
.wadah {
  display: grid;
  grid-template-columns: 100px 200px 150px;
}
```

If there are 6 child elements inside the container:
- Elements 1, 2, and 3 occupy the first row (columns 1, 2, 3).
- Elements 4, 5, and 6 automatically wrap down to occupy the second row (columns 1, 2, 3).

## Defining Rows: `grid-template-rows`

Just like columns, you can lock the height of each row:

```css
/* Row 1 is 80px tall, row 2 is 160px tall */
.wadah {
  display: grid;
  grid-template-rows: 80px 160px;
}
```

If the content exceeds the number of rows you defined manually, the browser automatically creates **implicit rows** whose height adjusts to fit their content.

## Adding Spacing: `gap`

Just like in Flexbox, you can easily add spacing between grid cells:

```css
.wadah {
  gap: 16px;          /* 16px space between both rows and columns */
  row-gap: 20px;      /* Space between rows only */
  column-gap: 10px;   /* Space between columns only */
}
```

However, using pixels (`px`) or percentages (`%`) can get awkward when accounting for `gap`. In the next lesson, we'll meet the true hero unit of CSS Grid: **the `fr` unit**!
