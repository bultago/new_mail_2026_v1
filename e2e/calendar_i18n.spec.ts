import { test, expect } from '@playwright/test';

test('verify calendar i18n coverage & language switching', async ({ page }) => {
    // 1. Login
    await page.goto('http://localhost:3000/login');

    // Force Korean locale before login to ensure consistent UI
    await page.evaluate(() => localStorage.setItem('user-locale', 'ko'));
    await page.reload();

    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    // 2. Go to Schedule
    await page.goto('http://localhost:3000/schedule');
    await page.waitForTimeout(2000);

    // 3. Initial (Korean) Check
    await expect(page.getByRole('button', { name: '일정등록' })).toBeVisible();

    // 4. Switch to Japanese
    // Open Language Menu (Globe icon button)
    await page.locator('button').filter({ has: page.locator('svg.lucide-globe') }).click();
    await page.waitForTimeout(500); // Wait for dropdown
    await expect(page.getByText('日本語')).toBeVisible();
    await page.getByText('日本語').click();

    // Wait for update
    await page.waitForTimeout(1000);

    // Verify Japanese Text
    await expect(page.getByRole('button', { name: '予定登録' })).toBeVisible();
    await expect(page.getByText('スケジュール')).toBeVisible();

    // 5. Switch to Chinese (Simplified)
    await page.locator('button').filter({ has: page.locator('svg.lucide-globe') }).click();
    await page.waitForTimeout(500);
    await expect(page.getByText('中文 (简体)')).toBeVisible();
    await page.getByText('中文 (简体)').click();

    await page.waitForTimeout(1000);

    // Verify Chinese Text
    await expect(page.getByRole('button', { name: '添加日程' })).toBeVisible();
    await expect(page.getByText('日程管理')).toBeVisible();

    // 6. Switch to English (UK)
    await page.locator('button').filter({ has: page.locator('svg.lucide-globe') }).click();
    await page.waitForTimeout(500);
    await page.getByText('English (UK)').click();

    await page.waitForTimeout(1000);

    // Verify English Text
    await expect(page.getByRole('button', { name: 'Add Event' })).toBeVisible();

    // Screenshot
    await page.screenshot({ path: 'calendar_multilang_verified.png' });
});
