function toggleDescription(event) {
    const description = event.target.previousElementSibling;
    description.classList.toggle("active");

    if (description.classList.contains("active")) {
        event.target.innerHTML = "Exibir menos";
    } else {
        event.target.innerHTML = "Exibir mais...";
    }
}
