---
id: web-foundation-lanjutan-23
courseId: fe-web-foundation-00
order: 46
title: "Attributes on the Input Element"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Configure input identity with the name and id attributes for data processing"
  - "Apply basic client-side validation using the required, placeholder, min/max, and pattern attributes"
codeSnippet: |
  <div>
    <label for="nama">Nama</label>
    <br>
    <input type="text" id="nama" name="nama" placeholder="Nama lengkap" required>
  </div>
  
  <div>
    <label for="email">Email</label>
    <br>
    <input type="email" id="email" name="email" placeholder="nama@email.com" required>
  </div>
---
The `<input>` element supports a wide variety of helper attributes. These attributes guide users while validating form data at the same time. To keep things clear, let's learn these attributes grouped by their function.

## 1. Identity & Data Submission

This group gives a name and data value to an input field:

- **`name`**: the data identifier sent to the server. Without `name`, the input's data **will not be sent**.
- **`value`**: the initial (*default*) value pre-filled inside the field.
- **`id`**: the unique identifier of the element on the HTML page, useful for linking an input with a `<label>`.

```html
<label for="username">Username:</label>
<input type="text" id="username" name="username" value="budi_santoso">
```

## 2. User Input Guidance

This group helps users understand what data is expected:

- **`placeholder`**: faint hint text inside an empty field. This text disappears automatically when the user starts typing.
- **`autocomplete`**: permission for the browser to auto-fill the field based on the user's typing history.

```html
<label for="userEmail">Email Address:</label>
<input type="email" id="userEmail" name="email" placeholder="name@domain.com" autocomplete="email">
```

## 3. Text Length & Number Range Limits

This group restricts the number of characters or numeric values allowed:

- **`minlength`** and **`maxlength`**: minimum and maximum character count for text.
- **`min`** and **`max`**: lowest and highest allowed values for numbers or dates.
- **`step`**: allowed numeric step interval (for example, multiples of 5).

```html
<!-- Restrict postal code to exactly 5 digits -->
<input type="text" name="postalcode" minlength="5" maxlength="5" placeholder="12345">

<!-- Restrict age between 17 and 60 years -->
<input type="number" name="age" min="17" max="60" value="20">
```

## 4. Field Status & Access Control

This group controls whether a field is required, editable, or disabled:

- **`required`**: marks a field as mandatory. The form cannot be submitted if this field is empty.
- **`readonly`**: text can only be read and highlighted, not modified. Its data is **still submitted**.
- **`disabled`**: disables the field completely and greys it out. Its data is **not submitted**.
- **`checked`**: makes an option box (*checkbox* or *radio*) checked by default from the start.

```html
<!-- Mandatory field -->
<input type="password" name="password" required>

<!-- Account data is still sent even though the user cannot edit it -->
<input type="text" name="role" value="Member" readonly>

<!-- Agreement checkbox is active by default -->
<input type="checkbox" name="agree" checked> Agree to the rules
```

## 5. File Selection & Custom Pattern Validation

This group sets specialized data criteria:

- **`accept`**: restricts which file formats can be selected on `type="file"`, such as images or PDFs.
- **`multiple`**: allows users to pick more than one file or enter multiple email addresses.
- **`pattern`**: a regular expression (*regex*) rule that must match for the data to be considered valid.

```html
<!-- Accepts PNG or JPG image files only -->
<input type="file" name="photo" accept=".png, .jpg, .jpeg" multiple>

<!-- Must start with the letters ID followed by 4 digits -->
<input type="text" name="memberId" pattern="ID[0-9]{4}" placeholder="ID1234">
```

## Automatic Browser Validation

Modern browsers check these attributes automatically. If a `required` field is empty or an email format is invalid, the browser immediately displays a warning message without needing any extra JavaScript code.

## Common Mistakes

1. **Forgetting to add the `name` attribute**: As a result, user input never reaches the server.
2. **Mixing up `disabled` and `readonly`**: Use `readonly` when you still want the data sent to the server. Use `disabled` when the data should genuinely be ignored.

## Try It Yourself

Check the form you created earlier. Add the `required` and `placeholder` attributes to the "Nama" and "Email" inputs. Make sure both inputs also have appropriate `name` attributes.
