---
id: css-layout-inti-4
courseId: fe-css-02
order: 13
title: "Cross-Axis Alignment with align-items & align-self"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Understand the role of the align-items property in aligning items on the Cross Axis"
  - "Master the values stretch, center, flex-start, flex-end, and baseline"
  - "Achieve the legendary 'Centering Anything' trick in CSS in 2 lines of code"
  - "Use align-self to exempt a single item from the container's rule"
codeLanguage: css
codeSnippet: |
  .tengah-sempurna {
    display: flex;
    justify-content: center; /* Tengah horizontal (Main Axis) */
    align-items: center;     /* Tengah vertikal (Cross Axis) */
    height: 100vh;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji align-items & align-self</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="wadah">
        <div class="kartu kartu-pendek">Pendek</div>
        <div class="kartu kartu-sedang">Sedang<br />Teks Dua Baris</div>
        <div class="kartu kartu-spesial">Item Spesial</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah {
    display: flex;
    height: 180px; /* Kontainer memiliki tinggi tetap */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 12px;
    border-radius: 8px;
    align-items: stretch; /* Ubah ke: center, flex-start, flex-end */
  }

  .kartu {
    width: 100px;
    padding: 12px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    margin-right: 12px;
    text-align: center;
  }

  .kartu-pendek { background-color: #1a6368; }
  .kartu-sedang { background-color: #df7b56; }
  .kartu-spesial {
    background-color: #d49438;
    /* Tambahkan align-self di sini jika ingin tampil beda sendiri! */
  }
playgroundTask: "Try changing 'align-items: stretch;' on '.wadah' to 'align-items: center;'. Run the code and notice how all cards float neatly in the middle of the container's height! Afterward, give the 'align-self: flex-end;' property specifically to '.kartu-spesial'."
---
In the classic CSS era, there was a famous joke among programmers: *"The hardest thing in the world is centering a box vertically in CSS."* Back then, people had to use complicated combinations of `line-height`, `table-cell`, or `transform: translateY(-50%)`.

With the arrival of Flexbox, that problem was solved forever thanks to the **`align-items`** property.

```text
align-items: flex-start   align-items: center     align-items: stretch
+--------------------+    +--------------------+  +--------------------+
| [1] [2] [3]        |    |                    |  | [1] [2] [3]        |
|                    |    | [1] [2] [3]        |  | [1] [2] [3]        |
|                    |    |                    |  | [1] [2] [3]        |
+--------------------+    +--------------------+  +--------------------+
```
## What Is `align-items`?

If `justify-content` works on the **Main Axis**, then `align-items` is responsible for positioning items on the **Cross Axis** perpendicular to it.

In `flex-direction: row`, the cross axis is the vertical direction (top-to-bottom).

### Variations of `align-items` Values:

1. **`stretch` (Default Value)**:
   Items will stretch to fill the entire height of the parent container (if the items do not have a rigidly locked `height`).
2. **`center`**:
   Items are placed right in the middle of the container's height. This is a lifesaver for web creators when aligning text and icons!
3. **`flex-start`**:
   Items stick to the ceiling (very top) of the container.
4. **`flex-end`**:
   Items stick to the floor (very bottom) of the container.
5. **`baseline`**:
   Items are aligned based on the *font baseline* of the text inside them. Very useful if one item has a 24px font size and its neighbor has a 14px font size so their letters stay aligned for reading.

## Legendary Recipe: Perfect Centering

Want to center a login card right in the dead center of a phone/computer screen? Just use these two lines:

```css
.layar-penuh {
  display: flex;
  justify-content: center; /* Horizontal center */
  align-items: center;     /* Vertical center */
}
```

## Independent Rebel: `align-self`

What if the container sets `align-items: center`, but there is one particular item that wants to stick to the bottom floor?

Use the **`align-self`** property written directly on the **child element**:

```css
.kartu-spesial {
  align-self: flex-end; /* Ignores container rule, chooses to stick to bottom */
}
```

The next module will discuss what happens if we have too many items and the screen is narrow!