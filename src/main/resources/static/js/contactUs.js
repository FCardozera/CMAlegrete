toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": true,
    "progressBar": true,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
};

function enviarMensagem(event) {
    event.preventDefault();
    console.log("validateForm function called");

    let email = document.getElementById("email").value;
    let name = document.getElementById("name").value;
    let subject = document.getElementById("subject").value;
    let message = document.getElementById("message").value;

    if (
        name === "" ||
        email === "" ||
        subject === "" ||
        message === ""
    ) {
        toastr.error("Por favor, preencha todos os campos para enviar a mensagem.");
        return false;
    }

    if (name.length < 3 || name.length > 80) {
        toastr.error("O nome completo deve ter entre 3 e 80 caracteres.");
        return false;
    }

    if (!email.includes("@") || !email.includes(".com")) {
        toastr.error("E-mail inválido!");
        return false;
    }

    const data = {
        email: email,
        name: name,
        subject: subject,
        message: message,
    };

    fetch("/fale-conosco", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then((response) => {
            if (response.ok) {
                toastr.success(name + " sua mensagem foi enviada com sucesso!");
                setTimeout(() => {
                    window.location.href = "/";
                }, 5000);
            } else {
                response.text().then(data => {
                    senhaError.textContent = data;
                });
            }
        })
        .catch((error) => {
            toastr.error("Ocorreu um erro ao enviar a mensagem!");
            console.error(error);
        });
}
