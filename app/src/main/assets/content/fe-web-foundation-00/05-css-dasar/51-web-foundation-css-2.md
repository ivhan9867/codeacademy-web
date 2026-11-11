---
id: web-foundation-css-2
courseId: fe-web-foundation-00
order: 51
title: "Menulis CSS Rule"
readingMinutes: 5
section: CSS_DASAR
codeSnippet: |
  h1 {
    color: green;
  }

  p {
    font-size: small;
    font-family: sans-serif;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan CSS Rule</title>
    </head>
    <body>
      <h1>Judul Halaman</h1>
      <p>Ini adalah paragraf pertama.</p>
    </body>
  </html>
learningObjectives:
  - "Memahami struktur dasar sebuah CSS rule: selector dan declaration block"
  - "Menulis selector dan declaration dengan syntax yang benar"
---
Satu aturan styling di CSS disebut **rule**. Tiap rule punya dua bagian: **selector** (elemen HTML mana yang mau di-styling) dan **declaration block** (styling apa yang diterapkan).

Lihat contoh kodenya. Rule pertama bilang "semua elemen `<h1>` warnanya hijau". Rule kedua bilang "semua elemen `<p>` ukuran fontnya kecil, pakai font sans-serif".

Beberapa aturan penting soal syntax-nya:
- Selector ditulis duluan, diikuti kurung kurawal `{ }`.
- Di dalam kurung kurawal, tiap declaration berbentuk `properti: nilai;` — jangan lupa titik koma di akhir tiap baris.
- Kamu bisa nulis lebih dari satu declaration dalam satu rule, seperti contoh `<p>` di atas yang punya dua properti sekaligus.
- CSS gak peduli spasi atau baris baru (mirip bahasa pemrograman lain), tapi sebaiknya satu declaration satu baris biar gampang dibaca.

Satu jebakan yang sering bikin bingung pemula: nilai dengan satuan (kayak `px`, `em`) gak boleh ada spasi di antara angka dan satuannya. `margin: 2em;` itu benar, tapi `margin: 2 em;` gak akan jalan sama sekali — browser bakal nganggep declaration-nya invalid dan ngabaikan seluruhnya.

Kamu juga bisa nulis komentar di CSS pakai `/* ... */`, persis kayak di HTML tapi bentuknya beda. Apapun yang ditulis di antara `/*` dan `*/` bakal diabaikan browser — berguna buat ngasih catatan di kode kamu sendiri.
