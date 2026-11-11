---
id: cpp-pend-1
courseId: cpp-fund-01
order: 4
title: "Apa Itu C++? Evolusi \"C with Classes\" oleh Bjarne Stroustrup"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - Memahami latar belakang sejarah kelahiran C++ dari proyek "C with Classes"
  - Membedakan paradigma prosedural (fokus pada fungsi) dan berorientasi objek (fokus pada data & entitas)
  - Memahami prinsip desain zero-overhead abstraction: kamu tidak membayar performa untuk fitur yang tidak kamu pakai
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  int main() {
      std::cout << "Bahasa C++: Cepat, Kuat, dan Berorientasi Objek\n";
      return 0;
  }
---

# Apa Itu C++? Evolusi "C with Classes" oleh Bjarne Stroustrup

Ketika Dr. Bjarne Stroustrup sedang mengerjakan tesis Ph.D.-nya di Cambridge University pada akhir tahun 1970-an, ia menghadapi dilema besar:
- Bahasa **Simula** memiliki fitur Pemrograman Berorientasi Objek (*Object-Oriented Programming / OOP*) yang sangat indah untuk merancang sistem kompleks, tetapi eksekusinya sangat lambat.
- Bahasa **BCPL dan C** luar biasa cepat dan hemat memori, tetapi karena bersifat prosedural murni, mengelola proyek perangkat lunak berskala besar menjadi sangat rumit.

Berangkat dari kebutuhan tersebut, Stroustrup memutuskan untuk menggabungkan yang terbaik dari kedua dunia: **kecepatan mesin bahasa C** dipadukan dengan **keindahan organisasi data dari Simula**.

Pada tahun 1979, lahirlah bahasa yang awalnya dinamai **"C with Classes"**, sebelum akhirnya secara resmi diganti namanya menjadi **C++** pada tahun 1983. Nama operator increment `++` (yang dalam C berarti *"tambah 1"*) menyimbolkan bahwa C++ adalah versi yang ditingkatkan satu tingkat lebih tinggi daripada bahasa C!

---

## Dari Prosedural ke Berorientasi Objek

Bagi kamu yang datang dari kelas C Fundamentals, perhatikan pergeseran pola pikir berikut:

| Pendekatan | Pemrograman Prosedural (Bahasa C) | Pemrograman Berorientasi Objek (C++) |
|:---|:---|:---|
| **Fokus Utama** | Fungsi, algoritma, dan langkah-langkah kerja (*Verba / Kata Kerja*). | Entitas data atau objek (*Nomina / Kata Benda*). |
| **Penyimpanan Data** | Variabel dan `struct` pasif yang dilemparkan ke berbagai fungsi. | Objek aktif yang menggabungkan data (*atribut*) bersama fungsi pengelolanya (*metode*). |
| **Keamanan Data** | Variabel di dalam `struct` bebas dimodifikasi oleh siapa saja dari luar. | Enkapsulasi: data penting dapat disembunyikan (*private*) dan hanya diakses melalui antarmuka resmi. |
| **Skalabilitas** | Sulit dikelola saat fungsi mencapai ribuan dan saling bergantung. | Sangat modular; komponen sistem dapat diwariskan (*inheritance*) dan digunakan ulang (*reusable*). |

---

## Prinsip Desain Emas: "Zero-Overhead Abstraction"

Banyak programmer bertanya: *"Jika C++ menambahkan begitu banyak fitur canggih, apakah program kita menjadi lebih lambat daripada bahasa C?"*

Jawabannya adalah **tidak**, berkat prinsip desain legendaris Bjarne Stroustrup:
> *"What you don't use, you don't pay for. And further: What you do use, you couldn't hand code any better."*
> *(Fitur yang tidak kamu gunakan tidak akan membebani CPU atau memori sama sekali. Dan fitur yang kamu gunakan, dirancang sedemikian rupa sehingga kamu tidak akan bisa menulis versi manualnya yang lebih cepat.)*

Contoh sederhana perbandingan program sapaan:

```cpp
#include <iostream>

int main() {
    std::cout << "Bahasa C++: Cepat, Kuat, dan Berorientasi Objek\n";
    return 0;
}
```

Output:
```text
Bahasa C++: Cepat, Kuat, dan Berorientasi Objek
```

Di modul berikutnya, kita akan membedah bagaimana objek `std::cout` dan `std::cin` bekerja menggantikan fungsi `printf` dan `scanf` lama dari bahasa C!
