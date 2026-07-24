class GestureUtils {
    constructor(driver) {
        this.driver = driver;
    }

    async tap(element) {
        await element.click();
    }

    async swipe(fromX, fromY, toX, toY) {
        await this.driver.performActions([{
            type: 'pointer',
            id: 'finger1',
            parameters: { pointerType: 'touch' },
            actions: [
                { type: 'pointerMove', duration: 0, x: fromX, y: fromY },
                { type: 'pointerDown', button: 0 },
                { type: 'pointerMove', duration: 600, x: toX, y: toY },
                { type: 'pointerUp', button: 0 }
            ]
        }]);
    }

    async scrollUntilVisible(locator) {
        // Implementation of scroll until element is visible
        let isVisible = false;
        while (!isVisible) {
            try {
                const el = await this.driver.$(locator);
                if (await el.isDisplayed()) isVisible = true;
            } catch (e) {
                await this.swipe(500, 800, 500, 200); // Swipe up
            }
        }
    }
}

module.exports = GestureUtils;
