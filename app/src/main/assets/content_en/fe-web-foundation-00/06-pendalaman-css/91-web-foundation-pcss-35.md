---
id: web-foundation-pcss-35
courseId: fe-web-foundation-00
order: 91
title: "Positioning: Static Flow vs. Non-Static Flow"
readingMinutes: 8
section: PENDALAMAN_CSS
learningObjectives:
  - "Conceptually distinguish between static flow and non-static flow"
  - "Understand that relative positioning lifts an element out of static flow without disturbing other elements"
  - "Use top, right, bottom, and left properties to shift non-static elements"
---
CSS has two ways of displaying elements: **static flow** and **non-static flow**. To understand the difference, let's imagine a web page in three dimensions.

Suppose we have three 200×200 pixel `<div>` elements, each with a different color:

```css
.box {
  width: 200px;
  height: 200px;
}

.first { background-color: #60d0a8; }
.second { background-color: #6495ed; }
.third { background-color: #ffa500; }
```

Because we have not set `position` on any of the three, all elements are rendered in **static flow**: neatly stacked from top to bottom, touching one another like stacked blocks.

## The domino effect in static flow

Now, try pushing the second box down using `margin-top`:

```css
.second {
  background-color: #6495ed;
  margin-top: 20px;
}
```

As a result, the third box (orange) also shifts downward following the second box. This is the defining characteristic of static flow: each element's position depends on the elements around it.

## Taking an element out of static flow

Compare that with applying `position: relative` to the second box:

```css
.second {
  background-color: #6495ed;
  position: relative;
}
```

Visually in the browser, there may be no change at all — yet in reality, this element has been "lifted" out of static flow. That means this element can now be shifted to any position **without affecting** the positions of surrounding elements.

## Shifting non-static elements

To actually move its position, use the `top`, `right`, `bottom`, or `left` properties:

```css
.second {
  background-color: #6495ed;

  position: relative;
  top: 30px;
  left: 10px;
}
```

The second box will shift 30px down and 10px to the right from its original position, **without** making the third box shift along with it like when using margin.

Here is a very important point to remember: the `top`, `right`, `bottom`, and `left` properties **only work** on elements that are outside of static flow — that is, elements with `position: relative`, `absolute`, or `fixed`. On elements with `position: static` (the default value), these four properties will have no effect at all.

## Quick exercise

1. Create three `<div>` elements with different sizes and colors, then shift the second box down with `margin-top: 20px`. Observe how the third box shifts along with it.
2. Replace `margin-top` with `position: relative; top: 20px;` on the second box, then compare: does the third box stay in place?
3. Add `left: 10px` to the same box and observe the direction it moves.
