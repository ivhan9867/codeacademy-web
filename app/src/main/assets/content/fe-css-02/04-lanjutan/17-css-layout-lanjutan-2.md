---
id: css-layout-lanjutan-2
courseId: fe-css-02
order: 21
title: "Membagi Lajur: grid-template-columns & grid-template-rows"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Menguasai sintaks properti grid-template-columns dan grid-template-rows"
  - "Menentukan ukuran lajur kolom/baris menggunakan piksel (px) dan persentase (%)"
  - "Menerapkan gap untuk memisahkan baris dan kolom secara rapi"
codeLanguage: css
codeSnippet: |
  .tata-letak {
    display: grid;
    grid-template-columns: 200px 1fr; /* Kolom 1 tetap 200px, kolom 2 sisa layar */
    grid-template-rows: 60px auto 50px;
    gap: 16px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji grid-template</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Membagi Kolom dan Baris</h2>
      <div class="wadah-grid">
        <div class="kotak">Kolom 1</div>
        <div class="kotak">Kolom 2</div>
        <div class="kotak">Kolom 3</div>
        <div class="kotak">Kolom 4</div>
        <div class="kotak">Kolom 5</div>
        <div class="kotak">Kolom 6</div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah-grid {
    display: grid;
    /* Tentukan jumlah kolom di sini: */
    grid-template-columns: 120px 180px 100px;
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kotak {
    background-color: #df7b56;
    color: white;
    font-weight: bold;
    padding: 20px 10px;
    border-radius: 6px;
    text-align: center;
  }
playgroundTask: "Coba ubah 'grid-template-columns' menjadi '33.3% 33.3% 33.3%;'. Jalankan kode dan amati bagaimana 3 kolom tersebut membagi lebar kontainer secara proporsional! Lalu coba tambahkan 'grid-template-rows: 100px 50px;'."
---
Setelah menyalakan `display: grid`, langkah berikutnya adalah memberitahu browser:
> *"Berapa banyak kolom dan baris yang kamu inginkan, dan berapa lebar masing-masing lajur tersebut?"*

Pertanyaan ini dijawab oleh dua properti fundamental: **`grid-template-columns`** dan **`grid-template-rows`**.

```text
grid-template-columns: 200px 1fr 200px;
+------------+------------------------+------------+
|   200px    |     1fr (Sisa Ruang)   |   200px    |
+------------+------------------------+------------+
```
## Menentukan Kolom: `grid-template-columns`

Setiap nilai yang kamu pisahkan dengan spasi mewakili **satu lajur kolom**:

```css
/* Membentuk 3 kolom: kolom pertama 100px, kedua 200px, ketiga 150px */
.wadah {
  display: grid;
  grid-template-columns: 100px 200px 150px;
}
```

Jika di dalam kontainer ada 6 elemen anak:
- Elemen 1, 2, 3 akan menempati baris pertama (kolom 1, 2, 3).
- Elemen 4, 5, 6 akan otomatis turun dan menempati baris kedua (kolom 1, 2, 3).

## Menentukan Baris: `grid-template-rows`

Sama halnya dengan kolom, kamu bisa mengunci tinggi masing-masing baris:

```css
/* Baris 1 setinggi 80px, baris 2 setinggi 160px */
.wadah {
  display: grid;
  grid-template-rows: 80px 160px;
}
```

Jika kontennya melebihi jumlah baris yang kamu tentukan secara manual, browser akan membuat **baris implisit (*implicit rows*)** secara otomatis dengan tinggi mengikuti isi kontennya.

## Memberi Jarak: `gap`

Sama seperti di Flexbox, kamu bisa memberikan celah pemisah antar-sel tanpa repot:

```css
.wadah {
  gap: 16px;          /* Jarak 16px antar-kolom dan antar-baris */
  row-gap: 20px;      /* Khusus jarak antar-baris */
  column-gap: 10px;   /* Khusus jarak antar-kolom */
}
```

Namun, memakai satuan piksel (`px`) atau persen (`%`) sering kali merepotkan saat memperhitungkan `gap`. Di modul berikutnya, kita akan berkenalan dengan satuan pahlawan sejati CSS Grid: **satuan `fr`**!
