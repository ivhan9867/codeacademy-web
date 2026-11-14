import os
from PIL import Image, ImageDraw, ImageFont

ASSETS_DIR = r"docs\manual_book_assets"
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

def draw_callout_badge(d, x, y, num):
    r = 11
    d.ellipse([x - r, y - r, x + r, y + r], fill="#E74C3C", outline="#FFFFFF", width=2)
    d.text((x - 4, y - 7), str(num), fill="#FFFFFF", font=get_font(11, bold=True))

def gen_facilitator_web_mockup():
    w, h = 600, 420
    img = Image.new("RGBA", (w, h), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)

    # Window frame
    d.rounded_rectangle([0, 0, w, h], radius=16, fill="#FFFFFF", outline="#D0D7DE", width=2)

    # Browser top bar
    d.rounded_rectangle([0, 0, w, 44], radius=16, fill="#F6F8FA")
    d.rectangle([0, 30, w, 44], fill="#F6F8FA")
    d.line([0, 44, w, 44], fill="#D0D7DE", width=1)

    # 3 dots
    d.ellipse([14, 16, 26, 28], fill="#FF5F56")
    d.ellipse([34, 16, 46, 28], fill="#FFBD2E")
    d.ellipse([54, 16, 66, 28], fill="#27C93F")

    # Address bar
    d.rounded_rectangle([90, 10, w - 30, 34], radius=8, fill="#FFFFFF", outline="#D0D7DE")
    d.text((105, 14), "🔒 https://codeacademy.app/facilitator/dashboard", fill="#57606A", font=get_font(10))

    # App Navbar
    d.rectangle([0, 45, w, 90], fill="#104246")
    d.text((20, 56), "</> CodeAcademy Facilitator Portal", fill="#FFFFFF", font=get_font(13, bold=True))
    d.rounded_rectangle([w - 140, 56, w - 20, 78], radius=10, fill="#287948")
    d.text((w - 128, 61), "● Live Realtime Sync", fill="#FFFFFF", font=get_font(9, bold=True))

    # Metric stats row
    stats = [
        ("Total Submissions", "12", "#104246"),
        ("Menunggu Review", "4", "#B45309"),
        ("Perlu Revisi", "2", "#DF7B56"),
        ("Disetujui", "6", "#287948")
    ]
    sw = (w - 50) / 4
    for i, (lbl, val, col) in enumerate(stats):
        bx = 20 + i * (sw + 8)
        d.rounded_rectangle([bx, 104, bx + sw, 156], radius=10, fill="#F8F9FA", outline="#E2DDD5")
        d.text((bx + 12, 112), val, fill=col, font=get_font(16, bold=True))
        d.text((bx + 12, 134), lbl, fill="#5A626A", font=get_font(8, bold=True))

    draw_callout_badge(d, 20 + sw - 10, 104, 1) # Callout 1 on Stats

    # Filter bar
    d.rounded_rectangle([20, 170, w - 20, 202], radius=8, fill="#F8F9FA", outline="#E2DDD5")
    d.text((32, 178), "🔍 Cari nama / email siswa...", fill="#8C847A", font=get_font(9))
    d.rounded_rectangle([w - 240, 175, w - 165, 197], radius=6, fill="#1A6368")
    d.text((w - 230, 180), "Semua (12)", fill="#FFFFFF", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 155, 175, w - 70, 197], radius=6, fill="#FFF4E5")
    d.text((w - 150, 180), "Menunggu (4)", fill="#B45309", font=get_font(8, bold=True))
    draw_callout_badge(d, w - 70, 175, 2) # Callout 2 on Filter

    # Table header
    d.rectangle([20, 214, w - 20, 238], fill="#F1ECE4")
    d.text((30, 220), "Mahasiswa", fill="#24211D", font=get_font(9, bold=True))
    d.text((180, 220), "Mata Kuliah", fill="#24211D", font=get_font(9, bold=True))
    d.text((350, 220), "Skor Kuis", fill="#24211D", font=get_font(9, bold=True))
    d.text((430, 220), "Status", fill="#24211D", font=get_font(9, bold=True))
    d.text((w - 75, 220), "Aksi", fill="#24211D", font=get_font(9, bold=True))

    # Row 1
    d.rectangle([20, 240, w - 20, 292], fill="#FFFFFF")
    d.line([20, 292, w - 20, 292], fill="#ECE7DF")
    d.text((30, 248), "Ahmad Fauzi", fill="#24211D", font=get_font(10, bold=True))
    d.text((30, 264), "ahmad.fauzi@kampus.ac.id", fill="#8C847A", font=get_font(8))
    d.text((180, 254), "Fondasi Web: HTML & CSS", fill="#1A6368", font=get_font(9, bold=True))
    d.text((360, 254), "90%", fill="#1A6368", font=get_font(10, bold=True))
    d.rounded_rectangle([425, 250, 505, 274], radius=10, fill="#FFF4E5")
    d.text((435, 256), "MENUNGGU", fill="#B45309", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 85, 250, w - 30, 274], radius=6, fill="#1A6368")
    d.text((w - 72, 256), "Tinjau", fill="#FFFFFF", font=get_font(9, bold=True))
    draw_callout_badge(d, w - 30, 250, 3) # Callout 3 on Tinjau button

    # Row 2
    d.rectangle([20, 294, w - 20, 346], fill="#FFFFFF")
    d.line([20, 346, w - 20, 346], fill="#ECE7DF")
    d.text((30, 302), "Budi Santoso", fill="#24211D", font=get_font(10, bold=True))
    d.text((30, 318), "budi.santoso@kampus.ac.id", fill="#8C847A", font=get_font(8))
    d.text((180, 308), "Node.js Fundamentals", fill="#1A6368", font=get_font(9, bold=True))
    d.text((360, 308), "85%", fill="#1A6368", font=get_font(10, bold=True))
    d.rounded_rectangle([425, 304, 510, 328], radius=10, fill="#FCEEE8")
    d.text((433, 310), "PERLU REVISI", fill="#DF7B56", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 85, 304, w - 30, 328], radius=6, fill="#5A626A")
    d.text((w - 72, 310), "Detail", fill="#FFFFFF", font=get_font(9, bold=True))

    # Row 3
    d.rectangle([20, 348, w - 20, 400], fill="#FFFFFF")
    d.text((30, 356), "Siti Rahmawati", fill="#24211D", font=get_font(10, bold=True))
    d.text((30, 372), "siti.rahma@kampus.ac.id", fill="#8C847A", font=get_font(8))
    d.text((180, 362), "Python Programming", fill="#1A6368", font=get_font(9, bold=True))
    d.text((360, 362), "95%", fill="#1A6368", font=get_font(10, bold=True))
    d.rounded_rectangle([425, 358, 495, 382], radius=10, fill="#EAF6EE")
    d.text((435, 364), "DISETUJUI", fill="#287948", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 85, 358, w - 30, 382], radius=6, fill="#5A626A")
    d.text((w - 72, 364), "Detail", fill="#FFFFFF", font=get_font(9, bold=True))

    out_p = os.path.join(ASSETS_DIR, "mockup_16_facilitator_web.png")
    img.save(out_p)
    print(f"Facilitator web mockup saved to: {out_p}")

