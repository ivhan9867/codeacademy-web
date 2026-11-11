---
id: cpp-lanjutan-9
courseId: cpp-fund-01
order: 25
title: "Manajemen Memori Aman: Smart Pointer (unique_ptr & shared_ptr)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami risiko fatal alokasi dinamis manual dengan new dan delete di bahasa C/C++ lama
  - Menguasai std::unique_ptr dan fungsi pembuat std::make_unique (C++14/17) untuk kepemilikan tunggal
  - Memahami std::shared_ptr dan mekanisme Reference Counting untuk kepemilikan memori bersama
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <memory>

  struct Senjata {
      int serangan = 100;
      ~Senjata() {
          std::cout << "Senjata dihancurkan otomatis!\n";
      }
  };

  int main() {
      // Mengalokasikan memori heap secara cerdas dan aman
      auto s = std::make_unique<Senjata>();
      std::cout << "Kekuatan: " << s->serangan << "\n";
      return 0; // Memori heap otomatis dibebaskan tanpa 'delete'!
  }
---

# Manajemen Memori Aman: Smart Pointer (unique_ptr & shared_ptr)

Di bahasa C++ lama (C++98), mengalokasikan data di heap dilakukan dengan operator `new`, dan membebaskannya dengan operator `delete`:

```cpp
// CARA LAMA (BERBAHAYA! HINDARI DI MODERN C++)
Karakter* hero = new Karakter();
// ... jika di tengah jalan ada return awal atau exception meledak ...
delete hero; // Baris ini tidak pernah tercapai -> MEMORY LEAK!
```

Jika seorang programmer lupa menulis `delete`, memori RAM komputer akan bocor secara perlahan sampai aplikasi macet kehabisan memori.

Mulai standar C++11/14/17, C++ modern memperkenalkan penyelamat memori bernama **Smart Pointer (Pointer Cerdas)** melalui header `<memory>`.

---

## 1. `std::unique_ptr`: Pemilik Tunggal Eksklusif

`std::unique_ptr` adalah jenis smart pointer yang **paling ringan dan paling sering digunakan di industri**:
- **Kepemilikan Tunggal**: Hanya ada tepat satu pointer yang boleh memiliki objek tersebut di memori.
- **Nol Overhead**: Kecepatannya sama persis seperti pointer mentah biasa.
- **Pembersihan Otomatis**: Begitu `unique_ptr` keluar dari cakupan kurung kurawal tempat ia hidup, **ia otomatis memanggil `delete`** untuk membebaskan memorinya!

Gunakan fungsi pembantu standar **`std::make_unique`**:

```cpp
#include <iostream>
#include <memory>
#include <string>

class BerkasLog {
private:
    std::string namaBerkas;

public:
    BerkasLog(const std::string& nama) : namaBerkas(nama) {
        std::cout << "[Buka] " << namaBerkas << "\n";
    }

    ~BerkasLog() {
        std::cout << "[Tutup] " << namaBerkas << " dibersihkan dari RAM!\n";
    }

    void catat(const std::string& pesan) const {
        std::cout << "Log: " << pesan << "\n";
    }
};

int main() {
    std::cout << "--- Memulai program ---\n";
    {
        // Alokasikan objek di heap menggunakan unique_ptr
        auto logPtr = std::make_unique<BerkasLog>("server.log");

        // Mengakses metode semudah pointer biasa dengan tanda panah '->'
        logPtr->catat("Koneksi berhasil");

        std::cout << "--- Keluar dari blok kurung kurawal ---\n";
    } // logPtr mati di sini -> Destruktor BerkasLog otomatis dipanggil seketika!

    std::cout << "--- Akhir dari program ---\n";
    return 0;
}
```

Output:
```text
--- Memulai program ---
[Buka] server.log
Log: Koneksi berhasil
--- Keluar dari blok kurung kurawal ---
[Tutup] server.log dibersihkan dari RAM!
--- Akhir dari program ---
```

Kamu tidak perlu mengetikkan kata `delete` satu kali pun!

---

## 2. `std::shared_ptr`: Kepemilikan Bersama

Bagaimana jika sebuah objek di memori harus dipakai bersama oleh beberapa sistem yang berbeda (misalnya aset tekstur gambar yang dipakai oleh 5 monster sekaligus di dalam game)?

Gunakan **`std::shared_ptr`** (dibuat dengan `std::make_shared`):
- Menggunakan sistem **Reference Counting (Hitungan Referensi)**.
- Setiap kali ada pointer baru yang ikut menunjuk objek tersebut, angka hitungannya bertambah 1.
- Setiap kali ada pointer yang mati, hitungannya berkurang 1.
- Objek di heap **hanya akan dihapus ketika hitungan referensi mencapai angka 0** (ketika semua pemiliknya sudah tidak ada lagi).

> **Pedoman Emas Modern C++:**
> *"Jangan gunakan pointer mentah ber-`new` / `delete`. Gunakan `std::unique_ptr` secara bawaan (*default*). Gunakan `std::shared_ptr` hanya jika objek memang benar-benar harus dimiliki bersama oleh banyak pihak."*

Di modul berikutnya, kita akan mempelajari bagaimana menangani situasi darurat dan kesalahan runtime menggunakan **Exception Handling**!
