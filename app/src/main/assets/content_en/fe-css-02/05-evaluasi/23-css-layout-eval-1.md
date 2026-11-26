---
id: css-layout-eval-1
courseId: fe-css-02
order: 31
title: "Mini Project: Modern Web Dashboard Page"
readingMinutes: 7
section: EVALUASI
learningObjectives:
  - "Integrate all Flexbox and CSS Grid techniques into a real-world dashboard architecture project"
  - "Build the macro page structure (Sidebar + Main Header + Widget Grid) using CSS Grid"
  - "Arrange micro analytics metric cards using Flexbox"
codeLanguage: css
codeSnippet: |
  .dasbor {
    display: grid;
    grid-template-columns: 220px 1fr;
    grid-template-rows: 60px 1fr;
    grid-template-areas:
      "sidebar header"
      "sidebar konten";
    min-height: 100vh;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Mini Proyek: Dasbor Web</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="dasbor">
        <!-- 1. Sidebar Kiri -->
        <aside class="sidebar">
          <div class="brand">⚡ CodeDash</div>
          <ul class="side-menu">
            <li><a href="#" class="aktif">Ringkasan</a></li>
            <li><a href="#">Proyek</a></li>
            <li><a href="#">Analisis</a></li>
            <li><a href="#">Pengaturan</a></li>
          </ul>
        </aside>

        <!-- 2. Header Atas -->
        <header class="header">
          <div class="sapaan">Selamat Datang, <strong>Rani</strong> 👋</div>
          <div class="user-avatar">RP</div>
        </header>

        <!-- 3. Konten Utama -->
        <main class="konten">
          <h2>Statistik Belajar Minggu Ini</h2>
          
          <div class="grid-metrik">
            <!-- Kartu Metrik 1 -->
            <div class="kartu-metrik">
              <span class="label">Total XP</span>
              <strong class="nilai">1,450 XP</strong>
              <span class="info">+20% dari minggu lalu</span>
            </div>

            <!-- Kartu Metrik 2 -->
            <div class="kartu-metrik">
              <span class="label">Streak Belajar</span>
              <strong class="nilai">12 Hari 🔥</strong>
              <span class="info">Pertahankan terus!</span>
            </div>

            <!-- Kartu Metrik 3 -->
            <div class="kartu-metrik">
              <span class="label">Modul Selesai</span>
              <strong class="nilai">24 Modul</strong>
              <span class="info">Target: 30 Modul</span>
            </div>
          </div>
        </main>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 0;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Tata Letak Makro dengan CSS Grid */
  .dasbor {
    display: grid;
    grid-template-columns: 200px 1fr;
    grid-template-rows: 60px 1fr;
    grid-template-areas:
      "sidebar header"
      "sidebar konten";
    min-height: 100vh;
  }

  .sidebar {
    grid-area: sidebar;
    background-color: #104246;
    color: white;
    padding: 20px 16px;
  }

  .brand {
    font-size: 18px;
    font-weight: bold;
    color: #389b9e;
    margin-bottom: 24px;
  }

  .side-menu {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .side-menu a {
    color: #b5ada4;
    text-decoration: none;
    padding: 8px 12px;
    border-radius: 6px;
    display: block;
    font-size: 14px;
  }

  .side-menu a.aktif, .side-menu a:hover {
    background-color: #1a6368;
    color: white;
  }

  /* 2. Header Atas dengan Flexbox */
  .header {
    grid-area: header;
    background-color: #ffffff;
    border-bottom: 1px solid #e2ddd5;
    padding: 0 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .user-avatar {
    width: 36px;
    height: 36px;
    background-color: #df7b56;
    color: white;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: bold;
    font-size: 13px;
  }

  /* 3. Area Konten & Metrik Grid */
  .konten {
    grid-area: konten;
    padding: 24px;
  }

  .konten h2 {
    margin: 0 0 16px 0;
    font-size: 20px;
  }

  .grid-metrik {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 16px;
  }

  .kartu-metrik {
    background-color: white;
    padding: 16px;
    border-radius: 10px;
    border: 1px solid #e2ddd5;
    display: flex;
    flex-direction: column;
    gap: 6px;
  }

  .label { font-size: 12px; color: #6b655d; }
  .nilai { font-size: 20px; color: #104246; font-weight: bold; }
  .info  { font-size: 11px; color: #287948; font-weight: 500; }
playgroundTask: "Try adapting the layout for mobile screens: change 'grid-template-columns: 200px 1fr;' to '1fr;', and update 'grid-template-areas: \"header\" \"konten\";'. Set '.sidebar { display: none; }'. Run the code and watch the desktop dashboard transform into a clean mobile app view!"
---
It's time to put your skills to the test by assembling the capstone project of this course: **a Modern Web Dashboard (*Analytics Dashboard*)**.

Notice how this page brings together everything you have learned into one cohesive structure:

```text
+--------------------------------------------------+
| HEADER (Flexbox: Brand + Search + Avatar)        |
+------------+-------------------------------------+
|  SIDEBAR   |  [KPI 1]   [KPI 2]   [KPI 3]        |
|  (Nav)     |  +-------------------------------+  |
|            |  | Table / Activity Card         |  |
+------------+--+-------------------------------+--+
```
## Project Architecture Breakdown

### 1. Macro Foundation: CSS Grid with `grid-template-areas`
The main page skeleton is split into 2 columns and 2 rows:
```css
.dasbor {
  display: grid;
  grid-template-columns: 200px 1fr;
  grid-template-rows: 60px 1fr;
  grid-template-areas:
    "sidebar header"
    "sidebar konten";
}
```
The sidebar locks cleanly to the left at full viewport height (`min-height: 100vh`), while the Header and Content sit neatly to its right.

### 2. Micro Components: Flexbox
- **Sidebar Menu**: Uses `display: flex; flex-direction: column; gap: 8px;` to space menu links vertically with precision.
- **Top Header**: Uses `display: flex; justify-content: space-between; align-items: center;` to push the greeting text to the left and the user avatar to the right.
- **User Avatar**: Uses `display: flex; justify-content: center; align-items: center;` to center the "RP" initials right inside the circle.
- **Metric Stat Cards**: Uses `repeat(auto-fit, minmax(180px, 1fr))` on the card wrapper to adapt column counts automatically, and vertical Flexbox inside each card so the label, figure, and caption line up cleanly.

You have just built an industry-grade web dashboard with clean, modern code—without a single float hack or old-school HTML table!
