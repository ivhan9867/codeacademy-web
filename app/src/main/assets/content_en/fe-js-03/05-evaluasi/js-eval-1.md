---
id: js-eval-1
courseId: fe-js-03
order: 33
title: "Mini Project: Building an Interactive To-Do List App"
readingMinutes: 8
section: EVALUASI
learningObjectives:
  - Combine HTML structure, CSS styling, and JavaScript logic into one complete mini project
  - Apply data-driven architecture: manipulate a data array in memory and then render it to the DOM
  - Handle form text input and button click actions in real time
codeLanguage: javascript
codeSnippet: |
  const list = ["Tugas 1"];
  list.push("Tugas 2");
  console.log("Total tugas:", list.length);
starterHtml: |
  <div style="font-family: sans-serif; max-width: 420px; margin: 0 auto; padding: 20px; border: 1px solid #E2E8F0; border-radius: 12px; background: #FFFFFF; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);">
    <h3 style="color: #0F172A; margin-top: 0; text-align: center;">Daftar Tugas Interaktif</h3>
    <div style="display: flex; gap: 8px; margin-bottom: 16px;">
      <input id="input-tugas" type="text" placeholder="Tulis tugas baru..." style="flex: 1; padding: 10px 12px; border: 1px solid #CBD5E1; border-radius: 6px; font-size: 14px;" value="Belajar DOM JavaScript" />
      <button id="tombol-tambah-tugas" style="background-color: #0D9488; color: white; border: none; padding: 10px 16px; border-radius: 6px; font-weight: bold; cursor: pointer;">
        Tambah
      </button>
    </div>
    <ul id="daftar-tugas" style="list-style: none; padding: 0; margin: 0;">
      <li style="padding: 10px 12px; background: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 6px; margin-bottom: 8px; color: #334155;">
        1. Selesaikan modul Fondasi Web
      </li>
    </ul>
  </div>
starterJs: |
  // 1. Data array sebagai sumber kebenaran (Single Source of Truth)
  const tugasList = ["Selesaikan modul Fondasi Web"];

  // 2. Menangkap elemen DOM yang dibutuhkan
  const inputTugas = document.querySelector("#input-tugas");
  const tombolTambah = document.querySelector("#tombol-tambah-tugas");
  const elemenDaftar = document.querySelector("#daftar-tugas");

  // 3. Fungsi untuk memperbarui tampilan daftar di layar
  const renderDaftar = () => {
    if (!elemenDaftar) return;
    elemenDaftar.innerHTML = "";
    
    tugasList.forEach((tugas, idx) => {
      const li = document.createElement("li");
      li.style.cssText = "padding: 10px 12px; background: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 6px; margin-bottom: 8px; color: #334155;";
      li.textContent = `${idx + 1}. ${tugas}`;
      elemenDaftar.appendChild(li);
    });
  };

  // 4. Memasang event listener pada tombol Tambah
  if (tombolTambah && inputTugas) {
    tombolTambah.addEventListener("click", () => {
      const teksBaru = inputTugas.value.trim();
      if (teksBaru !== "") {
        tugasList.push(teksBaru);
        renderDaftar();
        inputTugas.value = "";
        console.log(`Tugas ditambahkan: "${teksBaru}". Total tugas sekarang: ${tugasList.length}`);
      }
    });
  }

  console.log("Aplikasi To-Do List siap digunakan!");
playgroundTask: |
  Press the 'Run' button. In the preview window, click the 'Tambah' button to add the task currently in the input ('Belajar DOM JavaScript'). Watch the new item appear immediately in the list, then open the Console panel to verify the log:
  Tugas ditambahkan: "Belajar DOM JavaScript". Total tugas sekarang: 2
---

# Mini Project: Building an Interactive To-Do List App

It's time to put together all the puzzle pieces you've learned over the past 24 modules:
- **Variables & Arrays**: Storing the task list in memory.
- **Functions & Arrow Functions**: Crafting the display update (*render*) logic.
- **`if` Branching**: Ensuring the user does not add an empty task.
- **DOM Selectors**: Capturing the input box, button, and `<ul>` list container.
- **Event Listeners**: Responding when the *"Tambah"* (Add) button is clicked by the user.

Let's break down the architecture of this app step by step!

---

## 1. Data-Driven Architecture

The most common mistake beginners make is attaching text directly to HTML elements without recording it in a JavaScript variable first.

The proper professional approach is to use a **Data-Driven** pattern:
1. Create a JavaScript array as the central store:
   ```javascript
   const tugasList = ["Selesaikan modul Fondasi Web"];
   ```
2. When there is a new task, add its data to the array first using `tugasList.push(teksBaru)`.
3. After the data in the array grows, call `renderDaftar()` to redraw the `<li>` elements on the screen.

With this pattern, if you later want to count completed tasks or filter tasks, the data is already neatly stored inside the array!

---

## 2. Reading Input & Validation

To grab what the user typed inside an `<input>` box, we read the **`.value`** property:

```javascript
const teksBaru = inputTugas.value.trim();
```

The `.trim()` method removes extra spaces at the start and end of the text. By checking `if (teksBaru !== "")`, we ensure the user does not accidentally submit an empty task.

---

## 3. Creating New HTML Elements: document.createElement()

Instead of typing raw HTML strings, JavaScript provides a very safe way to assemble new elements from scratch:

```javascript
// 1. Create a new <li> tag
const li = document.createElement("li");

// 2. Set the task text
li.textContent = "1. Belajar DOM JavaScript";

// 3. Attach it into the parent <ul> tag
elemenDaftar.appendChild(li);
```

---

## Try It Directly in the Editor!

1. Press the **Run** button.
2. Click the **Tambah** button in the preview window.
3. Type a new task in the input box, such as *"Make afternoon coffee"*, then click **Tambah** again.
4. Open the **Console** panel to see real-time reports of added tasks!

You have officially built your first interactive web application using pure JavaScript!
