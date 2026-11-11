---
id: node-inti-7
courseId: be-node-01
order: 14
title: "Urutan Eksekusi Asinkron: nextTick, Promise, dan Timer"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Memahami hierarki prioritas eksekusi di Event Loop Node.js"
  - "Membedakan Microtask Queue (process.nextTick, Promise.then) vs Macrotask Queue (setTimeout, setImmediate)"
  - "Menelusuri kronologi output kode gabungan sinkron dan asinkron secara presisi"
codeLanguage: javascript
codeSnippet: |
  console.log("1. Sinkron");
  setTimeout(() => console.log("4. Timer Macrotask"), 0);
  Promise.resolve().then(() => console.log("3. Promise Microtask"));
  process.nextTick(() => console.log("2. NextTick Priority"));
---

# Urutan Eksekusi Asinkron: nextTick, Promise, dan Timer

Inilah salah satu topik yang paling sering diujikan dalam wawancara kerja insinyur back-end Node.js tingkat menengah ke atas: **Memprediksi urutan pasti eksekusi berbagai fungsi asinkron**.

Di balik layar, Node.js tidak hanya memiliki satu antrean antrian tugas, melainkan beberapa tingkatan antrean dengan hak prioritas yang berbeda!

![Fase Antrean Event Loop Microtask Macrotask](asset:images/be-node-01/event-loop-phases.png)

---

## 1. Tangga Kasta Eksekusi di Node.js

Ketika sebuah file JavaScript dijalankan di Node.js, urutan eksekusinya mengikuti hierarki yang sangat ketat:

1. **Kasta 1: Kode Sinkron (Call Stack Langsung)**
   Semua instruksi biasa yang langsung dibaca dari atas ke bawah. Selama ada kode sinkron yang masih berjalan di Call Stack, **tidak ada tugas asinkron apapun yang boleh masuk**.
2. **Kasta 2: Microtask Prioritas Tertinggi (`process.nextTick`)**
   Antrean `nextTickQueue` dieksekusi **seketika setelah operasi sinkron saat ini selesai**, mendahului semua microtask lainnya.
3. **Kasta 3: Standar Microtask (`Promise.then` / `catch` / `finally`)**
   Antrean microtask standar untuk segala penyelesaian Promise.
4. **Kasta 4: Macrotask / Timer Phase (`setTimeout` / `setInterval`)**
   Fungsi callback yang dijadwalkan oleh sistem timer Libuv setelah durasi waktunya habis.
5. **Kasta 5: Check Phase (`setImmediate`)**
   Callback khusus Node.js yang dieksekusi tepat setelah fase I/O Event Loop selesai berputar.

---

## 2. Eksperimen Penelusuran Mental

Perhatikan kode berikut. Jangan langsung melihat output di bawahnya! Cobalah tebak urutan angka dari 1 sampai 5:

```javascript
console.log("A. Kode Sinkron Pertama");

setTimeout(() => {
  console.log("E. Macrotask: setTimeout(0ms)");
}, 0);

Promise.resolve().then(() => {
  console.log("D. Microtask: Promise.then()");
});

process.nextTick(() => {
  console.log("C. Microtask Prioritas: process.nextTick()");
});

console.log("B. Kode Sinkron Terakhir");
```

Output:
```
A. Kode Sinkron Pertama
B. Kode Sinkron Terakhir
C. Microtask Prioritas: process.nextTick()
D. Microtask: Promise.then()
E. Macrotask: setTimeout(0ms)
```

---

## 3. Bedah Kronologi Alur (Langkah Demi Langkah)

Mari kita bedah secara ilmiah mengapa urutannya menjadi `A -> B -> C -> D -> E`:

1. **Langkah 1 (Call Stack)**: Node.js mengeksekusi baris sinkron pertama. Mencetak `A. Kode Sinkron Pertama`.
2. **Langkah 2 (Daftar Timer)**: Baris `setTimeout` dibaca. Libuv mencatat timer `0ms` ke daftar antrean macrotask.
3. **Langkah 3 (Daftar Promise)**: Baris `Promise.resolve().then(...)` dibaca. Callback-nya ditaruh di dalam antrean `microtask queue`.
4. **Langkah 4 (Daftar nextTick)**: Baris `process.nextTick(...)` dibaca. Callback-nya ditaruh di antrean khusus `nextTick queue`.
5. **Langkah 5 (Call Stack)**: Node.js mengeksekusi baris sinkron terakhir. Mencetak `B. Kode Sinkron Terakhir`.
6. **Langkah 6 (Pembersihan Microtask)**: Call Stack kini kosong! Sebelum Event Loop beralih ke Macrotask, Node.js memeriksa Microtask:
   - Pertama, seluruh antrean `nextTick` dieksekusi -> Mencetak `C. Microtask Prioritas...`.
   - Kedua, seluruh antrean `Promise` dieksekusi -> Mencetak `D. Microtask: Promise.then()`.
7. **Langkah 7 (Masuk ke Event Loop Macrotask)**: Sekarang Event Loop melangkah ke fase Timers -> Mengambil callback `setTimeout` -> Mencetak `E. Macrotask: setTimeout(0ms)`.

---

## 4. `process.nextTick` vs `setImmediate`

Dua fungsi ini sering tertukar namanya karena sejarah penamaan di awal perkembangan Node.js:

- **`process.nextTick()`**: Namanya mengisyaratkan "tik berikutnya", namun kenyataannya ia berjalan **SEKARANG JUGA** (begitu operasi sinkron saat ini selesai, sebelum Event Loop melanjutkan putaran).
- **`setImmediate()`**: Berjalan pada fase **Check** di putaran Event Loop berikutnya setelah menyelesaikan operasi I/O.

Dengan memahami urutan kasta ini, kamu tidak akan lagi bingung saat menemukan bug di mana sebuah variabel bernilai `undefined` karena dipanggil sebelum Promise selesai dieksekusi.

Sekarang, saatnya menguji seluruh pemahaman asinkronmu di modul penutup Section 3: **Latihan Tebak Outputnya!**
