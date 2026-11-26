---
id: js-inti-10
courseId: fe-js-03
order: 20
title: "Essential Array Methods: push, pop, slice, includes, & find"
readingMinutes: 7
section: INTI
learningObjectives:
  - Distinguish between array methods that modify the original data (mutating) and those that leave the original data intact (non-mutating)
  - Use push(), pop(), slice(), includes(), and find() appropriately for real-world cases
  - Avoid common beginner mistakes when reading the return values of each array method
codeLanguage: javascript
codeSnippet: |
  const antrean = ["Andi", "Bima", "Citra"];
  const adaBima = antrean.includes("Bima");
  const hurufC = antrean.find((nama) => nama.startsWith("C"));

  console.log(adaBima); // true
  console.log(hurufC);  // "Citra"
starterJs: |
  const daftarProduk = [
    { id: 1, nama: "Keyboard", stok: 5 },
    { id: 2, nama: "Mouse", stok: 0 },
    { id: 3, nama: "Monitor", stok: 3 }
  ];

  // 1. Cek keberadaan tag kategori dengan .includes()
  const kategori = ["elektronik", "kantor", "gaming"];
  const adaKategoriGaming = kategori.includes("gaming");
  console.log("Kategori gaming tersedia?", adaKategoriGaming);

  // 2. Cari satu objek produk yang cocok dengan .find()
  const produkKosong = daftarProduk.find((item) => item.stok === 0);
  console.log("Produk stok habis:", produkKosong.nama);

  // 3. Ambil potongan array tanpa merusak array asli dengan .slice()
  const duaProdukPertama = daftarProduk.slice(0, 2);
  console.log("Jumlah potongan:", duaProdukPertama.length);
  console.log("Jumlah asli tetap:", daftarProduk.length);
playgroundTask: |
  Run the starter code and check the Console. Make sure the output displays:
  Kategori gaming tersedia? true
  Produk stok habis: Mouse
  Jumlah potongan: 2
  Jumlah asli tetap: 3
verificationRules:
  - "js:.find("
  - "js:.includes("
---

# Essential Array Methods: push, pop, slice, includes, & find

In the previous module you learned `push()` to add items into an array. In real web applications, we don't just add data—we also need to remove the last item, copy part of a list, check whether an item exists, and search for specific data.

Before using an array method, there is one important rule you must understand: **does the method modify the original array (*mutating*) or leave the original array intact (*non-mutating*)?**

---

## Comparison of Mutating vs Non-Mutating Methods

| Method | Modifies Original Array? | Return Value | Main Purpose |
| :--- | :---: | :--- | :--- |
| **`push(item)`** | **Yes (Mutating)** | Updated array `length` | Adds one or more items to the end of the array |
| **`pop()`** | **Yes (Mutating)** | The last element that was removed | Retrieves and removes the very last item |
| **`slice(start, end)`** | **No (Non-Mutating)** | A new array containing the sliced elements | Copies part of an array without altering the source array |
| **`includes(value)`** | **No (Non-Mutating)** | Boolean (`true` or `false`) | Checks whether a primitive value exists inside the array |
| **`find(callback)`** | **No (Non-Mutating)** | The **first** matching element (or `undefined`) | Searches for a specific item (great for arrays of objects) |

---

## 1. Modifying the End of an Array: `push()` and `pop()`

`push()` and `pop()` operate on the very end of an array:

```javascript
const history = ["Home Page", "Products"];
const newLength = history.push("Cart");

console.log(history);   // ["Home Page", "Products", "Cart"]
console.log(newLength); // 3

const lastPage = history.pop();
console.log(lastPage); // "Cart"
console.log(history);  // ["Home Page", "Products"]
```

---

## 2. Copying a Portion Safely: `slice()`

`slice(start, end)` copies elements starting from the `start` index up to **before** the `end` index (the `end` index is not included):

```javascript
const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
const earlyWeek = days.slice(0, 3);

console.log(earlyWeek); // ["Monday", "Tuesday", "Wednesday"]
console.log(days);      // Still 5 days (unchanged!)
```

If called with no arguments (`days.slice()`), you get a complete shallow copy of the array that is separate from the original array.

---

## 3. Checking & Searching Data: `includes()` and `find()`

### `.includes()` to Check if a Simple Value Exists
Use `includes()` when you only need a "yes or no" answer:

```javascript
const allowedRoles = ["admin", "editor"];
console.log(allowedRoles.includes("editor")); // true
console.log(allowedRoles.includes("guest"));  // false
```

### `.find()` to Search for an Item with Specific Criteria
If an array contains objects or you need more complex search conditions, use `find()`:

```javascript
const users = [
  { id: 101, name: "Sari" },
  { id: 102, name: "Dimas" }
];

const target = users.find((user) => user.id === 102);
console.log(target); // { id: 102, name: "Dimas" }
```

---

## Common Beginner Mistakes to Avoid

1. **Assuming `push()` returns a new array**:
   `const updated = arr.push("X");` makes `updated` hold the array length number (for example `4`), not the array itself! Call `arr.push("X")` as its own statement.
2. **Forgetting that the end index of `slice(start, end)` is exclusive**:
   `arr.slice(1, 3)` only grabs indices `1` and `2`—index `3` is not included.
3. **Accessing properties on the result of `find()` without checking first**:
   If no match is found, `find()` returns `undefined`. Always check with `if (result)` or optional chaining (`result?.name`) before reading its properties so you don't trigger a `TypeError`.

Run the playground below to try `.includes()`, `.find()`, and `.slice()` firsthand!
