const MobileBasePage = require('./MobileBasePage');

class MobileLoginPage extends MobileBasePage {
    constructor(driver) {
        super(driver);
        this.emailField = 'id=com.example.foodserviceapp:id/emailInput';
        this.passwordField = 'id=com.example.foodserviceapp:id/passwordInput';
        this.loginButton = 'id=com.example.foodserviceapp:id/loginBtn';
    }

    async login(email, password) {
        await this.type(this.emailField, email);
        await this.type(this.passwordField, password);
        await this.click(this.loginButton);
    }
}

module.exports = MobileLoginPage;
