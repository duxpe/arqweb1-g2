// assets/js/addNews.js

console.log("addNews.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    // Pega o formulário pela tag (ou adicione um ID se tiver múltiplos formulários)
    const form = document.querySelector('.needs-validation');

    // Listener para o evento de submit do formulário
    form.addEventListener('submit', (event) => {
        // Se o formulário for inválido, previne o envio e para a propagação do evento
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        // Adiciona a classe 'was-validated' para exibir os feedbacks de validação
        form.classList.add('was-validated');
    }, false); // O 'false' indica que o listener é para a fase de "borbulhamento"
});