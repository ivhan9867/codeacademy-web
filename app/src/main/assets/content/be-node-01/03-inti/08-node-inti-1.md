---
id: node-inti-1
courseId: be-node-01
order: 8
title: "Sistem Modul CommonJS: require dan module.exports"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Memahami prinsip enkapsulasi kode dan pembagian file modular di Node.js"
  - "Mengekspor variabel, fungsi, atau objek menggunakan module.exports"
  - "Mengimpor dan menggunakan modul lokal menggunakan require()"
codeLanguage: javascript
codeSnippet: |
  // File: matematika.js
  function tambah(a, b) {
    return a + b;
  }
  module.exports = { tambah };

  // File: app.js
  const math = require('./matematika');
  console.log(`Hasil: ${math.tambah(10, 5)}`);
---

# Sistem Modul CommonJS: require dan module.exports

Ketika kamu membangun aplikasi back-end berskala nyata, mustahil seluruh kode ditaruh di dalam satu file raksasa ribuan baris. Kode harus dipecah menjadi modul-modul kecil yang memiliki tanggung jawab spesifik—misalnya modul otentikasi, modul database, dan modul perhitungan.

Sistem modul bawaan tertua dan paling mendasar di Node.js dinamakan **CommonJS**.

![Sistem Modul CommonJS](asset:images/be-node-01/commonjs-module-system.png)

---

## 1. Konsep Enkapsulasi: Setiap File Adalah Kotak Tertutup

Di Node.js, setiap file `.js` diperlakukan sebagai sebuah **modul independen**. 

Variabel atau fungsi apapun yang kamu tulis di dalam file tersebut bersifat **privat**. File lain di luar sana tidak akan bisa melihat atau menggunakan variabel tersebut kecuali jika kamu secara sengaja **mengekspornya** ke luar.

Dua pahlawan utama dalam CommonJS adalah:
1. **`module.exports`**: Wadah untuk membungkus data/fungsi yang ingin dibagikan ke dunia luar.
2. **`require()`**: Fungsi untuk menarik dan menggunakan modul dari file lain.

---

## 2. Cara Mengekspor dengan `module.exports`

Bayangkan kita memiliki sebuah file bernama `kalkulator.js`:

```javascript
// kalkulator.js
const KURS_DOLLAR = 16000; // Variabel privat (hanya bisa dibaca di file ini)

function konversiKeRupiah(dollar) {
  return dollar * KURS_DOLLAR;
}

function hitungDiskon(harga, persen) {
  return harga - (harga * persen / 100);
}

// Mengekspor fungsi-fungsi pilihan dalam bentuk object
module.exports = {
  konversiKeRupiah,
  hitungDiskon
};
```

Variabel `KURS_DOLLAR` tidak dimasukkan ke dalam `module.exports`, sehingga file lain tidak bisa mengubah atau mengotak-atik nilai kurs tersebut secara langsung. Inilah prinsip enkapsulasi yang aman!

---

## 3. Cara Mengimpor dengan `require()`

Sekarang, di file utama bernama `app.js`, kita memanggil modul kalkulator tersebut:

```javascript
// app.js
// Mengimpor file lokal wajib diawali './' atau '../'
const kalkulator = require('./kalkulator');

console.log("=== SISTEM TRANSAKSI KEUANGAN ===");

const hargaBarangUSD = 5;
const totalIDR = kalkulator.konversiKeRupiah(hargaBarangUSD);
console.log(`Harga 5 USD dalam Rupiah: Rp ${totalIDR}`);

const hargaDiskon = kalkulator.hitungDiskon(totalIDR, 10);
console.log(`Harga setelah diskon 10%: Rp ${hargaDiskon}`);
```

Output:
```
=== SISTEM TRANSAKSI KEUANGAN ===
Harga 5 USD dalam Rupiah: Rp 80000
Harga setelah diskon 10%: Rp 72000
```

---

## 4. Pola Destructuring pada `require`

Karena `module.exports` mengembalikan sebuah objek, kamu bisa memanfaatkan fitur *destructuring* JavaScript untuk hanya mengambil fungsi yang kamu butuhkan:

```javascript
// Mengambil fungsi spesifik secara langsung
const { konversiKeRupiah } = require('./kalkulator');

console.log(`Nilai 2 USD = Rp ${konversiKeRupiah(2)}`);
```

Output:
```
Nilai 2 USD = Rp 32000
```

---

## 5. Perbedaan Path Modul Lokal vs Modul Bawaan

Perhatikan aturan penulisan string di dalam fungsi `require()`:
- **Modul Lokal (file buatanmu sendiri)**: Wajib diawali tanda relatif `./` (di folder yang sama) atau `../` (naik satu folder). Contoh: `require('./layanan')`.
- **Modul Bawaan Node.js / Paket npm**: Ditulis langsung namanya tanpa awalan `./`. Contoh: `require('fs')` atau `require('path')`.

Jika kamu lupa menuliskan `./` saat memanggil filemu sendiri, Node.js akan mengira file tersebut adalah paket eksternal npm dan akan memunculkan error `Cannot find module`.

Di modul berikutnya, kita akan melihat saudaranya yang lebih modern: **ES Modules (`import` dan `export`)!**
