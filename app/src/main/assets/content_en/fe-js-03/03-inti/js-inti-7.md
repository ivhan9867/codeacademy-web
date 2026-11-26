---
id: js-inti-7
courseId: fe-js-03
order: 17
title: "Repeating Tasks Automatically: for and while Loops"
readingMinutes: 6
section: INTI
learningObjectives:
  - Understand the benefit of loops for automating repetitive tasks
  - Master the three parts of a for loop: initialization, stop condition, and step update
  - Understand how while loops work and how to avoid infinite loops
codeLanguage: javascript
codeSnippet: |
  for (let i = 1; i <= 3; i++) {
    console.log("Putaran ke-" + i);
  }
starterJs: |
  // Menghitung langkah menggunakan perulangan for
  console.log("Mulai hitungan:");

  for (let i = 1; i <= 3; i++) {
    console.log(`Langkah ke-${i}`);
  }

  console.log("Selesai!");
playgroundTask: |
  Run the starter code and check the Console panel. Make sure the output printed is:
  Mulai hitungan:
  Langkah ke-1
  Langkah ke-2
  Langkah ke-3
  Selesai!
---

# Repeating Tasks Automatically: for and while Loops

Imagine you are asked to print numbers 1 through 1,000 to the screen. Typing `console.log(1)`, `console.log(2)`, all the way to a thousand lines would waste time and be extremely tedious.

Computers were built to complete repetitive tasks like this in milliseconds. This capability is called **Looping**.

---

## 1. The for Loop (Most Popular)

Use a `for` loop when you **already know exactly how many times** a task should repeat.

The `for` syntax has three parts inside its parentheses:

```javascript
for (let i = 1; i <= 3; i++) {
  console.log(`Step #${i}`);
}
```

Output:
```text
Step #1
Step #2
Step #3
```

### Breaking Down the Three Parts of `for`:
1. **Initialization (`let i = 1;`)**: Sets up a counter variable. This part runs **only once** when the loop first begins.
2. **Test Condition (`i <= 3;`)**: Checked before each iteration starts. If `true`, the code inside `{}` runs. If `false`, the loop stops immediately.
3. **Step Update (`i++`)**: Runs after each iteration finishes. `i++` is shorthand for `i = i + 1`, increasing the counter value.

---

## 2. The while Loop

Use `while` when a loop must keep running **as long as a condition remains true**, and you might not know in advance on which iteration it will stop:

```javascript
let energy = 3;

while (energy > 0) {
  console.log(`Battery remaining: ${energy}`);
  energy--; // Decreases energy by 1
}

console.log("Battery empty!");
```

Output:
```text
Battery remaining: 3
Battery remaining: 2
Battery remaining: 1
Battery empty!
```

---

## Danger of an "Infinite Loop"

Look at the `energy--;` line in the `while` example above. If that line is deleted:
- The value of `energy` will stay `3` forever.
- The condition `energy > 0` will always be `true`.
- The computer will keep looping endlessly until memory runs out or the browser freezes.

**Tip:** Always make sure there is an instruction inside the loop that eventually causes the stop condition to be met!

In the next module, we will learn about a special container for storing multiple pieces of data at once: **Arrays**.
