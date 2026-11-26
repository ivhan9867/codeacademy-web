---
id: js-inti-3
courseId: fe-js-03
order: 13
title: "Math, Comparison, & Logical Operators"
readingMinutes: 6
section: INTI
learningObjectives:
  - Master standard arithmetic operators including remainder (modulo %)
  - Understand the importance of using strict equality (===) over loose equality (==)
  - Apply logical operators AND (&&), OR (||), and NOT (!) to build compound conditions
codeLanguage: javascript
codeSnippet: |
  console.log(10 % 3);       // 1 (sisa bagi)
  console.log(5 === "5");    // false (tipe berbeda)
  console.log(true && false);// false
starterJs: |
  const hargaBuku = 50000;
  const uangSaku = 75000;
  const punyaKupon = true;

  // Evaluasi kondisi
  const cukupUang = uangSaku >= hargaBuku;
  const bolehBeli = cukupUang && punyaKupon;

  console.log("Cukup uang?", cukupUang);
  console.log("Boleh beli?", bolehBeli);
  console.log("Sisa uang jika dibeli:", uangSaku - hargaBuku);
playgroundTask: |
  Run the starter code. Make sure the Console prints exact 3 lines:
  Cukup uang? true
  Boleh beli? true
  Sisa uang jika dibeli: 25000
---

# Math, Comparison, & Logical Operators

After storing data into variables, you need tools to process and compare that data. These tools are called **operators**.

In JavaScript, operators are divided into three main groups: math, comparison, and logic.

---

## 1. Math (Arithmetic) Operators

Math operators are used to perform basic numerical calculations:

- `+` (Addition)
- `-` (Subtraction)
- `*` (Multiplication)
- `/` (Division)
- **`%` (Modulo / Remainder)**: Calculates the remainder left over after whole-number division finishes.

### Math Code Example

```javascript
const itemPrice = 25000;
const quantity = 3;
const total = itemPrice * quantity;
const remainder = 10 % 3;

console.log("Total:", total);
console.log("Remainder of 10 % 3:", remainder);
```

Output:
```text
Total: 75000
Remainder of 10 % 3: 1
```

> **Modulo Note:** `10` divided by `3` gives `3`, with a remainder of `1`. Programmers frequently use modulo to check for even numbers (`number % 2 === 0`).

---

## 2. Comparison Operators

Comparison operators compare two values. The result of a comparison is always a **Boolean** value, either `true` or `false`:

- `>` (Greater than)
- `<` (Less than)
- `>=` (Greater than or equal to)
- `<=` (Less than or equal to)
- **`===` (Strict equality)**: Checks whether both the value AND the data type match.
- **`!==` (Strict inequality)**: Checks whether the value or data type differs.

### The Big Difference Between `==` and `===`

JavaScript has an older operator `==` (*loose equality*). This operator performs **type coercion**, meaning JavaScript quietly converts data types behind the scenes to make them look like a match.

In contrast, the modern `===` (*strict equality*) operator checks both value and data type with zero compromise.

Look at the comparison:

```javascript
console.log(10 == "10");
console.log(10 === "10");
console.log(15 > 10);
console.log(5 !== 10);
```

Output:
```text
true
false
true
true
```

### Why Are the Results Different?
- The first line `10 == "10"` produces `true`. JavaScript unilaterally converted the text `"10"` into the number `10`.
- The second line `10 === "10"` produces `false`. A `number` type is clearly not equal to a `string` type.

**Important Rule:** Always use **`===`** (triple equals) in your projects to prevent hidden bugs.

---

## 3. Logical Operators

Logical operators are used when you want to test multiple conditions at once:

1. **`&&` (AND)**: Evaluates to `true` only if **all** conditions are true.
2. **`||` (OR)**: Evaluates to `true` if **at least one** condition is true.
3. **`!` (NOT)**: Flips a Boolean value. Turns `true` into `false`, and vice versa.

### Logic Code Example

Imagine a movie theater rule: a viewer must have a ticket AND a membership card to get a discount:

```javascript
const hasTicket = true;
const hasMember = false;
const isCheckedIn = true;

// Discount condition: must have a ticket AND a membership card
console.log(hasTicket && hasMember);

// Entry condition: having a ticket OR a membership card is enough
console.log(hasTicket || hasMember);

// Flip the check-in status
console.log(!isCheckedIn);
```

Output:
```text
false
true
false
```

In the next module, we will use expressions like these to build interactive text using *Template Literals*.
