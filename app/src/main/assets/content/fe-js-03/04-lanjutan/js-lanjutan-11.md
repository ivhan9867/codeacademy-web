---
id: js-lanjutan-11
courseId: fe-js-03
order: 31
title: "Menyimpan Data di Browser dengan localStorage & JSON"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Memahami kegunaan localStorage agar data aplikasi tetap tersimpan setelah halaman dimuat ulang
  - Menggunakan JSON.stringify() dan JSON.parse() untuk menyimpan serta membaca objek atau array di localStorage
  - Menerapkan blok try/catch untuk melindungi aplikasi dari error saat membaca data JSON yang rusak
codeLanguage: javascript
codeSnippet: |
  const catatan = [{ teks: "Belajar JSON", selesai: true }];
  localStorage.setItem("catatan", JSON.stringify(catatan));
  const hasil = JSON.parse(localStorage.getItem("catatan") ?? "[]");
  console.log(hasil[0].teks);
starterJs: |
  const KUNCI_PENYIMPANAN = "profil_belajar_js";

  const dataProfil = {
    nama: "Bima",
    modulSelesai: 31,
    tema: "gelap"
  };

  // Fungsi aman untuk menyimpan data objek/array ke localStorage
  const simpanKeStorage = (kunci, nilai) => {
    try {
      const jsonTeks = JSON.stringify(nilai);
      localStorage.setItem(kunci, jsonTeks);
      console.log("Tersimpan sebagai JSON string:", jsonTeks);
    } catch (error) {
      console.log("Gagal menyimpan ke storage:", error.message);
    }
  };

  // Fungsi aman untuk membaca kembali dan mengubah string JSON menjadi objek
  const bacaDariStorage = (kunci, nilaiDefault) => {
    try {
      const mentah = localStorage.getItem(kunci);
      if (mentah === null) return nilaiDefault;
      return JSON.parse(mentah);
    } catch (error) {
      console.log("Data JSON rusak, memakai nilai default.");
      return nilaiDefault;
    }
  };

  simpanKeStorage(KUNCI_PENYIMPANAN, dataProfil);
  const profilTerbaca = bacaDariStorage(KUNCI_PENYIMPANAN, {});
  console.log("Nama terbaca kembali:", profilTerbaca.nama);
  console.log("Modul selesai:", profilTerbaca.modulSelesai);
playgroundTask: |
  Jalankan kode bawaan dan buka Console. Perhatikan bagaimana objek JavaScript diubah menjadi teks JSON terlebih dahulu lalu dibaca kembali menjadi objek utuh:
  Tersimpan sebagai JSON string: {"nama":"Bima","modulSelesai":31,"tema":"gelap"}
  Nama terbaca kembali: Bima
  Modul selesai: 31
verificationRules:
  - "js:JSON.stringify"
  - "js:JSON.parse"
---

# Menyimpan Data di Browser dengan localStorage & JSON

Pernahkah kamu mengisi daftar tugas atau mengganti tema menjadi *Dark Mode* di sebuah website, lalu ketika halaman di-refresh, pengaturanmu tetap aman dan tidak hilang?

Variabel JavaScript biasa (`let` dan `const`) hanya hidup di memori sementara (RAM)—begitu tab ditutup atau dimuat ulang, semua isinya musnah. Untuk menyimpannya secara persisten di perangkat pengguna, browser menyediakan **`localStorage`**.

---

## Aturan Penting: `localStorage` Hanya Menerima String!

`localStorage` bekerja seperti lemari loker berpasangan **kunci (*key*) dan nilai (*value*)**. Namun ada satu syarat mutlak: **seluruh nilai yang disimpan di `localStorage` wajib berupa teks (`String`)**.

Jika kamu langsung memasukkan array `["Apel", "Jeruk"]` atau objek `{ nama: "Bima" }`, JavaScript akan memaksanya berubah menjadi teks `"[object Object]"` dan datamu rusak!

Di sinilah peran format **JSON (*JavaScript Object Notation*)** sebagai penerjemah dua arah:

```text
  [ Objek / Array JS ]
           |
           |  JSON.stringify(data)   <-- Mengubah objek menjadi teks JSON
           v
  '{"nama":"Bima","tema":"gelap"}'
           |
           |  localStorage.setItem("profil", teksJson)
           v
  +------------------------+
  |  Browser localStorage  |  (Data bertahan walau browser ditutup!)
  +------------------------+
           |
           |  localStorage.getItem("profil")
           v
  '{"nama":"Bima","tema":"gelap"}'
           |
           |  JSON.parse(teksJson)   <-- Mengubah teks JSON kembali jadi objek JS
           v
  [ Objek / Array JS Utuh ]
```

---

## 1. Tiga Method Dasar `localStorage`

1. **Menyimpan atau memperbarui data (`setItem`)**:
   ```javascript
   localStorage.setItem("namaPengguna", "Sari");
   ```
2. **Membaca data (`getItem`)**:
   Mengembalikan string jika kunci ditemukan, atau **`null`** jika kunci belum ada.
   ```javascript
   const nama = localStorage.getItem("namaPengguna"); // "Sari" atau null
   ```
3. **Menghapus satu kunci (`removeItem`)**:
   ```javascript
   localStorage.removeItem("namaPengguna");
   ```

---

## 2. Mengubah Objek <-> JSON dengan `stringify` & `parse`

Untuk menyimpan array atau objek secara utuh:

```javascript
const daftarTugas = [
  { judul: "Pahami JSON", selesai: true },
  { judul: "Coba localStorage", selesai: false }
];

// Simpan: Objek -> String JSON
localStorage.setItem("tugas", JSON.stringify(daftarTugas));

// Ambil: String JSON -> Objek
const teksTersimpan = localStorage.getItem("tugas");
const tugasPulih = teksTersimpan ? JSON.parse(teksTersimpan) : [];
console.log(tugasPulih[0].judul); // "Pahami JSON"
```

---

## 3. Wajib Bungkus `JSON.parse()` dengan `try/catch`!

Apa yang terjadi jika data di `localStorage` terpotong atau tidak sengaja diubah manual oleh pengguna menjadi bukan format JSON valid (misalnya `"{rusak"`)?

`JSON.parse("{rusak")` akan langsung melempar **`SyntaxError`** dan membuat seluruh aplikasi berhenti bekerja!

Solusi profesionalnya adalah membungkus proses parsing dengan **`try/catch`**:

```javascript
const muatDataAman = (kunci, fallback) => {
  try {
    const mentah = localStorage.getItem(kunci);
    if (mentah === null) return fallback;
    return JSON.parse(mentah);
  } catch (err) {
    console.log("Format JSON tidak valid, kembali ke nilai awal.");
    return fallback;
  }
};
```

Dengan fungsi pembantu ini, aplikasi kamu dijamin tidak akan pernah *crash* hanya karena data penyimpanan lokal bermasalah. Jalankan kode di playground untuk melihat proses konversi JSON bolak-balik di Console!
