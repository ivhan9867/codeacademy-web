---
id: c-inti-6
courseId: c-fund-01
order: 14
title: "Teks di Dunia C: Karakter Array dan Karakter Penutup Null ('\\0')"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami bahwa bahasa C tidak memiliki tipe data string primitif bawaan"
  - "Mengetahui peran krusial karakter null terminator ('\\0') sebagai penanda akhir teks"
  - "Menggunakan fungsi manipulasi string standar dari <string.h> (strlen, strcpy, strcmp)"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>
  #include <string.h>

  int main(void) {
      char nama[] = "Rani";
      printf("Nama: %s (Panjang teks: %zu karakter)\n", nama, strlen(nama));
      return 0;
  }
---
Jika kamu berasal dari bahasa lain, kamu mungkin terbiasa menulis `string nama = "Budi";`.

Di bahasa C, ada satu kejutan besar: **bahasa C murni TIDAK MEMILIKI tipe data bernama `string`**!

Lalu bagaimana komputer memproses kata, kalimat, atau teks di bahasa C? Jawabannya: teks di C adalah **Array dari Karakter Tunggal (`char[]`) yang diakhiri oleh karakter rahasia bernama Null Terminator (`'\0'`)**.

![Anatomi String C dengan Null Terminator](asset:images/c-fund-01/string-null-terminator.png)

## Sang Penjaga Gerbang: Karakter Null (`'\0'`)

Saat kamu menulis:
```c
char kata[] = "KODING";
```

Di dalam memori RAM, array tersebut sebenarnya memesan **7 byte**, bukan 6 byte!
1. Indeks `0`: `'K'`
2. Indeks `1`: `'O'`
3. Indeks `2`: `'D'`
4. Indeks `3`: `'I'`
5. Indeks `4`: `'N'`
6. Indeks `5`: `'G'`
7. Indeks `6`: **`'\0'`** (Karakter bernilai biner 0)

Fungsi seperti `printf("%s", kata)` tidak tahu berapa panjang array tersebut. `printf` hanya berjalan membaca karakter satu per satu mulai dari indeks 0, dan ia akan **terus membaca sampai matanya menabrak karakter `'\0'`**. Begitu menabrak `'\0'`, ia tahu bahwa teks telah selesai.

Jika karakter `'\0'` ini tidak sengaja terhapus, `printf` akan terus membaca data acak di memori RAM hingga menemukan angka nol secara kebetulan, menghasilkan teks kacau yang penuh simbol aneh!

## Fungsi Standar Perpustakaan `<string.h>`

Untuk memanipulasi teks, bahasa C menyediakan berkas header khusus `#include <string.h>`:

```c
#include <stdio.h>
#include <string.h>

int main(void) {
    char kotaAsal[20] = "Bandung";
    char kotaTujuan[20];

    /* 1. Menghitung panjang teks (tidak menghitung '\0') */
    size_t panjang = strlen(kotaAsal);
    printf("Panjang kata '%s': %zu huruf\n", kotaAsal, panjang);

    /* 2. Menyalin teks: strcpy(tujuan, sumber) */
    strcpy(kotaTujuan, "Jakarta");
    printf("Kota tujuan: %s\n", kotaTujuan);

    /* 3. Membandingkan teks: strcmp(teks1, teks2) */
    /* Menghasilkan 0 jika kedua teks persis sama */
    if (strcmp(kotaAsal, kotaTujuan) == 0) {
        printf("Kedua kota sama.\n");
    } else {
        printf("Kedua kota berbeda.\n");
    }

    return 0;
}
```

Output:
```text
Panjang kata 'Bandung': 7 huruf
Kota tujuan: Jakarta
Kedua kota berbeda.
```

**Peringatan Penting:** Di bahasa C, kamu **tidak boleh** membandingkan teks dengan operator sama dengan biasa (`if (teks1 == teks2)`). Kode itu tidak akan membandingkan isi katanya, melainkan membandingkan alamat memorinya! Selalu gunakan fungsi `strcmp()` untuk memeriksa kesamaan teks.
