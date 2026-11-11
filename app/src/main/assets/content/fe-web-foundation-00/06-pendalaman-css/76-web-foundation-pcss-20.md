---
id: web-foundation-pcss-20
courseId: fe-web-foundation-00
order: 76
title: "Apa Itu Box Element"
readingMinutes: 3
section: PENDALAMAN_CSS
codeSnippet: |
  * {
    border: 1px solid red;
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
  - "Memahami batas luar elemen (edge of element)"
  - "Memakai selector universal untuk melihat kotak elemen saat debugging"
---
## Memvisualisasikan Kotak Elemen

Setelah mengenal analogi kotak kado pada modul sebelumnya, kamu tahu bahwa setiap elemen web memiliki empat lapisan: *content*, *padding*, *border*, dan *margin*. Namun saat membuka halaman web biasa, kotak-kotak ini sering kali tampak tidak terlihat karena latar belakangnya transparan dan tidak memiliki garis tepi bawaan.

Selain keempat lapisan tersebut, ada konsep penting yang disebut **edge of element**, yaitu batas terluar dari sebuah elemen tempat area margin berakhir. Batas ini memang tidak tampak secara fisik, tetapi browser selalu menghitungnya secara ketat untuk menentukan di mana elemen berikutnya boleh mulai ditampilkan.

## Trik Melihat Semua Kotak dengan Selector Universal

Ketika kamu sedang menyusun tata letak halaman dan merasa ada elemen yang posisinya tidak pas, kamu bisa meminta browser menggambar garis di sekeliling seluruh elemen sekaligus. Caranya adalah dengan memasang border tipis menggunakan **selector universal** (`*`):

```css
* {
  border: 1px solid red;
}
```

Dengan satu baris aturan di atas, browser akan membingkai setiap elemen—mulai dari elemen pembungkus besar seperti `<body>` dan `<header>` hingga elemen kecil seperti `<span>` dan `<a>`—dengan garis merah. Trik ini sangat populer di kalangan developer web untuk proses *debugging* tata letak karena kamu bisa melihat dengan mata kepala sendiri bagaimana kotak-kotak tersebut saling bertumpuk dan bersisian.

Kamu sebenarnya sudah menyentuh konsep box model sejak latihan footer terdahulu, tepatnya saat memberi deklarasi `padding: 20px`. Pada modul-modul berikutnya, kita akan mempelajari cara mengatur dan mengendalikan keempat lapisan ini secara presisi satu per satu.
