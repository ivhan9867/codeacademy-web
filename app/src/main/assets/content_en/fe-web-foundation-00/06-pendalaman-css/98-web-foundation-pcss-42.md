---
id: web-foundation-pcss-42
courseId: fe-web-foundation-00
order: 98
title: "The Clear Property and Overflow Technique"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Use the clear property to stop the effect of float"
  - "Understand the left, right, and both values on the clear property"
  - "Use overflow: auto as a practical way to fix parent collapse caused by float"
---
There are two common ways to solve the "parent losing height" problem caused by `float` that we discussed in the previous lesson.

## Method 1: The clear property

The `clear` property is built specifically to stop the effect of `float`. With `clear`, an element is once again "acknowledged" by elements in normal flow.

The usual way to apply it is by creating an empty element (often a `<div>` with no content) that is given the `clear` property:

```html
<div class="container">
  <img src="foto.jpg" alt="Photo" style="float: left;" />
  <div class="clearfix"></div>
</div>
```

```css
.clearfix {
  clear: left;
}
```

The `clear` value matches the `float` value you want to stop:

- `clear: left` stops the effect of elements floated to the left.
- `clear: right` stops the effect of elements floated to the right.
- `clear: both` stops **both directions at once** — the most common choice when you are unsure which float direction needs to be stopped, or when there are both left-floated and right-floated elements at the same time.

## Method 2: The overflow technique

The second method is more practical: simply add `overflow: auto` to the **parent** element containing the floated elements, without needing to add an extra empty element.

```css
.container {
  overflow: auto;
}
```

With this single line, the parent will once again calculate the height of its children properly, including floated children. Many developers prefer this approach because it is shorter and avoids adding HTML elements that are not actually needed semantically.

Both methods produce the same final result. Choosing between them usually comes down to habit or project needs — and in the next exercise, we will use the `overflow: auto` technique to tidy up the Profile Page layout.

## Quick exercise

1. Create a `<div>` containing a single `<img>` with `float: left`, then observe that the `<div>`'s height becomes 0.
2. Add `overflow: auto` to that `<div>`, then observe how its height returns to normal.
3. Create two elements with `float: left` and `float: right` inside one parent, then apply `clear: both` on an empty element after them to stop both floats.
