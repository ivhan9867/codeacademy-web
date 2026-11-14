import os
from PIL import Image, ImageDraw, ImageFont

ASSETS_DIR = r"docs\manual_book_assets"
DRAWABLE_DIR = r"app\src\main\res\drawable"
os.makedirs(ASSETS_DIR, exist_ok=True)

FONT_REG = r"C:\Windows\Fonts\segoeui.ttf"
FONT_BOLD = r"C:\Windows\Fonts\segoeuib.ttf"
FONT_CODE = r"C:\Windows\Fonts\consola.ttf"

def get_font(size, bold=False, code=False):
    try:
        p = FONT_CODE if code else (FONT_BOLD if bold else FONT_REG)
        return ImageFont.truetype(p, size)
    except Exception:
        return ImageFont.load_default()

def create_base_phone(w=320, h=640, bg="#1A1816", status_dark=False):
    img = Image.new("RGBA", (w + 24, h + 30), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    # Chassis
    d.rounded_rectangle([6, 6, w + 18, h + 24], radius=28, fill="#1B1E22", outline="#2F3640", width=3)
    # Screen
    sbox = [12, 12, w + 12, h + 18]
    d.rounded_rectangle(sbox, radius=22, fill=bg)
    # Notch / Camera
    d.ellipse([w // 2 + 7, 16, w // 2 + 17, 26], fill="#0A0C0E")
    # Status bar
    scol = "#24211D" if status_dark else "#FFFFFF"
    d.text((24, 18), "12:10", fill=scol, font=get_font(10, bold=True))
    d.text((w - 40, 18), "5G  100%", fill=scol, font=get_font(9))
    return img, d, sbox

def draw_callout_badge(d, x, y, num):
    r = 11
    d.ellipse([x - r, y - r, x + r, y + r], fill="#E74C3C", outline="#FFFFFF", width=2)
    d.text((x - 4, y - 7), str(num), fill="#FFFFFF", font=get_font(11, bold=True))

def draw_bottom_nav(d, sbox, active_idx=0, is_dark=True):
    sx1, sy1, sx2, sy2 = sbox
    nh = 46
    ny = sy2 - nh
    bg = "#262320" if is_dark else "#FFFFFF"
    d.rectangle([sx1, ny, sx2, sy2], fill=bg)
    d.line([sx1, ny, sx2, ny], fill="#3B3632" if is_dark else "#E2DDD5", width=1)
    tabs = ["Beranda", "Kelas", "Progres", "Komunitas", "Profil"]
    tw = (sx2 - sx1) / 5
    for i, t in enumerate(tabs):
        tx = sx1 + i * tw + tw / 2
        is_act = (i == active_idx)
        col = "#389B9E" if is_act else ("#8C847A" if is_dark else "#6B655D")
        if is_act:
            d.rounded_rectangle([tx - 16, ny + 5, tx + 16, ny + 22], radius=8, fill="#1A6368")
            d.ellipse([tx - 3, ny + 10, tx + 3, ny + 16], fill="#389B9E")
        else:
            d.ellipse([tx - 3, ny + 12, tx + 3, ny + 18], fill=col)
        d.text((tx - 13, ny + 27), t[:6], fill=col, font=get_font(8, bold=is_act))

# ----------------- 1. SPLASH SCREEN -----------------
def gen_splash():
    img, d, s = create_base_phone(bg="#104246")
    # Logo
    logo_p = os.path.join(DRAWABLE_DIR, "ic_codeacademy_logo.webp")
    if os.path.exists(logo_p):
        try:
            im_logo = Image.open(logo_p).convert("RGBA").resize((90, 90))
            img.paste(im_logo, (s[0] + 115, s[1] + 180), im_logo)
        except Exception:
            d.rectangle([s[0] + 115, s[1] + 180, s[0] + 205, s[1] + 270], fill="#1A6368")
    else:
        d.rectangle([s[0] + 115, s[1] + 180, s[0] + 205, s[1] + 270], fill="#1A6368")
    
    d.text((s[0] + 70, s[1] + 290), "CodeAcademy", fill="#FFFFFF", font=get_font(22, bold=True))
    d.text((s[0] + 55, s[1] + 325), "Mobile-First Coding Platform", fill="#389B9E", font=get_font(12))
    
    # Progress indicator
    d.rounded_rectangle([s[0] + 70, s[1] + 460, s[0] + 250, s[1] + 466], radius=3, fill="#1A6368")
    d.rounded_rectangle([s[0] + 70, s[1] + 460, s[0] + 180, s[1] + 466], radius=3, fill="#DF7B56")
    d.text((s[0] + 80, s[1] + 480), "Menyiapkan kurikulum belajar...", fill="#A0C8C8", font=get_font(10))
    img.save(os.path.join(ASSETS_DIR, "mockup_01_splash.png"))

# ----------------- 2. INSTALL SCREEN -----------------
def gen_install():
    img, d, s = create_base_phone(bg="#F1ECE4", status_dark=True)
    # Android Package Installer Dialog Card
    cx1, cy1, cx2, cy2 = s[0] + 20, s[1] + 160, s[2] - 20, s[1] + 440
    d.rounded_rectangle([cx1, cy1, cx2, cy2], radius=16, fill="#FFFFFF", outline="#E2DDD5", width=2)
    
    # App Icon
    d.rounded_rectangle([cx1 + 20, cy1 + 25, cx1 + 75, cy1 + 80], radius=12, fill="#104246")
    d.text((cx1 + 32, cy1 + 38), "</>", fill="#389B9E", font=get_font(18, bold=True))
    
    d.text((cx1 + 88, cy1 + 30), "CodeAcademy", fill="#24211D", font=get_font(15, bold=True))
    d.text((cx1 + 88, cy1 + 52), "Versi 1.0.0 (Release)", fill="#6B655D", font=get_font(10))
    
    d.line([cx1 + 20, cy1 + 95, cx2 - 20, cy1 + 95], fill="#ECE7DF", width=1)
    d.text((cx1 + 20, cy1 + 115), "Apakah Anda ingin menginstal", fill="#24211D", font=get_font(12, bold=True))
    d.text((cx1 + 20, cy1 + 135), "aplikasi ini?", fill="#24211D", font=get_font(12, bold=True))
    d.text((cx1 + 20, cy1 + 165), "Aplikasi tidak memerlukan akses", fill="#6B655D", font=get_font(10))
    d.text((cx1 + 20, cy1 + 182), "khusus apa pun.", fill="#6B655D", font=get_font(10))
    
    # Buttons
    d.rounded_rectangle([cx2 - 170, cy2 - 50, cx2 - 95, cy2 - 16], radius=8, fill="#ECE7DF")
    d.text((cx2 - 150, cy2 - 40), "BATAL", fill="#6B655D", font=get_font(10, bold=True))
    
    d.rounded_rectangle([cx2 - 85, cy2 - 50, cx2 - 15, cy2 - 16], radius=8, fill="#1A6368")
    d.text((cx2 - 68, cy2 - 40), "INSTAL", fill="#FFFFFF", font=get_font(10, bold=True))
    
    # Callout on INSTAL button
    draw_callout_badge(d, cx2 - 15, cy2 - 50, 1)
    img.save(os.path.join(ASSETS_DIR, "mockup_02_install.png"))

# ----------------- 3. LOGIN SCREEN -----------------
def gen_login():
    img, d, s = create_base_phone(bg="#FAF7F2", status_dark=True)
    # Header
    d.text((s[0] + 25, s[1] + 45), "CodeAcademy", fill="#1A6368", font=get_font(13, bold=True))
    d.text((s[0] + 25, s[1] + 75), "Lanjutkan progres", fill="#24211D", font=get_font(18, bold=True))
    d.text((s[0] + 25, s[1] + 100), "belajarmu.", fill="#DF7B56", font=get_font(18, bold=True))
    d.text((s[0] + 25, s[1] + 128), "Masuk ke akunmu untuk melanjutkan materi", fill="#6B655D", font=get_font(10))
    
    # Email field
    d.text((s[0] + 25, s[1] + 165), "Email", fill="#24211D", font=get_font(11, bold=True))
    d.rounded_rectangle([s[0] + 25, s[1] + 185, s[2] - 25, s[1] + 225], radius=8, fill="#FFFFFF", outline="#E2DDD5", width=1)
    d.text((s[0] + 38, s[1] + 197), "ahmad@kampus.ac.id", fill="#24211D", font=get_font(11))
    draw_callout_badge(d, s[2] - 25, s[1] + 185, 1)
    
    # Password field
    d.text((s[0] + 25, s[1] + 245), "Kata Sandi", fill="#24211D", font=get_font(11, bold=True))
    d.rounded_rectangle([s[0] + 25, s[1] + 265, s[2] - 25, s[1] + 305], radius=8, fill="#FFFFFF", outline="#E2DDD5", width=1)
    d.text((s[0] + 38, s[1] + 279), "••••••••••••", fill="#24211D", font=get_font(11))
    
    # Forgot Password link
    d.text((s[2] - 125, s[1] + 318), "Lupa Kata Sandi?", fill="#1A6368", font=get_font(10, bold=True))
    draw_callout_badge(d, s[2] - 25, s[1] + 318, 2)
    
    # Login button
    d.rounded_rectangle([s[0] + 25, s[1] + 355, s[2] - 25, s[1] + 400], radius=10, fill="#1A6368")
    d.text((s[0] + 115, s[1] + 370), "Masuk ke Akun", fill="#FFFFFF", font=get_font(12, bold=True))
    draw_callout_badge(d, s[2] - 25, s[1] + 375, 3)
    
    # Divider or Google
    d.line([s[0] + 25, s[1] + 430, s[2] - 25, s[1] + 430], fill="#E2DDD5", width=1)
    d.text((s[0] + 145, s[1] + 422), " atau ", fill="#6B655D", font=get_font(9))
    
    # Register footer
    d.text((s[0] + 65, s[1] + 550), "Belum punya akun?", fill="#6B655D", font=get_font(11))
    d.text((s[0] + 175, s[1] + 550), "Daftar Sekarang", fill="#DF7B56", font=get_font(11, bold=True))
    draw_callout_badge(d, s[0] + 255, s[1] + 550, 4)
    
    img.save(os.path.join(ASSETS_DIR, "mockup_03_login.png"))

# ----------------- 3B. REGISTER SCREEN -----------------
def gen_register():
    img, d, s = create_base_phone(bg="#FAF7F2", status_dark=True)
    d.text((s[0] + 25, s[1] + 45), "CodeAcademy", fill="#1A6368", font=get_font(13, bold=True))
    d.text((s[0] + 25, s[1] + 75), "Mulai perjalanan", fill="#24211D", font=get_font(18, bold=True))
    d.text((s[0] + 25, s[1] + 100), "codingmu.", fill="#DF7B56", font=get_font(18, bold=True))
    
    fields = [
        ("Nama Lengkap", "Ahmad Fauzi"),
        ("Email", "ahmad@kampus.ac.id"),
        ("Kata Sandi", "••••••••"),
        ("Konfirmasi Kata Sandi", "••••••••")
    ]
    cur_y = s[1] + 135
    for lbl, val in fields:
        d.text((s[0] + 25, cur_y), lbl, fill="#24211D", font=get_font(10, bold=True))
        d.rounded_rectangle([s[0] + 25, cur_y + 16, s[2] - 25, cur_y + 50], radius=8, fill="#FFFFFF", outline="#E2DDD5", width=1)
        d.text((s[0] + 36, cur_y + 26), val, fill="#24211D", font=get_font(10))
        cur_y += 62
        
    d.rounded_rectangle([s[0] + 25, cur_y + 15, s[2] - 25, cur_y + 58], radius=10, fill="#1A6368")
    d.text((s[0] + 120, cur_y + 28), "Daftar Akun", fill="#FFFFFF", font=get_font(12, bold=True))
    
    d.text((s[0] + 75, s[1] + 575), "Sudah punya akun?", fill="#6B655D", font=get_font(10))
    d.text((s[0] + 175, s[1] + 575), "Masuk", fill="#1A6368", font=get_font(10, bold=True))
    img.save(os.path.join(ASSETS_DIR, "mockup_03b_register.png"))

# ----------------- 4. ONBOARDING -----------------
def gen_onboarding():
    img, d, s = create_base_phone(bg="#1A1816")
    d.text((s[0] + 25, s[1] + 45), "LANGKAH 2 DARI 5", fill="#DF7B56", font=get_font(9, bold=True))
    d.text((s[0] + 25, s[1] + 65), "Pilih Jalur Belajar", fill="#FFFFFF", font=get_font(18, bold=True))
    d.text((s[0] + 25, s[1] + 92), "Pilih fokus utama yang ingin kamu kuasai pertama.", fill="#8C847A", font=get_font(10))
    
    tracks = [
        ("Front-End Web Development", "HTML5 Semantik, CSS3, Flexbox & Grid", True),
        ("Fondasi Web (106 Modul)", "Struktur Dokumen, Box Model & Styling", False),
        ("CSS Layout Modern (24 Modul)", "Flexbox 1D, CSS Grid 2D & Responsif", False),
        ("JavaScript Fundamentals (26 Modul)", "Logika ES6, Array/Object & DOM Event", False),
    ]
    ty = s[1] + 130
    for title, desc, sel in tracks:
        bg_col = "#262320" if not sel else "#1A6368"
        b_col = "#389B9E" if sel else "#3B3632"
        d.rounded_rectangle([s[0] + 20, ty, s[2] - 20, ty + 68], radius=12, fill=bg_col, outline=b_col, width=2 if sel else 1)
        d.text((s[0] + 35, ty + 15), title, fill="#FFFFFF", font=get_font(12, bold=True))
        d.text((s[0] + 35, ty + 38), desc, fill="#8C847A" if not sel else "#D0EBEB", font=get_font(9))
        if sel:
            d.ellipse([s[2] - 50, ty + 25, s[2] - 34, ty + 41], fill="#389B9E")
        ty += 80
        
    d.rounded_rectangle([s[0] + 20, s[1] + 530, s[2] - 20, s[1] + 575], radius=10, fill="#DF7B56")
    d.text((s[0] + 125, s[1] + 545), "Lanjutkan", fill="#FFFFFF", font=get_font(12, bold=True))
    img.save(os.path.join(ASSETS_DIR, "mockup_04_onboarding.png"))

# ----------------- 5. HOME SCREEN -----------------
def gen_home():
    img, d, s = create_base_phone(bg="#1A1816")
    # Header bar
    d.text((s[0] + 20, s[1] + 40), "☰", fill="#FFFFFF", font=get_font(18, bold=True))
    draw_callout_badge(d, s[0] + 38, s[1] + 40, 1)
    
    d.text((s[0] + 55, s[1] + 38), "Halo, Ahmad!", fill="#FFFFFF", font=get_font(14, bold=True))
    d.text((s[0] + 55, s[1] + 56), "Semangat belajar coding hari ini", fill="#8C847A", font=get_font(9))
    
    # Streak Pill
    d.rounded_rectangle([s[2] - 95, s[1] + 40, s[2] - 18, s[1] + 66], radius=12, fill="#3A2216", outline="#DF7B56")
    d.text((s[2] - 88, s[1] + 46), "🔥 7 Hari", fill="#DF7B56", font=get_font(10, bold=True))
    draw_callout_badge(d, s[2] - 18, s[1] + 40, 2)
    
    # Carousel Hero Banner
    banner_box = [s[0] + 18, s[1] + 85, s[2] - 18, s[1] + 185]
    d.rounded_rectangle(banner_box, radius=14, fill="#104246", outline="#389B9E", width=1)
    d.text((banner_box[0] + 16, banner_box[1] + 15), "MOBILE-FIRST CODING", fill="#DF7B56", font=get_font(8, bold=True))
    d.text((banner_box[0] + 16, banner_box[1] + 32), "Belajar Coding Praktis", fill="#FFFFFF", font=get_font(13, bold=True))
    d.text((banner_box[0] + 16, banner_box[1] + 50), "di Smartphone", fill="#FFFFFF", font=get_font(13, bold=True))
    d.rounded_rectangle([banner_box[0] + 16, banner_box[1] + 72, banner_box[0] + 105, banner_box[1] + 92], radius=6, fill="#DF7B56")
    d.text((banner_box[0] + 28, banner_box[1] + 76), "Mulai Belajar", fill="#FFFFFF", font=get_font(9, bold=True))
    draw_callout_badge(d, banner_box[1] + 120, banner_box[1] + 15, 3)
    
    # Continue Learning Card
    d.text((s[0] + 20, s[1] + 202), "Lanjutkan Belajar", fill="#FFFFFF", font=get_font(12, bold=True))
    c_box = [s[0] + 18, s[1] + 222, s[2] - 18, s[1] + 295]
    d.rounded_rectangle(c_box, radius=12, fill="#262320", outline="#3B3632", width=1)
    d.text((c_box[0] + 16, c_box[1] + 12), "Fondasi Web: HTML & CSS", fill="#FFFFFF", font=get_font(11, bold=True))
    d.text((c_box[0] + 16, c_box[1] + 30), "Modul 5 dari 8 • 60% Selesai", fill="#8C847A", font=get_font(9))
    d.rounded_rectangle([c_box[0] + 16, c_box[1] + 50, c_box[1] + 180, c_box[1] + 56], radius=3, fill="#3B3632")
    d.rounded_rectangle([c_box[0] + 16, c_box[1] + 50, c_box[0] + 120, c_box[1] + 56], radius=3, fill="#389B9E")
    d.rounded_rectangle([c_box[2] - 70, c_box[1] + 25, c_box[2] - 12, c_box[1] + 52], radius=8, fill="#1A6368")
    d.text((c_box[2] - 58, c_box[1] + 32), "Lanjut", fill="#FFFFFF", font=get_font(9, bold=True))
    draw_callout_badge(d, c_box[2] - 12, c_box[1] + 25, 4)
    
    # Daily Quests
    d.text((s[0] + 20, s[1] + 312), "Misi Harian", fill="#FFFFFF", font=get_font(12, bold=True))
    q_box = [s[0] + 18, s[1] + 332, s[2] - 18, s[1] + 415]
    d.rounded_rectangle(q_box, radius=12, fill="#262320", outline="#3B3632")
    d.text((q_box[0] + 14, q_box[1] + 14), "✓ Selesaikan 1 Modul", fill="#389B9E", font=get_font(10, bold=True))
    d.text((q_box[2] - 55, q_box[1] + 14), "+25 XP", fill="#D49438", font=get_font(10, bold=True))
    d.text((q_box[0] + 14, q_box[1] + 44), "○ Tanya AI Tutor 1x", fill="#FFFFFF", font=get_font(10))
    d.text((q_box[2] - 55, q_box[1] + 44), "+15 XP", fill="#8C847A", font=get_font(10))
    
    # Bottom Nav Bar
    draw_bottom_nav(d, s, active_idx=0, is_dark=True)
    draw_callout_badge(d, s[0] + 35, s[3] - 46, 5)
    
    img.save(os.path.join(ASSETS_DIR, "mockup_05_home.png"))

# ----------------- 6. DRAWER & SEARCH -----------------
def gen_drawer():
    img, d, s = create_base_phone(bg="#1A1816")
    # Drawer overlay on left 80%
    dw = int((s[2] - s[0]) * 0.82)
    d.rectangle([s[0], s[1], s[0] + dw, s[3]], fill="#262320")
    d.line([s[0] + dw, s[1], s[0] + dw, s[3]], fill="#3B3632", width=2)
    
    # Header user
    d.rounded_rectangle([s[0] + 20, s[1] + 40, s[0] + 65, s[1] + 85], radius=22, fill="#1A6368")
    d.text((s[0] + 35, s[1] + 50), "AF", fill="#FFFFFF", font=get_font(14, bold=True))
    d.text((s[0] + 78, s[1] + 45), "Ahmad Fauzi", fill="#FFFFFF", font=get_font(12, bold=True))
    d.text((s[0] + 78, s[1] + 65), "Lv. 4 Code Builder • 450 XP", fill="#D49438", font=get_font(9))
    
    d.line([s[0] + 20, s[1] + 105, s[0] + dw - 20, s[1] + 105], fill="#3B3632", width=1)
    
    items = [
        ("🏠", "Beranda"),
        ("🔍", "Cari Materi"),
        ("📈", "Progres Belajar"),
        ("🔖", "Modul Tersimpan"),
        ("🏆", "Sertifikat Saya"),
        ("💬", "Diskusi Hangat"),
        ("⚡", "Playground Live"),
        ("🤖", "Tanya AI Tutor"),
        ("⚙️", "Pengaturan & Akun"),
        ("🚪", "Keluar")
    ]
    iy = s[1] + 120
    for ic, txt in items:
        d.text((s[0] + 25, iy), ic, fill="#389B9E", font=get_font(12))
        d.text((s[0] + 55, iy), txt, fill="#FFFFFF", font=get_font(11))
        iy += 38
    img.save(os.path.join(ASSETS_DIR, "mockup_06_drawer.png"))

def gen_search():
    img, d, s = create_base_phone(bg="#1A1816")
    # Search input box
    d.rounded_rectangle([s[0] + 16, s[1] + 40, s[2] - 16, s[1] + 80], radius=10, fill="#262320", outline="#389B9E", width=1)
    d.text((s[0] + 28, s[1] + 52), "🔍 Flexbox", fill="#FFFFFF", font=get_font(11))
    d.text((s[2] - 40, s[1] + 52), "✕", fill="#8C847A", font=get_font(11))
    
    d.text((s[0] + 20, s[1] + 98), "Ditemukan 3 materi terkait", fill="#8C847A", font=get_font(9))
    
    results = [
        ("CSS Flexbox: Dasar Container", "Fondasi Web • Modul 6"),
        ("Flex Direction, Wrap & Order", "CSS Layout Modern • Modul 2"),
        ("Justify Content & Align Items", "CSS Layout Modern • Modul 3"),
    ]
    ry = s[1] + 120
    for tit, sub in results:
        d.rounded_rectangle([s[0] + 16, ry, s[2] - 16, ry + 60], radius=10, fill="#262320", outline="#3B3632")
        d.text((s[0] + 30, ry + 12), tit, fill="#FFFFFF", font=get_font(11, bold=True))
        d.text((s[0] + 30, ry + 32), sub, fill="#389B9E", font=get_font(9))
        ry += 70
        
    img.save(os.path.join(ASSETS_DIR, "mockup_06b_search.png"))

# ----------------- 7. COURSES & CLASS DETAIL -----------------
def gen_courses():
    img, d, s = create_base_phone(bg="#1A1816")
    d.text((s[0] + 20, s[1] + 40), "Katalog Kelas", fill="#FFFFFF", font=get_font(16, bold=True))
    
    # Filter chips
    chips = ["Semua", "Berjalan", "Selesai", "Front-End"]
    cx = s[0] + 20
    for i, ch in enumerate(chips):
        act = (i == 0)
        w = len(ch) * 8 + 16
        d.rounded_rectangle([cx, s[1] + 75, cx + w, s[1] + 100], radius=12, fill="#1A6368" if act else "#262320", outline="#389B9E" if act else "#3B3632")
        d.text((cx + 8, s[1] + 81), ch, fill="#FFFFFF", font=get_font(9, bold=act))
        cx += w + 8
        
    # Course Cards
    courses = [
        ("Fondasi Web: Dari Nol", "106 Modul • HTML & CSS Dasar", "Orientasi", "#1E3835", "#6AD3CA"),
        ("CSS Layout Modern: Flexbox & Grid", "24 Modul • 2 Kuis Bab", "Menengah", "#3B2F22", "#F0BD7E"),
        ("JavaScript Fundamentals", "26 Modul • 2 Kuis Bab", "Pemula", "#1E3835", "#6AD3CA"),
    ]
    cy = s[1] + 120
    for tit, inf, lvl, cbg, ctx in courses:
        d.rounded_rectangle([s[0] + 16, cy, s[2] - 16, cy + 90], radius=12, fill="#262320", outline="#3B3632")
        d.text((s[0] + 28, cy + 14), tit, fill="#FFFFFF", font=get_font(11, bold=True))
        d.text((s[0] + 28, cy + 36), inf, fill="#8C847A", font=get_font(9))
        # Level pill
        d.rounded_rectangle([s[0] + 28, cy + 58, s[0] + 90, cy + 76], radius=6, fill=cbg)
        d.text((s[0] + 36, cy + 62), lvl, fill=ctx, font=get_font(8, bold=True))
        # Button buka
        d.rounded_rectangle([s[2] - 80, cy + 54, s[2] - 25, cy + 78], radius=6, fill="#1A6368")
        d.text((s[2] - 68, cy + 60), "Buka", fill="#FFFFFF", font=get_font(9, bold=True))
        cy += 105
        
    draw_bottom_nav(d, s, active_idx=1, is_dark=True)
    img.save(os.path.join(ASSETS_DIR, "mockup_07_courses.png"))

def gen_class_detail():
    img, d, s = create_base_phone(bg="#1A1816")
    d.text((s[0] + 20, s[1] + 40), "← Detail Kelas", fill="#FFFFFF", font=get_font(14, bold=True))
    d.text((s[0] + 20, s[1] + 75), "Fondasi Web: HTML & CSS", fill="#FFFFFF", font=get_font(15, bold=True))
    d.text((s[0] + 20, s[1] + 98), "8 Modul • 60% Selesai (5/8 Modul)", fill="#389B9E", font=get_font(10))
    
    # Fast track banner
    fb = [s[0] + 16, s[1] + 120, s[2] - 16, s[1] + 180]
    d.rounded_rectangle(fb, radius=10, fill="#203632", outline="#389B9E", width=1)
    d.text((fb[0] + 12, fb[1] + 10), "Sudah paham dasar materi?", fill="#FFFFFF", font=get_font(10, bold=True))
    d.text((fb[0] + 12, fb[1] + 26), "Ikuti Tes Penempatan Fast-Track!", fill="#8C847A", font=get_font(9))
    d.rounded_rectangle([fb[2] - 80, fb[1] + 18, fb[2] - 12, fb[1] + 44], radius=6, fill="#389B9E")
    d.text((fb[2] - 70, fb[1] + 25), "Ikuti Tes", fill="#104246", font=get_font(9, bold=True))
    
    # Modules
    d.text((s[0] + 20, s[1] + 198), "Daftar Modul Belajar", fill="#FFFFFF", font=get_font(12, bold=True))
    mods = [
        ("1. Pengantar Struktur HTML", "✓ Selesai", "#287948"),
        ("2. Heading, Paragraf & Teks", "✓ Selesai", "#287948"),
        ("3. Tautan & Gambar", "✓ Selesai", "#287948"),
        ("4. Pengenalan CSS & Styling", "● Sedang Dipelajari", "#DF7B56"),
        ("5. CSS Box Model", "○ Belum Mulai", "#8C847A"),
    ]
    my = s[1] + 225
    for mtit, stat, scol in mods:
        d.rounded_rectangle([s[0] + 16, my, s[2] - 16, my + 52], radius=8, fill="#262320", outline="#3B3632")
        d.text((s[0] + 26, my + 10), mtit, fill="#FFFFFF", font=get_font(10, bold=True))
        d.text((s[0] + 26, my + 28), stat, fill=scol, font=get_font(9))
        my += 60
        
    img.save(os.path.join(ASSETS_DIR, "mockup_07b_class_detail.png"))

# ----------------- 8. MATERI READER -----------------
def gen_materi_reader():
    img, d, s = create_base_phone(bg="#1A1816")
    # Top bar
    d.text((s[0] + 20, s[1] + 40), "← Fondasi Web", fill="#FFFFFF", font=get_font(12, bold=True))
    d.text((s[2] - 50, s[1] + 40), "🔖 ☰", fill="#389B9E", font=get_font(14))
    draw_callout_badge(d, s[2] - 40, s[1] + 40, 2) # Bookmark
    
    # Title
    d.text((s[0] + 20, s[1] + 75), "Memahami CSS Box Model", fill="#FFFFFF", font=get_font(15, bold=True))
    d.text((s[0] + 20, s[1] + 100), "Setiap elemen HTML pada dasarnya adalah kotak.", fill="#D0D5DD", font=get_font(10))
    d.text((s[0] + 20, s[1] + 118), "Box model terdiri atas: margin, border, padding, isi.", fill="#D0D5DD", font=get_font(10))
    
    # Code snippet block
    c_box = [s[0] + 16, s[1] + 150, s[2] - 16, s[1] + 265]
    d.rounded_rectangle(c_box, radius=8, fill="#161514", outline="#3B3733", width=1)
    d.text((c_box[0] + 14, c_box[1] + 10), "CSS", fill="#8C847A", font=get_font(9, bold=True))
    d.text((c_box[2] - 45, c_box[1] + 10), "Salin", fill="#389B9E", font=get_font(9))
    d.line([c_box[0], c_box[1] + 26, c_box[2], c_box[1] + 26], fill="#262320")
    
    lines = [
        (".card-box {", "#E06D53"),
        ("  width: 280px;", "#F8F5F0"),
        ("  padding: 16px;", "#5CB887"),
        ("  border: 2px solid #389B9E;", "#5CB887"),
        ("  margin: 12px auto;", "#5CB887"),
        ("}", "#E06D53")
    ]
    ly = c_box[1] + 34
    for txt, col in lines:
        d.text((c_box[0] + 14, ly), txt, fill=col, font=get_font(9, code=True))
        ly += 16
    draw_callout_badge(d, c_box[2] - 16, c_box[1] + 50, 3) # Code box
    
    # Action buttons
    d.rounded_rectangle([s[0] + 16, s[1] + 285, s[0] + 155, s[1] + 320], radius=8, fill="#1A6368")
    d.text((s[0] + 28, s[1] + 295), "⚡ Coba di Playground", fill="#FFFFFF", font=get_font(9, bold=True))
    draw_callout_badge(d, s[0] + 155, s[1] + 285, 4)
    
    d.rounded_rectangle([s[0] + 165, s[1] + 285, s[2] - 16, s[1] + 320], radius=8, fill="#262320", outline="#DF7B56")
    d.text((s[0] + 175, s[1] + 295), "🤖 Tanya AI Tutor", fill="#DF7B56", font=get_font(9, bold=True))
    draw_callout_badge(d, s[2] - 16, s[1] + 285, 5)
    
    # Mark Complete Button
    d.rounded_rectangle([s[0] + 16, s[1] + 550, s[2] - 16, s[1] + 595], radius=10, fill="#287948")
    d.text((s[0] + 95, s[1] + 565), "✓ Tandai Selesai (+20 XP)", fill="#FFFFFF", font=get_font(11, bold=True))
    draw_callout_badge(d, s[2] - 16, s[1] + 550, 6)
    
    img.save(os.path.join(ASSETS_DIR, "mockup_08_materi_reader.png"))

# ----------------- 9. PLAYGROUND -----------------
def gen_playground():
    img, d, s = create_base_phone(bg="#161514")
    d.text((s[0] + 20, s[1] + 40), "⚡ Playground Live", fill="#FFFFFF", font=get_font(13, bold=True))
    
    # Editor tabs
    tabs = ["HTML", "CSS", "JS", "Preview"]
    tx = s[0] + 16
    for i, t in enumerate(tabs):
        act = (i == 0)
        d.rounded_rectangle([tx, s[1] + 65, tx + 62, s[1] + 90], radius=6, fill="#201E1C" if not act else "#1A6368")
        d.text((tx + 14, s[1] + 71), t, fill="#FFFFFF" if act else "#8C847A", font=get_font(9, bold=act))
        tx += 68
        
    # Code Editor Canvas
    d.rounded_rectangle([s[0] + 16, s[1] + 100, s[2] - 16, s[1] + 310], radius=8, fill="#201E1C", outline="#3B3733")
    code_lines = [
        ("1", "<div class=\"kartu\">"),
        ("2", "  <h1>Halo Dunia!</h1>"),
        ("3", "  <p>Praktik live di HP</p>"),
        ("4", "  <button>Klik Saya</button>"),
        ("5", "</div>")
    ]
    cy = s[1] + 115
    for num, cl in code_lines:
        d.text((s[0] + 25, cy), num, fill="#6B655D", font=get_font(9, code=True))
        d.text((s[0] + 45, cy), cl, fill="#E06D53" if "<" in cl else "#FFFFFF", font=get_font(9, code=True))
        cy += 20
        
    # Live preview container
    d.text((s[0] + 20, s[1] + 325), "Live Preview Hasil Render:", fill="#8C847A", font=get_font(9, bold=True))
    d.rounded_rectangle([s[0] + 16, s[1] + 345, s[2] - 16, s[1] + 490], radius=8, fill="#FAF7F2", outline="#E2DDD5")
    d.text((s[0] + 35, s[1] + 365), "Halo Dunia!", fill="#104246", font=get_font(14, bold=True))
    d.text((s[0] + 35, s[1] + 390), "Praktik live di HP", fill="#6B655D", font=get_font(10))
    d.rounded_rectangle([s[0] + 35, s[1] + 415, s[0] + 125, s[1] + 445], radius=6, fill="#DF7B56")
    d.text((s[0] + 48, s[1] + 424), "Klik Saya", fill="#FFFFFF", font=get_font(9, bold=True))
    
    # Action buttons
    d.rounded_rectangle([s[0] + 16, s[1] + 530, s[0] + 150, s[1] + 575], radius=8, fill="#1A6368")
    d.text((s[0] + 40, s[1] + 545), "▶ Jalankan", fill="#FFFFFF", font=get_font(11, bold=True))
    
    d.rounded_rectangle([s[0] + 165, s[1] + 530, s[2] - 16, s[1] + 575], radius=8, fill="#262320", outline="#389B9E")
    d.text((s[0] + 185, s[1] + 545), "🤖 Tanya AI", fill="#389B9E", font=get_font(11, bold=True))
    
    img.save(os.path.join(ASSETS_DIR, "mockup_09_playground.png"))

# ----------------- 10. AI TUTOR -----------------
def gen_ai_tutor():
    img, d, s = create_base_phone(bg="#1A1816")
    # Top
    d.text((s[0] + 20, s[1] + 40), "← AI Tutor CodeAcademy", fill="#FFFFFF", font=get_font(13, bold=True))
    d.text((s[0] + 20, s[1] + 58), "Didukung Gemini AI • Konteks: CSS Box Model", fill="#389B9E", font=get_font(8))
    
    # Suggestion pills
    d.rounded_rectangle([s[0] + 16, s[1] + 80, s[0] + 145, s[1] + 104], radius=12, fill="#262320", outline="#389B9E")
    d.text((s[0] + 24, s[1] + 86), "Apa beda margin & padding?", fill="#389B9E", font=get_font(8))
    
    # Chat message 1 (User)
    u_box = [s[0] + 90, s[1] + 120, s[2] - 16, s[1] + 175]
    d.rounded_rectangle(u_box, radius=12, fill="#1A6368")
    d.text((u_box[0] + 12, u_box[1] + 10), "Bagaimana cara memusatkan", fill="#FFFFFF", font=get_font(9))
    d.text((u_box[0] + 12, u_box[1] + 26), "elemen kartu ini ke tengah", fill="#FFFFFF", font=get_font(9))
    d.text((u_box[0] + 12, u_box[1] + 42), "layar secara horizontal?", fill="#FFFFFF", font=get_font(9))
    
    # Chat message 2 (AI Tutor)
    ai_box = [s[0] + 16, s[1] + 195, s[2] - 50, s[1] + 375]
    d.rounded_rectangle(ai_box, radius=12, fill="#262320", outline="#3B3632")
    d.text((ai_box[0] + 12, ai_box[1] + 10), "🤖 AI Tutor", fill="#DF7B56", font=get_font(9, bold=True))
    d.text((ai_box[0] + 12, ai_box[1] + 28), "Kamu bisa menggunakan dua cara:", fill="#FFFFFF", font=get_font(9))
    d.text((ai_box[0] + 12, ai_box[1] + 45), "1. Gunakan margin otomatis:", fill="#D0EBEB", font=get_font(9, bold=True))
    d.text((ai_box[0] + 20, ai_box[1] + 62), "margin: 0 auto;", fill="#5CB887", font=get_font(9, code=True))
    d.text((ai_box[0] + 12, ai_box[1] + 82), "2. Atau gunakan Flexbox:", fill="#D0EBEB", font=get_font(9, bold=True))
    d.text((ai_box[0] + 20, ai_box[1] + 99), "display: flex;", fill="#5CB887", font=get_font(9, code=True))
    d.text((ai_box[0] + 20, ai_box[1] + 115), "justify-content: center;", fill="#5CB887", font=get_font(9, code=True))
    d.text((ai_box[0] + 12, ai_box[1] + 138), "Pastikan elemen pembungkus memiliki", fill="#8C847A", font=get_font(8))
    d.text((ai_box[0] + 12, ai_box[1] + 152), "lebar (width) yang jelas ya!", fill="#8C847A", font=get_font(8))
    
    # Input field
    d.rounded_rectangle([s[0] + 16, s[1] + 540, s[2] - 65, s[1] + 585], radius=10, fill="#262320", outline="#3B3632")
    d.text((s[0] + 30, s[1] + 554), "Tanyakan seputar coding...", fill="#8C847A", font=get_font(10))
    d.rounded_rectangle([s[2] - 55, s[1] + 540, s[2] - 16, s[1] + 585], radius=10, fill="#DF7B56")
    d.text((s[2] - 42, s[1] + 554), "➤", fill="#FFFFFF", font=get_font(13))
    
    img.save(os.path.join(ASSETS_DIR, "mockup_10_ai_tutor.png"))

# ----------------- 11. QUIZ & RESULT -----------------
def gen_quiz():
    img, d, s = create_base_phone(bg="#1A1816")
    # Header timer
    d.text((s[0] + 20, s[1] + 40), "Ujian Evaluasi: CSS Dasar", fill="#FFFFFF", font=get_font(12, bold=True))
    d.text((s[2] - 80, s[1] + 40), "⏱️ 08:45", fill="#DF7B56", font=get_font(11, bold=True))
    
    # Progress bar
    d.rounded_rectangle([s[0] + 20, s[1] + 68, s[2] - 20, s[1] + 74], radius=3, fill="#3B3632")
    d.rounded_rectangle([s[0] + 20, s[1] + 68, s[0] + 120, s[1] + 74], radius=3, fill="#389B9E")
    
    d.text((s[0] + 20, s[1] + 95), "Soal 3 dari 10", fill="#8C847A", font=get_font(10))
    d.text((s[0] + 20, s[1] + 120), "Properti CSS manakah yang", fill="#FFFFFF", font=get_font(13, bold=True))
    d.text((s[0] + 20, s[1] + 140), "digunakan untuk memberi jarak", fill="#FFFFFF", font=get_font(13, bold=True))
    d.text((s[0] + 20, s[1] + 160), "di dalam garis batas (border)?", fill="#FFFFFF", font=get_font(13, bold=True))
    
    opts = [
        ("A", "margin", False),
        ("B", "padding", True),
        ("C", "spacing", False),
        ("D", "outline", False)
    ]
    oy = s[1] + 200
    for ltr, otxt, sel in opts:
        bg_col = "#1A6368" if sel else "#262320"
        b_col = "#389B9E" if sel else "#3B3632"
        d.rounded_rectangle([s[0] + 16, oy, s[2] - 16, oy + 48], radius=8, fill=bg_col, outline=b_col, width=2 if sel else 1)
        d.text((s[0] + 30, oy + 14), f"{ltr}.  {otxt}", fill="#FFFFFF", font=get_font(11, bold=sel))
        if sel:
            d.text((s[2] - 45, oy + 14), "✓", fill="#389B9E", font=get_font(13, bold=True))
        oy += 58
        
    d.rounded_rectangle([s[0] + 16, s[1] + 540, s[2] - 16, s[1] + 585], radius=10, fill="#DF7B56")
    d.text((s[0] + 115, s[1] + 555), "Soal Berikutnya >", fill="#FFFFFF", font=get_font(11, bold=True))
    img.save(os.path.join(ASSETS_DIR, "mockup_11_quiz.png"))

def gen_quiz_result():
    img, d, s = create_base_phone(bg="#1A1816")
    d.text((s[0] + 20, s[1] + 40), "Hasil Evaluasi", fill="#FFFFFF", font=get_font(14, bold=True))
    
    # Celebration card
    c_box = [s[0] + 20, s[1] + 85, s[2] - 20, s[1] + 320]
    d.rounded_rectangle(c_box, radius=14, fill="#104246", outline="#389B9E", width=2)
    d.text((c_box[0] + 90, c_box[1] + 25), "🏆", font=get_font(36))
    d.text((c_box[0] + 55, c_box[1] + 80), "Selamat! Kamu Lulus", fill="#FFFFFF", font=get_font(15, bold=True))
    d.text((c_box[0] + 75, c_box[1] + 120), "SKOR AKHIR", fill="#389B9E", font=get_font(10, bold=True))
    d.text((c_box[0] + 80, c_box[1] + 140), "90%", fill="#D49438", font=get_font(34, bold=True))
    d.text((c_box[0] + 45, c_box[1] + 195), "9 dari 10 Soal Terjawab Benar", fill="#FFFFFF", font=get_font(10))
    d.text((c_box[0] + 85, c_box[1] + 215), "+100 XP Diperoleh!", fill="#DF7B56", font=get_font(10, bold=True))
    
    # Action buttons
    d.rounded_rectangle([s[0] + 20, s[1] + 350, s[2] - 20, s[1] + 395], radius=10, fill="#DF7B56")
    d.text((s[0] + 85, s[1] + 365), "📜 Klaim Sertifikat", fill="#FFFFFF", font=get_font(11, bold=True))
    
    d.rounded_rectangle([s[0] + 20, s[1] + 410, s[2] - 20, s[1] + 455], radius=10, fill="#262320", outline="#3B3632")
    d.text((s[0] + 95, s[1] + 425), "Lihat Pembahasan", fill="#FFFFFF", font=get_font(11))
    
    img.save(os.path.join(ASSETS_DIR, "mockup_11b_quiz_result.png"))

# ----------------- 12. FINAL PROJECT -----------------
def gen_final_project():
    img, d, s = create_base_phone(bg="#1A1816")
    d.text((s[0] + 20, s[1] + 40), "← Pengumpulan Proyek Akhir", fill="#FFFFFF", font=get_font(12, bold=True))
    d.text((s[0] + 20, s[1] + 70), "Tugas: Web Portofolio", fill="#FFFFFF", font=get_font(14, bold=True))
    
    # Rubric checklist
    d.rounded_rectangle([s[0] + 16, s[1] + 100, s[2] - 16, s[1] + 210], radius=10, fill="#262320", outline="#3B3632")
    d.text((s[0] + 28, s[1] + 112), "Kriteria Penilaian:", fill="#DF7B56", font=get_font(10, bold=True))
    d.text((s[0] + 28, s[1] + 132), "✓ Struktur HTML5 Semantik", fill="#FFFFFF", font=get_font(9))
    d.text((s[0] + 28, s[1] + 152), "✓ CSS Flexbox & Tata Letak Responsif", fill="#FFFFFF", font=get_font(9))
    d.text((s[0] + 28, s[1] + 172), "✓ Minimal 3 Bagian: Header, Galeri, Kontak", fill="#FFFFFF", font=get_font(9))
    
    # GitHub Repo Input
    d.text((s[0] + 20, s[1] + 230), "URL Repositori GitHub / GitLab:", fill="#FFFFFF", font=get_font(10, bold=True))
    d.rounded_rectangle([s[0] + 16, s[1] + 250, s[2] - 16, s[1] + 290], radius=8, fill="#161514", outline="#389B9E")
    d.text((s[0] + 26, s[1] + 262), "https://github.com/ahmad/portfolio", fill="#389B9E", font=get_font(9, code=True))
    
    # Notes Input
    d.text((s[0] + 20, s[1] + 310), "Catatan untuk Fasilitator Reviewer:", fill="#FFFFFF", font=get_font(10, bold=True))
    d.rounded_rectangle([s[0] + 16, s[1] + 330, s[2] - 16, s[1] + 410], radius=8, fill="#161514", outline="#3B3632")
    d.text((s[0] + 26, s[1] + 342), "Sudah ditambahkan animasi interaktif", fill="#8C847A", font=get_font(9))
    d.text((s[0] + 26, s[1] + 358), "menggunakan CSS transitions.", fill="#8C847A", font=get_font(9))
    
    # Submit button
    d.rounded_rectangle([s[0] + 16, s[1] + 530, s[2] - 16, s[1] + 575], radius=10, fill="#1A6368")
    d.text((s[0] + 85, s[1] + 545), "🚀 Kumpulkan Proyek Akhir", fill="#FFFFFF", font=get_font(11, bold=True))
    
    img.save(os.path.join(ASSETS_DIR, "mockup_12_final_project.png"))

# ----------------- 13. COMMUNITY -----------------
def gen_community():
    img, d, s = create_base_phone(bg="#1A1816")
    d.text((s[0] + 20, s[1] + 40), "Komunitas & Forum", fill="#FFFFFF", font=get_font(15, bold=True))
    
    # Forum tabs
    d.text((s[0] + 20, s[1] + 75), "Diskusi Hangat", fill="#389B9E", font=get_font(11, bold=True))
    d.text((s[0] + 130, s[1] + 75), "Forum Kelas", fill="#8C847A", font=get_font(11))
    d.line([s[0] + 20, s[1] + 92, s[0] + 105, s[1] + 92], fill="#389B9E", width=2)
    
    threads = [
        ("Kenapa properti flex-wrap tidak bekerja?", "Budi Santoso • 12 balasan • 24 upvotes"),
        ("Tips lulus evaluasi akhir materi HTML & CSS", "Dewi Lestari • 18 balasan • 45 upvotes"),
        ("Tanya: Kapan sebaiknya pakai Grid vs Flexbox?", "Rian Pratama • 9 balasan • 16 upvotes")
    ]
    ty = s[1] + 110
    for tit, inf in threads:
        d.rounded_rectangle([s[0] + 16, ty, s[2] - 16, ty + 72], radius=10, fill="#262320", outline="#3B3632")
        d.text((s[0] + 28, ty + 12), tit, fill="#FFFFFF", font=get_font(10, bold=True))
        d.text((s[0] + 28, ty + 36), inf, fill="#389B9E", font=get_font(8))
        ty += 84
        
    # Floating action button
    d.rounded_rectangle([s[2] - 120, s[1] + 515, s[2] - 20, s[1] + 555], radius=20, fill="#DF7B56")
    d.text((s[2] - 105, s[1] + 528), "+ Tanya Baru", fill="#FFFFFF", font=get_font(9, bold=True))
    
    draw_bottom_nav(d, s, active_idx=3, is_dark=True)
    img.save(os.path.join(ASSETS_DIR, "mockup_13_community.png"))

# ----------------- 14. LEADERBOARD & CERTIFICATE -----------------
def gen_leaderboard_cert():
    img, d, s = create_base_phone(bg="#1A1816")
    d.text((s[0] + 20, s[1] + 40), "Papan Peringkat Siswa", fill="#FFFFFF", font=get_font(14, bold=True))
    
    ranks = [
        ("🥇", "1", "Nadia Putri", "1.250 XP"),
        ("🥈", "2", "Kevin Wijaya", "1.120 XP"),
        ("🥉", "3", "Siti Rahma", "980 XP"),
        ("⭐", "4", "Ahmad Fauzi (Kamu)", "850 XP"),
    ]
    ry = s[1] + 75
    for med, num, name, xp in ranks:
        is_you = ("Kamu" in name)
        bg_col = "#1A6368" if is_you else "#262320"
        d.rounded_rectangle([s[0] + 16, ry, s[2] - 16, ry + 42], radius=8, fill=bg_col)
        d.text((s[0] + 26, ry + 12), f"{med} #{num}", fill="#FFFFFF", font=get_font(10, bold=True))
        d.text((s[0] + 75, ry + 12), name, fill="#FFFFFF", font=get_font(10, bold=is_you))
        d.text((s[2] - 75, ry + 12), xp, fill="#D49438" if is_you else "#389B9E", font=get_font(10, bold=True))
        ry += 48
        
    # Certificate Preview Card
    d.text((s[0] + 20, s[1] + 295), "Sertifikat Kelulusan Resmi", fill="#FFFFFF", font=get_font(12, bold=True))
    cb = [s[0] + 16, s[1] + 320, s[2] - 16, s[1] + 490]
    d.rounded_rectangle(cb, radius=12, fill="#FAF7F2", outline="#D49438", width=2)
    d.text((cb[0] + 65, cb[1] + 18), "SERTIFIKAT KELULUSAN", fill="#104246", font=get_font(10, bold=True))
    d.text((cb[0] + 55, cb[1] + 38), "Diberikan dengan bangga kepada:", fill="#6B655D", font=get_font(8))
    d.text((cb[0] + 75, cb[1] + 56), "AHMAD FAUZI", fill="#104246", font=get_font(12, bold=True))
    d.text((cb[0] + 40, cb[1] + 78), "Atas keberhasilan menyelesaikan kelas:", fill="#6B655D", font=get_font(8))
    d.text((cb[0] + 35, cb[1] + 94), "Fondasi Web: HTML & Styling CSS", fill="#DF7B56", font=get_font(9, bold=True))
    d.text((cb[0] + 35, cb[1] + 120), "ID Kredensial: CA-2026-88912", fill="#8C847A", font=get_font(7, code=True))
    
    draw_bottom_nav(d, s, active_idx=2, is_dark=True)
    img.save(os.path.join(ASSETS_DIR, "mockup_14_leaderboard_cert.png"))

# ----------------- 15. PROFILE & SETTINGS -----------------
def gen_profile():
    img, d, s = create_base_phone(bg="#1A1816")
    # Avatar & Info
    d.rounded_rectangle([s[0] + 20, s[1] + 40, s[0] + 65, s[1] + 85], radius=22, fill="#1A6368")
    d.text((s[0] + 35, s[1] + 52), "AF", fill="#FFFFFF", font=get_font(14, bold=True))
    d.text((s[0] + 75, s[1] + 45), "Ahmad Fauzi", fill="#FFFFFF", font=get_font(13, bold=True))
    d.text((s[0] + 75, s[1] + 65), "Front-End Track • 4 Sertifikat", fill="#389B9E", font=get_font(9))
    
    # Heatmap section
    d.text((s[0] + 20, s[1] + 105), "Peta Semangat Belajar (Heatmap)", fill="#FFFFFF", font=get_font(10, bold=True))
    hx, hy = s[0] + 20, s[1] + 125
    for row in range(5):
        for col in range(16):
            c_fill = "#287948" if (row + col) % 3 == 0 else ("#1E3835" if (row + col) % 2 == 0 else "#262320")
            d.rectangle([hx + col * 16, hy + row * 16, hx + col * 16 + 12, hy + row * 16 + 12], fill=c_fill)
            
    # Settings items
    d.text((s[0] + 20, s[1] + 225), "Pengaturan & Preferensi", fill="#FFFFFF", font=get_font(11, bold=True))
    st = [
        ("Mode Gelap", "Tampilan gelap aktif", "ON"),
        ("Bahasa Aplikasi", "Bahasa Indonesia", "Ganti"),
        ("Notifikasi Belajar", "Pengingat streak harian", "ON"),
        ("Ulangi Onboarding", "Pilih ulang target jalur", "Pilih"),
        ("Bantuan & FAQ", "Panduan dan kontak pengembang", ">")
    ]
    sy = s[1] + 248
    for tit, sub, val in st:
        d.rounded_rectangle([s[0] + 16, sy, s[2] - 16, sy + 44], radius=8, fill="#262320", outline="#3B3632")
        d.text((s[0] + 28, sy + 8), tit, fill="#FFFFFF", font=get_font(10, bold=True))
        d.text((s[0] + 28, sy + 24), sub, fill="#8C847A", font=get_font(8))
        d.text((s[2] - 55, sy + 14), val, fill="#389B9E", font=get_font(9, bold=True))
        sy += 50
        
    draw_bottom_nav(d, s, active_idx=4, is_dark=True)
    img.save(os.path.join(ASSETS_DIR, "mockup_15_profile_settings.png"))

def main():
    print("Generating all mockups...")
    gen_splash()
    gen_install()
    gen_login()
    gen_register()
    gen_onboarding()
    gen_home()
    gen_drawer()
    gen_search()
    gen_courses()
    gen_class_detail()
    gen_materi_reader()
    gen_playground()
    gen_ai_tutor()
    gen_quiz()
    gen_quiz_result()
    gen_final_project()
    gen_community()
    gen_leaderboard_cert()
    gen_profile()
    print("All mockups generated successfully in docs/manual_book_assets/!")

if __name__ == "__main__":
    main()
