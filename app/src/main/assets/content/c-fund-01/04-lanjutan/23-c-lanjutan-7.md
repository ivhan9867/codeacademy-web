---
id: c-lanjutan-7
courseId: c-fund-01
order: 23
title: "Menyimpan Jejak ke Cakram: Pengantar File I/O (fopen, fprintf, fclose)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami bahwa data di RAM akan lenyap saat program ditutup (volatile memory)"
  - "Membuka dan menutup berkas teks menggunakan fopen() dan fclose()"
  - "Menulis data ke dalam berkas dengan fprintf() dan membaca baris dengan fgets()"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      FILE *fp = fopen("catatan.txt", "w");
      if (fp == NULL) return 1;

      fprintf(fp, "Halo dari program C!\n");
      fclose(fp);
      return 0;
  }
---
Semua variabel yang kita buat di Stack maupun Heap memiliki satu kelemahan besar: **sifatnya sementara (*volatile*)**. Begitu program ditutup atau komputermu dimatikan, seluruh data di RAM akan lenyap seketika.

Untuk menyimpan data secara permanen, kita harus menuliskannya ke dalam **Berkas di Cakram Penyimpanan (*File I/O*)**.

![Alur Buka, Tulis/Baca, dan Tutup File I/O di Bahasa C](asset:images/c-fund-01/alur-file-io-c.png)

## Siklus Standar Bekerja dengan Berkas

Di bahasa C, bekerja dengan file selalu melalui 3 langkah mutlak:
1. **Buka Berkas** (`fopen`)
2. **Operasi Baca/Tulis** (`fprintf` / `fgets`)
3. **Tutup Berkas** (`fclose`)

## Tipe Penunjuk Berkas: `FILE *`

Sistem operasi mengelola berkas menggunakan struktur khusus bernama `FILE`. Kita cukup membuat pointer ke struktur tersebut: `FILE *fp;`.

### Mode Pembukaan Berkas (`fopen`):
- `"w"` (**Write / Tulis Baru**): Membuat file baru untuk ditulis. Jika file sudah ada sebelumnya, seluruh isinya akan **dihapus bersih dan ditimpa**!
- `"r"` (**Read / Baca**): Membuka file yang sudah ada untuk dibaca. Jika file tidak ditemukan, ia menghasilkan `NULL`.
- `"a"` (**Append / Tambah di Akhir**): Membuka file untuk menuliskan data tambahan di baris paling akhir tanpa menghapus isi lama.

## Contoh: Menulis dan Membaca Berkas Teks

Perhatikan contoh program utuh berikut:

```c
#include <stdio.h>

int main(void) {
    /* 1. MENULIS KE BERKAS */
    FILE *fpTulis = fopen("skor.txt", "w");
    if (fpTulis == NULL) {
        printf("Gagal membuat berkas!\n");
        return 1;
    }

    fprintf(fpTulis, "Pemain: Rani | Poin: %d\n", 950);
    fprintf(fpTulis, "Pemain: Budi | Poin: %d\n", 820);
    fclose(fpTulis); // Selalu tutup berkas!

    printf("Data berhasil disimpan ke skor.txt.\n\n");

    /* 2. MEMBACA KEMBALI DARI BERKAS */
    FILE *fpBaca = fopen("skor.txt", "r");
    if (fpBaca == NULL) {
        printf("Gagal membuka berkas untuk dibaca!\n");
        return 1;
    }

    char baris[100];
    printf("Isi berkas skor.txt:\n");
    /* Membaca baris demi baris menggunakan fgets */
    while (fgets(baris, sizeof(baris), fpBaca) != NULL) {
        printf("-> %s", baris);
    }

    fclose(fpBaca); // Selalu tutup berkas!
    return 0;
}
```

Output di Terminal:
```text
Data berhasil disimpan ke skor.txt.

Isi berkas skor.txt:
-> Pemain: Rani | Poin: 950
-> Pemain: Budi | Poin: 820
```

## Mengapa Menutup Berkas (`fclose`) Itu Wajib?

Saat kamu menulis dengan `fprintf()`, sistem operasi tidak langsung menulis setiap huruf ke hard disk atau SSD fisik (karena itu sangat lambat). Sistem operasi menampung tulisanmu terlebih dahulu di dalam penyangga memori (*buffer*).

Ketika kamu memanggil **`fclose(fp)`**, sistem operasi akan menyapu seluruh sisa data dari penyangga ke disk fisik (*flush*) lalu melepaskan sumber daya file tersebut. Jika kamu lupa memanggil `fclose()`, sebagian data tulisanmu bisa hilang atau file tersebut terkunci dan tidak bisa dibuka oleh aplikasi lain!
