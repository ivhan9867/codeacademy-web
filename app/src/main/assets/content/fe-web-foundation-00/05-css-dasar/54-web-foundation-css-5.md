---
id: web-foundation-css-5
courseId: fe-web-foundation-00
order: 54
title: "Tiga Konsep Penting: Inheritance, Group Selector, dan Rule Order"
readingMinutes: 5
section: CSS_DASAR
learningObjectives:
  - "Memahami konsep inheritance (pewarisan style) pada CSS"
  - "Memahami urutan pembacaan rule CSS dan kapan konflik styling terjadi"
---
Sebelum masuk lebih jauh, ada tiga konsep dasar yang bakal sering kamu temui.

**Inheritance (pewarisan).** Beberapa properti CSS otomatis "diturunkan" dari elemen induk ke elemen di dalamnya. Contoh: kalau kamu set `color: white` di `<footer>`, semua elemen di dalam footer (teks, link, dll) ikut jadi putih, walau kamu gak nulis rule khusus buat masing-masing. Ini kenapa struktur HTML yang rapi itu penting — struktur yang jelas bikin pewarisan style jadi gampang diprediksi.

**Group Selector.** Udah kamu praktikkan di modul sebelumnya — kalau beberapa selector butuh declaration yang sama, gabung aja pakai koma: `h2, h3 { color: #00a2c6; }` daripada nulis dua rule terpisah.

**Rule Order (urutan pembacaan).** *Cascading* di "Cascading Style Sheet" artinya "mengalir" — CSS dibaca dari atas ke bawah. Kalau ada dua rule yang saling tabrakan buat elemen yang sama, rule yang ditulis **paling akhir** yang menang. Ini kenapa urutan penulisan CSS itu penting, terutama kalau kamu pakai lebih dari satu sumber styling.

Ada satu cara buat "memaksa" satu declaration menang terlepas dari urutan, yaitu nambah `!important` di akhir nilainya:

```css
p {
  color: red !important;
}
```

Tapi hati-hati, pakai `!important` cuma kalau benar-benar perlu. Kalau kebiasaan dipakai sembarangan, kamu bakal kesulitan nebak kenapa suatu styling gak berubah pas di-edit — mending pahami urutan rule-nya dengan baik dulu.
