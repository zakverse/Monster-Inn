/**
 * Monster Inn — Unified BGM Controller  (monster-inn-music.js)
 * =============================================================
 * Scope "lobby"    → Sunrise_at_the_Gate.mp3   (Landing + Login pages)
 * Scope "main-app" → Arrival_at_the_Hearth.mp3 (Dashboard + all app pages)
 *
 * localStorage keys used:
 *   mi_music_enabled   "true" / "false"
 *   mi_lobby_time      float (seconds)
 *   mi_mainapp_time    float (seconds)
 *   mi_music_volume    float 0-1
 *
 * NO backend / Java / auth files changed.
 */
(function (root) {
  'use strict';

  /* ── Constants ─────────────────────────────────────────────────── */
  var KEY_ENABLED    = 'mi_music_enabled';
  var KEY_LOBBY_TIME = 'mi_lobby_time';
  var KEY_MAIN_TIME  = 'mi_mainapp_time';
  var KEY_VOLUME     = 'mi_music_volume';

  var SRC_LOBBY   = '/images/audio/Sunrise_at_the_Gate.mp3';
  var SRC_MAINAPP = '/images/audio/Arrival_at_the_Hearth.mp3';

  var DEFAULT_VOL   = 0.30;
  var FADE_STEP_MS  = 40;
  var FADE_DUR_MS   = 160;
  var SAVE_TICK_MS  = 350;

  /* ── State ─────────────────────────────────────────────────────── */
  var _audio      = null;
  var _scope      = null;
  var _enabled    = true;
  var _vol        = DEFAULT_VOL;
  var _saveTimer  = null;
  var _fadeTimer  = null;

  /* ── Storage helpers ───────────────────────────────────────────── */
  function lsGet(k, fb) {
    try { var v = localStorage.getItem(k); return v !== null ? v : fb; }
    catch (e) { return fb; }
  }
  function lsSet(k, v) { try { localStorage.setItem(k, String(v)); } catch (e) {} }
  function lsFloat(k, fb) {
    var n = parseFloat(lsGet(k, ''));
    return isFinite(n) && n >= 0 ? n : fb;
  }

  /* ── Preferences ───────────────────────────────────────────────── */
  function readPrefs() {
    _enabled = lsGet(KEY_ENABLED, 'true') !== 'false';
    _vol     = lsFloat(KEY_VOLUME, DEFAULT_VOL);
  }

  /* ── Time save / restore ───────────────────────────────────────── */
  function saveTime() {
    if (!_audio || !_scope) return;
    var t = _audio.currentTime;
    if (!isFinite(t) || t < 0) return;
    lsSet(_scope === 'lobby' ? KEY_LOBBY_TIME : KEY_MAIN_TIME, t);
  }
  function restoreTime() {
    if (!_audio || !_scope) return;
    var t = lsFloat(_scope === 'lobby' ? KEY_LOBBY_TIME : KEY_MAIN_TIME, 0);
    if (t > 0) { try { _audio.currentTime = t; } catch (e) {} }
  }

  /* ── Fade helpers ──────────────────────────────────────────────── */
  function clearFade() { if (_fadeTimer) { clearInterval(_fadeTimer); _fadeTimer = null; } }

  function fadeIn(target) {
    clearFade();
    _audio.volume = 0;
    var steps = Math.max(1, Math.round(FADE_DUR_MS / FADE_STEP_MS));
    var step  = target / steps;
    var cur   = 0;
    _fadeTimer = setInterval(function () {
      cur += step;
      if (cur >= target) { _audio.volume = target; clearFade(); }
      else               { _audio.volume = cur; }
    }, FADE_STEP_MS);
  }

  function fadeOut(cb) {
    clearFade();
    if (!_audio || _audio.paused) { if (cb) cb(); return; }
    var start = _audio.volume;
    var steps = Math.max(1, Math.round(FADE_DUR_MS / FADE_STEP_MS));
    var step  = start / steps;
    _fadeTimer = setInterval(function () {
      if (_audio.volume - step <= 0.001) {
        _audio.volume = 0; _audio.pause(); clearFade(); if (cb) cb();
      } else {
        _audio.volume -= step;
      }
    }, FADE_STEP_MS);
  }

  /* ── Save loop ─────────────────────────────────────────────────── */
  function startLoop() {
    if (_saveTimer) clearInterval(_saveTimer);
    _saveTimer = setInterval(saveTime, SAVE_TICK_MS);
  }
  function stopLoop() { if (_saveTimer) { clearInterval(_saveTimer); _saveTimer = null; } }

  /* ── Attempt play ──────────────────────────────────────────────── */
  function tryPlay(withFade) {
    if (!_audio) return;
    var p; try { p = _audio.play(); } catch (e) { return; }
    var afterPlay = function () {
      if (withFade) fadeIn(_vol); else _audio.volume = _vol;
      startLoop();
    };
    if (p && typeof p.then === 'function') {
      p.then(afterPlay).catch(function () {
        /* Blocked by browser — will unlock on first tap */
        _audio.volume = _vol;
      });
    } else { afterPlay(); }
  }

  /* ── Autoplay unlock ───────────────────────────────────────────── */
  function setupUnlock() {
    function onTouch() {
      if (_enabled && _audio && _audio.paused) tryPlay(true);
      document.removeEventListener('pointerdown', onTouch);
      document.removeEventListener('keydown', onTouch);
    }
    document.addEventListener('pointerdown', onTouch, { once: true });
    document.addEventListener('keydown',     onTouch, { once: true });
  }

  /* ── Build audio element ───────────────────────────────────────── */
  function buildAudio(src) {
    /* Reuse if already in DOM with same scope */
    var ex = document.getElementById('mi-bgm-audio');
    if (ex) {
      var exSrc = ex.dataset.miSrc || '';
      if (exSrc === src) return ex;
      ex.pause(); ex.remove();
    }
    var a = document.createElement('audio');
    a.id           = 'mi-bgm-audio';
    a.dataset.miSrc = src;
    a.loop         = true;
    a.preload      = 'auto';
    a.setAttribute('aria-hidden', 'true');
    var s = document.createElement('source');
    s.src = src; s.type = 'audio/mpeg';
    a.appendChild(s);
    a.volume = 0;
    document.body.appendChild(a);
    return a;
  }

  /* ── Update toggle buttons ─────────────────────────────────────── */
  function updateToggles() {
    document.querySelectorAll('[data-mi-music-toggle]').forEach(function (btn) {
      var onLbl  = btn.getAttribute('data-label-on')  || 'Musik: ON';
      var offLbl = btn.getAttribute('data-label-off') || 'Musik: OFF';
      var icon   = btn.querySelector('[data-mi-icon]');
      var span   = btn.querySelector('[data-mi-text]');

      if (span) { span.textContent = _enabled ? onLbl : offLbl; }
      else      { btn.textContent  = _enabled ? onLbl : offLbl; }

      btn.setAttribute('aria-pressed', String(_enabled));

      if (icon) {
        icon.className = _enabled ? 'fa-solid fa-volume-high' : 'fa-solid fa-volume-xmark';
        icon.style.color = _enabled ? '#85a67a' : '#e8734a';
      }

      /* Visual state on the button wrapper */
      btn.setAttribute('data-mi-on', _enabled ? '1' : '0');
    });
  }

  /* ── Toggle ────────────────────────────────────────────────────── */
  function toggle() {
    _enabled = !_enabled;
    lsSet(KEY_ENABLED, _enabled);
    if (_enabled) { restoreTime(); tryPlay(true); }
    else          { saveTime(); fadeOut(null); stopLoop(); }
    updateToggles();
  }

  /* ── Init (called once per page) ──────────────────────────────── */
  function init() {
    readPrefs();
    _scope = document.body.getAttribute('data-music-scope') || null;
    if (!_scope) return;  /* page has no music — do nothing */

    var src = _scope === 'lobby' ? SRC_LOBBY : SRC_MAINAPP;
    _audio  = buildAudio(src);
    _audio.loop = true;

    /* Try restore immediately */
    restoreTime();

    /* Re-restore after metadata (more reliable with browsers) */
    _audio.addEventListener('loadedmetadata', restoreTime);

    /* Play when ready */
    _audio.addEventListener('canplay', function handler() {
      _audio.removeEventListener('canplay', handler);
      restoreTime();
      if (_enabled) tryPlay(true);
    });

    /* If cached & already ready */
    if (_audio.readyState >= 2) { restoreTime(); if (_enabled) tryPlay(true); }

    /* Autoplay unlock */
    setupUnlock();

    /* Wire toggle buttons */
    updateToggles();
    document.querySelectorAll('[data-mi-music-toggle]').forEach(function (btn) {
      btn.addEventListener('click', function () {
        if (root.MonsterInnSfx) root.MonsterInnSfx.play('click');
        toggle();
      });
    });

    /* Save before navigation */
    window.addEventListener('pagehide',    saveTime);
    window.addEventListener('beforeunload', saveTime);
    window.addEventListener('visibilitychange', function () {
      if (document.hidden) saveTime();
    });

    /* Start save loop if already playing */
    if (!_audio.paused) startLoop();
  }

  /* ── Public API ────────────────────────────────────────────────── */
  root.MonsterInnMusic = Object.freeze({
    init:      init,
    toggle:    toggle,
    isEnabled: function () { return _enabled; },
    saveState: saveTime
  });

  /* Auto-boot */
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

})(window);
