package com.example.data.local

import com.example.data.model.*

object InitialDataSeed {

    val defaultTracks = listOf(
        TrackEntity(
            id = "front-end",
            name = "Front-End Development",
            description = /* seed */ "Pelajari cara membangun antarmuka web yang menarik, responsif, dan interaktif dari nol.",
            icon = "🌐"
        ),
        TrackEntity(
            id = "back-end",
            name = "Back-End Development",
            description = /* seed */ "Bangun server handal, REST API, dan kelola database yang mendukung aplikasi web modern.",
            icon = "⚙️"
        ),
        TrackEntity(
            id = "python",
            name = "Python Programming",
            description = /* seed */ "Kuasai Python untuk otomasi, analisis data, backend web, dan machine learning pemula.",
            icon = "🐍"
        ),
        TrackEntity(
            id = "c-lang",
            name = "Bahasa C",
            description = /* seed */ "Pahami fondasi arsitektur komputer, alokasi memori, dan algoritma dasar sistem.",
            icon = "⚡"
        ),
        TrackEntity(
            id = "cpp-lang",
            name = "Bahasa C++",
            description = /* seed */ "Pelajari pemrograman berorientasi objek (OOP), STL, dan aplikasi performa tinggi.",
            icon = "🚀"
        )
    )

    val defaultCourses = listOf(
        CourseEntity(
            id = "fe-web-foundation-00",
            trackId = "front-end",
            title = /* seed */ "Fondasi Web: Dari Nol ke Baris Kode Pertama",
            level = "Orientasi",
            durationMinutes = 522,
            rating = 0f,
            studentsCount = 0,
            instructor = "Budi Santoso, Senior Front-End Engineer",
            description = /* seed */ "Pelajari struktur HTML lengkap dan styling CSS dari dasar hingga teknik positioning & float. Kelas ini menjadi fondasi sebelum melangkah ke layout modern Flexbox dan Grid.",
            isFeatured = true
        ),
        CourseEntity(
            id = "fe-css-02",
            trackId = "front-end",
            title = /* seed */ "CSS Layout Modern: Flexbox & Grid",
            level = "Pemula-Menengah",
            durationMinutes = 136,
            rating = 0f,
            studentsCount = 0,
            instructor = "Siti Rahmawati, UI/UX & Web Designer",
            description = /* seed */ "Kuasai teknik tata letak modern web menggunakan CSS Flexbox satu dimensi dan CSS Grid dua dimensi untuk membangun antarmuka web yang fleksibel, responsif, dan profesional.",
            isFeatured = true
        ),
        CourseEntity(
            id = "fe-js-03",
            trackId = "front-end",
            title = /* seed */ "JavaScript Fundamentals",
            level = "Pemula",
            durationMinutes = 142,
            rating = 0f,
            studentsCount = 0,
            instructor = "Andi Wijaya, Fullstack Developer",
            description = /* seed */ "Kuasai logika pemrograman web modern (ES2015+): variabel let/const, tipe data, operator, alur if/else dan loop, array & objek, fungsi arrow, manipulasi DOM & event listener, serta pengantar konsep asinkron melalui proyek to-do list interaktif.",
            isFeatured = true
        ),
        CourseEntity(
            id = "be-node-01",
            trackId = "back-end",
            title = /* seed */ "Node.js Fundamentals",
            level = "Pemula",
            durationMinutes = 150,
            rating = 0f,
            studentsCount = 0,
            instructor = "Rian Hidayat, Backend Lead",
            description = /* seed */ "Kuasai pemrograman sisi server dengan Node.js runtime: arsitektur event loop, non-blocking I/O, sistem modul (CommonJS & ESM), manipulasi file, hingga pembuatan server HTTP & REST API.",
            isFeatured = true
        ),
        CourseEntity(
            id = "py-fund-01",
            trackId = "python",
            title = /* seed */ "Python Fundamentals",
            level = "Pemula",
            durationMinutes = 127,
            rating = 0f,
            studentsCount = 0,
            instructor = "Dewi Lestari, Data Scientist & AI Educator",
            description = /* seed */ "Pelajari bahasa pemrograman dengan sintaks terbersih. Cocok untuk data science, otomatisasi script, dan web development.",
            isFeatured = true
        ),
        CourseEntity(
            id = "c-fund-01",
            trackId = "c-lang",
            title = /* seed */ "C Fundamentals: Sintaks & Memori",
            level = "Pemula",
            durationMinutes = 153,
            rating = 0f,
            studentsCount = 0,
            instructor = "Dr. Hendra Gunawan, Dosen Ilmu Komputer",
            description = /* seed */ "Kuasai fondasi komputasi tingkat rendah: tipe data primitif, pointer memori, struct, dan alokasi heap dinamis.",
            isFeatured = false
        ),
        CourseEntity(
            id = "cpp-fund-01",
            trackId = "cpp-lang",
            title = /* seed */ "C++ Fundamentals: Dari C ke OOP",
            level = "Pemula-Menengah",
            durationMinutes = 165,
            rating = 0f,
            studentsCount = 0,
            instructor = "Dr. Hendra Gunawan, Dosen Ilmu Komputer",
            description = /* seed */ "Kuasai Pemrograman Berorientasi Objek (OOP) dan fitur C++ modern: referensi, STL vector & string, class, konstruktor/destruktor RAII, pewarisan, polimorfisme virtual, template, dan smart pointer.",
            isFeatured = false
        )
    )

