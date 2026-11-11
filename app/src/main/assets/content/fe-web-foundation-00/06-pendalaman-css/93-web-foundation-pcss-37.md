---
id: web-foundation-pcss-37
courseId: fe-web-foundation-00
order: 93
title: "Positioning: Relative Positioning"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Menerapkan position: relative untuk menggeser elemen dari posisi aslinya"
  - "Memahami bahwa pergeseran relative dihitung dari posisi seharusnya elemen tersebut berada"
---
Seperti yang sudah kita singgung sebelumnya, `position: relative` memungkinkan kita menggeser elemen ke atas, kanan, bawah, atau kiri, **tanpa memengaruhi** posisi elemen lain di sekitarnya. Ini terjadi karena elemen dengan `position: relative` dikeluarkan dari normal flow.

```css
p.kedua {
  position: relative;
  top: 10px;
  left: 100px;
}
```

Pada contoh di atas, paragraf kedua akan bergeser **10 piksel ke bawah** dan **100 piksel ke kanan** dari posisi aslinya.

Satu hal penting yang perlu diingat: nilai `top`, `left`, `right`, dan `bottom` pada relative positioning **selalu dihitung dari posisi seharusnya elemen itu berada** — bukan dari sudut halaman atau elemen lain. Jadi kalau kamu mengatur `top: 10px`, artinya elemen bergeser 10px ke bawah dari tempat ia *seharusnya* muncul di normal flow, bukan 10px dari atas halaman.

Ruang yang sebelumnya ditempati elemen ini di normal flow **tetap dipertahankan kosong** — seolah elemen itu masih ada di sana, meski secara visual sudah berpindah. Inilah yang membedakan relative dengan absolute, yang akan kita bahas selanjutnya.
