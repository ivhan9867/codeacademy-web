---
id: web-foundation-pcss-40
courseId: fe-web-foundation-00
order: 96
title: "Floating"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Understand the basic purpose of the float property for placing elements to the left or right"
  - "Use float to arrange multi-column layouts"
  - "Understand the importance of setting width when using float"
---
The `float` property has a simple job: it makes an element "float" to the **left** or **right** side of its parent. When applied in certain contexts, surrounding elements will flow around the floated element (called *text wrapping*) — similar to an image surrounded by text in a magazine or article.

```css
img {
  float: left;
  margin-right: 12px;
}
```

In addition to text wrapping, `float` was also used for a long time as one of the main techniques for building **multi-column layouts**, because the concept is simple and easy to understand.

## Arranging two columns with float

```css
.left-content {
  float: left;
  width: 30%;
}

.right-content {
  float: right;
  width: 70%;
}
```

With `float: left` and `float: right`, these two elements will sit side by side: the left content takes 30% of the width, and the right content takes the remaining 70%.

> **Important:** When using `float`, **always specify a `width`**. Without a clear width, the browser does not know how much space to allocate for each element, and the result can be unpredictable.

## Arranging more than two columns

What if we need three or more columns? Simply apply `float: left` to all of them, and the columns will line up sequentially from left to right, following their order in the HTML:

```css
.kolom {
  float: left;
  width: 33.33%;
}
```

```html
<div class="kolom">Column 1</div>
<div class="kolom">Column 2</div>
<div class="kolom">Column 3</div>
```

These three `<div>` elements will line up side by side from left to right, each taking one-third of its parent's width.
