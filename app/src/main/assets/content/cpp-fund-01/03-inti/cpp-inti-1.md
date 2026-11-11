---
id: cpp-inti-1
courseId: cpp-fund-01
order: 9
title: "Fungsi Lebih Cerdas: Overloading Fungsi di C++"
readingMinutes: 6
section: INTI
learningObjectives:
  - Memahami konsep Function Overloading untuk menamai fungsi dengan nama yang sama selama parameternya berbeda
  - Mengetahui aturan pembeda overloading: perbedaan jumlah parameter atau perbedaan tipe data
  - Memahami proses Name Mangling yang dilakukan kompiler C++ untuk menghasilkan simbol unik
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  int tambah(int a, int b) { return a + b; }
  double tambah(double a, double b) { return a + b; }

  int main() {
      std::cout << tambah(3, 4) << "\n";
      std::cout << tambah(2.5, 4.5) << "\n";
      return 0;
  }
---

# Fungsi Lebih Cerdas: Overloading Fungsi di C++

Di bahasa C, setiap fungsi wajib memiliki nama yang unik mutlak di seluruh program. Jika kamu ingin membuat fungsi pencetak untuk angka integer, kamu menamainya `cetakInt(int x)`. Jika ingin mencetak angka pecahan, kamu terpaksa menamainya `cetakDouble(double x)`. Jika ingin mencetak teks, kamu menamainya `cetakString(char* s)`.

Mengingat begitu banyak variasi nama untuk tugas yang pada dasarnya sama adalah hal yang sangat melelahkan.

C++ menyelesaikan masalah ini dengan fitur **Function Overloading (Kelebihan Beban Fungsi)**.

---

## Apa Itu Function Overloading?

Function Overloading memungkinkan kamu membuat **beberapa fungsi dengan nama yang sama persis**, asalkan **daftar parameternya berbeda** (baik jenis tipe datanya maupun jumlah parameternya).

Perhatikan contoh program berikut:

```cpp
#include <iostream>
#include <string>

// 1. Versi untuk dua bilangan bulat
int kali(int a, int b) {
    return a * b;
}

// 2. Versi untuk dua bilangan pecahan (tipe parameter berbeda)
double kali(double a, double b) {
    return a * b;
}

// 3. Versi untuk tiga bilangan bulat (jumlah parameter berbeda)
int kali(int a, int b, int c) {
    return a * b * c;
}

int main() {
    std::cout << "kali(4, 5)        = " << kali(4, 5) << "\n";
    std::cout << "kali(2.5, 4.0)    = " << kali(2.5, 4.0) << "\n";
    std::cout << "kali(2, 3, 4)     = " << kali(2, 3, 4) << "\n";
    return 0;
}
```

Output:
```text
kali(4, 5)        = 20
kali(2.5, 4.0)    = 10
kali(2, 3, 4)     = 24
```

---

## Bagaimana Kompiler Membedakannya? (Name Mangling)

Di balik layar, kompiler C++ melakukan teknik pintar bernama **Name Mangling**.

Kompiler diam-diam mengubah nama fungsi di berkas biner dengan menyisipkan kode tipe parameternya:
- `kali(int, int)` diubah menjadi simbol unik seperti `_Z4kaliii`
- `kali(double, double)` diubah menjadi `_Z4kalidd`
- `kali(int, int, int)` diubah menjadi `_Z4kaliiii`

Bagi CPU, mereka tetap merupakan tiga fungsi yang terpisah dengan nama yang berbeda. Namun bagi kita sebagai manusia, kita cukup mengingat satu nama yang intuitif: `kali`!

---

## Aturan Emas: Return Type BUKAN Pembeda Overloading

Perhatikan aturan penting ini: **Perbedaan tipe nilai balik (*return type*) saja TIDAK cukup untuk dianggap overloading**:

```cpp
// ERROR KOMPILASI!
int hitung(int x);
double hitung(int x); // Ditolak kompiler!
```

Mengapa? Karena saat seseorang memanggil `hitung(5);`, kompiler tidak bisa menebak versi mana yang diinginkan pemanggil hanya berdasarkan parameternya.

Di modul berikutnya, kita akan melihat bagaimana parameter fungsi di C++ bisa memiliki nilai bawaan (*default argument*)!
