---
id: c-lanjutan-1
courseId: c-fund-01
order: 17
title: "Kunci Gerbang Memori: Mengenal Pointer & Operator Alamat"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Memahami konsep alamat memori RAM fisik (heksadesimal)"
  - "Menguasai dua operator kunci: address-of (&) dan dereference (*)"
  - "Memahami cara kerja deklarasi pointer dan memanipulasi nilai variabel dari jarak jauh"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      int angka = 42;
      int *ptr = &angka; // ptr menyimpan alamat memori variabel angka

      printf("Nilai angka: %d\n", angka);
      printf("Alamat angka: %p\n", (void*)&angka);
      printf("Isi ptr: %p\n", (void*)ptr);
      printf("Nilai lewat dereference (*ptr): %d\n", *ptr);
      return 0;
  }
---
Selamat datang di bab paling bergengsi sekaligus paling ditakuti oleh pemula: **Pointer (Penunjuk Memori)**!

Banyak orang menyerah saat mempelajari pointer karena mereka membayangkannya sebagai sesuatu yang gaib. Padahal pointer adalah konsep yang sangat sederhana jika kamu membayangkan dunia nyata.

![Analogi Variabel Sebagai Rumah dan Pointer Sebagai Catatan Alamat](asset:images/c-fund-01/analogi-pointer-memori.png)

## Analogi: Rumah dan Secarik Kertas Berisi Alamat

Bayangkan sebuah kota perumahan (RAM komputermu):
1. **Variabel Biasa (`int angka = 42`)**:
   Ini adalah **sebuah rumah fisik**. Di dalam rumah tersebut, ada penghuni berupa angka `42`. Rumah tersebut berdiri di sebidang tanah tertentu yang memiliki alamat jalan resmi (misalnya: `Jalan Kenanga No. 0x7ffd98`).
2. **Operator Alamat (`&angka`)**:
   Tanda ampersand (`&`) bertugas mencatat dan membaca **nomor alamat jalan** tempat rumah itu berdiri.
3. **Pointer (`int *ptr`)**:
   Pointer adalah **secarik kertas catatan alamat**. Variabel pointer tidak menyimpan angka 42, melainkan menyimpan **alamat rumah** `0x7ffd98`.
4. **Operator Dereferensi (`*ptr`)**:
   Ketika kamu menulis tanda bintang di depan pointer (`*ptr`), kamu menyuruh kurir: *"Tolong datangi alamat yang tertulis di kertas ini, buka pintunya, dan ambil/ubah barang yang ada di dalamnya!"*

## Pembuktian Kode di Memori

Mari kita periksa bagaimana komputer mencatat alamat memori dalam format heksadesimal (`%p`):

```c
#include <stdio.h>

int main(void) {
    int target = 100;
    int *ptrTarget = &target; // ptrTarget menunjuk ke variabel target

    printf("1. Nilai target asli: %d\n", target);
    printf("2. Alamat memori target (&target): %p\n", (void*)&target);
    printf("3. Nilai yang disimpan ptrTarget: %p\n", (void*)ptrTarget);
    printf("4. Mengintip nilai lewat dereferensi (*ptrTarget): %d\n", *ptrTarget);

    /* Mengubah nilai variabel dari jarak jauh lewat pointer! */
    *ptrTarget = 500;

    printf("\n--- Setelah *ptrTarget = 500 ---\n");
    printf("5. Nilai target sekarang menjadi: %d\n", target);

    return 0;
}
```

Output:
```text
1. Nilai target asli: 100
2. Alamat memori target (&target): 0x7ffd3040abec
3. Nilai yang disimpan ptrTarget: 0x7ffd3040abec
4. Mengintip nilai lewat dereferensi (*ptrTarget): 100

--- Setelah *ptrTarget = 500 ---
5. Nilai target sekarang menjadi: 500
```
*(Catatan: Alamat heksadesimal `0x7ffd...` akan berbeda di setiap komputer dan setiap kali program dijalankan, karena sistem operasi mengalokasikan ruang RAM yang dinamis).*

## Dua Wajah Tanda Bintang (`*`) yang Sering Membingungkan

Pemula sering bingung karena tanda bintang `*` dipakai dalam dua konteks berbeda:

1. **Saat Deklarasi Tipe Data**:
   ```c
   int *p;
   ```
   Tanda bintang di sini berarti: *"Variabel `p` ini adalah sebuah **POINTER** yang dipersiapkan untuk menampung alamat memori variabel bertipe int."*

2. **Saat Operasi Eksekusi (Dereferensi)**:
   ```c
   *p = 25;
   ```
   Tanda bintang di sini bukan deklarasi, melainkan kata kerja: *"Datangi alamat yang disimpan di dalam `p`, lalu ubah isinya menjadi 25!"*

Dengan memahami pointer, kamu kini memegang kunci kendali perangkat keras paling murni di dunia komputer!
