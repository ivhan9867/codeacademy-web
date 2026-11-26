---
id: js-lanjutan-10
courseId: fe-js-03
order: 30
title: "Handling HTML Forms, preventDefault(), & Input Validation"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - Understand why the browser reloads the page when a form is submitted and how to stop it with event.preventDefault()
  - Capture the submit event on a <form> element using addEventListener()
  - Validate text input with trim() and display error or success messages cleanly in the DOM
codeLanguage: javascript
codeSnippet: |
  const form = document.querySelector("#form-daftar");
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    console.log("Form diproses tanpa reload halaman!");
  });
starterHtml: |
  <div style="font-family: sans-serif; max-width: 380px; margin: 0 auto; padding: 18px; border: 1px solid #E2E8F0; border-radius: 10px; background: #FFFFFF;">
    <h3 style="margin-top: 0; color: #0F172A;">Form Pendaftaran Kelas</h3>
    <form id="form-kelas">
      <label for="input-nama" style="display: block; font-size: 13px; font-weight: bold; color: #334155; margin-bottom: 4px;">Nama Lengkap</label>
      <input id="input-nama" type="text" placeholder="Minimal 3 karakter..." style="width: 100%; box-sizing: border-box; padding: 8px 10px; border: 1px solid #CBD5E1; border-radius: 6px; margin-bottom: 10px;" />

      <label for="input-email" style="display: block; font-size: 13px; font-weight: bold; color: #334155; margin-bottom: 4px;">Email</label>
      <input id="input-email" type="text" placeholder="contoh@email.com" style="width: 100%; box-sizing: border-box; padding: 8px 10px; border: 1px solid #CBD5E1; border-radius: 6px; margin-bottom: 12px;" />

      <button type="submit" style="width: 100%; background: #0D9488; color: #FFFFFF; border: none; padding: 10px; border-radius: 6px; font-weight: bold; cursor: pointer;">
        Daftar Sekarang
      </button>
    </form>
    <p id="pesan-feedback" style="margin: 12px 0 0 0; font-size: 13px; font-weight: 600;"></p>
  </div>
starterJs: |
  const form = document.querySelector("#form-kelas");
  const inputNama = document.querySelector("#input-nama");
  const inputEmail = document.querySelector("#input-email");
  const pesanFeedback = document.querySelector("#pesan-feedback");

  if (form && inputNama && inputEmail && pesanFeedback) {
    form.addEventListener("submit", (event) => {
      // Mencegah browser me-reload halaman saat form dikirim
      event.preventDefault();

      const nama = inputNama.value.trim();
      const email = inputEmail.value.trim();

      // Validasi input kosong atau terlalu pendek
      if (nama.length < 3) {
        pesanFeedback.textContent = "Error: Nama minimal harus 3 karakter.";
        pesanFeedback.style.color = "#DC2626";
        console.log("Validasi gagal: nama terlalu pendek");
        return;
      }

      // Validasi format email sederhana
      if (!email.includes("@") || !email.includes(".")) {
        pesanFeedback.textContent = "Error: Masukkan alamat email yang valid.";
        pesanFeedback.style.color = "#DC2626";
        console.log("Validasi gagal: email tidak valid");
        return;
      }

      // Jika semua lolos validasi
      pesanFeedback.textContent = `Pendaftaran berhasil untuk ${nama}!`;
      pesanFeedback.style.color = "#0D9488";
      console.log(`Form valid! Terdaftar: ${nama} (${email})`);
      form.reset();
    });
  }
playgroundTask: |
  Press 'Run'. In the preview window:
  1. Immediately click 'Daftar Sekarang' while the fields are empty and observe the red error message that appears.
  2. Fill in Nama (at least 3 letters) and an Email containing '@' and '.', then click 'Daftar Sekarang' again.
  3. Verify the teal success message and the Console log: 'Form valid! Terdaftar: ...'
verificationRules:
  - "js:preventDefault"
  - "js:addEventListener"
---

# Handling HTML Forms, preventDefault(), & Input Validation

Almost every web application—from login boxes and search bars to registration forms—uses the `<form>` tag. However, when beginners try processing a form with JavaScript, a strange thing often happens: **the page suddenly flashes, reloads, and all data in the Console vanishes!**

Why does that happen, and how can we control it cleanly?

---

## 1. Default Form Behavior & the Role of `event.preventDefault()`

By default since the early days of the web, when a user clicks a `type="submit"` button or presses `Enter` inside a `<form>`, the browser automatically sends the data to a server URL and **reloads (*refreshes*) the page**.

To take control using JavaScript (for client-side validation or sending data via an API without a *reload*), we must call **`event.preventDefault()`** inside the `"submit"` event listener:

```text
  User clicks Submit / presses Enter
                 |
                 v
     "submit" event fires on <form>
                 |
                 +-- Without preventDefault() --> Page reloads (JS state lost!)
                 |
                 +-- With event.preventDefault() --> Reload cancelled!
                                                     JavaScript can validate input
                                                     & show feedback in the DOM
```

> **Important Tip:** Always attach `addEventListener("submit", ...)` to the **`<form>`** element, not a `click` listener on its button. That way, users who press the **Enter** key on their keyboard are automatically handled too!

---

## 2. Reading Input Values with `.trim()`

Users often accidentally type only blank spaces (for example `"   "`). If we only check `if (name === "")`, those spaces would pass as valid!

Always strip leading and trailing spaces with **`.trim()`** before validating:

```javascript
const name = nameInput.value.trim();

if (name === "") {
  console.log("Name cannot be empty!");
}
```

---

## 3. Clean Validation Pattern with *Early Return*

To keep validation code from filling up with deeply nested `if/else` blocks, use the **early return** pattern (check the failure condition first, show the error message, and immediately `return`):

```javascript
form.addEventListener("submit", (event) => {
  event.preventDefault();

  const name = nameInput.value.trim();
  const email = emailInput.value.trim();

  if (name.length < 3) {
    showError("Name must be at least 3 characters.");
    return; // Stop here, do not continue below
  }

  if (!email.includes("@")) {
    showError("Invalid email format.");
    return;
  }

  // Code below is guaranteed to run only when all inputs are valid
  showSuccess(`Hello, ${name}!`);
  form.reset(); // Clears all inputs inside the form
});
```

### Advantages of This Pattern
1. **One focus per block**: Each validation rule is clearly separated and easy to extend.
2. **User-friendly feedback**: Instead of using an intrusive `alert()`, we write messages directly into a `<p>` element on the page—red for errors and green/teal for success.
3. **`form.reset()`**: With one call, every input box is cleared cleanly after the data is processed.

Try practicing directly with the interactive form in the playground editor below!
