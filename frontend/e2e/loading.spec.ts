import { test, expect } from '@playwright/test';

test.describe('Global Loading Screen Verification', () => {

    test('verify login loading screen', async ({ page }) => {
        // 1. Go to Login Page
        await page.goto('/login');
        await page.waitForLoadState('networkidle');

        // 2. Fill Login Form
        await page.getByLabel('아이디').fill('testuser');
        await page.getByLabel('비밀번호').fill('password');

        // 3. Click Login
        await page.getByRole('button', { name: '로그인' }).click();

        // 4. Verify Loading Screen
        // It should appear immediately
        const loadingOverlay = page.locator('.absolute.inset-0.z-50.flex.flex-col');
        await expect(loadingOverlay).toBeVisible();
        await expect(page.getByText('Logging in...')).toBeVisible();

        // Verify Skeleton exists (by checking for a pulse element inside)
        await expect(loadingOverlay.locator('.animate-pulse').first()).toBeVisible();

        // Capture screenshot of the new Loading Screen
        await page.screenshot({ path: 'login_loading.png' });

        // 5. Verify Redirection happens (after 2s)
        await expect(page).toHaveURL(/\/mail\/list\/inbox/, { timeout: 10000 });
    });

});

