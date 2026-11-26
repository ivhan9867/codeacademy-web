---
id: js-lanjutan-9
courseId: fe-js-03
order: 29
title: "Destructuring & Spread Operator (...) for Concise Code"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Extract values from objects and arrays into separate variables using Destructuring
  - Use default values and variable renaming (aliasing) when destructuring objects
  - Copy and merge arrays or objects immutably with the Spread Operator (...)
codeLanguage: javascript
codeSnippet: |
  const user = { nama: "Raka", peran: "Mentor" };
  const { nama, peran } = user;
  const userAktif = { ...user, aktif: true };

  console.log(nama, peran);
  console.log(userAktif);
starterJs: |
  const profilDasar = {
    nama: "Nadia",
    kota: "Bandung",
    level: 2
  };

  // 1. Object destructuring + default value
  const { nama, kota, lencana = "Pemula" } = profilDasar;
  console.log(`${nama} dari ${kota} (Lencana: ${lencana})`);

  // 2. Spread operator untuk membuat objek baru tanpa mengubah profilDasar
  const profilDiperbarui = {
    ...profilDasar,
    level: 3,
    aktif: true
  };

  // 3. Array destructuring & spread
  const hobiAwal = ["Coding", "Membaca"];
  const semuaHobi = [...hobiAwal, "Fotografi"];
  const [hobiUtama, hobiKedua] = semuaHobi;

  console.log("Level lama:", profilDasar.level, "| Level baru:", profilDiperbarui.level);
  console.log("Hobi pertama:", hobiUtama, "| Hobi kedua:", hobiKedua);
playgroundTask: |
  Run the starter code and open the Console. Make sure the printed output is:
  Nadia dari Bandung (Lencana: Pemula)
  Level lama: 2 | Level baru: 3
  Hobi pertama: Coding | Hobi kedua: Membaca
verificationRules:
  - "js:..."
  - "js:const {"
---

# Destructuring & Spread Operator (...) for Concise Code

When working with large objects or arrays, writing `pengguna.nama`, `pengguna.email`, and `pengguna.kota` repeatedly makes code feel long and verbose.

Modern JavaScript (ES6+) provides two favorite features among web programmers—**Destructuring** and the **Spread Operator (`...`)**—to keep code much cleaner, shorter, and safe from data mutation bugs.

---

## Old Style vs Modern ES6+ Comparison

| Need | Before ES6 (Old Style) | After ES6+ (Destructuring & Spread) |
| :--- | :--- | :--- |
| Get object properties | `const nama = user.nama;`<br>`const kota = user.kota;` | `const { nama, kota } = user;` |
| Get array elements | `const a = angka[0];`<br>`const b = angka[1];` | `const [a, b] = angka;` |
| Merge two arrays | `arr1.concat(arr2)` | `[...arr1, ...arr2]` |
| Copy & update object | `Object.assign({}, obj, { aktif: true })` | `{ ...obj, aktif: true }` |

---

## 1. Object Destructuring

Destructuring means "unpacking" the contents of an object by **property name** directly into standalone variables:

```javascript
const kursus = {
  judul: "JavaScript Fundamentals",
  durasiJam: 12,
  tingkat: "Pemula"
};

const { judul, tingkat } = kursus;
console.log(judul);   // "JavaScript Fundamentals"
console.log(tingkat); // "Pemula"
```

### Providing Default Values & Renaming Variables
You can immediately provide a fallback value if a property does not exist yet, or give it an alias using a colon (`:`):

```javascript
const { judul: namaKelas, sertifikat = true } = kursus;
console.log(namaKelas);  // "JavaScript Fundamentals"
console.log(sertifikat); // true
```

---

## 2. Array Destructuring

Unlike objects that match *property names*, array destructuring matches **order position (index)**:

```javascript
const warna = ["Merah", "Kuning", "Hijau"];
const [pertama, kedua] = warna;

console.log(pertama); // "Merah"
console.log(kedua);   // "Kuning"
```

If you want to skip a specific item, simply leave an empty space between commas:
```javascript
const [, , ketiga] = warna;
console.log(ketiga); // "Hijau"
```

---

## 3. Spread Operator (`...`) for Safe Copying & Merging

If you copy an object with regular `=` (`const salinan = asli;`), both point to the same object in memory—changing `salinan` will also corrupt `asli`.

With the **Spread Operator (`...`)**, you unpack all contents of the old container into a new container (**immutable copy**):

```javascript
const keranjangAwal = ["Buku", "Pena"];
const keranjangBaru = [...keranjangAwal, "Penggaris"];

console.log(keranjangAwal); // ["Buku", "Pena"] (safe, unchanged!)
console.log(keranjangBaru); // ["Buku", "Pena", "Penggaris"]
```

The same applies to objects. Properties written **after** `...objekLama` will overwrite the values of matching properties on the new copy:

```javascript
const pengaturanAwal = { tema: "terang", suara: true };
const pengaturanBaru = { ...pengaturanAwal, tema: "gelap" };

console.log(pengaturanAwal.tema); // "terang"
console.log(pengaturanBaru.tema); // "gelap"
```

> **Important Note:** The spread operator creates a one-level copy (*shallow copy*). For simple objects or flat lists, this pattern is very fast, clean, and standard in modern frameworks like React and Vue.

Test both techniques directly in the playground editor!
