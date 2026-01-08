import { test, expect } from '@playwright/test';

test('verify mail write i18n', async ({ page }) => {
    // 1. Login
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();

    // 2. Wait for redirect to inbox
    await page.waitForURL('**/mail/list/inbox');

    // 3. Navigate to Mail Write
    // Option A: Click sidebar button
    // await page.getByRole('link', { name: '메일쓰기' }).click(); 
    // Option B: Direct navigation
    await page.goto('http://localhost:3000/mail/write');

    // 4. Verify I18n
    // "받는사람" label
    await expect(page.getByText('받는사람', { exact: true })).toBeVisible();

    // "제목" label
    await expect(page.getByText('제목', { exact: true })).toBeVisible();

    // "보내기" button
    await expect(page.getByRole('button', { name: '보내기' })).toBeVisible();

    // "내용" label
    await expect(page.getByText('내용', { exact: true })).toBeVisible();

    // Take screenshot for evidence
    await page.screenshot({ path: 'i18n_verification.png' });

    // Wait for 5 seconds so the user can see the browser
    await page.waitForTimeout(5000);
});
