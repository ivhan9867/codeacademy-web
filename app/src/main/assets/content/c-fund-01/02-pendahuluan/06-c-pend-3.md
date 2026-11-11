---
id: c-pend-3
courseId: c-fund-01
order: 6
title: "Berkomunikasi dengan Terminal: Mencetak dengan printf()"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - "Menguasai format specifier dasar pada fungsi printf (%d, %f, %c, %s)"
  - "Memahami karakter escape seperti newline (\\n) dan tab (\\t)"
  - "Mencetak angka desimal dengan pembatasan jumlah angka di belakang koma"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      int skor = 95;
      float ipk = 3.85f;
      char grade = 'A';

      printf("Skor: %d | IPK: %.2f | Grade: %c\n", skor, ipk, grade);
      return 0;
  }
---
Fungsi `printf()` berasal dari kata **print formatted** (cetak dengan format khusus). Berbeda dengan fungsi cetak sederhana di bahasa lain yang bisa langsung menggabungkan teks dengan tanda tambah, di bahasa C kita menggunakan sistem **Format Specifier (Penentu Format)**.

Perhatikan contoh kode berikut:

```c
#include <stdio.h>

int main(void) {
    int umur = 20;
    float tinggi = 172.5f;
    char inisial = 'R';

    printf("Nama inisial: %c\n", inisial);
    printf("Umur: %d tahun\n", umur);
    printf("Tinggi badan: %.1f cm\n", tinggi);

    return 0;
}
```

Output:
```text
Nama inisial: R
Umur: 20 tahun
Tinggi badan: 172.5 cm
```

![Tabel Format Specifier pada printf](asset:images/c-fund-01/format-specifier-printf.png)

## Tabel Penentu Format (*Format Specifier*)

Setiap kali compiler C melihat tanda persen (`%`) di dalam teks string `printf`, ia akan menganggapnya sebagai "lubang penampung" (*placeholder*) yang harus diisi oleh nilai variabel yang diletakkan setelah tanda koma:

| Format Specifier | Tipe Data yang Ditampung | Contoh Nilai |
| :---: | :--- | :--- |
| **`%d`** atau **`%i`** | Bilangan bulat (*Integer*) | `42`, `-10`, `0` |
| **`%f`** | Bilangan pecahan (*Float*) | `3.14159f` |
| **`%lf`** | Bilangan pecahan presisi ganda (*Double*) | `123456.789012` |
| **`%c`** | Satu karakter tunggal (*Character*) | `'A'`, `'z'`, `'9'` |
| **`%s`** | Rangkaian teks / string (*String*) | `"CodeAcademy"` |
| **`%%`** | Mencetak tanda persen itu sendiri | `%` |

## Mengendalikan Angka di Belakang Koma

Secara default, `%f` akan mencetak 6 digit angka di belakang koma (misalnya `3.850000`).

Jika kamu hanya ingin menampilkan 2 angka di belakang koma, selipkan titik dan angka sebelum huruf `f`:
- `%.2f` ➔ menghasilkan `3.85`
- `%.1f` ➔ menghasilkan `3.9` (otomatis dibulatkan)
- `%.0f` ➔ membuang seluruh koma pecahan

## Karakter Escape (*Escape Sequences*)

Tanda backslash (`\`) digunakan untuk menyisipkan perintah kontrol yang tidak terlihat:
- `\n` : Pindah baris baru (*Newline*). Tanpa ini, semua cetakan teks berikutnya akan menempel menyambung ke samping kanan.
- `\t` : Lompat sejauh satu tab spasi (biasanya 4 atau 8 spasi), sangat berguna untuk membuat tabel data rapi.
- `\"` : Mencetak tanda kutip ganda di dalam teks.
- `\\` : Mencetak satu karakter backslash.

Mari kita lihat contoh pemformatan tab dan baris baru:

```c
#include <stdio.h>

int main(void) {
    printf("Item\tJumlah\tHarga\n");
    printf("Buku\t2\tRp 50.000\n");
    printf("Pena\t5\tRp 15.000\n");
    return 0;
}
```

Output:
```text
Item	Jumlah	Harga
Buku	2	Rp 50.000
Pena	5	Rp 15.000
```
