---
id: web-foundation-inti-3
courseId: fe-web-foundation-00
order: 18
title: "Bedah Kode: Folder Proyek dan index.html"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Memahami struktur folder proyek web dan file root"
  - "Mengetahui fungsi file index.html sebagai berkas gerbang utama website"
  - "Membiasakan penamaan berkas web dengan standar huruf kecil dan tanpa spasi"
---
Di Playground kamu tidak perlu menyiapkan apa-apa. Tapi begitu nanti kamu bekerja di laptop, ada beberapa konsep dasar yang wajib diketahui. Modul ini membahasnya, sekaligus menyambungkan apa yang sudah kamu pelajari tentang web server dan browser.

## Folder proyek (root project directory)

Sebuah website pada dasarnya adalah **kumpulan berkas**: berkas HTML, gambar, CSS, JavaScript, dan lainnya. Semua berkas itu disimpan bersama dalam satu folder biasa yang disebut **folder proyek**, atau dalam istilah teknisnya *root project directory*.

Contoh isinya:

```text
profil-rani/
  index.html
  gambar/
    foto.jpg
```

Folder `profil-rani` adalah akar (*root*) proyek. Semua berkas lain dialamatkan dari titik ini. Di Playground, folder semacam ini sudah diurus otomatis oleh aplikasi.

## Nama berkas: nama dan format

Perhatikan nama `index.html`. Ia terdiri dari dua bagian yang dipisah titik:

- **`index`** adalah nama berkasnya
- **`.html`** adalah formatnya, sering disebut *ekstensi*

Ekstensi memberi tahu komputer dan browser jenis isi berkas tersebut: `.html` untuk dokumen HTML, `.css` untuk berkas gaya, `.js` untuk JavaScript, `.jpg` untuk gambar. Tanpa `.html`, editor dan sistem operasi cenderung memperlakukan berkasmu sebagai teks biasa, dan browser tidak akan menampilkannya sebagai halaman web.

## Kenapa namanya index?

Ingat modul Web Server: saat kamu mengetik alamat seperti `contoh.com` tanpa menyebut nama berkas, server harus memutuskan sendiri berkas mana yang dikirim. Konvensinya, server mencari berkas bernama **index** (umumnya `index.html`) di folder yang diminta, lalu mengirim berkas itu.

- `contoh.com` membuat server mengirim `index.html` secara otomatis
- `contoh.com/tentang.html` membuat server mengirim `tentang.html`, karena nama berkasnya disebut

Itulah kenapa hampir semua website punya `index.html` di folder utamanya, dan kenapa kamu jarang melihat nama itu muncul di address bar.

## Membuka dari perangkat vs lewat domain

Kalau kamu membuka berkas HTML langsung dari penyimpanan laptop, address bar akan menampilkan awalan `file://` diikuti lokasi berkas di komputermu. Tidak ada server yang terlibat. Browser cuma membaca berkas dari penyimpanan lokal.

Bandingkan dengan `https://contoh.com`, di mana browser meminta berkas ke server lewat internet. Untuk belajar, keduanya sama-sama berfungsi. Tapi hanya halaman yang ada di server yang bisa dibuka orang lain.

Untuk bacaan tambahan, cari artikel "URI schemes" di MDN Web Docs.

## Coba pikirkan

Sebuah folder proyek berisi tiga berkas: `index.html`, `tentang.html`, dan `kontak.html`. Kalau pengunjung membuka `contoh.com/` tanpa menyebut nama berkas, berkas mana yang dikirim server? Jawabannya `index.html`. Kalau ia membuka `contoh.com/kontak.html`, yang dikirim adalah `kontak.html`.

Sekarang kita kembali ke kode. Saatnya memberi struktur pada halaman profil tadi.
                    
