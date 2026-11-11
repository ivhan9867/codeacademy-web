---
id: web-foundation-lanjutan-25
courseId: fe-web-foundation-00
order: 48
title: "Special Character"
readingMinutes: 4
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menampilkan karakter khusus HTML seperti <, >, &, dan " menggunakan named entity (&lt;, &gt;, &amp;, &quot;)"
  - "Menampilkan simbol hak cipta (&copy;) dan simbol non-ASCII menggunakan character reference"
codeSnippet: |
  <p>1 &lt; 2 dan 5 &gt; 3</p>
  <p>Harga mulai dari &euro;10</p>
  <p>Profil Rani &copy; 2026. Dibuat dengan penuh semangat belajar&hellip;</p>
---
Pernahkah kamu membayangkan apa yang terjadi jika kamu mengetik simbol `<` langsung di teks artikel? Browser akan mengira kamu sedang membuka tag HTML baru. Akibatnya, struktur halaman bisa rusak. 

Modul ini membahas cara menampilkan simbol-simbol khusus tersebut dengan aman menggunakan **Character Reference**.

## Mengapa Karakter Khusus Diperlukan?

Beberapa karakter memiliki makna khusus dalam sintaks HTML. Karakter lain, seperti simbol hak cipta (©), tidak selalu tersedia di semua tombol keyboard. 

Untuk menampilkannya, HTML menggunakan format kode khusus. Format ini selalu diawali tanda ampersand (`&`) dan diakhiri titik koma (`;`):

- **Named Entity**: ditulis menggunakan nama kata kuncinya, misalnya `&copy;`.
- **Numeric Entity**: ditulis menggunakan nomor kode karakternya, misalnya `&#169;`.

Keduanya menghasilkan simbol yang sama di layar browser.

## 1. Karakter Terlarang HTML (Reserved Characters)

Karakter-karakter ini digunakan langsung oleh mesin browser untuk membaca kode HTML. Oleh sebab itu, jangan pernah mengetiknya secara polos di dalam konten:

- **`&lt;`** : simbol kurang dari (`<`). Singkatan dari *less than*.
- **`&gt;`** : simbol lebih dari (`>`). Singkatan dari *greater than*.
- **`&amp;`** : simbol dan (`&`). Singkatan dari *ampersand*.
- **`&quot;`** : tanda kutip ganda lurus (`"`).
- **`&apos;`** : tanda petik tunggal (`'`).

```html
<!-- Browser membaca ini sebagai teks biasa, bukan sebagai tag -->
<p>Rumus matematika: 5 &lt; 10 dan 20 &gt; 15.</p>
<p>Untuk membuat judul, gunakan tag &lt;h1&gt; di berkas HTML.</p>
```

## 2. Simbol Hak Cipta & Merek Dagang

Kelompok ini paling sering dipakai di bagian footer website untuk menandai status legalitas:

- **`&copy;`** : simbol hak cipta (©).
- **`&reg;`** : simbol merek dagang terdaftar (®).
- **`&trade;`** : simbol merek dagang umum (™).

```html
<footer>
  <p>&copy; 2026 PT Teknologi Nusantara &trade;. Hak cipta dilindungi.</p>
</footer>
```

## 3. Tipografi & Tanda Baca Halus

Kelompok ini membuat tulisan artikel tampak rapi layaknya hasil cetak buku profesional:

- **`&nbsp;`** : spasi yang tidak boleh dipotong baris (*non-breaking space*). Mencegah dua kata terpisah ke baris baru.
- **`&mdash;`** : tanda pisah panjang em-dash (—).
- **`&hellip;`** : tanda elipsis atau tiga titik bersambung (…).
- **`&ldquo;`** dan **`&rdquo;`** : tanda kutip ganda melengkung pembuka dan penutup (“ ”).

```html
<p>Dia berkata, &ldquo;Tunggu sebentar&hellip;&rdquo; lalu melangkah pergi &mdash; tanpa ragu.</p>
<p>Harga paket hemat: Rp&nbsp;50.000 (angka tidak akan terpisah dari kata Rp).</p>
```

## 4. Simbol Mata Uang Internasional

Bila keyboard fisik kamu tidak memiliki tombol mata uang asing, gunakan kode entitas berikut:

- **`&euro;`** : simbol mata uang Euro (€).
- **`&pound;`** : simbol mata uang Poundsterling (£).
- **`&yen;`** : simbol mata uang Yen atau Yuan (¥).

```html
<p>Tarif sewa server Eropa sebesar &euro;25 per bulan.</p>
```

## Kesalahan yang Sering Terjadi

1. **Lupa memberi tanda titik koma (`;`) di akhir**: Browser tidak akan mengenali kode tersebut dan menampilkannya sebagai teks mentah seperti `&copy` saja.
2. **Menulis `<` langsung di teks**: Browser bisa mengira itu adalah tag HTML yang belum ditutup.

## Coba Sendiri

Buka berkas latihan Halaman Profil milikmu. Tambahkan satu baris hak cipta pada bagian paling bawah halaman, misalnya: `Halaman Profil &copy; 2026 Rani`. Periksa hasilnya di browser!
                    
