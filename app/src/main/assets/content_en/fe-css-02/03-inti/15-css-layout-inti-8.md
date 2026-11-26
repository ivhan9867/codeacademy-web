---
id: css-layout-inti-8
courseId: fe-css-02
order: 17
title: "Hands-On Practice: Building a Modern & Responsive Navigation Bar"
readingMinutes: 7
section: INTI
learningObjectives:
  - "Combine display: flex, justify-content, align-items, and gap into a real-world component"
  - "Build an elegant navigation bar (Navbar) with a logo, link menu, and call-to-action (CTA) button"
  - "Apply list margin reset techniques and precise vertical alignment"
codeLanguage: css
codeSnippet: |
  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #104246;
    padding: 12px 24px;
  }

  .nav-links {
    display: flex;
    gap: 20px;
    list-style: none;
    margin: 0;
    padding: 0;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: Navbar Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <!-- Header Navigasi -->
      <nav class="navbar">
        <div class="logo">CodeAcademy</div>
        <ul class="nav-links">
          <li><a href="#">Kelas</a></li>
          <li><a href="#">Tantangan</a></li>
          <li><a href="#">Komunitas</a></li>
        </ul>
        <button class="btn-cta">Mulai Belajar</button>
      </nav>

      <main class="hero">
        <h1>Kuasai Skill Web Bersama Kami</h1>
        <p>Belajar coding dengan materi terstruktur dan latihan interaktif langsung di browsermu.</p>
      </main>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 0;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Atur navbar menjadi flex container */
  .navbar {
    background-color: #104246;
    padding: 14px 24px;
    color: white;
    /* Tambahkan display: flex; justify-content; align-items di sini */
  }

  .logo {
    font-size: 20px;
    font-weight: bold;
    color: #389b9e;
  }

  /* 2. Atur menu links agar sejajar horizontal */
  .nav-links {
    list-style: none;
    margin: 0;
    padding: 0;
    /* Jadikan flex container juga untuk meratakan tautan di dalamnya! */
  }

  .nav-links a {
    color: #faf7f2;
    text-decoration: none;
    font-size: 15px;
    font-weight: 500;
  }

  .nav-links a:hover {
    color: #389b9e;
  }

  /* 3. Tombol Call to Action */
  .btn-cta {
    background-color: #df7b56;
    color: white;
    border: none;
    padding: 10px 18px;
    border-radius: 8px;
    font-weight: bold;
    cursor: pointer;
  }

  .hero {
    padding: 40px 24px;
    text-align: center;
  }
playgroundTask: "Complete 2 flexbox steps in CSS: (1) In '.navbar', add 'display: flex; justify-content: space-between; align-items: center;'. (2) In '.nav-links', add 'display: flex; gap: 20px;'. Run the code and watch a modern website Navbar come to life in Live Preview!"
---
It is time to bring together all the Flexbox techniques we have learned into one of the most important components on every website: the **Top Navigation Bar**.

A good navbar has three element zones:
1. **Web Logo** on the left side.
2. **Page Links List** in the middle or beside the logo.
3. **Call-to-Action Button** on the right side.

```text
.navbar (display: flex; justify-content: space-between; align-items: center)
+-------------------------------------------------------------+
| [Logo]              [Home]   [Courses] [Forum]      [Login] |
+-------------------------------------------------------------+
```
## Architecture Breakdown: Nested Flexbox

Look closely at the HTML structure in this exercise:

```html
<nav class="navbar">       <!-- Outer Container (Flex Level 1) -->
  <div class="logo">...</div>
  <ul class="nav-links">   <!-- Inner Container (Flex Level 2) -->
    <li><a href="#">...</a></li>
    <li><a href="#">...</a></li>
  </ul>
  <button class="btn-cta">...</button>
</nav>
```

Here we apply the **Nested Flexbox** technique, which is very common in the professional world:

1. **Flex Level 1 (The `<nav class="navbar">` element)**:
   - `display: flex;` enables the flexible row.
   - `justify-content: space-between;` places the Logo at the far left edge, the CTA button at the far right edge, and pushes the menu links in between them.
   - `align-items: center;` vertically aligns the logo, link text, and CTA button right in the middle of the navbar's height.

2. **Flex Level 2 (The `<ul class="nav-links">` element)**:
   - The `<ul>` tag is `block` by default and stacks each `<li>` downward.
   - By adding `display: flex; gap: 20px;` to `.nav-links`, all menu links instantly line up horizontally with a neat 20px gap!
   - Don't forget to include `list-style: none; margin: 0; padding: 0;` to strip away the bullet dots and default browser margins.

Open the Playground editor below and see for yourself how easy and clean Flexbox CSS code is for creating an elegant interface component!