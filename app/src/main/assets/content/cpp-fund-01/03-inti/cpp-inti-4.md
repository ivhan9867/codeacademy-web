---
id: cpp-inti-4
courseId: cpp-fund-01
order: 12
title: "Manipulasi Teks Canggih: Menjelajah Fitur std::string"
readingMinutes: 6
section: INTI
learningObjectives:
  - Menguasai metode inti pengolahan teks pada kelas std::string (substr, find, empty, append)
  - Memahami cara memeriksa hasil pencarian karakter atau substring menggunakan konstanta std::string::npos
  - Mengonversi data antara angka dan string secara aman menggunakan std::to_string() dan std::stoi()
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <string>

  int main() {
      std::string kalimat = "Belajar C++ Modern";
      std::cout << "Substr: " << kalimat.substr(8, 3) << "\n";
      return 0;
  }
---

# Manipulasi Teks Canggih: Menjelajah Fitur std::string

Di modul pendahuluan, kita sudah melihat bahwa `std::string` dapat digabungkan dengan mudah menggunakan operator `+`. Sekarang, mari kita jelajahi metode-metode kelas (*member functions*) bawaan yang membuat pemrosesan teks di C++ menjadi sangat bertenaga.

---

## 1. Mengambil Potongan Teks: `.substr(posisi, panjang)`

Gunakan `.substr()` untuk mengekstrak sebagian teks dari sebuah string:
- Argumen pertama: indeks awal karakter.
- Argumen kedua: berapa banyak karakter yang ingin diambil.

```cpp
#include <iostream>
#include <string>

int main() {
    std::string berkas = "laporan_keuangan_2026.pdf";

    // Mengambil kata "keuangan" (mulai indeks 8, sepanjang 8 karakter)
    std::string topik = berkas.substr(8, 8);

    std::cout << "Topik berkas: " << topik << "\n";
    return 0;
}
```

Output:
```text
Topik berkas: keuangan
```

---

## 2. Mencari Kata: `.find()` dan `std::string::npos`

Metode `.find()` mencari kemunculan pertama suatu kata kunci dan mengembalikan **nomor indeksnya**.

Jika kata kunci yang dicari **tidak ditemukan**, `.find()` mengembalikan nilai konstanta khusus bernama **`std::string::npos`** (*no position*):

```cpp
#include <iostream>
#include <string>

int main() {
    std::string kalimat = "Pemrograman C++ berorientasi objek";

    size_t posisi = kalimat.find("C++");

    if (posisi != std::string::npos) {
        std::cout << "Kata 'C++' ditemukan di indeks ke-" << posisi << "\n";
    } else {
        std::cout << "Kata tidak ditemukan.\n";
    }

    return 0;
}
```

Output:
```text
Kata 'C++' ditemukan di indeks ke-12
```

---

## 3. Konversi Antara Angka dan String

Dalam rekayasa perangkat lunak, kita sering perlu mengubah angka menjadi teks atau sebaliknya:
- **`std::to_string(angka)`**: Mengubah angka integer atau float menjadi objek `std::string`.
- **`std::stoi(teks)`**: (*String to Integer*) Mengubah string teks menjadi bilangan bulat.

```cpp
#include <iostream>
#include <string>

int main() {
    int nilaiAsli = 100;
    std::string teksNilai = std::to_string(nilaiAsli);
    std::string pesan = "Skor kamu adalah " + teksNilai;

    std::cout << pesan << "\n";

    std::string inputPengguna = "45";
    int hasilTambah = std::stoi(inputPengguna) + 5;
    std::cout << "Hasil hitung: " << hasilTambah << "\n";

    return 0;
}
```

Output:
```text
Skor kamu adalah 100
Hasil hitung: 50
```

Semua metode ini bekerja secara aman di memori tanpa risiko kebocoran memori (*memory leak*). Di modul berikutnya, kita akan mempelajari wadah koleksi paling populer di C++: **`std::vector`**!
