---
id: js-inti-3
courseId: fe-js-03
order: 13
title: "Operator Matematika, Perbandingan & Logika"
readingMinutes: 6
section: INTI
learningObjectives:
  - Menguasai operator aritmatika standar termasuk sisa bagi (modulo %)
  - Memahami keharusan menggunakan perbandingan ketat (===) dibanding perbandingan longgar (==)
  - Menerapkan operator logika AND (&&), OR (||), dan NOT (!) untuk merumuskan kondisi ganda
codeLanguage: javascript
codeSnippet: |
  console.log(10 % 3);       // 1 (sisa bagi)
  console.log(5 === "5");    // false (tipe berbeda)
  console.log(true && false);// false
starterJs: |
  const hargaBuku = 50000;
  const uangSaku = 75000;
  const punyaKupon = true;

  // Evaluasi kondisi
  const cukupUang = uangSaku >= hargaBuku;
  const bolehBeli = cukupUang && punyaKupon;

  console.log("Cukup uang?", cukupUang);
  console.log("Boleh beli?", bolehBeli);
  console.log("Sisa uang jika dibeli:", uangSaku - hargaBuku);
playgroundTask: |
  Jalankan kode bawaan. Pastikan Console mencetak tepat 3 baris:
  Cukup uang? true
  Boleh beli? true
  Sisa uang jika dibeli: 25000
---

# Operator Matematika, Perbandingan & Logika

Setelah berhasil menyimpan data ke dalam variabel, kamu membutuhkan alat untuk mengolah dan membandingkan data tersebut. Alat ini dinamakan **operator**.

Di JavaScript, operator terbagi menjadi tiga kelompok utama: matematika, perbandingan, dan logika.

---

## 1. Kelompok Operator Matematika (Aritmatika)

Operator matematika digunakan untuk melakukan perhitungan angka dasar:

- `+` (Penjumlahan)
- `-` (Pengurangan)
- `*` (Perkalian)
- `/` (Pembagian)
- **`%` (Modulo / Sisa Bagi)**: Menghitung sisa angka setelah proses pembagian bulat selesai.

### Contoh Kode Matematika

```javascript
const hargaItem = 25000;
const jumlah = 3;
const total = hargaItem * jumlah;
const sisaBagi = 10 % 3;

console.log("Total:", total);
console.log("Sisa bagi 10 % 3:", sisaBagi);
```

Output:
```text
Total: 75000
Sisa bagi 10 % 3: 1
```

> **Catatan Modulo:** Angka `10` dibagi `3` menghasilkan `3`, dengan sisa `1`. Programmer sering menggunakan modulo untuk mengecek angka genap (`angka % 2 === 0`).

---

## 2. Kelompok Operator Perbandingan (*Comparison*)

Operator perbandingan membandingkan dua nilai. Hasil perbandingan selalu berupa nilai **Boolean**, yaitu `true` (benar) atau `false` (salah):

- `>` (Lebih besar dari)
- `<` (Lebih kecil dari)
- `>=` (Lebih besar atau sama dengan)
- `<=` (Lebih kecil atau sama dengan)
- **`===` (Sama dengan ketat / *strict equality*)**: Memeriksa kesamaan nilai sekaligus tipe datanya.
- **`!==` (Tidak sama dengan ketat)**: Memeriksa apakah nilai atau tipe datanya berbeda.

### Perbedaan Besar Antara `==` dan `===`

JavaScript memiliki operator lama `==` (*loose equality*). Operator ini melakukan **pemaksaan tipe data** (*type coercion*). Artinya, JavaScript diam-diam mengubah tipe data agar terlihat cocok.

Sebaliknya, operator modern `===` (*strict equality*) memeriksa nilai dan tipe data tanpa kompromi.

Perhatikan perbandingannya:

```javascript
console.log(10 == "10");
console.log(10 === "10");
console.log(15 > 10);
console.log(5 !== 10);
```

Output:
```text
true
false
true
true
```

### Mengapa Hasilnya Berbeda?
- Baris pertama `10 == "10"` menghasilkan `true`. JavaScript secara sepihak mengubah teks `"10"` menjadi angka `10`.
- Baris kedua `10 === "10"` menghasilkan `false`. Tipe angka (`number`) jelas tidak sama dengan teks (`string`).

**Aturan Penting:** Selalu gunakan **`===`** tiga sama dengan di proyekmu untuk mencegah bug tersembunyi.

---

## 3. Kelompok Operator Logika (*Logical Operators*)

Operator logika digunakan saat kamu ingin menguji beberapa syarat sekaligus:

1. **`&&` (AND / Dan)**: Menghasilkan `true` hanya jika **semua** syarat bernilai benar.
2. **`||` (OR / Atau)**: Menghasilkan `true` jika **salah satu** syarat sudah bernilai benar.
3. **`!` (NOT / Bukan)**: Membalik nilai Boolean. Mengubah `true` menjadi `false`, dan sebaliknya.

### Contoh Kode Logika

Bayangkan aturan masuk bioskop: penonton harus memiliki tiket DAN kartu member untuk mendapat diskon:

```javascript
const punyaTiket = true;
const punyaMember = false;
const sudahCheckIn = true;

// Syarat diskon: harus punya tiket DAN punya member
console.log(punyaTiket && punyaMember);

// Syarat masuk bioskop: cukup punya tiket ATAU punya member
console.log(punyaTiket || punyaMember);

// Membalik status check-in
console.log(!sudahCheckIn);
```

Output:
```text
false
true
false
```

Pada modul berikutnya, kita akan memanfaatkan ekspresi ini untuk merangkai teks interaktif menggunakan fitur *Template Literals*.
