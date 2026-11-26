---
id: js-inti-9
courseId: fe-js-03
order: 19
title: "Truthy, Falsy, & Modern Operators (&&, ||, ??)"
readingMinutes: 7
section: INTI
learningObjectives:
  - Know all Falsy values in JavaScript and distinguish them from Truthy values
  - Understand the short-circuiting mechanism in the && and || logical operators
  - Use the Nullish Coalescing operator (??) to safely set default values without sacrificing 0 or empty strings
codeLanguage: javascript
codeSnippet: |
  const jumlahItem = 0;
  const pakaiOr = jumlahItem || 10;      // 10 (keliru jika 0 valid!)
  const pakaiNullish = jumlahItem ?? 10; // 0  (aman!)

  console.log("Dengan ||:", pakaiOr);
  console.log("Dengan ??:", pakaiNullish);
starterJs: |
  const skorPemain = 0;
  const namaTampilan = "";
  const catatanPengguna = null;

  // Bandingkan || dengan ?? untuk nilai 0 dan string kosong
  const hasilDenganOr = skorPemain || 100;
  const hasilDenganNullish = skorPemain ?? 100;

  // Gunakan ?? untuk nilai yang memang null atau undefined
  const labelCatatan = catatanPengguna ?? "Belum ada catatan";
  const labelNama = namaTampilan ?? "Anonim";

  console.log("Skor pakai || :", hasilDenganOr);
  console.log("Skor pakai ?? :", hasilDenganNullish);
  console.log("Catatan       :", labelCatatan);
  console.log("Nama (??)     :", `"${labelNama}"`);
playgroundTask: |
  Run the starter code and check the Console panel. Observe the difference between the || and ?? operators:
  Skor pakai || : 100
  Skor pakai ?? : 0
  Catatan       : Belum ada catatan
  Nama (??)     : ""
  Try changing the catatanPengguna value to "Siap belajar" and run it again to see the change.
verificationRules:
  - "js:??"
  - "js:console.log"
---

# Truthy, Falsy, & Modern Operators (&&, ||, ??)

When writing an `if` condition, you don't always pass in a pure boolean value (`true` or `false`). Often you check a text, number, or object variable directly:

```javascript
const name = "Budi";
if (name) {
  console.log("Hello, " + name);
}
```

Why is `"Budi"` accepted by `if`? Because JavaScript automatically evaluates every value as either **Truthy** (treated as `true`) or **Falsy** (treated as `false`).

---

## Complete List of the 7 Falsy Values in JavaScript

Across the entire JavaScript language, there are only **7 groups of Falsy values**. Apart from the values in the table below, **everything else is Truthy**.

| No | Falsy Value | Data Type | Brief Explanation |
| :---: | :--- | :--- | :--- |
| 1 | `false` | Boolean | Explicit boolean false value |
| 2 | `0` (as well as `-0` and `0n`) | Number / BigInt | Zero (whether integer, negative zero, or BigInt zero) |
| 3 | `""` / `''` / `` `` `` | String | Empty string with not a single character (a space `" "` is Truthy!) |
| 4 | `null` | Null | Intentional empty value |
| 5 | `undefined` | Undefined | Variable has not been assigned a value |
| 6 | `NaN` | Number | *Not-a-Number* (result of a failed math operation) |
| 7 | `document.all` | Object (Legacy) | Legacy browser object kept only for old site compatibility |

> **Beginner Trap:** The string `"0"`, the string `"false"`, an empty array `[]`, and an empty object `{}` are all **Truthy**! Only a truly empty string (`""`) is Falsy.

---

## Logical Operators & Short-Circuiting (`&&` and `||`)

Besides returning booleans inside `if`, the `&&` and `||` operators in JavaScript actually **return one of their operand values** through a mechanism called *short-circuiting* (stopping early):

1. **AND Operator (`&&`)**: Evaluates from left to right. If the left side is **Falsy**, immediately return the left side. If the left side is **Truthy**, return the right side.
   ```javascript
   const isLoggedIn = true;
   const greeting = isLoggedIn && "Welcome!";
   console.log(greeting); // "Welcome!"
   ```
2. **OR Operator (`||`)**: If the left side is **Truthy**, immediately return the left side. If the left side is **Falsy**, return the right side.

In the past, programmers often used `||` to provide a fallback (*default value*):

```javascript
const discountAmount = 0; // A 0% discount is a valid value!
const finalDiscount = discountAmount || 10;
console.log(finalDiscount); // 10 -> BUG! The number 0 was treated as Falsy!
```

Because `0` is Falsy, the `||` operator mistakenly assumed `discountAmount` was missing and overwrote it with `10`.

---

## The Modern Solution: Nullish Coalescing (`??`)

Since ES2020, JavaScript introduced the **`??`** (*Nullish Coalescing*) operator. This operator is much more precise because it **only treats `null` and `undefined` as missing values**.

```text
  leftValue ?? defaultValue
     |
     +-- If leftValue is null or undefined --> return defaultValue
     |
     +-- If leftValue is 0, "", false, or anything else --> return leftValue
```

Direct comparison of `||` vs `??`:

| Initial Value (`x`) | Result of `x \|\| "Default"` | Result of `x ?? "Default"` |
| :--- | :--- | :--- |
| `null` | `"Default"` | `"Default"` |
| `undefined` | `"Default"` | `"Default"` |
| `0` | `"Default"` (often causes bugs) | `0` (safe) |
| `""` | `"Default"` | `""` (safe) |
| `false` | `"Default"` | `false` (safe) |

---

## Practical Default Value Pattern

Use `??` whenever the number `0`, empty text `""`, or `false` is a valid input from a user or server:

```javascript
const settings = {
  darkTheme: false,
  pageLimit: 0,
  language: undefined
};

const theme = settings.darkTheme ?? true;   // false (not overwritten!)
const limit = settings.pageLimit ?? 20;     // 0     (not overwritten!)
const language = settings.language ?? "en"; // "en"  (uses default)
```

Try running the exercise code in the playground to see the real difference between `||` and `??` in the Console!
