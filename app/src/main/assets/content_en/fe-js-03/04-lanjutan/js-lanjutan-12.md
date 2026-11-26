---
id: js-lanjutan-12
courseId: fe-js-03
order: 32
title: "Fetching API Data with fetch() & Error Handling"
readingMinutes: 8
section: LANJUTAN
learningObjectives:
  - Understand the HTTP request and server response flow using the Fetch API
  - Combine async/await with fetch() and response.json() in sequence
  - Handle both network failures and HTTP error statuses (404/500) using response.ok checks and try/catch blocks
codeLanguage: javascript
codeSnippet: |
  const ambilPengguna = async () => {
    try {
      const res = await fetch("https://jsonplaceholder.typicode.com/users/1");
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const data = await res.json();
      console.log(data.name);
    } catch (err) {
      console.log("Terjadi kesalahan:", err.message);
    }
  };
starterJs: |
  const ambilDataPengguna = async (idPengguna) => {
    const url = `https://jsonplaceholder.typicode.com/users/${idPengguna}`;
    console.log("1. Mengirim request ke:", url);

    try {
      // Tahap 1: Tunggu jawaban HTTP dari server
      const response = await fetch(url);

      // Tahap 2: Pastikan status HTTP sukses (200-299)
      if (!response.ok) {
        throw new Error(`Server merespons dengan status ${response.status}`);
      }

      // Tahap 3: Ubah body respons menjadi objek JavaScript (juga asinkron!)
      const pengguna = await response.json();

      console.log("2. Data berhasil diterima!");
      console.log("Nama  :", pengguna.name);
      console.log("Email :", pengguna.email);
      console.log("Kota  :", pengguna.address.city);
    } catch (error) {
      // Menangkap error jaringan (offline) maupun Error dari throw di atas
      console.log("Gagal memuat data API:", error.message);
    }
  };

  ambilDataPengguna(1);
playgroundTask: |
  Press 'Run' and wait a moment while watching the Console panel. Make sure the data fetching steps run cleanly (or that an error message is caught cleanly by catch if your device is offline):
  1. Mengirim request ke: https://jsonplaceholder.typicode.com/users/1
  2. Data berhasil diterima!
  Try changing the ambilDataPengguna(1) argument to ambilDataPengguna(9999) to test how response.ok handles an HTTP 404 status!
verificationRules:
  - "js:async"
  - "js:try"
---

# Fetching API Data with fetch() & Error Handling

Modern websites almost always display live, dynamic data—the latest product catalog, today's weather forecast, or a newly updated account profile. That data is not handwritten inside HTML files; instead, it is fetched from a server through an **API (*Application Programming Interface*)** using the browser's built-in function: **`fetch()`**.

---

## Request & Response Workflow of `fetch()`

```text
  [ JavaScript Code ]
           |
           |  1. await fetch(url)
           v
  +------------------------+
  |     Server / API       |
  +------------------------+
           |
           |  2. Returns a Response Object (headers & status)
           v
     Check: response.ok ?
       |            |
     false         true
       |            |
       v            v
   throw Error   3. await response.json()  <-- Parse JSON body
       |            |
       v            v
  [ catch Block ] [ JS Data Ready to Display in the DOM ]
```

Notice that there are **two `await` steps** when fetching JSON data:
1. **`await fetch(url)`**: Waits for the server to reply to the connection (*status code* & *headers*).
2. **`await response.json()`**: Waits for the entire response body to finish downloading and converts it from JSON text into a JavaScript object.

---

## Classic `fetch()` Trap: A 404 Status Does Not Automatically Go to `catch`!

Many beginners assume that if an API address is wrong (`404 Not Found`) or the server crashes (`500 Internal Server Error`), the `catch` block will run right away. **Actually, it won't!**

`fetch()` only jumps to `catch` if the internet connection drops completely or the domain cannot be reached. If the server still responds—even if the reply is `"404 Not Found"`—`fetch()` considers the delivery itself successful.

Therefore, **always check the `response.ok` property** manually:

```javascript
const response = await fetch(url);

// response.ok is true only when the HTTP status is in the 200 - 299 range
if (!response.ok) {
  throw new Error(`Request failed with HTTP code ${response.status}`);
}
```

When `throw new Error(...)` runs, program flow jumps straight to the `catch (error)` block below!

---

## Complete `async/await` + `fetch()` + `try/catch` Pattern

Here is a clean, resilient industry-standard pattern:

```javascript
const loadPost = async () => {
  try {
    const response = await fetch("https://jsonplaceholder.typicode.com/posts/1");

    if (!response.ok) {
      throw new Error(`HTTP Error: ${response.status}`);
    }

    const post = await response.json();
    console.log("Title:", post.title);
  } catch (error) {
    console.log("Could not load post:", error.message);
  }
};

loadPost();
```

### Why Is This Pattern Important?
- **Never freezes the UI**: Thanks to `async/await`, the web page stays responsive while waiting for the server reply.
- **Crash-proof**: Both network failures and 404/500 errors are handled in one place (`catch`).
- **Easy to pair with UI**: Inside `try`, you can render data to the DOM; inside `catch`, you can show a friendly message like *"Connection issue, please try again"*.

Run the playground below, observe the output in the Console, and then test for yourself how `response.ok` catches the error when the user ID is changed to a nonexistent number!
