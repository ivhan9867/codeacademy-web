---
id: web-foundation-pcss-14
courseId: fe-web-foundation-00
order: 70
title: "Exercise: Setting Text Size, Weight, and Alignment on the Profile Page"
readingMinutes: 8
section: PENDALAMAN_CSS
codeSnippet: |
  nav a:hover {
    font-weight: bold;
  }
codeLanguage: css
starterHtml: |
  <aside>
    <article class="profile">
      <header>
        <!-- Kode lainnya disembunyikan... -->
      </header>
      <section>
        <!-- Kode lainnya disembunyikan... -->
      </section>
    </article>
  </aside>
learningObjectives:
  - "Apply font-size, font-weight, and text-decoration to navigation links"
  - "Use the :hover pseudo-class to bold links on hover"
  - "Clean up the header and footer using text-align"
---
## Exercise: text size, weight, and alignment

In this exercise, we'll refine several parts of the Profile Page you built earlier. Open your latest project in VS Code and follow these steps.

### Step 1: Navigation size and weight

First, set the text size for the navigation list (`<nav>`). Add the following rule to `style.css`:

```css
nav a {
  font-size: 18px;
}
```

Next, make the navigation links normal weight and remove the underline:

```css
nav a {
  font-size: 18px;
  font-weight: 400;
  text-decoration: none;
}
```

Open the page in your browser. The navigation text should now be larger and no longer underlined.

### Step 2: Effect when hovering over a link

Links feel more interactive when they respond to the cursor. Here we use the `:hover` pseudo-class:

```css
nav a:hover {
  font-weight: bold;
}
```

Now when the cursor hovers over a navigation link, its text becomes bold. A simple effect like this is enough to signal to visitors that the element is clickable.

### Step 3: Adding a class to `<article>`

To make it easier to target the profile section, add the `profile` class to the `<article>` element:

```html
<aside>
  <article class="profile">
    <header>
      <!-- Other code hidden... -->
    </header>
    <section>
      <!-- Other code hidden... -->
    </section>
  </article>
</aside>
```

With this class, we can target `<header>` only inside the profile section rather than across the whole page:

```css
.profile header {
  text-align: center;
}
```

Notice that `.profile header` is the descendant selector we learned earlier. It means this rule applies to any `<header>` located inside an element with the `profile` class.

### Step 4: Centered and bold footer

Finally, center-align and bold the footer text:

```css
footer {
  padding: 20px;
  color: white;
  background-color: #00a2c6;
  text-align: center;
  font-weight: bold;
}
```

Once you have followed all the steps, your `style.css` should look like this:

```css
body {
  font-family: 'Quicksand', sans-serif;
}

h2,
h3 {
  color: #00a2c6;
}

footer {
  padding: 20px;
  color: white;
  background-color: #00a2c6;
  text-align: center;
  font-weight: bold;
}

nav a {
  font-size: 18px;
  font-weight: 400;
  text-decoration: none;
}

nav a:hover {
  font-weight: bold;
}

.profile header {
  text-align: center;
}
```

Small updates like these show how CSS works in layers: each rule improves one piece, and the final result comes from all of them working together.
