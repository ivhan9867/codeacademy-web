---
id: web-foundation-pcss-34
courseId: fe-web-foundation-00
order: 90
title: "Positioning: Mengenal Properti Position"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Memahami keterbatasan margin untuk memindahkan posisi elemen"
  - "Mengenal empat nilai utama properti position: static, relative, absolute, fixed"
---
Sejauh ini, kita sudah tahu cara memindahkan posisi elemen dengan `margin`. Tapi ada satu masalah: mengubah margin sebuah elemen **selalu berdampak** pada posisi elemen lain di sekitarnya. Lantas, bagaimana kalau kita ingin memindahkan satu elemen tanpa mengganggu elemen lainnya sama sekali?

Jawabannya ada pada properti `position`, yang mengatur **skema pemosisian** (*positioning schema*) sebuah elemen. Ada empat nilai utama yang perlu kita kenal:

| Nilai | Penjelasan singkat |
|---|---|
| `static` | Perilaku bawaan setiap elemen: tersusun mengikuti normal flow, tanpa bisa dipindah dengan `top`/`left`/dst |
| `relative` | Elemen bisa digeser dari posisi semula, tanpa memengaruhi elemen di sekitarnya |
| `absolute` | Elemen dikeluarkan sepenuhnya dari normal flow, posisinya relatif terhadap induk ber-posisi non-static terdekat (atau jendela browser jika tidak ada) |
| `fixed` | Seperti absolute, tapi posisinya selalu relatif terhadap jendela browser, bahkan tetap di tempat saat halaman di-scroll |

Keempat nilai ini akan kita bahas satu per satu secara lebih mendalam di modul-modul berikutnya. Tapi sebelum itu, ada satu konsep dasar yang perlu dipahami lebih dulu: apa sebenarnya **normal flow** (atau *static flow*) itu, dan mengapa terkadang kita perlu mengeluarkan elemen darinya.
