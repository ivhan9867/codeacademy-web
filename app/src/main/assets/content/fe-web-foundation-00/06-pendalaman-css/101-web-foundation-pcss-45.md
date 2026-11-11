---
id: web-foundation-pcss-45
courseId: fe-web-foundation-00
order: 101
title: "Meta Tag Viewport untuk Responsivitas Layout"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Memahami apa itu viewport dan mengapa ukurannya berbeda di tiap perangkat"
  - "Menambahkan meta tag viewport agar halaman tidak otomatis mengecil di perangkat mobile"
  - "Mengetahui fitur device simulation di DevTools browser untuk menguji tampilan mobile"
---
Layout dengan `float` yang baru saja kita buat memang sudah rapi — tapi itu baru teruji di layar laptop atau desktop. Bagaimana tampilannya di ponsel?

## Menguji tampilan mobile dengan DevTools

Browser modern seperti Google Chrome dan Mozilla Firefox punya fitur **inspector** yang bisa mensimulasikan tampilan di berbagai ukuran layar, termasuk perangkat mobile. Untuk membukanya, tekan `Ctrl + Shift + I` (atau klik kanan lalu pilih "Inspect"), lalu aktifkan mode simulasi perangkat (biasanya ikon ponsel/tablet kecil di pojok inspector). Dari sana kamu bisa memilih perangkat seperti "iPhone 12 Pro" untuk melihat tampilan website seolah dibuka dari ponsel tersebut.

Kalau kamu coba sekarang, hasilnya mungkin mengecewakan: seluruh halaman tampil **sangat kecil**, seolah di-zoom out jauh.

## Apa itu viewport?

**Viewport** adalah area yang bisa dilihat pengguna pada layar perangkatnya. Ukuran viewport jauh lebih kecil di ponsel dibanding desktop. Dulu, sebelum ponsel dan tablet umum dipakai untuk browsing, kebanyakan website didesain dengan ukuran statis yang hanya cocok untuk layar desktop. Browser di ponsel kemudian mengatasi ini dengan cara "mengecilkan" seluruh halaman agar muat di layar kecil — hasilnya, teks jadi terlalu kecil untuk dibaca.

## Mengatur viewport dengan meta tag

Solusinya, kita perlu memberi tahu browser bagaimana seharusnya viewport diatur, lewat tag `<meta>` di dalam `<head>`:

```html
<meta name="viewport" content="width=device-width, initial-scale=1" />
```

Tag ini memberi tahu browser:

- `width=device-width`: lebar viewport mengikuti lebar layar perangkat sebenarnya, bukan lebar default yang biasanya jauh lebih besar.
- `initial-scale=1`: tingkat zoom awal diatur ke 1 (tidak di-zoom sama sekali).

Setelah tag ini ditambahkan, tampilan halaman akan terlihat dalam ukuran yang sesuai dengan layar perangkat — tidak lagi mengecil otomatis seperti sebelumnya.

Tapi, masalah baru muncul: sekarang `<article>` terasa terlalu sempit, dan `<aside>` tidak tertata dengan baik di layar kecil. Ini wajar, karena layout dua-kolom yang kita buat memang dirancang untuk layar lebar. Untuk mengatasi ini, kita butuh teknik **responsivitas** lebih lanjut, yang akan kita bahas di modul berikutnya: CSS Media Query.
