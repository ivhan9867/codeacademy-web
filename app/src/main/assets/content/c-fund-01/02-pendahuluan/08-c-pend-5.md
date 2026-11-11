---
id: c-pend-5
courseId: c-fund-01
order: 8
title: "Komentar, Gaya Kode, & Membaca Pesan Error Compiler GCC"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - "Mengetahui cara menulis komentar satu baris dan multi-baris di C"
  - "Memahami pentingnya aturan case-sensitivity dan konvensi penamaan variabel"
  - "Mampu membaca dan menguraikan pesan error kompilasi umum dari compiler GCC"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  /* Komentar multi-baris:
     Program menghitung keliling lingkaran sederhana */
  int main(void) {
      const float PI = 3.14159f; // Komentar satu baris
      float radius = 7.0f;
      float keliling = 2 * PI * radius;

      printf("Keliling: %.2f\n", keliling);
      return 0;
  }
---
Sebelum kita masuk ke materi logika dan perhitungan yang lebih dalam, mari kita pelajari etika penulisan kode serta cara menjadi "detektif" saat compiler menolak menerjemahkan kodemu.

Perhatikan contoh kode berikut:

```c
#include <stdio.h>

/* Komentar multi-baris:
   Fungsi ini menghitung luas persegi panjang */
int main(void) {
    int panjang = 10; // Nilai panjang sisi
    int lebar = 5;     // Nilai lebar sisi
    int luas = panjang * lebar;

    printf("Luas: %d\n", luas);
    return 0;
}
```

Output:
```text
Luas: 50
```

## 1. Komentar di Bahasa C

Komentar adalah catatan yang diabaikan sepenuhnya oleh compiler. Komentar ditulis untuk membantu sesama programmer (atau dirimu sendiri di masa depan) memahami maksud dari suatu baris kode:

- **Komentar Satu Baris (`//`)**:
  Semua teks setelah dua garis miring hingga akhir baris dianggap komentar.
- **Komentar Multi-Baris (`/* ... */`)**:
  Semua teks yang berada di antara `/*` pembuka dan `*/` penutup akan diabaikan oleh compiler, bahkan jika membentang puluhan baris.

## 2. Aturan Mutlak: Case-Sensitive

Bahasa C sangat membedakan huruf besar dan huruf kecil (*case-sensitive*):
- Variabel `total`, `Total`, dan `TOTAL` adalah **tiga variabel yang sama sekali berbeda** di memori!
- Kata kunci bahasa C selalu ditulis dengan **huruf kecil**: `int`, `return`, `if`, `while`, `void`. Menulis `Int` atau `Return` akan langsung memicu error kompilasi.

## 3. Menghadapi Pesan Error Compiler GCC

Saat kodemu salah, compiler GCC tidak bermaksud memusuhimu. Ia justru memberikan petunjuk lokasi kerusakan secara sangat presisi.

![Anatomi Pesan Error Compiler GCC](asset:images/c-fund-01/anatomi-error-gcc.png)

Mari kita bedah format pesan error standar GCC:
```text
program.c:7:5: error: expected ';' before 'return'
```

Cara membacanya:
1. `program.c` : Nama file tempat kesalahan ditemukan.
2. `7` : **Nomor baris** kode yang bermasalah.
3. `5` : **Nomor kolom karakter** tempat error terdeteksi.
4. `error: expected ';' before 'return'` : Penjelasan masalahnya! Artinya: *"Saya mengharapkan tanda titik koma (`;`) sebelum kata kunci `return`, tapi kamu lupa menuliskannya di baris sebelumnya!"*

### Tiga Jenis Masalah yang Sering Ditemui Pemula:

1. **`error: expected ';' before ...`**
   Penyebab: Lupa menutup baris instruksi dengan tanda titik koma pada baris sebelumnya.
2. **`error: 'namaVariabel' undeclared`**
   Penyebab: Kamu mencoba menggunakan variabel yang belum pernah kamu deklarasikan tipenya, atau terjadi salah ketik huruf kapital (*typo*).
3. **`warning: implicit declaration of function 'printf'`**
   Penyebab: Lupa menyertakan `#include <stdio.h>` di baris paling atas program.

Selamat! Kamu telah menyelesaikan seluruh fondasi pendahuluan bahasa C. Di bab berikutnya (**Section 3: INTI**), kita akan mulai memanipulasi tipe data, alur logika, perulangan, dan memecahkan tebak output program!
