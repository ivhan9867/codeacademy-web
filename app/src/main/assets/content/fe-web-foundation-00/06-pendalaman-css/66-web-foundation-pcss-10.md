---
id: web-foundation-pcss-10
courseId: fe-web-foundation-00
order: 66
title: "Text Styling: Line Height, Text Indent, dan Text Alignment"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  /* Cara 1: angka tanpa satuan (dikalikan font-size) */
  p { font-size: 16px; line-height: 2; }

  /* Cara 2: persentase */
  p { font-size: 16px; line-height: 200%; }

  /* Cara 3: nilai absolut */
  p { font-size: 16px; line-height: 32px; }
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
  - "Mengatur jarak antarbaris dengan line-height"
  - "Membuat indentasi dan hanging indent dengan text-indent"
  - "Mengatur perataan teks dengan text-align"
---
## Text: tata letak dan gaya paragraf

### line-height

`line-height` mengatur jarak antarbaris. Nilainya adalah **jarak minimal** dari baris ke baris. Jika ada karakter yang lebih tinggi dari biasanya dalam satu baris, tinggi baris akan menyesuaikan agar tidak terpotong.

Ada tiga cara umum menulisnya. Ketiganya akan menghasilkan jarak dua kali ukuran font pada contoh ini:

```css
/* Cara 1: angka tanpa satuan (dikalikan font-size) */
p { font-size: 16px; line-height: 2; }

/* Cara 2: persentase */
p { font-size: 16px; line-height: 200%; }

/* Cara 3: nilai absolut */
p { font-size: 16px; line-height: 32px; }
```

Cara pertama paling disarankan. Karena tidak punya satuan, nilainya ikut menyesuaikan jika ukuran font berubah di tempat lain.

### text-indent

`text-indent` menggeser **baris pertama** sebuah paragraf, mirip dengan indentasi di dokumen cetak.

```css
p {
  text-indent: 2em;
}
```

Nilainya bisa berupa `px`, `em`, `in`, atau `%`. Persentase dihitung dari lebar elemen induk.

Nilai negatif juga diperbolehkan. Hasilnya adalah *hanging indent*, yaitu baris pertama menjorok keluar dari batas kiri paragraf. Efek ini sering dipakai pada daftar referensi.

Perlu diingat, properti ini hanya memengaruhi baris pertama. Untuk menggeser seluruh baris, gunakan `margin` atau `padding`, yang akan kita bahas di box model.

### text-align

`text-align` mengatur perataan teks dalam satu blok:

- `left` (default untuk bahasa yang dibaca dari kiri ke kanan)
- `right`
- `center`
- `justify`: teks rata kiri dan kanan, dengan spasi disesuaikan.

```css
h1 {
  text-align: center;
}
```

Nilai default `left` akan berubah menjadi `right` jika bahasa halaman memakai arah baca dari kanan ke kiri, misalnya bahasa Arab.
