---
id: js-lanjutan-2
courseId: fe-js-03
order: 22
title: "Gaya Modern: Arrow Function yang Ringkas"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - Memahami sintaks panah (`=>`) yang diperkenalkan pada standar ES6
  - Menguasai penulisan arrow function standar dan bentuk ringkas (*implicit return*)
  - Mengetahui keunggulan arrow function dalam membuat kode manipulasi data lebih bersih
codeLanguage: javascript
codeSnippet: |
  const kaliDua = (x) => x * 2;
  console.log(kaliDua(5)); // 10
starterJs: |
  // 1. Arrow function dengan blok kurung kurawal
  const hitungLuasPersegiPanjang = (panjang, lebar) => {
    return panjang * lebar;
  };

  // 2. Arrow function sebaris (implicit return: tanpa return dan tanpa kurung kurawal)
  const kuadrat = (angka) => angka * angka;

  console.log("Luas meja (5 x 3):", hitungLuasPersegiPanjang(5, 3));
  console.log("Kuadrat dari 7:", kuadrat(7));
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Pastikan outputnya adalah:
  Luas meja (5 x 3): 15
  Kuadrat dari 7: 49
---

# Gaya Modern: Arrow Function yang Ringkas

Pada standar ES6 (2015), JavaScript memperkenalkan cara baru yang sangat populer untuk menulis fungsi: **Arrow Function** (fungsi panah).

Dinamakan demikian karena menggunakan simbol panah tebal **`=>`** (dibuat dari tanda sama dengan dan tanda lebih besar).

---

## Transformasi dari Fungsi Tradisional

Perhatikan bagaimana sebuah fungsi penjumlahan bertransformasi:

### Cara Lama (Tradisional):
```javascript
function tambah(a, b) {
  return a + b;
}
```

### Cara Modern (Arrow Function):
```javascript
const tambah = (a, b) => {
  return a + b;
};
```

Pada dasarnya, kata kunci `function` dihilangkan, lalu digantikan oleh simbol panah `=>` di antara tanda kurung parameter dan kurung kurawal `{}`.

---

## Kekuatan Super: Implicit Return (Sebaris Saja)

Kelebihan utama arrow function terasa saat isi fungsi kamu sangat sederhana (hanya satu baris perhitungan). Kamu boleh **menghapus tanda kurung kurawal `{}` DAN kata kunci `return`**:

```javascript
// Sangat ringkas dan elegan!
const kaliTiga = (angka) => angka * 3;

console.log(kaliTiga(4)); // Output: 12
```

Ketika kamu tidak menggunakan kurung kurawal, JavaScript otomatis mengembalikan nilai hasil ekspresi tersebut (*implicit return*).

---

## Kapan Sebaiknya Menggunakan Arrow Function?
1. **Untuk fungsi ringkas pemroses data**: Sangat cocok digunakan bersama metode pengolah array modern seperti `.map()` dan `.filter()` yang akan kita pelajari sebentar lagi.
2. **Sebagai fungsi penangan aksi (*event callback*)**: Sangat praktis saat membuat aksi klik tombol pada halaman web.

Di modul berikutnya, kita akan menyelidiki di mana saja variabel boleh hidup dan diakses melalui konsep **Scope**!
