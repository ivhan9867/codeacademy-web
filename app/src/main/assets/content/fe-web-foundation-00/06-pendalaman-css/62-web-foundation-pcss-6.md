---
id: web-foundation-pcss-6
courseId: fe-web-foundation-00
order: 62
title: "Pseudo-class dan Pseudo-element Selector"
readingMinutes: 7
section: PENDALAMAN_CSS
codeSnippet: |
  a:hover {
    color: blue;
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
  - "Membedakan pseudo-class (keadaan elemen) dan pseudo-element (bagian dari elemen)"
  - "Menggunakan :hover, :visited, ::before, dan ::after dengan benar"
---
Selector yang sudah kita pelajari memilih elemen berdasarkan **apa yang ada di HTML**. Pseudo selector berbeda: ia memilih sesuatu yang **tidak tertulis** secara langsung di HTML, misalnya keadaan elemen saat disentuh kursor, atau bagian kecil dari sebuah elemen.

Ada dua jenisnya: pseudo-class dan pseudo-element.

## Pseudo-class: memilih berdasarkan keadaan

Pseudo-class memilih elemen berdasarkan **kondisi atau keadaannya**. Penulisannya memakai satu titik dua (`:`).

```css
a:hover {
  color: blue;
}
```

Aturan di atas membuat tautan berubah biru ketika kursor berada di atasnya. Beberapa pseudo-class yang sering dipakai:

- `:hover`: saat kursor berada di atas elemen.
- `:visited`: tautan yang sudah pernah dikunjungi.
- `:focus`: elemen yang sedang aktif menerima input, misalnya kolom isian yang sedang diketik.
- `:first-child` dan `:last-child`: elemen yang menjadi anak pertama atau terakhir dari induknya.

```css
a:visited {
  color: purple;
}

input:focus {
  outline: 2px solid #00a2c6;
}
```

Daftar pseudo-class standar cukup panjang. Kamu bisa melihat referensi lengkapnya di dokumentasi MDN ketika membutuhkannya.

## Pseudo-element: memilih bagian dari elemen

Pseudo-element memilih **bagian tertentu** dari sebuah elemen, atau membuat konten semu yang tampak di halaman tanpa harus menuliskannya di HTML. Penulisannya memakai dua titik dua (`::`).

Yang paling sering dipakai adalah `::before` dan `::after`, yaitu konten semu yang muncul sebelum dan sesudah isi sebuah elemen.

```css
blockquote::before {
  content: "“";
}

blockquote::after {
  content: "”";
}
```

Dengan aturan ini, tanda kutip akan muncul di sekeliling kutipan tanpa harus kamu ketik di HTML. Perlu diingat bahwa properti `content` wajib ada agar pseudo-element tampil, meskipun isinya kosong (`content: "";`).

Ada juga pseudo-element lain, misalnya `::first-line` dan `::first-letter`, yang menargetkan baris pertama atau huruf pertama sebuah paragraf.

```css
p::first-letter {
  font-size: 2em;
  font-weight: bold;
}
```

## Ringkasan perbedaan

| Jenis | Penulisan | Memilih |
|---|---|---|
| Pseudo-class | `:` | Elemen dalam keadaan tertentu |
| Pseudo-element | `::` | Bagian dari elemen atau konten semu |

Memakai `::` untuk pseudo-element bukan kewajiban, tetapi kebiasaan yang baik agar mudah dibedakan dari pseudo-class.

## Latihan singkat

1. Buat tiga tautan. Beri warna berbeda ketika `:hover`, dan warna lain ketika `:visited`.
2. Buat sebuah `<blockquote>` dan tambahkan tanda kutip dengan `::before` dan `::after`.
3. Buat `<input type="text">` dan beri garis tepi berwarna ketika `:focus`. Coba ketik sesuatu di dalamnya dan lihat hasilnya.