    val defaultModules = listOf(
        // HTML & CSS Modules
        ModuleEntity(
            id = "mod_html_1",
            courseId = "fe-web-foundation-00",
            order = 1,
            title = /* seed */ "1. Struktur Semantik HTML5",
            readingMinutes = 6,
            content = """
# Struktur Dokumen HTML5 Semantik

HTML5 memperkenalkan elemen semantik yang memberikan makna jelas bagi browser, developer, dan mesin pencari (SEO).

### Elemen Kunci:
- `<header>`: Bagian pembuka halaman atau section
- `<nav>`: Navigasi utama
- `<main>`: Konten sentral utama halaman
- `<article>`: Konten mandiri yang dapat didistribusikan secara terpisah
- `<section>`: Pengelompokan konten tematik
- `<footer>`: Informasi hak cipta, kontak, atau link sekunder

### Keuntungan Semantik:
1. **Aksesibilitas (A11y)**: Screen reader dapat bernavigasi lebih akurat.
2. **SEO**: Algoritma Google memahami hierarki topik lebih baik.
3. **Maintainability**: Kode jauh lebih mudah dibaca dibandingkan bersarang div-soup (`<div class="div-wrap">`).
            """.trimIndent(),
            codeLanguage = "html",
            codeSnippet = """
<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8">
  <title>Portofolio Pengembang</title>
</head>
<body>
  <header>
    <h1>CodeAcademy Developer</h1>
    <nav>
      <a href="#proyek">Proyek</a>
      <a href="#kontak">Kontak</a>
    </nav>
  </header>
  <main>
    <article>
      <h2>Aplikasi Cuaca Cerdas</h2>
      <p>Dibangun dengan Compose & Gemini API.</p>
    </article>
  </main>
  <footer>
    <p>&copy; 2026 CodeAcademy Indonesia.</p>
  </footer>
</body>
</html>
            """.trimIndent(),
            starterHtml = "<header>\n  <h1>Selamat Datang di CodeAcademy</h1>\n  <p>Coba ubah teks ini di editor!</p>\n</header>",
            starterCss = "body {\n  font-family: sans-serif;\n  background: #0f1115;\n  color: #00cec9;\n  padding: 24px;\n}",
            starterJs = "console.log('Halo Web Developer!');",
            playgroundTask = "Buat sebuah kartu profil berisi judul <h2>, paragraf <p>, dan tombol interaktif.",
            hasArAsset = false
        ),
        ModuleEntity(
            id = "mod_html_2",
            courseId = "fe-web-foundation-00",
            order = 2,
            title = /* seed */ "2. CSS Flexbox Layout Modern",
            readingMinutes = 8,
            content = """
# CSS Flexbox: Tata Letak Fleksibel 1-Dimensi

Flexbox (`display: flex`) menyelesaikan masalah centering dan penyusunan elemen secara dinamis tanpa float atau kalkulasi manual.

### Properti Penting Flex Container:
- `display: flex;` mengaktifkan konteks fleksibel.
- `flex-direction: row | column;` arah sumbu utama.
- `justify-content: center | space-between | space-around;` penjajaran di sumbu utama.
- `align-items: center | stretch | flex-start;` penjajaran di sumbu silang.
- `gap: 16px;` jarak antar item tanpa margin hacks.
            """.trimIndent(),
            codeLanguage = "css",
            codeSnippet = """
.card-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  min-height: 100vh;
  background-color: #0F1115;
}

.card {
  background: #1A1D23;
  color: #F5F5F7;
  padding: 24px;
  border-radius: 16px;
  border: 1px solid #6C5CE7;
  box-shadow: 0 8px 24px rgba(108, 92, 231, 0.2);
}
            """.trimIndent(),
            starterHtml = "<div class=\"navbar\">\n  <div class=\"logo\">🚀 CodeAcademy</div>\n  <div class=\"links\">\n    <button class=\"btn\">Mulai Belajar</button>\n  </div>\n</div>",
            starterCss = ".navbar {\n  display: flex;\n  justify-content: space-between;\n  align-items: center;\n  background: #1a1d23;\n  padding: 16px 24px;\n  border-radius: 12px;\n  color: #fff;\n}\n.btn {\n  background: #6c5ce7;\n  color: white;\n  border: none;\n  padding: 10px 18px;\n  border-radius: 8px;\n  font-weight: bold;\n}",
            starterJs = "document.querySelector('.btn').addEventListener('click', () => alert('Selamat Belajar!'));",
            playgroundTask = "Ubah tombol agar memiliki efek hover glowing dan ubah justify-content.",
            hasArAsset = false
        ),
        // JavaScript Modules
        ModuleEntity(
            id = "mod_js_1",
            courseId = "fe-js-03",
            order = 1,
            title = /* seed */ "1. ES6+ Destructuring & Arrow Functions",
            readingMinutes = 7,
            content = """
# JavaScript Modern: Sintaks Bersih ES6+

Fitur ES6+ membuat kode JavaScript lebih ringkas, ekspresif, dan minim bug.

### 1. Arrow Functions
Fungsi ringkas yang tidak mengikat konteks `this` sendiri:
`const tambah = (a, b) => a + b;`

### 2. Object & Array Destructuring
Mengekstrak nilai properti secara instan:
`const { nama, skor } = user;`

### 3. Template Literals
String interpolation dengan backtick:
```javascript
const pesan = `Halo ${'$'}{nama}, skor Anda adalah ${'$'}{skor} XP!`;
```
            """.trimIndent(),
            codeLanguage = "javascript",
            codeSnippet = """
// Contoh ES6+ Destructuring & Filter
const students = [
  { name: 'Adit', xp: 450, track: 'Web' },
  { name: 'Bella', xp: 820, track: 'Python' },
  { name: 'Citra', xp: 1200, track: 'Web' }
];

const topWebStudents = students
  .filter(({ track, xp }) => track === 'Web' && xp > 400)
  .map(({ name, xp }) => `${'$'}{name.toUpperCase()} (Total: ${'$'}{xp} XP)`);

console.log(topWebStudents);
            """.trimIndent(),
            starterHtml = "<div id=\"app\">\n  <h2>Statistik Belajar Siswa</h2>\n  <button id=\"calcBtn\">Hitung Level</button>\n  <p id=\"result\"></p>\n</div>",
            starterCss = "#app { background: #1a1d23; color: #fff; padding: 20px; border-radius: 12px; } #calcBtn { background: #00cec9; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer; }",
            starterJs = "const calculateLevel = (xp) => Math.floor(xp / 250) + 1;\ndocument.getElementById('calcBtn').onclick = () => {\n  const xp = 850;\n  document.getElementById('result').innerText = `Level Anda: \${calculateLevel(xp)} (Total \${xp} XP)`;\n};",
            playgroundTask = "Ubah nilai XP dan coba tambahkan badge reward jika level >= 3.",
            hasArAsset = false
        ),
        // Python Modules
        ModuleEntity(
            id = "mod_py_1",
            courseId = "py-fund-01",
            order = 1,
            title = /* seed */ "1. List Comprehension & Dictionary Magic",
            readingMinutes = 6,
            content = """
# Pythonic Code: List Comprehension & Dicts

List Comprehension adalah cara elegan khas Python untuk membuat list baru dari iterable yang sudah ada dalam satu baris ekspresi yang cepat.

### Format Umum:
`[ekspresi for item in koleksi if kondisi]`

### Keunggulan:
- Lebih cepat dibanding perulangan `for` konvensional dengan `.append()`.
- Lebih mudah dibaca (Pythonic).
            """.trimIndent(),
            codeLanguage = "python",
            codeSnippet = """
# Filter angka genap dan kuadratkan
angka = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
kuadrat_genap = [n ** 2 for n in angka if n % 2 == 0]
print("Kuadrat Genap:", kuadrat_genap)

# Dictionary Comprehension
mahasiswa = {"Budi": 85, "Ayu": 92, "Doni": 68}
lulus = {nama: ("Lulus" if nilai >= 75 else "Remedial") for nama, nilai in mahasiswa.items()}
print("Status Kelulusan:", lulus)
            """.trimIndent(),
            starterHtml = null,
            starterCss = null,
            starterJs = null,
            playgroundTask = null,
            hasArAsset = true
        ),
        // C Language Modules with Pointer Visualizer
        ModuleEntity(
            id = "mod_c_1",
            courseId = "c-fund-01",
            order = 1,
            title = /* seed */ "1. Anatomi Pointer & Alamat Memori",
            readingMinutes = 9,
            content = """
# Menguasai Pointer di Bahasa C

Pointer adalah variabel yang menyimpan **alamat memori** dari variabel lain, bukan menyimpan nilainya langsung.

### Operator Utama:
- `&` (Address-of Operator): Mendapatkan alamat memori fisik variabel (misal: `&angka`).
- `*` (Dereference Operator): Mengakses atau memodifikasi nilai di alamat yang ditunjuk oleh pointer (misal: `*ptr`).

### Visualisasi Memori:
Jika variabel `int x = 42;` terletak di alamat memori `0x7FFEE`, maka pointer `int *p = &x;` menyimpan nilai `0x7FFEE`. Melakukan `*p = 99;` akan langsung mengubah nilai di memori `x` menjadi 99!
            """.trimIndent(),
            codeLanguage = "c",
            codeSnippet = """
#include <stdio.h>
#include <stdlib.h>

void ubahNilai(int *ptr) {
    *ptr = *ptr * 2; // Mengalikan nilai asli di memori
}

int main() {
    int saldo = 50000;
    int *pSaldo = &saldo;

    printf("Saldo Awal: %d (Alamat: %p)\n", saldo, (void*)pSaldo);
    ubahNilai(pSaldo);
    printf("Saldo Setelah Digandakan: %d\n", saldo);

    return 0;
}
            """.trimIndent(),
            starterHtml = null,
            starterCss = null,
            starterJs = null,
            playgroundTask = null,
            hasArAsset = true
        ),
        // C++ Module
        ModuleEntity(
            id = "mod_cpp_1",
            courseId = "cpp-fund-01",
            order = 1,
            title = /* seed */ "1. Modern C++ Smart Pointers & STL Vector",
            readingMinutes = 8,
            content = """
# Modern C++: Memory Safety & STL Vector

Di C++ modern (C++11 ke atas), manajemen memori manual dengan `new` dan `delete` digantikan oleh **Smart Pointers** untuk mencegah memory leak.

### 1. `std::unique_ptr`
Kepemilikan tunggal yang eksklusif. Memori otomatis dibebaskan ketika keluar dari scope (RAII).

### 2. `std::shared_ptr`
Kepemilikan bersama menggunakan Reference Counting.

### 3. `std::vector`
Dynamic array yang aman dan fleksibel dari Standard Template Library.
            """.trimIndent(),
            codeLanguage = "cpp",
            codeSnippet = """
#include <iostream>
#include <memory>
#include <vector>
#include <algorithm>

struct Player {
    std::string name;
    int xp;
    Player(std::string n, int x) : name(n), xp(x) {}
    ~Player() { std::cout << name << " dihapus dari memori.\n"; }
};

int main() {
    // Unique pointer dengan make_unique (RAII)
    auto p1 = std::make_unique<Player>("DevMaster", 1500);
    std::cout << "Pemain: " << p1->name << " (" << p1->xp << " XP)\n";

    std::vector<int> scores = {400, 950, 120, 800};
    std::sort(scores.begin(), scores.end(), std::greater<int>());
    
    std::cout << "Skor Tertinggi: " << scores[0] << "\n";
    return 0;
}
            """.trimIndent(),
            starterHtml = null,
            starterCss = null,
            starterJs = null,
            playgroundTask = null,
            hasArAsset = true
        )
    )

