---
id: node-lanjutan-7
courseId: be-node-01
order: 22
title: "Variabel Lingkungan (process.env) & Tebak Output Server"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Memahami pentingnya variabel lingkungan (environment variables) untuk konfigurasi dinamis dan rahasia aplikasi"
  - "Membaca nilai konfigurasi dari process.env beserta penetapan nilai fallback default"
  - "Menguji intuisi alur request-response HTTP dan routing dengan latihan tebak output"
codeLanguage: javascript
codeSnippet: |
  // Konfigurasi dinamis dengan nilai default (fallback)
  const PORT = process.env.PORT || 3000;
  const NODE_ENV = process.env.NODE_ENV || 'development';

  console.log(`Menjalankan server pada port: ${PORT} (Mode: ${NODE_ENV})`);
---

# Variabel Lingkungan (process.env) & Tebak Output Server

Jangan pernah menuliskan nomor port yang kaku, kata sandi database, atau kunci rahasia API langsung di dalam file kode sumber JavaScriptmu (*hardcoding*). Mengapa? Karena kode tersebut akan diunggah ke repositori Git di mana orang lain bisa melihatnya, dan nomor port di komputer lokalmu belum tentu sama dengan nomor port yang disediakan oleh penyedia cloud (seperti AWS, GCP, atau Heroku).

Solusi standar industri untuk masalah ini adalah **Variabel Lingkungan (*Environment Variables*)** yang dibaca melalui **`process.env`**.

![Variabel Lingkungan process.env dan Siklus Request](asset:images/be-node-01/process-env-dan-server.png)

---

## 1. Membaca Konfigurasi Dinamis dengan `process.env`

Objek `process.env` otomatis berisi seluruh variabel lingkungan yang dikirimkan oleh sistem operasi sebelum Node.js dijalankan.

Pola terbaik adalah selalu menyediakan **nilai cadangan (*fallback*)** menggunakan operator `||`:

```javascript
console.log("=== MEMBACA VARIABEL LINGKUNGAN ===");

// Jika penyedia cloud menyediakan process.env.PORT, pakai itu. Jika tidak, pakai 3000
const PORT = process.env.PORT || 3000;

// Menentukan mode kerja: development atau production
const MODE = process.env.NODE_ENV || "development";

console.log(`Port Server : ${PORT}`);
console.log(`Mode Kerja  : ${MODE}`);

if (MODE === "production") {
  console.log("Status Keamanan: Mode ketat diaktifkan.");
} else {
  console.log("Status Keamanan: Mode pengembangan lokal.");
}
```

Output (jika dijalankan biasa tanpa variabel khusus):
```
=== MEMBACA VARIABEL LINGKUNGAN ===
Port Server : 3000
Mode Kerja  : development
Status Keamanan: Mode pengembangan lokal.
```

Namun jika dijalankan di terminal dengan menyuntikkan variabel di depannya:
```bash
PORT=8000 NODE_ENV=production node server.js
```

Maka output seketika berubah menjadi:
```
=== MEMBACA VARIABEL LINGKUNGAN ===
Port Server : 8000
Mode Kerja  : production
Status Keamanan: Mode ketat diaktifkan.
```
Tanpa perlu mengubah satu baris pun kode JavaScriptmu!

---

## 2. Latihan Interaktif: Tebak Output Server HTTP

Mari kita uji kemampuan analisismu dalam membaca alur server HTTP lengkap dengan routing, header, dan status code.

Amati kode server simulasi berikut:

```javascript
const http = require('node:http');

const PORT = process.env.PORT || 8080;

const server = http.createServer((req, res) => {
  const { method, url } = req;

  console.log(`[REQ] ${method} ${url}`);

  if (url === '/api/cek' && method === 'GET') {
    res.statusCode = 200;
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify({ status: 'ok', serverTime: 1700000000 }));
  } 
  else if (url === '/api/cek' && method === 'POST') {
    res.statusCode = 405; // 405 Method Not Allowed
    res.setHeader('Content-Type', 'text/plain');
    res.end('Metode POST tidak diizinkan pada rute ini');
  } 
  else {
    res.statusCode = 404;
    res.end('Halaman Tidak Ditemukan');
  }
});

// Simulasi dua panggilan masuk berturut-turut
console.log(`Server siap di port ${PORT}`);
```

### Skenario Uji:
Bayangkan server di atas menerima dua permintaan berturut-turut:
1. Permintaan 1: `POST /api/cek`
2. Permintaan 2: `GET /api/bantuan`

**Pertanyaan:**
1. Apa kode status HTTP yang dikembalikan untuk Permintaan 1?
2. Apa tipe konten (*Content-Type*) dan teks balasan untuk Permintaan 1?
3. Apa kode status HTTP yang dikembalikan untuk Permintaan 2?

*Catat tebakan analisismu sebelum membaca pembahasan di bawah garis pemisah!*

---

## Kunci Jawaban & Pembahasan Lengkap

### Jawaban Permintaan 1 (`POST /api/cek`):
1. **Status Code**: `405 Method Not Allowed`
2. **Content-Type**: `text/plain`
3. **Isi Respon**: `"Metode POST tidak diizinkan pada rute ini"`

**Alasan:**
Meskipun URL-nya cocok (`/api/cek`), metode yang dikirimkan adalah `POST`. Logika program memeriksa kondisi `else if (url === '/api/cek' && method === 'POST')` yang secara eksplisit mengatur status code 405 untuk menginformasikan klien bahwa endpoint ini hanya bisa dibaca dengan GET.

### Jawaban Permintaan 2 (`GET /api/bantuan`):
1. **Status Code**: `404 Not Found`
2. **Content-Type**: `text/plain` (default bawaan node)
3. **Isi Respon**: `"Halaman Tidak Ditemukan"`

**Alasan:**
URL `/api/bantuan` tidak terdaftar dalam percabangan manapun, sehingga alur langsung meluncur ke blok penutup `else` dengan status code 404.

---

Jika tebakanmu tepat, kamu telah menguasai fondasi arsitektur jaringan web di Node.js!

Di **Section 5 (Evaluasi)** berikutnya, kita akan mengintegrasikan seluruh ilmu yang kamu dapatkan ke dalam: **Mini Proyek REST API Catatan (*Notes API*)!**
