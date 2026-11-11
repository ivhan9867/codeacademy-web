---
id: node-eval-1
courseId: be-node-01
order: 23
title: "Mini Proyek: Membangun REST API Catatan (Notes API)"
readingMinutes: 8
section: EVALUASI
learningObjectives:
  - "Menggabungkan modul bawaan node:http dan node:fs/promises ke dalam arsitektur REST API utuh"
  - "Mengimplementasikan operasi CRUD data (Membaca semua catatan dan Menambah catatan baru)"
  - "Menelusuri simulasi siklus request-response dan persistensi data file JSON"
codeLanguage: javascript
codeSnippet: |
  const http = require('node:http');
  const fs = require('node:fs/promises');

  // Mini REST API Catatan tanpa framework eksternal
  const server = http.createServer(async (req, res) => {
    res.setHeader('Content-Type', 'application/json');
    if (req.url === '/api/catatan' && req.method === 'GET') {
      const data = await fs.readFile('catatan.json', 'utf8');
      res.end(data);
    }
  });
---

# Mini Proyek: Membangun REST API Catatan (Notes API)

Selamat! Kamu telah menempuh perjalanan panjang dari memahami mesin V8, arsitektur non-blocking, manipulasi modul dan file, hingga konsep server HTTP dan middleware. 

Di modul evaluasi pertama ini, kita akan menyatukan seluruh potongan puzzle tersebut untuk membangun sebuah aplikasi nyata: **REST API Catatan Mandiri (*Notes API*)**.

Karena kelas ini menggunakan metode **Baca & Tebak Output**, kita akan membedah arsitektur kode lengkapnya, melihat bagaimana data mengalir dari request pengguna ke file sistem, lalu menelusuri respon terminalnya.

![Arsitektur Mini Proyek Notes API](asset:images/be-node-01/mini-proyek-notes-api.png)

---

## 1. Spesifikasi Sistem Notes API

Aplikasi server ini dirancang dengan prinsip:
1. **Murni Tanpa Pustaka Pihak Ketiga**: Hanya menggunakan modul resmi `node:http`, `node:fs/promises`, dan `node:path`.
2. **Persistensi File JSON**: Seluruh data catatan disimpan secara permanen di dalam berkas `catatan.json` di disk server.
3. **Mendukung Dua Operasi Kunci**:
   - `GET /api/catatan`: Membaca seluruh daftar catatan yang tersimpan.
   - `POST /api/catatan`: Menerima catatan baru, menambahkan stempel waktu dan ID unik, lalu menyimpannya ke file.
