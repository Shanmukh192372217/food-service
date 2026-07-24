const { By } = require('selenium-webdriver');
const BasePage = require('../utilities/BasePage');

class LoginPage extends BasePage {
    constructor(driver) {
        super(driver);
        this.emailInput = By.id('emailInput');
        this.passwordInput = By.id('passwordInput');
        this.loginBtn = By.id('loginBtn');
        this.errorMessage = By.id('errorMessage');
    }

    async login(email, password) {
        await this.type(this.emailInput, email);
        await this.type(this.passwordInput, password);
        await this.click(this.loginBtn);
    }

    async getError() {
        return await this.getText(this.errorMessage);
    }
}

module.exports = LoginPage;
