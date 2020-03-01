package com.gopro.webdriver;

import org.junit.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.Optional;

public class GoProMainPage {

    private static WebDriver driver;

    @BeforeClass
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");

    }


    @AfterClass
    public static void cleanUp() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }


    @ParameterizedTest
    @ValueSource(strings = {"United States", "Mexico"})
    public void test1(String country) throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://www.gopro.com");
        driver.findElement(By.xpath("//div[@class='gpn-locale-select-container gpn-user-nav-item']")).click();

        waitUntilVisible(driver, By.xpath("//div[@class='country-name']"), true);


        String xPathWithVariable = "//div[@class='country-name']//span[contains(text(),'" + country + "')]/../..//button[contains(@class,'country-language country-language-en')]";


        driver.findElement(By.xpath(xPathWithVariable)).click();


        waitUntilVisible(driver, By.xpath("//a[contains(@class,'gpn-menu-item-link')]//span[text()='Cameras']"), true);
        driver.findElement(By.xpath("//a[contains(@class,'gpn-menu-item-link')]//span[text()='Cameras']")).click();
        Thread.sleep(4000);

        waitUntilVisible(driver, By.xpath("//div[@class='wrapper']//button[@class='add-to-cart btn btn-medium btn-round']"), true);
        driver.findElement(By.xpath("//div[@class='wrapper']//button[@class='add-to-cart btn btn-medium btn-round']")).click();


        waitUntilVisible(driver, By.xpath("//div[@id='minicart-popup']"), true);

        driver.findElement(By.xpath("//span[@class='icon-exit-stroke']")).click();

        driver.findElement(By.xpath("//div[@class='wrapper']//button[@class='add-to-cart btn btn-medium btn-round']")).click();

        waitUntilVisible(driver, By.xpath("//a[@class='mini-cart-viewcart-cta btn-round']"), true);
        driver.findElement(By.xpath("//a[@class='mini-cart-viewcart-cta btn-round']")).click();
        waitUntilVisible(driver, By.xpath("//div[@class='fixed-cart-header']/h1"), true);

        waitUntilVisible(driver, By.xpath("(//tr[@class='cart-row']//td//div[@class='qty-input-group']//input[@type='number'])[1]"), true);
        String quantityValue = driver.findElement(By.xpath("(//tr[@class='cart-row']//td//div[@class='qty-input-group']//input[@type='number'])[1]")).getAttribute("value");
        Assert.assertTrue(quantityValue.equals("2"));

        Assert.assertTrue(driver.findElement(By.xpath("//img[@title='HERO8 Black']")).isDisplayed());

        Thread.sleep(2000);

        driver.findElement(By.xpath("(//tr[@class='cart-row']//td//div[@class='qty-input-group']//input[@class='button-minus'])[1]")).click();
        String secondQuantityValue = driver.findElement(By.xpath("(//tr[@class='cart-row']//td//div[@class='qty-input-group']//input[@type='number'])[1]")).getAttribute("value");
        Assert.assertTrue(secondQuantityValue.equals("1"));
        Assert.assertTrue(driver.findElement(By.xpath("//img[@title='HERO8 Black']")).isDisplayed());

        Thread.sleep(2000);

        driver.findElement(By.xpath("//img[@title='HERO8 Black']/../..//div[@class='item-user-actions']//button[@value='Remove']")).click();

        Thread.sleep(5000);

        try {
            driver.findElement(By.xpath("//img[@title='HERO8 Black']/../..//div[@class='item-user-actions']//button[@value='Remove']")).click();

        } catch (NoSuchElementException ex) {


        }

        driver.close();


    }

    public Optional<WebElement> waitUntilVisible(WebDriver driver, By locator, boolean visible) {
        if (visible) {
            return waitUntil(driver, ExpectedConditions.visibilityOfElementLocated(locator));
        } else {
            waitUntil(driver, ExpectedConditions.invisibilityOfElementLocated(locator));
            return Optional.empty();
        }
    }

    public <T> Optional<T> waitUntil(WebDriver driver, ExpectedCondition<T> expectedCondition) {
        WebDriverWait driverWait = new WebDriverWait(driver, 4);

        return Optional.of(driverWait.until(expectedCondition));
    }


}
