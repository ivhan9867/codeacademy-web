"""
Generate crisp 300-DPI visual elements matching the 'Manual Book - PBA Kel 1.pdf'
design template tailored for CodeAcademy (Android + Web Portal).
"""
import math
import random
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

OUT_DIR = Path("docs/manual_book_assets/template_pba")
OUT_DIR.mkdir(parents=True, exist_ok=True)
LOGO_PATH = Path("app/src/main/res/drawable/ic_codeacademy_logo.webp")

FONT_BOLD = r"C:\Windows\Fonts\segoeuib.ttf"
FONT_REG = r"C:\Windows\Fonts\segoeui.ttf"
FONT_ITALIC = r"C:\Windows\Fonts\segoeuii.ttf"


def load_font(path: str, size: int):
    try:
        return ImageFont.truetype(path, size)
    except Exception:
        return ImageFont.load_default()


def generate_cover() -> Path:
    # High-res A4 portrait canvas (1654 x 2339 ~ 200 DPI, lightweight & sharp in Word)
    w, h = 1654, 2339
    img = Image.new("RGB", (w, h), (5, 22, 66))
    d = ImageDraw.Draw(img)

    # Vertical deep navy -> royal tech blue -> bright cyan floor gradient
    for y in range(h):
        t = y / h
        if t < 0.65:
            u = t / 0.65
            r = int(4 + 10 * u)
            g = int(18 + 56 * u)
            b = int(58 + 115 * u)
        else:
            u = (t - 0.65) / 0.35
            r = int(14 + 28 * u)
            g = int(74 + 92 * u)
            b = int(173 + 68 * u)
        d.line([(0, y), (w, y)], fill=(r, g, b))

    # Perspective circuit/grid lines on floor & vertical data beams
    horizon_y = int(h * 0.66)
    cx = w // 2
    for i in range(-10, 11):
        x_bot = cx + i * 210
        x_top = cx + i * 38
        d.line([(x_top, horizon_y), (x_bot, h - 180)], fill=(45, 165, 245), width=2)
    for k in range(1, 10):
        u = (k / 10) ** 1.5
        yg = int(horizon_y + u * (h - 190 - horizon_y))
        d.line([(0, yg), (w, yg)], fill=(38, 148, 235), width=2)

    rng = random.Random(2026)
    # Tech vertical circuit traces on top/sides
    for _ in range(48):
        x = rng.randint(40, w - 40)
        y1 = rng.randint(20, horizon_y - 60)
        y2 = y1 + rng.randint(80, 360)
        col = (34, 130, 225) if rng.random() > 0.35 else (90, 210, 255)
        d.line([(x, y1), (x, y2)], fill=col, width=3)
        if rng.random() > 0.45:
            d.ellipse([x - 6, y1 - 6, x + 6, y1 + 6], fill=(140, 230, 255))
        if rng.random() > 0.5:
            bend = x + rng.choice([-45, 45])
            d.line([(x, y2), (bend, y2 + 35)], fill=col, width=3)

    # Glow backdrop behind title & logo
    glow = Image.new("RGBA", (w, h), (0, 0, 0, 0))
    dg = ImageDraw.Draw(glow)
    dg.ellipse([cx - 520, 480, cx + 520, 1460], fill=(40, 160, 255, 55))
    img = Image.alpha_composite(img.convert("RGBA"), glow).convert("RGB")
    d = ImageDraw.Draw(img)

    # Title typography
    f_h1 = load_font(FONT_BOLD, 126)
    f_h2 = load_font(FONT_BOLD, 56)
    f_sub = load_font(FONT_ITALIC, 46)

    title1 = "MANUAL BOOK"
    b1 = d.textbbox((0, 0), title1, font=f_h1)
    d.text(((w - (b1[2] - b1[0])) // 2, 230), title1, fill=(255, 255, 255), font=f_h1)

    title2 = "APLIKASI CODEACADEMY"
    b2 = d.textbbox((0, 0), title2, font=f_h2)
    d.text(((w - (b2[2] - b2[0])) // 2, 395), title2, fill=(224, 242, 254), font=f_h2)

    # Rounded square container with official CodeAcademy Codi logo
    box_s = 620
    bx1 = (w - box_s) // 2
    by1 = 620
    # Sample logo corner color so container card matches WebP background seamlessly
    card_fill = (238, 250, 249)
    logo = None
    if LOGO_PATH.exists():
        logo = Image.open(LOGO_PATH).convert("RGBA")
        card_fill = logo.getpixel((6, 6))[:3]
        target = 520
        logo = logo.resize((target, target), Image.Resampling.LANCZOS)

    d.rounded_rectangle(
        [bx1 - 14, by1 - 14, bx1 + box_s + 14, by1 + box_s + 14],
        radius=128,
        fill=(8, 16, 36),
        outline=(56, 189, 248),
        width=6,
    )
    d.rounded_rectangle(
        [bx1, by1, bx1 + box_s, by1 + box_s],
        radius=116,
        fill=card_fill,
    )

    if logo is not None:
        img.paste(logo, (bx1 + (box_s - target) // 2, by1 + (box_s - target) // 2), logo)
        d = ImageDraw.Draw(img)

    # Subtitle lines
    sub1 = "Platform Pembelajaran"
    sub2 = "Web Development & Coding Mobile-First"
    sub3 = "(Aplikasi Android & Portal Web Fasilitator / Super Admin)"
    f_sub_sm = load_font(FONT_REG, 32)
    for idx, (txt, font, y_pos, col) in enumerate([
        (sub1, f_sub, 1470, (255, 255, 255)),
        (sub2, f_sub, 1540, (255, 255, 255)),
        (sub3, f_sub_sm, 1630, (186, 230, 253)),
    ]):
        bb = d.textbbox((0, 0), txt, font=font)
        d.text(((w - (bb[2] - bb[0])) // 2, y_pos), txt, fill=col, font=font)

    # Bottom white bar + PBA Kel 1 ribbon footer
    bar_top = h - 180
    d.rectangle([0, bar_top, w, h], fill=(255, 255, 255))
    d.rectangle([0, h - 16, w, h], fill=(15, 42, 130))

    # Dark blue chevron ribbon left
    rib_y1, rib_y2 = bar_top + 38, bar_top + 116
    rib_mid = (rib_y1 + rib_y2) // 2
    d.polygon([(0, rib_y1), (680, rib_y1), (735, rib_mid), (680, rib_y2), (0, rib_y2)], fill=(15, 42, 130))
    f_rib = load_font(FONT_BOLD, 30)
    d.text((34, rib_y1 + 20), "Pengembangan Bahan Ajar Edisi I 2026", fill=(255, 255, 255), font=f_rib)

    # Two light-blue chevron arrows
    for offset, col in [(705, (37, 99, 235)), (845, (96, 165, 250))]:
        pts = [
            (offset, rib_y1),
            (offset + 120, rib_y1),
            (offset + 175, rib_mid),
            (offset + 120, rib_y2),
            (offset, rib_y2),
            (offset + 55, rib_mid),
        ]
        d.polygon(pts, fill=col)

    # Right page badge 1 + >>>
    cx_p = w - 165
    d.ellipse([cx_p - 40, rib_mid - 40, cx_p + 40, rib_mid + 40], fill=(15, 42, 130))
    f_pg = load_font(FONT_BOLD, 32)
    bp = d.textbbox((0, 0), "1", font=f_pg)
    d.text((cx_p - (bp[2] - bp[0]) // 2, rib_mid - 20), "1", fill=(255, 255, 255), font=f_pg)
    f_chev = load_font(FONT_BOLD, 38)
    d.text((w - 102, rib_mid - 24), ">>>", fill=(37, 99, 235), font=f_chev)

    out = OUT_DIR / "cover_pba_codeacademy.png"
    img.save(out, format="PNG")
    return out


def generate_toc_banner() -> Path:
    # Full-width top light-blue tint header with pixel geometric blue blocks on right
    w, h = 1654, 250
    img = Image.new("RGB", (w, h), (235, 243, 255))
    d = ImageDraw.Draw(img)

    # Pixel blocks at top right (matching Page 2 of Manual Book - PBA Kel 1.pdf)
    blk_w, blk_h = 155, 48
    blocks = [
        (w - blk_w * 4, 0, (15, 42, 130)),
        (w - blk_w * 2, 0, (29, 78, 216)),
        (w - blk_w * 1, 0, (15, 42, 130)),
        (w - blk_w * 3, blk_h, (37, 99, 235)),
        (w - blk_w * 2, blk_h, (15, 42, 130)),
        (w - blk_w * 1, blk_h * 2, (29, 78, 216)),
    ]
    for x, y, col in blocks:
        d.rectangle([x, y, x + blk_w, y + blk_h], fill=col)

    f_title = load_font(FONT_BOLD, 92)
    d.text((90, 95), "DAFTAR ISI", fill=(17, 24, 39), font=f_title)
    out = OUT_DIR / "toc_banner_pba.png"
    img.save(out, format="PNG")
    return out


def generate_chapter_badges() -> None:
    s = 220
    f_num = load_font(FONT_BOLD, 108)
    for ch in range(1, 22):
        img = Image.new("RGBA", (s, s), (0, 0, 0, 0))
        d = ImageDraw.Draw(img)
        d.ellipse([10, 10, s - 10, s - 10], fill=(15, 42, 130))
        txt = str(ch)
        bb = d.textbbox((0, 0), txt, font=f_num)
        tw, th = bb[2] - bb[0], bb[3] - bb[1]
        d.text(((s - tw) // 2 - bb[0], (s - th) // 2 - bb[1] - 4), txt, fill=(255, 255, 255), font=f_num)
        img.save(OUT_DIR / f"ch_badge_{ch:02d}.png", format="PNG")


def generate_diamond_check() -> Path:
    s = 96
    img = Image.new("RGBA", (s, s), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    cx, cy, r = s // 2, s // 2, 36
    pts = [(cx, cy - r), (cx + r, cy), (cx, cy + r), (cx - r, cy)]
    d.polygon(pts, fill=(239, 246, 255), outline=(29, 78, 216), width=5)
    # Checkmark inside diamond
    d.line([(cx - 14, cy + 2), (cx - 3, cy + 13), (cx + 17, cy - 10)], fill=(15, 42, 130), width=7)
    out = OUT_DIR / "diamond_check.png"
    img.save(out, format="PNG")
    return out


def generate_footer_ribbon() -> Path:
    w, h = 1120, 88
    img = Image.new("RGBA", (w, h), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    y1, y2 = 8, 78
    ym = (y1 + y2) // 2
    d.polygon([(0, y1), (700, y1), (748, ym), (700, y2), (0, y2)], fill=(15, 42, 130))
    f_rib = load_font(FONT_BOLD, 30)
    d.text((24, y1 + 16), "Pengembangan Bahan Ajar Edisi I 2026", fill=(255, 255, 255), font=f_rib)
    for offset, col in [(722, (37, 99, 235)), (862, (96, 165, 250))]:
        pts = [
            (offset, y1),
            (offset + 118, y1),
            (offset + 166, ym),
            (offset + 118, y2),
            (offset, y2),
            (offset + 48, ym),
        ]
        d.polygon(pts, fill=col)
    out = OUT_DIR / "footer_ribbon_pba.png"
    img.save(out, format="PNG")
    return out


def main() -> None:
    print("Generated:", generate_cover())
    print("Generated:", generate_toc_banner())
    generate_chapter_badges()
    print("Generated:", generate_diamond_check())
    print("Generated:", generate_footer_ribbon())


if __name__ == "__main__":
    main()
