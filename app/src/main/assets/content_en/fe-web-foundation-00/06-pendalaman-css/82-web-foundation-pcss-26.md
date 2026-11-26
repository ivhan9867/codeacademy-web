---
id: web-foundation-pcss-26
courseId: fe-web-foundation-00
order: 82
title: "Display Roles: Inline, Block, Inline-Block, and None"
readingMinutes: 9
section: PENDALAMAN_CSS
codeSnippet: |
  span {
    display: block;
  }

  a {
    display: inline-block;
    width: 120px;
    padding: 8px;
  }
codeLanguage: css
starterHtml: |
  <nav>
    <ul>
      <li><a href="#">Beranda</a></li>
      <li><a href="#">Tentang</a></li>
      <li><a href="#">Kontak</a></li>
    </ul>
  </nav>
learningObjectives:
  - "Recall the behavior of inline and block elements from the HTML module"
  - "Change an element's display behavior with the display property"
  - "Use display: inline-block to combine inline and block characteristics"
  - "Hide an element from the page with display: none"
---
In the previous HTML module, you learned that every HTML element has one of two default display behaviors: **inline** or **block**. Now let's dive deeper, because CSS allows us to **change** that default behavior.

## Recalling inline and block behavior

**Inline elements** (such as `<span>`, `<a>`, `<strong>`):
- Do not start on a new line when rendered.
- Their width and height follow their content's size and **cannot be changed** with `width`/`height`.
- Margin and padding only affect **horizontal** spacing, not vertical distance to other elements.

**Block elements** (such as `<div>`, `<p>`, `<h1>`):
- Always start on a new line, pushing the following element down below.
- If no width is set, the element fills the full width of its parent.
- Their dimensions (`width`, `height`) can be set freely.
- Can contain other HTML elements inside them.

## Changing behavior with the display property

The `display` property lets us change this default behavior. Its values include:

| Value | Effect |
|---|---|
| `inline` | Makes a block element behave like an inline element |
| `block` | Makes an inline element behave like a block element |
| `inline-block` | Does not create a new line (like inline), but dimensions can be set (like block) |
| `none` | Hides the element completely from the page |

```css
span {
  display: block;
}

a {
  display: inline-block;
  width: 120px;
  padding: 8px;
}
```

## Use case: horizontal navigation

One of the most common uses of `display` is making navigation horizontal. Navigation lists are usually built with `<ul>` and `<li>`, both of which are block-level. As a result, each menu item appears on its own line.

```html
<nav>
  <ul>
    <li><a href="#">Beranda</a></li>
    <li><a href="#">Tentang</a></li>
    <li><a href="#">Kontak</a></li>
  </ul>
</nav>
```

By changing `<li>` to `inline`, the menu items will appear side by side on a single line:

```css
nav li {
  display: inline;
  margin-right: 16px;
}
```

If you need to add vertical `padding` to each item (for example, to increase the clickable area), use `inline-block` instead of `inline`, because `inline` does not support dimension sizing properly:

```css
nav li {
  display: inline-block;
  padding: 8px 16px;
}
```

## Hiding elements with display: none

The `none` value makes an element disappear completely, as if it were never on the page. Other elements will fill the space it leaves behind.

```css
.sembunyikan-di-mobile {
  display: none;
}
```

This property is often used together with *media queries* to hide specific elements on small screens, a topic we will cover in the responsiveness module.

## Quick exercise

1. Create a navigation list with `<ul>` and `<li>`, then change `<li>` to `inline-block` with padding.
2. Create a `<span>` and change it to `display: block`. Observe how surrounding elements shift.
3. Create an element with `display: none`, then compare it with an element using `opacity: 0`. What is the difference in the space left on the page?
