// assets/js/components.js

console.log("EStou sendo chamado!");

async function loadComponent(selector, templatePath) {
    try {
        const response = await fetch(templatePath);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const html = await response.text();
        
        const elements = document.querySelectorAll(selector);
        elements.forEach(element => {
            element.innerHTML = html;
        });

    } catch (error) {
        console.error(`Erro ao carregar componente ${selector} de ${templatePath}:`, error);
    }
}

// Quando o DOM estiver completamente carregado, injeta os componentes
document.addEventListener('DOMContentLoaded', () => {
    loadComponent('app-header', 'assets/templates/header.html'); 
});