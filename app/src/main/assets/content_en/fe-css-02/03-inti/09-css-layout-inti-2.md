---
id: css-layout-inti-2
courseId: fe-css-02
order: 11
title: "Flow Direction: Controlling flex-direction"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Understand the concept of Main Axis and Cross Axis"
  - "Master the 4 values of the flex-direction property: row, row-reverse, column, column-reverse"
  - "Know how switching axes affects the alignment direction of child elements"
codeLanguage: css
codeSnippet: |
  .wadah-flex {
    display: flex;
    flex-direction: column; /* Mengubah arah sumbu ke bawah */
    gap: 12px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji flex-direction</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Eksperimen Arah Flexbox</h2>
      <div class="wadah">
        <div class="kartu kartu-1">Langkah 1</div>
        <div class="kartu kartu-2">Langkah 2</div>
        <div class="kartu kartu-3">Langkah 3</div>
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
    flex-direction: row; /* Nilai default: kiri ke kanan */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    padding: 16px 24px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    margin: 4px;
    text-align: center;
  }

  .kartu-1 { background-color: #1a6368; }
  .kartu-2 { background-color: #df7b56; }
  .kartu-3 { background-color: #d49438; }
playgroundTask: "Try changing 'flex-direction: row;' in the '.wadah' selector to 'flex-direction: column;'. Run the code and notice how Langkah 1, 2, and 3 now line up downward like a vertical menu! Afterward, try changing it to 'row-reverse' to reverse the step order."
---
To master Flexbox, there is one secret key you must always picture in your head: **Main Axis and Cross Axis**.

The direction of these axes is controlled by the **`flex-direction`** property.

```text
flex-direction: row                flex-direction: column
  Main Axis ------------>            Cross Axis -------->
  +------+ +------+ +------+        | +--------+
  |  1   | |  2   | |  3   |        v |   1    |
  +------+ +------+ +------+      Main+--------+
  | Cross Axis                    Axis|   2    |
  v                                   +--------+
```
## Two Axes That Rotate Together

1. **Main Axis**:
   The primary direction along which flex elements flow and line up.
2. **Cross Axis**:
   The line perpendicular (90 degrees) to the main axis.

When you change the value of `flex-direction`, you are actually rotating the direction of the main axis:

### 1. `flex-direction: row` (Default)
- **Main Axis**: Horizontal (across from left to right).
- **Cross Axis**: Vertical (perpendicular from top to bottom).
- Child elements line up neatly like a string of words in a sentence.

### 2. `flex-direction: row-reverse`
- **Main Axis**: Reversed horizontal (from right to left).
- Child elements have their display order reversed: the first element sits on the far right, and the last element sits on the left.

### 3. `flex-direction: column`
- **Main Axis**: Vertical (downward from top to bottom).
- **Cross Axis**: Horizontal (perpendicular from left to right).
- Child elements flow downward like form cards or a vertical menu on a phone screen.

### 4. `flex-direction: column-reverse`
- **Main Axis**: Reversed vertical (flowing from bottom to top).
- The first element sits at the bottom of the container, and subsequent elements stack upward above it. Very often used for *chat bubble* interfaces or message logs.

Keep this axis direction firmly in mind, because in the next module, our alignment property (`justify-content`) will always operate **along the Main Axis**, regardless of which direction it points!