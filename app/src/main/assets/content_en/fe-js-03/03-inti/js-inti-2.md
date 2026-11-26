---
id: js-inti-2
courseId: fe-js-03
order: 12
title: "JavaScript Primitive Data Types"
readingMinutes: 6
section: INTI
learningObjectives:
  - Learn the 5 main primitive data types: String, Number, Boolean, Undefined, and Null
  - Use the typeof operator to identify the data type of a value
  - Understand the fundamental difference between undefined (not yet assigned) and null (intentionally emptied)
codeLanguage: javascript
codeSnippet: |
  console.log(typeof "Halo"); // "string"
  console.log(typeof 42);     // "number"
  console.log(typeof true);   // "boolean"
starterJs: |
  const namaProduk = "Laptop Pro";
  const harga = 15000000;
  const stokTersedia = true;
  let voucherDiskon; // Variabel tanpa nilai awal

  console.log("Tipe namaProduk:", typeof namaProduk);
  console.log("Tipe harga:", typeof harga);
  console.log("Tipe stokTersedia:", typeof stokTersedia);
  console.log("Nilai voucherDiskon:", voucherDiskon);
playgroundTask: |
  Run the starter code and observe the Console panel. Make sure the output is:
  Tipe namaProduk: string
  Tipe harga: number
  Tipe stokTersedia: boolean
  Nilai voucherDiskon: undefined
---

# JavaScript Primitive Data Types

Just like in real life we distinguish between alphabet letters, paper money, and on/off light switches, computers also group data into specific categories called **Data Types**.

In JavaScript, the most fundamental kinds of data are grouped as **Primitive Data Types**. Let's learn the five most important ones!

---

## 1. String (Text)
Strings represent sequences of letters or words. Their hallmark is that they are always wrapped in double quotes (`"..."`), single quotes (`'...'`), or backticks (`` `...` ``):

```javascript
const name = "Siti Rahmawati";
const initials = 'SR';
```

---

## 2. Number
JavaScript does not separate whole numbers (*integers*) and decimal numbers (*floats*). Both belong to a single type: **Number**:

```javascript
const age = 24;
const weight = 55.5;
const negativeTemp = -4;
```

---

## 3. Boolean (Binary Truth)
This data type is very simple because it only has two possible values: **`true`** or **`false`**. It is commonly used as a condition switch:

```javascript
const isLoggedIn = true;
const hasVipAccess = false;
```

---

## 4. Undefined vs 5. Null (Two Kinds of "Empty")

Beginners are often confused about the difference between `undefined` and `null`. Both look similar, yet they have different philosophical meanings:

- **`undefined`**: The variable box has been created, but the computer **has not been told what is inside**. This is the default value of an uninitialized variable:
  ```javascript
  let homeAddress;
  console.log(homeAddress); // undefined
  ```

- **`null`**: The programmer **intentionally** filled the box with an "empty" or "nothing" value:
  ```javascript
  let profilePhoto = null; // The user intentionally hasn't uploaded a photo yet
  console.log(profilePhoto); // null
  ```

> **Simple Analogy:**
> - `undefined`: A parcel box arrives at your house having never had anything placed inside it by the sender.
> - `null`: The sender intentionally put a slip of paper inside saying *"This box is intentionally left empty"*.

---

## Checking Data Types with typeof

If you are unsure what type of data is inside a variable, use the built-in **`typeof`** operator:

```javascript
const age = 25;
console.log(typeof age); // prints "number"

const fullName = "Vhan";
console.log(typeof fullName); // prints "string"
```

Try running the code editor below to see how `typeof` automatically detects different kinds of data!
