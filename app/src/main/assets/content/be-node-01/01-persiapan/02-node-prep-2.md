---
id: node-prep-2
courseId: be-node-01
order: 2
title: "Peta Materi & Roadmap Back-End Developer"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Memetakan 5 section materi dari dasar runtime hingga pembuatan server HTTP"
  - "Memahami peran Node.js dalam siklus pengembangan aplikasi full-stack modern"
  - "Mengetahui tonggak pencapaian belajar di setiap fase kelas"
codeLanguage: javascript
codeSnippet: |
  // Gambaran modul dan fungsionalitas di Node.js
  const kurikulum = {
    fase: "Node.js Fundamentals",
    totalSection: 5,
    topikKunci: ["V8 Engine", "Non-Blocking I/O", "CommonJS/ESM", "fs & http", "REST API"]
  };

  console.log(`Fokus Pembelajaran: ${kurikulum.topikKunci.join(" -> ")}`);
---

# Peta Materi & Roadmap Back-End Developer

Dalam arsitektur web modern, aplikasi terbagi menjadi dua sisi utama: **Front-End** (antarmuka yang dilihat pengguna di peramban) dan **Back-End** (server yang memproses aturan bisnis, memvalidasi keamanan, serta berinteraksi dengan database dan file sistem). 

Node.js adalah salah satu teknologi terpopuler di dunia yang memungkinkan kamu menggunakan satu bahasa yang sama—**JavaScript**—di kedua sisi tersebut.

![Peta Kurikulum Node.js Fundamentals](asset:images/be-node-01/roadmap-node-fundamentals.png)

---

## 1. Lima Tahapan Belajar Kita

Kurikulum ini disusun secara berjenjang menjadi 5 section terarah dengan total 24 modul:

```
[01. Persiapan]
  └── Aturan main, metode baca & tebak, setup komputer opsional
       ↓
[02. Memahami Node.js]
  └── V8 engine, perbedaan browser vs server, Event Loop, REPL, objek process
       ↓
[03. Modul, File & Asinkron]
  └── CommonJS vs ESM, path & os, manipulasi fs (sinkron vs asinkron), EventEmitter, urutan Event Loop
       ↓
[04. Server & npm]
  └── npm & package.json, membuat HTTP server native, routing URL, request parsing, pengantar Express
       ↓
[05. Proyek & Evaluasi]
  └── Mini proyek REST API Catatan (Notes API), rangkuman best practice & karir lanjutan
```

Mari kita bedah apa yang akan kamu kuasai di masing-masing fase:

### Fase 1: Persiapan (Modul 01 - 03)
Kamu membangun pola pikir *mental tracing*, memahami batasan lingkungan aplikasi, dan mempelajari cara menjalankan Node.js secara mandiri di komputer pribadimu jika ingin bereksperimen lebih jauh.

### Fase 2: Memahami Node.js (Modul 04 - 07)
Kamu akan memahami mengapa Node.js diciptakan: membebaskan JavaScript dari kurungan browser dengan mesin V8, arsitektur *non-blocking I/O*, bagaimana libuv mengelola tugas latar belakang, serta memanfaatkan objek `process` untuk berinteraksi dengan sistem operasi.

### Fase 3: Modul, File & Asinkron (Modul 08 - 15)
Jantung pemrograman Node.js berada di sini:
- Bagaimana memecah kode besar menjadi modul-modul rapi menggunakan **CommonJS** (`require`) dan **ES Modules** (`import`).
- Membaca dan menulis file di hard drive menggunakan modul bawaan `fs`.
- Menyelami cara kerja asinkron modern dengan `Promise` dan `async/await`.
- Membedah urutan eksekusi antrean mikro dan makro di **Event Loop** melalui latihan interaktif tebak output.

### Fase 4: Server & npm (Modul 16 - 22)
Kamu bertransformasi menjadi insinyur jaringan server:
- Mengelola dependensi pihak ketiga menggunakan **npm** (*Node Package Manager*).
- Menghidupkan server web dari nol menggunakan modul bawaan `http`.
- Membaca alamat URL (*routing*), mengurai data JSON yang dikirimkan klien, dan membalasnya dengan status code yang tepat.
- Mengenal filosofi framework **Express** dan strategi mengamankan server dari crash.

### Fase 5: Proyek & Evaluasi (Modul 23 - 24)
Kamu akan membaca dan menelusuri arsitektur sebuah proyek mini **REST API Catatan (Notes API)** utuh langkah demi langkah, melihat bagaimana semua konsep yang telah dipelajari berpadu dengan harmonis.

---

## 2. Contoh Gambaran Alur Kode Back-End

Berikut adalah cuplikan kode yang memperlihatkan bagaimana beberapa modul bawaan Node.js saling berkolaborasi di dunia nyata:

```javascript
// Simulasi pembacaan arsitektur aplikasi
const konfigurasi = {
  namaAplikasi: "Portal Data Akademik",
  versi: "1.0.0",
  lingkungan: "development"
};

function statusLayanan(nama) {
  return `Layanan [${nama}] aktif dan siap melayani permintaan.`;
}

console.log(`=== ${konfigurasi.namaAplikasi} v${konfigurasi.versi} ===`);
console.log(`Mode Operasional: ${konfigurasi.lingkungan}`);
console.log(statusLayanan("Database File"));
console.log(statusLayanan("HTTP Gateway"));
```

Output:
```
=== Portal Data Akademik v1.0.0 ===
Mode Operasional: development
Layanan [Database File] aktif dan siap melayani permintaan.
Layanan [HTTP Gateway] aktif dan siap melayani permintaan.
```

---

## 3. Prasyarat yang Dibutuhkan

Karena kelas ini adalah kelanjutan dari **JavaScript Fundamentals (`fe-js-03`)**, kita tidak akan mengulang lagi sintaks dasar seperti:
- Deklarasi variabel (`let`, `const`)
- Tipe data primitif (string, number, boolean)
- Penggunaan array dan method dasar (`push`, `map`, `filter`)
- Struktur percabangan (`if/else`, `switch`) dan perulangan (`for`, `while`)
- Fungsi dasar dan *arrow function*

Jika ada bagian dasar tersebut yang kamu rasa masih belum kokoh, kamu selalu bisa membuka kembali materi di kelas *JavaScript Fundamentals*.

Di modul berikutnya, kita akan membahas cara menyiapkan compiler dan runtime Node.js di komputer pribadimu bagi kamu yang ingin mencobanya secara langsung!
