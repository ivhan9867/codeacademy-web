"""
Generate crisp, high-resolution Flowchart & User Flow PNG diagrams for
Manual_Book_CodeAcademy.md, DOCX, and HTML outputs.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import shutil

OUT_DOCS_ASSETS = Path("docs/manual_book_assets")
OUT_PUBLIC_ASSETS = Path("public/docs/manual_book_assets")
OUT_DOCS_ASSETS.mkdir(parents=True, exist_ok=True)
OUT_PUBLIC_ASSETS.mkdir(parents=True, exist_ok=True)

# Palette (Royal Tech Blue + CodeAcademy Teal + Editorial Neutral)
BG = (248, 250, 252)
CARD = (255, 255, 255)
NAVY = (15, 42, 130)
BLUE = (29, 78, 216)
TEAL = (13, 148, 136)
TEAL_DARK = (16, 66, 70)
EMERALD = (16, 185, 129)
AMBER = (217, 119, 6)
VIOLET = (109, 40, 217)
RED = (220, 38, 38)
BORDER = (203, 213, 225)
TEXT = (17, 24, 39)
MUTED = (71, 85, 105)
LINE = (100, 116, 139)


def get_font(size: int, bold: bool = False):
    candidates = [
        "C:/Windows/Fonts/segoeuib.ttf" if bold else "C:/Windows/Fonts/segoeui.ttf",
        "C:/Windows/Fonts/arialbd.ttf" if bold else "C:/Windows/Fonts/arial.ttf",
    ]
    for path in candidates:
        if Path(path).exists():
            return ImageFont.truetype(path, size)
    return ImageFont.load_default()


F_TITLE = get_font(26, bold=True)
F_SUB = get_font(15, bold=False)
F_HEAD = get_font(16, bold=True)
F_BODY = get_font(13, bold=False)
F_TAG = get_font(12, bold=True)


def draw_rounded_box(draw, xy, fill, outline, radius=14, width=2):
    draw.rounded_rectangle(xy, radius=radius, fill=fill, outline=outline, width=width)


def draw_centered_text(draw, box, title, subtitle="", title_color=TEXT, sub_color=MUTED):
    x1, y1, x2, y2 = box
    cx = (x1 + x2) // 2
    lines_sub = subtitle.split("\n") if subtitle else []
    total_h = 22 + (len(lines_sub) * 18)
    start_y = y1 + ((y2 - y1) - total_h) // 2
    draw.text((cx, start_y), title, font=F_HEAD, fill=title_color, anchor="ma")
    cur_y = start_y + 24
    for ln in lines_sub:
        draw.text((cx, cur_y), ln, font=F_BODY, fill=sub_color, anchor="ma")
        cur_y += 18


def draw_diamond(draw, cx, cy, w, h, fill, outline, label, text_color=NAVY):
    pts = [(cx, cy - h // 2), (cx + w // 2, cy), (cx, cy + h // 2), (cx - w // 2, cy)]
    draw.polygon(pts, fill=fill, outline=outline)
    draw.line(pts + [pts[0]], fill=outline, width=2)
    for idx, ln in enumerate(label.split("\n")):
        offset = (idx - (len(label.split("\n")) - 1) / 2) * 18
        draw.text((cx, cy + offset), ln, font=F_TAG, fill=text_color, anchor="mm")


def draw_arrow(draw, p1, p2, color=LINE, width=3, label=None, label_offset=(0, -12)):
    draw.line([p1, p2], fill=color, width=width)
    x1, y1 = p1
    x2, y2 = p2
    import math
    ang = math.atan2(y2 - y1, x2 - x1)
    ah = 10
    p_left = (x2 - ah * math.cos(ang - 0.45), y2 - ah * math.sin(ang - 0.45))
    p_right = (x2 - ah * math.cos(ang + 0.45), y2 - ah * math.sin(ang + 0.45))
    draw.polygon([p2, p_left, p_right], fill=color)
    if label:
        mx = (x1 + x2) // 2 + label_offset[0]
        my = (y1 + y2) // 2 + label_offset[1]
        draw.text((mx, my), label, font=F_TAG, fill=color, anchor="mm")


def save_both(img: Image.Image, filename: str):
    p1 = OUT_DOCS_ASSETS / filename
    p2 = OUT_PUBLIC_ASSETS / filename
    img.save(p1, format="PNG", optimize=True)
    shutil.copy2(p1, p2)
    print(f"Generated {p1} ({img.size[0]}x{img.size[1]})")


def build_header(draw, w, title, subtitle):
    draw.rectangle([0, 0, w, 92], fill=NAVY)
    draw.rectangle([0, 88, w, 92], fill=TEAL)
    draw.text((40, 22), title, font=F_TITLE, fill=(255, 255, 255))
    draw.text((40, 56), subtitle, font=F_SUB, fill=(203, 213, 225))


def draw_stick_actor(draw, cx, cy, label, color):
    # Head
    draw.ellipse([cx - 16, cy - 42, cx + 16, cy - 10], fill=CARD, outline=color, width=3)
    # Body
    draw.line([(cx, cy - 10), (cx, cy + 28)], fill=color, width=3)
    # Arms
    draw.line([(cx - 22, cy + 4), (cx + 22, cy + 4)], fill=color, width=3)
    # Legs
    draw.line([(cx, cy + 28), (cx - 18, cy + 54)], fill=color, width=3)
    draw.line([(cx, cy + 28), (cx + 18, cy + 54)], fill=color, width=3)
    for idx, ln in enumerate(label.split("\n")):
        draw.text((cx, cy + 66 + idx * 17), ln, font=F_TAG, fill=color, anchor="ma")


def gen_flow_00_use_case():
    W, H = 1200, 840
    img = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(img)
    build_header(
        d, W,
        "USE CASE DIAGRAM: EKOSISTEM APLIKASI CODEACADEMY",
        "Pemetaan Interaksi 4 Aktor (Siswa/Tamu, Fasilitator, Super Admin, Publik) terhadap Sistem Android & Web"
    )

    # System boundary box
    d.rounded_rectangle([240, 118, 960, 805], radius=18, fill=CARD, outline=NAVY, width=2)
    d.rounded_rectangle([240, 118, 960, 160], radius=16, fill=NAVY)
    d.text((600, 139), "SISTEM CODEACADEMY (ANDROID MOBILE APP + WEB PORTAL)", font=F_HEAD, fill=(255, 255, 255), anchor="mm")

    # Left Actors: Siswa (top-left) & Publik (bottom-left)
    draw_stick_actor(d, 115, 310, "Siswa / Tamu\n(Android App)", TEAL_DARK)
    draw_stick_actor(d, 115, 675, "Publik / Rekruter\n(Web verify.html)", BLUE)

    # Right Actors: Fasilitator (top-right) & Super Admin (bottom-right)
    draw_stick_actor(d, 1085, 310, "Fasilitator\n(Web Portal)", EMERALD)
    draw_stick_actor(d, 1085, 635, "Super Admin\n(Web Panel)", VIOLET)

    # Use case ovals (x1, y1, x2, y2, label, color)
    uc_left = [
        (285, 185, 575, 240, "Autentikasi, Google Link & Onboarding", TEAL),
        (285, 258, 575, 313, "Baca 156 Modul & Placement Test", TEAL),
        (285, 331, 575, 386, "Playground (Web Studio & JS Console)", TEAL),
        (285, 404, 575, 459, "Tanya AI Tutor Codi (4 Bahasa)", TEAL),
        (285, 477, 575, 532, "Kuis Bab, Evaluasi & Proyek Akhir", TEAL),
        (285, 550, 575, 605, "Forum, Leaderboard & Profile Studio", TEAL),
        (285, 655, 575, 715, "Verifikasi Sertifikat (ID CC-...)", BLUE),
    ]
    uc_right = [
        (635, 195, 925, 255, "Review Proyek Akhir (Rubrik 40/60)", EMERALD),
        (635, 275, 925, 335, "Moderasi Forum & Pin Terverifikasi", EMERALD),
        (635, 355, 925, 415, "Terbitkan Pengumuman & Profil Staf", EMERALD),
        (635, 465, 925, 525, "Kelola & Promosi Tim Fasilitator", VIOLET),
        (635, 545, 925, 605, "Ban / Unban Akun Siswa Cloud", VIOLET),
        (635, 625, 925, 685, "Audit Sertifikat & Export CSV", VIOLET),
        (635, 705, 925, 765, "Analitik Rasio Lulus & Klasemen", VIOLET),
    ]

    for (x1, y1, x2, y2, lbl, col) in uc_left:
        d.ellipse([x1, y1, x2, y2], fill=(240, 253, 250) if col == TEAL else (239, 246, 255), outline=col, width=2)
        d.text(((x1 + x2) // 2, (y1 + y2) // 2), lbl, font=F_TAG, fill=TEXT, anchor="mm")
        if col == TEAL:
            d.line([(145, 315), (x1, (y1 + y2) // 2)], fill=TEAL, width=2)
        else:
            d.line([(145, 675), (x1, (y1 + y2) // 2)], fill=BLUE, width=2)

    for (x1, y1, x2, y2, lbl, col) in uc_right:
        d.ellipse([x1, y1, x2, y2], fill=(236, 253, 245) if col == EMERALD else (237, 233, 254), outline=col, width=2)
        d.text(((x1 + x2) // 2, (y1 + y2) // 2), lbl, font=F_TAG, fill=TEXT, anchor="mm")
        if col == EMERALD:
            d.line([(1055, 315), (x2, (y1 + y2) // 2)], fill=EMERALD, width=2)
        else:
            d.line([(1055, 635), (x2, (y1 + y2) // 2)], fill=VIOLET, width=2)

    # Inheritance arrow: Super Admin inherits Fasilitator
    draw_arrow(d, (1085, 575), (1085, 410), color=VIOLET, width=3, label="extends", label_offset=(36, 0))

    save_both(img, "flow_00_use_case_diagram.png")


def gen_flow_01():
    W, H = 1200, 820
    img = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(img)
    build_header(
        d, W,
        "FLOWCHART 1: NAVIGASI & GATEKEEPING APLIKASI MOBILE",
        "Alur Splash Screen -> Ban Monitor -> Autentikasi -> Onboarding 4 Langkah -> Main Dashboard"
    )

    # Nodes
    b_splash = (430, 120, 770, 190)
    draw_rounded_box(d, b_splash, TEAL_DARK, TEAL, radius=18)
    draw_centered_text(d, b_splash, "Splash Screen & Init", "Load Catalog JSON + SessionManager", (255, 255, 255), (204, 251, 241))

    draw_arrow(d, (600, 190), (600, 230))
    draw_diamond(d, 600, 285, 260, 110, (254, 243, 199), AMBER, "Akun Dibekukan?\n(isBanned == true)", (146, 64, 14))

    # Banned branch (right)
    b_banned = (850, 240, 1150, 330)
    draw_rounded_box(d, b_banned, (254, 226, 226), RED)
    draw_centered_text(d, b_banned, "AccountBannedScreen", "Tampil Alasan Ban, Cek Status,\nAjukan Banding / Logout", RED, (127, 29, 29))
    draw_arrow(d, (730, 285), (850, 285), color=RED, label="Ya", label_offset=(0, -14))

    # Logged in decision
    draw_arrow(d, (600, 340), (600, 385), label="Tidak", label_offset=(28, 0))
    draw_diamond(d, 600, 440, 260, 110, (219, 234, 254), BLUE, "Sudah Login?\n(Auth / Guest)", NAVY)

    # Login branch (left)
    b_login = (50, 395, 360, 485)
    draw_rounded_box(d, b_login, CARD, BLUE)
    draw_centered_text(d, b_login, "Login / Register / Guest", "Email, Google Credential Manager,\nForgot Password & Guest Mode", NAVY)
    draw_arrow(d, (470, 440), (360, 440), color=BLUE, label="Belum", label_offset=(0, -14))

    # Onboarded decision
    draw_arrow(d, (600, 495), (600, 540), label="Sudah", label_offset=(30, 0))
    draw_diamond(d, 600, 595, 260, 110, (237, 233, 254), VIOLET, "Sudah Onboarding?\n(isOnboarded == true)", VIOLET)

    # Onboarding branch (right)
    b_onb = (850, 545, 1150, 645)
    draw_rounded_box(d, b_onb, CARD, VIOLET)
    draw_centered_text(d, b_onb, "Onboarding 4 Langkah", "1. Welcome  2. Code Preview\n3. Roadmap  4. Plan Summary", VIOLET)
    draw_arrow(d, (730, 595), (850, 595), color=VIOLET, label="Belum", label_offset=(0, -14))

    # Main Dashboard
    draw_arrow(d, (600, 650), (600, 695), label="Ya", label_offset=(20, 0))
    b_main = (140, 695, 1060, 785)
    draw_rounded_box(d, b_main, (236, 253, 245), EMERALD, radius=16, width=3)
    draw_centered_text(
        d, b_main,
        "MAIN DASHBOARD (5 Tab BottomNav + Navigation Drawer)",
        "Beranda  |  Kursus (Katalog & Detail)  |  Progres  |  Komunitas (Forum & Leaderboard)  |  Profil & Bahasa",
        (6, 78, 59), (4, 120, 87)
    )
    save_both(img, "flow_01_app_navigation_architecture.png")


def gen_flow_02():
    W, H = 1200, 840
    img = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(img)
    build_header(
        d, W,
        "FLOWCHART 2: SIKLUS BELAJAR, EVALUASI & PENERBITAN SERTIFIKAT",
        "Dari Pemilihan Kelas, Placement Test, Materi + Playground + AI Tutor hingga Review Fasilitator"
    )

    # Top row
    b1 = (50, 130, 310, 215)
    draw_rounded_box(d, b1, CARD, BLUE)
    draw_centered_text(d, b1, "1. Pilih Jalur & Kelas", "HTML/CSS, CSS Layout,\natau JS Fundamentals", NAVY)

    draw_arrow(d, (310, 172), (370, 172))
    draw_diamond(d, 495, 172, 250, 105, (254, 243, 199), AMBER, "Ikut Placement Test?\n(Opsional Fast-Track)", (146, 64, 14))

    b_ft = (680, 130, 960, 215)
    draw_rounded_box(d, b_ft, (236, 253, 245), EMERALD)
    draw_centered_text(d, b_ft, "Fast-Track & Bonus XP", "Bagian awal terbuka otomatis\njika skor >= ambang batas", (6, 78, 59))
    draw_arrow(d, (620, 172), (680, 172), color=EMERALD, label="Lulus", label_offset=(0, -14))

    # Row 2: Learning Loop
    draw_arrow(d, (495, 224), (495, 285), label="Mulai / Lewati", label_offset=(55, 0))
    b_learn = (170, 285, 820, 385)
    draw_rounded_box(d, b_learn, (240, 253, 250), TEAL, width=3)
    draw_centered_text(
        d, b_learn,
        "2. Siklus Belajar Interaktif (+XP & Daily Quest)",
        "Materi Reader (Markdown + Bookmark)  <->  Playground (Web Studio & JS Console)\n<->  AI Tutor Codi (Context-Aware Gemini AI dalam 4 Bahasa)",
        TEAL_DARK, MUTED
    )

    # Row 3: Section Quiz
    draw_arrow(d, (495, 385), (495, 435))
    b_sq = (250, 435, 740, 520)
    draw_rounded_box(d, b_sq, CARD, VIOLET)
    draw_centered_text(d, b_sq, "3. Section Quiz (Kuis Bab + Anti-Curang ON_STOP)", "Wajib lulus per bab untuk membuka bab berikutnya", VIOLET)

    # Row 4: Final Quiz -> Project -> Review
    draw_arrow(d, (495, 520), (495, 565))
    b_fq = (60, 565, 350, 655)
    draw_rounded_box(d, b_fq, CARD, BLUE)
    draw_centered_text(d, b_fq, "4. Kuis Evaluasi Akhir", "Skor >= 70% membuka\nProyek Akhir (40% Bobot)", NAVY)

    b_proj = (420, 565, 740, 655)
    draw_rounded_box(d, b_proj, CARD, TEAL)
    draw_centered_text(d, b_proj, "5. Final Project Submission", "Kirim kode Playground, URL,\ncatatan & rubrik ke Firestore", TEAL_DARK)

    draw_arrow(d, (350, 610), (420, 610))
    draw_arrow(d, (740, 610), (805, 610))

    draw_diamond(d, 945, 610, 270, 120, (254, 243, 199), AMBER, "Review Fasilitator\n(60% Proyek)\nLulus >= 70?", (146, 64, 14))

    # Revision loop back
    b_rev = (805, 435, 1140, 515)
    draw_rounded_box(d, b_rev, (254, 226, 226), RED)
    draw_centered_text(d, b_rev, "Status: REVISION_REQUESTED", "Notifikasi feedback masuk ->\nPerbaiki & Kirim Ulang", RED, (127, 29, 29))
    draw_arrow(d, (945, 550), (945, 515), color=RED, label="Revisi", label_offset=(28, 0))
    draw_arrow(d, (805, 475), (740, 565), color=RED)

    # Approved -> Certificate
    draw_arrow(d, (945, 670), (945, 715), color=EMERALD, label="Approved", label_offset=(38, 0))
    b_cert = (250, 715, 1120, 800)
    draw_rounded_box(d, b_cert, (236, 253, 245), EMERALD, width=3)
    draw_centered_text(
        d, b_cert,
        "6. SERTIFIKAT DIGITAL TERBIT (ID: CC-yyyyMMxxxx-xxxxxx)",
        "Tersimpan di Room & Firestore -> Dapat dilihat di Aplikasi & diverifikasi publik via verify.html",
        (6, 78, 59), (4, 120, 87)
    )
    save_both(img, "flow_02_learning_evaluation_loop.png")


def gen_flow_03():
    W, H = 1200, 740
    img = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(img)
    build_header(
        d, W,
        "USER FLOW: PERJALANAN LENGKAP PENGGUNA DI DALAM APLIKASI",
        "7 Tahap Pengalaman Pengguna (End-to-End Mobile Journey) dari Masuk hingga Sertifikasi"
    )

    steps = [
        ("01", "Akses & Akun", "Login Email / Google\natau Guest Anonim\n+ Ban Monitor", BLUE),
        ("02", "Onboarding", "Welcome, Live Code,\nRoadmap & Target\nWaktu Harian", VIOLET),
        ("03", "Beranda (Home)", "Daily Streak, Misi,\nDaily Challenge\n& Lanjut Belajar", TEAL),
        ("04", "Belajar & Praktik", "156 Modul Markdown,\nWeb Studio, JS Console\n& AI Tutor Codi", TEAL_DARK),
        ("05", "Evaluasi & Proyek", "Placement Test,\nSection Quiz, Final\nQuiz & Submission", AMBER),
        ("06", "Komunitas", "Forum Real-Time,\nLeaderboard & Profil\nTiered (Admin/Fasil)", EMERALD),
        ("07", "Profil & Sertifikat", "Heatmap Aktivitas,\n4 Bahasa (ID/EN/JA/ZH)\n& Sertifikat CC-...", NAVY),
    ]

    # Top row: 4 cards, Bottom row: 3 cards
    card_w, card_h = 250, 190
    top_y = 140
    bot_y = 430
    gap = 35
    start_x_top = (W - (4 * card_w + 3 * gap)) // 2

    coords = []
    for i in range(4):
        x1 = start_x_top + i * (card_w + gap)
        coords.append((x1, top_y, x1 + card_w, top_y + card_h))

    start_x_bot = (W - (3 * card_w + 2 * gap)) // 2
    for i in range(3):
        x1 = start_x_bot + i * (card_w + gap)
        coords.append((x1, bot_y, x1 + card_w, bot_y + card_h))

    for idx, ((num, title, desc, col), box) in enumerate(zip(steps, coords)):
        draw_rounded_box(d, box, CARD, col, radius=16, width=2)
        # Badge number
        bx1, by1, bx2, by2 = box
        d.rounded_rectangle([bx1 + 14, by1 + 14, bx1 + 54, by1 + 42], radius=8, fill=col)
        d.text((bx1 + 34, by1 + 28), num, font=F_TAG, fill=(255, 255, 255), anchor="mm")
        d.text((bx1 + 66, by1 + 28), title, font=F_HEAD, fill=col, anchor="lm")
        d.line([(bx1 + 14, by1 + 52), (bx2 - 14, by1 + 52)], fill=BORDER, width=1)
        cur_y = by1 + 68
        for ln in desc.split("\n"):
            d.text((bx1 + 16, cur_y), ln, font=F_BODY, fill=TEXT)
            cur_y += 22

    # Arrows
    for i in range(3):
        b_cur = coords[i]
        b_nxt = coords[i + 1]
        draw_arrow(d, (b_cur[2], (b_cur[1] + b_cur[3]) // 2), (b_nxt[0], (b_nxt[1] + b_nxt[3]) // 2), color=LINE)

    # Connector from Step 4 to Step 5
    b4 = coords[3]
    b5 = coords[4]
    p_mid_1 = ((b4[0] + b4[2]) // 2, b4[3])
    p_mid_2 = ((b4[0] + b4[2]) // 2, b4[3] + 45)
    p_mid_3 = ((b5[0] + b5[2]) // 2, b4[3] + 45)
    p_mid_4 = ((b5[0] + b5[2]) // 2, b5[1])
    d.line([p_mid_1, p_mid_2, p_mid_3], fill=LINE, width=3)
    draw_arrow(d, p_mid_3, p_mid_4, color=LINE)

    for i in range(4, 6):
        b_cur = coords[i]
        b_nxt = coords[i + 1]
        draw_arrow(d, (b_cur[2], (b_cur[1] + b_cur[3]) // 2), (b_nxt[0], (b_nxt[1] + b_nxt[3]) // 2), color=LINE)

    # Footer note
    d.text(
        (W // 2, 685),
        "Seluruh layar antarmuka & notifikasi mendukung perpindahan bahasa instan: Bahasa Indonesia, English, Japanese, dan Chinese.",
        font=F_SUB, fill=MUTED, anchor="mm"
    )
    save_both(img, "flow_03_user_flow_student.png")


def gen_flow_04():
    W, H = 1200, 760
    img = Image.new("RGB", (W, H), BG)
    d = ImageDraw.Draw(img)
    build_header(
        d, W,
        "FLOWCHART 4: ARSITEKTUR DATA, ISOLASI AKUN & HIERARKI PERAN",
        "Sinkronisasi Offline-First Room v16 <-> Cloud Firestore & Tiering Profil Publik"
    )

    # Left: Local Android Storage
    b_local = (50, 130, 540, 410)
    draw_rounded_box(d, b_local, CARD, TEAL, radius=16, width=2)
    d.rounded_rectangle([50, 130, 540, 178], radius=14, fill=TEAL)
    d.text((295, 154), "LOKAL PERANGKAT (ANDROID OFFLINE-FIRST)", font=F_HEAD, fill=(255, 255, 255), anchor="mm")

    l1 = (75, 198, 515, 260)
    draw_rounded_box(d, l1, (240, 253, 250), TEAL)
    draw_centered_text(d, l1, "Room Database (AppDatabase v16)", "UserProfile, ModuleProgress, Bookmarks, Drafts,\nSectionQuiz, ProjectSubmission, ForumPost")

    l2 = (75, 278, 515, 336)
    draw_rounded_box(d, l2, (240, 253, 250), TEAL)
    draw_centered_text(d, l2, "DataStore (SessionManager)", "Active Account ID, AppLanguage (id/en/ja/zh),\nDaily Micro Challenge & Coach Marks")

    l3 = (75, 352, 515, 396)
    draw_rounded_box(d, l3, (236, 253, 245), EMERALD)
    draw_centered_text(d, l3, "Isolasi Multi-Akun (accountId)", "Berpindah akun tidak mencampur progres")

    # Right: Cloud Firestore
    b_cloud = (660, 130, 1150, 410)
    draw_rounded_box(d, b_cloud, CARD, BLUE, radius=16, width=2)
    d.rounded_rectangle([660, 130, 1150, 178], radius=14, fill=BLUE)
    d.text((905, 154), "CLOUD BACKEND (FIREBASE FIRESTORE)", font=F_HEAD, fill=(255, 255, 255), anchor="mm")

    c1 = (685, 198, 1125, 260)
    draw_rounded_box(d, c1, (239, 246, 255), BLUE)
    draw_centered_text(d, c1, "Koleksi users & facilitators", "Profil Publik, accessRole, Ban Status,\nStreak, XP & Custom Showcase")

    c2 = (685, 278, 1125, 336)
    draw_rounded_box(d, c2, (239, 246, 255), BLUE)
    draw_centered_text(d, c2, "forum_posts & announcements", "Diskusi Real-Time, authorAccessRole,\nJawaban Terverifikasi & Pengumuman")

    c3 = (685, 352, 1125, 396)
    draw_rounded_box(d, c3, (239, 246, 255), BLUE)
    draw_centered_text(d, c3, "project_submissions & certificates", "Antrean Review 40/60 & ID CC-...")

    # Bidirectional sync arrows
    draw_arrow(d, (540, 245), (660, 245), color=NAVY, label="Sync", label_offset=(0, -14))
    draw_arrow(d, (660, 305), (540, 305), color=TEAL, label="Realtime", label_offset=(0, -14))

    # Bottom: Role Tiering Showcase
    b_roles = (50, 455, 1150, 710)
    draw_rounded_box(d, b_roles, CARD, VIOLET, radius=16, width=2)
    d.text((600, 482), "HIERARKI IDENTITAS & TAMPILAN PROFIL PUBLIK DI APLIKASI", font=F_HEAD, fill=NAVY, anchor="mm")

    r1 = (75, 515, 405, 685)
    draw_rounded_box(d, r1, (240, 253, 250), TEAL)
    draw_centered_text(
        d, r1,
        "PESERTA (SISWA / TAMU)",
        "Banner & Avatar Kustom\n(Oceanic, Midnight, Lime, dll)\nBadge Level XP (Lv. 1 - 10)\nPin Lencana & Skill Tags",
        TEAL_DARK, TEXT
    )

    r2 = (435, 515, 765, 685)
    draw_rounded_box(d, r2, (236, 253, 245), EMERALD, width=2)
    draw_centered_text(
        d, r2,
        "FASILITATOR (accessRole: facilitator)",
        "Banner Emerald-Gold Prestige\nAvatar Bingkai Emas Berdenyut\nBadge: FASILITATOR / MENTOR RESMI\nHak: Review Proyek & Pin Forum",
        (6, 78, 59), TEXT
    )

    r3 = (795, 515, 1125, 685)
    draw_rounded_box(d, r3, (237, 233, 254), VIOLET, width=3)
    draw_centered_text(
        d, r3,
        "SUPER ADMIN (super_admin)",
        "Banner Cosmic Aurora + Light Sweep\nAvatar Halo Berputar 360 + Aura Pulse\nBadge: SYSTEM ARCHITECT\nHak: Promosi Tim, Ban & Audit CSV",
        VIOLET, TEXT
    )

    save_both(img, "flow_04_auth_sync_and_roles.png")


if __name__ == "__main__":
    gen_flow_00_use_case()
    gen_flow_01()
    gen_flow_02()
    gen_flow_03()
    gen_flow_04()
    print("All 5 flow & use case diagrams generated successfully!")
