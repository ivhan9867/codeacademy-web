---
id: py-fund-lanjutan-5
courseId: py-fund-01
order: 22
title: "Menjinakkan Error: Blok try & except"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - "Memahami konsep Exception (eksepsi) runtime yang menyebabkan program berhenti mendadak"
  - "Menangkap dan menangani error secara ramah menggunakan blok try dan except"
  - "Menjaga stabilitas aplikasi agar tetap menyala meskipun terjadi input tak valid"
---
Bayangkan kamu sedang menggunakan aplikasi mobile perbankan. Saat kamu tidak sengaja mengetik huruf pada kolom nomor rekening, apakah kamu ingin aplikasinya tiba-tiba tertutup sendiri (*force close/crash*)? Tentu tidak! Kamu ingin aplikasi menampilkan pesan santun: *"Mohon masukkan angka saja ya."*

Dalam istilah teknis, kegagalan saat program sedang berjalan disebut **Exception (Eksepsi)**. Di Python, kita menjinakkannya menggunakan blok **`try`** dan **`except`**.

## Prinsip Kerja Sabuk Pengaman: `try` - `except`

Alurnya sangat manusiawi:
1. **`try`**: *"Python, coba jalankan baris-baris ini..."*
2. **`except`**: *"...tapi kalau di tengah jalan terjadi error tertentu, jangan panik dan jangan matikan program! Lakukan tindakan penyelamatan ini sebagai gantinya."*

Perhatikan contoh pembagian angka berikut:

```python
def bagi_uang(total_uang, jumlah_orang):
    try:
        jatah = total_uang / jumlah_orang
        print(f"Masing-masing mendapat: Rp{jatah:.0f}")
    except ZeroDivisionError:
        print("Peringatan: Jumlah orang tidak boleh 0!")

# Uji coba 1: Berjalan mulus
bagi_uang(100000, 4)

# Uji coba 2: Memicu ZeroDivisionError, namun ditangani dengan mulus!
bagi_uang(50000, 0)

print("Program selesai dengan selamat tanpa crash!")
```

Output:
```
Masing-masing mendapat: Rp25000
Peringatan: Jumlah orang tidak boleh 0!
Program selesai dengan selamat tanpa crash!
```

## Menangkap Beragam Jenis Error

Kamu juga bisa menangkap error konversi tipe data (`ValueError`):

```python
input_pengguna = "bukan_angka"

try:
    umur = int(input_pengguna)
    print(f"Umur Anda: {umur}")
except ValueError:
    print("Format salah! Masukkan angka bulat.")
```

> Penanganan error adalah ciri pembeda utama antara kode amatir yang rapuh dan perangkat lunak profesional yang tangguh (*robust*).

Selamat! Seluruh materi dasar hingga lanjutan kini telah kamu kantongi. Di bagian penutup (**Evaluasi**), kita akan menggabungkan semuanya dalam sebuah **Mini Proyek Kasir**!
