import { test, expect } from '@playwright/test';

test('verify calendar views', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    await page.goto('http://localhost:3000/schedule');
    await page.waitForTimeout(1000);

    // Default: Month
    await expect(page.getByText('월간', { exact: true })).toHaveClass(/text-blue-600/);

    // Switch to Week
    await page.getByText('주간', { exact: true }).click();
    await page.waitForURL('**/schedule/week');
    await expect(page.getByText('주간', { exact: true })).toHaveClass(/text-blue-600/);

    // Check Week Title (Range)
    // E.g. "2026.1.4 - 1.10"
    await expect(page.locator('h2')).toContainText('-');

    // Switch to Day
    await page.getByText('일간', { exact: true }).click();
    await page.waitForURL('**/schedule/day');

    // Check Day Title
    // E.g. "2026.1.8 (목)"
    await expect(page.locator('h2')).toContainText('(');

    // Screenshot
    await page.screenshot({ path: 'calendar_views_verified.png' });
});
