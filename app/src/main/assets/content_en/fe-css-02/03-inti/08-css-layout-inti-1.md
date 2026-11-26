---
id: css-layout-inti-1
courseId: fe-css-02
order: 10
title: "The Flexbox Family: Flex Container and Flex Items"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Understand the hierarchical relationship between a Flex Container (parent) and Flex Items (direct children)"
  - "Know the immediate changes to child elements when the display: flex property is enabled"
  - "Distinguish properties meant for the container versus properties meant for items"
codeLanguage: css
codeSnippet: |
  .flex-container {
    display: flex;
    background-color: #e2f0f1;
    padding: 16px;
    border: 2px dashed #1a6368;
  }

  .flex-item {
    background-color: #1a6368;
    color: white;
    padding: 20px;
    margin: 8px;
    border-radius: 8px;
    font-weight: bold;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Keluarga Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>Sebelum Menjadi Flex Container:</h3>
      <div class="kontainer-biasa">
        <div class="kotak">Item 1 (Blok Biasa)</div>
        <div class="kotak">Item 2 (Blok Biasa)</div>
        <div class="kotak">Item 3 (Blok Biasa)</div>
      </div>

      <h3>Setelah Diberi display: flex:</h3>
      <div class="flex-container">
        <div class="flex-item">Item A</div>
        <div class="flex-item">Item B</div>
        <div class="flex-item">Item C</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  h3 {
    color: #104246;
    margin-top: 20px;
  }

  .kontainer-biasa {
    background-color: #f1ece4;
    padding: 12px;
    border: 2px dashed #968e86;
  }

  .kotak {
    background-color: #6b655d;
    color: white;
    padding: 12px;
    margin: 6px 0;
    border-radius: 6px;
  }

  .flex-container {
    display: flex;
    background-color: #e2f0f1;
    padding: 12px;
    border: 2px dashed #1a6368;
  }

  .flex-item {
    background-color: #1a6368;
    color: white;
    padding: 20px;
    margin: 6px;
    border-radius: 8px;
    font-weight: bold;
    text-align: center;
  }
playgroundTask: "Try adding one new child element '<div class=\"flex-item\">Item D</div>' inside '.flex-container'. Press 'Run' and observe how Item D automatically enters side by side without breaking the arrangement!"
---
Welcome to the core material! The number-one rule in Flexbox that you must engrave in your mind is:

> **Flexbox is a relationship between a Parent (*Flex Container*) and its Direct Children (*Flex Items*).**

You cannot simply apply Flexbox commands to child elements without first enabling flexible mode on their wrapping parent element.

```text
.flex-container (display: flex)
+---------------------------------------------+
|  +--------+   +--------+   +--------+       |
|  | Item A |   | Item B |   | Item C |       |  <-- Direct children (Flex Items)
|  +--------+   +--------+   +--------+       |
+---------------------------------------------+
```
## Step One: Opening the Gate with `display: flex`

To turn an ordinary element into a flexible container, simply give one magic line of CSS to its wrapper element:

```css
.wadah {
  display: flex;
}
```

Immediately after this line is applied, remarkable things happen to the **direct children** inside it:
1. **Lining Up Horizontally**: All direct children (which previously stacked downward because they were `block` type) automatically line up horizontally from left to right.
2. **Adapting to Content Width**: Child elements are no longer greedy taking 100% of the screen width. They shrink to fit the width of the text or content inside them (unless specified otherwise).
3. **Automatic Equal Height**: By default, all child elements will automatically stretch to match the height of the tallest child! The problem of matching column heights that used to torment web makers is now solved in one line.

## Critical Note: Only Affects Direct Children

Remember, Flexbox behavior is only passed down to **first-level children** (*direct children*):

```html
<div class="flex-container"> <!-- Flex Container -->
  <div class="flex-item">    <!-- Flex Item -->
    <p>Text inside</p>       <!-- NOT a Flex Item, remains ordinary text -->
  </div>
</div>
```

If you want the `<p>` element inside `.flex-item` to also become flexible, you must declare `display: flex` again on that `.flex-item` (*nested flexbox*).

Let's explore the property that controls flow direction in the next module!