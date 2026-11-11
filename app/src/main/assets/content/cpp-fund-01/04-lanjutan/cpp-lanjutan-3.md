---
id: cpp-lanjutan-3
courseId: cpp-fund-01
order: 19
title: "Jati Diri Objek: Pointer 'this' dan Method Chaining"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - Memahami keberadaan pointer implisit this yang menunjuk ke alamat objek pemanggil saat ini
  - Menggunakan this->atribut untuk mengatasi benturan nama antara parameter fungsi dan atribut kelas
  - Menguasai pola desain Method Chaining yang elegan dengan mengembalikan referensi objek (*this)
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  class Kalkulator {
  public:
      int nilai = 0;
      Kalkulator& tambah(int n) {
          nilai += n;
          return *this;
      }
  };

  int main() {
      Kalkulator k;
      k.tambah(5).tambah(10);
      std::cout << "Nilai: " << k.nilai << "\n";
      return 0;
  }
---

# Jati Diri Objek: Pointer 'this' dan Method Chaining

Ketika kamu memiliki sepuluh objek mobil yang memanggil metode yang sama `mobilA.injakGas()` dan `mobilB.injakGas()`, bagaimana kode di dalam fungsi `injakGas()` tahu kecepatan mobil siapa yang harus ditambah?

Di balik layar, C++ secara otomatis menyisipkan sebuah pointer rahasia ke dalam setiap metode non-statis: **Pointer `this`**.

---

## Apa Itu Pointer `this`?

**`this`** adalah pointer konstan yang menyimpan **alamat memori dari objek yang sedang memanggil metode tersebut**.

### 1. Mengatasi Ambiguitas Nama Parameter
Sering kali kita ingin menamai parameter fungsi dengan nama yang sama persis seperti atribut kelasnya (misalnya parameter `umur` untuk mengisi atribut `umur`).

Tanpa pointer `this`, menulis `umur = umur;` akan membuat kompiler bingung karena kedua kata tersebut dianggap merujuk pada parameter lokal. Dengan pointer `this`, batasannya menjadi sangat jelas:

```cpp
#include <iostream>
#include <string>

class Karyawan {
private:
    std::string nama;
    int gaji;

public:
    void aturData(const std::string& nama, int gaji) {
        // this->nama merujuk pada atribut milik objek kelas ini
        // nama di sisi kanan merujuk pada parameter masukan fungsi
        this->nama = nama;
        this->gaji = gaji;
    }

    void cetak() const {
        std::cout << "Karyawan: " << this->nama << " | Gaji: Rp" << this->gaji << "\n";
    }
};

int main() {
    Karyawan k;
    k.aturData("Hendra", 8500000);
    k.cetak();
    return 0;
}
```

Output:
```text
Karyawan: Hendra | Gaji: Rp8500000
```

---

## 2. Pola Keren: Method Chaining (`return *this;`)

Pernahkah kamu melihat kode yang dipanggil berantai seperti:
`kalkulator.tambah(5).kali(2).kurangi(3);`?

Pola desain yang anggun ini disebut **Method Chaining (Pemanggilan Berantai)**. Kuncinya sangat sederhana: fungsi harus mengembalikan referensi ke objek itu sendiri menggunakan sintaks **`return *this;`**:

```cpp
#include <iostream>

class KalkulatorAkumulator {
private:
    int total = 0;

public:
    // Mengembalikan referensi objek saat ini (KalkulatorAkumulator&)
    KalkulatorAkumulator& tambah(int angka) {
        total += angka;
        return *this; // Kembalikan raga objek ini sendiri!
    }

    KalkulatorAkumulator& kali(int angka) {
        total *= angka;
        return *this;
    }

    void cetakTotal() const {
        std::cout << "Hasil akhir akumulasi: " << total << "\n";
    }
};

int main() {
    KalkulatorAkumulator hitung;

    // Rantai instruksi dalam satu baris ekspresif!
    hitung.tambah(10).tambah(5).kali(2);

    hitung.cetakTotal(); // (0 + 10 + 5) * 2 = 30
    return 0;
}
```

Output:
```text
Hasil akhir akumulasi: 30
```

Di modul berikutnya, kita akan mempelajari pilar kedua OOP yang memungkinkan kita membangun silsilah kode yang dapat diwariskan: **Inheritance (Pewarisan)**!
