---
id: web-foundation-inti-5
courseId: fe-web-foundation-00
order: 20
title: "Anatomy of an HTML Element"
readingMinutes: 5
section: INTI
codeLanguage: html
learningObjectives:
  - "Distinguish between an opening tag, content, and a closing tag"
  - "Understand the concept of nested elements and their closing order rules"
  - "Recognize void elements / self-closing tags that have no closing tag"
codeSnippet: |
  <p>Satu paragraf.</p>
  <p>Paragraf kedua, terpisah jarak.</p>
  
  <p>Baris pertama <br> baris kedua, tanpa jarak paragraf.</p>
  
  <p>Aku sedang belajar <strong>HTML</strong> hari ini.</p>
---
You have already used `<h1>` and `<p>`. Now it's time to dissect the element itself: what it is made of, and why it must be written in that exact way.

## What is an HTML element?

An **element** is the building block of HTML: a piece of code that marks a part of content along with its role. For example, a paragraph element and a heading element:

```html
<p>Ini sebuah paragraf.</p>
<h1>Ini sebuah judul</h1>
```

## Why are elements important?

Compare these two things. Text without elements displays as a clump of plain text. Text wrapped in `<p>` is recognized by the browser as a paragraph, so it automatically gets top and bottom spacing and is separated from other parts. `<h1>` automatically appears large and bold. That built-in appearance is called the browser's *default style*.

Even more important than appearance: elements carry **meaning**. Search engines use them to understand your page's contents, and *screen readers* (assistive tools for visually impaired users) use them to read the page aloud in a logical order. Without elements, all of that is lost.

## Three parts of an element

```text
  <p>   Halo semuanya!   </p>
   |          |            |
opening tag  content   closing tag
```

- **Opening tag**: the element name wrapped in `<` and `>`. For a paragraph, the element name is `p`, so its opening tag is `<p>`
- **Content**: the inside of the element, such as text
- **Closing tag**: the same as the opening tag, but with a forward slash `/` before the element name. It marks where the element ends

A classic beginner mistake is **forgetting to write the closing tag**. The browser will try to guess on its own where the element ends, and its guess often doesn't match expectations: the rest of the page may accidentally end up wrapped inside the wrong element.

## Elements inside elements

An element may contain other elements inside it. An element sitting inside is called a **child-element**, and its wrapper is called the **parent** element.

```html
<p>Aku sedang belajar <strong>HTML</strong> hari ini.</p>
```

Here `<p>` is the parent and `<strong>` is its child. The `<strong>` element marks important text, and usually renders in bold. The golden rule: an element opened **last** must be closed **first**.

```html
<!-- Wrong: closing order is swapped -->
<p><strong>Penting</p></strong>

<!-- Correct -->
<p><strong>Penting</strong></p>
```

## Elements without content

Just a quick preview: there are several elements that have no content and therefore don't need a closing tag. Examples include `<br>` for a line break and `<img>` for displaying an image. Both will be covered in the HTML Deep Dive section of this course.

## Common mistakes

- Forgetting the closing tag
- Swapping the closing order on nested elements
- Misspelling an element name, such as `<pp>`, so the browser doesn't recognize it

## Try it yourself

Open the Playground with the example below. Compare the part using two `<p>` tags with the part using `<br>`. Observe the line spacing, then add `<strong>` around one word in any paragraph.
