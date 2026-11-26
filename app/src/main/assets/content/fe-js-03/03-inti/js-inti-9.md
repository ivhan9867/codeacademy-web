---
id: js-inti-9
courseId: fe-js-03
order: 19
title: "Truthy, Falsy, & Operator Modern (&&, ||, ??)"
readingMinutes: 7
section: INTI
learningObjectives:
  - Mengenal seluruh nilai Falsy di JavaScript dan membedakannya dengan nilai Truthy
  - Memahami mekanisme short-circuiting pada operator logika && dan ||
  - Menggunakan operator Nullish Coalescing (??) untuk menetapkan nilai default secara aman tanpa mengorbankan angka 0 atau string kosong
codeLanguage: javascript
codeSnippet: |
  const jumlahItem = 0;
  const pakaiOr = jumlahItem || 10;      // 10 (keliru jika 0 valid!)
  const pakaiNullish = jumlahItem ?? 10; // 0  (aman!)

  console.log("Dengan ||:", pakaiOr);
  console.log("Dengan ??:", pakaiNullish);
starterJs: |
  const skorPemain = 0;
  const namaTampilan = "";
  const catatanPengguna = null;

  // Bandingkan || dengan ?? untuk nilai 0 dan string kosong
  const hasilDenganOr = skorPemain || 100;
  const hasilDenganNullish = skorPemain ?? 100;

  // Gunakan ?? untuk nilai yang memang null atau undefined
  const labelCatatan = catatanPengguna ?? "Belum ada catatan";
  const labelNama = namaTampilan ?? "Anonim";

  console.log("Skor pakai || :", hasilDenganOr);
  console.log("Skor pakai ?? :", hasilDenganNullish);
  console.log("Catatan       :", labelCatatan);
  console.log("Nama (??)     :", `"${labelNama}"`);
playgroundTask: |
  Jalankan kode bawaan dan periksa panel Console. Amati perbedaan antara operator || dan ??:
  Skor pakai || : 100
  Skor pakai ?? : 0
  Catatan       : Belum ada catatan
  Nama (??)     : ""
  Coba ubah nilai catatanPengguna menjadi "Siap belajar" lalu jalankan ulang untuk melihat perubahannya.
verificationRules:
  - "js:??"
  - "js:console.log"
---

# Truthy, Falsy, & Operator Modern (&&, ||, ??)

Saat menulis kondisi `if`, kamu tidak selalu memasukkan nilai boolean murni (`true` atau `false`). Sering kali kamu memeriksa sebuah variabel teks, angka, atau objek secara langsung:

```javascript
const nama = "Budi";
if (nama) {
  console.log("Halo, " + nama);
}
```

Mengapa `"Budi"` bisa diterima oleh `if`? Karena JavaScript secara otomatis menilai setiap nilai sebagai **Truthy** (dianggap `true`) atau **Falsy** (dianggap `false`).

---

## Daftar Lengkap 7 Nilai Falsy di JavaScript

Di seluruh bahasa JavaScript, hanya ada **7 kelompok nilai Falsy**. Selain nilai-nilai pada tabel di bawah ini, **semuanya bersifat Truthy**.

| No | Nilai Falsy | Tipe Data | Keterangan Singkat |
| :---: | :--- | :--- | :--- |
| 1 | `false` | Boolean | Nilai boolean salah secara eksplisit |
| 2 | `0` (serta `-0` dan `0n`) | Number / BigInt | Angka nol (baik integer, minus nol, maupun BigInt nol) |
| 3 | `""` / `''` / `` `` `` | String | String kosong tanpa satu karakter pun (spasi `" "` adalah Truthy!) |
| 4 | `null` | Null | Nilai kosong yang disengaja |
| 5 | `undefined` | Undefined | Variabel belum diisi nilai |
| 6 | `NaN` | Number | *Not-a-Number* (hasil operasi matematika yang gagal) |
| 7 | `document.all` | Object (Legacy) | Objek lawas browser khusus untuk kompatibilitas situs lama |

> **Perangkap Pemula:** String `"0"`, string `"false"`, array kosong `[]`, dan objek kosong `{}` semuanya bernilai **Truthy**! Hanya string yang benar-benar kosong (`""`) yang bersifat Falsy.

---

## Operator Logika & Short-Circuiting (`&&` dan `||`)

Selain mengembalikan boolean di dalam `if`, operator `&&` dan `||` di JavaScript sebenarnya **mengembalikan salah satu nilai operannya** melalui mekanisme *short-circuit* (berhenti lebih awal):

1. **Operator AND (`&&`)**: Mengevaluasi dari kiri ke kanan. Jika sisi kiri **Falsy**, langsung kembalikan sisi kiri. Jika sisi kiri **Truthy**, kembalikan sisi kanan.
   ```javascript
   const sudahLogin = true;
   const sapaan = sudahLogin && "Selamat datang!";
   console.log(sapaan); // "Selamat datang!"
   ```
2. **Operator OR (`||`)**: Jika sisi kiri **Truthy**, langsung kembalikan sisi kiri. Jika sisi kiri **Falsy**, kembalikan sisi kanan.

Dulu, programmer sering memakai `||` untuk memberi nilai cadangan (*default value*):

```javascript
const jumlahDiskon = 0; // Diskon 0% adalah nilai valid!
const diskonFinal = jumlahDiskon || 10;
console.log(diskonFinal); // 10 -> BUG! Angka 0 dianggap Falsy!
```

Karena `0` termasuk Falsy, operator `||` keliru menganggap `jumlahDiskon` tidak ada isinya dan menimpanya dengan `10`.

---

## Solusi Modern: Nullish Coalescing (`??`)

Sejak ES2020, JavaScript menghadirkan operator **`??`** (*Nullish Coalescing*). Operator ini jauh lebih presisi karena **hanya menganggap `null` dan `undefined` sebagai nilai kosong**.

```text
  nilaiKiri ?? nilaiDefault
     |
     +-- Jika nilaiKiri adalah null atau undefined --> kembalikan nilaiDefault
     |
     +-- Jika nilaiKiri adalah 0, "", false, atau lainnya --> kembalikan nilaiKiri
```

Perbandingan langsung `||` vs `??`:

| Nilai Awal (`x`) | Hasil `x \|\| "Default"` | Hasil `x ?? "Default"` |
| :--- | :--- | :--- |
| `null` | `"Default"` | `"Default"` |
| `undefined` | `"Default"` | `"Default"` |
| `0` | `"Default"` (sering jadi bug) | `0` (aman) |
| `""` | `"Default"` | `""` (aman) |
| `false` | `"Default"` | `false` (aman) |

---

## Pola Default Value yang Praktis

Gunakan `??` ketika angka `0`, teks kosong `""`, atau status `false` adalah input yang sah dari pengguna atau server:

```javascript
const pengaturan = {
  temaGelap: false,
  batasHalaman: 0,
  bahasa: undefined
};

const tema = pengaturan.temaGelap ?? true;      // false (tidak tertimpa!)
const batas = pengaturan.batasHalaman ?? 20;    // 0     (tidak tertimpa!)
const bahasa = pengaturan.bahasa ?? "id";       // "id"  (memakai default)
```

Coba jalankan kode latihan di playground untuk melihat perbedaan nyata antara `||` dan `??` di Console!
