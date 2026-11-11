---
id: web-foundation-pcss-18
courseId: fe-web-foundation-00
order: 74
title: "Opacity"
readingMinutes: 4
section: PENDALAMAN_CSS
codeSnippet: |
  img {
    opacity: 50%;
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
  - "Memahami perbedaan transparansi dan opacity"
  - "Menggunakan alpha channel agar hanya warna yang transparan"
---
## Opacity

Bayangkan selembar plastik mika berwarna. Satu lembar membuatnya sedikit tembus pandang. Menambah lembar berarti semakin sedikit cahaya yang lewat, dan objek di baliknya makin tidak terlihat. Konsep inilah yang dipakai CSS lewat properti `opacity`.

Perbedaannya bisa dirumuskan begini: **transparansi** adalah seberapa tembus pandang sebuah objek, sedangkan **opacity** adalah seberapa terlihat objek tersebut. Keduanya saling berkebalikan.

| Nilai | Hasil |
|---|---|
| `0%` | Elemen tidak terlihat |
| Di antara 0% dan 100% | Elemen tembus pandang, tetapi masih terlihat |
| `100%` | Elemen sepenuhnya terlihat |

```css
img {
  opacity: 50%;
}
```

### Opacity pada warna saja

`opacity` pada elemen berlaku untuk **seluruh elemen**, termasuk isi dan elemen turunannya. Kadang kamu hanya ingin warnanya yang transparan, sementara teks di dalamnya tetap solid. Untuk itu, gunakan **alpha channel**, yaitu komponen keempat setelah merah, hijau, dan biru. Format yang mendukungnya adalah `rgba()`, `hsla()`, dan HEX dengan dua digit tambahan.

```css
.overlay {
  background-color: rgba(0, 0, 0, 0.5); /* hitam dengan transparansi 50% */
}
```

Dengan cara ini, latar `.overlay` menjadi setengah transparan, sedangkan teks di atasnya tetap utuh.
