---
id: c-lanjutan-8
courseId: c-fund-01
order: 24
title: "Latihan Tingkat Lanjut: Tebak Output Pointer & Alokasi Memori"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Melatih kemampuan menalar alur dereferensi pointer ganda dan modifikasi tidak langsung"
  - "Menganalisis manipulasi struct lewat pointer dan operator panah"
  - "Mengasah intuisi deteksi bug memori dan prediksi output program C tingkat lanjut"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  void gandakan(int *p) {
      *p = (*p) * 2;
  }

  int main(void) {
      int angka = 15;
      gandakan(&angka);
      printf("Hasil: %d\n", angka);
      return 0;
  }
---
Saatnya menguji kemampuanmu di tingkat para master bahasa C!

Dua soal di bawah ini akan menguji apakah kamu benar-benar sudah menguasai alur pointer, modifikasi memori lewat fungsi, dan interaksi struct.

Tahan dirimu, jangan mengintip jawaban terlebih dahulu. Telusuri alamat dan isi variabel di kepalamu!

---

## Soal 1: Jalur Estafet Dereferensi Pointer

Perhatikan potongan kode C berikut dengan sangat teliti:

```c
#include <stdio.h>

void misteri(int *p1, int *p2) {
    *p1 = *p1 + 5;
    *p2 = *p2 + *p1;
}

int main(void) {
    int a = 10;
    int b = 20;

    int *ptrA = &a;
    int *ptrB = &b;

    misteri(ptrA, ptrB);

    printf("a = %d, b = %d\n", a, b);
    return 0;
}
```

Berapakah nilai variabel `a` dan `b` yang akan dicetak di terminal?

---

### Jawaban & Pembahasan Soal 1

Output:
```text
a = 15, b = 35
```

**Penjelasan Langkah demi Langkah:**
1. Variabel `a = 10` dan `b = 20`.
2. `ptrA` menyimpan alamat memori `a`, dan `ptrB` menyimpan alamat memori `b`.
3. Fungsi `misteri(ptrA, ptrB)` dipanggil:
   - Parameter `p1` memegang alamat `a`.
   - Parameter `p2` memegang alamat `b`.
4. Baris pertama fungsi: `*p1 = *p1 + 5;`
   - Ambil nilai di alamat `a` (yaitu `10`), tambah 5 menjadi `15`.
   - Tuliskan kembali ke alamat `a`. Nilai `a` sekarang resmi menjadi **`15`**!
5. Baris kedua fungsi: `*p2 = *p2 + *p1;`
   - Ambil nilai di alamat `b` (yaitu `20`).
   - Ambil nilai terbaru di alamat `a` (yaitu `15`).
   - Jumlahkan keduanya: `20 + 15 = 35`.
   - Tuliskan ke alamat `b`. Nilai `b` sekarang resmi menjadi **`35`**!
6. Ketika fungsi selesai, nilai variabel asli di `main()` telah berubah menjadi `a = 15` dan `b = 35`.

---

## Soal 2: Mutasi Anggota Struct Lewat Operator Panah

Perhatikan program yang memanipulasi struct berikut:

```c
#include <stdio.h>

typedef struct {
    int id;
    int poin;
} Pemain;

void beriBonus(Pemain *p, int tambahan) {
    p->poin += tambahan;
    if (p->poin >= 100) {
        p->id = p->id + 1000;
    }
}

int main(void) {
    Pemain p1 = {7, 85};
    Pemain *ptr = &p1;

    beriBonus(ptr, 20);

    printf("ID: %d | Poin: %d\n", p1.id, p1.poin);
    return 0;
}
```

Apakah nilai ID dan Poin pemain `p1` yang akan ditampilkan ke layar?

---

### Jawaban & Pembahasan Soal 2

Output:
```text
ID: 1007 | Poin: 105
```

**Penjelasan Alur:**
1. Awalnya `p1.id = 7` dan `p1.poin = 85`.
2. Pointer `ptr` menunjuk ke alamat `p1`.
3. Fungsi `beriBonus` dipanggil dengan parameter `ptr` dan `tambahan = 20`:
   - `p->poin += 20`: Nilai poin awal `85 + 20` berubah menjadi **`105`**.
   - Pemeriksaan kondisi: Apakah `p->poin >= 100`? Ya (105 >= 100, kondisi bernilai benar!).
   - Blok if dieksekusi: `p->id = p->id + 1000`, sehingga ID awal `7 + 1000` berubah menjadi **`1007`**.
4. Di fungsi `main()`, nilai struct asli `p1` telah termutasi menjadi `ID: 1007` dan `Poin: 105`.

Luar biasa! Kamu telah membuktikan bahwa konsep pointer, struct, dan memori bukanlah misteri lagi. Sekarang mari kita padukan semuanya dalam **Section 5: EVALUASI** untuk merancang Mini Proyek Sistem Manajemen Data Mahasiswa!
