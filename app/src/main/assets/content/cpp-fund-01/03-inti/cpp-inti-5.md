---
id: cpp-inti-5
courseId: cpp-fund-01
order: 13
title: "Koleksi Dinamis Modern: Pengantar std::vector (STL Dasar)"
readingMinutes: 6
section: INTI
learningObjectives:
  - Memahami peran std::vector sebagai wadah array dinamis standar di C++ Standard Template Library (STL)
  - Memahami tata letak memori kontigu vector dan bagaimana ukurannya membesar secara otomatis
  - Menguasai manipulasi vector dasar: push_back(), pop_back(), size(), dan perbedaan operator [] vs method .at()
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <vector>

  int main() {
      std::vector<int> angka = {10, 20, 30};
      angka.push_back(40);
      std::cout << "Ukuran: " << angka.size() << ", Elemen terakhir: " << angka.back() << "\n";
      return 0;
  }
---

# Koleksi Dinamis Modern: Pengantar std::vector (STL Dasar)

Di bahasa C, array memiliki ukuran yang kaku: `int data[10];` hanya bisa memuat tepat 10 item. Jika datanya bertambah menjadi 11, program akan rusak. Untuk membuatnya dinamis, kamu harus memanggil `malloc()`, `realloc()`, menghitung ukuran `sizeof(int)`, dan jangan sampai lupa memanggil `free()`.

Pustaka Standar C++ (*Standard Template Library / STL*) menghadirkan wadah dinamis paling populer dan serbaguna di dunia C++: **`std::vector`** melalui header `<vector>`.

![Diagram Memori Dinamis std::vector](asset:images/cpp-fund-01/layout-vector-memori.png)

---

## Apa Itu std::vector?

`std::vector` adalah **array yang ukurannya dapat membesar dan mengecil secara otomatis** sesuai kebutuhan.
- Elemen-elemennya disimpan secara berdampingan rapat di memori heap (*contiguous memory*), sehingga pembacaan datanya secepat array murni.
- Mengelola alokasi dan pembersihan memorinya sendiri secara otomatis berkat prinsip RAII.

Sintaks pembuatan vector menggunakan tanda kurung siku generik `<tipeData>`:

```cpp
#include <iostream>
#include <vector>

int main() {
    // 1. Membuat vector dengan nilai awal
    std::vector<int> nilai = {80, 85, 90};

    // 2. Menambahkan elemen baru ke paling belakang
    nilai.push_back(95);
    nilai.push_back(100);

    std::cout << "Banyak elemen: " << nilai.size() << "\n";
    std::cout << "Elemen pertama: " << nilai[0] << "\n";
    std::cout << "Elemen terakhir: " << nilai.back() << "\n";

    return 0;
}
```

Output:
```text
Banyak elemen: 5
Elemen pertama: 80
Elemen terakhir: 100
```

---

## Akses Elemen: Operator `[]` vs `.at()`

Ada dua cara mengakses elemen pada indeks tertentu:
1. **Operator `nilai[indeks]`**: Sangat cepat karena tidak melakukan pengecekan batas (*bounds check*). Jika kamu mengakses indeks yang melebihi ukuran array, perilakunya berbahaya (*undefined behavior*).
2. **Metode `nilai.at(indeks)`**: Aman. Jika indeks di luar jangkauan, C++ akan melempar kesalahan (*exception `std::out_of_range`*) yang bisa ditangani program sebelum crash.

```cpp
std::vector<int> skor = {10, 20};
std::cout << skor.at(1) << "\n"; // Mencetak 20
```

---

## Ukuran (*Size*) vs Kapasitas (*Capacity*)

Perhatikan dua konsep menarik ini:
- **`size()`**: Berapa banyak elemen nyata yang saat ini ada di dalam vector.
- **`capacity()`**: Berapa banyak ruang memori yang sudah disiapkan di belakang layar.

Ketika ruang kapasitas habis, vector akan otomatis mengalokasikan blok memori baru yang biasanya **2 kali lipat lebih besar**, memindahkan data lama ke tempat baru, dan menghapus memori lama tanpa campur tangan programmer.

Di modul berikutnya, kita akan mempelajari cara paling modern dan bersih untuk menelusuri seluruh isi vector ini: **Range-Based For Loop**!
