---
id: js-lanjutan-6
courseId: fe-js-03
order: 26
title: "Bridge to the Web Page: Selecting Elements with querySelector"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Understand the Document Object Model (DOM) as a representation of an HTML document that JavaScript can manipulate
  - Find specific HTML elements using CSS selectors with document.querySelector()
  - Modify element text via .textContent and visual properties via .style
codeLanguage: javascript
codeSnippet: |
  const judul = document.querySelector("#judul");
  judul.textContent = "Teks Baru!";
  judul.style.color = "teal";
starterHtml: |
  <div style="font-family: sans-serif; padding: 16px; border: 1px solid #CBD5E1; border-radius: 8px;">
    <h3 id="judul-kartu" style="color: #1E293B; margin-top: 0;">Judul Asli</h3>
    <p class="deskripsi-teks" style="color: #64748B; margin-bottom: 0;">Deskripsi awal dokumen.</p>
  </div>
starterJs: |
  // 1. Menemukan elemen HTML menggunakan selektor CSS
  const elemenJudul = document.querySelector("#judul-kartu");
  const elemenDeskripsi = document.querySelector(".deskripsi-teks");

  // 2. Mengubah teks dan warna secara dinamis
  if (elemenJudul) {
    elemenJudul.textContent = "JavaScript Mengubah Dunia Web!";
    elemenJudul.style.color = "#0D9488";
  }

  if (elemenDeskripsi) {
    elemenDeskripsi.textContent = "Teks ini berhasil diperbarui secara dinamis dari JavaScript.";
  }

  console.log("DOM berhasil diperbarui!");
playgroundTask: |
  Press the 'Run' button. Check the preview window: notice that the text 'Judul Asli' immediately changes to 'JavaScript Mengubah Dunia Web!' in teal. Open the Console panel to verify the message:
  DOM berhasil diperbarui!
---

# Bridge to the Web Page: Selecting Elements with querySelector

So far, we have observed JavaScript at work through the Console panel. Now it's time to connect that logic to what users actually see on the browser screen!

How does JavaScript interact with HTML tags? The answer is through the **DOM (Document Object Model)**.

```text
document
 └── <html>
      ├── <head> (<title>)
      └── <body>
           ├── <h1 id="judul">Halo</h1>
           └── <button class="btn">Klik</button>
                 ^-- selected via document.querySelector(".btn")
```
---

## What Is the DOM?

When a browser finishes reading an HTML document, it converts every `<header>`, `<div>`, `<h1>`, and `<p>` tag into nodes of a JavaScript object tree called the **DOM**.

The main object representing the entire web page is named **`document`**. Through the `document` object, JavaScript can read, add, modify text, or even remove any element on the screen.

---

## 1. Finding Elements: document.querySelector()

To target a specific HTML element, use the powerful **`document.querySelector()`** method.

This method uses the same CSS selector rules you already mastered in the previous CSS course:
- **By Tag**: `document.querySelector("h1")` (selects the first `<h1>` tag found).
- **By Class**: `document.querySelector(".nama-class")` (starts with a dot).
- **By ID**: `document.querySelector("#nama-id")` (starts with a hash symbol).

Example:
```html
<p id="pesan-selamat">Selamat datang di toko kami!</p>
```

```javascript
const pesan = document.querySelector("#pesan-selamat");
```

---

## 2. Changing Text: .textContent

Once you have captured an element and stored it in a variable, you can change the text inside it using the **`.textContent`** property:

```javascript
const pesan = document.querySelector("#pesan-selamat");
pesan.textContent = "Special Promo Today: 50% Off!";
```

Instantly, the text visitors read on the browser screen changes to the new sentence!

---

## 3. Changing Visual Styles: .style

You can also change CSS properties directly through the **`.style`** property:

```javascript
pesan.style.color = "#2563EB";           // Change text color to blue
pesan.style.backgroundColor = "#EFF6FF"; // Light blue background (camelCase)
pesan.style.padding = "12px";
```

> **Note on CSS Properties:** In regular CSS, we write `background-color` with a hyphen. In JavaScript, because a hyphen is treated as subtraction, property names are written in **`camelCase`** (`backgroundColor`, `fontSize`, `borderRadius`).

Try running the practice editor below and watch how the text in the preview window is immediately updated by JavaScript!
