---
id: c-eval-1
courseId: c-fund-01
order: 25
title: "Mini Proyek: Sistem Manajemen Mahasiswa Berbasis Struct & Pointer"
readingMinutes: 8
section: EVALUASI
learningObjectives:
  - "Merancang struct data mahasiswa lengkap dengan alokasi heap dinamis"
  - "Membangun fungsi modular (tambah data, hitung rata-rata nilai, cetak tabel laporan)"
  - "Menelusuri diagram memori sistem dan membersihkan seluruh alokasi dengan free()"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>
  #include <stdlib.h>
  #include <string.h>

  typedef struct {
      int nim;
      char nama[30];
      float ipk;
  } Mahasiswa;

  void tampilkanMahasiswa(const Mahasiswa *m) {
      printf("| %-5d | %-12s | %-4.2f |\n", m->nim, m->nama, m->ipk);
  }
---

# Mini Proyek: Sistem Manajemen Mahasiswa

Selamat! Kamu telah menempuh perjalanan dari variabel dasar, alur kontrol, array, string, pointer, hingga alokasi memori dinamis di heap. Di modul evaluasi pertama ini, kita akan menyatukan semua potongan puzzle tersebut menjadi sebuah program utuh: **Sistem Manajemen Data Mahasiswa**.

Karena di kelas ini kita menggunakan metode **Baca & Tebak Output**, kita akan membedah arsitektur program ini lapis demi lapis, memetakan kondisi memorinya di RAM, lalu membaca alur eksekusinya sampai tuntas.

![Arsitektur Mini Proyek Manajemen Mahasiswa](asset:images/c-fund-01/mini-proyek-arsitektur.png)

---

## 1. Spesifikasi Kebutuhan Program

Program manajemen ini dirancang dengan prinsip modularitas C:
1. **Model Data (`struct Mahasiswa`)**: Menyimpan NIM (integer), Nama (string array `char[30]`), dan IPK (float).
2. **Kapasitas Fleksibel di Heap**: Daftar mahasiswa disimpan dalam array dinamis di heap menggunakan `malloc()`. Jika kita ingin mencatat 3 mahasiswa, kita memesan ruang persis untuk 3 struct `Mahasiswa`.
3. **Fungsi Cetak dengan Pointer `const`**: Menampilkan daftar tabel tanpa menyalin seluruh struct (efisien secara memori) dan terlindungi dari modifikasi tidak sengaja.
4. **Fungsi Analisis Statistik**: Menghitung rata-rata IPK angkatan dengan mengiterasi array pointer.
5. **Sanitasi Memori**: Memastikan seluruh memori yang dipesan di heap dibebaskan dengan `free()` sebelum program berakhir.

---

## 2. Kode Lengkap Program

Perhatikan implementasi program di bawah ini. Telusuri bagaimana struct dideklarasikan, bagaimana memori dipesan, serta bagaimana operator panah (`->`) digunakan saat mengakses data lewat pointer.

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// 1. Definisi model data mahasiswa
typedef struct {
    int nim;
    char nama[30];
    float ipk;
} Mahasiswa;

// 2. Fungsi mencetak satu baris tabel (menggunakan pointer konstanta)
void cetakBaris(const Mahasiswa *m) {
    printf("| %-5d | %-12s | %-4.2f |\n", m->nim, m->nama, m->ipk);
}

// 3. Fungsi menghitung rata-rata IPK seluruh mahasiswa
float hitungRataRata(const Mahasiswa *daftar, int total) {
    if (total <= 0) return 0.0f;
    float totalIpk = 0.0f;
    for (int i = 0; i < total; i++) {
        totalIpk += daftar[i].ipk;
    }
    return totalIpk / (float)total;
}

