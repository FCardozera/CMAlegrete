document.getElementById('assistantCPF').addEventListener('input', function (e) {
    var value = e.target.value;
    var cpfPattern = value.replace(/\D/g, '')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1-$2')
        .replace(/(-\d{2})\d+?$/, '$1');
    e.target.value = cpfPattern;
});

document.getElementById('adminCPF').addEventListener('input', function (e) {
    var value = e.target.value;
    var cpfPattern = value.replace(/\D/g, '')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1-$2')
        .replace(/(-\d{2})\d+?$/, '$1');
    e.target.value = cpfPattern;
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

function registrarAdmin(event) {
    event.preventDefault();
    console.log("validateForm function called");

    const submitButton = event.target;
    let email = document.getElementById("adminEmail").value;
    let name = document.getElementById("adminName").value;
    let cpf = document.getElementById("adminCPF").value;
    let militaryOrganization = document.getElementById("adminMilitaryOrganization").value;

    name = capitalizeWords(name);

    if (
        name === "" ||
        cpf === "" ||
        email === ""
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

    if (!email.includes("@") || !email.includes(".com")) {
        toastr.error("E-mail inválido!");
        return false;
    }

    const data = {
        email: email,
        name: name,
        cpf: cpf,
        militaryOrganization: militaryOrganization,
    };

    submitButton.disabled = true;
    submitButton.textContent = 'ADICIONANDO...';

    fetch("/registrar/admin", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then((response) => {
            if (response.ok) {
                toastr.success("Administrador adicionado com sucesso!");
                submitButton.textContent = 'Adicionado!';
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
            submitButton.textContent = 'ADICIONAR';
        });
}

function registrarAssistente(event) {
    event.preventDefault();
    console.log("validateForm function called");

    const submitButton = event.target;
    let email = document.getElementById("assistantEmail").value;
    let name = document.getElementById("assistantName").value;
    let cpf = document.getElementById("assistantCPF").value;
    let militaryOrganization = document.getElementById("assistantMilitaryOrganization").value;

    name = capitalizeWords(name);

    if (
        name === "" ||
        cpf === "" ||
        email === ""
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

    if (!email.includes("@") || !email.includes(".com")) {
        toastr.error("E-mail inválido!");
        return false;
    }

    const data = {
        email: email,
        name: name,
        cpf: cpf,
        militaryOrganization: militaryOrganization,
    };

    submitButton.disabled = true;
    submitButton.textContent = 'ADICIONANDO...';

    fetch("/registrar/assistente", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then((response) => {
            if (response.ok) {
                toastr.success("Assistente adicionado com sucesso!");
                submitButton.textContent = 'Adicionado!';
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
            submitButton.textContent = 'ADICIONAR';
        });
}