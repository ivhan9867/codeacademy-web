---
id: web-foundation-pcss-20
courseId: fe-web-foundation-00
order: 76
title: "What Is a Box Element"
readingMinutes: 3
section: PENDALAMAN_CSS
codeSnippet: |
  * {
    border: 1px solid red;
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
  - "Understand the outer boundary of an element (edge of element)"
  - "Use the universal selector to inspect element boxes during debugging"
---
## Visualizing Element Boxes

After learning the gift box analogy in the previous lesson, you know that every web element has four layers: *content*, *padding*, *border*, and *margin*. Yet when you open a typical web page, these boxes are often invisible because their backgrounds are transparent and they have no default borders.

Beyond those four layers, there is an important concept called the **edge of element**—the outermost boundary of an element where the margin area ends. This boundary is not physically visible, but the browser always calculates it strictly to determine where the next element can begin.

## Trick to See Every Box with the Universal Selector

When building a page layout and noticing that an element looks out of place, you can ask the browser to draw outlines around every element at once. Simply add a thin border using the **universal selector** (`*`):

```css
* {
  border: 1px solid red;
}
```

With that single rule, the browser frames every element—from large wrappers like `<body>` and `<header>` down to small elements like `<span>` and `<a>`—with a red outline. This trick is very popular among web developers for layout *debugging* because it lets you see firsthand how those boxes stack and sit next to each other.

You actually touched the box model back in the earlier footer exercise when you added `padding: 20px`. In the upcoming lessons, we will learn how to control each of these four layers with precision one by one.
