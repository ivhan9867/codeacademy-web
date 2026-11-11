---
id: node-inti-2
courseId: be-node-01
order: 9
title: "ES Modules (ESM): import dan export Modern di Node.js"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami sintaks standar ECMAScript Modules (import dan export)"
  - "Mengetahui cara mengaktifkan ESM di Node.js via package.json ('type': 'module') atau ekstensi .mjs"
  - "Membedakan karakteristik teknis CommonJS vs ES Modules"
codeLanguage: javascript
codeSnippet: |
  // File: format.mjs (ESM)
  export const formatRupiah = (nominal) => `Rp ${nominal.toLocaleString("id-ID")}`;
  export default function cetakHeader(judul) {
    console.log(`=== ${judul} ===`);
  }

  // File: index.mjs
  import cetakHeader, { formatRupiah } from './format.mjs';
  cetakHeader("LAPORAN");
  console.log(formatRupiah(50000));
---

# ES Modules (ESM): import dan export Modern di Node.js

Pada tahun 2015, komite standar resmi JavaScript (ECMAScript) meluncurkan sistem modul standar universal bernama **ES Modules (ESM)** dengan sintaks `import` dan `export`. 

Sintaks ini sekarang menjadi standar resmi yang didukung secara penuh oleh browser modern maupun Node.js versi LTS terkini.

![Perbedaan CommonJS vs ES Modules](asset:images/be-node-01/commonjs-vs-esm.png)

---

## 1. Dua Cara Mengaktifkan ESM di Node.js

Secara default, Node.js memperlakukan file `.js` sebagai modul CommonJS. Agar Node.js tahu bahwa kita ingin menggunakan sintaks `import/export`, ada dua cara yang bisa digunakan:

1. **Menggunakan Ekstensi `.mjs`**: Beri nama filemu dengan akhiran `.mjs` (misalnya `server.mjs` dan `helper.mjs`).
2. **Menambahkan `"type": "module"` di `package.json`**: Menentukan seluruh file `.js` di proyek tersebut berjalan dalam mode ES Modules.

---

## 2. Sintaks Named Export dan Default Export

Di dalam ES Modules, kita mengenal dua jenis ekspor:

### A. Named Export (Bisa banyak per file)
Digunakan saat kita ingin mengekspor beberapa variabel atau fungsi secara terpisah:

```javascript
// stringUtils.mjs
export const versi = "2.0";

export function ubahKeHurufBesar(teks) {
  return teks.toUpperCase();
}

export function potongTeks(teks, panjang) {
  return teks.slice(0, panjang) + "...";
}
```

### B. Default Export (Maksimal satu per file)
Digunakan untuk mengekspor fungsi atau class utama dari sebuah modul:

```javascript
// logger.mjs
export default function logSistem(pesan) {
  const timestamp = "2025-10-25 10:00:00";
  console.log(`[${timestamp}] INFO: ${pesan}`);
}
```

---

## 3. Mengimpor Modul ESM

Di file utama kita mengimpor kedua modul tersebut:

```javascript
// main.mjs
// 1. Mengimpor default export (nama variabel bebas)
import logSistem from './logger.mjs';

// 2. Mengimpor named export (nama harus persis sama, di dalam kurung kurawal)
import { versi, ubahKeHurufBesar, potongTeks } from './stringUtils.mjs';

logSistem("Memulai inisialisasi aplikasi...");
console.log(`Versi Utilitas: ${versi}`);

const judulArtikel = "Tutorial Membangun Server Backend dengan Node.js";
console.log(ubahKeHurufBesar("selamat datang"));
console.log(potongTeks(judulArtikel, 25));
```

Output:
```
[2025-10-25 10:00:00] INFO: Memulai inisialisasi aplikasi...
Versi Utilitas: 2.0
SELAMAT DATANG
Tutorial Membangun Server...
```

> **Perhatian Khusus ESM:**
> Berbeda dengan CommonJS yang membolehkan kamu menghilangkan ekstensi file (misal `require('./logger')`), di ES Modules kamu **wajib menuliskan ekstensi file secara lengkap**, seperti `import ... from './logger.mjs'`.

---

## 4. Perbandingan Cepat: CommonJS vs ES Modules

| Fitur | CommonJS (CJS) | ES Modules (ESM) |
| :--- | :--- | :--- |
| **Sintaks Impor** | `const x = require('./x')` | `import x from './x.mjs'` |
| **Sintaks Ekspor** | `module.exports = ...` | `export const ...` / `export default` |
| **Waktu Pemuatan** | Sinkron (saat kode berjalan / *runtime*) | Asinkron & dianalisis sebelum kode jalan (*parse time*) |
| **Ekstensi Wajib** | Tidak wajib (opsional) | **Wajib** mencantumkan ekstensi (`.js`/`.mjs`) |
| **`__dirname` & `__filename`** | Tersedia langsung | Tidak tersedia langsung (diganti `import.meta.url`) |

Di modul berikutnya, kita akan menjelajahi senjata bawaan Node.js yang sangat sering dipakai sehari-hari: **Modul Path dan Modul OS!**
