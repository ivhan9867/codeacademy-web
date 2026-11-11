---
id: c-inti-1
courseId: c-fund-01
order: 9
title: "Wadah Bit di Memori: Tipe Data Primitif & Variabel"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami tipe data primitif C: char, int, float, dan double"
  - "Mengetahui ukuran byte masing-masing tipe data menggunakan operator sizeof"
  - "Memahami bahaya deklarasi variabel tanpa inisialisasi (garbage value)"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      printf("Ukuran int: %zu byte\n", sizeof(int));
      printf("Ukuran float: %zu byte\n", sizeof(float));
      printf("Ukuran double: %zu byte\n", sizeof(double));
      printf("Ukuran char: %zu byte\n", sizeof(char));
      return 0;
  }
---
Di bahasa tingkat tinggi seperti Python atau JavaScript, kamu bisa membuat variabel tanpa memedulikan berapa banyak bit RAM yang dialokasikan di belakang layar.

Di bahasa C, setiap kali kamu mendeklarasikan variabel, kamu sedang meminta kartu izin resmi kepada sistem operasi:
> *"Tolong siapkan kotak memori sebesar X byte di RAM untuk menampung data jenis ini!"*

Perhatikan kode untuk memeriksa ukuran fisik tipe data di arsitektur komputermu:

```c
#include <stdio.h>

int main(void) {
    int bilangan = 100;
    float pecahan = 3.14f;
    double pecahanPresisi = 2.718281828;
    char huruf = 'Z';

    printf("int: %d (Ukuran: %zu byte)\n", bilangan, sizeof(bilangan));
    printf("float: %.2f (Ukuran: %zu byte)\n", pecahan, sizeof(pecahan));
    printf("double: %.9lf (Ukuran: %zu byte)\n", pecahanPresisi, sizeof(pecahanPresisi));
    printf("char: %c (Ukuran: %zu byte)\n", huruf, sizeof(huruf));

    return 0;
}
```

Output:
```text
int: 100 (Ukuran: 4 byte)
float: 3.14 (Ukuran: 4 byte)
double: 2.718281828 (Ukuran: 8 byte)
char: Z (Ukuran: 1 byte)
```

![Ukuran Byte Tipe Data Primitif di Memori RAM](asset:images/c-fund-01/tipe-data-memori-byte.png)

## Empat Tipe Data Primitif Utama

1. **`char` (1 Byte = 8 Bit)**:
   - Digunakan untuk menampung satu karakter tunggal (`'A'`, `'#'`, `'9'`).
   - Di dalam memori, komputer menyimpannya sebagai angka kode ASCII (misal huruf `'A'` bernilai angka `65`).
   - Rentang nilai angka: `-128` hingga `127` (atau `0` hingga `255` jika menggunakan `unsigned char`).

2. **`int` (Standar 4 Byte = 32 Bit di arsitektur 64-bit modern)**:
   - Menampung bilangan bulat tanpa desimal.
   - Rentang nilai: sekitar `-2.147.483.648` hingga `+2.147.483.647` (lebih dari 2 miliar).

3. **`float` (4 Byte = 32 Bit)**:
   - Menampung bilangan desimal pecahan tunggal (*Single Precision*).
   - Akurat hingga sekitar 6 - 7 digit angka signifikan. Selalu akhiri literal angka dengan huruf `f` (contoh: `3.14f`).

4. **`double` (8 Byte = 64 Bit)**:
   - Menampung bilangan desimal pecahan presisi ganda (*Double Precision*).
   - Akurat hingga sekitar 15 - 17 digit angka signifikan. Sangat direkomendasikan untuk komputasi ilmiah dan matematika presisi tinggi.

## Operator `sizeof` dan Format `%zu`

Operator bawaan `sizeof` digunakan untuk mengukur berapa byte memori yang dihabiskan oleh suatu tipe data atau variabel. Karena hasil ukurannya bertipe khusus `size_t`, kita mencetaknya di `printf` menggunakan penentu format **`%zu`**.

## Bahaya Nyata: Nilai Sampah (*Garbage Value*)

Di bahasa C, jika kamu mendeklarasikan variabel tanpa memberinya nilai awal (*inisialisasi*), memori variabel tersebut **TIDAK OTOMATIS BERNILAI 0**!

```c
int poin; // Lupa memberi nilai awal (= 0)
printf("Poin: %d\n", poin);
```

Output yang keluar bisa berupa angka aneh acak seperti `32764` atau `-1073741824`. Itu adalah "nilai sampah" (*garbage value*)—sisa bit data yang pernah ditinggalkan oleh program lain di kotak RAM tersebut sebelumnya.

**Aturan Emas:** Selalu inisialisasi variabelmu sejak pertama kali dibuat (contoh: `int poin = 0;`)!
