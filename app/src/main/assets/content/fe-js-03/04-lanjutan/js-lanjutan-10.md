---
id: js-lanjutan-10
courseId: fe-js-03
order: 30
title: "Menangani Form HTML, preventDefault(), & Validasi Input"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - Memahami alasan browser memuat ulang halaman saat form dikirim dan cara mencegahnya dengan event.preventDefault()
  - Menangkap event submit pada elemen <form> menggunakan addEventListener()
  - Melakukan validasi input teks dengan trim() serta menampilkan pesan error atau sukses secara rapi di DOM
codeLanguage: javascript
codeSnippet: |
  const form = document.querySelector("#form-daftar");
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    console.log("Form diproses tanpa reload halaman!");
  });
starterHtml: |
  <div style="font-family: sans-serif; max-width: 380px; margin: 0 auto; padding: 18px; border: 1px solid #E2E8F0; border-radius: 10px; background: #FFFFFF;">
    <h3 style="margin-top: 0; color: #0F172A;">Form Pendaftaran Kelas</h3>
    <form id="form-kelas">
      <label for="input-nama" style="display: block; font-size: 13px; font-weight: bold; color: #334155; margin-bottom: 4px;">Nama Lengkap</label>
      <input id="input-nama" type="text" placeholder="Minimal 3 karakter..." style="width: 100%; box-sizing: border-box; padding: 8px 10px; border: 1px solid #CBD5E1; border-radius: 6px; margin-bottom: 10px;" />

      <label for="input-email" style="display: block; font-size: 13px; font-weight: bold; color: #334155; margin-bottom: 4px;">Email</label>
      <input id="input-email" type="text" placeholder="contoh@email.com" style="width: 100%; box-sizing: border-box; padding: 8px 10px; border: 1px solid #CBD5E1; border-radius: 6px; margin-bottom: 12px;" />

      <button type="submit" style="width: 100%; background: #0D9488; color: #FFFFFF; border: none; padding: 10px; border-radius: 6px; font-weight: bold; cursor: pointer;">
        Daftar Sekarang
      </button>
    </form>
    <p id="pesan-feedback" style="margin: 12px 0 0 0; font-size: 13px; font-weight: 600;"></p>
  </div>
starterJs: |
  const form = document.querySelector("#form-kelas");
  const inputNama = document.querySelector("#input-nama");
  const inputEmail = document.querySelector("#input-email");
  const pesanFeedback = document.querySelector("#pesan-feedback");

  if (form && inputNama && inputEmail && pesanFeedback) {
    form.addEventListener("submit", (event) => {
      // Mencegah browser me-reload halaman saat form dikirim
      event.preventDefault();

      const nama = inputNama.value.trim();
      const email = inputEmail.value.trim();

      // Validasi input kosong atau terlalu pendek
      if (nama.length < 3) {
        pesanFeedback.textContent = "Error: Nama minimal harus 3 karakter.";
        pesanFeedback.style.color = "#DC2626";
        console.log("Validasi gagal: nama terlalu pendek");
        return;
      }

      // Validasi format email sederhana
      if (!email.includes("@") || !email.includes(".")) {
        pesanFeedback.textContent = "Error: Masukkan alamat email yang valid.";
        pesanFeedback.style.color = "#DC2626";
        console.log("Validasi gagal: email tidak valid");
        return;
      }

      // Jika semua lolos validasi
      pesanFeedback.textContent = `Pendaftaran berhasil untuk ${nama}!`;
      pesanFeedback.style.color = "#0D9488";
      console.log(`Form valid! Terdaftar: ${nama} (${email})`);
      form.reset();
    });
  }
playgroundTask: |
  Tekan 'Jalankan'. Pada jendela pratinjau:
  1. Langsung klik 'Daftar Sekarang' dalam keadaan kosong dan amati pesan error merah yang muncul.
  2. Isi Nama (minimal 3 huruf) dan Email yang mengandung '@' dan '.', lalu klik 'Daftar Sekarang' lagi.
  3. Verifikasi pesan sukses berwarna toska serta log Console: 'Form valid! Terdaftar: ...'
