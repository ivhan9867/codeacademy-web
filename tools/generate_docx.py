"""
Generate Manual_Book_CodeAcademy.docx following the 'Manual Book - PBA Kel 1.pdf'
visual template (blue circuit cover + Codi icon, geometric TOC, PBA chapter circle
badges, diamond-check subheadings, 01/02/03 step rows, data tables & footer ribbon).
"""
import io
import os
import re
import shutil
from pathlib import Path

from docx import Document
from docx.enum.section import WD_SECTION
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn
from docx.shared import Cm, Pt, RGBColor

try:
    from PIL import Image
except ImportError:
    Image = None

MD_PATH = Path("docs/Manual_Book_CodeAcademy.md")
ASSETS_DIR = Path("docs")
TPL_DIR = Path("docs/manual_book_assets/template_pba")

OUT_DOCS = Path("docs/Manual_Book_CodeAcademy.docx")
OUT_ROOT = Path("Manual Book CodeAcademy.docx")
OUT_PUBLIC = Path("public/docs/Manual_Book_CodeAcademy.docx")

# PBA Kel 1 Royal Tech Blue + Editorial Neutral Palette
C_NAVY = RGBColor(0x0F, 0x2A, 0x82)
C_BLUE = RGBColor(0x1D, 0x4E, 0xD8)
C_BLUE_SOFT = RGBColor(0x25, 0x63, 0xEB)
C_TEAL = RGBColor(0x1A, 0x63, 0x68)
C_DARK = RGBColor(0x11, 0x18, 0x27)
C_TEXT = RGBColor(0x1F, 0x29, 0x37)
C_MUTED = RGBColor(0x4B, 0x55, 0x63)
C_WHITE = RGBColor(0xFF, 0xFF, 0xFF)


def set_cell_shading(cell, fill_hex: str) -> None:
    shading = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    cell._tc.get_or_add_tcPr().append(shading)


def set_cell_margins(cell, top=80, bottom=80, left=120, right=120) -> None:
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


def set_horizontal_only_borders(table, color="CBD5E1", sz="4") -> None:
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        '<w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        '<w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        f'<w:bottom w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        '<w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        f'<w:insideH w:val="single" w:sz="{sz}" w:space="0" w:color="{color}"/>'
        '<w:insideV w:val="none" w:sz="0" w:space="0" w:color="auto"/>'
        "</w:tblBorders>"
    )
    tblPr.append(borders)


def set_table_light_borders(table, color="CBD5E1") -> None:
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


def add_page_number_field(paragraph, font_size=10.5, color=C_WHITE) -> None:
    def make_run(text="", fld=None):
        r = paragraph.add_run(text)
        r.font.name = "Calibri"
        r.font.size = Pt(font_size)
        r.font.bold = True
        r.font.color.rgb = color
        if fld is not None:
            r._r.append(fld)
        return r

    make_run(fld=parse_xml(r'<w:fldChar %s w:fldCharType="begin"/>' % nsdecls("w")))
    make_run(fld=parse_xml(r'<w:instrText %s xml:space="preserve"> PAGE </w:instrText>' % nsdecls("w")))
    make_run(fld=parse_xml(r'<w:fldChar %s w:fldCharType="separate"/>' % nsdecls("w")))
    make_run("2")
    make_run(fld=parse_xml(r'<w:fldChar %s w:fldCharType="end"/>' % nsdecls("w")))


def add_rich_runs(paragraph, text: str, base_size=10.5, base_color=C_TEXT, base_bold=False) -> None:
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
            r.font.color.rgb = C_DARK if base_color == C_TEXT else base_color
            r.bold = True
        elif token.startswith("*") and token.endswith("*"):
            r = paragraph.add_run(token[1:-1])
            r.font.name = "Calibri"
            r.font.size = Pt(base_size)
            r.font.color.rgb = base_color
            r.italic = True
            r.bold = base_bold
        elif token.startswith("`") and token.endswith("`"):
            r = paragraph.add_run(token[1:-1])
            r.font.name = "Consolas"
            r.font.size = Pt(max(base_size - 0.8, 8.5))
            r.font.color.rgb = C_NAVY
            r.bold = True
        pos = match.end()
    if pos < len(text):
        r = paragraph.add_run(text[pos:])
        r.font.name = "Calibri"
        r.font.size = Pt(base_size)
        r.font.color.rgb = base_color
        r.bold = base_bold


