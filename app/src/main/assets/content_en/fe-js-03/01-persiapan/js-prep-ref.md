---
id: js-prep-ref
courseId: fe-js-03
order: 5
title: "Further References & JavaScript Documentation"
readingMinutes: 4
section: PERSIAPAN
learningObjectives:
  - Discover trusted JavaScript documentation sources used by professional programmers
  - Choose further learning references that match your personal learning style
  - Read Console error messages and Stack Traces to speed up debugging
---

# Further References & JavaScript Documentation

This course is designed to help you grasp the foundations of JavaScript logic step by step. In real-world web development, however, professional programmers never stop checking documentation—not because they forgot everything, but because there are always details worth double-checking.

Here are three of the best long-term references to keep handy, plus a quick guide to reading error traces in the Console.

---

## 1. Three Essential JavaScript References

| Learning Resource | Main Focus | Best For |
| :--- | :--- | :--- |
| **MDN Web Docs (JavaScript Guide)** | The most complete web standard documentation: syntax details, method parameters, browser compatibility, plus DOM & Web API specs. | When you forget a method's argument order (like `.slice()`) or need to verify official browser behavior. |
| **JavaScript.info** | Structured tutorials from basics to advanced topics (*closures*, *promises*, *event loop*, DOM) with visual explanations and practical examples. | When you want to dive deep into the technical reasons behind how JavaScript works. |
| **Eloquent JavaScript** | A free digital book by Marijn Haverbeke that trains algorithmic thinking through case studies and challenging practice projects. | When you're comfortable with basic syntax and want to sharpen your problem-solving logic. |

> **Quick Search Tip:** When searching on a search engine, add the keyword **`mdn`** at the end—for example `"array find mdn"` or `"localStorage mdn"`. You'll land straight on the most accurate documentation page.

---

## 2. How to Read a Console Stack Trace When an Error Happens

When your code fails to run and red text appears in the Console, don't guess blindly. The browser actually gives you an error location map called a **Stack Trace**.

Look at this example error message:

```text
Uncaught TypeError: Cannot read properties of undefined (reading 'toUpperCase')
    at formatNama (app.js:12:24)
    at tampilkanProfil (app.js:18:5)
    at app.js:22:1
```

Let's break that message down into 3 important parts:

```text
+-------------------------------------------------------------------+
| 1. ERROR TYPE & MESSAGE                                           |
|    TypeError: Cannot read properties of undefined                 |
|    -> Meaning: A variable holding undefined was accessed as if    |
|       it had a property/method (.toUpperCase).                    |
+-------------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------------+
| 2. ORIGIN OF THE PROBLEM (Top line in the Stack Trace)            |
|    at formatNama (app.js:12:24)                                   |
|    -> Meaning: The error blew up in function formatNama, file     |
|       app.js, line 12, character (column) 24.                     |
+-------------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------------+
| 3. CALL PATH (Reverse order back to the initial caller)           |
|    at tampilkanProfil (app.js:18:5)  <-- called formatNama        |
|    at app.js:22:1                    <-- execution entry point    |
+-------------------------------------------------------------------+
```

### Practical Steps When You Hit a Console Error

1. **Read the error type first**:
   - `SyntaxError`: Usually a missing parenthesis `)`, curly brace `}`, comma `,`, or quote `"`.
   - `ReferenceError`: A misspelled variable name or a variable used before it was declared.
   - `TypeError`: A variable turned out to be `null` or `undefined` when you accessed `.property` or called `function()`, or you tried to reassign a `const` variable.
2. **Focus on the first line that mentions your own code**:
   Ignore lines from third-party libraries if any appear; check the line number (`:12`) in your own file or editor.
3. **Insert a `console.log()` one line before the error**:
   Inspect the variable's contents right before the failing line to verify whether it really holds what you expect or is actually `undefined`.

By getting into the habit of reading *stack traces* calmly, you'll save tons of time when building larger web applications!
