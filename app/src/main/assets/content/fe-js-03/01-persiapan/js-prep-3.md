---
id: js-prep-3
courseId: fe-js-03
order: 3
title: Mengatur Playground & Menangkap console.log di Console
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - Memahami anatomi playground interaktif: editor kode, live preview, dan panel console
  - Mengetahui cara mengaktifkan toggle panel Console untuk melihat output teks
  - Menjalankan kode JavaScript pertama dan memverifikasi hasilnya di Console
codeLanguage: javascript
codeSnippet: |
  console.log("Halo, JavaScript!");
  console.log("Console siap digunakan.");
starterJs: |
  // Coba ubah pesan di bawah ini sesukamu
  console.log("Halo dari CodeAcademy!");
  console.log("Saya siap belajar JavaScript.");
playgroundTask: |
  Buka toggle panel Console di bagian bawah jendela pratinjau, lalu tekan tombol 'Jalankan'. Pastikan Console menampilkan:
  Halo dari CodeAcademy!
  Saya siap belajar JavaScript.
---

# Mengatur Playground & Menangkap console.log di Console

Di kelas HTML dan CSS sebelumnya, setiap perubahan kode langsung tampak secara visual di layar pratinjau (*live preview*). Namun, logika JavaScript sering kali bekerja di balik layar tanpa langsung mengubah tampilan warna atau tombol.

Lalu, bagaimana cara kita melihat apa yang sedang diproses oleh program? Jawabannya adalah melalui **Console**.

---

## Anatomi Playground CodeAcademy

Playground di aplikasi ini dirancang khusus untuk mempermudah eksperimen kode kamu:

1. **Area Editor**: Tempat kamu membaca dan mengetik kode JavaScript (`starterJs`).
2. **Jendela Pratinjau (*Live Preview*)**: Menampilkan hasil visual saat kita menghubungkan JavaScript dengan elemen HTML/CSS.
3. **Panel Console (Toggle di Bawah)**: Panel khusus yang menangkap pesan teks dari perintah `console.log()`. Jika belum terlihat, cukup ketuk tombol **Console** di bagian bawah area pratinjau. Panel ini dapat menampung hingga 100 baris riwayat pesan.
4. **Tombol Jalankan & Reset**:
   - **Jalankan**: Mengeksekusi ulang seluruh kode yang ada di editor.
   - **Reset**: Mengembalikan kode latihan ke kondisi semula jika terjadi kesalahan yang sulit diperbaiki.

---

## Perintah Pertama: console.log()

`console.log()` adalah perintah paling populer di dunia JavaScript. Anggap fungsi ini seperti pengeras suara mini yang mengumumkan nilai variabel atau pesan apa pun ke panel Console.

Perhatikan contoh berikut:

```javascript
console.log("Belajar logika web");
console.log(2026);
```

Output di panel Console:
```text
Belajar logika web
2026
```

Setiap kali kamu memanggil `console.log()`, komputer akan mencetak pesan yang ada di dalam tanda kurung, lalu otomatis berpindah ke baris baru.

---

## Saatnya Praktik!

Di bawah materi ini, ada editor kode latihan:
1. Periksa kode bawaan di `starterJs`.
2. Pastikan panel **Console** di bawah jendela pratinjau sudah terbuka.
3. Tekan tombol **Jalankan**.
4. Amati dua baris teks yang muncul di Console.

Jika kedua baris teks sudah muncul dengan tepat, selamat! Kamu sudah siap melangkah ke Bagian 2 untuk memahami dasar-dasar sintaks JavaScript secara mendalam.
