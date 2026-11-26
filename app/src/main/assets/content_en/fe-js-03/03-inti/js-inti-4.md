---
id: js-inti-4
courseId: fe-js-03
order: 14
title: "Composing Clean Text with Template Literals"
readingMinutes: 5
section: INTI
learningObjectives:
  - Understand the drawbacks of traditional text concatenation with the plus (+) operator
  - Master writing Template Literals using backticks (`)
  - Embed variables and expressions inside strings using ${...} interpolation syntax
codeLanguage: javascript
codeSnippet: |
  const nama = "Rina";
  const usia = 22;
  console.log(`Halo, nama saya ${nama} dan usia saya ${usia} tahun.`);
starterJs: |
  const namaPengguna = "Budi";
  const skorKuis = 90;
  const totalSoal = 100;

  // Merangkai kalimat dinamis dengan template literal
  const laporan = `Siswa ${namaPengguna} meraih nilai ${skorKuis} dari total ${totalSoal}.`;
  console.log(laporan);
playgroundTask: |
  Run the starter code. Make sure the Console prints a single line:
  Siswa Budi meraih nilai 90 dari total 100.
  Try changing namaPengguna or skorKuis and run it again to see the text update automatically.
---

# Composing Clean Text with Template Literals

In web applications, you will very frequently build dynamic sentences: showing a user's name in a welcome greeting, assembling an order confirmation message, or presenting a score summary.

Before ES6 (2015), assembling text in JavaScript could be a nightmare. Let's see why!

---

## The Old Way: Concatenation with Plus (+)

In the past, the only way to combine variables into a string was by using the `+` operator:

```javascript
const name = "Vhan";
const city = "Bandung";

// Prone to missing spaces and hard to read
const sentence = "Hello, my name is " + name + " and I live in " + city + ".";
console.log(sentence);
```

Notice how easy it is to forget a space after `"Hello, my name is "` or before `" and I live in"`. If there are 5 variables in one sentence, the line of code becomes cluttered with confusing quotes.

---

## The Modern Way: Template Literals

ES6 introduced an elegant solution called **Template Literals**. Its defining features are:
1. Instead of regular quotes (`"` or `'`), it uses **backticks (`` ` ``)** (usually located below the Esc key on a keyboard).
2. You insert variables directly inside the text using a dollar sign and curly braces: **`${variableName}`**. This process is called **String Interpolation**.

Compare the code above when written with Template Literals:

```javascript
const name = "Vhan";
const city = "Bandung";

const sentence = `Hello, my name is ${name} and I live in ${city}.`;
console.log(sentence);
```

Output:
```text
Hello, my name is Vhan and I live in Bandung.
```

Much cleaner, neater, and easier to read!

---

## Embedding Calculations Inside `${...}`

Inside `${...}`, you can insert not just variable names, but also math expressions:

```javascript
const itemPrice = 50000;
const quantity = 3;

console.log(`Your shopping total: Rp${itemPrice * quantity}`);
```

Output:
```text
Your shopping total: Rp150000
```

You can also make text wrap onto a new line simply by pressing **Enter** inside the backticks without needing to type `\n`.

Now that we've mastered data and strings, it's time to teach our program how to make decisions with `if`/`else` in the next module!
