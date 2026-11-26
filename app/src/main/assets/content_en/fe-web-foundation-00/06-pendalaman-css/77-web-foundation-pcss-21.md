---
id: web-foundation-pcss-21
courseId: fe-web-foundation-00
order: 77
title: "Box Model: Box Dimensions"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  <div style="width: 300px; height: 300px;">
    <p style="width: 75%; height: 75%;">Paragraf ini 75% dari div induknya.</p>
  </div>
codeLanguage: html
learningObjectives:
  - "Control box dimensions with width and height"
  - "Constrain sizes using min-width, max-width, min-height, and max-height"
  - "Handle overflowing content using overflow"
---
## Box dimensions

A box's dimensions are controlled with `width` and `height`. By default, a box is always just big enough to fit its content. Setting `width` or `height` lets you take explicit control over that size.

There are three units used most often:

- **Pixels (`px`)**: fixed size and easy to control. Popular, though less flexible across different screen sizes.
- **Percentage (`%`)**: relative to the size of the parent element or browser window. Great for layouts that adapt to screen width.
- **Em (`em`)**: relative to the element's font size. The box grows when the text size increases.

Many developers now prefer percentages and `em` so layouts remain more adaptive.

```html
<div style="width: 300px; height: 300px;">
  <p style="width: 75%; height: 75%;">This paragraph is 75% of its parent div.</p>
</div>
```

In this example, the `<div>` measures 300 × 300 pixels. The paragraph inside it is 75% of its parent's size, which comes out to about 225 × 225 pixels.

### Constraining dimensions

Modern layouts often expand and shrink to match the screen. To keep content comfortable to read, we can set limits using `min-width`, `max-width`, `min-height`, and `max-height`:

- `min-width` sets the minimum allowed width.
- `max-width` sets the maximum allowed width.

```css
.article {
  max-width: 650px;
}
```

Width limits like this are important so lines of text don't stretch too wide. Lines that are too long tire the reader's eyes quickly, and on phones an unconstrained box can become awkward if not bounded properly.

### Overflow

If you set a fixed size, but the content inside is larger than the box, the content will **overflow** outside the box. This can happen when there is too much text or a large image.

```html
<div style="width: 200px; height: 100px;">
  <p>A very long paragraph that does not fit inside this small box and will spill out past its edges.</p>
</div>
```

To control this behavior, use the `overflow` property. Its possible values will be covered in the next section alongside the `box-sizing` concept.

## Quick exercise

1. Style navigation links with `font-size: 18px`, `font-weight: 400`, and no underline. Add `:hover` to make them bold.
2. Add the `profile` class to `<article>`, then center-align the `<header>` inside it.
3. Change the `body` text color to `#333`, and give a `.card` element a `#f0f8ff` background with `padding: 16px`.
4. Create a `<div>` with a 50% transparent black background using `rgba()`, and verify that the text inside remains solid.
5. Apply `* { border: 1px solid red; }` and observe all the element boxes that appear. Remove it when you're done.
6. Create a `<div>` with `width: 200px` and a long paragraph inside it, then observe how the text overflows.
