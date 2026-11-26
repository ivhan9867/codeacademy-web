---
id: web-foundation-lanjutan-11
courseId: fe-web-foundation-00
order: 34
title: "Exercise: Navigation That Actually Works"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Connect a navigation menu to page sections using anchor ID links (#id)"
  - "Test instant page scrolling when internal links are clicked"
codeSnippet: |
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  
  <ul>
    <li><a href="#tentang-saya">Tentang Saya</a></li>
    <li><a href="#hobi">Hobi</a></li>
    <li><a href="#rencana-belajar">Rencana Belajar</a></li>
  </ul>
  
  <h2 id="tentang-saya">Tentang Saya</h2>
  <p>Aku suka menggambar dan bermain gitar.</p>
  
  <h2 id="hobi">Hobi</h2>
  <h3>Menggambar</h3>
  <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
  
  <h2 id="rencana-belajar">Rencana Belajar</h2>
  <p>Menuntaskan dasar HTML sebelum lanjut ke CSS.</p>
---
Remember the navigation list you created a few modules ago? Now it is time to make it truly functional: click a link, and jump straight to the target section.

## Exercise flow

1. Add an `id` attribute to each `<h2>` that serves as a navigation target
2. Turn each item in the navigation list into an `<a>` element
3. Set each `<a>`'s `href` to a hash sign (`#`) followed by its target `id`
4. Run the code and try clicking each navigation link

## Why use id and the hash sign?

`id` is an attribute you can place on any element to give it a unique identifier. One of its uses is acting as a jump target within the same page. If you write `href="#tentang-saya"`, the browser immediately scrolls to the element with `id="tentang-saya"`.

```html
<a href="#hobi">Hobi</a>
<!-- ... -->
<h2 id="hobi">Hobi</h2>
```

That hash sign (`#`) in front of the `id` name is what tells the browser the destination is inside this very page, rather than a new URL.

## Code breakdown

Look at `codeSnippet`: each `<h2>` now has an `id` matching its topic (`tentang-saya`, `hobi`, `rencana-belajar`), and each item in the navigation list is wrapped in an `<a>` whose `href` points to that `id` with a hash sign.

Try clicking one of the navigation links in the Playground output. The page scrolls directly to the target section, even though not a single line of JavaScript has been written.

## Common mistakes

- The `href` value does not match the target `id` value character for character (including letter case)
- Forgetting to include the hash sign in front of the `id` name in `href`
- Using the same `id` on more than one element, leaving the browser unsure where to jump

## Try it yourself

Add a new "Contact" section complete with its `id`, then add a new navigation item pointing to it.
