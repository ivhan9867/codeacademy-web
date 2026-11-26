---
id: css-layout-pend-3
courseId: fe-css-02
order: 8
title: "The Exhausting Past: Traps of CSS Float & Positioning"
readingMinutes: 5
section: PENDAHULUAN
learningObjectives:
  - "Understand the original purpose of the float property and why using it for column layouts is very fragile"
  - "Recognize the container collapsing phenomenon and the clearfix incantation of the past"
  - "Know the limitations of positioning properties when used for overall page layouts"
codeLanguage: css
codeSnippet: |
  .kolom-kiri {
    float: left;
    width: 60%;
    background-color: #389b9e;
  }

  .kolom-kanan {
    float: right;
    width: 35%;
    background-color: #df7b56;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Eksperimen Float Klasik</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="pembungkus">
        <div class="kolom-kiri">Konten Utama (Float Left)</div>
        <div class="kolom-kanan">Sidebar (Float Right)</div>
      </div>
      <div class="footer">Footer Bawah Halaman</div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 20px;
    background-color: #faf7f2;
  }

  .pembungkus {
    border: 3px solid #104246;
    background-color: #e2f0f1;
    padding: 10px;
  }

  .kolom-kiri {
    float: left;
    width: 60%;
    background-color: #389b9e;
    color: white;
    padding: 20px;
    box-sizing: border-box;
  }

  .kolom-kanan {
    float: right;
    width: 35%;
    background-color: #df7b56;
    color: white;
    padding: 20px;
    box-sizing: border-box;
  }

  .footer {
    background-color: #24211d;
    color: white;
    padding: 15px;
    margin-top: 15px;
    text-align: center;
  }
playgroundTask: "Look closely at the Live Preview panel: the wrapper box (.pembungkus) looks flattened or collapsed in height because its children are floated! Now add the property 'overflow: auto;' inside the '.pembungkus' selector, then run it again. Observe how the wrapper container hugs its children once more."
---
Before we feel grateful for the convenience of Flexbox and Grid, it is important to understand the "collective trauma" once experienced by web developers all over the world: **building columns with `float`**.

```text
Before Clearfix (Parent Collapsing):      After Flexbox / Clearfix:
+-------- .parent (height: 0px) --------+ +-------- .parent (auto height) ------+
  [float: left]  [float: left]            |  [ Item 1 ]   [ Item 2 ]            |
                                          +-------------------------------------+
```
## What Was the Original Purpose of `float`?

The `float` property (with values `left` or `right`) was created to mimic newspaper layouts:
> You place a photo on the left side of an article, and then paragraph text flows wrapping around the right and bottom of that photo.

That was its only purpose.

However, because in the past CSS had no other way to place two boxes side by side, web developers were forced to float (`float: left`) all page columns.

## Disaster 1: Container Collapsing

When an element is floated, that element is **lifted out of the normal document flow**.

As a result, its parent element (`.pembungkus`) feels as if it has no children inside it at all. The parent container's height instantly becomes 0 pixels (*collapse*), its background vanishes, and elements below it (such as `.footer`) jump upward and crash into the floating columns!

To fix this, developers had to inject a strange snippet of code called the **clearfix hack**:

```css
/* Tedious ancient trick */
.pembungkus::after {
  content: "";
  display: table;
  clear: both;
}
```

## Disaster 2: Difficulty Matching Column Heights

If the content column has long text while the sidebar column only has one paragraph, the sidebar column stays short and leaves an empty hole underneath it. Making both columns the same height using float was nearly impossible without JavaScript tricks or excessive padding-margin hacks.

## What About `position: absolute`?

The `position: absolute` property can indeed place an element anywhere using `top`, `left`, `right`, and `bottom` coordinates. However, an absolute element **does not affect the placement of any other element at all**. If the content inside it grows longer, an absolute element will ruthlessly collide with and overlap the text underneath it.

That is why the web industry agreed: **we need an official layout system that is smart, flexible, and does not break the document flow**.