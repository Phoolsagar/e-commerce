document.addEventListener("DOMContentLoaded", () => {
  const toggle = document.querySelector("[data-menu-toggle]");
  const menu = document.querySelector("[data-menu]");

  if (toggle && menu) {
    toggle.addEventListener("click", () => {
      menu.classList.toggle("is-open");
      toggle.setAttribute("aria-expanded", menu.classList.contains("is-open"));
    });

    menu.querySelectorAll("a").forEach((link) => {
      link.addEventListener("click", () => {
        menu.classList.remove("is-open");
        toggle.setAttribute("aria-expanded", "false");
      });
    });
  }
});
