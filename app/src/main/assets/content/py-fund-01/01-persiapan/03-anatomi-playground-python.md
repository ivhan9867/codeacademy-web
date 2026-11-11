---
id: py-fund-persiapan-3
courseId: py-fund-01
order: 3
title: "Anatomi Playground: Editor Kode & Panel Terminal"
readingMinutes: 4
section: PERSIAPAN
learningObjectives:
  - "Mengenal bagian-bagian utama antarmuka Playground Python di aplikasi"
  - "Membedakan area penulisan instruksi (Editor) dan area keluaran hasil (Console/Terminal)"
  - "Memahami alur eksekusi dari tombol 'Jalankan' ke output layar"
codeLanguage: python
starterCode: |
  # Selamat datang di Playground Python CodeAcademy!
  nama = "Siswa Hebat"
  print(f"Halo, {nama}! 👋")
  print("Selamat datang di meja kerja digital Python.")
  print("Tekan tombol 'Jalankan' untuk melihat pesan ini di Terminal!")
playgroundTask: "Ubah variabel 'nama' dengan namamu sendiri, lalu tekan tombol 'Jalankan' dan periksa keluaran di panel Terminal!"
---
Sebelum kita menulis kode Python pertama kita, mari kita luangkan waktu sejenak untuk mengenal tempat bermain kita: **Playground Interaktif Python**.

![Antarmuka Playground Python](asset:images/py-fund-01/playground-overview.png)

Playground ini dirancang seperti meja kerja ilmuwan digital modern. Khusus materi Python, antarmuka dirancang fokus pada satu lembar kerja kode dan satu panel terminal, tanpa gangguan tab web.

## 1. Area Editor Kode Python (`main.py`)

Editor kode adalah lembaran kerja utama tempat kamu menyusun instruksi untuk komputer. 

Fitur penting editor:
- **Pewarnaan Sintaks (*Syntax Highlighting*)**: Teks kode tidak monoton satu warna. Kata kunci (`def`, `for`, `if`), teks kutipan (`"..."`), angka, dan fungsi bawaan (`print`, `len`) otomatis diberi warna kontras untuk mempermudah membaca logika program.
- **Kursor & Pengetikan Nyaman**: Mendukung spasi dan baris baru yang rapi sesuai aturan indentasi bahasa Python.

## 2. Area Terminal / Konsol Output

Terminal adalah pengeras suara bagi komputermu. 

Komputer memproses logika dalam diam di memori. Ketika kamu memerintahkannya untuk mencetak sesuatu menggunakan fungsi `print()`, hasilnya akan dimunculkan baris demi baris di panel gelap terminal ini. Jika terjadi kesalahan ketik atau error, petunjuk perbaikan juga akan ditampilkan di sini.

## Siklus Eksekusi & Tombol Kontrol

```
[Tulis Kode di Editor] ──► [Tekan Tombol 'Jalankan'] ──► [Amati Output di Terminal]
```

Cobalah perhatikan 3 tombol aksi utama di bilah Playground:
- **Jalankan (Play)**: Mengeksekusi kode Python dan menampilkan hasilnya di terminal secara instan.
- **Reset**: Mengembalikan kode editor ke template bawaan modul ini jika kamu ingin mengulang dari awal.
- **Bersihkan Terminal**: Menghapus teks keluaran sebelumnya agar layar kembali bersih dan nyaman dibaca.

> **Perlindungan Otomatis**: Playground dilengkapi pengawas batas waktu (timeout 5 detik) agar aplikasi tetap responsif meskipun kamu tidak sengaja menulis perulangan tanpa henti (*infinite loop*).

Di modul selanjutnya, kita akan mempelajari cara membaca pesan error ketika komputer tidak memahami perintah yang kita tulis.

