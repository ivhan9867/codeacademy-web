---
id: js-lanjutan-8
courseId: fe-js-03
order: 28
title: "Menunggu Tanpa Macet: Pengantar Singkat Promise & async/await"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami perbedaan eksekusi Synchronous (seketika & berurutan) dan Asynchronous (tidak membekukan antrean)
  - Mengenal konsep Promise sebagai representasi nilai masa depan
  - Menggunakan pasangan kata kunci async dan await untuk membaca proses asinkron dengan gaya kode yang bersih
codeLanguage: javascript
codeSnippet: |
  const jeda = () => new Promise((res) => setTimeout(res, 500));
  const jalan = async () => {
    console.log("Mulai");
    await jeda();
    console.log("Selesai");
  };
  jalan();
starterJs: |
  // Fungsi simulasi tugas asinkron dengan jeda waktu 1 detik
  const tungguSatuDetik = () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve("Data berhasil disiapkan!");
      }, 1000);
    });
  };

  // Menjalankan fungsi asinkron dengan async dan await
  const prosesData = async () => {
    console.log("1. Memulai proses...");
    const hasil = await tungguSatuDetik();
    console.log("2.", hasil);
    console.log("3. Selesai!");
  };

  prosesData();
playgroundTask: |
  Jalankan kode bawaan. Buka panel Console dan tunggu sejenak (1 detik). Pastikan Console mencetak 3 baris secara berurutan:
  1. Memulai proses...
  2. Data berhasil disiapkan!
  3. Selesai!
---

# Menunggu Tanpa Macet: Pengantar Singkat Promise & async/await

Secara alami, JavaScript mengeksekusi instruksi secara **Sinkron (*Synchronous*)**: baris 2 baru dijalankan setelah baris 1 selesai, baris 3 baru berjalan setelah baris 2 tuntas.

Namun, bagaimana jika ada satu tugas yang membutuhkan waktu beberapa detik?
Misalnya: menunggu berkas gambar selesai diunduh atau memuat data dari database.

Jika JavaScript menunggu sambil membeku (*blocking*), tombol di layar tidak bisa diklik, animasi macet, dan pengguna akan mengira aplikasi kita rusak!

---

## Analogi Antrean Kedai Kopi

- **Sistem Sinkron (*Blocking*)**:
  Kamu memesan segelas kopi. Kasir menolak melayani orang berikutnya dan membiarkan antrean mengular panjang selama 5 menit sampai kopimu selesai diseduh. Seluruh kedai macet total.
- **Sistem Asinkron (*Non-Blocking*)**:
  Kamu memesan kopi dan membayar. Kasir memberimu **struk nomor antrean (*Promise*)**. Kamu dipersilakan duduk santai. Kasir langsung melayani pembeli berikutnya. Begitu kopimu matang, namamu dipanggil dan kopimu diserahkan.

---

## Mengenal Promise (Janji Nilai Masa Depan)

**Promise** adalah objek JavaScript yang mewakili sebuah janji:
> *"Tugas ini belum selesai sekarang, tapi saya berjanji akan memberi tahu hasilnya nanti (apakah berhasil atau gagal)."*

Sebuah Promise memiliki 3 status:
1. `pending`: Sedang dalam proses (kopi sedang diseduh).
2. `fulfilled`: Berhasil selesai ditepati (kopi siap dinikmati).
3. `rejected`: Gagal/dibatalkan (biji kopi habis).

---

## Gaya Modern: async dan await

Sebelumnya, pengembang web menggunakan rantai `.then()` yang panjang untuk menangani Promise. Sejak ES2017, JavaScript memperkenalkan pasangan kata kunci yang jauh lebih bersih: **`async`** dan **`await`**.

1. **`async`**: Ditaruh di depan deklarasi fungsi untuk memberi tahu komputer bahwa di dalam fungsi ini terdapat proses yang menunggu waktu.
2. **`await`**: Ditaruh sebelum pemanggilan Promise untuk menjeda eksekusi fungsi tersebut secara rapi sampai hasilnya selesai, **tanpa membekukan antarmuka browser**.

```javascript
const ambilData = async () => {
  console.log("Mulai meminta data...");
  const respon = await tugasBerat(); // Menunggu tanpa membekukan browser
  console.log("Hasil didapat:", respon);
};
```

Hebat bukan? Kodenya tampak lurus dan mudah dibaca seperti kode biasa, tetapi berjalan dengan kecanggihan sistem asinkron di baliknya!

Di bagian akhir (**Bagian 5: Proyek & Evaluasi**), kita akan menggabungkan semua materi yang sudah kita pelajari untuk membangun satu aplikasi to-do list interaktif yang utuh!
