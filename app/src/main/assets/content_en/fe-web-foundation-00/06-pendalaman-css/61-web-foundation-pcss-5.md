---
id: web-foundation-pcss-5
courseId: fe-web-foundation-00
order: 61
title: "Combinators: Child Selector and Descendant Selector"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  div > p {
    background-color: yellow;
  }
codeLanguage: css
starterHtml: |
  <div>
    <p>Paragraf ini anak langsung dari div, jadi kuning.</p>
    <section>
      <p>Paragraf ini ada di dalam section, bukan anak langsung div, jadi tidak kuning.</p>
    </section>
  </div>
learningObjectives:
  - "Differentiate between child selector (>) and descendant selector (space)"
  - "Determine when to use child selector to prevent styles from spreading to deeply nested elements"
---
The last two combinators deal with **parent and child relationships**. They look similar, but their scopes are different, and that difference is often the source of hard-to-find bugs.

## Child Selector (>)

The child selector only targets elements that are **direct children** of their parent.

```css
div > p {
  background-color: yellow;
}
```

```html
<div>
  <p>This paragraph is a direct child of the div, so it turns yellow.</p>
  <section>
    <p>This paragraph is inside a section, not a direct child of the div, so it does not turn yellow.</p>
  </section>
</div>
```

In the example above, the second paragraph is not affected by the style because there is a `<section>` in between. The child selector ensures the style applies only one level deep.

## Descendant Selector (space)

The descendant selector uses a **space** between two selectors. The rule applies to *all* descendants, whether direct or indirect, at any depth.

```css
div p {
  background-color: yellow;
}
```

With this rule, both paragraphs in the previous example will turn yellow, because both are descendants of the `<div>`.

## When to Use Which?

The descendant selector is practical and concise, but it can spread further than you intend. The child selector is more specific and safer when the HTML structure changes frequently. As a simple guideline:

- Use **descendant** when you genuinely want the style to apply to all contents within a container.
- Use **child** when you only want to target elements at a specific level.

> **Best practice:** Avoid excessively long selectors like `body div section article p span`. Such selectors are fragile because changing the HTML structure even slightly can break all the styles. Instead, simply use clear classes, for example, `.article-content p`.

## Quick Exercise

1. Create a `<div class="box">` containing two direct `<p>` elements and one `<section>` that contains a `<p>`.
2. First, use `.box > p` to give it a yellow background color. Notice which paragraphs change.
3. Replace it with `.box p` and compare the results.
