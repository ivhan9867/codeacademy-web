---
id: web-foundation-css-3
courseId: fe-web-foundation-00
order: 52
title: "Three Ways to Attach CSS to HTML"
readingMinutes: 5
section: CSS_DASAR
codeSnippets:
  external: |
    <head>
      <title>Halaman Saya</title>
      <link rel="stylesheet" href="style.css">
    </head>
  embedded: |
    <head>
      <title>Halaman Saya</title>
      <style>
        h1 {
          color: green;
        }
      </style>
    </head>
  inline: |
    <h1 style="color: green;">Judul Halaman</h1>
learningObjectives:
  - "Distinguish between the three ways to attach CSS: external, embedded, and inline"
  - "Choose the right method for simple styling needs"
---
The CSS rules you write will not affect anything until they are "attached" to an HTML document. There are three ways to do this.

**1. External Style Sheet** — the most recommended approach. You create a separate file with a `.css` extension, then link it to HTML using a `<link>` element inside `<head>`. This single CSS file can be reused across many HTML pages at once — which is why this approach is the most powerful for real projects.

**2. Embedded Style Sheet** — CSS rules are written directly inside a `<style>` element, usually placed inside `<head>`. Unlike external style sheets, this styling only applies to that one HTML file and cannot be reused on other pages.

**3. Inline Style** — styling is written directly inside the `style` attribute on the HTML element itself, with no selector at all (since the target element is already obvious). This is the quickest way to style a single element, but the least recommended for larger projects because it mixes HTML and styling together and becomes hard to maintain.

For learning and small exercises, all three are fine. However, when you build real websites later, get into the habit of using an External Style Sheet from the start — that is what nearly all professional web developers use.
