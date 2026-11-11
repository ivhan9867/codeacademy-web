---
id: web-foundation-inti-5
courseId: fe-web-foundation-00
order: 20
title: "Anatomi Elemen HTML"
readingMinutes: 5
section: INTI
codeLanguage: html
learningObjectives:
  - "Membedakan tag pembuka (opening tag), isi konten, dan tag penutup (closing tag)"
  - "Memahami konsep elemen bersarang (nested elements) dan aturan penutupannya"
  - "Mengenal void element / self-closing tag yang tidak memiliki tag penutup"
codeSnippet: |
  <p>Satu paragraf.</p>
  <p>Paragraf kedua, terpisah jarak.</p>
  
  <p>Baris pertama <br> baris kedua, tanpa jarak paragraf.</p>
  
  <p>Aku sedang belajar <strong>HTML</strong> hari ini.</p>
---
Kamu sudah memakai `<h1>` dan `<p>`. Sekarang saatnya membedah elemen itu sendiri: dari apa ia dibuat, dan kenapa penulisannya harus persis begitu.

## Apa itu elemen HTML?

**Elemen** adalah unit pembangun HTML: sepotong kode yang menandai satu bagian konten beserta perannya. Misalnya, elemen paragraf dan elemen judul:

```html
<p>Ini sebuah paragraf.</p>
<h1>Ini sebuah judul</h1>
```

## Kenapa elemen penting?

Bandingkan dua hal ini. Teks tanpa elemen tampil sebagai gumpalan teks polos. Teks yang dibungkus `<p>` dikenali browser sebagai paragraf, sehingga otomatis diberi jarak atas dan bawah dan dipisahkan dari bagian lain. `<h1>` otomatis tampil besar dan tebal. Tampilan bawaan itu dinamakan *default style* browser.

Yang lebih penting dari tampilan: elemen adalah **makna**. Mesin pencari memakainya untuk memahami isi halamanmu, dan pembaca layar (*screen reader*, alat bantu bagi penyandang tunanetra) memakainya untuk membacakan halaman dengan urutan yang masuk akal. Tanpa elemen, semua itu hilang.

## Tiga bagian penyusun elemen

```text
  <p>   Halo semuanya!   </p>
   |          |            |
tag pembuka  konten   tag penutup
```

- **Tag pembuka**: nama elemen yang dibungkus tanda `<` dan `>`. Untuk paragraf, nama elemennya `p`, jadi tag pembukanya `<p>`
- **Konten**: isi elemen, misalnya teks
- **Tag penutup**: sama seperti tag pembuka, tetapi ada garis miring `/` sebelum nama elemen. Ia menandai di mana elemen berakhir

Kesalahan klasik pemula adalah **lupa menulis tag penutup**. Browser akan mencoba menebak sendiri di mana elemen berakhir, dan tebakannya sering tidak sesuai harapan: sisa halaman bisa ikut terbungkus di dalam elemen yang salah.

## Elemen di dalam elemen

Sebuah elemen boleh memuat elemen lain di dalamnya. Elemen yang berada di dalam disebut **child-element** (elemen anak), dan pembungkusnya disebut **parent** (elemen induk).

```html
<p>Aku sedang belajar <strong>HTML</strong> hari ini.</p>
```

Di sini `<p>` adalah induk dan `<strong>` adalah anaknya. Elemen `<strong>` menandai teks yang penting, dan biasanya tampil tebal. Aturan pentingnya: elemen yang dibuka **belakangan** harus ditutup **lebih dulu**.

```html
<!-- Salah: urutan penutupnya tertukar -->
<p><strong>Penting</p></strong>

<!-- Benar -->
<p><strong>Penting</strong></p>
```

## Elemen tanpa isi

Sekilas saja: ada beberapa elemen yang tidak punya konten sehingga tidak memerlukan tag penutup. Contohnya `<br>` untuk pindah baris dan `<img>` untuk menampilkan gambar. Keduanya akan dibahas di bagian Pendalaman HTML pada kelas ini.

## Kesalahan yang sering terjadi

- Lupa tag penutup
- Menukar urutan penutup pada elemen bersarang
- Salah ketik nama elemen, misalnya `<pp>`, sehingga browser tidak mengenalinya

## Coba sendiri

Buka Playground dengan contoh di bawah. Bandingkan bagian yang memakai dua `<p>` dengan bagian yang memakai `<br>`. Amati jarak antarbaris, lalu tambahkan `<strong>` pada satu kata di paragraf mana pun.
                    
