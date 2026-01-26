import { test, expect } from '@playwright/test';

test('verify multi-language support', async ({ page }) => {
    // Login
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    // Go to Write Page
    await page.goto('http://localhost:3000/mail/write');

    // Helper to open language menu (Globe icon)
    // Using a robust selector that finds the button containing the Globe SVG/icon
    const langMenuBtn = page.locator('button').filter({ has: page.locator('.lucide-globe') });

    // 1. Switch to English (US)
    await langMenuBtn.click();
    await page.getByText('English (US)').click();

    // Verify English
    await expect(page.locator('h2')).toContainText('Compose Mail'); // Title Check
    await expect(page.getByText('To', { exact: true })).toBeVisible();
    await expect(page.getByText('Subject', { exact: true })).toBeVisible();
    await expect(page.getByRole('button', { name: 'Send' })).toBeVisible();

    // Verify Dropdowns (Eng)
    await expect(page.getByRole('option', { name: 'Recent Mails' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: 'Auto-save Off' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: 'Default Signature' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: 'Unicode (UTF-8)' })).toHaveCount(1);

    await page.screenshot({ path: 'multilang_en.png' });
    await page.waitForTimeout(1000);

    // 2. Switch to Japanese
    await langMenuBtn.click();
    await page.getByText('日本語').click();

    // Verify Japanese
    await expect(page.locator('h2')).toContainText('メール作成'); // Title Check
    await expect(page.getByText('宛先', { exact: true })).toBeVisible();
    await expect(page.getByText('件名', { exact: true })).toBeVisible();
    await expect(page.getByRole('button', { name: '送信' })).toBeVisible();

    // Verify Dropdowns (Jp)
    await expect(page.getByRole('option', { name: '最近送信したメール' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: '自動保存なし' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: '基本署名' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: 'Unicode (UTF-8)' })).toHaveCount(1);

    await page.screenshot({ path: 'multilang_ja.png' });
    await page.waitForTimeout(1000);

    // 3. Switch to Chinese (Simplified)
    await langMenuBtn.click();
    await page.getByText('中文 (简体)').click();

    // Verify Chinese
    await expect(page.locator('h2')).toContainText('撰写邮件'); // Title Check
    await expect(page.getByText('收件人', { exact: true })).toBeVisible();
    await expect(page.getByText('主题', { exact: true })).toBeVisible();
    await expect(page.getByRole('button', { name: '发送' })).toBeVisible();

    // Verify Dropdowns (Zh)
    await expect(page.getByRole('option', { name: '最近发送的邮件' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: '不自动保存' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: '默认签名' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: 'Unicode (UTF-8)' })).toHaveCount(1);

    // 4. Switch to Chinese (Traditional)
    await langMenuBtn.click();
    await page.getByText('中文 (繁體)').click();

    // Verify Traditional Chinese
    await expect(page.locator('h2')).toContainText('撰寫郵件'); // Title Check
    await expect(page.getByText('收件者', { exact: true })).toBeVisible();
    await expect(page.getByText('主旨', { exact: true })).toBeVisible();
    await expect(page.getByRole('button', { name: '傳送' })).toBeVisible();

    // Verify Dropdowns (Zh-TW)
    await expect(page.getByRole('option', { name: '最近傳送的郵件' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: '不自動儲存' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: '預設簽名' })).toHaveCount(1);
    await expect(page.getByRole('option', { name: 'Unicode (UTF-8)' })).toHaveCount(1);

    await page.screenshot({ path: 'multilang_zh_tw.png' });

    // Final pause for user visibility
    await page.waitForTimeout(5000);
});
