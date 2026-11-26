---
id: web-foundation-css-5
courseId: fe-web-foundation-00
order: 54
title: "Three Key Concepts: Inheritance, Group Selectors, and Rule Order"
readingMinutes: 5
section: CSS_DASAR
learningObjectives:
  - "Understand the concept of CSS inheritance"
  - "Understand how CSS rules are read and when styling conflicts occur"
---
Before we dive deeper, there are three fundamental concepts you will encounter frequently.

**Inheritance.** Some CSS properties are automatically "inherited" from a parent element to the elements inside it. For example, if you set `color: white` on a `<footer>`, all elements inside that footer (text, links, etc.) will also turn white, even if you don't write specific rules for each one. This is why a clean HTML structure is important—a clear structure makes style inheritance easy to predict.

**Group Selectors.** You have already practiced this in previous modules—if multiple selectors need the same declarations, just group them using a comma: `h2, h3 { color: #00a2c6; }` instead of writing two separate rules.

**Rule Order.** The "Cascading" in "Cascading Style Sheets" means that CSS is read from top to bottom. If two rules conflict for the same element, the rule written **last** wins. This is why the order of your CSS is important, especially if you are using more than one styling source.

There is a way to "force" a declaration to win regardless of the order by adding `!important` at the end of the value:

```css
p {
  color: red !important;
}
```

However, be careful: only use `!important` when absolutely necessary. If you get into the habit of using it indiscriminately, you will find it difficult to debug why a style isn't changing when you edit it—it is better to have a solid understanding of rule order first.
