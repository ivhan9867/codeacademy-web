---
id: css-layout-pend-2
courseId: fe-css-02
order: 7
title: "Quick Flashback: Normal Flow, Display, & Box Model"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Recall the Normal Flow concept and the difference between block vs inline display behavior"
  - "Re-understand the 4 layers of the Box Model (content, padding, border, margin)"
  - "Observe the limitations of Normal Flow when trying to arrange elements side by side in balance"
codeLanguage: css
codeSnippet: |
  .kotak-blok {
    display: block;
    background-color: #df7b56;
    color: white;
    padding: 12px;
    margin-bottom: 8px;
  }

  .kotak-inline {
    display: inline;
    background-color: #1a6368;
    color: white;
    padding: 12px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Kilas Balik Box Model & Flow</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>Perilaku Block (Menguasai 1 Baris Penuh):</h3>
      <div class="kotak-blok">Blok 1</div>
      <div class="kotak-blok">Blok 2</div>

      <h3>Perilaku Inline (Mengalir Sebaris Teks):</h3>
      <span class="kotak-inline">Inline A</span>
      <span class="kotak-inline">Inline B</span>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 20px;
    background-color: #faf7f2;
    color: #24211d;
  }

  h3 {
    margin-top: 16px;
    margin-bottom: 8px;
    color: #104246;
  }

  .kotak-blok {
    display: block;
    background-color: #df7b56;
    color: white;
    padding: 12px;
    margin-bottom: 8px;
    border-radius: 6px;
  }

  .kotak-inline {
    display: inline;
    background-color: #1a6368;
    color: white;
    padding: 8px 12px;
    border-radius: 6px;
  }
playgroundTask: "Try changing 'display: block;' on the '.kotak-blok' selector to 'display: inline-block;'. Run the code and notice how Blok 1 and Blok 2 can now sit side by side, yet still have a small default text whitespace gap!"
---
Before we dive into the magic of Flexbox and Grid, let's refresh your memory on how a browser naturally arranges elements without modern layout intervention.

This natural system is called **Normal Flow**.

```text
+------------------ MARGIN ------------------+
| +---------------- BORDER ----------------+ |
| | +------------- PADDING --------------+ | |
| | |           CONTENT AREA             | | |
| | +------------------------------------+ | |
| +----------------------------------------+ |
+--------------------------------------------+
```
## 1. Two Main Characters: Block vs Inline

By default, HTML elements are divided into two display worlds:

- **Block Elements (`<div>`, `<p>`, `<h1>`, `<header>`, etc.)**:
  Their nature is selfish and space-hungry. They always start a new line and stretch as wide as possible to fill their parent container's width (100% width). Other block elements that come after them are forced down to the next line.
- **Inline Elements (`<span>`, `<a>`, `<strong>`, `<em>`, etc.)**:
  Their nature is polite and space-saving. They only take up as much space as the text or content inside them. Other inline elements can sit side by side on the same line just like words in a paragraph sentence. Unfortunately, pure inline elements **cannot have their `width` and `height` set precisely**.

## 2. Four Layers of the Box Model

Every element on the browser screen is wrapped by 4 box layers:
1. **Content**: The core space where text or images reside.
2. **Padding**: Empty space between the content and the border line.
3. **Border**: The outer edge line bounding the box.
4. **Margin**: Empty space outside the border line that separates this element from its neighbors.

## Classic Problem: Wanting to Sit Side by Side While Keeping Dimensions

When you want to create a row of menu buttons or product cards, you get stuck at a crossroads:
- If you use `display: block`, they refuse to sit side by side (always stacking downward).
- If you use `display: inline`, you are not free to set their height and width.
- There used to be a middle-ground solution called `display: inline-block`, but this solution left a visual bug: browsers always insert a small *whitespace* gap between elements as if they were ordinary text spaces!

This need is what drove web engineers to create a smarter way: turning the parent wrapper into a flexible container.