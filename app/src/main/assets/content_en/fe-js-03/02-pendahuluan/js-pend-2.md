---
id: js-pend-2
courseId: fe-js-03
order: 7
title: Linking JavaScript to an HTML Document
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Understand the function of the `<script>` element in embedding JavaScript logic into a web page
  - Differentiate between writing internal (inline) scripts and external script files (`.js`)
  - Understand the technical reasons for placing the `<script>` tag right before the closing `</body>` tag
codeLanguage: javascript
codeSnippet: |
  console.log("Script successfully loaded and executed!");
starterJs: |
  // This code executes alongside the HTML document on the side
  console.log("Script successfully loaded and executed!");
starterHtml: |
  <div style="font-family: sans-serif; padding: 20px; text-align: center;">
    <h2 style="color: #0F766E; margin-bottom: 8px;">First Web Page</h2>
    <p style="color: #475569; margin: 0;">JavaScript is monitoring this document via the Console.</p>
  </div>
playgroundTask: |
  Press the 'Run' button. Observe the HTML text that appears in the preview window, then open the Console panel at the bottom to ensure the message 'Script successfully loaded and executed!' is printed correctly.
---

# Linking JavaScript to an HTML Document

For JavaScript instructions to work and bring elements to life on a web page, the code must be connected to an HTML file.

HTML provides a special element called the `<script>` tag for this task.

---

## 1. Internal Script (Inline Script)

The simplest way is to write JavaScript code directly inside the `<script>` tag in your HTML document:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>JavaScript Exercise</title>
</head>
<body>
  <h1>Hello World</h1>

  <script>
    console.log("Message from internal script");
  </script>
</body>
</html>
```

This method is suitable for short, single-page experiments, but it is impractical for large projects because it mixes presentation code with programming logic.

---

## 2. External Script (Separate .js File)

The professional industry standard is to separate JavaScript code into its own file ending with `.js` (for example, `app.js` or `main.js`), and then link it using the `src` attribute:

```html
<!-- Linking an external JavaScript file -->
<script src="app.js"></script>
```

**Advantages of the external approach:**
- HTML code stays clean and readable.
- A single JavaScript file can be reused by many HTML pages at once.
- Browsers can cache the JavaScript file so pages load faster on subsequent visits.

---

## Where is the Best Position to Put the `<script>` Tag?

The golden rule for beginners: **place the `<script>` tag right at the very bottom before the closing `</body>` tag**.

```html
<body>
  <!-- 1. All visual elements are written here -->
  <header>...</header>
  <main>...</main>

  <!-- 2. The script is placed at the end before the body closes -->
  <script src="app.js"></script>
</body>
```

**Why?**
Browsers read HTML code from top to bottom. If you place the `<script>` inside the `<head>`, the browser will pause to download and execute the JavaScript before rendering buttons or text on the screen. As a result:
1. The user's screen looks blank/white for longer.
2. If the JavaScript code tries to find a button that the browser hasn't finished drawing yet, the program will throw an error (*element not found*).

---

## Practicing in the CodeAcademy Playground

In the CodeAcademy interactive editor, you don't need to type the `<script>` tag manually every time. The **JS** code tab you write is automatically injected by the system at the end of the preview **HTML** document.

Try pressing the **Run** button, observe the preview page on the side, and then open the **Console** panel to see the logged output of your code!
