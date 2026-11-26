---
id: css-layout-prep-2
courseId: fe-css-02
order: 2
title: "Capability Map: When Flexbox, When Grid?"
readingMinutes: 5
section: PERSIAPAN
learningObjectives:
  - "Distinguish between 1-dimensional (Flexbox) and 2-dimensional (CSS Grid) systems through real-world analogies"
  - "Understand the Content-First vs Layout-First approach in interface design"
  - "Choose the right technology for the interface components you are going to build"
---
One of the most common questions from beginners is: *"If there is already an advanced Grid, is Flexbox still needed? And when should I choose one over the other?"*

The answer: **they complement each other, rather than compete with each other**. Professional web developers use Flexbox and Grid side by side on the same page.

```text
Flexbox (1-Dimensional - 1 active axis):
  [ Item 1 ]──[ Item 2 ]──[ Item 3 ]  (row OR column)

CSS Grid (2-Dimensional - rows & columns simultaneously):
  +----------+----------+----------+
  | Header (span 3 columns)        |
  +----------+---------------------+
  | Sidebar  | Main Content        |
  +----------+---------------------+
```
## Analogy 1: Bookshelf vs Cinema Seating Plan

Imagine two real-world situations:

1. **Flexbox is like a Bookshelf (1-Dimensional)**:
   When you arrange books on a shelf, you line them up along a single horizontal line. If there is a thicker or taller book, the shelf still flows the other books beside it to the right. Its flow direction focuses on **one dimension**: horizontal (row) or vertical (column).
2. **Grid is like a Cinema Seating Plan (2-Dimensional)**:
   When booking cinema tickets, seats are arranged in rows (letters A, B, C...) and columns (numbers 1, 2, 3...). You can point to seat `C-4`, which sits right at the intersection of a row and a column. CSS Grid works on **two dimensions simultaneously** with precision.

## Content-First vs Layout-First

The fundamental mindset difference between the two lies in how you start:

- **Flexbox follows a Content-First pattern**:
  You have several elements with different text or icon sizes, and you want those elements to share space, line up neatly, or adjust to available space flexibly. Examples: navigation menus, rows of tags/categories, and action buttons with icons.
- **Grid follows a Layout-First pattern**:
  You design the box container grid first (for example: a Header area at the top, a 250px-wide Sidebar on the left, Content in the center, and a Footer at the bottom), and only then place elements into the reserved boxes.

## Quick Decision-Making Guide Table

| Interface Requirement | Ideal Choice | Main Reason |
| :--- | :---: | :--- |
| Aligning an icon next to button text | **Flexbox** | 1D flow, very easy to vertically center position |
| Creating a navigation bar with logo on the left & menu on the right | **Flexbox** | Flexible space distribution with `space-between` |
| Dividing the main page skeleton (Header, Sidebar, Main, Footer) | **CSS Grid** | Sturdy 2D layout that does not shift unexpectedly |
| Product card photo gallery that aligns evenly in boxes | **CSS Grid** | Locks rows and columns with proportional sizes |
| Aligning price text and buttons inside a product card | **Flexbox** | Nested Flexbox inside a Grid cell |

In this course, we will learn Flexbox first as the foundation of element flexibility, then step into CSS Grid to conquer two-dimensional layout architecture.