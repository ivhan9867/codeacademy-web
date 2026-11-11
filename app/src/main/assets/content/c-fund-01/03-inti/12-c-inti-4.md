---
id: c-inti-4
courseId: c-fund-01
order: 12
title: "Perulangan Tanpa Henti: for, while, dan do-while"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Menguasai perulangan terhitung menggunakan loop for"
  - "Memahami loop berbasis kondisi while dan jaminan minimal satu kali eksekusi pada do-while"
  - "Mengendalikan siklus perulangan menggunakan pernyataan break dan continue"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      for (int i = 1; i <= 5; i++) {
          printf("Iterasi ke-%d\n", i);
      }
      return 0;
  }
---
Komputer diciptakan untuk melakukan pekerjaan repetitif jutaan kali per detik tanpa pernah merasa lelah atau bosan. Di bahasa C, kita mengendalikan perulangan menggunakan tiga konstruksi: **`for`**, **`while`**, dan **`do-while`**.

![Perbandingan Alur Kerja Loop for, while, dan do-while](asset:images/c-fund-01/alur-perulangan-loop.png)

## 1. Perulangan Terhitung: Loop `for`

Loop `for` paling ideal digunakan ketika kamu sudah tahu persis berapa kali sebuah blok kode harus diulang (misalnya mengulang 5 kali atau membaca 100 elemen).

Strukturnya memiliki 3 bagian di dalam kurung:
```c
for (inisialisasi; kondisi_berjalan; perubahan_langkah) { ... }
```

Perhatikan contoh berikut:

```c
#include <stdio.h>

int main(void) {
    for (int i = 1; i <= 3; i++) {
        printf("Hitungan maju: %d\n", i);
    }
    return 0;
}
```

Output:
```text
Hitungan maju: 1
Hitungan maju: 2
Hitungan maju: 3
```

Alur kerja:
1. `int i = 1` : Dijalankan sekali saja di awal.
2. `i <= 3` : Diperiksa sebelum perulangan berjalan. Apakah 1 <= 3? Ya (benar), maka blok di dalam kurawal dieksekusi.
3. `i++` : Dijalankan di akhir setiap putaran (menaikkan nilai `i` menjadi 2).
4. Siklus berulang sampai kondisi bernilai salah (`i` menjadi 4), lalu loop berhenti.

## 2. Perulangan Berbasis Kondisi: Loop `while`

Loop `while` ideal digunakan ketika kamu **tidak tahu pasti berapa kali perulangan harus terjadi**, karena perulangan bergantung pada suatu kondisi yang berubah saat program berjalan (misal: selama baterai masih di atas 10%).

```c
#include <stdio.h>

int main(void) {
    int energi = 3;

    while (energi > 0) {
        printf("Robot berjalan... (Energi: %d)\n", energi);
        energi--; // Kurangi energi agar tidak terjadi infinite loop!
    }

    printf("Energi habis, robot berhenti.\n");
    return 0;
}
```

Output:
```text
Robot berjalan... (Energi: 3)
Robot berjalan... (Energi: 2)
Robot berjalan... (Energi: 1)
Energi habis, robot berhenti.
```

## 3. Jaminan Minimal Satu Kali: Loop `do-while`

Pada `while` biasa, jika kondisi dari awal sudah salah, blok kodenya **tidak akan pernah disentuh sama sekali**.

Sebaliknya, pada `do-while`, blok kode dijamin **pasti dijalankan minimal 1 kali** terlebih dahulu, baru kemudian kondisinya diperiksa di akhir:

```c
#include <stdio.h>

int main(void) {
    int pinSalah = 0;

    do {
        printf("Verifikasi keamanan dijalankan...\n");
    } while (pinSalah > 0);

    return 0;
}
```

Output:
```text
Verifikasi keamanan dijalankan...
```

## Mengendalikan Putaran: `break` dan `continue`

- **`break`**: Memaksa loop untuk **langsung berhenti seketika** dan melompat keluar dari perulangan.
- **`continue`**: Menghentikan putaran saat ini, mengabaikan sisa baris di bawahnya, dan **langsung melompat ke putaran berikutnya**.
