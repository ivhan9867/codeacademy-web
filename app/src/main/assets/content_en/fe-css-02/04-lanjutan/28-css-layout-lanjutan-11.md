---
id: css-layout-lanjutan-11
courseId: fe-css-02
order: 30
title: "Hybrid Architecture: Combining Flexbox & CSS Grid"
readingMinutes: 9
section: LANJUTAN
learningObjectives:
  - "Design a full page skeleton using a hybrid Grid (macro) and Flexbox (micro) architecture"
  - "Use a quick decision checklist to choose between Grid and Flexbox for each UI component"
  - "Integrate a navbar, sidebar, card gallery, and pill tag list into one cohesive page"
codeLanguage: css
codeSnippet: |
  /* Makro Halaman: CSS Grid */
  .app-shell {
    display: grid;
    grid-template-columns: 200px 1fr;
    grid-template-rows: auto 1fr auto;
    gap: 16px;
  }

  /* Mikro Komponen: Flexbox */
  .top-nav {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Arsitektur Hibrida: Grid + Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="app-shell">
        <!-- Area Header: Menggunakan Flexbox -->
        <header class="app-header">
          <strong class="brand">DevBoard</strong>
          <nav class="nav-menu">
            <a href="#">Ringkasan</a>
            <a href="#">Proyek</a>
            <a href="#">Pengaturan</a>
          </nav>
        </header>

        <!-- Area Sidebar: Grid Track Kiri -->
        <aside class="app-sidebar">
          <h4>Filter Topik</h4>
          <div class="pill-group">
            <span class="pill">Grid</span>
            <span class="pill">Flexbox</span>
            <span class="pill">UI Kit</span>
            <span class="pill">Tokens</span>
          </div>
        </aside>

        <!-- Area Konten Utama: Grid di dalamnya & Flexbox di kartu -->
        <main class="app-main">
          <div class="card">
            <h4>Desain Sistem</h4>
            <p>Menyatukan Grid sebagai struktur luar dan Flexbox di dalam komponen.</p>
            <div class="card-actions">
              <span class="status">Aktif</span>
              <button class="btn">Buka</button>
            </div>
          </div>
          <div class="card">
            <h4>Audit Layout</h4>
            <p>Pastikan jarak dan perataan konsisten di semua ukuran layar.</p>
            <div class="card-actions">
              <span class="status">Selesai</span>
              <button class="btn">Buka</button>
            </div>
          </div>
        </main>

        <footer class="app-footer">
          Arsitektur Hibrida CSS Modern &bull; Flexbox &amp; Grid
        </footer>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 12px;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. MAKRO SHELL: Ubah .app-shell menjadi CSS Grid 2 kolom */
  .app-shell {
    /* Tambahkan display: grid; dan grid-template-columns: 180px 1fr; di sini */
    gap: 14px;
  }

  /* 2. MIKRO NAVBAR: Jadikan .app-header menggunakan Flexbox */
  .app-header {
    grid-column: 1 / -1;
    /* Tambahkan display: flex; justify-content: space-between; align-items: center; */
    background-color: #104246;
    color: white;
    padding: 12px 18px;
    border-radius: 10px;
  }

  .nav-menu {
    display: flex;
    gap: 14px;
  }

  .nav-menu a {
    color: #e2f0f1;
    text-decoration: none;
    font-size: 13px;
  }

  .app-sidebar {
    background-color: #e2f0f1;
    padding: 14px;
    border-radius: 10px;
  }

  .app-sidebar h4 {
    margin: 0 0 10px 0;
    color: #104246;
  }

  .pill-group {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }

  .pill {
    background-color: #1a6368;
    color: white;
    padding: 4px 10px;
    border-radius: 999px;
    font-size: 11px;
    font-weight: bold;
  }

  .app-main {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 12px;
  }

  .card {
    display: flex;
    flex-direction: column;
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 14px;
  }

  .card h4 {
    margin: 0 0 6px 0;
    color: #104246;
  }

  .card p {
    margin: 0 0 14px 0;
    font-size: 13px;
    color: #6b655d;
    flex-grow: 1;
  }

  .card-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #f1ece4;
    padding-top: 10px;
  }

  .status {
    font-size: 12px;
    font-weight: bold;
    color: #df7b56;
  }

  .btn {
    background-color: #104246;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 12px;
    cursor: pointer;
  }

  .app-footer {
    grid-column: 1 / -1;
    text-align: center;
    background-color: #f1ece4;
    padding: 10px;
    border-radius: 8px;
    font-size: 12px;
    color: #6b655d;
  }
