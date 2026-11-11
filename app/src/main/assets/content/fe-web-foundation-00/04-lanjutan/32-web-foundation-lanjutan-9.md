---
id: web-foundation-lanjutan-9
courseId: fe-web-foundation-00
order: 32
title: "Format Teks Inline: Tautan, Penekanan, dan Kutipan Pendek"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Membuat tautan internal dan eksternal menggunakan elemen <a> dengan atribut href"
  - "Memberikan penekanan makna pada teks menggunakan <em>, <strong>, dan kutipan inline <q>"
codeSnippet: |
  <p>
    Aku <em>sungguh-sungguh</em> menikmati proses belajar ini.
    <strong>Catatan:</strong> semua latihan aku kerjakan langsung di Playground.
  </p>
  
  <p>
    Kalau penasaran, kamu bisa baca lebih lanjut di
    <a href="https://developer.mozilla.org" target="_blank" rel="noopener">MDN Web Docs</a>.
  </p>
  
  <p>
    Seperti kata MDN, <q cite="https://developer.mozilla.org">HTML adalah fondasi
    dari setiap halaman web</q>.
  </p>
---
Sejauh ini kamu memakai elemen yang bersifat **block**: selalu membuat baris baru sendiri, seperti `<p>`, `<h1>`, dan `<ul>`. Sekarang, kenalan dengan elemen **inline**: elemen yang menyatu mengikuti alur teks, tanpa membuat baris baru.

## Anchor: membuat tautan

Elemen `<a>` (anchor) dipakai untuk membuat hyperlink ke halaman lain, situs lain, berkas, alamat email, atau bagian lain di halaman yang sama. Atribut wajibnya adalah `href`, yang menentukan tujuan tautan.

```html
<a href="https://developer.mozilla.org">Kunjungi MDN Web Docs</a>
```

Beberapa atribut khusus lain yang bisa dipakai pada `<a>`:

- **target**: ke mana tautan dibuka. `_blank` membuka di tab baru, `_self` (bawaan) membuka di tab yang sama
- **download**: menyuruh browser mengunduh berkas yang dituju, alih-alih membukanya
- **rel**: menyatakan hubungan antara halaman ini dan halaman tujuan, misalnya `nofollow` (memberi tahu mesin pencari untuk tidak "mengikuti" reputasi tautan ini) atau `noopener` (alasan keamanan saat memakai `target="_blank"`)
- **hreflang**: bahasa dari dokumen tujuan
- **ping**: daftar URL yang diberi tahu secara diam-diam saat tautan diklik, biasa dipakai untuk pelacakan

```html
<a href="https://contoh.com/berkas.pdf" download>Unduh Berkas</a>
<a href="https://contoh.com" target="_blank" rel="noopener">Buka di tab baru</a>
```

## Penekanan kata: em dan strong

- **`<em>`** (*emphasis*) menandai kata yang perlu penekanan nada bicara. Bawaannya ditampilkan miring
- **`<strong>`** menandai kata yang sangat penting atau mendesak. Bawaannya ditampilkan tebal, dan pembaca layar akan mengubah nada suaranya untuk menekankan pentingnya

```html
<p>Aku <em>sendiri</em> yang menulis kode ini.</p>
<p><strong>Penting:</strong> simpan pekerjaanmu sebelum keluar dari Playground.</p>
```

Bedanya dengan sekadar bikin teks miring atau tebal lewat CSS: `<em>` dan `<strong>` membawa **makna**, bukan cuma tampilan. Ini penting untuk pembaca layar dan mesin pencari.

## Kutipan pendek: q

Kalau `<blockquote>` untuk kutipan panjang yang berdiri sendiri sebagai blok, `<q>` untuk kutipan pendek yang menyatu di dalam kalimat (inline). Browser otomatis menambahkan tanda kutip di sekelilingnya.

```html
<p>
  Menurut MDN, <q cite="https://developer.mozilla.org">HTML adalah tulang
  punggung setiap halaman web</q>, dan itu bukan berlebihan.
</p>
```

Sama seperti `blockquote`, atribut `cite` pada `<q>` tidak mengubah tampilan, hanya informasi tambahan.

## Kesalahan yang sering terjadi

- Lupa mengisi `href` pada `<a>`, sehingga tautan tidak mengarah ke mana pun
- Memakai `<em>` atau `<strong>` hanya demi gaya visual (miring/tebal), padahal maksudnya cuma estetika. Untuk itu ada cara lain di CSS nanti
- Memakai `<q>` untuk kutipan panjang yang seharusnya jadi blok tersendiri

## Coba sendiri

Tambahkan satu kalimat di halaman profilmu yang memakai `<em>` untuk menekankan satu kata, dan satu `<a>` yang menautkan ke situs favoritmu.
                    
