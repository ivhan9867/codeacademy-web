---
id: node-inti-6
courseId: be-node-01
order: 13
title: "Pola Desain Event-Driven dengan EventEmitter"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Memahami konsep arsitektur Publisher-Subscriber (Pub/Sub) pada modul node:events"
  - "Membuat instance EventEmitter, meregistrasi pendengar dengan .on() dan .once()"
  - "Memicu event kustom dengan parameter data menggunakan method .emit()"
codeLanguage: javascript
codeSnippet: |
  const EventEmitter = require('node:events');
  const pemancar = new EventEmitter();

  pemancar.on('pesanMasuk', (pengirim, teks) => {
    console.log(`[Pesan dari ${pengirim}]: ${teks}`);
  });

  pemancar.emit('pesanMasuk', 'Siti', 'Server siap dideploy!');
---

# Pola Desain Event-Driven dengan EventEmitter

Node.js dibangun di atas filosofi **Event-Driven Architecture** (*Arsitektur Berbasis Peristiwa*). Sebagian besar modul bawaan Node.js—termasuk server HTTP, socket TCP, dan stream file—sebenarnya adalah turunan dari sebuah kelas sakti bernama **`EventEmitter`**.

Jika kamu pernah menulis `tombol.addEventListener('click', ...)` di browser, maka `EventEmitter` adalah padanannya di sisi server!

![Pola Arsitektur EventEmitter](asset:images/be-node-01/event-emitter-pattern.png)

---

## 1. Konsep Publisher - Subscriber (Pub/Sub)

Bayangkan sebuah menara pemancar radio:
1. **Subscriber (Pendengar)**: Radio-radio di rumah yang menyetel frekuensi tertentu untuk mendengarkan lagu (`.on()`).
2. **Publisher (Pemancar)**: Menara pemancar yang mengudarakan siaran ke frekuensi tersebut (`.emit()`).
3. Menara pemancar tidak perlu tahu siapa saja orang yang sedang mendengarkan. Ia hanya memancarkan sinyal, dan semua radio yang sedang menyala akan otomatis meresponsnya!

Dalam kode Node.js:
- `.on(namaEvent, callback)`: Mendaftarkan fungsi pendengar (*listener*).
- `.emit(namaEvent, ...data)`: Memancarkan sinyal peristiwa sambil membawa data opsional.

---

## 2. Implementasi Sistem Notifikasi Sederhana

Mari kita buat sebuah sistem notifikasi pesanan menggunakan `EventEmitter`:

```javascript
const EventEmitter = require('node:events');

// 1. Membuat instance EventEmitter
const sistemPesanan = new EventEmitter();

console.log("=== SISTEM TOKO ONLINE DIMULAI ===");

// 2. Mendaftarkan pendengar untuk event 'pesananBaru'
sistemPesanan.on('pesananBaru', (idPesanan, totalHarga) => {
  console.log(`[LOGISTIK] Menyiapkan kemasan untuk Order #${idPesanan}`);
  console.log(`[KASIR] Mencatat penerimaan uang sebesar Rp ${totalHarga}`);
});

// 3. Mendaftarkan pendengar lain untuk event yang sama (Bisa banyak listener!)
sistemPesanan.on('pesananBaru', (idPesanan) => {
  console.log(`[EMAIL] Mengirimkan bukti tanda terima Order #${idPesanan} ke pelanggan.`);
});

// 4. Memancarkan event 'pesananBaru' dengan data
console.log("-> Pelanggan menekan tombol 'Bayar'...");
sistemPesanan.emit('pesananBaru', 'ORD-991', 150000);

console.log("-> Pelanggan kedua melakukan pembayaran...");
sistemPesanan.emit('pesananBaru', 'ORD-992', 320000);
```

Output:
```
=== SISTEM TOKO ONLINE DIMULAI ===
-> Pelanggan menekan tombol 'Bayar'...
[LOGISTIK] Menyiapkan kemasan untuk Order #ORD-991
[KASIR] Mencatat penerimaan uang sebesar Rp 150000
[EMAIL] Mengirimkan bukti tanda terima Order #ORD-991 ke pelanggan.
-> Pelanggan kedua melakukan pembayaran...
[LOGISTIK] Menyiapkan kemasan untuk Order #ORD-992
[KASIR] Mencatat penerimaan uang sebesar Rp 320000
[EMAIL] Mengirimkan bukti tanda terima Order #ORD-992 ke pelanggan.
```

Perhatikan betapa rapinya kode kita! Bagian logistik, kasir, dan pengiriman email tidak saling bertumpuk di satu fungsi yang rumit. Mereka terpisah secara mandiri dan hanya bereaksi saat event `pesananBaru` dipancarkan.

---

## 3. Method Sekali Dengar: `.once()`

Jika kamu ingin sebuah fungsi pendengar hanya dijalankan **satu kali saja**, lalu setelah itu dicabut otomatis:

```javascript
const EventEmitter = require('node:events');
const server = new EventEmitter();

// .once hanya bereaksi pada pancaran pertama
server.once('boot', () => {
  console.log("[INIT] Pemanasan database selesai. Layanan siap.");
});

server.emit('boot'); // Dicetak
server.emit('boot'); // Diabaikan (tidak dicetak)
```

Output:
```
[INIT] Pemanasan database selesai. Layanan siap.
```

---

## 4. Keistimewaan Penanganan Event `'error'`

Di Node.js, nama event `'error'` memiliki sifat keramat khusus:

> **Aturan Wajib:**
> Jika sebuah `EventEmitter` memancarkan event bernama `'error'` dan **tidak ada satu pun listener** yang didaftarkan untuk menangkapnya (`.on('error', ...)`), maka Node.js akan menganggapnya sebagai kesalahan tak tertangani (*Unhandled Exception*) dan program servermu akan langsung crash!

```javascript
const EventEmitter = require('node:events');
const koneksi = new EventEmitter();

// Selalu daftarkan penanganan event 'error'
koneksi.on('error', (err) => {
  console.log(`[TERTANGKAP] Error berhasil diredam: ${err.message}`);
});

koneksi.emit('error', new Error('Kabel LAN terlepas'));
```

Output:
```
[TERTANGKAP] Error berhasil diredam: Kabel LAN terlepas
```

Di modul berikutnya, kita akan menyingkap rahasia terdalam di balik layar Node.js: **Fase-fase Event Loop, `process.nextTick`, Microtask, dan Macrotask!**