def gen_verify_web_mockup():
    w, h = 600, 380
    img = Image.new("RGBA", (w, h), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)

    # Window frame
    d.rounded_rectangle([0, 0, w, h], radius=16, fill="#FFFFFF", outline="#D0D7DE", width=2)

    # Browser top bar
    d.rounded_rectangle([0, 0, w, 44], radius=16, fill="#F6F8FA")
    d.rectangle([0, 30, w, 44], fill="#F6F8FA")
    d.line([0, 44, w, 44], fill="#D0D7DE", width=1)

    # 3 dots
    d.ellipse([14, 16, 26, 28], fill="#FF5F56")
    d.ellipse([34, 16, 46, 28], fill="#FFBD2E")
    d.ellipse([54, 16, 66, 28], fill="#27C93F")

    # Address bar
    d.rounded_rectangle([90, 10, w - 30, 34], radius=8, fill="#FFFFFF", outline="#D0D7DE")
    d.text((105, 14), "🔒 https://codeacademy.app/verify.html?id=CA-2026-88912", fill="#57606A", font=get_font(10))

    # App Navbar
    d.rectangle([0, 45, w, 85], fill="#104246")
    d.text((20, 56), "</> CodeAcademy Certificate Verification", fill="#FFFFFF", font=get_font(12, bold=True))

    # Center card
    card_x, card_y = 60, 105
    card_w, card_h = w - 120, 250
    d.rounded_rectangle([card_x, card_y, card_x + card_w, card_y + card_h], radius=14, fill="#FAF7F2", outline="#D49438", width=2)

    d.rounded_rectangle([card_x + 20, card_y + 16, card_x + 210, card_y + 38], radius=10, fill="#287948")
    d.text((card_x + 30, card_y + 21), "✓ KREDENSIAL TERVERIFIKASI", fill="#FFFFFF", font=get_font(9, bold=True))

    d.text((card_x + 20, card_y + 48), "AHMAD FAUZI", fill="#104246", font=get_font(18, bold=True))
    d.text((card_x + 20, card_y + 72), "Telah menyelesaikan kurikulum kelas & evaluasi proyek akhir resmi.", fill="#5A626A", font=get_font(9))

    d.line([card_x + 20, card_y + 92, card_x + card_w - 20, card_y + 92], fill="#E2DDD5", width=1)

    fields = [
        ("Mata Kuliah / Kelas:", "Fondasi Web: HTML & CSS"),
        ("Nilai Akhir Kelulusan:", "88% (Sangat Memuaskan)"),
        ("ID Kredensial:", "CA-2026-88912"),
        ("Tanggal Terbit:", "12 November 2026"),
        ("Status Keabsahan:", "Sah & Terdaftar di Basis Data")
    ]
    fy = card_y + 102
    for lbl, val in fields:
        d.text((card_x + 20, fy), lbl, fill="#8C847A", font=get_font(9, bold=True))
        d.text((card_x + 180, fy), val, fill="#104246" if "Sah" not in val else "#287948", font=get_font(9, bold=True))
        fy += 26

    out_p = os.path.join(ASSETS_DIR, "mockup_17_verify_web.png")
    img.save(out_p)
    print(f"Verify web mockup saved to: {out_p}")

if __name__ == "__main__":
    gen_facilitator_web_mockup()
    gen_verify_web_mockup()
