---
id: css-layout-inti-7
courseId: fe-css-02
order: 14
title: "Mengubah Urutan Tampilan dengan Properti order"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Memahami cara kerja properti order untuk mengubah urutan visual Flex Items"
  - "Mengetahui nilai default (0) dan perbandingan angka positif/negatif"
  - "Memahami etika aksesibilitas terkait perbedaan antara urutan visual dan urutan DOM HTML"
codeLanguage: css
codeSnippet: |
  .item-prioritas {
    order: -1; /* Melompat ke urutan visual paling awal */
  }

  .item-terakhir {
    order: 1;  /* Pindah ke urutan visual paling akhir */
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Uji properti order</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Memanipulasi Urutan Visual</h2>
      <div class="wadah">
        <div class="kartu kartu-a">Item A (Asli #1)</div>
        <div class="kartu kartu-b">Item B (Asli #2)</div>
        <div class="kartu kartu-c">Item C (Asli #3)</div>
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
    gap: 12px;
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    padding: 16px;
    border-radius: 8px;
  }

  .kartu {
    flex: 1;
    padding: 20px;
    color: white;
    font-weight: bold;
    border-radius: 6px;
    text-align: center;
  }

  .kartu-a {
    background-color: #1a6368;
    order: 0;
  }

  .kartu-b {
    background-color: #df7b56;
    order: 0;
  }

  .kartu-c {
    background-color: #d49438;
    order: 0;
  }
playgroundTask: "Coba beri 'order: -1;' pada '.kartu-c' dan 'order: 2;' pada '.kartu-a'. Tekan 'Jalankan' dan amati bagaimana Item C melompat ke urutan pertama di sebelah kiri, disusul Item B di tengah, dan Item A di ujung kanan—tanpa kita mengubah baris HTML sedikit pun!"
---
Pernahkah kamu menghadapi situasi di mana pada layar desktop sebuah gambar harus berada di sebelah kanan teks, tetapi ketika dibuka di layar HP kamu ingin gambarnya muncul di paling atas sebelum teks?

Dahulu kamu harus menulis ulang HTML atau menggunakan trik JavaScript. Di Flexbox, kamu cukup menggunakan satu properti sederhana: **`order`**.

> **Ilustrasi Konsep:** Pengaturan Urutan Visual Elemen dengan Properti order
## Bagaimana Properti `order` Bekerja?

Secara default, setiap elemen anak flex memiliki nilai **`order: 0`**.

Browser akan menampilkan elemen berurutan berdasarkan nilai angka `order` dari yang terkecil ke yang terbesar:

- Elemen dengan nilai angka lebih kecil akan tampil **lebih awal**.
- Elemen dengan nilai angka lebih besar akan tampil **lebih belakangan**.
- Jika dua elemen memiliki nilai `order` yang sama (misalnya sama-sama bernilai `0`), browser akan menampilkannya sesuai urutan kemunculan aslinya di dokumen HTML (*source order*).

```css
.item-1 { order: 2; }
.item-2 { order: -1; } /* Nilai negatif diizinkan! */
.item-3 { order: 1; }
```
Urutan tampilan visual di layar akan menjadi:
1. **Item 2** (karena `-1` paling kecil)
2. **Item 3** (karena `1`)
3. **Item 1** (karena `2` paling besar)

## Peringatan Penting tentang Aksesibilitas (*Accessibility*)

Meskipun properti `order` sangat memudahkan desain responsif, ada satu aturan etika yang wajib kamu patuhi:

> **Properti `order` hanya mengubah tampilan visual di mata pengguna, tetapi TIDAK MENGUBAH urutan dokumen HTML aslinya.**

Artinya:
- Pembaca layar tuna netra (*Screen Reader*) akan tetap membacakan dokumen sesuai urutan teks HTML aslinya.
- Tombol navigasi keyboard (**Tab**) juga akan berpindah fokus sesuai urutan HTML aslinya, bukan urutan visual di layar!

Gunakan `order` hanya untuk penyesuaian tata letak estetika responsif, bukan untuk merombak alur logika halaman secara drastis.

Di modul berikutnya, kita akan menggabungkan semua materi Flexbox dalam satu latihan praktikum nyata: merakit Navigation Bar!
