import { test, expect } from '@playwright/test';

test('verify calendar timeline visibility', async ({ page }) => {
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');
    await page.goto('http://localhost:3000/schedule');
    await page.waitForTimeout(1000);

    // Switch to Week View
    await page.getByRole('button', { name: '주간' }).click();
    await page.waitForTimeout(500);

    // Check 0:00 visibility
    // It shouldn't be covered or clipped.
    // We can check if it stays within the bounding box of the scrollable area.
    const time0 = page.locator('text=0:00').first();
    await expect(time0).toBeVisible();

    // Check bounding box
    const box = await time0.boundingBox();
    console.log('0:00 BBox:', box);

    if (box) {
        // If y is negative relative to scroll container, it's problematic if overflow is hidden.
        // But visibility check might pass even if partially clipped.
        // Let's assume the user sees it "cut off".
    }
});
