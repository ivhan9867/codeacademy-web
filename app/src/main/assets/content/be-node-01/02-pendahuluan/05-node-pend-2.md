---
id: node-pend-2
courseId: be-node-01
order: 5
title: "Arsitektur Non-Blocking I/O & Konsep Event Loop"
readingMinutes: 7
section: PENDAHULUAN
learningObjectives:
  - "Memahami perbedaan mendasar model I/O blocking (sinkron) vs non-blocking (asinkron)"
  - "Mengetahui peran mesin V8 (single thread) dan libuv (thread pool & event loop) dalam menangani beban kerja tinggi"
  - "Menelusuri urutan eksekusi kode asynchronous berbasis timer sederhana"
codeLanguage: javascript
codeSnippet: |
  console.log("1. Pesanan masuk ke kasir");

  setTimeout(() => {
    console.log("3. Kopi selesai diracik di dapur (Callback Event Loop)");
  }, 100);

  console.log("2. Kasir lanjut melayani pelanggan berikutnya");
---

# Arsitektur Non-Blocking I/O & Konsep Event Loop

Salah satu pertanyaan paling sering diajukan oleh pemula adalah: *"Jika Node.js hanya berjalan pada satu thread utama (single-threaded), bagaimana mungkin server Node.js mampu melayani puluhan ribu pengguna secara bersamaan tanpa macet?"*

Jawabannya terletak pada dua pilar utamanya: **Non-Blocking I/O** dan **Event Loop**.

![Arsitektur Event Loop dan Non-Blocking](asset:images/be-node-01/arsitektur-event-loop.png)

---

## 1. Analogi Kedai Kopi: Kasir Blocking vs Non-Blocking

Bayangkan sebuah kedai kopi yang sangat ramai:

### A. Model Tradisional (Blocking I/O - Multi-thread)
Setiap ada pelanggan datang, kasir mencatat pesanan, lalu kasir **berdiri mematung diam di depan mesin espresso selama 3 menit** menunggu kopi matang sebelum menyerahkannya ke pelanggan. Selama 3 menit itu, antrean di belakangnya terhenti total (*blocked*). Jika ingin melayani 10 pelanggan sekaligus, pemilik kedai harus menggaji 10 kasir baru (menambah *thread* memori yang sangat boros).

### B. Model Node.js (Non-Blocking I/O - Event-Driven)
Hanya ada **satu kasir lincah (*single thread*)**. Ketika kamu memesan kopi:
1. Kasir mencatat pesanan dan menyerahkan tiket ke dapur barista (*libuv thread pool*).
2. Kasir **langsung melayani pelanggan kedua dan ketiga** tanpa menunggu kopi pertama selesai dibuat.
3. Begitu barista dapur selesai membuat kopi, sebuah lonceng berbunyi (*Event*).
4. Kasir yang sedang senggang langsung memanggil nomormu dan menyerahkan kopi tersebut (*Callback*).

Hasilnya? Satu kasir dapat menangani ratusan pelanggan per jam dengan sangat efisien!

---

## 2. Mengenal Libuv dan Event Loop

Di balik layar, Node.js terdiri dari dua mesin utama:
- **V8 Engine**: Menjalankan instruksi kode JavaScriptmu (menghitung angka, membaca variabel, menjalankan perulangan) pada **Call Stack** utama.
- **Libuv (C Library)**: Pustaka sakti yang mengurus tugas-tugas lambat yang berkaitan dengan I/O (*Input/Output*)—seperti membaca file dari SSD, meminta data ke database, atau mendengarkan panggilan jaringan.

**Event Loop** adalah pengawas lalu lintas tanpa henti. Tugasnya sangat sederhana:
1. Ia terus memantau apakah **Call Stack** (tempat kode berjalan) sedang kosong.
2. Jika Call Stack sudah kosong, Event Loop akan mengambil tugas callback dari antrean tugas (*Callback Queue*) dan mendorongnya ke Call Stack untuk dieksekusi.

---

## 3. Menelusuri Urutan Eksekusi Kode Asinkron

Mari kita uji pemahamanmu dengan menelusuri potongan kode berikut. Bacalah secara teliti dan perhatikan urutan angka yang dicetak:

```javascript
console.log("Langkah 1: Pelanggan A memesan kopi");

// Simulasi proses asinkron di dapur barista
setTimeout(() => {
  console.log("Langkah 4: Kopi Pelanggan A selesai disajikan");
}, 0);

console.log("Langkah 2: Pelanggan B memesan teh");
console.log("Langkah 3: Pelanggan C memesan roti");
```

Output:
```
Langkah 1: Pelanggan A memesan kopi
Langkah 2: Pelanggan B memesan teh
Langkah 3: Pelanggan C memesan roti
Langkah 4: Kopi Pelanggan A selesai disajikan
```

### Mengapa Langkah 4 Muncul Paling Akhir padahal Waktunya `0` milidetik?
Inilah rahasia Event Loop:
1. Baris `Langkah 1` langsung dieksekusi di Call Stack dan langsung dicetak.
2. Ketika `setTimeout` dipanggil, Node.js mendaftarkan fungsi callback-nya ke sistem timer Libuv di latar belakang. Meskipun delay-nya `0ms`, callback tersebut **tidak langsung dieksekusi**, melainkan diletakkan di dalam **Callback Queue**.
3. Call Stack terus melanjutkan membaca kode sinkron berikutnya: `Langkah 2` lalu `Langkah 3`.
4. Baru setelah seluruh kode sinkron selesai dieksekusi dan Call Stack benar-benar bersih, Event Loop mengizinkan callback di Callback Queue naik ke Call Stack untuk mencetak `Langkah 4`!

---

## 4. Pelajaran Emas untuk Programmer Back-End

Karena Node.js menggunakan satu thread utama untuk menerima request, ada satu aturan keramat:

> **JANGAN PERNAH MEMBLOKIR CALL STACK!**
> Hindari perulangan matematika berat tanpa akhir (seperti enkripsi file gigabyte secara sinkron atau loop triliunan kali di thread utama), karena seluruh pengguna lain yang sedang mengakses web servermu akan ikut membeku menunggu loop tersebut selesai.

Di modul berikutnya, kita akan melihat bagaimana kita mengeksekusi instruksi di Node.js secara langsung lewat **REPL** dan **File Skrip**!
