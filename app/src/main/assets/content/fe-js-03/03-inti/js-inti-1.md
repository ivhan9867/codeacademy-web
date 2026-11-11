---
id: js-inti-1
courseId: fe-js-03
order: 9
title: "Kotak Berlabel: Menyimpan Data dengan let dan const"
readingMinutes: 5
section: INTI
learningObjectives:
  - Memahami analogi variabel sebagai wadah berlabel untuk menyimpan nilai di memori komputer
  - Membedakan penggunaan kata kunci const (nilai konstan) dan let (nilai dinamis)
  - Memahami alasan meninggalkan kata kunci lama var demi keamanan kode modern
codeLanguage: javascript
codeSnippet: |
  const namaSitus = "CodeAcademy";
  let poin = 100;
  poin = poin + 25;

  console.log(namaSitus); // "CodeAcademy"
  console.log(poin);      // 125
starterJs: |
  // 1. Nilai tetap yang tidak akan berubah
  const namaAplikasi = "CodeAcademy";

  // 2. Nilai yang nilainya dapat bertambah
  let skorPengguna = 80;
  skorPengguna = skorPengguna + 15;

  console.log("Aplikasi:", namaAplikasi);
  console.log("Skor Akhir:", skorPengguna);
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Pastikan output yang muncul adalah:
  Aplikasi: CodeAcademy
  Skor Akhir: 95
  Perhatikan bagaimana variabel skorPengguna dapat diubah nilainya karena dideklarasikan dengan 'let'.
---

# Kotak Berlabel: Menyimpan Data dengan let dan const

Saat kamu berselancar di internet, aplikasi web perlu mengingat banyak hal: siapa nama akunmu, berapa barang yang ada di keranjang belanja, atau apakah kamu sudah menekan tombol *like*.

Untuk mengingat informasi tersebut, komputer menyediakan tempat di memori yang disebut **Variabel**.

![Analogi Variabel sebagai Kotak Berlabel](asset:images/fe-js-03/variabel-kotak.png)

---

## Analogi Kotak Penyimpanan Berlabel

Bayangkan kamu memiliki banyak kotak kardus di dalam kamar:
1. Kamu menempelkan stiker nama di luar kotak: misalnya label **`skor`**.
2. Kamu memasukkan sebuah barang ke dalamnya: misalnya angka **`100`**.
3. Kapan pun kamu butuh angka tersebut, kamu cukup memanggil nama label kotaknya: `console.log(skor)`.

Di JavaScript modern (ES6+), ada dua kata kunci utama untuk membuat kotak ini: **`const`** dan **`let`**.

---

## 1. const (Kotak Bersegel Permanen)

`const` adalah singkatan dari *constant* (tetap). Gunakan `const` untuk menyimpan data yang **tidak boleh diubah nilainya** sepanjang program berjalan:

```javascript
const pi = 3.14159;
const negaraAsal = "Indonesia";

console.log(negaraAsal);
```

Output:
```text
Indonesia
```

Jika kamu mencoba menimpa nilai `const`:
```javascript
const peran = "Admin";
peran = "User"; // TypeError: Assignment to constant variable.
```
Komputer akan menolak dan memunculkan error demi mencegah data penting terubah tanpa sengaja.

---

## 2. let (Kotak yang Isinya Boleh Diganti)

Gunakan `let` ketika nilai data di dalam kotak **memang akan berubah atau diperbarui** seiring interaksi pengguna:

```javascript
let nyawaPemain = 3;
console.log("Awal:", nyawaPemain);

// Pemain terkena rintangan
nyawaPemain = nyawaPemain - 1;
console.log("Setelah terkena rintangan:", nyawaPemain);
```

Output:
```text
Awal: 3
Setelah terkena rintangan: 2
```

> **Perhatikan:** Saat memperbarui nilai variabel `nyawaPemain`, kamu **tidak perlu** mengetikkan kata `let` lagi di depannya. Mengetik `let` hanya dilakukan sekali saat pertama kali membuat (*mendeklarasikan*) kotak tersebut.

---

## Mengapa Tidak Menggunakan var Lagi?

Sebelum tahun 2015, JavaScript hanya memiliki kata kunci `var`. Namun, `var` memiliki kelemahan serius: variabelnya bisa bocor keluar dari blok kode (*scope*) dan dapat ditimpa ulang tanpa peringatan, sehingga sering menimbulkan bug tersembunyi pada aplikasi skala besar.

**Aturan Praktik Terbaik Industri:**
> *"Gunakan **`const` secara default**. Jika nilainya nanti memang harus diperbarui (seperti penghitung angka atau skor), barulah gunakan **`let`**. Hindari penggunaan `var`."*

Di modul selanjutnya, kita akan mengeksplorasi apa saja jenis-jenis benda (*tipe data*) yang bisa dimasukkan ke dalam kotak variabel ini!
