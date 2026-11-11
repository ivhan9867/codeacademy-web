---
id: cpp-inti-7
courseId: cpp-fund-01
order: 15
title: "Jembatan dari C ke C++: Perbedaan struct C++ dan class"
readingMinutes: 6
section: INTI
learningObjectives:
  - Mengetahui evolusi struct di C++ yang mampu memiliki fungsi anggota (member function/method) sendiri
  - Memahami satu-satunya perbedaan teknis antara struct dan class dalam bahasa C++
  - Menguasai konvensi industri: kapan menggunakan struct (data pasif) dan kapan menggunakan class (objek aktif berenkapsulasi)
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  struct Titik {
      int x;
      int y;
      void tampilkan() {
          std::cout << "(" << x << ", " << y << ")\n";
      }
  };

  int main() {
      Titik t{3, 4};
      t.tampilkan();
      return 0;
  }
---

# Jembatan dari C ke C++: Perbedaan struct C++ dan class

Di bahasa C, sebuah `struct` hanyalah kumpulan variabel pasif (*Plain Old Data*). Kamu tidak bisa memasukkan fungsi ke dalam `struct` di C tanpa melibatkan trik pointer fungsi yang rumit.

C++ mengubah aturan main secara radikal: **di C++, sebuah `struct` dapat memiliki variabel DAN fungsi di dalamnya!**

Perhatikan kode berikut:

```cpp
#include <iostream>
#include <string>

struct Pemain {
    std::string nama;
    int skor;

    // Fungsi anggota (Member Function / Method) langsung di dalam struct!
    void tambahSkor(int poin) {
        skor += poin;
    }

    void cetakInfo() {
        std::cout << "Pemain: " << nama << " | Skor: " << skor << "\n";
    }
};

int main() {
    Pemain p1{"Andi", 50};
    p1.tambahSkor(25);
    p1.cetakInfo();

    return 0;
}
```

Output:
```text
Pemain: Andi | Skor: 75
```

Hebat sekali, bukan? Data dan tindakan yang mengelolanya kini disatukan di bawah satu atap yang sama!

---

## Jadi, Apa Perbedaan `struct` dan `class` di C++?

Banyak pemula mengira bahwa `class` memiliki fitur yang jauh lebih banyak daripada `struct`. Faktanya sangat mengejutkan:

> **Di C++, `struct` dan `class` hampir 100% IDENTIK!**
> Keduanya bisa memiliki data, fungsi anggota, konstruktor, bahkan pewarisan (*inheritance*).

Lalu apa **satu-satunya perbedaan teknis** di antara keduanya?
Jawabannya adalah **Hak Akses Bawaan (*Default Access Specifier*)**:

1. **Pada `struct`**: Semua variabel dan fungsi secara bawaan berstatus **`public`** (bebas diakses dan diubah dari luar).
2. **Pada `class`**: Semua variabel dan fungsi secara bawaan berstatus **`private`** (terkunci rapat, tidak bisa diakses langsung dari luar).

Perhatikan perbandingan ini:

```cpp
struct RekeningStruct {
    double saldo; // Otomatis PUBLIC! Bebas diakses siapa saja
};

class RekeningClass {
    double saldo; // Otomatis PRIVATE! Terkunci dari akses luar
};

int main() {
    RekeningStruct r1;
    r1.saldo = 1000000; // Berhasil dikompilasi!

    RekeningClass r2;
    // r2.saldo = 1000000; // ERROR KOMPILASI! 'saldo' is private within this context
}
```

---

## Konvensi Standar Industri

Meskipun secara teknis kamu bisa menggunakan `struct` untuk membuat apa pun yang bisa dibuat oleh `class`, komunitas programmer C++ menyepakati konvensi berikut:

- Gunakan **`struct`**: Jika tipe datamu hanyalah sekumpulan data publik yang sederhana tanpa aturan bisnis ketat (misalnya koordinat `Point { int x, y; }`, warna RGB, atau konfigurasi ukuran).
- Gunakan **`class`**: Jika tipe datamu memiliki aturan logika internal, data sensitif yang harus dilindungi, atau menggunakan pilar OOP lanjutan (enkapsulasi, pewarisan, dan polimorfisme).

Di modul berikutnya, kita akan mendalami bagaimana hak akses `public` dan `private` membentuk benteng pertahanan data yang disebut **Enkapsulasi**, lengkap dengan latihan pertama "Tebak Outputnya"!
