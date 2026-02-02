import { chromium } from 'playwright';
import { mkdirSync } from 'fs';
import { join } from 'path';

const screenshotsDir = join(process.cwd(), 'screenshots');
mkdirSync(screenshotsDir, { recursive: true });

const pages = [
    { url: 'http://localhost:3001/login', name: 'login' },
    { url: 'http://localhost:3001/mail/list/inbox', name: 'mail-list-inbox' },
    { url: 'http://localhost:3001/mail/list/sent', name: 'mail-list-sent' },
    { url: 'http://localhost:3001/mail/list/drafts', name: 'mail-list-drafts' },
    { url: 'http://localhost:3001/mail/list/trash', name: 'mail-list-trash' },
    { url: 'http://localhost:3001/mail/write', name: 'mail-write' },
    { url: 'http://localhost:3001/mail/result', name: 'mail-result' },
    { url: 'http://localhost:3001/mail/read/1', name: 'mail-read' },
    { url: 'http://localhost:3001/mail/receipt', name: 'mail-receipt' },
    { url: 'http://localhost:3001/mail/receipt/1', name: 'mail-receipt-detail' },
    { url: 'http://localhost:3001/addr/list', name: 'address-book' },
    { url: 'http://localhost:3001/schedule', name: 'calendar-month' },
    { url: 'http://localhost:3001/schedule/week', name: 'calendar-week' },
    { url: 'http://localhost:3001/schedule/day', name: 'calendar-day' },
    { url: 'http://localhost:3001/schedule/search', name: 'calendar-search' },
    { url: 'http://localhost:3001/board', name: 'board' },
    { url: 'http://localhost:3001/settings', name: 'settings' },
    { url: 'http://localhost:3001/notification', name: 'notification' }
];

async function captureScreenshots() {
    console.log('Starting screenshot capture...');
    console.log(`Screenshots will be saved to: ${screenshotsDir}\n`);

    const browser = await chromium.launch({
        headless: false,  // Show browser for debugging
        slowMo: 300       // Slow down for better reliability
    });

    const context = await browser.newContext({
        viewport: { width: 1920, height: 1080 }
    });
    const page = await context.newPage();

    // Login first
    try {
        console.log('Logging in...');
        await page.goto('http://localhost:3001/login', {
            waitUntil: 'domcontentloaded',
            timeout: 30000
        });

        await page.waitForTimeout(2000);

        // Fill login form with actual credentials from CLAUDE.md
        console.log('Filling login form...');
        await page.fill('#domain', 'sogang.ac.kr');
        await page.fill('#id', 'mailadm');
        await page.fill('#password', 'tims');

        console.log('Submitting login form...');
        await page.click('button[type="submit"]');

        // Wait for navigation after login
        await page.waitForURL('http://localhost:3001/mail/list/inbox', {
            timeout: 10000,
            waitUntil: 'domcontentloaded'
        });

        console.log('✓ Login successful\n');
        await page.waitForTimeout(2000);
    } catch (error) {
        console.error('Login failed:', error.message);
        console.log('Continuing anyway...\n');
    }

    // Capture regular pages (full desktop size)
    for (const pageInfo of pages) {
        try {
            console.log(`\nNavigating to ${pageInfo.url}...`);
            await page.goto(pageInfo.url, {
                waitUntil: 'domcontentloaded',
                timeout: 30000
            });

            console.log('Waiting for page to render...');
            await page.waitForTimeout(3000);

            const screenshotPath = join(screenshotsDir, `${pageInfo.name}.png`);
            console.log(`Taking screenshot...`);
            await page.screenshot({
                path: screenshotPath,
                fullPage: false
            });
            console.log(`✓ Saved: ${screenshotPath}`);
        } catch (error) {
            console.error(`✗ Failed to capture ${pageInfo.name}:`, error.message);
        }
    }

    // Capture popup pages by clicking actual UI elements
    console.log('\n\nCapturing popup windows...');

    // 1. Popup: Mail Read
    try {
        console.log('\nCapturing mail read popup...');
        await page.goto('http://localhost:3001/mail/list/inbox', {
            waitUntil: 'domcontentloaded',
            timeout: 30000
        });
        await page.waitForTimeout(2000);

        const [mailReadPopup] = await Promise.all([
            page.waitForEvent('popup'),
            page.click('button[title="팝업읽기"]') // ExternalLink icon button in table
        ]);

        await mailReadPopup.waitForLoadState('domcontentloaded');
        await mailReadPopup.waitForTimeout(2000);

        await mailReadPopup.screenshot({
            path: join(screenshotsDir, 'popup-read.png'),
            fullPage: false
        });
        console.log('✓ Saved: popup-read.png');
        await mailReadPopup.close();
    } catch (error) {
        console.error('✗ Failed to capture mail read popup:', error.message);
    }

    // 2. Popup: Mail Write
    try {
        console.log('\nCapturing mail write popup...');
        await page.goto('http://localhost:3001/mail/list/inbox', {
            waitUntil: 'domcontentloaded',
            timeout: 30000
        });
        await page.waitForTimeout(2000);

        const [mailWritePopup] = await Promise.all([
            page.waitForEvent('popup'),
            page.click('a[href="/popup/write"]') // Sidebar popup write link
        ]);

        await mailWritePopup.waitForLoadState('domcontentloaded');
        await mailWritePopup.waitForTimeout(2000);

        await mailWritePopup.screenshot({
            path: join(screenshotsDir, 'popup-write.png'),
            fullPage: false
        });
        console.log('✓ Saved: popup-write.png');
        await mailWritePopup.close();
    } catch (error) {
        console.error('✗ Failed to capture mail write popup:', error.message);
    }

    // 3. Popup: Address Book
    try {
        console.log('\nCapturing address book popup...');
        await page.goto('http://localhost:3001/mail/write', {
            waitUntil: 'domcontentloaded',
            timeout: 30000
        });
        await page.waitForTimeout(2000);

        const [addressPopup] = await Promise.all([
            page.waitForEvent('popup'),
            // Click address book button - it's next to  "Recent Recipients" dropdown in the TO field row
            page.locator('button', { hasText: '주소록' }).first().click()
        ]);

        await addressPopup.waitForLoadState('domcontentloaded');
        await addressPopup.waitForTimeout(2000);

        await addressPopup.screenshot({
            path: join(screenshotsDir, 'popup-address.png'),
            fullPage: false
        });
        console.log('✓ Saved: popup-address.png');
        await addressPopup.close();
    } catch (error) {
        console.error('✗ Failed to capture address book popup:', error.message);
    }

    await browser.close();
    console.log('\n\n✓ Screenshot capture complete!');
    console.log(`Check screenshots in: ${screenshotsDir}`);
}

captureScreenshots().catch(console.error);
