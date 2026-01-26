import { test, expect } from '@playwright/test';

test('verify calendar ui', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    await page.goto('http://localhost:3000/schedule');
    await page.waitForTimeout(3000);
    await page.screenshot({ path: 'calendar_debug_view.png' });

    // Sidebar
    await expect(page.getByRole('button', { name: '일정등록' })).toBeVisible();
    await expect(page.getByText('내 캘린더')).toBeVisible();

    // Month Header
    await expect(page.locator('h2', { hasText: '월' }).first()).toBeVisible();

    // Screenshot
    await page.screenshot({ path: 'calendar_ui_verified.png' });
});
