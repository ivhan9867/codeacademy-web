---
id: web-foundation-pcss-6
courseId: fe-web-foundation-00
order: 62
title: "Pseudo-class and Pseudo-element Selectors"
readingMinutes: 7
section: PENDALAMAN_CSS
codeSnippet: |
  a:hover {
    color: blue;
  }
codeLanguage: css
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Area Latihan</title>
    </head>
    <body>
      <header>
        <h1>Judul Contoh</h1>
        <p>Paragraf contoh untuk mencoba properti CSS di atas.</p>
      </header>
      <nav>
        <ul>
          <li><a href="#">Beranda</a></li>
          <li><a href="#">Tentang</a></li>
          <li><a href="#">Kontak</a></li>
        </ul>
      </nav>
      <main>
        <div class="box">
          <span>Teks di dalam span</span>
          <p>Paragraf lain di dalam sebuah div.</p>
          <img src="https://placehold.co/150" alt="Contoh gambar" />
        </div>
      </main>
    </body>
  </html>
learningObjectives:
  - "Differentiate between pseudo-classes (element states) and pseudo-elements (parts of an element)"
  - "Use :hover, :visited, ::before, and ::after properly"
---
The selectors we have learned so far match elements based on **what is written in HTML**. Pseudo selectors are different: they target things that are **not directly written** in HTML, such as an element's state when hovered by a cursor, or a specific part of an element.

There are two types: pseudo-classes and pseudo-elements.

## Pseudo-class: selecting based on state

A pseudo-class selects an element based on its **condition or state**. It is written with a single colon (`:`).

```css
a:hover {
  color: blue;
}
```

The rule above turns a link blue when the cursor hovers over it. Some commonly used pseudo-classes include:

- `:hover`: when the cursor is over the element.
- `:visited`: links that have already been visited.
- `:focus`: an element that is currently active and receiving input, such as an input field being typed into.
- `:first-child` and `:last-child`: elements that are the first or last child of their parent.

```css
a:visited {
  color: purple;
}

input:focus {
  outline: 2px solid #00a2c6;
}
```

The list of standard pseudo-classes is quite long. You can check the full reference in the MDN documentation whenever you need it.

## Pseudo-element: selecting part of an element

A pseudo-element selects a **specific part** of an element, or creates virtual content that appears on the page without needing to be written in HTML. It is written with a double colon (`::`).

The most common ones are `::before` and `::after`, which insert virtual content before and after an element's actual content.

```css
blockquote::before {
  content: "“";
}

blockquote::after {
  content: "”";
}
```

With these rules, quotation marks appear around a quote without you having to type them in HTML. Keep in mind that the `content` property is required for a pseudo-element to appear, even if its value is empty (`content: "";`).

There are also other pseudo-elements, such as `::first-line` and `::first-letter`, which target the first line or first letter of a paragraph.

```css
p::first-letter {
  font-size: 2em;
  font-weight: bold;
}
```

## Summary of differences

| Type | Syntax | Selects |
|---|---|---|
| Pseudo-class | `:` | An element in a specific state |
| Pseudo-element | `::` | Part of an element or virtual content |

Using `::` for pseudo-elements is not strictly required by browsers for legacy ones, but it is a good habit so you can easily tell them apart from pseudo-classes.

## Quick exercise

1. Create three links. Give them a different color on `:hover`, and another color when `:visited`.
2. Create a `<blockquote>` and add quotation marks around it using `::before` and `::after`.
3. Create an `<input type="text">` and give it a colored outline on `:focus`. Try typing inside it and see the result.