def load_image_stream(path: Path) -> io.BytesIO | None:
    if not path.exists():
        return None
    if Image is not None:
        im = Image.open(path)
        if im.mode in ("P", "LA"):
            im = im.convert("RGBA")
        elif im.mode not in ("RGB", "RGBA"):
            im = im.convert("RGB")
        buf = io.BytesIO()
        im.save(buf, format="PNG")
        buf.seek(0)
        return buf
    return io.BytesIO(path.read_bytes())


def image_width_cm(path: Path, in_two_col: bool = False) -> float:
    if in_two_col:
        return 6.6
    if Image is None or not path.exists():
        return 8.5
    with Image.open(path) as im:
        w, h = im.size
    ratio = w / max(h, 1)
    if ratio > 1.3:
        return 14.2  # Web & onboarding landscape mockups
    return 7.0       # Mobile portrait mockups


def enable_auto_update_fields(doc: Document) -> None:
    settings = doc.settings.element
    if settings.find(qn("w:updateFields")) is None:
        settings.append(parse_xml(f'<w:updateFields {nsdecls("w")} w:val="true"/>'))


def add_bookmark(paragraph, name: str, bm_id: int) -> None:
    start = parse_xml(f'<w:bookmarkStart {nsdecls("w")} w:id="{bm_id}" w:name="{name}"/>')
    end = parse_xml(f'<w:bookmarkEnd {nsdecls("w")} w:id="{bm_id}"/>')
    paragraph._p.insert(0, start)
    paragraph._p.append(end)


def add_pageref_field(paragraph, bookmark_name: str, fallback: str, font_size=10.5, color=C_BLUE) -> None:
    def make_run(text="", fld=None):
        r = paragraph.add_run(text)
        r.font.name = "Calibri"
        r.font.size = Pt(font_size)
        r.font.bold = True
        r.font.color.rgb = color
        if fld is not None:
            r._r.append(fld)
        return r

    make_run(fld=parse_xml(r'<w:fldChar %s w:fldCharType="begin"/>' % nsdecls("w")))
    make_run(fld=parse_xml(r'<w:instrText %s xml:space="preserve"> PAGEREF %s \h </w:instrText>' % (nsdecls("w"), bookmark_name)))
    make_run(fld=parse_xml(r'<w:fldChar %s w:fldCharType="separate"/>' % nsdecls("w")))
    make_run(fallback)
    make_run(fld=parse_xml(r'<w:fldChar %s w:fldCharType="end"/>' % nsdecls("w")))


def configure_document() -> Document:
    doc = Document()
    enable_auto_update_fields(doc)
    normal = doc.styles["Normal"]
    normal.font.name = "Calibri"
    normal.font.size = Pt(10.5)
    normal.font.color.rgb = C_TEXT

    # Section 0: Full-bleed Cover Page (A4, zero margins, no header/footer)
    sec0 = doc.sections[0]
    sec0.page_width = Cm(21.0)
    sec0.page_height = Cm(29.7)
    sec0.top_margin = Cm(0.0)
    sec0.bottom_margin = Cm(0.0)
    sec0.left_margin = Cm(0.0)
    sec0.right_margin = Cm(0.0)
    sec0.header_distance = Cm(0.0)
    sec0.footer_distance = Cm(0.0)
    sec0.header.is_linked_to_previous = False
    sec0.footer.is_linked_to_previous = False
    return doc


