---
id: c-lanjutan-4
courseId: c-fund-01
order: 20
title: "Membungkus Data Majemuk: Mendefinisikan struct & typedef"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami konsep struct untuk menggabungkan berbagai tipe data berbeda menjadi satu objek"
  - "Mengakses anggota struct menggunakan operator titik (.) dan operator panah (->) pada pointer"
  - "Meringkas penamaan tipe struct menggunakan kata kunci typedef"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  typedef struct {
      char nama[30];
      int umur;
      float ipk;
  } Mahasiswa;

  int main(void) {
      Mahasiswa mhs = {"Rani Pratiwi", 20, 3.90f};
      printf("Nama: %s (IPK: %.2f)\n", mhs.nama, mhs.ipk);
      return 0;
  }
---
Array sangat hebat untuk menyimpan sekumpulan data bertipe sama (misal 5 buah angka `int`). Namun di dunia nyata, entitas data sering kali berbentuk **majemuk dengan tipe data yang beragam**:
- Seorang Mahasiswa memiliki **Nama** (`char[]`), **NIM** (`int`), dan **IPK** (`float`).
- Sebuah Titik Koordinat memiliki koordinat **X** (`int`) dan **Y** (`int`).

Di bahasa C, cara menggabungkan berbagai variabel berbeda tipe menjadi satu kesatuan tipe data baru adalah dengan menggunakan **`struct` (Structure)**.

![Anatomi struct di Memori dan Operator Panah](asset:images/c-fund-01/struct-dan-operator-panah.png)

## Mendefinisikan dan Menggunakan `struct`

Mari kita lihat cara membuat tipe data kustom bernama `Mahasiswa`:

```c
#include <stdio.h>
#include <string.h>

/* Mendefinisikan cetak biru (blueprint) struct */
struct Mahasiswa {
    char nama[50];
    int nim;
    float ipk;
};

int main(void) {
    /* Membuat variabel instansiasi dari struct Mahasiswa */
    struct Mahasiswa mhs1;

    /* Mengisi anggota struct menggunakan operator titik (.) */
    strcpy(mhs1.nama, "Rani Pratiwi");
    mhs1.nim = 10126001;
    mhs1.ipk = 3.92f;

    printf("--- Profil Mahasiswa ---\n");
    printf("NIM   : %d\n", mhs1.nim);
    printf("Nama  : %s\n", mhs1.nama);
    printf("IPK   : %.2f\n", mhs1.ipk);

    return 0;
}
```

Output:
```text
--- Profil Mahasiswa ---
NIM   : 10126001
Nama  : Rani Pratiwi
IPK   : 3.92
```

## Mempercantik dengan `typedef`

Menulis kata `struct Mahasiswa` berulang-ulang setiap kali mendeklarasikan variabel cukup panjang. Kita bisa menggunakan kata kunci **`typedef` (Type Definition)** untuk membuat nama alias baru yang singkat:

```c
typedef struct {
    char nama[50];
    int nim;
    float ipk;
} Mahasiswa; // Sekarang 'Mahasiswa' resmi menjadi nama tipe data mandiri!
```

Setelah ini, kamu cukup menulis: `Mahasiswa mhs1;` tanpa perlu menulis kata `struct` lagi!

## Mengakses Lewat Pointer: Operator Panah (`->`)

Jika kamu memegang **pointer** yang menunjuk ke sebuah struct, ada aturan penulisan khusus:

```c
#include <stdio.h>

typedef struct {
    int x;
    int y;
} Titik;

int main(void) {
    Titik titikPusat = {10, 25};
    Titik *ptr = &titikPusat; // Pointer menunjuk ke struct

    /* Cara 1 (Panjang dan berbelit): (*ptr).x */
    printf("X lewat cara 1: %d\n", (*ptr).x);

    /* Cara 2 (Standar industri yang anggun): ptr->x */
    printf("X lewat panah  : %d\n", ptr->x);
    printf("Y lewat panah  : %d\n", ptr->y);

    /* Mengubah nilai anggota lewat pointer */
    ptr->x = 50;
    printf("X setelah diubah: %d\n", titikPusat.x);

    return 0;
}
```

Output:
```text
X lewat cara 1: 10
X lewat panah  : 10
Y lewat panah  : 25
X setelah diubah: 50
```

Operator panah **`->` (Arrow Operator)** adalah singkatan langsung dari "buka dereferensi struct lalu ambil anggotanya":
`ptr->nama` identik dengan `(*ptr).nama`.
