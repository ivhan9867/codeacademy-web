---
id: js-eval-1
courseId: fe-js-03
order: 25
title: "Proyek Mini: Membangun Aplikasi To-Do List Interaktif"
readingMinutes: 8
section: EVALUASI
learningObjectives:
  - Menggabungkan struktur HTML, gaya CSS, dan logika JavaScript dalam satu proyek mini yang utuh
  - Menerapkan arsitektur berbasis data: memanipulasi array data di memori lalu merendernya ke DOM
  - Menangani aksi input teks formulir dan klik tombol secara real-time
codeLanguage: javascript
codeSnippet: |
  const list = ["Tugas 1"];
  list.push("Tugas 2");
  console.log("Total tugas:", list.length);
starterHtml: |
  <div style="font-family: sans-serif; max-width: 420px; margin: 0 auto; padding: 20px; border: 1px solid #E2E8F0; border-radius: 12px; background: #FFFFFF; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);">
    <h3 style="color: #0F172A; margin-top: 0; text-align: center;">Daftar Tugas Interaktif</h3>
    <div style="display: flex; gap: 8px; margin-bottom: 16px;">
      <input id="input-tugas" type="text" placeholder="Tulis tugas baru..." style="flex: 1; padding: 10px 12px; border: 1px solid #CBD5E1; border-radius: 6px; font-size: 14px;" value="Belajar DOM JavaScript" />
      <button id="tombol-tambah-tugas" style="background-color: #0D9488; color: white; border: none; padding: 10px 16px; border-radius: 6px; font-weight: bold; cursor: pointer;">
        Tambah
      </button>
    </div>
    <ul id="daftar-tugas" style="list-style: none; padding: 0; margin: 0;">
      <li style="padding: 10px 12px; background: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 6px; margin-bottom: 8px; color: #334155;">
        1. Selesaikan modul Fondasi Web
      </li>
    </ul>
  </div>
starterJs: |
  // 1. Data array sebagai sumber kebenaran (Single Source of Truth)
  const tugasList = ["Selesaikan modul Fondasi Web"];

  // 2. Menangkap elemen DOM yang dibutuhkan
  const inputTugas = document.querySelector("#input-tugas");
  const tombolTambah = document.querySelector("#tombol-tambah-tugas");
  const elemenDaftar = document.querySelector("#daftar-tugas");

  // 3. Fungsi untuk memperbarui tampilan daftar di layar
  const renderDaftar = () => {
    if (!elemenDaftar) return;
    elemenDaftar.innerHTML = "";
    
    tugasList.forEach((tugas, idx) => {
      const li = document.createElement("li");
      li.style.cssText = "padding: 10px 12px; background: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 6px; margin-bottom: 8px; color: #334155;";
      li.textContent = `${idx + 1}. ${tugas}`;
      elemenDaftar.appendChild(li);
    });
  };

  // 4. Memasang event listener pada tombol Tambah
  if (tombolTambah && inputTugas) {
    tombolTambah.addEventListener("click", () => {
      const teksBaru = inputTugas.value.trim();
      if (teksBaru !== "") {
        tugasList.push(teksBaru);
        renderDaftar();
        inputTugas.value = "";
        console.log(`Tugas ditambahkan: "${teksBaru}". Total tugas sekarang: ${tugasList.length}`);
      }
    });
  }

  console.log("Aplikasi To-Do List siap digunakan!");
playgroundTask: |
  Tekan tombol 'Jalankan'. Di jendela pratinjau, klik tombol 'Tambah' untuk memasukkan tugas yang ada di input ('Belajar DOM JavaScript'). Amati item baru yang langsung muncul di daftar, lalu buka panel Console untuk memverifikasi log:
  Tugas ditambahkan: "Belajar DOM JavaScript". Total tugas sekarang: 2
---

# Proyek Mini: Membangun Aplikasi To-Do List Interaktif

Saatnya menyatukan semua potongan puzzle yang sudah kamu pelajari selama 24 modul terakhir:
- **Variabel & Array**: Menyimpan daftar tugas di memori.
- **Fungsi & Arrow Function**: Meracik logika pembaruan tampilan (*render*).
- **Percabangan `if`**: Memastikan pengguna tidak menambahkan tugas kosong.
- **DOM Selector**: Menangkap kotak input, tombol, dan wadah daftar `<ul>`.
- **Event Listener**: Merespons saat tombol *"Tambah"* diklik oleh pengguna.

Mari kita bedah arsitektur aplikasi ini langkah demi langkah!

---

## 1. Data-Driven Architecture (Pola Berbasis Data)

Kesalahan paling umum yang dilakukan pemula adalah langsung menempelkan teks ke elemen HTML tanpa mencatatnya di variabel JavaScript terlebih dahulu.

Cara profesional yang benar adalah menggunakan pola **berbasis data (*Data-Driven*)**:
1. Buat array JavaScript sebagai pusat penyimpanan utama:
   ```javascript
   const tugasList = ["Selesaikan modul Fondasi Web"];
   ```
2. Ketika ada tugas baru, masukkan dulu datanya ke dalam array menggunakan `tugasList.push(teksBaru)`.
3. Setelah data di array bertambah, panggil fungsi `renderDaftar()` untuk menggambar ulang elemen `<li>` di layar.

Dengan pola ini, jika nanti kamu ingin menghitung jumlah tugas yang selesai atau menyaring tugas, datanya sudah tersimpan rapi di dalam array!

---

## 2. Membaca Input & Validasi

Untuk mengambil apa yang diketik pengguna di dalam kotak `<input>`, kita membaca properti **`.value`**:

```javascript
const teksBaru = inputTugas.value.trim();
```

Metode `.trim()` bertugas membuang spasi kosong di awal dan di akhir teks. Dengan pengecekan `if (teksBaru !== "")`, kita memastikan pengguna tidak sengaja memasukkan tugas kosong.

---

## 3. Membuat Elemen HTML Baru: document.createElement()

Alih-alih mengetik string HTML mentah, JavaScript menyediakan cara yang sangat aman untuk merakit elemen baru dari nol:

```javascript
// 1. Buat tag <li> baru
const li = document.createElement("li");

// 2. Beri teks tugas
li.textContent = "1. Belajar DOM JavaScript";

// 3. Tempelkan ke dalam tag <ul> induk
elemenDaftar.appendChild(li);
```

---

## Coba Langsung di Editor!

1. Tekan tombol **Jalankan**.
2. Klik tombol **Tambah** di jendela pratinjau.
3. Ketikkan tugas baru di kotak input, misalnya *"Bikin kopi sore"*, lalu klik **Tambah** lagi.
4. Buka panel **Console** untuk melihat laporan penambahan tugas secara real-time!

Kamu resmi telah membangun aplikasi web interaktif pertamamu menggunakan JavaScript murni!
