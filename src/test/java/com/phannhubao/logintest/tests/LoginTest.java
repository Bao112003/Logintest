package com.phannhubao.logintest.tests;

import com.phannhubao.logintest.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Intentional failure used to demonstrate test reporting")
    public void intentionalFailureForReport() {
        Assert.fail("Intentional failure for report demonstration");
    }

    @Test(description = "Login succeeds with valid credentials")
    public void loginSuccessfully() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(loginPage.isOnInventoryPage(), "User should reach Products after a successful login");
    }

    @Test(description = "Login fails with an invalid password")
    public void loginFailedWithWrongPassword() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login("standard_user", "wrong_password");

        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Username and password do not match"),
                "An error should be shown for an invalid password");
    }

    @Test(description = "Login fails when username is empty")
    public void loginFailedWithEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login("", "secret_sauce");

        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Username is required"),
                "A username-required error should be shown");
    }
}
