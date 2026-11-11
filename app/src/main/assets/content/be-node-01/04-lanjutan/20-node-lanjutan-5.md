---
id: node-lanjutan-5
courseId: be-node-01
order: 20
title: "Pengantar Framework Express: Konsep & Minimal Setup"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami alasan mengapa industri menggunakan framework seperti Express.js di atas Node.js"
  - "Memahami konsep pipeline Middleware: (req, res, next)"
  - "Melihat perbandingan penulisan routing deklaratif Express vs modul bawaan http"
codeLanguage: javascript
codeSnippet: |
  // Konsep aplikasi Express minimal
  const express = require('express');
  const app = express();

  app.use(express.json()); // Middleware otomatis pengurai JSON

  app.get('/api/salam', (req, res) => {
    res.json({ pesan: "Halo dari Express API!" });
  });

  app.listen(3000, () => console.log("Server Express siap di port 3000"));
---

# Pengantar Framework Express: Konsep & Minimal Setup

Setelah di dua modul sebelumnya kamu merasakan sendiri betapa detailnya menulis server menggunakan modul bawaan `node:http` (mengurai stream secara manual, mencocokkan string URL, mengatur status code dan header satu per satu), kamu mungkin berpikir: *"Apakah tidak ada cara yang lebih ringkas dan elegan?"*

Tentu ada! Di industri back-end profesional, kita menggunakan **Web Framework**. Dan framework yang paling legendaris, ringan, serta banyak dipakai di dunia Node.js adalah **Express.js**.

![Konsep Pipeline Middleware Express](asset:images/be-node-01/express-middleware-flow.png)

---

## 1. Filosofi Express: Pembungkus Manis di Atas Modul `http`

Express bukanlah bahasa baru. Express hanyalah sebuah pustaka JavaScript biasa yang dibangun persis di atas modul bawaan `node:http`. 

Tugas Express adalah menyederhanakan kode yang berulang-ulang (*boilerplate code*) menjadi fungsi-fungsi pendek yang sangat ekspresif dan mudah dibaca oleh manusia.

---

## 2. Konsep Sakti: Middleware Pipeline

Konsep paling fundamental dari Express adalah **Middleware**. 

Bayangkan sebuah pabrik perakitan mobil:
Ketika mobil berjalan di ban berjalan (*conveyor belt*), mobil tersebut melewati beberapa pos kerja berturut-turut:
1. Pos 1 memasang roda.
2. Pos 2 mengecat bodi.
3. Pos 3 memeriksa mesin sebelum mobil diserahkan ke pembeli.

Di Express, setiap permintaan klien (`req`) melewati pipa perakitan serupa:
```javascript
function middlewareSaya(req, res, next) {
  // 1. Lakukan sesuatu pada req atau res (misal: cek login, catat log)
  console.log(`[AUDIT] Akses rute pada: ${new Date().toISOString()}`);

  // 2. Oper ke pos pekerja berikutnya!
  next();
}
```

Jika fungsi `next()` dipanggil, permintaan akan diteruskan ke middleware atau rute berikutnya. Jika tidak dipanggil, proses berhenti di pos tersebut (misalnya jika pengguna belum login, middleware langsung membalas dengan status 401 Unauthorized).

---

## 3. Perbandingan Kode: Modul `http` Bawaan vs Express

Perhatikan bagaimana Express mengubah kode yang rumit menjadi sangat sederhana:

### A. Versi Modul `http` Bawaan (Manual)
```javascript
// Membutuhkan pengumpulan chunk manual dan penulisan header
if (req.url === '/api/buku' && req.method === 'GET') {
  res.statusCode = 200;
  res.setHeader('Content-Type', 'application/json');
  res.end(JSON.stringify(daftarBuku));
}
```

### B. Versi Express.js (Deklaratif & Ringkas)
```javascript
// Express menyediakan method khusus yang langsung membaca URL dan mengirim JSON
app.get('/api/buku', (req, res) => {
  res.json(daftarBuku); // Otomatis mengatur status 200 & Content-Type application/json!
});
```

---

## 4. Contoh Skrip Minimal Server Express

Perhatikan kode lengkap aplikasi Express sederhana berikut:

```javascript
const express = require('express');
const app = express();
const PORT = 3000;

// Middleware bawaan untuk mem-parsing body JSON secara otomatis (tanpa req.on('data')!)
app.use(express.json());

// Database simulasi di memori
const artikel = [
  { id: 1, judul: "Pengenalan Node.js untuk Pemula" },
  { id: 2, judul: "Arsitektur Event Loop Modern" }
];

// 1. Endpoint GET: Mengambil seluruh artikel
app.get('/artikel', (req, res) => {
  res.status(200).json({ status: "sukses", data: artikel });
});

// 2. Endpoint POST: Menambah artikel baru
app.post('/artikel', (req, res) => {
  const judulBaru = req.body.judul; // req.body langsung tersedia berkat middleware!
  const dataBaru = { id: artikel.length + 1, judul: judulBaru };
  artikel.push(dataBaru);
  
  res.status(201).json({ status: "dibuat", data: dataBaru });
});

app.listen(PORT, () => {
  console.log(`Server Express menyala di port ${PORT}`);
});
```

Output saat server pertama kali dijalankan di terminal:
```
Server Express menyala di port 3000
```

Dengan menguasai modul bawaan `node:http` terlebih dahulu, kamu kini memahami apa yang sesungguhnya terjadi di balik kemudahan Express. Kamu bukan sekadar "pengetik kode framework", melainkan insinyur yang memahami arsitektur di bawahnya!

Di modul berikutnya, kita akan membahas hal yang sangat penting untuk keandalan server: **Strategi Penanganan Error agar Server Tidak Tumbang!**