    val defaultQuizQuestions = listOf(
        // HTML & CSS Quiz
        QuizQuestionEntity(
            courseId = "fe-web-foundation-00",
            question = "Manakah elemen HTML5 semantik yang paling tepat digunakan untuk membungkus konten artikel blog mandiri?",
            optionsJson = "<section>|||<article>|||<div class='article'>|||<main>",
            correctIndex = 1,
            explanation = "Elemen <article> dirancang khusus untuk membungkus konten mandiri yang dapat didistribusikan atau digunakan kembali secara independen (seperti artikel blog, posting forum, atau berita)."
        ),
        QuizQuestionEntity(
            courseId = "fe-web-foundation-00",
            question = "Properti Flexbox apa yang digunakan untuk mengatur perataan elemen di sepanjang sumbu utama (main axis)?",
            optionsJson = "align-items|||justify-content|||flex-direction|||align-content",
            correctIndex = 1,
            explanation = "'justify-content' mengatur perataan item pada sumbu utama (main axis), sedangkan 'align-items' mengatur perataan pada sumbu silang (cross axis)."
        ),
        QuizQuestionEntity(
            courseId = "fe-web-foundation-00",
            question = "Bagaimana cara membuat layout CSS Flexbox agar item tersusun vertikal dari atas ke bawah?",
            optionsJson = "flex-direction: column;|||flex-orientation: vertical;|||display: flex-column;|||align-items: vertical;",
            correctIndex = 0,
            explanation = "'flex-direction: column;' mengubah arah sumbu utama menjadi vertikal sehingga flex items berbaris dari atas ke bawah."
        ),
        QuizQuestionEntity(
            courseId = "fe-web-foundation-00",
            question = "Apa fungsi dari tag <meta charset=\"UTF-8\"> dalam dokumen HTML5?",
            optionsJson = "Menghubungkan file CSS|||Menentukan resolusi layar|||Menetapkan pengkodean karakter dokumen|||Mempercepat loading server",
            correctIndex = 2,
            explanation = "<meta charset=\"UTF-8\"> memberitahu browser untuk merender karakter dengan standar encoding UTF-8, mendukung simbol dan karakter multibahasa."
        ),
        // JavaScript Quiz
        QuizQuestionEntity(
            courseId = "fe-js-03",
            question = "Apa output dari: const { x = 10, y = 20 } = { x: 50 }; console.log(x + y);",
            optionsJson = "30|||70|||50|||NaN",
            correctIndex = 1,
            explanation = "x mengambil nilai 50 dari object, sedangkan y mengambil nilai default 20. Totalnya 50 + 20 = 70."
        ),
        QuizQuestionEntity(
            courseId = "fe-js-03",
            question = "Manakah metode array JavaScript yang mengembalikan array baru berisi elemen yang memenuhi syarat kondisi tertentu?",
            optionsJson = "map()|||filter()|||reduce()|||forEach()",
            correctIndex = 1,
            explanation = "Metode '.filter()' menyaring elemen dan menghasilkan array baru hanya dengan elemen yang mengembalikan 'true' pada fungsi pengujian."
        ),
        QuizQuestionEntity(
            courseId = "fe-js-03",
            question = "Manakah pernyataan yang benar mengenai Arrow Function (=>) di JavaScript?",
            optionsJson = "Memiliki kata kunci 'function' wajib|||Tidak mengikat nilai 'this' tersendiri|||Tidak dapat mengembalikan nilai|||Hanya bisa digunakan untuk angka",
            correctIndex = 1,
            explanation = "Arrow functions mewarisi konteks 'this' dari scope leksikal tempat mereka didefinisikan (tidak memiliki 'this' tersendiri)."
        ),
        // Python Quiz
        QuizQuestionEntity(
            courseId = "py-fund-01",
            question = "Apa hasil dari ekspresi list comprehension berikut: [x * 2 for x in range(4) if x % 2 != 0]?",
            optionsJson = "[0, 2, 4, 6]|||[2, 6]|||[1, 3]|||[0, 4]",
            correctIndex = 1,
            explanation = "range(4) menghasilkan 0, 1, 2, 3. Nilai ganjil adalah 1 dan 3. Dikalikan 2 menghasilkan [2, 6]."
        ),
        QuizQuestionEntity(
            courseId = "py-fund-01",
            question = "Tipe data bawaan Python apa yang menyimpan pasangan key-value yang unik?",
            optionsJson = "List|||Tuple|||Dictionary|||Set",
            correctIndex = 2,
            explanation = "Dictionary di Python menyimpan data dalam bentuk pasangan kunci dan nilai (key-value pairs) dengan kunci yang harus bersifat unik dan hashable."
        ),
        // C Quiz
        QuizQuestionEntity(
            courseId = "c-fund-01",
            question = "Jika int *ptr = &angka;, apa arti dari operasi '*ptr = 100;'?",
            optionsJson = "Mengubah alamat memori ptr menjadi 100|||Mengubah nilai variabel angka di memorinya menjadi 100|||Membuat variabel baru bernama ptr|||Menghapus pointer dari memori",
            correctIndex = 1,
            explanation = "Operasi dereferencing '*ptr' mengakses lokasi memori yang ditunjuk (yaitu variabel 'angka') dan menetapkan nilainya menjadi 100."
        )
    )

