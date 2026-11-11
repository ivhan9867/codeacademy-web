---
id: web-foundation-pcss-8
courseId: fe-web-foundation-00
order: 64
title: "Font Styling: Font Family dan Font Size"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  body {
    font-family: sans-serif;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Area Latihan</title>
    </head>
    <body>
      <header>
        <h1>Judul Contoh</h1>
        <p>Paragraf contoh untuk mencoba properti CSS di atas.</p>
      </header>
      <nav>
        <ul>
          <li><a href="#">Beranda</a></li>
          <li><a href="#">Tentang</a></li>
          <li><a href="#">Kontak</a></li>
        </ul>
      </nav>
      <main>
        <div class="box">
          <span>Teks di dalam span</span>
          <p>Paragraf lain di dalam sebuah div.</p>
          <img src="https://placehold.co/150" alt="Contoh gambar" />
        </div>
      </main>
    </body>
  </html>
learningObjectives:
  - "Menyusun font-family dengan fallback yang benar, termasuk generic font family di akhir"
  - "Memilih satuan font-size yang tepat (px, em, rem, %, vw, vh)"
  - "Memahami kapan @font-face diperlukan"
---
## Font: jenis, ukuran, dan ketebalan

### font-family

`font-family` menentukan jenis font yang dipakai. Biasanya ditulis di `body` agar berlaku ke seluruh halaman.

```css
body {
  font-family: sans-serif;
}
```

Nilai `font-family` bisa berisi **beberapa font sekaligus**, yang disebut *font stack*. Browser mencoba font pertama. Jika tidak tersedia di perangkat pengunjung, browser lanjut ke font berikutnya, dan seterusnya.

```css
body {
  font-family: "Open Sans", Arial, sans-serif;
}
```

Ada beberapa aturan penulisannya:

- Pisahkan setiap nilai dengan **koma**.
- Nama font yang punya **spasi** wajib diapit tanda kutip, misalnya `"Open Sans"`.
- Tutup daftar dengan **generic font family** (`serif`, `sans-serif`, `monospace`, dan lainnya). Nilai ini dijamin didukung semua browser, jadi berfungsi sebagai pengaman terakhir.

Generic font family yang paling sering dipakai:

| Nilai | Ciri-ciri |
|---|---|
| `serif` | Ada sirip (garis runcing) di ujung karakter, misalnya Times New Roman |
| `sans-serif` | Tanpa sirip, tampilannya bersih, misalnya Arial atau Open Sans |
| `monospace` | Lebar tiap karakter sama, misalnya Consolas, sering dipakai untuk kode |
| `cursive` | Menyerupai tulisan tangan |
| `system-ui` | Mengikuti font bawaan sistem operasi pengunjung |

### @font-face

Kalau kamu ingin memakai font yang tidak ada di komputer pengunjung, CSS menyediakan `@font-face` untuk mendaftarkan berkas font sendiri:

```css
@font-face {
  font-family: "Font Saya";
  src: url("FILE-FONT.ttf");
}

p {
  font-family: "Font Saya", sans-serif;
}
```

Setelah didaftarkan, nama `"Font Saya"` bisa dipakai seperti font biasa. Kita tidak akan membahas `@font-face` lebih dalam di modul ini. Di bagian Google Fonts nanti, kamu akan melihat bahwa teknik ini juga dipakai secara otomatis.

### font-size

`font-size` mengatur ukuran huruf. Nilai dan satuannya harus **menempel tanpa spasi**.

```css
h1 {
  font-size: 1.5em;  /* benar */
}

/* font-size: 1.5 em;  -> salah, rule ini diabaikan browser */
```

Satuan dibagi menjadi dua kelompok:

**Relative unit** bergantung pada sesuatu yang lain:

- `em`: relatif terhadap ukuran font elemen itu sendiri. `2em` berarti dua kali ukuran font saat ini.
- `rem`: relatif terhadap ukuran font elemen root (`<html>`). Lebih mudah diprediksi karena tidak bertumpuk seperti `em`.
- `%`: persentase dari ukuran font induk. `150%` berarti 1,5 kali ukuran induk.
- `vw` dan `vh`: masing-masing 1% dari lebar dan tinggi viewport. Berguna untuk teks yang ikut membesar mengikuti layar.

**Absolute unit** nilainya tetap:

- `px`: piksel. Paling umum dipakai dan mudah dipahami.
- `pt`, `pc`, `mm`, `cm`, `in`: lebih sering dipakai untuk dokumen cetak.

```css
body {
  font-size: 16px;
}

h1 {
  font-size: 150%; /* 150% dari 16px = 24px */
}
```

Di contoh di atas, `<h1>` mewarisi `16px` dari `body`, lalu dikalikan 150%. Hasilnya 24px, atau 50% lebih besar dari teks biasa.

Kamu juga bisa memakai kata kunci: `xx-small`, `x-small`, `small`, `medium`, `large`, `x-large`, dan `xx-large`. Browser biasanya memulai dari `medium`, lalu menyesuaikan yang lain secara konsisten.

> **Catatan aksesibilitas:** Untuk teks utama, lebih baik gunakan `rem` atau `em` daripada `px`. Dengan begitu, teks ikut membesar ketika pengunjung mengubah ukuran font di pengaturan browser.
