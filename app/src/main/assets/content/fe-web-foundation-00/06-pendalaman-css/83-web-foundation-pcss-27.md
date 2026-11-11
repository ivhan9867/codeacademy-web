---
id: web-foundation-pcss-27
courseId: fe-web-foundation-00
order: 83
title: "Box Shadow"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu {
    box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.3);
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
  - "Menambahkan bayangan pada kotak elemen dengan box-shadow"
  - "Mengatur jarak horizontal, vertikal, blur, spread, dan warna pada box-shadow"
---
Sebelumnya, pada materi formatting teks, kamu sudah belajar memberi bayangan pada teks dengan `text-shadow`. Sekarang giliran **kotak elemen** yang bisa diberi bayangan, menggunakan properti `box-shadow`.

Cara kerja `box-shadow` mirip dengan `text-shadow`: kamu menentukan jarak horizontal, jarak vertikal, tingkat keburaman, dan warna. Bedanya, `box-shadow` punya satu nilai tambahan, yaitu **spread** (tingkat sebaran bayangan).

```css
.kartu {
  box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.3);
}
```

Berikut urutan dan arti tiap nilainya:

1. **Jarak horizontal**: seberapa jauh bayangan bergeser ke kiri (nilai negatif) atau kanan (nilai positif).
2. **Jarak vertikal**: seberapa jauh bayangan bergeser ke atas (nilai negatif) atau bawah (nilai positif).
3. **Blur** (opsional): tingkat keburaman bayangan. Semakin besar nilainya, bayangan semakin lembut dan menyebar.
4. **Spread** (opsional): tingkat sebaran bayangan. Semakin besar nilainya, bayangan semakin melebar ke segala arah, bukan hanya mengikuti bentuk kotak.
5. **Warna**: warna bayangan, biasanya memakai `rgba()` agar bisa diatur transparansinya.

```css
.kartu-lembut {
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.15);
}

.kartu-tegas {
  box-shadow: 2px 2px 0 0 #333;
}
```

Perbedaan dua contoh di atas cukup mencolok: yang pertama memakai blur besar dan warna transparan, sehingga bayangannya lembut dan menyebar seperti efek "melayang". Yang kedua tanpa blur dan tanpa transparansi, sehingga bayangannya tampak tegas seperti garis tepi tambahan.

Penting untuk diingat, `box-shadow` mengikuti bentuk kotak elemen **di luar border**, dan tidak memengaruhi area margin.

## Latihan singkat

1. Buat sebuah kartu dengan `box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2)`, lalu amati efek melayangnya.
2. Ubah nilai blur menjadi `0`, lalu bandingkan hasilnya dengan langkah sebelumnya.
3. Tambahkan nilai spread positif (misalnya `4px`) pada salah satu bayangan, lalu amati bagaimana bayangan melebar.
