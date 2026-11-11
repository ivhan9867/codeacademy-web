---
id: py-fund-pendahuluan-2
courseId: py-fund-01
order: 6
title: "Baris Pertama: Mencetak Teks dengan print()"
readingMinutes: 4
section: PENDAHULUAN
learningObjectives:
  - "Menggunakan fungsi print() untuk memunculkan teks dan angka ke terminal"
  - "Memahami pentingnya pasangan tanda petik ganda atau tunggal pada string"
  - "Mencetak beberapa nilai sekaligus menggunakan pemisah koma"
codeLanguage: python
starterCode: |
  # Latihan Baris Pertama: Fungsi print()
  # 1. Cetak kalimat perkenalan kamu
  print("Halo, saya sedang belajar Python di CodeAcademy!")

  # 2. Cetak beberapa nilai sekaligus dengan pemisah koma
  print("Nama:", "Pelajar Hebat", "| Tahun:", 2026)
playgroundTask: "Ubah nama di dalam fungsi print() dengan nama kamu sendiri, lalu tambahkan baris print baru yang mencetak hobi atau cita-citamu!"
---
Momen yang ditunggu-tunggu telah tiba! Saatnya kamu memberikan instruksi pertamamu kepada komputer.

Di Python, perintah untuk menampilkan sesuatu ke layar terminal dinamakan `print()`. Bayangkan `print()` seperti mikrofon yang membacakan apa pun yang kamu taruh di dalam tanda kurungnya.

## Menulis Teks Pertama

Untuk mencetak kalimat, kita harus membungkus kata-kata tersebut di dalam tanda petik ganda (`"..."`) atau petik tunggal (`'...'`):

```python
print("Halo, saya sedang belajar Python!")
```

Ketika kamu menekan tombol **Jalankan**, terminal akan langsung membalas:
```
Halo, saya sedang belajar Python!
```

> Mengapa harus pakai tanda petik? Tanda petik memberi tahu Python: *"Ini adalah tulisan biasa (teks), jangan dianggap sebagai nama perintah atau rumus!"*

## Mencetak Beberapa Nilai Sekaligus

Kamu bisa mencetak beberapa potong teks atau angka dalam satu panggilan `print()` dengan memisahkannya menggunakan tanda koma (`,`):

```python
print("Skor pemain:", 100)
print("Nama:", "Budi", "Level:", 5)
```

Hasil di terminal:
```
Skor pemain: 100
Nama: Budi Level: 5
```

Perhatikan bahwa Python secara otomatis menyisipkan satu spasi di antara nilai-nilai yang dipisahkan oleh tanda koma. Sangat rapi dan praktis!

## Cobalah Sendiri!

Ketik kode ini di editor:
1. Cetak nama lengkapmu.
2. Cetak kota asalmu.
3. Cetak angka tahun favoritmu.

Lalu perhatikan urutan keluaran di terminal. Setiap baris `print()` akan otomatis membuat baris baru di bawahnya. 

Di modul berikutnya, kita akan belajar cara menyimpan teks atau angka ini ke dalam "wadah memori" bernama **Variabel**.
