---
id: css-layout-lanjutan-10
courseId: fe-css-02
order: 29
title: "CSS Variables (--var) & Consistent Spacing Systems"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Declare Design Tokens for spacing, colors, and border radius inside the :root selector"
  - "Use the var() function to keep gap, padding, and card corners consistent"
  - "Dynamically change CSS Grid column counts inside Media Queries just by updating a variable"
codeLanguage: css
codeSnippet: |
  :root {
    --space-sm: 8px;
    --space-md: 16px;
    --space-lg: 24px;
    --radius-card: 12px;
    --grid-cols: 1;
  }

  .katalog {
    display: grid;
    grid-template-columns: repeat(var(--grid-cols), 1fr);
    gap: var(--space-md);
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: CSS Variables & Spacing</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Katalog Berbasis Design Tokens</h2>
      <div class="katalog">
        <div class="item-kartu">
          <span class="label">Token A</span>
          <h4>Spacing Konsisten</h4>
          <p>Semua jarak memakai skala kelipatan 8px dari :root.</p>
        </div>
        <div class="item-kartu">
          <span class="label">Token B</span>
          <h4>Warna Terpusat</h4>
          <p>Ubah satu baris di :root, seluruh tema ikut berubah serempak.</p>
        </div>
        <div class="item-kartu">
          <span class="label">Token C</span>
          <h4>Kolom Dinamis</h4>
          <p>Jumlah kolom grid dikontrol variabel --grid-cols.</p>
        </div>
      </div>
    </body>
  </html>
starterCss: |
  /* 1. Definisikan Design Tokens di :root di sini! */
  /* Contoh:
  :root {
    --space-sm: 8px;
    --space-md: 16px;
    --color-primary: #104246;
    --color-accent: #df7b56;
    --radius-md: 12px;
    --grid-cols: 2;
  }
  */

  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  h2 {
    color: var(--color-primary, #104246);
    margin-bottom: var(--space-md, 16px);
  }

  .katalog {
    display: grid;
    grid-template-columns: repeat(var(--grid-cols, 1), 1fr);
    gap: var(--space-md, 16px);
  }

  .item-kartu {
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: var(--radius-md, 10px);
    padding: var(--space-md, 16px);
  }

  .label {
    display: inline-block;
    background-color: var(--color-accent, #df7b56);
    color: white;
    padding: 4px 10px;
    border-radius: 999px;
    font-size: 11px;
    font-weight: bold;
    margin-bottom: var(--space-sm, 8px);
  }

  .item-kartu h4 {
    margin: 0 0 var(--space-sm, 8px) 0;
    color: var(--color-primary, #104246);
  }

  .item-kartu p {
    margin: 0;
    font-size: 13px;
    color: #6b655d;
    line-height: 1.5;
  }
playgroundTask: "Uncomment/enable the ':root { ... }' block at the very top of the CSS with variable values like '--space-md: 20px;', '--color-primary: #1a6368;', '--radius-md: 14px;', and '--grid-cols: 2;'. Make sure the properties below call 'var(...)', then click Run to see everything update together!"
verificationRules:
  - "css::root"
  - "css:var("
---
Imagine you have already written 300 lines of CSS with `padding: 16px`, `gap: 16px`, and the primary color `#104246` scattered across dozens of selectors. Suddenly, the designer asks to widen every spacing to `20px` and change the brand color. Updating them one by one is exhausting and easy to miss!

That is where **CSS Custom Properties** (commonly called **CSS Variables**) become the backbone of modern design systems.

```text
                       +----------------------------+
                       | :root (Source of Truth)    |
                       | --space-md: 16px;          |
                       | --grid-cols: 2;            |
                       +-------------+--------------+
                                     |
             +-----------------------+-----------------------+
             v                       v                       v
    .navbar { gap: var(...) }  .grid { repeat(var(...)) }  .card { padding: var(...) }
```

## 1. Defining Tokens in `:root`

The `:root` selector targets the highest element in the document (`<html>`), making variables defined inside it accessible globally anywhere. Variable names **must start with two hyphens (`--`)**:

```css
:root {
  /* Spacing Scale (4px / 8px multiples) */
  --space-xs: 4px;
  --space-sm: 8px;
  --space-md: 16px;
  --space-lg: 24px;
  --space-xl: 32px;

  /* Color Palette & Radius */
  --color-primary: #104246;
  --color-accent: #df7b56;
  --radius-card: 12px;
}
```

## 2. Reading Variables with `var()` & Fallback Values

To use these tokens inside `gap`, `padding`, or color properties, call `var(--variable-name)`. You can also provide a *fallback* value as the second parameter in case the variable isn't defined yet:

```css
.kartu {
  padding: var(--space-md);
  border-radius: var(--radius-card, 8px); /* Uses 8px if --radius-card is missing */
}
```

## 3. Smart Trick: Controlling CSS Grid via Variables in `@media`

Instead of rewriting a long `grid-template-columns` declaration at every *breakpoint*, you only need to update **one number variable** inside your Media Query:

```css
:root {
  --grid-cols: 1;
}

.galeri {
  display: grid;
  grid-template-columns: repeat(var(--grid-cols), 1fr);
  gap: var(--space-md);
}

@media (min-width: 768px) {
  :root {
    --grid-cols: 2; /* Automatically becomes 2 columns! */
  }
}

@media (min-width: 1024px) {
  :root {
    --grid-cols: 3; /* Automatically becomes 3 columns! */
  }
}
```

Using a *Design Tokens* system built on `:root` and `var()` keeps your CSS layouts clean, uniform, easy to maintain across a large team, and ready to adapt to themes like *dark mode* in the future!
