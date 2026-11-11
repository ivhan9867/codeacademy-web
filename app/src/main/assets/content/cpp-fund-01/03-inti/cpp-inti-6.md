---
id: cpp-inti-6
courseId: cpp-fund-01
order: 14
title: "Perulangan Bersih: Range-Based For Loop di C++11/17"
readingMinutes: 6
section: INTI
learningObjectives:
  - Membandingkan loop indeks klasik bahasa C dengan Range-Based For Loop standar C++11/17
  - Menguasai sintaks perulangan berbasis rentang pada array statis dan objek std::vector
  - Memahami perbedaan tiga variasi iterasi: by-value (salin), by-reference (ubah data), dan const-reference (baca cepat aman)
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <vector>

  int main() {
      std::vector<int> angka = {1, 2, 3};
      for (const auto& n : angka) {
          std::cout << n << " ";
      }
      std::cout << "\n";
      return 0;
  }
---

# Perulangan Bersih: Range-Based For Loop di C++11/17

Di bahasa C, menelusuri array selalu membutuhkan variabel indeks manual:
```c
for (size_t i = 0; i < jumlah; i++) {
    printf("%d\n", data[i]);
}
```
Penulisan ini rawan kesalahan *off-by-one* (misalnya salah menulis `<= jumlah` alih-alih `< jumlah`) yang menyebabkan program membaca memori di luar batas.

Mulai standar C++11, C++ memperkenalkan fitur perulangan modern yang sangat bersih bernama **Range-Based For Loop** (dikenal di bahasa lain sebagai perulangan *for-each*).

---

## Sintaks Range-Based For Loop

Sintaksnya mirip seperti membaca kalimat bahasa Inggris:
> *"Untuk setiap **elemen** di dalam **kontainer**, jalankan blok ini"*.

```cpp
#include <iostream>
#include <vector>
#include <string>

int main() {
    std::vector<std::string> bahasa = {"C", "C++", "Python", "Rust"};

    // Sintaks: for (tipeElemen variabel : namaKoleksi)
    for (std::string b : bahasa) {
        std::cout << "Bahasa: " << b << "\n";
    }

    return 0;
}
```

Output:
```text
Bahasa: C
Bahasa: C++
Bahasa: Python
Bahasa: Rust
```

Kompiler C++ secara otomatis mengetahui kapan perulangan harus dimulai dan di mana akhir dari koleksi tersebut tanpa kamu harus mengetikkan indeks `i` sama sekali!

---

## Tiga Variasi Iterasi yang Wajib Dikuasai

Sama seperti aturan parameter fungsi di modul sebelumnya, cara kamu menuliskan variabel penampung di dalam `for (...)` menentukan efisiensi dan perilakunya:

### 1. `for (auto item : koleksi)` (Pass-by-Value)
Menyalin setiap elemen. Cocok hanya untuk tipe primitif kecil (`int`, `char`, `double`). Jangan gunakan ini untuk vector berisi string atau objek besar karena terjadi fotokopi data berulang kali.

### 2. `for (auto& item : koleksi)` (Pass-by-Reference)
Menggunakan referensi langsung. Gunakan variasi ini **jika kamu ingin memodifikasi data di dalam vector secara langsung**:

```cpp
#include <iostream>
#include <vector>

int main() {
    std::vector<int> skor = {10, 20, 30};

    // Kalikan semua skor dengan 2
    for (auto& s : skor) {
        s = s * 2; // Mengubah elemen di dalam vector asli!
    }

    for (int s : skor) {
        std::cout << s << " ";
    }
    std::cout << "\n";

    return 0;
}
```

Output:
```text
20 40 60 
```

### 3. `for (const auto& item : koleksi)` (Paling Sering Digunakan di Industri)
Variasi ini adalah standar emas jika kamu **hanya ingin membaca elemen**:
- `const`: Menjamin elemen tidak akan terubah tanpa sengaja.
- `&`: Menjamin **nol byte objek yang disalin** (sangat cepat!).

Di modul berikutnya, kita akan menyeberangi jembatan penting dari C ke C++: melihat bagaimana `struct` bertransformasi menjadi fondasi kelas OOP!
