---
id: web-foundation-lanjutan-20
courseId: fe-web-foundation-00
order: 43
title: "Other Table Elements and Attributes"
readingMinutes: 5
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Add a table title with <caption> and structure tables with <thead>, <tbody>, and <tfoot>"
  - "Improve table accessibility using the scope attribute on <th> elements"
codeSnippet: |
  <table border="1">
    <caption>Quick Data</caption>
    <thead>
      <tr>
        <th scope="col">Category</th>
        <th scope="col">Description</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <th scope="row">Hometown</th>
        <td>Semarang</td>
      </tr>
      <tr>
        <th scope="row">Start Learning</th>
        <td>2026</td>
      </tr>
    </tbody>
  </table>
---
Besides `table`, `tr`, `td`, and `th`, there are several other table elements and attributes that are less commonly used but good to know.

## Additional Elements

- **caption**: a title for the entire table, placed right after the opening `<table>` tag
- **thead**: groups the header rows of a table
- **tbody**: groups the body/data rows of a table
- **tfoot**: groups the footer rows of a table (e.g., total rows)
- **col** and **colgroup**: defines a single column or a group of columns, typically used to style specific columns via CSS

```html
<table border="1">
  <caption>Weekly Study Schedule</caption>
  <thead>
    <tr>
      <th>Day</th>
      <th>Topic</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Monday</td>
      <td>HTML</td>
    </tr>
    <tr>
      <td>Wednesday</td>
      <td>CSS</td>
    </tr>
  </tbody>
</table>
```

## Additional Attributes for td/th

- **headers**: associates a data cell with the `id` of a specific header cell, useful for accessibility in complex tables
- **scope**: on a `<th>`, specifies whether the header applies to a `row`, `col` (column), `rowgroup`, or `colgroup`

```html
<table border="1">
  <tr>
    <th scope="col">Name</th>
    <th scope="col">Hobby</th>
  </tr>
  <tr>
    <th scope="row">Rani</th>
    <td>Drawing</td>
  </tr>
</table>
```

These elements and attributes are not mandatory for simple tables, but they are extremely useful for large and complex tables, especially for accessibility.

## Common Mistakes

- Placing `<caption>` anywhere other than directly after the opening `<table>` tag
- Using `thead`/`tbody`/`tfoot` in the wrong order (the correct order is: `thead`, then `tbody`, and finally `tfoot`)

## Try It Yourself

Add a `<caption>` to the table you created in previous modules.
