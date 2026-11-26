---
id: web-foundation-pcss-38
courseId: fe-web-foundation-00
order: 94
title: "Positioning: Absolute Positioning"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Understand that absolute positioning completely removes an element from the normal flow"
  - "Learn that absolute positioning is relative to the browser window by default"
  - "Learn how to make an absolute element relative to a parent element by setting the parent to position: relative"
---
`position: absolute` is similar to `relative` in one way: the element is removed from the normal flow, so its movement does not affect surrounding elements. However, there is a major difference: an element with `position: absolute` is **completely ignored** by elements in the normal flow.

## The empty space is filled

Unlike `relative`, which leaves an empty gap at its original position, an `absolute` element leaves no space behind. As a result, the elements **below it will move up** to occupy the space that the element previously occupied.

```css
h1 {
  position: absolute;
  left: 500px;
}
```

In this example, the `<h1>` shifts 500px from the left, and the paragraph that was previously below the `<h1>` now moves up to take the `<h1>`'s original position.

## Relative to what?

By default, absolute positioning is calculated **relative to the browser window**. However, there is an important exception: if this element has a **parent** that is also outside the normal flow (meaning the parent uses `position: relative`, `absolute`, or `fixed`), then the absolute position will be calculated relative to that parent, rather than the browser window.

```html
<div class="parent">
  <h1>Title</h1>
</div>
```

If `.parent` is **not** given any `position` (it remains `static`), then the `<h1>` with `position: absolute` will ignore the `.parent` entirely and calculate its position relative to the browser window.

But once we add:

```css
.parent {
  position: relative;
}
```

The position of the `<h1>` will now be calculated **relative to the `.parent`**, no longer relative to the browser window. This pattern—a parent with `position: relative` without any `top`/`left` values, used solely to "anchor" the position of its children—is very common in practice and is often called a *positioning context*.