def build_cover_page(doc: Document) -> None:
    cover_img = TPL_DIR / "cover_pba_codeacademy.png"
    stream = load_image_stream(cover_img)
    p = doc.paragraphs[0] if doc.paragraphs else doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(0)
    if stream is not None:
        p.add_run().add_picture(stream, width=Cm(21.0))

    # Section 1: Content pages with PBA Kel 1 ribbon footer
    sec1 = doc.add_section(WD_SECTION.NEW_PAGE)
    sec1.page_width = Cm(21.0)
    sec1.page_height = Cm(29.7)
    sec1.top_margin = Cm(1.4)
    sec1.bottom_margin = Cm(2.4)
    sec1.left_margin = Cm(1.8)
    sec1.right_margin = Cm(1.8)
    sec1.header_distance = Cm(0.7)
    sec1.footer_distance = Cm(0.55)
    sec1.header.is_linked_to_previous = False
    sec1.footer.is_linked_to_previous = False

    # Running Header (subtle top rule + doc label)
    hp = sec1.header.paragraphs[0]
    hp.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    hr = hp.add_run("MANUAL BOOK CODEACADEMY — ANDROID & WEB PORTAL")
    hr.font.name = "Calibri"
    hr.font.size = Pt(8.5)
    hr.font.bold = True
    hr.font.color.rgb = C_MUTED

    # Running Footer matching PBA Kel 1 ribbon + chevron + PAGE number + >>>
    footer = sec1.footer
    fp_clear = footer.paragraphs[0]
    fp_clear.paragraph_format.space_before = Pt(0)
    fp_clear.paragraph_format.space_after = Pt(2)

    ftbl = footer.add_table(rows=1, cols=3, width=Cm(17.4))
    ftbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    remove_table_borders(ftbl)

    c_rib, c_pg, c_chev = ftbl.rows[0].cells
    c_rib.width = Cm(14.0)
    c_pg.width = Cm(1.2)
    c_chev.width = Cm(2.2)

    set_cell_margins(c_rib, top=20, bottom=20, left=0, right=40)
    p_rib = c_rib.paragraphs[0]
    rib_stream = load_image_stream(TPL_DIR / "footer_ribbon_pba.png")
    if rib_stream is not None:
        p_rib.add_run().add_picture(rib_stream, width=Cm(11.6))

    set_cell_shading(c_pg, "0F2A82")
    set_cell_margins(c_pg, top=60, bottom=60, left=60, right=60)
    c_pg.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
    p_pg = c_pg.paragraphs[0]
    p_pg.alignment = WD_ALIGN_PARAGRAPH.CENTER
    add_page_number_field(p_pg, font_size=10.0, color=C_WHITE)

    set_cell_margins(c_chev, top=60, bottom=60, left=80, right=0)
    c_chev.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
    p_chev = c_chev.paragraphs[0]
    rc = p_chev.add_run(">>>")
    rc.font.name = "Calibri"
    rc.font.size = Pt(11.5)
    rc.font.bold = True
    rc.font.color.rgb = C_BLUE_SOFT

    # Bottom thin dark-blue bar in footer
    p_bar = footer.add_paragraph()
    p_bar.paragraph_format.space_before = Pt(3)
    p_bar.paragraph_format.space_after = Pt(0)
    pBdr = parse_xml(
        f'<w:pBdr {nsdecls("w")}>'
        '<w:bottom w:val="single" w:sz="18" w:space="1" w:color="0F2A82"/>'
        "</w:pBdr>"
    )
    p_bar._p.get_or_add_pPr().append(pBdr)


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
        c_title = tbl.rows[0].cells[col]
        c_title.width = Cm(8.7)
        set_cell_margins(c_title, top=50, bottom=30, left=80, right=80)
        pt = c_title.paragraphs[0]
        pt.alignment = WD_ALIGN_PARAGRAPH.CENTER
        add_rich_runs(pt, headers[col] if col < len(headers) else "", base_size=9.5, base_color=C_NAVY, base_bold=True)

        c_img = tbl.rows[1].cells[col]
        c_img.width = Cm(8.7)
        c_img.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
        set_cell_margins(c_img, top=30, bottom=30, left=80, right=80)
        pi = c_img.paragraphs[0]
        pi.alignment = WD_ALIGN_PARAGRAPH.CENTER
        m = re.search(r"!\[[^\]]*\]\(([^)]+)\)", img_cells[col] if col < len(img_cells) else "")
        if m:
            img_path = ASSETS_DIR / m.group(1)
            stream = load_image_stream(img_path)
            if stream is not None:
                pi.add_run().add_picture(stream, width=Cm(image_width_cm(img_path, in_two_col=True)))

        c_cap = tbl.rows[2].cells[col]
        c_cap.width = Cm(8.7)
        set_cell_margins(c_cap, top=20, bottom=90, left=80, right=80)
        pc = c_cap.paragraphs[0]
        pc.alignment = WD_ALIGN_PARAGRAPH.CENTER
        cap_txt = cap_cells[col] if col < len(cap_cells) else ""
        add_rich_runs(pc, cap_txt, base_size=9.0, base_color=C_MUTED)


