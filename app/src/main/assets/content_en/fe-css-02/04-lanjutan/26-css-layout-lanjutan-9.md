---
id: css-layout-lanjutan-9
courseId: fe-css-02
order: 28
title: "Mobile-First Strategy, Breakpoints, & Modern Media Queries"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - "Understand the Mobile-First philosophy and why min-width is cleaner than max-width"
  - "Learn the industry-standard breakpoint list for phones, tablets, and desktop screens"
  - "Dynamically adapt Grid and Flexbox structures using @media rules"
codeLanguage: css
codeSnippet: |
  /* Dasar: Mobile (< 640px) - 1 kolom */
  .grid-artikel {
    display: grid;
    grid-template-columns: 1fr;
    gap: 16px;
  }

  /* Tablet (>= 768px) - 2 kolom */
  @media (min-width: 768px) {
    .grid-artikel {
      grid-template-columns: repeat(2, 1fr);
    }
  }

  /* Desktop (>= 1024px) - 3 kolom */
  @media (min-width: 1024px) {
    .grid-artikel {
      grid-template-columns: repeat(3, 1fr);
    }
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Latihan: Mobile-First & Media Query</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <header class="site-header">
        <h2>Berita Teknologi</h2>
        <span class="tag-mode">Responsif Mobile-First</span>
      </header>

      <section class="grid-artikel">
        <article class="post">
          <h4>1. CSS Grid Level 2</h4>
          <p>Menyusun tata letak kompleks dengan baris dan kolom yang bersih.</p>
        </article>
        <article class="post">
          <h4>2. Mengenal Media Query</h4>
          <p>Menyesuaikan jumlah kolom berdasarkan lebar layar perangkat.</p>
        </article>
        <article class="post">
          <h4>3. Performa Mobile</h4>
          <p>Desain ringan dimulai dari layar sempit lalu berkembang ke desktop.</p>
        </article>
      </section>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  .site-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #104246;
    color: white;
    padding: 12px 18px;
    border-radius: 10px;
    margin-bottom: 18px;
  }

  .site-header h2 {
    margin: 0;
    font-size: 18px;
  }

  .tag-mode {
    background-color: #df7b56;
    padding: 4px 10px;
    border-radius: 14px;
    font-size: 12px;
    font-weight: bold;
  }

  /* Gaya dasar Mobile-First (1 kolom) */
  .grid-artikel {
    display: grid;
    grid-template-columns: 1fr;
    gap: 14px;
  }

  .post {
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 16px;
  }

  .post h4 {
    margin: 0 0 8px 0;
    color: #104246;
  }

  .post p {
    margin: 0;
    font-size: 13px;
    color: #6b655d;
    line-height: 1.5;
  }

  /* Tambahkan @media (min-width: 768px) dan @media (min-width: 1024px) di bawah ini */
playgroundTask: "Add Mobile-First Media Query rules at the bottom of the CSS: create '@media (min-width: 600px)' to change '.grid-artikel' to 'grid-template-columns: repeat(2, 1fr);' and '@media (min-width: 900px)' to 'repeat(3, 1fr);' (or use 768px/1024px breakpoints). Observe the result in the preview!"
verificationRules:
  - "css:@media"
  - "css:grid-template-columns"
---
More than half of internet users browse the web on smartphones. Instead of designing for a wide desktop monitor first and then struggling to chop it down for mobile, modern best practice follows the **Mobile-First** approach.

```text
  MOBILE-FIRST PROGRESSION (min-width)
+----------------+     +-------------------------+     +-----------------------------------+
| Mobile < 640px | --> | Tablet >= 768px         | --> | Desktop >= 1024px                 |
| [ Card 1 ]     |     | [ Card 1 ] [ Card 2 ]   |     | [ Card 1 ] [ Card 2 ] [ Card 3 ]  |
| [ Card 2 ]     |     | [ Card 3 ]              |     |                                   |
| [ Card 3 ]     |     +-------------------------+     +-----------------------------------+
+----------------+
```

## Industry-Standard Breakpoint Table

A **breakpoint** is the screen width where a layout adapts. You don't need a separate *media query* for every phone model; just stick to these common ranges:

| Device Category | Screen Width Range | Media Query (`min-width`) | Common Layout Pattern |
| :--- | :--- | :--- | :--- |
| **Mobile** *(Base)* | `< 640px` | *(No `@media` — default styles)* | 1 stacked column, compact menu |
| **Small Tablet / Landscape Phone** | `>= 640px` | `@media (min-width: 640px)` | Start 2 columns for compact cards |
| **Tablet** | `>= 768px` | `@media (min-width: 768px)` | 2-column grid, full horizontal navbar |
| **Desktop** | `>= 1024px` | `@media (min-width: 1024px)` | 3–4 column grid + sidebars appear |
| **Wide Screen** | `>= 1280px` | `@media (min-width: 1280px)` | `max-width` on main container to keep text comfortable to read |

> **Required in HTML**: Always include `<meta name="viewport" content="width=device-width, initial-scale=1.0" />` inside `<head>`. Without it, mobile browsers zoom out the desktop version and `@media` rules won't trigger properly!

## Why Is `min-width` Better Than `max-width`?

| Aspect | Mobile-First (`min-width`) | Desktop-First (`max-width`) |
| :--- | :--- | :--- |
| **CSS Flow** | Start simple (1 column), then **add** features on wider screens | Start complex (many columns), then **override/reset** on small screens |
| **Code Cleanliness** | Minimal reset declarations (`float: none`, removing margins, etc.) | Full of override rules to undo the desktop layout |
| **Mobile Performance** | Phones don't have to process heavy desktop grid rules | Phones must read desktop rules first before overriding them |

Here is a clean **Media Query** example written in Mobile-First style:

```css
/* 1. Base styles for all screens (Mobile first) */
.layout-utama {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
}

/* 2. Extra rules for Tablet and up */
@media (min-width: 768px) {
  .layout-utama {
    grid-template-columns: 240px 1fr; /* Sidebar appears on the left */
  }
}

/* 3. Adjustments for Desktop and up */
@media (min-width: 1024px) {
  .layout-utama {
    grid-template-columns: 280px 1fr 220px; /* Full 3 columns */
  }
}
```

Notice how the later rules automatically inherit `display: grid` and `gap: 16px` without repeating them. Try completing the `@media` blocks in the Playground!
