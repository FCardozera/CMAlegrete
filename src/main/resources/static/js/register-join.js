document.getElementById('cpf').addEventListener('input', function (e) {
    var value = e.target.value;
    var cpfPattern = value.replace(/\D/g, '') // Remove qualquer coisa que não seja número
        .replace(/(\d{3})(\d)/, '$1.$2') // Adiciona ponto após o terceiro dígito
        .replace(/(\d{3})(\d)/, '$1.$2') // Adiciona ponto após o sexto dígito
        .replace(/(\d{3})(\d)/, '$1-$2') // Adiciona traço após o nono dígito
        .replace(/(-\d{2})\d+?$/, '$1'); // Impede entrada de mais de 11 dígitos
    e.target.value = cpfPattern;
});

document.getElementById('telefone').addEventListener('input', function (e) {
    var value = e.target.value;
    // Remove qualquer caractere que não seja número
    var phonePattern = value.replace(/\D/g, '')
        .replace(/(\d{2})(\d)/, '($1) $2')     // Adiciona parênteses em torno dos dois primeiros dígitos
        .replace(/(\d{5})(\d)/, '$1-$2')       // Adiciona traço após o quinto dígito
        .replace(/(-\d{4})\d+?$/, '$1');       // Impede a entrada de mais de 11 dígitos
    e.target.value = phonePattern;
});

function validaCPF(cpf) {
    cpf = cpf.replace(/\D+/g, '');
    if (cpf.length !== 11) return false;

    let soma = 0;
    let resto;
    if (/^(\d)\1{10}$/.test(cpf)) return false; // Verifica sequências iguais

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

document.getElementById('form-associe').addEventListener('submit', function (e) {
    var cpf = document.getElementById('cpf').value;
    if (!validaCPF(cpf)) {
        e.preventDefault(); // Impede o envio do formulário
        alert('CPF inválido. Verifique o número digitado.');
        document.getElementById('cpf').focus(); // Foca no campo de CPF após o erro
    }
});