---
id: web-foundation-lanjutan-24
courseId: fe-web-foundation-00
order: 47
title: "Mengirim Data Formulir"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Menentukan tujuan pengiriman data dengan atribut action dan metode pengiriman dengan atribut method (GET vs POST)"
  - "Memahami perbedaan transmisi data URL parameter (GET) dan request body (POST)"
codeSnippet: |
  <form action="https://contoh.com/kontak" method="POST">
    <div>
      <label for="nama">Nama</label>
      <br>
      <input type="text" id="nama" name="nama" required>
    </div>
  
    <div>
      <label for="email">Email</label>
      <br>
      <input type="email" id="email" name="email" required>
    </div>
  
    <div>
      <label for="pesan">Pesan</label>
      <br>
      <textarea id="pesan" name="pesan" rows="4" cols="30"></textarea>
    </div>
  
    <button type="submit">Kirim</button>
  </form>
---
Formulir sudah bisa diisi, tapi datanya baru berguna kalau bisa dikirim ke tujuan yang tepat. Modul ini membahas caranya, sekaligus menyambungkannya kembali ke materi web server yang sudah kamu pelajari.

## Elemen form

Semua kolom input yang berkaitan sebaiknya dibungkus satu elemen `<form>`, yang bertugas sebagai pembungkus sekaligus penentu ke mana dan bagaimana datanya dikirim.

```html
<form>
  <div>
    <label for="email">Email</label>
    <br>
    <input type="email" id="email" name="email">
  </div>

  <div>
    <label for="password">Kata Sandi</label>
    <br>
    <input type="password" id="password" name="password">
  </div>

  <button type="submit">Kirim</button>
</form>
```

## Kenapa atribut name wajib di setiap input

Saat formulir dikirim, setiap data dikirim sebagai pasangan **key** dan **value**: key adalah nilai atribut `name`, dan value adalah isian penggunanya. Tanpa `name`, server tidak tahu data itu harus dianggap sebagai apa, jadi data itu diabaikan.

## Atribut action dan method

Dua atribut ini pada `<form>` menentukan ke mana dan bagaimana data dikirim:

- **action**: alamat tujuan pengiriman data, bisa berupa absolute URL (`https://contoh.com/kirim`) atau relative URL. Kalau tidak diisi, data dikirim ke alamat halaman yang sedang dibuka saat ini
- **method**: cara pengiriman datanya, ada dua pilihan:
  - **GET**: data ditempel di URL sebagai parameter, dipisah tanda `&` untuk tiap pasangan data, contohnya `https://contoh.com/?email=budi@mail.com&password=123`. Cocok untuk permintaan yang sekadar mengambil data
  - **POST**: data dikirim di dalam body request, tidak terlihat di URL. Cocok untuk data yang lebih sensitif atau lebih besar, seperti pendaftaran akun

```html
<form action="https://contoh.com/kirim" method="POST">
  <!-- kolom input -->
</form>
```

## Catatan untuk latihan di Playground

Karena latihanmu di Playground belum terhubung ke server sungguhan, menekan tombol submit tidak akan benar-benar mengirim data ke mana pun. Yang penting kamu pahami dulu: struktur formulirnya benar, setiap input punya `name`, dan kamu tahu peran `action` serta `method`. Bagian mengirim data ke server sungguhan akan kamu praktikkan nanti saat belajar backend.

## Kesalahan yang sering terjadi

- Lupa membungkus kolom input dengan `<form>`
- Memakai `method="GET"` untuk data sensitif seperti kata sandi, padahal datanya akan terlihat jelas di URL

## Coba sendiri

Bungkus input "Nama", "Email", dan "Pesan" yang sudah kamu buat dengan satu `<form>`, tambahkan tombol `<button type="submit">Kirim</button>` di akhir.
                    
