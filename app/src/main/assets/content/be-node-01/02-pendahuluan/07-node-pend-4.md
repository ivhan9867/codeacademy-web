---
id: node-pend-4
courseId: be-node-01
order: 7
title: "Objek Global, process, dan Akses Lingkungan Sistem"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - "Memahami konsep objek global (global dan globalThis) di Node.js sebagai pengganti window"
  - "Mengakses informasi sistem operasi dan runtime melalui objek process"
  - "Membaca argumen baris perintah menggunakan process.argv dan direktori kerja process.cwd()"
codeLanguage: javascript
codeSnippet: |
  // Membaca identitas proses dan sistem operasi
  console.log(`Platform OS   : ${process.platform}`);
  console.log(`Node Version  : ${process.version}`);
  console.log(`Direktori Run : ${process.cwd()}`);
  console.log(`Penggunaan RAM: ${Math.round(process.memoryUsage().heapUsed / 1024 / 1024)} MB`);
---

# Objek Global, process, dan Akses Lingkungan Sistem

Di browser, objek tertinggi tempat segala fungsi dan variabel global bernaung adalah `window`. Jika kamu mengetik `setTimeout`, sebenarnya itu adalah `window.setTimeout`.

Lalu, bagaimana dengan Node.js yang tidak memiliki jendela browser? Node.js menyediakan padanannya sendiri, yaitu objek **`global`** (dan standar universal modern **`globalThis`**), serta sebuah objek pengendali sistem yang sangat krusial: **`process`**.

![Objek Global dan Process di Node.js](asset:images/be-node-01/global-dan-process.png)

---

## 1. Objek `global` dan `globalThis`

Segala hal bawaan yang bisa kamu panggil langsung tanpa perlu di-impor—seperti `console`, `setTimeout`, `setInterval`, dan `clearTimeout`—sebenarnya adalah properti dari objek `global`.

```javascript
console.log("=== MEMERIKSA KESETARAAN GLOBAL ===");

// setTimeout berada di bawah objek global
console.log(global.setTimeout === setTimeout);

// globalThis adalah standar JavaScript universal lintas runtime
console.log(globalThis === global);
```

Output:
```
=== MEMERIKSA KESETARAAN GLOBAL ===
true
true
```

> **Catatan Arsitektur:**
> Berbeda dengan browser di mana variabel `var nama = "Budi"` otomatis menempel ke `window.nama`, di Node.js variabel yang kamu deklarasikan di sebuah file **terisolasi di modul tersebut** dan tidak mencemari objek `global`. Ini mencegah terjadinya tabrakan nama variabel antar file!

---

## 2. Mengenal Objek `process`

Objek `process` adalah jembatan penghubung langsung antara aplikasi JavaScriptmu dengan sistem operasi tempat Node.js berjalan. Objek ini selalu tersedia di mana saja secara global tanpa perlu memanggil `require`.

Berikut beberapa properti dan fungsi terpenting pada `process`:

1. **`process.platform`**: Mengetahui sistem operasi server (`'linux'`, `'darwin'` untuk macOS, `'win32'` untuk Windows).
2. **`process.version`**: Mengetahui versi Node.js yang sedang berjalan.
3. **`process.cwd()`**: Singkatan dari *Current Working Directory*—mengetahui folder tempat perintah terminal dipanggil.
4. **`process.uptime()`**: Menghitung sudah berapa detik program server telah berjalan.
5. **`process.memoryUsage()`**: Mengukur konsumsi RAM oleh heap V8.

---

## 3. Membaca Argumen Terminal dengan `process.argv`

Pernahkah kamu melihat perintah terminal seperti ini?
```bash
node server.js --port 5000 --env production
```

Bagaimana program JavaScript di dalam file `server.js` membaca kata `--port 5000` tersebut? Jawabannya adalah melalui array **`process.argv`** (*Argument Vector*).

Perhatikan kode simulasi berikut:

```javascript
console.log("=== INSPEKSI ARGUMEN TERMINAL ===");

// process.argv mengembalikan array string
const argumen = process.argv;

console.log(`Jumlah Argumen: ${argumen.length}`);
console.log(`Elemen 0 (Path Node) : ${argumen[0]}`);
console.log(`Elemen 1 (Path File) : ${argumen[1]}`);

// Memeriksa jika ada parameter tambahan yang dikirimkan
if (argumen.length > 2) {
  console.log(`Argumen Pengguna     : ${argumen.slice(2).join(", ")}`);
} else {
  console.log("Tidak ada argumen tambahan yang diberikan.");
}
```

Jika program di atas dijalankan di terminal dengan perintah `node server.js mode=rahasia`:

Output:
```
=== INSPEKSI ARGUMEN TERMINAL ===
Jumlah Argumen: 3
Elemen 0 (Path Node) : /usr/local/bin/node
Elemen 1 (Path File) : /app/server.js
Argumen Pengguna     : mode=rahasia
```

### Aturan Indeks `process.argv`:
- **Indeks 0**: Selalu berisi path absolut ke executable program `node`.
- **Indeks 1**: Selalu berisi path absolut ke file skrip JavaScript yang sedang dijalankan.
- **Indeks 2 ke atas**: Berisi seluruh argumen tambahan yang diketikkan pengguna di terminal!

---

## 4. Rangkuman Section 2

Kita telah menuntaskan pemahaman esensial mengenai Node.js:
1. Node.js adalah runtime berbasis V8 dan Libuv untuk mengeksekusi JavaScript di sisi server.
2. Menggunakan arsitektur *single-threaded non-blocking* dan *Event Loop*.
3. REPL untuk eksperimen kilat, sedangkan file skrip untuk program terstruktur.
4. Objek `process` memberikan kontrol langsung terhadap sistem operasi, argumen terminal, dan daur hidup aplikasi.

Di **Section 3** berikutnya, kita akan masuk ke materi paling krusial bagi arsitektur perangkat lunak: **Sistem Modul, Manipulasi File System, dan Penanganan Eksekusi Asinkron!**
