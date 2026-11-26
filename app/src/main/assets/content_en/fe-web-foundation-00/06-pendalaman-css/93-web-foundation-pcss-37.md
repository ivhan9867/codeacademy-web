---
id: web-foundation-pcss-37
courseId: fe-web-foundation-00
order: 93
title: "Positioning: Relative Positioning"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Apply position: relative to shift an element from its original position"
  - "Understand that the relative shift is calculated from the element's original intended position"
---
As we touched upon previously, `position: relative` allows usポーツ to shift an element upward, rightward, downward, or leftward **without affecting** the position of other surrounding elements. This happens because an element with `position: relative` is offset relative to its normal flow.

```css
p.second {
  position: relative;
  top: 10px;
  left: 100px;
}
```

In the example above, the second paragraph will shift **10 pixels down** and **100 pixels to the right** from its original position.

One important thing to remember: the `top`, `left`, `right`, and `bottom` values in relative positioning are **always calculated from the position where the element was originally supposed to be** — not from the corner of the page or another element. So if you set `top: 10px`, it means the element shifts 10px downward from where it *should* have appeared in the normal flow, not 10px from the top of the page.

The space previously occupied by this element in the normal flow **remains empty** — as if the element is still there, even though it has visually moved. This is what distinguishes relative from absolute positioning, which we will discuss next.
