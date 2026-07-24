const { until, By } = require('selenium-webdriver');
const logger = require('./Logger');

class BasePage {
    constructor(driver) {
        this.driver = driver;
    }

    async open(url) {
        logger.info(`Opening URL: ${url}`);
        await this.driver.get(url);
    }

    async waitForElement(locator, timeout = 10000) {
        return await this.driver.wait(until.elementLocated(locator), timeout);
    }

    async click(locator) {
        logger.info(`Clicking on element: ${JSON.stringify(locator)}`);
        const el = await this.waitForElement(locator);
        await el.click();
    }

    async type(locator, text) {
        logger.info(`Typing "${text}" into element: ${JSON.stringify(locator)}`);
        const el = await this.waitForElement(locator);
        await el.sendKeys(text);
    }

    async getText(locator) {
        const el = await this.waitForElement(locator);
        return await el.getText();
    }

    async isDisplayed(locator) {
        try {
            const el = await this.waitForElement(locator, 5000);
            return await el.isDisplayed();
        } catch (e) {
            return false;
        }
    }

    async takeScreenshot(name) {
        const data = await this.driver.takeScreenshot();
        require('fs').writeFileSync(`./screenshots/${name}.png`, data, 'base64');
    }
}

module.exports = BasePage;
