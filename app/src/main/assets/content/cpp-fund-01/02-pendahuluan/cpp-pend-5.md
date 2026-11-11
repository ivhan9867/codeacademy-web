---
id: cpp-pend-5
courseId: cpp-fund-01
order: 8
title: "Alias Memori: Mengenal Referensi (&) dan Bedanya dengan Pointer C"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - Memahami konsep Referensi C++ sebagai alias langsung (nama panggilan alternatif) bagi sel memori yang sudah ada
  - Membedakan secara komprehensif antara Referensi C++ dan Pointer C klasik
  - Menguasai dua aturan mutlak referensi: wajib diinisialisasi saat lahir dan tidak dapat dialihkan (cannot rebind)
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  int main() {
      int skor = 10;
      int& aliasSkor = skor; // aliasSkor adalah nama lain untuk skor
      aliasSkor = 25;
      std::cout << "Nilai skor asli: " << skor << "\n";
      return 0;
  }
---

# Alias Memori: Mengenal Referensi (&) dan Bedanya dengan Pointer C

Di kelas C Fundamentals, kamu sudah belajar bahwa untuk memanipulasi data tanpa menyalinnya, kita menggunakan **Pointer** (`int* ptr = &x;`). Namun, pointer menuntut sintaks yang cerewet: kamu harus dereference dengan tanda bintang `*ptr`, waspada terhadap pointer liar (*dangling pointer*), dan selalu mengecek apakah nilainya `NULL`.

C++ memperkenalkan konsep baru yang jauh lebih bersih, aman, dan indah: **Referensi (`&`)**.

![Perbandingan Pointer C dan Referensi C++](asset:images/cpp-fund-01/perbandingan-pointer-referensi.png)

---

## Analogi Nama Panggilan Resmi (Alias)

Bayangkan seorang teman bernama lengkap **"Alexander"**. Di rumah atau di tongkrongan, teman-temannya memanggilnya dengan nama panggilan **"Alex"**.

Apakah "Alexander" dan "Alex" adalah dua orang yang berbeda? **Tentu saja tidak!** Keduanya adalah dua nama berbeda yang menunjuk pada **satu raga fisik manusia yang sama persis**. Jika kamu mentraktir "Alex" semangkuk bakso, perut "Alexander" yang kenyang.

Inilah cara kerja Referensi di C++:

```cpp
#include <iostream>

int main() {
    int skor = 10;
    int& aliasSkor = skor; // aliasSkor adalah referensi (alias) bagi skor

    std::cout << "Skor awal: " << skor << "\n";

    // Mengubah nilai melalui nama alias
    aliasSkor = 95;

    std::cout << "Skor setelah diubah lewat alias: " << skor << "\n";
    std::cout << "Nilai aliasSkor: " << aliasSkor << "\n";

    return 0;
}
```

Output:
```text
Skor awal: 10
Skor setelah diubah lewat alias: 95
Nilai aliasSkor: 95
```

---

## Tiga Perbedaan Fundamental: Pointer vs Referensi

Meskipun di tingkat instruksi assembly compiler keduanya sama-sama memanfaatkan alamat memori, dari sudut pandang aturan bahasa C++, keduanya sangat berbeda:

| Aspek | Pointer (Bahasa C) | Referensi (Bahasa C++) |
|:---|:---|:---|
| **Definisi Fisik** | Variabel tersendiri yang menyimpan angka alamat memori. | Bukan variabel terpisah, melainkan sekadar **nama alias** untuk variabel yang sudah ada. |
| **Inisialisasi Awal** | Boleh tidak diisi nilai awal (`int* p;`), atau diisi `nullptr`. | **Wajib langsung diinisialisasi** saat dideklarasikan (`int& r = x;`). |
| **Bisa Berganti Target?** | **Bisa**. Pointer bisa diarahkan ke alamat lain kapan saja (`p = &y;`). | **Tidak bisa (Immutable)**. Sekali terikat pada `x`, ia akan selamanya menjadi alias bagi `x`. |
| **Sintaks Akses** | Butuh operator dereference `*p` untuk membaca atau menulis nilai. | Menggunakan nama biasa tanpa tanda bintang (`r = 50;`). |

Perhatikan contoh penting berikut:
```cpp
int a = 10;
int b = 20;

int& ref = a; // ref terikat permanen pada a
ref = b;      // INI BUKAN memindahkan ref ke b! 
              // Baris ini menyalin nilai b (20) ke dalam variabel a!

std::cout << a << "\n"; // Output: 20
```

Pemahaman tentang referensi ini adalah fondasi paling penting sebelum kita masuk ke materi berikutnya: bagaimana fungsi di C++ melewatkan data tanpa menyalin (*Pass-by-Reference*)!
