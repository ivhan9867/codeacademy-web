---
id: c-pend-2
courseId: c-fund-01
order: 5
title: "Anatomi Program C Pertama: Header, main(), & return 0"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - "Membedah baris demi baris struktur standar program C"
  - "Memahami peran preprocessor directive #include <stdio.h>"
  - "Mengetahui fungsi int main() sebagai pintu gerbang eksekusi dan makna kode keluar return 0"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      printf("Belajar C bersama CodeAcademy!\n");
      return 0;
  }
---
Setiap program C yang sah memiliki kerangka dasar yang tidak boleh ditinggalkan. Mari kita bedah struktur standar program bahasa C:

```c
#include <stdio.h>

int main(void) {
    printf("Belajar C bersama CodeAcademy!\n");
    return 0;
}
```

Output:
```text
Belajar C bersama CodeAcademy!
```

Mari kita periksa baris demi baris seperti seorang insinyur yang membongkar mesin mobil:

![Anatomi Program Bahasa C](asset:images/c-fund-01/anatomi-program-c.png)

## 1. Baris Pertama: `#include <stdio.h>`

Tanda pagar (`#`) menandakan sebuah **Preprocessor Directive**—perintah yang diproses oleh compiler sebelum proses kompilasi kode dimulai.

- Kata kunci `include` berarti: *"Tolong ambil isi dari berkas perpustakaan luar ini dan tempelkan di bagian atas kodemu!"*
- `<stdio.h>` adalah singkatan dari **Standard Input/Output Header**. Berkas header ini berisi deklarasi fungsi-fungsi standar untuk berinteraksi dengan terminal, seperti fungsi `printf()` (mencetak teks) dan `scanf()` (membaca masukan).
- Jika kamu lupa menulis baris ini tetapi memanggil fungsi `printf()`, compiler akan mengeluarkan peringatan atau error bahwa ia tidak mengenal fungsi tersebut!

## 2. Baris Utama: `int main(void)`

Komputer harus tahu dari mana ia harus mulai menjalankan instruksi kodemu.

- `main()` adalah **fungsi gerbang utama (entry point)**. Apa pun nama variabel atau fungsi lain yang kamu buat, sistem operasi akan selalu memulai eksekusi program dari baris pertama di dalam fungsi `main()`.
- Kata `int` di depan `main` menyatakan bahwa saat fungsi ini selesai bertugas, ia akan mengembalikan sebuah nilai bilangan bulat (*integer*) kepada sistem operasi.
- Kata `void` di dalam tanda kurung menegaskan bahwa fungsi `main()` ini tidak menerima parameter masukan apa pun dari command line saat dipanggil.

## 3. Kurung Kurawal Pembuka dan Penutup: `{ ... }`

Blok kurung kurawal menandai wilayah tubuh fungsi (*body of function*). Semua baris instruksi logika yang ingin kamu jalankan diletakkan di dalam kurung kurawal ini.

## 4. Perintah Inti: `printf("...\n");`

`printf` adalah fungsi perpustakaan standar untuk mencetak data berformat ke layar terminal. Karakter `\n` adalah karakter baris baru (*newline*) yang memindahkan kursor cetak ke baris berikutnya.

Dan jangan pernah lupakan: **setiap pernyataan instruksi di bahasa C wajib diakhiri dengan tanda titik koma (`;`)**!

## 5. Baris Penutup: `return 0;`

Ketika program berhasil menyelesaikan semua instruksinya tanpa masalah, ia mengirimkan angka `0` kembali ke sistem operasi (Linux, Windows, atau macOS).

Dalam standar konvensi sistem operasi:
- Angka `0` berarti: *"Program berjalan mulus, sukses, dan selesai tanpa error!"*
- Angka bukan nol (seperti `1`, `-1`) biasanya digunakan untuk menandakan bahwa program berhenti karena ada kegagalan atau galat tertentu.
