---
id: js-inti-1
courseId: fe-js-03
order: 11
title: "Labeled Boxes: Storing Data with let and const"
readingMinutes: 5
section: INTI
learningObjectives:
  - Understand the analogy of a variable as a labeled container for storing values in computer memory
  - Distinguish between using the const keyword (constant value) and let (dynamic value)
  - Understand why the old var keyword is left behind for modern code safety
codeLanguage: javascript
codeSnippet: |
  const namaSitus = "CodeAcademy";
  let poin = 100;
  poin = poin + 25;

  console.log(namaSitus); // "CodeAcademy"
  console.log(poin);      // 125
starterJs: |
  // 1. Nilai tetap yang tidak akan berubah
  const namaAplikasi = "CodeAcademy";

  // 2. Nilai yang nilainya dapat bertambah
  let skorPengguna = 80;
  skorPengguna = skorPengguna + 15;

  console.log("Aplikasi:", namaAplikasi);
  console.log("Skor Akhir:", skorPengguna);
playgroundTask: |
  Run the starter code and check the Console panel. Make sure the output shown is:
  Aplikasi: CodeAcademy
  Skor Akhir: 95
  Notice how the skorPengguna variable can have its value changed because it was declared with 'let'.
---

# Labeled Boxes: Storing Data with let and const

When you browse the internet, web applications need to remember many things: what your account name is, how many items are in your shopping cart, or whether you have pressed the *like* button.

To remember that information, the computer provides storage slots in memory called **Variables**.

```text
Computer Memory:
  +------------------+      +------------------+
  | label: siteName  |      | label: points    |
  | "CodeAcademy"    |      | 100 -> 125       |
  +------------------+      +------------------+
      (const: fixed)           (let: dynamic)
```
---

## The Labeled Storage Box Analogy

Imagine you have many cardboard boxes in your room:
1. You stick a name label on the outside of a box: for example, the label **`score`**.
2. You put an item inside it: for example, the number **`100`**.
3. Whenever you need that number, you simply call the box's label name: `console.log(score)`.

In modern JavaScript (ES6+), there are two main keywords for creating these boxes: **`const`** and **`let`**.

---

## 1. const (Permanently Sealed Box)

`const` is short for *constant* (fixed). Use `const` to store data whose **value must not change** throughout the program:

```javascript
const pi = 3.14159;
const homeCountry = "Indonesia";

console.log(homeCountry);
```

Output:
```text
Indonesia
```

If you try to overwrite a `const` value:
```javascript
const role = "Admin";
role = "User"; // TypeError: Assignment to constant variable.
```
The computer will reject it and throw an error to prevent important data from being altered accidentally.

---

## 2. let (Box Whose Contents Can Be Replaced)

Use `let` when the data inside the box **will indeed change or be updated** as the user interacts with your app:

```javascript
let playerLives = 3;
console.log("Start:", playerLives);

// Player hits an obstacle
playerLives = playerLives - 1;
console.log("After hitting an obstacle:", playerLives);
```

Output:
```text
Start: 3
After hitting an obstacle: 2
```

> **Note:** When updating the value of `playerLives`, you **do not** need to type `let` in front of it again. You only type `let` once when first creating (*declaring*) the box.

---

## Why Don't We Use var Anymore?

Before 2015, JavaScript only had the `var` keyword. However, `var` had serious flaws: its variables could leak out of code blocks (*scope*) and be overwritten without warning, often causing hidden bugs in large-scale applications.

**Industry Best Practice Rule:**
> *"Use **`const` by default**. Only if a value genuinely needs to be updated later (like a counter or score), use **`let`**. Avoid using `var`."*

In the next module, we will explore what kinds of items (*data types*) can be placed inside these variable boxes!
