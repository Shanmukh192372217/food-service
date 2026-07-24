require('dotenv').config();

module.exports = {
    capabilities: {
        platformName: 'Android',
        'appium:automationName': 'UiAutomator2',
        'appium:deviceName': process.env.DEVICE_NAME || 'Android Emulator',
        'appium:platformVersion': process.env.PLATFORM_VERSION || '13.0',
        'appium:app': process.env.APK_PATH || './app/app-debug.apk',
        'appium:appPackage': 'com.example.foodserviceapp',
        'appium:appActivity': 'com.example.foodserviceapp.MainActivity',
        'appium:noReset': false,
        'appium:fullReset': true,
        'appium:newCommandTimeout': 3600
    },
    server: {
        host: '127.0.0.1',
        port: 4723
    }
};
