# CodeAcademy Web Ecosystem Portal

Repositori web ini berisi antarmuka pendukung untuk aplikasi **CodeAcademy Mobile** (Proyek Tugas Akhir):
1. **Landing Page Terpadu** (`index.html`)
2. **Portal Fasilitator & Penilaian Tugas Akhir** (`facilitator/index.html`)
3. **Portal Verifikasi Kredensial Sertifikat** (`verify.html`)
4. **Buku Panduan Pengguna / Manual Book** (`docs/Manual_Book_CodeAcademy.html` & `docs/Manual_Book_CodeAcademy.pdf`)

---

## Struktur Berkas
```text
public/
├── index.html                   # Halaman utama ekosistem CodeAcademy
├── verify.html                  # Portal publik verifikasi sertifikat kelulusan
├── README.md                    # Petunjuk deployment & hosting
├── facilitator/
│   ├── index.html               # Dashboard penilaian penguji / fasilitator
│   ├── style.css                # Desain visual dashboard & komponen
│   └── app.js                   # Logika real-time Firebase Firestore & review
└── docs/
    ├── Manual_Book_CodeAcademy.html  # Versi web Manual Book (bisa langsung dicetak ke PDF)
    ├── Manual_Book_CodeAcademy.pdf   # Berkas PDF resmi (19 halaman A4)
    └── manual_book_assets/           # Aset gambar mockup resolusi tinggi
```

---

## Cara Menjalankan Secara Lokal

### Menggunakan Python:
```bash
python tools/serve_web.py
```
atau dari dalam folder `public`:
```bash
cd public
python -m http.server 8080
```
Buka di browser:
- **Landing Portal**: [http://localhost:8080/](http://localhost:8080/)
- **Dashboard Fasilitator**: [http://localhost:8080/facilitator/](http://localhost:8080/facilitator/)
  - **PIN Masuk**: `admin123`
- **Verifikasi Sertifikat**: [http://localhost:8080/verify.html](http://localhost:8080/verify.html)

---

## Cara Deployment ke GitHub Pages

### Opsi 1: Satu Repositori Penuh dengan Folder `public`
1. Push seluruh folder project ke repositori GitHub Anda.
2. Di repositori GitHub: buka tab **Settings** > **Pages**.
3. Di bagian **Build and deployment**:
   - Source: **Deploy from a branch**
   - Branch: `main` / `master`
   - Folder: pilih `/ (root)` atau jika Anda memindahkan isi `public/` ke branch `gh-pages` / folder `docs`.

### Opsi 2: Menggunakan Branch `gh-pages` Khusus
Jika Anda ingin folder `public/` langsung menjadi website utama:
```bash
git subtree push --prefix public origin gh-pages
```
Website Anda akan langsung aktif di:
`https://<username-github>.github.io/<nama-repo>/`

---

## Konfigurasi Firebase Firestore
Aplikasi web ini menggunakan Firebase JavaScript SDK (v9 compat). Konfigurasi default terhubung ke project Firebase CodeAcademy:
- File konfigurasi: `public/facilitator/app.js` dan `public/verify.html`
- Collection Firestore: `project_submissions`
- Dokumen ID: `{studentUid}_{courseId}`
