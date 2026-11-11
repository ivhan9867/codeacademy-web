---
id: cpp-lanjutan-10
courseId: cpp-fund-01
order: 26
title: "Menangani Situasi Darurat: Exception Handling (try, catch, throw)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami mekanisme penanganan kesalahan runtime di C++ menggunakan try, catch, dan throw
  - Mengetahui hierarki kelas eksepsi standar C++ (std::exception, std::runtime_error, std::invalid_argument)
  - Memahami konsep Stack Unwinding yang menjamin destruktor objek tetap dipanggil secara aman saat terjadi error
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <stdexcept>

  double bagi(double a, double b) {
      if (b == 0.0) {
          throw std::invalid_argument("Pembagi tidak boleh bernilai nol!");
      }
      return a / b;
  }

  int main() {
      try {
          std::cout << bagi(10.0, 0.0) << "\n";
      } catch (const std::exception& e) {
          std::cout << "Tertangkap error: " << e.what() << "\n";
      }
      return 0;
  }
---

# Menangani Situasi Darurat: Exception Handling (try, catch, throw)

Dalam aplikasi dunia nyata, hal-hal tak terduga pasti terjadi:
- Koneksi internet tiba-tiba terputus saat mengunduh data.
- Pengguna memasukkan angka pembagi 0 ke kalkulator.
- Berkas yang dicari di harddisk tiba-tiba tidak ada.

Di bahasa C, fungsi biasanya mengembalikan kode angka error (misal `-1` atau `NULL`). Namun kode error ini sangat mudah diabaikan oleh programmer ceroboh, hingga akhirnya memicu *crash* tak terkendali di tempat lain.

C++ menyediakan sistem penanganan darurat terstruktur bernama **Exception Handling (Penanganan Eksepsi)**.

---

## Tiga Komponen Utama: `throw`, `try`, dan `catch`

1. **`throw` (Melempar Bahaya)**: Saat sebuah fungsi mendeteksi kondisi darurat yang tidak bisa diselesaikan sendiri, ia "melempar" objek kesalahan ke atas.
2. **`try` (Zona Percobaan)**: Membungkus blok kode yang berpotensi memicu bahaya.
3. **`catch` (Jaring Pengaman)**: Menangkap objek kesalahan yang dilempar dan melakukan tindakan penyelamatan agar program tidak *crash*.

Perhatikan contoh berikut:

```cpp
#include <iostream>
#include <stdexcept> // Header untuk kelas eksepsi standar

int tarikUang(int saldo, int nominal) {
    if (nominal <= 0) {
        throw std::invalid_argument("Nominal penarikan harus lebih dari 0!");
    }
    if (nominal > saldo) {
        throw std::runtime_error("Saldo tidak mencukupi untuk penarikan ini!");
    }
    return saldo - nominal;
}

int main() {
    int saldoRekening = 100000;

    try {
        std::cout << "Mencoba menarik Rp150.000...\n";
        saldoRekening = tarikUang(saldoRekening, 150000);
        std::cout << "Penarikan sukses!\n";
    } 
    catch (const std::exception& e) {
        // e.what() mengembalikan pesan teks kesalahan yang dilempar
        std::cout << "[Peringatan Bank] " << e.what() << "\n";
    }

    std::cout << "Program tetap berjalan normal. Sisa saldo: Rp" << saldoRekening << "\n";
    return 0;
}
```

Output:
```text
Mencoba menarik Rp150.000...
[Peringatan Bank] Saldo tidak mencukupi untuk penarikan ini!
Program tetap berjalan normal. Sisa saldo: Rp100000
```

---

## Keajaiban: Stack Unwinding & RAII

Apa yang terjadi pada variabel lokal dan objek lain ketika sebuah error dilempar?

C++ memiliki mekanisme hebat bernama **Stack Unwinding**:
Saat `throw` dieksekusi, CPU akan melompat mundur melewati semua fungsi yang sedang berjalan hingga menemukan blok `catch` terdekat.
Selama proses lompatan tersebut, **semua objek lokal di stack otomatis dipanggil destruktornya secara sempurna**!

Artinya, jika kamu menggunakan `std::string`, `std::vector`, atau `std::unique_ptr`, memori mereka dijamin tetap bersih dan bebas dari kebocoran memori meskipun terjadi error di tengah jalan!

Selamat! Kamu telah menyelesaikan seluruh 10 modul di **Bagian 4: LANJUTAN**!
Di bagian pamungkas (**Bagian 5: EVALUASI**), kita akan merangkai seluruh pilar OOP ini menjadi satu aplikasi utuh: **Mini Proyek Sistem Perpustakaan Berorientasi Objek**!
