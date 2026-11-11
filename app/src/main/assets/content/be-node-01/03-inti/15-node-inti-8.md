---
id: node-inti-8
courseId: be-node-01
order: 15
title: "Latihan Interaktif: Tebak Output Asinkron & Event Loop"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Melatih simulasi mental eksekusi kode asinkron bertingkat"
  - "Mengidentifikasi urutan cetak gabungan async/await, nextTick, dan timer"
  - "Memverifikasi penalaran alur eksekusi dengan kunci jawaban dan analisis mendalam"
codeLanguage: javascript
codeSnippet: |
  async function latihan() {
    console.log("A");
    await null;
    console.log("B");
  }
  latihan();
  console.log("C");
---

# Latihan Interaktif: Tebak Output Asinkron & Event Loop

Selamat telah sampai di modul penutup Section Inti! Pada modul-modul sebelumnya, kamu telah mempelajari sistem modul, operasi berkas, `EventEmitter`, hingga kasta antrean Event Loop.

Sekarang, saatnya menguji ketajaman intuisimu dengan **Latihan Tebak Output**.

![Simulasi Mental Tracing Event Loop](asset:images/be-node-01/latihan-tebak-output-async.png)

---

## Soal 1: Tantangan `async/await` dan Sinkron

Amati kode JavaScript berikut ini dengan teliti:

```javascript
async function prosesTransaksi() {
  console.log("1. Memulai verifikasi akun");
  await null; // Suspensi Promise microtask
  console.log("2. Verifikasi selesai, saldo berkurang");
}

console.log("0. Server menyala");
prosesTransaksi();
console.log("3. Menampilkan dashboard admin");
```

**Pertanyaan:**
Apakah urutan output yang dicetak ke layar konsol terminal?
A. `0 -> 1 -> 2 -> 3`  
B. `0 -> 3 -> 1 -> 2`  
C. `0 -> 1 -> 3 -> 2`  
D. `1 -> 0 -> 2 -> 3`  

*Tentukan jawabanmu sebelum menggulir ke bawah garis pemisah!*

---

## Kunci Jawaban & Pembahasan Soal 1

**Jawaban yang Benar: C (`0 -> 1 -> 3 -> 2`)**

### Analisis Alur Eksekusi:
1. `console.log("0. Server menyala")` dieksekusi pertama secara sinkron di Call Stack.
2. Fungsi `prosesTransaksi()` dipanggil.
3. Di dalam fungsi, baris `1. Memulai verifikasi akun` langsung dieksekusi secara sinkron karena belum ada kata kunci `await`.
4. Baris `await null;` ditemui! Kata kunci `await` membungkus nilai di kanannya menjadi sebuah Promise dan **menangguhkan sisa fungsi `prosesTransaksi` ke antrean Microtask**.
5. Call Stack utama kembali ke file global dan mengeksekusi `console.log("3. Menampilkan dashboard admin")`.
6. Seluruh baris sinkron utama telah tuntas. Event Loop memeriksa antrean Microtask dan melanjutkan sisa fungsi: mencetak `2. Verifikasi selesai, saldo berkurang`.

---

## Soal 2: Grand Master Event Loop (Campuran Lengkap)

Sekarang, mari kita naikkan tingkat kesulitannya ke tingkat wawancara insinyur back-end:

```javascript
console.log("Mulai");

setTimeout(() => {
  console.log("Timeout 1");
}, 0);

process.nextTick(() => {
  console.log("NextTick 1");
});

Promise.resolve().then(() => {
  console.log("Promise 1");
  process.nextTick(() => {
    console.log("NextTick di dalam Promise");
  });
});

console.log("Selesai");
```

**Pertanyaan:**
Tuliskan urutan persis dari 6 baris teks yang akan dicetak di terminal!

*Ambil secarik kertas, buat urutan antreannya, lalu cocokkan dengan kunci di bawah ini!*

---

## Kunci Jawaban & Pembahasan Soal 2

Output Lengkap:
```
Mulai
Selesai
NextTick 1
Promise 1
NextTick di dalam Promise
Timeout 1
```

### Analisis Langkah Demi Langkah:
1. **Fase Sinkron (Call Stack)**:
   - Baris 1 mencetak `Mulai`.
   - `setTimeout` didaftarkan ke antrean Macrotask/Timer.
   - `process.nextTick` pertama didaftarkan ke `nextTickQueue`.
   - `Promise.resolve().then` didaftarkan ke `microtaskQueue`.
   - Baris terakhir mencetak `Selesai`.
2. **Fase Pengurasan Microtask**:
   - Node.js memproses `nextTickQueue` terlebih dahulu: mencetak `NextTick 1`.
   - Node.js beralih ke `microtaskQueue`: mencetak `Promise 1`.
   - Namun di dalam callback Promise tersebut, ada pemanggilan `process.nextTick` baru! Node.js langsung memasukkannya ke antrean prioritas dan mengeksekusinya sebelum berpindah ke fase macrotask: mencetak `NextTick di dalam Promise`.
3. **Fase Timer / Macrotask**:
   - Setelah seluruh microtask dan nextTick benar-benar kosong, barulah timer `setTimeout` dieksekusi: mencetak `Timeout 1`.

Jika analisismu tepat di kedua soal di atas, selamat! Kamu telah memiliki intuisi asinkron setara insinyur back-end berpengalaman!

Di **Section 4** berikutnya, kita akan melangkah ke dunia jaringan: **Manajemen Paket dengan npm, Membangun Web Server HTTP, dan Menangani REST API!**
