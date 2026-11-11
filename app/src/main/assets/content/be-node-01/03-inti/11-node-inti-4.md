---
id: node-inti-4
courseId: be-node-01
order: 11
title: "Bekerja dengan File System: Sinkron vs Asinkron (fs)"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Memahami peran modul node:fs dalam membaca dan menulis file di penyimpanan lokal"
  - "Membedakan metode sinkron (blocking) fs.readFileSync vs metode asinkron (non-blocking) fs.readFile"
  - "Mengenal konvensi Error-First Callback (err, data) pada fungsi asinkron Node.js klasik"
codeLanguage: javascript
codeSnippet: |
  const fs = require('node:fs');

  // Asinkron (Non-Blocking) dengan Error-First Callback
  fs.readFile('catatan.txt', 'utf8', (err, data) => {
    if (err) {
      console.log(`Gagal membaca: ${err.message}`);
      return;
    }
    console.log(`Isi file: ${data}`);
  });
---

# Bekerja dengan File System: Sinkron vs Asinkron (fs)

Di sisi server, aplikasi kita harus mampu membaca berkas konfigurasi, menyimpan catatan log, mengunggah foto pengguna, dan menulis file laporan. Di Node.js, seluruh operasi baca-tulis berkas dilakukan melalui modul **`node:fs`** (*File System*).

Hampir setiap fungsi di dalam modul `fs` hadir dalam dua varian: **Sinkron (*Sync*)** dan **Asinkron (*Async*)**.

![File System Sinkron vs Asinkron](asset:images/be-node-01/fs-sync-vs-async.png)

---

## 1. Varian Sinkron (`fs.readFileSync`): Mudah tapi Mematikan

Varian sinkron selalu diakhiri dengan kata `Sync`. Ketika fungsi ini dipanggil, Node.js akan menghentikan seluruh eksekusi baris kode berikutnya sampai berkas selesai dibaca dari hard drive.

```javascript
const fs = require('node:fs');

console.log("1. Mulai membaca file konfigurasi secara sinkron...");

try {
  // readFileSync memblokir Call Stack sampai data selesai dibaca
  const data = fs.readFileSync('config.txt', 'utf8');
  console.log(`2. Isi config: ${data}`);
} catch (error) {
  console.log(`2. Terjadi kesalahan: ${error.message}`);
}

console.log("3. Lanjut ke proses server berikutnya...");
```

Output (jika file `config.txt` berisi tulisan `MODE=PROD`):
```
1. Mulai membaca file konfigurasi secara sinkron...
2. Isi config: MODE=PROD
3. Lanjut ke proses server berikutnya...
```

### Mengapa Metode Sinkron Berbahaya di Web Server?
Jika file tersebut berukuran besar (misalnya 100 MB) dan butuh 2 detik untuk dibaca dari disk, maka selama 2 detik itu seluruh server Node.js akan membeku total! Ribuan pengguna lain yang sedang mengakses web servermu tidak akan mendapat respons sama sekali.

> **Aturan Penggunaan:**
> Gunakan metode `Sync` **hanya sekali** saat server pertama kali dinyalakan (misalnya membaca file konfigurasi awal sebelum server membuka port jaringan). Jangan pernah gunakan di dalam alur penanganan request pengguna!

---

## 2. Varian Asinkron (`fs.readFile`): Standar Emas Back-End

Varian asinkron tidak memakai akhiran `Sync`. Ia menyerahkan tugas membaca file ke pustaka Libuv di latar belakang, sehingga Call Stack utama langsung bebas mengeksekusi baris berikutnya tanpa terhenti!

Fungsi asinkron klasik di Node.js menggunakan pola **Error-First Callback**: fungsi callback yang selalu menerima dua parameter: `(err, data)`.

Perhatikan urutan eksekusi berikut:

```javascript
const fs = require('node:fs');

console.log("1. Memulai instruksi pembacaan file asinkron...");

fs.readFile('pengumuman.txt', 'utf8', (err, data) => {
  if (err) {
    console.log(`4. Terjadi error saat membaca: ${err.message}`);
    return;
  }
  console.log(`4. Data berhasil dibaca: "${data}"`);
});

console.log("2. Menjalankan tugas komputasi lainnya...");
console.log("3. Server tetap responsif melayani pengguna lain!");
```

Output:
```
1. Memulai instruksi pembacaan file asinkron...
2. Menjalankan tugas komputasi lainnya...
3. Server tetap responsif melayani pengguna lain!
4. Data berhasil dibaca: "Ujian susulan diadakan hari Senin."
```

### Bedah Alur:
1. Angka `1` dicetak.
2. `fs.readFile` dipanggil. Libuv mengambil alih operasi I/O hard drive. Node.js tidak menunggu!
3. Angka `2` dan `3` langsung dicetak tanpa jeda.
4. Ketika hard drive selesai membaca teks, Event Loop mengambil callback `(err, data)` dan mencetak angka `4`.

---

## 3. Menulis File dengan `fs.writeFile`

Untuk membuat file baru atau menimpa isi file yang sudah ada:

```javascript
const fs = require('node:fs');

const konten = "Log Aktivitas: Pengguna 'admin' berhasil login.\n";

fs.writeFile('aktivitas.log', konten, 'utf8', (err) => {
  if (err) {
    console.log(`Gagal menulis file: ${err.message}`);
    return;
  }
  console.log("File aktivitas.log berhasil disimpan ke disk!");
});
```

Output:
```
File aktivitas.log berhasil disimpan ke disk!
```

Meskipun callback sangat handal, jika kita memiliki operasi file yang bertingkat (baca file A, lalu tulis ke file B, lalu ubah izin file C), kita akan terjebak dalam sarang callback bersarang (*Callback Hell*).

Bagaimana cara menulis operasi file asinkron modern yang bersih? Jawabannya ada di modul berikutnya: **`fs/promises` dan `async`/`await`!**
