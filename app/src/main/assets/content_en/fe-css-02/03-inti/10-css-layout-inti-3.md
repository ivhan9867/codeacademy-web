---
id: css-layout-inti-3
courseId: fe-css-02
order: 12
title: "Aligning the Main Axis with justify-content"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Understand the role of the justify-content property in distributing empty space along the Main Axis"
  - "Master the 6 main values: flex-start, center, flex-end, space-between, space-around, and space-evenly"
  - "Know real-world use cases for navbars, pagination buttons, and widgets"
codeLanguage: css
codeSnippet: |
  .navigasi {
    display: flex;
    justify-content: space-between; /* Mendorong logo ke kiri & menu ke kanan */
    align-items: center;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji justify-content</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Eksperimen justify-content</h2>
      <div class="wadah">
        <div class="item item-1">Logo</div>
        <div class="item item-2">Pencarian</div>
        <div class="item item-3">Akun</div>
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
    justify-content: flex-start; /* Ubah nilai ini! */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
    min-height: 80px;
  }

  .item {
    padding: 12px 20px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    text-align: center;
  }

  .item-1 { background-color: #1a6368; }
  .item-2 { background-color: #df7b56; }
  .item-3 { background-color: #287948; }
playgroundTask: "Try changing the value 'justify-content: flex-start;' on the '.wadah' selector to 'space-between', then press 'Run'. Notice how Logo jumps to the far left and Akun jumps to the far right! Afterward, try the values 'center' and 'space-evenly'."
---
When the total width of child elements is smaller than the width of their parent container, there will be **remaining empty space**.

How do we distribute or place that remaining empty space along the **Main Axis**? The answer is the **`justify-content`** property.

```text
flex-start    : |[1][2][3]              |
center        : |       [1][2][3]       |
space-between : |[1]       [2]       [3]|
space-evenly  : |   [1]    [2]    [3]   |
```
## 6 Main Values of `justify-content`

Let's dissect its behavior when `flex-direction: row` (horizontal axis):

### 1. `flex-start` (Default)
All items gather tightly at the start of the main axis (left edge). The remaining empty space is left stacked on the right edge.

### 2. `center`
All items gather tightly right in the dead center of the container. Empty space is split equally on the far left and far right sides. Very suitable for pagination number button bars or banner titles.

### 3. `flex-end`
All items are pushed to gather at the end of the main axis (right edge). Very frequently used for "Submit" and "Cancel" buttons in the bottom-right corner of a modal dialog.

### 4. `space-between` (Web Developer Favorite!)
The first item sticks to the far-left edge, the last item sticks to the far-right edge, and the remaining empty space is divided evenly right between the items in the middle. This is the legendary recipe for creating a **Website Header** (logo on the left, navigation on the right)!

### 5. `space-around`
Every item receives an equal share of empty space on its left and right sides. As a result, the gap between items in the middle looks twice as wide as the distance from the outer items to the container walls.

### 6. `space-evenly`
All empty gaps (the distance from the edge wall to the first item, the distance between items, and the distance from the last item to the edge wall) are divided **truly identically**. The appearance becomes very symmetrical and harmonious.

Try experimenting with all six values above in the Playground to see the difference firsthand!