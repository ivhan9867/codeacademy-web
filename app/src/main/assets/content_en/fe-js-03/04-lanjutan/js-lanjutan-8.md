---
id: js-lanjutan-8
courseId: fe-js-03
order: 28
title: "Waiting Without Freezing: A Brief Introduction to Promise & async/await"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Understand the difference between Synchronous execution (immediate & sequential) and Asynchronous execution (does not block the queue)
  - Learn the concept of a Promise as a representation of a future value
  - Use the async and await keyword pair to write asynchronous processes in a clean code style
codeLanguage: javascript
codeSnippet: |
  const jeda = () => new Promise((res) => setTimeout(res, 500));
  const jalan = async () => {
    console.log("Mulai");
    await jeda();
    console.log("Selesai");
  };
  jalan();
starterJs: |
  // Fungsi simulasi tugas asinkron dengan jeda waktu 1 detik
  const tungguSatuDetik = () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve("Data berhasil disiapkan!");
      }, 1000);
    });
  };

  // Menjalankan fungsi asinkron dengan async dan await
  const prosesData = async () => {
    console.log("1. Memulai proses...");
    const hasil = await tungguSatuDetik();
    console.log("2.", hasil);
    console.log("3. Selesai!");
  };

  prosesData();
playgroundTask: |
  Run the starter code. Open the Console panel and wait a moment (1 second). Make sure the Console prints 3 lines in order:
  1. Memulai proses...
  2. Data berhasil disiapkan!
  3. Selesai!
---

# Waiting Without Freezing: A Brief Introduction to Promise & async/await

Naturally, JavaScript executes instructions **Synchronously**: line 2 only runs after line 1 finishes, and line 3 only runs after line 2 completes.

However, what if a task takes several seconds?
For example: waiting for an image file to finish downloading or loading data from a database.

If JavaScript waits while freezing (*blocking*), buttons on the screen cannot be clicked, animations stutter, and users will think our app is broken!

---

## Coffee Shop Queue Analogy

- **Synchronous System (*Blocking*)**:
  You order a cup of coffee. The cashier refuses to serve anyone else and lets the line grow long for 5 minutes until your coffee finishes brewing. The entire shop grinds to a halt.
- **Asynchronous System (*Non-Blocking*)**:
  You order a coffee and pay. The cashier gives you a **queue ticket (*Promise*)**. You are free to sit back and relax. The cashier immediately serves the next customer. As soon as your coffee is ready, your name is called and your coffee is handed to you.

---

## Getting to Know Promise (A Future Value Promise)

A **Promise** is a JavaScript object that represents a pledge:
> *"This task isn't finished yet, but I promise to let you know the result later (whether it succeeds or fails)."*

A Promise has 3 states:
1. `pending`: In progress (coffee is being brewed).
2. `fulfilled`: Successfully completed (coffee is ready to enjoy).
3. `rejected`: Failed/canceled (out of coffee beans).

---

## Modern Style: async and await

Previously, web developers used long `.then()` chains to handle Promises. Since ES2017, JavaScript introduced a much cleaner keyword pair: **`async`** and **`await`**.

1. **`async`**: Placed in front of a function declaration to tell the computer that this function contains a process that needs to wait.
2. **`await`**: Placed before calling a Promise to pause that function's execution neatly until the result finishes, **without freezing the browser interface**.

```javascript
const ambilData = async () => {
  console.log("Starting data request...");
  const respon = await tugasBerat(); // Waits without freezing the browser
  console.log("Result received:", respon);
};
```

Great, isn't it? The code looks linear and easy to read like normal code, while running with the power of an asynchronous system behind the scenes!

In the final section (**Section 5: Project & Evaluation**), we will combine everything we have learned to build one complete interactive to-do list application!
