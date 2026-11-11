---
id: node-lanjutan-6
courseId: be-node-01
order: 21
title: "Strategi Penanganan Error & Global Exception Handler"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Membedakan synchronous error vs asynchronous error dalam aplikasi server"
  - "Memahami mengapa unhandled exception mematikan proses Node.js seketika"
  - "Menangkap error tak terduga secara global dengan process.on('uncaughtException') dan process.on('unhandledRejection')"
codeLanguage: javascript
codeSnippet: |
  process.on('uncaughtException', (err) => {
    console.error(`[FATAL CRASH PREVENTED] ${err.message}`);
    // Catat log, bersihkan koneksi, lalu matikan proses dengan aman
    process.exit(1);
  });

  process.on('unhandledRejection', (reason) => {
    console.error(`[UNHANDLED PROMISE] Alasan: ${reason}`);
  });
---

# Strategi Penanganan Error & Global Exception Handler

Di aplikasi antarmuka pengguna (seperti peramban atau ponsel), jika terjadi error JavaScript kecil di satu tombol, aplikasi biasanya masih bisa digunakan. Namun di lingkungan server, taruhannya jauh lebih besar:

> **Hukum Keras Server Node.js:**
> Karena Node.js berjalan pada satu proses utama (*single process*), jika sebuah error terjadi dan tidak ditangkap (*Unhandled Exception*), maka **seluruh proses Node.js akan mati seketika (*crash*)!** 
> Artinya, bukan hanya satu pengguna yang gagal bertransaksi, melainkan seluruh ribuan pengguna lain di seluruh dunia tiba-tiba akan mendapati server tidak bisa diakses!

Oleh karena itu, strategi penanganan error adalah benteng pertahanan terpenting bagi seorang insinyur back-end.

![Strategi Penanganan Error Server](asset:images/be-node-01/error-handling-strategies.png)

---

## 1. Menangani Error Sinkron dengan `try...catch`

Untuk kode yang dieksekusi langsung di Call Stack (seperti parsing string JSON atau operasi matematika), blok standar `try...catch` adalah pelindung utamamu:

```javascript
console.log("1. Menerima kiriman data mentah dari pengguna...");

const payloadKotor = "{ nama: 'Budi' "; // Kurang tanda tutup kurung kurawal (JSON Rusak!)

try {
  const data = JSON.parse(payloadKotor);
  console.log("Data berhasil diproses:", data);
} catch (error) {
  console.log(`[TERTANGKAP] Data cacat ditolak dengan aman: ${error.name} - ${error.message}`);
}

console.log("2. Server tetap tegak berdiri melayani request berikutnya!");
```

Output:
```
1. Menerima kiriman data mentah dari pengguna...
[TERTANGKAP] Data cacat ditolak dengan aman: SyntaxError - Expected property name or '}' in JSON at position 2
2. Server tetap tegak berdiri melayani request berikutnya!
```

---

## 2. Menangani Error Asinkron pada Promise / `async-await`

Pada fungsi asinkron modern, kegagalan (seperti file tidak ditemukan atau query database gagal) harus ditangkap menggunakan blok `try...catch` di dalam fungsi `async`, atau method `.catch()` pada Promise:

```javascript
const fs = require('node:fs/promises');

async function muatKonfigurasi() {
  try {
    const config = await fs.readFile('file-yang-tidak-ada.json', 'utf8');
    return JSON.parse(config);
  } catch (err) {
    console.log(`[PERINGATAN] File tidak ada, menggunakan konfigurasi default.`);
    return { port: 8080, mode: "fallback" };
  }
}

muatKonfigurasi().then(cfg => {
  console.log(`Server berjalan pada port: ${cfg.port}`);
});
```

Output:
```
[PERINGATAN] File tidak ada, menggunakan konfigurasi default.
Server berjalan pada port: 8080
```

---

## 3. Jaring Pengaman Terakhir: Global Exception Handler

Bagaimana jika ada error tak terduga yang lolos dari blok `try...catch` manapun? Node.js menyediakan dua event pengaman darurat pada objek `process`:

1. **`uncaughtException`**: Menangkap segala error sinkron yang luput ditangani.
2. **`unhandledRejection`**: Menangkap Promise yang gagal (*rejected*) namun tidak memiliki pemanggilan `.catch()`.

Perhatikan kode simulasi berikut:

```javascript
// Memasang jaring pengaman global
process.on('uncaughtException', (err) => {
  console.log(`[GLOBAL GUARD] Terjadi error fatal liar: ${err.message}`);
  console.log("Membersihkan resource dan mencatat log sebelum shutdown...");
  // Di produksi: kirim sinyal ke sistem monitoring, lalu restart proses
});

process.on('unhandledRejection', (alasan) => {
  console.log(`[PROMISE GUARD] Ada Promise tertolak tanpa .catch(): ${alasan}`);
});

console.log("A. Sistem menyala normal");

// Memicu unhandled rejection secara sengaja
Promise.reject("Koneksi database timeout setelah 5 detik");

console.log("B. Baris utama selesai");
```

Output:
```
A. Sistem menyala normal
B. Baris utama selesai
[PROMISE GUARD] Ada Promise tertolak tanpa .catch(): Koneksi database timeout setelah 5 detik
```

Dengan memasang jaring pengaman global ini, kamu bisa mencatat (*logging*) riwayat error secara akurat sebelum mengambil tindakan pemulihan yang tepat.

Di modul berikutnya, kita akan membahas cara mengamankan data sensitif dan konfigurasi dinamis: **Variabel Lingkungan (`process.env`) dan Latihan Tebak Output Server!**