    val defaultBadges = listOf(
        BadgeEntity(
            id = "badge_first_code",
            name = "First Line of Code",
            emoji = "🌱",
            description = /* seed */ "Menyelesaikan modul pembelajaran pertama di CodeAcademy.",
            requiredXp = 50
        ),
        BadgeEntity(
            id = "badge_streak_3",
            name = "Streak Champion",
            emoji = "🔥",
            description = /* seed */ "Mempertahankan rutinitas belajar 3 hari berturut-turut.",
            requiredXp = 150
        ),
        BadgeEntity(
            id = "badge_quiz_master",
            name = "Quiz Wizard",
            emoji = "🎯",
            description = /* seed */ "Lulus evaluasi quiz dengan skor kelulusan sempurna 100%.",
            requiredXp = 300
        ),
        BadgeEntity(
            id = "badge_web_dev",
            name = "Web Architect",
            emoji = "🌐",
            description = /* seed */ "Menyelesaikan seluruh modul dan playground track Web Development.",
            requiredXp = 500
        ),
        BadgeEntity(
            id = "badge_polyglot",
            name = "Polyglot Coder",
            emoji = "⚡",
            description = /* seed */ "Mempelajari lebih dari 2 bahasa pemrograman di CodeAcademy.",
            requiredXp = 1000
        )
    )

