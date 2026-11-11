---
id: web-foundation-lanjutan-12
courseId: fe-web-foundation-00
order: 35
title: "Semantic HTML: Mengorganisasi Halaman Konten"
readingMinutes: 4
section: LANJUTAN
learningObjectives:
  - "Memahami konsep dan keuntungan semantic HTML bagi SEO dan pembaca layar (screen reader)"
  - "Membedakan elemen bermakna semantik dan elemen murni visual"
---
Ingat analogi dokumen koran? Sebuah halaman koran punya kepala berita, isi berita, kolom samping, dan catatan kaki. Halaman web punya kebutuhan yang sama, dan HTML punya elemen khusus untuk itu.

## Sebelum HTML5: segalanya jadi div

Sebelum elemen-elemen yang akan kamu pelajari di modul ini ada, developer mengelompokkan bagian halaman (header situs, navigasi, artikel) semuanya memakai `<div>`, generic element yang tidak punya makna apa pun. Untuk membedakannya, mereka mengandalkan `class` atau `id`, misalnya `<div class="header">`.

Masalahnya, browser, mesin pencari, dan pembaca layar tidak tahu bahwa `<div class="header">` itu sebenarnya sebuah header. Bagi mereka, itu cuma `<div>` biasa dengan label tempelan. Strukturnya jadi tidak bermakna, atau disebut *semantic meaningless*.

## HTML5: elemen yang punya nama sesuai perannya

HTML5 memperkenalkan elemen-elemen yang namanya sendiri sudah menjelaskan perannya: `<header>`, `<nav>`, `<main>`, `<article>`, `<aside>`, `<footer>`, dan `<section>`. Ini disebut **semantic HTML**.

Dengan elemen semantik, browser, mesin pencari, dan pembaca layar langsung tahu peran setiap bagian tanpa perlu menebak dari nama class. Ini juga memudahkan sesama developer membaca kode satu sama lain.

Modul-modul berikutnya membahas elemen-elemen ini satu per satu, lalu kamu akan menata ulang halaman profilmu memakainya.
                    
