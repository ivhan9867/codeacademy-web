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

