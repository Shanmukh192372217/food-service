const { By } = require('selenium-webdriver');
const BasePage = require('../utilities/BasePage');

class DashboardPage extends BasePage {
    constructor(driver) {
        super(driver);
        this.welcomeHeading = By.xpath("//h2[contains(text(), 'Admin')]");
        this.addFoodBtn = By.id('addFoodBtn');
        this.logoutBtn = By.id('logoutBtn');
        this.foodListTable = By.id('foodTable');
    }

    async isAt() {
        return await this.isDisplayed(this.welcomeHeading);
    }

    async logout() {
        await this.click(this.logoutBtn);
    }
}

module.exports = DashboardPage;
