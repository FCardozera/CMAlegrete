document.getElementById('cpf').addEventListener('input', function (e) {
    var value = e.target.value;
    var cpfPattern = value.replace(/\D/g, '')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1-$2')
        .replace(/(-\d{2})\d+?$/, '$1');
    e.target.value = cpfPattern;
});

document.getElementById('phoneNumber').addEventListener('input', function (e) {
    var value = e.target.value;
    var phonePattern = value.replace(/\D/g, '')
        .replace(/(\d{2})(\d)/, '($1) $2')
        .replace(/(\d{5})(\d)/, '$1-$2')
        .replace(/(-\d{4})\d+?$/, '$1');
    e.target.value = phonePattern;
});

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

function formReenviarEmail(event) {
    const name = document.getElementById("name-div");
    const cpf = document.getElementById("cpf-div");
    const phoneNumber = document.getElementById("phoneNumber-div");
    const address = document.getElementById("address-div");
    const militaryOrganization = document.getElementById("militaryOrganization-div");
    const info = document.getElementById("info-div");
    const buttonEnviarAplicacao = document.getElementById("button-enviar-aplicacao");
    const buttonReenviarEmail = document.getElementById("button-reenviar-email");
    const formReenviarEmail = document.getElementById("form-reenviar-email");

    name.style.display = "none";
    cpf.style.display = "none";
    phoneNumber.style.display = "none";
    address.style.display = "none";
    militaryOrganization.style.display = "none";
    info.style.display = "none";
    formReenviarEmail.style.display = "none";
    buttonEnviarAplicacao.style.display = "none";
    buttonReenviarEmail.style.display = "block";
}

function validacpf(cpf) {
    cpf = cpf.replace(/\D+/g, '');
    if (cpf.length !== 11) return false;

    let soma = 0;
    let resto;
    if (/^(\d)\1{10}$/.test(cpf)) return false;

    for (let i = 1; i <= 9; i++) soma += parseInt(cpf.substring(i - 1, i)) * (11 - i);
    resto = (soma * 10) % 11;
    if ((resto === 10) || (resto === 11)) resto = 0;
    if (resto !== parseInt(cpf.substring(9, 10))) return false;

    soma = 0;
    for (let i = 1; i <= 10; i++) soma += parseInt(cpf.substring(i - 1, i)) * (12 - i);
    resto = (soma * 10) % 11;
    if ((resto === 10) || (resto === 11)) resto = 0;
    if (resto !== parseInt(cpf.substring(10, 11))) return false;

    return true;
}

function capitalizeWords(input) {
    return input
        .toLowerCase()
        .replace(/\b\w/g, (letter) => letter.toUpperCase());
}

function enviarAplicacao(event) {
    event.preventDefault();
    console.log("validateForm function called");

    const submitButton = event.target;
    let email = document.getElementById("email").value;
    let name = document.getElementById("name").value;
    let cpf = document.getElementById("cpf").value;
    let phoneNumber = document.getElementById("phoneNumber").value;
    let address = document.getElementById("address").value;
    let militaryOrganization = document.getElementById("militaryOrganization").value;
    phoneNumber = phoneNumber.replace(/\D/g, "");

    name = capitalizeWords(name);
    address = capitalizeWords(address);

    if (
        name === "" ||
        cpf === "" ||
        email === "" ||
        phoneNumber === ""
    ) {
        toastr.error("Por favor, preencha todos os campos para efetuar o cadastro.");
        return false;
    }

    if (name.length < 3 || name.length > 80) {
        toastr.error("O nome completo deve ter entre 3 e 80 caracteres.");
        return false;
    }

    if (cpf.length !== 14) {
        toastr.error("O campo de CPF deve conter 14 dígitos.");
        return false;
    }

    if (!validacpf(cpf)) {
        toastr.error("CPF inválido. Verifique o número digitado.");
        document.getElementById('cpf').focus();
        return false;
    }

    if (phoneNumber.length !== 11) {
        toastr.error("O campo Telefone deve conter 11 dígitos.");
        return false;
    }

    if (!email.includes("@") || !email.includes(".com")) {
        toastr.error("E-mail inválido!");
        return false;
    }

    const data = {
        email: email,
        name: name,
        cpf: cpf,
        phoneNumber: phoneNumber,
        address: address,
        militaryOrganization: militaryOrganization,
    };

    submitButton.disabled = true;
    submitButton.textContent = 'Enviando...';

    fetch("/associe", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then((response) => {
            if (response.ok) {
                toastr.success("Sua aplicação foi remetida com sucesso! E-mail enviado para: " + email);
                submitButton.textContent = 'Aplicação Enviada!';
                setTimeout(() => {
                    window.location.href = "/";
                }, 5000);
            } else {
                toastr.error("Ocorreu um erro ao cadastrar.");
            }
        })
        .catch((error) => {
            toastr.error("Erro ao processar sua solicitação. Tente novamente.");
            console.error(error);
            submitButton.disabled = false;
            submitButton.textContent = 'Enviar Aplicação';
        });
}

function reenviarEmail(event) {
    event.preventDefault();
    console.log("validateForm function called");

    const submitButton = event.target;
    let email = document.getElementById("email").value;

    if (email === "") {
        toastr.error("Por favor, preencha todos os campos para efetuar o cadastro.");
        return false;
    }

    if (!email.includes("@") || !email.includes(".com")) {
        toastr.error("E-mail inválido!");
        return false;
    }

    const data = {
        email: email
    };

    submitButton.disabled = true;
    submitButton.textContent = 'Reenviando...';

    fetch("/associe/reenviarEmail", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then((response) => {
            if (response.ok) {
                toastr.success("Sua aplicação foi remetida com sucesso! E-mail reenviado para: " + email);
                submitButton.textContent = 'E-mail reenviado!';
                setTimeout(() => {
                    window.location.href = "/";
                }, 5000);
            } else {
                return response.text().then((errorText) => {
                    toastr.error("Erro: " + errorText);
                });
            }
        })
        .catch((error) => {
            toastr.error("Ocorreu um erro inesperado, tente novamente mais tarde!");
            submitButton.disabled = true;
            submitButton.textContent = 'Reenviar E-mail de Confirmação';
            console.error(error);
        });
}

