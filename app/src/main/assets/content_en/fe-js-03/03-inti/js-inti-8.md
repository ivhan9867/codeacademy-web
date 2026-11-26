---
id: js-inti-8
courseId: fe-js-03
order: 18
title: "Numbered Shelves: Storing Collections with Basic Arrays"
readingMinutes: 6
section: INTI
learningObjectives:
  - Understand the Array data structure as a container for storing a list of values in one variable
  - Understand the concept of zero-based indexing when reading or modifying data
  - Use the length property and push() method to manage array size
codeLanguage: javascript
codeSnippet: |
  const buah = ["Apel", "Jeruk", "Mangga"];
  console.log(buah[0]);      // "Apel"
  console.log(buah.length);  // 3
starterJs: |
  const daftarBelanja = ["Buku", "Pensil", "Penggaris"];

  // Menambahkan barang baru di urutan paling belakang
  daftarBelanja.push("Penghapus");

  console.log("Item pertama:", daftarBelanja[0]);
  console.log("Jumlah seluruh item:", daftarBelanja.length);
  console.log("Daftar lengkap:", daftarBelanja.join(", "));
playgroundTask: |
  Run the starter code. Make sure the Console displays:
  Item pertama: Buku
  Jumlah seluruh item: 4
  Daftar lengkap: Buku, Pensil, Penggaris, Penghapus
---

# Numbered Shelves: Storing Collections with Basic Arrays

Imagine you have a list of 10 students in a class. If you create a separate variable for every student:
```javascript
const student1 = "Budi";
const student2 = "Siti";
const student3 = "Rani";
// ... up to student10
```
This code is very awkward to manage. What if the number of students grows to 100?

To store a collection of many items under a single variable name, JavaScript provides a special container called an **Array**.

```text
const fruits = ["Apple", "Orange", "Mango", "Banana"];
Index :            0         1        2        3
Length: 4 (last index = fruits.length - 1)
```
---

## The Numbered Shelf Analogy

Imagine a shoe rack cabinet with sequentially numbered compartments:
- In compartment number 0, black shoes are stored.
- In compartment number 1, running shoes are stored.
- In compartment number 2, sandals are stored.

In JavaScript, an array is written using square brackets **`[...]`** and each item is separated by a comma:

```javascript
const studentNames = ["Budi", "Siti", "Rani"];
```

---

## Golden Rule: Indices Start at 0 (Zero-Based Index)

One of the most important things for beginners to remember: **computers always start counting from 0, not 1**.

To retrieve data from an array, write the array name followed by its index number inside square brackets:

```javascript
const hobbies = ["Reading", "Coding", "Cycling"];

console.log(hobbies[0]); // First element: "Reading"
console.log(hobbies[1]); // Second element: "Coding"
console.log(hobbies[2]); // Third element: "Cycling"
```

Output:
```text
Reading
Coding
Cycling
```

---

## Two Essential Array Features

### 1. Finding the Item Count (`.length`)
Use the `.length` property to find out how many total items are inside an array:
```javascript
const colors = ["Red", "Yellow", "Green"];
console.log(colors.length); // 3
```

### 2. Adding a New Item (`.push()`)
Use the `.push()` method to append new data to the very end:
```javascript
const tasks = ["Learn HTML", "Learn CSS"];
tasks.push("Learn JavaScript");

console.log(tasks); // ["Learn HTML", "Learn CSS", "Learn JavaScript"]
console.log(tasks.length); // 3
```

Congratulations! You have finished all of **Part 3: Program Logic & Flow**. In the next section, we will jump into more exciting topics: crafting functions, dissecting objects, and bringing web pages to life through DOM manipulation!
