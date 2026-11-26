---
id: web-foundation-pcss-2
courseId: fe-web-foundation-00
order: 58
title: "Basic Selectors: Type Selector and Class Selector"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  .red {
    color: red;
  }
codeLanguage: css
starterHtml: |
  <h1 class="red">Judul berwarna merah</h1>
  <p class="red">Paragraf berwarna merah</p>
learningObjectives:
  - "Explain the purpose of the type selector and class selector"
  - "Apply one class to multiple elements, and combine multiple classes on a single element"
---
A selector is the part of a CSS rule that determines which elements will receive styling. So far, you have already used its most basic form. Now let's cover the five commonly used *basic selectors*:

1. Type selector
2. Class selector
3. ID selector
4. Attribute selector
5. Universal selector

Let's start with the first two.

## Type selector

A type selector uses an **HTML element name** as its target. That means the rule you write will apply to *every* element with that name in the document.

```css
span {
  color: red;
}
```

If a document has five `<span>` elements, all of them will turn red. Type selectors are well suited for base styles that apply broadly, such as making all paragraphs use the same font.

## Class selector

A class selector targets elements based on the value of their `class` attribute. It is written with a leading dot (`.`) followed by the class name.

```css
.red {
  color: red;
}
```

```html
<h1 class="red">Red heading</h1>
<p class="red">Red paragraph</p>
```

The advantage of a class is that it is **shareable**: a single class can be used across different types of elements. Both the heading and the paragraph above are red because both use the `red` class.

### One element, multiple classes

An element can have more than one class. Separate each class name with a space inside the `class` attribute.

```html
<h1 class="red skyblue-bg">Red text with a sky-blue background</h1>
<p class="red fancy">Red paragraph with fancy styling</p>
```

This lets you compose styles like building blocks: one class controls text color, another controls the background, and an element simply uses whatever combination it needs.

### Targeting a class on a specific element

Sometimes you want the same class to behave differently depending on the element type. To do that, write the element name followed by a dot and the class name.

```css
h1.fancy {
  font-style: italic;
}

p.fancy {
  text-decoration: underline;
}
```

Here, the `fancy` class on a heading makes it italic, whereas on a paragraph it adds an underline. Both elements share the `fancy` class, yet different rules apply.

> **Tip:** When naming classes, use names that describe their *function* or *purpose*, such as `.card` or `.important-text`, rather than color names like `.red`. Color-based names feel awkward if you ever decide to change the color later.

## Quick exercise

Create a document with one `<h2>`, two `<p>` elements, and one `<span>`. Then:

1. Write a type selector rule that makes all `<p>` elements blue.
2. Create an `.important` class and apply it to one of the paragraphs and to the `<span>`. Make sure both become bold (`font-weight: bold;`).
3. Add a second class, such as `.small` with `font-size: 14px;`, to the same paragraph that has `.important`. Notice how both classes apply at the same time.
