---
id: py-fund-pendahuluan-3
courseId: py-fund-01
order: 7
title: "Variabel: Wadah Penyimpan Nilai di Memori"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Memahami konsep variabel sebagai label penunjuk data di memori komputer"
  - "Membuat variabel dan memberikan nilai awal dengan operator penugasan (=)"
  - "Memperbarui atau menimpa nilai variabel yang sudah ada"
codeLanguage: python
starterCode: |
  # Latihan Variabel: Wadah Penyimpan Nilai
  nama_siswa = "Rian"
  umur = 16
  bahasa_favorit = "Python"

  print("Profil Siswa:")
  print("Nama:", nama_siswa)
  print("Umur:", umur, "tahun")
  print("Bahasa Favorit:", bahasa_favorit)
playgroundTask: "Ubah nilai variabel 'nama_siswa' dan 'umur' dengan datamu sendiri, lalu buat satu variabel baru bernama 'target_belajar' dan cetak nilainya ke terminal!"
---
Bayangkan kamu memiliki beberapa kardus penyimpanan di kamarmu. Di luar setiap kardus, kamu menempelkan label stiker bertuliskan nama seperti *"Buku"*, *"Sepatu"*, atau *"Koleksi Koin"*.

Setiap kali kamu butuh sepatumu, kamu tidak perlu mencari ke seluruh sudut ruangan; kamu cukup melihat kardus berlabel *"Sepatu"*.

![Konsep Variabel sebagai Wadah Berlabel](asset:images/py-fund-01/variable-box-concept.png)

Dalam pemrograman, **variabel** bekerja persis seperti wadah berlabel tersebut. Variabel memberi nama pada data yang disimpan di memori komputer sehingga data itu bisa kamu panggil kembali kapan saja.

## Membuat Variabel Pertama

Untuk membuat variabel di Python, kamu cukup menulis nama variabel, diikuti tanda sama dengan (`=`), lalu nilainya:

```python
nama_pengguna = "Ahmad"
umur = 17
kota = "Surabaya"

print(nama_pengguna)
print(umur)
print(kota)
```

Tanda `=` di sini bukan berarti "sama dengan" dalam matematika, melainkan **operator penugasan** (*assignment operator*). Artinya: *"Simpan nilai di sebelah kanan ke dalam wadah variabel di sebelah kiri."*

## Mengubah Nilai Variabel

Sesuai namanya, variabel nilainya bisa bervariasi alias berubah sepanjang jalannya program:

```python
skor = 10
print("Skor mula-mula:", skor)

# Pemain mengumpulkan koin emas!
skor = 25
print("Skor sekarang:", skor)
```

Output:
```
Skor mula-mula: 10
Skor sekarang: 25
```

Ketika baris `skor = 25` dijalankan, nilai lama (`10`) digantikan dengan nilai baru (`25`).

> Variabel membuat kodemu dinamis dan fleksibel. Daripada menulis angka atau nama yang sama berulang-ulang, simpan nilainya dalam variabel!

Di modul selanjutnya, kita akan mempelajari aturan main penting saat memberi nama pada variabel agar kodemu rapi dan tidak memicu error.
