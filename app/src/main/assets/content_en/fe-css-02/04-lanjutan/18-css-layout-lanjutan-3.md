---
id: css-layout-lanjutan-3
courseId: fe-css-02
order: 22
title: "The Power of Fractions & Repetition: The fr Unit & repeat()"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Understand how the fractional unit (fr) automatically distributes remaining free space"
  - "Combine fixed units (px) with fractional units (fr)"
  - "Simplify repeated column declarations using the repeat() function"
codeLanguage: css
codeSnippet: |
  .galeri-modern {
    display: grid;
    grid-template-columns: repeat(3, 1fr); /* 3 kolom sama lebar yang fleksibel */
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji satuan fr & repeat()</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Pemanfaatan fr & repeat()</h2>
      <div class="wadah">
        <div class="item">1</div>
        <div class="item">2</div>
        <div class="item">3</div>
        <div class="item">4</div>
        <div class="item">5</div>
        <div class="item">6</div>
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
    display: grid;
    /* Ubah kode di bawah ini memakai fr dan repeat(): */
    grid-template-columns: 1fr 2fr 1fr;
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .item {
    background-color: #1a6368;
    color: white;
    font-weight: bold;
    padding: 24px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Try changing 'grid-template-columns: 1fr 2fr 1fr;' to 'repeat(4, 1fr);'. Click 'Run' and watch how the container immediately splits the screen into 4 equal columns with clean, concise syntax!"
---
In the old days when using percentages (`33.33%`), if you added `gap: 20px;`, the total width would exceed 100% and cause your page to overflow sideways (*horizontal scroll*). You had to write messy math like `calc((100% - 40px) / 3)`.

CSS Grid solved this headache by inventing a new unit: **`fr` (Fractional Unit)**.

```text
grid-template-columns: repeat(3, 1fr); gap: 16px;
+---------------+  +---------------+  +---------------+
|      1fr      |  |      1fr      |  |      1fr      |
+---------------+  +---------------+  +---------------+
```
## What Is the `fr` Unit?

One `1fr` unit represents **"one share of the available remaining space"**.

The browser first subtracts any fixed pixel sizes and `gap` distances from the container's total width. The remaining space is then divided according to the `fr` values:

```css
/* Splits remaining space into 4 shares:
   - Column 1 gets 1 share
   - Column 2 gets 2 shares (twice as wide)
   - Column 3 gets 1 share */
.grid {
  grid-template-columns: 1fr 2fr 1fr;
}
```

You can even mix fixed units with `fr`:

```css
/* Fixed 250px sidebar, while the content area takes ALL remaining space */
.tata-letak {
  grid-template-columns: 250px 1fr;
}
```
No more manual subtraction math!

## Shortening Code with `repeat()`

What if you want to build a 12-column layout? Do you have to type `1fr 1fr 1fr...` twelve times? Of course not!

Use the built-in function **`repeat(count, size)`**:

```css
/* The tiring way: */
grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr;

/* The clean and concise way: */
grid-template-columns: repeat(6, 1fr);
```

You can even repeat a multi-track pattern:

```css
/* Produces 4 columns: 100px 1fr 100px 1fr */
grid-template-columns: repeat(2, 100px 1fr);
```

Super clean, easy to read, and effortless to adjust later.
