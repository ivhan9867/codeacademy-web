---
id: py-fund-lanjutan-1
courseId: py-fund-01
order: 18
title: "Pasangan Data Berlabel: Dictionary (dict)"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami struktur data berpasangan key-value pada Dictionary"
  - "Membuat, membaca, menambah, dan memperbarui data di dalam dictionary"
  - "Mengakses data secara aman dengan metode .get()"
---
Pada modul List, kita menyimpan data secara berurutan dan memanggilnya dengan nomor indeks (`0, 1, 2`). Namun dalam dunia nyata, data sering kali lebih masuk akal jika diberi **label nama**, bukan nomor.

Contohnya profil seorang siswa:
- Nama: "Lestari"
- Jurusan: "Ilmu Komputer"
- Semester: 3
- IPK: 3.85

Di Python, struktur data pasangan label (*key*) dan nilai (*value*) ini dinamakan **Dictionary (`dict`)**.

![Konsep Key dan Value pada Dictionary](asset:images/py-fund-01/dict-key-value.png)

## Membuat Dictionary Pertama

Dictionary ditulis dengan kurung kurawal `{...}`, di mana antara *key* dan *value* dipisahkan oleh titik dua (`:`):

```python
profil_siswa = {
    "nama": "Lestari",
    "jurusan": "Ilmu Komputer",
    "semester": 3,
    "ipk": 3.85
}

print(profil_siswa["nama"])     # Lestari
print(profil_siswa["ipk"])      # 3.85
```

## Menambah dan Mengubah Data

Menambah properti baru atau mengubah data yang sudah ada sangat mudah:

```python
# Mengubah semester:
profil_siswa["semester"] = 4

# Menambahkan kontak baru:
profil_siswa["email"] = "lestari@kampus.id"

print(profil_siswa)
```

## Membaca Aman dengan Metode `.get()`

Jika kamu memanggil kunci yang tidak pernah ada menggunakan kurung siku biasa (`profil_siswa["alamat"]`), Python akan memunculkan `KeyError` yang menghentikan program.

Untuk mencegah crash, gunakan metode `.get()`:

```python
alamat = profil_siswa.get("alamat", "Belum diisi")
print(f"Alamat: {alamat}")
# Output: Alamat: Belum diisi
```

Jika kuncinya tidak ada, Python tidak akan error, melainkan memberikan nilai default yang kamu tentukan. Sangat elegan!

Di modul berikutnya, kita akan berkenalan dengan dua struktur data pelengkap: **Tuple** yang tahan banting dan **Set** yang anti-duplikat.
