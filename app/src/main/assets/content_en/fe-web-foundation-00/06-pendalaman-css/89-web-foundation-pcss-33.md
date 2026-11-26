---
id: web-foundation-pcss-33
courseId: fe-web-foundation-00
order: 89
title: "Exercise: Adjusting Padding and Margin on Body and Main"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Recognize the browser's default margin and padding on the body element"
  - "Remove the default gap using margin: 0 and padding: 0 on body"
  - "Add padding to the main element so content remains comfortable to view"
---
There is one important detail you might not have noticed yet. Look closely: there is a thin gap between the Profile Page content and the edges of the browser window. This gap may not be very noticeable at normal zoom, but it becomes very clear when you zoom in.

## Where does this gap come from?

The answer: **the browser's default margin**. Every browser applies its own standard styling (*default stylesheet*) to the `<body>` element, including a small margin around it. Because of this, even though we never wrote any margin for `<body>`, that gap still appears.

## Removing the gap on body

Remove the default margin and padding by setting both to `0`:

```css
body {
  font-family: 'Quicksand', sans-serif;

  margin: 0;
  padding: 0;
}
```

After this, the layout will fill the entire width and height of the browser without any gap.

## Giving main some breathing room

However, a new problem appears: the content inside `<main>` now sits right up against the edge of the screen, with no space at all. To fix this, add `padding` to the `<main>` element:

```css
main {
  padding: 20px;
}
```

With this combination — `body` without margin/padding, but `main` with padding — the page fills all the way to the screen edges, while its content still maintains a comfortable distance from the edges.

With this exercise complete, almost every part of the Profile Page looks neat. Only one big thing remains untouched: making `<div id="content">` and `<aside>` appear **side by side horizontally**. For that, we need to learn **positioning** and **floating**, two topics we will cover in the next section.

## Quick exercise

1. Add `margin: 0; padding: 0;` to `body`, then observe the gap at the screen edges disappearing.
2. Add `padding: 20px` to `main`, then compare reading comfort before and after.
3. Try changing the `main` padding to `40px`, then see how the space around the content changes.
