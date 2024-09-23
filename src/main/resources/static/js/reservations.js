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

const modal = document.getElementById("imageModal");
const modalImage = document.getElementById("modalImage");
const closeBtn = document.getElementsByClassName("close")[0];

document.querySelectorAll('.swiper-slide img').forEach(img => {
    img.addEventListener('click', function () {
        modal.style.display = "flex";
        modalImage.src = this.src;
    });
});

closeBtn.onclick = function () {
    modal.style.display = "none";
};

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};
