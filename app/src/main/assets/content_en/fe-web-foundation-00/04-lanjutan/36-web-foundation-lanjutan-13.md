---
id: web-foundation-lanjutan-13
courseId: fe-web-foundation-00
order: 36
title: "Semantic HTML: Header, Footer, Main, and Nav"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Structure the main page layout using <header>, <nav>, <main>, and <footer>"
  - "Ensure every web page has one clear <main> primary content region"
codeSnippet: |
  <header>
    <h1>Profil Rani</h1>
    <p>Pelajar dan calon web developer dari Semarang.</p>
  </header>
  
  <main>
    <p>Konten utama halaman akan ada di sini.</p>
  </main>
  
  <footer>
    <p>&copy; 2026 Rani.</p>
  </footer>
---
The first four semantic elements you are learning act like the head, foot, main body, and navigation of a page.

## Header

`<header>` is the introduction or opening section of content. At the `<body>` level, it usually contains the site title and main navigation.

```html
<header>
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  <nav>
    <ul>
      <li><a href="#tentang-saya">Tentang Saya</a></li>
      <li><a href="#hobi">Hobi</a></li>
    </ul>
  </nav>
</header>
```

`<header>` can also be used inside an `<article>` or `<section>`, usually to hold that section's title and author. One important rule: a `<header>` must not be placed inside another `<header>` (no nesting).

## Footer

`<footer>` is the closing footnote area, usually containing copyright info, contact details, or extra links.

```html
<footer>
  <p>&copy; 2026 Rani. Built as an HTML learning exercise.</p>
</footer>
```

Just like `<header>`, `<footer>` can also be placed inside an `<article>` or `<section>` for section-specific extra info, and it likewise cannot be nested inside another `<footer>`.

## Main

`<main>` wraps the **primary** content of the page. Inside it, you can have many `<section>` or `<article>` elements, as long as all of them are part of the main content.

```html
<body>
  <header>
    <!-- header content -->
  </header>
  <main>
    <!-- main content -->
  </main>
  <footer>
    <!-- footer content -->
  </footer>
</body>
```

The strict rule: **there can only be one `<main>` per page**.

> Note: `<!-- text -->` is an HTML comment. The text inside is not displayed on the page—it is just a note for fellow developers.

## Nav

`<nav>` wraps a group of **major** navigation links, such as the site's primary menu. You already saw an example inside `<header>` above.

Not every group of links needs to be wrapped in `<nav>`. For example, a few "related articles" links at the end of an article are usually not considered primary navigation, so they don't need `<nav>`.

Clear navigation greatly improves accessibility: screen reader users can jump straight to navigation without having to listen through the entire page first.

## Common mistakes

- Placing more than one `<main>` on a single page
- Putting a `<header>` inside another `<header>`
- Wrapping every group of links in `<nav>`, when only primary navigation needs it

## Try it yourself

Before jumping into the full exercise in the next module, try wrapping your own title and opening paragraph in `<header>` in the Playground, and check whether the visual appearance changes (spoiler: it won't, because `<header>` has no default styling applied yet).
