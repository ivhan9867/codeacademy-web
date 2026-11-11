---
id: node-pend-3
courseId: be-node-01
order: 6
title: "Mode Interaktif REPL dan Menjalankan File Skrip"
readingMinutes: 6
section: PENDAHULUAN
learningObjectives:
  - "Memahami konsep dan fungsi REPL (Read-Eval-Print Loop) di terminal Node.js"
  - "Memahami alur eksekusi file skrip JavaScript dari baris pertama hingga selesai"
  - "Mengetahui cara menghentikan proses program secara terencana menggunakan process.exit()"
codeLanguage: javascript
codeSnippet: |
  // Contoh pengendalian siklus hidup proses
  console.log("Memulai inisialisasi skrip...");

  const statusServer = "ERROR_DATABASE";

  if (statusServer === "ERROR_DATABASE") {
    console.log("[FATAL] Database tidak merespons. Menghentikan program.");
    process.exit(1); // Keluar dengan kode kegagalan
  }

  console.log("Baris ini tidak akan pernah dieksekusi.");
---

# Mode Interaktif REPL dan Menjalankan File Skrip

Saat bekerja dengan Node.js di komputer, ada dua cara utama untuk mengeksekusi instruksi JavaScript:
1. Menggunakan **REPL (*Read-Eval-Print Loop*)** untuk eksperimen kilat.
2. Menyimpan instruksi ke dalam **File Skrip (`.js`)** untuk aplikasi nyata.

![Alur Kerja REPL dan Eksekusi Skrip](asset:images/be-node-01/repl-dan-skrip.png)

---

## 1. Apa itu REPL?

REPL adalah singkatan dari:
- **Read**: Node membaca ekspresi JavaScript yang kamu ketik di terminal.
- **Eval**: Mesin V8 mengevaluasi (menghitung atau menjalankan) kode tersebut.
- **Print**: Hasil evaluasi langsung dicetak ke layar seketika.
- **Loop**: Sistem kembali menunggu ketikanmu berikutnya.

Jika kamu membuka terminal di komputermu dan hanya mengetikkan `node` lalu menekan Enter, kamu akan disambut tanda prompt `>`:

```text
$ node
Welcome to Node.js v20.12.0.
Type ".help" for more information.
> 10 + 25
35
> const nama = "Budi";
undefined
> `Halo, ${nama}!`
'Halo, Budi!'
> .exit
```

REPL sangat berguna seperti kalkulator canggih untuk menguji sebuah method string, mengecek logika regular expression, atau memeriksa nilai matematika tanpa perlu membuat file baru.

Untuk keluar dari REPL, kamu cukup mengetik `.exit` atau menekan `Ctrl + C` dua kali.

---

## 2. Menjalankan File Skrip (`.js`)

Untuk proyek aplikasi server, seluruh kode tentu disimpan di dalam file teks berekstensi `.js`. Ketika perintah `node app.js` dijalankan:
1. Node membaca seluruh isi file dari baris pertama hingga akhir.
2. V8 mengompilasi dan mengeksekusinya secara berurutan (*top-to-bottom*).
3. Jika tidak ada tugas asinkron atau listener server yang aktif mendengarkan koneksi, program akan otomatis **berhenti dan kembali ke prompt terminal**.

Perhatikan contoh eksekusi skrip berikut:

```javascript
console.log("1. Program dimulai");

const daftarLayanan = ["Auth", "Payment", "Notification"];

for (let i = 0; i < daftarLayanan.length; i++) {
  console.log(`- Mengaktifkan modul: ${daftarLayanan[i]}`);
}

console.log("2. Semua modul berhasil diaktifkan");
console.log("3. Program selesai secara normal.");
```

Output:
```
1. Program dimulai
- Mengaktifkan modul: Auth
- Mengaktifkan modul: Payment
- Mengaktifkan modul: Notification
2. Semua modul berhasil diaktifkan
3. Program selesai secara normal.
```

---

## 3. Mengendalikan Siklus Hidup Proses dengan `process.exit()`

Dalam pengembangan back-end, ada kalanya program harus dihentikan secara paksa dan segera—misalnya ketika file konfigurasi kunci API tidak ditemukan atau koneksi ke database terputus.

Node.js menyediakan fungsi khusus: `process.exit(code)`.

- `process.exit(0)`: Keluar secara normal dan sukses (*Exit Code 0*).
- `process.exit(1)` (atau angka selain 0): Keluar dengan status terjadi kesalahan (*Exit Code Error*).

Perhatikan kode simulasi berikut:

```javascript
console.log("=== SISTEM PEMERIKSAAN KELAYAKAN SERVER ===");

const kuotaTersisaMB = 50;
const batasMinimalMB = 100;

console.log(`Memeriksa memori: ${kuotaTersisaMB} MB tersedia...`);

if (kuotaTersisaMB < batasMinimalMB) {
  console.log("[CRITICAL] Memori tidak mencukupi batas aman (100 MB).");
  console.log("Menghentikan sistem sebelum terjadi crash fatal...");
  process.exit(1);
}

// Baris-baris di bawah ini TIDAK AKAN PERNAH berjalan
console.log("Server berhasil lolos uji coba!");
console.log("Membuka gerbang lalu lintas data...");
```

Output:
```
=== SISTEM PEMERIKSAAN KELAYAKAN SERVER ===
Memeriksa memori: 50 MB tersedia...
[CRITICAL] Memori tidak mencukupi batas aman (100 MB).
Menghentikan sistem sebelum terjadi crash fatal...
```

Begitu fungsi `process.exit(1)` terpanggil, Node.js langsung mematikan proses saat itu juga. Baris apapun setelah pemanggilan fungsi tersebut tidak akan pernah disentuh oleh CPU.

Di modul berikutnya, kita akan menjelajahi lebih dalam mengenai objek sakti ini: **Objek Global dan Objek `process`!**
