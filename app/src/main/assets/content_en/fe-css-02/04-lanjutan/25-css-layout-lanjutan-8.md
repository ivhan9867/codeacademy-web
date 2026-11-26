---
id: css-layout-lanjutan-8
courseId: fe-css-02
order: 27
title: "Alignment Matrix: place-items, place-content, & justify-self"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - "Understand the 6-property CSS Grid alignment matrix for positioning cell items and the entire grid"
  - "Use place-items, place-content, and place-self shorthands for cleaner CSS"
  - "Distinguish between aligning items inside cells (items/self) versus aligning the entire grid inside its container (content)"
codeLanguage: css
codeSnippet: |
  .grid-matriks {
    display: grid;
    grid-template-columns: repeat(3, 90px);
    grid-template-rows: repeat(2, 70px);
    gap: 8px;
    place-items: center;
    place-content: center;
  }

  .item-khusus {
    justify-self: end;
    align-self: start;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: Alignment Matriks CSS Grid</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>Matriks Alignment CSS Grid</h3>
      <div class="grid-board">
        <div class="cell">1</div>
        <div class="cell">2</div>
        <div class="cell spesial">3 (Pojok)</div>
        <div class="cell">4</div>
        <div class="cell">5</div>
        <div class="cell">6</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Atur Grid Container dengan ukuran track tetap agar terlihat ruang ekstra */
  .grid-board {
    /* Tambahkan display: grid; dan place-items: center; di sini */
    grid-template-columns: repeat(3, 96px);
    grid-template-rows: repeat(2, 80px);
    gap: 10px;
    min-height: 240px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    border-radius: 10px;
    padding: 12px;
    place-content: center;
  }

  .cell {
    background-color: #104246;
    color: white;
    padding: 8px 14px;
    border-radius: 6px;
    font-weight: bold;
    font-size: 13px;
  }

  /* 2. Pengecualian untuk satu item saja */
  .spesial {
    background-color: #df7b56;
    justify-self: end;
    align-self: start;
  }
playgroundTask: "Add 'display: grid;' and 'place-items: center;' to '.grid-board'. Watch how all numbered boxes automatically center inside their respective cells, while box 3 sticks to the top-right corner of its cell thanks to 'justify-self: end' and 'align-self: start'!"
verificationRules:
  - "css:display: grid"
  - "css:place-items"
---
In CSS Grid, the alignment system is even more complete than Flexbox because we have **two active axes at the same time** (horizontal and vertical). There are **6 core alignment properties** plus 3 *shorthand* properties that form a neat matrix.

## Key Concept: Cell Contents (*Items*) vs. Whole Grid (*Content*)

Before memorizing the names, understand the two area levels in CSS Grid:

```text
  LEVEL 1: *-items / *-self (Inside CELL)        LEVEL 2: *-content (Whole GRID in Container)
+-----------------------------------------+    +-----------------------------------------+
| [  (item)  ] | [  (item)  ]             |    |                                         |
| -------------+-------------             |    |         +-------+-------+               |
| [  (item)  ] | [  (item)  ]             |    |         | Cell1 | Cell2 |  (group of    |
|                                         |    |         +-------+-------+   cells       |
| (aligns box position inside its cell)   |    |         | Cell3 | Cell4 |   centered in |
+-----------------------------------------+    |         +-------+-------+   container)  |
                                               +-----------------------------------------+
```

## The 6-Property CSS Grid Alignment Matrix

The naming rule is completely consistent: **`justify-*`** is always for the horizontal (row) axis, and **`align-*`** is always for the vertical (column) axis.

| Alignment Target | Horizontal Axis (`justify-*`) | Vertical Axis (`align-*`) | Shorthand (`place-*`) |
| :--- | :--- | :--- | :--- |
| **All Items Inside Cells** *(on Container)* | `justify-items` | `align-items` | `place-items` |
| **One Specific Item in Its Cell** *(on Item)* | `justify-self` | `align-self` | `place-self` |
| **All Grid Tracks in Container** *(on Container)* | `justify-content` | `align-content` | `place-content` |

The most common values are `start`, `end`, `center`, and `stretch` (the default for `*-items`, making items fill their cell). For `*-content`, you also get `space-between`, `space-around`, and `space-evenly`.

## The Magic of `place-items` & `place-content` Shorthands

The `place-*` syntax accepts one or two values in the order: **`<align> <justify>`** (vertical first, then horizontal). If you write just one value, both axes get the same value.

```css
/* Shortest centering trick in CSS history (2 lines!) */
.kotak-tengah {
  display: grid;
  place-items: center; /* Equivalent to: align-items: center; justify-items: center; */
}
```

Meanwhile, if your Grid has fixed column/row sizes (for example `grid-template-columns: repeat(3, 100px)`) inside a wider container, you can center the entire grid table inside the container with `place-content`:

```css
.papan-grid {
  display: grid;
  grid-template-columns: repeat(3, 100px);
  grid-template-rows: repeat(2, 80px);
  place-content: center; /* Moves the entire grid to the center of the container */
}
```

## Per-Item Exception: `justify-self` & `align-self`

Want every cell centered, except one badge or "Edit" button pinned to the top-right corner of its cell? Use `justify-self` and `align-self` directly on that Grid Item (or shorten it with `place-self: start end;`).

```css
.badge-pojok {
  justify-self: end;   /* Flush right inside its cell */
  align-self: start;   /* Flush top inside its cell */
}
```

Enable `display: grid` and `place-items: center` in the Playground to see the alignment matrix in action!
