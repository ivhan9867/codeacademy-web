---
id: cpp-eval-1
courseId: cpp-fund-01
order: 27
title: "Mini Proyek OOP: Merancang Sistem Perpustakaan Sederhana"
readingMinutes: 7
section: EVALUASI
learningObjectives:
  - Mengintegrasikan seluruh pilar OOP (Kelas Abstrak, Pewarisan, Polimorfisme Runtime, dan Smart Pointer)
  - Mengelola koleksi heterogen menggunakan wadah std::vector berisi std::unique_ptr
  - Membangun arsitektur perangkat lunak C++ yang modular, aman dari kebocoran memori, dan mudah dikembangkan
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <vector>
  #include <memory>
  #include <string>

  class Item {
  public:
      virtual void info() const = 0;
      virtual ~Item() = default;
  };

  class Buku : public Item {
  private:
      std::string judul;
  public:
      Buku(const std::string& j) : judul(j) {}
      void info() const override {
          std::cout << "Buku: " << judul << "\n";
      }
  };

  int main() {
      std::vector<std::unique_ptr<Item>> daftar;
      daftar.push_back(std::make_unique<Buku>("Belajar C++"));
      for (const auto& item : daftar) {
          item->info();
      }
      return 0;
  }
---

# Mini Proyek OOP: Merancang Sistem Perpustakaan Sederhana

Saatnya menyatukan seluruh pengetahuan yang telah kamu pelajari selama 26 modul terakhir!

Kita akan merancang sebuah **Sistem Manajemen Inventaris Perpustakaan Modern** yang menerapkan:
1. **Kelas Abstrak (`ItemKoleksi`)**: Berfungsi sebagai interface dengan fungsi virtual murni `tampilkanDetail() = 0`.
2. **Pewarisan (*Inheritance*)**: Kelas `Buku` dan `Majalah` mewarisi sifat dasar `ItemKoleksi`.
3. **Smart Pointer (`std::unique_ptr`)**: Mengelola kepemilikan memori heap tanpa perlu menulis `delete` manual.
4. **Wadah STL (`std::vector`)**: Menyimpan daftar koleksi yang dapat menampung buku dan majalah sekaligus secara polimorfis (*Heterogeneous Collection*).
5. **Polimorfisme Runtime**: Menelusuri seluruh inventaris dan memanggil metode yang tepat untuk setiap jenis item.

---

## Kode Lengkap Sistem Perpustakaan (Standar C++17)

Perhatikan bagaimana seluruh komponen dirangkai secara bersih dan elegan:

```cpp
#include <iostream>
#include <vector>
#include <memory>
#include <string>

// 1. Kelas Abstrak (Interface Dasar)
class ItemKoleksi {
protected:
    std::string judul;
    int tahunTerbit;

public:
    ItemKoleksi(const std::string& j, int thn)
        : judul(j), tahunTerbit(thn) {}

    // Destruktor virtual wajib untuk polimorfisme yang aman
    virtual ~ItemKoleksi() = default;

    // Pure Virtual Function yang wajib diisi oleh kelas turunan
    virtual void tampilkanDetail() const = 0;

    const std::string& getJudul() const {
        return judul;
    }
};

// 2. Kelas Turunan: Buku
class Buku : public ItemKoleksi {
private:
    std::string penulis;
    int jumlahHalaman;

public:
    Buku(const std::string& j, int thn, const std::string& p, int hal)
        : ItemKoleksi(j, thn), penulis(p), jumlahHalaman(hal) {}

    void tampilkanDetail() const override {
        std::cout << "[Buku]    " << judul 
                  << " (" << tahunTerbit << ")"
                  << " | Penulis: " << penulis 
                  << " | Tebal: " << jumlahHalaman << " hal.\n";
    }
};

// 3. Kelas Turunan: Majalah
class Majalah : public ItemKoleksi {
private:
    int edisiNomor;

public:
    Majalah(const std::string& j, int thn, int edisi)
        : ItemKoleksi(j, thn), edisiNomor(edisi) {}

    void tampilkanDetail() const override {
        std::cout << "[Majalah] " << judul 
                  << " (" << tahunTerbit << ")"
                  << " | Edisi Ke-" << edisiNomor << "\n";
    }
};

// 4. Kelas Pengelola Sistem Perpustakaan
class Perpustakaan {
private:
    // Menyimpan koleksi unik polimorfis secara aman di heap
    std::vector<std::unique_ptr<ItemKoleksi>> daftarKoleksi;

public:
    void tambahItem(std::unique_ptr<ItemKoleksi> item) {
        // std::move digunakan untuk memindahkan kepemilikan unique_ptr
        daftarKoleksi.push_back(std::move(item));
    }

    void tampilkanSemuaKoleksi() const {
        std::cout << "========================================\n";
        std::cout << "     KATALOG KOLEKSI PERPUSTAKAAN       \n";
        std::cout << "========================================\n";

        for (const auto& item : daftarKoleksi) {
            // Polimorfisme bekerja otomatis memanggil versi Buku atau Majalah!
            item->tampilkanDetail();
        }

        std::cout << "========================================\n";
        std::cout << "Total koleksi tersimpan: " << daftarKoleksi.size() << " item\n";
    }
};

int main() {
    Perpustakaan perpus;

    // Menambahkan berbagai jenis koleksi ke perpustakaan
    perpus.tambahItem(std::make_unique<Buku>(
        "C++ Primer", 2012, "Stanley Lippman", 976
    ));

    perpus.tambahItem(std::make_unique<Majalah>(
        "Linux Journal", 2020, 301
    ));

    perpus.tambahItem(std::make_unique<Buku>(
        "The C++ Programming Language", 2013, "Bjarne Stroustrup", 1368
    ));

    // Menampilkan seluruh katalog perpustakaan
    perpus.tampilkanSemuaKoleksi();

    return 0; // Seluruh memori heap dibebaskan otomatis tanpa kebocoran!
}
```

Output:
```text
========================================
     KATALOG KOLEKSI PERPUSTAKAAN       
========================================
[Buku]    C++ Primer (2012) | Penulis: Stanley Lippman | Tebal: 976 hal.
[Majalah] Linux Journal (2020) | Edisi Ke-301
[Buku]    The C++ Programming Language (2013) | Penulis: Bjarne Stroustrup | Tebal: 1368 hal.
========================================
Total koleksi tersimpan: 3 item
```

---

## Analisis Keunggulan Arsitektur:
1. **Koleksi Polimorfis (`std::vector<std::unique_ptr<ItemKoleksi>>`)**: Satu vektor dapat menampung objek `Buku` dan `Majalah` secara berdampingan tanpa terkena *Object Slicing*.
2. **Nol Kebocoran Memori**: Berkat `std::unique_ptr`, ketika objek `perpus` selesai bertugas di akhir fungsi `main()`, seluruh objek `Buku` dan `Majalah` di heap otomatis dihancurkan dengan aman.
3. **Mudah Diperluas (*Extensible*)**: Jika besok kamu ingin menambahkan kelas `DVDDigital` atau `JurnalIlmiah`, kamu cukup membuat satu kelas baru yang mengimplementasikan `tampilkanDetail()`. Kelas `Perpustakaan` sama sekali tidak perlu diubah!

Di modul terakhir, kita akan merangkum seluruh fondasi C++ ini dan menyusun peta jalan karirmu sebagai Software Engineer C++ profesional!
