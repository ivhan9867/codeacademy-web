---
id: web-foundation-lanjutan-15
courseId: fe-web-foundation-00
order: 38
title: "Generic Elements: Div and Span"
readingMinutes: 5
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Understand the role of generic elements <div> (block) and <span> (inline) when no semantic tag fits"
  - "Know when to use div/span and when to prioritize semantic tags"
codeSnippet: |
  <div id="content">
    <article>
      <h2>Tentang Saya</h2>
      <p>Aku suka menggambar dan bermain gitar.</p>
    </article>
  </div>
  
  <p>
    Kontak singkat: <span class="info">Semarang, Jawa Tengah</span>.
  </p>
---
Sometimes no semantic element truly fits a piece of content. For those cases, HTML provides two generic elements that carry no meaning of their own: `<div>` and `<span>`.

## Div

`<div>` is a generic **block**-level container. It produces no visual effect at all until styled with CSS. Its sole purpose is to group several elements so they are easier to lay out or style together, usually via `class` or `id` attributes.

```html
<div id="content">
  <article>First content</article>
  <article>Second content</article>
</div>
```

Use `<div>` when you genuinely need a wrapper, yet none of the semantic elements (`article`, `section`, `aside`, and so on) fit the meaning.

## Span

`<span>` serves a similar purpose to `<div>`, except it is **inline**: it does not start a new line, and is used to wrap a small portion of text inside a paragraph, list, or heading.

```html
<ul>
  <li>Rani: <span class="phone">0812xxxxxxx</span></li>
  <li>Budi: <span class="phone">0813xxxxxxx</span></li>
</ul>
```

With `<span class="phone">`, you can later apply dedicated CSS styling to all those phone numbers without affecting the surrounding text.

Another example—marking specific words inside a paragraph:

```html
<p>
  To make a pudding, you need <span class="ingredient">milk</span>,
  <span class="ingredient">sugar</span>, and <span class="ingredient">agar-agar</span>.
</p>
```

## Common mistakes

- Using `<div>` when a semantic element is a better fit (such as `<article>` or `<section>`)
- Using `<div>` for text in the middle of a sentence instead of `<span>` (because `<div>` is block-level and will break the line)

## Try it yourself

Wrap all the main content of your profile page (every section between the header and footer) in a single `<div id="content">`.
