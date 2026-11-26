---
id: css-layout-lanjutan-3
courseId: fe-css-02
order: 22
title: "Kekuatan Fraksi dan Pengulangan: Satuan fr & Fungsi repeat()"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami cara kerja satuan fraksi (fr) dalam mendistribusikan sisa ruang kosong secara otomatis"
  - "Mengombinasikan satuan tetap (px) dengan satuan fraksi (fr)"
  - "Meringkas deklarasi kolom berulang menggunakan fungsi repeat()"
codeLanguage: css
codeSnippet: |
  .galeri-modern {
    display: grid;
    grid-template-columns: repeat(3, 1fr); /* 3 kolom sama lebar yang fleksibel */
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji satuan fr & repeat()</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Pemanfaatan fr & repeat()</h2>
      <div class="wadah">
        <div class="item">1</div>
        <div class="item">2</div>
        <div class="item">3</div>
        <div class="item">4</div>
        <div class="item">5</div>
        <div class="item">6</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah {
    display: grid;
    /* Ubah kode di bawah ini memakai fr dan repeat(): */
    grid-template-columns: 1fr 2fr 1fr;
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .item {
    background-color: #1a6368;
    color: white;
    font-weight: bold;
    padding: 24px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Coba ubah 'grid-template-columns: 1fr 2fr 1fr;' menjadi 'repeat(4, 1fr);'. Tekan 'Jalankan' dan amati bagaimana kontainer langsung membelah layar menjadi 4 kolom yang sama rata dengan sintaks yang sangat singkat dan bersih!"
---
Di masa lalu saat memakai persentase (`33.33%`), jika kamu menambahkan `gap: 20px;`, total lebarnya akan menjadi lebih dari 100% dan membuat halamanmu jebol ke samping (*horizontal scroll*). Kamu harus menghitung rumus rumit `calc((100% - 40px) / 3)`.

CSS Grid menyelesaikan penderitaan ini dengan menciptakan satuan baru: **`fr` (Fractional Unit / Unit Fraksi)**.

```text
grid-template-columns: repeat(3, 1fr); gap: 16px;
+---------------+  +---------------+  +---------------+
|      1fr      |  |      1fr      |  |      1fr      |
+---------------+  +---------------+  +---------------+
```
## Apa itu Satuan `fr`?

Satu unit `1fr` mewakili **"satu bagian dari sisa ruang kosong yang tersedia"**.

Browser akan terlebih dahulu mengurangi lebar total kontainer dengan ukuran piksel tetap dan jarak `gap`. Sisa ruang yang ada kemudian dibagi-bagi sesuai jumlah angka `fr`:

```css
/* Membagi ruang menjadi 4 bagian:
   - Kolom 1 dapat 1 bagian
   - Kolom 2 dapat 2 bagian (dua kali lebih lebar)
   - Kolom 3 dapat 1 bagian */
.grid {
  grid-template-columns: 1fr 2fr 1fr;
}
```

Bahkan kamu bisa memadukan satuan tetap dengan `fr`:

```css
/* Sidebar tetap selebar 250px, sedangkan area konten mengambil SEMUA sisa layar yang tersisa */
.tata-letak {
  grid-template-columns: 250px 1fr;
}
```
Tidak perlu lagi menghitung pengurangan matematika manual!

## Meringkas Kode dengan `repeat()`

Bagaimana jika kamu ingin membuat layout dengan 12 kolom? Apakah kamu harus mengetik `1fr 1fr 1fr...` sebanyak dua belas kali? Tentu tidak!

Gunakan fungsi bawaan **`repeat(jumlah_pengulangan, ukuran)`**:

```css
/* Cara melelahkan: */
grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr;

/* Cara elegan dan ringkas: */
grid-template-columns: repeat(6, 1fr);
```

Bahkan kamu bisa mengulang pola:

```css
/* Menghasilkan 4 kolom: 100px 1fr 100px 1fr */
grid-template-columns: repeat(2, 100px 1fr);
```

Sangat bersih, mudah dibaca, dan mudah diubah sewaktu-waktu.
