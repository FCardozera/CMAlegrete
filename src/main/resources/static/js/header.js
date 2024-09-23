function toggleMenu() {
    const navMenu = document.getElementById("nav-menu");
    const overlay = document.querySelector('.menu-overlay');

    // Alternar o menu lateral e o overlay
    navMenu.classList.toggle("open");
    overlay.classList.toggle("active");
}