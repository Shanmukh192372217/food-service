const { remote } = require('webdriverio');
const appiumConfig = require('../config/appiumConfig');

class MobileDriverFactory {
    static async createDriver() {
        const options = {
            hostname: appiumConfig.server.host,
            port: appiumConfig.server.port,
            capabilities: appiumConfig.capabilities,
            logLevel: 'error'
        };
        return await remote(options);
    }
}

module.exports = MobileDriverFactory;
