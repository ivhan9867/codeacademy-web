---
id: js-pend-3
courseId: fe-js-03
order: 6
title: "Jendela Pertama: Menampilkan Output dengan console.log()"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Memahami anatomi perintah console.log() untuk memeriksa isi data program
  - Mengetahui cara mencetak string, angka, dan beberapa nilai sekaligus dalam satu panggilan
  - Memanfaatkan Console sebagai alat bantu utama untuk memantau nilai variabel secara langsung
codeLanguage: javascript
codeSnippet: |
  console.log("Nama:", "Vhan");
  console.log("Usia:", 20);
  console.log("Status:", true);
starterJs: |
  // Eksperimen mencetak data diri kamu
  const nama = "Vhan";
  const modulSelesai = 6;

  console.log("Halo, nama saya", nama);
  console.log("Jumlah modul yang sudah saya pelajari:", modulSelesai);
playgroundTask: |
  Jalankan kode dan buka panel Console di bagian bawah pratinjau. Pastikan Console menampilkan dua baris:
  Halo, nama saya Vhan
  Jumlah modul yang sudah saya pelajari: 6
---

# Jendela Pertama: Menampilkan Output dengan console.log()

Dalam membangun aplikasi web, kamu tidak bisa selalu mengandalkan tampilan visual untuk mengetahui apa yang sedang terjadi di dalam memori komputer.

Sering kali kita perlu memeriksa: *"Apakah data formulir sudah tersimpan?"*, *"Berapa hasil perkalian ini?"*, atau *"Apakah tombol ini benar-benar diklik?"*. Di sinilah fungsi **`console.log()`** menjadi sahabat terbaik programmer.

---

## Membedah Sintaks console.log()

Sintaks ini terdiri dari dua bagian:
- `console`: Objek bawaan peramban yang mewakili konsol pengembang (*developer console*).
- `.log(...)`: Metode atau aksi untuk mencetak informasi yang dimasukkan ke dalam tanda kurung ke layar konsol.

### 1. Mencetak Teks (String)
Teks harus selalu dibungkus dengan tanda kutip ganda (`"..."`) atau kutip tunggal (`'...'`):

```javascript
console.log("Selamat pagi, Web Developer!");
```

Output:
```text
Selamat pagi, Web Developer!
```

### 2. Mencetak Angka (Number) & Operasi Matematika
Angka ditulis langsung tanpa tanda kutip. Jika kamu memasukkan perhitungan matematika, JavaScript akan menyelesaikannya terlebih dahulu sebelum mencetak hasilnya:

```javascript
console.log(100);
console.log(15 + 25);
```

Output:
```text
100
40
```

> **Perhatian:** Jangan bungkus angka dengan tanda kutip jika kamu ingin menghitungnya. `console.log("15 + 25")` akan mencetak teks murni `"15 + 25"`, bukan angka `40`.

### 3. Mencetak Banyak Nilai Sekaligus
Kamu dapat memasukkan beberapa nilai ke dalam satu perintah `console.log()` dengan memisahkannya menggunakan tanda koma (`,`). JavaScript akan otomatis menambahkan spasi pemisah di antara setiap nilai:

```javascript
console.log("Skor pemain:", 95, "Poin");
```

Output:
```text
Skor pemain: 95 Poin
```

---

## Saatnya Berlatih!

Lihat kode pada editor di bawah:
1. Kode tersebut mencetak string dan angka menggunakan pemisah tanda koma.
2. Tekan tombol **Jalankan**.
3. Buka panel **Console** dan verifikasi kedua baris output yang dihasilkan.
4. Jangan ragu mengubah nama `"Vhan"` menjadi namamu sendiri, lalu tekan **Jalankan** lagi untuk melihat perubahannya seketika!
