---
id: c-inti-5
courseId: c-fund-01
order: 13
title: "Deretan Loker Bersebelahan: Array Satu Dimensi"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami array sebagai blok memori yang berdampingan (contiguous memory)"
  - "Menguasai indeks berbasis 0 dan cara mendeklarasikan serta menginisialisasi array"
  - "Menyadari bahaya akses di luar batas array (out of bounds) yang tidak dijaga oleh C"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      int nilai[4] = {85, 90, 78, 92};
      for (int i = 0; i < 4; i++) {
          printf("Nilai siswa ke-%d: %d\n", i + 1, nilai[i]);
      }
      return 0;
  }
---
Bayangkan kamu memiliki 50 orang siswa. Tentu sangat melelahkan jika kamu harus membuat 50 nama variabel terpisah: `int nilai1, nilai2, nilai3, ... nilai50`.

Solusinya adalah **Array (Larik)**: kumpulan elemen yang memiliki **tipe data sama**, disimpan di dalam **blok memori yang berdampingan persis (*contiguous block*)**, dan diakses menggunakan satu nama bersama dengan nomor indeks.

![Visualisasi Array Sebagai Deretan Loker Memori Berurutan](asset:images/c-fund-01/anatomi-array-memori.png)

## Analogi: Deretan Loker Penitipan Barang

Bayangkan sebuah lemari loker panjang bernomor urut:
- Seluruh loker memiliki ukuran yang sama persis (misalnya loker `int` masing-masing selebar 4 byte).
- Di dunia pemrograman komputer dan bahasa C, penomoran loker selalu dimulai dari angka **0** (bukan 1!).
- Jika array memiliki 5 elemen, maka indeks yang sah adalah `0`, `1`, `2`, `3`, dan `4`.

Mari kita lihat contoh kode pembuatan dan pembacaan array:

```c
#include <stdio.h>

int main(void) {
    /* Deklarasi dan inisialisasi array berisi 5 angka */
    int skor[5] = {10, 20, 30, 40, 50};

    /* Mengubah isi loker indeks ke-2 (elemen ke-3) */
    skor[2] = 99;

    /* Menghitung total dan mencetak setiap elemen */
    int total = 0;
    for (int i = 0; i < 5; i++) {
        printf("Loker skor[%d] = %d\n", i, skor[i]);
        total += skor[i];
    }

    printf("Total akumulasi nilai: %d\n", total);
    return 0;
}
```

Output:
```text
Loker skor[0] = 10
Loker skor[1] = 20
Loker skor[2] = 99
Loker skor[3] = 40
Loker skor[4] = 50
Total akumulasi nilai: 219
```

## Menghitung Jumlah Elemen Array Secara Otomatis

Di bahasa C, array tidak memiliki properti bawaan seperti `.length` pada JavaScript atau `len()` pada Python.

Namun, kamu bisa menghitung jumlah elemennya menggunakan trik `sizeof`:
```c
int data[] = {12, 45, 67, 89, 23, 56}; // Ukuran otomatis 6 elemen

int jumlahElemen = sizeof(data) / sizeof(data[0]);
/* Total seluruh byte array dibagi ukuran 1 elemen (4 byte) = 24 / 4 = 6 elemen */
```

## Peringatan Bahaya: Batas Array (*Out of Bounds*)

Bahasa C mengutamakan kecepatan eksekusi di atas segalanya, sehingga peramban dan compiler C **TIDAK MEMERIKSA BATAS ARRAY (*no bounds checking*)**.

Jika kamu punya array `int angka[5];` lalu kamu mencoba mengakses `angka[10] = 500;`, compiler tidak akan melarangmu!

Apa yang terjadi? Programmu akan menerobos dan menimpa data di kotak RAM tetangga milik variabel lain. Ini bisa memicu perilaku aneh tak terduga (*undefined behavior*) atau sistem operasi akan langsung mematikan paksa programmu dengan pesan legendaris: **Segmentation fault (core dumped)**!

Selalu jaga agar indeks perulanganmu tidak melebihi batas ukuran array yang sebenarnya.
