---
id: js-pend-5
courseId: fe-js-03
order: 10
title: "Making Friends with Errors: How to Read Error Messages in the Console"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Understand the anatomy of browser Console error messages without feeling intimidated
  - Recognize the three most common error types: SyntaxError, ReferenceError, and TypeError
  - Develop basic skills to trace the line of code causing an issue (debugging)
codeLanguage: javascript
codeSnippet: |
  // 1. SyntaxError: Tanda kutip tidak ditutup
  // const nama = "Andi;

  // 2. ReferenceError: Variabel belum dideklarasikan
  // console.log(umur);

  // 3. TypeError: Mengubah nilai konstanta
  // const pi = 3.14;
  // pi = 3.14159;
starterJs: |
  // Kode di bawah ini sudah diperbaiki dan siap dijalankan:
  const pesanPenyemangat = "Error adalah bukti kamu sedang belajar!";
  console.log("Status:", pesanPenyemangat);
playgroundTask: |
  Run the starter code and make sure the Console displays:
  Status: Error adalah bukti kamu sedang belajar!
  As an experiment, try replacing the variable name in the console.log line with a wrong name (for example pesanSalah), then run it again to see how a ReferenceError is displayed in the Console.
---

# Making Friends with Errors: How to Read Error Messages in the Console

When you write code and make a mistake, the program stops and displays red text in the Console panel.

To beginners, this red text often looks intimidating. In reality, **an error message is the best clue the computer gives you** to help you find the problem quickly. The process of finding and fixing these mistakes is called **Debugging**.

---

## Anatomy of an Error Message

When an error occurs, the Console usually gives you three key pieces of information:
1. **Error Type**: The kind of rule violation that occurred.
2. **Message**: An explanation of why the action is not allowed.
3. **Line Number**: The line where the problem was first detected.

Let's learn the three types of errors you will encounter most often:

---

## 1. SyntaxError (Grammar Mistake)
This happens when you break JavaScript's writing rules, similar to forgetting a period or misspelling a word in human language.

**Example:**
```javascript
const greeting = "Hello everyone;
```

**Console Message:**
```text
Uncaught SyntaxError: Invalid or unexpected token
```
> **Meaning:** JavaScript found an opening quote `"`, but the line ended before finding its matching closing quote.

---

## 2. ReferenceError (Reference Not Found)
This happens when you tell the computer to call or use a variable that **has never been created**.

**Example:**
```javascript
console.log(fullName);
```

**Console Message:**
```text
Uncaught ReferenceError: fullName is not defined
```
> **Meaning:** The computer searched through all of memory, but there is no box named `fullName`. You may have forgotten to declare it first, or there is a capitalization typo.

---

## 3. TypeError (Type Mistake / Forbidden Operation)
This happens when you force the computer to perform an operation that is not allowed on that kind of data.

**Example:**
```javascript
const coins = 100;
coins = 200; // A const variable cannot be reassigned
```

**Console Message:**
```text
Uncaught TypeError: Assignment to constant variable.
```
> **Meaning:** You already promised that `coins` is a fixed value (*constant*), yet on the next line you tried to overwrite it with a new number.

---

## Three Steps to Fix Errors Like a Pro
1. **Take a Breath & Read the Message**: Don't close the Console right away. Slowly read what kind of error it is.
2. **Check the Line Number**: Click or look at the line number shown (for example `:3`). Inspect that line and the one right above it.
3. **Check Spelling & Paired Punctuation**: 80% of beginner errors are caused by mismatched capitalization, unclosed quotes, or a missing parenthesis `()`.

Now you understand the basics of the JavaScript ecosystem! In the next section (**Part 3: Program Logic & Flow**), we will start diving into the core powers of programming: variables, data types, conditionals, and loops.
