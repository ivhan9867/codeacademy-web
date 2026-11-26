---
id: css-layout-pend-1
courseId: fe-css-02
order: 6
title: "Why Do We Badly Need Modern Layout?"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Know the brief history of web layout evolution from pure HTML era to modern browsers"
  - "Understand the challenges of the multi-device era (desktop, tablet, smartphone)"
  - "Realize why past layout approaches are no longer sufficient for modern web interfaces"
---
When the web was first invented by Tim Berners-Lee in the early 1990s, its goal was very simple: sharing scientific research documents among researchers. Back then, web pages did not need floating interactive buttons, advanced menu bars, or dashboard cards. Just paragraph text documents with blue *hyperlinks*.

However, over the next two decades, the web transformed into the largest application platform in the world.

```text
1990s Era (Table) -> 2000s Era (Float/Clearfix) -> Modern Era (Flexbox & Grid)
  [Rigid & Fragile]    [Prone to Collapsing]        [Responsive & Precise]
```
## Waves of Web Layout Evolution

The journey of laying out elements in browsers went through several eras that were quite exhausting for earlier web developers:

1. **HTML Table Era (`<table>`) — Late 1990s**:
   Before CSS came with layout capabilities, website makers forced `<table>`, `<tr>`, and `<td>` tags to split a page into headers, sidebars, and content. This approach made HTML code very messy (*tag soup*), slow to load, and terrible for search engine readability (*SEO*) as well as *screen readers*.
2. **CSS Float & Positioning Era — Early 2000s**:
   CSS introduced the `float` property, which was originally intended only to align images so text could flow beside them (like a print newspaper). Web developers then "hijacked" this property to divide page columns. The result? Pages frequently broke, parent elements lost their natural height (*collapsing container*), and a complicated incantation named `clearfix` was born.
3. **Smartphone Explosion Era (Responsive Web Design) — 2010 onward**:
   When people began opening websites from various phone and tablet screen sizes, rigid pixel-based and float layout techniques started to crumble. Developers needed a system that was naturally **flexible, elastic, and space-aware**.

## The Birth of Official Standards: Flexbox and CSS Grid

The web standards body (W3C) realized that web developers needed true layout tools specifically designed for application interfaces.

From here, two official CSS modules were born:
- **CSS Flexible Box Layout (Flexbox)**: Designed to distribute space and align elements along a single line (row or column) with high flexibility.
- **CSS Grid Layout (Grid)**: Designed to build a two-dimensional skeleton that has rows and columns simultaneously.

Both are now fully supported by 100% of modern browsers (Chrome, Safari, Firefox, Edge, both on computers and smartphones). By understanding both of these tools, you no longer need to use weird tricks to organize a neat, professional web page!