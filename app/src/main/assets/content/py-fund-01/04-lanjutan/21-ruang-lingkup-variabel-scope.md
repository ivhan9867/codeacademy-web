---
id: py-fund-lanjutan-4
courseId: py-fund-01
order: 21
title: "Ruang Lingkup Variabel: Lokal vs Global"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - "Membedakan variabel lokal (di dalam fungsi) dan variabel global (di luar fungsi)"
  - "Memahami siklus hidup variabel lokal yang musnah setelah fungsi selesai berjalan"
  - "Menghindari efek samping tidak terduga akibat modifikasi variabel global"
---
Bayangkan sebuah rumah tangga. Ada barang milik bersama di ruang keluarga (misalnya televisi), dan ada barang pribadi di dalam kamar masing-masing (misalnya buku harian). Siapa pun di rumah bisa menonton TV, tetapi buku harian di kamar hanya bisa dibaca oleh pemilik kamar tersebut.

Dalam pemrograman Python, konsep kepemilikan dan batas wilayah akses ini disebut **Scope (Ruang Lingkup)**.

![Visualisasi Ruang Lingkup Variabel Lokal dan Global](asset:images/py-fund-01/variable-scope.png)

## Variabel Global (Milik Seluruh Program)

Variabel yang didefinisikan di luar semua fungsi disebut **Variabel Global**. Variabel ini bisa dibaca dari bagian mana pun dalam file kodemu:

```python
nama_sekolah = "SMA Bintang Bangsa"  # Global

def info_sekolah():
    print(f"Siswa terdaftar di {nama_sekolah}")

info_sekolah()
```

## Variabel Lokal (Milik Internal Fungsi)

Variabel yang dibuat di dalam blok fungsi adalah **Variabel Lokal**. Variabel ini lahir saat fungsi dipanggil, dan **musnah dari memori** begitu fungsi selesai dijalankan:

```python
def hitung_diskon():
    potongan_harga = 10000  # Lokal
    print(f"Potongan: Rp{potongan_harga}")

hitung_diskon()

# COBA PANGGIL DARI LUAR FUNGSI:
# print(potongan_harga)
# -> NameError: name 'potongan_harga' is not defined!
```

Dunia luar tidak tahu apa pun tentang variabel `potongan_harga` karena ruang lingkupnya terkunci di dalam fungsi `hitung_diskon`.

> Mengapa aturan ini penting? Isolasi lokal melindungi kodemu! Kamu bisa membuat variabel dengan nama umum (seperti `i`, `total`, `hasil`) di dalam puluhan fungsi berbeda tanpa khawatir saling bertabrakan atau merusak nilai satu sama lain.

Di modul berikutnya, kita akan belajar teknik penting menjaga agar program tidak mendadak mogok (*crash*) saat pengguna memasukkan data salah: **Penanganan Error dengan try-except**!
