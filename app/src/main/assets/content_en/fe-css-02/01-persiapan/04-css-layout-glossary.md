---
id: css-layout-prep-glossary
courseId: fe-css-02
order: 4
title: "CSS Layout Glossary"
readingMinutes: 8
section: PERSIAPAN
learningObjectives:
  - "Learn key technical terms in the CSS Flexbox, CSS Grid, and Responsive Design ecosystems"
  - "Understand the conceptual differences between container properties and item properties"
  - "Have a quick reference dictionary for reading documentation and working on layout projects"
---
In the world of modern web layout, you will often encounter English terms that sound similar but play different roles. Use this glossary as a pocket dictionary while studying the entire **Modern CSS Layout** module.

```text
+-------------------------------------------------------------------+
|                    CSS LAYOUT TERMS MAP                           |
+-----------------------+---------------------+---------------------+
|     FLEXBOX (1D)      |    CSS GRID (2D)    |  RESPONSIVE & TOKENS|
| - Flex Container      | - Grid Container    | - Media Query       |
| - Main & Cross Axis   | - Track, Line, Cell | - Breakpoint        |
| - justify / align     | - fr, repeat, minmax| - Mobile-First      |
| - grow/shrink/basis   | - auto-fit/auto-fill| - CSS Variables     |
+-----------------------+---------------------+---------------------+
```

## Key Terms List

- **Flex Container** — The parent element given `display: flex` or `display: inline-flex` to manage the layout of all direct children within it.
- **Flex Item** — A direct child element inside a Flex Container whose position, size, and order are governed by Flexbox rules.
- **Main Axis** — The primary axis of the Flex Item flow, determined by `flex-direction` (by default, horizontal from left to right).
- **Cross Axis** — The axis that is always perpendicular to the Main Axis (by default, vertical from top to bottom).
- **Flexbox** — A one-dimensional (*1D*) CSS layout system designed to arrange elements in rows or columns with flexible space distribution.
- **CSS Grid** — A two-dimensional (*2D*) CSS layout system that allows for the simultaneous arrangement of rows and columns within a matrix structure.
- **Grid Container** — The parent element given `display: grid` or `display: inline-grid` as the main canvas where grid cells are formed.
- **Grid Item** — A direct child element inside a Grid Container that occupies one or more cells within the grid.
- **Grid Track** — The space between two adjacent Grid Lines, which can be a single vertical column lane or a single horizontal row lane.
- **Grid Line** — Numbered horizontal or vertical boundary lines (starting from 1) that form the framework of columns and rows in CSS Grid.
- **Grid Cell** — The smallest unit of a box in CSS Grid, formed by the intersection of one row and one column.
- **Grid Area** — A rectangular space formed by one or a combination of several Grid Cells, which can be given a specific name via `grid-template-areas`.
- **Explicit Grid** — Rows and columns whose sizes are defined manually and explicitly by the developer using `grid-template-columns` and `grid-template-rows`.
- **Implicit Grid** — Additional rows or columns automatically created by the browser when the number of Grid Items exceeds the capacity of the Explicit Grid.
- **fr (Fraction Unit)** — A flexible unit specific to CSS Grid that represents a fraction of the remaining free space inside the Grid Container.
- **gap** — A shorthand property to define the gutter distance between rows and columns in both Flexbox and CSS Grid without affecting outer margins.
- **justify-content** — A container property to manage the distribution and alignment of items along the Main Axis (Main Axis in Flexbox, or the horizontal axis in Grid).
- **align-items** — A container property to align items on the Cross Axis (Cross Axis in Flexbox, or the vertical axis within each Grid cell).
- **align-content** — A container property to manage the spacing and alignment between multiple lines (*multi-line*) when there is remaining space on the vertical axis.
- **align-self** — An individual property on a specific item to override the `align-items` rule of its parent container.
- **flex-direction** — A Flex Container property that determines the direction of the Main Axis, namely `row`, `row-reverse`, `column`, or `column-reverse`.
- **flex-wrap** — A Flex Container property that determines whether Flex Items are forced to fit in a single line (`nowrap`) or are allowed to wrap to a new line (`wrap`).
- **flex-grow** — A Flex Item property (a proportion number) that determines how much an item is allowed to expand to fill remaining empty space in the container.
- **flex-shrink** — A Flex Item property that determines the item's shrinkage factor when the total width of items exceeds the available container space.
- **flex-basis** — The ideal initial size of a Flex Item along the Main Axis before remaining space is distributed by `flex-grow` or `flex-shrink`.
- **order** — An item property in Flexbox and Grid that changes the visual display order of elements on the screen without changing the original HTML structure.
- **grid-template-columns** — A Grid Container property to define the number and width of each column in an Explicit Grid.
- **grid-template-rows** — A Grid Container property to define the number and height of each row in an Explicit Grid.
- **grid-template-areas** — A Grid Container property that allows for visual page layout mapping using text-based area names.
- **repeat()** — A CSS Grid function to write a repeating track size pattern concisely, for example, `repeat(3, 1fr)`.
- **minmax()** — A CSS Grid function to set a minimum and maximum size range for a track so it remains elastic but safe from content clipping.
- **auto-fit** — A keyword within `repeat()` that fills the row with as many columns as possible and collapses empty tracks so existing items expand fully.
- **auto-fill** — A keyword within `repeat()` that continues to create empty spare tracks at the end of a row even if the number of items is less than the screen capacity.
- **place-items** — A shorthand property in CSS Grid that combines `align-items` and `justify-items` into one short declaration.
- **place-content** — A shorthand property to align the entire block of rows and columns in the container simultaneously on both axes.
- **justify-items** — A Grid Container property to manage the horizontal alignment of all Grid Items within their respective cells.
- **justify-self** — An individual property on a single Grid Item to manage its own horizontal alignment within a cell without affecting other items.
- **Media Query** — A conditional CSS rule (`@media`) that only applies specific styles if device conditions, such as screen width or orientation, are met.
- **Breakpoint** — A specific screen width threshold (e.g., `768px` or `1024px`) where the layout structure changes to ensure comfortable viewing on new devices.
- **Mobile-First** — A design and CSS writing approach that starts with the small mobile screen view first, then enhances it for larger screens using `min-width`.
- **Viewport** — The window area actually visible to the user when opening a web page on a phone, tablet, or desktop monitor.
- **CSS Custom Properties (CSS Variables)** — Native CSS variables (prefixed with `--`, such as `--space-md`) stored in `:root` and called using the `var()` function for design consistency.

Save this module as a quick reference whenever you encounter new properties in the core and advanced materials!