verificationRules:
  - "js:preventDefault"
  - "js:addEventListener"
---

# Menangani Form HTML, preventDefault(), & Validasi Input

Hampir setiap aplikasi web—mulai dari kolom login, pencarian, hingga formulir pendaftaran—menggunakan tag `<form>`. Namun, saat pemula mencoba memproses form dengan JavaScript, sering muncul satu kejadian aneh: **halaman tiba-tiba berkedip dan me-reload ulang, lalu seluruh data di Console hilang!**

Mengapa demikian, dan bagaimana cara mengendalikannya dengan rapi?

---

## 1. Perilaku Bawaan Form & Peran `event.preventDefault()`

Secara bawaan sejak awal sejarah web, ketika pengguna menekan tombol `type="submit"` atau tombol `Enter` di dalam `<form>`, browser otomatis mengirim data ke URL server dan **memuat ulang (*refresh*) halaman**.

Untuk mengambil alih kendali menggunakan JavaScript (validasi di sisi klien atau mengirim data lewat API tanpa *reload*), kita harus memanggil **`event.preventDefault()`** di dalam event listener `"submit"`:

```text
  Pengguna klik Submit / tekan Enter
                 |
                 v
     Event "submit" terpicu pada <form>
                 |
                 +-- Tanpa preventDefault() --> Halaman reload (state JS hilang!)
                 |
                 +-- Dengan event.preventDefault() --> Reload dibatalkan!
                                                       JavaScript bebas validasi input
                                                       & tampilkan umpan balik di DOM
```

> **Tips Penting:** Selalu pasang `addEventListener("submit", ...)` pada elemen **`<form>`**, bukan `click` pada tombolnya. Dengan begitu, pengguna yang menekan tombol **Enter** di keyboard juga otomatis tertangani!

---

## 2. Membaca Nilai Input dengan `.trim()`

Pengguna sering tidak sengaja mengetik spasi kosong saja (misalnya `"   "`). Jika kita hanya mengecek `if (nama === "")`, spasi tersebut dianggap valid!

Selalu bersihkan spasi di awal dan akhir teks dengan **`.trim()`** sebelum melakukan validasi:

```javascript
const nama = inputNama.value.trim();

if (nama === "") {
  console.log("Nama tidak boleh kosong!");
}
```

---

## 3. Pola Validasi Bersih dengan *Early Return*

Agar kode validasi tidak dipenuhi `if/else` yang bersarang terlalu dalam, gunakan pola **early return** (periksa kondisi gagal terlebih dahulu, tampilkan pesan error, lalu langsung `return`):

```javascript
form.addEventListener("submit", (event) => {
  event.preventDefault();

  const nama = inputNama.value.trim();
  const email = inputEmail.value.trim();

  if (nama.length < 3) {
    tampilkanError("Nama minimal 3 karakter.");
    return; // Berhenti di sini, jangan lanjutkan ke bawah
  }

  if (!email.includes("@")) {
    tampilkanError("Format email tidak valid.");
    return;
  }

  // Kode di bawah ini dijamin hanya berjalan jika seluruh input valid
  tampilkanSukses(`Halo, ${nama}!`);
  form.reset(); // Mengosongkan semua input di dalam form
});
```

### Keunggulan Pola Ini
1. **Satu fokus per blok**: Setiap aturan validasi terpisah dengan jelas dan mudah ditambah.
2. **Umpan balik ramah pengguna**: Alih-alih memakai `alert()` yang mengganggu, kita menulis pesan langsung ke elemen `<p>` di halaman dengan warna merah untuk error dan hijau/toska untuk sukses.
3. **`form.reset()`**: Sekali panggil, seluruh kotak input kembali bersih setelah data berhasil diproses.

Coba praktikkan langsung form interaktif di editor playground di bawah ini!
