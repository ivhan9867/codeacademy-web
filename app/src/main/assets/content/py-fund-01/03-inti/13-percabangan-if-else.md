---
id: py-fund-inti-4
courseId: py-fund-01
order: 13
title: "Percabangan Keputusan: if & else"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami konsep percabangan alur logika program"
  - "Menulis struktur if dan else dengan lekukan blok kode (indentasi)"
  - "Menghindari IndentationError pada blok keputusan"
codeLanguage: python
starterCode: |
  # Latihan Percabangan if dan else
  saldo = 25000
  harga_kopi = 18000

  print("Memeriksa saldo belanja...")
  if saldo >= harga_kopi:
      print("Transaksi berhasil! Selamat menikmati kopi.")
      sisa_saldo = saldo - harga_kopi
      print(f"Sisa saldo Anda: Rp{sisa_saldo:,}")
  else:
      kekurangan = harga_kopi - saldo
      print(f"Saldo tidak cukup. Anda kurang Rp{kekurangan:,}")
playgroundTask: "Ubah nilai variabel 'saldo' menjadi 10000, lalu jalankan program kembali. Perhatikan bagaimana alur beralih mengeksekusi blok else!"
---
Dalam kehidupan sehari-hari, kita selalu mengambil keputusan bersyarat:
*"Jika hari ini hujan, saya bawa payung. Jika tidak hujan, saya tidak bawa payung."*

Dalam pemrograman, kemampuan memilih jalur tindakan berdasarkan kondisi ini disebut **percabangan (conditional branching)**.

![Diagram Alur Logika if else](asset:images/py-fund-01/if-else-flowchart.png)

## Struktur Sintaks `if` dan `else`

Di Python, percabangan ditulis menggunakan kata kunci `if` dan `else`, diakhiri dengan tanda titik dua (`:`), serta blok kode yang dimajukan (**indentasi** 4 spasi):

```python
saldo = 25000
harga_kopi = 18000

if saldo >= harga_kopi:
    print("Pembelian berhasil! Selamat menikmati kopi.")
    sisa = saldo - harga_kopi
    print(f"Sisa saldo Anda: Rp{sisa}")
else:
    print("Maaf, saldo Anda tidak mencukupi.")
    kekurangan = harga_kopi - saldo
    print(f"Anda kurang Rp{kekurangan}")
```

Perhatikan baris kode di bawah `if` dan `else`: baris-baris tersebut menjorok masuk ke dalam. Di Python, indentasi ini adalah **penanda blok**, bukan sekadar hiasan!

## Rahasia Indentasi Python

Bahasa pemrograman lain seperti C, Java, atau JavaScript menggunakan tanda kurung kurawal `{ ... }` untuk membungkus blok kode. Namun Python mewajibkan **spasi indentasi**:

```python
# BENAR:
if True:
    print("Ini masuk ke dalam blok if")

# SALAH (Memicu IndentationError):
if True:
print("Lupa menjorokkan spasi!")
```

> Standar komunitas Python merekomendasikan **4 spasi** untuk setiap tingkat indentasi. Sebagian besar editor modern otomatis menambahkan 4 spasi saat kamu menekan tombol Enter setelah tanda titik dua `:`.

Bagaimana jika ada lebih dari 2 pilihan, misalnya penilaian rapor A, B, C, D? Di modul berikutnya, kita akan berkenalan dengan penyelamat kita: `elif`!
