---
id: web-foundation-pcss-47
courseId: fe-web-foundation-00
order: 103
title: "Exercise: Building a Mobile Version for the Profile Page"
readingMinutes: 8
section: PENDALAMAN_CSS
learningObjectives:
  - "Add the viewport meta tag to an HTML document"
  - "Write a media query so #content and aside stack vertically on narrow screens"
---
The last two modules have given us plenty of theory. Now it is time to apply both to the Profile Page: the viewport meta tag and a media query.

## Step 1: Adding the viewport meta tag

Open `index.html`, then add the following tag inside `<head>`:

```html
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Halaman Profil Bandung</title>

  <link rel="stylesheet" href="assets/styles/style.css" />
</head>
```

If you run it now in mobile simulation, the page no longer shrinks — yet its two-column layout (`#content` and `aside`) still feels too narrow for a small screen.

## Step 2: Rearranging the layout with a media query

We will make `#content` and `aside` **stack vertically** (instead of sitting side by side horizontally) when the viewport width drops below 1000px. To do that, give both `width: 100%` inside a media query:

```css
#content {
  float: left;
  width: 75%;
}

aside {
  float: right;
  width: 25%;
  padding-left: 20px;
}

@media screen and (max-width: 1000px) {
  #content,
  aside {
    width: 100%;
    padding: 0;
  }
}
```

Explanation of each part:

- `screen`: this media query applies to screen displays (not when printing).
- `max-width: 1000px`: the rules inside only take effect when the viewport width is **1000px or less**.
- `width: 100%` on `#content` and `aside`: because both are 100%, they automatically stack vertically (remember: floated elements need free space beside them to sit side by side; at 100% width, there is no space left).
- `padding: 0`: removes the padding previously used to create spacing in the two-column layout, since it is no longer needed when stacked.

After this change, the Profile Page automatically adapts: showing two side-by-side columns on wide screens, and stacking neatly vertically on narrow screens — all with just a single media query block.

## Quick exercise

1. Add the viewport meta tag to `index.html`, then try opening the page in DevTools mobile simulation.
2. Add the `@media screen and (max-width: 1000px)` media query that sets `#content` and `aside` to `width: 100%`.
3. Manually widen and narrow the browser window (without DevTools) to watch the transition between the two-column layout and the stacked layout.
