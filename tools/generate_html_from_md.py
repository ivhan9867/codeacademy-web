"""
Generate synced HTML Manual Book (19 Chapters, PBA Kel 1 theme + Print/PDF support)
from docs/Manual_Book_CodeAcademy.md into docs/Manual_Book_CodeAcademy.html and
public/docs/Manual_Book_CodeAcademy.html.
"""
import html
import re
import shutil
from pathlib import Path

MD_PATH = Path("docs/Manual_Book_CodeAcademy.md")
OUT_DOCS = Path("docs/Manual_Book_CodeAcademy.html")
OUT_PUBLIC = Path("public/docs/Manual_Book_CodeAcademy.html")


def inline_md(text: str) -> str:
    esc = html.escape(text, quote=False)
    esc = re.sub(r"\*\*([^*]+)\*\*", r"<strong>\1</strong>", esc)
    esc = re.sub(r"\*([^*]+)\*", r"<em>\1</em>", esc)
    esc = re.sub(r"`([^`]+)`", r"<code>\1</code>", esc)
    return esc


def parse_row(line: str) -> list[str]:
    s = line.strip()
    if s.startswith("|"):
        s = s[1:]
    if s.endswith("|"):
        s = s[:-1]
    return [c.strip() for c in s.split("|")]


def md_to_html_body(md_text: str) -> str:
    lines = md_text.splitlines()
    out = []
    i = 0
    while i < len(lines) and i < 6:
        if lines[i].strip().startswith("## DAFTAR ISI"):
            break
        i += 1

    in_chapter = False

    def close_chapter():
        nonlocal in_chapter
        if in_chapter:
            out.append('</div><footer class="pba-footer"><span class="ribbon">Pengembangan Bahan Ajar Edisi I 2026</span><span class="chev">&gt;&gt;&gt;</span></footer></section>')
            in_chapter = False

    while i < len(lines):
        line = lines[i].rstrip()
        stripped = line.strip()
        if not stripped or stripped == "---":
            i += 1
            continue

        if stripped == "## DAFTAR ISI":
            i += 1
            toc_rows = []
            while i < len(lines):
                s = lines[i].strip()
                if s.startswith("## "):
                    break
                if s.startswith("- "):
                    cleaned = re.sub(r"\.{3,}", " ", s[2:].strip()).strip()
                    m = re.match(r"^\*\*(.+?)\*\*\s+(\S+)$", cleaned)
                    lbl, pg = (m.group(1), m.group(2)) if m else (cleaned, "")
                    toc_rows.append((lbl, pg))
                i += 1
            out.append('<section class="page toc-page"><div class="toc-banner"><h2>DAFTAR ISI</h2></div><div class="toc-list">')
            for idx, (lbl, pg) in enumerate(toc_rows):
                num_str = f"{int(pg):02d}" if pg.isdigit() else f"{idx+1:02d}"
                out.append(f'<div class="toc-row"><span class="toc-num">{num_str}</span><span class="toc-lbl">{html.escape(lbl)}</span></div>')
            out.append('</div><footer class="pba-footer"><span class="ribbon">Pengembangan Bahan Ajar Edisi I 2026</span><span class="page-dot">02</span></footer></section>')
            continue

        if stripped.startswith("|") and i + 1 < len(lines) and re.match(r"^\|\s*:?-+:?\s*\|", lines[i + 1].strip()):
            rows = []
            has_img = False
            while i < len(lines) and lines[i].strip().startswith("|"):
                if "![" in lines[i]:
                    has_img = True
                rows.append(parse_row(lines[i]))
                i += 1
            if has_img and len(rows) >= 3:
                headers = rows[0]
                imgs = rows[2]
                caps = rows[3] if len(rows) > 3 else ["", ""]
                out.append('<div class="img-grid">')
                for col in range(2):
                    m = re.search(r"!\[([^\]]*)\]\(([^)]+)\)", imgs[col] if col < len(imgs) else "")
                    src = m.group(2) if m else ""
                    alt = m.group(1) if m else ""
                    ht = inline_md(headers[col] if col < len(headers) else "")
                    ct = inline_md(caps[col] if col < len(caps) else "")
                    out.append(f'<figure class="img-card"><figcaption class="img-head">{ht}</figcaption><img src="{html.escape(src)}" alt="{html.escape(alt)}"><figcaption class="img-cap">{ct}</figcaption></figure>')
                out.append('</div>')
            else:
                headers = rows[0]
                data_rows = rows[2:] if len(rows) > 2 else []
                out.append('<div class="table-wrap"><table class="spec-table"><thead><tr>')
                for h_txt in headers:
                    out.append(f"<th>{inline_md(h_txt)}</th>")
                out.append("</tr></thead><tbody>")
                for r_vals in data_rows:
                    out.append("<tr>")
                    for c_idx in range(len(headers)):
                        val = r_vals[c_idx] if c_idx < len(r_vals) else ""
                        out.append(f"<td>{inline_md(val)}</td>")
                    out.append("</tr>")
                out.append("</tbody></table></div>")
            continue

        if stripped.startswith("## "):
            close_chapter()
            title_txt = stripped[3:].strip()
            m = re.match(r"^(\d+)\.\s*(.+)$", title_txt)
            if m:
                ch_num, ch_title = m.group(1), m.group(2).strip()
                out.append(f'<section class="page chapter-page"><div class="ch-head"><span class="ch-badge">{html.escape(ch_num)}</span><h2>{html.escape(ch_title)}</h2></div><div class="ch-rule"><span></span><i></i></div><div class="ch-body">')
            else:
                out.append(f'<section class="page chapter-page"><h2 class="ch-plain">{html.escape(title_txt)}</h2><div class="ch-body">')
            in_chapter = True
            i += 1
            continue

        if stripped.startswith("### "):
            out.append(f'<h3 class="diamond-sub"><span class="diamond" aria-hidden="true">&#10003;</span> {inline_md(stripped[4:].strip())}</h3>')
            i += 1
            continue

        if stripped.startswith("#### "):
            out.append(f'<h4 class="sub-h4">{inline_md(stripped[5:].strip())}</h4>')
            i += 1
            continue

        img_match = re.match(r"^!\[([^\]]*)\]\(([^)]+)\)$", stripped)
        if img_match:
            alt, src = img_match.group(1), img_match.group(2)
            cap = ""
            if i + 1 < len(lines) and lines[i + 1].strip().startswith("*") and lines[i + 1].strip().endswith("*"):
                cap = lines[i + 1].strip()[1:-1]
                i += 1
            out.append(f'<figure class="solo-fig"><img src="{html.escape(src)}" alt="{html.escape(alt)}">{f"<figcaption>{inline_md(cap)}</figcaption>" if cap else ""}</figure>')
            i += 1
            continue

        if stripped.startswith("- "):
            out.append('<ul class="bullet-list">')
            while i < len(lines) and lines[i].strip().startswith("- "):
                out.append(f"<li>{inline_md(lines[i].strip()[2:].strip())}</li>")
                i += 1
            out.append("</ul>")
            continue

        if re.match(r"^(\d+)\.\s+", stripped):
            out.append('<div class="steps-list">')
            while i < len(lines):
                nm = re.match(r"^(\d+)\.\s+(.*)$", lines[i].strip())
                if not nm:
                    break
                out.append(f'<div class="step-row"><span class="step-num">{int(nm.group(1)):02d}</span><div class="step-text">{inline_md(nm.group(2).strip())}</div></div>')
                i += 1
            out.append("</div>")
            continue

        parts = [stripped]
        i += 1
        while (
            i < len(lines)
            and lines[i].strip()
            and not lines[i].strip().startswith(("#", "-", "|", "!", "*"))
            and not re.match(r"^\d+\.\s+", lines[i].strip())
            and lines[i].strip() != "---"
        ):
            parts.append(lines[i].strip())
            i += 1
        out.append(f'<p class="body-p">{inline_md(" ".join(parts))}</p>')

    close_chapter()
    return "\n".join(out)


