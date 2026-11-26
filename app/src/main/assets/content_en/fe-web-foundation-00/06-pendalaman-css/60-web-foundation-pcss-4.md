---
id: web-foundation-pcss-4
courseId: fe-web-foundation-00
order: 60
title: "Combinators: Adjacent Sibling and General Sibling Selectors"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  img + p {
    color: green;
  }
codeLanguage: css
starterHtml: |
  <img src="foto.jpg" alt="Profile photo">
  <p>This paragraph is right below the image, so it turns green.</p>
  <p>This paragraph is not directly below the image, so its color remains unchanged.</p>
learningObjectives:
  - "Differentiate between the adjacent sibling selector (+) and the general sibling selector (~)"
  - "Explain the condition required for two elements to be considered siblings"
---
So far, our selectors have only targeted a single type of element or a single condition. With **combinators**, we can construct selectors from multiple interconnected basic selectors. There are four combinators: adjacent sibling, general sibling, child, and descendant. We will cover the first two now.

Before diving in, let's understand the term **sibling**. Two elements are considered siblings if they share the **same parent**. For example, two paragraphs inside the same `<article>` are siblings.

## Adjacent Sibling Selector (+)

The adjacent sibling selector combines two selectors using the `+` sign. The rule applies only to the **second** element, and only if that element is located **directly after** the first element.

```css
img + p {
  color: green;
}
```

```html
<img src="foto.jpg" alt="Profile photo">
<p>This paragraph is right below the image, so it turns green.</p>
<p>This paragraph is not directly below the image, so its color remains unchanged.</p>
```

Two conditions must be met:
- The second element must **immediately** follow the first element in the HTML code.
- Both elements must share the **same parent**.

This pattern is useful, for example, to apply special spacing to the first paragraph immediately following a heading.

## General Sibling Selector (~)

The general sibling selector works similarly to the adjacent selector, but is more lenient. The rule applies to **all** second elements that appear *after* the first element, as long as they share the same parent, even if they are not directly adjacent.

```css
img ~ p {
  color: green;
}
```

With this rule, all paragraphs located after the `<img>` within the same parent will turn green. The symbol used is the tilde (`~`), which is usually found to the left of the `1` key on your keyboard.

## The Difference in One Sentence

`+` targets **one** element that is directly adjacent; `~` targets **all** sibling elements that appear after it.

## Quick Exercise

Create an `<article>` with the following order: `<h2>`, `<p>`, `<p>`, `<img>`, `<p>`, `<p>`.

1. Use `h2 + p` to make the first paragraph after the heading blue.
2. Use `img + p` to make the paragraph directly after the image green.
3. Use `img ~ p` and observe: which paragraphs change color?
