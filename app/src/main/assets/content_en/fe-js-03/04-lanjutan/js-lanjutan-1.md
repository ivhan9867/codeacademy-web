---
id: js-lanjutan-1
courseId: fe-js-03
order: 21
title: "Reusable Recipes: Function Declarations & Parameters"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Understand the concept of a function as a self-contained block of instructions that can be reused repeatedly
  - Distinguish between parameters (placeholders in the function) and arguments (actual values when called)
  - Master the role of the return keyword in sending back processed results
codeLanguage: javascript
codeSnippet: |
  function sapa(nama) {
    return `Halo, ${nama}!`;
  }
  console.log(sapa("Dina")); // "Halo, Dina!"
starterJs: |
  // 1. Mendefinisikan resep fungsi perhitungan diskon
  function hitungDiskon(hargaAsli, persenDiskon) {
    const potongan = (hargaAsli * persenDiskon) / 100;
    return hargaAsli - potongan;
  }

  // 2. Memanggil fungsi berulang kali dengan data berbeda
  const hargaBaju = hitungDiskon(100000, 20);
  const hargaSepatu = hitungDiskon(300000, 10);

  console.log("Harga baju setelah diskon 20%: Rp" + hargaBaju);
  console.log("Harga sepatu setelah diskon 10%: Rp" + hargaSepatu);
playgroundTask: |
  Run the starter code and check the Console panel. Make sure the output printed is:
  Harga baju setelah diskon 20%: Rp80000
  Harga sepatu setelah diskon 10%: Rp270000
---

# Reusable Recipes: Function Declarations & Parameters

Have you ever heard the important programming principle **DRY (*Don't Repeat Yourself*)**? It means: don't write the same logic over and over again.

If you need to calculate a price discount in 5 different places, copying the same calculation formula 5 times is bad practice. If the discount formula changes, you'd have to hunt down and fix all five places.

The solution is to wrap that logic inside a **Function**.

---

## The Juicer & Recipe Analogy

Imagine a juice blender machine:
1. **Input Ingredients (*Parameters*)**: You put in oranges and a spoonful of sugar.
2. **Processing**: The machine spins its blades and squeezes out the juice.
3. **Finished Output (*Return*)**: You get a glass of fresh orange juice.

In JavaScript, a function is declared using the **`function`** keyword:

```javascript
// Defining the function
function createGreeting(name) {
  return `Good morning, ${name}!`;
}

// Calling the function
const message1 = createGreeting("Rina");
const message2 = createGreeting("Budi");

console.log(message1);
console.log(message2);
```

Output:
```text
Good morning, Rina!
Good morning, Budi!
```

---

## Parameters vs Arguments: What's the Difference?

These two terms often get mixed up, yet the difference is very simple:
- **Parameter**: The placeholder variable name written when you **design** the function. In the example above, `name` is a parameter.
- **Argument**: The actual value you pass in when **calling** the function. In the example above, `"Rina"` and `"Budi"` are arguments.

---

## The Power of the return Keyword

The **`return`** keyword sends the result of the function's work back to the line of code that called it:

```javascript
function multiply(a, b) {
  return a * b;
  console.log("This text will never run!");
}

const result = multiply(4, 5);
console.log(result); // 20
```

> **Heads Up:** As soon as a `return` instruction runs, the function **immediately stops and exits**. Any lines of code placed right below `return` inside the same curly brace block are ignored by the computer.

In the next module, we will learn a modern, more concise function writing style that web developers love: **Arrow Functions**!
