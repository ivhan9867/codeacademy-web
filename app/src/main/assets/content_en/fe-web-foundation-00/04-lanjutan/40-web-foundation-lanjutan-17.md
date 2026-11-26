---
id: web-foundation-lanjutan-17
courseId: fe-web-foundation-00
order: 40
title: "Exercise: Grouping Content with Div, Article, Aside, and Section"
readingMinutes: 8
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Organize info cards, hobby articles, and side panels using a combination of section, article, and aside"
  - "Apply a generic <div> wrapper for visual layout purposes"
codeSnippet: |
  <main>
    <div id="content">
      <article id="tentang-saya">
        <h2>Tentang Saya</h2>
        <p>Aku suka menggambar dan bermain gitar.</p>
      </article>
  
      <article id="hobi">
        <h2>Hobi</h2>
        <section>
          <h3>Menggambar</h3>
          <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
        </section>
        <section>
          <h3>Bermain Gitar</h3>
          <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
        </section>
      </article>
  
      <article id="rencana-belajar">
        <h2>Rencana Belajar</h2>
        <p>Menuntaskan dasar HTML sebelum lanjut ke CSS.</p>
      </article>
    </div>
  
    <aside>
      <article>
        <h2>Info Singkat</h2>
        <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
      </article>
    </aside>
  </main>
---
The major sections are organized. Now it is time to structure the inside of `<main>` in greater detail using `<div>`, `<article>`, `<aside>`, and `<section>` together.

## Exercise flow

1. Inside `<main>`, split the content into two parts: the main content (wrapped in a `<div>`) and the quick info content (wrapped in an `<aside>`)
2. Inside the `<div>`, wrap each topic (About Me, Hobbies, Study Plan) in its own `<article>`
3. Inside the Hobbies `<article>`, wrap the Drawing and Playing Guitar subsections in their own `<section>` elements
4. Move the `id` attribute from the `<h2>` element to its wrapping `<article>` element, because the `<article>` represents that entire topic
5. Add `id="content"` to the wrapping `<div>` to make styling easier later
6. Run and compare the result

## Code breakdown

Take a look at `codeSnippet`. The structure inside `<main>` is now:

```text
main
  div#content
    article#tentang-saya
    article#hobi
      section (Menggambar)
      section (Bermain Gitar)
    article#rencana-belajar
  aside
    article (Info Singkat)
```

A few important details:

- The `id` is now on `<article>`, no longer on the `<h2>` inside it. Since the navigation links use `href="#tentang-saya"`, and `id="tentang-saya"` is now on `<article>`, those links continue to work without issue
- `<div id="content">` carries no semantic meaning at all. Its purpose is purely to make styling easier later, separating the "main content" from the "quick info" beside it
- `<section>` is used for Drawing (`Menggambar`) and Playing Guitar (`Bermain Gitar`) because both are sub-themes within the broader Hobbies theme, each starting with a heading

If you run this page, once again there is no visible change. But try clicking the "Hobi" navigation link — the page still jumps to the right section, because the `id` was moved properly.

## Common mistakes

- Moving the `id` to `<article>` while forgetting to remove it from the old `<h2>`, resulting in two elements with the same `id`
- Placing a `<section>` without a heading as its first element

## Try it yourself

Add one new `<article>` for a "Kontak" (Contact) topic inside `<div id="content">`, complete with its `id`, then add its navigation link in `<header>`.
