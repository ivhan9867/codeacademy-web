---
id: node-inti-5
courseId: be-node-01
order: 12
title: "Asinkron Bersih di Node.js: fs/promises & async/await"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Memahami keunggulan API berbasis Promise pada sub-modul node:fs/promises"
  - "Menulis operasi baca dan tulis file menggunakan kata kunci async dan await"
  - "Menerapkan penanganan kesalahan asinkron yang kokoh menggunakan blok try...catch"
codeLanguage: javascript
codeSnippet: |
  const fs = require('node:fs/promises');

  async function bacaDanTulis() {
    try {
      const data = await fs.readFile('sumber.txt', 'utf8');
      await fs.writeFile('salinan.txt', data.toUpperCase(), 'utf8');
      console.log("File berhasil disalin dan diubah ke huruf besar!");
    } catch (err) {
      console.error(`Error: ${err.message}`);
    }
  }
---

# Asinkron Bersih di Node.js: fs/promises & async/await

Pada modul sebelumnya, kita melihat bagaimana *Callback* menjaga server tetap non-blocking. Namun jika kita memiliki alur berantai—misalnya membaca data pengguna, memverifikasinya, lalu menulis file sertifikat PDF—kode dengan callback akan membentuk pola segitiga miring ke kanan (*Callback Hell* atau *Pyramid of Doom*) yang sulit dibaca dan rentan bug.

Untuk mengatasi ini, Node.js menyediakan sub-modul resmi modern: **`node:fs/promises`**.

![Operasi File dengan async await](asset:images/be-node-01/fs-promises-async-await.png)

---

## 1. Mengapa Memilih `fs/promises`?

Setiap fungsi di dalam `node:fs/promises` mengembalikan sebuah **`Promise`**. Ini memungkinkan kita menggunakan kombinasi sintaks **`async`** dan **`await`**, sehingga kode asinkron kita terlihat bersih dan mengalir lurus ke bawah persis seperti kode sinkron, tanpa kehilangan sifat non-blocking-nya!

Perhatikan perbandingan cara impor:
- Cara Lama (Callback): `const fs = require('node:fs');`
- **Cara Modern (Promises)**: `const fs = require('node:fs/promises');`
- Di ES Modules: `import fs from 'node:fs/promises';`

---

## 2. Membaca dan Menulis File dengan `async`/`await`

Mari kita telusuri alur kerja fungsi asinkron yang membaca file JSON dan menuliskan kembali versi terbarunya:

```javascript
const fs = require('node:fs/promises');

async function kelolaPengguna() {
  console.log("1. Memulai proses pembaruan data pengguna...");

  try {
    // Membaca file secara asinkron dengan await
    const isiRaw = await fs.readFile('pengguna.json', 'utf8');
    const pengguna = JSON.parse(isiRaw);
    console.log(`2. Data pengguna terbaca: ${pengguna.nama} (Poin: ${pengguna.poin})`);

    // Menambah poin pengguna
    pengguna.poin += 50;

    // Menuliskan kembali ke file secara asinkron
    const dataTerbaru = JSON.stringify(pengguna, null, 2);
    await fs.writeFile('pengguna.json', dataTerbaru, 'utf8');
    console.log(`3. Data berhasil diperbarui! Poin sekarang: ${pengguna.poin}`);

  } catch (error) {
    console.log(`Terjadi kesalahan: ${error.message}`);
  }

  console.log("4. Seluruh rangkaian proses asinkron selesai.");
}

console.log("0. Sebelum fungsi dipanggil");
kelolaPengguna();
console.log("9. Baris utama setelah fungsi dipanggil");
```

Jika file `pengguna.json` awalnya berisi: `{"nama": "Ahmad", "poin": 100}`, perhatikan output eksekusinya:

Output:
```
0. Sebelum fungsi dipanggil
1. Memulai proses pembaruan data pengguna...
9. Baris utama setelah fungsi dipanggil
2. Data pengguna terbaca: Ahmad (Poin: 100)
3. Data berhasil diperbarui! Poin sekarang: 150
4. Seluruh rangkaian proses asinkron selesai.
```

### Mengapa Angka `9` Muncul Sebelum Angka `2`?
Ini adalah konsep kunci yang sangat indah:
1. Ketika baris `await fs.readFile(...)` ditemui, eksekusi di dalam fungsi `kelolaPengguna` **ditunda sementara** sambil menunggu Libuv membaca data dari hard drive.
2. Call Stack utama tidak diam! Ia langsung melompat keluar dari fungsi tersebut dan mengeksekusi baris berikutnya di file utama, yaitu mencetak `9. Baris utama...`.
3. Setelah pembacaan hard drive tuntas, Promise ter-resolve, dan sisa baris di dalam `kelolaPengguna` dilanjutkan kembali secara teratur (angka 2, 3, dan 4).

---

## 3. Method Praktis Lainnya di `fs/promises`

Selain `readFile` dan `writeFile`, ada beberapa method esensial lainnya:

```javascript
const fs = require('node:fs/promises');

async function kelolaFolder() {
  try {
    // 1. fs.mkdir: Membuat folder baru
    await fs.mkdir('laporan_2025', { recursive: true });
    console.log("Folder laporan_2025 berhasil dibuat.");

    // 2. fs.appendFile: Menambahkan baris baru di akhir file tanpa menimpa
    await fs.appendFile('laporan_2025/log.txt', "Baris 1: Aplikasi dimulai\n");
    await fs.appendFile('laporan_2025/log.txt', "Baris 2: Pemrosesan selesai\n");
    console.log("Catatan log berhasil ditambahkan.");

    // 3. fs.readdir: Membaca daftar file di dalam folder
    const isiFolder = await fs.readdir('laporan_2025');
    console.log(`Daftar berkas: ${isiFolder.join(', ')}`);
  } catch (err) {
    console.error(err);
  }
}

kelolaFolder();
```

Output:
```
Folder laporan_2025 berhasil dibuat.
Catatan log berhasil ditambahkan.
Daftar berkas: log.txt
```

Dengan menguasai `fs/promises` dan `async/await`, kamu telah memiliki fondasi terkuat untuk mengelola data berbasis file di server.

Di modul berikutnya, kita akan mempelajari mekanisme komunikasi internal Node.js yang sangat populer: **Pola Desain `EventEmitter`!**
