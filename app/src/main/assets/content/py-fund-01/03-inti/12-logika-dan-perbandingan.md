---
id: py-fund-inti-3
courseId: py-fund-01
order: 12
title: "Operator Perbandingan & Logika Boolean"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Menggunakan operator pembanding: ==, !=, >, <, >=, <="
  - "Membedakan operator penugasan (=) dengan operator kesetaraan (==)"
  - "Menggabungkan beberapa kondisi dengan operator logika and, or, dan not"
---
Agar program kita bisa berpikir cerdas dan membuat keputusan, program harus mampu membandingkan nilai: *"Apakah saldo cukup?", "Apakah umur pengguna di atas 17 tahun?", "Apakah kata sandinya cocok?"*.

Hasil dari setiap perbandingan selalu berupa nilai Boolean: `True` atau `False`.

## Operator Pembanding (Comparison Operators)

| Operator | Arti | Contoh | Hasil |
|---|---|---|---|
| `==` | Sama dengan | `5 == 5` | `True` |
| `!=` | Tidak sama dengan | `5 != 3` | `True` |
| `>` | Lebih besar dari | `10 > 20` | `False` |
| `<` | Lebih kecil dari | `10 < 20` | `True` |
| `>=` | Lebih besar atau sama dengan | `15 >= 15` | `True` |
| `<=` | Lebih kecil atau sama dengan | `12 <= 10` | `False` |

> **PERINGATAN JEBAKAN PEMULA**:
> - Tanda satu `=` adalah perintah menyimpan data ke variabel (`x = 10`).
> - Tanda ganda `==` adalah pertanyaan menguji kesamaan (`x == 10`).

## Operator Logika: `and`, `or`, `not`

Terkadang sebuah keputusan membutuhkan lebih dari satu syarat. Di sinilah kita menggunakan operator logika:

1. **`and`**: Bernilai `True` HANYA jika **semua** syarat terpenuhi.
2. **`or`**: Bernilai `True` jika **salah satu saja** syarat terpenuhi.
3. **`not`**: Membalik nilai kebenaran (`not True` menjadi `False`, dan sebaliknya).

Contoh nyata: Syarat mengikuti ujian SIM di Indonesia
```python
umur = 18
lulus_tes_kesehatan = True

bisa_buat_sim = (umur >= 17) and lulus_tes_kesehatan
print(f"Boleh membuat SIM? {bisa_buat_sim}")
# Output: Boleh membuat SIM? True
```

Kini kita sudah bisa menguji kondisi. Di modul berikutnya, kita akan menggunakan kondisi ini untuk mengatur jalannya program dengan percabangan `if` dan `else`!
