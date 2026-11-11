---
id: c-prep-2
courseId: c-fund-01
order: 2
title: "Peta Alur Belajar: Dari Sintaks Dasar Menembus Memori Fisik"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Memahami struktur 5 bab utama pembelajaran Bahasa C di CodeAcademy"
  - "Mengetahui tahapan peralihan dari logika alur program menuju manajemen memori manual"
  - "Mengenal konsep pointer dan struct sebelum dibahas secara teknis"
---
Mempelajari bahasa C adalah perjalanan bertahap: kita mulai dari permukaan yang ramah, lalu perlahan menyelam ke jantung mesin komputer.

Kurikulum kelas ini dirancang menjadi 5 bab utama yang saling terhubung secara runtut:

![Peta Alur Materi Pembelajaran Bahasa C](asset:images/c-fund-01/roadmap-c-fundamentals.png)

## Tahapan Pembelajaran

### 1. Persiapan (Modul 1 - 3)
Membangun cara pandang, memahami metode belajar "Baca & Tebak Output", serta melihat cara mudah jika kamu ingin mencoba mengompilasi kode C sendiri di laptop atau komputer rumahmu.

### 2. Dasar-Dasar C (Modul 4 - 8)
Mengenal anatomi struktur program C: fungsi `#include`, fungsi gerbang `main()`, aturan tata bahasa (titik koma, case-sensitivity), cara mencetak teks berformat dengan `printf()`, membaca masukan terminal dengan `scanf()`, dan menguraikan pesan error yang diberikan oleh compiler.

### 3. Alur Program & Fungsi (Modul 9 - 16)
Mulai membangun logika komputasi:
- Tipe data primitif (`int`, `float`, `char`) dan ukuran byt-nya.
- Operator aritmatika, perbandingan, dan pembagian bilangan bulat.
- Percabangan logika (`if-else`, `switch-case`).
- Perulangan (`for`, `while`, `do-while`).
- Penyimpanan data bersebelahan dalam **Array** dan manipulasi teks (*String* sebagai array karakter dengan null terminator `\0`).
- Membagi kode menjadi sub-program mandiri dengan **Fungsi**.

### 4. Pointer, Struct & Memori (Modul 17 - 24)
Inilah puncak keistimewaan bahasa C yang membedakannya dari bahasa lain:
- **Pointer**: Memahami variabel yang menyimpan alamat memori RAM fisik, bukan sekadar nilai.
- **Pass-by-Reference**: Mengirimkan alamat memori agar fungsi luar bisa mengubah nilai variabel asli.
- **Struct**: Membungkus beberapa tipe data berbeda menjadi satu objek utuh.
- **Alokasi Memori Dinamis (Heap)**: Mengendalikan pemesanan memori saat program berjalan menggunakan `malloc()` dan membebaskannya secara bertanggung jawab menggunakan `free()`.
- Menghindari momok terbesar pemula: *Memory Leak*, *Dangling Pointer*, dan *Segmentation Fault*.
- Dasar membaca dan menulis berkas (*File I/O*).

### 5. Proyek & Evaluasi (Modul 25 - 26)
Menelaah rancang bangun program utuh: **Sistem Manajemen Data Mahasiswa** yang menggabungkan struct, pointer, array dinamis, dan kalkulasi statistik secara terstruktur.

Mari kita lanjutkan ke modul berikutnya untuk melihat bagaimana kode C dapat dikompilasi secara mandiri di komputermu!
