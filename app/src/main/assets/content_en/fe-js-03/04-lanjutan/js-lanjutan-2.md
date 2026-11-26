---
id: js-lanjutan-2
courseId: fe-js-03
order: 22
title: "Modern Style: Concise Arrow Functions"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - Understand the arrow syntax (=>) introduced in the ES6 standard
  - Master writing standard arrow functions and the concise form (implicit return)
  - Know the advantages of arrow functions for writing cleaner data-processing code
codeLanguage: javascript
codeSnippet: |
  const kaliDua = (x) => x * 2;
  console.log(kaliDua(5)); // 10
starterJs: |
  // 1. Arrow function dengan blok kurung kurawal
  const hitungLuasPersegiPanjang = (panjang, lebar) => {
    return panjang * lebar;
  };

  // 2. Arrow function sebaris (implicit return: tanpa return dan tanpa kurung kurawal)
  const kuadrat = (angka) => angka * angka;

  console.log("Luas meja (5 x 3):", hitungLuasPersegiPanjang(5, 3));
  console.log("Kuadrat dari 7:", kuadrat(7));
playgroundTask: |
  Run the starter code and check the Console panel. Make sure the output is:
  Luas meja (5 x 3): 15
  Kuadrat dari 7: 49
---

# Modern Style: Concise Arrow Functions

In the ES6 (2015) standard, JavaScript introduced a very popular new way to write functions: **Arrow Functions**.

They are named this way because they use a fat arrow symbol **`=>`** (typed using an equals sign and a greater-than sign).

---

## Transforming from a Traditional Function

Watch how an addition function transforms:

### The Old Way (Traditional):
```javascript
function add(a, b) {
  return a + b;
}
```

### The Modern Way (Arrow Function):
```javascript
const add = (a, b) => {
  return a + b;
};
```

Basically, the `function` keyword is removed and replaced by the arrow symbol `=>` between the parameter parentheses and the curly braces `{}`.

---

## Superpower: Implicit Return (One-Liner)

The main advantage of arrow functions shines when your function body is very simple (just a single calculation line). You are allowed to **remove both the curly braces `{}` AND the `return` keyword**:

```javascript
// Very concise and elegant!
const multiplyByThree = (num) => num * 3;

console.log(multiplyByThree(4)); // Output: 12
```

When you omit the curly braces, JavaScript automatically returns the result of that expression (*implicit return*).

---

## When Should You Use Arrow Functions?
1. **For short data-processing functions**: Ideal alongside modern array methods like `.map()` and `.filter()`, which we will learn shortly.
2. **As action handler functions (*event callbacks*)**: Very convenient when handling button clicks on a web page.

In the next module, we will investigate where variables are allowed to live and be accessed through the concept of **Scope**!
