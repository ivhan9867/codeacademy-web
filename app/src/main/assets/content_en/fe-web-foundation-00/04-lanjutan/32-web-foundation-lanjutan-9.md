---
id: web-foundation-lanjutan-9
courseId: fe-web-foundation-00
order: 32
title: "Inline Text Formatting: Links, Emphasis, and Short Quotes"
readingMinutes: 7
section: LANJUTAN
codeLanguage: html
learningObjectives:
  - "Create internal and external links using the <a> element with the href attribute"
  - "Add semantic emphasis to text using <em>, <strong>, and inline quotes with <q>"
codeSnippet: |
  <p>
    Aku <em>sungguh-sungguh</em> menikmati proses belajar ini.
    <strong>Catatan:</strong> semua latihan aku kerjakan langsung di Playground.
  </p>
  
  <p>
    Kalau penasaran, kamu bisa baca lebih lanjut di
    <a href="https://developer.mozilla.org" target="_blank" rel="noopener">MDN Web Docs</a>.
  </p>
  
  <p>
    Seperti kata MDN, <q cite="https://developer.mozilla.org">HTML adalah fondasi
    dari setiap halaman web</q>.
  </p>
---
So far you have used **block** elements: elements that always start on a new line of their own, like `<p>`, `<h1>`, and `<ul>`. Now meet **inline** elements: elements that flow right along with the text, without starting a new line.

## Anchor: creating links

The `<a>` (anchor) element is used to create hyperlinks to another page, another site, a file, an email address, or another section on the same page. Its required attribute is `href`, which specifies the link's destination.

```html
<a href="https://developer.mozilla.org">Visit MDN Web Docs</a>
```

Several other special attributes can be used on `<a>`:

- **target**: where the link opens. `_blank` opens in a new tab, `_self` (default) opens in the same tab
- **download**: tells the browser to download the linked file instead of opening it
- **rel**: declares the relationship between this page and the destination page, such as `nofollow` (telling search engines not to follow/endorse the link's reputation) or `noopener` (for security when using `target="_blank"`)
- **hreflang**: the language of the destination document
- **ping**: a list of URLs notified silently when the link is clicked, commonly used for tracking

```html
<a href="https://example.com/file.pdf" download>Download File</a>
<a href="https://example.com" target="_blank" rel="noopener">Open in new tab</a>
```

## Word emphasis: em and strong

- **`<em>`** (*emphasis*) marks words that need vocal stress or emphasis. By default, it is displayed in italics
- **`<strong>`** marks text that is very important or urgent. By default, it is displayed in bold, and screen readers change their tone to emphasize its importance

```html
<p>I wrote this code <em>myself</em>.</p>
<p><strong>Important:</strong> save your work before leaving the Playground.</p>
```

Unlike simply making text italic or bold with CSS, `<em>` and `<strong>` carry **meaning**, not just appearance. This matters for screen readers and search engines.

## Short quotations: q

While `<blockquote>` is for long quotes that stand alone as a block, `<q>` is for short quotes woven into a sentence (inline). Browsers automatically add quotation marks around it.

```html
<p>
  According to MDN, <q cite="https://developer.mozilla.org">HTML is the backbone
  of every web page</q>, and that is no exaggeration.
</p>
```

Just like on `blockquote`, the `cite` attribute on `<q>` does not change how it looks—it is only extra metadata.

## Common mistakes

- Forgetting to set `href` on `<a>`, leaving the link pointing nowhere
- Using `<em>` or `<strong>` purely for visual styling (italic/bold) when you only mean aesthetics. CSS has dedicated properties for that later
- Using `<q>` for a long quote that ought to be its own block

## Try it yourself

Add a sentence to your profile page that uses `<em>` to emphasize one word, and an `<a>` linking to your favorite website.
