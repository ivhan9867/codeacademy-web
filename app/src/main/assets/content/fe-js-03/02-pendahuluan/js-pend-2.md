---
id: js-pend-2
courseId: fe-js-03
order: 5
title: Menautkan JavaScript ke Dokumen HTML
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - Memahami fungsi elemen `<script>` dalam menyisipkan logika JavaScript ke halaman web
  - Membedakan penulisan skrip internal (inline) dan skrip berkas eksternal (`.js`)
  - Memahami alasan teknis menempatkan tag `<script>` tepat sebelum penutup `</body>`
codeLanguage: javascript
codeSnippet: |
  console.log("Skrip berhasil dimuat dan dijalankan!");
starterJs: |
  // Kode ini dieksekusi bersamaan dengan dokumen HTML di samping
  console.log("Skrip berhasil dimuat dan dijalankan!");
starterHtml: |
  <div style="font-family: sans-serif; padding: 20px; text-align: center;">
    <h2 style="color: #0F766E; margin-bottom: 8px;">Halaman Web Pertama</h2>
    <p style="color: #475569; margin: 0;">JavaScript sedang memantau dokumen ini melalui Console.</p>
  </div>
playgroundTask: |
  Tekan tombol 'Jalankan'. Amati teks HTML yang muncul di jendela pratinjau, lalu buka panel Console di bagian bawah untuk memastikan pesan 'Skrip berhasil dimuat dan dijalankan!' tercetak dengan benar.
---

# Menautkan JavaScript ke Dokumen HTML

Agar instruksi JavaScript dapat bekerja dan menghidupkan elemen di halaman web, kode tersebut harus dihubungkan ke berkas HTML.

HTML menyediakan elemen khusus bernama tag `<script>` untuk tugas ini.

---

## 1. Skrip Internal (Inline Script)

Cara paling sederhana adalah menuliskan kode JavaScript langsung di dalam tag `<script>` di dokumen HTML kamu:

```html
<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8">
  <title>Latihan JavaScript</title>
</head>
<body>
  <h1>Halo Dunia</h1>

  <script>
    console.log("Pesan dari skrip internal");
  </script>
</body>
</html>
```

Cara ini cocok untuk eksperimen singkat satu halaman, namun kurang praktis untuk proyek besar karena mencampurkan kode tampilan dan logika pemrograman.

---

## 2. Skrip Eksternal (File .js Terpisah)

Standar profesional di industri adalah memisahkan kode JavaScript ke dalam berkas tersendiri berakhiran `.js` (misalnya `app.js` atau `main.js`), lalu menghubungkannya menggunakan atribut `src`:

```html
<!-- Menautkan berkas JavaScript eksternal -->
<script src="app.js"></script>
```

**Keuntungan cara eksternal:**
- Kode HTML tetap bersih dan mudah dibaca.
- Satu berkas JavaScript dapat digunakan kembali oleh banyak halaman HTML sekaligus.
- Browser dapat menyimpan berkas JavaScript ke dalam memori singgah (*cache*) sehingga halaman terbuka lebih cepat pada kunjungan berikutnya.

---

## Di Mana Posisi Terbaik Menaruh Tag `<script>`?

Aturan emas bagi pemula: **tempatkan tag `<script>` tepat di bagian paling bawah sebelum tag penutup `</body>`**.

```html
<body>
  <!-- 1. Semua elemen visual ditulis di sini -->
  <header>...</header>
  <main>...</main>

  <!-- 2. Skrip diletakkan di akhir sebelum penutup body -->
  <script src="app.js"></script>
</body>
```

**Mengapa demikian?**
Browser membaca kode HTML dari baris paling atas ke bawah. Jika kamu menaruh `<script>` di dalam `<head>`, browser akan berhenti sejenak untuk mengunduh dan menjalankan JavaScript sebelum menggambar tombol atau teks di layar. Akibatnya:
1. Layar pengguna terlihat putih/kosong lebih lama.
2. Jika kode JavaScript mencoba mencari tombol yang belum sempat digambar browser, program akan menghasilkan pesan error (*elemen tidak ditemukan*).

---

## Praktik di Playground CodeAcademy

Di editor interaktif CodeAcademy, kamu tidak perlu mengetikkan tag `<script>` secara manual setiap saat. Tab kode **JS** yang kamu tulis otomatis disisipkan oleh sistem di akhir dokumen **HTML** pratinjau.

Coba tekan tombol **Jalankan**, amati halaman pratinjau di samping, lalu buka panel **Console** untuk melihat hasil log kodenya!