def build_html(body_html: str) -> str:
    return f"""<!DOCTYPE html>
<html lang="id">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Manual Book Aplikasi CodeAcademy — Android &amp; Web Portal</title>
  <style>
    * {{ box-sizing: border-box; margin: 0; padding: 0; }}
    body {{ font-family: 'Segoe UI', system-ui, -apple-system, sans-serif; background: #E5E7EB; color: #1F2937; line-height: 1.55; }}
    .toolbar {{ position: sticky; top: 0; z-index: 100; background: #0F2A82; color: #fff; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; gap: 12px; box-shadow: 0 2px 10px rgba(0,0,0,0.18); }}
    .toolbar .brand {{ display: flex; align-items: center; gap: 10px; font-weight: 700; font-size: 15px; }}
    .toolbar .brand img {{ width: 28px; height: 28px; border-radius: 6px; background: #fff; }}
    .toolbar .acts {{ display: flex; gap: 10px; flex-wrap: wrap; }}
    .btn {{ background: #fff; color: #0F2A82; border: none; padding: 8px 14px; border-radius: 8px; font-weight: 700; font-size: 13px; cursor: pointer; text-decoration: none; }}
    .btn-ghost {{ background: rgba(255,255,255,0.14); color: #fff; }}
    .sheet-wrap {{ max-width: 900px; margin: 24px auto; padding: 0 16px; display: flex; flex-direction: column; gap: 22px; }}
    .cover-page {{ background: linear-gradient(160deg, #041242 0%, #0F2A82 62%, #1D4ED8 100%); color: #fff; border-radius: 12px; padding: 54px 36px 28px; text-align: center; box-shadow: 0 8px 28px rgba(15,42,130,0.22); }}
    .cover-logo {{ width: 148px; height: 148px; border-radius: 28px; background: #EFFDFB; padding: 14px; margin: 22px auto; box-shadow: 0 10px 30px rgba(0,0,0,0.3); }}
    .cover-page h1 {{ font-size: 36px; letter-spacing: 0.03em; font-weight: 800; }}
    .cover-page .sub {{ font-size: 18px; color: #DBEAFE; margin-top: 6px; font-weight: 700; }}
    .cover-page .desc {{ font-size: 15px; font-style: italic; color: #BAE6FD; margin-top: 12px; }}
    .page {{ background: #fff; border-radius: 12px; padding: 32px 36px 22px; box-shadow: 0 4px 18px rgba(15,23,42,0.08); }}
    .toc-banner {{ background: #EBF3FF; padding: 20px 24px; border-radius: 8px; margin-bottom: 16px; border-left: 6px solid #0F2A82; }}
    .toc-banner h2 {{ font-size: 26px; color: #111827; font-weight: 800; }}
    .toc-row {{ display: flex; align-items: center; gap: 14px; padding: 8px 6px; border-bottom: 1px solid #E2E8F0; font-size: 14px; }}
    .toc-num {{ font-weight: 800; color: #1D4ED8; min-width: 28px; }}
    .ch-head {{ display: flex; align-items: center; gap: 16px; margin-bottom: 10px; }}
    .ch-badge {{ width: 48px; height: 48px; border-radius: 50%; background: #0F2A82; color: #fff; font-weight: 800; font-size: 22px; display: inline-flex; align-items: center; justify-content: center; flex-shrink: 0; }}
    .ch-head h2 {{ font-size: 22px; color: #111827; font-weight: 800; }}
    .ch-rule {{ display: flex; height: 4px; margin-bottom: 18px; align-items: center; }}
    .ch-rule span {{ width: 46%; height: 4px; background: #1D4ED8; }}
    .ch-rule i {{ width: 54%; height: 1px; background: #94A3B8; display: block; }}
    .diamond-sub {{ display: flex; align-items: center; gap: 8px; color: #1E40AF; font-size: 15px; font-weight: 700; margin: 16px 0 8px; }}
    .diamond {{ display: inline-flex; align-items: center; justify-content: center; width: 20px; height: 20px; border: 1.5px solid #1D4ED8; background: #EFF6FF; color: #0F2A82; font-size: 11px; border-radius: 4px; flex-shrink: 0; }}
    .sub-h4 {{ color: #0F2A82; font-size: 14px; font-weight: 700; margin: 12px 0 6px; }}
    .body-p {{ font-size: 14px; margin-bottom: 10px; color: #1F2937; }}
    .bullet-list {{ margin: 6px 0 12px 22px; font-size: 14px; }}
    .bullet-list li {{ margin-bottom: 5px; }}
    .steps-list {{ margin: 8px 0 14px; border-top: 1px solid #CBD5E1; }}
    .step-row {{ display: flex; gap: 16px; padding: 10px 4px; border-bottom: 1px solid #CBD5E1; align-items: flex-start; font-size: 14px; }}
    .step-num {{ font-size: 18px; font-weight: 800; color: #1D4ED8; min-width: 30px; }}
    .table-wrap {{ overflow-x: auto; margin: 10px 0 14px; }}
    .spec-table {{ width: 100%; border-collapse: collapse; font-size: 13px; }}
    .spec-table th {{ background: #0F2A82; color: #fff; text-align: left; padding: 9px 10px; }}
    .spec-table td {{ border: 1px solid #CBD5E1; padding: 8px 10px; vertical-align: top; }}
    .spec-table tr:nth-child(even) td {{ background: #F8FAFC; }}
    .solo-fig {{ text-align: center; margin: 14px 0; }}
    .solo-fig img {{ max-width: 100%; max-height: 430px; border-radius: 10px; border: 1px solid #E2E8F0; }}
    .solo-fig figcaption, .img-cap {{ font-size: 12.5px; color: #4B5563; font-style: italic; margin-top: 6px; }}
    .img-grid {{ display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin: 12px 0; }}
    .img-card {{ text-align: center; }}
    .img-card img {{ max-width: 100%; max-height: 360px; border-radius: 10px; border: 1px solid #E2E8F0; }}
    .img-head {{ font-size: 13px; font-weight: 700; color: #0F2A82; margin-bottom: 6px; }}
    code {{ background: #EFF6FF; color: #0F2A82; padding: 1px 5px; border-radius: 4px; font-family: Consolas, monospace; font-size: 12.5px; }}
    .pba-footer {{ margin-top: 22px; padding-top: 10px; border-top: 2px solid #0F2A82; display: flex; justify-content: space-between; align-items: center; font-size: 12px; }}
    .pba-footer .ribbon {{ background: #0F2A82; color: #fff; padding: 4px 14px; border-radius: 4px; font-weight: 700; }}
    .pba-footer .chev, .pba-footer .page-dot {{ color: #1D4ED8; font-weight: 800; }}
    @media (max-width: 680px) {{ .img-grid {{ grid-template-columns: 1fr; }} .page {{ padding: 20px; }} }}
    @media print {{ .toolbar {{ display: none; }} body {{ background: #fff; }} .sheet-wrap {{ margin: 0; max-width: none; padding: 0; gap: 0; }} .cover-page, .page {{ box-shadow: none; border-radius: 0; page-break-after: always; }} }}
  </style>
</head>
<body>
  <header class="toolbar">
    <div class="brand">
      <img src="../assets/logo.png" alt="Logo CodeAcademy">
      <span>Manual Book CodeAcademy (Android &amp; Web Portal)</span>
    </div>
    <div class="acts">
      <a class="btn btn-ghost" href="../index.html">&larr; Kembali</a>
      <a class="btn btn-ghost" href="Manual_Book_CodeAcademy.docx" download>Unduh DOCX</a>
      <button type="button" class="btn" onclick="window.print()">Cetak / Simpan PDF</button>
    </div>
  </header>
  <main class="sheet-wrap">
    <section class="cover-page">
      <h1>MANUAL BOOK</h1>
      <div class="sub">APLIKASI CODEACADEMY &amp; PORTAL WEB</div>
      <img class="cover-logo" src="../assets/logo.png" alt="Maskot Codi CodeAcademy">
      <p class="desc">Platform Pembelajaran Web Development &amp; Coding Mobile-First<br>(Aplikasi Android Offline-First, Portal Fasilitator, Panel Super Admin &amp; Verifikasi Sertifikat)</p>
      <div style="margin-top:26px;font-size:13px;font-weight:700;color:#93C5FD;">Pengembangan Bahan Ajar Edisi I 2026</div>
    </section>
    {body_html}
  </main>
</body>
</html>"""


def main() -> None:
    md_text = MD_PATH.read_text(encoding="utf-8")
    body = md_to_html_body(md_text)
    full_html = build_html(body)
    OUT_DOCS.write_text(full_html, encoding="utf-8")
    shutil.copy2(OUT_DOCS, OUT_PUBLIC)
    print("Generated HTML:", OUT_DOCS, OUT_DOCS.stat().st_size, "bytes")
    print("Copied HTML:", OUT_PUBLIC, OUT_PUBLIC.stat().st_size, "bytes")


if __name__ == "__main__":
    main()
