function sendContract(event) {
    event.preventDefault();
    console.log("validateForm function called");

    const inputFile = document.getElementById("formFile");

    if (inputFile === null) {
        alert("Por favor, efetue o envio de 01 arquivo válido (.pdf).");
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
    }).catch((error) => ("Algo deu errado!", error));
}
