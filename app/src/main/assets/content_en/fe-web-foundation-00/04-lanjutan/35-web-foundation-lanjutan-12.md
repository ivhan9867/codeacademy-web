---
id: web-foundation-lanjutan-12
courseId: fe-web-foundation-00
order: 35
title: "Semantic HTML: Organizing Page Content"
readingMinutes: 4
section: LANJUTAN
learningObjectives:
  - "Understand the concept and benefits of semantic HTML for SEO and screen readers"
  - "Distinguish between semantically meaningful elements and purely visual ones"
---
Remember the newspaper analogy? A newspaper page has a masthead, news stories, side columns, and footnotes. A web page has the exact same needs, and HTML has dedicated elements for them.

## Before HTML5: everything became a div

Before the elements you will learn in this module existed, developers grouped page sections (site headers, navigation, articles) using `<div>`, a generic element that carries no meaning at all. To tell them apart, they relied on `class` or `id`, such as `<div class="header">`.

The problem is that browsers, search engines, and screen readers do not know that `<div class="header">` is actually a header. To them, it is just an ordinary `<div>` with a label stuck onto it. The structure becomes meaningless, often described as *semantically meaningless*.

## HTML5: elements named after their roles

HTML5 introduced elements whose very names describe their roles: `<header>`, `<nav>`, `<main>`, `<article>`, `<aside>`, `<footer>`, and `<section>`. This is called **semantic HTML**.

With semantic elements, browsers, search engines, and screen readers immediately know each section's role without having to guess from class names. It also makes it much easier for fellow developers to read each other's code.

The next few modules cover these elements one by one, and then you will reorganize your profile page using them.
