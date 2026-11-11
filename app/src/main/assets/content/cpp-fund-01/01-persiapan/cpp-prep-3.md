---
id: cpp-prep-3
courseId: cpp-fund-01
order: 3
title: "Menyiapkan Kompiler C++ di Komputermu (g++ & std=c++17)"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - Memahami tahapan rantai kompilasi C++ dari berkas teks hingga biner executable
  - Menguasai perintah terminal dasar kompilasi: g++ -std=c++17 berkas.cpp -o program
  - Menjalankan file executable hasil kompilasi di sistem operasi Linux, macOS, atau Windows
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  int main() {
      std::cout << "Kompilasi sukses dengan standar C++17!\n";
      return 0;
  }
---

# Menyiapkan Kompiler C++ di Komputermu (g++ & std=c++17)

Meskipun seluruh materi di CodeAcademy disajikan dengan format **Baca & Tebak Output**, sangat dianjurkan bagimu untuk mencoba mengompilasi kode-kode latihan di komputer pribadimu untuk memperkuat jam terbang teknis.

---

## Rantai Kompilasi C++ (Compilation Pipeline)

Saat kamu menulis kode C++, komputer tidak bisa langsung mengeksekusi teks tersebut. Ada tiga tahapan penting di balik layar:

1. **Preprocessor**: Memproses direktif yang diawali tanda `#` (seperti `#include <iostream>`). Kode header akan disalin langsung ke dalam berkasmu.
2. **Compiler**: Menerjemahkan kode C++ menjadi bahasa assembly dan berkas objek (*machine code* perantara). Di tahap ini, sintaks dan tipe data diperiksa ketat.
3. **Linker**: Menggabungkan berkas objek kodemu dengan kode pustaka standar C++ (STL) menjadi satu berkas biner berekstensi `.exe` (di Windows) atau berkas executable mandiri (di Linux/macOS).

---

## Perintah Standar Kompilasi: g++

Compiler C++ yang paling banyak digunakan di dunia adalah **GCC (`g++`)** dan **Clang (`clang++`)**.

Untuk memastikan semua fitur modern yang kita pelajari (seperti `auto`, range-based for loop, dan struktur kelas modern) didukung penuh, gunakan selalu flag **`-std=c++17`**:

### 1. Simpan Kode ke Berkas Teks
Buat sebuah berkas bernama `program.cpp` dan tulis kode berikut:

```cpp
#include <iostream>

int main() {
    std::cout << "Kompilasi sukses dengan standar C++17!\n";
    return 0;
}
```

### 2. Kompilasi Melalui Terminal / Command Prompt
Buka terminal di folder tempat berkas `program.cpp` berada, lalu ketik perintah berikut:

```bash
g++ -std=c++17 program.cpp -o program
```

**Penjelasan Perintah:**
- `g++`: Nama program compiler C++.
- `-std=c++17`: Menginstruksikan compiler untuk menggunakan standar bahasa C++17.
- `program.cpp`: Berkas kode sumber (*source code*) yang ingin dikompilasi.
- `-o program`: Menamai berkas biner keluaran (*output binary*) dengan nama `program`.

### 3. Jalankan Program
Ketik perintah berikut untuk menjalankan program:
- Di Linux / macOS: `./program`
- Di Windows: `program.exe` atau `.\program`

Output di terminal komputermu:
```text
Kompilasi sukses dengan standar C++17!
```

---

Sekarang fondasi pemahamanmu sudah siap! Di **Bagian 2: Dasar-Dasar C++**, kita akan langsung membedah perbedaan utama antara bahasa C lama dengan C++ modern.
