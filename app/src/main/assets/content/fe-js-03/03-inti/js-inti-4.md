---
id: js-inti-4
courseId: fe-js-03
order: 14
title: "Merangkai Teks Bersih dengan Template Literals"
readingMinutes: 5
section: INTI
learningObjectives:
  - Memahami kelemahan penggabungan teks tradisional dengan operator plus (+)
  - Menguasai penulisan Template Literals menggunakan tanda backtick (`)
  - Menyisipkan variabel dan ekspresi ke dalam string dengan sintaks interpolasi ${...}
codeLanguage: javascript
codeSnippet: |
  const nama = "Rina";
  const usia = 22;
  console.log(`Halo, nama saya ${nama} dan usia saya ${usia} tahun.`);
starterJs: |
  const namaPengguna = "Budi";
  const skorKuis = 90;
  const totalSoal = 100;

  // Merangkai kalimat dinamis dengan template literal
  const laporan = `Siswa ${namaPengguna} meraih nilai ${skorKuis} dari total ${totalSoal}.`;
  console.log(laporan);
playgroundTask: |
  Jalankan kode bawaan. Pastikan Console mencetak satu baris:
  Siswa Budi meraih nilai 90 dari total 100.
  Coba ubah namaPengguna atau skorKuis lalu jalankan kembali untuk melihat teks menyesuaikan secara otomatis.
---

# Merangkai Teks Bersih dengan Template Literals

Dalam aplikasi web, kamu akan sangat sering merangkai kalimat dinamis: menampilkan nama pengguna di ucapan selamat datang, menyusun pesan konfirmasi pesanan, atau menyajikan ringkasan nilai.

Sebelum ES6 (2015), merangkai teks di JavaScript bisa menjadi mimpi buruk. Mari kita lihat alasannya!

---

## Cara Lama: Konkatenasi dengan Tanda Plus (+)

Dahulu, satu-satunya cara menggabungkan variabel ke dalam string adalah menggunakan operator `+`:

```javascript
const nama = "Vhan";
const kota = "Bandung";

// Rentan salah spasi dan sulit dibaca
const kalimat = "Halo, nama saya " + nama + " dan saya tinggal di " + kota + ".";
console.log(kalimat);
```

Perhatikan betapa mudahnya kita lupa memberi spasi setelah kata `"Halo, nama saya "` atau sebelum `" dan saya"`. Jika ada 5 variabel dalam satu kalimat, baris kode akan dipenuhi oleh tanda kutip yang membingungkan.

---

## Cara Modern: Template Literals

ES6 memperkenalkan solusi elegan bernama **Template Literals**. Ciri khasnya:
1. Tidak menggunakan tanda kutip biasa (`"` atau `'`), melainkan menggunakan karakter **backtick (`` ` ``)** (biasanya terletak di bawah tombol Esc pada keyboard).
2. Memasukkan variabel langsung di dalam teks menggunakan tanda dolar dan kurung kurawal: **`${namaVariabel}`**. Proses ini disebut **Interpolasi String**.

Bandingkan kode di atas jika ditulis dengan Template Literals:

```javascript
const nama = "Vhan";
const kota = "Bandung";

const kalimat = `Halo, nama saya ${nama} dan saya tinggal di ${kota}.`;
console.log(kalimat);
```

Output:
```text
Halo, nama saya Vhan dan saya tinggal di Bandung.
```

Jauh lebih bersih, rapi, dan mudah dibaca!

---

## Menyisipkan Operasi Hitung di Dalam `${...}`

Di dalam tanda `${...}`, kamu tidak hanya bisa memasukkan nama variabel, tetapi juga ekspresi matematika:

```javascript
const hargaBarang = 50000;
const jumlahBeli = 3;

console.log(`Total belanja Anda: Rp${hargaBarang * jumlahBeli}`);
```

Output:
```text
Total belanja Anda: Rp150000
```

Kamu juga bisa membuat teks melompat ke baris baru cukup dengan menekan tombol **Enter** di dalam backtick tanpa perlu mengetikkan `\n`.

Kini setelah kita menguasai data dan string, saatnya mengajari program kita cara mengambil keputusan dengan `if`/`else` di modul berikutnya!
