---
id: css-layout-prep-3
courseId: fe-css-02
order: 3
title: "Setting Up the Playground for Layout Experiments"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Learn box-layout visualization techniques using contrasting background colors and dashed borders"
  - "Use the Run, Reset, and Live Preview buttons in the CodeAcademy Playground"
  - "Try out the first starter template to test container and item behavior"
codeLanguage: css
codeSnippet: |
  .wadah {
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
  }

  .kotak {
    background-color: #df7b56;
    color: white;
    padding: 16px;
    margin: 8px;
    border-radius: 8px;
    text-align: center;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji Layout Playground</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="wadah">
        <div class="kotak">Kotak 1</div>
        <div class="kotak">Kotak 2</div>
        <div class="kotak">Kotak 3</div>
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
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
  }

  .kotak {
    background-color: #df7b56;
    color: white;
    padding: 16px;
    margin: 8px;
    border-radius: 8px;
    text-align: center;
  }
playgroundTask: "Try opening the CSS tab in the Playground, add the line 'display: flex;' inside the '.wadah' selector block, then tap the 'Run' button. Notice how all three boxes immediately line up side by side!"
---
Learning layout without color visualization is like arranging bedroom furniture in the dark. We are often confused about why an element shifts downward or refuses to grow, when the cause is simply an invisible wrapper box boundary.

```text
+--- .container (border: 2px dashed #1a6368) ---+
|  +----------+  +----------+  +----------+     |
|  |  Item 1  |  |  Item 2  |  |  Item 3  |     |
|  +----------+  +----------+  +----------+     |
+-----------------------------------------------+
```
## Developer Secret Trick: Contrasting Colors & Dashed Lines

Throughout this course, we will frequently use **visual debugging**:

1. **Outer Container**: We give it a soft background color with a dashed border (`border: 2px dashed #1a6368`). This helps us see the maximum boundary of the available space.
2. **Child Elements (Items)**: We give them a bright solid color and rounded corners (`border-radius: 8px`). This helps us observe how the size and position of each child change when layout properties are enabled.

## Making Use of the CodeAcademy Playground Features

Below the practical lesson modules, you can experiment directly with the mini editor:

- **HTML & CSS Tabs**: You can switch between the document structure (`index.html`) and style rules (`style.css`).
- **Run Button**: Re-renders all code into the **Live Preview** panel at the bottom or side of your screen.
- **Reset Button**: If your code breaks or gets deleted accidentally, the Reset button restores the editor text to a clean *starter state*.

## Time to Try It Yourself

Look at the starter code in this module. Currently, the three `.kotak` elements are inside `.wadah`. Because `<div>` elements are *block* type by default, the three boxes stack from top to bottom.

Go ahead and complete the **playgroundTask**:
> Add `display: flex;` inside the `.wadah` selector, then press **Run**. In an instant, all three boxes will line up side by side. That is the magic of Flexbox that we are going to explore thoroughly!