---
id: py-fund-inti-2
courseId: py-fund-01
order: 11
title: "Menggabungkan String & Keajaiban f-string"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Mengetahui cara lama penggabungan string dengan tanda tambah (+)"
  - "Menguasai sintaks f-string (formatted string literal) modern"
  - "Menyisipkan ekspresi matematika langsung di dalam kurung kurawal f-string"
---
Ketika membuat aplikasi, kita sering kali perlu menyusun kalimat yang menyertakan data dinamis dari variabel, seperti: *"Halo Budi, sisa kuotamu adalah 4.5 GB"*.

Bagaimana cara terbaik membuatnya di Python?

![Anatomi f-string di Python](asset:images/py-fund-01/fstring-anatomy.png)

## Cara Jadul: Menggunakan Tanda Tambah (`+`)

Dahulu, programmer menggabungkan string dengan operator `+` (*concatenation*). Namun cara ini merepotkan karena semua angka harus diubah dulu menjadi `str()`:

```python
nama = "Citra"
skor = 95

# Cara lama yang kaku:
pesan = "Pemain " + nama + " meraih skor " + str(skor) + " poin."
print(pesan)
```

Jika kamu lupa menulis `str(skor)`, Python akan langsung melempar `TypeError: can only concatenate str to str, not int`.

## Cara Modern: f-string (Formatted String)

Sejak Python versi 3.6, diperkenalkan fitur **f-string**. Cara pakainya sangat elegan:
1. Awali kutipan dengan huruf `f` (bisa huruf kecil `f` atau besar `F`).
2. Tulis variabel atau ekspresi langsung di dalam tanda kurung kurawal `{}`.

```python
nama = "Citra"
skor = 95

# Cara modern f-string yang bersih:
pesan = f"Pemain {nama} meraih skor {skor} poin."
print(pesan)
```

Python akan otomatis mengambil isi dari `{nama}` dan `{skor}` lalu menggabungkannya ke dalam teks secara mulus!

## Melakukan Perhitungan di Dalam `{}`

Kamu bahkan bisa menyisipkan operasi hitungan atau logika langsung di dalam kurung kurawal:

```python
harga = 15000
jumlah = 3

print(f"Total tagihan belanja Anda: Rp{harga * jumlah:,}")
# Output: Total tagihan belanja Anda: Rp45,000
```

> f-string adalah salah satu fitur paling disukai programmer Python karena membuat kode bersih, ringkas, dan mudah dibaca sekilas.

Di modul berikutnya, kita akan mempelajari operator perbandingan untuk membandingkan dua nilai sebelum mengambil keputusan.
