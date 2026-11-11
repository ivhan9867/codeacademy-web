---
id: c-lanjutan-5
courseId: c-fund-01
order: 21
title: "Memori Dinamis: Menguasai malloc(), calloc(), dan free()"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Membedakan karakteristik memori Stack (otomatis/statis) dan memori Heap (manual/dinamis)"
  - "Mengalokasikan memori saat program berjalan menggunakan malloc() dan calloc() dari <stdlib.h>"
  - "Memeriksa pointer NULL untuk mencegah crash dan membebaskan memori dengan free()"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>
  #include <stdlib.h>

  int main(void) {
      int *arr = malloc(3 * sizeof(int));
      if (arr == NULL) return 1;

      arr[0] = 10; arr[1] = 20; arr[2] = 30;
      printf("Elemen ke-2: %d\n", arr[1]);

      free(arr); // Wajib dibebaskan!
      arr = NULL;
      return 0;
  }
---
Selama ini, semua variabel dan array yang kita buat berukuran kaku dan ditentukan sejak sebelum program dijalankan (misal `int angka[100];`). Ukuran ini disimpan di wilayah memori komputer bernama **Stack**.

Namun, bagaimana jika kamu sedang membuat aplikasi kasir atau game di mana kamu tidak tahu berapa banyak data yang akan dimasukkan pengguna? Bisa 5 data, bisa 50.000 data!
- Jika membuat array terlalu kecil, memori kurang dan program *crash*.
- Jika membuat array terlalu besar, kamu memboroskan RAM komputer pengguna.

Solusinya adalah meminta memori secara fleksibel saat program sedang berjalan di wilayah bernama **Heap** menggunakan **Alokasi Memori Dinamis (*Dynamic Memory Allocation*)**!

![Perbedaan Arsitektur Memori Stack vs Heap](asset:images/c-fund-01/stack-vs-heap-memori.png)

## Perbedaan Memori: Stack vs Heap

| Aspek | Memori Stack | Memori Heap |
| :--- | :--- | :--- |
| **Sifat Alokasi** | Otomatis saat fungsi dipanggil | Manual atas permintaan programmer |
| **Ukuran** | Terbatas dan kaku | Sangat luas (seluas sisa RAM komputer) |
| **Pembersihan** | Otomatis dibersihkan saat fungsi selesai | **Wajib dibebaskan manual** oleh programmer |
| **Fungsi Kunci** | Variabel lokal biasa | `malloc()`, `calloc()`, `free()` |

## Fungsi Kunci dari Header `<stdlib.h>`

Untuk menggunakan memori dinamis, kita wajib menyertakan `#include <stdlib.h>`.

### 1. `malloc(jumlah_byte)` — Memory Allocation
Memesan blok memori sebesar total byte yang diminta.
```c
int *arr = (int*) malloc(5 * sizeof(int)); // Memesan 5 * 4 byte = 20 byte di Heap
```

### 2. Pengecekan Wajib: Apakah Memori Berhasil Dialokasikan?
Jika RAM komputer penuh, `malloc` akan mengembalikan pointer bernilai **`NULL`**. Jangan pernah menggunakan pointer sebelum mengecek apakah nilainya `NULL`:
```c
if (arr == NULL) {
    printf("Gagal mengalokasikan memori!\n");
    return 1; // Hentikan program
}
```

### 3. `free(pointer)` — Membebaskan Memori
Ini adalah hukum mutlak di bahasa C: **Semua memori yang kamu pinjam dari Heap dengan `malloc` WAJIB kamu kembalikan ke sistem operasi menggunakan fungsi `free()`!**

Mari kita lihat contoh program utuh:

```c
#include <stdio.h>
#include <stdlib.h>

int main(void) {
    int jumlah = 3;

    /* 1. Alokasikan memori dinamis untuk 3 bilangan integer */
    int *daftarNilai = (int*) malloc(jumlah * sizeof(int));

    /* 2. Verifikasi keselamatan */
    if (daftarNilai == NULL) {
        printf("Gagal alokasi RAM!\n");
        return 1;
    }

    /* 3. Gunakan memori persis seperti array biasa */
    daftarNilai[0] = 85;
    daftarNilai[1] = 92;
    daftarNilai[2] = 78;

    printf("Daftar nilai di Heap:\n");
    for (int i = 0; i < jumlah; i++) {
        printf("Nilai [%d] = %d\n", i, daftarNilai[i]);
    }

    /* 4. Kembalikan memori dan netralkan pointer */
    free(daftarNilai);
    daftarNilai = NULL; // Mencegah pointer liar (dangling pointer)

    printf("Memori berhasil dibebaskan dengan aman.\n");
    return 0;
}
```

Output:
```text
Daftar nilai di Heap:
Nilai [0] = 85
Nilai [1] = 92
Nilai [2] = 78
Memori berhasil dibebaskan dengan aman.
```

## Alternatif: `calloc()`

Jika `malloc` memberikan memori dengan isi acak (*garbage value*), fungsi saudaranya **`calloc(jumlah_elemen, ukuran_elemen)`** akan otomatis mengosongkan seluruh bit memori tersebut menjadi **angka nol (`0`)**:
```c
int *nolSemua = (int*) calloc(5, sizeof(int)); // 5 elemen int, otomatis bernilai 0
```
