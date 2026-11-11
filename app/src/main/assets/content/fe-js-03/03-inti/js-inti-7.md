---
id: js-inti-7
courseId: fe-js-03
order: 15
title: "Mengulang Tugas Otomatis: Perulangan for dan while"
readingMinutes: 6
section: INTI
learningObjectives:
  - Memahami manfaat perulangan (loop) untuk mengotomatiskan tugas berulang
  - Menguasai tiga komponen dalam perulangan for: inisialisasi, kondisi henti, dan pembaruan langkah
  - Memahami cara kerja perulangan while dan cara menghindari perulangan tanpa henti (infinite loop)
codeLanguage: javascript
codeSnippet: |
  for (let i = 1; i <= 3; i++) {
    console.log("Putaran ke-" + i);
  }
starterJs: |
  // Menghitung langkah menggunakan perulangan for
  console.log("Mulai hitungan:");

  for (let i = 1; i <= 3; i++) {
    console.log(`Langkah ke-${i}`);
  }

  console.log("Selesai!");
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Pastikan output yang tercetak adalah:
  Mulai hitungan:
  Langkah ke-1
  Langkah ke-2
  Langkah ke-3
  Selesai!
---

# Mengulang Tugas Otomatis: Perulangan for dan while

Bayangkan kamu diminta mencetak angka 1 sampai 1.000 ke layar. Mengetik `console.log(1)`, `console.log(2)`, sampai seribu baris tentu membuang waktu dan sangat membosankan.

Komputer diciptakan untuk menyelesaikan tugas repetitif seperti ini dalam hitungan milidetik. Kemampuan ini disebut **Perulangan (*Looping*)**.

---

## 1. Perulangan for (Paling Populer)

Gunakan perulangan `for` ketika kamu **sudah tahu persis berapa kali** tugas tersebut harus diulang.

Sintaks `for` memiliki tiga bagian di dalam tanda kurungnya:

```javascript
for (let i = 1; i <= 3; i++) {
  console.log(`Langkah ke-${i}`);
}
```

Output:
```text
Langkah ke-1
Langkah ke-2
Langkah ke-3
```

### Membedah Tiga Bagian `for`:
1. **Inisialisasi (`let i = 1;`)**: Menyiapkan variabel penghitung (*counter*). Bagian ini hanya dijalankan **satu kali** saat perulangan pertama kali dimulai.
2. **Kondisi Uji (`i <= 3;`)**: Diperiksa sebelum setiap putaran dimulai. Jika bernilai `true`, kode di dalam `{}` dijalankan. Jika `false`, perulangan berhenti seketika.
3. **Pembaruan Langkah (`i++`)**: Dijalankan setiap kali satu putaran selesai. `i++` adalah singkatan dari `i = i + 1`, bertugas menaikkan nilai hitungan.

---

## 2. Perulangan while

Gunakan `while` ketika perulangan harus terus berjalan **selama suatu syarat masih bernilai benar**, dan kamu mungkin belum tahu pasti di putaran ke berapa syarat itu akan berhenti:

```javascript
let energi = 3;

while (energi > 0) {
  console.log(`Baterai tersisa: ${energi}`);
  energi--; // Mengurangi energi sebesar 1
}

console.log("Baterai habis!");
```

Output:
```text
Baterai tersisa: 3
Baterai tersisa: 2
Baterai tersisa: 1
Baterai habis!
```

---

## Bahaya "Infinite Loop" (Perulangan Abadi)

Perhatikan baris `energi--;` pada contoh `while` di atas. Jika baris tersebut dihapus:
- Nilai `energi` akan selamanya bernilai `3`.
- Kondisi `energi > 0` akan selamanya bernilai `true`.
- Komputer akan terus berputar tanpa henti sampai memori habis atau peramban macet (*freeze*).

**Tips:** Selalu pastikan ada instruksi di dalam perulangan yang membuat kondisi henti akhirnya tercapai!

Di modul berikutnya, kita akan mempelajari wadah khusus untuk menyimpan banyak data sekaligus: **Array**.
