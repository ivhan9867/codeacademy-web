---
id: css-layout-lanjutan-5
courseId: fe-css-02
order: 24
title: "Visual Layout Sketching with grid-template-areas"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Understand the concept of ASCII-art map-based page layout design"
  - "Connect HTML elements to named regions using the grid-area property"
  - "Build a complete page skeleton (header, sidebar, content, footer) visually"
codeLanguage: css
codeSnippet: |
  .layout-halaman {
    display: grid;
    grid-template-areas:
      "kepala kepala"
      "samping utama"
      "kaki kaki";
    grid-template-columns: 240px 1fr;
    gap: 12px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji grid-template-areas</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="halaman">
        <header class="header">Area Header (Logo & Nav)</header>
        <aside class="sidebar">Area Sidebar</aside>
        <main class="content">Area Konten Utama Artikel</main>
        <footer class="footer">Area Footer Hak Cipta</footer>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .halaman {
    display: grid;
    /* Peta Denah Wilayah */
    grid-template-areas:
      "header header"
      "sidebar content"
      "footer footer";
    grid-template-columns: 180px 1fr;
    grid-template-rows: 60px 180px 50px;
    gap: 10px;
  }

  /* Menghubungkan elemen ke nama peta: */
  .header  { grid-area: header; background-color: #104246; color: white; padding: 16px; border-radius: 6px; }
  .sidebar { grid-area: sidebar; background-color: #389b9e; color: white; padding: 16px; border-radius: 6px; }
  .content { grid-area: content; background-color: #ffffff; border: 1px solid #1a6368; padding: 16px; border-radius: 6px; }
  .footer  { grid-area: footer; background-color: #24211d; color: white; padding: 16px; border-radius: 6px; text-align: center; }
playgroundTask: "Try swapping 'sidebar' and 'content' on the second row of the map: change '\"sidebar content\"' to '\"content sidebar\"', and change 'grid-template-columns: 180px 1fr;' to '1fr 180px;'. Click 'Run' and watch the sidebar move to the right as easily as flipping a floor plan!"
---
The most intuitive and fun feature in all of CSS is **`grid-template-areas`**.

Many web designers sketch a rough page wireframe on paper:
```text
+--------------------------+
|          HEADER          |
+--------------+-----------+
|   SIDEBAR    |  CONTENT  |
+--------------+-----------+
|          FOOTER          |
+--------------------------+
```

With `grid-template-areas`, you can write that exact wireframe sketch **directly into your CSS code** just like the diagram above!

```text
grid-template-areas:
  "header  header"
  "sidebar main  "
  "footer  footer";
```
## Two Easy Steps to Use Template Areas

### Step 1: Name Each Child Element's Region
In the CSS for each child element, use the **`grid-area`** property to assign a region name (without quotes):

```css
header { grid-area: header; }
aside  { grid-area: sidebar; }
main   { grid-area: content; }
footer { grid-area: footer; }
```

### Step 2: Draw the Map on the Parent Container
On the parent container, use **`grid-template-areas`**. Each double-quoted string represents **one row**, and each word inside represents a **column**:

```css
.halaman {
  display: grid;
  grid-template-columns: 240px 1fr;
  grid-template-areas:
    "header header"   /* Row 1: header spans 2 columns */
    "sidebar content" /* Row 2: sidebar on the left, content on the right */
    "footer footer";  /* Row 3: footer spans 2 columns */
}
```

## Rules for Template Areas

1. Every row must have the **exact same number of columns**.
2. Every named area must form a **complete rectangle or square**. You cannot create "L"-shaped or "T"-shaped areas.
3. If you want to leave a cell completely empty, use a dot (`.`):
   ```css
   grid-template-areas:
     "header header"
     "content ."       /* The second column in this row is left empty */
     "footer footer";
   ```

Designing a page layout has never been this concise and crystal clear!
