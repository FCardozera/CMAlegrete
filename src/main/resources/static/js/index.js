const swiper = new Swiper('.swiper-container', {
    loop: true,
    speed: 1300, // Velocidade da transição (1.3 segundo)
    autoplay: {
        delay: 4000, // Autoplay a cada 4 segundos
        disableOnInteraction: false, // Continua mesmo após interação
    },
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
    }
});
