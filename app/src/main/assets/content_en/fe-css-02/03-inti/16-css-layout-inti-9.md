---
id: css-layout-inti-9
courseId: fe-css-02
order: 18
title: "Advanced Alignment: align-content & Perfect Centering"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Precisely distinguish the functions of align-items (per line) and align-content (across multiple lines)"
  - "Apply horizontal and vertical perfect centering techniques using Flexbox"
  - "Avoid common pitfalls when align-content has no effect or a container has no height"
codeLanguage: css
codeSnippet: |
  .center-sempurna {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 240px;
  }

  .multi-baris {
    display: flex;
    flex-wrap: wrap;
    align-content: space-between;
    height: 260px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: align-content & Centering</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>1. Kotak Centering Sempurna</h3>
      <div class="hero-box">
        <div class="kartu-sambutan">
          <h4>Selamat Datang!</h4>
          <p>Posisiku tepat di tengah secara horizontal &amp; vertikal.</p>
        </div>
      </div>

      <h3>2. Perataan Multi-Baris (align-content)</h3>
      <div class="galeri-wrap">
        <span class="pill">HTML5</span>
        <span class="pill">CSS3</span>
        <span class="pill">Flexbox</span>
        <span class="pill">CSS Grid</span>
        <span class="pill">Responsive</span>
        <span class="pill">Variables</span>
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

  /* 1. Lengkapi agar .kartu-sambutan berada tepat di tengah .hero-box */
  .hero-box {
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    border-radius: 10px;
    min-height: 180px;
    padding: 16px;
    /* Tambahkan display: flex; justify-content: center; align-items: center; di sini */
  }

  .kartu-sambutan {
    background-color: #104246;
    color: #ffffff;
    padding: 16px 24px;
    border-radius: 8px;
    text-align: center;
  }

  .kartu-sambutan h4 {
    margin: 0 0 6px 0;
  }

  .kartu-sambutan p {
    margin: 0;
    font-size: 13px;
  }

  /* 2. Wadah multi-baris dengan ruang ekstra di sumbu vertikal */
  .galeri-wrap {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    height: 180px;
    padding: 14px;
    background-color: #f1ece4;
    border: 2px dashed #968e86;
    border-radius: 10px;
    /* Coba tambahkan align-content: center; atau space-between; */
  }

  .pill {
    background-color: #df7b56;
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-weight: bold;
    font-size: 13px;
    height: fit-content;
  }
playgroundTask: "In '.hero-box', enable perfect centering by adding 'display: flex;', 'justify-content: center;', and 'align-items: center;'. Then in '.galeri-wrap', add 'align-content: center;' to pull all rows of pills together into the vertical center!"
verificationRules:
  - "css:display: flex"
  - "css:justify-content"
  - "css:align-items"
---
One of the most frequent confusions beginners experience when using Flexbox is telling apart **`align-items`** and **`align-content`**. Both control positioning on the *Cross Axis*, yet their scope of work is very different.

## Difference Between `align-items` vs `align-content`

Imagine a bookcase with several shelf tiers.
- **`align-items`** arranges the position of individual books **within a single shelf tier** (for instance, centering a short item relative to a tall item on the same row).
- **`align-content`** arranges the spacing and position of **all the shelf tiers as a whole** relative to the cabinet height (only works if items wrap into **more than one line** using `flex-wrap: wrap`).

```text
  align-items: center (1 row / per row)          align-content: center (across multiple rows)
+-----------------------------------------+    +-----------------------------------------+
|                                         |    |              (empty space)              |
|   [A]     [  B  ]     [ C ]             |    |   [Item 1]    [Item 2]    [Item 3]      |
|  (aligned on center axis of this row)   |    |   [Item 4]    [Item 5]                  |
|                                         |    |              (empty space)              |
+-----------------------------------------+    +-----------------------------------------+
```

Summary of popular values for `align-content` when `flex-wrap: wrap` is active:

| `align-content` Value | Multi-Line Behavior on Vertical Axis |
| :--- | :--- |
| `stretch` *(default)* | Each row stretches evenly to fill the entire container height |
| `flex-start` | All rows pack tightly toward the top edge of the container |
| `center` | The entire group of rows gathers in the vertical center of the container |
| `space-between` | First row at the very top, last row at the very bottom |
| `space-around` | Space above, below, and between rows is divided evenly |

## Perfect Centering Pattern

Back in the day, putting an element dead-center on the screen both horizontally and vertically required tricks with `position: absolute`, `top: 50%`, and `transform: translate(-50%, -50%)`. With Flexbox, just three magic lines on the parent container do it:

```css
.wadah-tengah {
  display: flex;
  justify-content: center; /* Center on Main Axis (horizontal) */
  align-items: center;     /* Center on Cross Axis (vertical) */
  min-height: 100vh;       /* Important: give container height so vertical space exists */
}
```

A super-concise alternative for a single child inside a flex container is giving `margin: auto` to the child element:

```css
.wadah {
  display: flex;
  min-height: 300px;
}

.anak-tunggal {
  margin: auto; /* Automatically absorbs remaining space on all four sides */
}
```

## Common Pitfalls to Avoid

1. **Using `align-content` without `flex-wrap: wrap`**:
   If the container still uses `flex-wrap: nowrap` (default), all items stay on a single line. In a single-line state, `align-content` **has no effect whatsoever**!
2. **Forgetting to give a height (`height` / `min-height`) to the container**:
   If the container's height snugly matches its content height, there is no leftover space on the vertical axis. As a result, neither `align-items: center` nor `align-content: center` will appear to move anything at all.
3. **Mistaking `margin: auto` for `align-items: center` when there are many items**:
   If there are many wrapped items whose total height exceeds the container (*overflow*), `margin: auto` can push the top portion of items outside the scrollable area, whereas `justify-content: center` stays safe.

Test the difference between single-box centering and multi-line alignment directly in the Playground now!