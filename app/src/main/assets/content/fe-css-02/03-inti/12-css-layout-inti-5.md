---
id: css-layout-inti-5
courseId: fe-css-02
order: 14
title: "Bungkus Rapi Baris Jamak: flex-wrap & gap"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Memahami perilaku default Flexbox yang memaksakan semua item dalam satu baris (nowrap)"
  - "Mengizinkan elemen berlipat ke baris berikutnya menggunakan flex-wrap: wrap"
  - "Menggunakan properti modern gap untuk memberi jarak antar-item secara bersih tanpa margin hack"
codeLanguage: css
codeSnippet: |
  .daftar-tag {
    display: flex;
    flex-wrap: wrap; /* Izinkan pindah baris jika layar sempit */
    gap: 12px;       /* Jarak horizontal dan vertikal sekaligus */
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji flex-wrap & gap</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Kategori Topik Belajar</h2>
      <div class="wadah-kategori">
        <span class="chip">HTML5</span>
        <span class="chip">CSS3</span>
        <span class="chip">Flexbox</span>
        <span class="chip">CSS Grid</span>
        <span class="chip">Responsive Web</span>
        <span class="chip">JavaScript</span>
        <span class="chip">DOM API</span>
        <span class="chip">Web Design</span>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
  }

  .wadah-kategori {
    display: flex;
    flex-wrap: nowrap; /* Nilai default: memaksakan sebaris */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
    width: 320px; /* Lebar dibatasi agar terjadi overflow */
  }

  .chip {
    background-color: #1a6368;
    color: white;
    padding: 8px 14px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: 500;
    white-space: nowrap;
  }
playgroundTask: "Amati tampilan awal: chip topik terpotong atau keluar dari garis pembatas karena 'flex-wrap: nowrap'. Ubah nilainya menjadi 'flex-wrap: wrap;', lalu tambahkan 'gap: 8px;' di dalam '.wadah-kategori'. Jalankan kode dan saksikan bagaimana chip otomatis melipat ke baris baru dengan jarak yang sangat rapi!"
---
Secara bawaan, Flexbox memiliki watak yang sangat keras kepala: **semua item dipaksa berjejer dalam satu baris tunggal (*single-line flex container*)**.

Bahkan jika lebar layar mengecil (misalnya di smartphone), item flex akan terus menyusut dan saling menjepit demi mempertahankan baris tunggal tersebut sampai akhirnya meluap (*overflow*) keluar layar secara berantakan.

Untuk mengatasi hal ini, kita membutuhkan dua penyelamat: **`flex-wrap`** dan **`gap`**.

```text
flex-wrap: wrap; gap: 12px;
+-----------------------------+
| [Card 1]  [Card 2] [Card 3] |
|                             | <-- gap: 12px
| [Card 4]  [Card 5]          |
+-----------------------------+
```
## Mengizinkan Baris Baru dengan `flex-wrap`

Properti `flex-wrap` diletakkan pada kontainer induk untuk menentukan apakah item boleh turun ke baris berikutnya saat ruang habis:

1. **`nowrap` (Default)**:
   Semua item dipaksa dalam satu baris. Jika tidak muat, item akan menyusut atau jebol keluar wadah.
2. **`wrap`**:
   Item-item yang tidak lagi muat di baris pertama akan dengan anggun turun dan membentuk baris baru di bawahnya. Sangat esensial untuk galeri foto, deretan tag/kategori, dan daftar kartu.
3. **`wrap-reverse`**:
   Sama seperti wrap, tetapi baris baru muncul di **atas** baris sebelumnya.

## Mengatur Jarak Elegan dengan `gap`

Dahulu, untuk memberi jarak antar-item di Flexbox, kita harus memberi `margin-right: 10px; margin-bottom: 10px;` pada item, lalu menambahkan trik margin negatif pada kontainer agar item paling kanan tidak menjorok keluar.

Kini, CSS modern menghadirkan properti **`gap`** langsung di kontainer induk:

```css
.wadah {
  display: flex;
  flex-wrap: wrap;
  gap: 16px; /* Jarak 16px ke samping dan ke bawah */
}
```

Bahkan kamu bisa membedakan jarak baris dan kolom:
- `row-gap: 20px;` (jarak vertikal antar-baris)
- `column-gap: 12px;` (jarak horizontal antar-kolom)
- Shorthand: `gap: 20px 12px;` (vertikal horizontal)

Properti `gap` hanya memberikan jarak **di antara** dua item, dan **tidak pernah menambah margin ekstra** di dinding terluar kontainer. Bersih, presisi, dan modern!
