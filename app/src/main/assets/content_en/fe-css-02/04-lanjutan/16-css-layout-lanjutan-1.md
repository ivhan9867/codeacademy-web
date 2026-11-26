---
id: css-layout-lanjutan-1
courseId: fe-css-02
order: 20
title: "Welcome to the Matrix: Basic Concepts of CSS Grid"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Understand the fundamental difference of two-dimensional layout architecture (rows and columns simultaneously)"
  - "Learn Grid anatomy terminology: Grid Lines, Grid Tracks, Grid Cells, and Grid Areas"
  - "Enable display: grid on a container element"
codeLanguage: css
codeSnippet: |
  .kontainer-grid {
    display: grid;
    grid-template-columns: 1fr 1fr; /* Membelah jadi 2 kolom seimbang */
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Konsep Dasar CSS Grid</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Memasuki Matriks 2 Dimensi</h2>
      <div class="kontainer-grid">
        <div class="kartu">Sel 1</div>
        <div class="kartu">Sel 2</div>
        <div class="kartu">Sel 3</div>
        <div class="kartu">Sel 4</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .kontainer-grid {
    display: grid;
    grid-template-columns: 1fr 1fr; /* Dua kolom sama lebar */
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
    padding: 30px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Try changing 'grid-template-columns: 1fr 1fr;' to 'grid-template-columns: 1fr 1fr 1fr 1fr;'. Press 'Run' and observe how those 4 cards immediately line up into 4 columns in a single horizontal row!"
---
If Flexbox is the king of **one-dimensional** layout (you choose to arrange elements either horizontally OR vertically), then **CSS Grid** is the emperor of **two-dimensional** layout.

With CSS Grid, you can dictate element positions across **rows and columns simultaneously**.

```text
   Line 1       Line 2       Line 3       Line 4
     |            |            |            |
  ---+------------+------------+------------+--- Row Line 1
     |  Cell 1,1  |  Cell 1,2  |  Cell 1,3  |
  ---+------------+------------+------------+--- Row Line 2
     |  Cell 2,1  |  Cell 2,2  |  Cell 2,3  |
  ---+------------+------------+------------+--- Row Line 3
```
## Anatomy of CSS Grid Vocabulary

So that we can speak the same language, let's understand 4 important terms in the Grid world:

1. **Grid Lines**:
   Invisible dividing lines that split rows and columns. These lines are numbered sequentially starting from `1` (not from 0!). If you have 2 columns, then there are 3 column lines: the leftmost line (1), the center dividing line (2), and the rightmost line (3).
2. **Grid Track**:
   The space between two adjacent grid lines. Simply put, this is the technical name for **one full column** or **one full row**.
3. **Grid Cell**:
   The smallest box inside a Grid, which is the intersection between one row and one column. Like a single small square on math graph paper.
4. **Grid Area**:
   A combination of multiple rectangular cells forming one large region (for example: an area spanning 2 rows and 3 columns).

## Enabling Grid: `display: grid`

Just like Flexbox, the first step is turning on the Grid engine on the parent container:

```css
.wadah {
  display: grid;
}
```

When you only write `display: grid`, there is no dramatic visual change yet because by default Grid only forms 1 column.

In the next module, we will learn how to slice columns and rows however we want!