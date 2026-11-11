---
id: c-lanjutan-6
courseId: c-fund-01
order: 22
title: "Dosa Fatal Pemrograman C: Memory Leak & Dangling Pointer"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami konsep kebocoran memori (Memory Leak) dan dampaknya pada performa sistem"
  - "Mengetahui fenomena pointer liar/gentayangan (Dangling Pointer)"
  - "Menerapkan praktik pertahanan terbaik (Defensive Programming) pada pointer C"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>
  #include <stdlib.h>

  void contohBocor(void) {
      int *ptr = malloc(1000 * sizeof(int));
      /* Lupa memanggil free(ptr)!
         Alamat ptr lenyap dari stack saat fungsi selesai,
         tetapi 4000 byte di heap tertinggal selamanya (Memory Leak)! */
  }
---
Bahasa C memberikanmu kebebasan total layaknya mengendarai mobil balap F1 tanpa sabuk pengaman dan tanpa pembatas kecepatan. Kecepatan eksekusinya luar biasa, tetapi sedikit kecerobohan bisa berakibat fatal.

Dua dosa paling mematikan dalam manajemen memori di bahasa C adalah: **Memory Leak** dan **Dangling Pointer**.

![Ilustrasi Bahaya Memory Leak dan Dangling Pointer](asset:images/c-fund-01/bahaya-memori-leak-dangling.png)

## Dosa 1: Kebocoran Memori (*Memory Leak*)

Memory leak terjadi ketika kamu memesan memori di Heap dengan `malloc()`, tetapi **lupa membebaskannya dengan `free()`**, dan kemudian kamu kehilangan pointer yang memegang alamat tersebut.

Perhatikan skenario berikut:
```c
void ambilData(void) {
    int *buffer = malloc(1024 * 1024 * 10); // Memesan 10 MB RAM di Heap!
    // Lakukan sesuatu...
    // LUPA free(buffer)!
} // Fungsi selesai: variabel pointer 'buffer' di Stack lenyap!
```

Apa yang terjadi?
Variabel pointer `buffer` yang tinggal di Stack memang musnah saat fungsi selesai. Namun, **10 MB memori di Heap masih terkunci dan tidak bisa diakses oleh siapa pun** karena alamatnya sudah hilang ditelan bumi!

Jika fungsi `ambilData()` ini dipanggil 100 kali dalam aplikasi server atau game, komputermu akan kehilangan 1.000 MB (1 GB) RAM secara perlahan-lahan sampai akhirnya sistem kehabisan memori dan membeku (*freeze*).

## Dosa 2: Pointer Liar / Gentayangan (*Dangling Pointer*)

Dangling pointer adalah kebalikan dari memory leak:
Kamu sudah membebaskan memori dengan `free()`, tetapi kamu **masih tetap mencoba membaca atau menulis nilai lewat pointer tersebut**!

```c
int *ptr = malloc(sizeof(int));
*ptr = 42;

free(ptr); // Memori sudah resmi dikembalikan ke sistem operasi!

// BAHAYA BESAR (Dangling Pointer):
printf("%d\n", *ptr); // Mencoba membaca rumah yang sudah dirubuhkan!
*ptr = 99;            // Mencoba menulis data ke tanah milik orang lain!
```

Mencoba mengakses pointer yang sudah di-`free` akan memicu **Undefined Behavior** (perilaku tak terduga). Programmu bisa berjalan seolah-olah normal hari ini, tetapi besok tiba-tiba mati mendadak dengan pesan *Segmentation fault*.

## Tiga Jurus Kebal Pertahanan Memori

Untuk menjadi programmer C profesional yang disegani, patuhi 3 aturan pertahanan (*defensive programming*) ini:

1. **Prinsip Berpasangan**: Setiap kali kamu mengetik `malloc()`, saat itu juga langsung cari tempat di mana kamu harus menuliskan `free()` pasangannya!
2. **Segera Nol-kan Setelah `free()`**:
   Selalu beri nilai `NULL` pada pointer tepat setelah dibebaskan:
   ```c
   free(ptr);
   ptr = NULL; // Aman! Pointer tidak lagi menunjuk ke mana-mana
   ```
3. **Cek `NULL` Sebelum Dereferensi**:
   ```c
   if (ptr != NULL) {
       *ptr = 100;
   }
   ```
