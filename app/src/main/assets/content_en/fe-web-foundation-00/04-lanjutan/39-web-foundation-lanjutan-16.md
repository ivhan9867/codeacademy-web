---
id: web-foundation-lanjutan-16
courseId: fe-web-foundation-00
order: 39
title: "Exercise: Structuring Header, Main, and Footer"
readingMinutes: 8
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Reorganize the profile project using semantic layout tags <header>, <main>, and <footer>"
  - "Verify the semantic structure of the profile page in the Playground"
codeSnippet: |
  <header>
    <h1>Profil Rani</h1>
    <p>Pelajar dan calon web developer dari Semarang.</p>
    <nav>
      <ul>
        <li><a href="#tentang-saya">Tentang Saya</a></li>
        <li><a href="#hobi">Hobi</a></li>
        <li><a href="#rencana-belajar">Rencana Belajar</a></li>
      </ul>
    </nav>
  </header>
  
  <main>
    <h2 id="tentang-saya">Tentang Saya</h2>
    <p>Aku suka menggambar dan bermain gitar.</p>
  
    <h2 id="hobi">Hobi</h2>
    <h3>Menggambar</h3>
    <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
    <h3>Bermain Gitar</h3>
    <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
  
    <h2 id="rencana-belajar">Rencana Belajar</h2>
    <p>Menuntaskan dasar HTML sebelum lanjut ke CSS.</p>
  
    <h2>Info Singkat</h2>
    <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
  </main>
  
  <footer>
    <p>&copy; 2026 Rani. Dibuat sebagai latihan belajar HTML.</p>
  </footer>
---
It is time to reorganize your entire profile page using the semantic elements you just learned.

## Goal

So far, your profile page has a title, navigation, and several content sections, yet all of them sit "flat" directly inside `<body>` without top-level grouping. In this exercise, you will organize them into three major regions: the head (`header`), main content (`main`), and foot (`footer`).

## Exercise flow

1. Add a copyright paragraph at the very bottom of the page before restructuring
2. Wrap the title, description, and navigation in `<header>`
3. Wrap the copyright paragraph in `<footer>`
4. Wrap all remaining content (everything between header and footer) in `<main>`
5. Run the code and compare its appearance with before

## Code breakdown

Look at `codeSnippet`. Notice three things:

- **Header content**: the title (`h1`), description (`p`), and `nav` containing the link list are all wrapped in `<header>`
- **Main content**: all body sections (Tentang Saya, Hobi with its two subsections, Rencana Belajar) are wrapped in `<main>`, because this is the content visitors came to read
- **Footer content**: the copyright paragraph is wrapped in `<footer>`

When you run this page, its appearance **does not change at all** compared to before restructuring. That is completely normal. You have only reorganized the document structure without changing any visual styles yet (that will be CSS's job later). Now, however, browsers, search engines, and screen readers have a far clearer understanding of each section's role on your page.

## Common mistakes

- Placing navigation outside `<header>` when it belongs to the page header
- Wrapping the footer inside `<main>` (the footer should be a sibling of `<main>`, not its child)

## Try it yourself

After reorganizing, add one new sentence inside `<footer>`, such as your name and the year you created the page.
