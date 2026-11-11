---
id: web-foundation-lanjutan-22
courseId: fe-web-foundation-00
order: 45
title: "Formulir: Elemen Input dan Tipe-tipenya"
readingMinutes: 8
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Membuat wadah formulir interaktif dengan elemen <form>"
  - "Mengenal ragam tipe input: text, email, password, number, checkbox, radio, dan submit"
  - "Menghubungkan input dengan teks keterangan menggunakan elemen <label>"
codeSnippet: |
  <div>
    <label for="nama">Nama</label>
    <br>
    <input type="text" id="nama">
  </div>
  
  <div>
    <label for="email">Email</label>
    <br>
    <input type="email" id="email">
  </div>
  
  <div>
    <label for="pesan">Pesan</label>
    <br>
    <textarea id="pesan" rows="4" cols="30"></textarea>
  </div>
---
Sejak awal belajar, kamu sudah membuat halaman yang **menampilkan** informasi. Sekarang giliran halaman yang bisa **menerima** informasi dari pengunjungnya, lewat formulir.

## Kenapa formulir penting

Hampir setiap kali kamu mengisi data di sebuah website (login, mendaftar akun, mengisi komentar), kamu sedang berinteraksi dengan formulir. Formulir adalah cara utama sebuah halaman web menangkap masukan dari penggunanya.

## Elemen input dan tipe-tipenya

Elemen `<input>` adalah elemen paling sering dipakai untuk menangkap data. Bedanya dari elemen lain: `<input>` punya banyak sekali **tipe**, diatur lewat atribut `type`.

```html
<input type="email">
<input type="password">
```

Beberapa tipe yang umum dipakai:

- **text** (bawaan bila `type` tidak diisi): teks satu baris
- **password**: sama seperti text, tapi karakternya disembunyikan jadi bintang
- **email**: khusus format email, browser akan menandai error kalau formatnya salah
- **number**: hanya menerima angka
- **search**: untuk pencarian, biasanya ada ikon hapus teks
- **date**, **time**, **datetime-local**: untuk tanggal dan waktu, disertai popup bantu pengisian
- **checkbox**: kotak centang
- **radio**: pilihan tunggal dari beberapa opsi; beberapa `radio` dengan `name` yang sama membentuk satu kelompok pilihan
- **range**: memilih angka lewat slider, dibatasi rentang tertentu
- **color**: memilih warna lewat color picker
- **file**: memilih berkas dari perangkat
- **submit**: tombol untuk mengirim formulir
- **hidden**: data tersembunyi, tidak terlihat pengguna, tapi tetap ikut terkirim

Perhatikan: `<input>` tetap satu baris teks saja, bahkan kalau kamu menekan Enter di dalamnya saat mengetik. Baris barunya akan otomatis dihapus.

## Textarea: input banyak baris

Untuk input yang butuh banyak baris (komentar, deskripsi panjang), pakai `<textarea>`, bukan `<input>`. Berbeda dari `<input>`, `<textarea>` punya tag penutup dan isinya ditulis di antara tag pembuka dan penutup.

```html
<textarea rows="4" cols="30">Tulis pesanmu di sini</textarea>
```

Atribut `rows` dan `cols` mengatur tinggi dan lebar area teksnya.

## Label: keterangan untuk input

Elemen `<label>` memberi keterangan pada sebuah input. Dua manfaatnya:

- Pembaca layar bisa menjelaskan fungsi input itu kepada penggunanya
- Mengklik teks label akan otomatis memfokuskan kursor ke input terkait

Cara menghubungkannya: beri `id` pada `<input>`, lalu beri atribut `for` pada `<label>` dengan nilai yang **sama persis** dengan `id` tadi.

```html
<label for="nama">Nama</label>
<br>
<input type="text" id="nama">
```

`placeholder` (dibahas modul berikutnya) bukan pengganti `<label>`. `placeholder` cuma petunjuk contoh isian yang hilang saat mulai diketik, sedangkan `<label>` adalah keterangan permanen.

## Kesalahan yang sering terjadi

- Memakai `<input>` untuk teks panjang, padahal seharusnya `<textarea>`
- Nilai `for` pada `<label>` tidak sama persis dengan `id` pada input pasangannya
- Mengira `placeholder` bisa menggantikan `<label>`

## Coba sendiri

Buat satu pasang `<label>` dan `<input type="text">` untuk mengisi "Kota Asal", terhubung dengan `id` dan `for` yang sesuai.
                    
