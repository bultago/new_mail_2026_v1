import { test, expect } from '@playwright/test';

test('schedule drag and drop interaction', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');
    await page.goto('http://localhost:3000/schedule');

    // Go to Week View for testing
    await page.getByRole('button', { name: '주간' }).click();
    await expect(page).toHaveURL(/.*schedule-week/);

    // Navigate to 2026.01
    const pickerButton = page.locator('.relative > button.font-bold').filter({ hasText: /\d{4}\.\d{2}/ }).first();
    await pickerButton.click();

    // Switch to Year 2026
    const popup = page.locator('.absolute.top-full');
    await popup.locator('span.cursor-pointer').first().click();
    await popup.locator('.grid.grid-cols-4 button', { hasText: '1' }).first().click();

    // Verify "주간회의" exists (Jan 5 10:00)
    const schedule = page.getByText('주간회의').first();
    await expect(schedule).toBeVisible();

    // Get initial position for logging
    const initialBox = await schedule.boundingBox();
    console.log(`Initial Y: ${initialBox?.y}`);

    // Drag "주간회의" to the same column (Day Column)
    // Target the Body Grid Column (inside overflow-y-auto), index 1 (Sunday)
    const dayColumn = page.locator('.flex-1.overflow-y-auto .grid.grid-cols-8 > div').nth(1);

    // Drag schedule to the center of the day column (12:00)
    await schedule.dragTo(dayColumn);

    // Verify position changed
    // Initial was 10:00 = 480px.
    // DragTo moves center-to-center.
    // Source Center (480+24=504) -> Target Center (576).
    // Delta = +72px = 1.5 hours.
    // New Top = 480 + 72 = 552px (11:30).
    await expect(schedule).toHaveCSS('top', '552px');

    // Optional: Check logic
    const newBox = await schedule.boundingBox();
    console.log(`New Y: ${newBox?.y}`);
});
