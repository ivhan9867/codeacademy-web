---
id: css-layout-prep-glossary
courseId: fe-css-02
order: 4
title: "Glosarium Istilah CSS Layout"
readingMinutes: 8
section: PERSIAPAN
learningObjectives:
  - "Mengenal istilah teknis utama dalam ekosistem CSS Flexbox, CSS Grid, dan Responsive Design"
  - "Memahami perbedaan konseptual antara properti container dan properti item"
  - "Memiliki kamus referensi cepat saat membaca dokumentasi maupun mengerjakan proyek layout"
---
Dalam dunia tata letak web modern, kamu akan sering menjumpai istilah bahasa Inggris yang terdengar mirip namun memiliki peran berbeda. Gunakan glosarium ini sebagai kamus saku selama mempelajari seluruh modul **CSS Layout Modern**.

```text
+-------------------------------------------------------------------+
|                    PETA ISTILAH CSS LAYOUT                        |
+-----------------------+---------------------+---------------------+
|     FLEXBOX (1D)      |    CSS GRID (2D)    |  RESPONSIF & TOKEN  |
| - Flex Container      | - Grid Container    | - Media Query       |
| - Main & Cross Axis   | - Track, Line, Cell | - Breakpoint        |
| - justify / align     | - fr, repeat, minmax| - Mobile-First      |
| - grow/shrink/basis   | - auto-fit/auto-fill| - CSS Variables     |
+-----------------------+---------------------+---------------------+
```

## Daftar Istilah Utama

