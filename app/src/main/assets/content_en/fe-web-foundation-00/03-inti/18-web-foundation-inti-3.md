---
id: web-foundation-inti-3
courseId: fe-web-foundation-00
order: 18
title: "Code Breakdown: Project Folder and index.html"
readingMinutes: 5
section: INTI
learningObjectives:
  - "Understand the structure of a web project folder and root directory"
  - "Know the role of the index.html file as the main gateway file of a website"
  - "Develop the habit of naming web files using lowercase letters and no spaces"
---
In the Playground, you don't need to set anything up. But once you work on a laptop later, there are a few fundamental concepts you must know. This module covers them, while also connecting what you already learned about web servers and browsers.

## Project folder (root project directory)

A website is essentially a **collection of files**: HTML files, images, CSS, JavaScript, and more. All of those files are stored together in one regular folder called the **project folder**, or technically the *root project directory*.

Example contents:

```text
profil-rani/
  index.html
  gambar/
    foto.jpg
```

The `profil-rani` folder is the *root* of the project. All other files are addressed relative to this point. In the Playground, a folder like this is handled automatically by the app.

## File names: name and format

Look at the name `index.html`. It consists of two parts separated by a dot:

- **`index`** is the file name
- **`.html`** is its format, often called the *extension*

The extension tells the computer and browser what kind of content is inside the file: `.html` for HTML documents, `.css` for style sheets, `.js` for JavaScript, `.jpg` for images. Without `.html`, editors and operating systems tend to treat your file as plain text, and the browser won't display it as a web page.

## Why is it named index?

Remember the Web Server module: when you type an address like `contoh.com` without specifying a file name, the server has to decide on its own which file to send. By convention, the server looks for a file named **index** (usually `index.html`) in the requested folder, then sends that file.

- `contoh.com` makes the server automatically send `index.html`
- `contoh.com/tentang.html` makes the server send `tentang.html`, because the file name was specified

That is why almost every website has an `index.html` in its main folder, and why you rarely see that name appear in the address bar.

## Opening from a device vs via a domain

If you open an HTML file directly from your laptop's storage, the address bar will show a `file://` prefix followed by the file's location on your computer. No server is involved. The browser is simply reading the file from local storage.

Compare that with `https://contoh.com`, where the browser requests files from a server over the internet. For learning, both work equally well. However, only pages hosted on a server can be opened by other people.

For further reading, look up the article "URI schemes" on MDN Web Docs.

## Think about it

A project folder contains three files: `index.html`, `tentang.html`, and `kontak.html`. If a visitor opens `contoh.com/` without specifying a file name, which file does the server send? The answer is `index.html`. If they open `contoh.com/kontak.html`, the file sent is `kontak.html`.

Now let's get back to the code. It's time to give structure to that profile page.
