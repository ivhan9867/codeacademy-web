---
id: web-foundation-pcss-48
courseId: fe-web-foundation-00
order: 104
title: "Exercise: Applying Sticky Position to Navigation"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Learn position: sticky as a combination of relative and fixed behaviors"
  - "Apply sticky to the nav element so it remains visible while scrolling"
  - "Understand the role of display: inline on header so sticky works properly"
---
There is a small issue you may not have noticed yet on our Profile Page: the navigation only appears at the top, right below the jumbotron. As soon as the user scrolls down to read an article, that navigation disappears off screen — even though the user might want to jump straight to another section.

## Getting to know sticky positioning

There is one `position` value we have not covered yet: **`sticky`**. This value combines traits from both `relative` and `fixed`:

- Like `relative`, the element stays in its normal position as long as it has not "touched" the viewport edge.
- Once the user scrolls until the element reaches the viewport edge (for example `top: 0`), the element "sticks" there like `fixed`, remaining visible as long as its parent container is still on screen.

## Applying sticky to nav

Add `position: sticky` and `top: 0` to the `nav` selector:

```css
nav {
  background-color: #00a2c6;
  padding: 5px;

  position: sticky;
  top: 0;
}
```

`top: 0` is essential here — it tells the browser that the navigation must stick **right at the top edge** of the viewport when scrolling.

## Why isn't it working yet? The role of display: inline on header

If you test it right now, the navigation probably still will not stick properly. That is because `<nav>` sits inside `<header>`, and by default `<header>` (as a block element) "contains" its children within its own height boundary.

The fix is to set `<header>` to `display: inline`:

```css
header {
  display: inline;
}
```

With this, `<header>` no longer "locks" `<nav>` inside its height bounds, allowing `<nav>` to actually stick to the browser viewport's edge when the page scrolls — not just the edge of `<header>`.

Now, when users scroll down, the navigation remains visibly pinned to the top of the screen, making it far easier to access at any time.

## Quick exercise

1. Add `position: sticky; top: 0;` to `nav`, then scroll the page and observe whether the navigation sticks.
2. If it does not yet, add `display: inline` to `header`, then try again.
3. Experiment with `top: 10px` and observe how the navigation's gap from the top edge changes when it sticks.
