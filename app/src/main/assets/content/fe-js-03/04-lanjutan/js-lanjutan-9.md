---
id: js-lanjutan-9
courseId: fe-js-03
order: 29
title: "Destructuring & Spread Operator (...) untuk Kode Ringkas"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Mengekstrak nilai dari object dan array ke dalam variabel terpisah menggunakan Destructuring
  - Menggunakan nilai default dan penggantian nama variabel (aliasing) saat melakukan destructuring objek
  - Menyalin dan menggabungkan array maupun objek secara immutable dengan Spread Operator (...)
codeLanguage: javascript
codeSnippet: |
  const user = { nama: "Raka", peran: "Mentor" };
  const { nama, peran } = user;
  const userAktif = { ...user, aktif: true };

  console.log(nama, peran);
  console.log(userAktif);
starterJs: |
  const profilDasar = {
    nama: "Nadia",
    kota: "Bandung",
    level: 2
  };

  // 1. Object destructuring + default value
  const { nama, kota, lencana = "Pemula" } = profilDasar;
  console.log(`${nama} dari ${kota} (Lencana: ${lencana})`);

  // 2. Spread operator untuk membuat objek baru tanpa mengubah profilDasar
  const profilDiperbarui = {
    ...profilDasar,
    level: 3,
    aktif: true
  };

  // 3. Array destructuring & spread
  const hobiAwal = ["Coding", "Membaca"];
  const semuaHobi = [...hobiAwal, "Fotografi"];
  const [hobiUtama, hobiKedua] = semuaHobi;

  console.log("Level lama:", profilDasar.level, "| Level baru:", profilDiperbarui.level);
  console.log("Hobi pertama:", hobiUtama, "| Hobi kedua:", hobiKedua);
playgroundTask: |
  Jalankan kode bawaan dan buka Console. Pastikan output yang tercetak adalah:
  Nadia dari Bandung (Lencana: Pemula)
  Level lama: 2 | Level baru: 3
  Hobi pertama: Coding | Hobi kedua: Membaca
verificationRules:
  - "js:..."
  - "js:const {"
---

# Destructuring & Spread Operator (...) untuk Kode Ringkas

Saat bekerja dengan data objek atau array yang besar, menulis `pengguna.nama`, `pengguna.email`, dan `pengguna.kota` berulang-ulang membuat kode terasa panjang dan bertele-tele.

JavaScript modern (ES6+) menyediakan dua fitur favorit programmer web—**Destructuring** dan **Spread Operator (`...`)**—agar kode jauh lebih bersih, ringkas, dan aman dari bug mutasi data.

---

## Perbandingan Gaya Lama vs Gaya Modern ES6+

| Kebutuhan | Sebelum ES6 (Gaya Lama) | Sesudah ES6+ (Destructuring & Spread) |
| :--- | :--- | :--- |
| Ambil properti objek | `const nama = user.nama;`<br>`const kota = user.kota;` | `const { nama, kota } = user;` |
| Ambil elemen array | `const a = angka[0];`<br>`const b = angka[1];` | `const [a, b] = angka;` |
| Gabung dua array | `arr1.concat(arr2)` | `[...arr1, ...arr2]` |
| Salin & ubah objek | `Object.assign({}, obj, { aktif: true })` | `{ ...obj, aktif: true }` |

---

## 1. Object Destructuring

Destructuring berarti "membongkar" isi objek berdasarkan **nama propertinya** langsung menjadi variabel mandiri:

```javascript
const kursus = {
  judul: "JavaScript Fundamentals",
  durasiJam: 12,
  tingkat: "Pemula"
};

const { judul, tingkat } = kursus;
console.log(judul);   // "JavaScript Fundamentals"
console.log(tingkat); // "Pemula"
```

### Memberi Nilai Default & Mengganti Nama Variabel
Kamu bisa langsung memberi nilai cadangan jika properti belum ada, atau memberi nama alias dengan tanda titik dua (`:`):

```javascript
const { judul: namaKelas, sertifikat = true } = kursus;
console.log(namaKelas);  // "JavaScript Fundamentals"
console.log(sertifikat); // true
```

---

## 2. Array Destructuring

Berbeda dengan objek yang mencocokkan *nama properti*, destructuring array mencocokkan **posisi urutan (indeks)**:

```javascript
const warna = ["Merah", "Kuning", "Hijau"];
const [pertama, kedua] = warna;

console.log(pertama); // "Merah"
console.log(kedua);   // "Kuning"
```

Jika ingin melewati item tertentu, cukup biarkan kosong di antara tanda koma:
```javascript
const [, , ketiga] = warna;
console.log(ketiga); // "Hijau"
```

---

## 3. Spread Operator (`...`) untuk Salin & Gabung Aman

Jika kamu menyalin objek dengan tanda `=` biasa (`const salinan = asli;`), keduanya menunjuk ke objek yang sama di memori—mengubah `salinan` akan ikut merusak `asli`.

Dengan **Spread Operator (`...`)**, kamu membongkar seluruh isi wadah lama ke dalam wadah baru (**immutable copy**):

```javascript
const keranjangAwal = ["Buku", "Pena"];
const keranjangBaru = [...keranjangAwal, "Penggaris"];

console.log(keranjangAwal); // ["Buku", "Pena"] (aman, tidak berubah!)
console.log(keranjangBaru); // ["Buku", "Pena", "Penggaris"]
```

Hal yang sama berlaku untuk objek. Properti yang ditulis **setelah** `...objekLama` akan menimpa nilai properti yang sama pada salinan baru:

```javascript
const pengaturanAwal = { tema: "terang", suara: true };
const pengaturanBaru = { ...pengaturanAwal, tema: "gelap" };

console.log(pengaturanAwal.tema); // "terang"
console.log(pengaturanBaru.tema); // "gelap"
```

> **Catatan Penting:** Spread operator membuat salinan satu tingkat (*shallow copy*). Untuk objek sederhana atau daftar datar, pola ini sangat cepat, bersih, dan menjadi standar di framework modern seperti React maupun Vue.

Uji langsung kedua teknik ini di editor playground!
