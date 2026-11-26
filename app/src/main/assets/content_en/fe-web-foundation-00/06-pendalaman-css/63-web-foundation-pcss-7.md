---
id: web-foundation-pcss-7
courseId: fe-web-foundation-00
order: 63
title: "Text Formatting: Splitting Font and Text Properties"
readingMinutes: 4
section: PENDALAMAN_CSS
learningObjectives:
  - "Differentiate between font properties (character appearance) and text properties (text layout and decoration)"
  - "Recognize practical examples of applying font and text properties to paragraph elements"
---
## Understanding the Two Groups of Text Styling

Now that you know how to write selectors, let's dive into the part you see most often on websites: **text**. Almost every web page is dominated by written content, so how text is displayed plays a huge role in reading comfort and the professional feel of your site.

In CSS, text styling is neatly separated into two major categories:

### 1. Font Properties Group
This category controls the **physical characteristics of the letters themselves**, as if you were choosing the metal type blocks on a typewriter. Properties in this group include:
- `font-family`: specifies the typeface (for example, Arial, Roboto, or Georgia).
- `font-size`: sets how large or small the letters are.
- `font-weight`: controls the thickness of the letters (such as *bold* or *normal*).
- `font-style`: controls the slant of the letters (*italic* or *normal*).

### 2. Text Properties Group
This category controls **how a group of letters is arranged within a sentence or paragraph**, much like formatting paragraphs in Microsoft Word. Properties in this group include:
- `text-align`: sets paragraph alignment (left, center, right, or justified).
- `line-height`: controls the vertical spacing between lines of text.
- `text-decoration`: adds an underline or strikethrough to text.
- `text-transform`: turns text into all uppercase or all lowercase letters.
- `letter-spacing`: adjusts the spacing between individual characters.

## Quick Code Example

Notice how both groups work side by side on a single element:

```css
p {
  /* Font Group: styling the letterforms */
  font-family: 'Helvetica Neue', sans-serif;
  font-size: 16px;
  font-weight: 400;

  /* Text Group: styling the paragraph layout */
  text-align: justify;
  line-height: 1.6;
  letter-spacing: 0.5px;
}
```

## Looking Ahead

This separation exists so you won't get confused when looking for the right property:
- Want to change the typeface to look more casual or elegant? Look in the **font group**.
- Want to adjust line spacing so text isn't too cramped on a phone screen? Look in the **text group**.

In the upcoming lessons in this section, we will cover font properties first, followed by text properties, before combining both in a hands-on exercise.
