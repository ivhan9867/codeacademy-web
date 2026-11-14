---
id: css-layout-inti-2
courseId: fe-css-02
order: 9
title: "Arah Aliran: Mengendalikan flex-direction"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami konsep Main Axis (Sumbu Utama) dan Cross Axis (Sumbu Silang)"
  - "Menguasai 4 nilai properti flex-direction: row, row-reverse, column, column-reverse"
  - "Mengetahui bagaimana pergantian sumbu memengaruhi arah perataan elemen anak"
codeLanguage: css
codeSnippet: |
  .wadah-flex {
    display: flex;
    flex-direction: column; /* Mengubah arah sumbu ke bawah */
    gap: 12px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji flex-direction</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Eksperimen Arah Flexbox</h2>
      <div class="wadah">
        <div class="kartu kartu-1">Langkah 1</div>
        <div class="kartu kartu-2">Langkah 2</div>
        <div class="kartu kartu-3">Langkah 3</div>
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
    display: flex;
    flex-direction: row; /* Nilai default: kiri ke kanan */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    padding: 16px 24px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    margin: 4px;
    text-align: center;
  }

  .kartu-1 { background-color: #1a6368; }
  .kartu-2 { background-color: #df7b56; }
  .kartu-3 { background-color: #d49438; }
playgroundTask: "Coba ubah 'flex-direction: row;' di selector '.wadah' menjadi 'flex-direction: column;'. Jalankan kode dan perhatikan bagaimana langkah 1, 2, dan 3 kini berbaris ke bawah seperti menu vertikal! Setelah itu, coba ganti menjadi 'row-reverse' untuk membalik urutan langkah."
---
Untuk menguasai Flexbox, ada satu kunci rahasia yang harus selalu kamu bayangkan di kepala: **Sumbu Utama (*Main Axis*) dan Sumbu Silang (*Cross Axis*)**.

Arah sumbu ini dikendalikan oleh properti **`flex-direction`**.

> **Ilustrasi Konsep:** Konsep Main Axis dan Cross Axis pada Flexbox
## Dua Sumbu yang Berputar Bersama

1. **Main Axis (Sumbu Utama)**:
   Arah utama tempat elemen-elemen flex mengalir dan berbaris.
2. **Cross Axis (Sumbu Silang)**:
   Garis tegak lurus (90 derajat) terhadap sumbu utama.

Ketika kamu mengubah nilai `flex-direction`, kamu sebenarnya sedang memutar arah sumbu utama:

### 1. `flex-direction: row` (Bawaan Default)
- **Main Axis**: Horizontal (mendatar dari kiri ke kanan).
- **Cross Axis**: Vertikal (tegak lurus dari atas ke bawah).
- Elemen anak berbaris rapi seperti deretan kata dalam kalimat.

### 2. `flex-direction: row-reverse`
- **Main Axis**: Horizontal terbalik (dari kanan ke kiri).
- Elemen anak dibalik urutan tampilannya: elemen pertama berada di ujung kanan, dan elemen terakhir berada di kiri.

### 3. `flex-direction: column`
- **Main Axis**: Vertikal (menurun dari atas ke bawah).
- **Cross Axis**: Horizontal (tegak lurus dari kiri ke kanan).
- Elemen anak mengalir ke bawah seperti kartu formulir atau menu vertikal di layar ponsel.

### 4. `flex-direction: column-reverse`
- **Main Axis**: Vertikal terbalik (mengalir dari bawah ke atas).
- Elemen pertama berada di dasar kontainer, elemen berikutnya menumpuk ke atasnya. Sangat sering dipakai untuk antarmuka *chat bubble* atau log pesan.

Ingat baik-baik arah sumbu ini, karena di modul berikutnya, properti perataan kita (`justify-content`) akan selalu bekerja **searah dengan Main Axis** apa pun arahnya!
