---
id: js-lanjutan-6
courseId: fe-js-03
order: 22
title: "Jembatan ke Halaman Web: Memilih Elemen dengan querySelector"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - Memahami Document Object Model (DOM) sebagai representasi dokumen HTML yang dapat dimanipulasi JavaScript
  - Menemukan elemen HTML spesifik menggunakan selektor CSS dengan document.querySelector()
  - Memodifikasi teks elemen melalui .textContent dan properti visual melalui .style
codeLanguage: javascript
codeSnippet: |
  const judul = document.querySelector("#judul");
  judul.textContent = "Teks Baru!";
  judul.style.color = "teal";
starterHtml: |
  <div style="font-family: sans-serif; padding: 16px; border: 1px solid #CBD5E1; border-radius: 8px;">
    <h3 id="judul-kartu" style="color: #1E293B; margin-top: 0;">Judul Asli</h3>
    <p class="deskripsi-teks" style="color: #64748B; margin-bottom: 0;">Deskripsi awal dokumen.</p>
  </div>
starterJs: |
  // 1. Menemukan elemen HTML menggunakan selektor CSS
  const elemenJudul = document.querySelector("#judul-kartu");
  const elemenDeskripsi = document.querySelector(".deskripsi-teks");

  // 2. Mengubah teks dan warna secara dinamis
  if (elemenJudul) {
    elemenJudul.textContent = "JavaScript Mengubah Dunia Web!";
    elemenJudul.style.color = "#0D9488";
  }

  if (elemenDeskripsi) {
    elemenDeskripsi.textContent = "Teks ini berhasil diperbarui secara dinamis dari JavaScript.";
  }

  console.log("DOM berhasil diperbarui!");
playgroundTask: |
  Tekan tombol 'Jalankan'. Periksa jendela pratinjau: perhatikan bahwa teks 'Judul Asli' seketika berganti menjadi 'JavaScript Mengubah Dunia Web!' dengan warna toska. Buka panel Console untuk memverifikasi pesan:
  DOM berhasil diperbarui!
---

# Jembatan ke Halaman Web: Memilih Elemen dengan querySelector

Selama ini, kita mengamati kerja JavaScript melalui panel Console. Sekarang saatnya menyatukan logika tersebut dengan apa yang dilihat pengguna di layar peramban!

Bagaimana cara JavaScript berinteraksi dengan tag HTML? Jawabannya adalah melalui **DOM (Document Object Model)**.

> **Ilustrasi Konsep:** Hubungan JavaScript dan Pohon DOM
---

## Apa Itu DOM?

Ketika browser selesai membaca dokumen HTML, browser mengubah setiap tag `<header>`, `<div>`, `<h1>`, dan `<p>` menjadi simpul-simpul pohon objek JavaScript yang disebut **DOM**.

Objek utama yang mewakili seluruh halaman web ini bernama **`document`**. Melalui objek `document`, JavaScript dapat membaca, menambah, mengubah teks, maupun menghapus elemen apa pun di layar.

---

## 1. Menemukan Elemen: document.querySelector()

Untuk menargetkan elemen HTML tertentu, gunakan metode sakti **`document.querySelector()`**.

Metode ini menggunakan aturan penamaan selektor CSS yang sudah kamu kuasai di kelas CSS sebelumnya:
- **Berdasarkan Tag**: `document.querySelector("h1")` (mengambil tag `<h1>` pertama yang ditemukan).
- **Berdasarkan Class**: `document.querySelector(".nama-class")` (diawali tanda titik).
- **Berdasarkan ID**: `document.querySelector("#nama-id")` (diawali tanda pagar).

Contoh:
```html
<p id="pesan-selamat">Selamat datang di toko kami!</p>
```

```javascript
const pesan = document.querySelector("#pesan-selamat");
```

---

## 2. Mengubah Teks: .textContent

Setelah berhasil menangkap elemen dan menyimpannya di variabel, kamu dapat mengubah teks yang ada di dalamnya menggunakan properti **`.textContent`**:

```javascript
const pesan = document.querySelector("#pesan-selamat");
pesan.textContent = "Promo Spesial Hari Ini: Diskon 50%!";
```

Seketika itu juga, teks yang dibaca pengunjung di layar peramban akan berubah menjadi kalimat baru!

---

## 3. Mengubah Gaya Tampilan: .style

Kamu juga bisa mengubah properti CSS secara langsung melalui properti **`.style`**:

```javascript
pesan.style.color = "#2563EB";           // Mengubah warna teks menjadi biru
pesan.style.backgroundColor = "#EFF6FF"; // Background biru muda (camelCase)
pesan.style.padding = "12px";
```

> **Catatan Properti CSS:** Di CSS biasa kita menulis `background-color` dengan tanda hubung. Di JavaScript, karena tanda hubung dianggap pengurangan matematika, nama properti ditulis dengan format **`camelCase`** (`backgroundColor`, `fontSize`, `borderRadius`).

Coba jalankan editor latihan di bawah dan perhatikan bagaimana teks di jendela pratinjau langsung diperbarui oleh JavaScript!
