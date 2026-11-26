---
id: js-prep-ref
courseId: fe-js-03
order: 5
title: "Referensi Lanjutan & Dokumentasi JavaScript"
readingMinutes: 4
section: PERSIAPAN
learningObjectives:
  - Mengetahui sumber dokumentasi JavaScript tepercaya yang digunakan programmer profesional
  - Memilih referensi belajar lanjutan yang sesuai dengan gaya belajar masing-masing
  - Membaca anatomi pesan error dan Stack Trace di Console agar proses debugging lebih cepat
---

# Referensi Lanjutan & Dokumentasi JavaScript

Kelas ini dirancang agar kamu memahami fondasi logika JavaScript secara bertahap dan terarah. Namun di dunia pengembangan web nyata, programmer profesional tidak pernah berhenti membuka dokumentasi—bukan karena lupa, melainkan karena selalu ada detail baru yang perlu dipastikan.

Berikut tiga referensi terbaik yang bisa kamu simpan sebagai pegangan jangka panjang, ditambah panduan cepat membaca jejak error di Console.

---

## 1. Tiga Referensi Utama JavaScript

| Sumber Belajar | Fokus Utama | Cocok Untuk Siapa |
| :--- | :--- | :--- |
| **MDN Web Docs (JavaScript Guide)** | Dokumentasi standar web paling lengkap: detail sintaks, parameter method, kompatibilitas browser, hingga spesifikasi DOM & Web API. | Saat kamu lupa urutan argumen method (misalnya `.slice()`) atau butuh kepastian perilaku fitur browser resmi. |
| **JavaScript.info** | Tutorial terstruktur dari dasar hingga topik lanjutan (*closures*, *promises*, *event loop*, DOM) dengan penjelasan visual dan contoh kasus nyata. | Saat kamu ingin mendalami alasan teknis di balik cara kerja JavaScript secara menyeluruh. |
| **Eloquent JavaScript** | Buku digital gratis karya Marijn Haverbeke yang melatih cara berpikir algoritmis melalui studi kasus dan proyek latihan menantang. | Saat kamu sudah nyaman dengan dasar sintaks dan ingin mengasah logika pemecahan masalah lebih tajam. |

> **Tips Mencari Dokumentasi dengan Cepat:** Saat mencari sesuatu di mesin pencari, tambahkan kata kunci **`mdn`** di belakangnya—misalnya `"array find mdn"` atau `"localStorage mdn"`. Kamu akan langsung diarahkan ke halaman dokumentasi paling akurat.

---

## 2. Cara Membaca Console Stack Trace Saat Terjadi Error

Ketika kode gagal berjalan dan muncul teks merah di Console, jangan langsung menebak-nebak secara acak. Browser sebenarnya sudah memberi peta lokasi error yang disebut **Stack Trace**.

Perhatikan contoh pesan error berikut:

```text
Uncaught TypeError: Cannot read properties of undefined (reading 'toUpperCase')
    at formatNama (app.js:12:24)
    at tampilkanProfil (app.js:18:5)
    at app.js:22:1
```

Mari kita bedah pesan tersebut menjadi 3 bagian penting:

```text
+-------------------------------------------------------------------+
| 1. JENIS & PESAN ERROR                                            |
|    TypeError: Cannot read properties of undefined                 |
|    -> Artinya: Ada variabel bernilai undefined yang dipanggil     |
|       seolah-olah memiliki properti/method (.toUpperCase).        |
+-------------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------------+
| 2. TITIK AWAL MASALAH (Baris paling atas di Stack Trace)          |
|    at formatNama (app.js:12:24)                                   |
|    -> Artinya: Error meledak di fungsi formatNama, file app.js,   |
|       baris ke-12, karakter (kolom) ke-24.                        |
+-------------------------------------------------------------------+
                              |
                              v
+-------------------------------------------------------------------+
| 3. JALUR PEMANGGILAN (Urutan terbalik ke pemanggil awal)          |
|    at tampilkanProfil (app.js:18:5)  <-- yang memanggil formatNama|
|    at app.js:22:1                    <-- titik awal eksekusi      |
+-------------------------------------------------------------------+
```

### Langkah Praktis Saat Menemui Error di Console

1. **Baca jenis error-nya terlebih dahulu**:
   - `SyntaxError`: Biasanya lupa tanda kurung `)`, kurawal `}`, koma `,`, atau kutip `"`.
   - `ReferenceError`: Nama variabel salah ketik atau variabel dipanggil sebelum dideklarasikan.
   - `TypeError`: Nilai variabel ternyata `null` atau `undefined` saat kamu mengakses `.property` atau memanggil `fungsi()`, atau kamu mencoba mengubah variabel `const`.
2. **Fokus pada baris pertama yang menyebut kodemu sendiri**:
   Abaikan baris yang berasal dari library pihak ketiga jika ada; periksa nomor baris (`:12`) pada file atau editor kamu.
3. **Sisipkan `console.log()` satu baris sebelum error**:
   Cek isi variabel persis sebelum baris yang bermasalah untuk memastikan apakah nilainya benar-benar sesuai harapan atau ternyata `undefined`.

Dengan membiasakan diri membaca *stack trace* secara tenang, kamu akan menghemat banyak waktu saat membangun aplikasi web yang lebih besar!
