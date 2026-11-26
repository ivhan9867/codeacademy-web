---
id: web-foundation-lanjutan-4
courseId: fe-web-foundation-00
order: 27
title: "Lists: Unordered, Ordered, and Description Lists"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Create unordered lists with <ul> and ordered lists with <ol>"
  - "Organize term-and-description pairs using <dl>, <dt>, and <dd> elements"
  - "Configure the type, start, and reversed attributes on ordered lists"
codeSnippet: |
  <h2>Navigasi</h2>
  <ul>
    <li>Tentang Saya</li>
    <li>Hobi</li>
    <li>Rencana Belajar</li>
  </ul>
  
  <h2>Langkah Belajarku Hari Ini</h2>
  <ol>
    <li>Baca materi</li>
    <li>Coba kodenya di Playground</li>
    <li>Kerjakan tantangan kecil</li>
  </ol>
---
Not every piece of text belongs in a paragraph. A list is one common example, and HTML provides three types of lists.

## Three types of lists

- **Unordered list**: a list where order doesn't matter, usually displayed with bullets
- **Ordered list**: a list where order matters, displayed with automatic numbering
- **Description list**: a list containing terms along with their explanations

## Unordered list

Created with `<ul>`, with each item wrapped in `<li>`.

```html
<ul>
  <li>History</li>
  <li>Geography</li>
  <li>Tourism</li>
</ul>
```

An `<li>` can contain other HTML elements—even headings or paragraphs—and a `<ul>` can contain another `<ul>` inside it to create a *nested list*.

## Ordered list

Created with `<ol>`, while its items still use `<li>`. The difference is that each item is automatically numbered in sequence, making it ideal for step-by-step instructions.

```html
<ol>
  <li>Open the Playground</li>
  <li>Write the document skeleton</li>
  <li>Run the code</li>
</ol>
```

You can change the numbering style using the `type` attribute:

- `type="1"` — numbers (default)
- `type="a"` — lowercase letters
- `type="A"` — uppercase letters
- `type="i"` — lowercase Roman numerals
- `type="I"` — uppercase Roman numerals

Two other useful attributes on `<ol>`:

- **start**: sets the starting number, for example `start="7"` makes the first item start at 7
- **reversed**: reverses the numbering so it counts down. This attribute needs no value—just write its name

```html
<ol start="7">
  <li>Seventh item</li>
  <li>Eighth item</li>
</ol>

<ol reversed>
  <li>Displayed with descending numbers</li>
  <li>Then counts down by one more</li>
</ol>
```

## Description list

Used for pairs of terms and their explanations, such as a glossary. Created with `<dl>`, using `<dt>` (*description term*) for the term and `<dd>` (*description details*) for its explanation.

```html
<dl>
  <dt>HTML</dt>
  <dd>A markup language used to structure web pages.</dd>

  <dt>Element</dt>
  <dd>An HTML building block that marks up a piece of content.</dd>
</dl>
```

One `<dt>` can be followed by more than one `<dd>` if a term has multiple explanations.

## Common mistakes

- Using `<ol>` for a list whose order doesn't actually matter
- Forgetting that `<li>` must sit inside `<ul>` or `<ol>`, never on its own

## Try it yourself

Add a `<ul>` listing your hobbies on your profile page, then create an `<ol>` with three steps describing how you practice coding each day.
