---
id: web-foundation-pend-4
courseId: fe-web-foundation-00
order: 12
title: "Web Server"
readingMinutes: 4
section: PENDAHULUAN
learningObjectives:
  - "Understand the function of a web server as a computer that stores and serves website files"
  - "Explain the flow of requests and responses between a browser and a server"
---
When you search for something on a search engine, or type a product name into an online store's search bar, a list of results immediately appears. Where does the data come from? Who prepares it? The answer lies in a computer working behind the scenes: a server.

## Client, server, and the request-response cycle

The device used to access a website is called a **client**. More specifically, the browser acts as the client because it is the one requesting data. The computer that stores and serves the website's contents is called a **server**.

Here is the flow:

1. The browser sends a **request** to the server.
2. The server processes that request.
3. The server sends back a **response** containing the needed data, such as HTML, CSS, JavaScript files, images, and other assets.

This sequence is called the *request-response cycle*.

Imagine a restaurant. You are the customer hungry for information. The waiter is the browser who takes and delivers your order. The kitchen is the server that cooks the order. Once ready, the dish (web page) is brought by the waiter to your table. You have to wait briefly, though usually not for long.

## What is a web server?

Many people picture a server as a giant, super-fast computer. Often true, though incomplete. The term **web server** can refer to two sides working together:

- **Hardware side**: a computer whose specs are tailored to its needs. A website storing lots of photos needs large storage, while a website performing complex calculations needs a powerful processor.
- **Software side**: a program running on that computer so it can accept requests and send data using **HTTP** (HyperText Transfer Protocol), the standard rules for exchanging data between browsers and servers. Examples include NGINX and Apache. You can also build one yourself with a server-side programming language, for instance via Node.js.

The version of HTTP where data is encrypted is called **HTTPS**. That is why a padlock icon appears in the address bar of many websites.

## DNS: the phonebook of the internet

Every device connected to the internet has an **IP address**, a string of numbers that works like a home address, including the server where a website is stored. Humans have a hard time remembering strings of numbers, especially dozens of them, and a server's IP address can also change from time to time. That's why we use **domain names** like `contoh.com`.

A domain name doesn't replace an IP. Computers still use IPs behind the scenes. What bridges the two is **DNS (Domain Name System)**. Here is how it works:

1. You open `contoh.com`. The browser first checks the **DNS cache**, a record of IP addresses for domains visited previously.
2. If the IP address is in that record, the browser uses it right away.
3. If not, the computer asks a **DNS server**, whose job is to provide the IP address for registered domains.
4. After receiving the IP address, the browser sends a request to the web server at that address.

An easy analogy is your phone's contacts list: you remember your friend's name, while your phone remembers their number.

For extra reading, look up the article "What is a web server?" on MDN Web Docs and visual explanations of how DNS works.
