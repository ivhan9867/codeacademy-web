---
id: web-foundation-inti-4
courseId: fe-web-foundation-00
order: 19
title: "Practice: Marking Up Content with Elements"
readingMinutes: 7
section: INTI
codeLanguage: html
isProjectContinuation: true
verificationRules:
  - "<h1>"
  - "<h2>"
  - "<p>"
learningObjectives:
  - "Practice marking up profile text using heading and paragraph elements"
  - "Observe how text appearance changes after being wrapped in HTML elements in the Playground"
  - "Understand how browsers distinguish headings and paragraphs based on markup tags"
codeSnippet: |
  <!DOCTYPE html>
  <html>
    <head>
      <meta charset="utf-8">
      <title>Profil Rani</title>
    </head>
    <body>
      <h1>Profil Rani</h1>
      <p>Pelajar dan calon web developer dari Semarang.</p>
  
      <h2>Tentang Saya</h2>
      <p>
        Aku suka menggambar dan bermain gitar. Sejak kecil aku penasaran
        bagaimana sebuah aplikasi dibuat.
      </p>
  
      <h2>Hobi</h2>
      <h3>Menggambar</h3>
      <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
      <h3>Bermain Gitar</h3>
      <p>Lagu pertama yang kupelajari memakai empat kunci sederhana.</p>
    </body>
  </html>
---
In the first exercise, your profile page merged into a single block because the browser didn't know the role of each section. Now we fix it by **marking up** each section using HTML elements.

## Exercise flow

1. Write the HTML document skeleton
2. Move all text inside the body section
3. Wrap each piece of text with the appropriate element
4. Add basic settings in the head section
5. Run it and compare with the previous result

Retype the code yourself in the Playground—don't just read it.

## Step 1: document skeleton

Every HTML document starts with the same skeleton:

```html
<!DOCTYPE html>
<html>
  <head>
  </head>
  <body>
    <!-- all visible content is written here -->
  </body>
</html>
```

Briefly explained: `<!DOCTYPE html>` tells the browser this is a modern HTML document, `<html>` wraps the entire document, `<head>` stores information about the page (not displayed), and `<body>` holds everything displayed on the screen.

## Steps 2 and 3: identifying the role of each part

Here is how to think about it: for every piece of text, ask **"what is the role of this text?"** and then choose the matching element.

- "Profil Rani" is the main page title, so it uses `<h1>`
- "Tentang Saya" and "Hobi" are subheadings, so they use `<h2>`
- "Menggambar" and "Bermain Gitar" are sections inside Hobbies, so they use `<h3>`
- Normal sentences are paragraphs, so they use `<p>`

Headings are ranked from `h1` to `h6`; the larger the number, the lower the level. A healthy rule of thumb: use `h1` only once per page, `h2` for major sections beneath it, and `h3` for sections beneath `h2`. Never skip levels just because you want smaller text. Font size is CSS's job, while heading levels declare **content hierarchy**.

## Step 4: settings in head

Two things that belong in `<head>` right from the start:

- `<meta charset="utf-8">` specifies how characters are stored and read, so letters and special symbols display properly
- `<title>` holds the page title, which appears in the browser tab, bookmarks, and search results. This is not the title shown inside the page itself

## Code breakdown

Look at the final result in the *Practice Code Example* section below. Notice three things:

- All text is now inside `<body>`, and every piece of text has its own element
- Indentation uses two spaces. The browser doesn't care about that, but humans reading the code find it very helpful
- The browser result: the title and subheadings appear large and bold, and paragraphs are neatly separated. This looks very different from the earlier block of text, even though the wording is identical

## Common mistakes

- Placing content inside `<head>`, causing the text not to appear on the page
- Using `h1` multiple times, or picking `h3` simply because its font is smaller
- Forgetting to close an element, such as writing `<p>` without `</p>`

## Try it yourself

Add a new section named "Cita-cita" (Aspirations) as an `<h2>`, followed by one `<p>` containing a sentence about your dream career. Place it between the Tentang Saya and Hobi sections.
