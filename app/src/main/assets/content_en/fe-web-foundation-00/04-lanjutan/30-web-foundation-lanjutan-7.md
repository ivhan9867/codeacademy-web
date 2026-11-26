---
id: web-foundation-lanjutan-7
courseId: fe-web-foundation-00
order: 30
title: "Exercise: Adding Images to Your Profile Page"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Add a profile image and supporting visuals to a personal profile project"
  - "Write descriptive alternative text (alt) for every image"
codeSnippet: |
  <h1>Profil Rani</h1>
  <p>Pelajar dan calon web developer dari Semarang.</p>
  
  <h2>Tentang Saya</h2>
  <img src="https://picsum.photos/seed/rani/400/220" alt="Ilustrasi suasana belajar coding">
  <p>Aku suka menggambar dan bermain gitar.</p>
  
  <h2>Hobi</h2>
  <img src="https://picsum.photos/seed/hobi/400/220" alt="Ilustrasi alat gambar dan gitar">
  <h3>Menggambar</h3>
  <p>Aku biasa menggambar karakter kartun di buku catatan.</p>
---
Now it is your turn to add images to your own profile page.

## Exercise flow

1. Open your latest profile page in the Playground
2. Add an `<img>` in each section where an image fits well (for example, under the "Hobbies" heading)
3. Fill `src` with an image URL from the internet, and `alt` with a brief description
4. Run the code and view the result

## About image addresses: absolute vs. relative URLs

Since you are not managing local image files in the Playground yet, this exercise uses an **absolute URL**: the full address of an image from a website, such as `https://picsum.photos/400/250`.

As extra knowledge: later when you work on a project with its own image folder (for instance, `assets/image/`), you can use a **relative URL**, which is an image path written relative to the location of `index.html`, without naming any domain:

```html
<!-- Relative URL: image stored inside the project's assets/image folder -->
<img src="assets/image/bosscha.jpg" alt="Bosscha Observatory">
```

Both are equally valid—just pick the one that matches where your image is stored.

## Code breakdown

Notice `codeSnippet`: each content section (Tentang Saya, Hobi) now has a supporting image right after its heading, with an `alt` attribute relevant to that section's content.

## Common mistakes

- Putting `<img>` inside `<head>` (it will not appear, because `head` is not for visible content)
- Filling `alt` with text unrelated to the image

## Try it yourself

Compare how the page looks before and after adding images. Also try intentionally mistyping the image URL, then observe the `alt` text that appears in its place.
