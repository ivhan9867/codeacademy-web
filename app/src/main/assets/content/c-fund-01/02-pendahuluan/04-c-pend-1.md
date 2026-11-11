---
id: c-pend-1
courseId: c-fund-01
order: 4
title: "Apa Itu Bahasa C dan Mengapa Masih Merajai Dunia Sistem?"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Mengetahui sejarah singkat kelahiran bahasa C di Bell Labs"
  - "Memahami konsep close to the metal (dekat dengan perangkat keras)"
  - "Mengetahui mengapa C tetap tak tergantikan di era komputasi modern"
codeLanguage: c
codeSnippet: |
  #include <stdio.h>

  int main(void) {
      /* Bahasa C memberi kendali penuh tanpa lapisan perantara */
      printf("Kecepatan maksimal dan kendali mutlak!\n");
      return 0;
  }
---
Jika sebuah bahasa pemrograman diciptakan lebih dari setengah abad yang lalu (tahun 1972), mengapa di era kecerdasan buatan dan komputasi awan saat ini bahasa C tetap menjadi salah satu bahasa paling vital di dunia?

Jawabannya adalah: **kecepatan murni, kendali tanpa kompromi, dan ukuran jejak memori yang sangat kecil (*minimal overhead*)**.

![Bahasa C Sebagai Inti Sistem Operasi dan Perangkat Keras](asset:images/c-fund-01/peran-bahasa-c.png)

## Lahir dari Kebutuhan Membangun Sistem Operasi UNIX

Pada awal tahun 1970-an di Bell Telephone Laboratories, **Dennis Ritchie** dan **Ken Thompson** sedang merancang sistem operasi UNIX. Saat itu, sebagian besar sistem operasi ditulis menggunakan bahasa Assembly—bahasa yang sangat terikat pada satu jenis mikroprosesor tertentu. Menulis ulang seluruh sistem operasi setiap kali komputer berganti model adalah pekerjaan yang luar biasa melelahkan.

Dennis Ritchie kemudian menciptakan **Bahasa C**. Bahasa ini menggabungkan dua kelebihan:
1. **Ekspresi Tingkat Tinggi**: Memiliki fungsi, perulangan, dan tipe data terstruktur yang mudah dipahami manusia.
2. **Kekuatan Tingkat Rendah**: Mampu memanipulasi alamat bit dan byte di dalam RAM secara langsung layaknya bahasa Assembly.

Dengan bahasa C, UNIX berhasil ditulis ulang menjadi sistem operasi pertama yang **portabel**—dapat dipindahkan ke berbagai arsitektur mesin komputer dengan mudah cukup dengan mengompilasikannya kembali.

## Mengapa Tidak Menggunakan Python atau JavaScript Saja?

Bahasa modern seperti Python, Java, atau JavaScript memiliki fitur kenyamanan tingkat tinggi bernama **Garbage Collector (Pengumpul Sampah Otomatis)** dan berjalan di atas mesin virtual (*runtime engine*).

Fitur ini sangat bagus untuk membuat aplikasi web dengan cepat, namun memiliki kelemahan fatal:
- **Jeda Waktu (*Latency*)**: Garbage collector bisa sewaktu-waktu menghentikan program sejenak untuk membersihkan memori yang tidak terpakai.
- **Konsumsi RAM Besar**: Lapisan perantara memakan memori berlebih.

Bayangkan jika perangkat rem mobil otomatis (*anti-lock braking system*), mesin pacu jantung medis, atau roket antariksa mengalami jeda 0,5 detik hanya karena sistem sedang membersihkan memori yang tidak terpakai. Tragedi bisa terjadi!

Di sinilah bahasa C tidak tergantikan:
- **Kernel Sistem Operasi**: Linux, macOS (Darwin), dan Windows NT ditulis menggunakan C/C++.
- **Basis Data Raksasa**: Mesin penyimpanan data PostgreSQL, MySQL, dan SQLite ditulis dengan C.
- **Perangkat Tertanam (*Embedded Systems & IoT*)**: Mikrokontroler mesin cuci, router Wi-Fi, jam tangan pintar, hingga modul satelit.
- **Interpreter Bahasa Lain**: Python itu sendiri (CPython) dan mesin JavaScript V8 sebagian besar dibangun menggunakan C dan C++.

Di modul berikutnya, kita akan membedah anatomi kode program bahasa C yang paling mendasar!
