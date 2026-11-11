---
id: web-foundation-pcss-14
courseId: fe-web-foundation-00
order: 70
title: "Latihan: Menetapkan Ukuran, Ketebalan, dan Alignment Teks di Halaman Profil"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  nav a:hover {
    font-weight: bold;
  }
codeLanguage: css
starterHtml: |
  <aside>
    <article class="profile">
      <header>
        <!-- Kode lainnya disembunyikan... -->
      </header>
      <section>
        <!-- Kode lainnya disembunyikan... -->
      </section>
    </article>
  </aside>
learningObjectives:
  - "Menerapkan font-size, font-weight, dan text-decoration pada navigasi"
  - "Menerapkan pseudo-class :hover untuk efek tebal pada tautan"
  - "Merapikan header dan footer dengan text-align"
---
## Latihan: ukuran, ketebalan, dan alignment teks

Di latihan ini kita merapikan beberapa bagian Halaman Profil yang sudah kamu buat. Buka proyek terakhirmu di VS Code, lalu ikuti langkah berikut.

### Langkah 1: Ukuran dan ketebalan navigasi

Pertama, atur ukuran teks pada daftar navigasi (`<nav>`). Tambahkan rule berikut di `style.css`:

```css
nav a {
  font-size: 18px;
}
```

Setelah itu, buat tautan navigasi tidak tebal dan tanpa garis bawah:

```css
nav a {
  font-size: 18px;
  font-weight: 400;
  text-decoration: none;
}
```

Jalankan halaman di browser. Teks navigasi seharusnya lebih besar dan tidak lagi bergaris bawah.

### Langkah 2: Efek saat kursor berada di atas tautan

Tautan akan terasa lebih interaktif jika berubah ketika disorot kursor. Di sini kita memakai pseudo-class `:hover`:

```css
nav a:hover {
  font-weight: bold;
}
```

Sekarang ketika kursor berada di atas tautan navigasi, teksnya menjadi tebal. Efek seperti ini sederhana, tetapi cukup memberi tahu pengunjung bahwa elemen tersebut bisa diklik.

### Langkah 3: Memberi class pada `<article>`

Agar lebih mudah menargetkan bagian profil, beri class `profile` pada elemen `<article>`:

```html
<aside>
  <article class="profile">
    <header>
      <!-- Kode lainnya disembunyikan... -->
    </header>
    <section>
      <!-- Kode lainnya disembunyikan... -->
    </section>
  </article>
</aside>
```

Dengan class ini, kita bisa menargetkan `<header>` hanya di dalam profil, bukan di seluruh halaman:

```css
.profile header {
  text-align: center;
}
```

Perhatikan bahwa selector `.profile header` adalah descendant selector yang kita pelajari sebelumnya. Artinya, aturan ini berlaku untuk `<header>` apa pun yang berada di dalam elemen dengan class `profile`.

### Langkah 4: Footer rata tengah dan tebal

Terakhir, buat teks footer rata tengah dan tebal:

```css
footer {
  padding: 20px;
  color: white;
  background-color: #00a2c6;
  text-align: center;
  font-weight: bold;
}
```

Jika seluruh langkah sudah diikuti, isi `style.css` akan terlihat seperti ini:

```css
body {
  font-family: 'Quicksand', sans-serif;
}

h2,
h3 {
  color: #00a2c6;
}

footer {
  padding: 20px;
  color: white;
  background-color: #00a2c6;
  text-align: center;
  font-weight: bold;
}

nav a {
  font-size: 18px;
  font-weight: 400;
  text-decoration: none;
}

nav a:hover {
  font-weight: bold;
}

.profile header {
  text-align: center;
}
```

Perubahan kecil seperti ini menunjukkan bahwa CSS bekerja berlapis: setiap aturan memperbaiki satu bagian, dan hasil akhirnya terbentuk dari kombinasi semuanya.
