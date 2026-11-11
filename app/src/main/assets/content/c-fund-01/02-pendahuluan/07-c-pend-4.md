---
id: c-pend-4
courseId: c-fund-01
order: 7
title: "Menerima Masukan Pengguna: Cara Kerja scanf() & Simbol &"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Memahami cara membaca masukan terminal menggunakan fungsi scanf()"
  - "Mengetahui alasan penting penggunaan operator ampersand (&) pada argumen scanf"
  - "Menyadari keterbatasan pembacaan teks spasi pada scanf"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      int angka;
      /* Membaca masukan integer dari keyboard */
      printf("Masukkan sebuah angka: ");
      scanf("%d", &angka);

      printf("Kamu memasukkan angka: %d\n", angka);
      return 0;
  }
---
Jika program hanya bisa mencetak data yang sudah ditentukan sejak awal, program tersebut akan kaku. Kita ingin program bisa meminta masukan (*input*) dari pengguna di terminal.

Di bahasa C, fungsi kembaran dari `printf()` adalah **`scanf()`** (singkatan dari *scan formatted*).

Perhatikan kode simulasi interaksi berikut:

```c
#include <stdio.h>

int main(void) {
    int umur;
    float berat;

    printf("Masukkan umur: ");
    scanf("%d", &umur);

    printf("Masukkan berat badan: ");
    scanf("%f", &berat);

    printf("Data tersimpan: Umur %d tahun, Berat %.1f kg.\n", umur, berat);
    return 0;
}
```

Simulasi Masukan Terminal:
```text
22
65.4
```

Output:
```text
Data tersimpan: Umur 22 tahun, Berat 65.4 kg.
```

![Mengapa scanf Membutuhkan Simbol Ampersand Alamat Memori](asset:images/c-fund-01/cara-kerja-scanf-ampersand.png)

## Mengapa Harus Ada Simbol Ampersand (`&`)?

Ini adalah salah satu pertanyaan paling membingungkan bagi pemula di bahasa C:
> *"Kenapa saat memanggil `printf(\"%d\", umur)` kita tidak pakai tanda `&`, tetapi saat memanggil `scanf(\"%d\", &umur)` kita WAJIB memakai tanda `&`?"*

Jawabannya adalah **Alamat Memori**:

1. Pada `printf("%d", umur)`:
   Fungsi `printf` hanya bertugas **membaca nilai**. Program cukup menyalin isi nilai variabel `umur` (misalnya angka 22) lalu menampilkannya ke layar.

2. Pada `scanf("%d", &umur)`:
   Fungsi `scanf` bertugas **mengubah nilai variabel dari dalam fungsi**. Bahasa C secara default bekerja dengan sistem salinan (*pass-by-value*). Jika kamu hanya mengirimkan `umur`, `scanf` hanya menerima salinan angka dan tidak bisa mengubah variabel asli yang ada di fungsi `main()`.
   
   Oleh karena itu, tanda **`&` (Address-Of Operator)** digunakan untuk mengirimkan **ALAMAT RUMAH (alamat memori fisik)** tempat variabel `umur` tinggal di RAM. Dengan memegang alamat rumahnya, `scanf` bisa mendatangi langsung lokasi memori tersebut dan meletakkan angka yang diketikkan pengguna ke dalamnya!

## Aturan Khusus Karakter Teks (*String*)

Jika variabelmu bertipe array karakter (*string*), kamu **TIDAK PERLU** menambahkan tanda `&`:

```c
char nama[50];
scanf("%s", nama); /* Tanpa tanda &, karena nama array sudah merupakan alamat memori! */
```

*Catatan:* `%s` pada `scanf` akan otomatis berhenti membaca begitu ia menemui spasi. Jika pengguna mengetik `"Rani Pratiwi"`, yang terbaca ke dalam variabel `nama` hanyalah kata `"Rani"`. Kita akan mempelajari teknik membaca kalimat lengkap berspasi saat membahas bab Array Karakter nanti.
