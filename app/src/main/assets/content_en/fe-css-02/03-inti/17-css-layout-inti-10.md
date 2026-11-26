---
id: css-layout-inti-10
courseId: fe-css-02
order: 19
title: "Real-World Flexbox Patterns: Media Object, Card Footer, & Sticky Footer"
readingMinutes: 8
section: INTI
learningObjectives:
  - "Build the classic Media Object pattern for comments, notifications, and user profiles"
  - "Align Card Footer positions so they always stick neatly to the bottom of cards using margin-top: auto"
  - "Implement a full-page Sticky Footer using min-height: 100vh and flex-direction: column"
codeLanguage: css
codeSnippet: |
  .page-shell {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }

  .page-footer {
    margin-top: auto;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Pola Nyata Flexbox</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <div class="page-shell">
        <header class="top-bar">Portal Belajar Web</header>

        <main class="main-area">
          <!-- Pola 1: Media Object -->
          <article class="media-object">
            <div class="avatar">RA</div>
            <div class="media-body">
              <h4>Rina Andriani</h4>
              <p>Trik margin-top: auto sangat membantu merapikan footer kartu yang panjang teksnya berbeda!</p>
            </div>
          </article>

          <!-- Pola 2: Kartu dengan Footer Menempel di Bawah -->
          <div class="card-row">
            <div class="card">
              <h4>Paket Dasar</h4>
              <p>Cocok untuk pemula.</p>
              <button class="card-action">Pilih Paket</button>
            </div>
            <div class="card">
              <h4>Paket Lengkap</h4>
              <p>Berisi akses penuh proyek studi kasus, latihan interaktif, dan evaluasi akhir.</p>
              <button class="card-action">Pilih Paket</button>
            </div>
          </div>
        </main>

        <!-- Pola 3: Sticky Footer -->
        <footer class="page-footer">
          &copy; 2026 CodeAcademy Indonesia
        </footer>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 0;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Sticky Footer Shell: lengkapi display: flex dan flex-direction: column */
  .page-shell {
    min-height: 100vh;
    /* Tambahkan display: flex; flex-direction: column; di sini */
  }

  .top-bar {
    background-color: #104246;
    color: white;
    padding: 14px 20px;
    font-weight: bold;
  }

  .main-area {
    padding: 20px;
    flex: 1;
  }

  /* 2. Media Object */
  .media-object {
    display: flex;
    align-items: flex-start;
    gap: 14px;
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 14px;
    margin-bottom: 20px;
  }

  .avatar {
    width: 44px;
    height: 44px;
    border-radius: 50%;
    background-color: #df7b56;
    color: white;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .media-body h4 {
    margin: 0 0 4px 0;
    color: #104246;
  }

  .media-body p {
    margin: 0;
    font-size: 13px;
    line-height: 1.5;
  }

  /* 3. Card Footer Sejajar */
  .card-row {
    display: flex;
    gap: 14px;
  }

  .card {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 10px;
    padding: 16px;
  }

  .card h4 {
    margin: 0 0 6px 0;
    color: #104246;
  }

  .card p {
    margin: 0 0 14px 0;
    font-size: 13px;
    color: #6b655d;
  }

  .card-action {
    margin-top: auto;
    background-color: #1a6368;
    color: white;
    border: none;
    padding: 8px 12px;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
  }

  .page-footer {
    margin-top: auto;
    background-color: #e2f0f1;
    color: #104246;
    text-align: center;
    padding: 12px;
    font-size: 13px;
    font-weight: bold;
  }
playgroundTask: "Look at '.page-shell' in CSS: add 'display: flex;' and 'flex-direction: column;' so the page footer immediately drops and sticks to the bottom of the screen (Sticky Footer). Also try changing the text length in one of the cards to see how '.card-action' with 'margin-top: auto' always stays aligned!"
verificationRules:
  - "css:display: flex"
  - "css:flex-direction"
---
In real-world web projects, you will repeatedly encounter three classic component patterns: **Media Object**, **Aligned Card Footer**, and **Sticky Footer**. All three can be solved elegantly with a combination of `flex-direction: column`, `flex-shrink: 0`, and `margin-top: auto`.

```text
1) MEDIA OBJECT                2) CARD FOOTER AUTO           3) STICKY FOOTER (100vh)
+-----------------------+      +-----------+ +-----------+   +------------------------+
| [IMG] Comment Title   |      | Title A   | | Title B   |   | Header                 |
| (fix) Long & neat     |      | Short.    | | This text |   +------------------------+
|       body text       |      |           | | is longer |   | Main Content           |
+-----------------------+      | [Button]  | | [Button]  |   | (auto remaining space) |
                               +-----------+ +-----------+   +------------------------+
                                                             | Footer (sticks bottom) |
                                                             +------------------------+
```

## Pattern 1: Media Object (Avatar + Content)

Social media comment components, notification items, or contact lists always consist of one fixed-size image/icon on the left and a flexible text block on the right.

```css
.media-object {
  display: flex;
  align-items: flex-start; /* Keep avatar at top even with long text */
  gap: 14px;
}

.avatar {
  width: 48px;
  height: 48px;
  flex-shrink: 0; /* CRUCIAL: prevent avatar from squishing when right text is long! */
}

.media-body {
  flex: 1; /* Take up all remaining available width */
}
```

Without `flex-shrink: 0`, long text on the right will squeeze the avatar until its circle shape gets dented into an oval.

## Pattern 2: Bottom-Aligned Card Footer with `margin-top: auto`

When several cards sit side by side in a row, each card's description length is almost always different. To keep the action buttons at the bottom of the cards always in line:

```css
.card {
  display: flex;
  flex-direction: column; /* Stack card contents from top to bottom */
}

.card-action {
  margin-top: auto; /* Push button to bottom of card by absorbing remaining space above it */
}
```

Why is `margin-top: auto` so awesome in Flexbox? Because in `flex-direction: column` mode, a vertical margin set to `auto` claims **all remaining empty space** between the last paragraph and the button, locking the button to the very bottom of the card.

## Pattern 3: Sticky Footer (*Footer Always at the Bottom of the Screen*)

Pages with short content (such as a login page or an "About Us" page) often leave the `<footer>` element floating awkwardly in the middle of the screen. The modern Flexbox solution only needs 4 lines:

```css
.page-shell {
  min-height: 100vh;      /* Minimum height equals screen height (Viewport Height) */
  display: flex;
  flex-direction: column;
}

.main-area {
  flex: 1;                /* Main content fills remaining space; automatically pushes footer to bottom */
}
```

With the combination of `min-height: 100vh` and `flex: 1` (or `margin-top: auto` on `<footer>`), the footer will stick neatly to the bottom of the screen when content is sparse, and still move down normally when page content grows taller!

Open the Playground and complete the `.page-shell` code to test all three at once!