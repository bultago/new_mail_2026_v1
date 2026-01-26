import { test, expect } from '@playwright/test';

test.describe('Mail Send Result Verification', () => {

    test.beforeEach(async ({ page }) => {
        // Login
        await page.goto('http://localhost:3000/login');
        await page.getByPlaceholder('아이디를 입력하세요').fill('user');
        await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
        await page.getByRole('button', { name: '로그인' }).click();
        await page.waitForURL('**/mail/list/inbox');
    });

    test('verify general send success', async ({ page }) => {
        await page.goto('http://localhost:3000/mail/write');

        // Fill Form
        await page.getByRole('button', { name: '주소록추가' }).waitFor(); // Wait for load

        // Input To
        await page.locator('.grid input[type="text"]').first().fill('recipient@sogang.ac.kr');

        // Subject
        await page.getByPlaceholder('제목을 입력하세요').fill('General Send Success');

        // Click Send
        await page.getByRole('button', { name: '보내기' }).first().click();

        // Use waitForTimeout to capture loading state if needed, but for test stability allow it to finish
        // Verify Loading Overlay
        await expect(page.getByText('Sending Mail...')).toBeVisible();

        // Wait for redirection
        await page.waitForURL('**/mail/result?*');

        // Verify Content
        await expect(page.locator('h2')).toContainText('메일이 성공적으로 발송되었습니다');
        await expect(page.getByRole('button', { name: '받은편지함으로 이동' })).toBeVisible();

        // Screenshot
        await page.screenshot({ path: 'result_success.png' });
    });

    test('verify send failure (mock)', async ({ page }) => {
        await page.goto('http://localhost:3000/mail/write');

        await page.locator('.grid input[type="text"]').first().fill('recipient@sogang.ac.kr');

        // Subject with "fail" to trigger mock failure
        await page.getByPlaceholder('제목을 입력하세요').fill('This will fail');

        await page.getByRole('button', { name: '보내기' }).first().click();

        await page.waitForURL('**/mail/result?*');

        // Verify Failure
        await expect(page.locator('h2')).toContainText('메일 발송에 실패했습니다');
        await expect(page.getByText('SMTP Connection Timeout')).toBeVisible();
        await expect(page.getByText('recipient@sogang.ac.kr')).toBeVisible(); // Failed rcpt list

        await page.screenshot({ path: 'result_fail.png' });
    });

    test('verify batch send success', async ({ page }) => {
        await page.goto('http://localhost:3000/mail/write');

        // 6 recipients
        const recipients = Array.from({ length: 6 }, (_, i) => `user${i}@test.com`).join(', ');

        await page.locator('.grid input[type="text"]').first().fill(recipients);

        await page.getByPlaceholder('제목을 입력하세요').fill('Batch Send Test');

        await page.getByRole('button', { name: '보내기' }).first().click();

        await page.waitForURL('**/mail/result?*');

        // Verify Batch Message
        await expect(page.locator('h2')).toContainText('배치 메일 발송이 접수되었습니다');

        await page.screenshot({ path: 'result_batch.png' });
    });

});
