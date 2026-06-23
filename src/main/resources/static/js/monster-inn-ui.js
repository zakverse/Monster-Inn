(function () {
    const AudioContextClass = window.AudioContext || window.webkitAudioContext;
    const STORAGE_KEY = "monsterInnSfxEnabled";
    const MASTER_VOLUME = 0.16;
    let audioContext = null;
    let enabled = readPreference();

    function readPreference() {
        try {
            return window.localStorage.getItem(STORAGE_KEY) !== "false";
        } catch (error) {
            return true;
        }
    }

    function savePreference() {
        try {
            window.localStorage.setItem(STORAGE_KEY, String(enabled));
        } catch (error) {
            // SFX tetap dapat digunakan ketika localStorage tidak tersedia.
        }
    }

    function getContext() {
        if (!AudioContextClass) return null;
        if (!audioContext) audioContext = new AudioContextClass();
        return audioContext;
    }

    function unlock() {
        const context = getContext();
        if (!context) return Promise.resolve(false);
        if (context.state === "running") return Promise.resolve(true);
        return context.resume()
            .then(() => context.state === "running")
            .catch(() => false);
    }

    function tone(context, options) {
        const start = context.currentTime + (options.delay || 0);
        const duration = Math.min(options.duration || 0.1, 0.6);
        const attack = Math.min(options.attack || 0.008, duration / 2);
        const oscillator = context.createOscillator();
        const gain = context.createGain();

        oscillator.type = options.type || "square";
        oscillator.frequency.setValueAtTime(options.frequency, start);
        if (options.endFrequency) {
            oscillator.frequency.exponentialRampToValueAtTime(
                Math.max(options.endFrequency, 1),
                start + duration
            );
        }

        gain.gain.setValueAtTime(0.0001, start);
        gain.gain.exponentialRampToValueAtTime(
            Math.max((options.volume || 0.5) * MASTER_VOLUME, 0.0001),
            start + attack
        );
        gain.gain.exponentialRampToValueAtTime(0.0001, start + duration);

        oscillator.connect(gain).connect(context.destination);
        oscillator.start(start);
        oscillator.stop(start + duration + 0.02);
    }

    const sounds = {
        click(context) {
            tone(context, {
                frequency: 210,
                endFrequency: 105,
                duration: 0.075,
                volume: 0.55,
                type: "square"
            });
        },
        success(context) {
            [523.25, 659.25, 783.99].forEach((frequency, index) => {
                tone(context, {
                    frequency,
                    duration: 0.14,
                    delay: index * 0.09,
                    volume: 0.42,
                    type: "square"
                });
            });
        },
        error(context) {
            tone(context, {
                frequency: 145,
                endFrequency: 82,
                duration: 0.28,
                volume: 0.48,
                type: "sawtooth"
            });
            tone(context, {
                frequency: 116,
                endFrequency: 70,
                duration: 0.24,
                delay: 0.035,
                volume: 0.28,
                type: "square"
            });
        },
        magic(context) {
            [880, 1174.66, 1396.91, 1760].forEach((frequency, index) => {
                tone(context, {
                    frequency,
                    endFrequency: frequency * 1.12,
                    duration: 0.18,
                    delay: index * 0.055,
                    volume: 0.25,
                    type: "sine"
                });
            });
        },
        coin(context) {
            tone(context, {
                frequency: 988,
                endFrequency: 1318.51,
                duration: 0.09,
                volume: 0.48,
                type: "square"
            });
            tone(context, {
                frequency: 1318.51,
                endFrequency: 1760,
                duration: 0.13,
                delay: 0.07,
                volume: 0.38,
                type: "square"
            });
        }
    };

    function play(name) {
        if (!enabled || !sounds[name]) return Promise.resolve(false);
        return unlock().then((ready) => {
            if (!ready || !enabled) return false;
            sounds[name](audioContext);
            return true;
        });
    }

    function setEnabled(nextEnabled) {
        enabled = Boolean(nextEnabled);
        savePreference();
        if (enabled) unlock();
        return enabled;
    }

    window.MonsterInnSfx = Object.freeze({
        play,
        unlock,
        isEnabled: () => enabled,
        setEnabled,
        toggle: () => setEnabled(!enabled)
    });

    function unlockAfterInteraction() {
        unlock().then((ready) => {
            if (!ready) return;
            document.removeEventListener("pointerdown", unlockAfterInteraction);
            document.removeEventListener("keydown", unlockAfterInteraction);
        });
    }

    document.addEventListener("pointerdown", unlockAfterInteraction);
    document.addEventListener("keydown", unlockAfterInteraction);
})();

