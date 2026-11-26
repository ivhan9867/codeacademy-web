---
id: web-foundation-pend-6
courseId: fe-web-foundation-00
order: 14
title: "Website Anatomy"
readingMinutes: 2
section: PENDAHULUAN
learningObjectives:
  - "Distinguish the roles of the web trio: HTML (structure), CSS (appearance), and JavaScript (behavior)"
  - "Understand the skeleton, skin, and movement analogy in building interactive web pages"
---
Up to this point, you already know how data moves from the server to your screen. Now let's open it up: what is a web page actually made of?

## The three foundations of a website

Almost all websites are built from three core technologies:

- **HTML**: a *markup* language that defines the structure or skeleton of a page, such as headings, paragraphs, images, and buttons.
- **CSS**: a styling language (*style*) that controls appearance, such as colors, font sizes, spacing, and layout.
- **JavaScript**: the main programming language run directly by the browser to make pages interactive.

Technically, a web page can exist with HTML alone. However, the result looks plain and rigid. Today there is hardly any website that doesn't use all three together, and if any one of them is missing, the result feels incomplete.

## Analogy: building a house

Picture a house:

- **HTML** is the frame and walls: determining how many rooms there are, and where the doors and windows go.
- **CSS** is the paint, flooring, and furniture arrangement: making the house look comfortable and pleasing to the eye.
- **JavaScript** is the electricity: switches that turn on lights, automatic doors, and everything that reacts when you interact with it.

A house without paint can still be lived in, but it's less appealing. A house without electricity looks nice, but doesn't feel "alive."

## Just a peek

No need to understand the details yet, because you will learn them one by one in upcoming courses. Just look at how the three share the work for a single heading.

HTML (structure):

```html
<h1 id="judul">Halo, Web!</h1>
```

CSS (appearance):

```css
h1 {
  color: teal;
}
```

JavaScript (interaction):

```js
document.getElementById("judul").textContent = "Halo, Dunia!";
```

HTML creates the heading, CSS colors it, and JavaScript changes its text. In the HTML and CSS modules in this course, you will practice them yourself in the Playground.
