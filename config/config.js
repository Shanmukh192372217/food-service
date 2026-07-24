require('dotenv').config();

module.exports = {
    baseUrl: process.env.BASE_URL || 'https://shanmukh192372217.github.io/food-service/web_dashboard.html',
    browser: process.env.BROWSER || 'chrome',
    headless: process.env.HEADLESS === 'true',
    timeout: {
        implicit: 10000,
        explicit: 15000
    },
    admin: {
        email: 'hoteladmin@gmail.com',
        password: 'AdminPassword123'
    },
    paths: {
        screenshots: './screenshots/',
        reports: './reports/',
        excel: './excel/E2E_Report.xlsx',
        logs: './logs/execution.log'
    }
};
