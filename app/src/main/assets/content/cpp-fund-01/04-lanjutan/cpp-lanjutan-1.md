---
id: cpp-lanjutan-1
courseId: cpp-fund-01
order: 17
title: "Class & Object: Membangun Cetakan dan Instansiasi Nyata"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami perbedaan mendasar antara Class (cetakan abstrak) dan Object (instansiasi fisik di memori)
  - Merancang kelas yang menggabungkan keadaan (state/atribut) dan perilaku (behavior/metode)
  - Mengetahui cara menginstansiasi banyak objek mandiri dari satu definisi kelas yang sama
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <string>

  class Mobil {
  public:
      std::string merek;
      int kecepatan = 0;

      void gas(int akselerasi) {
          kecepatan += akselerasi;
      }
  };

  int main() {
      Mobil mobil1;
      mobil1.merek = "Sedan";
      mobil1.gas(60);
      std::cout << mobil1.merek << " melaju: " << mobil1.kecepatan << " km/jam\n";
      return 0;
  }
---

# Class & Object: Membangun Cetakan dan Instansiasi Nyata

Pemrograman Berorientasi Objek (*Object-Oriented Programming / OOP*) adalah cara memandang perangkat lunak bukan sebagai kumpulan baris instruksi yang kaku, melainkan sebagai sekumpulan **Objek** di dunia nyata yang saling berinteraksi.

Dua pilar pembentuknya adalah **Class** dan **Object**.

![Analogi Cetakan Kue dan Objek Fisik](asset:images/cpp-fund-01/cetakan-dan-objek.png)

---

## Analogi Cetakan Kue (Class) dan Kue Jadi (Object)

Bayangkan sebuah cetakan kue berbentuk bintang:
- **Class (Cetakan)**: Hanyalah sebuah pola cetakan berbahan aluminium di atas meja. Cetakan itu sendiri tidak bisa dimakan, tidak memiliki rasa, dan tidak memakan ruang di dalam perutmu.
- **Object (Kue Jadi / Instansiasi)**: Ketika kamu menuangkan adonan cokelat ke cetakan tersebut dan memanggangnya, lahirlah sebuah **Kue Bintang Cokelat**. Ketika kamu menuangkan adonan vanila, lahirlah **Kue Bintang Vanila**.

Kedua kue tersebut memiliki bentuk bintang yang sama persis (karena berasal dari cetakan yang sama), tetapi mereka adalah dua kue fisik yang terpisah di dunia nyata. Jika kue cokelat digigit, kue vanila tetap utuh!

---

## Anatomi Kelas: Atribut & Metode

Di dalam C++, sebuah kelas tersusun dari dua hal:
1. **Atribut (*Member Variables / State*)**: Data atau ciri-ciri yang dimiliki objek (misal: merek, warna, kecepatan).
2. **Metode (*Member Functions / Behavior*)**: Tindakan atau kemampuan yang bisa dilakukan objek tersebut (misal: injak gas, rem, nyalakan lampu).

Perhatikan contoh kode berikut:

```cpp
#include <iostream>
#include <string>

class Mobil {
public:
    std::string merek;
    int kecepatan = 0;

    void injakGas(int tambahan) {
        kecepatan += tambahan;
        std::cout << merek << " menambah kecepatan sebesar " << tambahan << " km/jam.\n";
    }

    void injakRem(int kurangi) {
        kecepatan -= kurangi;
        if (kecepatan < 0) kecepatan = 0;
        std::cout << merek << " mengerem. Kecepatan sekarang: " << kecepatan << " km/jam.\n";
    }
};

int main() {
    // Menginstansiasi dua objek mandiri dari kelas Mobil
    Mobil mobilA;
    mobilA.merek = "Avanza";

    Mobil mobilB;
    mobilB.merek = "Innova";

    // Berinteraksi dengan masing-masing objek
    mobilA.injakGas(50);
    mobilB.injakGas(80);
    mobilA.injakRem(20);

    std::cout << "\n--- Ringkasan Akhir ---\n";
    std::cout << mobilA.merek << ": " << mobilA.kecepatan << " km/jam\n";
    std::cout << mobilB.merek << ": " << mobilB.kecepatan << " km/jam\n";

    return 0;
}
```

Output:
```text
Avanza menambah kecepatan sebesar 50 km/jam.
Innova menambah kecepatan sebesar 80 km/jam.
Avanza mengerem. Kecepatan sekarang: 30 km/jam.

--- Ringkasan Akhir ---
Avanza: 30 km/jam
Innova: 80 km/jam
```

Perhatikan bahwa saat `mobilA` mengerem dari 50 menjadi 30 km/jam, kecepatan `mobilB` tetap berada di 80 km/jam. Masing-masing objek memiliki ruang memorinya sendiri-sendiri secara terisolasi!

Di modul berikutnya, kita akan mempelajari bagaimana sebuah objek lahir ke dunia dan bagaimana ia dimusnahkan secara aman melalui **Konstruktor dan Destruktor**!
