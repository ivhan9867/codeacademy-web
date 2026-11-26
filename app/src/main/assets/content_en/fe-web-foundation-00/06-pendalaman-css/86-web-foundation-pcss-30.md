---
id: web-foundation-pcss-30
courseId: fe-web-foundation-00
order: 86
title: "Exercise: Setting Image Dimensions"
readingMinutes: 9
section: PENDALAMAN_CSS
learningObjectives:
  - "Set image dimensions using width and max-height"
  - "Keep image aspect ratios proportional with object-fit and object-position"
  - "Differentiate sizing rules for article images and the logo image"
---
This exercise feels familiar, doesn't it? We are going to apply the box model properties we have learned to the image elements on the Profile Page: the images inside `<article>` (History, Geography, Tourism), and the logo image inside `<aside>`.

## Setting article image dimensions

Open `style.css`, then create a new rule for the `.featured-image` class:

```css
.featured-image {
  width: 100%;
  max-height: 300px;
}
```

This selector does not apply to any element yet because no element has the `featured-image` class. Add that class to every `<img>` inside `<article>` elements, except the logo in `<aside>`:

```html
<article id="sejarah">
  <h2>Sejarah</h2>
  <img src="assets/image/history.jpg" alt="sejarah" class="featured-image" />
  <!-- ... -->
</article>

<article id="geografis">
  <h2>Geografis</h2>
  <img src="assets/image/geografis.jpg" alt="geografis" class="featured-image" />
  <!-- ... -->
</article>

<article id="wisata">
  <h2>Wisata</h2>
  <section>
    <h3>Farm House Lembang</h3>
    <img src="assets/image/farm-house.jpg" alt="farm house" class="featured-image" />
  </section>
  <section>
    <h3>Observatorium Bosscha</h3>
    <img src="assets/image/bosscha.jpg" alt="bosscha" class="featured-image" />
  </section>
</article>
```

After running it, you might be surprised: the images fill the viewport width, but their aspect ratio looks stretched or unbalanced. This happens because we set both `width: 100%` and `max-height: 300px`, forcing the image into a box size that does not match its original aspect ratio.

## Fixing the aspect ratio with object-fit and object-position

To fix this, add the following two properties:

```css
.featured-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  object-position: center;
}
```

- `object-fit: cover` keeps the image's original aspect ratio by cropping the parts that do not fit inside the box.
- `object-position: center` determines which part of the image stays visible after cropping. The `center` value prioritizes the middle of the image.

With this combination, the image will always look proportional, even if the box dimensions do not exactly match its original aspect ratio. As a trade-off, some parts of the image may be cropped — and you can control which part gets cropped via `object-position`, such as `top`, `bottom`, `left`, or `right`.

## Setting the logo size

Finally, adjust the logo size in `<aside>` so it is not too large. Create a new rule for `.profile img`:

```css
.profile img {
  width: 200px;
}
```

With this, the logo will appear much more proportional compared to its oversized default dimensions.

## Quick exercise

1. Apply `.featured-image` to all article images, then observe the effect of `object-fit: cover`.
2. Replace `object-position: center` with `object-position: top`, then compare which part of the image is visible.
3. Style the logo in `<aside>` with `width: 150px`, then compare it with `200px`.
