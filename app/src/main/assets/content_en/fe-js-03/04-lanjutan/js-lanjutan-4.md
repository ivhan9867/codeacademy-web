---
id: js-lanjutan-4
courseId: fe-js-03
order: 24
title: "Describing Real-World Entities: Basic JavaScript Objects"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Understand the Object data structure as a representation of real-world entities in code
  - Master creating key-value pairs inside curly braces
  - Access and modify object properties using dot notation and bracket notation
codeLanguage: javascript
codeSnippet: |
  const buku = {
    judul: "Belajar JavaScript",
    halaman: 250
  };
  console.log(buku.judul); // "Belajar JavaScript"
starterJs: |
  // 1. Mendefinisikan objek profil
  const profilPengguna = {
    nama: "Siti Rahmawati",
    keahlian: "Front-End Developer",
    usia: 24,
    aktif: true
  };

  // 2. Menambahkan properti baru ke objek
  profilPengguna.kota = "Bandung";

  console.log("Nama:", profilPengguna.nama);
  console.log("Keahlian:", profilPengguna.keahlian);
  console.log("Kota:", profilPengguna["kota"]);
playgroundTask: |
  Run the starter code and observe the Console panel. Make sure the generated output is:
  Nama: Siti Rahmawati
  Keahlian: Front-End Developer
  Kota: Bandung
---

# Describing Real-World Entities: Basic JavaScript Objects

Arrays are great for storing lists of similar items (such as a list of numbers `[1, 2, 3]` or color names). However, what if you want to describe one person completely?
- What is their name?
- How old are they?
- What city do they live in?
- Is their account active?

If you used an array `["Siti", 24, "Bandung", true]`, you would have to remember that index 0 is name, index 1 is age, and so on. That gets very confusing!

The best solution for modeling real-world things is an **Object**.

---

## Anatomy of a JavaScript Object

An object is created using curly braces **`{ ... }`** and stores data as **Key-Value Pairs**:

```javascript
const car = {
  brand: "Toyota",
  model: "Avanza",
  year: 2024,
  color: "White",
  running: true
};
```

Each line above is called a **Property**:
- `brand` is the **key**.
- `"Toyota"` is the **value**.

---

## How to Access Object Properties

There are two main ways to retrieve values from an object:

### 1. Dot Notation (Most Commonly Used)
Simply place a dot (`.`) between the object name and its property name:
```javascript
console.log(car.brand); // "Toyota"
console.log(car.year);  // 2024
```

### 2. Bracket Notation (Square Brackets with Text)
Use square brackets and wrap the key name in quotes:
```javascript
console.log(car["color"]); // "White"
```
This approach is very useful when the key name is stored inside another variable.

---

## Updating & Adding New Properties

You can update existing property values or add new properties very intuitively:

```javascript
const user = { name: "Andi", score: 80 };

// Updating an existing property
user.score = 95;

// Adding a new property
user.email = "andi@example.com";

console.log(user);
// { name: "Andi", score: 95, email: "andi@example.com" }
```

> **Interesting Question:** Why are we allowed to change `user.score` even though the variable was declared with `const`?
> Because the `const` keyword only locks the outer object container so it cannot be replaced by a different object. You are still free to organize and modify the contents inside that object!

In the next module, we will combine the power of Arrays and Functions by learning **Modern Array Methods: forEach, map, and filter**!
