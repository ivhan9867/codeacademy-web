---
id: web-foundation-pcss-30
courseId: fe-web-foundation-00
order: 86
title: "Latihan: Menetapkan Ukuran pada Gambar"
readingMinutes: 9
section: PENDALAMAN_CSS
learningObjectives:
  - "Mengatur dimensi gambar dengan width dan max-height"
  - "Menjaga rasio gambar tetap proporsional dengan object-fit dan object-position"
  - "Membedakan penerapan ukuran untuk gambar artikel dan gambar logo"
---
Latihan ini terasa familier, bukan? Kita akan menerapkan properti-properti box model yang sudah dipelajari pada elemen gambar di Halaman Profil: gambar-gambar di dalam `<article>` (Sejarah, Geografis, Wisata), dan gambar logo di dalam `<aside>`.

## Menetapkan ukuran gambar artikel

Buka `style.css`, lalu buat rule baru untuk class `.featured-image`:

```css
.featured-image {
  width: 100%;
  max-height: 300px;
}
```

Selector ini belum berlaku ke elemen manapun, karena belum ada elemen dengan class `featured-image`. Tambahkan class tersebut ke setiap `<img>` di dalam elemen `<article>`, kecuali logo di `<aside>`:

```html
<article id="sejarah">
  <h2>Sejarah</h2>
  <img src="assets/image/history.jpg" alt="sejarah" class="featured-image" />
  <!-- ... -->
</article>

<article id="geografis">
  <h2>Geografis</h2>
  <img src="assets/image/geografis.jpg" alt="geografis" class="featured-image" />
  <!-- ... -->
</article>

<article id="wisata">
  <h2>Wisata</h2>
  <section>
    <h3>Farm House Lembang</h3>
    <img src="assets/image/farm-house.jpg" alt="farm house" class="featured-image" />
  </section>
  <section>
    <h3>Observatorium Bosscha</h3>
    <img src="assets/image/bosscha.jpg" alt="bosscha" class="featured-image" />
  </section>
</article>
```

Setelah dijalankan, kamu mungkin akan kaget: gambar tampil memenuhi lebar viewport, tapi rasionya terlihat tidak seimbang. Ini terjadi karena kita menetapkan `width: 100%` sekaligus `max-height: 300px`, sehingga gambar dipaksa masuk ke ukuran kotak yang tidak sama persis dengan rasio aslinya.

## Memperbaiki rasio dengan object-fit dan object-position

Solusinya, tambahkan dua properti berikut:

```css
.featured-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  object-position: center;
}
```

- `object-fit: cover` membuat gambar tetap mempertahankan rasio aslinya, dengan cara memotong (*crop*) bagian yang tidak muat ke dalam kotak.
- `object-position: center` menentukan bagian mana dari gambar yang tetap terlihat setelah dipotong. Nilai `center` berarti bagian tengah gambar yang diprioritaskan.

Dengan kombinasi ini, gambar akan selalu terlihat proporsional, meski ukuran kotaknya tidak sama persis dengan rasio aslinya. Konsekuensinya, sebagian area gambar mungkin terpotong — dan kamu bisa mengatur bagian mana yang dipotong lewat `object-position`, misalnya `top`, `bottom`, `left`, atau `right`.

## Mengatur ukuran logo

Terakhir, atur ukuran logo di `<aside>` agar tidak terlalu besar. Buat rule baru untuk `.profile img`:

```css
.profile img {
  width: 200px;
}
```

Dengan ini, logo akan tampil lebih proporsional dibanding ukuran bawaannya yang terlalu besar.

## Latihan singkat

1. Terapkan `.featured-image` pada seluruh gambar artikel, lalu amati efek `object-fit: cover`.
2. Ganti `object-position: center` dengan `object-position: top`, lalu bandingkan bagian gambar mana yang terlihat.
3. Atur logo di `<aside>` dengan `width: 150px`, lalu bandingkan dengan `200px`.
