---
id: css-layout-inti-7
courseId: fe-css-02
order: 16
title: "Changing Display Order with the order Property"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Understand how the order property works to change the visual order of Flex Items"
  - "Know the default value (0) and positive/negative number comparisons"
  - "Understand accessibility ethics regarding differences between visual order and HTML DOM order"
codeLanguage: css
codeSnippet: |
  .item-prioritas {
    order: -1; /* Melompat ke urutan visual paling awal */
  }

  .item-terakhir {
    order: 1;  /* Pindah ke urutan visual paling akhir */
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji properti order</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Memanipulasi Urutan Visual</h2>
      <div class="wadah">
        <div class="kartu kartu-a">Item A (Asli #1)</div>
        <div class="kartu kartu-b">Item B (Asli #2)</div>
        <div class="kartu kartu-c">Item C (Asli #3)</div>
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
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    flex: 1;
    padding: 20px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    text-align: center;
  }

  .kartu-a {
    background-color: #1a6368;
    order: 0;
  }

  .kartu-b {
    background-color: #df7b56;
    order: 0;
  }

  .kartu-c {
    background-color: #d49438;
    order: 0;
  }
playgroundTask: "Try giving 'order: -1;' to '.kartu-c' and 'order: 2;' to '.kartu-a'. Press 'Run' and observe how Item C jumps to first place on the left, followed by Item B in the middle, and Item A on the far right—without us changing a single line of HTML!"
---
Have you ever faced a situation where on a desktop screen an image needs to be on the right side of text, yet when opened on a phone screen you want that image to appear at the very top before the text?

In the past you had to rewrite the HTML or use JavaScript tricks. In Flexbox, you simply use one simple property: **`order`**.

```text
HTML Order : [A (order:2)] [B (order:1)] [C (order:3)]
UI Display : [B (order:1)] [A (order:2)] [C (order:3)]
```
## How Does the `order` Property Work?

By default, every flex child element has a value of **`order: 0`**.

The browser renders elements sequentially based on their `order` numeric value from smallest to largest:

- Elements with a smaller numeric value appear **earlier**.
- Elements with a larger numeric value appear **later**.
- If two elements share the same `order` value (for example, both are `0`), the browser displays them according to their original appearance order in the HTML document (*source order*).

```css
.item-1 { order: 2; }
.item-2 { order: -1; } /* Negative values are allowed! */
.item-3 { order: 1; }
```
The visual display order on screen will become:
1. **Item 2** (because `-1` is the smallest)
2. **Item 3** (because `1`)
3. **Item 1** (because `2` is the largest)

## Important Warning About Accessibility

Even though the `order` property makes responsive design super convenient, there is one ethical rule you must obey:

> **The `order` property only changes the visual display for the user's eyes, and DOES NOT CHANGE the original HTML document order.**

This means:
- Assistive *Screen Readers* for visually impaired users will still read the document in its original HTML text order.
- Keyboard navigation keys (**Tab**) will also move focus according to the original HTML order, not the visual order on screen!

Use `order` only for responsive aesthetic layout adjustments, not to drastically rearrange the logical flow of a page.

In the next module, we will combine all Flexbox concepts in a real practical exercise: assembling a Navigation Bar!