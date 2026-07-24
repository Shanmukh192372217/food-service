const DriverFactory = require('../utilities/DriverFactory');
const excelReport = require('../utilities/ExcelReport');
const logger = require('../utilities/Logger');

let driver;

before(async function() {
    driver = await DriverFactory.createDriver();
    this.driver = driver;
});

afterEach(async function() {
    const test = this.currentTest;
    const status = test.state === 'passed' ? 'PASS' : 'FAIL';

    excelReport.addTestCase({
        id: test.title.split(' ')[0],
        module: test.parent.title,
        name: test.title,
        status: status,
        duration: test.duration,
        failureReason: test.err ? test.err.message : ''
    });

    if (status === 'FAIL') {
        const screenshotName = `${test.title.replace(/\s/g, '_')}_failed`;
        const data = await this.driver.takeScreenshot();
        require('fs').writeFileSync(`./screenshots/${screenshotName}.png`, data, 'base64');
        logger.error(`Test Failed: ${test.title}. Screenshot saved.`);
    }
});

after(async function() {
    await excelReport.generate();
    if (this.driver) await this.driver.quit();
});