    val defaultArAssets = listOf(
        ArAssetEntity(
            id = "ar_pointer_c",
            moduleId = "mod_c_1",
            title = /* seed */ "Visualisasi Pointer & Address Alokasi Memori",
            assetUrl = "https://models.codeacademy.id/ar/c_pointer_memory.glb",
            codeLanguage = "c",
            visualizationType = "pointer",
            description = /* seed */ "Simulasi visual alamat RAM heksadesimal 0x7FFEE, nilai register CPU, dan panah dereference secara skematik interaktif."
        ),
        ArAssetEntity(
            id = "ar_memory_py",
            moduleId = "mod_py_1",
            title = /* seed */ "Visualisasi Objek Heap & Reference Counting Python",
            assetUrl = "https://models.codeacademy.id/ar/py_object_heap.glb",
            codeLanguage = "python",
            visualizationType = "memory",
            description = /* seed */ "Visualisasi alokasi PyObject di Memori Heap dan garbage collector reference count."
        ),
        ArAssetEntity(
            id = "ar_stl_cpp",
            moduleId = "mod_cpp_1",
            title = /* seed */ "Visualisasi Dynamic Resizing std::vector di Memori",
            assetUrl = "https://models.codeacademy.id/ar/cpp_vector_growth.glb",
            codeLanguage = "cpp",
            visualizationType = "flow",
            description = /* seed */ "Animasi kapasitas vector, reallocation, dan pemindahan pointer memori secara dinamis."
        )
    )
}
