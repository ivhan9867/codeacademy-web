---
id: web-foundation-pcss-46
courseId: fe-web-foundation-00
order: 102
title: "Specific Styles with Media Queries"
readingMinutes: 7
section: PENDALAMAN_CSS
learningObjectives:
  - "Write basic @media rules with media-type and media-feature-rule"
  - "Apply targeted styling that only takes effect below or above a certain viewport width"
---
The viewport meta tag makes the page scale to match the device, but that is not enough. The `<article>` element is still too cramped, and `<aside>` is still hard to read on small screens. To truly apply different layouts for different screen sizes, CSS provides **Media Queries**.

With media queries, we can give instructions like: "apply this styling **only if** the viewport width is smaller than 480 pixels."

## Media query syntax structure

```css
@media media-type and (media-feature-rule) {
  /* Any CSS rules go here */
}
```

- **media-type**: the kind of media being targeted. Common values are `screen` (device screens) or `print` (when printed).
- **media-feature-rule**: the condition that must be met, such as viewport width (`max-width`, `min-width`), screen orientation, or pointer device type (touch, mouse, etc.).
- **media-block**: the set of CSS rules that only apply when both conditions above are satisfied.

Example usage:

```css
aside {
  width: 25%;
}

@media screen and (max-width: 600px) {
  aside {
    width: 100%;
  }
}
```

In this example, `<aside>` normally uses a width of 25%. But as soon as the viewport width drops below 600px, the rule inside `@media` "overrides" the earlier rule, making `<aside>` take the full width (100%).

Imagine this: with media queries like these, we can move `<aside>` above `#content` when the screen narrows, and return them side by side horizontally when the screen widens again — all automatically following the viewport width, with zero JavaScript.

Equipped with this, we are ready to fix the mobile layout issue on the Profile Page in the next exercise.
