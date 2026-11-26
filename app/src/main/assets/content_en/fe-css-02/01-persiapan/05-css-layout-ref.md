---
id: css-layout-prep-ref
courseId: fe-css-02
order: 5
title: "Further References & Learning Resources"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Know accurate official documentation sources for Flexbox and CSS Grid specifications"
  - "Get familiar with visual practice platforms and interactive games to sharpen layout intuition"
  - "Understand how to combine reference reading with hands-on experiments in browser DevTools"
---
Learning CSS layout is not enough by reading theory just once. Professional web developers always keep a list of trusted references for when they forget a property name or run into unique layout cases.

```text
[Official Docs (MDN)] + [Visual Guides (CSS-Tricks)] + [Practice Games] = Fast Layout Mastery
```

## 1. Official Documentation & Technical References

The following sources are the most accurate references because they are continuously updated to match modern browser standards:

| Source | Material Focus | When to Use |
| :--- | :--- | :--- |
| **MDN Web Docs: CSS Flexible Box Layout** | Complete Flexbox specification, `flex-basis` behavior, and browser compatibility | When you need explanations of edge-case behaviors or official property values |
| **MDN Web Docs: CSS Grid Layout** | Guide to Explicit/Implicit Grid concepts, subgrid, and alignment | When designing complex grid structures or checking Grid function syntax |
| **web.dev: Learn CSS (Layout & Grid)** | Structured modules from the Google Chrome team on Flexbox, Grid, and Responsive Design | When you want to understand best practices and modern UI performance |

List of primary reference links:
- **MDN Flexbox**: `https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_flexible_box_layout`
- **MDN CSS Grid**: `https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout`
- **web.dev Learn CSS**: `https://web.dev/learn/css`

## 2. Visual Guides & Popular Cheatsheets

When you forget whether a property belongs to the *container* or to the *item*, the illustrated guides from **CSS-Tricks** are the fastest lifesavers:

- **CSS-Tricks: A Complete Guide to Flexbox** (`https://css-tricks.com/snippets/css/a-complete-guide-to-flexbox/`) — Clearly separates the left column (properties for Parent/Container) and the right column (properties for Children/Items), complete with visual illustrations.
- **CSS-Tricks: A Complete Guide to CSS Grid** (`https://css-tricks.com/snippets/css/complete-guide-grid/`) — Presents visual diagrams for `grid-template-areas`, `justify-items`, `place-items`, and grid track flow.

## 3. Interactive Game-Based Practice

To train your brain's reflexes in determining `justify-content`, `align-items`, and `grid-column` without overthinking, try these two classic free games:

- **Flexbox Froggy** (`https://flexboxfroggy.com/`) — Consists of 24 interactive levels guiding frogs onto lily pads using Flexbox properties (`justify-content`, `align-items`, `flex-direction`, `order`, and `align-self`).
- **Grid Garden** (`https://cssgridgarden.com/`) — Consists of 28 levels for watering a carrot garden using CSS Grid properties (`grid-column-start`, `grid-column-end`, `grid-template`, and `fr`).

## 4. Effective Practice Tips with Browser DevTools

Besides the websites above, your best weapon is already built into your laptop browser (Chrome, Edge, or Firefox):

1. Right-click an element on a web page and choose **Inspect**.
2. Look for the small badge labeled `flex` or `grid` next to the element in the **Elements** panel.
3. Click that `grid` or `flex` badge to display overlay guide lines showing row numbers, column numbers, and `gap` areas directly on top of the web page.
4. Use the **Flexbox/Grid Editor** icon button in the **Styles** panel to test alignment variations instantly without retyping code.

With the combination of structured materials in this app and the reference sources above, you are ready to step into the core Flexbox and Grid material with confidence!