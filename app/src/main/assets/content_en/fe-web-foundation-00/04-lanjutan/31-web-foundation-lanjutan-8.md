---
id: web-foundation-lanjutan-8
courseId: fe-web-foundation-00
order: 31
title: "Block Text Formatting: Blockquotes, Preformatted Text, and Figures"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Use <blockquote> for long block quotations along with attribution"
  - "Display code or text with preserved spacing using the <pre> element"
  - "Wrap illustrative content and its caption with <figure> and <figcaption>"
codeSnippet: |
  <blockquote cite="https://id.wikipedia.org/wiki/HTML">
    <p>HTML adalah bahasa markup standar untuk membangun halaman web.</p>
  </blockquote>
  
  <figure>
    <img src="https://picsum.photos/seed/gitar/400/220" alt="Gitar akustik">
    <figcaption>Gitar pertamaku, dibelikan orang tua saat SMA.</figcaption>
  </figure>
  
  <pre>
  Kutipan favoritku:
    "Belajar sedikit tiap hari
     lebih baik daripada banyak sekali lalu berhenti."
  </pre>
---
Besides paragraphs, headings, and lists, there are several special elements for block-level text formatting (elements that take up their own full line): `<blockquote>`, `<pre>`, and `<figure>`.

## Long quotations: blockquote

To display a long quotation or testimonial, use `<blockquote>`. Its content can include paragraphs, headings, or even lists.

```html
<blockquote cite="https://en.wikipedia.org/wiki/Website">
  <p>
    A website is a collection of related web pages,
    typically hosted on the same web server.
  </p>
</blockquote>
```

The `cite` attribute is optional and holds the source URL of the quote. This attribute does not change the visual appearance at all—it only provides extra information for machines or assistive tools, not directly for human readers.

## Preformatted text: pre

You already know that browsers ignore extra spaces and blank lines in HTML. However, for content like code examples or poetry, those spaces and line breaks actually matter. The `<pre>` element makes the browser display its contents **exactly as you wrote them**, including spaces and line breaks.

```html
<pre>
  First line
      Second line, indented inward
  Third line
</pre>
```

By default, `<pre>` content is also rendered in a monospace font (where every character has the same width), the classic style used for code.

## Figure and figcaption

The `<figure>` element is used for self-contained content: illustrations, diagrams, photos, or even code snippets. Its content can be moved away from the main document flow without changing the overall meaning of the document.

Inside `<figure>`, you can add `<figcaption>` as a title or caption for that content.

```html
<figure>
  <img src="https://picsum.photos/seed/gitar/400/220" alt="Acoustic guitar">
  <figcaption>My first guitar, bought by my parents in high school.</figcaption>
</figure>
```

`<figure>` doesn't have to contain an image. It is also great for wrapping a poem excerpt or a code block that you want to give a caption.

## Common mistakes

- Using `<blockquote>` for a short one- or two-word quote mid-sentence (that should use `<q>`, covered in the next module)
- Thinking `<pre>` only changes the font style, when its primary role is preserving spaces and line breaks verbatim

## Try it yourself

Add a `<figure>` containing an image and a `<figcaption>` on your profile page, along with a `<blockquote>` holding your favorite quote.
