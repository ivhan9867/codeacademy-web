---
id: web-foundation-lanjutan-2
courseId: fe-web-foundation-00
order: 25
title: "Paragraphs"
readingMinutes: 4
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Use the <p> element to organize clean blocks of readable text"
  - "Understand whitespace collapsing and default margin behavior on paragraphs"
codeSnippet: |
  <p>Aku sedang belajar HTML lebih dalam sekarang.</p>
  <p>Rasanya menyenangkan bisa mengerti struktur di balik sebuah halaman web.</p>
---
You have been using the `<p>` element since the first module. Now it is time to understand this element more deeply.

## Paragraphs are the most fundamental element

Almost all long-form text on a web page is wrapped in a `<p>` element. When you use it, the browser automatically places it on a new line with a bit of spacing underneath. This spacing is called **margin**, a CSS concept you will customize yourself later in the styling lessons.

```html
<p>This is the first paragraph.</p>
<p>This is the second paragraph, automatically spaced away from the first.</p>
```

## Paragraphs can contain other elements

The contents of a `<p>` don't have to be plain text. You can place other elements inside it, as long as they are *inline* elements (elements that flow with the text rather than taking a full line of their own). An example is the emphasis element you will learn in an upcoming module:

```html
<p>I am learning <strong>HTML</strong> today.</p>
```

## When not to use a paragraph

Use `<p>` specifically for content that is actually a paragraph. For headings, don't use `<p>` even though it won't technically throw an error, because heading elements are a much better fit (covered in the next module).

> All text displayed on a page should be wrapped in an appropriate element. Text left floating without a wrapping element is called *anonymous text*, and it is best avoided.

## Common mistakes

- Using `<p>` for titles when a heading element is far more appropriate
- Stacking multiple `<br>` tags to separate "paragraphs" instead of using separate `<p>` elements

## Try it yourself

Add a new paragraph to your profile page describing your learning experience so far.
