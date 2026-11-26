---
id: js-lanjutan-5
courseId: fe-js-03
order: 25
title: "Processing Modern Collections: forEach, map, and filter Methods"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Understand the advantages of modern array methods over manual for loops
  - Master the forEach method to run an action on every array element
  - Apply map to transform data and filter to select elements without modifying the source array
codeLanguage: javascript
codeSnippet: |
  const angka = [1, 2, 3];
  const kaliDua = angka.map((x) => x * 2);
  console.log(kaliDua); // [2, 4, 6]
starterJs: |
  const daftarNilai = [60, 75, 80, 95, 50];

  // 1. filter: Menyaring nilai yang memenuhi syarat kelulusan (>= 70)
  const nilaiLulus = daftarNilai.filter((nilai) => nilai >= 70);

  // 2. map: Mentransformasi setiap nilai dengan menambahkan bonus 5 poin
  const nilaiDenganBonus = daftarNilai.map((nilai) => nilai + 5);

  console.log("Nilai Lulus:", nilaiLulus.join(", "));
  console.log("Nilai + Bonus 5:", nilaiDenganBonus.join(", "));
playgroundTask: |
  Run the starter code and check the Console panel. Make sure the output is:
  Nilai Lulus: 75, 80, 95
  Nilai + Bonus 5: 65, 80, 85, 100, 55
---

# Processing Modern Collections: forEach, map, and filter Methods

In an earlier module, we used a `for (let i = 0; i < array.length; i++)` loop to walk through an array's items. That works, but it is fairly verbose and prone to semicolon typos.

Modern JavaScript provides built-in methods that are much more elegant and expressive. The three most important ones are **`forEach`**, **`map`**, and **`filter`**.

---

## 1. forEach(): Running an Action for Every Item

Use `.forEach()` when you simply want to perform a side action (such as printing data to the console) for each item in an array:

```javascript
const fruits = ["Apple", "Orange", "Melon"];

fruits.forEach((item, index) => {
  console.log(`Fruit #${index + 1}: ${item}`);
});
```

Output:
```text
Fruit #1: Apple
Fruit #2: Orange
Fruit #3: Melon
```

---

## 2. map(): Transforming Data

Use `.map()` when you want to **transform every element** into a new form.

This method returns a **new array** with the exact same length without changing the original array:

```javascript
const prices = [10000, 25000, 50000];

// Double all prices
const doubledPrices = prices.map((price) => price * 2);

console.log(doubledPrices);
// Output: [20000, 50000, 100000]
```

---

## 3. filter(): Selecting Matching Items

Use `.filter()` when you want to **pick a subset of items** that meet a certain condition.

The function inside `filter` must return a boolean (`true` or `false`). If `true`, the item is included in the new array; if `false`, the item is skipped:

```javascript
const numbers = [12, 5, 8, 130, 44];

// Only keep numbers greater than 10
const largeNumbers = numbers.filter((item) => item > 10);

console.log(largeNumbers);
// Output: [12, 130, 44]
```

---

## Summary Comparison of All Three

| Method | Main Purpose | Produces a New Value? |
|:---|:---|:---:|
| **`forEach()`** | Only executes an action (e.g. logging) | No (`undefined`) |
| **`map()`** | Transforms every item into a new item | Yes (New Array) |
| **`filter()`** | Filters items based on a true/false condition | Yes (New Array) |

Best of all, `.map()` and `.filter()` are **safe (*immutable*)**: the original data in your source array stays intact and untouched!

In the next module, we will connect JavaScript code to the real world: manipulating HTML elements on a web page using the **DOM**!
