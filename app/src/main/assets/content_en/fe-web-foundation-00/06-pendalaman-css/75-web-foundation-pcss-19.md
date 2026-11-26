---
id: web-foundation-pcss-19
courseId: fe-web-foundation-00
order: 75
title: "Box Model"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Understand the box model concept as the foundation of element layout in CSS"
  - "Understand the gift box analogy for the four box model layers: content, padding, border, and margin"
---
## Introduction to the Box Model

In a browser's eyes, every HTML element—from `<h1>` headings, `<p>` paragraphs, `<a>` links, and `<img>` images to `<div>` containers—is treated as a **rectangular box**. How the browser controls the size, decoration, and position of these boxes is known as the **Box Model**.

To picture it easily in everyday life, you can use the **gift box analogy**:

```text
+-------------------------------------------------+
|  MARGIN (space between gift boxes on a shelf)   |
|  +-------------------------------------------+  |
|  |  BORDER (the cardboard box walls)         |  |
|  |  +-------------------------------------+  |  |
|  |  |  PADDING (foam / bubble wrap)       |  |  |
|  |  |  +-------------------------------+  |  |  |
|  |  |  |  CONTENT (main gift item)     |  |  |  |
|  |  |  +-------------------------------+  |  |  |
|  |  +-------------------------------------+  |  |
|  +-------------------------------------------+  |
+-------------------------------------------------+
```

This box has four layers arranged from the innermost part to the outermost:

### 1. Content (The Gift Item)
This is the core of the element, where text, images, or media are placed. The width and height of this area are usually determined by the content inside or by the `width` and `height` properties.

### 2. Padding (Cushioning / Bubble Wrap)
Imagine wrapping a fragile gift in bubble wrap. In CSS, **padding** is the transparent empty space surrounding the content so text doesn't sit directly against the box walls. If you give the element a background color (`background-color`), that color fills the padding area as well.

### 3. Border (Cardboard Box Walls)
This is the physical cardboard container enclosing the item and its cushioning. In CSS, a **border** is the outline running along the outer edge of the padding. You can customize its thickness, line style (such as solid, dashed, or dotted), and color.

### 4. Margin (Space Between Gift Boxes)
When you place several gift boxes side by side on a shelf, you leave space between them so they don't bump into each other. In CSS, **margin** is the empty space outside the border that separates the element from neighboring elements. Margins are always transparent.

## Connection to Upcoming Lessons

Every layout technique in CSS comes back to understanding these four box model layers. In the next lesson, we will see how browsers actually draw these element boxes and how to reveal their outer boundaries using a simple selector trick.
