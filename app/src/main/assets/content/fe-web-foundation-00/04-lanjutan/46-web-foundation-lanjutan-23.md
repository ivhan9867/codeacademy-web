---
id: web-foundation-lanjutan-23
courseId: fe-web-foundation-00
order: 46
title: "Atribut pada Elemen Input"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Mengatur identitas input dengan atribut name dan id untuk pemrosesan data"
  - "Menerapkan validasi dasar di sisi klien menggunakan atribut required, placeholder, min/max, dan pattern"
codeSnippet: |
  <div>
    <label for="nama">Nama</label>
    <br>
    <input type="text" id="nama" name="nama" placeholder="Nama lengkap" required>
  </div>
  
  <div>
    <label for="email">Email</label>
    <br>
    <input type="email" id="email" name="email" placeholder="nama@email.com" required>
  </div>
---
Elemen `<input>` memiliki beragam atribut pendukung. Atribut ini memandu pengguna sekaligus memvalidasi data formulir. Agar tidak membingungkan, mari pelajari atribut-atribut ini berdasarkan kelompok fungsinya.

## 1. Identitas & Pengiriman Data

Kelompok ini bertugas memberi nama dan nilai data pada kolom input:

- **`name`**: label penanda data saat dikirim ke server. Tanpa `name`, data input **tidak akan terkirim**.
- **`value`**: nilai awal (*default*) yang langsung terisi di dalam kolom.
- **`id`**: identitas unik elemen di halaman HTML, berguna untuk menghubungkan input dengan `<label>`.

```html
<label for="username">Nama Pengguna:</label>
<input type="text" id="username" name="username" value="budi_santoso">
```

## 2. Panduan Pengisian bagi Pengguna

Kelompok ini membantu pengguna memahami data apa yang diminta:

- **`placeholder`**: teks petunjuk samar di dalam kolom kosong. Teks ini otomatis hilang saat pengguna mulai mengetik.
- **`autocomplete`**: izin bagi browser untuk mengisi otomatis kolom berdasarkan riwayat ketikan pengguna.

```html
<label for="emailPengguna">Alamat Email:</label>
<input type="email" id="emailPengguna" name="email" placeholder="nama@domain.com" autocomplete="email">
```

## 3. Pembatas Panjang Teks & Rentang Angka

Kelompok ini membatasi jumlah karakter atau besaran angka yang boleh diketik:

- **`minlength`** dan **`maxlength`**: batas minimal dan maksimal jumlah karakter teks.
- **`min`** dan **`max`**: batas nilai terendah dan tertinggi untuk angka atau tanggal.
- **`step`**: interval lompatan angka yang diizinkan (misal kelipatan 5).

```html
<!-- Membatasi kode pos tepat 5 digit -->
<input type="text" name="kodepos" minlength="5" maxlength="5" placeholder="12345">

<!-- Membatasi usia antara 17 hingga 60 tahun -->
<input type="number" name="usia" min="17" max="60" value="20">
```

## 4. Status & Kontrol Akses Kolom

Kelompok ini mengatur apakah kolom wajib diisi, dapat diedit, atau dinonaktifkan:

- **`required`**: menandai kolom wajib diisi. Formulir tidak bisa dikirim jika kolom ini kosong.
- **`readonly`**: teks hanya bisa dibaca dan disorot, tidak bisa diubah. Datanya **tetap ikut terkirim**.
- **`disabled`**: menonaktifkan kolom sepenuhnya dan tampak abu-abu. Datanya **tidak ikut terkirim**.
- **`checked`**: membuat kotak pilihan (*checkbox* atau *radio*) langsung tercentang sejak awal.

```html
<!-- Kolom wajib diisi -->
<input type="password" name="sandi" required>

<!-- Data akun tetap terkirim meski tidak bisa diedit pengguna -->
<input type="text" name="peran" value="Member" readonly>

<!-- Checkbox persetujuan langsung aktif -->
<input type="checkbox" name="setuju" checked> Setuju dengan aturan
```

## 5. Pemilihan Berkas & Validasi Pola Khusus

Kelompok ini mengatur kriteria data khusus:

- **`accept`**: membatasi format berkas yang boleh dipilih pada `type="file"`, misalnya gambar atau PDF.
- **`multiple`**: mengizinkan pengguna memilih lebih dari satu berkas atau memasukkan beberapa email.
- **`pattern`**: aturan ekspresi reguler (*regex*) yang wajib dipenuhi agar data dianggap sah.

```html
<!-- Hanya menerima berkas foto PNG atau JPG -->
<input type="file" name="foto" accept=".png, .jpg, .jpeg" multiple>

<!-- Wajib diawali huruf ID diikuti 4 angka -->
<input type="text" name="memberId" pattern="ID[0-9]{4}" placeholder="ID1234">
```

## Validasi Otomatis Browser

Browser modern memeriksa atribut-atribut di atas secara otomatis. Jika ada kolom `required` yang kosong atau format email tidak sesuai, browser langsung menampilkan pesan peringatan tanpa memerlukan kode JavaScript tambahan.

## Kesalahan yang Sering Terjadi

1. **Lupa memberi atribut `name`**: Akibatnya, isian pengguna tidak pernah sampai ke server.
2. **Tertukar antara `disabled` dan `readonly`**: Gunakan `readonly` bila data tetap ingin dikirim ke server. Gunakan `disabled` bila data memang ingin diabaikan.

## Coba Sendiri

Periksa formulir yang sudah kamu buat sebelumnya. Tambahkan atribut `required` dan `placeholder` pada input "Nama" dan "Email". Pastikan kedua input tersebut juga sudah memiliki atribut `name` yang tepat.
                    
