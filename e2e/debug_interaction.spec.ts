import { test, expect } from '@playwright/test';

test('debug week view interaction', async ({ page }) => {
    // Capture console logs
    page.on('console', msg => console.log('PAGE LOG:', msg.text()));

    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');
    await page.goto('http://localhost:3000/schedule');
    await page.waitForTimeout(1000);

    await page.getByRole('button', { name: '주간' }).click();
    await page.waitForTimeout(500);

    const days = page.locator('.flex-1.overflow-y-auto .grid.grid-cols-8 > .relative.border-r');
    const mon = days.nth(1);

    // Check bounding box
    const box = await mon.boundingBox();
    console.log('Monday Box:', box);

    if (box) {
        console.log('Clicking at:', box.x + 20, box.y + 100);
        await page.mouse.move(box.x + 20, box.y + 100);
        await page.mouse.down();
        await page.waitForTimeout(100); // Wait bit
        await page.mouse.up();
    }

    await page.waitForTimeout(1000);
});
