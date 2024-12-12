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

function sendContract(event) {
    event.preventDefault();

    const inputFile = document.getElementById("formFile");
    const alertBox = document.createElement('div');
    const uploadBox = document.querySelector('.upload-box');

    // Remover alertas anteriores
    document.querySelectorAll('.alert').forEach(alert => alert.remove());

    if (inputFile.files.length === 0) {
        alertBox.className = 'alert alert-danger';
        alertBox.textContent = 'Por favor, efetue o envio de um arquivo válido (.pdf).';
        uploadBox.append(alertBox);
        return false;
    }

    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
    });
    let token = params.token;

    const formData = new FormData();
    for (const file of inputFile.files) {
        formData.append("file", file);
    }
    formData.append("token", token);

    fetch("/enviar-contrato", {
        method: "post",
        body: formData,
    })
    .then(response => {
        if (response.ok) {
            // Mensagem de sucesso
            alertBox.className = 'alert alert-success';
            alertBox.textContent = 'Contrato enviado com sucesso! Seu contrato será analisado e retornaremos com a resposta em até 04 dias úteis!';
            uploadBox.append(alertBox);

            // Redirecionar para a página inicial após alguns segundos
            setTimeout(() => {
                toastr.success("Sua aplicação foi remetida com sucesso! E-mail enviado para: " + email);
                window.location.href = "/";
            }, 5000);
        } else {
            throw new Error('Erro no envio do arquivo.');
        }
    })
    .catch((error) => {
        alertBox.className = 'alert alert-danger';
        alertBox.textContent = 'Algo deu errado! Verifique se o seu arquivo é formato .pdf de tamanho máximo 1MB e possui assinatura digital GOV válida e tente novamente.';
        uploadBox.append(alertBox);
    });
}
