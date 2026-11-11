---
id: c-inti-8
courseId: c-fund-01
order: 16
title: "Latihan Uji Logika: Tebak Output Alur Program & Array"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Melatih kemampuan eksekusi mental (mental trace) membaca program C tanpa compiler"
  - "Memahami perilaku operator prefix dan postfix (++i vs i++)"
  - "Menganalisis perulangan bersarang dan mutasi elemen array"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      int a = 5;
      int b = ++a; // Prefix increment
      int c = a++; // Postfix increment
      printf("a=%d, b=%d, c=%d\n", a, b, c);
      return 0;
  }
---
Saatnya menguji ketajaman logikamu! Inilah metode latihan khas kelas Bahasa C di CodeAcademy.

Alih-alih langsung melihat jawaban, **tahan dirimu sejenak**. Ambil secarik kertas atau jalankan alur program di kepalamu baris demi baris, tebak output yang akan keluar, lalu periksa penjelasan di bawah garis pembatas.

---

## Soal 1: Misteri Prefix vs Postfix Increment

Perhatikan kode berikut:

```c
#include <stdio.h>

int main(void) {
    int x = 10;
    int y = ++x; // Prefix: tambah dulu, baru berikan nilainya
    int z = x++; // Postfix: berikan nilainya dulu, baru ditambah

    printf("Nilai x: %d\n", x);
    printf("Nilai y: %d\n", y);
    printf("Nilai z: %d\n", z);

    return 0;
}
```

Berapakah nilai `x`, `y`, dan `z` yang dicetak di terminal?

---

### Jawaban & Pembahasan Soal 1

Output:
```text
Nilai x: 12
Nilai y: 11
Nilai z: 11
```

**Penjelasan Baris Demi Baris:**
1. Awalnya `x = 10`.
2. Pada `int y = ++x;`: Operator **prefix** (`++x`) menaikkan nilai `x` terlebih dahulu menjadi `11`, baru kemudian nilainya diberikan kepada `y`. Sehingga `y = 11` dan `x = 11`.
3. Pada `int z = x++;`: Operator **postfix** (`x++`) memberikan nilai `x` saat ini (`11`) kepada variabel `z` terlebih dahulu, baru setelah ekspresi selesai dievaluasi, nilai `x` dinaikkan menjadi `12`. Sehingga `z = 11` dan `x = 12`.
4. Hasil akhir: `x = 12`, `y = 11`, dan `z = 11`.

---

## Soal 2: Mutasi Elemen Array dan Perulangan Bersyarat

Perhatikan potongan program berikut:

```c
#include <stdio.h>

int main(void) {
    int angka[5] = {2, 4, 6, 8, 10};

    for (int i = 0; i < 5; i++) {
        if (angka[i] % 4 == 0) {
            angka[i] = angka[i] * 2;
        } else {
            angka[i] = angka[i] + 1;
        }
    }

    printf("Hasil: %d, %d, %d, %d, %d\n", 
           angka[0], angka[1], angka[2], angka[3], angka[4]);
    return 0;
}
```

Tebak apa yang akan dicetak oleh fungsi `printf`?

---

### Jawaban & Pembahasan Soal 2

Output:
```text
Hasil: 3, 8, 7, 16, 11
```

**Penjelasan Perulangan:**
- `i = 0`: `angka[0] = 2`. Apakah `2 % 4 == 0`? Tidak. Masuk blok `else`: `2 + 1 = 3`.
- `i = 1`: `angka[1] = 4`. Apakah `4 % 4 == 0`? Ya. Masuk blok `if`: `4 * 2 = 8`.
- `i = 2`: `angka[2] = 6`. Apakah `6 % 4 == 0`? Tidak. Masuk blok `else`: `6 + 1 = 7`.
- `i = 3`: `angka[3] = 8`. Apakah `8 % 4 == 0`? Ya. Masuk blok `if`: `8 * 2 = 16`.
- `i = 4`: `angka[4] = 10`. Apakah `10 % 4 == 0`? Tidak. Masuk blok `else`: `10 + 1 = 11`.

Larik array baru menjadi: `{3, 8, 7, 16, 11}`.

Kemampuan melacak nilai variabel langkah demi langkah seperti ini adalah modal terpentingmu untuk menaklukkan bab paling menantang sekaligus paling mengagumkan di dunia pemrograman: **Pointer dan Manajemen Memori** di Section 4!
