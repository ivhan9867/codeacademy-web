---
id: css-layout-inti-6
courseId: fe-css-02
order: 15
title: "Item Flexibility: flex-grow, flex-shrink, and flex-basis"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Understand how the trio of flexibility properties on child elements work: flex-grow, flex-shrink, and flex-basis"
  - "Know how empty space is distributed proportionally"
  - "Master the industry-popular shorthand flex: 1"
codeLanguage: css
codeSnippet: |
  .kolom-utama {
    flex: 2; /* Mengambil jatah ruang 2x lipat */
  }

  .sidebar {
    flex: 1; /* Mengambil jatah ruang 1x */
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji flex-grow, shrink, basis</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Pembagian Jatah Ruang</h2>
      <div class="wadah">
        <div class="item item-kiri">Sidebar (flex: 1)</div>
        <div class="item item-kanan">Konten Utama (flex: 2)</div>
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
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
    gap: 12px;
  }

  .item {
    padding: 24px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    text-align: center;
  }

  .item-kiri {
    background-color: #df7b56;
    flex: 1; /* Coba ganti nilainya! */
  }

  .item-kanan {
    background-color: #1a6368;
    flex: 2; /* Coba ganti nilainya! */
  }
playgroundTask: "Change '.item-kiri' to 'flex: 1;' and '.item-kanan' to 'flex: 1;'. Run the code and notice how both columns now have the exact same width (50% - 50%) regardless of the text length inside them!"
---
So far we have mostly been giving commands to the **Parent Container**. Now it is time to empower the **Child Elements (Flex Items)** so they can decide the fate of their own sizes!

This power is controlled by a trio: **`flex-grow`**, **`flex-shrink`**, and **`flex-basis`**.

```text
600px Container (flex-grow):
|[  flex: 1 (200px)  ]|[       flex: 2 (400px)       ]|
```
## 1. `flex-basis`: Initial Size Before Flexing

`flex-basis` is the ideal starting size of an item before remaining empty space is distributed.
- By default its value is `auto` (following the width/height of its content or an explicit `width` value).
- You can set an absolute value like `flex-basis: 200px;`.

## 2. `flex-grow`: Ability to Claim Empty Space

If the container has leftover empty space, who gets to claim it and how much?
- The default value is `0` (meaning the item refuses to grow, staying at its original size).
- If you set `flex-grow: 1;` on one item, that item will stretch to consume all remaining available space.
- If item A is given `flex-grow: 1` and item B is given `flex-grow: 2`, then the remaining empty space is divided into 3 parts: 1 part for A and 2 parts for B.

## 3. `flex-shrink`: Ability to Shrink When Space Is Tight

Conversely, if the container is too narrow and space goes negative, who is willing to be sacrificed and shrink?
- The default value is `1` (meaning all items agree to shrink proportionally so they don't burst out of the container).
- If you give `flex-shrink: 0;`, that item becomes **immune**: it refuses to shrink and will keep its original size!

## The Legend: Shorthand `flex: 1`

Writing three properties separately (`flex-grow`, `flex-shrink`, `flex-basis`) is quite long. Because of that, developers use the compact shorthand form:

```css
/* flex: [grow] [shrink] [basis] */
.kotak {
  flex: 1 1 0%; /* Often written concisely as: flex: 1; */
}
```

When you write **`flex: 1;`** on several child elements inside the same container, you are commanding:
> *"Split the entire width of this container equally and fairly among all of us, ignoring each item's natural text width!"*

This is the fastest and cleanest way to create equal-width columns in Flexbox.