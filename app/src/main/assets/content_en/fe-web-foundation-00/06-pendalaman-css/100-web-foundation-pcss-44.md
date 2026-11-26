---
id: web-foundation-pcss-44
courseId: fe-web-foundation-00
order: 100
title: "Exercise: Applying Float to Content and Aside Elements"
readingMinutes: 12
section: PENDALAMAN_CSS
learningObjectives:
  - "Arrange #content and aside side by side horizontally using float"
  - "Solve the collapse issue on main using overflow: auto"
  - "Understand why padding on floated elements can break total width, and how to fix it with box-sizing: border-box"
---
We have already learned the concepts behind layouting with `float`. Now it is time to apply them to our case study: placing `<div id="content">` and `<aside>` **side by side horizontally**.

## Step 1: Float on content

Open `style.css`, then add `float: left` to the `#content` selector:

```css
#content {
  float: left;
}
```

Also add its width, 75%:

```css
#content {
  float: left;
  width: 75%;
}
```

If you run it right now, the layout will actually look **broken**. That is normal — because `<aside>` does not have any `float` yet, that element (and `<footer>` below it) shifts upward to occupy the space left behind by `#content`.

## Step 2: Float on aside

Add `float: right` to `<aside>`, with a width of 25% (the remainder of the 75% already used by `#content`):

```css
#content {
  float: left;
  width: 75%;
}

aside {
  float: right;
  width: 25%;
}
```

Now the layout looks **even messier** than before: `<main>` shrinks to 0 pixels tall, causing `<footer>` to rise up and overlap the content above it.

## Step 3: Fixing collapse with overflow: auto

This is the *collapse* issue we discussed in the previous module: `<main>` no longer recognizes `#content` and `<aside>` as part of its height because both are floated. To fix it, add `overflow: auto` to `<main>`:

```css
main {
  padding: 20px;
  overflow: auto;
}
```

Now the layout looks much better, and `<footer>` returns to its proper position.

## Step 4: Adding spacing between columns

So that `#content` and `<aside>` do not press right against each other, add `padding-left` to `<aside>`:

```css
aside {
  float: right;
  width: 25%;
  padding-left: 20px;
}
```

However, this change actually pushes `<aside>` down below `#content`! This happens because adding `padding-left: 20px` makes the **total width** of `<aside>` exceed 25% (content width + padding), so it no longer fits in the remaining available space.

## Step 5: Fixing it with box-sizing: border-box

Remember the `box-sizing` lesson from the Box Dimensions module? Now is the time to put it to work. With `box-sizing: border-box`, `padding` and `border` are calculated **inside** the specified width instead of adding on top of it.

Apply it to every element using the universal selector:

```css
* {
  box-sizing: border-box;
}

body {
  font-family: 'Quicksand', sans-serif;
  margin: 0;
  padding: 0;
}
```

After this, the width of `<aside>` stays at an exact 25% even with `padding-left`, and the layout snaps cleanly into place with `#content` and `<aside>` aligned side by side.

This exercise is challenging — we ran into three issues in a row (collapse, jumbled alignment, and overflowing width). Yet that is precisely how we learn how the box model, float, and box-sizing interact in real-world practice.

## Quick exercise

1. Apply `float: left` on `#content` (75%) and `float: right` on `aside` (25%), then observe the collapse issue on `main`.
2. Add `overflow: auto` on `main`, then observe the fix.
3. Add `padding-left: 20px` on `aside`, then observe `aside` dropping below.
4. Add `* { box-sizing: border-box; }` and observe the layout snap back cleanly.
