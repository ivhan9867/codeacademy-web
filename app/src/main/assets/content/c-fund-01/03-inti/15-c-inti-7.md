---
id: c-inti-7
courseId: c-fund-01
order: 15
title: "Membagi Beban: Deklarasi, Definisi, & Prototipe Fungsi"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami manfaat modularitas kode menggunakan fungsi mandiri"
  - "Membedakan parameter formal pada fungsi dan argumen saat pemanggilan"
  - "Memahami peran prototipe fungsi (forward declaration) agar compiler mengenali fungsi sebelum main()"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  /* Prototipe Fungsi (Forward Declaration) */
  int hitungLuas(int p, int l);

  int main(void) {
      int hasil = hitungLuas(8, 4);
      printf("Luas: %d\n", hasil);
      return 0;
  }

  /* Definisi Fungsi */
  int hitungLuas(int p, int l) {
      return p * l;
  }
---
Menumpuk semua ratusan baris kode di dalam satu fungsi `main()` raksasa adalah resep bencana: kode menjadi sulit dibaca, sulit diuji, dan mustahil dipakai ulang.

Solusinya adalah **Fungsi (Function)**: blok kode mandiri yang dirancang untuk menyelesaikan satu tugas spesifik.

![Anatomi dan Alur Pemanggilan Fungsi di Bahasa C](asset:images/c-fund-01/anatomi-fungsi-c.png)

## Anatomi Sebuah Fungsi

Sebuah fungsi di C memiliki 4 elemen utama:
1. **Tipe Nilai Kembalian (*Return Type*)**: Tipe data yang akan dihasilkan dan dikembalikan oleh fungsi kepada pemanggilnya (misal `int`, `float`, atau `void` jika fungsi tidak menghasilkan nilai apa pun).
2. **Nama Fungsi**: Nama pengenal (misal `hitungPersegi`).
3. **Daftar Parameter**: Variabel masukan yang dibutuhkan fungsi untuk bekerja.
4. **Tubuh Fungsi (`{ ... }`)**: Kumpulan baris instruksi yang dijalankan dan diakhiri dengan pernyataan `return`.

Perhatikan contoh kode modular berikut:

```c
#include <stdio.h>

/* 1. Prototipe Fungsi */
int hitungMaksimum(int a, int b);
void sapaPengguna(const char nama[]);

int main(void) {
    sapaPengguna("Rani");

    int angkaTerbesar = hitungMaksimum(45, 80);
    printf("Nilai terbesar adalah: %d\n", angkaTerbesar);

    return 0;
}

/* 2. Definisi Fungsi */
int hitungMaksimum(int a, int b) {
    if (a > b) {
        return a;
    } else {
        return b;
    }
}

void sapaPengguna(const char nama[]) {
    printf("Halo %s, selamat belajar fungsi di C!\n", nama);
}
```

Output:
```text
Halo Rani, selamat belajar fungsi di C!
Nilai terbesar adalah: 80
```

## Mengapa Membutuhkan Prototipe Fungsi (*Function Prototype*)?

Compiler C bekerja membaca kode secara berurutan **dari baris paling atas ke paling bawah**.

Jika kamu langsung mendefinisikan fungsi `hitungMaksimum()` di bawah fungsi `main()` tanpa menuliskan prototipe di bagian atas, compiler akan protes:
> *"Hei, di baris main() kamu memanggil fungsi 'hitungMaksimum', tapi saya belum pernah melihat fungsi itu didefinisikan sebelumnya!"*

**Prototipe Fungsi (Forward Declaration)** adalah "pemberitahuan awal" satu baris di bagian atas:
```c
int hitungMaksimum(int a, int b);
```
Baris ini memberitahu compiler: *"Tenang saja, nanti di bawah akan ada fungsi bernama `hitungMaksimum` yang menerima 2 angka integer dan mengembalikan sebuah integer. Tolong izinkan fungsi `main()` memanggilnya terlebih dahulu!"*

## Konsep Pass-by-Value: Salinan Murni

Perlu dicatat bahwa secara bawaan di bahasa C, parameter fungsi menerima **salinan nilai** (*pass-by-value*):
```c
void ubahNilai(int x) {
    x = 99; // Hanya mengubah salinan lokal x di dalam fungsi ini!
}

int main(void) {
    int angka = 10;
    ubahNilai(angka);
    printf("%d\n", angka); // Nilainya TETAP 10, tidak berubah menjadi 99!
    return 0;
}
```

Jika kamu ingin sebuah fungsi bisa mengubah nilai variabel asli di luar dirinya, kamu harus mengirimkan **Pointer (Alamat Memori)** variabel tersebut. Dan itulah yang akan kita taklukkan di bab lanjutan nanti!
