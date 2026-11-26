---
id: web-foundation-pcss-31
courseId: fe-web-foundation-00
order: 87
title: "Exercise: Applying Card Style to Article Elements"
readingMinutes: 8
section: PENDALAMAN_CSS
learningObjectives:
  - "Create a card effect using box-shadow and border-radius"
  - "Organize spacing inside cards with padding and margin"
---
Now it is time to make the appearance more appealing: a **card** effect on every article section, complete with a soft shadow and rounded corners.

## Adding the card effect

Create a new rule with the `.card` selector:

```css
.card {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 5px;
}
```

This selector does not apply anywhere yet. Add the `card` class to every `<article>` element, including `<article class="profile">` inside `<aside>`:

```html
<div id="content">
  <article id="sejarah" class="card">
    <h2>Sejarah</h2>
    <img src="assets/image/history.jpg" alt="sejarah" />
  </article>

  <article id="geografis" class="card">
    <h2>Geografis</h2>
    <img src="assets/image/geografis.jpg" alt="geografis" />
  </article>

  <article id="wisata" class="card">
    <h2>Wisata</h2>
    <p>Sejak dibukanya Jalan Tol Cipularang, kota Bandung telah menjadi tujuan utama dalam menikmati liburan akhir pekan.</p>
  </article>
</div>

<aside>
  <article class="profile card">
    <header>
      <h2>Kota Bandung</h2>
      <p>Kota Kembang Paris van Java</p>
      <figure>
        <img src="assets/image/bandung-badge.png" />
        <figcaption>Lambang</figcaption>
      </figure>
    </header>
    <section>
      <!-- ... -->
    </section>
  </article>
</aside>
```

Open the page in your browser. Every `<article>` will now be surrounded by a soft shadow with rounded corners. If the shadow feels too subtle, you can adjust its values yourself. To experiment more easily, you can use online *Box Shadow Generator* tools to preview results visually before copying the values into your code.

## Giving card content breathing room

The content inside the cards still feels cramped because it touches the edges. Add `padding` and `margin-top` to the same `.card` rule:

```css
.card {
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
  border-radius: 5px;

  padding: 20px;
  margin-top: 20px;
}
```

Now the content inside each card has breathing room, and the spacing between cards is much clearer. If you feel this padding or margin size is too small or too large, feel free to adjust it to your taste.

## Quick exercise

1. Apply the `card` class to every `<article>`, including the profile in `<aside>`.
2. Change the `box-shadow` values so the shadow is more pronounced, such as `0 6px 16px 0 rgba(0, 0, 0, 0.3)`.
3. Experiment with larger `padding` (such as `32px`) and observe how the card feels more spacious.
