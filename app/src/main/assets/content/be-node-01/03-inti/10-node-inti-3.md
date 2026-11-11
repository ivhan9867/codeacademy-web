---
id: node-inti-3
courseId: be-node-01
order: 10
title: "Modul Bawaan Inti: Manipulasi Path dan Informasi OS"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami pentingnya modul node:path untuk menangani perbedaan separator path antar sistem operasi"
  - "Menggunakan method path.join, path.resolve, path.basename, dan path.extname"
  - "Membaca spesifikasi perangkat keras dan memori sistem via modul node:os"
codeLanguage: javascript
codeSnippet: |
  const path = require('node:path');
  const os = require('node:os');

  const fileConfig = path.join('data', 'config', 'app.json');
  console.log(`Path Aman : ${fileConfig}`);
  console.log(`Sistem OS : ${os.type()} (${os.arch()})`);
---

# Modul Bawaan Inti: Manipulasi Path dan Informasi OS

Salah satu kehebatan Node.js adalah ia dilengkapi dengan **Baterai Bawaan (*Built-in Modules*)**—kumpulan modul canggih yang siap pakai tanpa perlu mengunduh apapun lewat internet.

Dua modul bawaan yang paling sering dibutuhkan sejak hari pertama adalah **`node:path`** dan **`node:os`**.

*(Catatan Modern: Sejak Node.js v16+, disarankan memberi awalan `node:` seperti `require('node:path')` untuk mempertegas bahwa modul tersebut adalah modul resmi bawaan inti Node.js).*

![Modul Bawaan Path dan OS](asset:images/be-node-01/modul-path-os.png)

---

## 1. Masalah Besar Separator Path Antar Sistem Operasi

Di dunia komputer:
- **Windows** menggunakan garis miring terbalik (*backslash*): `C:\Users\Budi\data\file.txt`
- **Linux & macOS (Server Cloud)** menggunakan garis miring biasa (*slash*): `/home/budi/data/file.txt`

Jika kamu menggabungkan path file dengan cara manual seperti:
`const lokasi = "folder/" + "file.txt"` -> Kode ini akan rentan rusak saat dipindahkan ke server Windows atau Linux yang berbeda!

Solusinya: **Gunakan fungsi `path.join()`!**

---

## 2. Eksplorasi Modul `node:path`

Modul `path` secara cerdas akan mendeteksi sistem operasi tempat kode sedang berjalan dan menyusun alamat file yang 100% valid.

Mari kita lihat method-method terpentingnya:

```javascript
const path = require('node:path');

console.log("=== MANIPULASI PATH FILE ===");

// 1. path.join: Menggabungkan potongan folder secara aman
const alamatLengkap = path.join('proyek', 'database', 'users.json');
console.log(`Hasil path.join    : ${alamatLengkap}`);

// 2. path.basename: Mengambil nama file beserta ekstensinya saja
const namaFile = path.basename('/var/www/html/index.html');
console.log(`Nama file          : ${namaFile}`);

// 3. path.extname: Mengambil format ekstensi file
const ekstensi = path.extname('laporan-keuangan.pdf');
console.log(`Ekstensi file      : ${ekstensi}`);

// 4. path.parse: Membedah path menjadi object rinci
const infoPath = path.parse('/app/src/server.js');
console.log(`Nama file murni    : ${infoPath.name}`);
console.log(`Direktori induk    : ${infoPath.dir}`);
```

Output di sistem operasi Linux/macOS:
```
=== MANIPULASI PATH FILE ===
Hasil path.join    : proyek/database/users.json
Nama file          : index.html
Ekstensi file      : .pdf
Nama file murni    : server
Direktori induk    : /app/src
```

---

## 3. Membaca Informasi Perangkat Keras dengan `node:os`

Sebagai engineer back-end, servermu berjalan di mesin fisik atau komputasi awan (*cloud*). Kamu sering perlu memeriksa kapasitas RAM dan jumlah inti CPU untuk mengoptimalkan kinerja aplikasi.

Modul `node:os` menyediakan jendela ke perangkat keras tersebut:

```javascript
const os = require('node:os');

console.log("=== DIAGNOSTIK SERVER HARDWARE ===");

console.log(`Nama Host (Hostname): ${os.hostname()}`);
console.log(`Tipe Platform       : ${os.type()} - ${os.platform()}`);
console.log(`Arsitektur CPU      : ${os.arch()}`);
console.log(`Jumlah Core CPU     : ${os.cpus().length} core`);

// os.totalmem() dan os.freemem() mengembalikan satuan Byte
const totalRamGB = (os.totalmem() / (1024 ** 3)).toFixed(2);
const freeRamGB = (os.freemem() / (1024 ** 3)).toFixed(2);

console.log(`Total RAM Mesin     : ${totalRamGB} GB`);
console.log(`Sisa RAM Tersedia   : ${freeRamGB} GB`);
```

Output:
```
=== DIAGNOSTIK SERVER HARDWARE ===
Nama Host (Hostname): server-cloud-production
Tipe Platform       : Linux - linux
Arsitektur CPU      : x64
Jumlah Core CPU     : 4 core
Total RAM Mesin     : 16.00 GB
Sisa RAM Tersedia   : 11.45 GB
```

Dengan modul `os`, kamu bisa membuat skrip pemantau kesehatan server yang memberi peringatan otomatis jika sisa RAM berada di bawah batas aman.

Di modul berikutnya, kita akan membahas modul yang paling legendaris di Node.js: **Modul File System (`node:fs`)!**
