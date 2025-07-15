console.log("Header sendo chamado!");

class AppHeader extends HTMLElement {
  connectedCallback() {
	fetch('assets/templates/header.html')
      .then(res => res.text())
      .then(html => {
        this.innerHTML = html;
      });
  }
}
customElements.define('app-header', AppHeader);
