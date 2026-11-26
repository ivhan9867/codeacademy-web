---
id: js-prep-3
courseId: fe-js-03
order: 3
title: Setting Up the Playground & Capturing console.log in the Console
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - Understand the anatomy of the interactive playground: code editor, live preview, and console panel
  - Know how to toggle the Console panel to view text output
  - Run your first JavaScript code and verify the output in the Console
codeLanguage: javascript
codeSnippet: |
  console.log("Halo, JavaScript!");
  console.log("Console siap digunakan.");
starterJs: |
  // Coba ubah pesan di bawah ini sesukamu
  console.log("Halo dari CodeAcademy!");
  console.log("Saya siap belajar JavaScript.");
playgroundTask: |
  Open the Console panel toggle at the bottom of the preview window, then click the 'Run' button. Make sure the Console displays:
  Halo dari CodeAcademy!
  Saya siap belajar JavaScript.
---

# Setting Up the Playground & Capturing console.log in the Console

In the earlier HTML and CSS courses, every code change appeared visually right away in the *live preview* screen. However, JavaScript logic often works behind the scenes without immediately changing colors or buttons.

So how can we see what our program is actually processing? The answer is through the **Console**.

---

## Anatomy of the CodeAcademy Playground

The playground in this app is built to make experimenting with code easy:

1. **Editor Area**: Where you read and type JavaScript code (`starterJs`).
2. **Preview Window (*Live Preview*)**: Displays visual results whenever we connect JavaScript to HTML/CSS elements.
3. **Console Panel (Toggle at Bottom)**: A dedicated panel that captures text messages from `console.log()`. If it isn't visible yet, tap the **Console** button at the bottom of the preview area. This panel can hold up to 100 lines of message history.
4. **Run & Reset Buttons**:
   - **Run**: Re-executes all the code currently in the editor.
   - **Reset**: Restores the exercise code back to its original state if something breaks and is hard to undo.

---

## Your First Command: console.log()

`console.log()` is the most popular command in the JavaScript world. Think of it as a mini megaphone that announces a variable's value or any message to the Console panel.

Take a look at this example:

```javascript
console.log("Belajar logika web");
console.log(2026);
```

Output in the Console panel:
```text
Belajar logika web
2026
```

Every time you call `console.log()`, the computer prints whatever message is inside the parentheses and automatically moves to a new line.

---

## Time to Practice!

Below this lesson is an interactive exercise editor:
1. Check the starter code in `starterJs`.
2. Make sure the **Console** panel below the preview window is open.
3. Click the **Run** button.
4. Watch the two lines of text appear in the Console.

Once both lines show up properly, congratulations! You're ready to move on to Section 2 to explore JavaScript syntax basics in depth.
