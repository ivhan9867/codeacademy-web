---
id: c-inti-3
courseId: c-fund-01
order: 11
title: "Pengambilan Keputusan: Percabangan if, else if, & switch-case"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Mengendalikan alur logika program menggunakan if, else if, dan else"
  - "Memahami percabangan banyak arah menggunakan switch-case"
  - "Menyadari bahaya fall-through jika lupa menulis pernyataan break pada switch"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      char grade = 'B';
      switch (grade) {
          case 'A': printf("Luar Biasa!\n"); break;
          case 'B': printf("Bagus Sekali!\n"); break;
          case 'C': printf("Cukup.\n"); break;
          default:  printf("Perlu Mengulang.\n"); break;
      }
      return 0;
  }
---
Sebuah program komputer menjadi cerdas ketika ia mampu mengambil keputusan: *"Jika kondisi A terpenuhi lakukan tugas 1, jika tidak lakukan tugas 2."*

Di bahasa C, ada dua struktur percabangan utama: **`if-else`** dan **`switch-case`**.

Perhatikan contoh kode berikut:

```c
#include <stdio.h>

int main(void) {
    int nilai = 82;

    if (nilai >= 85) {
        printf("Predikat: A (Istimewa)\n");
    } else if (nilai >= 70) {
        printf("Predikat: B (Lulus Memuaskan)\n");
    } else if (nilai >= 55) {
        printf("Predikat: C (Lulus Bersyarat)\n");
    } else {
        printf("Predikat: D (Tidak Lulus)\n");
    }

    return 0;
}
```

Output:
```text
Predikat: B (Lulus Memuaskan)
```

![Struktur Percabangan if-else dan switch-case](asset:images/c-fund-01/percabangan-if-switch.png)

## 1. Percabangan `if`, `else if`, `else`

Komputer akan memeriksa kondisi di dalam kurung `()` dari atas ke bawah:
- Begitu ada satu kondisi yang bernilai benar (`1`), blok kode di dalamnya akan langsung dieksekusi, dan sisa blok `else if` di bawahnya akan **diabaikan**.
- Jika tidak ada satu pun kondisi yang terpenuhi, maka blok penampung terakhir **`else`** yang akan dieksekusi.

## 2. Percabangan Nilai Tetap: `switch-case`

Jika kamu ingin mencocokkan satu variabel bilangan bulat atau karakter dengan banyak kemungkinan nilai angka pasti, menggunakan `switch-case` jauh lebih bersih daripada menulis tumpukan `if-else` yang panjang.

Perhatikan contoh menu berikut:

```c
#include <stdio.h>

int main(void) {
    int pilihanMenu = 2;

    switch (pilihanMenu) {
        case 1:
            printf("Menampilkan Profil Pengguna...\n");
            break;
        case 2:
            printf("Membuka Pengaturan Sistem...\n");
            break;
        case 3:
            printf("Keluar dari Aplikasi...\n");
            break;
        default:
            printf("Pilihan menu tidak valid!\n");
            break;
    }

    return 0;
}
```

Output:
```text
Membuka Pengaturan Sistem...
```

## Bahaya Nyata: Efek Melorot (*Fall-Through*) pada switch

Perhatikan kata kunci **`break;`** di akhir setiap `case`.

Di bahasa C, jika kamu lupa menulis `break;`, program tidak akan berhenti saat menemukan kecocokan. Ia akan terus mengeksekusi baris `case` di bawahnya secara membabi buta (*fall-through*) meskipun nilainya tidak cocok!

Mari kita lihat apa yang terjadi jika `break` dihapus:

```c
int level = 1;
switch (level) {
    case 1: printf("Satu\n"); // Lupa break!
    case 2: printf("Dua\n");  // Lupa break!
    default: printf("Selesai\n");
}
```

Output:
```text
Satu
Dua
Selesai
```

Ketiga baris tercetak semua karena program melorot ke bawah tanpa ada `break` yang menahan! Pastikan kamu selalu menulis `break;` di setiap cabang `case` kecuali jika kamu sengaja mendesain alur gabungan.
