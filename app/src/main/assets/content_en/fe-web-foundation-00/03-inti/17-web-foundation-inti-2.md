---
id: web-foundation-inti-2
courseId: fe-web-foundation-00
order: 17
title: "Practice: Your First Web Page"
readingMinutes: 7
section: INTI
codeLanguage: html
learningObjectives:
  - "Run your first web document directly in the Playground"
  - "Explain why whitespace and line breaks are ignored by the browser in plain text"
codeSnippet: |
  Profil Rani
  
  Pelajar dan calon web developer dari Semarang.
  
  Tentang Saya
  
  Aku suka menggambar dan bermain gitar. Sejak kecil aku penasaran bagaimana sebuah aplikasi dibuat.
  
  Hobi
  
  Menggambar
  
  Aku biasa menggambar karakter kartun di buku catatan.
  
  Bermain Gitar
  
  Lagu pertama yang kupelajari memakai empat kunci sederhana.
---
It's time to create your first web page. The goal of this exercise is simple: you type a page yourself, watch how the browser treats it, and discover something surprising.

## Exercise flow

1. Open the Playground via the **Try in Playground** button in the *Practice Code Example* section below this lesson
2. Look at the profile text already provided in the HTML editor
3. Run the code and observe the result
4. Replace the content with your own profile, then run it again

The text in the example consists of a name, an opening sentence, and two short story sections about hobbies. Everything is written neatly, with blank lines between sections, just like normal writing.

## What do you see?

Take a close look at the output. All the text **merges together** into one long block. The title, opening sentence, and hobby stories combine with no spacing—even though you pressed Enter many times in the editor.

Why is that? Because the browser reads HTML with its own rules: extra spaces and blank lines are **ignored**, and the text is joined into a single flow. The browser also doesn't know which part is the title, which is a subheading, and which is a paragraph, because nothing has told it yet. All it received was *plain text*.

> Content without markers is just text. Page structure must be declared explicitly, and the tool for that is HTML.

This is the main reason HTML exists. Humans recognize headings by font size and spacing, while browsers can only understand what has been **marked up**.

## Common mistakes

- Assuming pressing Enter in the editor is the same as starting a new line in the browser
- Editing the code but forgetting to re-run it, then wondering why the output stayed the same
- Thinking something is broken, when that's simply how browsers work

## Try it yourself

Add one sentence about your dream career at the end of the text, then run it. That sentence will appear stuck to the end of the same paragraph, rather than on a new line. Keep this observation in mind. In the next modules, you will fix it the right way.

But before that, let's dissect what is actually happening behind the scenes: what a project folder is and why a web page file is usually named index.html.
