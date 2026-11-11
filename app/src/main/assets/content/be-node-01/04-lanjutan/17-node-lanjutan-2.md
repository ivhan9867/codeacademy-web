---
id: node-lanjutan-2
courseId: be-node-01
order: 17
title: "Membangun Web Server Pertama dengan Modul Bawaan http"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Memahami cara kerja modul bawaan node:http dalam membuka port jaringan TCP"
  - "Membedah objek Request (req) dan Response (res)"
  - "Mengatur HTTP status code, response header, dan mengakhiri aliran data dengan res.end()"
codeLanguage: javascript
codeSnippet: |
  const http = require('node:http');

  const server = http.createServer((req, res) => {
    res.statusCode = 200;
    res.setHeader('Content-Type', 'text/plain');
    res.end('Halo dari Server Node.js!');
  });

  server.listen(3000, () => {
    console.log('Server berjalan di http://localhost:3000');
  });
---

# Membangun Web Server Pertama dengan Modul Bawaan http

Saat kamu mengetik sebuah alamat di browser ponsel atau laptopmu, browsermu mengirimkan surat permintaan (*HTTP Request*) melintasi kabel internet ke sebuah komputer server. Komputer server tersebut membaca surat tersebut, memprosesnya, lalu mengirimkan surat balasan (*HTTP Response*).

Dengan modul bawaan **`node:http`**, komputermu bisa langsung bertindak sebagai komputer server tersebut hanya dalam belasan baris kode!

![Anatomi Web Server HTTP Bawaan](asset:images/be-node-01/http-web-server.png)

---

## 1. Anatomi `http.createServer()`

Mari kita bedah fungsi paling ikonik di seluruh ekosistem Node.js:

```javascript
const http = require('node:http');

// 1. Membuat server dan mendaftarkan fungsi penangan request (Request Listener)
const server = http.createServer((req, res) => {
  console.log(`[LOG] Permintaan masuk: ${req.method} ${req.url}`);

  // Mengatur kode status HTTP (200 artinya Sukses/OK)
  res.statusCode = 200;

  // Mengatur header (memberitahu browser format data balasan)
  res.setHeader('Content-Type', 'text/html; charset=utf-8');

  // Mengirim isi balasan dan menutup koneksi
  res.end('<h1>Selamat Datang di Server Backend Node.js!</h1><p>Dibuat murni tanpa framework.</p>');
});

// 2. Membuka port jaringan (misalnya port 3000)
const PORT = 3000;
server.listen(PORT, () => {
  console.log(`=== SERVER AKTIF ===`);
  console.log(`Mendengarkan permintaan pada port ${PORT}...`);
});
```

Jika kode di atas dijalankan di komputer lalu ada pengguna yang membuka peramban ke alamat `http://localhost:3000`, terminal server akan mencetak:

Output:
```
=== SERVER AKTIF ===
Mendengarkan permintaan pada port 3000...
[LOG] Permintaan masuk: GET /
```

---

## 2. Memahami Dua Karakter Utama: `req` dan `res`

Fungsi callback di dalam `http.createServer` selalu menerima dua objek istimewa:

### A. Objek Request (`req`)
Merupakan representasi dari surat yang dikirimkan oleh klien. Kamu bisa membaca:
- `req.method`: Metode HTTP yang digunakan klien (`'GET'`, `'POST'`, `'PUT'`, `'DELETE'`).
- `req.url`: Alamat URL yang diminta (misalnya `'/'` atau `'/produk'`).
- `req.headers`: Header pengirim (seperti informasi tipe browser atau token otorisasi).

### B. Objek Response (`res`)
Merupakan kertas kosong yang akan kamu isi untuk dikirimkan kembali ke klien:
- `res.statusCode = 200`: Memberikan tanda status apakah permintaan berhasil (`200`), data salah (`400`), ditolak (`403`), atau tidak ditemukan (`404`).
- `res.setHeader(kunci, nilai)`: Memberi label informasi seperti jenis konten (`'text/plain'`, `'text/html'`, `'application/json'`).
- `res.end(isi)`: **Wajib dipanggil!** Mengirim potongan data terakhir dan memberi tahu peramban bahwa respon sudah selesai. Jika kamu lupa memanggil `res.end()`, peramban klien akan terus berputar memuat (*loading*) tanpa henti hingga waktu koneksi habis (*timeout*).

---

## 3. Menghidupkan Siklus Non-Stop Event Loop

Perhatikan bahwa skrip server ini **tidak langsung berhenti** setelah baris terakhir dibaca. 

Mengapa? Karena pemanggilan method `server.listen(PORT)` mendaftarkan sebuah *Socket Listener* aktif pada Event Loop. Event Loop akan terus berjaga selama 24 jam nonstop untuk mendengarkan panggilan jaringan yang masuk, sampai proses dimatikan secara manual dengan `Ctrl + C`.

Di modul berikutnya, kita akan membuat server ini lebih pintar: **Menangani Berbagai Alamat URL (*Routing*)!**
