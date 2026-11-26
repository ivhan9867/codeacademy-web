---
id: web-foundation-pcss-32
courseId: fe-web-foundation-00
order: 88
title: "Exercise: Applying Jumbotron and Navigation to the Header Element"
readingMinutes: 9
section: PENDALAMAN_CSS
learningObjectives:
  - "Create a jumbotron effect on the header using large padding and a background color"
  - "Transform a navigation list into a horizontal layout using display and list-style-type"
  - "Customize the background color and text color of the navigation"
---
Our `<header>` element still feels a bit plain. It is time to give it a **jumbotron** effect: a large, colored header with a prominent title, accompanied by clean navigation.

## Creating the Jumbotron Effect

Create a new rule with the `.jumbotron` selector:

```css
.jumbotron {
  font-size: 20px;
  padding: 60px;
  background-color: #00c8eb;
  text-align: center;
  color: white;
}
```

Apply this class by wrapping the `<h1>` and `<p>` inside the `<header>` in a `<div>`:

```html
<header>
  <div class="jumbotron">
    <h1>Bandung</h1>
    <p>The largest metropolitan city in West Java Province, which also serves as the provincial capital.</p>
  </div>
  <nav>
    <ul>
      <li><a href="#sejarah">History</a></li>
      <li><a href="#geografis">Geography</a></li>
      <li><a href="#wisata">Tourism</a></li>
    </ul>
  </nav>
</header>
```

The header will now appear with large padding and a bright background color, resembling a jumbotron on modern websites.

## Organizing the Navigation

Next, let's make the navigation horizontal. Add the `nav li` rule:

```css
nav li {
  display: inline;
  list-style-type: none;
  margin-right: 20px;
}
```

- `display: inline` makes each `<li>` appear side-by-side without forcing a new line.
- `list-style-type: none` removes the bullet points in front of each item.

Then, give the navigation a background color with the `nav` rule:

```css
nav {
  background-color: #00a2c6;
  padding: 5px;
}
```

Finally, adjust the text color of the navigation links by adding `color: white` to the existing `nav a` rule from the previous exercise:

```css
nav a {
  font-size: 18px;
  font-weight: 400;
  text-decoration: none;
  color: white;
}
```

With all these steps completed, the header and navigation of the Profile Page are starting to look neat and cohesive, featuring a matching color scheme from the jumbotron down to the navigation bar.

## Quick Exercise

1. Create `.jumbotron` and wrap `<h1>` + `<p>` inside `<header>` with `<div class="jumbotron">`.
2. Change the navigation `<li>` to `inline` and remove its bullet points.
3. Add a background color to `<nav>`, then adjust the link text color so it contrasts well with that background.
