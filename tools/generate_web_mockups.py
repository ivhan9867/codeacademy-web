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
    d.ellipse([x - r, y - r, x + r, y + r], fill="#E26D45", outline="#FFFFFF", width=2)
    d.text((x - 4, y - 7), str(num), fill="#FFFFFF", font=get_font(11, bold=True))

def gen_facilitator_web_mockup():
    w, h = 600, 420
    img = Image.new("RGBA", (w, h), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)

    # Window frame (editorial paper #F7FAFA)
    d.rounded_rectangle([0, 0, w, h], radius=16, fill="#F7FAFA", outline="#D5E3E2", width=2)

    # Browser top bar
    d.rounded_rectangle([0, 0, w, 44], radius=16, fill="#EEF6F5")
    d.rectangle([0, 30, w, 44], fill="#EEF6F5")
    d.line([0, 44, w, 44], fill="#D5E3E2", width=1)

    # 3 dots
    d.ellipse([14, 16, 26, 28], fill="#FF5F56")
    d.ellipse([34, 16, 46, 28], fill="#FFBD2E")
    d.ellipse([54, 16, 66, 28], fill="#27C93F")

    # Address bar (Actual GitHub Pages URL)
    d.rounded_rectangle([84, 10, w - 20, 34], radius=8, fill="#FFFFFF", outline="#D5E3E2")
    d.text((96, 15), "https://ivhan9867.github.io/codeacademy-web/facilitator/index.html", fill="#4F6668", font=get_font(9))

    # Sticky Frosted Editorial Navbar
    d.rectangle([0, 45, w, 86], fill="#F7FAFA")
    d.line([0, 86, w, 86], fill="#D5E3E2", width=1)
    d.rounded_rectangle([20, 54, 42, 76], radius=6, fill="#104246")
    d.text((24, 58), ">_", fill="#FFFFFF", font=get_font(9, bold=True, code=True))
    d.text((50, 57), "CodeAcademy", fill="#0D2B2E", font=get_font(12, bold=True))
    d.rounded_rectangle([140, 56, 232, 74], radius=9, fill="#D9EEEC")
    d.text((148, 59), "Portal Fasilitator", fill="#104246", font=get_font(8, bold=True))

    d.rounded_rectangle([w - 155, 55, w - 20, 75], radius=10, fill="#E9F6EE", outline="#B9E2C8")
    d.text((w - 144, 59), "● Live Firestore Sync", fill="#1F7A45", font=get_font(8, bold=True, code=True))

    # Page header
    d.text((20, 94), "EVALUASI PROYEK AKHIR", fill="#1A6368", font=get_font(8, bold=True, code=True))
    d.text((20, 106), "Antrean Penilaian Peserta", fill="#0D2B2E", font=get_font(14, bold=True))

    # Metric stats row
    stats = [
        ("Total Submission", "12", "#0D2B2E"),
        ("Menunggu Review", "4", "#B45309"),
        ("Perlu Revisi", "2", "#E26D45"),
        ("Disetujui", "6", "#1F7A45")
    ]
    sw = (w - 64) / 4
    for i, (lbl, val, col) in enumerate(stats):
        bx = 20 + i * (sw + 8)
        d.rounded_rectangle([bx, 132, bx + sw, 178], radius=10, fill="#FFFFFF", outline="#D5E3E2")
        d.text((bx + 10, 138), lbl, fill="#4F6668", font=get_font(8, bold=True))
        d.text((bx + 10, 152), val, fill=col, font=get_font(15, bold=True))

    draw_callout_badge(d, 20 + sw - 6, 132, 1) # Callout 1 on Stats

    # Filter bar
    d.rounded_rectangle([20, 188, w - 20, 218], radius=10, fill="#FFFFFF", outline="#D5E3E2")
    d.text((32, 196), "Cari nama peserta, email, atau modul...", fill="#799092", font=get_font(9))
    d.rounded_rectangle([w - 228, 193, w - 168, 213], radius=6, fill="#104246")
    d.text((w - 216, 197), "Semua", fill="#FFFFFF", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 162, 193, w - 102, 213], radius=6, fill="#F2F7F7")
    d.text((w - 150, 197), "Pending", fill="#4F6668", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 96, 193, w - 28, 213], radius=6, fill="#F2F7F7")
    d.text((w - 86, 197), "Approved", fill="#4F6668", font=get_font(8, bold=True))
    draw_callout_badge(d, w - 34, 190, 2) # Callout 2 on Filter

    # Table header
    d.rectangle([20, 228, w - 20, 250], fill="#F2F7F7")
    d.line([20, 250, w - 20, 250], fill="#D5E3E2")
    d.text((30, 234), "PESERTA", fill="#4F6668", font=get_font(8, bold=True))
    d.text((175, 234), "MODUL KURSUS", fill="#4F6668", font=get_font(8, bold=True))
    d.text((335, 234), "SKOR KUIS", fill="#4F6668", font=get_font(8, bold=True))
    d.text((405, 234), "STATUS", fill="#4F6668", font=get_font(8, bold=True))
    d.text((w - 75, 234), "AKSI", fill="#4F6668", font=get_font(8, bold=True))

    # Row 1
    d.rectangle([20, 251, w - 20, 300], fill="#FFFFFF")
    d.line([20, 300, w - 20, 300], fill="#E6EFEF")
    d.text((30, 258), "Ahmad Fauzi", fill="#0D2B2E", font=get_font(10, bold=True))
    d.text((30, 274), "ahmad.fauzi@kampus.ac.id", fill="#4F6668", font=get_font(8))
    d.text((175, 265), "Fondasi Web: Dari Nol", fill="#0D2B2E", font=get_font(9, bold=True))
    d.text((345, 265), "90%", fill="#104246", font=get_font(10, bold=True, code=True))
    d.rounded_rectangle([400, 261, 480, 283], radius=10, fill="#FEF4E6")
    d.text((412, 266), "PENDING", fill="#B45309", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 90, 261, w - 30, 283], radius=6, fill="#FFFFFF", outline="#104246", width=2)
    d.text((w - 76, 266), "Tinjau", fill="#104246", font=get_font(9, bold=True))
    draw_callout_badge(d, w - 30, 260, 3) # Callout 3 on Tinjau button

    # Row 2
    d.rectangle([20, 301, w - 20, 350], fill="#FFFFFF")
    d.line([20, 350, w - 20, 350], fill="#E6EFEF")
    d.text((30, 308), "Budi Santoso", fill="#0D2B2E", font=get_font(10, bold=True))
    d.text((30, 324), "budi.santoso@kampus.ac.id", fill="#4F6668", font=get_font(8))
    d.text((175, 315), "CSS Layout Modern", fill="#0D2B2E", font=get_font(9, bold=True))
    d.text((345, 315), "85%", fill="#104246", font=get_font(10, bold=True, code=True))
    d.rounded_rectangle([400, 311, 480, 333], radius=10, fill="#FDF0EB")
    d.text((416, 316), "REVISI", fill="#E26D45", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 90, 311, w - 30, 333], radius=6, fill="#FFFFFF", outline="#104246", width=1)
    d.text((w - 76, 316), "Tinjau", fill="#104246", font=get_font(9, bold=True))

    # Row 3
    d.rectangle([20, 351, w - 20, 400], fill="#FFFFFF")
    d.text((30, 358), "Siti Rahmawati", fill="#0D2B2E", font=get_font(10, bold=True))
    d.text((30, 374), "siti.rahma@kampus.ac.id", fill="#4F6668", font=get_font(8))
    d.text((175, 365), "JavaScript Fundamentals", fill="#0D2B2E", font=get_font(9, bold=True))
    d.text((345, 365), "95%", fill="#104246", font=get_font(10, bold=True, code=True))
    d.rounded_rectangle([400, 361, 480, 383], radius=10, fill="#E9F6EE")
    d.text((409, 366), "APPROVED", fill="#1F7A45", font=get_font(8, bold=True))
    d.rounded_rectangle([w - 90, 361, w - 30, 383], radius=6, fill="#FFFFFF", outline="#104246", width=1)
    d.text((w - 76, 366), "Tinjau", fill="#104246", font=get_font(9, bold=True))

    out_p = os.path.join(ASSETS_DIR, "mockup_16_facilitator_web.png")
    img.save(out_p)
    print(f"Facilitator web mockup saved to: {out_p}")

