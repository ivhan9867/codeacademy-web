---
id: js-inti-10
courseId: fe-js-03
order: 20
title: "Method Array Esensial: push, pop, slice, includes, & find"
readingMinutes: 7
section: INTI
learningObjectives:
  - Membedakan method array yang mengubah data asli (mutating) dan yang tidak mengubah data asli (non-mutating)
  - Menggunakan push(), pop(), slice(), includes(), dan find() sesuai kebutuhan kasus nyata
  - Menghindari kesalahan umum pemula saat membaca nilai kembalian masing-masing method array
codeLanguage: javascript
codeSnippet: |
  const antrean = ["Andi", "Bima", "Citra"];
  const adaBima = antrean.includes("Bima");
  const hurufC = antrean.find((nama) => nama.startsWith("C"));

  console.log(adaBima); // true
  console.log(hurufC);  // "Citra"
starterJs: |
  const daftarProduk = [
    { id: 1, nama: "Keyboard", stok: 5 },
    { id: 2, nama: "Mouse", stok: 0 },
    { id: 3, nama: "Monitor", stok: 3 }
  ];

  // 1. Cek keberadaan tag kategori dengan .includes()
  const kategori = ["elektronik", "kantor", "gaming"];
  const adaKategoriGaming = kategori.includes("gaming");
  console.log("Kategori gaming tersedia?", adaKategoriGaming);

  // 2. Cari satu objek produk yang cocok dengan .find()
  const produkKosong = daftarProduk.find((item) => item.stok === 0);
  console.log("Produk stok habis:", produkKosong.nama);

  // 3. Ambil potongan array tanpa merusak array asli dengan .slice()
  const duaProdukPertama = daftarProduk.slice(0, 2);
  console.log("Jumlah potongan:", duaProdukPertama.length);
  console.log("Jumlah asli tetap:", daftarProduk.length);
playgroundTask: |
  Jalankan kode bawaan dan periksa Console. Pastikan output menampilkan:
  Kategori gaming tersedia? true
  Produk stok habis: Mouse
  Jumlah potongan: 2
  Jumlah asli tetap: 3
verificationRules:
  - "js:.find("
  - "js:.includes("
---

# Method Array Esensial: push, pop, slice, includes, & find

Pada modul sebelumnya kamu sudah mengenal `push()` untuk menambah item ke dalam array. Di aplikasi web nyata, kita tidak hanya menambah data—kita juga perlu menghapus item terakhir, menyalin sebagian daftar, mengecek keberadaan item, serta mencari data tertentu.

Sebelum memakai method array, ada satu aturan penting yang wajib kamu pahami: **apakah method tersebut mengubah array aslinya (*mutating*) atau membiarkan array aslinya tetap utuh (*non-mutating*)?**

---

## Perbandingan Method Mutating vs Non-Mutating

| Method | Mengubah Array Asli? | Nilai Kembalian (*Return Value*) | Kegunaan Utama |
| :--- | :---: | :--- | :--- |
| **`push(item)`** | **Ya (Mutating)** | Panjang (*length*) array terbaru | Menambah satu atau lebih item ke akhir array |
| **`pop()`** | **Ya (Mutating)** | Elemen terakhir yang dihapus | Mengambil dan menghapus item paling belakang |
| **`slice(awal, akhir)`** | **Tidak (Non-Mutating)** | Array baru berisi potongan elemen | Menyalin sebagian array tanpa merusak array sumber |
| **`includes(nilai)`** | **Tidak (Non-Mutating)** | Boolean (`true` atau `false`) | Mengecek apakah sebuah nilai primitif ada di dalam array |
| **`find(callback)`** | **Tidak (Non-Mutating)** | Elemen **pertama** yang cocok (atau `undefined`) | Mencari satu item spesifik (sangat cocok untuk array of objects) |

---

## 1. Mengubah Ujung Array: `push()` dan `pop()`

`push()` dan `pop()` bekerja di bagian paling belakang array:

```javascript
const riwayat = ["Halaman Utama", "Produk"];
const panjangBaru = riwayat.push("Keranjang");

console.log(riwayat);     // ["Halaman Utama", "Produk", "Keranjang"]
console.log(panjangBaru); // 3

const halamanTerakhir = riwayat.pop();
console.log(halamanTerakhir); // "Keranjang"
console.log(riwayat);         // ["Halaman Utama", "Produk"]
```

---

## 2. Menyalin Sebagian Tanpa Merusak: `slice()`

`slice(awal, akhir)` menyalin elemen mulai dari indeks `awal` hingga **sebelum** indeks `akhir` (indeks `akhir` tidak ikut terbawa):

```javascript
const hari = ["Senin", "Selasa", "Rabu", "Kamis", "Jumat"];
const awalMinggu = hari.slice(0, 3);

console.log(awalMinggu); // ["Senin", "Selasa", "Rabu"]
console.log(hari);       // Tetap 5 hari (tidak berubah!)
```

Jika dipanggil tanpa argumen (`hari.slice()`), kamu mendapatkan salinan utuh array baru yang terpisah dari array aslinya.

---

## 3. Mengecek & Mencari Data: `includes()` dan `find()`

### `.includes()` untuk Cek Keberadaan Nilai Sederhana
Gunakan `includes()` jika kamu hanya ingin tahu jawaban "ada atau tidak":

```javascript
const peranDiizinkan = ["admin", "editor"];
console.log(peranDiizinkan.includes("editor")); // true
console.log(peranDiizinkan.includes("tamu"));   // false
```

### `.find()` untuk Mencari Item dengan Kondisi Khusus
Jika array berisi objek atau kamu perlu kriteria pencarian lebih kompleks, gunakan `find()`:

```javascript
const pengguna = [
  { id: 101, nama: "Sari" },
  { id: 102, nama: "Dimas" }
];

const target = pengguna.find((user) => user.id === 102);
console.log(target); // { id: 102, nama: "Dimas" }
```

---

## Kesalahan Umum Pemula yang Harus Dihindari

1. **Mengira `push()` mengembalikan array baru**:
   `const baru = arr.push("X");` akan membuat `baru` berisi angka panjang array (misalnya `4`), bukan array-nya! Panggil `arr.push("X")` sebagai baris perintah tersendiri.
2. **Lupa bahwa indeks akhir `slice(awal, akhir)` bersifat eksklusif**:
   `arr.slice(1, 3)` mengambil indeks `1` dan `2` saja—indeks `3` tidak ikut masuk.
3. **Langsung mengakses properti hasil `find()` tanpa pengecekan**:
   Jika data tidak ditemukan, `find()` mengembalikan `undefined`. Selalu cek dengan `if (hasil)` atau optional chaining (`hasil?.nama`) sebelum membaca propertinya agar tidak memicu `TypeError`.

Jalankan playground di bawah untuk mencoba `.includes()`, `.find()`, dan `.slice()` secara langsung!
