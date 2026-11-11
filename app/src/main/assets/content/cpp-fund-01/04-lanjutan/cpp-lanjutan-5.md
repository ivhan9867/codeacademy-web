---
id: cpp-lanjutan-5
courseId: cpp-fund-01
order: 21
title: "Identitas Jamak: Overriding, Fungsi virtual & Polimorfisme Runtime"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Memahami fenomena Static Binding dan mengapa pointer kelas induk membutuhkan fungsi virtual
  - Menguasai kata kunci virtual pada kelas induk dan kata kunci override (C++11) pada kelas anak
  - Memahami cara kerja tabel virtual (vtable) dalam mewujudkan Polimorfisme Runtime
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  class Hewan {
  public:
      virtual void suara() const {
          std::cout << "Suara hewan umum\n";
      }
      virtual ~Hewan() = default;
  };

  class Kucing : public Hewan {
  public:
      void suara() const override {
          std::cout << "Meong!\n";
      }
  };

  int main() {
      Kucing c;
      Hewan* h = &c;
      h->suara(); // "Meong!" berkat kata kunci virtual
      return 0;
  }
---

# Identitas Jamak: Overriding, Fungsi virtual & Polimorfisme Runtime

Kata **Polimorfisme** berasal dari bahasa Yunani: *Poly* (banyak) dan *Morph* (bentuk). Di dunia pemrograman, polimorfisme adalah kemampuan untuk memperlakukan berbagai objek dari kelas-kelas turunan yang berbeda seolah-olah mereka adalah satu jenis kelas induk yang sama, namun masing-masing tetap menunjukkan perilaku aslinya yang unik.

Mari kita lihat sebuah jebakan klasik jika kita tidak menggunakan kata kunci **`virtual`**.

---

## Masalah: Pengikatan Dini (*Static Binding*)

Perhatikan apa yang terjadi jika kita menggunakan pointer kelas induk tanpa `virtual`:

```cpp
class Musuh {
public:
    void serang() const {
        std::cout << "Serangan musuh standar!\n";
    }
};

class Naga : public Musuh {
public:
    void serang() const {
        std::cout << "Semburan api naga membakar!\n";
    }
};
```

Jika kamu menulis:
```cpp
Naga n;
Musuh* pMusuh = &n;
pMusuh->serang(); // Output: "Serangan musuh standar!" (BUKAN suara naga!)
```

Mengapa pointer `pMusuh` memanggil fungsi milik `Musuh`, padahal objek nyata di memori adalah seekor `Naga`?
Karena secara default, C++ menggunakan **Static Binding (Pengikatan Statis saat Kompilasi)**: kompiler hanya melihat tipe pointernya (`Musuh*`), bukan benda nyata apa yang ditunjuknya!

---

## Solusi: Kata Kunci `virtual` dan `override`

Untuk memberi tahu kompiler: *"Tolong jangan putuskan sekarang! Periksa wujud objek aslinya saat program berjalan di memori!"*, kita menambahkan kata kunci **`virtual`** di kelas induk:

```cpp
#include <iostream>

class Musuh {
public:
    // Menandai fungsi sebagai virtual agar diperiksa secara dinamis saat runtime
    virtual void serang() const {
        std::cout << "Serangan musuh standar!\n";
    }

    // Selalu buat destruktor virtual jika kelasmu memiliki fungsi virtual!
    virtual ~Musuh() = default;
};

class Naga : public Musuh {
public:
    // Kata kunci override memastikan ejaan fungsinya cocok persis dengan induk
    void serang() const override {
        std::cout << "Semburan api naga membakar!\n";
    }
};

class MonsterLendir : public Musuh {
public:
    void serang() const override {
        std::cout << "Cipratan lendir beracun!\n";
    }
};

int main() {
    Naga smaug;
    MonsterLendir slime;

    // Kumpulan musuh disimpan dalam array pointer kelas induk Musuh*
    Musuh* daftarMusuh[] = {&smaug, &slime};

    for (Musuh* m : daftarMusuh) {
        m->serang(); // Polimorfisme ajaib terjadi di sini!
    }

    return 0;
}
```

Output:
```text
Semburan api naga membakar!
Cipratan lendir beracun!
```

---

## Bagaimana Cara Kerjanya? (vtable & vptr)

Bagaimana C++ bisa tahu bahwa `daftarMusuh[0]` adalah Naga dan `daftarMusuh[1]` adalah Slime?

![Mekanisme Tabel Virtual vtable](asset:images/cpp-fund-01/mekanisme-vtable-polimorfisme.png)

1. Begitu sebuah kelas memiliki minimal satu fungsi `virtual`, kompiler diam-diam membuatkan sebuah tabel fungsi rahasia bernama **vtable (*Virtual Method Table*)**.
2. Setiap objek kelas tersebut akan disisipi pointer tersembunyi bernama **vptr (*virtual table pointer*)**.
3. Saat baris `m->serang()` dipanggil, CPU membaca pointer `vptr` milik objek tersebut, melirik tabel `vtable` Naga, dan melompat ke alamat fungsi `Naga::serang()`. Proses penelusuran dinamis saat runtime ini disebut **Dynamic Dispatch**.

---

## Latihan "Tebak Outputnya": Virtual vs Non-Virtual

Uji penalaran mentalmu pada perbedaan fungsi virtual dan non-virtual berikut:

```cpp
#include <iostream>

class Induk {
public:
    virtual void fungsiA() { std::cout << "Induk: A\n"; }
    void fungsiB()          { std::cout << "Induk: B\n"; }
};

class Anak : public Induk {
public:
    void fungsiA() override { std::cout << "Anak: A\n"; }
    void fungsiB()          { std::cout << "Anak: B\n"; }
};

int main() {
    Anak obj;
    Induk* ptr = &obj;

    ptr->fungsiA();
    ptr->fungsiB();

    return 0;
}
```

---

### Kunci Jawaban & Pembahasan

Output sebenarnya:
```text
Anak: A
Induk: B
```

**Penjelasan Alur Mental Execution:**
1. `ptr` adalah pointer bertipe `Induk*` yang menunjuk ke objek nyata `Anak`.
2. Saat `ptr->fungsiA()` dipanggil: karena `fungsiA` dideklarasikan sebagai **`virtual`**, terjadi *dynamic dispatch* menelusuri vtable objek `Anak`. Versi yang dieksekusi adalah **`Anak: A`**.
3. Saat `ptr->fungsiB()` dipanggil: karena `fungsiB` **TIDAK memiliki kata kunci virtual**, C++ langsung menggunakan *static binding*. Kompiler hanya melihat tipe pointernya (`Induk*`), sehingga yang dieksekusi adalah **`Induk: B`**!

Di modul berikutnya, kita akan mempelajari bentuk tertinggi polimorfisme: **Pure Virtual Function dan Kelas Abstrak (Interface)**!
