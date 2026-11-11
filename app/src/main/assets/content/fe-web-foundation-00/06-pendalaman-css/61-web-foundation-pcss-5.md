---
id: web-foundation-pcss-5
courseId: fe-web-foundation-00
order: 61
title: "Combinators: Child Selector dan Descendant Selector"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  div > p {
    background-color: yellow;
  }
codeLanguage: css
starterHtml: |
  <div>
    <p>Paragraf ini anak langsung dari div, jadi kuning.</p>
    <section>
      <p>Paragraf ini ada di dalam section, bukan anak langsung div, jadi tidak kuning.</p>
    </section>
  </div>
learningObjectives:
  - "Membedakan child selector (>) dan descendant selector (spasi)"
  - "Menentukan kapan memakai child selector agar gaya tidak meluas ke elemen yang terlalu dalam"
---
Dua combinator terakhir berkaitan dengan **hubungan induk dan anak**. Keduanya tampak mirip, tetapi cakupannya berbeda, dan perbedaan itu sering menjadi sumber bug yang sulit dicari.

## Child selector (>)

Child selector hanya menargetkan elemen yang **anak langsung** dari induknya.

```css
div > p {
  background-color: yellow;
}
```

```html
<div>
  <p>Paragraf ini anak langsung dari div, jadi kuning.</p>
  <section>
    <p>Paragraf ini ada di dalam section, bukan anak langsung div, jadi tidak kuning.</p>
  </section>
</div>
```

Di contoh di atas, paragraf kedua tidak terkena gaya karena di antaranya ada `<section>`. Child selector memastikan gaya hanya berlaku sampai satu tingkat.

## Descendant selector (spasi)

Descendant selector memakai **spasi** di antara dua selector. Aturannya berlaku untuk *semua* turunan, baik langsung maupun tidak langsung, pada kedalaman berapa pun.

```css
div p {
  background-color: yellow;
}
```

Dengan aturan ini, kedua paragraf pada contoh sebelumnya akan kuning, karena keduanya adalah turunan dari `<div>`.

## Kapan memakai yang mana?

Descendant selector praktis dan ringkas, tetapi bisa meluas lebih jauh dari yang kamu niatkan. Child selector lebih spesifik dan lebih aman ketika struktur HTML-nya sering berubah. Sebagai pedoman sederhana:

- Pakai **descendant** kalau memang ingin gaya berlaku untuk semua isi suatu wadah.
- Pakai **child** kalau kamu hanya ingin menyasar elemen pada tingkat tertentu.

> **Kebiasaan yang baik:** Hindari selector yang terlalu panjang seperti `body div section article p span`. Selector seperti itu rapuh, karena mengubah sedikit struktur HTML bisa mematahkan seluruh gaya. Cukup pakai class yang jelas, misalnya `.isi-artikel p`.

## Latihan singkat

1. Buat `<div class="kotak">` yang berisi dua `<p>` langsung dan satu `<section>` yang berisi `<p>`.
2. Pertama, gunakan `.kotak > p` untuk memberi warna latar kuning. Lihat paragraf mana saja yang berubah.
3. Ganti dengan `.kotak p` dan bandingkan hasilnya.
