---
id: js-lanjutan-3
courseId: fe-js-03
order: 23
title: "Batas Wilayah Variabel: Global Scope vs Block Scope"
readingMinutes: 5
section: LANJUTAN
learningObjectives:
  - Memahami konsep Scope sebagai batas jangkauan akses suatu variabel di dalam kode
  - Membedakan antara Global Scope (akses menyeluruh) dan Block Scope (akses terbatas di dalam kurung kurawal)
  - Mengetahui mengapa isolasi variabel penting untuk mencegah konflik penamaan
codeLanguage: javascript
codeSnippet: |
  const global = "Saya terlihat di mana-mana";
  {
    const lokal = "Saya hanya terlihat di dalam kurung ini";
  }
  // console.log(lokal); // ReferenceError: lokal is not defined
starterJs: |
  const namaAplikasi = "CodeAcademy"; // Global Scope

  function prosesStatusPengguna(poin) {
    // Variabel level berada dalam Block Scope fungsi ini
    const level = poin >= 100 ? "Master" : "Pemula";
    console.log(`[Di dalam fungsi] Aplikasi: ${namaAplikasi}, Level: ${level}`);
  }

  prosesStatusPengguna(120);

  // Variabel namaAplikasi tetap dapat diakses di luar fungsi
  console.log(`[Di luar fungsi] Selamat datang di ${namaAplikasi}!`);
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Pastikan output yang muncul adalah:
  [Di dalam fungsi] Aplikasi: CodeAcademy, Level: Master
  [Di luar fungsi] Selamat datang di CodeAcademy!
---

# Batas Wilayah Variabel: Global Scope vs Block Scope

Pernahkah kamu bertanya-tanya: *"Jika saya membuat variabel di dalam fungsi atau di dalam percabangan if, bisakah variabel itu dipanggil dari luar?"*

Aturan tentang di mana suatu variabel boleh dibaca atau diubah disebut **Scope (Cakupan Wilayah)**.

---

## Analogi KTP Nasional vs Kunci Kamar

- **KTP Nasional (Global)**: Berlaku dan diakui di mana saja di seluruh penjuru negeri.
- **Kunci Kamar Kos (Block / Lokal)**: Hanya berguna dan hanya bisa membuka pintu di kamar kos kamu sendiri. Orang di luar jalanan tidak bisa menggunakan kunci tersebut.

---

## 1. Global Scope

Variabel yang dideklarasikan di luar fungsi atau di luar blok kurung kurawal mana pun disebut berada di **Global Scope**.

```javascript
const mataUang = "IDR"; // Global

function cetakHarga(jumlah) {
  // Variabel global dapat dibaca dari dalam fungsi mana pun
  console.log(`${mataUang} ${jumlah}`);
}

cetakHarga(50000); // Output: IDR 50000
```

Meskipun praktis, hindari membuat terlalu banyak variabel global karena berisiko tertimpa atau bentrok tanpa sengaja saat aplikasimu semakin besar.

---

## 2. Block Scope (`let` dan `const`)

Setiap kali kamu melihat sepasang tanda kurung kurawal `{ ... }` (di dalam fungsi, percabangan `if`, atau perulangan `for`), kamu sedang melihat sebuah **Blok**.

Variabel yang dibuat menggunakan `let` atau `const` di dalam blok tersebut **terkunci di dalamnya dan tidak bisa diakses dari luar**:

```javascript
if (true) {
  const rahasia = "123456";
  console.log(rahasia); // Berhasil: mencetak "123456"
}

console.log(rahasia); 
// Error! ReferenceError: rahasia is not defined
```

Begitu blok `{}` selesai dieksekusi, variabel `rahasia` akan dihapus dari memori.

### Mengapa Block Scope Sangat Membantu?
Karena variabel terisolasi rapi, kamu bebas menggunakan nama variabel umum seperti `hasil`, `total`, atau `i` di berbagai fungsi berbeda tanpa khawatir mereka akan saling bertabrakan atau merusak satu sama lain!

Di modul selanjutnya, kita akan mempelajari wadah data yang lebih canggih untuk memodelkan benda nyata: **JavaScript Object**!
