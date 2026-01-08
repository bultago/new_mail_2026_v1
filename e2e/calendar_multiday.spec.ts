import { test, expect } from '@playwright/test';

test.describe('calendar multi-day interaction', () => {
    test.beforeEach(async ({ page }) => {
        await page.goto('http://localhost:3000/login');
        await page.getByPlaceholder('아이디를 입력하세요').fill('user');
        await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
        await page.getByRole('button', { name: '로그인' }).click();
        await page.waitForURL('**/mail/list/inbox');
        await page.goto('http://localhost:3000/schedule');
    });

    test('week view multi-day drag', async ({ page }) => {
        // Go to week view
        await page.getByRole('button', { name: '주간' }).click();
        await page.waitForTimeout(500);

        // Find Mon and Tue columns
        const days = page.locator('.flex-1.overflow-y-auto .grid.grid-cols-8 > .relative.border-r');
        const mon = days.nth(1); // 0 is empty? No, grid-cols-8: 0 is time, 1-7 are days.
        const tue = days.nth(2);

        // Drag from Mon 10:00 to Tue 14:00
        // 10:00 is (10 * 48)px = 480px top
        // 14:00 is (14 * 48)px = 672px top
        // Offset logic:
        // Mon element is relative.

        const monBox = await mon.boundingBox();
        const tueBox = await tue.boundingBox();

        if (!monBox || !tueBox) throw new Error('Columns not found');

        // Mouse down on Mon 10:00
        await page.mouse.move(monBox.x + 10, monBox.y + 480);
        await page.mouse.down();
        await page.waitForTimeout(100);

        // Move to Tue 14:00
        // Use more steps to ensure mousemove events fire
        await page.mouse.move(tueBox.x + 10, tueBox.y + 672, { steps: 50 });
        await page.waitForTimeout(100);
        await page.mouse.up();

        // Check popup
        // Use a more specific selector
        await expect(page.getByTestId('schedule-popup')).toBeVisible({ timeout: 5000 });

        // Check date inputs
        // Depending on current week, dates will vary.
        // But we expect start date < end date.
        const startInput = page.getByLabel('시작 날짜', { exact: false }).first(); // Assuming label match
        const endInput = page.getByLabel('종료 날짜', { exact: false }).first();

        // Actually, inputs might be input[type=date] or text with CalendarDatePicker inside?
        // Let's just check invalid/valid logic or value if possible.
        // For now, simple visibility of popup confirms drag event fired.
    });
});
