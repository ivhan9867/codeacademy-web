---
id: web-foundation-pend-6
courseId: fe-web-foundation-00
order: 14
title: "Anatomi Website"
readingMinutes: 2
section: PENDAHULUAN
learningObjectives:
  - "Membedakan peran tritunggal web: HTML (struktur), CSS (tampilan), dan JavaScript (perilaku)"
  - "Memahami analogi kerangka, kulit, dan gerakan dalam membangun halaman web interaktif"
---
Sampai di sini kamu sudah tahu bagaimana data berpindah dari server ke layarmu. Sekarang kita bongkar isinya: halaman web itu sebenarnya tersusun dari apa?

## Tiga fondasi website

Hampir semua website dibangun dari tiga teknologi utama:

- **HTML**: bahasa *markup* yang menentukan struktur atau kerangka halaman, seperti judul, paragraf, gambar, dan tombol.
- **CSS**: bahasa penataan gaya (*style*) yang mengatur tampilan, seperti warna, ukuran huruf, jarak, dan tata letak.
- **JavaScript**: bahasa pemrograman utama yang dijalankan langsung oleh browser untuk membuat halaman interaktif.

Secara teknis, halaman web bisa berdiri hanya dengan HTML. Namun hasilnya tampak polos dan kaku. Hari ini nyaris tidak ada website yang tidak memakai ketiganya sekaligus, dan kalau salah satunya hilang, hasilnya terasa tidak lengkap.

## Analogi: membangun rumah

Bayangkan sebuah rumah:

- **HTML** adalah kerangka dan dinding: menentukan ada berapa ruangan, serta di mana letak pintu dan jendela.
- **CSS** adalah cat, lantai, dan penataan perabot: membuat rumah tampak nyaman dan enak dipandang.
- **JavaScript** adalah kelistrikan: saklar yang menyalakan lampu, pintu otomatis, dan segala hal yang bereaksi saat kamu berinteraksi.

Rumah tanpa cat tetap bisa ditinggali, tetapi kurang menarik. Rumah tanpa listrik tampak bagus, tetapi tidak terasa "hidup".

## Sekadar mengintip

Belum perlu paham detailnya, karena kamu akan mempelajarinya satu per satu di kelas berikutnya. Lihat saja bagaimana ketiganya berbagi tugas untuk satu judul.

HTML (struktur):

```html
<h1 id="judul">Halo, Web!</h1>
```

CSS (tampilan):

```css
h1 {
  color: teal;
}
```

JavaScript (interaksi):

```js
document.getElementById("judul").textContent = "Halo, Dunia!";
```

HTML membuat judulnya, CSS mewarnainya, dan JavaScript mengubah teksnya. Di modul-modul HTML dan CSS pada kelas ini, kamu akan mempraktikkannya sendiri di Playground.