int main(void) {
    int jumlah = 3;

    printf("=== SISTEM AKADEMIK KAMPUS C ===\n");
    printf("Mengalokasikan memori untuk %d mahasiswa...\n\n", jumlah);

    // Alokasi memori dinamis di heap
    Mahasiswa *daftar = (Mahasiswa *)malloc((size_t)jumlah * sizeof(Mahasiswa));

    // Validasi alokasi heap
    if (daftar == NULL) {
        printf("Error: Gagal mengalokasikan memori heap!\n");
        return 1;
    }

    // Mengisi data mahasiswa 1
    daftar[0].nim = 101;
    strcpy(daftar[0].nama, "Budi");
    daftar[0].ipk = 3.75f;

    // Mengisi data mahasiswa 2
    daftar[1].nim = 102;
    strcpy(daftar[1].nama, "Siti");
    daftar[1].ipk = 3.90f;

    // Mengisi data mahasiswa 3 (menggunakan pointer arithmetic & arrow)
    Mahasiswa *m3 = daftar + 2;
    m3->nim = 103;
    strcpy(m3->nama, "Reza");
    m3->ipk = 3.45f;

    // Menampilkan laporan dalam bentuk tabel
    printf("+-------+--------------+------+\n");
    printf("| NIM   | Nama         | IPK  |\n");
    printf("+-------+--------------+------+\n");
    for (int i = 0; i < jumlah; i++) {
        cetakBaris(&daftar[i]);
    }
    printf("+-------+--------------+------+\n");

    // Hitung statistik
    float rataRata = hitungRataRata(daftar, jumlah);
    printf("Jumlah Mahasiswa : %d orang\n", jumlah);
    printf("Rata-rata IPK    : %.2f\n", rataRata);

    // Selalu bersihkan memori heap!
    free(daftar);
    daftar = NULL;

    printf("\nStatus: Memori heap berhasil dibersihkan.\n");
    return 0;
}
```

Output:
```
=== SISTEM AKADEMIK KAMPUS C ===
Mengalokasikan memori untuk 3 mahasiswa...

+-------+--------------+------+
| NIM   | Nama         | IPK  |
+-------+--------------+------+
| 101   | Budi         | 3.75 |
| 102   | Siti         | 3.90 |
| 103   | Reza         | 3.45 |
+-------+--------------+------+
Jumlah Mahasiswa : 3 orang
Rata-rata IPK    : 3.70

Status: Memori heap berhasil dibersihkan.
```

---

## 3. Bedah Alur Memori (Mental Execution)

Mari kita bayangkan apa yang terjadi di dalam chip RAM komputer ketika program di atas dieksekusi:

1. **Di Stack Frame `main`**:
   - Dibuat variabel lokal `int jumlah = 3`.
   - Dibuat variabel pointer `Mahasiswa *daftar`.
2. **Panggilan `malloc(3 * sizeof(Mahasiswa))`**:
   - Komputer mengukur ukuran satu struct `Mahasiswa`. Ukurannya sekitar `4 byte (nim) + 30 byte (nama) + padding + 4 byte (ipk)` = ±40 byte.
   - Heap memesan blok kontigu sebesar ±120 byte. Alamat awal blok ini (misalnya `0x7000`) diserahkan ke pointer `daftar` di stack.
3. **Pengisian Data Mahasiswa 3 (`daftar + 2`)**:
   - `daftar + 2` melompat sejauh 2 ukuran struct ke alamat `0x7000 + 2 * 40 = 0x7050`.
   - `m3->nim = 103;` secara akurat mengubah memori di alamat tersebut.
4. **Panggilan Fungsi `cetakBaris(&daftar[i])`**:
   - Alih-alih menyalin struct sebesar 40 byte, kita hanya melempar pointer alamat memori sebesar 8 byte. Ini membuat eksekusi program sangat cepat dan hemat daya!
5. **Perhitungan Rata-Rata**:
   - $(3.75 + 3.90 + 3.45) / 3 = 11.10 / 3 = 3.70$.
6. **Pelepasan Memori `free(daftar)`**:
   - Blok 120 byte di heap dikembalikan ke sistem operasi. Variabel `daftar` diatur ke `NULL` agar tidak menjadi dangling pointer.

---

## 4. Coba di Komputermu (Opsional)

Jika kamu memiliki laptop dengan GCC atau menggunakan compiler online di browsermu:

1. Simpan kode di atas sebagai `mahasiswa.c`.
2. Kompilasi dan jalankan:
   ```bash
   gcc mahasiswa.c -o mahasiswa
   ./mahasiswa
   ```
3. Kamu akan melihat tabel tercetak rapi di terminal dengan waktu eksekusi instan (kurang dari 1 milidetik). Inilah kecepatan murni bahasa C!
