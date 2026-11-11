---
id: c-prep-3
courseId: c-fund-01
order: 3
title: "Menyiapkan Compiler C di Komputermu (Opsional)"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Memahami perbedaan antara bahasa terkompilasi (Compiled Language) dan terinterpretasi (Interpreted)"
  - "Mengetahui perintah standar kompilasi menggunakan GCC di terminal komputer"
  - "Mengenal alternatif compiler online gratis yang bisa diakses via browser laptop"
codeLanguage: c
codeSnippet: |
  /* Simpan sebagai: salam.c */
  #include <stdio.h>

  int main(void) {
      int tahun = 2026;
      printf("Belajar C di tahun %d!\n", tahun);
      return 0;
  }
---
Sebelum kita mulai membaca baris demi baris sintaks C, ada baiknya kamu memahami bagaimana sebuah kode teks berekstensi `.c` berubah menjadi program yang dapat dieksekusi oleh prosesor komputermu.

Bagian ini bersifat **opsional**. Kamu tetap bisa menyelesaikan kelas ini di dalam aplikasi CodeAcademy tanpa perlu menginstal compiler apa pun. Namun, jika kamu ingin mempraktikkan kode-kode modul ini di komputer pribadi, panduan singkat ini akan sangat membantumu.

![Proses Kompilasi: Source Code ke Preprocessor, Compiler, Linker, lalu Executable](asset:images/c-fund-01/alur-kompilasi-gcc.png)

## Apa itu Kompilasi?

Bahasa seperti Python atau JavaScript adalah bahasa terinterpretasi: kode dibaca baris demi baris oleh interpreter saat program berjalan.

Bahasa C adalah **Compiled Language (Bahasa Terkompilasi)**:
1. **Source Code (`program.c`)**: Teks bahasa manusia yang kamu tulis.
2. **Compiler (misal: GCC atau Clang)**: Menerjemahkan seluruh kode C sekaligus ke dalam kode mesin biner (`0` dan `1`) yang dimengerti langsung oleh CPU.
3. **Executable (`program` atau `program.exe`)**: File aplikasi mandiri berkecepatan tinggi yang bisa langsung dijalankan tanpa membutuhkan C compiler lagi!

## Coba di Komputermu Menggunakan GCC

Jika kamu menggunakan Linux, macOS, atau Windows (via WSL / MinGW), compiler yang paling standar digunakan adalah **GCC (GNU Compiler Collection)**.

Berikut langkah kompilasi di terminal atau command prompt:

### Langkah 1: Tulis Kode
Tulis kode program di text editor favoritmu (misal: VS Code atau Notepad), lalu simpan dengan nama `salam.c`.

```c
#include <stdio.h>

int main(void) {
    int tahun = 2026;
    printf("Belajar C di tahun %d!\n", tahun);
    return 0;
}
```

### Langkah 2: Kompilasi dengan Terminal
Buka terminal di folder tempat file tersebut disimpan, lalu ketik perintah kompilasi:

```bash
gcc salam.c -o salam
```

*Penjelasan:*
- `gcc` adalah nama program compiler.
- `salam.c` adalah nama file kode sumbermu.
- `-o salam` memberitahu compiler untuk menghasilkan file aplikasi keluaran (*output*) bernama `salam` (atau `salam.exe` di Windows).

### Langkah 3: Jalankan Program
Jalankan file aplikasi yang telah tercipta:

```bash
./salam
```

Output di terminal:
```text
Belajar C di tahun 2026!
```

## Alternatif Praktis: Compiler Online

Jika kamu belum memiliki compiler terinstal di komputer, kamu bisa membuka peramban web di laptop dan mencari compiler C online gratis (seperti onlinegdb, replit, atau godbolt). Kamu cukup menempelkan contoh kode dari materi ini dan menekan tombol run di website tersebut.

Sekarang fondasi pemahaman kita sudah siap. Mari melangkah ke **Section 2** untuk membedah anatomi program C pertamamu!