def gen_verify_web_mockup():
    w, h = 600, 380
    img = Image.new("RGBA", (w, h), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)

    # Window frame (editorial paper #F7FAFA)
    d.rounded_rectangle([0, 0, w, h], radius=16, fill="#F7FAFA", outline="#D5E3E2", width=2)

    # Browser top bar
    d.rounded_rectangle([0, 0, w, 44], radius=16, fill="#EEF6F5")
    d.rectangle([0, 30, w, 44], fill="#EEF6F5")
    d.line([0, 44, w, 44], fill="#D5E3E2", width=1)

    # 3 dots
    d.ellipse([14, 16, 26, 28], fill="#FF5F56")
    d.ellipse([34, 16, 46, 28], fill="#FFBD2E")
    d.ellipse([54, 16, 66, 28], fill="#27C93F")

    # Address bar (Actual GitHub Pages URL)
    d.rounded_rectangle([84, 10, w - 20, 34], radius=8, fill="#FFFFFF", outline="#D5E3E2")
    d.text((96, 15), "https://ivhan9867.github.io/codeacademy-web/verify.html?certId=CA-2026-88912", fill="#4F6668", font=get_font(9))

    # Editorial Navbar
    d.rectangle([0, 45, w, 84], fill="#F7FAFA")
    d.line([0, 84, w, 84], fill="#D5E3E2", width=1)
    d.rounded_rectangle([20, 53, 42, 75], radius=6, fill="#104246")
    d.text((24, 57), ">_", fill="#FFFFFF", font=get_font(9, bold=True, code=True))
    d.text((50, 56), "CodeAcademy — Verifikasi Sertifikat", fill="#0D2B2E", font=get_font(12, bold=True))

    # Center editorial card (1.5px solid #0D2B2E)
    card_x, card_y = 56, 102
    card_w, card_h = w - 112, 254
    d.rounded_rectangle([card_x, card_y, card_x + card_w, card_y + card_h], radius=14, fill="#FFFFFF", outline="#0D2B2E", width=2)

    d.rounded_rectangle([card_x + 20, card_y + 16, card_x + 218, card_y + 38], radius=10, fill="#E9F6EE", outline="#B9E2C8")
    d.text((card_x + 30, card_y + 21), "✓ KREDENSIAL TERVERIFIKASI", fill="#1F7A45", font=get_font(9, bold=True))

    d.text((card_x + 20, card_y + 48), "AHMAD FAUZI", fill="#0D2B2E", font=get_font(18, bold=True))
    d.text((card_x + 20, card_y + 72), "Telah menyelesaikan kurikulum WebDev & evaluasi proyek akhir resmi.", fill="#4F6668", font=get_font(9))

    d.line([card_x + 20, card_y + 92, card_x + card_w - 20, card_y + 92], fill="#D5E3E2", width=1)

    fields = [
        ("Modul Kursus:", "Fondasi Web: Dari Nol ke Baris Kode Pertama"),
        ("Nilai Akhir Kelulusan:", "88 / 100 (Sangat Memuaskan)"),
        ("ID Kredensial:", "CA-2026-88912"),
        ("Tanggal Terbit:", "14 November 2026"),
        ("Status Keabsahan:", "Sah & Terdaftar di Cloud Firestore")
    ]
    fy = card_y + 104
    for lbl, val in fields:
        d.text((card_x + 20, fy), lbl, fill="#4F6668", font=get_font(9, bold=True))
        d.text((card_x + 172, fy), val, fill="#0D2B2E" if "Sah" not in val else "#1F7A45", font=get_font(9, bold=True))
        fy += 26

    out_p = os.path.join(ASSETS_DIR, "mockup_17_verify_web.png")
    img.save(out_p)
    print(f"Verify web mockup saved to: {out_p}")

if __name__ == "__main__":
    gen_facilitator_web_mockup()
    gen_verify_web_mockup()
