---
id: cpp-prep-2
courseId: cpp-fund-01
order: 2
title: "Peta Materi: Roadmap 5 Bagian C++ Fundamentals"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - Memahami kurikulum 5 tahapan terstruktur dari C++ Fundamentals
  - Mengetahui keterkaitan fitur baru C++ (referensi, STL, template) dengan pilar OOP
  - Menyiapkan diri menghadapi transisi logika dari pemikiran prosedural ke pemikiran modular
codeLanguage: cpp
codeSnippet: |
  #include <iostream>

  int main() {
      // 5 Pilar Utama: Persiapan, Dasar, Fungsi/STL/Class, OOP Modern, Evaluasi
      std::cout << "Jalur Belajar C++ Fundamentals Siap Dimulai!\n";
      return 0;
  }
---

# Peta Materi: Roadmap 5 Bagian C++ Fundamentals

Menguasai C++ bukan sekadar menghafal sintaks baru, melainkan mengubah cara kita mengabstraksikan masalah di dunia nyata ke dalam kode program yang terstruktur rapi dan tahan banting.

Kurikulum ini dirancang bertahap dalam **5 Bagian Utama (28 Modul)**:

```
[01. Persiapan] ──> [02. Dasar-Dasar C++] ──> [03. Fungsi, STL & Class]
                                                            │
[05. Proyek Akhir] <── [04. OOP & Fitur Modern] <───────────┘
```

---

## Rincian 5 Bagian Belajar

### Bagian 1: Persiapan (Modul 1 – 3)
Menyelaraskan pola pikir belajar, memahami roadmap kurikulum, dan panduan opsional mengompilasi kode C++17 menggunakan `g++` di komputermu sendiri.

### Bagian 2: Dasar-Dasar C++ (Modul 4 – 8)
Mengenal peningkatan pertama dari C ke C++:
- Mengganti input/output lama dengan aliran stream `std::cout` dan `std::cin`.
- Merapikan ruang nama (*namespace*) menggunakan `std::`.
- Menggunakan kelas teks dinamis `std::string` dan deduksi tipe otomatis dengan `auto`.
- Memahami konsep **Referensi (`&`)** sebagai alias langsung pada memori fisik dan perbedaannya dengan pointer C.

### Bagian 3: Fungsi, STL & Class (Modul 9 – 16)
Memperkaya logika modular:
- **Function Overloading**: Memberikan nama fungsi yang sama untuk tipe data berbeda.
- **Default Arguments & Pass-by-Reference**: Menghindari salinan memori yang mahal dengan `const T&`.
- **Pengantar STL (`std::vector`)**: Meninggalkan manajemen memori manual array C dan beralih ke wadah dinamis otomatis.
- **Range-Based For Loop**: Menulis perulangan bersih ala bahasa modern.
- **Transisi struct ke class**: Membedah perbedaan hak akses `public` dan `private` untuk mencapai enkapsulasi data.

### Bagian 4: OOP & Fitur Modern (Modul 17 – 26)
Jantung kekuatan C++ modern:
- **Class & Object**: Membangun cetakan data dan instansiasinya.
- **Siklus Hidup Objek**: Memahami konstruktor (*member initializer list*) dan destruktor dengan prinsip **RAII**.
- **Pointer `this`**: Mengenali identitas objek internal.
- **Pewarisan (Inheritance)**: Menghubungkan kelas induk dan kelas anak melalui hak akses `protected`.
- **Polimorfisme Runtime**: Menyelesaikan misteri fungsi `virtual`, `override`, dan tabel virtual (*vtable*).
- **Kelas Abstrak & Interface**: Merancang kontrak arsitektur sistem (`= 0`).
- **Operator Overloading & Template**: Membuat fungsi dan kelas generik serbaguna.
- **Smart Pointer (`std::unique_ptr` & `std::shared_ptr`)**: Mengelola heap tanpa rasa takut terhadap kebocoran memori (*memory leak*).
- **Exception Handling**: Menangani error secara anggun dengan `try`, `catch`, dan `throw`.

### Bagian 5: Proyek & Evaluasi (Modul 27 – 28)
Mengintegrasikan seluruh konsep ke dalam satu **Mini Proyek OOP: Sistem Perpustakaan Sederhana**, diikuti rangkuman arsitektur perangkat lunak C++ modern.

Mari lanjutkan ke modul ketiga untuk melihat bagaimana kode C++17 dikompilasi secara manual di komputer!
