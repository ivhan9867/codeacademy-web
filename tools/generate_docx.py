import io
import os
import re
import shutil
from pathlib import Path

from docx import Document
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn
from docx.shared import Cm, Inches, Pt, RGBColor

try:
    from PIL import Image
except ImportError:
    Image = None

MD_PATH = Path("docs/Manual_Book_CodeAcademy.md")
ASSETS_DIR = Path("docs")
LOGO_PATH = Path("app/src/main/res/drawable/ic_codeacademy_logo.webp")

OUT_DOCS = Path("docs/Manual_Book_CodeAcademy.docx")
OUT_ROOT = Path("Manual Book CodeAcademy.docx")
OUT_PUBLIC = Path("public/docs/Manual_Book_CodeAcademy.docx")

C_NAVY_DARK = RGBColor(0x10, 0x42, 0x46)
C_TEAL = RGBColor(0x1A, 0x63, 0x68)
C_CORAL = RGBColor(0xDF, 0x7B, 0x56)
C_TEXT = RGBColor(0x24, 0x21, 0x1D)
C_MUTED = RGBColor(0x5A, 0x62, 0x6A)
C_WHITE = RGBColor(0xFF, 0xFF, 0xFF)


def set_cell_shading(cell, fill_hex: str) -> None:
    shading = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    cell._tc.get_or_add_tcPr().append(shading)


def set_cell_margins(cell, top=100, bottom=100, left=140, right=140) -> None:
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement("w:tcMar")
    for name, val in [("top", top), ("bottom", bottom), ("left", left), ("right", right)]:
        node = OxmlElement(f"w:{name}")
        node.set(qn("w:w"), str(val))
        node.set(qn("w:type"), "dxa")
        tcMar.append(node)
    tcPr.append(tcMar)


def remove_table_borders(table) -> None:
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        '<w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        '<w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        '<w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        '<w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        '<w:insideH w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        '<w:insideV w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        "</w:tblBorders>"
    )
    tblPr.append(borders)


def set_table_light_borders(table, color="D8DEE4") -> None:
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        f'<w:top w:val="single" w:sz="4" w:space="0" w:color="{color}"/>'
        f'<w:left w:val="single" w:sz="4" w:space="0" w:color="{color}"/>'
        f'<w:bottom w:val="single" w:sz="4" w:space="0" w:color="{color}"/>'
        f'<w:right w:val="single" w:sz="4" w:space="0" w:color="{color}"/>'
        f'<w:insideH w:val="single" w:sz="4" w:space="0" w:color="{color}"/>'
        f'<w:insideV w:val="single" w:sz="4" w:space="0" w:color="{color}"/>'
        "</w:tblBorders>"
    )
    tblPr.append(borders)


def add_paragraph_bottom_border(paragraph, color="104246", sz="12") -> None:
    pPr = paragraph._p.get_or_add_pPr()
    pBdr = parse_xml(
        f'<w:pBdr {nsdecls("w")}>'
        f'<w:bottom w:val="single" w:sz="{sz}" w:space="4" w:color="{color}"/>'
        "</w:pBdr>"
    )
    pPr.append(pBdr)


def add_page_number_field(run) -> None:
    fldChar1 = parse_xml(r'<w:fldChar %s w:fldCharType="begin"/>' % nsdecls("w"))
    instrText = parse_xml(r'<w:instrText %s xml:space="preserve"> PAGE </w:instrText>' % nsdecls("w"))
    fldChar2 = parse_xml(r'<w:fldChar %s w:fldCharType="separate"/>' % nsdecls("w"))
    fldChar3 = parse_xml(r'<w:fldChar %s w:fldCharType="end"/>' % nsdecls("w"))
    run._r.append(fldChar1)
    run._r.append(instrText)
    run._r.append(fldChar2)
    run._r.append(fldChar3)


