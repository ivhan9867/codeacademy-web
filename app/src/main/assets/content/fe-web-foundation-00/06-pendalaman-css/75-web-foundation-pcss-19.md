---
id: web-foundation-pcss-19
courseId: fe-web-foundation-00
order: 75
title: "Box Model"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Mengenal konsep box model sebagai pondasi tata letak elemen di CSS"
  - "Memahami analogi kotak kado untuk empat lapisan box model: content, padding, border, dan margin"
---
## Pengantar Box Model

Di mata browser, setiap elemen HTML—mulai dari judul `<h1>`, paragraf `<p>`, tautan `<a>`, gambar `<img>`, hingga kontainer `<div>`—diperlakukan sebagai sebuah **kotak persegi empat** (*box*). Cara browser mengatur ukuran, dekorasi, dan posisi kotak-kotak ini dikenal dengan istilah **Box Model**.

Agar lebih mudah membayangkannya dalam kehidupan sehari-hari, kamu bisa menggunakan **analogi kotak kado**:

```text
+-------------------------------------------------+
|  MARGIN (jarak renggang antar-kado di rak)      |
|  +-------------------------------------------+  |
|  |  BORDER (dinding kardus kado)             |  |
|  |  +-------------------------------------+  |  |
|  |  |  PADDING (busa / bubble wrap kado)  |  |  |
|  |  |  +-------------------------------+  |  |  |
|  |  |  |  CONTENT (barang kado utama)  |  |  |  |
|  |  |  +-------------------------------+  |  |  |
|  |  +-------------------------------------+  |  |
|  +-------------------------------------------+  |
+-------------------------------------------------+
```

Kotak ini memiliki empat lapisan yang tersusun dari bagian terdalam menuju terluar:

### 1. Content (Barang Kado)
Ini adalah inti dari elemen, yaitu tempat teks, gambar, atau media diletakkan. Lebar dan tinggi area ini biasanya ditentukan oleh teks di dalamnya atau lewat properti `width` dan `height`.

### 2. Padding (Bantalan / Bubble Wrap)
Bayangkan kamu membungkus barang kado yang mudah pecah dengan plastik gelembung (*bubble wrap*). Di CSS, **padding** adalah ruang kosong transparan di sekeliling konten agar teks tidak langsung menempel pada dinding kotak. Jika kamu memberi warna latar (`background-color`), warnanya akan merembes memenuhi area padding ini.

### 3. Border (Dinding Kardus Kado)
Ini adalah kardus pembungkus fisik yang mengelilingi barang dan bantalannya. Di CSS, **border** adalah garis pembatas di tepi luar padding. Kamu bisa mengatur ketebalan garis, gaya garis (seperti solid, putus-putus, atau bertitik), serta warnanya.

### 4. Margin (Jarak Antar-Kado)
Ketika kamu meletakkan beberapa kotak kado berdampingan di atas rak hadiah, kamu tentu memberi jarak agar kado-kado tersebut tidak saling berhimpitan. Di CSS, **margin** adalah ruang kosong di luar border yang memisahkan elemen tersebut dari elemen-elemen tetangga di sekitarnya. Margin selalu transparan.

## Hubungan dengan Modul Selanjutnya

Semua pengaturan tata letak di CSS bermuara pada pemahaman keempat lapisan box model ini. Pada modul berikutnya, kita akan melihat bagaimana browser menggambar kotak elemen ini secara nyata dan cara melihat batas kotak terluarnya menggunakan trik selektor sederhana.
