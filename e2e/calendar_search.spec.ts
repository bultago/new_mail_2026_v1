import { test, expect } from '@playwright/test';

test('verify calendar search', async ({ page }) => {
    // Log console to help debug
    page.on('console', msg => console.log('PAGE LOG:', msg.text()));

    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    await page.goto('http://localhost:3000/schedule');
    await page.waitForTimeout(2000); // Wait for load

    // Type search
    const searchInput = page.getByPlaceholder('일정 검색...');
    await expect(searchInput).toBeVisible();
    await searchInput.fill('회의');
    await searchInput.press('Enter');

    console.log('Search submitted');

    // Wait for navigation
    // Try waiting for any change or specific element first
    await page.waitForTimeout(2000);
    console.log('Current URL:', page.url());

    // Check if URL changed
    if (!page.url().includes('/schedule/search')) {
        await page.screenshot({ path: 'search_nav_failed.png' });
        throw new Error('Navigation to search failed');
    }

    // Verify Result
    await expect(page.getByText('검색 결과:')).toBeVisible(); // Partial match
    await expect(page.getByText('주간회의')).toBeVisible();

    // Verify Click to Edit
    await page.getByText('주간회의').click();
    await expect(page.getByText('일정 수정')).toBeVisible();

    // Screenshot
    await page.screenshot({ path: 'calendar_search_verified.png' });
});
