---
id: cpp-pend-4
courseId: cpp-fund-01
order: 7
title: "Modernisasi Variabel: Tipe Primitif, std::string, dan auto"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - Memahami keunggulan kelas std::string dibanding array karakter null-terminated char[] di bahasa C
  - Menguasai operasi manipulasi string dasar seperti penggabungan dengan operator plus (+) dan pengecekan panjang (.length())
  - Memanfaatkan kata kunci auto pada standar C++ modern untuk deduksi tipe data secara otomatis dan aman saat kompilasi
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <string>

  int main() {
      std::string salam = "Halo, " + std::string("Dunia C++!");
      auto skor = 100; // Kompiler mendeteksi int
      std::cout << salam << " Nilai: " << skor << "\n";
      return 0;
  }
---

# Modernisasi Variabel: Tipe Primitif, std::string, dan auto

Di bahasa C, mengelola teks adalah salah satu hal yang paling rawan bug memori. Kamu harus menyiapkan array `char teks[50]`, memastikan ada karakter null `\0` di ujungnya, menggunakan fungsi rawan lubang keamanan seperti `strcpy` atau `strcat`, dan selalu waspada terhadap bahaya *buffer overflow*.

C++ mengakhiri mimpi buruk tersebut dengan menghadirkan kelas modern bernama **`std::string`** melalui header `<string>`.

---

## 1. std::string: Teks yang Tumbuh Otomatis

`std::string` mengelola alokasi dan pembebasan memorinya sendiri secara dinamis di balik layar. Kamu tidak perlu lagi menebak berapa ukuran karakter yang harus dialokasikan:

```cpp
#include <iostream>
#include <string>

int main() {
    std::string namaDepan = "Budi";
    std::string namaBelakang = "Santoso";

    // Menggabungkan string semudah operasi matematika biasa
    std::string namaLengkap = namaDepan + " " + namaBelakang;

    std::cout << "Nama: " << namaLengkap << "\n";
    std::cout << "Jumlah karakter: " << namaLengkap.length() << "\n";

    return 0;
}
```

Output:
```text
Nama: Budi Santoso
Jumlah karakter: 12
```

### Keunggulan `std::string`:
- Menggabungkan string cukup dengan tanda tambah (`+`).
- Mengetahui panjang teks cukup memanggil `.length()` atau `.size()`.
- Mengakses karakter tertentu semudah membaca array `namaLengkap[0]`.
- Ketika variabel `std::string` keluar dari cakupan blok fungsinya, memorinya otomatis dibersihkan secara aman!

---

## 2. Deduksi Tipe Otomatis: Kata Kunci auto

Mulai standar C++11 dan dimatangkan pada C++17, kompiler C++ dibekali kecerdasan untuk menebak (*menduksi*) tipe data variabel berdasarkan nilai awalnya menggunakan kata kunci **`auto`**:

```cpp
#include <iostream>
#include <string>

int main() {
    auto angkaBulat = 42;             // Tipe terdeteksi: int
    auto angkaPecahan = 3.14159;       // Tipe terdeteksi: double
    auto huruf = 'C';                 // Tipe terdeteksi: char
    auto teks = std::string("Modern");// Tipe terdeteksi: std::string

    std::cout << "Angka: " << angkaBulat << "\n";
    std::cout << "Pecahan: " << angkaPecahan << "\n";
    std::cout << "Karakter: " << huruf << "\n";
    std::cout << "Pesan: " << teks << "\n";

    return 0;
}
```

Output:
```text
Angka: 42
Pecahan: 3.14159
Karakter: C
Pesan: Modern
```

### Apakah `auto` Memperlambat Program?
**Sama sekali tidak!** Deduksi tipe oleh `auto` dilakukan **pada saat kompilasi (*compile-time*)**, bukan saat program berjalan (*runtime*). Begitu program biner selesai dibuat, variabel `angkaBulat` tetaplah sebuah `int` murni yang secepat kilat.

> **Aturan Penggunaan `auto`:**
> Gunakan `auto` terutama saat tipe datanya sangat panjang (misalnya tipe iterator pustaka STL yang rumit), atau saat tipe datanya sudah sangat jelas terlihat dari sisi kanan tanda sama dengan.

Di modul berikutnya, kita akan mempelajari salah satu fitur paling istimewa yang membedakan C++ dari bahasa C: **Referensi (`&`)**!
