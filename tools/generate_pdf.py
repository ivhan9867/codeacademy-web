import os
import sys
from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.pdfgen import canvas
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont

# Register Fonts
FONT_REG = r"C:\Windows\Fonts\segoeui.ttf"
FONT_BOLD = r"C:\Windows\Fonts\segoeuib.ttf"
try:
    pdfmetrics.registerFont(TTFont("SegoeUI", FONT_REG))
    pdfmetrics.registerFont(TTFont("SegoeUI-Bold", FONT_BOLD))
    FONT_NORMAL_NAME = "SegoeUI"
    FONT_BOLD_NAME = "SegoeUI-Bold"
except Exception:
    FONT_NORMAL_NAME = "Helvetica"
    FONT_BOLD_NAME = "Helvetica-Bold"

PDF_PATH = r"docs\Manual_Book_CodeAcademy.pdf"
ASSETS_DIR = r"docs\manual_book_assets"
DRAWABLE_DIR = r"app\src\main\res\drawable"

PAGE_WIDTH, PAGE_HEIGHT = A4  # 595.27 x 841.89

# Colors matching CodeAcademy brand & Sample PDF
C_NAVY_DARK = colors.HexColor("#104246")
C_TEAL_PRIMARY = colors.HexColor("#1A6368")
C_TEAL_LIGHT = colors.HexColor("#389B9E")
C_CORAL = colors.HexColor("#DF7B56")
C_TEXT_DARK = colors.HexColor("#24211D")
C_TEXT_MUTED = colors.HexColor("#5A626A")
C_LINE_GREY = colors.HexColor("#D8DEE4")
C_CALLOUT_RED = colors.HexColor("#E74C3C")
C_WHITE = colors.white
C_CARD_BG = colors.HexColor("#F8F9FA")

def draw_running_header(c):
    # App Logo / Icon on top-left
    c.saveState()
    icon_p = os.path.join(DRAWABLE_DIR, "ic_codeacademy_logo.webp")
    if os.path.exists(icon_p):
        try:
            c.drawImage(icon_p, 45, 792, width=24, height=24, mask='auto')
        except Exception:
            c.setFillColor(C_TEAL_PRIMARY)
            c.circle(57, 804, 12, fill=1, stroke=0)
    else:
        c.setFillColor(C_TEAL_PRIMARY)
        c.circle(57, 804, 12, fill=1, stroke=0)
        
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 9.5)
    c.drawString(76, 808, "MANUAL BOOK APLIKASI CODEACADEMY")
    c.setFont(FONT_BOLD_NAME, 9.5)
    c.drawString(76, 796, "(VERSI MOBILE)")
    
    # Dividing horizontal line
    c.setStrokeColor(C_NAVY_DARK)
    c.setLineWidth(1.2)
    c.line(45, 786, 550, 786)
    c.restoreState()

def draw_running_footer(c, page_str):
    c.saveState()
    # Solid bottom stripe
    c.setFillColor(C_NAVY_DARK)
    c.rect(0, 0, PAGE_WIDTH, 26, fill=1, stroke=0)
    # Page number
    c.setFillColor(C_WHITE)
    c.setFont(FONT_BOLD_NAME, 10)
    c.drawRightString(550, 9, page_str)
    c.restoreState()

def draw_keterangan_box(c, x, y, w, h, items):
    c.saveState()
    c.setFillColor(C_CARD_BG)
    c.setStrokeColor(C_LINE_GREY)
    c.setLineWidth(0.8)
    c.roundRect(x, y, w, h, 6, fill=1, stroke=1)
    
    c.setFillColor(C_TEXT_DARK)
    c.setFont(FONT_BOLD_NAME, 8.5)
    c.drawString(x + 10, y + h - 14, "Keterangan Gambar:")
    
    cur_y = y + h - 26
    c.setFont(FONT_NORMAL_NAME, 8)
    for num, txt in items:
        # Red circle with number
        c.setFillColor(C_CALLOUT_RED)
        c.circle(x + 16, cur_y + 3, 5, fill=1, stroke=0)
        c.setFillColor(C_WHITE)
        c.setFont(FONT_BOLD_NAME, 6.5)
        c.drawCentredString(x + 16, cur_y + 1, str(num))
        
        c.setFillColor(C_TEXT_DARK)
        c.setFont(FONT_NORMAL_NAME, 7.8)
        c.drawString(x + 26, cur_y, txt)
        cur_y -= 12
    c.restoreState()

