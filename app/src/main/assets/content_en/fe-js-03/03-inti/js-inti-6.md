---
id: js-inti-6
courseId: fe-js-03
order: 16
title: "Choosing Specific Options with the switch Statement"
readingMinutes: 5
section: INTI
learningObjectives:
  - Understand when to use switch as a cleaner alternative to many else if branches
  - Master the building blocks of switch: case, break, and default
  - Understand the importance of break to prevent unintended fall-through behavior
codeLanguage: javascript
codeSnippet: |
  const hari = "Senin";
  switch (hari) {
    case "Senin":
      console.log("Mulai bekerja");
      break;
    default:
      console.log("Hari biasa");
  }
starterJs: |
  const kodeRole = "EDITOR";

  switch (kodeRole) {
    case "ADMIN":
      console.log("Hak Akses: Kelola Penuh Sistem");
      break;
    case "EDITOR":
      console.log("Hak Akses: Sunting & Terbitkan Konten");
      break;
    case "VIEWER":
      console.log("Hak Akses: Hanya Membaca Konten");
      break;
    default:
      console.log("Hak Akses: Tidak Dikenal");
      break;
  }
playgroundTask: |
  Run the starter code and check the Console panel. Make sure the output printed is:
  Hak Akses: Sunting & Terbitkan Konten
  Try changing the kodeRole variable to "VIEWER" or "GUEST" to see the matching case or default fallback get triggered.
---

# Choosing Specific Options with the switch Statement

Imagine you are building an account role system: there are `"ADMIN"`, `"EDITOR"`, `"VIEWER"`, `"CONTRIBUTOR"`, and `"GUEST"`.

If you use `if / else if`, your code will repeatedly write `if (role === "ADMIN") ... else if (role === "EDITOR") ...`.

For situations where we want to match **one variable against many possible exact values**, JavaScript provides a much cleaner structure called **`switch`**.

---

## Anatomy of switch

```javascript
const light = "Yellow";

switch (light) {
  case "Red":
    console.log("Stop!");
    break;
  case "Yellow":
    console.log("Be careful, get ready!");
    break;
  case "Green":
    console.log("Keep going!");
    break;
  default:
    console.log("Broken light / unknown color");
    break;
}
```

Output:
```text
Be careful, get ready!
```

---

## Three Key Components of switch

1. **`case "VALUE":`**: The checkpoint. If the variable's value matches the value next to `case` exactly, the instructions below it run.
2. **`break;`**: The exit command. As soon as `break` is reached, JavaScript immediately jumps out of the `switch` block.
3. **`default:`**: The safety net. This section runs if none of the `case` options above match (similar to the final `else` in an `if` chain).

---

## Watch Out for "Fall-Through"!

What happens if you forget to write the `break` keyword?

```javascript
const level = 1;

switch (level) {
  case 1:
    console.log("Level 1");
    // Forgot to write break!
  case 2:
    console.log("Level 2");
    break;
}
```

Generated output:
```text
Level 1
Level 2
```

Without `break`, JavaScript continues sliding down (*fall-through*) and executes the instructions in the next `case` even though its condition did not match! Therefore, never omit `break` at the end of each `case`.

In the next module, we will learn how to tell the computer to do things repeatedly without getting tired using **loops**!
