---
id: cpp-inti-2
courseId: cpp-fund-01
order: 10
title: "Parameter Fleksibel: Argumen Bawaan (Default Parameter)"
readingMinutes: 5
section: INTI
learningObjectives:
  - Menguasai sintaks penetapan nilai bawaan (default arguments) pada deklarasi parameter fungsi
  - Memahami aturan penempatan parameter default dari urutan paling kanan ke kiri
  - Mengetahui kapan menggunakan default parameter sebagai solusi yang lebih ringkas dibanding overloading fungsi
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <string>

  void sapa(const std::string& nama, const std::string& gelar = "Siswa") {
      std::cout << "Halo, " << gelar << " " << nama << "!\n";
  }

  int main() {
      sapa("Budi");
      sapa("Siti", "Dokter");
      return 0;
  }
---

# Parameter Fleksibel: Argumen Bawaan (Default Parameter)

Sering kali kita membuat fungsi yang memiliki parameter umum dengan nilai yang hampir selalu sama di sebagian besar situasi.

Misalnya fungsi untuk mencetak struk: warna teks bawaannya biasanya "Hitam", jumlah cetak bawaannya biasanya 1 lembar. Namun sesekali, kita ingin mengubah warnanya menjadi "Merah" atau mencetak 3 rangkap.

Di bahasa C, kamu harus selalu mengetikkan semua parameter tersebut setiap kali fungsi dipanggil. C++ menghadirkan kemudahan bernama **Default Parameter (Argumen Bawaan)**.

---

## Menetapkan Nilai Default

Kamu dapat langsung memberikan nilai awal pada parameter menggunakan tanda sama dengan (`=`) di dalam tanda kurung fungsi:

```cpp
#include <iostream>

// Pajak default adalah 11% (0.11), diskon default adalah 0
double hitungTotal(double harga, double pajak = 0.11, double diskon = 0.0) {
    double totalPajak = harga * pajak;
    return (harga + totalPajak) - diskon;
}

int main() {
    // 1. Memanggil dengan 1 argumen (pajak dan diskon menggunakan nilai default)
    std::cout << "Belanja 1: Rp" << hitungTotal(100000) << "\n";

    // 2. Memanggil dengan 2 argumen (diskon tetap menggunakan nilai default 0.0)
    std::cout << "Belanja 2 (Pajak 0%): Rp" << hitungTotal(100000, 0.0) << "\n";

    // 3. Memanggil dengan 3 argumen lengkap (semua default ditimpa)
    std::cout << "Belanja 3 (Lengkap): Rp" << hitungTotal(100000, 0.11, 20000) << "\n";

    return 0;
}
```

Output:
```text
Belanja 1: Rp111000
Belanja 2 (Pajak 0%): Rp100000
Belanja 3 (Lengkap): Rp91000
```

---

## Aturan Penting: Dari Kanan ke Kiri

Kompiler C++ menuntut satu aturan ketat: **Parameter yang memiliki nilai default harus diletakkan di bagian paling kanan**.

Kamu tidak boleh memiliki parameter biasa (tanpa default) setelah parameter yang memiliki nilai default:

```cpp
// BENAR: parameter default berada di paling kanan
void aturJendela(int lebar, int tinggi, bool fullscreen = false);

// SALAH! Error kompilasi!
void aturJendela(bool fullscreen = false, int lebar, int tinggi);
```

Mengapa? Karena saat pemanggil hanya memberikan 2 argumen seperti `aturJendela(800, 600)`, kompiler akan memasangkan argumen dari kiri ke kanan. Jika parameter default ditaruh di awal, kompiler akan bingung menentukan argumen mana yang ditujukan untuk parameter yang mana.

Di modul berikutnya, kita akan membahas salah satu topik performa terpenting dalam C++: **Pass-by-Value vs Pass-by-Reference (`const T&`)**!
