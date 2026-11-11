---
id: c-prep-1
courseId: c-fund-01
order: 1
title: "Selamat Datang di Dunia Bahasa C: Mesin di Balik Perangkat Lunak"
readingMinutes: 4
section: PERSIAPAN
learningObjectives:
  - "Memahami peran bahasa C sebagai fondasi sistem operasi dan bahasa pemrograman modern"
  - "Mengetahui metode belajar khusus kelas ini: Mode Baca, Analisis Alur, dan Tebak Output"
  - "Menyiapkan pola pikir rekayasa perangkat lunak tingkat rendah yang menghargai memori dan efisiensi"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      printf("Selamat datang di dunia Bahasa C!\n");
      return 0;
  }
---
Selamat datang di kelas **C Fundamentals: Sintaks & Memori**!

Jika kamu pernah menggunakan sistem operasi Windows, Linux, atau macOS, berselancar dengan browser Chrome, atau menggunakan database raksasa seperti PostgreSQL dan MySQL, kamu sebenarnya sedang menikmati hasil karya yang dibangun di atas bahasa C.

Diciptakan oleh Dennis Ritchie di Bell Labs pada tahun 1972, bahasa C sering dijuluki sebagai **"Bahasa Latin di Dunia Pemrograman"**. Memahami bahasa C akan mengubah cara pandangmu terhadap komputer: kamu tidak lagi sekadar menulis kode di atas awan, melainkan memahami bagaimana setiap baris instruksi diterjemahkan menjadi lompatan bit di dalam keping RAM fisik.

![Posisi Bahasa C Antara Perangkat Keras dan Bahasa Tingkat Tinggi](asset:images/c-fund-01/arsitektur-bahasa-c.png)

## Metode Belajar: Mode Baca & Tebak Output

Perlu diketahui bahwa aplikasi CodeAcademy saat ini **tidak menyertakan compiler C di dalam aplikasi**. Berbeda dengan kelas web atau Python yang memiliki live playground, kelas Bahasa C ini dirancang khusus dalam format **Membaca Mendalam & Menalar Output (Mental Execution)**:

1. **Kode Valid & Bersih**: Setiap contoh kode ditulis sesuai standar resmi **C11**, lengkap dengan header dan fungsi `main()`.
2. **Kotak Output Pasti**: Tepat di bawah setiap blok kode, kamu akan menemukan kotak teks **Output:** yang mencerminkan hasil pasti jika kode tersebut dikompilasi dan dijalankan.
3. **Latihan "Tebak Outputnya"**: Di modul-modul inti dan lanjutan, kamu akan disajikan potongan program untuk kamu tebak hasilnya terlebih dahulu secara mandiri sebelum membuka kunci pembahasan di bawah garis pemisah.

Mari kita lihat contoh kode paling mendasar di bahasa C:

```c
#include <stdio.h>

int main(void) {
    printf("Halo, calon insinyur sistem!\n");
    return 0;
}
```

Output:
```text
Halo, calon insinyur sistem!
```

## Mengapa Belajar C Tanpa Menekan Tombol Run Justru Lebih Kuat?

Banyak programmer pemula terbiasa dengan metode *trial and error*: mengubah kode secara acak, menekan tombol Run, lalu berharap kodenya berhasil. 

Di dunia bahasa C, kebiasaan itu berbahaya karena kesalahan memori kecil bisa menyebabkan kebocoran memori (*memory leak*) atau program mati mendadak (*crash/segmentation fault*). 

Dengan melatih kemampuan membaca alur program di kepala (*tracing*), kamu akan membangun intuisi yang sangat tajam mengenai alur variabel dan alamat pointer.

Mari kita lanjutkan ke modul berikutnya untuk melihat peta materi lengkap yang akan kita taklukkan bersama!
