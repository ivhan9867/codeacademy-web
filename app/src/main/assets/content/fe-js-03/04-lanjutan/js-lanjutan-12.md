---
id: js-lanjutan-12
courseId: fe-js-03
order: 32
title: "Mengambil Data API dengan fetch() & Penanganan Error"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - Memahami alur permintaan HTTP (request) dan jawaban server (response) menggunakan Fetch API
  - Menggabungkan async/await dengan fetch() serta response.json() secara berurutan
  - Menangani kegagalan jaringan maupun status HTTP error (404/500) menggunakan pengecekan response.ok dan blok try/catch
codeLanguage: javascript
codeSnippet: |
  const ambilPengguna = async () => {
    try {
      const res = await fetch("https://jsonplaceholder.typicode.com/users/1");
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      console.log(data.name);
    } catch (err) {
      console.log("Terjadi kesalahan:", err.message);
    }
  };
starterJs: |
  const ambilDataPengguna = async (idPengguna) => {
    const url = `https://jsonplaceholder.typicode.com/users/${idPengguna}`;
    console.log("1. Mengirim request ke:", url);

    try {
      // Tahap 1: Tunggu jawaban HTTP dari server
      const response = await fetch(url);

      // Tahap 2: Pastikan status HTTP sukses (200-299)
      if (!response.ok) {
        throw new Error(`Server merespons dengan status ${response.status}`);
      }

      // Tahap 3: Ubah body respons menjadi objek JavaScript (juga asinkron!)
      const pengguna = await response.json();

      console.log("2. Data berhasil diterima!");
      console.log("Nama  :", pengguna.name);
      console.log("Email :", pengguna.email);
      console.log("Kota  :", pengguna.address.city);
    } catch (error) {
      // Menangkap error jaringan (offline) maupun Error dari throw di atas
      console.log("Gagal memuat data API:", error.message);
    }
  };

  ambilDataPengguna(1);
playgroundTask: |
  Tekan 'Jalankan' dan tunggu sejenak sambil membuka panel Console. Pastikan langkah pengambilan data berjalan rapi (atau pesan error tertangkap bersih oleh catch jika perangkat sedang offline):
  1. Mengirim request ke: https://jsonplaceholder.typicode.com/users/1
  2. Data berhasil diterima!
  Coba ganti argumen ambilDataPengguna(1) menjadi ambilDataPengguna(9999) untuk menguji penanganan status HTTP 404 oleh response.ok!
verificationRules:
  - "js:async"
  - "js:try"
---

# Mengambil Data API dengan fetch() & Penanganan Error

Website modern hampir selalu menampilkan data yang hidup dan dinamis—daftar produk terbaru, prakiraan cuaca hari ini, atau profil akun yang baru saja diperbarui. Data tersebut tidak ditulis manual di dalam file HTML, melainkan diambil dari server melalui **API (*Application Programming Interface*)** menggunakan fungsi bawaan browser: **`fetch()`**.

---

## Alur Kerja Request & Response `fetch()`

```text
  [ Kode JavaScript ]
           |
           |  1. await fetch(url)
           v
  +------------------------+
  |     Server / API       |
  +------------------------+
           |
           |  2. Mengembalikan Objek Response (header & status)
           v
     Cek: response.ok ?
       |            |
     false         true
       |            |
       v            v
   throw Error   3. await response.json()  <-- Baca isi data JSON
       |            |
       v            v
  [ Blok catch ]  [ Data JS Siap Ditampilkan ke DOM ]
```

Perhatikan bahwa ada **dua kali proses `await`** saat mengambil data JSON:
1. **`await fetch(url)`**: Menunggu server membalas koneksi (*status code* & *headers*).
2. **`await response.json()`**: Menunggu seluruh isi paket data (*body*) selesai diunduh dan diubah dari teks JSON menjadi objek JavaScript.

---

## Jebakan Klasik `fetch()`: Status 404 Tidak Otomatis Masuk ke `catch`!

Banyak pemula mengira jika alamat API salah (`404 Not Found`) atau server rusak (`500 Internal Server Error`), blok `catch` akan langsung berjalan. **Ternyata tidak!**

`fetch()` hanya masuk ke `catch` jika koneksi internet putus total atau domain tidak bisa dihubungi. Jika server masih menjawab—meskipun jawabannya `"404 Tidak Ditemukan"`—`fetch()` menganggap pengiriman surat berhasil.

Oleh karena itu, **selalu periksa properti `response.ok`** secara manual:

```javascript
const response = await fetch(url);

// response.ok bernilai true hanya jika status HTTP berada di rentang 200 - 299
if (!response.ok) {
  throw new Error(`Permintaan gagal dengan kode HTTP ${response.status}`);
}
```

Ketika `throw new Error(...)` dieksekusi, alur program langsung melompat ke blok `catch (error)` di bawahnya!

---

## Pola Lengkap `async/await` + `fetch()` + `try/catch`

Berikut pola standar industri yang bersih dan tahan banting:

```javascript
const muatPostingan = async () => {
  try {
    const response = await fetch("https://jsonplaceholder.typicode.com/posts/1");

    if (!response.ok) {
      throw new Error(`HTTP Error: ${response.status}`);
    }

    const postingan = await response.json();
    console.log("Judul:", postingan.title);
  } catch (error) {
    console.log("Tidak dapat memuat postingan:", error.message);
  }
};

muatPostingan();
```

### Mengapa Pola Ini Penting?
- **Tidak membekukan tampilan**: Berkat `async/await`, halaman web tetap responsif saat menunggu jawaban server.
- **Anti-crash**: Kegagalan jaringan maupun error 404/500 sama-sama ditangani di satu tempat (`catch`).
- **Mudah dipadukan dengan UI**: Di dalam `try`, kamu bisa menampilkan data ke DOM; di dalam `catch`, kamu bisa menampilkan pesan ramah seperti *"Koneksi bermasalah, silakan coba lagi"*.

Jalankan playground di bawah ini, amati output di Console, lalu uji sendiri bagaimana `response.ok` menangkap error saat ID pengguna diubah ke angka yang tidak tersedia!
