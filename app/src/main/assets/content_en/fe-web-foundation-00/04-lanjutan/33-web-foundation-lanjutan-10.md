---
id: web-foundation-lanjutan-10
courseId: fe-web-foundation-00
order: 33
title: "Advanced Inline Text Formatting: Citations, Terms, Subscript, Superscript, Highlights, and Line Breaks"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Use <cite> for referenced work titles and <dfn> for terms being defined"
  - "Format text with <sub>, <sup>, <mark>, and the <br> line break element"
codeSnippet: |
  <p>Aku baru selesai membaca <cite>Filosofi Teras</cite> bulan ini.</p>
  
  <p><dfn>HTML</dfn> adalah bahasa markup untuk menyusun struktur halaman web.</p>
  
  <p>Air memiliki rumus kimia H<sub>2</sub>O, dan 2<sup>10</sup> sama dengan 1024.</p>
  
  <p>Kata kunci favoritku sejauh ini: <mark>elemen</mark> dan <mark>atribut</mark>.</p>
  
  <p>
    Alamat latihanku:<br>
    Jl. Belajar No. 1<br>
    Semarang, Jawa Tengah
  </p>
---
There are a few more inline elements that are useful for enriching your page content.

## Citation: cite

The `<cite>` element is used to reference the title of a work: a book, article, movie, song, and the like. By default, it is rendered in italics.

```html
<p>I just finished reading <cite>The Rainbow Troops</cite> last week.</p>
```

## Defining a term: dfn

The `<dfn>` element is used when you define a term for the first time, typically within the sentence that explains it. By default, it is also rendered in italics.

```html
<p><dfn>HTML</dfn> is a markup language used to structure web pages.</p>
```

## Subscript and superscript: sub and sup

- **`<sub>`**: displays smaller text slightly below the normal baseline
- **`<sup>`**: displays smaller text slightly above the normal baseline

Commonly used for chemical formulas or math expressions.

```html
<p>Water has the chemical formula H<sub>2</sub>O.</p>
<p>5<sup>2</sup> equals 25.</p>
```

## Highlighting text: mark

The `<mark>` element highlights parts of text that are most relevant in a sentence, such as search keywords. By default, it appears with a yellow background and black text.

```html
<p>Search results for the word <mark>Playground</mark> were found in three modules.</p>
```

## Line break: br

You already know that browsers ignore Enter keystrokes in source code. When you genuinely need a line break in the middle of a paragraph (not a new paragraph), use `<br>`. This element has no content and needs no closing tag, just like `<img>`.

```html
<p>
  Jl. Merdeka No. 10<br>
  Semarang, Central Java
</p>
```

Use `<br>` sparingly. If you need to separate two distinct ideas, a new `<p>` is usually better than stacking `<br>` tags.

## Common mistakes

- Using `<br>` repeatedly to create spacing between paragraphs instead of using separate `<p>` elements
- Using `<mark>` just to decorate text without any content-relevance reason

## Try it yourself

Add a sentence that uses `<sup>` to write an exponent, and a short address that uses `<br>` for line breaks.