def add_data_table(doc: Document, rows: list[list[str]]) -> None:
    if len(rows) < 2:
        return
    headers = rows[0]
    data_rows = rows[2:] if len(rows) > 2 else []
    ncols = max(len(headers), 1)
    tbl = doc.add_table(rows=1 + len(data_rows), cols=ncols)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_light_borders(tbl, color="CBD5E1")

    col_w = 17.4 / ncols
    for col_idx, h_txt in enumerate(headers):
        cell = tbl.rows[0].cells[col_idx]
        cell.width = Cm(col_w)
        cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
        set_cell_shading(cell, "0F2A82")
        set_cell_margins(cell, top=90, bottom=90, left=100, right=100)
        p = cell.paragraphs[0]
        p.paragraph_format.space_after = Pt(0)
        add_rich_runs(p, h_txt, base_size=9.5, base_color=C_WHITE, base_bold=True)

    for r_idx, row_vals in enumerate(data_rows):
        row = tbl.rows[r_idx + 1]
        bg = "F8FAFC" if r_idx % 2 == 1 else "FFFFFF"
        for col_idx in range(ncols):
            cell = row.cells[col_idx]
            cell.width = Cm(col_w)
            cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
            set_cell_shading(cell, bg)
            set_cell_margins(cell, top=75, bottom=75, left=100, right=100)
            p = cell.paragraphs[0]
            p.paragraph_format.space_after = Pt(0)
            val = row_vals[col_idx] if col_idx < len(row_vals) else ""
            add_rich_runs(p, val, base_size=9.5, base_color=C_TEXT)

    spacer = doc.add_paragraph()
    spacer.paragraph_format.space_before = Pt(0)
    spacer.paragraph_format.space_after = Pt(4)


def add_pba_chapter_heading(doc: Document, title_txt: str) -> None:
    m = re.match(r"^(\d+)\.\s*(.+)$", title_txt)
    if m:
        ch_num = int(m.group(1))
        ch_title = m.group(2).strip()
        badge_path = TPL_DIR / f"ch_badge_{ch_num:02d}.png"
        badge_stream = load_image_stream(badge_path)
        tbl = doc.add_table(rows=1, cols=2)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        remove_table_borders(tbl)
        c0, c1 = tbl.rows[0].cells
        c0.width = Cm(2.0)
        c1.width = Cm(15.4)
        c0.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
        c1.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
        set_cell_margins(c0, top=20, bottom=40, left=0, right=80)
        set_cell_margins(c1, top=20, bottom=40, left=40, right=0)

        if badge_stream is not None:
            p0 = c0.paragraphs[0]
            p0.paragraph_format.space_after = Pt(0)
            p0.add_run().add_picture(badge_stream, width=Cm(1.55))

        p1 = c1.paragraphs[0]
        p1.paragraph_format.space_after = Pt(0)
        p1.paragraph_format.keep_with_next = True
        r1 = p1.add_run(ch_title)
        r1.font.name = "Calibri"
        r1.font.size = Pt(16.5)
        r1.font.bold = True
        r1.font.color.rgb = C_DARK
        add_bookmark(p1, f"bm_ch_{ch_num:02d}", 100 + ch_num)

        # Signature PBA two-tone horizontal divider (thick royal blue left + thin gray right)
        div = doc.add_table(rows=1, cols=2)
        div.alignment = WD_TABLE_ALIGNMENT.CENTER
        remove_table_borders(div)
        d0, d1 = div.rows[0].cells
        d0.width = Cm(8.4)
        d1.width = Cm(9.0)
        set_cell_margins(d0, top=0, bottom=0, left=0, right=0)
        set_cell_margins(d1, top=0, bottom=0, left=0, right=0)
        d0._tc.get_or_add_tcPr().append(
            parse_xml(f'<w:tcBorders {nsdecls("w")}><w:bottom w:val="single" w:sz="24" w:space="0" w:color="1D4ED8"/></w:tcBorders>')
        )
        d1._tc.get_or_add_tcPr().append(
            parse_xml(f'<w:tcBorders {nsdecls("w")}><w:bottom w:val="single" w:sz="6" w:space="0" w:color="94A3B8"/></w:tcBorders>')
        )
        p_sp = doc.add_paragraph()
        p_sp.paragraph_format.space_before = Pt(0)
        p_sp.paragraph_format.space_after = Pt(5)
    else:
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(8)
        p.paragraph_format.space_after = Pt(6)
        p.paragraph_format.keep_with_next = True
        r = p.add_run(title_txt)
        r.font.name = "Calibri"
        r.font.size = Pt(15)
        r.font.bold = True
        r.font.color.rgb = C_NAVY


