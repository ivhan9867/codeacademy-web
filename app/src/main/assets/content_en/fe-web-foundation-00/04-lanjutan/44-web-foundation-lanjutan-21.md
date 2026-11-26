---
id: web-foundation-lanjutan-21
courseId: fe-web-foundation-00
order: 44
title: "Exercise: Adding a Quick Data Table"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Create a study schedule or quick profile data table inside a web page"
  - "Apply <thead>, <tbody>, and spanning cell structures to a profile data table"
codeSnippet: |
  <aside>
    <article>
      <header>
        <h2>Info Singkat</h2>
        <p>Halaman ini dibuat sebagai proyek latihan belajar HTML.</p>
      </header>
  
      <section>
        <h3>Data Singkat</h3>
        <table>
          <tr>
            <th>Kota Asal</th>
            <td>Semarang</td>
          </tr>
          <tr>
            <th>Mulai Belajar</th>
            <td>2026</td>
          </tr>
          <tr>
            <th>Alat Belajar</th>
            <td>CodeAcademy Playground</td>
          </tr>
          <tr>
            <th>Target</th>
            <td>Menguasai dasar web</td>
          </tr>
        </table>
      </section>
    </article>
  </aside>
---
Now it is time to add a real table to your profile page, inside the "Info Singkat" (Quick Info) card you already created.

## Goal

The "Info Singkat" section in `<aside>` currently only contains a heading and a single sentence. You will enrich it with a short data table about yourself, without borders, blending neatly into that card.

## Exercise flow

1. Wrap the existing "Info Singkat" heading and sentence in a `<header>`, inside the same `<article>`
2. Add a new `<section>` right after that `<header>`, inside the same `<article>`
3. Inside `<section>`, add an `<h3>` for "Data Singkat" (Quick Data) and a `<table>` containing several rows of data about yourself
4. Run and check the result

## Code breakdown

Look at `codeSnippet`. The structure of `<article>` inside `<aside>` is now:

```text
article
  header
    h2 "Info Singkat"
    p "Halaman ini dibuat sebagai..."
  section
    h3 "Data Singkat"
    table
```

Why split it into `header` and `section`? To make it clear which part is the card's opening (title and short description) and which part is the additional data (the table) — the same `header` and `section` pattern inside a larger `article` that you learned earlier.

The table here intentionally omits the `border` attribute, so the cell borders are not visible to the naked eye. This is valid — the `border` attribute is optional, and later the table's appearance (including borders, cell spacing, and more) will be styled more cleanly with CSS.

## Common mistakes

- Placing `<table>` outside `<section>`, making the structure inconsistent with the header+section pattern you already learned
- Forgetting that without the `border` attribute, a table is still valid even though its lines are not visible

## Try it yourself

Add one more row to your table, for example "Language Currently Learning: HTML".
