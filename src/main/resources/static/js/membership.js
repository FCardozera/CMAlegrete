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

function enviarAplicacao(event) {
    event.preventDefault();
    console.log("validateForm function called");

    let email = document.getElementById("email").value;
    let name = document.getElementById("name").value;
    let cpf = document.getElementById("cpf").value;
    let rg = document.getElementById("rg").value;
    let phoneNumber = document.getElementById("phoneNumber").value;
    let address = document.getElementById("address").value;
    let militaryOrganization = document.getElementById("militaryOrganization").value;
    // cpf = cpf.replace(/\D/g, "");
    phoneNumber = phoneNumber.replace(/\D/g, "");

    if (
        name === "" ||
        cpf === "" ||
        rg === "" ||
        email === "" ||
        phoneNumber === ""
    ) {
        alert("Por favor, preencha todos os campos para efetuar o cadastro.");
        return false;
    }

    if (name.length < 3 || name.length > 80) {
        alert("O nome completo deve ter entre 3 e 80 caracteres.");
        return false;
    }

    if (cpf.length !== 14) {
        alert("O campo de cpf deve conter 14 dígitos.");
        return false;
    }

    if (rg.length !== 10) {
        alert("O campo de cpf deve conter 10 dígitos.");
        return false;
    }

    if (!validacpf(cpf)) {
        event.preventDefault();
        alert('cpf inválido. Verifique o número digitado.');
        document.getElementById('cpf').focus();
    }

    if (phoneNumber.length !== 11) {
        alert("O campo Telefone deve conter 11 dígitos.");
        return false;
    }

    if (!email.includes("@") || !email.includes(".com")) {
        alert("E-mail inválido!");
        return false;
    }

    const data = {
        email: email,
        name: name,
        cpf: cpf,
        rg: rg,
        phoneNumber: phoneNumber,
        address: address,
        militaryOrganization: militaryOrganization,
    };

    fetch("/associe-se", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then((response) => {
            if (response.ok) {
                alert("Sua aplicação foi remetida com sucesso!" + "\n" + "E-mail enviado para: " + email);
                window.location.href = "/";
            }
        })
        .catch((error) => {
            alert("Ocorreu um erro ao cadastrar!");
            console.error(error);
        });
}
