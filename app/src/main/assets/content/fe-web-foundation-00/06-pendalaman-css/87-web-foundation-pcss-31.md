---
id: web-foundation-pcss-31
courseId: fe-web-foundation-00
order: 87
title: "Latihan: Menerapkan Card Style pada Elemen Article"
readingMinutes: 8
section: PENDALAMAN_CSS
learningObjectives:
  - "Membuat efek kartu dengan box-shadow dan border-radius"
  - "Menata jarak konten dalam kartu dengan padding dan margin"
---
Sekarang giliran membuat tampilan yang lebih menarik: efek **kartu** (*card*) pada setiap bagian artikel, lengkap dengan bayangan lembut dan sudut yang tumpul.

## Menambahkan efek kartu

Buat rule baru dengan selector `.card`:

```css
.card {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 5px;
}
```

Selector ini belum berlaku ke mana pun. Tambahkan class `card` ke setiap elemen `<article>`, termasuk `<article class="profile">` di dalam `<aside>`:

```html
<div id="content">
  <article id="sejarah" class="card">
    <h2>Sejarah</h2>
    <img src="assets/image/history.jpg" alt="sejarah" />
  </article>

  <article id="geografis" class="card">
    <h2>Geografis</h2>
    <img src="assets/image/geografis.jpg" alt="geografis" />
  </article>

  <article id="wisata" class="card">
    <h2>Wisata</h2>
    <p>Sejak dibukanya Jalan Tol Cipularang, kota Bandung telah menjadi tujuan utama dalam menikmati liburan akhir pekan.</p>
  </article>
</div>

<aside>
  <article class="profile card">
    <header>
      <h2>Kota Bandung</h2>
      <p>Kota Kembang Paris van Java</p>
      <figure>
        <img src="assets/image/bandung-badge.png" />
        <figcaption>Lambang</figcaption>
      </figure>
    </header>
    <section>
      <!-- ... -->
    </section>
  </article>
</aside>
```

Jalankan halaman di browser. Setiap `<article>` sekarang akan dikelilingi bayangan lembut dengan sudut yang tumpul. Jika bayangannya terasa kurang terlihat, kamu bisa menyesuaikan nilainya sendiri. Untuk mempermudah bereksperimen, kamu bisa memakai tools *Box Shadow Generator* yang tersedia online untuk melihat hasil secara visual sebelum menyalin nilainya ke kode.

## Memberi ruang pada konten kartu

Konten di dalam kartu masih terasa sempit karena menempel ke tepi. Tambahkan `padding` dan `margin-top` pada rule `.card` yang sama:

```css
.card {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 5px;

  padding: 20px;
  margin-top: 20px;
}
```

Sekarang konten di dalam setiap kartu punya ruang napas, dan jarak antar kartu juga lebih terlihat jelas. Kalau kamu merasa ukuran padding atau margin ini terlalu kecil atau terlalu besar, jangan ragu menyesuaikannya sesuai selera.

## Latihan singkat

1. Terapkan class `card` ke seluruh `<article>`, termasuk profil di `<aside>`.
2. Ubah nilai `box-shadow` agar bayangannya lebih tegas, misalnya `0 6px 16px 0 rgba(0, 0, 0, 0.3)`.
3. Eksperimen dengan `padding` yang lebih besar (misalnya `32px`) dan amati bagaimana kartu terasa lebih lega.