def add_rich_runs(paragraph, text: str, base_size=10.5, base_color=C_TEXT, base_bold=False) -> None:
    """Parse **bold**, *italic*, and `code` inline markdown into Word runs."""
    pattern = re.compile(r"(\*\*[^*]+\*\*|\*[^*]+\*|`[^`]+`)")
    pos = 0
    for match in pattern.finditer(text):
        if match.start() > pos:
            r = paragraph.add_run(text[pos : match.start()])
            r.font.name = "Calibri"
            r.font.size = Pt(base_size)
            r.font.color.rgb = base_color
            r.bold = base_bold
        token = match.group(0)
        if token.startswith("**") and token.endswith("**"):
            r = paragraph.add_run(token[2:-2])
            r.font.name = "Calibri"
            r.font.size = Pt(base_size)
            r.font.color.rgb = base_color
            r.bold = True
        elif token.startswith("*") and token.endswith("*"):
            r = paragraph.add_run(token[1:-1])
            r.font.name = "Calibri"
            r.font.size = Pt(base_size)
            r.font.color.rgb = base_color
            r.bold = base_bold
            r.italic = True
        elif token.startswith("`") and token.endswith("`"):
            r = paragraph.add_run(token[1:-1])
            r.font.name = "Consolas"
            r.font.size = Pt(max(base_size - 0.5, 8.5))
            r.font.color.rgb = C_NAVY_DARK
            r.bold = base_bold
        pos = match.end()
    if pos < len(text):
        r = paragraph.add_run(text[pos:])
        r.font.name = "Calibri"
        r.font.size = Pt(base_size)
        r.font.color.rgb = base_color
        r.bold = base_bold


def load_image_stream(path: Path):
    if not path.exists():
        return None
    if path.suffix.lower() == ".webp" and Image is not None:
        buf = io.BytesIO()
        with Image.open(path) as img:
            img.save(buf, format="PNG")
        buf.seek(0)
        return buf
    return str(path)


def image_width_cm(path: Path, in_two_col: bool = False) -> float:
    if in_two_col:
        return 6.6
    if Image is not None and path.exists():
        with Image.open(path) as img:
            w, h = img.size
        if w > h * 1.15:
            return 15.5
    return 7.6


def configure_document() -> Document:
    doc = Document()

    normal = doc.styles["Normal"]
    normal.font.name = "Calibri"
    normal.font.size = Pt(10.5)
    normal.font.color.rgb = C_TEXT

    sec = doc.sections[0]
    sec.page_width = Cm(21.0)
    sec.page_height = Cm(29.7)
    sec.top_margin = Cm(2.0)
    sec.bottom_margin = Cm(2.0)
    sec.left_margin = Cm(2.0)
    sec.right_margin = Cm(2.0)
    sec.different_first_page_header_footer = True

    # Running Header
    hdr = sec.header
    hp = hdr.paragraphs[0]
    hp.paragraph_format.space_after = Pt(4)
    hr = hp.add_run("MANUAL BOOK APLIKASI CODEACADEMY (VERSI MOBILE)")
    hr.font.name = "Calibri"
    hr.font.size = Pt(9)
    hr.font.bold = True
    hr.font.color.rgb = C_NAVY_DARK
    add_paragraph_bottom_border(hp, color="104246", sz="8")

    # Running Footer
    ftr = sec.footer
    ftbl = ftr.add_table(rows=1, cols=2, width=Cm(17.0))
    ftbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    remove_table_borders(ftbl)
    c_left, c_right = ftbl.rows[0].cells
    c_left.width = Cm(12.0)
    c_right.width = Cm(5.0)
    pl = c_left.paragraphs[0]
    rl = pl.add_run("CodeAcademy Mobile — Panduan Pengguna")
    rl.font.name = "Calibri"
    rl.font.size = Pt(8.5)
    rl.font.color.rgb = C_MUTED

    pr = c_right.paragraphs[0]
    pr.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    rr_lbl = pr.add_run("Halaman ")
    rr_lbl.font.name = "Calibri"
    rr_lbl.font.size = Pt(8.5)
    rr_lbl.font.color.rgb = C_MUTED
    rr_pg = pr.add_run()
    rr_pg.font.name = "Calibri"
    rr_pg.font.size = Pt(8.5)
    rr_pg.font.bold = True
    rr_pg.font.color.rgb = C_NAVY_DARK
    add_page_number_field(rr_pg)

    return doc


