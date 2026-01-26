import { test, expect } from '@playwright/test';

test('calendar date picker navigation', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');
    await page.goto('http://localhost:3000/schedule');

    // Open Picker - Use a more specific selector strategy finding the button that contains a date pattern
    // The button text is like "2026.01"
    const pickerButton = page.locator('.relative > button.font-bold').filter({ hasText: /\d{4}\.\d{2}/ }).first();
    await pickerButton.click();

    // Check Initial Month View
    // The popup should be visible
    const popup = page.locator('.absolute.top-full');
    await expect(popup).toBeVisible();

    // Header check
    const headerYearSpan = page.getByTestId('picker-year');
    await expect(headerYearSpan).toContainText(String(new Date().getFullYear()));

    // Click Year to switch to Year Mode
    await headerYearSpan.click();

    // Verify Year Grid (12 items)
    // We can look for buttons with IDs
    const yearGrid = popup.locator('.grid.grid-cols-4');
    await expect(yearGrid.locator('button')).toHaveCount(12);

    // Select a different year (e.g., current year - 1)
    const currentYear = new Date().getFullYear();
    const targetYear = currentYear - 1;
    await page.getByTestId(`year-${targetYear}`).click();

    // Verify returning to Month View
    // Year header should now be targetYear
    await expect(headerYearSpan).toHaveText(String(targetYear));

    // Select Month (e.g., '1')
    // Re-locate grid for months (it changes structure/contents)
    const monthGrid = popup.locator('.grid.grid-cols-4');
    await monthGrid.getByRole('button', { name: '1', exact: true }).click();

    // Verify Picker Closed and Button Updated
    await expect(pickerButton).toHaveText(`${targetYear}.01`);
});
