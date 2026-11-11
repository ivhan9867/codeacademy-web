---
id: web-foundation-inti-6
courseId: fe-web-foundation-00
order: 21
title: "Atribut pada Elemen HTML"
readingMinutes: 5
section: INTI
codeLanguage: html
learningObjectives:
  - "Memahami fungsi atribut sebagai pemberi informasi tambahan pada tag pembuka"
  - "Menulis format atribut yang benar dengan pasangan nama dan nilai (name="value")"
  - "Mengenal atribut global yang dapat dipasang pada hampir semua elemen HTML"
codeSnippet: |
  <h1 lang="id" title="Halaman profil">Profil Rani</h1>
  
  <p class="catatan" id="salam">Halo! Selamat datang di halamanku.</p>
  
  <p hidden>Paragraf ini disembunyikan oleh atribut hidden.</p>
  
  <p contenteditable="true">Klik teks ini lalu ubah isinya.</p>
---
Elemen menentukan **apa** sebuah bagian konten. Kadang kita perlu memberi keterangan tambahan tentang elemen itu: gambar mana yang ditampilkan, bahasa apa yang dipakai, atau bagaimana ia dikenali oleh kode lain. Untuk itulah ada **atribut**.

## Apa itu atribut?

**Atribut** adalah informasi tambahan untuk sebuah elemen. Ia ditulis di dalam tag pembuka, dan umumnya tidak tampil sebagai isi halaman, melainkan memengaruhi cara elemen itu bekerja atau dikenali.

Contoh paling terkenal adalah elemen gambar:

```html
<img src="kucing.jpg" alt="Kucing sedang tidur">
```

Atribut `src` memberi tahu browser di mana letak berkas gambarnya, dan `alt` berisi teks pengganti bila gambar gagal dimuat atau dibacakan oleh pembaca layar. Tanpa `src`, elemen `img` tidak tahu harus menampilkan apa.

## Anatomi atribut

```text
<p class="catatan">Teks</p>
    |     |
  nama   nilai
```

Aturan penulisannya:

- Atribut ditulis di **tag pembuka saja**, setelah nama elemen, dan dipisah spasi
- Bentuknya `nama="nilai"`, dengan nilai diapit tanda kutip ganda
- Satu elemen boleh memiliki banyak atribut, dipisah spasi

Contoh atribut `class`: ia memberi nama pengenal pada elemen supaya nanti CSS bisa menargetkannya, misalnya "beri warna pada semua elemen yang class-nya catatan". Satu nama class boleh dipakai banyak elemen sekaligus.

Kembarannya adalah `id`, nama pengenal yang harus **unik**: satu `id` hanya boleh dipakai satu elemen dalam satu halaman.

## Atribut yang umum dipakai

Beberapa atribut bisa dipakai di hampir semua elemen. Ini disebut atribut global. Yang lain hanya cocok untuk elemen tertentu, seperti `src` pada `img`. Berikut atribut global yang perlu kamu kenal, dikelompokkan supaya mudah diingat.

Identitas:

- **id**: pengenal unik elemen
- **class**: nama kelompok, boleh sama di banyak elemen
- **data-nama**: menyimpan data khusus milikmu pada elemen. Awalannya `data-`, lalu nama bebas, misalnya `data-level="1"`

Bahasa dan teks:

- **lang**: bahasa isi elemen, misalnya `lang="id"` untuk bahasa Indonesia
- **dir**: arah teks, kiri ke kanan (`ltr`) atau kanan ke kiri (`rtl`)
- **translate**: menentukan apakah isi boleh diterjemahkan otomatis (`yes` atau `no`)
- **spellcheck**: menentukan apakah ejaan dan tata bahasa diperiksa

Tampilan dan perilaku:

- **style**: menulis gaya CSS langsung pada satu elemen
- **hidden**: menyembunyikan elemen dari halaman
- **contenteditable**: membuat isi elemen bisa diedit langsung oleh pengunjung
- **draggable**: membuat elemen bisa diseret

Bantuan dan navigasi:

- **title**: keterangan tambahan yang muncul saat kursor diarahkan ke elemen
- **tabindex**: urutan fokus saat menekan tombol Tab
- **accesskey**: tombol pintas untuk memfokuskan elemen

Kamu tidak perlu menghafal semuanya. Cukup tahu bahwa atribut-atribut ini ada, lalu cari daftar lengkapnya dengan kata kunci "HTML global attributes" di MDN Web Docs saat membutuhkannya.

## Kesalahan yang sering terjadi

- Menulis atribut di tag penutup. Tag penutup tidak pernah membawa atribut
- Lupa tanda kutip pada nilai atribut
- Memakai `id` yang sama di beberapa elemen

## Coba sendiri

Buka contoh di bawah. Perhatikan paragraf yang bisa kamu klik lalu ubah isinya (`contenteditable`) dan paragraf yang tidak tampil (`hidden`). Lalu hapus kata `hidden` dan jalankan lagi untuk melihat paragraf itu muncul.
                    