def build_cover_page(doc: Document) -> None:
    p_spacer = doc.add_paragraph()
    p_spacer.paragraph_format.space_before = Pt(36)

    logo_stream = load_image_stream(LOGO_PATH)
    if logo_stream is not None:
        p_logo = doc.add_paragraph()
        p_logo.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_logo.paragraph_format.space_after = Pt(18)
        p_logo.add_run().add_picture(logo_stream, width=Cm(3.2))

    p_badge = doc.add_paragraph()
    p_badge.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_badge.paragraph_format.space_after = Pt(10)
    rb = p_badge.add_run("DOKUMEN PANDUAN PENGGUNA • LAMPIRAN TUGAS AKHIR")
    rb.font.name = "Calibri"
    rb.font.size = Pt(10)
    rb.font.bold = True
    rb.font.color.rgb = C_CORAL

    p_title = doc.add_paragraph()
    p_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_title.paragraph_format.space_after = Pt(4)
    rt = p_title.add_run("MANUAL BOOK APLIKASI CODEACADEMY")
    rt.font.name = "Calibri"
    rt.font.size = Pt(24)
    rt.font.bold = True
    rt.font.color.rgb = C_NAVY_DARK

    p_ver = doc.add_paragraph()
    p_ver.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_ver.paragraph_format.space_after = Pt(14)
    rv = p_ver.add_run("(VERSI MOBILE — ANDROID)")
    rv.font.name = "Calibri"
    rv.font.size = Pt(15)
    rv.font.bold = True
    rv.font.color.rgb = C_TEAL

    p_sub = doc.add_paragraph()
    p_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_sub.paragraph_format.space_after = Pt(36)
    rs = p_sub.add_run("Platform Pembelajaran Web Development & Coding Mobile-First")
    rs.font.name = "Calibri"
    rs.font.size = Pt(12)
    rs.font.italic = True
    rs.font.color.rgb = C_MUTED

    # Metadata summary card
    tbl = doc.add_table(rows=5, cols=2)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_light_borders(tbl, color="D8DEE4")
    meta_rows = [
        ("Nama Aplikasi", "CodeAcademy Mobile (Android)"),
        ("Jalur Kurikulum", "Front-End Web Development (HTML5, CSS3, Flexbox/Grid, JavaScript ES6)"),
        ("Layanan AI & Cloud", "Firebase Authentication, Cloud Firestore & Firebase AI Logic (Gemini)"),
        ("Portal Web Pendamping", "Portal Fasilitator & Portal Verifikasi Sertifikat Digital"),
        ("Tahun Dokumen", "2026"),
    ]
    for idx, (k, v) in enumerate(meta_rows):
        c0, c1 = tbl.rows[idx].cells
        c0.width = Cm(5.5)
        c1.width = Cm(11.0)
        set_cell_shading(c0, "F2F7F7")
        set_cell_shading(c1, "FFFFFF")
        set_cell_margins(c0, top=110, bottom=110, left=140, right=140)
        set_cell_margins(c1, top=110, bottom=110, left=140, right=140)
        p0 = c0.paragraphs[0]
        r0 = p0.add_run(k)
        r0.font.name = "Calibri"
        r0.font.size = Pt(10)
        r0.font.bold = True
        r0.font.color.rgb = C_NAVY_DARK

        p1 = c1.paragraphs[0]
        r1 = p1.add_run(v)
        r1.font.name = "Calibri"
        r1.font.size = Pt(10)
        r1.font.color.rgb = C_TEXT

    doc.add_page_break()


def parse_md_table_row(line: str) -> list[str]:
    stripped = line.strip()
    if stripped.startswith("|"):
        stripped = stripped[1:]
    if stripped.endswith("|"):
        stripped = stripped[:-1]
    return [cell.strip() for cell in stripped.split("|")]


