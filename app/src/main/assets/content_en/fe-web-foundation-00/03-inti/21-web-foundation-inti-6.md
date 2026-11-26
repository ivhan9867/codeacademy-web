---
id: web-foundation-inti-6
courseId: fe-web-foundation-00
order: 21
title: "Attributes on HTML Elements"
readingMinutes: 5
section: INTI
codeLanguage: html
learningObjectives:
  - "Understand the role of attributes in providing extra information on an opening tag"
  - "Write attributes using the correct name-and-value pair format"
  - "Recognize global attributes that can be placed on almost any HTML element"
codeSnippet: |
  <h1 lang="id" title="Halaman profil">Profil Rani</h1>
  
  <p class="catatan" id="salam">Halo! Selamat datang di halamanku.</p>
  
  <p hidden>Paragraf ini disembunyikan oleh atribut hidden.</p>
  
  <p contenteditable="true">Klik teks ini lalu ubah isinya.</p>
---
Elements define **what** a piece of content is. Sometimes we need to give extra details about that element: which image to display, which language is used, or how it can be identified by other code. That is what **attributes** are for.

## What is an attribute?

An **attribute** is extra information for an element. It is written inside the opening tag, and generally does not appear as visible page content—instead, it affects how the element works or is identified.

The most famous example is the image element:

```html
<img src="kucing.jpg" alt="Kucing sedang tidur">
```

The `src` attribute tells the browser where the image file is located, and `alt` holds fallback text if the image fails to load or is read aloud by a screen reader. Without `src`, the `img` element wouldn't know what to show.

## Anatomy of an attribute

```text
<p class="catatan">Teks</p>
    |     |
   name  value
```

Rules for writing attributes:

- Attributes are written in the **opening tag only**, after the element name, separated by a space
- The format is `name="value"`, with the value wrapped in double quotes
- One element can have multiple attributes, separated by spaces

Take the `class` attribute as an example: it gives an identifier name to an element so CSS can target it later, such as "apply a color to every element whose class is catatan". One class name can be shared by many elements at once.

Its counterpart is `id`, an identifier that must be **unique**: a single `id` may only be used by one element on a page.

## Commonly used attributes

Some attributes can be used on almost every element. These are called global attributes. Others only fit specific elements, such as `src` on `img`. Here are the global attributes you should know, grouped so they are easy to remember.

Identity:

- **id**: unique identifier for an element
- **class**: group name, which may be shared across multiple elements
- **data-name**: stores your own custom data on an element. It starts with `data-`, followed by any name you choose, for example `data-level="1"`

Language and text:

- **lang**: language of the element's content, for example `lang="en"` for English or `lang="id"` for Indonesian
- **dir**: text direction, left-to-right (`ltr`) or right-to-left (`rtl`)
- **translate**: specifies whether the content may be translated automatically (`yes` or `no`)
- **spellcheck**: specifies whether spelling and grammar should be checked

Appearance and behavior:

- **style**: writes CSS styles directly on a single element
- **hidden**: hides the element from the page
- **contenteditable**: makes the element's content directly editable by visitors
- **draggable**: makes the element draggable

Assistance and navigation:

- **title**: extra tooltip text that appears when hovering the cursor over the element
- **tabindex**: focus order when pressing the Tab key
- **accesskey**: shortcut key to focus the element

You don't need to memorize all of them. Just know that these attributes exist, and search for "HTML global attributes" on MDN Web Docs for the full list whenever you need it.

## Common mistakes

- Writing attributes in the closing tag. A closing tag never carries attributes
- Forgetting quotation marks around an attribute value
- Using the same `id` on multiple elements

## Try it yourself

Open the example below. Notice the paragraph you can click and edit (`contenteditable`) and the paragraph that is not visible (`hidden`). Then delete the word `hidden` and run the code again to see that paragraph appear.
