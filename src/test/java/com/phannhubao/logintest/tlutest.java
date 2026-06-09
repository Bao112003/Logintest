package com.phannhubao.logintest;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class tlutest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeClass
    public static void setup() {
        ChromeOptions options = new ChromeOptions();

        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterClass(alwaysRun = true)
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginAccount1() {
        assertLoginSucceeds(
                requiredEnvironmentVariable("2351067806"),
                requiredEnvironmentVariable("Bao112003@"));
    }

    @Test
    public void testLoginAccount2() {
        assertLoginSucceeds(
                requiredEnvironmentVariable("2351067806"),
                requiredEnvironmentVariable("0336040785"));
    }

    private void assertLoginSucceeds(String username, String password) {
        Assert.assertTrue(
                performLogin(username, password),
                "GitHub marks this test as failed because login was not successful");
    }

    private boolean performLogin(String username, String password) {
        driver.get("https://sinhvien1.tlu.edu.vn/#/login");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
        List<WebElement> inputs = driver.findElements(By.tagName("input"));

        WebElement usernameInput = null;
        WebElement passwordInput = null;

        for (WebElement input : inputs) {
            String type = input.getAttribute("type");
            String placeholder = input.getAttribute("placeholder");
            String name = input.getAttribute("name");

            if ("password".equals(type)) {
                passwordInput = input;
            } else if ("text".equals(type)
                    || containsIgnoreCase(placeholder, "ma")
                    || containsIgnoreCase(placeholder, "ten")
                    || containsIgnoreCase(name, "user")) {
                if (usernameInput == null) {
                    usernameInput = input;
                }
            }
        }

        if (usernameInput == null && !inputs.isEmpty()) {
            usernameInput = inputs.get(0);
        }
        if (passwordInput == null && inputs.size() > 1) {
            passwordInput = inputs.get(1);
        }

        if (usernameInput == null || passwordInput == null) {
            throw new IllegalStateException("Could not detect username/password inputs");
        }

        usernameInput.clear();
        usernameInput.sendKeys(username);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        passwordInput.sendKeys(Keys.ENTER);

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.not(ExpectedConditions.urlContains("/login")),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert, .error, [role='alert']"))));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // The final URL still determines whether login succeeded.
        }

        return !driver.getCurrentUrl().contains("/login");
    }

    private boolean containsIgnoreCase(String value, String expected) {
        return value != null && value.toLowerCase().contains(expected);
    }

    private String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
