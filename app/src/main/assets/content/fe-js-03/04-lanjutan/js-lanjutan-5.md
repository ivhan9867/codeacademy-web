---
id: js-lanjutan-5
courseId: fe-js-03
order: 21
title: "Mengolah Koleksi Modern: Method forEach, map, dan filter"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Memahami keunggulan metode array modern dibanding perulangan manual for loop
  - Menguasai metode forEach untuk menjalankan aksi pada setiap elemen array
  - Menerapkan map untuk mentransformasi data dan filter untuk menyaring elemen tanpa mengubah array asal
codeLanguage: javascript
codeSnippet: |
  const angka = [1, 2, 3];
  const kaliDua = angka.map((x) => x * 2);
  console.log(kaliDua); // [2, 4, 6]
starterJs: |
  const daftarNilai = [60, 75, 80, 95, 50];

  // 1. filter: Menyaring nilai yang memenuhi syarat kelulusan (>= 70)
  const nilaiLulus = daftarNilai.filter((nilai) => nilai >= 70);

  // 2. map: Mentransformasi setiap nilai dengan menambahkan bonus 5 poin
  const nilaiDenganBonus = daftarNilai.map((nilai) => nilai + 5);

  console.log("Nilai Lulus:", nilaiLulus.join(", "));
  console.log("Nilai + Bonus 5:", nilaiDenganBonus.join(", "));
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Pastikan outputnya adalah:
  Nilai Lulus: 75, 80, 95
  Nilai + Bonus 5: 65, 80, 85, 100, 55
---

# Mengolah Koleksi Modern: Method forEach, map, dan filter

Di modul sebelumnya, kita menggunakan perulangan `for (let i = 0; i < array.length; i++)` untuk menelusuri isi array. Cara itu berhasil, tetapi cukup panjang dan rawan salah ketik tanda titik koma.

JavaScript modern menyediakan metode bawaan yang jauh lebih elegan dan ekspresif. Tiga yang paling penting adalah **`forEach`**, **`map`**, dan **`filter`**.

---

## 1. forEach(): Menjalankan Aksi untuk Setiap Item

Gunakan `.forEach()` jika kamu hanya ingin melakukan tindakan sampingan (seperti mencetak data ke konsol) untuk setiap anggota array:

```javascript
const buah = ["Apel", "Jeruk", "Melon"];

buah.forEach((item, indeks) => {
  console.log(`Buah ke-${indeks + 1}: ${item}`);
});
```

Output:
```text
Buah ke-1: Apel
Buah ke-2: Jeruk
Buah ke-3: Melon
```

---

## 2. map(): Mengubah Bentuk Data (Transformasi)

Gunakan `.map()` jika kamu ingin **mengubah setiap elemen** menjadi bentuk baru.

Fungsi ini akan mengembalikan **array baru** dengan panjang yang sama persis tanpa mengubah array aslinya:

```javascript
const hargaRupiah = [10000, 25000, 50000];

// Gandakan semua harga
const hargaGanda = hargaRupiah.map((harga) => harga * 2);

console.log(hargaGanda);
// Output: [20000, 50000, 100000]
```

---

## 3. filter(): Menyaring Item Pilihan

Gunakan `.filter()` jika kamu ingin **memilih sebagian item** yang memenuhi kriteria tertentu.

Fungsi di dalam filter harus mengembalikan nilai boolean (`true` atau `false`). Jika `true`, item tersebut dimasukkan ke array baru; jika `false`, item dibuang:

```javascript
const angka = [12, 5, 8, 130, 44];

// Hanya ambil angka yang lebih besar dari 10
const angkaBesar = angka.filter((item) => item > 10);

console.log(angkaBesar);
// Output: [12, 130, 44]
```

---

## Ringkasan Perbedaan Ketiganya

| Metode | Tujuan Utama | Menghasilkan Nilai Baru? |
|:---|:---|:---:|
| **`forEach()`** | Hanya menjalankan aksi (misalnya cetak log) | Tidak (`undefined`) |
| **`map()`** | Mengubah setiap item menjadi item baru | Ya (Array Baru) |
| **`filter()`** | Menyaring item berdasarkan syarat benar/salah | Ya (Array Baru) |

Hebatnya, `.map()` dan `.filter()` bersifat **aman (*immutable*)**: data awal di array asli kamu tetap utuh dan tidak rusak!

Di modul berikutnya, kita akan menghubungkan kode JavaScript dengan dunia nyata: memanipulasi elemen HTML di halaman web menggunakan **DOM**!
