function toggleMenu() {
    const navMenu = document.getElementById("nav-menu");
    const overlay = document.querySelector('.menu-overlay');
    const body = document.querySelector("body");

    // Alternar o menu lateral e o overlay
    navMenu.classList.toggle("open");
    overlay.classList.toggle("active");

    if (navMenu.classList.contains("open")) {
        body.classList.add("no-scroll");
    } else {
        body.classList.remove("no-scroll");
    }
}