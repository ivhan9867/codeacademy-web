---
id: py-fund-inti-7
courseId: py-fund-01
order: 16
title: "Perulangan: for loop & Fungsi range()"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami konsep perulangan untuk mengotomatisasi tugas repetitif"
  - "Melakukan iterasi pada elemen koleksi list menggunakan for item in koleksi"
  - "Menghasilkan rentang bilangan berurutan menggunakan fungsi range()"
codeLanguage: python
starterCode: |
  # Latihan Perulangan for dan range()
  kategori_teknologi = ["Web", "Data Science", "Kecerdasan Buatan", "Otomasi"]

  print("Daftar Bidang Spesialisasi:")
  for index, bidang in enumerate(kategori_teknologi, start=1):
      print(f"{index}. Python untuk {bidang}")

  print("\nHitung mundur peluncuran roket:")
  for detik in range(5, 0, -1):
      print(f"Mulai dalam {detik}...")
  print("Roket meluncur ke angkasa! 🚀")
playgroundTask: "Tambahkan satu bidang teknologi baru ke dalam list 'kategori_teknologi', lalu ubah hitungan mundur range() agar dimulai dari 10 detik!"
---
Salah satu keunggulan terbesar komputer dibanding manusia adalah: komputer tidak pernah bosan atau lelah melakukan tugas yang sama ribuan kali.

Jika kamu disuruh mencetak kalimat salam sebanyak 100 kali, menulis 100 baris `print()` tentu sangat melelahkan. Dengan **perulangan (loop)**, kamu cukup menuliskannya dalam 2 baris saja!

## Mengulang Setiap Elemen di List

Bentuk perulangan paling alami di Python adalah membaca item list satu per satu:

```python
siswa_bimbingan = ["Dewi", "Rafi", "Siti", "Budi"]

for nama in siswa_bimbingan:
    print(f"Halo {nama}, selamat datang di kelas Python!")
```

Output:
```
Halo Dewi, selamat datang di kelas Python!
Halo Rafi, selamat datang di kelas Python!
Halo Siti, selamat datang di kelas Python!
Halo Budi, selamat datang di kelas Python!
```

Python mengambil item pertama (`"Dewi"`), memasukkannya ke variabel penampung sementara `nama`, lalu menjalankan blok kode di bawahnya. Setelah selesai, Python otomatis mengambil item berikutnya sampai daftar habis.

## Mengulang Angka dengan `range()`

Bagaimana jika kita ingin mengulang sesuatu sebanyak $N$ kali? Gunakan fungsi bawaan `range()`:

```python
# Mengulang angka 1 sampai 5:
for hitungan in range(1, 6):
    print(f"Putaran ke-{hitungan}")
```

Perhatikan: `range(1, 6)` berarti mulai dari angka 1 dan berhenti **sebelum** angka 6 (yaitu 1, 2, 3, 4, 5).

Bila kamu hanya menulis satu angka, misalnya `range(3)`, hitungan dimulai dari 0:
```python
for i in range(3):
    print(i) # Mencetak 0, 1, 2
```

> Kekuatan sejati pemrograman dimulai ketika kamu menggabungkan perulangan dengan kondisi `if` di dalamnya!

Di modul berikutnya, kita akan membahas saudara kembar perulangan `for`, yaitu perulangan **`while`** yang berjalan selama kondisi masih terpenuhi.
