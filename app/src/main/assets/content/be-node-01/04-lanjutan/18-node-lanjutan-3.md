---
id: node-lanjutan-3
courseId: be-node-01
order: 18
title: "Routing Sederhana & Penanganan URL pada HTTP Server"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami konsep routing sebagai pengatur lalu lintas endpoint URL di sisi server"
  - "Mengurai req.url dan req.method menggunakan struktur percabangan if/else atau switch"
  - "Menangani respon halaman tidak ditemukan (HTTP 404 Not Found)"
codeLanguage: javascript
codeSnippet: |
  const http = require('node:http');

  const server = http.createServer((req, res) => {
    if (req.url === '/' && req.method === 'GET') {
      res.end('Halaman Utama');
    } else if (req.url === '/tentang' && req.method === 'GET') {
      res.end('Tentang Kami');
    } else {
      res.statusCode = 404;
      res.end('404 Halaman Tidak Ditemukan');
    }
  });
---

# Routing Sederhana & Penanganan URL pada HTTP Server

Sebuah aplikasi web nyata tidak hanya memiliki satu halaman. Aplikasi memiliki halaman utama (`/`), halaman profil pengguna (`/profil`), katalog produk (`/produk`), dan layanan data lainnya.

Tugas mengarahkan pengguna ke konten yang tepat berdasarkan alamat URL dan metode HTTP yang mereka minta disebut sebagai **Routing** (*Perutean*).

![Diagram Routing URL Server HTTP](asset:images/be-node-01/routing-http-server.png)

---

## 1. Mekanisme Routing Tingkat Rendah

Di dalam modul bawaan `node:http`, kita melakukan routing secara mandiri dengan memeriksa nilai string pada:
1. **`req.url`**: Jalur alamat yang diminta pengguna.
2. **`req.method`**: Metode permintaan (misalnya `GET` untuk meminta data, `POST` untuk mengirim data baru).

Mari kita bangun router sederhana menggunakan logika `if / else`:

```javascript
const http = require('node:http');

const server = http.createServer((req, res) => {
  const method = req.method;
  const url = req.url;

  console.log(`[ROUTE MAPPING] Menerima: ${method} ${url}`);

  // Header default untuk teks UTF-8
  res.setHeader('Content-Type', 'text/plain; charset=utf-8');

  // Percabangan rute
  if (url === '/' && method === 'GET') {
    res.statusCode = 200;
    res.end('Beranda: Selamat datang di portal utama kami!');
  } 
  else if (url === '/status' && method === 'GET') {
    res.statusCode = 200;
    res.end('Status Layanan: Semua sistem backend beroperasi normal.');
  } 
  else if (url === '/kontak' && method === 'GET') {
    res.statusCode = 200;
    res.end('Kontak: Hubungi tim kami di support@codeacademy.id');
  } 
  else {
    // Jalur fallback: Jika alamat tidak dikenali
    res.statusCode = 404;
    res.end('Kesalahan 404: Rute yang kamu cari tidak ditemukan di server.');
  }
});

server.listen(4000, () => {
  console.log('Server Router aktif di port 4000');
});
```

---

## 2. Simulasi Alur Permintaan dan Respon

Mari kita lakukan simulasi mental jika server di atas menerima serangkaian request dari pengguna internet:

### Kasus 1: Pengguna membuka `http://localhost:4000/`
- `req.url` bernilai `'/'`, `req.method` bernilai `'GET'`.
- Kondisi percabangan pertama terpenuhi.
- Status Code: `200 OK`.
- Respon Klien: `"Beranda: Selamat datang di portal utama kami!"`.

### Kasus 2: Pengguna membuka `http://localhost:4000/status`
- `req.url` bernilai `'/status'`, `req.method` bernilai `'GET'`.
- Kondisi percabangan kedua terpenuhi.
- Status Code: `200 OK`.
- Respon Klien: `"Status Layanan: Semua sistem backend beroperasi normal."`.

### Kasus 3: Pengguna iseng membuka `http://localhost:4000/halaman-rahasia`
- Alamat tidak cocok dengan rute manapun.
- Program jatuh ke blok `else`.
- Status Code: `404 Not Found`.
- Respon Klien: `"Kesalahan 404: Rute yang kamu cari tidak ditemukan di server."`.

---

## 3. Pentingnya Menjaga Status Code yang Akurat

Mengapa kita harus repot-repot mengatur `res.statusCode = 404`?
Karena mesin peramban, aplikasi mobile (Android/iOS), dan mesin pencari Google membaca angka status code tersebut untuk mengetahui kondisi halaman secara otomatis. Jika halaman tidak ditemukan tetapi servermu tetap membalas dengan status code `200`, aplikasi klien akan tertipu dan mengira data berhasil dimuat.

Di modul berikutnya, kita akan mempelajari bagaimana cara aplikasi modern berkomunikasi: **Membaca Data Request POST dan Mengirimkan Respons Format JSON!**
