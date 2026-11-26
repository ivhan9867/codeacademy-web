---
id: js-lanjutan-11
courseId: fe-js-03
order: 31
title: "Saving Data in the Browser with localStorage & JSON"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Understand how localStorage keeps application data saved even after a page reload
  - Use JSON.stringify() and JSON.parse() to store and read objects or arrays in localStorage
  - Apply try/catch blocks to protect the application from errors when reading corrupted JSON data
codeLanguage: javascript
codeSnippet: |
  const catatan = [{ teks: "Belajar JSON", selesai: true }];
  localStorage.setItem("catatan", JSON.stringify(catatan));
  const hasil = JSON.parse(localStorage.getItem("catatan") ?? "[]");
  console.log(hasil[0].teks);
starterJs: |
  const KUNCI_PENYIMPANAN = "profil_belajar_js";

  const dataProfil = {
    nama: "Bima",
    modulSelesai: 31,
    tema: "gelap"
  };

  // Fungsi aman untuk menyimpan data objek/array ke localStorage
  const simpanKeStorage = (kunci, nilai) => {
    try {
      const jsonTeks = JSON.stringify(nilai);
      localStorage.setItem(kunci, jsonTeks);
      console.log("Tersimpan sebagai JSON string:", jsonTeks);
    } catch (error) {
      console.log("Gagal menyimpan ke storage:", error.message);
    }
  };

  // Fungsi aman untuk membaca kembali dan mengubah string JSON menjadi objek
  const bacaDariStorage = (kunci, nilaiDefault) => {
    try {
      const mentah = localStorage.getItem(kunci);
      if (mentah === null) return nilaiDefault;
      return JSON.parse(mentah);
    } catch (error) {
      console.log("Data JSON rusak, memakai nilai default.");
      return nilaiDefault;
    }
  };

  simpanKeStorage(KUNCI_PENYIMPANAN, dataProfil);
  const profilTerbaca = bacaDariStorage(KUNCI_PENYIMPANAN, {});
  console.log("Nama terbaca kembali:", profilTerbaca.nama);
  console.log("Modul selesai:", profilTerbaca.modulSelesai);
playgroundTask: |
  Run the starter code and open the Console. Notice how the JavaScript object is converted into a JSON string first and then read back as a complete object:
  Tersimpan sebagai JSON string: {"nama":"Bima","modulSelesai":31,"tema":"gelap"}
  Nama terbaca kembali: Bima
  Modul selesai: 31
verificationRules:
  - "js:JSON.stringify"
  - "js:JSON.parse"
---

# Saving Data in the Browser with localStorage & JSON

Have you ever filled out a to-do list or switched a website's theme to *Dark Mode*, and when you refreshed the page, your settings stayed safe and didn't disappear?

Regular JavaScript variables (`let` and `const`) only live in temporary memory (RAM)—the moment a tab is closed or reloaded, everything in them is wiped out. To store data persistently on the user's device, the browser provides **`localStorage`**.

---

## Important Rule: `localStorage` Only Accepts Strings!

`localStorage` works like a locker cabinet with **key and value** pairs. However, there is one strict requirement: **every value stored in `localStorage` must be text (`String`)**.

If you directly store an array `["Apple", "Orange"]` or an object `{ name: "Bima" }`, JavaScript forces it into the text `"[object Object]"` and your data gets corrupted!

This is where **JSON (*JavaScript Object Notation*)** steps in as a two-way translator:

```text
  [ JS Object / Array ]
           |
           |  JSON.stringify(data)   <-- Converts an object into JSON text
           v
  '{"name":"Bima","theme":"dark"}'
           |
           |  localStorage.setItem("profile", jsonText)
           v
  +------------------------+
  |  Browser localStorage  |  (Data persists even after closing the browser!)
  +------------------------+
           |
           |  localStorage.getItem("profile")
           v
  '{"name":"Bima","theme":"dark"}'
           |
           |  JSON.parse(jsonText)   <-- Converts JSON text back into a JS object
           v
  [ Complete JS Object / Array ]
```

---

## 1. Three Basic `localStorage` Methods

1. **Save or update data (`setItem`)**:
   ```javascript
   localStorage.setItem("userName", "Sari");
   ```
2. **Read data (`getItem`)**:
   Returns a string if the key is found, or **`null`** if the key does not exist yet.
   ```javascript
   const name = localStorage.getItem("userName"); // "Sari" or null
   ```
3. **Delete a single key (`removeItem`)**:
   ```javascript
   localStorage.removeItem("userName");
   ```

---

## 2. Converting Objects <-> JSON with `stringify` & `parse`

To store an array or object intact:

```javascript
const taskList = [
  { title: "Learn JSON", done: true },
  { title: "Try localStorage", done: false }
];

// Save: Object -> JSON String
localStorage.setItem("tasks", JSON.stringify(taskList));

// Load: JSON String -> Object
const savedText = localStorage.getItem("tasks");
const restoredTasks = savedText ? JSON.parse(savedText) : [];
console.log(restoredTasks[0].title); // "Learn JSON"
```

---

## 3. Always Wrap `JSON.parse()` in `try/catch`!

What happens if data in `localStorage` gets truncated or accidentally edited by hand into invalid JSON (for example `"{broken"`)?

`JSON.parse("{broken")` will immediately throw a **`SyntaxError`** and crash the entire application!

The professional solution is to wrap the parsing step with **`try/catch`**:

```javascript
const loadDataSafely = (key, fallback) => {
  try {
    const raw = localStorage.getItem(key);
    if (raw === null) return fallback;
    return JSON.parse(raw);
  } catch (err) {
    console.log("Invalid JSON format, falling back to default value.");
    return fallback;
  }
};
```

With this helper function, your application is guaranteed never to crash just because local storage data was corrupted. Run the code in the playground to watch the round-trip JSON conversion in the Console!
