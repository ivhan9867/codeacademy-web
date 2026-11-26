---
id: web-foundation-inti-8
courseId: fe-web-foundation-00
order: 23
title: "Introduction to HTML Summary"
readingMinutes: 3
section: INTI
learningObjectives:
  - "Summarize element anatomy, attributes, and the standard HTML document skeleton"
  - "Assess readiness to build a standalone HTML document before moving on to advanced topics"
---
Congratulations! You have written your first lines of code and built a complete HTML document from scratch. Before jumping into the quiz, let's recap your journey so far.

## Key takeaways

- **Introduction to HTML**: HTML is a markup language used to organize web page structure. Think of it as the body's skeleton, with CSS as its appearance and JavaScript as its movement.
- **Your first web page**: plain text without markup is merged by the browser into a single block. Structure must be expressed through HTML.
- **Project folder and index.html**: a website is a collection of files inside a project folder. Filenames consist of a name and an extension, and `index.html` is the file a server sends by default.
- **Marking up content**: each piece of text is wrapped in an element matching its role, such as `h1` to `h3` for tiered headings and `p` for paragraphs.
- **Element anatomy**: an element has an opening tag, content, and a closing tag. Elements can be nested, forming parents and children.
- **Attributes**: extra information in the opening tag written as `name="value"`, such as `src`, `class`, `id`, and `lang`.
- **Document anatomy**: doctype, followed by `html` as the root, with `head` for page information and `body` for visible content. Together they form the DOM tree.

## The skeleton you now master

```html
<!DOCTYPE html>
<html lang="id">
  <head>
    <meta charset="utf-8">
    <title>Judul Halaman</title>
  </head>
  <body>
    <h1>Judul Utama</h1>
    <p>Sebuah paragraf.</p>
  </body>
</html>
```

This skeleton will be the starting point for almost every web page you build going forward.

## Key terms

- **Element**: the building block of HTML that marks up one piece of content
- **Opening and closing tags**: markers for the start and end of an element
- **Attribute**: extra information on an element, written in the opening tag
- **Child element**: an element placed inside another element
- **DOM Tree**: the hierarchical structure of elements in a document
- **Doctype**: the first line declaring which HTML standard is used

Feel ready? Move on to the Introduction to HTML Quiz. After that, your journey continues into the Deeper HTML and CSS Basics sections of this course.