def add_pba_subheading(doc: Document, sub_txt: str) -> None:
    p = doc.add_paragraph()
    p.paragraph_format.space_before = Pt(9)
    p.paragraph_format.space_after = Pt(4)
    p.paragraph_format.keep_with_next = True
    diamond_stream = load_image_stream(TPL_DIR / "diamond_check.png")
    if diamond_stream is not None:
        p.add_run().add_picture(diamond_stream, width=Cm(0.45))
        r_sp = p.add_run("  ")
        r_sp.font.size = Pt(11.5)
    r = p.add_run(sub_txt)
    r.font.name = "Calibri"
    r.font.size = Pt(11.5)
    r.font.bold = True
    r.font.color.rgb = C_BLUE


def add_pba_steps_table(doc: Document, steps: list[tuple[str, str]]) -> None:
    tbl = doc.add_table(rows=len(steps), cols=2)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_horizontal_only_borders(tbl, color="CBD5E1", sz="4")
    for idx, (num_str, body_txt) in enumerate(steps):
        c0, c1 = tbl.rows[idx].cells
        c0.width = Cm(1.5)
        c1.width = Cm(15.9)
        c0.vertical_alignment = WD_ALIGN_VERTICAL.TOP
        c1.vertical_alignment = WD_ALIGN_VERTICAL.TOP
        set_cell_margins(c0, top=80, bottom=80, left=40, right=60)
        set_cell_margins(c1, top=80, bottom=80, left=60, right=40)

        p0 = c0.paragraphs[0]
        p0.paragraph_format.space_after = Pt(0)
        r0 = p0.add_run(f"{int(num_str):02d}")
        r0.font.name = "Calibri"
        r0.font.size = Pt(14)
        r0.font.bold = True
        r0.font.color.rgb = C_BLUE

        p1 = c1.paragraphs[0]
        p1.paragraph_format.space_after = Pt(0)
        p1.paragraph_format.line_spacing = 1.15
        add_rich_runs(p1, body_txt, base_size=10.2)

    p_sp = doc.add_paragraph()
    p_sp.paragraph_format.space_before = Pt(0)
    p_sp.paragraph_format.space_after = Pt(4)


def add_pba_toc(doc: Document, items: list[str]) -> None:
    toc_stream = load_image_stream(TPL_DIR / "toc_banner_pba.png")
    if toc_stream is not None:
        p_banner = doc.add_paragraph()
        p_banner.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p_banner.paragraph_format.space_before = Pt(0)
        p_banner.paragraph_format.space_after = Pt(8)
        p_banner.add_run().add_picture(toc_stream, width=Cm(17.4))

    tbl = doc.add_table(rows=len(items), cols=2)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_horizontal_only_borders(tbl, color="CBD5E1", sz="4")
    for idx, raw_item in enumerate(items):
        # Extract label & page indicator cleanly
        cleaned = re.sub(r"\.{3,}", " ", raw_item).strip()
        m = re.match(r"^\*\*(.+?)\*\*\s+(\S+)$", cleaned)
        if m:
            label, pg = m.group(1), m.group(2)
        else:
            parts = cleaned.rsplit(" ", 1)
            label = parts[0].replace("**", "")
            pg = parts[1] if len(parts) > 1 else str(idx + 1)

        c0, c1 = tbl.rows[idx].cells
        c0.width = Cm(1.4)
        c1.width = Cm(16.0)
        set_cell_margins(c0, top=50, bottom=50, left=40, right=40)
        set_cell_margins(c1, top=50, bottom=50, left=40, right=40)

        p0 = c0.paragraphs[0]
        p0.paragraph_format.space_after = Pt(0)
        ch_match = re.match(r"^(\d+)\.\s+", label)
        if ch_match:
            ch_n = int(ch_match.group(1))
            add_pageref_field(p0, f"bm_ch_{ch_n:02d}", f"{int(pg):02d}" if pg.isdigit() else pg, font_size=11.0, color=C_BLUE)
        else:
            r0 = p0.add_run(f"{int(pg):02d}" if pg.isdigit() else pg)
            r0.font.name = "Calibri"
            r0.font.size = Pt(11)
            r0.font.bold = True
            r0.font.color.rgb = C_BLUE

        p1 = c1.paragraphs[0]
        p1.paragraph_format.space_after = Pt(0)
        r1 = p1.add_run(label)
        r1.font.name = "Calibri"
        r1.font.size = Pt(10.5)
        r1.font.color.rgb = C_DARK


