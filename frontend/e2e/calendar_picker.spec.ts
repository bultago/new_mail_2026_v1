import { test, expect } from '@playwright/test';

test('verify calendar date picker interaction', async ({ page }) => {
    // 1. Login
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    // 2. Go to Schedule
    await page.goto('http://localhost:3000/schedule');
    await page.waitForTimeout(1000);

    // 3. Test Picker in Main View
    // Target the main header picker specifically via TestID
    // Use regex to match any YYYY.MM pattern
    const mainHeaderPicker = page.getByTestId('calendar-main-header').getByRole('button', { name: /^\d{4}\.\d{2}$/ });
    await expect(mainHeaderPicker).toBeVisible();

    // Store the text to verify it later? No, just click it.
    await mainHeaderPicker.click();

    // Verify Popup
    const popup = page.locator('.absolute.top-full'); // Naive selector for now, or add test-id
    await expect(popup).toBeVisible();
    await expect(popup.getByText('2026')).toBeVisible();

    // Change Year to 2027
    await popup.locator('button').filter({ has: page.locator('svg.lucide-chevron-right') }).click();
    await expect(popup.getByText('2027')).toBeVisible();

    // Select Month (e.g., 5 = May)
    await popup.getByRole('button', { name: '5', exact: true }).click();

    // Verify Popup Closed
    await expect(popup).toBeHidden();

    // Verify Popup Closed
    await expect(popup).toBeHidden();

    // Verify Date Updated (2027.05)
    // We explicitly set it to 2027.05, so we can expect that.
    await expect(page.getByTestId('calendar-main-header').getByText('2027.05')).toBeVisible();

    // 4. Test Picker in Sidebar
    // It should also reflect the change
    const sidebarPicker = page.locator('.p-4').getByRole('button', { name: '2027.05' });
    await expect(sidebarPicker).toBeVisible();
});
