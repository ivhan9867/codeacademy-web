---
id: js-inti-5
courseId: fe-js-03
order: 13
title: "Mengambil Keputusan: Percabangan if, else if, dan else"
readingMinutes: 6
section: INTI
learningObjectives:
  - Memahami prinsip percabangan kode untuk membuat program mampu mengambil keputusan
  - Menguasai struktur penulisan if, else if, dan else secara bertingkat
  - Memahami bagaimana urutan pengujian kondisi mempengaruhi cabang mana yang dieksekusi
codeLanguage: javascript
codeSnippet: |
  const umur = 18;
  if (umur >= 17) {
    console.log("Boleh membuat KTP");
  } else {
    console.log("Belum cukup umur");
  }
starterJs: |
  const nilaiUjian = 85;

  // Evaluasi predikat nilai
  if (nilaiUjian >= 90) {
    console.log("Predikat: A (Luar Biasa)");
  } else if (nilaiUjian >= 75) {
    console.log("Predikat: B (Sangat Baik)");
  } else {
    console.log("Predikat: C (Perlu Peningkatan)");
  }
playgroundTask: |
  Jalankan kode bawaan. Pastikan Console menampilkan:
  Predikat: B (Sangat Baik)
  Coba ubah variabel nilaiUjian menjadi 92 (akan memicu cabang A) atau 65 (akan memicu cabang C) untuk melihat alur percabangan bekerja!
---

# Mengambil Keputusan: Percabangan if, else if, dan else

Program komputer tidak hanya menjalankan baris kode secara lurus dari atas ke bawah. Program yang cerdas harus bisa mengambil keputusan:
- *"Jika saldo mencukupi, lanjutkan transaksi. Jika tidak, tampilkan peringatan."*
- *"Jika kata sandi benar, buka halaman utama. Jika salah, kunci akun."*

Kemampuan memilih jalur ini disebut **Percabangan (Control Flow)**.

![Alur Pengambilan Keputusan If Else](asset:images/fe-js-03/alur-percabangan.png)

---

## 1. Percabangan Dasar: if dan else

Struktur paling dasar menggunakan kata kunci `if` (jika) dan `else` (selain itu):

```javascript
const suhuTubuh = 38.2;

if (suhuTubuh >= 37.5) {
  console.log("Status: Demam");
} else {
  console.log("Status: Normal");
}
```

Output:
```text
Status: Demam
```

**Cara Kerja:**
1. JavaScript memeriksa kondisi di dalam tanda kurung `(suhuTubuh >= 37.5)`.
2. Karena `38.2 >= 37.5` bernilai `true`, blok kode di dalam `{}` pertama dieksekusi.
3. Blok `else` otomatis dilewati.

---

## 2. Menghadapi Banyak Pilihan: else if

Jika pilihan kondisi lebih dari dua, gunakan `else if` di antara `if` dan `else`:

```javascript
const jam = 14;

if (jam < 12) {
  console.log("Selamat Pagi!");
} else if (jam < 18) {
  console.log("Selamat Siang!");
} else {
  console.log("Selamat Malam!");
}
```

Output:
```text
Selamat Siang!
```

---

## Aturan Emas Urutan Pengecekan

JavaScript mengecek kondisi **dari atas ke bawah**. Begitu menemukan kondisi yang bernilai `true`:
1. Blok kode bersangkutan langsung dijalankan.
2. Seluruh cabang di bawahnya **diabaikan dan tidak akan diperiksa lagi**.

Oleh karena itu, selalu susun syarat dari yang paling spesifik atau bernilai paling tinggi ke yang lebih umum.

Di modul berikutnya, kita akan mempelajari alternatif percabangan bernama `switch` yang sangat rapi untuk mencocokkan nilai tunggal!
