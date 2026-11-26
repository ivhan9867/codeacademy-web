---
id: js-lanjutan-1
courseId: fe-js-03
order: 21
title: "Resep Reusable: Deklarasi Fungsi & Parameter"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami konsep fungsi sebagai blok instruksi mandiri yang dapat digunakan berulang kali
  - Membedakan antara parameter (penampung di fungsi) dan argumen (nilai nyata saat dipanggil)
  - Menguasai peran kata kunci return dalam mengembalikan nilai hasil pemrosesan
codeLanguage: javascript
codeSnippet: |
  function sapa(nama) {
    return `Halo, ${nama}!`;
  }
  console.log(sapa("Dina")); // "Halo, Dina!"
starterJs: |
  // 1. Mendefinisikan resep fungsi perhitungan diskon
  function hitungDiskon(hargaAsli, persenDiskon) {
    const potongan = (hargaAsli * persenDiskon) / 100;
    return hargaAsli - potongan;
  }

  // 2. Memanggil fungsi berulang kali dengan data berbeda
  const hargaBaju = hitungDiskon(100000, 20);
  const hargaSepatu = hitungDiskon(300000, 10);

  console.log("Harga baju setelah diskon 20%: Rp" + hargaBaju);
  console.log("Harga sepatu setelah diskon 10%: Rp" + hargaSepatu);
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Pastikan output yang tercetak adalah:
  Harga baju setelah diskon 20%: Rp80000
  Harga sepatu setelah diskon 10%: Rp270000
---

# Resep Reusable: Deklarasi Fungsi & Parameter

Pernahkah kamu mendengar prinsip penting pemrograman **DRY (*Don't Repeat Yourself*)**? Artinya: jangan menuliskan logika yang sama berulang-ulang.

Jika kamu butuh menghitung diskon harga di 5 tempat berbeda, menyalin rumus perhitungan yang sama sebanyak 5 kali adalah cara kerja yang buruk. Jika rumus diskon berubah, kamu harus mencari dan memperbaikinya di kelima tempat tersebut.

Solusinya adalah membungkus logika tersebut ke dalam sebuah **Fungsi (*Function*)**.

---

## Analogi Mesin Jus & Resep Masakan

Bayangkan sebuah mesin blender pembuat jus:
1. **Bahan Masukan (*Parameter*)**: Kamu memasukkan buah jeruk dan sesendok gula.
2. **Proses Pengolahan**: Mesin memutar pisau dan memeras sarinya.
3. **Hasil Jadi (*Return*)**: Kamu mendapatkan segelas jus jeruk segar.

Di JavaScript, fungsi dideklarasikan menggunakan kata kunci **`function`**:

```javascript
// Mendefinisikan fungsi
function buatSapaan(nama) {
  return `Selamat pagi, ${nama}!`;
}

// Memanggil fungsi
const pesan1 = buatSapaan("Rina");
const pesan2 = buatSapaan("Budi");

console.log(pesan1);
console.log(pesan2);
```

Output:
```text
Selamat pagi, Rina!
Selamat pagi, Budi!
```

---

## Parameter vs Argumen: Apa Bedanya?

Dua istilah ini sering tertukar, namun perbedaannya sangat sederhana:
- **Parameter**: Nama variabel penampung yang ditulis saat kamu **merancang** fungsi. Pada contoh di atas, `nama` adalah parameter.
- **Argumen**: Nilai nyata yang kamu berikan saat **memanggil** fungsi. Pada contoh di atas, `"Rina"` dan `"Budi"` adalah argumen.

---

## Kekuatan Kata Kunci return

Kata kunci **`return`** bertugas mengembalikan nilai hasil kerja fungsi ke baris kode yang memanggilnya:

```javascript
function kali(a, b) {
  return a * b;
  console.log("Teks ini tidak akan pernah dijalankan!");
}

const hasil = kali(4, 5);
console.log(hasil); // 20
```

> **Perhatian:** Begitu instruksi `return` dieksekusi, fungsi akan **langsung berhenti dan keluar**. Baris kode apa pun yang diletakkan tepat di bawah `return` di dalam blok kurung kurawal yang sama akan diabaikan oleh komputer.

Di modul berikutnya, kita akan mempelajari gaya penulisan fungsi modern yang lebih ringkas dan paling disukai oleh developer web: **Arrow Function**!
