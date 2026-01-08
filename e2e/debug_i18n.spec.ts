import { test, expect } from '@playwright/test';
import fs from 'fs';

test('debug calendar i18n visuals', async ({ page }) => {
    const logs: string[] = [];
    const log = (msg: string) => logs.push(msg);

    try {
        await page.goto('http://localhost:3000/login');

        // Force KO
        await page.evaluate(() => localStorage.setItem('user-locale', 'ko'));

        await page.getByPlaceholder('아이디를 입력하세요').fill('user');
        await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
        await page.getByRole('button', { name: '로그인' }).click();
        await page.waitForURL('**/mail/list/inbox');

        await page.goto('http://localhost:3000/schedule');
        await page.waitForTimeout(2000);

        // 1. KO Check
        const koBtn = page.getByRole('button', { name: '일정등록' });
        const koVisible = await koBtn.isVisible();
        log(`KO Button Visible: ${koVisible}`);
        if (!koVisible) {
            // Try to find the button by class and get text
            const text = await page.locator('button.bg-blue-600').textContent();
            log(`KO Actual Text: ${text}`);
        }

        // 2. EN Check
        // Open Globe Menu
        await page.locator('button').filter({ has: page.locator('svg.lucide-globe') }).click();
        await page.waitForTimeout(500);
        await page.getByText('English (US)').click();
        await page.waitForTimeout(2000);

        const enBtn = page.getByRole('button', { name: 'Add Event' });
        const enVisible = await enBtn.isVisible();
        log(`EN Button Visible: ${enVisible}`);
        if (!enVisible) {
            const text = await page.locator('button.bg-blue-600').textContent();
            log(`EN Actual Text: ${text}`);
        }

    } catch (e) {
        log(`Error: ${e}`);
    } finally {
        fs.writeFileSync('debug_log.txt', logs.join('\n'));
    }
});
