---
id: web-foundation-lanjutan-19
courseId: fe-web-foundation-00
order: 42
title: "Spanning Cells: Colspan and Rowspan"
readingMinutes: 5
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Merge multiple table columns using the colspan attribute"
  - "Merge multiple table rows using the rowspan attribute"
codeSnippet: |
  <table border="1">
    <tr>
      <th colspan="2">Rencana Belajar</th>
    </tr>
    <tr>
      <th rowspan="2">Bulan Ini</th>
      <td>Menuntaskan dasar HTML</td>
    </tr>
    <tr>
      <td>Berlatih di Playground setiap hari</td>
    </tr>
  </table>
---
Sometimes a single cell needs to span across more than one column or row at once, similar to the "merge cell" feature in Excel. In HTML, this is called a **spanning cell**.

## Colspan: spanning across columns

The `colspan` attribute on `<td>` or `<th>` makes that cell span across multiple columns at once.

```html
<table border="1">
  <tr>
    <th colspan="2">Personal Data</th>
  </tr>
  <tr>
    <td>Name</td>
    <td>Rani</td>
  </tr>
</table>
```

Above, the "Personal Data" header spans the width of the two columns below it.

## Rowspan: spanning across rows

The `rowspan` attribute works similarly, but spans downward across multiple rows.

```html
<table border="1">
  <tr>
    <th rowspan="2">Hobbies</th>
    <td>Drawing</td>
  </tr>
  <tr>
    <td>Playing Guitar</td>
  </tr>
</table>
```

The `<th>Hobbies</th>` cell above spans two rows downward, so the second row does not need to write it again — just one `<td>` for the extra item.

## Common mistakes

- Forgetting to reduce the number of `<td>`/`<th>` cells in the following row after using `rowspan`, causing the table to have an unwanted extra column
- Using `colspan`/`rowspan` values that do not match the actual number of columns/rows, making the table skewed or messy

## Try it yourself

Add the `border="1"` attribute to your table so the cell lines are visible, then try creating a header that spans two columns using `colspan`.
