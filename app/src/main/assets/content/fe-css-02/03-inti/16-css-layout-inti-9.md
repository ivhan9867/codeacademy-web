---
id: css-layout-inti-9
courseId: fe-css-02
order: 18
title: "Alignment Lanjutan: align-content & Centering Sempurna"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Membedakan secara presisi fungsi align-items (per baris) dan align-content (antar baris jamak)"
  - "Menerapkan teknik centering sempurna secara horizontal dan vertikal menggunakan Flexbox"
  - "Menghindari jebakan umum saat align-content tidak berefek atau container tidak memiliki tinggi"
codeLanguage: css
codeSnippet: |
  .center-sempurna {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 240px;
  }

  .multi-baris {
    display: flex;
    flex-wrap: wrap;
    align-content: space-between;
    height: 260px;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: align-content & Centering</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h3>1. Kotak Centering Sempurna</h3>
      <div class="hero-box">
        <div class="kartu-sambutan">
          <h4>Selamat Datang!</h4>
          <p>Posisiku tepat di tengah secara horizontal &amp; vertikal.</p>
        </div>
      </div>

      <h3>2. Perataan Multi-Baris (align-content)</h3>
      <div class="galeri-wrap">
        <span class="pill">HTML5</span>
        <span class="pill">CSS3</span>
        <span class="pill">Flexbox</span>
        <span class="pill">CSS Grid</span>
        <span class="pill">Responsive</span>
        <span class="pill">Variables</span>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Lengkapi agar .kartu-sambutan berada tepat di tengah .hero-box */
  .hero-box {
    background-color: #e2f0f1;
    border: 2px dashed #1a6368;
    border-radius: 10px;
    min-height: 180px;
    padding: 16px;
    /* Tambahkan display: flex; justify-content: center; align-items: center; di sini */
  }

  .kartu-sambutan {
    background-color: #104246;
    color: #ffffff;
    padding: 16px 24px;
    border-radius: 8px;
    text-align: center;
  }

  .kartu-sambutan h4 {
    margin: 0 0 6px 0;
  }

  .kartu-sambutan p {
    margin: 0;
    font-size: 13px;
  }

  /* 2. Wadah multi-baris dengan ruang ekstra di sumbu vertikal */
  .galeri-wrap {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    height: 180px;
    padding: 14px;
    background-color: #f1ece4;
    border: 2px dashed #968e86;
    border-radius: 10px;
    /* Coba tambahkan align-content: center; atau space-between; */
  }

  .pill {
    background-color: #df7b56;
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-weight: bold;
    font-size: 13px;
    height: fit-content;
  }
playgroundTask: "Pada '.hero-box', aktifkan centering sempurna dengan menambahkan 'display: flex;', 'justify-content: center;', dan 'align-items: center;'. Lalu pada '.galeri-wrap', tambahkan 'align-content: center;' untuk merapatkan seluruh baris pill ke tengah vertikal!"
verificationRules:
  - "css:display: flex"
  - "css:justify-content"
  - "css:align-items"
---
Salah satu kebingungan paling sering dialami pemula saat memakai Flexbox adalah membedakan **`align-items`** dan **`align-content`**. Keduanya sama-sama mengatur posisi di sumbu silang (*Cross Axis*), tetapi cakupan kerjanya sangat berbeda.

## Perbedaan `align-items` vs `align-content`

Bayangkan sebuah rak buku dengan beberapa tingkat papan.
- **`align-items`** mengatur posisi masing-masing buku **di dalam satu tingkat rak** (misalnya item pendek rata tengah terhadap item tinggi di baris yang sama).
- **`align-content`** mengatur jarak dan posisi **keseluruhan tingkat rak** terhadap tinggi lemari (hanya bekerja jika item terlipat menjadi **lebih dari satu baris** memakai `flex-wrap: wrap`).

```text
  align-items: center (1 baris / per baris)      align-content: center (antar baris jamak)
+-----------------------------------------+    +-----------------------------------------+
|                                         |    |              (ruang kosong)             |
|   [A]     [  B  ]     [ C ]             |    |   [Item 1]    [Item 2]    [Item 3]      |
|  (sejajar di sumbu tengah baris ini)    |    |   [Item 4]    [Item 5]                  |
|                                         |    |              (ruang kosong)             |
+-----------------------------------------+    +-----------------------------------------+
```

Ringkasan nilai populer untuk `align-content` saat `flex-wrap: wrap` aktif:

| Nilai `align-content` | Perilaku Baris Jamak di Sumbu Vertikal |
| :--- | :--- |
| `stretch` *(default)* | Tiap baris meregang merata memenuhi seluruh tinggi container |
| `flex-start` | Semua baris merapat ke sisi atas container |
| `center` | Seluruh kelompok baris berkumpul di tengah vertikal container |
| `space-between` | Baris pertama di paling atas, baris terakhir di paling bawah |
| `space-around` | Jarak di atas, bawah, dan antar baris dibagi merata |

## Pola Centering Sempurna (*Perfect Centering*)

Dulu, menaruh sebuah elemen tepat di tengah layar baik secara horizontal maupun vertikal membutuhkan trik `position: absolute`, `top: 50%`, dan `transform: translate(-50%, -50%)`. Dengan Flexbox, cukup tiga baris sakti pada container induknya:

```css
.wadah-tengah {
  display: flex;
  justify-content: center; /* Tengah di Main Axis (horizontal) */
  align-items: center;     /* Tengah di Cross Axis (vertikal) */
  min-height: 100vh;       /* Penting: beri tinggi wadah agar ruang vertikal tersedia */
}
```

Alternatif super ringkas untuk satu anak tunggal di dalam flex container adalah memberi `margin: auto` pada elemen anak:

```css
.wadah {
  display: flex;
  min-height: 300px;
}

.anak-tunggal {
  margin: auto; /* Otomatis menyerap ruang sisa di keempat sisi */
}
```

## Jebakan Umum (*Common Pitfalls*) yang Harus Dihindari

1. **Menggunakan `align-content` tanpa `flex-wrap: wrap`**:
   Jika wadah masih memakai `flex-wrap: nowrap` (bawaan), seluruh item hanya berada di satu baris. Dalam kondisi satu baris, `align-content` **tidak memiliki efek apa pun**!
2. **Lupa memberi tinggi (`height` / `min-height`) pada container**:
   Jika tinggi container pas mengikuti tinggi kontennya, maka tidak ada ruang sisa di sumbu vertikal. Akibatnya `align-items: center` maupun `align-content: center` terlihat tidak bergerak sama sekali.
3. **Salah mengira `margin: auto` sama dengan `align-items: center` saat item banyak**:
   Jika terdapat banyak item yang terbungkus (`wrap`) dan tingginya melebihi container (*overflow*), `margin: auto` bisa mendorong bagian atas item keluar dari area gulir (*scroll*), sedangkan `justify-content: center` tetap aman.

Uji langsung perbedaan centering satu kotak dan perataan multi-baris di Playground sekarang!
