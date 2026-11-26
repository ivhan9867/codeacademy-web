---
id: web-foundation-pcss-41
courseId: fe-web-foundation-00
order: 97
title: "The Problems with Applying Float"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Recognize the collapse problem: a parent element with a height of 0px when its child is floated"
  - "Understand that floated elements are taken out of the normal flow so they are not recognized by their parent"
---
`float` looks very easy to use, whether for text wrapping or building layouts. But there is one side effect that beginners almost always encounter: **the parent container loses its height**.

Imagine a `<div>` that only contains one `<img>` element, and that image is given `float: left`:

```html
<div class="container">
  <img src="photo.jpg" alt="Photo" />
</div>
```

```css
.container img {
  float: left;
}
```

The result is surprising: `<div class="container">` will have a **height of 0 pixels**, as if it contains nothing at all — even though visually the image is still displayed on the screen.

## Why does this happen?

The reason lies in the core nature of `float`: floated elements are **taken out of the normal flow**, so they are **not considered to exist** by their parent element. The parent only calculates its height based on elements that are still in the normal flow. Since the only child inside `.container` has been floated, the parent thinks it is empty.

This problem becomes even more apparent if you add another element (like a paragraph) below the floated image: that paragraph will "rise up" as if the image never existed, because the space that the image should occupy is no longer calculated by its parent.

There are two common ways to solve this problem, which we will discuss in the next two modules: using the **`clear`** property, and setting **`overflow: auto`** on the parent element.
