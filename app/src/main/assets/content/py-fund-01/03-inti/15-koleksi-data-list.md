---
id: py-fund-inti-6
courseId: py-fund-01
order: 15
title: "Koleksi Data: List, Indeks Positif & Negatif"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Membuat list dengan kurung siku [] untuk menampung banyak item berurutan"
  - "Mengakses elemen menggunakan indeks berbasis nol (0-based indexing) dan indeks negatif"
  - "Memodifikasi isi list serta menambah data dengan metode .append()"
---
Bayangkan kamu punya daftar belanjaan: telur, susu, roti, dan mentega. Daripada membuat 4 variabel terpisah:
```python
barang1 = "telur"
barang2 = "susu"
barang3 = "roti"
barang4 = "mentega"
```
Bukankah lebih praktis jika kamu bisa menaruh semuanya ke dalam satu wadah keranjang belanja?

Itulah fungsi **List** di Python!

![Konsep Indeks Positif dan Negatif pada List Python](asset:images/py-fund-01/list-indexing.png)

## Membuat List Pertama

List dibuat menggunakan tanda kurung siku `[...]`, dengan setiap item dipisahkan oleh tanda koma:

```python
daftar_belanja = ["telur", "susu", "roti", "mentega"]
print(daftar_belanja)
```

## Mengakses Item dengan Indeks

Setiap barang di dalam list memiliki nomor urut posisi yang disebut **indeks**.

> Di dunia pemrograman komputer, penghitungan selalu dimulai dari angka **0**, bukan 1!

- `daftar_belanja[0]` ➔ `"telur"` (item pertama)
- `daftar_belanja[1]` ➔ `"susu"` (item kedua)
- `daftar_belanja[2]` ➔ `"roti"` (item ketiga)

### Fitur Keren Python: Indeks Negatif!

Python memiliki fitur unik di mana kamu bisa menghitung dari urutan paling belakang menggunakan angka negatif:
- `daftar_belanja[-1]` ➔ `"mentega"` (item paling terakhir)
- `daftar_belanja[-2]` ➔ `"roti"` (item kedua dari belakang)

## Mengubah dan Menambah Isi List

List bersifat *mutable* (isinya bisa diubah atau ditambah sewaktu-waktu):

```python
daftar_belanja = ["telur", "susu", "roti"]

# Mengubah item kedua:
daftar_belanja[1] = "keju"

# Menambah item baru di antrean paling belakang:
daftar_belanja.append("kopi")

print(daftar_belanja)
# Output: ['telur', 'keju', 'roti', 'kopi']
```

Bila kamu ingin tahu ada berapa total item di dalam list, gunakan fungsi bawaan `len()`:
```python
print(f"Total belanjaan: {len(daftar_belanja)} barang")
```

Di modul selanjutnya, kita akan memproses seluruh isi list ini secara otomatis menggunakan keajaiban perulangan **for loop**!
