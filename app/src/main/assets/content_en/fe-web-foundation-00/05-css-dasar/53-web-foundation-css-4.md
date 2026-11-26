---
id: web-foundation-css-4
courseId: fe-web-foundation-00
order: 53
title: "Exercise: Styling the Profile Page"
readingMinutes: 6
section: CSS_DASAR
codeLanguage: css
codeSnippet: |
  body {
    font-family: sans-serif;
  }

  h2, h3 {
    color: #00a2c6;
  }

  footer {
    padding: 20px;
    color: white;
    background-color: #00a2c6;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Halaman Profil</title>
    </head>
    <body>
      <h2>Rani Pratiwi</h2>
      <h3>Tentang Saya</h3>
      <p>Saya suka membaca dan belajar hal baru.</p>
      <footer>© 2026 Halaman Profil Rani</footer>
    </body>
  </html>
learningObjectives:
  - "Create a separate CSS file and connect it to the profile page you built"
  - "Apply basic styling: font, text color, and background color"
---
It is time to spruce up Rani's profile page that you put together in the previous HTML modules.

## Exercise flow

1. Open your latest profile page in the Playground
2. Add new CSS: set `font-family` to `sans-serif` for the entire `<body>`
3. Give `<h2>` and `<h3>` a color using a hex code, such as `#00a2c6`
4. Give `<footer>` a background color (`background-color`), white text color, and a bit of `padding`
5. Link the CSS to your HTML via `<link rel="stylesheet" href="...">` in `<head>` — without this, your styling will not appear at all even if the CSS code is correct
6. Run and compare the before-and-after appearance

## Code breakdown

Look at `codeSnippet`: both `h2` and `h3` are given the color `#00a2c6`. Instead of writing two separate rules, both are combined into one using a comma — this is called a **group selector**, a concise way to write code when multiple elements need the exact same styling.

## Common mistakes

- Writing CSS rules but forgetting the `<link>` in HTML — the page stays plain even though the CSS file itself is right
- Forgetting the semicolon at the end of a declaration, silently breaking a rule
- Writing `color: #00a2c6` without the hash (`#`) in front of the color code

## Try it yourself

Replace `#00a2c6` with another hex code, or try using a standard color name like `color: teal;` — compare which one is easier for you to control precisely.
