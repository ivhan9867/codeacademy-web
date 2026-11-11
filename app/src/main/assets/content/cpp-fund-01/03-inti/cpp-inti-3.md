---
id: cpp-inti-3
courseId: cpp-fund-01
order: 11
title: "Efisiensi Memori: Pass-by-Value vs Pass-by-Reference (& dan const &)"
readingMinutes: 6
section: INTI
learningObjectives:
  - Memahami beban memori dan penurunan performa akibat penyalinan objek besar pada Pass-by-Value
  - Menguasai Pass-by-Reference (&) untuk fungsi yang memang bertujuan mengubah data asli pemanggil
  - Menerapkan idiom paling esensial dalam Modern C++: Pass-by-Const-Reference (const T&)
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <string>

  void tampilkan(const std::string& teks) {
      std::cout << teks << "\n";
  }

  int main() {
      std::string pesan = "C++ Efisien!";
      tampilkan(pesan);
      return 0;
  }
---

# Efisiensi Memori: Pass-by-Value vs Pass-by-Reference (& dan const &)

Di kelas C Fundamentals, kita mengetahui bahwa parameter fungsi secara default bersifat **Pass-by-Value**: nilai argumen akan disalin ke variabel baru di dalam stack fungsi.

Untuk tipe data primitif kecil seperti `int` (4 byte) atau `char` (1 byte), menyalin nilai ini sangat murah dan cepat. Namun, bagaimana jika kamu mengirimkan sebuah teks novel sepanjang 100.000 karakter di dalam `std::string`, atau daftar 50.000 data pengguna?

Menyalin objek besar tersebut setiap kali fungsi dipanggil akan memboroskan RAM dan membuat program menjadi lambat!

---

## Tiga Cara Melewatkan Parameter di C++

Mari kita bedah tiga gaya utama berikut:

### 1. Pass-by-Value (Menyalin Utuh / Fotokopi)
```cpp
void naikkanNilai(int x) {
    x = x + 10; // Hanya mengubah salinan lokal
}
```
Fungsi menerima lembar fotokopi. Jika fungsi mencoret-coret lembaran tersebut, dokumen asli di tempat pemanggil sama sekali tidak berubah.

### 2. Pass-by-Reference Biasa (`T&`)
Gunakan cara ini jika fungsi kamu **memang bertugas mengubah nilai asli** dari pemanggil:

```cpp
#include <iostream>

void tukar(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp;
}

int main() {
    int x = 5, y = 10;
    tukar(x, y);

    std::cout << "x = " << x << ", y = " << y << "\n";
    return 0;
}
```

Output:
```text
x = 10, y = 5
```

Bandingkan dengan bahasa C: di C kamu harus menulis `tukar(&x, &y)` dengan tanda bintang pointer yang rumit. Di C++, cukup tambahkan tanda `&` pada deklarasi parameter fungsi, dan pemanggilannya bersih seperti fungsi biasa!

---

## 3. Idiom Emas Industri: Pass-by-Const-Reference (`const T&`)

Bagaimana jika kita **tidak ingin mengubah data asli**, tetapi juga **tidak ingin memboroskan memori untuk menyalinnya**?

Jawabannya adalah **`const T&`**:
- Menggunakan `&` (referensi): Komputer hanya mengirimkan alias alamatnya, **nol byte objek yang disalin**.
- Menggunakan `const`: Kompiler menjamin secara hukum bahwa fungsi tersebut **hanya membaca (*read-only*)** dan dilarang keras mengubah datanya.

```cpp
#include <iostream>
#include <string>

// Melewatkan string besar dengan kecepatan maksimal dan keamanan mutlak
void cetakLaporan(const std::string& nama, const std::string& pesan) {
    // nama = "Budi"; // ERROR KOMPILASI! const melindungi data dari modifikasi tak sengaja
    std::cout << "[Laporan] " << nama << ": " << pesan << "\n";
}

int main() {
    std::string user = "Administrator";
    std::string catatan = "Sistem telah diperbarui ke standar C++17.";

    cetakLaporan(user, catatan);
    return 0;
}
```

Output:
```text
[Laporan] Administrator: Sistem telah diperbarui ke standar C++17.
```

---

## Panduan Cepat Pemilihan Parameter

1. Tipe data primitif kecil (`int`, `double`, `bool`, `char`): gunakan **Pass-by-Value** (`int x`).
2. Objek besar (`std::string`, `std::vector`, struct, class) untuk dibaca: gunakan **`const T&`** (`const std::string& s`).
3. Data yang ingin diubah nilainya oleh fungsi: gunakan **Pass-by-Reference** (`int& x` atau `std::string& s`).

Di modul berikutnya, kita akan mendalami fitur-fitur canggih pengolah teks yang dimiliki oleh `std::string`!
