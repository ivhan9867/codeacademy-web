---
id: web-foundation-pcss-50
courseId: fe-web-foundation-00
order: 106
title: "CSS Deep Dive Summary"
readingMinutes: 10
section: PENDALAMAN_CSS
learningObjectives:
  - "Review all CSS Deep Dive topics, from selectors to responsiveness"
---
We have come a long way in the CSS Deep Dive—from precise selectors, text and color styling, and the box model, all the way to building responsive layouts. Here is a summary to help you review each part.

## Basic Selectors

- **Type selector** (`p`) targets all elements with that tag name.
- **Class selector** (`.name`) targets elements based on the `class` attribute and can be used across many elements at once (*shareable*).
- **ID selector** (`#name`) targets a single element with an `id` attribute that is unique across the entire document.
- **Attribute selector** (`[type="text"]`) targets elements based on the presence or value of their attributes.
- **Universal selector** (`*`) targets all elements, often used for style resets.

## Combinators

- **Adjacent sibling (`+`)**: only the second element that immediately follows the first element, sharing the same parent.
- **General sibling (`~`)**: all matching second elements that follow the first element, as long as they share the same parent.
- **Child (`>`)**: only direct children of the parent element.
- **Descendant (space)**: all descendants, whether direct or nested deeper.

## Pseudo Selectors

- **Pseudo-class** (`:hover`, `:visited`, `:focus`) selects elements based on their state.
- **Pseudo-element** (`::before`, `::after`) selects or inserts a virtual part of an element, useful for adding content without writing it in HTML.

## Font Styling

- `font-family`: the typeface, ideally written as a font stack with a generic font family at the end.
- `font-size`: text size—use `rem`/`em` for accessibility, and `px` for exact control.
- `font-weight`, `font-style`, `font-variant`: boldness, slant, and small caps.
- `font`: shorthand for the properties above, in the order `style weight size/line-height family`.

## Text Styling

- `line-height`: spacing between lines.
- `text-indent`: indentation of the first line of a paragraph.
- `text-align`: text alignment (`left`, `right`, `center`, `justify`).
- `text-decoration`: underline, overline, or strikethrough.
- `text-transform`: changes capitalization without altering the original text.
- `letter-spacing` & `word-spacing`: spacing between characters and between words.
- `text-shadow`: shadow applied to text.

## Setting Color Values

- **Numeric values**: RGB, HEX, or HSL — precise and flexible.
- **Predefined color names**: color names such as `red`, `blue` — quick to use but limited.
- `color` sets the text color, while `background-color` sets the background color.
- `opacity` controls the visibility level of the entire element; an alpha channel (`rgba()`, `hsla()`) makes only the color transparent without affecting the contents inside.

## Box Model

Every HTML element generates a box with four layers: **content**, **padding**, **border**, and **margin**.

- **Dimensions**: `width` and `height` set the content size; `min-width`/`max-width` constrain the size range; content exceeding the box dimensions will *overflow*.
- **Box-sizing**: `content-box` (default) calculates padding and border outside `width`; `border-box` calculates both inside `width`, making the final rendered size match what you specified.
- **Border**: `border-width`, `border-style`, `border-color`, or the `border` shorthand (value order is flexible).
- **Padding**: space between the content and the border.
- **Margin**: space outside the border; vertical margins between sibling elements can collapse into one another (*margin collapsing*), and `margin: 0 auto` centers an element horizontally.
- **Display roles**: `inline`, `block`, `inline-block`, and `none` change how an element behaves visually.
- **Box shadow** and **border-radius** (*rounded corners*) enhance the appearance of boxes.

## Positioning

- **Normal flow** (`static`): default behavior, where block elements stack vertically in order.
- **Relative**: shifts an element from its original position without removing the space it originally occupied.
- **Absolute**: removes an element completely from the normal flow, positioning it relative to the nearest non-static positioned ancestor (or the browser window).
- **Fixed**: similar to absolute, but always relative to the browser window and stays in place when scrolling.
- **Sticky**: a combination of relative and fixed — sticks in place once it reaches the edge of the viewport.

## Floating

- `float: left`/`float: right` makes an element float to one side, allowing other elements to flow around it (*text wrapping*), and is also used for multi-column layouts.
- Common issue: a parent element losing its height (*collapse*) when all of its children are floated.
- Solutions: the `clear` property (usually on an empty element), or `overflow: auto` on the parent.

## Responsiveness

- **Viewport meta tag** (`width=device-width, initial-scale=1`) makes the page match the device's screen width instead of automatically scaling down.
- **Media queries** (`@media screen and (max-width: ...)`) apply specific styles only under certain screen conditions, such as turning a two-column layout into stacked rows on narrow screens.

## Debugging and Optimization

- Use online CSS validation tools to check for syntax errors.
- **Minifying** CSS (removing unnecessary whitespace, comments, and newlines) speeds up page load times.

With this summary, you now have a complete picture of CSS—from fine-grained details like selectors to techniques for building responsive pages. It is time to test your understanding with the quiz in the next section!
