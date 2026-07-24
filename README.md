# Food Service E2E Automation Framework

This is a production-ready Selenium WebDriver framework built with Node.js and Mocha.

## Features
- **POM Architecture**: Organized Page Object Model for scalability.
- **Excel Reporting**: Automatic generation of `E2E_Report.xlsx` with pass/fail styling.
- **CI/CD Integrated**: Fully configured with GitHub Actions.
- **Data Driven**: Supports hundreds of test scenarios dynamically.
- **Automatic Failure Handling**: Captures screenshots and logs on every failure.

## Prerequisites
- Node.js (v16+)
- Google Chrome / Edge / Firefox

## Setup
1. Clone the repository.
2. Install dependencies:
   ```bash
   npm install
   ```

## Execution
- **Run all tests (Headed):**
  ```bash
  npm test
  ```
- **Run in Headless mode (CI):**
  ```bash
  npm run test:headless
  ```

## Reports
- **HTML:** Check `reports/E2E_HTML_Report.html`
- **Excel:** Check `excel/E2E_Report.xlsx`
- **Logs:** Check `logs/execution.log`
