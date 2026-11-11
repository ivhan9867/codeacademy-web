---
id: web-foundation-pcss-28
courseId: fe-web-foundation-00
order: 84
title: "Rounded Corner (Border Radius)"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .kartu {
    border-radius: 8px;
  }

  .tombol {
    border-radius: 999px; /* sangat besar, menghasilkan ujung pil */
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
  - "Membuat sudut kotak menjadi bundar dengan border-radius"
  - "Mengatur radius tiap sudut secara individual"
  - "Membuat elemen berbentuk lingkaran penuh dengan border-radius: 50%"
---
CSS3 memperkenalkan properti `border-radius`, yang memungkinkan kita membuat sudut kotak menjadi bundar (*rounded corner*), alih-alih siku tajam seperti bawaannya. Properti ini banyak dipakai pada tombol, kartu, avatar, dan badge agar tampilan terasa lebih lembut dan modern.

Nilai `border-radius` menentukan tingkat lengkungan, dan biasanya ditulis dalam piksel atau persentase.

```css
.kartu {
  border-radius: 8px;
}

.tombol {
  border-radius: 999px; /* sangat besar, menghasilkan ujung pil */
}
```

Semakin besar nilainya, semakin bundar sudutnya. Nilai yang sangat besar (lebih besar dari setengah sisi terpendek kotak) akan menghasilkan bentuk *pill* atau kapsul pada tombol persegi panjang.

## Mengatur tiap sudut secara individual

Sama seperti border, padding, dan margin, `border-radius` juga mendukung pengaturan per sudut:

```css
.kartu {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
```

Atau ditulis sekaligus dalam satu baris, dengan urutan **kiri-atas, kanan-atas, kanan-bawah, kiri-bawah** (searah jarum jam, dimulai dari sudut kiri atas):

```css
.kartu {
  border-radius: 12px 12px 0 0; /* hanya sudut atas yang bundar */
}
```

Pola ini berguna, misalnya, untuk kartu yang menempel pada elemen lain di bagian bawah, sehingga hanya sudut atasnya yang perlu dibuat bundar.

## Membuat lingkaran penuh

Trik yang sering dipakai: jika sebuah elemen berbentuk **persegi** (lebar dan tinggi sama) diberi `border-radius: 50%`, hasilnya adalah **lingkaran sempurna**. Teknik ini umum dipakai untuk membuat foto profil berbentuk bulat.

```css
.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}
```

Properti `object-fit: cover` biasanya disandingkan dengan trik ini pada elemen `<img>`, agar gambar tetap proporsional memenuhi lingkaran tanpa gepeng.

## Latihan singkat

1. Buat sebuah kartu dengan `border-radius: 8px` pada seluruh sudut.
2. Buat sebuah tombol dengan `border-radius` besar (misalnya `999px`) sehingga ujungnya membentuk pil.
3. Buat sebuah `<img>` berukuran 80×80px dengan `border-radius: 50%` untuk menghasilkan avatar bulat.
4. Buat sebuah kartu notifikasi dengan sudut atas bundar (`12px`) dan sudut bawah siku (`0`).
