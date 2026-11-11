---
id: cpp-prep-1
courseId: cpp-fund-01
order: 1
title: "Selamat Datang di C++: Dari Pemikiran Prosedural ke OOP"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - Memahami alasan mendasar transisi dari pemrograman prosedural C menuju C++ modern
  - Mengetahui metode belajar khusus kelas ini: Mode Membaca Mendalam dan Tebak Output
  - Mengenal keunggulan C++ dalam industri perangkat lunak performa tinggi dan pengembangan game
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  int main() {
      std::cout << "Selamat datang di C++ Fundamentals!\n";
      return 0;
  }
---

# Selamat Datang di C++: Dari Pemikiran Prosedural ke OOP

Jika kamu telah menyelesaikan kelas **C Fundamentals: Sintaks & Memori (`c-fund-01`)**, kamu sudah memiliki pemahaman mendalam tentang bagaimana program berinteraksi dengan memori, pointer, dan instruksi CPU. Bahasa C memberikan kendali penuh, namun ketika proyek perangkat lunak berkembang menjadi jutaan baris kode (seperti mesin pencari, sistem operasi modern, atau game engine 3D raksasa), pendekatan prosedural murni di C mulai terasa sulit dikelola.

Di sinilah **C++** hadir sebagai jawaban revolusioner.

Diciptakan oleh **Bjarne Stroustrup** pada tahun 1979 di Bell Labs, C++ awalnya dinamai *"C with Classes"*. Filosofi utamanya sangat jelas: mempertahankan kecepatan dan kendali memori tingkat rendah dari bahasa C, namun menambahkan abstraksi tingkat tinggi melalui **Pemrograman Berorientasi Objek (Object-Oriented Programming / OOP)** dan pustaka standar yang sangat kaya (Standard Template Library / STL).

---

## Di Mana C++ Digunakan Saat Ini?

C++ adalah tulang punggung teknologi dunia nyata yang menuntut performa tanpa kompromi (*zero-overhead principle*):
- **Game Engine Raksasa**: Unreal Engine dibangun sepenuhnya di atas C++.
- **Browser Web Modern**: Mesin peramban Google Chrome (Chromium) dan Mozilla Firefox ditulis menggunakan C++.
- **Kecerdasan Buatan & Komputasi Grafis**: Framework deep learning (seperti TensorFlow dan PyTorch) mengandalkan modul inti C++ untuk kalkulasi matriks berkecepatan tinggi.
- **Sistem Tertanam & Dirgantara**: Perangkat medis, otomotif otonom, hingga perangkat lunak kendali roket antariksa.

---

## Metode Belajar: Mode Baca & Tebak Output

Perlu dipahami bahwa aplikasi CodeAcademy saat ini **tidak menyertakan compiler C++ di dalam aplikasi**. Berbeda dengan kelas web yang memiliki editor interaktif langsung di layar, kelas C++ ini dirancang khusus dengan metode **Mental Execution (Menalar Alur Kode & Membaca Output)**:

1. **Kode Standar C++17 Lengkap**: Setiap contoh kode ditulis secara utuh dan valid menurut standar resmi **C++17**, lengkap dengan header `#include` dan fungsi `main()`.
2. **Kotak Output Terverifikasi**: Tepat di bawah setiap contoh program, terdapat kotak teks **Output:** yang menampilkan hasil pasti saat kode tersebut dikompilasi dan dijalankan.
3. **Latihan "Tebak Outputnya"**: Pada materi inti dan lanjutan, kamu akan ditantang membaca sekuens kode program, menalar apa yang terjadi di memori, lalu menebak outputnya secara mandiri sebelum membuka kunci penjelasan di bawah garis pemisah.

Mari kita lihat contoh kode C++ pertama kita:

```cpp
#include <iostream>

int main() {
    std::cout << "Selamat datang di C++ Fundamentals!\n";
    std::cout << "Mulai perjalananmu menguasai OOP.\n";
    return 0;
}
```

Output:
```text
Selamat datang di C++ Fundamentals!
Mulai perjalananmu menguasai OOP.
```

Perhatikan bagaimana `printf` dari bahasa C digantikan oleh objek aliran stream `std::cout` yang jauh lebih fleksibel dan aman terhadap tipe data. Kita akan membedah anatomi aliran ini di modul berikutnya!
