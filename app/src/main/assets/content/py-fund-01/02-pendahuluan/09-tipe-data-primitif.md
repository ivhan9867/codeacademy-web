---
id: py-fund-pendahuluan-5
courseId: py-fund-01
order: 9
title: "Tipe Data Primitif: Integer, Float, String, & Boolean"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - "Membedakan 4 tipe data dasar: int, float, str, dan bool"
  - "Memeriksa jenis tipe data menggunakan fungsi type()"
  - "Melakukan konversi antar-tipe data dasar (type casting)"
---
Sama seperti lemari pakaian yang menampung benda yang berbeda-beda (ada baju, sepatu, dan aksesoris), variabel di Python juga menampung berbagai macam **tipe data**.

![4 Tipe Data Primitif di Python](asset:images/py-fund-01/primitive-types.png)

Ada 4 tipe data mendasar yang wajib kamu kuasai sejak hari pertama:

## 1. Integer (`int`): Bilangan Bulat
Angka bulat tanpa desimal atau koma, baik positif maupun negatif.
```python
jumlah_kursi = 40
suhu_beku = -5
```

## 2. Float (`float`): Bilangan Desimal
Angka pecahan yang menggunakan tanda titik (`.`) sebagai pemisah desimal.
```python
tinggi_badan = 168.5
nilai_pi = 3.14159
```

## 3. String (`str`): Untaian Teks
Kumpulan karakter (huruf, angka, spasi, simbol) yang diapit oleh tanda kutip.
```python
pesan = "Selamat pagi!"
kode_pos = "60111"  # Walau isinya angka, karena berpetik maka ini teks!
```

## 4. Boolean (`bool`): Sakelar Benar atau Salah
Hanya memiliki dua kemungkinan nilai: `True` (Benar) atau `False` (Salah). Perhatikan bahwa huruf pertamanya wajib kapital.
```python
sudah_lulus = True
tombol_aktif = False
```

## Mengetahui Tipe Data dengan `type()`

Jika kamu bingung tipe data apa yang sedang disimpan di sebuah variabel, tanyakan langsung pada Python menggunakan fungsi `type()`:

```python
x = 100
y = "100"
z = 100.0

print(type(x))  # <class 'int'>
print(type(y))  # <class 'str'>
print(type(z))  # <class 'float'>
```

## Mengubah Tipe Data (*Type Casting*)

Terkadang kita perlu mengubah satu tipe data ke tipe lain, misalnya mengubah teks angka `"25"` menjadi bilangan bulat `25` agar bisa dihitung:

```python
angka_teks = "25"
angka_asli = int(angka_teks)
print(angka_asli + 5)  # Menghasilkan 30!
```

Selamat! Kamu telah menyelesaikan bagian Pendahuluan. Di bagian **Inti**, kita akan mulai melakukan kalkulasi matematika dan manipulasi teks yang canggih!
