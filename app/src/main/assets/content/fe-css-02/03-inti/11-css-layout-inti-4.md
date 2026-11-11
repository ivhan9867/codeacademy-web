---
id: css-layout-inti-4
courseId: fe-css-02
order: 11
title: "Penyelarasan Sumbu Silang dengan align-items & align-self"
readingMinutes: 6
section: INTI
learningObjectives:
  - "Memahami peran properti align-items dalam menyelaraskan item pada Cross Axis (Sumbu Silang)"
  - "Menguasai nilai stretch, center, flex-start, flex-end, dan baseline"
  - "Mencapai trik legendaris 'Centering Anything' di CSS dalam 2 baris kode"
  - "Menggunakan align-self untuk membebaskan satu item dari aturan kontainer"
codeLanguage: css
codeSnippet: |
  .tengah-sempurna {
    display: flex;
    justify-content: center; /* Tengah horizontal (Main Axis) */
    align-items: center;     /* Tengah vertikal (Cross Axis) */
    height: 100vh;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji align-items & align-self</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="wadah">
        <div class="kartu kartu-pendek">Pendek</div>
        <div class="kartu kartu-sedang">Sedang<br />Teks Dua Baris</div>
        <div class="kartu kartu-spesial">Item Spesial</div>
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
    height: 180px; /* Kontainer memiliki tinggi tetap */
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 12px;
    border-radius: 8px;
    align-items: stretch; /* Ubah ke: center, flex-start, flex-end */
  }

  .kartu {
    width: 100px;
    padding: 12px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    margin-right: 12px;
    text-align: center;
  }

  .kartu-pendek { background-color: #1a6368; }
  .kartu-sedang { background-color: #df7b56; }
  .kartu-spesial {
    background-color: #d49438;
    /* Tambahkan align-self di sini jika ingin tampil beda sendiri! */
  }
playgroundTask: "Coba ubah 'align-items: stretch;' pada '.wadah' menjadi 'align-items: center;'. Jalankan kode dan perhatikan bagaimana semua kartu melayang rapi di tengah tinggi wadah! Setelah itu, beri properti 'align-self: flex-end;' khusus pada '.kartu-spesial'."
---
Di era CSS klasik, ada satu lelucon terkenal di kalangan programmer: *"Cara paling susah di dunia adalah menengahkan kotak secara vertikal di CSS."* Dahulu orang harus memakai kombinasi rumit `line-height`, `table-cell`, atau `transform: translateY(-50%)`.

Dengan hadirnya Flexbox, masalah itu selesai selamanya berkat properti **`align-items`**.

![Penyelarasan Sumbu Silang dengan align-items](asset:images/fe-css-02/align-items-cross-axis.png)

## Apa itu `align-items`?

Jika `justify-content` bertugas di **Sumbu Utama (Main Axis)**, maka `align-items` bertugas mengatur posisi item pada **Sumbu Silang (Cross Axis)** yang tegak lurus dengannya.

Pada `flex-direction: row`, sumbu silang adalah arah vertikal (atas-ke-bawah).

### Variasi Nilai `align-items`:

1. **`stretch` (Nilai Default)**:
   Item akan meregang memenuhi seluruh tinggi kontainer induk (jika item tidak memiliki `height` yang dikunci kaku).
2. **`center`**:
   Item ditaruh persis di tengah-tengah tinggi kontainer. Ini adalah penyelamat hidup pembuat web untuk menyelaraskan teks dan ikon!
3. **`flex-start`**:
   Item menempel di langit-langit (paling atas) kontainer.
4. **`flex-end`**:
   Item menempel di lantai (paling bawah) kontainer.
5. **`baseline`**:
   Item diselaraskan berdasarkan garis dasar teks (*font baseline*) di dalamnya. Sangat berguna jika satu item punya ukuran font 24px dan item sebelahnya punya font 14px agar huruf-hurufnya tetap sejajar bacaannya.

## Resep Legendaris: Centering Sempurna

Ingin menengahkan kartu login persis di tengah-tengah layar HP/komputer? Cukup gunakan dua baris ini:

```css
.layar-penuh {
  display: flex;
  justify-content: center; /* Tengah horizontal */
  align-items: center;     /* Tengah vertikal */
}
```

## Pemberontak Mandiri: `align-self`

Bagaimana jika kontainer menetapkan `align-items: center`, tetapi ada satu item tertentu yang ingin menempel di dasar bawah?

Gunakan properti **`align-self`** yang ditulis langsung pada **elemen anak**:

```css
.kartu-spesial {
  align-self: flex-end; /* Mengabaikan aturan kontainer, memilih nempel di bawah */
}
```

Modul berikutnya akan membahas apa yang terjadi jika item kita terlalu banyak dan layarnya sempit!
