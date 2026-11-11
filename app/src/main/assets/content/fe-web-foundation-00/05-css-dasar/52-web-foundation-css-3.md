---
id: web-foundation-css-3
courseId: fe-web-foundation-00
order: 52
title: "Tiga Cara Melampirkan CSS ke HTML"
readingMinutes: 5
section: CSS_DASAR
codeSnippets:
  external: |
    <head>
      <title>Halaman Saya</title>
      <link rel="stylesheet" href="style.css">
    </head>
  embedded: |
    <head>
      <title>Halaman Saya</title>
      <style>
        h1 {
          color: green;
        }
      </style>
    </head>
  inline: |
    <h1 style="color: green;">Judul Halaman</h1>
learningObjectives:
  - "Membedakan tiga cara melampirkan CSS: external, embedded, dan inline"
  - "Memilih cara yang tepat untuk kebutuhan styling sederhana"
---
Rule CSS yang udah kamu tulis itu gak akan ngaruh ke apapun kalau belum "dilampirkan" ke dokumen HTML. Ada tiga cara buat ngelakuin ini.

**1. External Style Sheet** — paling direkomendasikan. Kamu bikin file terpisah berekstensi `.css`, lalu sambungkan ke HTML pakai elemen `<link>` di dalam `<head>`. Satu file CSS ini bisa dipakai berulang kali di banyak halaman HTML sekaligus — ini kenapa cara ini paling powerful buat proyek nyata.

**2. Embedded Style Sheet** — rule CSS ditulis langsung di dalam elemen `<style>`, biasanya ditaruh di `<head>`. Bedanya sama external: styling ini cuma berlaku buat satu file HTML itu aja, gak bisa dipakai ulang di halaman lain.

**3. Inline Style** — styling ditulis langsung di atribut `style` pada elemen HTML itu sendiri, tanpa selector sama sekali (karena elemennya udah jelas yang mana). Cara ini paling gampang buat styling cepat satu elemen, tapi paling gak direkomendasikan buat proyek yang lebih besar karena bikin HTML dan styling tercampur jadi satu dan susah di-maintain.

Buat belajar dan latihan kecil, ketiganya oke-oke aja. Tapi kalau kamu bikin website sungguhan nanti, biasain pakai External Style Sheet dari awal — itu yang dipakai hampir semua proyek web developer profesional.
