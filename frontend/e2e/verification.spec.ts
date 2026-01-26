import { test, expect } from '@playwright/test';

test('verify application startup', async ({ page }) => {
    // Go to the starting url
    await page.goto('http://localhost:3000');

    // Expect the page to title "frontend"
    await expect(page).toHaveTitle(/frontend/);

    // Check if we are redirected to login or stay on main
    // Note: Adjust selector based on actual login page content
    // Assuming there is an input for login
    await expect(page.locator('input').first()).toBeVisible();

    // Take a screenshot
    await page.screenshot({ path: 'verification_screenshot.png' });
});
