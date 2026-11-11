---
id: web-foundation-pcss-7
courseId: fe-web-foundation-00
order: 63
title: "Formatting Teks: Pembagian Properti Font dan Text"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Membedakan properti font (tampilan huruf) dan properti text (tata letak dan dekorasi teks)"
  - "Mengetahui contoh penerapan praktis properti font dan properti text pada elemen paragraf"
---
## Memahami Dua Kelompok Styling Teks

Setelah tahu cara menulis selector, sekarang kita masuk ke bagian yang paling sering kamu jumpai di website: **teks**. Hampir setiap halaman web didominasi oleh tulisan, sehingga cara teks ditampilkan sangat menentukan kenyamanan membaca dan kesan profesional website kamu.

Di CSS, penataan teks dipisahkan secara rapi menjadi dua kategori besar:

### 1. Kelompok Properti Font (*Font Properties*)
Kategori ini mengatur **karakter fisik dari huruf itu sendiri**, seolah-olah kamu sedang memilih mata cetak huruf pada mesin tik. Properti di kelompok ini meliputi:
- `font-family`: menentukan jenis atau rupa huruf (misalnya Arial, Roboto, atau Georgia).
- `font-size`: mengatur besar-kecilnya ukuran huruf.
- `font-weight`: mengatur ketebalan huruf (seperti *bold* atau *normal*).
- `font-style`: mengatur kemiringan huruf (*italic* atau *normal*).

### 2. Kelompok Properti Text (*Text Properties*)
Kategori ini mengatur **cara kumpulan huruf ditata dalam sebuah kalimat atau paragraf**, mirip seperti kamu mengatur tata letak paragraf di Microsoft Word. Properti di kelompok ini meliputi:
- `text-align`: menentukan perataan paragraf (rata kiri, tengah, kanan, atau rata kanan-kiri).
- `line-height`: mengatur spasi atau jarak vertikal antarbaris teks.
- `text-decoration`: menambahkan garis bawah (*underline*) atau mencoret teks.
- `text-transform`: mengubah teks menjadi huruf kapital semua atau huruf kecil semua.
- `letter-spacing`: memberi jarak renggang antarhuruf.

## Contoh Kode Singkat

Perhatikan bagaimana kedua kelompok ini bekerja berdampingan pada satu elemen:

```css
p {
  /* Kelompok Font: mengatur wujud huruf */
  font-family: 'Helvetica Neue', sans-serif;
  font-size: 16px;
  font-weight: 400;

  /* Kelompok Text: mengatur tata letak paragraf */
  text-align: justify;
  line-height: 1.6;
  letter-spacing: 0.5px;
}
```

## Tujuan Pembelajaran ke Depan

Pemisahan ini dibuat agar kamu tidak bingung mencari properti yang tepat:
- Mau mengganti bentuk huruf agar lebih kasual atau elegan? Carilah di **kelompok font**.
- Mau merapikan jarak antarbaris agar tidak terlalu rapat saat dibaca di layar HP? Carilah di **kelompok text**.

Di modul-modul selanjutnya di sub-bab ini, kita akan membahas properti font terlebih dahulu, disusul dengan properti text, sebelum akhirnya menggabungkan keduanya dalam latihan mandiri.
