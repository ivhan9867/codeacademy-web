---
id: js-lanjutan-7
courseId: fe-js-03
order: 27
title: "Merespons Aksi Pengguna: Event Listener & Tombol Interaktif"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Memahami konsep peristiwa (Event) sebagai tindakan interaksi yang dilakukan pengguna
  - Menguasai penggunaan metode .addEventListener() untuk merespons aksi klik
  - Membangun komponen interaktif pertama: penghitung angka yang bertambah setiap kali tombol ditekan
codeLanguage: javascript
codeSnippet: |
  const btn = document.querySelector("#btn");
  btn.addEventListener("click", () => {
    console.log("Tombol ditekan!");
  });
starterHtml: |
  <div style="font-family: sans-serif; text-align: center; padding: 24px; border: 1px solid #E2E8F0; border-radius: 8px;">
    <h3 style="color: #0F172A; margin: 0 0 8px 0;">Penghitung Klik Interaktif</h3>
    <p style="font-size: 36px; font-weight: bold; color: #0D9488; margin: 12px 0;" id="angka-tampilan">0</p>
    <button id="tombol-tambah" style="background-color: #0D9488; color: white; border: none; padding: 10px 24px; border-radius: 6px; font-size: 15px; font-weight: bold; cursor: pointer;">
      Tambah +1
    </button>
  </div>
starterJs: |
  let jumlahKlik = 0;
  const tombol = document.querySelector("#tombol-tambah");
  const tampilanAngka = document.querySelector("#angka-tampilan");

  if (tombol && tampilanAngka) {
    // Memasang telinga pendengar peristiwa 'click'
    tombol.addEventListener("click", () => {
      jumlahKlik = jumlahKlik + 1;
      tampilanAngka.textContent = jumlahKlik;
      console.log("Tombol diklik! Total klik:", jumlahKlik);
    });
  }
playgroundTask: |
  Tekan 'Jalankan'. Di jendela pratinjau, klik tombol 'Tambah +1' sebanyak 3 kali. Amati angka di layar yang bertambah menjadi 3, lalu periksa panel Console untuk memverifikasi pesan:
  Tombol diklik! Total klik: 3
---

# Merespons Aksi Pengguna: Event Listener & Tombol Interaktif

Sebuah website menjadi benar-benar hidup ketika dapat merespons tindakan yang dilakukan oleh penggunanya:
- Pengguna mengklik tombol *"Kirim"*.
- Pengguna mengetikkan kata kunci pencarian.
- Pengguna menggeser kursor ke atas foto (*hover*).

Di dunia browser, setiap aksi interaksi pengguna tersebut disebut sebagai **Event (Peristiwa)**.

```text
[ Pengguna Klik Tombol ] ---> [ Browser Trigger Event "click" ]
                                         |
                                         v
                              [ Callback Function Berjalan ]
                                         |
                                         v
                              [ Tampilan DOM Diperbarui ]
```
---

## Memasang "Telinga Pendengar": addEventListener

Agar JavaScript tahu kapan suatu peristiwa terjadi pada elemen HTML tertentu, kita memasang fungsi penjaga yang disebut **Event Listener**:

```javascript
elemenTarget.addEventListener("namaEvent", fungsiTindakan);
```

### Dua Parameter Utama:
1. **Nama Event**: Jenis aksi yang ditunggu, misalnya:
   - `"click"`: Saat elemen diklik dengan mouse atau disentuh di layar ponsel.
   - `"input"`: Saat pengguna sedang mengetik di dalam kotak teks.
   - `"submit"`: Saat formulir dikirimkan.
2. **Fungsi Tindakan (*Handler / Callback*)**: Kode yang harus dijalankan saat peristiwa tersebut terjadi. Di sini, arrow function sangat nyaman digunakan!

---

## Studi Kasus: Tombol Penghitung Klik

Perhatikan kode latihan di bawah:
1. Kita memiliki variabel penyimpan data `let jumlahKlik = 0;`.
2. Kita memilih tombol dan teks angka menggunakan `document.querySelector()`.
3. Kita memasang event listener `"click"` pada tombol.
4. Setiap kali tombol ditekan:
   - `jumlahKlik` bertambah 1.
   - Teks angka di layar (`tampilanAngka.textContent`) langsung diperbarui.
   - Kita juga mencatat kejadian tersebut ke dalam Console.

Coba jalankan dan tekan tombol di jendela pratinjau sekarang juga! Kamu baru saja membuat komponen web interaktif pertamamu!
