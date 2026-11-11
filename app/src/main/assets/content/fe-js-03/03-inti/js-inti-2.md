---
id: js-inti-2
courseId: fe-js-03
order: 10
title: "Ragam Jenis Data Primitif JavaScript"
readingMinutes: 6
section: INTI
learningObjectives:
  - Mengenal 5 tipe data primitif utama: String, Number, Boolean, Undefined, dan Null
  - Memanfaatkan operator typeof untuk mengidentifikasi tipe data suatu nilai
  - Memahami perbedaan mendasar antara undefined (belum diisi) dan null (sengaja dikosongkan)
codeLanguage: javascript
codeSnippet: |
  console.log(typeof "Halo"); // "string"
  console.log(typeof 42);     // "number"
  console.log(typeof true);   // "boolean"
starterJs: |
  const namaProduk = "Laptop Pro";
  const harga = 15000000;
  const stokTersedia = true;
  let voucherDiskon; // Variabel tanpa nilai awal

  console.log("Tipe namaProduk:", typeof namaProduk);
  console.log("Tipe harga:", typeof harga);
  console.log("Tipe stokTersedia:", typeof stokTersedia);
  console.log("Nilai voucherDiskon:", voucherDiskon);
playgroundTask: |
  Jalankan kode bawaan dan amati panel Console. Pastikan outputnya adalah:
  Tipe namaProduk: string
  Tipe harga: number
  Tipe stokTersedia: boolean
  Nilai voucherDiskon: undefined
---

# Ragam Jenis Data Primitif JavaScript

Sama seperti di dunia nyata kita membedakan antara huruf alfabet, uang kertas, dan saklar lampu hidup/mati, komputer juga mengelompokkan data ke dalam jenis-jenis tertentu yang disebut **Tipe Data**.

Di JavaScript, data paling mendasar dikelompokkan sebagai **Tipe Data Primitif**. Mari kita pelajari lima jenis yang paling penting!

---

## 1. String (Teks)
String mewakili kumpulan karakter huruf atau kata. Ciri khasnya selalu diapit oleh tanda kutip ganda (`"..."`), kutip tunggal (`'...'`), atau backtick (`` `...` ``):

```javascript
const nama = "Siti Rahmawati";
const inisial = 'SR';
```

---

## 2. Number (Angka)
JavaScript tidak membedakan bilangan bulat (*integer*) dan bilangan pecahan (*float*). Keduanya masuk ke dalam satu tipe: **Number**:

```javascript
const umur = 24;
const beratBadan = 55.5;
const suhuMinus = -4;
```

---

## 3. Boolean (Kebenaran Biner)
Tipe data ini sangat sederhana karena hanya memiliki dua kemungkinan nilai: **`true`** (benar) atau **`false`** (salah). Tipe ini sering digunakan sebagai saklar kondisi:

```javascript
const sudahLogin = true;
const memilikiAksesVip = false;
```

---

## 4. Undefined vs 5. Null (Dua Jenis "Kosong")

Sering kali pemula bingung membedakan antara `undefined` dan `null`. Keduanya tampak mirip, namun memiliki arti filosofis yang berbeda:

- **`undefined`**: Kotak variabel sudah disiapkan, tetapi komputer **belum diberi tahu apa isinya**. Ini adalah nilai bawaan (*default*) dari variabel kosong:
  ```javascript
  let alamatRumah;
  console.log(alamatRumah); // undefined
  ```

- **`null`**: Programmer **secara sengaja** mengisi kotak tersebut dengan nilai "kosong" atau "tidak ada":
  ```javascript
  let fotoProfil = null; // Pengguna memang sengaja belum mengunggah foto
  console.log(fotoProfil); // null
  ```

> **Analogi Sederhana:**
> - `undefined`: Kotak paket diantar ke rumahmu dalam keadaan belum pernah diisi barang apa pun oleh pengirim.
> - `null`: Pengirim sengaja menaruh secarik kertas bertuliskan *"Kotak ini sengaja dikosongkan"*.

---

## Memeriksa Tipe Data dengan typeof

Jika kamu ragu apa jenis data yang ada di dalam sebuah variabel, gunakan operator bawaan **`typeof`**:

```javascript
const usia = 25;
console.log(typeof usia); // mencetak "number"

const namaLengkap = "Vhan";
console.log(typeof namaLengkap); // mencetak "string"
```

Coba jalankan editor kode di bawah untuk melihat bagaimana `typeof` mendeteksi berbagai jenis data secara otomatis!
