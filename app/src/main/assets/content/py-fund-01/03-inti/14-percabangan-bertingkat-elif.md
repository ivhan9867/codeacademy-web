---
id: py-fund-inti-5
courseId: py-fund-01
order: 14
title: "Percabangan Bertingkat dengan elif"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Menangani skenario multi-kondisi menggunakan rantai if-elif-else"
  - "Memahami alur evaluasi dari atas ke bawah (first match wins)"
  - "Menentukan kondisi default pada blok else penutup"
---
Tidak semua masalah hidup hanya hitam dan putih (hanya dua opsi). Sering kali kita menghadapi situasi dengan banyak alternatif:
- Nilai 85 ke atas = Grade A
- Nilai 70 sampai 84 = Grade B
- Nilai 55 sampai 69 = Grade C
- Nilai di bawah 55 = Grade D

Di Python, singkatan dari *"else if"* (kalau tidak, jika...) ditulis dengan kata kunci **`elif`**.

## Rantai Kondisi `if - elif - else`

Kamu bisa menyusun sebanyak mungkin `elif` yang kamu butuhkan di antara `if` pembuka dan `else` penutup:

```python
nilai_ujian = 78

if nilai_ujian >= 85:
    predikat = "A (Sangat Memuaskan)"
elif nilai_ujian >= 70:
    predikat = "B (Baik)"
elif nilai_ujian >= 55:
    predikat = "C (Cukup)"
else:
    predikat = "D (Perlu Remedial)"

print(f"Nilai: {nilai_ujian} -> Predikat: {predikat}")
# Output: Nilai: 78 -> Predikat: B (Baik)
```

## Alur Eksekusi: Yang Pertama Cocok yang Menang

Python membaca rantai `if-elif-else` dari atas ke bawah:
1. Python menguji kondisi pertama (`nilai_ujian >= 85`). Karena `78 >= 85` bernilai `False`, Python melompat ke kondisi berikutnya.
2. Python menguji kondisi kedua (`nilai_ujian >= 70`). Karena `78 >= 70` bernilai `True`, blok ini dieksekusi.
3. Begitu satu blok terpenuhi, Python **langsung keluar** dari seluruh rantai keputusan tanpa memeriksa `elif` atau `else` di bawahnya lagi.

> Selalu letakkan kondisi yang paling spesifik atau bernilai paling tinggi di bagian atas, agar kondisi tidak terserap oleh aturan yang lebih longgar.

Di modul berikutnya, kita akan belajar menyimpan banyak data sekaligus dalam satu variabel menggunakan struktur data paling populer di Python: **List**!
