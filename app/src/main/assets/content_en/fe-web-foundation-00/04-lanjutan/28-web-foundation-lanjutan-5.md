---
id: web-foundation-lanjutan-5
courseId: fe-web-foundation-00
order: 28
title: "Exercise: Navigation with Lists"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Practice creating a simple navigation menu using <ul> and <li> lists"
  - "Organize menu links inside a semantic list structure"
codeSnippet: |
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  
  <ul>
    <li>Tentang Saya</li>
    <li>Hobi</li>
    <li>Rencana Belajar</li>
  </ul>
  
  <h2>Tentang Saya</h2>
  <p>Aku suka menggambar dan bermain gitar.</p>
---
It is time to use `<ul>` for a real-world need: a navigation list on your profile page.

## Exercise flow

1. Open your latest profile page in the Playground
2. Add a `<ul>` containing the list of page sections right below the opening paragraph
3. Run the code and observe the result

## Why `<ul>` instead of `<ol>`?

A navigation list points to page sections that readers can open in whatever order they want, unlike a cooking recipe that must be followed in sequence. Because the order has no strict sequential meaning, `<ul>` is the right choice—even though its visual layout (such as displaying horizontally without bullets) will be styled with CSS in upcoming lessons.

## Code breakdown

Look at `codeSnippet`: right after the opening paragraph, there is a `<ul>` with three `<li>` items, each naming a section of the page.

Right now, that list only **looks** like navigation—it doesn't **work** as navigation yet. Clicking the text "Hobi" won't jump you to the Hobi section. For that, you need the anchor element (`<a>`), which you will learn a few modules ahead.

## Common mistakes

- Placing `<li>` outside `<ul>` or `<ol>`
- Thinking a navigation list should always use `<ol>` because it is "ordered top to bottom" — visual order is different from semantic order

## Try it yourself

Add one new item to your navigation list, such as "Contact".
