console.log("addNews.js está sendo carregado!");

document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('.needs-validation');

    form.addEventListener('submit', (event) => {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
});