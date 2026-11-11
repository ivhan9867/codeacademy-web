---
id: py-fund-inti-8
courseId: py-fund-01
order: 17
title: "Perulangan Bersyarat: while, break, & continue"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Membuat perulangan bersyarat dengan while loop"
  - "Mencegah terjadinya perulangan tanpa henti (infinite loop)"
  - "Mengendalikan siklus perulangan secara presisi dengan break dan continue"
---
Jika perulangan `for` digunakan saat kita sudah tahu berapa kali perulangan harus terjadi (misalnya sebanyak item di list), maka perulangan **`while`** digunakan saat kita ingin terus mengulang **selama suatu kondisi masih bernilai `True`**.

Bayangkan proses pengisian baterai ponsel: *"Selama baterai belum 100%, lanjutkan pengisian daya."*

## Sintaks Dasar `while`

```python
daya_baterai = 95

while daya_baterai < 100:
    print(f"Mengisi daya... Sekarang {daya_baterai}%")
    daya_baterai = daya_baterai + 1  # Menaikkan nilai agar suatu saat kondisi berhenti

print("Baterai penuh 100%!")
```

> **WASPADA INFINITE LOOP**: Jika kamu lupa menambahkan baris penaik nilai (`daya_baterai = daya_baterai + 1`), kondisinya akan selamanya `True` dan programmu akan terus berjalan tanpa henti sampai komputer kehabisan memori!

## Menghentikan Paksa dengan `break`

Kata kunci `break` berguna untuk langsung melompat keluar dari perulangan seketika itu juga saat suatu peristiwa terjadi:

```python
nomor_antrean = 1

while True:
    print(f"Melayani antrean nomor {nomor_antrean}")
    if nomor_antrean == 3:
        print("Batas layanan hari ini tercapai. Loket ditutup!")
        break
    nomor_antrean += 1
```

## Melewatkan Putaran dengan `continue`

Kata kunci `continue` berguna untuk mengabaikan sisa instruksi di putaran saat ini dan langsung melompat ke putaran berikutnya:

```python
# Mencetak angka 1 sampai 5, tetapi melewati angka 3:
for angka in range(1, 6):
    if angka == 3:
        continue
    print(angka)

# Output: 1, 2, 4, 5 (angka 3 dilewati!)
```

Luar biasa! Kamu telah menguasai seluruh fondasi inti logika pemrograman Python. Di bagian **Lanjutan**, kita akan menjelajahi struktur data kamus (`Dictionary`) dan merancang fungsi kita sendiri!
