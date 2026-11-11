---
id: cpp-inti-8
courseId: cpp-fund-01
order: 16
title: "Benteng Keamanan Data: Enkapsulasi, Access Specifier & Tebak Output"
readingMinutes: 7
section: INTI
learningObjectives:
  - Memahami prinsip Enkapsulasi sebagai pilar pertama Pemrograman Berorientasi Objek (OOP)
  - Menguasai pembagian zona akses public: dan private: serta metode Getter dan Setter
  - Melatih intuisi eksekusi mental melalui latihan komprehensif "Tebak Outputnya"
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  class Dompet {
  private:
      int saldo = 0;
  public:
      void setor(int uang) {
          if (uang > 0) saldo += uang;
      }
      int getSaldo() const {
          return saldo;
      }
  };

  int main() {
      Dompet d;
      d.setor(50000);
      std::cout << "Saldo: Rp" << d.getSaldo() << "\n";
      return 0;
  }
---

# Benteng Keamanan Data: Enkapsulasi, Access Specifier & Tebak Output

Bayangkan sebuah brankas bank:
Jika uang di dalam brankas diletakkan di atas meja luar tanpa penjaga, siapa pun bisa mengambil uang atau memasukkan uang palsu sesuka hati.

Untuk melindungi uang tersebut, bank membuat aturan:
1. Ruang brankas dikunci rapat di balik pintu baja (**Zona Private**).
2. Nasabah hanya boleh berinteraksi melalui loket teller resmi (**Zona Public**). Teller akan memverifikasi apakah uangmu asli sebelum menambahkannya ke catatan saldo.

Di dunia pemrograman berorientasi objek, prinsip melindungi data internal dan hanya mengizinkan modifikasi melalui fungsi resmi yang terverifikasi disebut **Enkapsulasi (Encapsulation)**.

---

## Access Specifier: `private:` dan `public:`

Di dalam sebuah kelas C++, kita membagi wilayah anggota menggunakan kata kunci penentu akses:

- **`private:`**: Hanya dapat dibaca dan dimodifikasi oleh fungsi yang berada **di dalam kelas itu sendiri**. Kode dari luar (seperti di dalam `main()`) sama sekali tidak boleh menyentuhnya.
- **`public:`**: Antarmuka resmi yang bebas dipanggil oleh kode luar.

Perhatikan implementasi kelas `DompetDigital`:

```cpp
#include <iostream>

class DompetDigital {
private:
    int saldo = 0; // Data sensitif terlindungi di zona private

public:
    // Setter dengan logika validasi resmi
    void isiSaldo(int jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
            std::cout << "Berhasil mengisi: Rp" << jumlah << "\n";
        } else {
            std::cout << "Pengisian gagal: nominal harus positif!\n";
        }
    }

    // Getter untuk membaca data secara aman
    int getSaldo() const {
        return saldo;
    }
};

int main() {
    DompetDigital dompet;

    // dompet.saldo = 1000000; // ERROR! Compiler menolak akses langsung ke zona private

    dompet.isiSaldo(50000);
    dompet.isiSaldo(-10000); // Percobaan ilegal ditolak teller!

    std::cout << "Saldo akhir tersimpan: Rp" << dompet.getSaldo() << "\n";
    return 0;
}
```

Output:
```text
Berhasil mengisi: Rp50000
Pengisian gagal: nominal harus positif!
Saldo akhir tersimpan: Rp50000
```

---

## Latihan "Tebak Outputnya"

Saatnya melatih penalaran mentalmu (*mental execution*)! Bacalah program C++17 berikut dari atas ke bawah. Coba tebak apa yang akan dicetak di layar sebelum melihat kunci jawaban di bawah!

```cpp
#include <iostream>
#include <vector>
#include <string>

class MesinTik {
private:
    std::string memori;
    int batasKarakter;

public:
    void inisialisasi(int batas) {
        batasKarakter = batas;
        memori = "";
    }

    void ketik(const std::string& teks) {
        if (memori.length() + teks.length() <= static_cast<size_t>(batasKarakter)) {
            memori += teks;
        } else {
            memori += "!";
        }
    }

    void cetak() const {
        std::cout << "Kertas: [" << memori << "]\n";
    }
};

int main() {
    MesinTik mesin;
    mesin.inisialisasi(6);

    std::vector<std::string> kata = {"Hi", " ", "C++", "!"};

    for (const auto& k : kata) {
        mesin.ketik(k);
    }

    mesin.cetak();
    return 0;
}
```

---

### Kunci Jawaban & Pembahasan

Output sebenarnya:
```text
Kertas: [Hi C++!]
```

**Penjelasan Langkah demi Langkah:**
1. Mesin diinisialisasi dengan `batasKarakter = 6` dan `memori = ""`.
2. Vektor memiliki 4 elemen teks: `{"Hi", " ", "C++", "!"}`.
3. Putaran 1: kata `"Hi"` (panjang 2). Panjang baru `0 + 2 = 2 <= 6`. Memori menjadi `"Hi"`.
4. Putaran 2: kata `" "` (panjang 1). Panjang baru `2 + 1 = 3 <= 6`. Memori menjadi `"Hi "`.
5. Putaran 3: kata `"C++"` (panjang 3). Panjang baru `3 + 3 = 6 <= 6`. Memori menjadi `"Hi C++"`.
6. Putaran 4: kata `"!"` (panjang 1). Panjang baru `6 + 1 = 7 > 6` (melebihi batas!). Kondisi `if` bernilai salah, sehingga cabang `else` dieksekusi: menambahkan karakter `"!"`.
7. Nilai akhir memori adalah `"Hi C++!"`. Panjang karakternya sekarang tepat 7 karakter dan dicetak dengan kurung siku: `[Hi C++!]`.

Selamat! Kamu telah menyelesaikan seluruh materi **Bagian 3: INTI**. Di bagian berikutnya (**Bagian 4: LANJUTAN**), kita akan menyelami pilar-pilar OOP yang sesungguhnya: konstruktor, pewarisan, polimorfisme virtual, dan smart pointer!
