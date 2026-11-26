---
id: css-layout-lanjutan-7
courseId: fe-css-02
order: 26
title: "Hands-On Practice: Responsive E-Commerce Product Card Gallery"
readingMinutes: 7
section: LANJUTAN
learningObjectives:
  - "Combine CSS Grid (for the macro gallery) and Flexbox (for internal card content)"
  - "Create e-commerce cards where the action button always aligns evenly at the bottom"
  - "Build a professional, production-ready UI component for real-world projects"
codeLanguage: css
codeSnippet: |
  .galeri-produk {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
  }

  .kartu-produk {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
starterHtml: |
  <!DOCTYPE html>
  <html lang="id">
    <head>
      <meta charset="UTF-8" />
      <title>Latihan: Galeri Produk E-Commerce</title>
      <link rel="stylesheet" href="style.css" />
    </head>
    <body>
      <h2>Katalog Kursus Pilihan</h2>
      <div class="galeri-produk">
        <!-- Kartu 1 -->
        <div class="kartu">
          <div class="badge">Terpopuler</div>
          <h3>Belajar HTML5 Semantik</h3>
          <p>Membangun struktur dokumen web modern dan ramah SEO dari dasar.</p>
          <div class="footer-kartu">
            <span class="harga">Gratis</span>
            <button class="btn-beli">Daftar</button>
          </div>
        </div>

        <!-- Kartu 2 (Deskripsi Pendek) -->
        <div class="kartu">
          <div class="badge">Baru</div>
          <h3>CSS Flexbox & Grid</h3>
          <p>Kuasai tata letak responsif masa kini.</p>
          <div class="footer-kartu">
            <span class="harga">Rp 0</span>
            <button class="btn-beli">Daftar</button>
          </div>
        </div>

        <!-- Kartu 3 (Deskripsi Panjang) -->
        <div class="kartu">
          <div class="badge">Lanjutan</div>
          <h3>JavaScript & DOM Interaktif</h3>
          <p>Memberi nyawa interaktif pada antarmuka web, event listener, dan manipulasi elemen.</p>
          <div class="footer-kartu">
            <span class="harga">Gratis</span>
            <button class="btn-beli">Daftar</button>
          </div>
        </div>
      </div>
    </body>
  </html>
starterCss: |
  body {
    font-family: sans-serif;
    margin: 16px;
    background-color: #faf7f2;
    color: #24211d;
  }

  /* 1. Atur galeri luar dengan CSS Grid */
  .galeri-produk {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px;
  }

  /* 2. Jadikan kartu sebagai Flexbox vertikal agar tombol selalu di dasar */
  .kartu {
    background-color: #ffffff;
    border: 1px solid #e2ddd5;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    /* Tambahkan display: flex; flex-direction: column; di sini */
  }

  .badge {
    align-self: flex-start;
    background-color: #e2f0f1;
    color: #1a6368;
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 11px;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .kartu h3 {
    margin: 0 0 8px 0;
    color: #104246;
    font-size: 16px;
  }

  .kartu p {
    color: #6b655d;
    font-size: 13px;
    line-height: 1.4;
    margin: 0 0 16px 0;
    /* Beri flex-grow: 1 agar paragraf ini mendorong footer kartu ke bawah! */
  }

  /* 3. Footer kartu (Harga & Tombol) */
  .footer-kartu {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #f1ece4;
    padding-top: 12px;
  }

  .harga {
    font-weight: bold;
    color: #df7b56;
    font-size: 14px;
  }

  .btn-beli {
    background-color: #1a6368;
    color: white;
    border: none;
    padding: 6px 14px;
    border-radius: 6px;
    font-weight: bold;
    cursor: pointer;
  }
playgroundTask: "Notice in Live Preview that the 'Daftar' buttons aren't aligned at the bottom yet because the card paragraphs have different lengths. Add: (1) 'display: flex; flex-direction: column;' to '.kartu'. (2) 'flex-grow: 1;' to '.kartu p'. Click 'Run' and watch the card footers lock neatly to the bottom!"
---
It's time to prove the true teamwork between **CSS Grid** and **Flexbox** in a real-world UI project: **an E-Commerce Product Card Gallery**.

Many beginners run into a classic problem with product cards:
- Cards with long descriptions become very tall.
- Cards with short descriptions look stubby.
- As a result, the "Buy" or "Enroll" buttons at the bottom end up sitting at uneven heights!

```text
Grid (Outer Gallery) -> [ Flexbox Card (column) ]
                        | [  Product Image    ] |
                        | [  Title & Price    ] |
                        | [  Buy Button       ] | (margin-top: auto)
```
## The Winning Collaboration Recipe

We divide the responsibilities cleanly:

1. **Outer Container Uses CSS Grid**:
   ```css
   .galeri-produk {
     display: grid;
     grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
     gap: 16px;
   }
   ```
   Grid guarantees that all product cards share **uniform width and height automatically**, regardless of how many columns fit on screen.

2. **Card Contents Use Vertical Flexbox**:
   ```css
   .kartu {
     display: flex;
     flex-direction: column; /* Flows top-to-bottom */
   }

   .kartu p {
     flex-grow: 1; /* Key trick: paragraph stretches to absorb extra space */
   }
   ```
   By giving `flex-grow: 1` to the paragraph text, the paragraph expands and **pushes `.footer-kartu` straight down to the very bottom** of the card.

3. **Card Footer Uses Horizontal Flexbox**:
   ```css
   .footer-kartu {
     display: flex;
     justify-content: space-between; /* Price on left, button on right */
     align-items: center;
   }
   ```

The result? A crisp, symmetrical product catalog interface that looks great on any device!
