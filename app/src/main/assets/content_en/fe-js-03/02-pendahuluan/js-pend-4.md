---
id: js-pend-4
courseId: fe-js-03
order: 9
title: "Writing Clean Code: Comments & Basic Syntax Rules"
readingMinutes: 4
section: PENDAHULUAN
learningObjectives:
  - Learn the purpose and syntax of single-line (`//`) and multi-line (`/* ... */`) comments
  - Understand that JavaScript is case-sensitive
  - Apply standard naming conventions (camelCase) and understand the role of semicolons (`;`)
codeLanguage: javascript
codeSnippet: |
  // This is a single-line comment
  const studentName = "Budi";

  /* Multi-line comments
     are useful for longer explanations */
  console.log(studentName);
starterJs: |
  // 1. These are comment notes that the machine ignores
  const firstName = "Rani";
  const lastName = "Kusuma";

  /* 2. Below, we print both names
        to the Console panel */
  console.log(firstName, lastName);
playgroundTask: |
  Run the code and check the Console panel. Ensure that only one line is printed:
  Rani Kusuma
  Notice how the text preceded by comment symbols does not affect the program's execution at all.
---

# Writing Clean Code: Comments & Basic Syntax Rules

Writing code isn't just about making the computer understand our instructions; it's also about ensuring that teammates (or your future self) can read and understand it easily.

Let's learn the basic rules for writing clean, standard JavaScript code.

---

## 1. Code Comments

Comments are text notes written specifically for humans to read. The browser ignores comments entirely when executing the program.

There are two types of comments in JavaScript:

### a. Single-line Comments (`//`)
Use two forward slashes to make short notes on a single line:

```javascript
// Calculate 10% member discount
const originalPrice = 100000;
const discountedPrice = originalPrice * 0.9;
```

### b. Multi-line Comments (`/* ... */`)
Use the opening `/*` and closing `*/` tags for longer explanations that span multiple lines:

```javascript
/*
  The following function is used to
  validate the user's email address
  before the data is sent to the database.
*/
console.log("Validation ready");
```

---

## 2. Case-Sensitivity

JavaScript is a highly **case-sensitive** language. Uppercase and lowercase letters are treated as completely different things by the computer:

```javascript
const score = 10;
const Score = 20;
const SCORE = 30;

console.log(score); // Outputs 10, not 20 or 30
```

The three names above are treated as three separate variables. Therefore, be careful when typing variable or function names to ensure the spelling is an exact match.

---

## 3. Standard Naming Style: camelCase

In the JavaScript ecosystem, the globally accepted standard for naming variables and functions is **`camelCase`** (like a camel's hump):
- The first word starts with a **lowercase letter**.
- Every subsequent word starts with an **uppercase letter** with no spaces.

Correct examples:
- `fullName` *(not full_name or FullName)*
- `totalItemPrice`
- `isUserLoggedIn`

---

## 4. Are Semicolons (`;`) Necessary?

At the end of every JavaScript instruction line, you can add a semicolon (`;`):

```javascript
const city = "Jakarta";
console.log(city);
```

JavaScript has a smart feature called *Automatic Semicolon Insertion (ASI)* that automatically treats the end of a line as the end of an instruction. However, making it a habit to write semicolons or consistently following a project's style guide is a good practice to ensure your code avoids ambiguous behavior.

In the next module, we will learn how to recognize error messages so you won't panic when you make a typo!