4. **Respon Standar REST**: Selalu mengembalikan format JSON yang rapi beserta kode status HTTP yang tepat (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`).

---

## 2. Kode Lengkap Program Server

Perhatikan implementasi kode di bawah ini. Telusuri bagaimana fungsi helper asinkron membaca dan menulis file, serta bagaimana router mengarahkan aliran data:

```javascript
// server.js
const http = require('node:http');
const fs = require('node:fs/promises');
const path = require('node:path');

const PORT = process.env.PORT || 3000;
const FILE_PATH = path.join(__dirname, 'catatan.json');

// 1. Fungsi pembantu: Membaca seluruh catatan dari file disk
async function bacaSemuaCatatan() {
  try {
    const raw = await fs.readFile(FILE_PATH, 'utf8');
    return JSON.parse(raw);
  } catch (error) {
    // Jika file belum ada, kembalikan array kosong
    return [];
  }
}

// 2. Fungsi pembantu: Menyimpan array catatan ke disk
async function simpanCatatan(data) {
  await fs.writeFile(FILE_PATH, JSON.stringify(data, null, 2), 'utf8');
}

// 3. Inisialisasi HTTP Web Server
const server = http.createServer(async (req, res) => {
  const { method, url } = req;

  // Pasang header default untuk seluruh respon API
  res.setHeader('Content-Type', 'application/json; charset=utf-8');

  console.log(`[LOG] Masuk: ${method} ${url}`);

  // ENDPOINT 1: Mengambil semua catatan
  if (url === '/api/catatan' && method === 'GET') {
    const daftar = await bacaSemuaCatatan();
    res.statusCode = 200;
    res.end(JSON.stringify({
      sukses: true,
      total: daftar.length,
      data: daftar
    }));
  }

  // ENDPOINT 2: Menambah catatan baru
  else if (url === '/api/catatan' && method === 'POST') {
    let bodyRaw = '';

    req.on('data', (chunk) => {
      bodyRaw += chunk.toString();
    });

    req.on('end', async () => {
      try {
        const payload = JSON.parse(bodyRaw);

        // Validasi input sederhana
        if (!payload.judul || !payload.isi) {
          res.statusCode = 400; // Bad Request
          res.end(JSON.stringify({
            sukses: false,
            pesan: "Judul dan isi catatan wajib diisi!"
          }));
          return;
        }

        const daftar = await bacaSemuaCatatan();
        const catatanBaru = {
          id: "NOTE-" + (daftar.length + 1),
          judul: payload.judul,
          isi: payload.isi,
          dibuatPada: new Date().toISOString()
        };

        daftar.push(catatanBaru);
        await simpanCatatan(daftar);

        res.statusCode = 201; // 201 Created
        res.end(JSON.stringify({
          sukses: true,
          pesan: "Catatan berhasil disimpan!",
          data: catatanBaru
        }));

      } catch (err) {
        res.statusCode = 400;
        res.end(JSON.stringify({ sukses: false, pesan: "Format JSON tidak valid!" }));
      }
    });
  }

  // Jalur Fallback: Rute tidak dikenal
  else {
    res.statusCode = 404;
    res.end(JSON.stringify({
      sukses: false,
      pesan: "Endpoint tidak ditemukan di Notes API"
    }));
  }
});

server.listen(PORT, () => {
  console.log(`=== NOTES API AKTIF ===`);
  console.log(`Server berjalan di http://localhost:${PORT}`);
});
```

Output di terminal saat server pertama kali dinyalakan:
```
=== NOTES API AKTIF ===
Server berjalan di http://localhost:3000
```

---

## 3. Simulasi Mental Alur Permintaan Data

Mari kita bayangkan dua peristiwa interaksi yang terjadi pada server di atas:

### Skenario 1: Klien Mengirim Catatan Pertama (POST)
Aplikasi klien mengirimkan HTTP POST ke `/api/catatan` dengan data:
```json
{
  "judul": "Belajar Node.js",
  "isi": "Hari ini saya memahami arsitektur non-blocking dan event loop."
}
```

1. Server mendeteksi kecocokan `url === '/api/catatan'` dan `method === 'POST'`.
2. Event `'data'` mengumpulkan teks JSON, lalu event `'end'` mem-parsing teks tersebut.
3. Fungsi `bacaSemuaCatatan()` dipanggil (mengembalikan array kosong `[]`).
4. Catatan baru diracik dengan `id: "NOTE-1"` dan stempel waktu ISO.
5. Fungsi `simpanCatatan()` menulis data ke file `catatan.json` di disk menggunakan `fs.writeFile`.
6. Server membalas ke klien dengan status code `201 Created`:
```json
{
  "sukses": true,
  "pesan": "Catatan berhasil disimpan!",
  "data": {
    "id": "NOTE-1",
    "judul": "Belajar Node.js",
    "isi": "Hari ini saya memahami arsitektur non-blocking dan event loop.",
    "dibuatPada": "2025-10-25T10:15:30.000Z"
  }
}
```

### Skenario 2: Klien Membaca Daftar Catatan (GET)
Setelah itu, klien membuka endpoint `GET /api/catatan`.
1. Server membaca file `catatan.json` yang baru saja diperbarui.
2. Server merespon seketika dengan status `200 OK`:
```json
{
  "sukses": true,
  "total": 1,
  "data": [
    {
      "id": "NOTE-1",
      "judul": "Belajar Node.js",
      "isi": "Hari ini saya memahami arsitektur non-blocking dan event loop.",
      "dibuatPada": "2025-10-25T10:15:30.000Z"
    }
  ]
}
```

---

## 4. Coba di Komputermu (Opsional)

Jika kamu ingin mengeksekusi proyek ini langsung di komputer pribadimu:
1. Simpan kode di atas sebagai `server.js`.
2. Di folder yang sama, jalankan di terminal:
   ```bash
   node server.js
   ```
3. Buka peramban di laptopmu dan kunjungi `http://localhost:3000/api/catatan`. Kamu akan langsung melihat array JSON ditampilkan di layarmu!

Luar biasa! Kamu telah melihat bagaimana seluruh komponen back-end berpadu menjadi satu kesatuan fungsional yang tangguh.

Di modul penutup berikutnya, kita akan merangkum seluruh fondasi utama dan melihat peta jalan untuk perjalanan karirmu selanjutnya!
