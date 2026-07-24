const ExcelJS = require('exceljs');
const moment = require('moment');
const config = require('../config/config');

class ExcelReport {
    constructor() {
        this.workbook = new ExcelJS.Workbook();
        this.testCases = [];
    }

    addTestCase(data) {
        this.testCases.push({
            id: data.id,
            module: data.module,
            name: data.name,
            browser: config.browser,
            status: data.status,
            startTime: data.startTime,
            endTime: data.endTime,
            duration: data.duration,
            failureReason: data.failureReason || 'N/A'
        });
    }

    async generate() {
        const sheet = this.workbook.addWorksheet('Test Cases');
        sheet.columns = [
            { header: 'Test ID', key: 'id', width: 10 },
            { header: 'Module', key: 'module', width: 20 },
            { header: 'Scenario Name', key: 'name', width: 40 },
            { header: 'Browser', key: 'browser', width: 15 },
            { header: 'Status', key: 'status', width: 12 },
            { header: 'Duration (ms)', key: 'duration', width: 15 },
            { header: 'Failure Reason', key: 'failureReason', width: 50 }
        ];

        this.testCases.forEach(tc => {
            const row = sheet.addRow(tc);
            const statusCell = row.getCell('status');
            if (tc.status === 'PASS') {
                statusCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'C6EFCE' } };
            } else {
                statusCell.fill = { type: 'pattern', pattern: 'solid', fgColor: { argb: 'FFC7CE' } };
            }
        });

        await this.workbook.xlsx.writeFile(config.paths.excel);
    }
}

module.exports = new ExcelReport();
