---
id: web-foundation-pend-5
courseId: fe-web-foundation-00
order: 13
title: "Web Browser"
readingMinutes: 3
section: PENDAHULUAN
learningObjectives:
  - "Understand the main function of a web browser in downloading, translating, and displaying web code"
  - "Recognize the role of the rendering engine in turning HTML into a visual display"
---
Imagine you want to travel to an island. You need the right vehicle and a clear destination address. In the web world, the vehicle is the **browser**, and the destination address is the **URL**.

## What is a browser?

A **web browser** (abbreviated as browser) is a program that fetches and displays web pages. Not just one website: every active website in the world can be explored through a browser, including pages linked together within a single domain.

There are many browser choices, including:

- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Safari
- Opera

Whichever browser you use, the way it works is the same: it follows the same rules to request and display web pages.

## Getting to know URLs

The box at the top of the browser where you type an address is called the **address bar**. Inside it is a **URL** (*Uniform Resource Locator*). A URL has three main parts. For example, `https://contoh.com/belajar/html`:

- **Protocol**: the communication rule being used, which is `https`
- **Domain name**: the name of the destination server, which is `contoh.com`
- **Path**: the location of the requested page or file on the server, which is `/belajar/html`

## How a browser works

When you press enter in the address bar, roughly this happens:

1. The browser looks up the IP address of the domain in the URL (via DNS).
2. The browser sends an **HTTP request** to the web server at that IP address.
3. The web server replies with an **HTTP response** containing HTML, CSS, JavaScript, images, and other needed files.
4. The browser assembles all those files into a page you can see and interact with.

This process repeats every time you open a new page.

## Status codes: signals from the server

Every response carries a **status code** telling you the outcome of your request. Three you will encounter most often:

- `200 OK`: the request succeeded, the page was sent
- `404 Not Found`: the requested page or resource was not found
- `500 Internal Server Error`: the server itself encountered a problem while processing the request

## Try it yourself

If you are reading this material on a laptop or PC, open any website, right-click, then choose **Inspect** and open the **Network** tab. Reload the page, then observe the list of files requested by the browser along with their status codes. That is the request-response cycle you just learned, running right in front of your eyes.
