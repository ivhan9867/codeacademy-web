---
id: web-foundation-lanjutan-3
courseId: fe-web-foundation-00
order: 26
title: "Headings"
readingMinutes: 4
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Build a structured heading hierarchy from <h1> through <h6>"
  - "Understand why headings matter for screen reader accessibility and SEO"
codeSnippet: |
  <h1>Profil Rani</h1>
  
  <h2>Tentang Saya</h2>
  <p>Pelajar dan calon web developer.</p>
  
  <h2>Rencana Belajar</h2>
  <h3>Bulan Ini</h3>
  <p>Menguntaskan dasar HTML sebelum lanjut ke CSS.</p>
---
You have already used `<h1>` and `<h2>`. This module covers headings completely.

## Six heading levels

HTML provides heading elements from `<h1>` to `<h6>`. The smaller the number, the higher its level. `<h1>` is the most important heading on the page, stepping down in importance all the way to `<h6>`.

```html
<h1>Main Heading</h1>
<h2>Subheading</h2>
<h3>Sub-subheading</h3>
```

## Why headings matter—beyond font size

Headings are not just a way to make text big and bold. They express the **outline** of a page. Assistive tools like *screen readers* rely on heading structure to help users jump from one section to another without having to read everything from the top. Search engines like Google also use headings to understand the structure and main topics of your page.

Because of this, heading order should make logical sense: `<h1>` once at the top, followed by `<h2>` for major sections beneath it, `<h3>` for subsections under `<h2>`, and so on—without skipping levels just to get a certain font size.

## Default browser appearance

By default, browsers display headings in bold, with `<h1>` being the largest and sizing down sequentially through `<h6>`. You can completely change this visual style later with CSS, while their meaning (which level they represent) stays intact.

## Try it yourself

On your profile page, add a new `<h2>` for a "Study Plan" section, followed by a `<p>` underneath it.