def render_markdown_body(doc: Document, md_text: str) -> None:
    lines = md_text.splitlines()
    i = 0
    while i < len(lines) and i < 6:
        if lines[i].strip().startswith("## DAFTAR ISI"):
            break
        i += 1

    chapter_count = 0
    while i < len(lines):
        line = lines[i].rstrip()
        stripped = line.strip()

        if not stripped or stripped == "---":
            i += 1
            continue

        # TOC block
        if stripped == "## DAFTAR ISI":
            i += 1
            toc_items = []
            while i < len(lines):
                s = lines[i].strip()
                if s.startswith("## "):
                    break
                if s.startswith("- "):
                    toc_items.append(s[2:].strip())
                i += 1
            add_pba_toc(doc, toc_items)
            continue

        # Markdown Tables (Image comparison OR Data/Specs table)
        if stripped.startswith("|") and i + 1 < len(lines) and re.match(r"^\|\s*:?-+:?\s*\|", lines[i + 1].strip()):
            tbl_rows = []
            has_img = False
            while i < len(lines) and lines[i].strip().startswith("|"):
                if "![" in lines[i]:
                    has_img = True
                tbl_rows.append(parse_md_table_row(lines[i]))
                i += 1
            if has_img:
                add_two_col_image_table(doc, tbl_rows)
            else:
                add_data_table(doc, tbl_rows)
            continue

        # H2 Section / Chapter Heading
        if stripped.startswith("## "):
            title_txt = stripped[3:].strip()
            chapter_count += 1
            if chapter_count >= 1:
                doc.add_page_break()
            add_pba_chapter_heading(doc, title_txt)
            i += 1
            continue

        # H3 Subheading with PBA diamond checkmark
        if stripped.startswith("### "):
            add_pba_subheading(doc, stripped[4:].strip())
            i += 1
            continue

        # H4 Subheading
        if stripped.startswith("#### "):
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(7)
            p.paragraph_format.space_after = Pt(3)
            p.paragraph_format.keep_with_next = True
            r = p.add_run(stripped[5:].strip())
            r.font.name = "Calibri"
            r.font.size = Pt(10.5)
            r.font.bold = True
            r.font.color.rgb = C_NAVY
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
                p_cap.paragraph_format.space_after = Pt(7)
                add_rich_runs(p_cap, lines[i].strip(), base_size=9.0, base_color=C_MUTED)
                i += 1
            continue

        # Nested bullet
        if re.match(r"^\s{2,}-\s+", line):
            item_txt = re.sub(r"^\s+-\s+", "", line)
            p = doc.add_paragraph(style="List Bullet 2")
            p.paragraph_format.space_after = Pt(2)
            add_rich_runs(p, item_txt, base_size=10.0)
            i += 1
            continue

        # Bullet
        if stripped.startswith("- "):
            p = doc.add_paragraph(style="List Bullet")
            p.paragraph_format.space_after = Pt(3)
            add_rich_runs(p, stripped[2:].strip(), base_size=10.2)
            i += 1
            continue

        # Consecutive numbered steps -> PBA 01/02/03 bordered step table
        num_match = re.match(r"^(\d+)\.\s+(.*)$", stripped)
        if num_match:
            steps = []
            while i < len(lines):
                nm = re.match(r"^(\d+)\.\s+(.*)$", lines[i].strip())
                if not nm:
                    break
                steps.append((nm.group(1), nm.group(2).strip()))
                i += 1
            add_pba_steps_table(doc, steps)
            continue

        # Normal paragraph
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
        p.paragraph_format.space_after = Pt(5)
        p.paragraph_format.line_spacing = 1.15
        add_rich_runs(p, " ".join(para_parts), base_size=10.2)


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
