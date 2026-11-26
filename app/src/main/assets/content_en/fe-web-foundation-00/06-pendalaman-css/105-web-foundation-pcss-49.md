---
id: web-foundation-pcss-49
courseId: fe-web-foundation-00
order: 105
title: "CSS Debugging and Optimization"
readingMinutes: 5
section: PENDALAMAN_CSS
learningObjectives:
  - "Know how to check CSS validity using online tools"
  - "Understand the benefits of CSS minification to speed up page load times"
---
After writing a lot of CSS throughout this module, it is a good practice to learn how to inspect and clean up your code before actually deploying it to production.

## Checking for CSS errors

The fastest way to make sure the CSS you write is valid is to use free **online validation tools**. You simply paste your CSS code, upload the file, or enter the website URL you want to check. Some services you can use include:

- W3C CSS Validation Service
- CSS Validator Online by codebeautify.org
- CSS Lint

Tools like these will point out which line has an issue—for example, a misspelled property, an invalid value, or incorrect syntax—which is much faster than searching for errors manually one by one.

## Reducing CSS file size (minification)

The size of a CSS file affects how quickly a page loads in the browser. The smaller the file size, the faster the page appears, especially on slow internet connections.

The **minification** process removes parts that do not affect how CSS works, yet take up file space, such as:

- new line characters,
- extra whitespace,
- unnecessary block delimiters,
- CSS comments.

These changes can be done manually, but it is much more practical to use automated tools, such as:

- CSSNANO Playground
- CSS Minifier Tool and Compressor
- Minify (JavaScript and CSS minifier)

Making a habit of checking validity and minifying CSS before using it on a real website is a great practice, especially for projects that will be accessed by many people.
