---
id: js-pend-3
courseId: fe-js-03
order: 8
title: "First Window: Displaying Output with console.log()"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Understand the anatomy of console.log() for inspecting program data
  - Know how to print strings, numbers, and multiple values at once in a single call
  - Use the Console as a primary tool for monitoring variable values directly
codeLanguage: javascript
codeSnippet: |
  console.log("Nama:", "Vhan");
  console.log("Usia:", 20);
  console.log("Status:", true);
starterJs: |
  // Eksperimen mencetak data diri kamu
  const nama = "Vhan";
  const modulSelesai = 6;

  console.log("Halo, nama saya", nama);
  console.log("Jumlah modul yang sudah saya pelajari:", modulSelesai);
playgroundTask: |
  Run the code and open the Console panel at the bottom of the preview. Make sure the Console displays two lines:
  Halo, nama saya Vhan
  Jumlah modul yang sudah saya pelajari: 6
---

# First Window: Displaying Output with console.log()

When building web applications, you can't always rely on visual changes on screen to tell what is happening inside the computer's memory.

Often we need to check: *"Was the form data saved?"*, *"What is the result of this multiplication?"*, or *"Did this button actually get clicked?"*. That is where the **`console.log()`** function becomes a programmer's best friend.

---

## Breaking Down console.log() Syntax

This syntax has two parts:
- `console`: A built-in browser object representing the *developer console*.
- `.log(...)`: A method or action that prints whatever information you put inside the parentheses onto the console screen.

### 1. Printing Text (String)
Text must always be wrapped in double quotes (`"..."`) or single quotes (`'...'`):

```javascript
console.log("Selamat pagi, Web Developer!");
```

Output:
```text
Selamat pagi, Web Developer!
```

### 2. Printing Numbers & Math Operations
Numbers are written directly without quotes. If you put a math expression inside, JavaScript solves it first before printing the result:

```javascript
console.log(100);
console.log(15 + 25);
```

Output:
```text
100
40
```

> **Watch out:** Do not wrap numbers in quotes if you want to calculate them. `console.log("15 + 25")` prints the literal text `"15 + 25"`, not the number `40`.

### 3. Printing Multiple Values at Once
You can pass several values into a single `console.log()` command by separating them with commas (`,`). JavaScript automatically inserts a space between each value:

```javascript
console.log("Skor pemain:", 95, "Poin");
```

Output:
```text
Skor pemain: 95 Poin
```

---

## Time to Practice!

Look at the code in the editor below:
1. It prints strings and a number using comma separators.
2. Click the **Run** button.
3. Open the **Console** panel and verify both output lines.
4. Feel free to change `"Vhan"` to your own name, then click **Run** again to see the update instantly!
