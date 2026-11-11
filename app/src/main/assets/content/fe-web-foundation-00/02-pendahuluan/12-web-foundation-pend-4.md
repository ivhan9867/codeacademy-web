---
id: web-foundation-pend-4
courseId: fe-web-foundation-00
order: 12
title: "Web Server"
readingMinutes: 4
section: PENDAHULUAN
learningObjectives:
  - "Memahami fungsi web server sebagai komputer penyimpan dan pelayan berkas website"
  - "Menjelaskan alur permintaan (request) dan tanggapan (response) antara browser dan server"
---
Saat kamu mencari sesuatu di mesin pencari, atau mengetik nama produk di kolom pencarian toko online, daftar hasil langsung muncul. Dari mana datanya? Siapa yang menyiapkannya? Jawabannya ada pada komputer yang bekerja di balik layar: server.

## Client, server, dan siklus request-response

Perangkat yang dipakai untuk mengakses website disebut **client**. Lebih tepatnya, browserlah yang bertindak sebagai client karena dialah yang meminta data. Komputer yang menyimpan dan menyajikan isi website disebut **server**.

Alurnya begini:

1. Browser mengirim **request** (permintaan) ke server.
2. Server memproses permintaan itu.
3. Server mengirim balik **response** (tanggapan) berisi data yang dibutuhkan, seperti berkas HTML, CSS, JavaScript, gambar, dan aset lain.

Rangkaian ini disebut *request-response cycle*.

Bayangkan sebuah restoran. Kamu adalah pengguna yang lapar akan informasi. Pelayan adalah browser yang mencatat dan mengantar pesananmu. Dapur adalah server yang memasak pesanan itu. Setelah matang, hidangan (halaman web) diantar pelayan ke mejamu. Kamu harus menunggu sebentar, tapi biasanya tidak lama.

## Apa itu web server?

Banyak orang membayangkan server sebagai komputer raksasa yang besar dan sangat kencang. Sering benar, tapi kurang lengkap. Istilah **web server** bisa merujuk pada dua sisi yang bekerja sama:

- **Sisi hardware**: komputer yang spesifikasinya disesuaikan dengan kebutuhan. Website yang menyimpan banyak foto butuh penyimpanan besar, sedangkan website yang melakukan hitungan rumit butuh prosesor yang kuat.
- **Sisi software**: program yang berjalan di komputer itu agar bisa menerima permintaan dan mengirim data memakai **HTTP** (HyperText Transfer Protocol), yaitu aturan standar pertukaran data antara browser dan server. Contohnya NGINX dan Apache. Kamu juga bisa membuatnya sendiri dengan bahasa pemrograman sisi server, misalnya lewat Node.js.

Versi HTTP yang datanya dienkripsi disebut **HTTPS**. Itulah alasan muncul ikon gembok di address bar banyak website.

## DNS: buku telepon internet

Setiap perangkat yang terhubung ke internet punya **alamat IP**, yaitu deretan angka yang berfungsi seperti alamat rumah, termasuk server tempat website disimpan. Manusia sulit mengingat deretan angka, apalagi kalau harus menghafal puluhan, dan alamat IP sebuah server juga bisa berubah sewaktu-waktu. Karena itu kita memakai **nama domain** seperti `contoh.com`.

Nama domain tidak menggantikan IP. Komputer tetap memakai IP di balik layar. Yang menjembatani keduanya adalah **DNS (Domain Name System)**. Cara kerjanya:

1. Kamu membuka `contoh.com`. Browser lebih dulu memeriksa **DNS cache**, yaitu catatan alamat IP dari domain yang pernah dikunjungi.
2. Kalau alamat IP-nya ada di catatan itu, browser langsung memakainya.
3. Kalau tidak ada, komputer bertanya ke **DNS server**, yang bertugas memberi tahu alamat IP untuk domain yang terdaftar.
4. Setelah mendapat alamat IP, browser mengirim request ke web server di alamat tersebut.

Analogi mudahnya seperti kontak di HP: kamu mengingat nama temanmu, sedangkan HP-lah yang mengingat nomornya.

Untuk bacaan tambahan, cari artikel "What is a web server?" di MDN Web Docs dan penjelasan visual tentang cara kerja DNS.
                    
