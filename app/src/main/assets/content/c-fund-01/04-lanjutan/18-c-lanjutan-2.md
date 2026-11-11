---
id: c-lanjutan-2
courseId: c-fund-01
order: 18
title: "Hubungan Mesra Pointer dan Array"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Menyadari bahwa nama array di bahasa C pada dasarnya adalah pointer ke elemen pertamanya"
  - "Memahami aritmatika pointer (pointer arithmetic) dan bagaimana compiler menghitung lompatan byte"
  - "Membaca elemen array menggunakan notasi pointer *(arr + i)"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      int angka[3] = {10, 20, 30};
      printf("Elemen pertama: %d\n", *angka);
      printf("Elemen kedua: %d\n", *(angka + 1));
      return 0;
  }
---
Di modul array sebelumnya, kita belajar cara mengakses data menggunakan kurung siku seperti `angka[0]` dan `angka[1]`.

Sekarang bersiaplah membuka rahasia terbesar di balik bahasa C:
> **Di balik layar, kurung siku `angka[i]` sebenarnya hanyalah topeng kosmetik (*syntactic sugar*) dari operasi Pointer!**

Perhatikan kode pembuktian berikut:

```c
#include <stdio.h>

int main(void) {
    int data[4] = {100, 200, 300, 400};
    int *ptr = data; // Nama array 'data' otomatis meluruh menjadi pointer ke &data[0]

    printf("Alamat data[0]: %p | Nilai: %d\n", (void*)&data[0], data[0]);
    printf("Alamat data   : %p | Nilai: %d\n", (void*)data, *data);

    printf("\n--- Membaca dengan Aritmatika Pointer ---\n");
    for (int i = 0; i < 4; i++) {
        printf("*(ptr + %d) = %d (Tinggal di alamat: %p)\n", 
               i, *(ptr + i), (void*)(ptr + i));
    }

    return 0;
}
```

Output:
```text
Alamat data[0]: 0x7ffd5040ab10 | Nilai: 100
Alamat data   : 0x7ffd5040ab10 | Nilai: 100

--- Membaca dengan Aritmatika Pointer ---
*(ptr + 0) = 100 (Tinggal di alamat: 0x7ffd5040ab10)
*(ptr + 1) = 200 (Tinggal di alamat: 0x7ffd5040ab14)
*(ptr + 2) = 300 (Tinggal di alamat: 0x7ffd5040ab18)
*(ptr + 3) = 400 (Tinggal di alamat: 0x7ffd5040ab1c)
```

![Aritmatika Pointer dan Lompatan Byte pada Array](asset:images/c-fund-01/pointer-dan-array.png)

## 1. Nama Array Adalah Pointer Konstan

Perhatikan baris pertama output di atas:
`&data[0]` memiliki alamat yang **sama persis** dengan `data`.

Di bahasa C, menuliskan nama array tanpa kurung siku berarti kamu sedang menunjuk ke **alamat memori elemen paling pertamanya (`&data[0]`)**.

## 2. Aritmatika Pointer (*Pointer Arithmetic*)

Perhatikan lonjakan alamat memori pada output:
- Elemen 0: `...ab10`
- Elemen 1: `...ab14` (bertambah 4 byte)
- Elemen 2: `...ab18` (bertambah 4 byte)
- Elemen 3: `...ab1c` (bertambah 4 byte)

Ketika kamu menulis `ptr + 1`, komputer **TIDAK** menambahkan 1 byte pada alamat memori!

Komputer cukup pintar untuk mengetahui bahwa `ptr` menunjuk ke tipe data `int` yang berukuran **4 byte**. Jadi saat kamu menulis `ptr + 1`, ia otomatis melompat sejauh:
$$\text{Alamat Baru} = \text{Alamat Lama} + (1 \times \text{sizeof}(int)) = \text{Alamat Lama} + 4\text{ byte}$$

Jika tipenya adalah `double` (8 byte), maka `ptr + 1` akan otomatis melompat sejauh 8 byte ke depan.

## 3. Rumus Kesetaraan Mutlak

Mulai hari ini, ingat rumus kesetaraan ini selamanya:

$$\text{data}[i] \iff *(\text{data} + i)$$

Bahkan karena sifat komutatif matematika penjumlahan, kode aneh seperti `2[data]` secara teknis sah di C karena diterjemahkan menjadi `*(2 + data)` yang menghasilkan `data[2]`! Tapi jangan tulis seperti itu di proyek nyata ya, agar rekan kerjamu tidak pusing.
