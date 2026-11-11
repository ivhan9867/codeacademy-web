---
id: cpp-lanjutan-7
courseId: cpp-fund-01
order: 23
title: "Memberi Makna Operator: Operator Overloading Dasar"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami konsep Operator Overloading untuk memperlakukan objek kustom sealami tipe data primitif
  - Menguasai pembuatan operator penjumlahan (operator+) pada kelas kustom
  - Menguasai overloading operator stream insertion (operator<<) agar objek dapat langsung dicetak dengan std::cout
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  struct Titik {
      int x, y;
      Titik operator+(const Titik& lain) const {
          return {x + lain.x, y + lain.y};
      }
  };

  int main() {
      Titik t1{2, 3};
      Titik t2{4, 5};
      Titik t3 = t1 + t2;
      std::cout << "(" << t3.x << ", " << t3.y << ")\n";
      return 0;
  }
---

# Memberi Makna Operator: Operator Overloading Dasar

Di C++, kamu bisa menjumlahkan dua angka integer dengan tanda tambah: `int total = a + b;`.

Namun, bagaimana jika kamu memiliki dua objek pecahan `Pecahan p1(1, 2);` dan `Pecahan p2(1, 4);`?
Di bahasa lain, kamu mungkin terpaksa menulis `Pecahan p3 = p1.tambah(p2);`.

C++ memberikan kebebasan luar biasa: kamu boleh memberi tahu kompiler bagaimana simbol **`+`**, **`-`**, **`*`**, bahkan **`<<`** harus bekerja saat diterapkan pada objek buatanmu sendiri! Kemampuan ini disebut **Operator Overloading**.

---

## 1. Menjumlahkan Dua Objek dengan `operator+`

Untuk meng-overload operator tambah, kita membuat fungsi anggota dengan nama khusus: **`operator+`**:

```cpp
#include <iostream>

class Vektor2D {
public:
    int x;
    int y;

    Vektor2D(int x = 0, int y = 0) : x(x), y(y) {}

    // Overloading operator '+'
    Vektor2D operator+(const Vektor2D& lain) const {
        return Vektor2D(this->x + lain.x, this->y + lain.y);
    }
};

int main() {
    Vektor2D v1(2, 5);
    Vektor2D v2(3, 1);

    // Sekarang kita bisa menjumlahkan objek sealami matematika biasa!
    Vektor2D v3 = v1 + v2; // Di balik layar: v1.operator+(v2)

    std::cout << "Hasil v3: (" << v3.x << ", " << v3.y << ")\n";
    return 0;
}
```

Output:
```text
Hasil v3: (5, 6)
```

---

## 2. Mencetak Objek Langsung dengan `std::cout <<`

Pernahkah kamu bermimpi bisa menulis `std::cout << objekKu << "\n";` secara langsung tanpa harus mengetik `objekKu.cetak()`?

Kamu bisa mewujudkannya dengan meng-overload operator **`<<`** di luar kelas sebagai fungsi berteman (*friend function*):

```cpp
#include <iostream>
#include <string>

class Mahasiswa {
private:
    std::string nama;
    int nilai;

public:
    Mahasiswa(const std::string& n, int nil) : nama(n), nilai(nil) {}

    // Mengizinkan operator<< mengakses data private untuk keperluan pencetakan
    friend std::ostream& operator<<(std::ostream& os, const Mahasiswa& m) {
        os << "Mahasiswa[Nama: " << m.nama << ", Nilai: " << m.nilai << "]";
        return os; // Kembalikan stream agar chaining '<<' tetap berfungsi
    }
};

int main() {
    Mahasiswa m("Rani", 98);

    // Mencetak objek kustom seindah tipe data bawaan C++!
    std::cout << m << "\n";

    return 0;
}
```

Output:
```text
Mahasiswa[Nama: Rani, Nilai: 98]
```

> **Catatan Bijak Rekayasa:**
> Operator Overloading adalah pisau bermata dua yang sangat tajam. Jangan pernah meng-overload simbol secara membingungkan (misalnya meng-overload `operator+` untuk menghapus data). Gunakan hanya jika maknanya intuitif dan selaras dengan matematika dunia nyata.

Di modul berikutnya, kita akan mempelajari bagaimana menulis satu kode fungsi yang bisa bekerja untuk semua jenis tipe data menggunakan **Template**!
