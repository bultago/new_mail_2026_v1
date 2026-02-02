import { test, expect } from '@playwright/test';

test('Verify Responsive Layout', async ({ page }) => {
    // Capture console logs and errors
    page.on('console', msg => console.log('BROWSER LOG:', msg.text()));
    page.on('pageerror', exception => console.log('BROWSER ERROR:', exception));

    // 1. Set Viewport to Mobile
    await page.setViewportSize({ width: 375, height: 800 });

    const port = 3001; // New Port
    const baseURL = `http://localhost:${port}`;

    // 2. Mail Write
    console.log('Navigating to Mail Write...');
    await page.goto(`${baseURL}/mail/write`);
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: 'responsive_mail_write.png' });

    // 3. Mail List (Inbox)
    console.log('Navigating to Mail Inbox...');
    await page.goto(`${baseURL}/mail/list/inbox`);
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: 'responsive_mail_list.png' });

    // 4. Board List
    console.log('Navigating to Board List...');
    await page.goto(`${baseURL}/board`);
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: 'responsive_board_list.png' });

    // 5. Board Write (Simulate click)
    console.log('Navigating to Board Write...');
    // We need to click "Write" button in Board List to see Board Write
    // Selector based on BoardView.vue content (button with PenSquare icon or text '글쓰기')
    // Button variant="outline" ... handleWrite
    // Let's assume text "글쓰기" or "Write" is present or we can click the button.
    // Finding button by icon class is brittle, try text.
    // In BoardView.vue: {{ t('common.write') || '글쓰기' }}
    // We will try navigating/clicking. Since it's a state toggle, we must click.
    await page.getByRole('button', { name: /글쓰기|Write/i }).click();
    await page.waitForTimeout(500); // Wait for v-if toggle
    await page.screenshot({ path: 'responsive_board_write.png' });
    // Close write view to reset if needed? No, just move to next page.

    // 6. Address Book List
    console.log('Navigating to Address Book...');
    await page.goto(`${baseURL}/addr/list`);
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: 'responsive_address_book.png' });

    // 7. Settings
    console.log('Navigating to Settings...');
    await page.goto(`${baseURL}/settings`);
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: 'responsive_settings.png' });

});
