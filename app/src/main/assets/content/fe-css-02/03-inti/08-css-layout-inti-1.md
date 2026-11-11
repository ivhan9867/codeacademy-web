---
id: css-layout-inti-1
courseId: fe-css-02
order: 8
title: "Keluarga Flexbox: Flex Container dan Flex Items"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami hubungan hierarki antara Flex Container (induk) dan Flex Items (anak langsung)"
  - "Mengetahui perubahan seketika elemen anak saat properti display: flex diaktifkan"
  - "Membedakan properti yang ditujukan untuk kontainer versus properti untuk item"
codeLanguage: css
codeSnippet: |
  .flex-container {
    display: flex;
    background-color: #e2f0f1;
    padding: 16px;
    border: 2px dashed #1a6368;
  }

  .flex-item {
    background-color: #1a6368;
    color: white;
    padding: 20px;
    margin: 8px;
    border-radius: 8px;
    font-weight: bold;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Keluarga Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>Sebelum Menjadi Flex Container:</h3>
      <div class="kontainer-biasa">
        <div class="kotak">Item 1 (Blok Biasa)</div>
        <div class="kotak">Item 2 (Blok Biasa)</div>
        <div class="kotak">Item 3 (Blok Biasa)</div>
      </div>

      <h3>Setelah Diberi display: flex:</h3>
      <div class="flex-container">
        <div class="flex-item">Item A</div>
        <div class="flex-item">Item B</div>
        <div class="flex-item">Item C</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  h3 {
    color: #104246;
    margin-top: 20px;
  }

  .kontainer-biasa {
    background-color: #f1ece4;
    padding: 12px;
    border: 2px dashed #968e86;
  }

  .kotak {
    background-color: #6b655d;
    color: white;
    padding: 12px;
    margin: 6px 0;
    border-radius: 6px;
  }

  .flex-container {
    display: flex;
    background-color: #e2f0f1;
    padding: 12px;
    border: 2px dashed #1a6368;
  }

  .flex-item {
    background-color: #1a6368;
    color: white;
    padding: 20px;
    margin: 6px;
    border-radius: 8px;
    font-weight: bold;
    text-align: center;
  }
playgroundTask: "Coba tambahkan satu elemen anak baru '<div class=\"flex-item\">Item D</div>' ke dalam '.flex-container'. Tekan 'Jalankan' dan amati bagaimana Item D otomatis masuk berdampingan tanpa merusak susunan!"
---
Selamat datang di materi inti! Aturan nomor satu dalam Flexbox yang wajib kamu tanamkan di kepala adalah:

> **Flexbox adalah hubungan antara Induk (*Flex Container*) dan Anak Langsung (*Flex Items*).**

Kamu tidak bisa begitu saja memberikan perintah Flexbox pada elemen anak tanpa terlebih dahulu mengaktifkan mode fleksibel pada elemen induk pembungkusnya.

![Hubungan Flex Container dan Flex Items](asset:images/fe-css-02/flex-container-dan-items.png)

## Langkah Pertama: Membuka Gerbang `display: flex`

Untuk mengubah elemen biasa menjadi wadah fleksibel, cukup berikan satu baris CSS ajaib pada elemen pembungkusnya:

```css
.wadah {
  display: flex;
}
```

Seketika setelah baris ini diterapkan, hal-hal luar biasa langsung terjadi pada **anak langsung** di dalamnya:
1. **Berbaris Horizontal**: Semua anak langsung (yang tadinya bertumpuk ke bawah karena bertipe `block`) otomatis berjejer mendatar dari kiri ke kanan.
2. **Menyesuaikan Lebar Konten**: Elemen anak tidak lagi rakus menguasai 100% lebar layar. Mereka menyusut sesuai lebar teks atau konten di dalamnya (kecuali diatur lain).
3. **Tinggi Seragam Secara Otomatis**: Secara default, semua elemen anak akan otomatis meregang (*stretch*) menyamakan tingginya dengan anak yang paling tinggi! Masalah menyamakan tinggi kolom yang dulu menyiksa para pembuat web kini tuntas dalam satu baris.

## Catatan Kritis: Hanya Memengaruhi Anak Langsung (*Direct Children*)

Ingat, sifat Flexbox hanya diturunkan ke **anak tingkat pertama** (*direct children*):

```html
<div class="flex-container"> <!-- Flex Container -->
  <div class="flex-item">    <!-- Flex Item -->
    <p>Teks di dalam</p>      <!-- BUKAN Flex Item, tetap teks biasa -->
  </div>
</div>
```

Jika kamu ingin elemen `<p>` di dalam `.flex-item` ikut menjadi fleksibel, kamu harus mendeklarasikan `display: flex` lagi pada `.flex-item` tersebut (*nested flexbox*).

Mari kita eksplorasi properti pengatur arah alirannya di modul berikutnya!
