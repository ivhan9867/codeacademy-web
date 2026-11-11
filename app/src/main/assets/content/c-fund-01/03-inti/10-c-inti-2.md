---
id: c-inti-2
courseId: c-fund-01
order: 10
title: "Manipulasi Nilai: Operator Aritmatika, Logika, & Type Casting"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Menguasai operator aritmatika, perbandingan, dan logika di bahasa C"
  - "Memahami jebakan pembagian bilangan bulat (integer division)"
  - "Menggunakan konversi tipe data eksplisit (Type Casting)"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      int a = 7, b = 2;
      printf("Pembagian bulat: %d\n", a / b);
      printf("Sisa bagi (modulus): %d\n", a % b);
      printf("Hasil desimal (casting): %.1f\n", (float)a / b);
      return 0;
  }
---
Setelah memiliki variabel di memori, kita bisa memanipulasi nilainya menggunakan berbagai macam operator matematika dan logika.

Perhatikan contoh kode berikut:

```c
#include <stdio.h>

int main(void) {
    int totalPemenang = 7;
    int tim = 2;

    int bagianBulat = totalPemenang / tim;
    int sisa = totalPemenang % tim;
    float hasilRill = (float)totalPemenang / tim;

    printf("Bagian per tim: %d\n", bagianBulat);
    printf("Sisa pembagian: %d\n", sisa);
    printf("Hasil presisi desimal: %.2f\n", hasilRill);

    return 0;
}
```

Output:
```text
Bagian per tim: 3
Sisa pembagian: 1
Hasil presisi desimal: 3.50
```

![Jebakan Pembagian Integer dan Solusi Type Casting](asset:images/c-fund-01/pembagian-integer-casting.png)

## 1. Jebakan Klasik: Pembagian Bilangan Bulat (*Integer Division*)

Perhatikan baris ini:
```c
int hasil = 5 / 2; // Hasilnya BUKAN 2.5, melainkan 2!
```

Di bahasa C, jika kedua operan bertipe bilangan bulat (`int`), hasil operasinya **wajib bertipe `int` juga**. Bagian pecahan desimal (`.5`) akan dipotong dan dibuang tanpa ampun (*truncated*), bukan dibulatkan ke atas!

Untuk mendapatkan hasil pecahan `2.5`, minimal salah satu angka harus berupa bilangan desimal:
```c
float hasil = 5.0f / 2; // Menghasilkan 2.5f
```

## 2. Operator Modulus / Sisa Bagi (`%`)

Operator persentase (`%`) digunakan untuk mencari **sisa pembagian bulat**:
- `7 % 2` ➔ menghasilkan `1` (karena 7 dibagi 2 adalah 3, bersisa 1).
- `10 % 5` ➔ menghasilkan `0` (karena 10 habis dibagi 5).

Operator modulus sangat sering dipakai untuk mengecek apakah suatu angka genap atau ganjil: `if (angka % 2 == 0) /* Genap */`.

## 3. Konversi Tipe Eksplisit (*Type Casting*)

Bagaimana jika kedua variabelmu sudah terlanjur bertipe `int` (misal: `int a = 7, b = 2;`), tapi kamu butuh hasil bagi desimalnya?

Gunakan **Type Casting** dengan menuliskan tipe tujuan di dalam kurung sebelum nama variabel:
```c
float desimal = (float)a / b; // a diubah sementara menjadi float (7.0f), sehingga 7.0f / 2 menghasilkan 3.5f
```

## 4. Operator Logika & Hubungan Boolean

Di bahasa C standar lama, tidak ada tipe data `boolean` bawaan:
- Angka `0` dianggap **SALAH / FALSE**.
- Angka **bukan nol** (biasanya `1`) dianggap **BENAR / TRUE**.

Operator Logika Utama:
- `&&` (**AND / Dan**): Bernilai benar hanya jika **KEDUA** kondisi bernilai benar.
- `||` (**OR / Atau**): Bernilai benar jika **SALAH SATU** kondisi bernilai benar.
- `!` (**NOT / Negasi**): Membalik logika (`!1` menghasilkan `0`, `!0` menghasilkan `1`).

Operator Perbandingan:
- `==` (Sama dengan, perhatikan: DUA tanda sama dengan!).
- `!=` (Tidak sama dengan).
- `>`, `<`, `>=`, `<=`.

Hati-hati: jangan pernah tertukar antara tanda pembanding `a == 5` dan tanda penugasan nilai `a = 5`!
