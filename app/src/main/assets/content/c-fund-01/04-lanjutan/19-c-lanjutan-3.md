---
id: c-lanjutan-3
courseId: c-fund-01
order: 19
title: "Mengubah Nilai Asli: Pass-by-Reference Menggunakan Pointer"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami keterbatasan pass-by-value saat fungsi ingin mengubah data di luar dirinya"
  - "Mengimplementasikan teknik pass-by-reference menggunakan pointer"
  - "Membedah kode klasik fungsi penukar nilai dua variabel (swap)"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  void tukar(int *px, int *py) {
      int temp = *px;
      *px = *py;
      *py = temp;
  }

  int main(void) {
      int a = 10, b = 20;
      tukar(&a, &b);
      printf("a = %d, b = %d\n", a, b);
      return 0;
  }
---
Di modul fungsi sebelumnya, kita sudah tahu bahwa secara bawaan bahasa C menganut paham **Pass-by-Value (Kirim Salinan)**:
Ketika kamu mengirim variabel ke dalam fungsi, fungsi tersebut hanya menerima lembar fotokopi dari nilai variabel. Apa pun coretan yang dibuat fungsi pada lembar fotokopi itu, dokumen aslinya di fungsi pemanggil tetap bersih dan tidak tersentuh.

Lalu bagaimana jika kita benar-benar **ingin** fungsi tersebut memodifikasi variabel aslinya?

Jawabannya adalah: **Kirim Alamat Memorinya (Pass-by-Reference)**!

![Perbandingan Pass-by-Value Salinan vs Pass-by-Reference Alamat Memori](asset:images/c-fund-01/pass-by-reference-swap.png)

## Kasus Klasik: Menukar Isi Dua Cangkir (*The Swap Function*)

Bayangkan kamu memiliki Cangkir A berisi Kopi (10) dan Cangkir B berisi Teh (20). Kamu ingin menukar isinya agar A berisi Teh dan B berisi Kopi.

### Percobaan Gagal (Pass-by-Value):
```c
void tukarGagal(int x, int y) {
    int temp = x;
    x = y;
    y = temp;
    // x dan y di sini memang tertukar, tapi mereka hanyalah variabel lokal yang langsung lenyap saat fungsi selesai!
}
```
Jika kamu memanggil `tukarGagal(a, b);`, nilai `a` dan `b` di fungsi `main()` sama sekali tidak tertukar!

---

### Solusi Sukses (Pass-by-Reference Menggunakan Pointer):

Kita ubah parameter fungsi agar menerima **pointer (alamat memori)** dengan tanda bintang (`*`), dan saat memanggil fungsi, kita kirimkan **alamat memori** dengan tanda ampersand (`&`):

```c
#include <stdio.h>

/* Fungsi menerima 2 alamat memori integer */
void tukarSukses(int *px, int *py) {
    int temp = *px; // Ambil nilai di alamat px, simpan sementara
    *px = *py;      // Masukkan nilai dari alamat py ke dalam alamat px
    *py = temp;     // Masukkan nilai temp ke dalam alamat py
}

int main(void) {
    int a = 10;
    int b = 20;

    printf("Sebelum tukar: a = %d, b = %d\n", a, b);

    /* Kirimkan ALAMAT MEMORI (&a dan &b) */
    tukarSukses(&a, &b);

    printf("Sesudah tukar: a = %d, b = %d\n", a, b);

    return 0;
}
```

Output:
```text
Sebelum tukar: a = 10, b = 20
Sesudah tukar: a = 20, b = 10
```

## Misteri `scanf()` Akhirnya Terpecahkan!

Sekarang kamu sudah memiliki pengetahuan lengkap untuk menjawab misteri di bab Pendahuluan:
```c
scanf("%d", &angka);
```

Mengapa fungsi `scanf()` membutuhkan tanda `&` di depan nama variabel?
Karena fungsi `scanf()` dirancang dengan teknik **Pass-by-Reference**! `scanf()` butuh alamat memori variabelmu agar ia bisa menuliskan angka yang diketik pengguna langsung ke dalam kotak variabel aslimu di memori RAM.
