import { test, expect } from '@playwright/test';

test('verify address popup i18n and persistence', async ({ page }) => {
    // 1. Login
    await page.goto('http://localhost:3000/login');
    await page.getByPlaceholder('아이디를 입력하세요').fill('user');
    await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
    await page.getByRole('button', { name: '로그인' }).click();
    await page.waitForURL('**/mail/list/inbox');

    // 2. Navigate to Write Page
    await page.goto('http://localhost:3000/mail/write');

    // Helper to open language menu
    const langMenuBtn = page.locator('button').filter({ has: page.locator('.lucide-globe') });

    // --- English Verification ---
    // Switch to English
    await langMenuBtn.click();
    await page.getByText('English (US)').click();

    // Verify Main Page switched
    await expect(page.getByRole('button', { name: 'Address Book' })).toBeVisible();

    // Open Popup
    const [popupEn] = await Promise.all([
        page.waitForEvent('popup'),
        page.getByRole('button', { name: 'Address Book' }).click()
    ]);
    await popupEn.waitForLoadState('load');
    await popupEn.waitForLoadState('networkidle'); // Ensure all network requests completed

    // Wait extra time for rendering/hydration
    await popupEn.waitForTimeout(5000);

    // Debugging: Take screenshot and log content
    await popupEn.screenshot({ path: 'popup_en_debug.png', fullPage: true });

    const content = await popupEn.content();
    console.log('Popup Content Dump:', content.substring(0, 500) + '...'); // Log partial content to avoid huge output

    const localeInStorage = await popupEn.evaluate(() => localStorage.getItem('user-locale'));
    console.log('Popup LocalStorage:', localeInStorage);

    // Wait for Vue hydration
    await popupEn.waitForTimeout(2000);
    console.log('Popup URL:', popupEn.url());

    // Diagnostic assertions
    // Check if 'Cancel' is visible (We saw it in HTML dump)
    // await expect(popupEn.getByRole('button', { name: 'Cancel' })).toBeVisible();

    // Dump body text
    const bodyText = await popupEn.innerText('body');
    console.log('Popup Body Text:', bodyText);

    // Check key fallback visibility if translation failed
    const hasKey = await popupEn.getByText('address_popup.tree.sogang').count() > 0;
    if (hasKey) console.log('Found raw key in DOM!');

    // Verify Popup Content (English)
    // Check unique content first to confirm correct page loaded
    await expect(popupEn.getByText('Sogang University')).toBeVisible(); // Tree
    await expect(popupEn.getByText('Address Book').first()).toBeVisible();
    await expect(popupEn.getByText('Inc. Position')).toBeVisible(); // Filter
    await expect(popupEn.getByRole('button', { name: 'Add Group' }).first()).toBeVisible(); // Action

    await popupEn.close();

    // --- Japanese Verification ---
    // Switch to Japanese
    await langMenuBtn.click();
    await page.getByText('日本語').click();

    // Verify Main Page switched
    await expect(page.getByRole('button', { name: 'アドレス帳' })).toBeVisible();

    // Open Popup
    const [popupJa] = await Promise.all([
        page.waitForEvent('popup'),
        page.getByRole('button', { name: 'アドレス帳' }).click()
    ]);
    await popupJa.waitForLoadState();

    // Verify Popup Content (Japanese)
    await expect(popupJa.getByText('アドレス帳').first()).toBeVisible();
    await expect(popupJa.getByText('西江大学')).toBeVisible(); // Tree
    await expect(popupJa.getByText('役職含む')).toBeVisible(); // Filter

    await popupJa.close();

    // --- Chinese Verification ---
    // Switch to Chinese
    await langMenuBtn.click();
    await page.getByText('中文 (简体)').click();

    // Verify Main Page switched
    // Note: '通讯录' might be used in header, but button is '通讯录'?
    // ko.json: "address_book": "주소찾기" (Common) vs "address_book": "주소록" (Header)
    // zh-CN.json: "address_book": "通讯录" (Common) vs "address_book": "通讯录" (Header)
    // So name is consistent.
    await expect(page.getByRole('button', { name: '通讯录' })).toBeVisible();

    // Open Popup
    const [popupZh] = await Promise.all([
        page.waitForEvent('popup'),
        page.getByRole('button', { name: '通讯录' }).click()
    ]);
    await popupZh.waitForLoadState();

    // Verify Popup Content (Chinese)
    await expect(popupZh.getByText('通讯录').first()).toBeVisible();
    await expect(popupZh.getByText('西江大学')).toBeVisible(); // Tree
    await expect(popupZh.getByText('包含职位')).toBeVisible(); // Filter

    await popupZh.close();
});
