---
id: web-foundation-pcss-49
courseId: fe-web-foundation-00
order: 105
title: "Debugging dan Optimasi CSS"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Mengetahui cara memeriksa validitas CSS dengan tools online"
  - "Memahami manfaat minifikasi CSS untuk mempercepat waktu muat halaman"
---
Setelah menulis banyak CSS di sepanjang modul ini, ada baiknya kita mengenal cara memeriksa dan merapikan kode sebelum benar-benar dipakai di produksi.

## Memeriksa kesalahan CSS

Cara tercepat memastikan CSS yang kamu tulis valid adalah dengan memakai **tools validasi online** yang tersedia gratis. Kamu cukup menempelkan kode CSS, mengunggah berkasnya, atau memasukkan alamat situs yang ingin diperiksa. Beberapa contoh layanan yang bisa dipakai:

- CSS Validation Service dari W3C
- CSS Validator Online dari codebeautify.org
- CSS Lint

Tools seperti ini akan menunjukkan baris mana yang bermasalah, misalnya properti yang salah ketik, nilai yang tidak valid, atau sintaks yang keliru — jauh lebih cepat dibanding mencarinya manual satu per satu.

## Mengecilkan ukuran CSS (minifikasi)

Ukuran berkas CSS memengaruhi seberapa cepat halaman dimuat di browser. Semakin kecil ukurannya, semakin cepat halaman tampil, terutama di koneksi internet yang lambat.

Proses **minifikasi** menghapus bagian-bagian yang tidak memengaruhi fungsi CSS, tapi memakan ukuran berkas, seperti:

- baris baru (*new line characters*),
- spasi berlebih (*whitespace*),
- pembatas blok yang tidak perlu,
- komentar CSS.

Perubahan ini bisa dilakukan manual, tapi jauh lebih praktis memakai tools otomatis, misalnya:

- CSSNANO Playground
- CSS Minifier Tool and Compressor
- Minify (JavaScript and CSS minifier)

Kebiasaan memeriksa validitas dan meminifikasi CSS sebelum dipakai di website yang sesungguhnya adalah praktik yang baik, terutama untuk proyek yang akan diakses banyak orang.