def build_pdf():
    os.makedirs(os.path.dirname(PDF_PATH), exist_ok=True)
    c = canvas.Canvas(PDF_PATH, pagesize=A4)
    c.setTitle("Manual Book Aplikasi CodeAcademy (Versi Mobile)")
    c.setAuthor("CodeAcademy Development Team")
    c.setSubject("Manual Book Dokumentasi Tugas Akhir")

    # ==========================================
    # PAGE 1: COVER
    # ==========================================
    # Full dark background
    c.setFillColor(C_NAVY_DARK)
    c.rect(0, 0, PAGE_WIDTH, PAGE_HEIGHT, fill=1, stroke=0)

    # Decorative geometric arcs (matching sample design)
    c.saveState()
    c.setStrokeColor(C_TEAL_PRIMARY)
    c.setLineWidth(38)
    c.circle(120, 150, 360, stroke=1, fill=0)
    c.setStrokeColor(C_TEAL_LIGHT)
    c.setLineWidth(14)
    c.circle(120, 150, 310, stroke=1, fill=0)
    c.restoreState()

    # Titles
    c.setFillColor(C_WHITE)
    c.setFont(FONT_BOLD_NAME, 26)
    c.drawString(60, 710, "MANUAL BOOK")
    c.drawString(60, 672, "APLIKASI")
    c.drawString(60, 634, "CODEACADEMY")
    
    c.setFillColor(C_CORAL)
    c.setFont(FONT_BOLD_NAME, 20)
    c.drawString(60, 595, "PLATFORM BELAJAR CODING")
    c.setFont(FONT_BOLD_NAME, 16)
    c.drawString(60, 568, "(VERSI MOBILE)")

    # Android Robot Icon
    c.saveState()
    c.setFillColor(colors.HexColor("#3DDC84"))
    # Head arc
    c.wedge(60, 480, 106, 526, 0, 180, fill=1, stroke=0)
    # Eyes
    c.setFillColor(C_WHITE)
    c.circle(75, 506, 2.5, fill=1, stroke=0)
    c.circle(91, 506, 2.5, fill=1, stroke=0)
    # Body
    c.setFillColor(colors.HexColor("#3DDC84"))
    c.roundRect(60, 440, 46, 35, 4, fill=1, stroke=0)
    c.restoreState()

    # Center Emblem / App Logo
    logo_p = os.path.join(DRAWABLE_DIR, "ic_codeacademy_logo.webp")
    if os.path.exists(logo_p):
        try:
            c.drawImage(logo_p, 60, 130, width=54, height=54, mask='auto')
        except Exception:
            pass

    c.setFillColor(C_WHITE)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(124, 162, "CodeAcademy Mobile")
    c.setFont(FONT_NORMAL_NAME, 9)
    c.drawString(124, 146, "Platform Pembelajaran Web Development & Coding Mobile-First")
    c.setFillColor(C_TEAL_LIGHT)
    c.setFont(FONT_NORMAL_NAME, 8.5)
    c.drawString(124, 132, "Buku Panduan Pengguna • Tugas Akhir Pengembangan Aplikasi")

    c.showPage()

    # ==========================================
    # PAGE 2: DAFTAR ISI (ii)
    # ==========================================
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 16)
    c.drawCentredString(PAGE_WIDTH / 2, 770, "DAFTAR ISI")
    c.setStrokeColor(C_NAVY_DARK)
    c.setLineWidth(1)
    c.line(220, 758, 375, 758)

    toc_items = [
        ("Halaman Awal", "i"),
        ("Daftar Isi", "ii"),
        ("1. Pengenalan Aplikasi CodeAcademy", "1"),
        ("2. Download dan Instalasi Aplikasi Versi Mobile", "2"),
        ("3. Pendaftaran Akun, Masuk & Pemulihan Sandi", "3"),
        ("4. Alur Onboarding Siswa Baru", "4"),
        ("5. Pengenalan Fitur Beranda (Home Dashboard)", "5"),
        ("6. Navigasi Menu Samping & Pencarian Materi", "6"),
        ("7. Katalog Kelas & Detail Silabus Kursus", "7"),
        ("8. Fitur Pembelajaran Interaktif (Materi Reader)", "8"),
        ("9. Fitur Playground Interaktif (Live Code Sandbox)", "9"),
        ("10. Fitur AI Tutor Cerdas (Didukung Gemini AI)", "10"),
        ("11. Sistem Evaluasi, Kuis Bab & Hasil Evaluasi", "11"),
        ("12. Pengumpulan Tugas Akhir (Final Project)", "12"),
        ("13. Forum Diskusi & Komunitas Belajar", "13"),
        ("14. Progres Belajar, Gamifikasi & Sertifikat Digital", "14"),
        ("15. Profil Pengguna & Pengaturan Aplikasi", "15"),
        ("16. Portal Web Fasilitator & Penilaian Tugas Akhir", "16"),
        ("17. Portal Web Verifikasi Sertifikat & Kredensial", "17"),
    ]

    ty = 722
    for title, pg in toc_items:
        c.setFillColor(C_TEXT_DARK)
        c.setFont(FONT_BOLD_NAME if pg in ["i", "ii", "1", "5", "16", "17"] else FONT_NORMAL_NAME, 9)
        c.drawString(55, ty, title)
        # Dot leader line
        c.setStrokeColor(C_LINE_GREY)
        c.setLineWidth(0.6)
        c.setDash(2, 4)
        c.line(55 + len(title) * 5.6 + 15, ty + 3, 515, ty + 3)
        c.setDash()
        # Page number
        c.drawRightString(540, ty, pg)
        ty -= 32

    draw_running_footer(c, "ii")
    c.showPage()

    # ==========================================
    # PAGE 3: PENGENALAN APLIKASI CODEACADEMY (Hal 1)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PENGENALAN APLIKASI CODEACADEMY")

    desc1 = "Aplikasi CodeAcademy merupakan platform pembelajaran pemrograman dan web development"
    desc2 = "berbasis mobile-first yang dirancang untuk memberikan pengalaman belajar coding terstruktur,"
    desc3 = "fleksibel, dan interaktif langsung dari smartphone. Aplikasi ini dilengkapi beragam fitur utama:"
    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, desc1)
    c.drawString(45, 722, desc2)
    c.drawString(45, 709, desc3)

    # 2-Column features
    col1 = [
        "• Kurikulum Komprehensif (Front-End, Back-End,",
        "  Python, Bahasa C, dan C++).",
        "• AI Tutor Cerdas (Didukung Google Gemini AI)",
        "  untuk panduan coding dan debugging real-time.",
        "• Interactive Code Playground untuk eksekusi",
        "  HTML, CSS, JS, dan logika program langsung di HP.",
        "• Sistem Kuis & Evaluasi berwaktu dengan",
        "  passing grade standar kelulusan."
    ]
    col2 = [
        "• Gamifikasi Lengkap: Poin XP, Level Siswa,",
        "  Daily Streak, Lencana (Badges), dan Heatmap.",
        "• Forum Diskusi Kelas dan Topik Diskusi Hangat",
        "  untuk kolaborasi tanya jawab antar siswa.",
        "• Pengumpulan Proyek Akhir berbasis rubrik",
        "  penilaian standar industri software engineering.",
        "• Sertifikat Digital Resmi dengan nomor ID kredensial",
        "  kelulusan yang dapat diverifikasi."
    ]
    
    fy = 690
    for l in col1:
        c.drawString(45, fy, l)
        fy -= 12
    fy = 690
    for l in col2:
        c.drawString(300, fy, l)
        fy -= 12

    # Mockup 1: Splash
    mock_p = os.path.join(ASSETS_DIR, "mockup_01_splash.png")
    if os.path.exists(mock_p):
        c.drawImage(mock_p, 195, 120, width=205, height=410, mask='auto')

    c.setFillColor(C_TEXT_MUTED)
    c.setFont(FONT_NORMAL_NAME, 8)
    c.drawCentredString(PAGE_WIDTH / 2, 95, "Tampilan Layar Pembuka (Splash Screen) Aplikasi CodeAcademy Mobile")

    draw_running_footer(c, "1")
    c.showPage()

    # ==========================================
    # PAGE 4: DOWNLOAD & INSTALASI (Hal 2)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "DOWNLOAD DAN INSTALASI APLIKASI VERSI MOBILE")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Aplikasi CodeAcademy dapat dipasang pada perangkat smartphone Android dengan spesifikasi:")
    c.drawString(45, 722, "• Sistem Operasi: Android 8.0 (Oreo) atau versi yang lebih baru (API Level 26+).")
    c.drawString(45, 709, "• Kapasitas Memori: RAM minimal 2 GB dan ruang penyimpanan kosong minimal 150 MB.")
    c.drawString(45, 696, "• Koneksi Jaringan: Memerlukan akses internet aktif untuk AI Tutor dan sinkronisasi data.")

    c.setFont(FONT_BOLD_NAME, 9.5)
    c.drawString(45, 672, "Langkah - Langkah Instalasi Paket APK:")
    c.setFont(FONT_NORMAL_NAME, 8.5)
    steps = [
        "1. Unduh berkas instalasi CodeAcademy-v1.0-release.apk dari portal distribusi kampus / repository.",
        "2. Buka folder Unduhan (Downloads) melalui aplikasi Pengelola Berkas (File Manager) di smartphone Anda.",
        "3. Ketuk berkas APK tersebut. Jika muncul peringatan keamanan sumber tidak dikenal, pilih opsi 'Setelan'",
        "   lalu aktifkan izin 'Izinkan penginstalan aplikasi dari sumber ini' (Allow from this source).",
        "4. Pada dialog penginstal paket yang muncul, ketuk tombol INSTAL (lihat gambar di bawah).",
        "5. Tunggu beberapa detik hingga proses instalasi selesai, lalu ketuk 'Buka' untuk mulai belajar."
    ]
    sy = 654
    for st in steps:
        c.drawString(45, sy, st)
        sy -= 13

    # Mockup 2: Install
    mock_p = os.path.join(ASSETS_DIR, "mockup_02_install.png")
    if os.path.exists(mock_p):
        c.drawImage(mock_p, 195, 140, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 100, 48, 395, 68, [
        (1, "Tombol Instal: Ketuk untuk mengonfirmasi dan memulai pemasangan aplikasi CodeAcademy."),
        (2, "Izin Aplikasi: Aplikasi dirancang aman tanpa meminta izin akses kontak atau penyimpanan rahasia.")
    ])

    draw_running_footer(c, "2")
    c.showPage()

    # ==========================================
    # PAGE 5: PENDAFTARAN & LOGIN (Hal 3)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PENDAFTARAN AKUN, MASUK & PEMULIHAN SANDI")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Setelah aplikasi terbuka, pengguna akan diarahkan ke Form Masuk (Login). Bagi pengguna baru,")
    c.drawString(45, 722, "silakan lakukan registrasi dengan mengetuk tautan 'Daftar Sekarang'.")

    # 2 Mockups: Login & Register
    mock_login = os.path.join(ASSETS_DIR, "mockup_03_login.png")
    mock_reg = os.path.join(ASSETS_DIR, "mockup_03b_register.png")
    if os.path.exists(mock_login):
        c.drawImage(mock_login, 60, 200, width=210, height=420, mask='auto')
    if os.path.exists(mock_reg):
        c.drawImage(mock_reg, 320, 200, width=210, height=420, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 135, [
        (1, "Input Email & Kata Sandi: Masukkan alamat email terdaftar serta kata sandi akun (minimal 6 karakter)."),
        (2, "Lupa Kata Sandi?: Fitur pemulihan akun untuk mengatur ulang sandi baru jika pengguna lupa password lama."),
        (3, "Tombol Masuk ke Akun: Menjalankan autentikasi lokal terenkripsi dan membuka Beranda utama."),
        (4, "Tautan Daftar Sekarang: Mengalihkan pengguna baru ke formulir registrasi akun."),
        (5, "Form Registrasi Akun Baru: Mengisi Nama Lengkap, Email aktif, Kata Sandi, dan Konfirmasi Kata Sandi."),
        (6, "Validasi Sistem: Memastikan email valid, password cocok, dan nama minimal memiliki panjang 2 karakter.")
    ])

    draw_running_footer(c, "3")
    c.showPage()

    # ==========================================
    # PAGE 6: ONBOARDING SISWA BARU (Hal 5 -> 4)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "ALUR ONBOARDING PENGENALAN JALUR BELAJAR")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Bagi siswa yang baru pertama kali mendaftar, sistem akan menyajikan alur interaktif Onboarding")
    c.drawString(45, 722, "sebanyak 5 langkah untuk mempersonalisasi jalur belajar dan menetapkan komitmen:")

    onb_steps = [
        "1. Welcome Step: Sambutan selamat datang serta penjelasan visi belajar coding mobile-first.",
        "2. Track Selection Step: Siswa memilih salah satu dari 4 peminatan kurikulum spesialisasi:",
        "   • Front-End Web Development (HTML, CSS, Modern Flexbox & Grid, JavaScript).",
        "   • Back-End Web Development (Node.js runtime, asynchronous, REST API, Database).",
        "   • Python Programming (Logika sintaks, otomatisasi skrip, struktur data, dan OOP).",
        "   • Bahasa C & C++ (Fondasi arsitektur komputasi, alokasi memori pointer, dan STL).",
        "3. Code Preview Step: Simulasi visual membaca penjelasan sintaks dan editor kode interaktif.",
        "4. Community Step: Pengenalan ekosistem forum tanya-jawab dan kompetisi Papan Peringkat.",
        "5. Plan Summary Step: Rekap preferensi target belajar harian dan memulai sesi belajar pertama."
    ]
    oy = 702
    for s in onb_steps:
        c.drawString(45, oy, s)
        oy -= 12

    # Mockup 4: Onboarding
    mock_onb = os.path.join(ASSETS_DIR, "mockup_04_onboarding.png")
    if os.path.exists(mock_onb):
        c.drawImage(mock_onb, 195, 120, width=205, height=410, mask='auto')

    c.setFillColor(C_TEXT_MUTED)
    c.setFont(FONT_NORMAL_NAME, 8)
    c.drawCentredString(PAGE_WIDTH / 2, 95, "Layar Pemilihan Jalur Spesialisasi pada Alur Onboarding Siswa Baru")

    draw_running_footer(c, "4")
    c.showPage()

    # ==========================================
    # PAGE 7: BERANDA / HOME DASHBOARD (Hal 5)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PENGENALAN FITUR BERANDA (HOME DASHBOARD)")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Beranda adalah dashboard utama yang menampilkan status progres belajar, misi harian, dan pintasan:")

    # Mockup 5: Home
    mock_home = os.path.join(ASSETS_DIR, "mockup_05_home.png")
    if os.path.exists(mock_home):
        c.drawImage(mock_home, 195, 230, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 165, [
        (1, "Menu Samping: Membuka navigation drawer untuk fitur Cari Materi, Tersimpan, dan Playground."),
        (2, "Badge Runtutan (Streak Harian): Menghitung hari aktif belajar berturut-turut untuk menjaga disiplin koding."),
        (3, "Carousel Banner Promosi: Menampilkan kartu highlight materi unggulan dan target pencapaian XP mingguan."),
        (4, "Kartu Lanjutkan Belajar: Akses langsung melanjutkan modul terakhir yang sedang dipelajari dengan satu klik."),
        (5, "Misi Harian (Daily Quests): Tantangan harian (baca modul, kuis, coba playground) dengan hadiah bonus XP."),
        (6, "Bilah Navigasi Bawah: Navigasi cepat ke 5 menu utama: Beranda, Kelas, Progres, Komunitas, dan Profil.")
    ])

    draw_running_footer(c, "5")
    c.showPage()

    # ==========================================
    # PAGE 8: DRAWER & SEARCH (Hal 6)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "NAVIGASI MENU SAMPING & PENCARIAN MATERI")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Fitur Drawer Menu dan Pencarian Materi Global memungkinkan navigasi lintas fitur secara efisien:")

    # 2 Mockups: Drawer & Search
    mock_draw = os.path.join(ASSETS_DIR, "mockup_06_drawer.png")
    mock_srch = os.path.join(ASSETS_DIR, "mockup_06b_search.png")
    if os.path.exists(mock_draw):
        c.drawImage(mock_draw, 60, 200, width=210, height=420, mask='auto')
    if os.path.exists(mock_srch):
        c.drawImage(mock_srch, 320, 200, width=210, height=420, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 135, [
        (1, "Profil Header Drawer: Menampilkan inisial avatar, nama siswa, peringkat level (Lv. 4), dan total XP."),
        (2, "Menu Aktivitas: Pintasan menuju Modul Tersimpan (Bookmarks), Notifikasi Belajar, dan Sertifikat Saya."),
        (3, "Menu Tools Coding: Akses langsung menuju Live Playground sandbox dan sesi Tanya AI Tutor."),
        (4, "Pencarian Materi Real-Time: Siswa dapat mengetik kata kunci topik (contoh: 'Flexbox', 'Loop', 'Pointer')."),
        (5, "Hasil Indeks Cepat: Sistem menampilkan kartu modul yang relevan beserta informasi kelas asalnya."),
        (6, "Pintasan Masuk Modul: Mengetuk hasil pencarian akan langsung membuka modul pembelajaran terkait.")
    ])

    draw_running_footer(c, "6")
    c.showPage()

    # ==========================================
    # PAGE 9: KATALOG KELAS & DETAIL KURSUS (Hal 7)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "KATALOG KELAS & DETAIL SILABUS KURSUS")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Menu Kelas menyajikan kurikulum terstruktur dan silabus pembelajaran berjenjang:")

    # 2 Mockups: Courses & Class Detail
    mock_crs = os.path.join(ASSETS_DIR, "mockup_07_courses.png")
    mock_dtl = os.path.join(ASSETS_DIR, "mockup_07b_class_detail.png")
    if os.path.exists(mock_crs):
        c.drawImage(mock_crs, 60, 200, width=210, height=420, mask='auto')
    if os.path.exists(mock_dtl):
        c.drawImage(mock_dtl, 320, 200, width=210, height=420, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 135, [
        (1, "Filter Kategori Jalur Belajar: Memilih kategori kelas (Semua, Front-End, Back-End, Python, C/C++)."),
        (2, "Pill Tingkat Kesulitan: Menandai jenjang kelas: Pemula (Hijau), Menengah (Kuning), atau Lanjut (Merah)."),
        (3, "Informasi Durasi & Modul: Estimasi total waktu belajar dan jumlah modul yang ada di dalam kelas."),
        (4, "Fast-Track Placement Test: Tes penempatan untuk melompati materi dasar bagi yang sudah paham."),
        (5, "Struktur Silabus Bab: Modul dikelompokkan ke dalam Bab Persiapan, Pendahuluan, Inti, dan Evaluasi."),
        (6, "Status Penyelesaian Modul: Indikator ikon tanda centang hijau bagi modul yang telah tuntas dipelajari.")
    ])

    draw_running_footer(c, "7")
    c.showPage()

    # ==========================================
    # PAGE 10: PEMBELAJARAN INTERAKTIF / READER (Hal 8)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "FITUR PEMBELAJARAN INTERAKTIF (MATERI READER)")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Layar baca modul didesain secara mobile-first untuk kenyamanan membaca penjelasan teknis dan kode:")

    # Mockup 8: Reader
    mock_rdr = os.path.join(ASSETS_DIR, "mockup_08_materi_reader.png")
    if os.path.exists(mock_rdr):
        c.drawImage(mock_rdr, 195, 220, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 155, [
        (1, "Navigasi Breadcrumb: Menampilkan hierarki kelas dan bab yang sedang dibaca."),
        (2, "Tombol Bookmark: Menandai materi ke dalam daftar Modul Tersimpan agar mudah diakses kembali."),
        (3, "Blok Kode Syntax Highlighting: Format kode berwarna standar industri lengkap dengan tombol 'Salin Kode'."),
        (4, "Tombol 'Coba di Playground': Membuka potongan kode langsung ke sandbox untuk diedit dan dites."),
        (5, "Tombol 'Tanya AI Tutor': Mengirimkan konteks modul ke AI Tutor jika ada penjelasan yang kurang jelas."),
        (6, "Tombol 'Tandai Selesai': Menyimpan status penyelesaian modul ke database Room dan memberikan +20 XP.")
    ])

    draw_running_footer(c, "8")
    c.showPage()

    # ==========================================
    # PAGE 11: PLAYGROUND LIVE (Hal 9)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "FITUR PLAYGROUND INTERAKTIF (LIVE CODE SANDBOX)")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Playground menyediakan lingkungan eksperimen koding langsung di smartphone tanpa perlu PC:")

    # Mockup 9: Playground
    mock_ply = os.path.join(ASSETS_DIR, "mockup_09_playground.png")
    if os.path.exists(mock_ply):
        c.drawImage(mock_ply, 195, 220, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 155, [
        (1, "Tab Editor Bahasa: Berpindah antara editor berkas HTML, stylesheet CSS, dan logika skrip JavaScript."),
        (2, "Editor Kode dengan Line Number: Editor teks ringan yang memudahkan identifikasi baris kode koding."),
        (3, "Live Preview Container: Menampilkan hasil render tampilan visual website secara langsung dan interaktif."),
        (4, "Tombol 'Jalankan' (Run): Memicu eksekusi kode dan merender pembaruan antarmuka pada area preview."),
        (5, "Tombol 'Tanya AI': Mengirimkan kode yang sedang ditulis ke AI Tutor untuk diperiksa bug atau sintaks error-nya."),
        (6, "Dukungan Python & C/C++: Mendukung metode mental execution trace output untuk bahasa backend dan sistem.")
    ])

    draw_running_footer(c, "9")
    c.showPage()

    # ==========================================
    # PAGE 12: AI TUTOR CERDAS (Hal 10)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "FITUR AI TUTOR CERDAS (DIDUKUNG GEMINI AI)")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "AI Tutor adalah asisten belajar personal berbasis LLM (Google Gemini API) yang siap membantu 24/7:")

    # Mockup 10: AI Tutor
    mock_ai = os.path.join(ASSETS_DIR, "mockup_10_ai_tutor.png")
    if os.path.exists(mock_ai):
        c.drawImage(mock_ai, 195, 220, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 155, [
        (1, "Konteks Modul Sadar Materi: AI otomatis memahami topik modul atau kelas yang sedang dibuka siswa."),
        (2, "Prompt Pills (Rekomendasi Cepat): Pertanyaan praktis siap pakai untuk memperdalam konsep koding."),
        (3, "Bantuan Debugging & Solusi Kode: Siswa dapat menempelkan error kode dan AI akan menerangkan solusinya."),
        (4, "Gaya Bahasa Pedagogis: Penjelasan ramah pemula, runtut langkah demi langkah, dan menyertakan analogi."),
        (5, "Input Pesan Fleksibel: Mendukung pertanyaan teoritis, sintaks fungsi, hingga perbaikan bug algoritma."),
        (6, "Riwayat Diskusi: Sesi percakapan tersimpan rapi selama sesi belajar berlangsung di aplikasi.")
    ])

    draw_running_footer(c, "10")
    c.showPage()

    # ==========================================
    # PAGE 13: SISTEM KUIS & EVALUASI (Hal 11)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "SISTEM EVALUASI, KUIS BAB & HASIL EVALUASI")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Untuk menguji pemahaman siswa di setiap akhir bab, aplikasi menyediakan kuis evaluasi berwaktu:")

    # 2 Mockups: Quiz & Result
    mock_qz = os.path.join(ASSETS_DIR, "mockup_11_quiz.png")
    mock_res = os.path.join(ASSETS_DIR, "mockup_11b_quiz_result.png")
    if os.path.exists(mock_qz):
        c.drawImage(mock_qz, 60, 200, width=210, height=420, mask='auto')
    if os.path.exists(mock_res):
        c.drawImage(mock_res, 320, 200, width=210, height=420, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 135, [
        (1, "Timer Hitung Mundur: Waktu ujian dibatasi (misal 10 menit) untuk melatih ketangkasan analisis kode."),
        (2, "Progress Pengerjaan: Menampilkan nomor soal aktif dari total soal yang harus diselesaikan."),
        (3, "Pilihan Ganda Interaktif: Opsi jawaban dilengkapi umpan balik visual saat dipilih oleh siswa."),
        (4, "Layar Hasil Evaluasi: Mengumumkan persentase skor akhir dan status kelulusan (Lulus jika skor >= 70%)."),
        (5, "Reward Gamifikasi: Siswa yang lulus menerima perolehan bonus XP (+50 XP s.d. +100 XP)."),
        (6, "Tombol Retake & Pembahasan: Siswa dapat mengulang kuis atau meninjau pembahasan jawaban yang keliru.")
    ])

    draw_running_footer(c, "11")
    c.showPage()

    # ==========================================
    # PAGE 14: PROYEK AKHIR (Hal 12)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PENGUMPULAN TUGAS AKHIR (FINAL PROJECT SUBMISSION)")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Sebagai syarat kelulusan sertifikasi pada tiap kelas, siswa wajib mengumpulkan Proyek Akhir studi kasus:")

    # Mockup 12: Final Project
    mock_fp = os.path.join(ASSETS_DIR, "mockup_12_final_project.png")
    if os.path.exists(mock_fp):
        c.drawImage(mock_fp, 195, 220, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 155, [
        (1, "Spesifikasi & Brief Tugas: Deskripsi detail proyek nyata yang harus dibangun (misal: Web Portofolio)."),
        (2, "Rubrik Kriteria Penilaian: Daftar checklist teknis kelayakan (HTML5 semantik, CSS responsif, styling)."),
        (3, "Input URL Repositori: Siswa memasukkan tautan repositori kode sumber (GitHub / GitLab / link demo)."),
        (4, "Catatan untuk Fasilitator: Kolom pesan untuk memberikan penjelasan arsitektur proyek kepada reviewer."),
        (5, "Fitur Muat dari Playground: Kode yang sudah diuji di playground dapat diimpor langsung tanpa ketik ulang."),
        (6, "Tombol Kumpulkan Proyek: Mengirim submisi untuk dinilai oleh sistem/fasilitator dan membuka sertifikat.")
    ])

    draw_running_footer(c, "12")
    c.showPage()

    # ==========================================
    # PAGE 15: FORUM KOMUNITAS (Hal 13)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "FORUM DISKUSI & KOMUNITAS BELAJAR")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Komunitas CodeAcademy adalah wadah sosial edukatif untuk saling membantu menyelesaikan kendala koding:")

    # Mockup 13: Community
    mock_com = os.path.join(ASSETS_DIR, "mockup_13_community.png")
    if os.path.exists(mock_com):
        c.drawImage(mock_com, 195, 220, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 155, [
        (1, "Tab Diskusi Hangat: Menyoroti thread pertanyaan paling populer dengan vote dan balasan terbanyak."),
        (2, "Tab Forum Kelas: Diskusi terfokus yang dikelompokkan secara spesifik per mata kuliah / materi modul."),
        (3, "Kartu Thread Pertanyaan: Menampilkan judul kendala, nama siswa pembuat thread, serta jumlah balasan."),
        (4, "Sistem Upvote & Apresiasi: Siswa dapat memberikan upvote pada pertanyaan atau jawaban yang bermanfaat."),
        (5, "Tombol Buat Diskusi Baru: Formulir untuk mengajukan kendala baru lengkap dengan lampiran potongan kode."),
        (6, "Jawaban Solutif: Diskusi saling terhubung untuk membangun budaya problem solving secara kolaboratif.")
    ])

    draw_running_footer(c, "13")
    c.showPage()

    # ==========================================
    # PAGE 16: LEADERBOARD & SERTIFIKAT (Hal 14)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PROGRES BELAJAR, GAMIFIKASI & SERTIFIKAT DIGITAL")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Fitur motivasi gamifikasi dan bukti pencapaian kelulusan resmi dalam bentuk sertifikat digital:")

    # Mockup 14: Leaderboard & Cert
    mock_lbc = os.path.join(ASSETS_DIR, "mockup_14_leaderboard_cert.png")
    if os.path.exists(mock_lbc):
        c.drawImage(mock_lbc, 195, 220, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 155, [
        (1, "Papan Peringkat Pelajar: Klasemen siswa teratas (Top 50) mingguan dan global berdasarkan akumulasi XP."),
        (2, "Highlight Posisi Siswa: Kartu berwarna khusus yang memperlihatkan peringkat dan selisih XP akun siswa."),
        (3, "Sistem Medali (Emas, Perak, Perunggu): Apresiasi penghargaan bagi tiga siswa dengan produktivitas belajar tertinggi."),
        (4, "Sertifikat Kelulusan Resmi: Bukti kompetensi penyelesaian kurikulum kelas lengkap dengan nama siswa."),
        (5, "ID Kredensial Unik: Kode verifikasi (misal: CA-2026-88912) untuk validasi keaslian sertifikat kelulusan."),
        (6, "Tombol Unduh & Bagikan: Memungkinkan siswa menyimpan sertifikat kelulusan dalam format digital.")
    ])

    draw_running_footer(c, "14")
    c.showPage()

    # ==========================================
    # PAGE 17: PROFIL & PENGATURAN (Hal 15)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PROFIL PENGGUNA & PENGATURAN APLIKASI")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Layar Profil memberikan kendali penuh dalam pengelolaan data akun dan preferensi tampilan aplikasi:")

    # Mockup 15: Profile
    mock_prf = os.path.join(ASSETS_DIR, "mockup_15_profile_settings.png")
    if os.path.exists(mock_prf):
        c.drawImage(mock_prf, 195, 220, width=205, height=410, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 155, [
        (1, "Header Profil & Statistik: Menampilkan inisial avatar, nama lengkap, jalur peminatan, dan total sertifikat."),
        (2, "Peta Semangat Belajar (Heatmap): Visualisasi intensitas keaktifan koding harian siswa ala GitHub matrix."),
        (3, "Pengaturan Mode Gelap (Dark Mode): Sakelar pengubah tema kontras gelap atau terang untuk kenyamanan mata."),
        (4, "Pilihan Bahasa Aplikasi: Mendukung antarmuka Bahasa Indonesia, English, Jepang, dan Mandarin."),
        (5, "Pengingat Belajar: Notifikasi harian untuk menjaga konsistensi mempertahankan rekor daily streak."),
        (6, "Ulangi Onboarding & Logout: Mengganti preferensi jalur belajar atau keluar dari sesi akun aplikasi dengan aman.")
    ])

    draw_running_footer(c, "15")
    c.showPage()

    # ==========================================
    # PAGE 18: PORTAL WEB FASILITATOR (Hal 16)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PORTAL WEB FASILITATOR & PENILAIAN TUGAS AKHIR")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "Untuk menjamin keamanan, objektivitas evaluasi, dan kenyamanan telaah kode sumber, sistem pengujian")
    c.drawString(45, 722, "proyek akhir dipisahkan ke Portal Web Fasilitator (/facilitator/index.html) dengan sinkronisasi real-time:")

    # Mockup 16: Facilitator Web (600x420)
    mock_fac = os.path.join(ASSETS_DIR, "mockup_16_facilitator_web.png")
    if os.path.exists(mock_fac):
        c.drawImage(mock_fac, 72, 230, width=450, height=315, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 160, [
        (1, "Otentikasi PIN Khusus Fasilitator: Melindungi portal dari akses tidak berwenang dengan PIN sesi aman."),
        (2, "Antrean Pengumpulan Real-Time: Daftar submission siswa tersinkron otomatis via Firestore tanpa reload."),
        (3, "Code Inspector & Live Preview: Memeriksa struktur kode HTML, CSS, dan JS dengan frame visual interaktif."),
        (4, "Rubrik Penilaian Terstruktur: Input nilai numerik (0-100) serta umpan balik edukatif / revisi untuk siswa."),
        (5, "Penerbitan Sertifikat Otomatis: Proyek yang dinyatakan LULUS (skor >= 70) langsung menerbitkan sertifikat."),
        (6, "Push Notifikasi Status ke Siswa: Status di aplikasi mobile siswa terupdate secara instan via snapshot listener.")
    ])

    draw_running_footer(c, "16")
    c.showPage()

    # ==========================================
    # PAGE 19: PORTAL WEB VERIFIKASI SERTIFIKAT (Hal 17)
    # ==========================================
    draw_running_header(c)
    c.setFillColor(C_NAVY_DARK)
    c.setFont(FONT_BOLD_NAME, 13)
    c.drawString(45, 755, "PORTAL WEB VERIFIKASI SERTIFIKAT DIGITAL")

    c.setFont(FONT_NORMAL_NAME, 8.8)
    c.setFillColor(C_TEXT_DARK)
    c.drawString(45, 735, "CodeAcademy menyediakan Portal Verifikasi Kredensial Publik (/verify.html) untuk memastikan keaslian")
    c.drawString(45, 722, "sertifikat kelulusan siswa yang dapat divalidasi langsung oleh pihak kampus maupun perusahaan:")

    # Mockup 17: Verify Web (600x380)
    mock_ver = os.path.join(ASSETS_DIR, "mockup_17_verify_web.png")
    if os.path.exists(mock_ver):
        c.drawImage(mock_ver, 72, 240, width=450, height=285, mask='auto')

    draw_keterangan_box(c, 45, 48, 505, 165, [
        (1, "Pencarian ID Kredensial Unik: Memvalidasi nomor sertifikat (misal: CA-2026-88912) ke pusat data sertifikat."),
        (2, "Lencana Kredensial Terverifikasi: Status keabsahan resmi (Verified Authentic) bertanda centang hijau."),
        (3, "Detail Identitas Lulusan: Menampilkan nama lengkap siswa, nama kelas/kursus, dan tanggal penerbitan."),
        (4, "Informasi Skor Proyek Akhir: Mencantumkan nilai evaluasi tugas akhir dan predikat kompetensi siswa."),
        (5, "Tautan Terintegrasi Mobile: Tombol Bagikan pada aplikasi Android langsung menyertakan URL verifikasi publik."),
        (6, "Proteksi Pemalsuan Dokumen: Setiap sertifikat terikat secara kriptografis dengan akun siswa dan ID kursus.")
    ])

    draw_running_footer(c, "17")
    c.showPage()

    c.save()
    print(f"Manual Book PDF generated successfully at: {PDF_PATH}")

if __name__ == "__main__":
    build_pdf()
