---
id: web-foundation-pcss-45
courseId: fe-web-foundation-00
order: 101
title: "The Viewport Meta Tag for Layout Responsiveness"
readingMinutes: 6
section: PENDALAMAN_CSS
learningObjectives:
  - "Understand what a viewport is and why its size differs across devices"
  - "Add the viewport meta tag so pages do not automatically shrink on mobile devices"
  - "Know how to use the device simulation feature in browser DevTools to test mobile views"
---
The `float` layout we just built looks neat — but it has only been tested on a laptop or desktop screen. How does it look on a phone?

## Testing mobile views with DevTools

Modern browsers like Google Chrome and Mozilla Firefox have an **inspector** feature that can simulate displays at different screen sizes, including mobile devices. To open it, press `Ctrl + Shift + I` (or right-click and choose "Inspect"), then toggle device simulation mode (usually a small phone/tablet icon in the corner of the inspector). From there you can pick a device like "iPhone 12 Pro" to view the website as if opened on that phone.

If you try it right now, the result may be disappointing: the entire page appears **tiny**, as if zoomed far out.

## What is a viewport?

The **viewport** is the area visible to the user on their device's screen. The viewport size is much smaller on a phone than on a desktop. Back before phones and tablets were commonly used for browsing, most websites were designed with static sizes suited only for desktop monitors. Mobile browsers handled this by "shrinking" the entire page to fit the small screen — as a result, text became too tiny to read.

## Configuring the viewport with a meta tag

The solution is to tell the browser how the viewport should be configured using a `<meta>` tag inside `<head>`:

```html
<meta name="viewport" content="width=device-width, initial-scale=1" />
```

This tag tells the browser:

- `width=device-width`: the viewport width follows the device's actual screen width rather than a much larger default width.
- `initial-scale=1`: the initial zoom level is set to 1 (not zoomed out at all).

Once this tag is added, the page appears at a size appropriate for the device screen — no longer shrinking automatically as before.

However, a new problem appears: now `<article>` feels too narrow, and `<aside>` is poorly arranged on small screens. That makes sense, because the two-column layout we built was designed for wide screens. To solve this, we need further **responsiveness** techniques, which we will cover in the next module: CSS Media Queries.
