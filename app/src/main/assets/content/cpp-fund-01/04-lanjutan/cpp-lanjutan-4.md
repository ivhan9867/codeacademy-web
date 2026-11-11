---
id: cpp-lanjutan-4
courseId: cpp-fund-01
order: 20
title: "Pohon Silsilah Kode: Pewarisan Tunggal (Inheritance & protected)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami prinsip Pewarisan (Inheritance) sebagai hubungan hierarki 'is-a' (adalah sebuah)
  - Menguasai sintaks pewarisan publik: class KelasAnak : public KelasInduk
  - Menguasai access specifier protected: yang menjembatani keamanan data antara kelas induk dan kelas anak
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  class Hewan {
  public:
      void makan() {
          std::cout << "Sedang makan...\n";
      }
  };

  class Kucing : public Hewan {
  public:
      void meong() {
          std::cout << "Meong!\n";
      }
  };

  int main() {
      Kucing c;
      c.makan(); // Mewarisi metode induk
      c.meong();
      return 0;
  }
---

# Pohon Silsilah Kode: Pewarisan Tunggal (Inheritance & protected)

Bayangkan kamu sedang membuat game petualangan:
Ada karakter **Prajurit**, **Penyihir**, dan **Pemanah**.
Ketiganya sama-sama memiliki nama, darah (*HP*), dan kemampuan berjalan. Apakah kamu harus menyalin kode `nama`, `darah`, dan fungsi `jalan()` ke ketiga kelas tersebut satu per satu? Tentu saja tidak!

Di dunia OOP, kita membuat satu kelas induk umum bernama **Karakter**, lalu kelas Prajurit, Penyihir, dan Pemanah akan **mewarisi (*inherit*)** seluruh sifat tersebut secara otomatis.

![Diagram Pohon Pewarisan OOP](asset:images/cpp-fund-01/diagram-pewarisan-oop.png)

---

## Hubungan "Is-A" (Adalah Sebuah)

Pewarisan digunakan ketika ada hubungan logis **"Kelas Anak adalah sebuah Kelas Induk"**:
- *Kucing adalah seekor Hewan.*
- *Truk adalah sebuah Kendaraan.*
- *Manajer adalah seorang Karyawan.*

Sintaks pewarisan di C++ ditulis setelah tanda titik dua `:` dengan kata kunci `public`:

```cpp
#include <iostream>
#include <string>

// 1. Kelas Induk (Base Class)
class Kendaraan {
protected:
    std::string merek; // Dapat diakses oleh kelas anak, tapi terlindungi dari luar!

public:
    Kendaraan(const std::string& m) : merek(m) {}

    void klakson() const {
        std::cout << merek << ": Tin tiin!\n";
    }
};

// 2. Kelas Anak (Derived Class) mewarisi Kendaraan
class Motor : public Kendaraan {
private:
    bool adaKeranjang;

public:
    // Meneruskan nama merek ke konstruktor kelas induk
    Motor(const std::string& m, bool keranjang)
        : Kendaraan(m), adaKeranjang(keranjang) {}

    void atraksiWheelie() const {
        std::cout << merek << " melakukan atraksi angkat roda depan!\n";
    }
};

int main() {
    Motor m1("Vespa", true);

    // Memanggil metode yang diwarisi dari kelas induk Kendaraan
    m1.klakson();

    // Memanggil metode spesifik milik kelas anak Motor
    m1.atraksiWheelie();

    return 0;
}
```

Output:
```text
Vespa: Tin tiin!
Vespa melakukan atraksi angkat roda depan!
```

---

## Access Specifier Ketiga: `protected:`

Di materi enkapsulasi, kita sudah mengenal `public:` dan `private:`. Sekarang kita berkenalan dengan zona penentu akses ketiga: **`protected:`**:

| Access Specifier | Akses dari Kelas Itu Sendiri | Akses dari Kelas Anak (Turunan) | Akses dari Kode Luar (main) |
|:---|:---:|:---:|:---:|
| **`public`** | Ya | Ya | **Ya** (Bebas) |
| **`protected`** | Ya | **Ya** | **Tidak** (Terkunci) |
| **`private`** | Ya | **Tidak** | **Tidak** (Terkunci) |

Jika variabel `merek` di atas kita beri status `private`, maka kelas anak `Motor` tidak akan bisa membaca atau mencetak nama `merek`. Namun jika kita beri status `public`, orang luar di `main()` bisa mengubah merek sesuka hati.

Status **`protected:`** adalah kompromi yang sempurna: **terbuka untuk anak cucu keluarga, tetapi tertutup rapat bagi orang asing di luar rumah!**

Di modul berikutnya, kita akan mempelajari bagaimana kelas anak dapat mengubah perilaku metode induknya secara dinamis melalui **Polimorfisme dan Fungsi Virtual**!
