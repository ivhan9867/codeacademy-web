---
id: py-fund-lanjutan-3
courseId: py-fund-01
order: 20
title: "Fungsi: Membungkus Logika dengan def & return"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami prinsip DRY (Don't Repeat Yourself) dalam rekayasa perangkat lunak"
  - "Mendefinisikan fungsi menggunakan kata kunci def"
  - "Menerima parameter input dan mengembalikan hasil dengan kata kunci return"
codeLanguage: python
starterCode: |
  # Latihan Membuat Fungsi Sendiri
  def hitung_diskon(harga_asli, persen_diskon):
      """Menghitung nominal potongan harga dan total bayar."""
      potongan = harga_asli * (persen_diskon / 100)
      total_bayar = harga_asli - potongan
      return total_bayar

  # Memanggil fungsi
  harga_buku = 80000
  diskon = 20
  bayar = hitung_diskon(harga_buku, diskon)

  print(f"Harga Asli : Rp{harga_buku:,}")
  print(f"Diskon     : {diskon}%")
  print(f"Total Bayar: Rp{int(bayar):,}")
playgroundTask: "Buat fungsi baru bernama 'hitung_keliling_persegi(sisi)' yang mengembalikan 4 * sisi. Panggil fungsi tersebut dengan sisi = 12 dan cetak hasilnya!"
---
Pernahkah kamu menyalin 10 baris kode yang sama ke 5 tempat berbeda di programmu? 
Jika suatu saat ada rumus yang berubah, kamu harus mencari dan memperbarui kelima tempat tersebut satu per satu. Melelahkan dan rawan salah!

Para insinyur perangkat lunak memiliki prinsip suci bernama **DRY**: *Don't Repeat Yourself* (Jangan Ulangi Dirimu Sendiri).

Cara terbaik menerapkannya adalah dengan membungkus sekumpulan instruksi menjadi satu paket rapi bernama **Fungsi (Function)**.

![Anatomi Fungsi di Python](asset:images/py-fund-01/function-anatomy.png)

## Mendefinisikan Fungsi dengan `def`

Fungsi di Python dibuat dengan kata kunci `def` (kependekan dari *define*):

```python
def sapa_dunia():
    print("Halo dari dalam fungsi!")
    print("Selamat belajar dan berkarya.")

# Memanggil fungsi:
sapa_dunia()
sapa_dunia()
```

Setiap kali `sapa_dunia()` dipanggil, seluruh isi di dalam blok indentasinya akan dijalankan.

## Fungsi dengan Parameter Input

Fungsi bisa menerima bahan masukan (*parameter*) agar perilakunya fleksibel:

```python
def sapa_pengguna(nama, peran):
    print(f"Halo {nama}! Anda masuk sebagai {peran}.")

sapa_pengguna("Rafi", "Admin")
sapa_pengguna("Siti", "Siswa")
```

## Mengembalikan Nilai dengan `return`

Fungsi tidak hanya sekadar mencetak teks, tetapi juga bisa menghitung dan mengembalikan hasil olahan ke variabel pemanggilnya menggunakan kata kunci **`return`**:

```python
def hitung_luas_persegi_panjang(panjang, lebar):
    luas = panjang * lebar
    return luas

# Menyimpan hasil fungsi ke variabel:
ruang_tamu = hitung_luas_persegi_panjang(6, 4)
kamar_tidur = hitung_luas_persegi_panjang(3, 3)

print(f"Luas ruang tamu: {ruang_tamu} m²")
print(f"Total luas rumah: {ruang_tamu + kamar_tidur} m²")
```

> Begitu Python menemui perintah `return`, eksekusi fungsi langsung selesai dan hasilnya diserahkan kembali ke pemanggil.

Di modul berikutnya, kita akan mempelajari batas wilayah kekuasaan variabel: apa itu **Variabel Lokal** dan **Variabel Global**!
