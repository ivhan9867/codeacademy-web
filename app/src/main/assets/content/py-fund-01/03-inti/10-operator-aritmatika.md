---
id: py-fund-inti-1
courseId: py-fund-01
order: 10
title: "Operator Aritmatika: Kalkulasi & Pembagian Khusus"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Menggunakan operator matematika standar (+, -, *, /) di Python"
  - "Membedakan pembagian bulat (//), modulo sisa bagi (%), dan perpangkatan (**)"
  - "Memahami urutan prioritas operasi matematika (KABATAKU)"
codeLanguage: python
starterCode: |
  # Latihan Operator Aritmatika
  panjang = 15
  lebar = 8

  luas = panjang * lebar
  keliling = 2 * (panjang + lebar)

  print("Dimensi Tanah:")
  print(f"Panjang: {panjang} m, Lebar: {lebar} m")
  print(f"Luas Tanah: {luas} m²")
  print(f"Keliling Tanah: {keliling} m")
playgroundTask: "Coba lakukan pembagian 100 dengan 3 menggunakan operator pembagian biasa (/), pembagian bulat (//), dan sisa bagi (%). Cetak ketiga hasilnya ke terminal!"
---
Komputer pada dasarnya adalah kalkulator super cepat. Di Python, kamu bisa melakukan semua operasi matematika yang biasa kamu gunakan sehari-hari, plus beberapa operator khusus yang sangat bermanfaat untuk coding.

## Tabel Operator Aritmatika

| Simbol | Nama Operasi | Contoh | Hasil |
|---|---|---|---|
| `+` | Penjumlahan | `10 + 5` | `15` |
| `-` | Pengurangan | `10 - 4` | `6` |
| `*` | Perkalian | `4 * 3` | `12` |
| `/` | Pembagian Biasa | `7 / 2` | `3.5` (selalu menghasilkan `float`) |
| `//` | Pembagian Bulat (*Floor Division*) | `7 // 2` | `3` (membuang angka di belakang koma) |
| `%` | Modulo (Sisa Bagi) | `7 % 2` | `1` |
| `**` | Pangkat (*Power*) | `2 ** 3` | `8` ($2^3 = 2 \times 2 \times 2$) |

## Kenapa Modulo (`%`) Sangat Penting?

Bagi pemula, operator `%` (modulo) terdengar asing. Modulo adalah **sisa dari hasil pembagian**.

Contoh sederhana:
Jika kamu punya 10 permen dan dibagi rata ke 3 anak, masing-masing mendapat 3 permen dan bersisa **1** permen.
Maka `10 % 3` bernilai `1`.

Modulo sangat sering digunakan untuk:
1. **Mengecek bilangan genap atau ganjil**: Angka genap jika di-modulo 2 selalu menghasilkan `0` (`angka % 2 == 0`).
2. **Membuat siklus waktu**: Menghitung sisa detik setelah dikonversi ke menit.

```python
total_detik = 125
menit = total_detik // 60
sisa_detik = total_detik % 60

print("Waktu:", menit, "menit", sisa_detik, "detik")
# Output: Waktu: 2 menit 5 detik
```

## Urutan Operasi (Prioritas)

Sama seperti aturan matematika baku, perkalian dan pembagian dikerjakan lebih dulu daripada penjumlahan dan pengurangan. Gunakan tanda kurung `()` untuk memprioritaskan kalkulasi:

```python
hasil = (10 + 5) * 2
print(hasil)  # 30 (bukan 20)
```

Di modul berikutnya, kita akan menyatukan angka-angka hasil kalkulasi ini ke dalam kalimat rapi menggunakan fitur terfavorit di Python: **f-string**!
