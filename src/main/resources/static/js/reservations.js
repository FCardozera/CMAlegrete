const swiper = new Swiper('.swiper-container', {
    loop: true,
    effect: 'fade', // Efeito fade
    fadeEffect: {
        crossFade: true,
    },
    speed: 1300, // Velocidade da transição (1.3 segundo)
    autoplay: {
        delay: 4000, // Autoplay a cada 4 segundos
        disableOnInteraction: false, // Continua mesmo após interação
    },
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
    },
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },
});
