---
id: web-foundation-pcss-39
courseId: fe-web-foundation-00
order: 95
title: "Positioning: Fixed Positioning"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Menggunakan position: fixed untuk membuat elemen tetap terlihat saat halaman di-scroll"
  - "Memahami bahwa fixed selalu relatif terhadap jendela browser, bukan elemen induk"
---
`position: fixed` sebenarnya adalah bentuk khusus dari absolute positioning. Perilakunya sama — elemen dikeluarkan dari normal flow — tapi dengan satu perbedaan penting: posisinya **selalu relatif terhadap jendela browser**, apa pun elemen induknya, dan **tetap di tempat** meski pengguna men-scroll halaman.

```css
header {
  position: fixed;
  top: 0;
  left: 0;
}
```

Dengan kode ini, `<header>` akan menempel di sudut kiri atas jendela browser. Ketika pengguna men-scroll halaman ke bawah, header ini akan **tetap terlihat** di posisi yang sama, sementara konten lain (seperti paragraf) bergerak di baliknya.

`position: fixed` sering dipakai untuk elemen yang memang harus selalu terlihat, seperti navigasi bar yang menempel di atas, tombol "kembali ke atas", atau widget chat yang muncul di pojok layar.

> **Perbandingan singkat:** `relative` menggeser elemen dari posisi aslinya tanpa menghilangkan ruang kosongnya. `absolute` menghilangkan elemen dari flow sepenuhnya, relatif terhadap induk ber-posisi non-static terdekat (atau jendela browser). `fixed` seperti `absolute`, tapi selalu relatif terhadap jendela browser dan tidak ikut bergerak saat di-scroll.
