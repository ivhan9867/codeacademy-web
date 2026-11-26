---
id: js-inti-8
courseId: fe-js-03
order: 18
title: "Rak Bernomor: Menyimpan Koleksi dengan Array Dasar"
readingMinutes: 6
section: INTI
learningObjectives:
  - Memahami struktur data Array sebagai wadah untuk menyimpan deretan nilai dalam satu variabel
  - Memahami konsep indeks berbasis nol (zero-based index) saat mengambil atau mengubah data
  - Memanfaatkan properti length dan method push() untuk memanipulasi ukuran array
codeLanguage: javascript
codeSnippet: |
  const buah = ["Apel", "Jeruk", "Mangga"];
  console.log(buah[0]);      // "Apel"
  console.log(buah.length);  // 3
starterJs: |
  const daftarBelanja = ["Buku", "Pensil", "Penggaris"];

  // Menambahkan barang baru di urutan paling belakang
  daftarBelanja.push("Penghapus");

  console.log("Item pertama:", daftarBelanja[0]);
  console.log("Jumlah seluruh item:", daftarBelanja.length);
  console.log("Daftar lengkap:", daftarBelanja.join(", "));
playgroundTask: |
  Jalankan kode bawaan. Pastikan Console menampilkan:
  Item pertama: Buku
  Jumlah seluruh item: 4
  Daftar lengkap: Buku, Pensil, Penggaris, Penghapus
---

# Rak Bernomor: Menyimpan Koleksi dengan Array Dasar

Bayangkan kamu memiliki daftar 10 siswa di kelas. Jika kamu membuat variabel terpisah untuk setiap siswa:
```javascript
const siswa1 = "Budi";
const siswa2 = "Siti";
const siswa3 = "Rani";
// ... sampai siswa10
```
Kode ini sangat merepotkan untuk dikelola. Bagaimana jika jumlah siswanya bertambah menjadi 100?

Untuk menyimpan kumpulan banyak data di dalam satu nama variabel, JavaScript menyediakan wadah spesial bernama **Array**.

```text
const buah = ["Apel", "Jeruk", "Mangga", "Pisang"];
Index :          0        1         2         3
Length: 4 (indeks terakhir = buah.length - 1)
```
---

## Analogi Rak Bernomor

Bayangkan sebuah lemari rak sepatu yang memiliki nomor laci berurutan:
- Di laci nomor 0, ditaruh sepatu hitam.
- Di laci nomor 1, ditaruh sepatu lari.
- Di laci nomor 2, ditaruh sandal.

Di JavaScript, array ditulis menggunakan tanda kurung siku **`[...]`** dan setiap item dipisahkan oleh tanda koma:

```javascript
const namaSiswa = ["Budi", "Siti", "Rani"];
```

---

## Aturan Emas: Indeks Dimulai dari 0 (Zero-Based Index)

Salah satu hal paling penting yang harus diingat pemula: **komputer selalu mulai berhitung dari angka 0, bukan 1**.

Untuk mengambil data dari dalam array, gunakan nama array diikuti nomor indeksnya di dalam tanda kurung siku:

```javascript
const hobi = ["Membaca", "Ngoding", "Bersepeda"];

console.log(hobi[0]); // Elemen pertama: "Membaca"
console.log(hobi[1]); // Elemen kedua: "Ngoding"
console.log(hobi[2]); // Elemen ketiga: "Bersepeda"
```

Output:
```text
Membaca
Ngoding
Bersepeda
```

---

## Dua Fitur Penting Array

### 1. Mengetahui Jumlah Item (`.length`)
Gunakan properti `.length` untuk mengetahui berapa total item yang ada di dalam array:
```javascript
const daftarWarna = ["Merah", "Kuning", "Hijau"];
console.log(daftarWarna.length); // 3
```

### 2. Menambah Item Baru (`.push()`)
Gunakan metode `.push()` untuk memasukkan data baru ke posisi paling akhir:
```javascript
const tugas = ["Belajar HTML", "Belajar CSS"];
tugas.push("Belajar JavaScript");

console.log(tugas); // ["Belajar HTML", "Belajar CSS", "Belajar JavaScript"]
console.log(tugas.length); // 3
```

Selamat! Kamu sudah menyelesaikan seluruh materi **Bagian 3: Logika & Alur Program**. Di bagian berikutnya, kita akan melompat ke materi yang lebih seru: meracik fungsi, membedah objek, dan menghidupkan halaman web melalui manipulasi DOM!
