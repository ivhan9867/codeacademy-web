---
id: py-fund-pendahuluan-4
courseId: py-fund-01
order: 8
title: "Aturan Penamaan Variabel & Konvensi snake_case"
readingMinutes: 4
section: PENDAHULUAN
learningObjectives:
  - "Mengetahui aturan sintaks penamaan variabel yang sah menurut Python"
  - "Membedakan kata kunci yang dilarang (reserved keywords)"
  - "Menerapkan konvensi penulisan snake_case standar komunitas Python (PEP 8)"
---
Kamu bebas menamai variabelmu sesuka hati, tetapi Python memiliki aturan mutlak yang tidak boleh dilanggar, serta etika komunitas agar kodemu mudah dibaca oleh programmer lain.

## 3 Aturan Wajib Penamaan Variabel

1. **Hanya boleh berisi huruf, angka, dan garis bawah (`_`)**: Karakter spesial seperti `@`, `#`, `$`, `%`, atau spasi dilarang keras!
2. **Tidak boleh diawali dengan angka**: `nama1` boleh, tetapi `1nama` akan langsung memicu `SyntaxError`.
3. **Sensitif terhadap huruf besar-kecil (*case-sensitive*)**: Variabel `umur`, `Umur`, dan `UMUR` dianggap sebagai 3 wadah yang sama sekali berbeda oleh Python.
4. **Bukan kata kunci terlarang (*reserved keywords*)**: Kamu tidak boleh memakai kata yang sudah menjadi perintah bawaan Python, seperti `print`, `if`, `for`, `while`, `True`, atau `def`.

## Standar Komunitas: snake_case

Di dunia pemrograman, setiap bahasa punya kebiasaan. Di Python, konvensi standar untuk variabel adalah **snake_case**:
- Semua huruf menggunakan huruf kecil.
- Setiap suku kata dipisahkan oleh garis bawah (*underscore* `_`).

Contoh penamaan yang baik vs buruk:

```python
# SANGAT DIANJURKAN (snake_case yang deskriptif):
total_harga_belanja = 50000
jumlah_siswa_aktif = 28
nama_depan = "Siti"

# HINDARI (sulit dibaca / melanggar konvensi):
thb = 50000                 # Terlalu singkat dan misterius
totalHargaBelanja = 50000   # CamelCase (biasa dipakai di JavaScript/Java)
total harga = 50000         # ERROR! Ada spasi!
```

> Tips Emas: Pilihlah nama variabel yang menjelaskan maknanya dengan jelas. Bayangkan orang lain (atau dirimu 6 bulan lagi) membaca kode tersebut tanpa petunjuk tambahan.

Di modul berikutnya, kita akan membedah jenis-jenis isi yang bisa dimasukkan ke dalam wadah variabel: dari angka bulat sampai nilai kebenaran.
