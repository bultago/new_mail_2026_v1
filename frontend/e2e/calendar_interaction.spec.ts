import { test, expect } from '@playwright/test';

test.describe('calendar interactions', () => {

    test.beforeEach(async ({ page }) => {
        // Login and Go to Schedule
        await page.goto('http://localhost:3000/login');
        await page.getByPlaceholder('아이디를 입력하세요').fill('user');
        await page.getByPlaceholder('비밀번호를 입력하세요').fill('password');
        await page.getByRole('button', { name: '로그인' }).click();
        await page.waitForURL('**/mail/list/inbox');
        await page.goto('http://localhost:3000/schedule');
        await page.waitForTimeout(1000);
    });

    test('double click to add schedule', async ({ page }) => {
        // Target specific date (e.g., 15th)
        // Note: Using text="15" might match multiple if previous/next month days are shown, 
        // but current logic renders them with diff classes.
        // Let's use exact match or filter by current month class if possible.
        // Simplification: Just find the button/div with text "15" inside the grid.

        // We need to target the cell, not just the number text, because the listener is on the cell div.
        // The cell has the number inside it.
        const dayCell = page.locator('.grid-cols-7 > div', { hasText: /^15$/ }).first();

        // Double click
        await dayCell.dblclick();

        // Verify Popup
        const popup = page.locator('.fixed.inset-0.z-50');
        await expect(popup).toBeVisible();
        await expect(popup.getByText('일정 등록')).toBeVisible(); // or translated title

        // Verify Date is pre-filled
        // Input type date value format is YYYY-MM-DD
        // We need to know which year/month we are in. Default is 2026-01 (from mocks/previous tests)
        // If today is used, it might be different. 
        // But the cell "15" behaves relative to the current view.
        // Let's check if the input contains the select date.
        // We can check the value of the date input.
        const dateInput = popup.locator('input[type="date"]').first();
        // Just check that it has a value, or check exact if we know the context.
        // For now, let's assume it picked the clicked date.
        await expect(dateInput).not.toBeEmpty();
    });

    test('drag and select range', async ({ page }) => {
        // Target 16th and 18th
        const day16 = page.locator('.grid-cols-7 > div', { hasText: /^16$/ }).first();
        const day18 = page.locator('.grid-cols-7 > div', { hasText: /^18$/ }).first();

        // Drag from 16 to 18
        await day16.hover();
        await page.mouse.down();
        await day16.hover(); // move a bit
        await day18.hover();
        await page.mouse.up();

        // Verify Popup
        const popup = page.locator('.fixed.inset-0.z-50');
        await expect(popup).toBeVisible();

        // Verify Start and End Date
        const startDateInput = popup.locator('input[type="date"]').first();
        const endDateInput = popup.locator('input[type="date"]').nth(1);

        // We expect start < end
        const startVal = await startDateInput.inputValue();
        const endVal = await endDateInput.inputValue();

        expect(startVal).not.toEqual(endVal);
        expect(startVal < endVal).toBeTruthy();
    });
});
