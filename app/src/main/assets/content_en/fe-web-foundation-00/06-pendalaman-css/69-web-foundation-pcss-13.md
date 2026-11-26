---
id: web-foundation-pcss-13
courseId: fe-web-foundation-00
order: 69
title: "Exercise: Using Fonts from Google Fonts"
readingMinutes: 6
section: PENDALAMAN_CSS
codeSnippet: |
  <head>
    <meta charset="utf-8" />
    <title>Halaman Profil</title>

    <link rel="stylesheet" href="assets/styles/style.css" />

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300..700&display=swap"
      rel="stylesheet"
    />
  </head>
codeLanguage: html
learningObjectives:
  - "Include a font from Google Fonts using the <link> tag"
  - "Apply the font in CSS and understand how it works behind the scenes"
---
## Loading fonts from Google Fonts

Instead of downloading font files one by one, you can use **Google Fonts**. This service provides over 1,500 free, open-source fonts. In this section, we'll use Quicksand as an example.

Here are the steps:

1. Open **fonts.google.com** and search for "Quicksand".
2. Choose the weights you need, such as 300 and 700, then click the button to add them to your selection.
3. Click **Get embed code**, then choose the **`<link>`** option.
4. Copy the `<link>` code into your HTML `<head>` element.

```html
<head>
  <meta charset="utf-8" />
  <title>Profile Page</title>

  <link rel="stylesheet" href="assets/styles/style.css" />

  <!-- Google Fonts -->
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link
    href="https://fonts.googleapis.com/css2?family=Quicksand:wght@300..700&display=swap"
    rel="stylesheet"
  />
</head>
```

After that, reference the font name in your CSS:

```css
body {
  font-family: "Quicksand", sans-serif;
}
```

What happens behind the scenes: the URL in the `<link>` tag actually points to a CSS file containing `@font-face` rules. So the exact technique we learned earlier is used automatically by Google Fonts. The `wght@300..700` parameter tells Google that we want weights ranging from 300 to 700.

> **Note:** Google Fonts can only be loaded when your device is online. For projects that are often developed offline, it is safer to use `@font-face` with locally stored font files.

## Quick exercise

Use the profile page you created earlier.

1. Change `font-family` on `body` to `"Quicksand", sans-serif`, and observe how the appearance updates.
2. Give `h1` a `font-size` in `rem`, then change the font size in your browser settings and watch what happens.
3. Apply `text-align: center` to a heading, and `text-indent: 2em` to a long paragraph.
4. Style `a` without an underline, but make it change color on `:hover`.
5. Rewrite one of your `font` rules using the shorthand syntax, and verify that the value order is correct.
