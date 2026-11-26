---
id: web-foundation-pcss-3
courseId: fe-web-foundation-00
order: 59
title: "Basic Selectors: ID, Attribute, and Universal Selectors"
readingMinutes: 7
section: PENDALAMAN_CSS
codeSnippet: |
  #special {
    background-color: skyblue;
  }
codeLanguage: css
starterHtml: |
  <div id="special">
    <p>Konten ini punya latar biru.</p>
  </div>
learningObjectives:
  - "Use the ID selector properly, including understanding why id values must be unique"
  - "Select elements by their attributes using attribute selectors"
  - "Use the universal selector appropriately and with care"
---
The final three basic selectors complete the set you need to know. All three are straightforward to write, yet each has its own rules and best practices to keep in mind.

## ID selector

An ID selector targets an element based on its `id` attribute. It is written with a hash symbol (`#`), also known as a *pound sign* or *octothorpe*.

```css
#special {
  background-color: skyblue;
}
```

```html
<div id="special">
  <p>This content has a blue background.</p>
</div>
```

The most important rule about `id` is its **uniqueness**. An `id` value must be unique and used on only one element per page. Unlike `class`, the `id` attribute is *not* shareable.

Often, IDs are used to give meaning to generic elements like `<div>` or `<span>`, such as `<div id="header">`. However, if you want a style to be reused across multiple elements, use `class` instead of `id`. Using `id` for repeated styles makes your CSS hard to maintain.

> **Note:** IDs also have higher *weight* (specificity) than classes. We will cover that in more detail in the module on the *cascade*. For now, just remember that IDs must be unique and should be used sparingly.

## Attribute selector

An attribute selector targets elements based on the **presence of an attribute** or **its value**. Here is a simple example:

```css
input[type="text"] {
  border: 1px solid #00a2c6;
}
```

The rule above only targets `<input>` elements whose `type` attribute has the value `text`. Here are several common forms you will use:

| Syntax | Meaning |
|---|---|
| `[attr]` | Elements that have the `attr` attribute, regardless of its value |
| `[attr="value"]` | The `attr` attribute equals `value` exactly |
| `[attr~="value"]` | The `attr` attribute contains the word `value` in a space-separated list of words |
| `[attr^="value"]` | The `attr` attribute value starts with `value` |
| `[attr$="value"]` | The `attr` attribute value ends with `value` |
| `[attr*="value"]` | The `attr` attribute value contains `value` anywhere inside it |

Practical example: `a[href^="https"]` targets links whose URLs start with `https`, which is handy for adding a small icon to secure external links.

## Universal selector

The universal selector is written with an asterisk (`*`) and applies to **all elements** in the document.

```css
* {
  box-sizing: border-box;
}
```

The universal selector is commonly used for initial style resets, like the rule above, which makes element width calculations easier to reason about (we will cover this in the box model). The universal selector can also be combined with other selectors to narrow down the target:

```css
.card * {
  margin: 0;
}
```

This rule only applies to all elements inside `.card`. Be careful when using it without limits, as rules that affect every element on a page can impact performance and make styles harder to predict.

## Quick exercise

1. Add `id="main-title"` to an `<h1>`, then write a `#main-title` rule that gives it a different text color.
2. Create two `<img>` elements: one with an `alt` attribute and one without `alt`. Use `img[alt]` to apply a `border` only to the image that has `alt`.
3. Create one link whose `href` starts with `https` and another whose `href` starts with `/`. Use `a[href^="https"]` to give external links a different color.
