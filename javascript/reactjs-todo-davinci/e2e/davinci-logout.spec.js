import { test, expect } from '@playwright/test';

const BASE_URL = 'http://localhost:8443';

test.describe('React - DaVinci Logout', () => {
  test('Login then logout, pass', async ({ page }) => {
    // Login
    await page.goto(BASE_URL);
    await page.getByRole('link', { name: 'Sign In', exact: true }).click();
    await page.getByLabel('Username').fill('reactdavinci@user.com');
    await page.getByLabel('Password').fill('bae0fzc-mzg3krg5FQB');
    await page.getByRole('button', { name: 'Sign On' }).click();
    await expect(page.getByText('Welcome back, React Davinci!')).toBeVisible();
    await expect(page.getByText('Protect with Ping')).toBeVisible();

    // Logout
    await page.locator('#account_dropdown').click();
    await page.getByRole('link', { name: 'Sign Out' }).click();
    await page.waitForURL(BASE_URL + '/logout');
    await page.waitForURL(BASE_URL);
    await expect(page.getByText('Welcome back')).not.toBeVisible();
    await expect(page.getByText('Protect with Ping')).toBeVisible();
  });
});
