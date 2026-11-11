---
id: web-foundation-inti-4
courseId: fe-web-foundation-00
order: 19
title: "Latihan: Menandai Konten dengan Elemen"
readingMinutes: 7
section: INTI
codeLanguage: html
isProjectContinuation: true
verificationRules:
  - "<h1>"
  - "<h2>"
  - "<p>"
learningObjectives:
  - "Mempraktikkan penandaan teks profil menggunakan elemen heading dan paragraf"
  - "Melihat perubahan tampilan teks setelah dibungkus elemen HTML di Playground"
  - "Memahami cara browser membedakan judul dan paragraf berdasarkan tag penanda"
codeSnippet: |
  <!DOCTYPE html>
  <html>
    <head>
      <meta charset="utf-8">
      <title>Profil Rani</title>
    </head>
    <body>
      <h1>Profil Rani</h1>
      <p>Pelajar dan calon web developer dari Semarang.</p>
  
      <h2>Tentang Saya</h2>
      <p>
        Aku suka menggambar dan bermain gitar. Sejak kecil aku penasaran
        bagaimana sebuah aplikasi dibuat.
      </p>
  
      <h2>Hobi</h2>
      <h3>Menggambar</h3>
      <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
      <h3>Bermain Gitar</h3>
      <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
    </body>
  </html>
---
Di latihan pertama, halaman profilmu menyatu jadi satu blok karena browser tidak tahu peran tiap bagian. Sekarang kita perbaiki dengan **menandai** setiap bagian memakai elemen HTML.

## Alur latihan

1. Tulis kerangka dokumen HTML
2. Pindahkan semua teks ke dalam bagian body
3. Bungkus setiap bagian teks dengan elemen yang tepat
4. Tambahkan pengaturan dasar di bagian head
5. Jalankan dan bandingkan dengan hasil sebelumnya

Ketik ulang kodenya sendiri di Playground, jangan hanya membaca.

## Langkah 1: kerangka dokumen

Setiap dokumen HTML dimulai dari kerangka yang sama:

```html
<!DOCTYPE html>
<html>
  <head>
  </head>
  <body>
    <!-- semua konten yang tampil ditulis di sini -->
  </body>
</html>
```

Penjelasan singkatnya: `<!DOCTYPE html>` memberi tahu browser bahwa ini dokumen HTML modern, `<html>` membungkus seluruh dokumen, `<head>` menyimpan informasi tentang halaman (tidak tampil), dan `<body>` menyimpan semua yang tampil di layar.

## Langkah 2 dan 3: mengenali peran tiap bagian

Cara berpikirnya: untuk setiap potongan teks, tanyakan **"apa peran teks ini?"** lalu pilih elemen yang cocok.

- "Profil Rani" adalah judul utama halaman, jadi memakai `<h1>`
- "Tentang Saya" dan "Hobi" adalah subjudul, jadi memakai `<h2>`
- "Menggambar" dan "Bermain Gitar" adalah bagian di dalam Hobi, jadi memakai `<h3>`
- Kalimat-kalimat biasa adalah paragraf, jadi memakai `<p>`

Judul berjenjang dari `h1` sampai `h6`, makin besar angkanya makin rendah tingkatnya. Aturan sehatnya: gunakan `h1` satu kali saja per halaman, `h2` untuk bagian besar di bawahnya, dan `h3` untuk bagian di bawah `h2`. Jangan melompat tingkat hanya karena ingin huruf lebih kecil. Ukuran huruf urusan CSS, sedangkan tingkat judul menyatakan **hierarki isi**.

## Langkah 4: pengaturan di head

Dua hal yang perlu ada di `<head>` sejak awal:

- `<meta charset="utf-8">` menentukan cara karakter disimpan dan dibaca, supaya huruf dan simbol khusus tampil benar
- `<title>` berisi judul halaman, yang muncul di tab browser, bookmark, dan hasil pencarian. Ini bukan judul yang tampil di dalam halaman

## Bedah kode

Lihat hasil akhirnya di bagian *Contoh Kode Praktik* di bawah. Perhatikan tiga hal:

- Semua teks sekarang berada di dalam `<body>`, dan setiap teks punya elemen sendiri
- Indentasi memakai dua spasi. Browser tidak peduli soal itu, tapi manusia yang membaca kodenya sangat terbantu
- Hasilnya di browser: judul dan subjudul tampil besar dan tebal, paragraf terpisah rapi. Ini berbeda jauh dari blok teks tadi, padahal isi kalimatnya sama persis

## Kesalahan yang sering terjadi

- Menaruh konten di dalam `<head>`, sehingga teksnya tidak tampil di halaman
- Memakai `h1` berkali-kali, atau memilih `h3` hanya karena hurufnya lebih kecil
- Lupa menutup elemen, misalnya menulis `<p>` tanpa `</p>`

## Coba sendiri

Tambahkan bagian baru bernama "Cita-cita" sebagai `<h2>`, diikuti satu `<p>` berisi kalimat tentang cita-citamu. Letakkan di antara bagian Tentang Saya dan Hobi.
                    
