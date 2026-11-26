---
id: web-foundation-lanjutan-24
courseId: fe-web-foundation-00
order: 47
title: "Submitting Form Data"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Define the data submission destination with the action attribute and the transmission method with the method attribute (GET vs POST)"
  - "Understand the difference between URL parameter data transmission (GET) and request body transmission (POST)"
codeSnippet: |
  <form action="https://contoh.com/kontak" method="POST">
    <div>
      <label for="nama">Name</label>
      <br>
      <input type="text" id="nama" name="nama" required>
    </div>
  
    <div>
      <label for="email">Email</label>
      <br>
      <input type="email" id="email" name="email" required>
    </div>
  
    <div>
      <label for="pesan">Message</label>
      <br>
      <textarea id="pesan" name="pesan" rows="4" cols="30"></textarea>
    </div>
  
    <button type="submit">Submit</button>
  </form>
---
Forms are ready to be filled out, but their data is only useful if it can be sent to the right destination. This module covers how to do that, tying it back to the web server concepts you've previously learned.

## The Form Element

All related input fields should ideally be wrapped inside a single `<form>` element, which acts as a wrapper and determines both where and how the data is sent.

```html
<form>
  <div>
    <label for="email">Email</label>
    <br>
    <input type="email" id="email" name="email">
  </div>

  <div>
    <label for="password">Password</label>
    <br>
    <input type="password" id="password" name="password">
  </div>

  <button type="submit">Submit</button>
</form>
```

## Why the `name` Attribute is Required for Every Input

When a form is submitted, each piece of data is sent as a **key-value** pair: the key is the value of the `name` attribute, and the value is what the user typed. Without a `name`, the server won't know what to label that data, so it gets ignored.

## The `action` and `method` Attributes

These two attributes on the `<form>` determine where and how the data is sent:

- **action**: The destination URL where the data is sent. This can be an absolute URL (`https://example.com/submit`) or a relative URL. If left blank, the data is sent to the current page's URL.
- **method**: The transmission method, which has two options:
  - **GET**: Data is appended to the URL as parameters, separated by an ampersand (`&`) for each data pair, for example: `https://example.com/?email=budi@mail.com&password=123`. This is ideal for requests that simply fetch data.
  - **POST**: Data is sent inside the request body, hidden from the URL. This is ideal for sensitive or larger data, such as account registration.

```html
<form action="https://contoh.com/kirim" method="POST">
  <!-- input fields -->
</form>
```

## Note for Playground Exercises

Because your exercises in the Playground are not yet connected to a real server, clicking the submit button won't actually send data anywhere. What matters right now is that you understand: the form structure is correct, every input has a `name`, and you know the roles of `action` and `method`. You will practice sending data to a real server later when you learn backend development.

## Common Mistakes

- Forgetting to wrap input fields with a `<form>`.
- Using `method="GET"` for sensitive data like passwords, which causes the data to be clearly exposed in the URL.

## Try It Yourself

Wrap the "Name", "Email", and "Message" inputs you created inside a single `<form>`, and add a `<button type="submit">Submit</button>` button at the end.
