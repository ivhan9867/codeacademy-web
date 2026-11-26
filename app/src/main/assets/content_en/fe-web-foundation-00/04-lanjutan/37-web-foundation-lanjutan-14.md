---
id: web-foundation-lanjutan-14
courseId: fe-web-foundation-00
order: 37
title: "Semantic HTML: Article, Aside, and Section"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Group standalone content with <article>, sidebars with <aside>, and themed segments with <section>"
  - "Build a complete article content hierarchy with internal headings"
codeSnippet: |
  <main>
    <article>
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
  
    <aside>
      <article>
        <h2>Info Singkat</h2>
        <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
      </article>
    </aside>
  </main>
---
The next three semantic elements are smaller and more specific than header, main, and footer.

## Article

`<article>` wraps content that is **self-contained**: it makes sense on its own without depending on surrounding content. Examples include a blog post, a comment, or a forum post. If a page has several standalone pieces of content, each one belongs in its own `<article>`.

```html
<article>
  <h2>About Me</h2>
  <p>I enjoy drawing and playing guitar.</p>
</article>

<article>
  <h2>Study Plan</h2>
  <p>Finishing HTML basics before moving on to CSS.</p>
</article>
```

An `<article>` may be nested inside another `<article>`, as long as its content is directly related to the parent article.

## Aside

`<aside>` has two meanings depending on where it sits:

- **Inside** an `<article>`: contains information related to that article, though not part of its core content
- **Outside** an `<article>` (directly inside `<main>` or `<body>`): contains information related to the page as a whole

It is commonly displayed beside the element that contains it (which you will style later using CSS).

```html
<main>
  <article>
    <!-- main content -->
  </article>

  <aside>
    <article>
      <h2>Quick Info</h2>
      <p>This page was built as an HTML practice project.</p>
    </article>
  </aside>
</main>
```

## Section

`<section>` groups content sharing the same theme, and should ideally begin with a heading (`h1`-`h6`) as its first element to mark the section's title. It works well inside a long `<article>` that can be broken down into distinct parts.

```html
<article>
  <section>
    <h3>Drawing</h3>
    <p>I usually sketch cartoon characters in a notebook.</p>
  </section>

  <section>
    <h3>Playing Guitar</h3>
    <p>The first song I learned used four simple chords.</p>
  </section>
</article>
```

## Common mistakes

- Using `<section>` without a heading inside it
- Using `<article>` for content that cannot actually stand on its own (and completely depends on surrounding content)
- Confusing `<section>` with `<div>` — use `<section>` when the content is a genuine themed segment with a heading, and use `<div>` (next module) when no semantic meaning fits

## Try it yourself

Wrap the "Study Plan" part of your profile page in an `<article>`, then add a small `<aside>` containing one sentence about the page itself.