def add_two_col_image_table(doc: Document, rows: list[list[str]]) -> None:
    if len(rows) < 3:
        return
    headers = rows[0]
    img_cells = rows[2]
    cap_cells = rows[3] if len(rows) > 3 else ["", ""]

    tbl = doc.add_table(rows=3, cols=2)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    remove_table_borders(tbl)

    for col in range(2):
        # Row 0: title
        c_title = tbl.rows[0].cells[col]
        c_title.width = Cm(8.3)
        set_cell_margins(c_title, top=60, bottom=40, left=80, right=80)
        pt = c_title.paragraphs[0]
        pt.alignment = WD_ALIGN_PARAGRAPH.CENTER
        add_rich_runs(pt, headers[col] if col < len(headers) else "", base_size=9.5, base_color=C_NAVY_DARK, base_bold=True)

        # Row 1: image
        c_img = tbl.rows[1].cells[col]
        c_img.width = Cm(8.3)
        c_img.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
        set_cell_margins(c_img, top=40, bottom=40, left=80, right=80)
        pi = c_img.paragraphs[0]
        pi.alignment = WD_ALIGN_PARAGRAPH.CENTER
        m = re.search(r"!\[[^\]]*\]\(([^)]+)\)", img_cells[col] if col < len(img_cells) else "")
        if m:
            img_path = ASSETS_DIR / m.group(1)
            stream = load_image_stream(img_path)
            if stream is not None:
                pi.add_run().add_picture(stream, width=Cm(image_width_cm(img_path, in_two_col=True)))

        # Row 2: caption
        c_cap = tbl.rows[2].cells[col]
        c_cap.width = Cm(8.3)
        set_cell_margins(c_cap, top=20, bottom=100, left=80, right=80)
        pc = c_cap.paragraphs[0]
        pc.alignment = WD_ALIGN_PARAGRAPH.CENTER
        cap_txt = cap_cells[col] if col < len(cap_cells) else ""
        add_rich_runs(pc, cap_txt, base_size=9.0, base_color=C_MUTED)


