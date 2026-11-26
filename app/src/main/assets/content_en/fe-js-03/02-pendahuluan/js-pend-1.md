---
id: js-pend-1
courseId: fe-js-03
order: 6
title: What Is JavaScript? The Brain Behind the Interactive Web
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Understand the primary role of JavaScript as a dynamic programming language in web browsers
  - Distinguish the responsibilities between HTML, CSS, and JavaScript on a website
  - Learn about the modern ECMAScript standard (ES2015+) that powers today's web
---

# What Is JavaScript? The Brain Behind the Interactive Web

Imagine you are building a car:
1. **HTML** is the chassis, steel frame, doors, and seats (*the core structure*).
2. **CSS** is the metallic paint, wheel design, leather upholstery, and window tint (*the visual beauty*).
3. **JavaScript** is the engine, battery, steering wheel, gas pedal, and onboard computer (*function and movement*).

Without JavaScript, a website is just a digital paper brochure: nice to look at, but completely unresponsive when touched. With JavaScript, a web page can think, respond to mouse clicks, calculate shopping cart totals, validate sign-up forms, and update data without having to *reload* the entire page.

---

## Brief History & the ECMAScript Standard

JavaScript was created by **Brendan Eich** in 1995 in just 10 days at Netscape. Even though its name includes the word "Java", JavaScript is a completely different language from Java—both in design philosophy and how it is used.

So that all web *browsers*—such as Chrome, Firefox, Safari, and Edge—execute JavaScript consistently, the language is standardized under the name **ECMAScript**.

In 2015, a major update was released known as **ES6 (ECMAScript 2015)**. This update introduced modern features such as:
- `let` and `const` variable declarations.
- *Arrow functions* (`=>`).
- *Template literals* (backticks `` ` ``).
- Asynchronous data handling (*Promises*).

Everything you learn in this course uses the modern standard (ES2015+), which is today's industry norm.

---

## How Does JavaScript Run on Your Screen?

Every modern browser contains a dedicated interpreter program (*engine*) inside it (for example, Google's V8 inside Chrome). When a browser reads your web document:
1. The browser assembles HTML elements into a document tree structure.
2. The browser applies CSS rules for layout and colors.
3. As soon as it encounters JavaScript code, the browser engine executes the instructions line by line from top to bottom.

For example, look at these simple lines of code:

```javascript
const namaSiswa = "Andi";
const tahunSekarang = 2026;

console.log("Selamat datang, " + namaSiswa);
console.log("Tahun aktif: " + tahunSekarang);
```

Output:
```text
Selamat datang, Andi
Tahun aktif: 2026
```

Best of all, you don't need any extra apps or complicated setup to run the code above. The browser on your phone and laptop is ready to run it right now!

In the next module, we'll learn how to place JavaScript code inside an HTML document so the two can work together.
