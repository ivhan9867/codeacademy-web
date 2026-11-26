---
id: js-prep-glossary
courseId: fe-js-03
order: 4
title: "JavaScript Terms Glossary"
readingMinutes: 7
section: PERSIAPAN
learningObjectives:
  - Learn core technical terms in JavaScript programming concisely and accurately
  - Understand the differences between commonly confused terms like Parameter vs. Argument and Null vs. Undefined
  - Use this glossary as a quick reference while reading core and advanced modules
---

# JavaScript Terms Glossary

When learning JavaScript, you will frequently run into technical English terms. You don't need to memorize all of them at once—use this page as a pocket dictionary whenever you need a quick refresher.

---

## Variables, Data Types, & Values

- **Variable** — A labeled storage container in computer memory used to hold data so it can be read or processed later.
- **const** — A keyword for declaring a variable whose binding is fixed and cannot be reassigned after creation.
- **let** — A modern keyword for declaring a *block-scoped* variable whose value can be changed or updated while the program runs.
- **var** — An older keyword for declaring variables with *function scope* and *hoisting* behavior; best avoided in modern code.
- **Primitive** — A basic, simplest data type whose value is single and cannot be mutated directly (*immutable*), such as string, number, and boolean.
- **String** — A data type representing text, written inside single quotes (`'...'`), double quotes (`"..."`), or *backticks* (`` `...` ``).
- **Number** — The numeric data type in JavaScript covering both whole numbers (*integers*) and decimal numbers (*floating-point*).
- **Boolean** — A logical data type with only two possible values: `true` or `false`.
- **Null** — A special value intentionally assigned to indicate that a variable is empty or holds no object at all.
- **Undefined** — The default value for a variable that has been declared without being assigned a value yet, or for a property that isn't found.
- **Template Literal** — A way to write strings using *backticks* (`` ` ``) that supports embedding variables with `${...}` as well as multi-line text.

---

## Operators, Logic, & Control Flow

- **Operator** — A special symbol such as `+`, `-`, `===`, `&&`, or `??` used to perform math, comparison, or logical operations on values.
- **Strict Equality (===)** — A strict comparison operator that checks whether two values match in both **value** and **data type** without automatic conversion.
- **Truthy** — Any non-boolean value considered `true` when evaluated in a boolean context such as an `if` condition.
- **Falsy** — A small set of values automatically treated as `false` in a boolean context: `false`, `0`, `-0`, `0n`, `""`, `null`, `undefined`, and `NaN`.
- **Nullish Coalescing (??)** — A modern operator that returns the right-hand value only when the left-hand value is `null` or `undefined`.
- **Conditional** — A decision-making structure in code (such as `if`, `else if`, `else`, or `switch`) that runs different blocks depending on specific conditions.
- **Loop** — A repetition mechanism (such as `for`, `while`, or `for...of`) that executes a block of code repeatedly while a condition holds.

---

## Data Structures: Array & Object

- **Array** — An ordered list data structure for storing multiple values in a single variable using square brackets `[...]`.
- **Index** — The position number of an element inside an array or a character in a string; in JavaScript, counting always starts at `0`.
- **Object** — A data structure that stores a collection of key-value pairs inside curly braces `{...}`.
- **Property** — A named key-and-value pair attached to an object to describe its data or characteristics.
- **Method** — A function stored as a property inside an object or data type (for example `array.push()` or `text.trim()`).
- **Destructuring** — A concise ES6+ syntax for unpacking values from arrays or properties from objects directly into separate variables.
- **Spread Operator** — The three-dot operator (`...`) used to unpack array elements or object properties so they can easily be copied or merged *immutably*.

---

## Functions & Scope

- **Function** — A packaged block of code designed to perform a specific task that can be called repeatedly across different parts of a program.
- **Parameter** — A placeholder variable name listed inside the parentheses when defining a function.
- **Argument** — The actual value passed into a function when that function is called or invoked.
- **Return Value** — The final output value sent back by a function to its caller using the `return` keyword.
- **Arrow Function** — A shorter way to write functions using arrow syntax `=>`, popular for *callbacks* and single-line functions.
- **Scope** — The region or visibility boundary where a variable and function can be accessed in program code.
- **Block Scope** — A restricted scope inside a pair of curly braces `{...}` (such as in `if` or `for`) that applies to `let` and `const`.
- **Global Scope** — The outermost scope where variables declared outside any function or block can be accessed from anywhere.
- **Closure** — A function's ability to remember and access variables from its outer scope even after that outer function has finished running.
- **Callback** — A function passed as an argument into another function to be executed later after a task completes or an event occurs.
- **Higher-Order Function** — A function that accepts another function as an argument, returns a new function as a result, or both (examples: `map`, `filter`, `find`).

---

## Web Interaction, DOM, & Storage

- **DOM (Document Object Model)** — A structured object-tree representation of an HTML document that lets JavaScript read and modify web page content.
- **querySelector** — A method on the `document` object used to select the first HTML element matching a given CSS selector pattern.
- **Event** — An occurrence or interaction on a web page, such as a button `click`, user typing (`input`), or form submission (`submit`).
- **Event Listener** — A watcher attached via `addEventListener()` on an HTML element to run a specific function when an event happens.
- **preventDefault** — A method on an event object used to stop the browser's default behavior, such as preventing an automatic page *reload* when submitting a form.
- **JSON** — Short for *JavaScript Object Notation*; a lightweight text-based data exchange and storage format easy for both humans and machines to read.
- **LocalStorage** — Built-in browser storage formatted as string *key-value* pairs whose data persists even after the page is closed or reloaded.

---

## Asynchronous Programming & Networking

- **Asynchronous** — An execution pattern where tasks run in the background without blocking subsequent lines of code or freezing the user interface.
- **Promise** — A JavaScript object representing the future outcome of an asynchronous operation, with three states: `pending`, `fulfilled`, and `rejected`.
- **async/await** — A modern pair of keywords built on top of Promises so asynchronous code reads cleanly and sequentially like synchronous code.
- **Fetch API** — The browser's modern built-in interface (`fetch()`) for sending HTTP requests to a server or API asynchronously and receiving the response.

---

## Debugging & Error Handling

- **SyntaxError** — A code grammar mistake (such as a missing closing parenthesis or a misspelled keyword) that prevents JavaScript from parsing the script.
- **ReferenceError** — An error thrown when code tries to access a variable that was never declared or is outside the current *scope*.
- **TypeError** — An error thrown when attempting an operation on an incompatible data type, such as calling a method on `undefined` or reassigning a `const`.
- **Stack Trace** — The call history trail with line and column numbers shown in the Console when an error occurs to help track down bugs.