def render_markdown_body(doc: Document, md_text: str) -> None:
    lines = md_text.splitlines()
    # Skip cover header lines (first 5 lines)
    i = 0
    while i < len(lines) and i < 6:
        if lines[i].strip().startswith("## DAFTAR ISI"):
            break
        i += 1

    first_h2_seen = False
    while i < len(lines):
        line = lines[i].rstrip()
        stripped = line.strip()

        if not stripped or stripped == "---":
            i += 1
            continue

        # Two-column markdown image table
        if stripped.startswith("|") and i + 1 < len(lines) and re.match(r"^\|\s*:?-+:?\s*\|", lines[i + 1].strip()):
            tbl_rows = []
            while i < len(lines) and lines[i].strip().startswith("|"):
                tbl_rows.append(parse_md_table_row(lines[i]))
                i += 1
            add_two_col_image_table(doc, tbl_rows)
            continue

        # H2 Section Heading
        if stripped.startswith("## "):
            title_txt = stripped[3:].strip()
            if first_h2_seen and title_txt.startswith("1. "):
                doc.add_page_break()
            first_h2_seen = True
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(14)
            p.paragraph_format.space_after = Pt(6)
            p.paragraph_format.keep_with_next = True
            r = p.add_run(title_txt)
            r.font.name = "Calibri"
            r.font.size = Pt(14)
            r.font.bold = True
            r.font.color.rgb = C_NAVY_DARK
            add_paragraph_bottom_border(p, color="1A6368", sz="8")
            i += 1
            continue

        # H3 Subheading
        if stripped.startswith("### "):
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(10)
            p.paragraph_format.space_after = Pt(4)
            p.paragraph_format.keep_with_next = True
            r = p.add_run(stripped[4:].strip())
            r.font.name = "Calibri"
            r.font.size = Pt(11.5)
            r.font.bold = True
            r.font.color.rgb = C_TEAL
            i += 1
            continue

        # H4 Subheading
        if stripped.startswith("#### "):
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(8)
            p.paragraph_format.space_after = Pt(3)
            p.paragraph_format.keep_with_next = True
            r = p.add_run(stripped[5:].strip())
            r.font.name = "Calibri"
            r.font.size = Pt(10.5)
            r.font.bold = True
            r.font.color.rgb = C_NAVY_DARK
            i += 1
            continue

        # Standalone Image
        img_match = re.match(r"^!\[([^\]]*)\]\(([^)]+)\)$", stripped)
        if img_match:
            img_path = ASSETS_DIR / img_match.group(2)
            stream = load_image_stream(img_path)
            if stream is not None:
                p_img = doc.add_paragraph()
                p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
                p_img.paragraph_format.space_before = Pt(6)
                p_img.paragraph_format.space_after = Pt(3)
                p_img.paragraph_format.keep_with_next = True
                p_img.add_run().add_picture(stream, width=Cm(image_width_cm(img_path, in_two_col=False)))
            i += 1
            if i < len(lines) and lines[i].strip().startswith("*") and lines[i].strip().endswith("*"):
                p_cap = doc.add_paragraph()
                p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
                p_cap.paragraph_format.space_after = Pt(8)
                add_rich_runs(p_cap, lines[i].strip(), base_size=9.0, base_color=C_MUTED)
                i += 1
            continue

        # Nested bullet ("   - ...")
        if re.match(r"^\s{2,}-\s+", line):
            item_txt = re.sub(r"^\s+-\s+", "", line)
            p = doc.add_paragraph(style="List Bullet 2")
            p.paragraph_format.space_after = Pt(2)
            add_rich_runs(p, item_txt, base_size=10.0)
            i += 1
            continue

        # Bullet ("- ...")
        if stripped.startswith("- "):
            p = doc.add_paragraph(style="List Bullet")
            p.paragraph_format.space_after = Pt(3)
            add_rich_runs(p, stripped[2:].strip(), base_size=10.5)
            i += 1
            continue

        # Numbered list ("1. ...")
        num_match = re.match(r"^(\d+)\.\s+(.*)$", stripped)
        if num_match:
            p = doc.add_paragraph()
            p.paragraph_format.left_indent = Cm(0.75)
            p.paragraph_format.first_line_indent = Cm(-0.5)
            p.paragraph_format.space_after = Pt(3)
            r_num = p.add_run(f"{num_match.group(1)}. ")
            r_num.font.name = "Calibri"
            r_num.font.size = Pt(10.5)
            r_num.font.bold = True
            r_num.font.color.rgb = C_TEAL
            add_rich_runs(p, num_match.group(2), base_size=10.5)
            i += 1
            continue

        # Normal paragraph (join soft-wrapped lines)
        para_parts = [stripped]
        i += 1
        while (
            i < len(lines)
            and lines[i].strip()
            and not lines[i].strip().startswith(("#", "-", "|", "!", "*"))
            and not re.match(r"^\d+\.\s+", lines[i].strip())
            and lines[i].strip() != "---"
        ):
            para_parts.append(lines[i].strip())
            i += 1
        p = doc.add_paragraph()
        p.paragraph_format.space_after = Pt(6)
        p.paragraph_format.line_spacing = 1.15
        add_rich_runs(p, " ".join(para_parts), base_size=10.5)


def main() -> None:
    md_text = MD_PATH.read_text(encoding="utf-8")
    doc = configure_document()
    build_cover_page(doc)
    render_markdown_body(doc, md_text)

    OUT_DOCS.parent.mkdir(parents=True, exist_ok=True)
    OUT_PUBLIC.parent.mkdir(parents=True, exist_ok=True)

    doc.save(OUT_DOCS)
    shutil.copy2(OUT_DOCS, OUT_ROOT)
    shutil.copy2(OUT_DOCS, OUT_PUBLIC)

    print("Generated:", OUT_DOCS, OUT_DOCS.stat().st_size, "bytes")
    print("Copied:", OUT_ROOT, OUT_ROOT.stat().st_size, "bytes")
    print("Copied:", OUT_PUBLIC, OUT_PUBLIC.stat().st_size, "bytes")


if __name__ == "__main__":
    main()
