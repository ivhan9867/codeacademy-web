---
id: py-fund-evaluasi-2
courseId: py-fund-01
order: 24
title: "Gaya Kode Bersih: Panduan Standar PEP 8"
readingMinutes: 4
section: EVALUASI
learningObjectives:
  - "Mengenal dokumen panduan resmi Python Enhancement Proposal 8 (PEP 8)"
  - "Menerapkan aturan spasi di sekitar operator dan koma"
  - "Menulis komentar yang informatif dan bermakna (#)"
---
Menulis kode yang bisa berjalan (*it works*) adalah langkah awal. Namun menulis kode yang bersih, rapi, dan mudah dirawat oleh programmer lain (*clean code*) adalah tanda seorang profesional sejati.

Komunitas Python memiliki satu dokumen pedoman gaya resmi yang diakui dunia: **PEP 8** (*Python Enhancement Proposal 8*).

## 4 Aturan Utama PEP 8 yang Wajib Diingat

### 1. Indentasi Selalu 4 Spasi
Gunakan selalu 4 spasi untuk setiap tingkat indentasi. Jangan pernah mencampur antara tombol *Tab* dan tombol *Spasi*.

### 2. Spasi di Sekitar Operator
Beri satu spasi sebelum dan sesudah operator penugasan (`=`) dan operator matematika:

```python
# SANGAT BAIK (PEP 8):
total_harga = harga_satuan * kuantitas + pajak

# BURUK (Terlalu padat / sesak):
total_harga=harga_satuan*kuantitas+pajak
```

### 3. Spasi Setelah Tanda Koma
Sama seperti tata bahasa menulis kalimat biasa, selalu beri satu spasi **setelah** koma, bukan sebelumnya:

```python
# BENAR:
buah = ["apel", "pisang", "semangka"]

# SALAH:
buah = ["apel","pisang","semangka"]
```

### 4. Menulis Komentar yang Menjelaskan 'Mengapa', Bukan 'Apa'
Gunakan tanda pagar `#` untuk memberikan catatan penjelas. Hindari menulis komentar yang hanya mengulang hal yang sudah jelas terbaca di kode:

```python
# BURUK (Komentar mubazir):
# Menambah skor dengan satu
skor = skor + 1

# BAIK (Menjelaskan alasan bisnis / konteks):
# Beri bonus 1 poin jika pemain berhasil menjawab dalam waktu kurang dari 3 detik
skor = skor + 1
```

> "Readability counts." — Keterbacaan itu sangat bernilai. Menulis kode rapi adalah bentuk kepedulianmu terhadap rekan tim dan dirimu sendiri di masa depan.

Di modul penutup terakhir, kita akan merangkum seluruh perjalanan kita dan melihat peta jalan karier yang bisa kamu tempuh selanjutnya!
