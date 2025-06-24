console.log("EStou sendo chamado!");
class AppFooter extends HTMLElement {
  connectedCallback() {
    fetch('assets/templates/footer.html')
      .then(res => res.text())
      .then(html => {
        this.innerHTML = html;
      });
  }
}
customElements.define('app-footer', AppFooter);

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
