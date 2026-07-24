$files = @(
    "Selenium_300_Test_Report_Final.xls",
    "Appium_300_Test_Report.xls",
    "Unit_API_300_Test_Report.xls",
    "Validation_300_Test_Report.xls",
    "Performance_300_Test_Report.xls",
    "Security_300_Test_Report.xls"
)

$allRows = ""

foreach ($file in $files) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw
        if ($content -match "<tbody>([\s\S]*?)</tbody>") {
            $allRows += $matches[1]
        }
    }
}

$finalHtml = @"
<html>
<head>
<meta charset="utf-8">
<style>
    table { border-collapse: collapse; width: 100%; font-family: 'Segoe UI', sans-serif; }
    th { background-color: #0070C0; color: white; border: 1px solid #333; padding: 10px; text-align: left; font-size: 14px; }
    td { border: 1px solid #ccc; padding: 8px; font-size: 12px; }
    .pass { background-color: #C6EFCE; color: #006100; font-weight: bold; text-align: center; }
</style>
</head>
<body>
    <h2>Master E2E Testing Report - Unified 1800 Test Cases</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Test ID</th><th>Module/Category</th><th>Suite</th><th>Description</th><th>Expected Outcome</th><th>Status</th><th>Duration</th><th>Comments</th>
            </tr>
        </thead>
        <tbody>
            $allRows
        </tbody>
    </table>
</body>
</html>
"@

$finalHtml | Out-File "Master_1800_Test_Report.xls" -Encoding utf8
Write-Host "Master report generated successfully."
