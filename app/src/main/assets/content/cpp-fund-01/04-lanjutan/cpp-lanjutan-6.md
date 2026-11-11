---
id: cpp-lanjutan-6
courseId: cpp-fund-01
order: 22
title: "Kontrak Wajib: Pure Virtual Function dan Abstract Class (Interface)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami konsep Pure Virtual Function dengan sintaks = 0 sebagai kewajiban implementasi bagi kelas anak
  - Memahami mengapa Kelas Abstrak (Abstract Class) tidak dapat diinstansiasi secara langsung menjadi objek mandiri
  - Merancang arsitektur sistem berbasis Interface murni untuk memisahkan spesifikasi kontrak dari implementasi nyata
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  class Bentuk {
  public:
      virtual double hitungLuas() const = 0; // Pure Virtual Function
      virtual ~Bentuk() = default;
  };

  class Persegi : public Bentuk {
  private:
      double sisi;
  public:
      Persegi(double s) : sisi(s) {}
      double hitungLuas() const override {
          return sisi * sisi;
      }
  };

  int main() {
      Persegi p(5.0);
      std::cout << "Luas: " << p.hitungLuas() << "\n";
      return 0;
  }
---

# Kontrak Wajib: Pure Virtual Function dan Abstract Class (Interface)

Bayangkan kamu diminta menghitung luas sebuah *"Bentuk"*.
Kamu tentu akan bertanya: *"Bentuk apa dulu? Apakah lingkaran? Segitiga? Atau persegi?"*.

Sebuah "Bentuk" itu sendiri adalah sebuah ide abstrak. Kamu tidak bisa menghitung luas sebuah ide abstrak sebelum wujud fisiknya spesifik. Namun, kamu bisa membuat aturan tegas:
> *"Siapa pun bangun ruang yang mengklaim dirinya adalah sebuah **Bentuk**, kamu **DIWAJIBKAN** memiliki rumus untuk menghitung luas!"*

Di C++, aturan kontrak wajib ini diwujudkan melalui **Pure Virtual Function (Fungsi Virtual Murni)**.

---

## Sintaks Pure Virtual Function: `= 0`

Untuk menjadikan suatu fungsi virtual sebagai pure virtual function, kita menambahkan penanda **`= 0`** di akhir deklarasinya:

```cpp
class Pembayaran {
public:
    // Pure Virtual Function: Tidak memiliki isi badan fungsi di sini
    virtual void prosesTransaksi(int nominal) = 0;

    virtual ~Pembayaran() = default;
};
```

---

## Kelas Abstrak (Abstract Class)

Sebuah kelas yang memiliki **minimal satu** pure virtual function secara otomatis disebut sebagai **Kelas Abstrak (*Abstract Class*)**.

### Konsekuensi Mutlak Kelas Abstrak:
Kamu **TIDAK BISA menciptakan objek langsung dari kelas abstrak**:
```cpp
Pembayaran p; // ERROR KOMPILASI! cannot declare variable 'p' to be of abstract type
```

Satu-satunya cara menggunakan kelas ini adalah dengan membuat **kelas turunan yang mengimplementasikan (mengisi badan) seluruh fungsi `= 0` tersebut**:

```cpp
#include <iostream>

class Pembayaran {
public:
    virtual void prosesTransaksi(int nominal) = 0;
    virtual ~Pembayaran() = default;
};

class TransferBank : public Pembayaran {
public:
    void prosesTransaksi(int nominal) override {
        std::cout << "Memproses transfer bank sebesar: Rp" << nominal << "\n";
    }
};

class DompetDigital : public Pembayaran {
public:
    void prosesTransaksi(int nominal) override {
        std::cout << "Memotong saldo e-wallet sebesar: Rp" << nominal << "\n";
    }
};

// Fungsi umum yang menerima kontrak interface apa pun tanpa peduli implementasinya
void lakukanPembayaran(Pembayaran* metode, int jumlah) {
    metode->prosesTransaksi(jumlah);
}

int main() {
    TransferBank bca;
    DompetDigital gopay;

    lakukanPembayaran(&bca, 150000);
    lakukanPembayaran(&gopay, 75000);

    return 0;
}
```

Output:
```text
Memproses transfer bank sebesar: Rp150000
Memotong saldo e-wallet sebesar: Rp75000
```

---

## Mengapa Konsep Interface Ini Sangat Dicintai?

Pola desain ini memisahkan **Kontrak (*What to do*)** dari **Implementasi Detail (*How to do it*)**:
- Jika esok hari perusahaanmu ingin menambahkan metode pembayaran baru seperti `KartuKredit`, kamu cukup membuat satu kelas baru yang mewarisi `Pembayaran`.
- Fungsi `lakukanPembayaran()` yang sudah ada sama sekali tidak perlu diubah atau diuji ulang!

Di modul berikutnya, kita akan mempelajari bagaimana memberi arti baru pada simbol matematika menggunakan **Operator Overloading**!
