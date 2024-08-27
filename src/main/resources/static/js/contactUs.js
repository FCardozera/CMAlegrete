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
        alert("Por favor, preencha todos os campos para efetuar o cadastro.");
        return false;
    }

    if (name.length < 3 || name.length > 80) {
        alert("O nome completo deve ter entre 3 e 80 caracteres.");
        return false;
    }

    if (!email.includes("@") || !email.includes(".com")) {
        alert("E-mail inválido!");
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
                alert(name + " sua mensagem foi enviada com sucesso!");
                window.location.href = "/";
            } else {
                response.text().then(data => {
                    senhaError.textContent = data;
                });
            }
        })
        .catch((error) => {
            alert("Ocorreu um erro ao enviar a mensagem!");
            console.error(error);
        });
}
