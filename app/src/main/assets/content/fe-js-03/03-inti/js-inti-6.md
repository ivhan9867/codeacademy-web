---
id: js-inti-6
courseId: fe-js-03
order: 14
title: "Memilih Opsi Spesifik dengan Pernyataan switch"
readingMinutes: 5
section: INTI
learningObjectives:
  - Memahami kapan menggunakan switch sebagai alternatif yang lebih bersih dibanding banyak else if
  - Menguasai komponen pembentuk switch: case, break, dan default
  - Memahami pentingnya break untuk mencegah perilaku meluncur bebas (fall-through) yang tidak diinginkan
codeLanguage: javascript
codeSnippet: |
  const hari = "Senin";
  switch (hari) {
    case "Senin":
      console.log("Mulai bekerja");
      break;
    default:
      console.log("Hari biasa");
  }
starterJs: |
  const kodeRole = "EDITOR";

  switch (kodeRole) {
    case "ADMIN":
      console.log("Hak Akses: Kelola Penuh Sistem");
      break;
    case "EDITOR":
      console.log("Hak Akses: Sunting & Terbitkan Konten");
      break;
    case "VIEWER":
      console.log("Hak Akses: Hanya Membaca Konten");
      break;
    default:
      console.log("Hak Akses: Tidak Dikenal");
      break;
  }
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Pastikan output yang tercetak adalah:
  Hak Akses: Sunting & Terbitkan Konten
  Coba ubah variabel kodeRole menjadi "VIEWER" atau "GUEST" untuk melihat case yang sesuai atau fallback default dipanggil.
---

# Memilih Opsi Spesifik dengan Pernyataan switch

Bayangkan kamu sedang membuat sistem role akun: ada "ADMIN", "EDITOR", "VIEWER", "CONTRIBUTOR", dan "GUEST".

Jika kamu menggunakan `if / else if`, kodenya akan berulang-ulang menuliskan `if (role === "ADMIN") ... else if (role === "EDITOR") ...`.

Untuk situasi di mana kita ingin mencocokkan **satu variabel terhadap banyak kemungkinan nilai pasti**, JavaScript menyediakan struktur yang jauh lebih rapi bernama **`switch`**.

---

## Anatomi switch

```javascript
const lampu = "Kuning";

switch (lampu) {
  case "Merah":
    console.log("Berhenti!");
    break;
  case "Kuning":
    console.log("Hati-hati, bersiap!");
    break;
  case "Hijau":
    console.log("Jalan terus!");
    break;
  default:
    console.log("Lampu rusak / tidak ada warna");
    break;
}
```

Output:
```text
Hati-hati, bersiap!
```

---

## Tiga Komponen Kunci switch

1. **`case "NILAI":`**: Titik pemeriksaan. Jika nilai variabel cocok persis dengan nilai di sebelah kata `case`, baris instruksi di bawahnya akan dijalankan.
2. **`break;`**: Perintah keluar. Begitu baris `break` tercapai, JavaScript langsung melompat keluar dari blok `switch`.
3. **`default:`**: Jaring pengaman. Bagian ini akan dijalankan jika tidak ada satu pun `case` di atas yang cocok (mirip fungsi `else` terakhir pada percabangan `if`).

---

## Hati-Hati dengan Fenomena "Fall-Through"!

Apa yang terjadi jika kamu lupa menuliskan kata kunci `break`?

```javascript
const tingkat = 1;

switch (tingkat) {
  case 1:
    console.log("Tingkat 1");
    // Lupa menulis break!
  case 2:
    console.log("Tingkat 2");
    break;
}
```

Output yang dihasilkan:
```text
Tingkat 1
Tingkat 2
```

Tanpa `break`, JavaScript akan terus meluncur (*fall-through*) dan mengeksekusi instruksi di `case` bawahnya meskipun syaratnya tidak cocok! Oleh karena itu, jangan sampai melewatkan `break` di setiap akhir `case`.

Di modul berikutnya, kita akan mempelajari bagaimana cara menyuruh komputer melakukan hal berulang kali tanpa lelah menggunakan perulangan (*looping*)!