- **Flex Container** — Elemen induk yang diberi `display: flex` atau `display: inline-flex` untuk mengatur tata letak seluruh anak langsung di dalamnya.
- **Flex Item** — Elemen anak langsung (*direct child*) di dalam Flex Container yang posisinya, ukurannya, dan urutannya diatur oleh aturan Flexbox.
- **Main Axis** — Sumbu utama arah aliran Flex Item yang ditentukan oleh `flex-direction` (secara default mendatar dari kiri ke kanan).
- **Cross Axis** — Sumbu silang yang selalu tegak lurus terhadap Main Axis (secara default vertikal dari atas ke bawah).
- **Flexbox** — Sistem tata letak CSS satu dimensi (*1-dimensional*) yang dirancang untuk menyusun elemen secara berbaris atau berkolom dengan distribusi ruang yang luwes.
- **CSS Grid** — Sistem tata letak CSS dua dimensi (*2-dimensional*) yang memungkinkan pengaturan baris dan kolom secara bersamaan dalam satu struktur matriks.
- **Grid Container** — Elemen induk yang diberi `display: grid` atau `display: inline-grid` sebagai kanvas utama tempat sel-sel kisi dibentuk.
- **Grid Item** — Elemen anak langsung di dalam Grid Container yang menempati satu atau beberapa sel di dalam kisi.
- **Grid Track** — Ruang di antara dua Grid Line yang berdekatan, bisa berupa satu lajur kolom vertikal maupun satu lajur baris horizontal.
- **Grid Line** — Garis pembatas horizontal atau vertikal bernomor (mulai dari 1) yang membentuk kerangka kolom dan baris pada CSS Grid.
- **Grid Cell** — Satuan kotak terkecil dalam CSS Grid yang terbentuk dari perpotongan satu baris dan satu kolom yang berdekatan.
- **Grid Area** — Bidang persegi panjang yang dibentuk oleh satu atau gabungan beberapa Grid Cell dan dapat diberi nama khusus melalui `grid-template-areas`.
- **Explicit Grid** — Baris dan kolom yang ukurannya didefinisikan secara manual dan eksplisit oleh pengembang menggunakan `grid-template-columns` dan `grid-template-rows`.
- **Implicit Grid** — Baris atau kolom tambahan yang otomatis diciptakan oleh browser ketika jumlah Grid Item melebihi kapasitas Explicit Grid.
- **fr (Fraction Unit)** — Satuan fleksibel khusus CSS Grid yang merepresentasikan pecahan dari sisa ruang kosong yang tersedia di dalam Grid Container.
- **gap** — Properti shorthand untuk menentukan jarak celah (*gutter*) antar baris dan kolom di dalam Flexbox maupun CSS Grid tanpa mengganggu margin luar.
- **justify-content** — Properti container untuk mengatur distribusi dan perataan posisi item di sepanjang sumbu utama (Main Axis di Flexbox, atau sumbu horizontal di Grid).
- **align-items** — Properti container untuk menyelaraskan posisi item pada sumbu silang (Cross Axis di Flexbox, atau sumbu vertikal di dalam setiap sel Grid).
- **align-content** — Properti container untuk mengatur jarak dan perataan antar baris jamak (*multi-line*) saat ada ruang sisa di sumbu vertikal.
- **align-self** — Properti individual pada satu item tertentu untuk menimpa aturan `align-items` milik container induknya.
- **flex-direction** — Properti Flex Container yang menentukan arah Main Axis, yaitu `row`, `row-reverse`, `column`, atau `column-reverse`.
- **flex-wrap** — Properti Flex Container yang menentukan apakah Flex Item dipaksa muat dalam satu baris (`nowrap`) atau boleh turun ke baris baru (`wrap`).
- **flex-grow** — Properti Flex Item berupa angka proporsi yang menentukan seberapa besar item boleh melebar untuk menghabiskan sisa ruang kosong container.
- **flex-shrink** — Properti Flex Item yang menentukan faktor penyusutan item ketika total lebar item melebihi ruang container yang tersedia.
- **flex-basis** — Ukuran awal ideal sebuah Flex Item di sepanjang Main Axis sebelum ruang sisa dibagi oleh `flex-grow` atau `flex-shrink`.
- **order** — Properti item dalam Flexbox dan Grid yang mengubah urutan tampilan visual elemen di layar tanpa mengubah struktur kode HTML aslinya.
- **grid-template-columns** — Properti Grid Container untuk menentukan jumlah dan lebar masing-masing kolom pada Explicit Grid.
- **grid-template-rows** — Properti Grid Container untuk menentukan jumlah dan tinggi masing-masing baris pada Explicit Grid.
- **grid-template-areas** — Properti Grid Container yang memungkinkan pemetaan tata letak halaman secara visual menggunakan nama area berbasis teks.
- **repeat()** — Fungsi CSS Grid untuk menulis pola ukuran track yang berulang secara ringkas, misalnya `repeat(3, 1fr)`.
- **minmax()** — Fungsi CSS Grid untuk menetapkan rentang ukuran minimum dan maksimum sebuah track agar tetap elastis namun aman dari pemotongan konten.
- **auto-fit** — Kata kunci di dalam `repeat()` yang mengisi baris dengan kolom sebanyak mungkin dan meruntuhkan track kosong agar item yang ada melebar penuh.
- **auto-fill** — Kata kunci di dalam `repeat()` yang terus membuat track kosong cadangan di akhir baris meskipun jumlah item lebih sedikit dari kapasitas layar.
- **place-items** — Properti shorthand di CSS Grid yang menggabungkan `align-items` dan `justify-items` dalam satu deklarasi singkat.
- **place-content** — Properti shorthand untuk menyelaraskan keseluruhan blok baris dan kolom pada container sekaligus di kedua sumbu.
- **justify-items** — Properti Grid Container untuk mengatur perataan horizontal seluruh Grid Item di dalam masing-masing selnya.
- **justify-self** — Properti individual pada satu Grid Item untuk mengatur perataan horizontalnya sendiri di dalam sel tanpa memengaruhi item lain.
- **Media Query** — Aturan CSS kondisional (`@media`) yang hanya menerapkan gaya tertentu jika kondisi perangkat seperti lebar layar atau orientasi terpenuhi.
- **Breakpoint** — Titik ambang batas lebar layar tertentu (misalnya `768px` atau `1024px`) tempat tata letak berubah struktur agar nyaman dilihat di perangkat baru.
- **Mobile-First** — Pendekatan desain dan penulisan CSS yang dimulai dari tampilan layar kecil ponsel terlebih dahulu, lalu diperkaya ke layar besar memakai `min-width`.
- **Viewport** — Area jendela layar yang benar-benar terlihat oleh pengguna saat membuka halaman web pada ponsel, tablet, atau monitor desktop.
- **CSS Custom Properties (CSS Variables)** — Variabel bawaan CSS (diawali tanda `--`, seperti `--space-md`) yang disimpan di `:root` dan dipanggil memakai fungsi `var()` untuk konsistensi desain.

Simpan modul ini sebagai rujukan cepat setiap kali kamu menemui properti baru di materi inti dan lanjutan!
