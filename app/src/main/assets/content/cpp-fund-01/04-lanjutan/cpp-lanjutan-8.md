---
id: cpp-lanjutan-8
courseId: cpp-fund-01
order: 24
title: "Kode Serbaguna: Pengantar Function Template & Class Template"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami prinsip Pemrograman Generik (Generic Programming) untuk menghilangkan duplikasi kode berbeda tipe data
  - Menguasai deklarasi Function Template menggunakan sintaks template <typename T>
  - Mengenal konsep Class Template sebagai fondasi di balik wadah STL seperti std::vector<T>
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  template <typename T>
  T nilaiMaksimal(T a, T b) {
      return (a > b) ? a : b;
  }

  int main() {
      std::cout << nilaiMaksimal(10, 25) << "\n";
      std::cout << nilaiMaksimal(5.75, 3.25) << "\n";
      return 0;
  }
---

# Kode Serbaguna: Pengantar Function Template & Class Template

Bayangkan kamu ingin membuat fungsi yang mencari nilai terbesar di antara dua angka.
- Versi integer: `int maks(int a, int b) { return (a > b) ? a : b; }`
- Versi double: `double maks(double a, double b) { return (a > b) ? a : b; }`
- Versi float: `float maks(float a, float b) { return (a > b) ? a : b; }`

Logika kodenya 100% sama persis: periksa `(a > b)`, lalu kembalikan yang lebih besar. Namun hanya karena tipe datanya berbeda, kamu terpaksa menyalin kodenya berkali-kali.

Untuk menghentikan duplikasi ini, C++ menyediakan fitur paling ampuh bernama **Template (Pemrograman Generik)**.

---

## 1. Function Template: Cetakan Fungsi

Dengan template, kamu tidak lagi menuliskan tipe data secara kaku (`int`, `double`). Kamu membuat simbol pengganti tipe umum bernama **`T`** (singkatan dari *Type*):

```cpp
#include <iostream>
#include <string>

// Mendeklarasikan cetakan fungsi generik
template <typename T>
T nilaiTerbesar(T a, T b) {
    return (a > b) ? a : b;
}

int main() {
    // Kompiler otomatis mencetak versi 'int'
    std::cout << "Maks int:    " << nilaiTerbesar(15, 40) << "\n";

    // Kompiler otomatis mencetak versi 'double'
    std::cout << "Maks double: " << nilaiTerbesar(3.14, 2.71) << "\n";

    // Kompiler otomatis mencetak versi 'std::string' (berdasarkan urutan abjad)
    std::string kata1 = "Apel", kata2 = "Jeruk";
    std::cout << "Maks string: " << nilaiTerbesar(kata1, kata2) << "\n";

    return 0;
}
```

Output:
```text
Maks int:    40
Maks double: 3.14
Maks string: Jeruk
```

Kompiler C++ akan memeriksa tipe data argumen yang kamu kirimkan, lalu secara otomatis menghasilkan versi biner fungsi yang tepat pada saat kompilasi!

---

## 2. Class Template: Wadah untuk Segala Tipe

Pernahkah kamu bertanya mengapa saat membuat vector kita menulis `std::vector<int>` atau `std::vector<std::string>`?
Karena `std::vector` adalah sebuah **Class Template**!

Kamu juga bisa membuat kelas generik buatanmu sendiri:

```cpp
#include <iostream>
#include <string>

template <typename T1, typename T2>
class PasanganData {
public:
    T1 pertama;
    T2 kedua;

    PasanganData(T1 a, T2 b) : pertama(a), kedua(b) {}

    void cetak() const {
        std::cout << "[" << pertama << " -> " << kedua << "]\n";
    }
};

int main() {
    // Menyimpan pasangan String dan Integer (misal: Nama dan Skor)
    PasanganData<std::string, int> skorSiswa("Siti", 95);
    skorSiswa.cetak();

    // Menyimpan pasangan Integer dan Double (misal: ID dan Berat)
    PasanganData<int, double> barang(101, 2.45);
    barang.cetak();

    return 0;
}
```

Output:
```text
[Siti -> 95]
[101 -> 2.45]
```

Satu definisi kelas dapat digunakan untuk ribuan kombinasi tipe data yang berbeda tanpa perlu mengetik ulang satu baris pun!

Di modul berikutnya, kita akan mempelajari revolusi manajemen memori modern yang menghapus rasa takut terhadap pointer liar: **Smart Pointer**!
