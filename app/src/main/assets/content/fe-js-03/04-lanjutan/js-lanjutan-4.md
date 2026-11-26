---
id: js-lanjutan-4
courseId: fe-js-03
order: 24
title: "Menggambarkan Objek Nyata: JavaScript Object Dasar"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami struktur data Object sebagai representasi entitas dunia nyata di dalam kode
  - Menguasai pembuatan pasangan kunci dan nilai (key-value pairs) di dalam kurung kurawal
  - Mengakses serta memodifikasi properti objek menggunakan dot notation dan bracket notation
codeLanguage: javascript
codeSnippet: |
  const buku = {
    judul: "Belajar JavaScript",
    halaman: 250
  };
  console.log(buku.judul); // "Belajar JavaScript"
starterJs: |
  // 1. Mendefinisikan objek profil
  const profilPengguna = {
    nama: "Siti Rahmawati",
    keahlian: "Front-End Developer",
    usia: 24,
    aktif: true
  };

  // 2. Menambahkan properti baru ke objek
  profilPengguna.kota = "Bandung";

  console.log("Nama:", profilPengguna.nama);
  console.log("Keahlian:", profilPengguna.keahlian);
  console.log("Kota:", profilPengguna["kota"]);
playgroundTask: |
  Jalankan kode bawaan dan amati panel Console. Pastikan output yang dihasilkan adalah:
  Nama: Siti Rahmawati
  Keahlian: Front-End Developer
  Kota: Bandung
---

# Menggambarkan Objek Nyata: JavaScript Object Dasar

Array sangat hebat untuk menyimpan daftar item yang serupa (seperti kumpulan angka `[1, 2, 3]` atau nama warna). Namun, bagaimana jika kamu ingin menggambarkan satu orang secara utuh?
- Namanya siapa?
- Berapa usianya?
- Di mana kotanya?
- Apakah status akunnya aktif?

Jika menggunakan array `["Siti", 24, "Bandung", true]`, kamu harus mengingat bahwa indeks 0 adalah nama, indeks 1 adalah usia, dan seterusnya. Ini sangat membingungkan!

Solusi terbaik untuk memodelkan benda nyata adalah **Object**.

---

## Anatomi JavaScript Object

Objek dibuat menggunakan tanda kurung kurawal **`{ ... }`** dan menyimpan data dalam bentuk **Pasangan Kunci dan Nilai (*Key-Value Pairs*)**:

```javascript
const mobil = {
  merek: "Toyota",
  model: "Avanza",
  tahun: 2024,
  warna: "Putih",
  beroperasi: true
};
```

Setiap baris di atas disebut sebagai **Properti**:
- `merek` adalah **kunci (*key*)**.
- `"Toyota"` adalah **nilai (*value*)**.

---

## Cara Mengakses Properti Objek

Ada dua cara utama untuk mengambil nilai dari sebuah objek:

### 1. Dot Notation (Paling Sering Digunakan)
Cukup gunakan tanda titik (`.`) di antara nama objek dan nama propertinya:
```javascript
console.log(mobil.merek); // "Toyota"
console.log(mobil.tahun); // 2024
```

### 2. Bracket Notation (Kurung Siku Teks)
Gunakan tanda kurung siku dan masukkan nama kunci di dalam tanda kutip:
```javascript
console.log(mobil["warna"]); // "Putih"
```
Cara ini sangat berguna jika nama kuncinya disimpan di dalam variabel lain.

---

## Mengubah & Menambah Properti Baru

Kamu dapat mengubah nilai properti lama atau menambahkan properti baru dengan cara yang sangat intuitif:

```javascript
const pengguna = { nama: "Andi", skor: 80 };

// Mengubah properti yang sudah ada
pengguna.skor = 95;

// Menambahkan properti baru
pengguna.email = "andi@contoh.com";

console.log(pengguna);
// { nama: "Andi", skor: 95, email: "andi@contoh.com" }
```

> **Pertanyaan Menarik:** Mengapa kita boleh mengubah isi properti `pengguna.skor` padahal variabelnya dibuat dengan `const`?
> Karena kata kunci `const` hanya mengunci wadah objeknya agar tidak digantikan oleh objek lain. Isi lemari di dalam objek tersebut tetap bebas kamu rapikan dan modifikasi!

Di modul selanjutnya, kita akan menggabungkan kekuatan Array dan Fungsi dengan mempelajari **Metode Pengolah Array Modern: forEach, map, dan filter**!
