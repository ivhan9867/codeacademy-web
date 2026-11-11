---
id: web-foundation-inti-2
courseId: fe-web-foundation-00
order: 17
title: "Latihan: Halaman Web Pertamamu"
readingMinutes: 7
section: INTI
codeLanguage: html
learningObjectives:
  - "Menjalankan dokumen web pertama langsung di Playground"
  - "Menjelaskan mengapa whitespace dan enter diabaikan oleh browser pada teks polos"
codeSnippet: |
  Profil Rani
  
  Pelajar dan calon web developer dari Semarang.
  
  Tentang Saya
  
  Aku suka menggambar dan bermain gitar. Sejak kecil aku penasaran bagaimana sebuah aplikasi dibuat.
  
  Hobi
  
  Menggambar
  
  Aku biasa menggambar karakter kartun di buku catatan.
  
  Bermain Gitar
  
  Lagu pertama yang kupelajari memakai empat kunci sederhana.
---
Saatnya membuat halaman web pertamamu. Tujuan latihan ini sederhana: kamu mengetik sendiri sebuah halaman, melihat bagaimana browser memperlakukannya, lalu menemukan sesuatu yang mengejutkan.

## Alur latihan

1. Buka Playground lewat tombol **Coba di Playground** di bagian *Contoh Kode Praktik* di bawah materi ini
2. Perhatikan teks profil yang sudah tersedia di editor HTML
3. Jalankan kodenya dan lihat hasilnya
4. Ganti isinya dengan profilmu sendiri, lalu jalankan lagi

Teks di contoh terdiri dari sebuah nama, satu kalimat pembuka, dan dua bagian cerita singkat tentang hobi. Semuanya ditulis rapi, dengan baris kosong di antara bagian-bagiannya, seperti tulisan biasa.

## Apa yang kamu lihat?

Coba perhatikan hasilnya. Semua teks **menyatu** menjadi satu blok panjang. Judul, kalimat pembuka, dan cerita hobi bergabung tanpa jarak. Padahal di editor kamu sudah menekan Enter berkali-kali.

Kenapa begitu? Karena browser membaca HTML dengan aturan sendiri: spasi berlebih dan baris kosong **diabaikan**, lalu teksnya digabung menjadi satu aliran. Browser juga tidak tahu mana yang judul, mana subjudul, dan mana paragraf, karena belum ada yang memberitahunya. Yang ia terima hanyalah teks polos (*plain text*).

> Konten tanpa penanda hanyalah teks. Struktur halaman harus dinyatakan secara eksplisit, dan alat untuk itu bernama HTML.

Inilah alasan utama HTML ada. Manusia melihat judul dari ukuran huruf dan jarak, sedangkan browser hanya bisa memahami apa yang **ditandai**.

## Kesalahan yang sering terjadi

- Mengira menekan Enter di editor sama dengan pindah baris di browser
- Mengubah kode tetapi lupa menjalankan ulang, lalu bingung kenapa hasilnya sama
- Mengira ada yang rusak, padahal browser memang bekerja seperti itu

## Coba sendiri

Tambahkan satu kalimat tentang cita-citamu di akhir teks, lalu jalankan. Kalimat itu akan muncul menempel di ujung paragraf yang sama, bukan di baris baru. Simpan pengamatan ini. Di modul-modul berikutnya kamu akan memperbaikinya dengan cara yang benar.

Tapi sebelum itu, kita bedah dulu apa yang sebenarnya terjadi di balik layar: apa itu folder proyek dan kenapa berkas halaman web biasanya bernama index.html.
                    
