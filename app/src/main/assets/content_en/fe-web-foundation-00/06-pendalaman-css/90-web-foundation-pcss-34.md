---
id: web-foundation-pcss-34
courseId: fe-web-foundation-00
order: 90
title: "Positioning: Getting to Know the Position Property"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Understand the limitations of margin for moving element positions"
  - "Learn the four main values of the position property: static, relative, absolute, fixed"
---
So far, we already know how to move an element's position using `margin`. But there is a catch: changing one element's margin **always affects** the positions of surrounding elements. So, what if we want to move a single element without disturbing any other elements at all?

The answer lies in the `position` property, which controls an element's **positioning scheme**. There are four main values we need to know:

| Value | Brief explanation |
|---|---|
| `static` | The default behavior of every element: arranged following normal flow, without being movable via `top`/`left`/etc. |
| `relative` | The element can be shifted from its original position without affecting surrounding elements |
| `absolute` | The element is removed completely from normal flow; its position is relative to the nearest non-static positioned ancestor (or the browser window if none exists) |
| `fixed` | Like absolute, but its position is always relative to the browser window, even staying in place when the page scrolls |

We will explore each of these four values in greater depth in the upcoming lessons. Before that, however, there is one foundational concept to understand first: what **normal flow** (or *static flow*) actually is, and why we sometimes need to take an element out of it.
