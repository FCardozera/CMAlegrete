const swiper = new Swiper('.swiper-container', {
    loop: true,
    speed: 1300,
    autoplay: {
        delay: 4000,
        disableOnInteraction: false,
    },
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
    }
});
