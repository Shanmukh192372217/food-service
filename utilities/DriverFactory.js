const { Builder } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const firefox = require('selenium-webdriver/firefox');
const edge = require('selenium-webdriver/edge');
const config = require('../config/config');

class DriverFactory {
    static async createDriver() {
        let builder = new Builder().forBrowser(config.browser);

        if (config.browser === 'chrome') {
            let options = new chrome.Options();
            if (config.headless) options.addArguments('--headless');
            builder.setChromeOptions(options);
        } else if (config.browser === 'firefox') {
            let options = new firefox.Options();
            if (config.headless) options.addArguments('--headless');
            builder.setFirefoxOptions(options);
        }

        const driver = await builder.build();
        await driver.manage().setTimeouts({ implicit: config.timeout.implicit });
        await driver.manage().window().maximize();
        return driver;
    }
}

module.exports = DriverFactory;
