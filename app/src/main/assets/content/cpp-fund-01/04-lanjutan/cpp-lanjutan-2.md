---
id: cpp-lanjutan-2
courseId: cpp-fund-01
order: 18
title: "Kelahiran & Kematian Objek: Konstruktor dan Destruktor (RAII)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Menguasai pembuatan Konstruktor berparameter menggunakan Member Initializer List
  - Menguasai Destruktor (~NamaKelas()) untuk membersihkan sumber daya secara otomatis
  - Memahami prinsip RAII (Resource Acquisition Is Initialization) dan urutan kelahiran serta kematian objek pada stack
codeLanguage: cpp
codeSnippet: |
  #include <iostream>
  #include <string>

  class Kucing {
  public:
      std::string nama;
      Kucing(const std::string& n) : nama(n) {
          std::cout << nama << " lahir!\n";
      }
      ~Kucing() {
          std::cout << nama << " tidur.\n";
      }
  };

  int main() {
      Kucing k("Mimi");
      return 0;
  }
---

# Kelahiran & Kematian Objek: Konstruktor dan Destruktor (RAII)

Di dunia nyata, setiap benda memiliki siklus hidup: saat dirakit dan saat didaur ulang.

Di bahasa C, jika kamu membuat struct yang membutuhkan alokasi memori, kamu harus mengingat dua hal: memanggil fungsi inisialisasi manual di awal, dan memanggil `free()` manual di akhir. Jika lupa memanggil fungsi pembersih, terjadilah kebocoran memori (*memory leak*).

C++ mengotomatiskan seluruh siklus ini melalui sepasang fungsi spesial: **Konstruktor (*Constructor*)** dan **Destruktor (*Destructor*)**.

---

## 1. Konstruktor: Upacara Kelahiran Objek

Konstruktor adalah fungsi anggota yang **otomatis dipanggil saat objek pertama kali diciptakan di memori**.
Ciri khasnya:
- Memiliki **nama yang sama persis dengan nama kelasnya**.
- **Tidak memiliki tipe nilai balik (*no return type*)**, bahkan tidak boleh diberi `void`.

### Modern C++: Member Initializer List
Standar industri modern mengharuskan kita menginisialisasi atribut kelas menggunakan **Member Initializer List** (ditulis setelah tanda titik dua `:` sebelum kurung kurawal `{}`):

```cpp
#include <iostream>
#include <string>

class Mahasiswa {
private:
    std::string nama;
    int nim;

public:
    // Konstruktor dengan Member Initializer List
    Mahasiswa(const std::string& n, int id)
        : nama(n), nim(id) // Menginisialisasi atribut langsung saat lahir
    {
        std::cout << "Objek mahasiswa " << nama << " berhasil dibuat.\n";
    }

    void perkenalkan() const {
        std::cout << "Halo, saya " << nama << " (NIM: " << nim << ")\n";
    }
};

int main() {
    // Objek langsung lahir dalam keadaan valid dan siap pakai
    Mahasiswa m1("Dina", 1024);
    m1.perkenalkan();

    return 0;
}
```

Output:
```text
Objek mahasiswa Dina berhasil dibuat.
Halo, saya Dina (NIM: 1024)
```

---

## 2. Destruktor: Upacara Pembersihan Otomatis

Destruktor adalah fungsi anggota yang **otomatis dipanggil saat objek keluar dari cakupan hidupnya (*out of scope*) atau dihancurkan**.
Ciri khasnya:
- Diawali dengan tanda tilde **`~`** diikuti nama kelas (misal `~Mahasiswa()`).
- Tidak menerima parameter apa pun dan tidak memiliki nilai balik.

Inilah inti dari prinsip paling agung di C++: **RAII (*Resource Acquisition Is Initialization*)**. Jika sebuah objek membuka berkas atau mengunci memori saat lahir di konstruktor, berkas dan memori tersebut **dijamin 100% otomatis ditutup dan dibersihkan saat objek mati di destruktor**, tanpa kamu harus repot mengetik perintah bersih-bersih manual!

---

## Latihan "Tebak Outputnya": Urutan Konstruksi & Destruksi

Objek di stack bekerja berdasarkan prinsip tumpukan piring (**LIFO: *Last In, First Out***):
Objek yang **paling terakhir lahir** akan menjadi objek yang **paling pertama mati**.

Perhatikan kode berikut dan tebak urutan kalimat yang dicetak:

```cpp
#include <iostream>
#include <string>

class Balok {
private:
    std::string warna;
public:
    Balok(const std::string& w) : warna(w) {
        std::cout << "[Lahir] Balok " << warna << "\n";
    }
    ~Balok() {
        std::cout << "[Mati]  Balok " << warna << "\n";
    }
};

int main() {
    std::cout << "--- Mulai main ---\n";
    Balok b1("Merah");
    {
        Balok b2("Hijau");
        Balok b3("Biru");
        std::cout << "--- Keluar blok kurung kurawal ---\n";
    }
    std::cout << "--- Akhir main ---\n";
    return 0;
}
```

---

### Kunci Jawaban & Pembahasan

Output sebenarnya:
```text
--- Mulai main ---
[Lahir] Balok Merah
[Lahir] Balok Hijau
[Lahir] Balok Biru
--- Keluar blok kurung kurawal ---
[Mati]  Balok Biru
[Mati]  Balok Hijau
--- Akhir main ---
[Mati]  Balok Merah
```

**Penjelasan Alur Mental Execution:**
1. `b1` ("Merah") lahir di cakupan `main()`.
2. Masuk ke blok dalam `{}`: `b2` ("Hijau") lahir, lalu `b3` ("Biru") lahir di atas tumpukan stack.
3. Mencetak kalimat *"Keluar blok kurung kurawal"*.
4. Begitu mencapai tanda penutup `}`, masa hidup blok dalam berakhir:
   - `b3` ("Biru") paling terakhir lahir, sehingga ia mati **pertama kali**.
   - `b2` ("Hijau") mati berikutnya.
5. Program mencetak *"Akhir main"*.
6. Fungsi `main()` selesai, barulah `b1` ("Merah") mati terakhir sebelum program ditutup.

Di modul berikutnya, kita akan menyelidiki bagaimana objek mengenali identitas dirinya sendiri menggunakan **Pointer `this`**!
