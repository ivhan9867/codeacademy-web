---
id: web-foundation-pcss-36
courseId: fe-web-foundation-00
order: 92
title: "Positioning: Normal Flow"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Understand normal flow as the default behavior of every block element"
  - "Learn that normal flow is equivalent to position: static"
---
**Normal flow** is the default way the browser lays out elements: every block element is placed directly below the previous one, from top to bottom, in the order they appear in the HTML. Since this is the default behavior, we don't actually need to write anything to get it. Technically, this behavior is equivalent to writing `position: static` — the default value of the `position` property.

```css
h1 {
  /* width not set, automatically takes up full width */
}

p {
  width: 450px;
}
```

Notice two things in the example above:

- The `<h1>` element without a defined `width` will automatically take up the **entire width** of the available space in the browser window.
- The `<p>` element given `width: 450px` will have its width restricted to just 450px. However, even though there is still empty space next to it, the next `<p>` element will **still appear below it**, not beside it.

That is the essence of normal flow: block elements are always arranged vertically, one by one, regardless of how much empty space is left beside them. To make elements sit side by side horizontally, we need another technique — such as `float`, which we will discuss after covering all types of positioning.
