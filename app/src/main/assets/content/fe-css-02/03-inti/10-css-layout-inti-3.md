---
id: css-layout-inti-3
courseId: fe-css-02
order: 10
title: "Meratakan Sumbu Utama dengan justify-content"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami peran properti justify-content dalam mendistribusikan ruang kosong pada Main Axis"
  - "Menguasai 6 variasi nilai utama: flex-start, center, flex-end, space-between, space-around, dan space-evenly"
  - "Mengetahui kasus penggunaan nyata untuk navbar, tombol pagination, dan widget"
codeLanguage: css
codeSnippet: |
  .navigasi {
    display: flex;
    justify-content: space-between; /* Mendorong logo ke kiri & menu ke kanan */
    align-items: center;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji justify-content</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Eksperimen justify-content</h2>
      <div class="wadah">
        <div class="item item-1">Logo</div>
        <div class="item item-2">Pencarian</div>
        <div class="item item-3">Akun</div>
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
    justify-content: flex-start; /* Ubah nilai ini! */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
    min-height: 80px;
  }

  .item {
    padding: 12px 20px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    text-align: center;
  }

  .item-1 { background-color: #1a6368; }
  .item-2 { background-color: #df7b56; }
  .item-3 { background-color: #287948; }
playgroundTask: "Coba ubah nilai 'justify-content: flex-start;' pada selector '.wadah' menjadi 'space-between', lalu tekan 'Jalankan'. Perhatikan bagaimana Logo melompat ke ujung kiri dan Akun melompat ke ujung kanan! Setelah itu, coba nilai 'center' dan 'space-evenly'."
---
Saat total lebar elemen anak lebih kecil daripada lebar kontainer induknya, akan ada **sisa ruang kosong**.

Bagaimana cara kita membagikan atau menaruh sisa ruang kosong tersebut di sepanjang Sumbu Utama (*Main Axis*)? Jawabannya adalah properti **`justify-content`**.

> **Ilustrasi Konsep:** Variasi Nilai justify-content pada Sumbu Utama
## 6 Nilai Utama `justify-content`

Mari kita bedah perilakunya saat `flex-direction: row` (sumbu horizontal):

### 1. `flex-start` (Default)
Semua item berkumpul rapat di awal sumbu utama (ujung kiri). Sisa ruang kosong dibiarkan menumpuk di ujung kanan.

### 2. `center`
Semua item berkumpul rapat persis di tengah-tengah kontainer. Ruang kosong dibagi sama rata di sisi paling kiri dan paling kanan. Sangat cocok untuk bilah tombol angka pagination atau judul banner.

### 3. `flex-end`
Semua item didorong berkumpul di akhir sumbu utama (ujung kanan). Sangat sering dipakai untuk tombol "Kirim" dan "Batal" di sudut kanan bawah modal dialog.

### 4. `space-between` (Favorit Pengembang Web!)
Item pertama menempel di ujung paling kiri, item terakhir menempel di ujung paling kanan, dan sisa ruang kosong dibagi rata persis di antara item-item yang berada di tengah. Inilah resep legendaris untuk membuat **Header Website** (logo di kiri, navigasi di kanan)!

### 5. `space-around`
Setiap item mendapatkan jatah ruang kosong yang sama di sisi kiri dan kanannya. Akibatnya, jarak antar-item di bagian tengah tampak dua kali lebih lebar daripada jarak item terluar ke dinding kontainer.

### 6. `space-evenly`
Seluruh celah kosong (jarak dari dinding tepi ke item pertama, jarak antar-item, hingga jarak dari item terakhir ke dinding tepi) dibagi **benar-benar sama persis**. Tampilan menjadi sangat simetris dan harmonis.

Cobalah bereksperimen dengan keenam nilai di atas pada Playground untuk melihat perbedaannya secara langsung!
