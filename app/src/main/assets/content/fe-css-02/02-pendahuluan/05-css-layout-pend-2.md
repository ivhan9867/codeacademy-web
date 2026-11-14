---
id: css-layout-pend-2
courseId: fe-css-02
order: 5
title: "Kilas Balik Cepat: Normal Flow, Display, & Box Model"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Mengingat kembali konsep Normal Flow dan perbedaan perilaku display block vs inline"
  - "Memahami kembali 4 lapisan Box Model (content, padding, border, margin)"
  - "Mengamati keterbatasan Normal Flow saat ingin menyusun elemen bersisian secara seimbang"
codeLanguage: css
codeSnippet: |
  .kotak-blok {
    display: block;
    background-color: #df7b56;
    color: white;
    padding: 12px;
    margin-bottom: 8px;
  }

  .kotak-inline {
    display: inline;
    background-color: #1a6368;
    color: white;
    padding: 12px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Kilas Balik Box Model & Flow</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>Perilaku Block (Menguasai 1 Baris Penuh):</h3>
      <div class="kotak-blok">Blok 1</div>
      <div class="kotak-blok">Blok 2</div>

      <h3>Perilaku Inline (Mengalir Sebaris Teks):</h3>
      <span class="kotak-inline">Inline A</span>
      <span class="kotak-inline">Inline B</span>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 20px;
    background-color: #faf7f2;
    color: #24211d;
  }

  h3 {
    margin-top: 16px;
    margin-bottom: 8px;
    color: #104246;
  }

  .kotak-blok {
    display: block;
    background-color: #df7b56;
    color: white;
    padding: 12px;
    margin-bottom: 8px;
    border-radius: 6px;
  }

  .kotak-inline {
    display: inline;
    background-color: #1a6368;
    color: white;
    padding: 8px 12px;
    border-radius: 6px;
  }
playgroundTask: "Coba ubah 'display: block;' pada selector '.kotak-blok' menjadi 'display: inline-block;'. Jalankan kode dan perhatikan bagaimana Blok 1 dan Blok 2 kini bisa duduk berdampingan, namun masih memiliki celah spasi putih bawaan teks!"
---
Sebelum kita masuk ke keajaiban Flexbox dan Grid, mari segarkan kembali ingatanmu tentang cara browser menyusun elemen secara alami tanpa campur tangan layout modern.

Sistem alami ini disebut **Normal Flow (Aliran Normal)**.

> **Ilustrasi Konsep:** Kilas Balik Box Model dan Aliran Normal Browser
## 1. Dua Karakter Utama: Block vs Inline

Secara default, elemen HTML terbagi ke dalam dua dunia tampilan (*display*):

- **Elemen Block (`<div>`, `<p>`, `<h1>`, `<header>`, dll)**:
  Sifatnya egois dan rakus ruang. Dia selalu memulai baris baru dan melebar sejauh mungkin memenuhi lebar wadah induknya (100% lebar). Elemen block lain yang datang setelahnya akan dipaksa turun ke baris berikutnya.
- **Elemen Inline (`<span>`, `<a>`, `<strong>`, `<em>`, dll)**:
  Sifatnya sopan dan hemat ruang. Dia hanya memakan ruang selebar teks atau konten di dalamnya. Elemen inline lain bisa berdampingan di baris yang sama persis seperti kata-kata dalam satu kalimat paragraf. Sayangnya, elemen inline murni **tidak bisa diatur lebar (`width`) dan tingginya (`height`) secara presisi**.

## 2. Empat Lapisan Box Model

Setiap elemen di layar browser dibungkus oleh 4 lapisan kotak:
1. **Content**: Ruang inti tempat teks atau gambar berada.
2. **Padding**: Ruang kosong di antara konten dan garis batas (border).
3. **Border**: Garis tepi pembatas kotak.
4. **Margin**: Ruang kosong di luar garis batas yang memisahkan elemen ini dari tetangganya.

## Masalah Klasik: Ingin Berdampingan Tapi Tetap Punya Dimensi

Ketika kamu ingin membuat deretan tombol menu atau kartu produk, kamu terjebak di persimpangan:
- Kalau pakai `display: block`, mereka menolak berdampingan (pasti bertumpuk ke bawah).
- Kalau pakai `display: inline`, kamu tidak leluasa mengatur tinggi dan lebarnya.
- Dulu ada solusi tengah bernama `display: inline-block`, tetapi solusi ini menyisakan bug visual: browser selalu menyisipkan spasi putih (*whitespace*) kecil di antara elemen seolah-olah mereka adalah spasi teks biasa!

Kebutuhan inilah yang memicu para insinyur web untuk menciptakan cara yang lebih cerdas: mengubah wadah induknya menjadi wadah fleksibel.
