---
id: web-foundation-inti-7
courseId: fe-web-foundation-00
order: 22
title: "Anatomy of an HTML Document"
readingMinutes: 7
section: INTI
codeLanguage: html
isProjectContinuation: true
verificationRules:
  - "<!DOCTYPE html>"
  - "<title>"
  - "<body>"
learningObjectives:
  - "Explain the purpose of the <!DOCTYPE html> declaration for the modern HTML5 standard"
  - "Distinguish the roles of <html>, <head> for metadata, and <body> for visible content"
  - "Add <meta charset='utf-8'> and <title> elements inside the head section"
codeSnippet: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="utf-8">
      <title>Profil Rani</title>
    </head>
    <body>
      <h1>Profil Rani</h1>
      <p>Pelajar dan calon web developer dari Semarang.</p>
  
      <h2>Tentang Saya</h2>
      <p>
        Aku suka menggambar dan bermain gitar. Sejak kecil aku penasaran
        bagaimana sebuah aplikasi dibuat.
      </p>
  
      <h2>Hobi</h2>
      <h3>Menggambar</h3>
      <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
      <h3>Bermain Gitar</h3>
      <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
    </body>
  </html>
---
A newspaper has a clear structure: a big headline, subheadings, and paragraphs underneath. A web page works the same way. The difference is that its structure isn't just seen by humans—it is also read by the browser. In this module, we will break down the entire anatomy of an HTML document from top to bottom.

## Document tree (DOM Tree)

Because elements can live inside other elements, the entire document forms a **hierarchy**, much like a family tree. There are parents, children, and siblings. Its shape can be visualized like a tree:

```text
html
  head
    meta
    title
  body
    h1
    h2
    p
```

The `html` element is the **root** of the tree. The `head` and `body` elements are children of `html`, and siblings to each other. The `h1`, `h2`, and `p` elements are children of `body`.

The browser stores your document as this tree in memory, which is called the **DOM** (*Document Object Model*). Later, when you learn JavaScript, you will update what appears on the page by modifying this tree.

## Doctype

The first line of an HTML document is the **doctype**. Its job is to tell the browser which HTML standard the document uses. In the past, the doctype was long and hard to memorize. Today, it is just one line:

```html
<!DOCTYPE html>
```

If the doctype is missing, the browser may enter *quirks mode*, a compatibility mode for legacy pages. As a result, layouts can look different across browsers and become unpredictable. So always write it, and always on the very first line.

## The html element

The `<html>` element wraps the entire document content and serves as the root of the tree. You should add the `lang` attribute to declare the page's language:

```html
<html lang="id">
```

With that, browsers and screen readers know the language of your page (for example, `id` for Indonesian or `en` for English).

## The head element

The `<head>` element stores **information about the page**, not content that visitors see. Anything inside `head` is not displayed on the page itself. Elements commonly placed inside it include:

- `<title>`: page title shown in the browser tab, bookmarks, and search results
- `<meta>`: metadata, such as character encoding, page description, or mobile viewport settings
- `<link>`: connects external resources, such as CSS files or the page icon
- `<style>`: writes CSS directly inside the document
- `<script>`: loads or writes JavaScript
- `<noscript>`: fallback content displayed if JavaScript is disabled
- `<base>`: base URL for all relative links on the page

Example of a simple `head` section:

```html
<head>
  <meta charset="utf-8">
  <title>Page Title</title>
  <style>
    /* CSS code is written here */
  </style>
</head>
```

The HTML5 specification technically allows omitting the `<head>` and `<body>` tags, because the browser will insert them automatically. Don't take advantage of that. Writing both makes the document clearer and avoids surprises, and it is the habit professional developers follow.

## The body element

The `<body>` element holds **everything that appears** on screen: headings, paragraphs, images, links, and so on. If `head` is the kitchen, `body` is the dining room where the dishes are served.

## A complete document

Putting it all together, like in the *Practice Code Example*, forms a complete and valid HTML document:

- First line: doctype
- The `html` element with `lang="id"` as the root
- `head` containing `meta charset` and `title`
- `body` containing visible content with heading and paragraph elements

## Common mistakes

- Putting `<title>` inside `<body>`
- Writing two `<body>` elements or two `<html>` elements
- Forgetting the doctype
- Expecting the contents of `head` to appear on the page

## Try it yourself

Add `<meta name="description" content="Personal profile page">` inside `head`, then run the code. Nothing changes on screen. Why? Because `head` is not meant to be displayed. That information is read by browsers and search engines.
