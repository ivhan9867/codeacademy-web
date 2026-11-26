---
id: js-prep-glossary
courseId: fe-js-03
order: 4
title: "Glosarium Istilah JavaScript"
readingMinutes: 7
section: PERSIAPAN
learningObjectives:
  - Mengenal istilah teknis inti dalam pemrograman JavaScript secara ringkas dan akurat
  - Memahami perbedaan istilah yang sering tertukar seperti Parameter vs Argument dan Null vs Undefined
  - Menjadikan glosarium ini sebagai rujukan cepat saat membaca modul inti maupun lanjutan
---

# Glosarium Istilah JavaScript

Saat mempelajari JavaScript, kamu akan sering bertemu istilah berbahasa Inggris yang terdengar teknis. Tidak perlu menghafal semuanya sekaligus—gunakan halaman ini sebagai kamus saku kapan saja kamu butuh penyegaran cepat.

---

## Variabel, Tipe Data, & Nilai

- **Variable** — Wadah penyimpanan berlabel di dalam memori komputer untuk menyimpan data agar bisa dibaca atau diproses kembali.
- **const** — Kata kunci untuk mendeklarasikan variabel yang nilainya tetap dan tidak boleh ditetapkan ulang (*reassign*) setelah dibuat.
- **let** — Kata kunci modern untuk mendeklarasikan variabel dengan *block scope* yang nilainya boleh diubah atau diperbarui selama program berjalan.
- **var** — Kata kunci lama untuk mendeklarasikan variabel yang memiliki *function scope* dan perilaku *hoisting*; sebaiknya dihindari pada kode modern.
- **Primitive** — Tipe data dasar paling sederhana yang nilainya bersifat tunggal dan tidak dapat diubah langsung (*immutable*), seperti string, number, dan boolean.
- **String** — Tipe data untuk merepresentasikan teks, ditulis di dalam tanda kutip tunggal (`'...'`), kutip ganda (`"..."`), atau *backtick* (`` `...` ``).
- **Number** — Tipe data numerik di JavaScript yang mencakup bilangan bulat (*integer*) maupun bilangan desimal (*floating-point*).
- **Boolean** — Tipe data logika yang hanya memiliki dua kemungkinan nilai: `true` (benar) atau `false` (salah).
- **Null** — Nilai khusus yang secara sengaja diberikan untuk menyatakan bahwa suatu variabel kosong atau belum memiliki objek apa pun.
- **Undefined** — Nilai bawaan untuk variabel yang sudah dideklarasikan tetapi belum pernah diberi nilai, atau properti yang tidak ditemukan.
- **Template Literal** — Cara menulis string menggunakan tanda *backtick* (`` ` ``) yang mendukung penyisipan variabel dengan `${...}` serta teks multi-baris.

---

## Operator, Logika, & Kontrol Alur

- **Operator** — Simbol khusus seperti `+`, `-`, `===`, `&&`, atau `??` yang digunakan untuk melakukan operasi matematika, perbandingan, atau logika pada nilai.
- **Strict Equality (===)** — Operator perbandingan ketat yang memeriksa apakah dua nilai sama persis baik dari segi **nilai** maupun **tipe datanya** tanpa konversi otomatis.
- **Truthy** — Setiap nilai non-boolean yang dianggap bernilai `true` ketika dievaluasi di dalam konteks kondisi boolean seperti `if`.
- **Falsy** — Kumpulan nilai terbatas yang otomatis dianggap `false` dalam konteks boolean, yaitu `false`, `0`, `-0`, `0n`, `""`, `null`, `undefined`, dan `NaN`.
- **Nullish Coalescing (??)** — Operator modern yang mengembalikan nilai di sisi kanan hanya jika nilai di sisi kiri bernilai `null` atau `undefined`.
- **Conditional** — Struktur pengambilan keputusan dalam kode (seperti `if`, `else if`, `else`, atau `switch`) yang menjalankan blok berbeda sesuai kondisi tertentu.
- **Loop** — Mekanisme perulangan (seperti `for`, `while`, atau `for...of`) untuk mengeksekusi blok kode berulang kali selama kondisi terpenuhi.

---

## Struktur Data: Array & Object

- **Array** — Struktur data berbentuk daftar berurutan untuk menyimpan banyak nilai sekaligus dalam satu variabel menggunakan tanda kurung siku `[...]`.
- **Index** — Nomor posisi elemen di dalam sebuah array atau karakter pada string; di JavaScript, penghitungan selalu dimulai dari `0`.
- **Object** — Struktur data yang menyimpan kumpulan pasangan kunci dan nilai (*key-value pairs*) di dalam tanda kurung kurawal `{...}`.
- **Property** — Pasangan nama (*key*) dan nilai yang melekat pada sebuah objek untuk mendeskripsikan data atau karakteristik objek tersebut.
- **Method** — Fungsi yang disimpan sebagai properti di dalam sebuah objek atau tipe data (misalnya `array.push()` atau `text.trim()`).
- **Destructuring** — Sintaks ringkas ES6+ untuk mengekstrak nilai dari array atau properti dari objek langsung ke dalam variabel terpisah.
- **Spread Operator** — Operator tiga titik (`...`) untuk membongkar elemen array atau properti objek agar mudah disalin maupun digabungkan secara *immutable*.

---

## Fungsi & Ruang Lingkup (Scope)

- **Function** — Blok kode terbungkus yang dirancang untuk menjalankan tugas tertentu dan dapat dipanggil berulang kali di berbagai bagian program.
- **Parameter** — Nama variabel penampung yang ditulis di dalam tanda kurung saat mendefinisikan sebuah fungsi.
- **Argument** — Nilai nyata yang dikirimkan ke dalam fungsi ketika fungsi tersebut dipanggil atau dijalankan.
- **Return Value** — Nilai hasil akhir yang dikirim kembali oleh sebuah fungsi ke pemanggilnya menggunakan kata kunci `return`.
- **Arrow Function** — Cara penulisan fungsi yang lebih ringkas dengan sintaks panah `=>`, populer dipakai untuk *callback* dan fungsi satu baris.
- **Scope** — Area atau batas keterlihatan tempat sebuah variabel dan fungsi dapat diakses di dalam kode program.
- **Block Scope** — Ruang lingkup terbatas di dalam pasangan kurung kurawal `{...}` (seperti pada `if` atau `for`) yang berlaku untuk `let` dan `const`.
- **Global Scope** — Ruang lingkup terluar di mana variabel dideklarasikan di luar fungsi atau blok mana pun sehingga dapat diakses dari mana saja.
- **Closure** — Kemampuan sebuah fungsi untuk tetap mengingat dan mengakses variabel dari ruang lingkup luarnya meskipun fungsi luar tersebut sudah selesai berjalan.
- **Callback** — Fungsi yang dikirimkan sebagai argumen ke fungsi lain untuk dijalankan nanti setelah tugas tertentu selesai atau suatu kejadian terjadi.
- **Higher-Order Function** — Fungsi yang menerima fungsi lain sebagai argumen, mengembalikan fungsi baru sebagai hasil, atau keduanya (contoh: `map`, `filter`, `find`).

---

## Interaksi Web, DOM, & Penyimpanan

- **DOM (Document Object Model)** — Representasi terstruktur berbentuk pohon objek dari dokumen HTML yang memungkinkan JavaScript membaca dan mengubah isi halaman web.
- **querySelector** — Method pada objek `document` untuk memilih elemen HTML pertama yang cocok dengan pola selektor CSS tertentu.
- **Event** — Kejadian atau interaksi yang berlangsung pada halaman web, seperti klik tombol (`click`), ketikan pengguna (`input`), atau pengiriman form (`submit`).
- **Event Listener** — Penjaga yang dipasang lewat `addEventListener()` pada elemen HTML untuk menjalankan fungsi tertentu saat sebuah event terjadi.
- **preventDefault** — Method pada objek event untuk menghentikan perilaku bawaan browser, misalnya mencegah halaman *reload* otomatis saat form dikirim.
- **JSON** — Singkatan dari *JavaScript Object Notation*; format pertukaran dan penyimpanan data berbasis teks ringan yang mudah dibaca manusia maupun mesin.
- **LocalStorage** — Ruang penyimpanan bawaan browser berbentuk pasangan *key-value* bertipe string yang datanya tetap bertahan meski halaman ditutup atau dimuat ulang.

---

## Pemrograman Asinkron & Jaringan

- **Asynchronous** — Pola eksekusi tugas yang berjalan di latar belakang tanpa memblokir baris kode berikutnya atau membekukan antarmuka pengguna.
- **Promise** — Objek JavaScript yang merepresentasikan hasil dari proses asinkron di masa depan, dengan tiga status: `pending`, `fulfilled`, dan `rejected`.
- **async/await** — Pasangan kata kunci modern di atas Promise agar penulisan kode asinkron terlihat rapi, berurutan, dan mudah dibaca seperti kode sinkron.
- **Fetch API** — Antarmuka modern bawaan browser (`fetch()`) untuk mengirim permintaan HTTP ke server atau API secara asinkron dan menerima responsnya.

---

## Debugging & Penanganan Error

- **SyntaxError** — Kesalahan tata bahasa penulisan kode (seperti kurung lupa ditutup atau salah ketik kata kunci) yang membuat JavaScript gagal membaca skrip.
- **ReferenceError** — Kesalahan yang muncul ketika kode mencoba mengakses variabel yang belum pernah dideklarasikan atau berada di luar *scope*.
- **TypeError** — Kesalahan saat mencoba melakukan operasi pada tipe data yang tidak sesuai, misalnya memanggil fungsi dari nilai `undefined` atau mengubah nilai `const`.
- **Stack Trace** — Jejak riwayat urutan pemanggilan fungsi beserta nomor baris dan kolom yang ditampilkan Console saat terjadi error untuk membantu pelacakan bug.
