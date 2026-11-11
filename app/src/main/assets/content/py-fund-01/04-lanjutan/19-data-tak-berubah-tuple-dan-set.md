---
id: py-fund-lanjutan-2
courseId: py-fund-01
order: 19
title: "Data Tak Berubah & Koleksi Unik: Tuple & Set"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - "Mengenal Tuple () sebagai koleksi data yang tidak dapat diubah (immutable)"
  - "Menggunakan Set {} untuk menghapus nilai duplikat secara otomatis"
  - "Memilih struktur data yang tepat sesuai kebutuhan kasus nyata"
---
Selain List dan Dictionary, Python memiliki dua struktur data bawaan yang punya peran unik: **Tuple** dan **Set**.

## 1. Tuple: List yang Dikunci Rapat (*Immutable*)

Tuple ditulis menggunakan tanda kurung biasa `(...)`. Berbeda dengan List yang bisa diubah-ubah sesuka hati, **Tuple tidak dapat diubah nilainya setelah dibuat** (*immutable*):

```python
# Koordinat GPS Monas (Latitude, Longitude)
koordinat_monas = (-6.175392, 106.827153)

print("Latitude:", koordinat_monas[0])
print("Longitude:", koordinat_monas[1])

# Percobaan mengubah isi tuple:
# koordinat_monas[0] = -6.200000 
# -> TypeError: 'tuple' object does not support item assignment!
```

**Kapan menggunakan Tuple?**
Gunakan Tuple untuk data yang sifatnya permanen dan tidak boleh sampai terutak-atik secara tidak sengaja oleh bagian lain program, seperti koordinat geografis, resolusi layar (`1920, 1080`), atau kode warna RGB.

## 2. Set: Koleksi Elemen Unik (Tanpa Duplikat)

Set ditulis dengan kurung kurawal `{...}` (tanpa pasangan titik dua). Keajaiban Set adalah: **ia menolak item ganda**.

```python
daftar_id_member = {101, 102, 103, 101, 102, 104}
print(daftar_id_member)
# Output: {101, 102, 103, 104}  (duplikat otomatis disaring!)
```

Set sangat berguna untuk membersihkan data dari duplikasi:
```python
kota_kunjungan = ["Bandung", "Surabaya", "Jakarta", "Bandung", "Surabaya"]
kota_unik = set(kota_kunjungan)
print(f"Mengunjungi {len(kota_unik)} kota berbeda.")
# Output: Mengunjungi 3 kota berbeda.
```

## Ringkasan 4 Struktur Data Python

- **List `[]`**: Berurutan, bisa diubah (*mutable*).
- **Tuple `()`**: Berurutan, tidak bisa diubah (*immutable*).
- **Dictionary `{k: v}`**: Berpasangan label & nilai, cepat dicari.
- **Set `{}`**: Tidak berurutan, semua item unik tanpa duplikat.

Di modul berikutnya, kita akan naik kelas dengan merancang resep instruksi sendiri menggunakan **Fungsi (Functions)**!
