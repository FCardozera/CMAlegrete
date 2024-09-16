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
        uploadBox.prepend(alertBox);
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

    fetch("/send-contract", {
        method: "post",
        body: formData,
    })
    .then(response => {
        if (response.ok) {
            // Mensagem de sucesso
            alertBox.className = 'alert alert-success';
            alertBox.textContent = 'Contrato enviado com sucesso! Seu contrato será analisado e retornaremos com a resposta em até 04 dias úteis!';
            uploadBox.prepend(alertBox);

            // Redirecionar para a página inicial após alguns segundos
            setTimeout(() => {
                window.location.href = "/";
            }, 2000);
        } else {
            throw new Error('Erro no envio do arquivo.');
        }
    })
    .catch((error) => {
        alertBox.className = 'alert alert-danger';
        alertBox.textContent = 'Algo deu errado! Verifique se o seu arquivo é formato .pdf de tamanho máximo 1MB e possui assinatura digital GOV válida e tente novamente.';
        uploadBox.prepend(alertBox);
    });
}
