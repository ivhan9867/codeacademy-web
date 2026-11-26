---
id: js-pend-5
courseId: fe-js-03
order: 10
title: "Bersahabat dengan Error: Cara Membaca Pesan Error di Console"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Memahami anatomi pesan error di Console peramban tanpa rasa takut
  - Mengenali tiga tipe error paling umum: SyntaxError, ReferenceError, dan TypeError
  - Mengembangkan keterampilan dasar melacak lokasi baris kode yang bermasalah (debugging)
codeLanguage: javascript
codeSnippet: |
  // 1. SyntaxError: Tanda kutip tidak ditutup
  // const nama = "Andi;

  // 2. ReferenceError: Variabel belum dideklarasikan
  // console.log(umur);

  // 3. TypeError: Mengubah nilai konstanta
  // const pi = 3.14;
  // pi = 3.14159;
starterJs: |
  // Kode di bawah ini sudah diperbaiki dan siap dijalankan:
  const pesanPenyemangat = "Error adalah bukti kamu sedang belajar!";
  console.log("Status:", pesanPenyemangat);
playgroundTask: |
  Jalankan kode bawaan dan pastikan Console menampilkan:
  Status: Error adalah bukti kamu sedang belajar!
  Untuk eksperimen, coba ganti nama variabel di baris console.log dengan nama yang salah (misalnya pesanSalah), lalu jalankan kembali untuk melihat bagaimana ReferenceError ditampilkan di Console.
---

# Bersahabat dengan Error: Cara Membaca Pesan Error di Console

Ketika kamu menulis kode dan terjadi kesalahan, program akan berhenti dan menampilkan teks berwarna merah di panel Console.

Bagi pemula, teks merah ini sering kali tampak menakutkan. Padahal, **pesan error adalah petunjuk terbaik yang diberikan komputer** untuk membantumu menemukan masalah dengan cepat. Proses mencari dan memperbaiki kesalahan ini disebut **Debugging**.

---

## Anatomi Pesan Error

Ketika terjadi kesalahan, Console biasanya memberikan tiga informasi utama:
1. **Nama Error (*Error Type*)**: Jenis pelanggaran aturan yang terjadi.
2. **Deskripsi Masalah (*Message*)**: Penjelasan mengapa hal tersebut dilarang.
3. **Lokasi Baris (*Line Number*)**: Pada baris ke berapa masalah tersebut pertama kali terdeteksi.

Mari kita pelajari tiga jenis error yang paling sering kamu temui:

---

## 1. SyntaxError (Kesalahan Tata Bahasa)
Terjadi saat kamu melanggar aturan penulisan JavaScript, mirip seperti lupa tanda titik atau salah ejaan dalam bahasa manusia.

**Contoh Kasus:**
```javascript
const sapaan = "Halo semuanya;
```

**Pesan di Console:**
```text
Uncaught SyntaxError: Invalid or unexpected token
```
> **Arti:** JavaScript menemukan tanda kutip pembuka `"`, tetapi baris sudah berakhir sebelum menemukan tanda kutip penutup pasangannya.

---

## 2. ReferenceError (Referensi Tidak Ditemukan)
Terjadi saat kamu menyuruh komputer memanggil atau menggunakan sebuah variabel yang **belum pernah dibuat**.

**Contoh Kasus:**
```javascript
console.log(namaLengkap);
```

**Pesan di Console:**
```text
Uncaught ReferenceError: namaLengkap is not defined
```
> **Arti:** Komputer mencari ke seluruh memori, tetapi tidak ada kotak bernama `namaLengkap`. Mungkin kamu lupa mendeklarasikannya terlebih dahulu, atau ada salah ketik huruf kapital (*typo*).

---

## 3. TypeError (Kesalahan Tipe / Operasi Terlarang)
Terjadi ketika kamu memaksa komputer melakukan operasi yang tidak diizinkan pada jenis data tersebut.

**Contoh Kasus:**
```javascript
const koin = 100;
koin = 200; // Variabel const tidak boleh diisi ulang
```

**Pesan di Console:**
```text
Uncaught TypeError: Assignment to constant variable.
```
> **Arti:** Kamu sudah berjanji bahwa `koin` adalah nilai tetap (*constant*), namun di baris berikutnya kamu mencoba menimpanya dengan angka baru.

---

## Tiga Langkah Mengatasi Error Seperti Profesional
1. **Tarik Napas & Baca Pesannya**: Jangan langsung menutup Console. Baca perlahan apa jenis error-nya.
2. **Cek Nomor Barisnya**: Klik atau lihat angka baris yang ditunjuk (misalnya `:3`). Periksa baris tersebut dan satu baris tepat di atasnya.
3. **Periksa Ejaan & Pasangan Tanda**: 80% error pemula disebabkan oleh huruf kapital yang berbeda, tanda kutip yang belum ditutup, atau tanda kurung `()` yang kurang satu.

Kini kamu sudah memahami dasar-dasar ekosistem JavaScript! Di bagian selanjutnya (**Bagian 3: Logika & Alur Program**), kita akan mulai mendalami inti kekuatan pemrograman: variabel, tipe data, percabangan, dan perulangan.