playgroundTask: "Complete the hybrid architecture: (1) Add 'display: grid; grid-template-columns: 180px 1fr;' to '.app-shell' for the macro page shell. (2) Add 'display: flex; justify-content: space-between; align-items: center;' to '.app-header' to arrange the logo and navigation menu!"
verificationRules:
  - "css:display: grid"
  - "css:display: flex"
---
The biggest question people ask after learning Flexbox and CSS Grid is: *"Which one should I use?"* The answer isn't picking one over the other—it's **combining both in a Hybrid Architecture**.

The golden rule followed by modern *UI Engineers* is simple:
> **Use CSS Grid for macro layouts (the page skeleton), and use Flexbox for micro layouts (elements inside components).**

## Hybrid Architecture Blueprint (*Page Blueprint*)

Notice how a professional dashboard page splits responsibilities between Grid and Flexbox:

```text
+-----------------------------------------------------------------------+
| HEADER (.app-header)  --> [FLEXBOX] Logo on left, menu & CTA on right |
+-------------------+---------------------------------------------------+
| SIDEBAR           | MAIN CONTENT (.app-main) --> [CSS GRID]           |
| (.app-sidebar)    | +-----------------------+ +---------------------+ |
|                   | | Card 1 (.card)        | | Card 2 (.card)      | |
| Tag Pills:        | | [FLEXBOX column]      | | [FLEXBOX column]    | |
| [Grid] [Flexbox]  | | Title + Description   | | Title + Description | |
| [UI Kit] [Tokens] | | --------------------- | | ------------------- | |
| [FLEXBOX wrap]    | | [Status]     [Button] | | [Status]   [Button] | |
|                   | +-----------------------+ +---------------------+ |
+-------------------+---------------------------------------------------+
| FOOTER (.app-footer) --> [GRID span 1 / -1]                           |
+-----------------------------------------------------------------------+
```

## Quick Decision Checklist: Grid vs. Flexbox

Use this reference table whenever you start styling a new block:

| Design Question | Choose **CSS Grid** | Choose **Flexbox** |
| :--- | :--- | :--- |
| **Layout dimensions?** | 2D (rows & columns together) | 1D (one direction: row or column) |
| **What drives sizing?** | **Layout-First**: structure tracks dictate cell size | **Content-First**: item content pushes space |
| **Component type?** | Page shell (header, sidebar, main), symmetrical card galleries | Navbar, button rows, *pill* tags (`wrap`), card internals |
| **Need items stacked in the same cell?** | Yes (`grid-area` / same row & column) | No (items flow sequentially) |
| **Pinning a button to the card bottom?** | Not directly | Yes (`flex-direction: column` + `flex-grow: 1` / `margin-top: auto`) |

## Professional 3-Step Workflow

1. **Wrap the page with Grid** (`.app-shell`) to divide the Header, Sidebar, Main Content, and Footer zones using `grid-template-columns` or `grid-template-areas`.
2. **Arrange repeating galleries with Grid** (`.app-main`) using `repeat(auto-fit, minmax(..., 1fr))` so cards stay symmetrically aligned.
3. **Polish details inside each zone with Flexbox**: link rows in the header (`justify-content: space-between`), wrapping tags in the sidebar (`flex-wrap: wrap`), and action buttons at the bottom of each card (`margin-top: auto`).

By completing the Playground exercise in this lesson, you now have the full foundation to build the course's final project using industry-standard web architecture!
