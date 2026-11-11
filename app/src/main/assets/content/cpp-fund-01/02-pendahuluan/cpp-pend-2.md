---
id: cpp-pend-2
courseId: cpp-fund-01
order: 5
title: "Program C++ Pertama: std::cout, std::cin, dan Aliran Stream"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - Memahami filosofi I/O berbasis Aliran Stream (iostream) pada C++
  - Menggunakan operator stream insertion (<<) bersama std::cout untuk mencetak berbagai tipe data tanpa format specifier
  - Memahami perbedaan performa antara manipulator std::endl dan karakter baris baru '\n'
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  int main() {
      int skor = 95;
      std::cout << "Skor Pemain: " << skor << "\n";
      return 0;
  }
---

# Program C++ Pertama: std::cout, std::cin, dan Aliran Stream

Di bahasa C, mencetak data ke layar menggunakan fungsi `printf("%s memiliki nilai %d\n", nama, nilai);`. Kamu wajib mengingat puluhan *format specifier* seperti `%d`, `%f`, `%s`, `%lf`, dan jika salah memasangkan tipe data, program bisa mengalami bug memori yang fatal.

C++ mengganti pendekatan kuno tersebut dengan konsep yang jauh lebih elegan: **Aliran Stream (`<iostream>`)**.

---

## Analogi Aliran Air (Stream)

Bayangkan sebuah selang air:
- Data dialirkan mengalir dari satu ujung ke ujung lainnya.
- Simbol **`<<`** disebut **Stream Insertion Operator** (operator penyisip aliran). Anggap tanda panah ganda ini menunjukkan arah aliran data: dari variabel kita mengalir **menuju** layar monitor (`std::cout`).

Perhatikan contoh program berikut:

```cpp
#include <iostream>

int main() {
    int umur = 20;
    double ipk = 3.85;
    char inisial = 'V';

    std::cout << "Inisial: " << inisial << "\n";
    std::cout << "Umur: " << umur << " tahun\n";
    std::cout << "IPK: " << ipk << "\n";

    return 0;
}
```

Output:
```text
Inisial: V
Umur: 20 tahun
IPK: 3.85
```

### Keunggulan Aliran Stream C++:
1. **Bebas Format Specifier**: Kamu tidak perlu menulis `%d` atau `%lf`. Kompiler C++ cukup cerdas untuk mengetahui tipe data secara otomatis (*Type-Safe*).
2. **Penggabungan Berantai (Chaining)**: Kamu dapat merangkai teks dan variabel sebanyak mungkin hanya dengan mengulang operator `<<` dalam satu baris.

---

## Membaca Input: std::cin dan Operator `>>`

Jika `std::cout` mengalirkan data keluar ke layar monitor (*Console Output*), maka **`std::cin`** mengalirkan data masuk dari keyboard pengguna (*Console Input*).

Arah panahnya dibalik menjadi **`>>`** (*Stream Extraction Operator*): data dari keyboard mengalir **menuju** variabel penampung:

```cpp
#include <iostream>

int main() {
    int angkaFavorit = 7; // Nilai awal jika tanpa input interaktif
    std::cout << "Angka favorit tersimpan: " << angkaFavorit << "\n";
    return 0;
}
```

Output:
```text
Angka favorit tersimpan: 7
```

---

## std::endl vs '\n': Mana yang Lebih Cepat?

Kamu mungkin sering melihat orang menulis `std::cout << "Halo" << std::endl;`.

Apa perbedaan `std::endl` dengan karakter baris baru biasa `'\n'`?
- `'\n'`: Hanya menyisipkan karakter baris baru ke dalam penyangga memori (*buffer*). Sangat cepat dan efisien.
- `std::endl`: Menyisipkan karakter baris baru **DAN memaksa peramban/terminal mengosongkan buffer memori seketika (*force flush*)**.

> **Tips Performa Software Engineer:**
> Memaksa flush memori berulang-ulang di dalam perulangan ribuan kali menggunakan `std::endl` dapat memperlambat program secara drastis. Gunakan selalu **`'\n'`** sebagai kebiasaan standar modern, kecuali kamu benar-benar butuh memastikan teks tercetak sebelum program mengalami crash.

Di modul berikutnya, kita akan mempelajari arti awalan `std::` dan bagaimana **Namespace** menyelamatkan proyek besar dari benturan nama variabel!
