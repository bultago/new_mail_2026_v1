import { test, expect } from '@playwright/test';

test('verify calendar shell', async ({ page }) => {
    // Login
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    // Goto Calendar
    await page.goto('http://localhost:3000/calendar');

    // Verify Shell Elements
    await expect(page.getByText('Calendar Sidebar')).toBeVisible();
    await expect(page.getByText('2026 January')).toBeVisible();
    await expect(page.getByText('Calendar Grid Placeholder')).toBeVisible();

    // Screenshot
    await page.screenshot({ path: 'calendar_shell_verified.png' });
});
