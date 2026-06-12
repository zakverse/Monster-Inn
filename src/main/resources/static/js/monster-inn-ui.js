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
