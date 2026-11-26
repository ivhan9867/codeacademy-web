---
id: js-inti-5
courseId: fe-js-03
order: 15
title: "Making Decisions: if, else if, and else Conditionals"
readingMinutes: 6
section: INTI
learningObjectives:
  - Understand the principle of code branching so programs can make decisions
  - Master writing tiered if, else if, and else structures
  - Understand how the order of condition checks affects which branch gets executed
codeLanguage: javascript
codeSnippet: |
  const umur = 18;
  if (umur >= 17) {
    console.log("Boleh membuat KTP");
  } else {
    console.log("Belum cukup umur");
  }
starterJs: |
  const nilaiUjian = 85;

  // Evaluasi predikat nilai
  if (nilaiUjian >= 90) {
    console.log("Predikat: A (Luar Biasa)");
  } else if (nilaiUjian >= 75) {
    console.log("Predikat: B (Sangat Baik)");
  } else {
    console.log("Predikat: C (Perlu Peningkatan)");
  }
playgroundTask: |
  Run the starter code. Make sure the Console displays:
  Predikat: B (Sangat Baik)
  Try changing the nilaiUjian variable to 92 (which triggers branch A) or 65 (which triggers branch C) to see the branching flow in action!
---

# Making Decisions: if, else if, and else Conditionals

Computer programs don't just run lines of code straight from top to bottom. A smart program must be able to make decisions:
- *"If the balance is sufficient, continue the transaction. Otherwise, show a warning."*
- *"If the password is correct, open the main page. Otherwise, lock the account."*

This ability to choose a path is called **Conditionals / Branching (Control Flow)**.

```text
        [ Condition: score >= 80? ]
               /                     (Yes)        (No)
             /                   "Pass Well"    [ score >= 60? ]
                         /                              (Yes)       (No)
                   "Pass"      "Remedial"
```
---

## 1. Basic Branching: if and else

The most basic structure uses the keywords `if` and `else`:

```javascript
const bodyTemp = 38.2;

if (bodyTemp >= 37.5) {
  console.log("Status: Fever");
} else {
  console.log("Status: Normal");
}
```

Output:
```text
Status: Fever
```

**How It Works:**
1. JavaScript checks the condition inside the parentheses `(bodyTemp >= 37.5)`.
2. Because `38.2 >= 37.5` evaluates to `true`, the code block inside the first `{}` runs.
3. The `else` block is automatically skipped.

---

## 2. Handling Multiple Choices: else if

When there are more than two condition options, use `else if` between `if` and `else`:

```javascript
const hour = 14;

if (hour < 12) {
  console.log("Good Morning!");
} else if (hour < 18) {
  console.log("Good Afternoon!");
} else {
  console.log("Good Evening!");
}
```

Output:
```text
Good Afternoon!
```

---

## The Golden Rule of Condition Ordering

JavaScript evaluates conditions **from top to bottom**. As soon as it finds a condition that is `true`:
1. That matching code block runs immediately.
2. All branches below it are **ignored and will not be checked**.

Therefore, always order your conditions from the most specific or highest threshold down to the more general ones.

In the next module, we will learn an alternative branching statement called `switch`, which is very clean for matching a single value!
