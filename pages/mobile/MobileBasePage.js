const logger = require('../../utilities/Logger');

class MobileBasePage {
    constructor(driver) {
        this.driver = driver;
    }

    async waitForElement(locator, timeout = 15000) {
        const el = await this.driver.$(locator);
        await el.waitForDisplayed({ timeout });
        return el;
    }

    async click(locator) {
        logger.info(`Mobile: Clicking on ${locator}`);
        const el = await this.waitForElement(locator);
        await el.click();
    }

    async type(locator, text) {
        logger.info(`Mobile: Typing "${text}" into ${locator}`);
        const el = await this.waitForElement(locator);
        await el.setValue(text);
    }

    async getText(locator) {
        const el = await this.waitForElement(locator);
        return await el.getText();
    }
}

module.exports = MobileBasePage;
