---
id: web-foundation-pcss-11
courseId: fe-web-foundation-00
order: 67
title: "Text Styling: Text Decoration, Text Transform, Spacing, dan Text Shadow"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  a {
    text-decoration: none;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Area Latihan</title>
    </head>
    <body>
      <header>
        <h1>Judul Contoh</h1>
        <p>Paragraf contoh untuk mencoba properti CSS di atas.</p>
      </header>
      <nav>
        <ul>
          <li><a href="#">Beranda</a></li>
          <li><a href="#">Tentang</a></li>
          <li><a href="#">Kontak</a></li>
        </ul>
      </nav>
      <main>
        <div class="box">
          <span>Teks di dalam span</span>
          <p>Paragraf lain di dalam sebuah div.</p>
          <img src="https://placehold.co/150" alt="Contoh gambar" />
        </div>
      </main>
    </body>
  </html>
learningObjectives:
  - "Menghilangkan atau mengubah dekorasi teks dengan text-decoration"
  - "Mengubah kapitalisasi dengan text-transform tanpa mengubah HTML"
  - "Mengatur jarak huruf dan kata, serta bayangan teks"
---
## Dekorasi dan transformasi teks

### text-decoration

`text-decoration` menambahkan garis pada teks:

- `underline`: garis bawah.
- `overline`: garis atas.
- `line-through`: garis coret.
- `none`: menghilangkan garis.

Pemakaian paling umum adalah menghilangkan garis bawah default pada tautan:

```css
a {
  text-decoration: none;
}
```

Hati-hati ketika menghilangkan garis bawah tautan. Pengunjung tetap perlu tahu bagian mana yang bisa diklik, jadi beri tanda lain, misalnya warna atau hover.

### text-transform

`text-transform` mengubah kapitalisasi **tanpa mengubah isi HTML**:

- `none`: tampil seperti yang ditulis.
- `capitalize`: huruf pertama setiap kata menjadi kapital.
- `lowercase`: semua huruf kecil.
- `uppercase`: semua huruf kapital.

```css
h2 {
  text-transform: uppercase;
}
```

Keuntungan utamanya, teks asli tetap tertulis normal di HTML. Ini penting untuk pencarian dan pembacaan oleh pembaca layar.

### letter-spacing dan word-spacing

- `letter-spacing` mengatur jarak antarhuruf.
- `word-spacing` mengatur jarak antarkata.

```css
h1 {
  letter-spacing: 2px;
}

p {
  word-spacing: 4px;
}
```

Keduanya sangat berguna untuk judul besar. Judul dengan `letter-spacing` sedikit lebar sering terlihat lebih elegan.

### text-shadow

`text-shadow` memberi bayangan pada teks. Nilainya berisi empat bagian, dengan urutan berikut:

1. Jarak horizontal (kiri atau kanan)
2. Jarak vertikal (atas atau bawah)
3. Tingkat keburaman (opsional)
4. Warna bayangan

```css
h1 {
  text-shadow: 2px 2px 4px #888;
}
```

Bayangan yang terlalu tajam atau terlalu gelap justru membuat teks sulit dibaca. Pakai secukupnya.
