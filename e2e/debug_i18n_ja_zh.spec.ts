import { test, expect } from '@playwright/test';
import fs from 'fs';

test('debug calendar i18n visuals ja/zh', async ({ page }) => {
    const logs: string[] = [];
    const log = (msg: string) => logs.push(msg);

    try {
        await page.goto('http://localhost:3000/login');
        await page.evaluate(() => localStorage.setItem('user-locale', 'ko')); // Start fresh

        await page.getByPlaceholder('아이디를 입력하세요').fill('user');
        await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
        await page.getByRole('button', { name: '로그인' }).click();
        await page.waitForURL('**/mail/list/inbox');

        await page.goto('http://localhost:3000/schedule');
        await page.waitForTimeout(2000);

        // 3. Switch to Japanese
        log('Switching to Japanese...');
        await page.locator('button').filter({ has: page.locator('svg.lucide-globe') }).click();
        await page.waitForTimeout(500);
        await page.getByText('日本語').click();
        await page.waitForTimeout(2000);

        const jaBtn = page.getByRole('button', { name: '予定登録' }); // "Add Schedule" in Ja
        const jaVisible = await jaBtn.isVisible();
        log(`JA Button Visible: ${jaVisible}`);
        if (!jaVisible) {
            const text = await page.locator('button.bg-blue-600').textContent();
            log(`JA Actual Text: ${text}`);
            // Also check header to see if it changed at all
            const header = await page.locator('.text-xl.font-bold').textContent();
            log(`JA Header Text: ${header}`);
        }

        // 4. Switch to Chinese (Simplified)
        log('Switching to Chinese (Simplified)...');
        await page.locator('button').filter({ has: page.locator('svg.lucide-globe') }).click();
        await page.waitForTimeout(500);
        await page.getByText('中文 (简体)').click();
        await page.waitForTimeout(2000);

        const zhBtn = page.getByRole('button', { name: '添加日程' }); // "Add Schedule" in Zh
        const zhVisible = await zhBtn.isVisible();
        log(`ZH Button Visible: ${zhVisible}`);
        if (!zhVisible) {
            const text = await page.locator('button.bg-blue-600').textContent();
            log(`ZH Actual Text: ${text}`);
        }

    } catch (e) {
        log(`Error: ${e}`);
    } finally {
        fs.writeFileSync('debug_log_ja_zh.txt', logs.join('\n'));
    }
});
