---
id: node-lanjutan-4
courseId: be-node-01
order: 19
title: "Membaca Payload Request & Mengirim Respons JSON"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami cara membaca stream request body menggunakan event 'data' dan 'end'"
  - "Mem-parsing payload mentah bertipe JSON menjadi objek JavaScript dengan JSON.parse()"
  - "Mengirimkan format respon REST API standar (application/json) via res.setHeader"
codeLanguage: javascript
codeSnippet: |
  const http = require('node:http');

  const server = http.createServer((req, res) => {
    if (req.method === 'POST' && req.url === '/api/pesan') {
      let bufferTeks = '';

      // Mengumpulkan potongan paket data jaringan
      req.on('data', (chunk) => {
        bufferTeks += chunk.toString();
      });

      // Setelah seluruh paket selesai diterima
      req.on('end', () => {
        const payload = JSON.parse(bufferTeks);
        res.setHeader('Content-Type', 'application/json');
        res.end(JSON.stringify({ status: 'berhasil', pesanDiterima: payload }));
      });
    }
  });
---

# Membaca Payload Request & Mengirim Respons JSON

Sebagian besar aplikasi mobile dan antarmuka web modern tidak lagi meminta file HTML utuh dari server. Mereka berkomunikasi menggunakan format pertukaran data standar dunia: **JSON (*JavaScript Object Notation*)**.

Ketika seorang pengguna mengirimkan formulir pendaftaran akun (metode `POST`), data yang dikirimkan tidak datang sekaligus dalam satu detik, melainkan mengalir secara bertahap dalam bentuk paket-paket data kecil (*Stream Chunks*).

![Aliran Stream Request dan Respons JSON](asset:images/be-node-01/request-payload-json.png)

---

## 1. Membaca Request Body Berbasis Stream

Di Node.js murni, objek `req` (*Request*) adalah turunan dari `EventEmitter` dan `Stream`. Untuk membaca isi data yang dikirimkan klien, kita mendengarkan dua event penting:

1. **`req.on('data', chunk => ...)`**: Dipicu setiap kali ada pecahan paket data yang berhasil tiba di port server. Kita mengumpulkannya ke dalam sebuah variabel penampung string.
2. **`req.on('end', () => ...)`**: Dipicu satu kali saat seluruh potongan data selesai dikirimkan oleh klien. Di sinilah saat yang tepat untuk mem-parsing data tersebut!

---

## 2. Implementasi Endpoint API REST Penerima Data

Perhatikan bagaimana kita membangun endpoint `/api/pengguna` yang menerima data POST dan membalasnya dengan JSON:

```javascript
const http = require('node:http');

const server = http.createServer((req, res) => {
  // Hanya menerima metode POST pada rute /api/pengguna
  if (req.url === '/api/pengguna' && req.method === 'POST') {
    let bodyRaw = '';

    // 1. Kumpulkan pecahan data jaringan
    req.on('data', (chunk) => {
      bodyRaw += chunk.toString();
    });

    // 2. Proses data setelah seluruh paket tiba
    req.on('end', () => {
      try {
        const dataPengguna = JSON.parse(bodyRaw);
        console.log(`[API] Berhasil menerima data untuk: ${dataPengguna.nama}`);

        // Buat objek balasan
        const responseData = {
          sukses: true,
          kode: 201,
          pesan: "Pengguna baru berhasil didaftarkan!",
          data: {
            id: "USR-" + Math.floor(Math.random() * 1000),
            nama: dataPengguna.nama,
            email: dataPengguna.email,
            didaftarkanPada: "2025-10-25"
          }
        };

        // 3. Kirim respon JSON ke klien
        res.statusCode = 201; // 201 Created
        res.setHeader('Content-Type', 'application/json; charset=utf-8');
        res.end(JSON.stringify(responseData));

      } catch (error) {
        // Jika data yang dikirimkan klien bukan JSON yang valid
        res.statusCode = 400; // 400 Bad Request
        res.setHeader('Content-Type', 'application/json');
        res.end(JSON.stringify({ sukses: false, pesan: "Format JSON tidak valid!" }));
      }
    });
  } else {
    res.statusCode = 404;
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify({ sukses: false, pesan: "Endpoint tidak ditemukan" }));
  }
});

server.listen(5000, () => {
  console.log('API Server aktif mendengarkan di port 5000');
});
```

---

## 3. Simulasi Alur Request dan Response Terminal

Jika sebuah aplikasi ponsel mengirimkan HTTP Request:
```http
POST /api/pengguna HTTP/1.1
Host: localhost:5000
Content-Type: application/json

{
  "nama": "Dewi Sartika",
  "email": "dewi@example.com"
}
```

Maka terminal server akan mencetak:
```
[API] Berhasil menerima data untuk: Dewi Sartika
```

Dan ponsel klien akan menerima paket data balasan berformat JSON yang sangat rapi:
```json
{
  "sukses": true,
  "kode": 201,
  "pesan": "Pengguna baru berhasil didaftarkan!",
  "data": {
    "id": "USR-742",
    "nama": "Dewi Sartika",
    "email": "dewi@example.com",
    "didaftarkanPada": "2025-10-25"
  }
}
```

Meskipun modul `http` bawaan Node.js ini sangat hebat, menulis pengumpulan stream `req.on('data')` dan routing `if/else` secara berulang-ulang untuk puluhan rute bisa menjadi sangat melelahkan.

Di sinilah peran penting dari framework web paling terkenal di jagat Node.js: **Express.js** yang akan kita bahas di modul berikutnya!
