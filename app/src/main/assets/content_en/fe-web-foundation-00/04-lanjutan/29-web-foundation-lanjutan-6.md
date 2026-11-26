---
id: web-foundation-lanjutan-6
courseId: fe-web-foundation-00
order: 29
title: "Images"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Display images on a web page using the <img> element"
  - "Provide the required src attribute for the image path and alt for accessibility alternative text"
  - "Maintain responsive image proportions using width or height"
codeSnippet: |
  <h2>Tentang Saya</h2>
  <img src="https://picsum.photos/400/250" alt="Ilustrasi suasana belajar" width="400">
  <p>Aku suka menggambar dan bermain gitar.</p>
---
A page without images can feel lifeless. This module covers how to display images in HTML.

## The img element

Images are displayed with the `<img>` element. This element is different from most other elements: it has no content and no closing tag.

```html
<img src="https://example.com/photo.jpg" alt="Image description">
```

Its two essential attributes:

- **src**: the image source, which can be a website URL or a local file path
- **alt**: alternative text shown if the image fails to load, and read aloud by screen readers. Not strictly required by syntax, but strongly recommended to always include

Another useful attribute: **title**, extra text that appears when hovering the cursor over the image.

## Common image formats

- **GIF** (`.gif`): supports animation, small size, limited color quality
- **JPEG** (`.jpg`, `.jpeg`): relatively small file size, great for photos, less crisp for images with text
- **PNG** (`.png`): sharper text and details than JPEG, but larger file size
- **WebP** (`.webp`): better compression than JPEG and PNG with comparable quality
- **SVG** (`.svg`): vector-based, small file size and stays sharp at any scale; great for logos and icons, not suited for photos

## Setting image dimensions

Dimensions are controlled through the `width` and `height` attributes. An important rule: **only set one of them**, and let the other adjust automatically to match the image's original aspect ratio.

```html
<!-- Safe: only width is set, height adjusts automatically -->
<img src="photo.jpg" alt="Photo" width="400">
```

If you force both values with a ratio that doesn't match the original image, the image will appear squished or stretched out of proportion.

```html
<!-- Avoid this if the aspect ratio doesn't match -->
<img src="photo.jpg" alt="Photo" width="500" height="200">
```

## Common mistakes

- Forgetting to fill in `alt`
- Setting both `width` and `height` at the same time with the wrong ratio, making the image look squished
- Assuming `<img>` needs a closing tag like `</img>`

## Try it yourself

Add an `<img>` to your profile page using any image from the internet (put its URL in `src`), complete with an `alt` description explaining the image.
