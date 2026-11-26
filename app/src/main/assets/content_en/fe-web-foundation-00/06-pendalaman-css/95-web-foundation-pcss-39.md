---
id: web-foundation-pcss-39
courseId: fe-web-foundation-00
order: 95
title: "Positioning: Fixed Positioning"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Use position: fixed to keep elements visible while scrolling the page"
  - "Understand that fixed is always relative to the browser window, not the parent element"
---
`position: fixed` is essentially a special type of absolute positioning. Its behavior is the same — the element is removed from the normal flow — but with one crucial difference: its position is **always relative to the browser window**, regardless of any parent element, and it **stays in place** even when the user scrolls the page.

```css
header {
  position: fixed;
  top: 0;
  left: 0;
}
```

With this code, the `<header>` will stick to the top-left corner of the browser window. When the user scrolls the page down, this header will **remain visible** in the exact same position, while other content (such as paragraphs) moves underneath it.

`position: fixed` is often used for elements that need to remain constantly visible, such as a sticky navigation bar at the top, a "back to top" button, or a chat widget in the corner of the screen.

> **Quick comparison:** `relative` shifts an element from its original position without removing its empty space. `absolute` removes the element entirely from the flow, relative to its nearest non-static positioned parent (or the browser window). `fixed` is like `absolute`, but it is always relative to the browser window and does not move when scrolling.
