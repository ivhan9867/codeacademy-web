---
id: node-lanjutan-1
courseId: be-node-01
order: 16
title: "Manajemen Paket dengan npm dan Anatomi package.json"
readingMinutes: 6
section: LANJUTAN
learningObjectives:
  - "Memahami peran npm registry sebagai repositori pustaka kode terbuka terbesar di dunia"
  - "Membedah struktur file package.json (metadata, scripts, dependencies, devDependencies)"
  - "Memahami sistem Semantic Versioning (SemVer) dengan simbol caret (^) dan tilde (~)"
codeLanguage: javascript
codeSnippet: |
  // Representasi struktur data package.json
  const manifest = {
    name: "toko-api",
    version: "1.0.0",
    dependencies: {
      express: "^4.19.2"
    },
    scripts: {
      start: "node server.js"
    }
  };
  console.log(`Menjalankan script: ${manifest.scripts.start}`);
---

# Manajemen Paket dengan npm dan Anatomi package.json

Sebagai insinyur back-end, kita tidak perlu menciptakan roda dari nol untuk setiap fitur. Jika kita membutuhkan enkripsi kata sandi yang aman, manipulasi tanggal yang kompleks, atau pembuatan token JWT, kita bisa memanfaatkan modul-modul buatan komunitas dunia melalui **npm (*Node Package Manager*)**.

npm otomatis terpasang saat kamu menginstal Node.js di komputermu.

![Anatomi package.json dan Ekosistem npm](asset:images/be-node-01/npm-package-json.png)

---

## 1. Jantung Proyek: `package.json`

Setiap proyek Node.js profesional selalu diawali dengan pembuatan file manifesto bernama **`package.json`**. File ini mencatat identitas aplikasi, skrip otomasi, serta daftar pustaka pihak ketiga yang dibutuhkan aplikasi tersebut.

Di komputer lokal, file ini dibuat dengan menjalankan perintah terminal:
```bash
npm init -y
```

Berikut adalah contoh anatomi isi file `package.json`:

```json
{
  "name": "portal-berita-api",
  "version": "1.0.0",
  "description": "REST API backend untuk portal berita nasional",
  "main": "server.js",
  "scripts": {
    "start": "node server.js",
    "dev": "node --watch server.js"
  },
  "dependencies": {
    "express": "^4.19.2",
    "dotenv": "^16.4.5"
  },
  "devDependencies": {
    "nodemon": "^3.1.0"
  }
}
```

---

## 2. Dependencies vs DevDependencies

Perhatikan dua kelompok dependensi di atas:

1. **`dependencies`**: Pustaka yang **wajib ada saat aplikasi berjalan di server produksi**. Contohnya framework web (`express`) dan library koneksi database. Diinstal dengan:
   ```bash
   npm install express
   ```
2. **`devDependencies`**: Alat bantu yang **hanya dibutuhkan oleh programmer saat mengetik kode di laptop pengembang**, seperti linter kode, compiler TypeScript, atau tool reload otomatis. Diinstal dengan bendera `-D`:
   ```bash
   npm install -D nodemon
   ```

---

## 3. Rahasia Semantic Versioning (SemVer)

Pernahkah kamu memperhatikan angka versi seperti `"express": "^4.19.2"`? Format ini disebut **Semantic Versioning** dengan pola:

$$\text{MAJOR} . \text{MINOR} . \text{PATCH}$$

- **MAJOR (4)**: Berubah jika ada perombakan besar yang merusak kompatibilitas kode lama (*breaking changes*).
- **MINOR (19)**: Berubah jika ada penambahan fitur baru yang tetap aman untuk kode lama.
- **PATCH (2)**: Berubah saat ada perbaikan bug kecil atau penambalan celah keamanan.

### Arti Simbol di Depan Versi:
- **Caret (`^4.19.2`)**: npm boleh memperbarui versi ke MINOR dan PATCH terbaru (misal naik ke `4.20.0`), namun dilarang keras melompat ke MAJOR `5.0.0`. Ini adalah format default yang paling populer.
- **Tilde (`~4.19.2`)**: npm hanya boleh memperbarui PATCH keamanan saja (misal naik ke `4.19.3`), tidak boleh mengubah minor.
- **Tanpa simbol (`4.19.2`)**: Versi terkunci mati persis pada nomor tersebut.

---

## 4. Folder `node_modules` dan `package-lock.json`

Ketika kamu menginstal paket via npm, dua hal akan tercipta:
- **`node_modules/`**: Folder tempat file fisik kode pustaka diunduh. Folder ini bisa sangat besar (ratusan megabyte) dan **TIDAK BOLEH diunggah ke Git/GitHub** (cukup masukkan ke file `.gitignore`).
- **`package-lock.json`**: Buku catatan resmi yang mengunci pohon dependensi secara presisi hingga ke sub-dependensi terkecil. Berkas ini memastikan bahwa ketika rekan setimmu atau server cloud menjalankan `npm install`, versi yang terpasang identik 100% tanpa perbedaan satu byte pun!

Di modul berikutnya, kita akan membangun pondasi utama backend internet: **Membuat Web Server Pertama dengan Modul Bawaan `http`!**
