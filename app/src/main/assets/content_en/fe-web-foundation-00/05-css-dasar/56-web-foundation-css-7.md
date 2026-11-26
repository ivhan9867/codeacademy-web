---
id: web-foundation-css-7
courseId: fe-web-foundation-00
order: 56
title: "CSS Basics Summary"
readingMinutes: 3
section: CSS_DASAR
learningObjectives:
  - "Summarize the core CSS concepts learned in this section"
---
Up to this point, you have learned the key pillars of CSS fundamentals:

- CSS is a **declarative** language, not a programming language—its job is to control presentation and visual style, not to process logic.
- A CSS **rule** consists of a **selector** (targeting elements) and a **declaration block** (a collection of properties and values inside curly braces `{}`).
- Three ways to attach CSS to HTML: **external stylesheet** (the best-practice standard), **embedded/internal** (using the `<style>` tag), and **inline style** (directly on an element's attribute).
- **Inheritance**: Child elements automatically inherit certain styles from their parent element, such as font family and text color.
- **Cascade**: CSS is read from top to bottom. When equally specific selectors target the same element, the declaration written last overrides earlier ones.
- The `color` property changes text color, whose value can be written using color names or hexadecimal codes.

## Complete CSS Basics Summary Code Example

Here is a complete HTML document bringing all of those core concepts together:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Developer Profile Card</title>
  <style>
    /* 1. Inheritance: text color and font family are inherited by all child elements */
    body {
      font-family: sans-serif;
      color: #333333;
      background-color: #F3F4F6;
    }

    /* 2. CSS rule with a class selector and declaration block */
    .profile-card {
      background-color: #FFFFFF;
      padding: 24px;
      border-radius: 12px;
      max-width: 400px;
    }

    /* 3. Element-specific selector with a hex color code */
    h1 {
      color: #1E40AF; /* Elegant dark blue */
      margin-top: 0;
    }

    /* 4. Cascade: the second rule overrides the first rule's color */
    p.status {
      color: gray;
    }
    p.status {
      color: #059669; /* Active green wins because it is written last */
    }
  </style>
</head>
<body>
  <div class="profile-card">
    <h1>Ahmad Fauzi</h1>
    <p class="status">● Status: Ready for Web Projects</p>
    <p>Beginner web developer focused on mastering modern HTML and CSS.</p>
  </div>
</body>
</html>
```

## Explaining the Result on Screen

When the code above runs:
1. **Clean Background & Typography**: `<body>` applies a light gray background (`#F3F4F6`) across the screen and passes down the `sans-serif` font and `#333333` text color to all paragraphs inside `.profile-card`.
2. **Standalone Content Card**: The `<div class="profile-card">` element renders as a crisp white box with rounded corners, separating the card content from the page background.
3. **Clear Heading Hierarchy**: The `<h1>` tag stands out sharply in dark blue (`#1E40AF`).
4. **Cascade Principle Proven**: The status text `<p class="status">` renders in **green** (`#059669`), not gray (`gray`), proving that the second declaration lower down successfully overrides the first declaration.

This is just the first step of the foundation. In the next section, we will go further into **CSS Deep Dive**—learning the Box Model, layout arrangement, floats, and flexible displays. Complete the quiz below to make sure you have a solid grasp of all the basic concepts!
