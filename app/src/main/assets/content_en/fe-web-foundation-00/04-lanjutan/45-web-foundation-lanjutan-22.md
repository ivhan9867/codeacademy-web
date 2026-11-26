---
id: web-foundation-lanjutan-22
courseId: fe-web-foundation-00
order: 45
title: "Forms: Input Elements and Their Types"
readingMinutes: 8
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Create an interactive form container with the <form> element"
  - "Recognize various input types: text, email, password, number, checkbox, radio, and submit"
  - "Connect inputs with descriptive text using the <label> element"
codeSnippet: |
  <div>
    <label for="nama">Nama</label>
    <br>
    <input type="text" id="nama">
  </div>
  
  <div>
    <label for="email">Email</label>
    <br>
    <input type="email" id="email">
  </div>
  
  <div>
    <label for="pesan">Pesan</label>
    <br>
    <textarea id="pesan" rows="4" cols="30"></textarea>
  </div>
---
Since the beginning of this course, you have built pages that **display** information. Now it is time for pages that can **receive** information from their visitors, through forms.

## Why forms matter

Almost every time you enter data on a website (logging in, signing up for an account, posting a comment), you are interacting with a form. Forms are the primary way a web page captures input from its users.

## The input element and its types

The `<input>` element is the most frequently used element for capturing data. What sets it apart from other elements is that `<input>` has many different **types**, configured via the `type` attribute.

```html
<input type="email">
<input type="password">
```

Some commonly used types:

- **text** (default when `type` is omitted): single-line text
- **password**: works like text, but characters are hidden as asterisks or dots
- **email**: specifically for email format; the browser flags an error if the format is invalid
- **number**: accepts numbers only
- **search**: for search fields, usually includes a clear-text icon
- **date**, **time**, **datetime-local**: for dates and times, accompanied by a helper picker popup
- **checkbox**: a checkable box
- **radio**: a single choice from multiple options; several `radio` inputs with the same `name` form one option group
- **range**: pick a number via a slider, bounded by a specific range
- **color**: pick a color via a color picker
- **file**: choose a file from the device
- **submit**: a button to submit the form
- **hidden**: hidden data, invisible to the user, yet still submitted with the form

Note: `<input>` remains strictly a single line of text, even if you press Enter while typing inside it. Any newline is automatically removed.

## Textarea: multi-line input

For inputs that need multiple lines (comments, long descriptions), use `<textarea>` instead of `<input>`. Unlike `<input>`, `<textarea>` has a closing tag, and its content is written between the opening and closing tags.

```html
<textarea rows="4" cols="30">Write your message here</textarea>
```

The `rows` and `cols` attributes control the height and width of the text area.

## Label: caption for an input

The `<label>` element provides a descriptive caption for an input. It has two benefits:

- Screen readers can explain the purpose of that input to users
- Clicking the label text automatically focuses the cursor on the associated input

How to link them: give the `<input>` an `id`, then give `<label>` a `for` attribute whose value is **identical** to that `id`.

```html
<label for="name">Name</label>
<br>
<input type="text" id="name">
```

`placeholder` (covered in the next module) is not a replacement for `<label>`. `placeholder` is only a sample hint that disappears as soon as you start typing, whereas `<label>` is a permanent caption.

## Common mistakes

- Using `<input>` for long text when `<textarea>` should be used
- The `for` value on `<label>` not matching the `id` on its paired input exactly
- Assuming `placeholder` can replace `<label>`

## Try it yourself

Create a `<label>` and `<input type="text">` pair for entering a "Hometown", connected with a matching `id` and `for`.
