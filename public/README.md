# CodeAcademy Web Ecosystem Portal

Repositori web ini berisi antarmuka pendukung untuk aplikasi **CodeAcademy Mobile** (Proyek Tugas Akhir):
1. **Landing Page Terpadu** (`index.html`)
2. **Portal Fasilitator & Penilaian Tugas Akhir** (`facilitator/index.html`)
3. **Portal Verifikasi Kredensial Sertifikat** (`verify.html`)
4. **Buku Panduan Pengguna / Manual Book** (`docs/Manual_Book_CodeAcademy.html` & `docs/Manual_Book_CodeAcademy.docx`)

---

## Struktur Berkas
```text
public/
├── index.html                        # Halaman utama ekosistem CodeAcademy
├── verify.html & verify.js           # Portal publik verifikasi sertifikat ke Firestore
├── firebase-config.js                # Konfigurasi Firebase Web SDK
├── README.md                         # Petunjuk deployment & hosting
├── assets/
│   ├── logo.svg & logo.png           # Ikon & logo aplikasi CodeAcademy
│   ├── site.css & particles.js       # Gaya dan efek halaman landing & verifikasi
├── facilitator/
│   ├── index.html                    # Dashboard fasilitator (Proyek, Forum, Pengumuman, Sertifikat)
│   ├── style.css                     # Desain visual dashboard & komponen modal 2-kolom
│   └── app.js                        # Logika Firebase Auth + real-time Firestore
└── docs/
    ├── Manual_Book_CodeAcademy.html  # Versi web Manual Book (A4 siap cetak)
    ├── Manual_Book_CodeAcademy.docx  # Versi Word (.docx) yang dapat diedit
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
  - **Login Fasilitator (Live)**: Masuk menggunakan akun Firebase Auth (Email/Password atau Google Sign-In) yang email-nya terdaftar pada dokumen koleksi Firestore `/facilitators/{email}`.
  - **Mode Demo Offline**: Klik *"Masuk Mode Demo Offline"* pada halaman login untuk meninjau alur UI tanpa menulis ke Firebase.
- **Verifikasi Sertifikat**: [http://localhost:8080/verify.html](http://localhost:8080/verify.html) (mendukung parameter `?id=CC-...`)

---

## Konfigurasi Firebase & Keamanan (`firestore.rules`)
- Aturan keamanan Firestore tersedia di root repositori pada berkas `firestore.rules`.
- Untuk memberi akses admin fasilitator ke sebuah akun Firebase:
  1. Aktifkan **Email/Password** dan **Google** provider pada Firebase Authentication.
  2. Buat dokumen di Firestore pada koleksi `facilitators` dengan ID dokumen berupa alamat email huruf kecil (contoh: `facilitators/dosen@kampus.ac.id`) dan field `{ "name": "Nama Fasilitator", "role": "facilitator" }`.
- Koleksi utama yang digunakan bersama aplikasi Android:
  - `project_submissions` (ID dokumen: `{firebaseUidOrAccountId}_{courseId}`)
  - `forum_posts` (thread diskusi & balasan kelas)
  - `certificates` (ID sertifikat format `CC-yyyyMMxxxx-xxxxxx`)
  - `announcements` (pengumuman kelas dari fasilitator ke Notifikasi Android)
  - `users` (profil & leaderboard XP)