(function () {
    const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

    function initNavbar() {
        const nav = document.querySelector(".mi-nav");
        const toggle = document.querySelector(".mi-menu-toggle");
        const links = document.querySelector(".mi-nav-links");

        if (nav) {
            const shrink = () => nav.classList.toggle("is-shrunk", window.scrollY > 26);
            shrink();
            window.addEventListener("scroll", shrink, { passive: true });
        }

        if (toggle && links) {
            toggle.addEventListener("click", () => {
                const isOpen = links.classList.toggle("is-open");
                toggle.setAttribute("aria-expanded", String(isOpen));
            });

            links.querySelectorAll("a").forEach((link) => {
                link.addEventListener("click", () => {
                    links.classList.remove("is-open");
                    toggle.setAttribute("aria-expanded", "false");
                });
            });
        }
    }

    function initReveal() {
        const targets = Array.from(document.querySelectorAll(
            ".mi-reveal, .mi-pixel-card, .pixel-card, .room-box, .element-card, .service-item"
        ));

        if (!targets.length) return;

        if (reduceMotion || !("IntersectionObserver" in window)) {
            targets.forEach((target) => target.classList.add("mi-revealed"));
            return;
        }

        targets.forEach((target) => target.classList.add("mi-pre-reveal"));

        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (!entry.isIntersecting) return;
                entry.target.classList.add("mi-revealed");
                entry.target.classList.remove("mi-pre-reveal");
                observer.unobserve(entry.target);
            });
        }, { threshold: 0.14, rootMargin: "0px 0px -8% 0px" });

        targets.forEach((target) => observer.observe(target));
    }

    function initParallax() {
        const moon = document.querySelector(".mi-moon");
        const stage = document.querySelector(".mi-inn-art");
        if (reduceMotion || (!moon && !stage)) return;

        let ticking = false;
        const update = () => {
            const y = window.scrollY;
            if (moon) moon.style.translate = "0 " + Math.min(y * 0.06, 28) + "px";
            if (stage) stage.style.translate = "0 " + Math.min(y * -0.025, -14) + "px";
            ticking = false;
        };

        window.addEventListener("scroll", () => {
            if (ticking) return;
            window.requestAnimationFrame(update);
            ticking = true;
        }, { passive: true });
    }

    function initPortalTransition() {
        const links = document.querySelectorAll(".js-portal-link");
        if (!links.length) return;

        let overlay = document.querySelector(".mi-transition-overlay");
        if (!overlay) {
            overlay = document.createElement("div");
            overlay.className = "mi-transition-overlay";
            overlay.setAttribute("aria-hidden", "true");
            document.body.appendChild(overlay);
        }

        links.forEach((link) => {
            link.addEventListener("click", (event) => {
                const href = link.getAttribute("href");
                if (!href || href.startsWith("#") || link.target === "_blank" || reduceMotion) return;
                event.preventDefault();
                overlay.classList.add("is-active");
                window.setTimeout(() => {
                    window.location.href = href;
                }, 520);
            });
        });
    }

    document.addEventListener("DOMContentLoaded", () => {
        initNavbar();
        initReveal();
        initParallax();
        initPortalTransition();
    });
})();
