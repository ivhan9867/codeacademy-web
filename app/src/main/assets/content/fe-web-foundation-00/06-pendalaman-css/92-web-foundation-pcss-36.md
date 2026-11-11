---
id: web-foundation-pcss-36
courseId: fe-web-foundation-00
order: 92
title: "Positioning: Normal Flow"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Memahami normal flow sebagai perilaku bawaan setiap elemen block"
  - "Mengetahui bahwa normal flow setara dengan position: static"
---
**Normal flow** adalah cara bawaan browser menyusun elemen: setiap elemen block ditempatkan tepat di bawah elemen sebelumnya, dari atas ke bawah, sesuai urutan di HTML. Karena ini adalah perilaku default, kita sebenarnya tidak perlu menuliskan apa pun untuk mendapatkannya. Secara teknis, perilaku ini setara dengan menuliskan `position: static` — nilai bawaan dari properti `position`.

```css
h1 {
  /* tidak diatur width, otomatis memenuhi lebar penuh */
}

p {
  width: 450px;
}
```

Perhatikan dua hal pada contoh di atas:

- Elemen `<h1>` yang tidak diberi `width` akan otomatis memenuhi **seluruh lebar** ruang yang tersedia di jendela browser.
- Elemen `<p>` yang diberi `width: 450px` akan membatasi lebarnya menjadi 450px saja. Tapi meskipun masih ada ruang kosong di sampingnya, elemen `<p>` berikutnya **tetap akan muncul di bawahnya**, bukan di sampingnya.

Itulah inti dari normal flow: elemen block selalu tersusun vertikal, satu per satu, tidak peduli seberapa banyak ruang kosong yang tersisa di sampingnya. Untuk membuat elemen tersusun berdampingan secara horizontal, kita perlu teknik lain — seperti `float`, yang akan kita bahas setelah mengenal seluruh jenis positioning.
