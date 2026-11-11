---
id: web-foundation-pend-5
courseId: fe-web-foundation-00
order: 13
title: "Web Browser"
readingMinutes: 3
section: PENDAHULUAN
learningObjectives:
  - "Memahami fungsi utama web browser dalam mengunduh, menerjemahkan, dan menampilkan kode web"
  - "Mengenal peran rendering engine dalam mengubah HTML menjadi tampilan visual"
---
Bayangkan kamu ingin bepergian ke sebuah pulau. Kamu butuh kendaraan yang tepat dan alamat tujuan yang jelas. Di dunia web, kendaraannya adalah **browser**, dan alamat tujuannya adalah **URL**.

## Apa itu browser?

**Web browser** (disingkat browser) adalah program yang mengambil dan menampilkan halaman web. Bukan hanya satu website: semua website yang aktif di dunia bisa dijelajahi lewat browser, termasuk halaman-halaman yang saling tertaut dalam satu domain.

Ada banyak pilihan browser, antara lain:

- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Safari
- Opera

Browser apa pun yang kamu pakai, cara kerjanya sama: mengikuti aturan yang sama untuk meminta dan menampilkan halaman web.

## Mengenal URL

Kotak di bagian atas browser tempat kamu mengetik alamat disebut **address bar**. Isinya adalah **URL** (*Uniform Resource Locator*). Sebuah URL punya tiga bagian utama. Contohnya `https://contoh.com/belajar/html`:

- **Protocol**: aturan komunikasi yang dipakai, yaitu `https`
- **Domain name**: nama server tujuan, yaitu `contoh.com`
- **Path**: letak halaman atau berkas yang diminta di server, yaitu `/belajar/html`

## Cara browser bekerja

Saat kamu menekan enter di address bar, yang terjadi kira-kira begini:

1. Browser mencari alamat IP dari domain di URL (lewat DNS).
2. Browser mengirim **HTTP request** ke web server di alamat IP itu.
3. Web server membalas dengan **HTTP response** berisi HTML, CSS, JavaScript, gambar, dan berkas lain yang dibutuhkan.
4. Browser merakit semua berkas itu menjadi halaman yang bisa kamu lihat dan gunakan.

Proses ini berulang setiap kali kamu membuka halaman baru.

## Kode status: tanda dari server

Setiap response membawa **kode status** yang memberi tahu hasil permintaanmu. Tiga yang paling sering kamu temui:

- `200 OK`: permintaan berhasil, halaman dikirim
- `404 Not Found`: halaman atau sumber daya yang diminta tidak ditemukan
- `500 Internal Server Error`: server sendiri mengalami masalah saat memproses permintaan

## Coba sendiri

Kalau kamu membuka materi ini lewat laptop atau PC, buka website apa saja, klik kanan, lalu pilih **Inspect** dan buka tab **Network**. Muat ulang halaman, lalu perhatikan daftar berkas yang diminta browser beserta kode statusnya. Itulah request-response cycle yang baru saja kamu pelajari, berjalan langsung di depan matamu.
                    
