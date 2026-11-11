---
id: py-fund-evaluasi-1
courseId: py-fund-01
order: 23
title: "Mini Proyek Terpadu: Program Kasir Toko Kopi"
readingMinutes: 8
section: EVALUASI
learningObjectives:
  - "Mengintegrasikan konsep variabel, dictionary, list, loop, percabangan, dan fungsi"
  - "Membangun program kalkulasi transaksi belanja kasir secara mandiri"
  - "Menghasilkan cetakan struk belanja yang rapi dan terformat di terminal"
codeLanguage: python
starterCode: |
  # Mini Proyek: Program Kasir Toko Kopi
  daftar_harga = {
      "Kopi Hitam": 15000,
      "Latte": 22000,
      "Roti Bakar": 18000,
      "Air Mineral": 6000
  }

  pesanan = [
      {"menu": "Latte", "qty": 2},
      {"menu": "Roti Bakar", "qty": 1}
  ]

  subtotal = 0
  print("=" * 36)
  print("         STRUK TOKO KOPI ☕")
  print("=" * 36)

  for item in pesanan:
      nama_item = item["menu"]
      jumlah = item["qty"]
      harga_satuan = daftar_harga[nama_item]
      total_item = harga_satuan * jumlah
      subtotal += total_item
      print(f"{nama_item:<16} x{jumlah}  Rp{total_item:>8,}")

  print("-" * 36)
  diskon = 0
  if subtotal >= 50000:
      diskon = subtotal * 0.10
      print(f"Subtotal            Rp{subtotal:>8,}")
      print(f"Diskon Promo (10%) -Rp{int(diskon):>8,}")

  total_bayar = subtotal - diskon
  print(f"TOTAL AKHIR         Rp{int(total_bayar):>8,}")
  print("=" * 36)
  print("    Terima kasih telah berkunjung!")
playgroundTask: "Tambahkan pesanan baru {'menu': 'Kopi Hitam', 'qty': 1} ke dalam list 'pesanan', lalu amati apakah total belanjamu berhak mendapatkan diskon promo 10%!"
---
Semua potongan puzzle yang telah kita pelajari selama ini — variabel, tipe data, kalkulasi aritmatika, perulangan `for`, percabangan `if-else`, dictionary, dan fungsi — saatnya kita satukan ke dalam satu proyek utuh yang nyata: **Sistem Kasir Toko Kopi**.

![Simulasi Cetakan Struk Kasir di Layar Terminal](asset:images/py-fund-01/cashier-receipt-terminal.png)

## Spesifikasi Program

Program kita akan memiliki tugas:
1. Menyimpan daftar menu beserta harga menggunakan `dict`.
2. Menyimpan daftar pesanan pelanggan yang berisi item dan jumlah menggunakan `list` of `dict`.
3. Menghitung subtotal belanja.
4. Memberikan diskon 10% jika total belanja mencapai Rp50.000 atau lebih.
5. Mencetak struk transaksi yang indah ke layar terminal.

## Kode Lengkap Proyek

Salin dan jalankan kode berikut di Playground, lalu amati setiap barisnya:

```python
# 1. Daftar Menu Toko Kopi (Dictionary)
daftar_harga = {
    "Kopi Susu": 18000,
    "Americano": 15000,
    "Croissant": 22000,
    "Roti Cokelat": 12000
}

# 2. Pesanan Pelanggan (List of Dictionaries)
pesanan_meja_3 = [
    {"menu": "Kopi Susu", "jumlah": 2},
    {"menu": "Croissant", "jumlah": 1},
    {"menu": "Roti Cokelat", "jumlah": 2}
]

# 3. Fungsi Menghitung Total dan Diskon
def hitung_transaksi(daftar_pesanan):
    subtotal = 0
    print("=" * 35)
    print("       KEDAI KOPI KELAS KITA       ")
    print("=" * 35)
    
    for item in daftar_pesanan:
        nama_menu = item["menu"]
        qty = item["jumlah"]
        harga_satuan = daftar_harga.get(nama_menu, 0)
        total_item = harga_satuan * qty
        subtotal += total_item
        print(f"{nama_menu:<15} {qty}x  Rp{total_item:>7,}")
        
    print("-" * 35)
    print(f"Subtotal       : Rp{subtotal:>7,}")
    
    # Syarat Diskon: Belanja >= 50.000 dapat diskon 10%
    diskon = 0
    if subtotal >= 50000:
        diskon = subtotal * 0.10
        print(f"Diskon Promo 10%: -Rp{int(diskon):>6,}")
    
    total_bayar = subtotal - diskon
    print("=" * 35)
    print(f"TOTAL BAYAR    : Rp{int(total_bayar):>7,}")
    print("=" * 35)
    print("   Terima kasih atas kunjungan Anda!   ")

# 4. Jalankan Fungsi
hitung_transaksi(pesanan_meja_3)
```

## Tantangan Eksplorasi untuk Kamu!

Coba modifikasi kode di atas di Playground:
1. Tambahkan menu baru: `"Matcha Latte"` seharga `20000`.
2. Ubah pesanan pelanggan untuk memesan matcha latte tersebut.
3. Ubah aturan diskon: berikan diskon 15% jika belanja di atas Rp80.000.

Perhatikan bagaimana semua konsep yang tadinya terpisah kini bekerja harmonis membentuk satu perangkat lunak yang fungsional!

Di modul berikutnya, kita akan mempelajari panduan gaya penulisan kode Python standar industri (**PEP 8**).
