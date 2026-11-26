---
id: js-lanjutan-7
courseId: fe-js-03
order: 27
title: "Responding to User Actions: Event Listeners & Interactive Buttons"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - Understand the concept of an Event as an interactive action performed by the user
  - Master using the .addEventListener() method to respond to click actions
  - Build your first interactive component: a number counter that increases every time a button is clicked
codeLanguage: javascript
codeSnippet: |
  const btn = document.querySelector("#btn");
  btn.addEventListener("click", () => {
    console.log("Tombol ditekan!");
  });
starterHtml: |
  <div style="font-family: sans-serif; text-align: center; padding: 24px; border: 1px solid #E2E8F0; border-radius: 8px;">
    <h3 style="color: #0F172A; margin: 0 0 8px 0;">Penghitung Klik Interaktif</h3>
    <p style="font-size: 36px; font-weight: bold; color: #0D9488; margin: 12px 0;" id="angka-tampilan">0</p>
    <button id="tombol-tambah" style="background-color: #0D9488; color: white; border: none; padding: 10px 24px; border-radius: 6px; font-size: 15px; font-weight: bold; cursor: pointer;">
      Tambah +1
    </button>
  </div>
starterJs: |
  let jumlahKlik = 0;
  const tombol = document.querySelector("#tombol-tambah");
  const tampilanAngka = document.querySelector("#angka-tampilan");

  if (tombol && tampilanAngka) {
    // Memasang telinga pendengar peristiwa 'click'
    tombol.addEventListener("click", () => {
      jumlahKlik = jumlahKlik + 1;
      tampilanAngka.textContent = jumlahKlik;
      console.log("Tombol diklik! Total klik:", jumlahKlik);
    });
  }
playgroundTask: |
  Press 'Run'. In the preview window, click the 'Tambah +1' button 3 times. Watch the number on the screen increase to 3, then check the Console panel to verify the message:
  Tombol diklik! Total klik: 3
---

# Responding to User Actions: Event Listeners & Interactive Buttons

A website truly comes alive when it can respond to actions performed by its users:
- A user clicks the *"Send"* button.
- A user types a search keyword.
- A user moves the cursor over a photo (*hover*).

In the browser world, every such user interaction is called an **Event**.

```text
[ User Clicks Button ] ---> [ Browser Triggers "click" Event ]
                                         |
                                         v
                            [ Callback Function Runs ]
                                         |
                                         v
                             [ DOM Display Updated ]
```
---

## Attaching a "Listening Ear": addEventListener

So JavaScript knows when an event happens on a specific HTML element, we attach a watcher function called an **Event Listener**:

```javascript
elemenTarget.addEventListener("namaEvent", fungsiTindakan);
```

### Two Main Parameters:
1. **Event Name**: The type of action to wait for, for example:
   - `"click"`: When an element is clicked with a mouse or tapped on a phone screen.
   - `"input"`: When the user is typing inside a text box.
   - `"submit"`: When a form is submitted.
2. **Action Function (*Handler / Callback*)**: The code to run when that event occurs. Arrow functions are very convenient to use here!

---

## Case Study: Click Counter Button

Look at the practice code below:
1. We have a variable storing the data `let jumlahKlik = 0;`.
2. We select the button and number text using `document.querySelector()`.
3. We attach a `"click"` event listener to the button.
4. Every time the button is clicked:
   - `jumlahKlik` increases by 1.
   - The number text on the screen (`tampilanAngka.textContent`) is updated immediately.
   - We also log the event to the Console.

Try running it and clicking the button in the preview window right now! You have just built your first interactive web component!
