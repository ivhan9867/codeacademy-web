/*
 * Cursor-reactive particle field (original implementation).
 * Add data-particles to an element; a canvas is injected behind its content.
 * Options: data-colors="#1A6368,#2AA9AE,#E26D45"  data-density (px gap)  data-radius (px)  data-alpha (resting opacity)
 * A virtual cursor drifts on its own until a real pointer moves, so the effect is visible on touch devices too.
 */
(function () {
  'use strict';

  function hexToRgb(h) {
    h = h.replace('#', '').trim();
    if (h.length === 3) h = h.split('').map(function (c) { return c + c; }).join('');
    var n = parseInt(h, 16);
    return [(n >> 16) & 255, (n >> 8) & 255, n & 255];
  }

  function init(host) {
    var colors = (host.dataset.colors || '#1FB5B0,#3B6BFF,#8B5CF6,#EC4899,#E26D45,#F2A93B').split(',').map(hexToRgb);
    colors.push(colors[0]); // close the loop so the gradient wraps smoothly
    var spacing = parseFloat(host.dataset.density) || 22;
    var radius = parseFloat(host.dataset.radius) || 230;
    var restAlpha = parseFloat(host.dataset.alpha) || 0.5;
    var reduce = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    var canvas = document.createElement('canvas');
    canvas.setAttribute('aria-hidden', 'true');
    canvas.style.cssText = 'position:absolute;inset:0;width:100%;height:100%;pointer-events:none;z-index:0;';
    if (getComputedStyle(host).position === 'static') host.style.position = 'relative';
    host.style.overflow = 'hidden';
    host.insertBefore(canvas, host.firstChild);
    for (var i = 1; i < host.children.length; i++) {
      var c = host.children[i];
      if (getComputedStyle(c).position === 'static') c.style.position = 'relative';
      c.style.zIndex = 1;
    }

    var ctx = canvas.getContext('2d');
    var dpr = Math.min(window.devicePixelRatio || 1, 2);
    var W = 0, H = 0, P = [];
    var cur = { x: -9999, y: -9999 };      // smoothed cursor used for drawing
    var tgt = { x: -9999, y: -9999 };      // where the cursor wants to be
    var lastMove = -1e9, speed = 0, lastX = 0, lastY = 0;
    var raf = 0, running = false;

    function build() {
      var r = host.getBoundingClientRect();
      W = r.width; H = r.height;
      canvas.width = Math.max(1, W * dpr); canvas.height = Math.max(1, H * dpr);
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
      P = [];
      var cols = Math.ceil(W / spacing) + 1, rows = Math.ceil(H / spacing) + 1;
      for (var y = 0; y < rows; y++) {
        for (var x = 0; x < cols; x++) {
          var hx = x * spacing + (y % 2 ? spacing / 2 : 0) + (Math.random() - 0.5) * 4;
          var hy = y * spacing + (Math.random() - 0.5) * 4;
          P.push({ hx: hx, hy: hy, x: hx, y: hy, ang: 0, len: 2, ph: Math.random() * 6.28 });
        }
      }
    }

    function colorAt(t) {
      t = ((t % 1) + 1) % 1;                 // wrap 0..1
      var f = t * (colors.length - 1), i = Math.min(colors.length - 2, Math.floor(f)), k = f - i;
      var a = colors[i], b = colors[i + 1];
      return [a[0] + (b[0] - a[0]) * k, a[1] + (b[1] - a[1]) * k, a[2] + (b[2] - a[2]) * k];
    }

    function onMove(e) {
      var r = host.getBoundingClientRect();
      var x = e.clientX - r.left, y = e.clientY - r.top;
      if (x < -60 || y < -60 || x > W + 60 || y > H + 60) return;
      if (lastMove < 0 || performance.now() - lastMove > 3000) { cur.x = x; cur.y = y; }
      speed = Math.min(80, Math.hypot(x - lastX, y - lastY));
      lastX = x; lastY = y;
      tgt.x = x; tgt.y = y;
      lastMove = performance.now();
    }

    function frame(now) {
      raf = requestAnimationFrame(frame);
      var idle = now - lastMove > 2500;
      if (idle) { // virtual cursor wanders on its own
        var t = now * 0.00035;
        tgt.x = W * (0.5 + 0.34 * Math.sin(t * 1.3));
        tgt.y = H * (0.5 + 0.30 * Math.sin(t * 1.9 + 1.2));
        speed *= 0.9;
      }
      cur.x += (tgt.x - cur.x) * (idle ? 0.04 : 0.13);
      cur.y += (tgt.y - cur.y) * (idle ? 0.04 : 0.13);
      speed *= 0.93;

      var R = radius * (1 + speed / 160);
      ctx.clearRect(0, 0, W, H);
      ctx.lineCap = 'round';

      for (var i = 0; i < P.length; i++) {
        var p = P[i];
        var dx = p.hx - cur.x, dy = p.hy - cur.y;
        var d = Math.hypot(dx, dy) || 1;
        var near = Math.max(0, 1 - d / R);
        var e = near * near * (3 - 2 * near);           // smoothstep: soft edge of the ring

        // push outward from the cursor: clear centre, bunched ring
        var push = e * R * 0.42;
        var tx = p.hx + (dx / d) * push, ty = p.hy + (dy / d) * push;
        p.x += (tx - p.x) * 0.16; p.y += (ty - p.y) * 0.16;

        // dashes point away from cursor (radial), dots drift a little at rest
        var radial = Math.atan2(dy, dx);
        var rest = p.ph + now * 0.0003;
        var da = Math.atan2(Math.sin(radial - p.ang), Math.cos(radial - p.ang));
        p.ang += da * (e > 0.02 ? 0.25 : 0);
        if (e <= 0.02) p.ang = rest;

        var len = 2 + e * 15;
        var ang = p.ang;
        // resting dots take colour from their place on the page, active ones from their angle around the cursor
        var tPos = (p.hx / W) * 0.65 + (p.hy / H) * 0.35;
        var cAng = colorAt((radial + Math.PI) / (Math.PI * 2));
        var cPos = colorAt(tPos);
        var m = Math.min(1, e * 2.2);
        var col = [cPos[0] + (cAng[0] - cPos[0]) * m, cPos[1] + (cAng[1] - cPos[1]) * m, cPos[2] + (cAng[2] - cPos[2]) * m];
        var a = restAlpha + e * (0.95 - restAlpha);
        // fade the very centre so the hole reads clearly
        if (d < R * 0.12) a *= d / (R * 0.12);

        ctx.strokeStyle = 'rgba(' + (col[0] | 0) + ',' + (col[1] | 0) + ',' + (col[2] | 0) + ',' + a.toFixed(3) + ')';
        ctx.lineWidth = 1.7 + e * 1.3;
        var hl = len / 2, cx = Math.cos(ang) * hl, cy = Math.sin(ang) * hl;
        ctx.beginPath();
        ctx.moveTo(p.x - cx, p.y - cy);
        ctx.lineTo(p.x + cx, p.y + cy);
        ctx.stroke();
      }
    }

    function start() { if (!running) { running = true; raf = requestAnimationFrame(frame); } }
    function stop() { running = false; cancelAnimationFrame(raf); }

    build();
    if (reduce) { tgt.x = cur.x = W * 0.5; tgt.y = cur.y = H * 0.5; frame(0); cancelAnimationFrame(raf); return; }

    window.addEventListener('pointermove', onMove, { passive: true });
    var rt;
    window.addEventListener('resize', function () { clearTimeout(rt); rt = setTimeout(build, 150); });
    if ('IntersectionObserver' in window) {
      new IntersectionObserver(function (en) { en[0].isIntersecting ? start() : stop(); }).observe(host);
    } else start();
  }

  function boot() { document.querySelectorAll('[data-particles]').forEach(init); }
  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', boot); else boot();
})();
