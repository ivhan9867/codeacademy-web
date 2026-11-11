---
id: web-foundation-lanjutan-8
courseId: fe-web-foundation-00
order: 31
title: "Format Teks Blok: Kutipan Panjang, Preformatted, dan Figure"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menggunakan <blockquote> untuk kutipan blok panjang beserta atribusi"
  - "Menampilkan kode atau teks dengan spasi asli menggunakan elemen <pre>"
  - "Membungkus konten ilustratif dan keterangannya dengan <figure> dan <figcaption>"
codeSnippet: |
  <blockquote cite="https://id.wikipedia.org/wiki/HTML">
    <p>HTML adalah bahasa markup standar untuk membangun halaman web.</p>
  </blockquote>
  
  <figure>
    <img src="https://picsum.photos/seed/gitar/400/220" alt="Gitar akustik">
    <figcaption>Gitar pertamaku, dibelikan orang tua saat SMA.</figcaption>
  </figure>
  
  <pre>
  Kutipan favoritku:
    "Belajar sedikit tiap hari
     lebih baik daripada banyak sekali lalu berhenti."
  </pre>
---
Selain paragraf, heading, dan list, ada beberapa elemen khusus untuk format teks yang bersifat blok (mengambil satu baris penuh sendiri): `<blockquote>`, `<pre>`, dan `<figure>`.

## Kutipan panjang: blockquote

Untuk menampilkan kutipan panjang atau testimoni, gunakan `<blockquote>`. Isinya boleh berupa paragraf, heading, bahkan list.

```html
<blockquote cite="https://id.wikipedia.org/wiki/Situs_web">
  <p>
    Situs web adalah sekumpulan halaman web yang saling berhubungan,
    umumnya berada pada peladen yang sama.
  </p>
</blockquote>
```

Atribut `cite` opsional, diisi URL sumber kutipan. Atribut ini tidak mengubah tampilan sama sekali, ia hanya informasi tambahan untuk mesin atau alat bantu, bukan untuk pembaca manusia secara langsung.

## Teks preformatted: pre

Kamu sudah tahu browser mengabaikan spasi berlebih dan baris kosong di HTML. Tapi untuk konten seperti contoh kode atau puisi, spasi dan baris itu justru penting. Elemen `<pre>` membuat browser menampilkan isinya **persis seperti yang kamu tulis**, termasuk spasi dan barisnya.

```html
<pre>
  Baris pertama
      Baris kedua, menjorok ke dalam
  Baris ketiga
</pre>
```

Secara bawaan, isi `<pre>` juga ditampilkan dengan huruf monospace (lebar setiap huruf sama), gaya khas untuk kode.

## Figure dan figcaption

Elemen `<figure>` dipakai untuk konten yang berdiri sendiri: ilustrasi, diagram, foto, atau bahkan potongan kode. Konten di dalamnya bisa dipindah posisinya dari alur utama dokumen tanpa mengubah makna dokumen secara keseluruhan.

Di dalam `<figure>`, kamu bisa menambahkan `<figcaption>` sebagai judul atau keterangan untuk konten tersebut.

```html
<figure>
  <img src="https://picsum.photos/seed/gitar/400/220" alt="Gitar akustik">
  <figcaption>Gitar pertamaku, dibelikan orang tua saat SMA.</figcaption>
</figure>
```

`<figure>` tidak harus berisi gambar. Ia juga cocok untuk membungkus kutipan puisi atau blok kode yang ingin diberi judul.

## Kesalahan yang sering terjadi

- Memakai `<blockquote>` untuk kutipan pendek satu-dua kata di tengah kalimat (harusnya `<q>`, dibahas di modul berikutnya)
- Mengira `<pre>` mengubah gaya huruf saja, padahal fungsi utamanya mempertahankan spasi dan baris apa adanya

## Coba sendiri

Tambahkan satu `<figure>` berisi gambar dan `<figcaption>` di halaman profilmu, lalu satu `<blockquote>` berisi kutipan favoritmu.
                    
