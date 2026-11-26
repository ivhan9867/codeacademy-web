---
id: web-foundation-pcss-15
courseId: fe-web-foundation-00
order: 71
title: "Colors"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Understand the concept of color values in CSS"
  - "Recognize three ways to write color values: named colors, hexadecimal (hex) codes, and the rgb() function"
  - "Connect color values to how they are applied in CSS properties"
---
## Introduction to Colors in CSS

Colors make a web page feel alive, comfortable to read, and visually distinctive. Imagine websites from the early 1990s, when almost every page had a dull gray background with plain black text. As modern browsers evolved, CSS introduced a rich coloring system that is fully supported across all digital devices.

Before learning which properties can accept colors, you first need to understand what a **color value** is in CSS and how to write one.

## What Is a Color Value in CSS?

Computer monitors and smartphone screens display colors by emitting a combination of three primary light colors: **Red**, **Green**, and **Blue**—abbreviated as **RGB**. When you write a color value in CSS, you are actually telling the browser how intensely to mix those three lights when drawing an element on screen.

CSS provides three main ways that are most commonly used to write color values:

### 1. Named Colors

CSS includes 140+ standard English color names that you can use right away without memorizing number codes. Examples include `red`, `blue`, `forestgreen`, `gold`, and `tomato`.

```css
h1 {
  color: tomato;
}
```

This method is very readable and quick to write during early prototyping, though your choices are limited to the predefined list of names.

### 2. Hexadecimal Codes (*Hex Color*)

A hexadecimal code starts with a hash sign (`#`) followed by a 6-character combination (digits `0-9` and letters `a-f`). The first two characters represent red intensity, the middle two represent green, and the last two represent blue (`#RRGGBB`).

```css
h1 {
  color: #ff6347; /* Same as tomato */
}
```

The hex format is the most popular choice in web design because it is compact and supports over 16 million unique color combinations.

### 3. The `rgb()` Function

With the `rgb()` function, you write the intensities of red, green, and blue directly as decimal numbers from `0` (dark/off) up to `255` (brightest/maximum).

```css
h1 {
  color: rgb(255, 99, 71); /* Same as tomato */
}
```

This format makes it easier to understand the exact proportion of each color component.

## Moving to the Next Lessons

All three formats above produce the exact same color on a browser screen. In the upcoming lessons, we will take a closer look at how the RGB color space works, how to add transparency effects, and how to apply colors directly to text and element backgrounds on your web page.
