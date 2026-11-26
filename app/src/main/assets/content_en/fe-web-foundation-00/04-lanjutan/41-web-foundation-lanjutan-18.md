---
id: web-foundation-lanjutan-18
courseId: fe-web-foundation-00
order: 41
title: "Tables: Introduction and Basic Structure"
readingMinutes: 6
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Build two-dimensional data tables using the <table>, <tr>, <th>, and <td> elements"
  - "Distinguish between header cells (<th>) and regular data cells (<td>)"
codeSnippet: |
  <table>
    <tr>
      <th>Bulan</th>
      <th>Target Belajar</th>
    </tr>
    <tr>
      <td>Januari</td>
      <td>Dasar HTML</td>
    </tr>
    <tr>
      <td>Februari</td>
      <td>Dasar CSS</td>
    </tr>
  </table>
---
Some information is much easier to understand when displayed in rows and columns: match standings, schedules, or price lists. For that, HTML provides table elements.

## What is a table in HTML

The `<table>` element represents **tabular data**: information presented in two dimensions, rows and columns, where each row-column intersection is called a **cell**.

## Three basic elements of a table

- **`<table>`**: the wrapper for the entire table
- **`<tr>`** (*table row*): a single row in the table
- **`<td>`** (*table data*) or **`<th>`** (*table header*): a single cell within that row

`<td>` is used for regular data cells, while `<th>` is used for header cells that act as column or row titles. By default, the content of `<th>` is displayed bold and center-aligned.

```html
<table>
  <tr>
    <th>Name</th>
    <th>Hobby</th>
  </tr>
  <tr>
    <td>Rani</td>
    <td>Drawing</td>
  </tr>
  <tr>
    <td>Budi</td>
    <td>Playing Guitar</td>
  </tr>
</table>
```

Each new `<tr>` means a new row, and the number of `<td>`/`<th>` elements inside it determines the number of columns in that row. A cell's content can be anything: text, images, or even another table inside it.

## Common mistakes

- Placing `<td>` or `<th>` directly inside `<table>` without wrapping them in `<tr>`
- An inconsistent number of cells across rows, making the table look misaligned
- Using `<th>` for every cell, even though only title rows/columns should use `<th>`

## Try it yourself

Create a small table with two columns (for example, "Month" and "Learning Goal") and three rows of data.
