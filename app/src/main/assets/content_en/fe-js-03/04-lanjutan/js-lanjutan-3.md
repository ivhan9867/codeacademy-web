---
id: js-lanjutan-3
courseId: fe-js-03
order: 23
title: "Variable Boundaries: Global Scope vs Block Scope"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - Understand the concept of Scope as the access boundary of a variable in code
  - Distinguish between Global Scope (broad access) and Block Scope (limited access inside curly braces)
  - Know why variable isolation is important to prevent naming conflicts
codeLanguage: javascript
codeSnippet: |
  const global = "Saya terlihat di mana-mana";
  {
    const lokal = "Saya hanya terlihat di dalam kurung ini";
  }
  // console.log(lokal); // ReferenceError: lokal is not defined
starterJs: |
  const namaAplikasi = "CodeAcademy"; // Global Scope

  function prosesStatusPengguna(poin) {
    // Variabel level berada dalam Block Scope fungsi ini
    const level = poin >= 100 ? "Master" : "Pemula";
    console.log(`[Di dalam fungsi] Aplikasi: ${namaAplikasi}, Level: ${level}`);
  }

  prosesStatusPengguna(120);

  // Variabel namaAplikasi tetap dapat diakses di luar fungsi
  console.log(`[Di luar fungsi] Selamat datang di ${namaAplikasi}!`);
playgroundTask: |
  Run the starter code and check the Console panel. Make sure the output shown is:
  [Di dalam fungsi] Aplikasi: CodeAcademy, Level: Master
  [Di luar fungsi] Selamat datang di CodeAcademy!
---

# Variable Boundaries: Global Scope vs Block Scope

Have you ever wondered: *"If I create a variable inside a function or inside an if branch, can that variable be accessed from outside?"*

The rules governing where a variable may be read or modified are called **Scope**.

---

## National ID vs Room Key Analogy

- **National ID (Global)**: Valid and recognized anywhere across the entire country.
- **Boarding Room Key (Block / Local)**: Only useful for unlocking your own room door. People out on the street cannot use that key.

---

## 1. Global Scope

A variable declared outside any function or curly-brace block is in the **Global Scope**.

```javascript
const currency = "IDR"; // Global

function printPrice(amount) {
  // Global variables can be read from inside any function
  console.log(`${currency} ${amount}`);
}

printPrice(50000); // Output: IDR 50000
```

Although convenient, avoid creating too many global variables because they risk being overwritten or colliding accidentally as your app grows larger.

---

## 2. Block Scope (`let` and `const`)

Whenever you see a pair of curly braces `{ ... }` (inside a function, an `if` branch, or a `for` loop), you are looking at a **Block**.

Variables created using `let` or `const` inside that block are **locked inside it and cannot be accessed from outside**:

```javascript
if (true) {
  const secret = "123456";
  console.log(secret); // Works: prints "123456"
}

console.log(secret); 
// Error! ReferenceError: secret is not defined
```

Once the `{}` block finishes executing, the `secret` variable is cleared from memory.

### Why Is Block Scope So Helpful?
Because variables are neatly isolated, you are free to reuse common variable names like `result`, `total`, or `i` across different functions without worrying that they will clash or break each other!

In the next module, we will learn a more sophisticated data container for modeling real-world things: **JavaScript Objects**!
