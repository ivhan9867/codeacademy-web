---
id: web-foundation-lanjutan-2
courseId: fe-web-foundation-00
order: 25
title: "Paragraf"
readingMinutes: 4
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menggunakan elemen <p> untuk menyusun blok teks bacaan yang rapi"
  - "Memahami perilaku whitespace collapsing dan margin default pada paragraf"
codeSnippet: |
  <p>Aku sedang belajar HTML lebih dalam sekarang.</p>
  <p>Rasanya menyenangkan bisa mengerti struktur di balik sebuah halaman web.</p>
---
Elemen `<p>` sudah kamu pakai sejak modul pertama. Sekarang saatnya memahami elemen ini lebih dalam.

## Paragraf adalah elemen paling mendasar

Hampir semua teks panjang di sebuah halaman web dibungkus elemen `<p>`. Saat kamu memakainya, browser otomatis menampilkannya di baris baru dengan sedikit jarak di bawahnya. Jarak ini disebut **margin**, sebuah konsep CSS yang akan kamu atur sendiri nanti di kelas styling.

```html
<p>Ini paragraf pertama.</p>
<p>Ini paragraf kedua, otomatis berjarak dari paragraf pertama.</p>
```

## Paragraf boleh berisi elemen lain

Isi sebuah `<p>` tidak harus teks polos. Kamu boleh menaruh elemen lain di dalamnya, asal elemen itu bersifat *inline* (elemen yang mengalir mengikuti teks, bukan mengambil satu baris penuh sendiri). Contohnya elemen penekanan kata yang akan kamu pelajari di modul mendatang:

```html
<p>Aku sedang belajar <strong>HTML</strong> hari ini.</p>
```

## Kapan tidak memakai paragraf

Gunakan `<p>` khusus untuk konten yang memang berupa paragraf. Untuk judul, jangan pakai `<p>` meski secara teknis tidak akan error, karena ada elemen heading yang jauh lebih tepat (dibahas di modul berikutnya).

> Semua teks yang tampil di halaman harus dibungkus elemen yang sesuai. Teks yang dibiarkan mengambang tanpa elemen pembungkus disebut *anonymous text*, dan sebaiknya dihindari.

## Kesalahan yang sering terjadi

- Memakai `<p>` untuk judul, padahal ada elemen heading yang lebih pas
- Menumpuk banyak `<br>` untuk memisahkan "paragraf", padahal seharusnya memakai elemen `<p>` yang terpisah

## Coba sendiri

Tambahkan satu paragraf baru di halaman profilmu yang menceritakan pengalaman belajarmu sejauh ini.
                    
