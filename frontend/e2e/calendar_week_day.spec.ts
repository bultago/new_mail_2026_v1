import { test, expect } from '@playwright/test';

test.describe('calendar week/day interactions', () => {

    test.beforeEach(async ({ page }) => {
        await page.goto('http://localhost:3000/login');
        await page.getByPlaceholder('아이디를 입력하세요').fill('user');
        await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
        await page.getByRole('button', { name: '로그인' }).click();
        await page.waitForURL('**/mail/list/inbox');
        await page.goto('http://localhost:3000/schedule');
        await page.waitForTimeout(1000);
    });

    test('week view double click and drag', async ({ page }) => {
        // Switch to Week View
        await page.getByRole('button', { name: '주간' }).click();

        // Wait for week view
        await expect(page.locator('text=0:00').first()).toBeVisible();

        // 1. Double Click (e.g., first day column, around 10:00)
        // 10:00 is around 10 * 48 = 480px down?
        // Let's use the day column directly.
        const firstDayCol = page.locator('.flex-1 .grid-cols-8 > div').nth(1); // 1st is time col

        // Emulate double click at specific position
        await firstDayCol.dblclick({ position: { x: 50, y: 100 } }); // ~02:00 ?

        // Verify Popup
        await expect(page.locator('.fixed.inset-0.z-50')).toBeVisible();
        await page.locator('button:has-text("취소")').click();


        // 2. Drag
        // Drag from y=200 to y=300
        await firstDayCol.hover({ position: { x: 50, y: 200 } });
        await page.mouse.down();
        await firstDayCol.hover({ position: { x: 50, y: 300 } });
        await page.mouse.up();

        // Verify Popup
        await expect(page.locator('.fixed.inset-0.z-50')).toBeVisible();
    });

    test('day view interactions', async ({ page }) => {
        // Switch to Day View
        await page.getByRole('button', { name: '일간' }).click();

        // Wait for day view
        const dayCol = page.locator('.flex-1 .grid-cols-\\[60px_1fr\\] > div').nth(1);

        // Double Click
        await dayCol.dblclick({ position: { x: 50, y: 100 } });
        await expect(page.locator('.fixed.inset-0.z-50')).toBeVisible();
        await page.locator('button:has-text("취소")').click();

        // Drag
        await dayCol.hover({ position: { x: 50, y: 200 } });
        await page.mouse.down();
        await dayCol.hover({ position: { x: 50, y: 300 } }); // 100px = ~ 1.5 hours (64px/hr)
        await page.mouse.up();

        await expect(page.locator('.fixed.inset-0.z-50')).toBeVisible();
    });
});
